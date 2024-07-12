# -*- coding: utf-8 -*-
import requests
import yaml


def getToken():
    new_value = "token"
    # 读取YAML-User
    with open('../config/User.yml', 'r') as file:
        config = yaml.safe_load(file)
    # 替换token
    config['token'] = new_value
    # 写回YAML-User
    with open('../config/User.yml', 'w') as file:
        yaml.safe_dump(config, file)


if __name__ == '__main__':
    getToken()
