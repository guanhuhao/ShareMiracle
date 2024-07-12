import requests as r
import json
import os

api_key = '9J5qFTYr6wPRxkoVoXycnoWf'
secret_key = 'Xa6eJelStx5i7Ft3qQH0NAT6AvOkqhkH'

def get_access_token():
    headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }

    url = f'https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id={api_key}&client_secret={secret_key}'
    payload = json.dumps("")

    res = r.post(
        url=url,
        data=payload,
        headers=headers
    )

    resJson = res.json()
    access_token = resJson.get('access_token')
    assert isinstance(access_token, str), 'access_token 获取失败，详细信息' + str(resJson)
    return access_token

access_token = get_access_token()

def ask_llm(messages: list[dict]) -> str:
    """
    messsages can be like
    [
        {
            "role": "user",
            "content": "介绍一下初音未来"
        }
    ]
    """
    payload = json.dumps({
        "messages": messages
    })

    headers = {
        'Content-Type': 'application/json'
    }
    try:
        url = 'https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-lite-8k?access_token=' + access_token
        res = r.post(url, headers=headers, data=payload)
        resJson = res.json()
        return resJson.get('result')
    except Exception as e:
        print(e)
        return ''


if __name__ == '__main__':
    answer = ask_llm([
        {
            "role": "user",
            "content": "介绍一下初音未来"
        }
    ])
    print(answer)