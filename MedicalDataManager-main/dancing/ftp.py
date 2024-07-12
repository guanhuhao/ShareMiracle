from typing import *
import os
import time

import ftplib
from loguru import logger
from email_tool import SmtpEmailSender

email = SmtpEmailSender(
    host='email.ustc.edu.cn',
    port=25,
    account=os.environ['EMAIL_USER'],
    password=os.environ['EMAIL_PASSWORD']
)

logger.add(
    sink='logs/DDSM.log', 
    format="{time:YYYY-MM-DD HH:mm:ss} | {level} | {message}", 
    level="INFO",
    rotation="1MB"
)

ftp = ftplib.FTP(
    host='figment.csee.usf.edu',
    user='anonymous'
)

def walk_dir(ftp: ftplib.FTP, dirname: str) -> Generator[str, None, None]:
    ftp.cwd(dirname)

    callback_result: List[Tuple[str]] = []
    def callback(*lines) -> None:
        print_infos = lines[0].split()
        auth = print_infos[0]
        name = print_infos[-1]
        callback_result.append((auth, name))
    
    ftp.dir(callback)

    for auth, name in callback_result:
        item_path = dirname + '/' + name
        # 这是一个文件夹
        if auth.startswith('d'):
            for sub_item_path in walk_dir(ftp, item_path):
                yield sub_item_path
        else:
            yield item_path


counts = {
    'success': 0,
    'fail': 0
}

fail_cases = []

target_ftp_path = '/pub/DDSM/cases/benigns/'
save_root = '/data/multimodal-medical-database/storage'

start_st = time.time()
for path in walk_dir(ftp, target_ftp_path):
    save_path = path.replace('/pub', save_root)
    save_path = save_path.replace('//', '/')
    save_dirname = os.path.dirname(save_path)
    if not os.path.exists(save_dirname):
        os.makedirs(save_dirname)
    ftp_path = 'ftp://' + (ftp.host + path).replace('//', '/')
    if os.path.exists(save_path):
        logger.info('{} is already donwloaded, skip'.format(save_path))
        counts['success'] += 1
        continue
    try:
        fp = open(save_path, 'wb')
        logger.info('download {} to {}'.format(ftp_path, save_path))
        ftp.retrbinary('RETR ' + path, fp.write, blocksize=1024)
        fp.close()
        counts['success'] += 1
    except Exception as e:
        logger.exception('exception happens when downloading ' + ftp_path)
        email.send(
            receivers='1193466151@qq.com',
            title='[miracle 数据服务器] FTP 下载进程出错',
            content=f'我在下载 {ftp_path} 时出错，错误原因：\n{e.__str__()}'
        )
        counts['fail'] += 1
        fail_cases.append(ftp_path)

end_st = time.time()

send_content = [
    f'对于 {target_ftp_path} 下所有文件下载完成',
    f'耗时 {round(end_st - start_st, 4)} s',
    f'成功 {counts["success"]}',
    f'失败 {counts["fail"]}',
]

if len(fail_cases) > 0:
    send_content.append(f'失败的 FTP 下载链接: {fail_cases}')

send_content = '\n'.join(send_content)

email.send(
    receivers='1193466151@qq.com',
    title='[miracle 数据服务器] FTP 下载进程结束',
    content=send_content
)
