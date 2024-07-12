import requests
import yaml
import json
import logging

logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(levelname)s - %(message)s', filename='app.log')

config = yaml.load(open('../../config/Organ.yml', 'r'), Loader=yaml.Loader)

host = config.get('host')
port = config.get('port')
api = config.get('api')
token = config.get('token') 

url = f'http://{host}:{port}/{api}/delete/user'

headers = {
    'Content-Type': 'application/json',
    'Authorization': f'Bearer {token}'
}

success_count = 0
failure_count = 0

try:
    with open('../../data/test_organ/delete-user.json', 'r', encoding='utf-8') as file:
        data = json.load(file)

    logging.info(f'Sending request to {url} with data: {data}')

    response = requests.post(url, json=data, headers=headers)

    if response.status_code == 200:
        logging.info('Request successful!')
        print('Request successful!')
        print(response.json())
        success_count += 1
    else:
        logging.error(f'Request failed with status code: {response.status_code}')
        logging.error(f'Response text: {response.text}')
        print('Request failed with status code:', response.status_code)
        print(response.text)
        failure_count += 1
        logging.error(f'Failing API endpoint: {url}')

except requests.exceptions.RequestException as e:
    logging.error(f'Request failed: {e}')
    failure_count += 1
    logging.error(f'Failing API endpoint: {url}')

finally:
    logging.info(f'Total successful test points: {success_count}')
    logging.info(f'Total failed test points: {failure_count}')
    if failure_count > 0:
        logging.warning('Some test points failed. Check the logs for details.')
    else:
        logging.info('All test points passed successfully.')
