# -*- coding: utf-8 -*-
import requests
import yaml


def getToken():
    config = yaml.load(open('../config/User.yml', 'r'), Loader=yaml.Loader)

    host = config.get('host')
    port = config.get('port')
    api = config.get('api')
    # token = config.get('token')

    # 认证服务的登录URL
    login_url = f'http://{host}:{port}/{api}/login'
    # FIXME:换成项目服务器部署地址！！
    login_url = f'http://172.16.120.234:8081/login'

    # 测试用户
    credentials = {
        'username': 'test_user',
        'password': 'test_password'
    }

    # 发送POST请求进行登录
    response = requests.post(login_url, json=credentials)

    # 检查响应状态码，确保请求成功
    if response.status_code == 200:
        # 解析响应体中的JSON数据
        auth_response = response.json()
        # 提取token
        token = auth_response.get('token')
        if token:
            print('Login successful, token obtained:', token)
            # TODO:替换所有yaml文件的token值
            # 读取YAML-User
            with open('../config/User.yml', 'r') as file:
                config = yaml.safe_load(file)
            # 替换token
            config['token'] = token
            # 写回YAML-User
            with open('../config/User.yml', 'w') as file:
                yaml.safe_dump(config, file)
        else:
            print('Login successful, but token not found in response')
    else:
        print('Login failed with status code:', response.status_code)
        print('Response:', response.text)
        raise ValueError

    return token


if __name__ == '__main__':
    getToken()