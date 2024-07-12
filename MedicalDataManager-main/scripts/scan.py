import json

with open('./data/storage.json', 'r', encoding='utf-8') as fp:
    storage = json.load(fp)

storage_path = storage['path']
