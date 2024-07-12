import requests as r
import json

server = 'http://localhost:8080'

def update_data(data_json: dict):
    payload = json.dumps(data_json)
    headers = {
        'Content-Type': 'application/json'
    }
    res = r.put(server + '/mdata/add-meta-info', data=payload, headers=headers)
    
    if res.status_code == 200:
        print(res.json())

if __name__ == '__main__':
    storage = json.load(open('../../data/storage.json', 'r'))
    ids = []
    for item in storage['storage']:
        update_data(item)
        ids.append(item['id'])
    
    print(len(set(ids)))