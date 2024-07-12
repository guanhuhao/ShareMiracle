from urllib.parse import urlparse, parse_qs
from typing import Dict, Union, Tuple, List
import os
import time
from tqdm import tqdm

import wget
import requests
import fake_useragent
from bs4 import BeautifulSoup
from loguru import logger

logger.remove(handler_id=None)
logger.add(
    sink='./logs/download_datasets.log',
    level='INFO',
    rotation='00:00',
    retention='14 days',
    compression='zip',
    encoding='utf-8',
    enqueue=True,
    format="{time:YYYY-MM-DD HH:mm:ss} | {level} | {message}"
)


_tqdm_progress_instance = None
_fake_useragent = fake_useragent.UserAgent(browsers=['chrome'])

_default_progress_kwargs = {
    'desc': 'Downloading',
    'unit': ' bytes',
    'unit_scale': 1,
    'colour': 'green'
}

_default_proxies = {  
    'http': 'http://localhost:7890',  
    'https': 'http://localhost:7890',  
}

_file_size_cache = {}

def _make_tqdm_progress(total: float, display_progress: bool, progress_kwargs: dict):
    if not display_progress:
        return
    global _tqdm_progress_instance
    _tqdm_progress_instance = tqdm(total=total, **progress_kwargs)


def _update_tqdm_progress(n: float, display_progress: bool):
    if not display_progress:
        return
    if isinstance(_tqdm_progress_instance, tqdm):
        _tqdm_progress_instance.update(n)


def _close_tqdm_progress(display_progress: bool):
    if not display_progress:
        return
    if isinstance(_tqdm_progress_instance, tqdm):
        _tqdm_progress_instance.close()


def get_actual_filesize_from_url(url):
    if url in _file_size_cache:
        return _file_size_cache[url]
    # 先使用 head， 如果没有结果用流式 GET
    response = requests.head(url)
    filesize = get_filesize_from_response(response)
    if filesize == 0:
        logger.info('HEAD is not available in {}, attempt to use stream GET instead'.format(url))
        response = make_download_response(url, False, 0)
        filesize = get_filesize_from_response(response)

    if filesize > 0:
        logger.info('{} 文件大小为 {} 字节'.format(url, filesize))
        _file_size_cache[url] = filesize

    return filesize

def make_download_response(url: str, resume: bool, already_download_bytes: int) -> requests.Response:
    headers = {
        'User-Agent': _fake_useragent.random
    }

    if resume:
        headers['Range'] = 'bytes={}-'.format(already_download_bytes)
    
    response = requests.get(url, stream=True, headers=headers)
    return response

def get_filesize_from_response(response: requests.Response) -> int:
    return int(response.headers.get('content-length', 0))

def get_io_mode(resume: bool, already_download_bytes: int) -> str:
    if already_download_bytes == 0 or not resume:
        return 'wb'
    else:
        return 'ab'
    
def write_chunk(chunk, save_path: str):
    with open(save_path, 'ab') as fp:
        fp.write(chunk)

