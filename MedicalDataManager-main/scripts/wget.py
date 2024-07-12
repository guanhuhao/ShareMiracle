import requests as r
import fake_useragent

ua = fake_useragent.FakeUserAgent().getChrome

res = r.get(
    'https://med.emory.edu/departments/radiation-oncology/research-laboratories/deformable-image-registration/downloads-and-reference-data/4dct.html',
    headers={
        'User-Agent': ua['useragent']
    }
)

if res.status_code == 200:
    with open('4.html', 'w', encoding='utf-8') as fp:
        fp.write(res.text)