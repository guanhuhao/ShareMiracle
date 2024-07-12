import requests
import json
import uuid
import os

api_key = '9J5qFTYr6wPRxkoVoXycnoWf'
secret_key = 'Xa6eJelStx5i7Ft3qQH0NAT6AvOkqhkH'

# 获取token
def get_access_token():
    url = 'https://localhost:8080/user/login'
    headers = {'Content-Type': 'application/x-www-form-urlencoded'}
    data = {'client_id': api_key, 'client_secret': secret_key}
    response = requests.post(url, headers=headers, data=data)
    if response.status_code == 200:
        token_data = response.json()
        return token_data['access_token']
    else:
        raise Exception('Failed to get access token')

access_token = get_access_token()

# 定义基础URL
base_url = "http://localhost:8080/dataset"

# 生成一个随机的测试数据集ID
test_dataset_id = str(uuid.uuid4())

# POST请求函数 - 添加数据集
def post_add_dataset():
    url = base_url + "/add"
    headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + access_token}
    data_set_add = {
    "id": 555666,
    "name": "测试数据集",
    "isPublic": 1,
    "datasetUrl": "www.baidu.com",
    "shareOrganization": [
        {"id": 11111},
        {"id": 22222},
        {"id": 33333},
        {"id": 44444},
        {"id": 55555}
    ]
}
    response = requests.post(url, data=json.dumps(data_set_add), headers=headers)
    return response

# DELETE请求函数 - 删除数据集
def delete_dataset():
    url = base_url + "/delete"
    headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + access_token}
    data = {"id": 555666}
    response = requests.delete(url, data=json.dumps(data), headers=headers)
    return response

# DELETE请求函数 - 批量删除数据集
def delete_batch_datasets():
    url = base_url + "/delete-batch"
    headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + access_token}
    # 假设这里是批量删除的参数
    data = {}  # 需要填充具体的批量删除参数
    response = requests.delete(url, data=json.dumps(data), headers=headers)
    return response

# PUT请求函数 - 更新数据集
def put_update_dataset():
    url = base_url + "/update"
    headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + access_token}
    data = {
        "id": 1,
        "name": "测试数据集",
        "datasetUrl": "www.baidu.com"
    }
    response = requests.put(url, data=json.dumps(data), headers=headers)
    return response

# PUT请求函数 - 更新数据集状态
def put_change_dataset_status():
    url = base_url + "/status"
    headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + access_token}
    data = {
        "id": 12345679,
        "isPublic": 1
    }
    response = requests.put(url, data=json.dumps(data), headers=headers)
    return response

# PUT请求函数 - 更新数据集组织
def put_change_dataset_organ():
    url = base_url + "/organ"
    headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + access_token}
    data = {
        "datasetId": 1,
        "ids": [11111, 22222, 33333, 44444, 55555]
    }
    response = requests.put(url, data=json.dumps(data), headers=headers)
    return response

# GET请求函数 - 查询数据集
def get_query_dataset():
    url = base_url + "/query-by-id"
    headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + access_token}
    # GET请求通常使用params参数
    params = {"id": 12345678}
    response = requests.get(url, params=params, headers=headers)
    return response

# 主函数，用于执行上述所有请求
def main():
    # 执行POST请求
    print(post_add_dataset().text)
    
    # 执行DELETE请求
    print(delete_dataset().text)
    
    # 执行DELETE批量删除请求
    print(delete_batch_datasets().text)
    
    # 执行PUT更新请求
    print(put_update_dataset().text)
    
    # 执行PUT状态更新请求
    print(put_change_dataset_status().text)
    
    # 执行PUT组织更新请求
    print(put_change_dataset_organ().text)
    
    # 执行GET查询请求
    print(get_query_dataset().text)

    # 清理测试数据
    teardown()

# 清理测试数据的函数
def teardown():
    # 尝试删除测试数据集
    delete_dataset(test_dataset_id)

# DELETE请求函数 - 删除数据集
def delete_dataset(dataset_id):
    url = base_url + "/delete"
    headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + access_token}
    data = {"id": dataset_id}
    response = requests.delete(url, data=json.dumps(data), headers=headers)
    return response

# 如果直接运行此脚本，则执行main函数
if __name__ == "__main__":
    main()