def download_file(url: str, save_name: Union[str, None] = None, save_dir: Union[str, None] = None, resume: bool=True, display_progress: bool = True, progress_kwargs: dict={}) -> dict:
    if save_name is None:
        save_name = wget.filename_from_url(url)
    if save_dir is None:
        save_dir = '.'
    if not os.path.exists(save_dir):
        os.makedirs(save_dir)

    # make save path
    abs_save_dir = os.path.abspath(save_dir)
    if not isinstance(save_name, str):
        save_name = 'unknown_file'
    save_path = os.path.join(abs_save_dir, save_name)
    already_download_bytes = 0
    if os.path.exists(save_path):
        already_download_bytes = os.path.getsize(save_path)
    
    for default_key, default_value in _default_progress_kwargs.items():
        if default_key not in progress_kwargs:
            progress_kwargs.__setitem__(default_key, default_value)

    # 发送请求
    response = make_download_response(url, resume, already_download_bytes)
    
    if resume:
        total_bytes = get_actual_filesize_from_url(url)
    else:
        total_bytes = get_filesize_from_response(response)

    # 如果下载的字节数和服务器返回的大小一致，说明下载完成
    if total_bytes == already_download_bytes:
        return {
            'save_path': save_path,
            'total_bytes': total_bytes,
            'cost_time': '复用已有的数据'
        }

    
    support_resume = response.status_code == 206
    
    # 制作内层进度条
    _make_tqdm_progress(total_bytes, display_progress, progress_kwargs)
    
    # 如果要求 resume 但是服务器不支持，重新发送一个普通的下载请求
    if resume and not support_resume:
        resume = False
        already_download_bytes = 0
        response = make_download_response(url, resume, already_download_bytes)
    # 否则 更新进度条信息为“继续下载”并更新进度条进度初始值
    else:
        _tqdm_progress_instance.set_description_str('Continue Downloading')
        _update_tqdm_progress(already_download_bytes, display_progress)
    
    _tqdm_progress_instance.set_postfix({'file': save_name, 'resumable': str(resume)})

    st = time.time()

    for chunk in response.iter_content(chunk_size=1024):
        write_chunk(chunk, save_path)
        _update_tqdm_progress(len(chunk), display_progress)
    
    _close_tqdm_progress(display_progress)
    logger.info('下载文件保存至：' + save_path)
    et = time.time()
    cost_time = round(et - st, 4)

    return {
        'save_path': save_path,
        'total_bytes': total_bytes,
        'cost_time': cost_time
    }


def download_file_from_google_drive(url: str, save_dir=None) -> int:
    file_id = solve_file_id_from_url(url)
    filename, token = parse_confirm(file_id)
    logger.info(f'Google Driver 解析成功, 文件名: {filename}, 令牌: {token}')    
    download_url = f'https://docs.google.com/uc?export=download&confirm={token}&id={file_id}'
    logger.info(f'{filename} 的真实下载链接: {download_url}')
    return download_file(
        url=download_url,
        save_dir=save_dir,
        save_name=filename,
        resume=True
    )



def solve_file_id_from_url(url: str) -> str:
    """
    获取 google 网盘的文件 ID

    接受的链接为文件的 share link

    返回 file_id
    """
    url_path = urlparse(url).path
    components = url_path.split('/')
    id_index = 0
    while id_index < len(components):
        current_name = components[id_index]
        id_index += 1
        if current_name == 'd':
            break

    file_id = components[id_index]
    return file_id


def parse_confirm(file_id: str) -> Tuple[str, str]:
    """
    从 验证界面 获取验证令牌，比如 字母 t，顺便分析得到文件名
    
    返回 ( filename, token )    
    """
    confirm_url = f'https://docs.google.com/uc?export=download&id={file_id}'

    headers = { 'User-Agent': _fake_useragent.random }
    response = requests.get(confirm_url, headers=headers)
    verify_html = response.content
    
    soup = BeautifulSoup(verify_html, 'html.parser')
    uc_text_el = soup.find('div', attrs={'id': 'uc-text'})
    filename = uc_text_el.find('a').text
    # 这其实是一个 url
    # https://docs.google.com/uc?export=download&id=12WMDWqagP5SXleO_NGB83PlzIsf4zvBc&confirm=t&uuid=9a0f8b14-7934-4c1f-a663-451b426728bd
    # 需要提取 confirm 的值，上面中就是 t
    verify_action = uc_text_el.find('form').attrs['action']
    token = parse_qs(verify_action)['confirm'][0]
    
    return filename, token

# TODO: do FTP download
def ftp_download(recurse: bool = True):
    pass


if __name__ == '__main__':
    url = 'https://kirigaya.cn/files/pdfs/endnote的基本用法.pdf'
    try:
        download_file(url, resume=True)
    except KeyboardInterrupt:
        pass
