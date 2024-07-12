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

index_name = "mdata-background-management"

query = {
    'query': {
        'match_all': {}
    },
    'size': 1000
}

search_response = es.search(index=index_name, body=query)
docs = []
for hit in search_response['hits']['hits']:
    document: dict = hit['_source']
    docs.append(document)    

print(len(docs))