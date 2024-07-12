from elasticsearch import Elasticsearch
import yaml
import time

config = yaml.load(open('../../config/ElasticSearch.yml', 'r'), Loader=yaml.Loader)

ES_HOST = config['host']
ES_PORT = config['port']
USERNAME = config['username']
PASSWORD = config['password']

es = Elasticsearch(  
    [{'host': ES_HOST, 'port': ES_PORT, 'scheme': 'http'}],
    basic_auth=(str(USERNAME), str(PASSWORD))
)  

if es.ping():  
    print('Yes, I can connect to Elasticsearch!')  
else:  
    print('No, I cannot connect to Elasticsearch!')

import json
storage = json.load(open('../../data/storage.json', 'r')).get('storage', [])
print(len(storage))

index_name = "mdata-background-management"



for data in storage:
    now_ms = int(time.time() * 1000)
    body = {
        'id': int(data['id']),
        'name': data['name'],
        'status': 0,
        'createTS': now_ms,
        'modifyTS': now_ms
    }
    
    es.index(
        index=index_name,
        id=str(id), # es 中不存在主键的概念，但是有着和图数据库一样的 数据项 id（字符串），可以用这个做主键
        body=body
    )