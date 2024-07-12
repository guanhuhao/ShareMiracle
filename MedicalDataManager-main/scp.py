import pexpect
import os
import sys

names = ['refcoco', '76_HaN-Seg', '63_WORD_coco', '6006_CHAOS', '6007_WORD', '6012_MSDColon', '68_kipa22', '78_LUNA16', '67_BTCV', 'all_datasets', '79_deep_lesion', '6016_Sliver07', '72_FLARE22Train', '52_BCV-Cervix', '57_StructSeg2019', 'labelsTrprocess', '6013_MSDLiver', '602_Dataset016Pancreas', '.ipynb_checkpoints', '55_FLARE22Train', '6005_3D-IRCADb', 'verse2019', '53_NIHPancrease', '65_kits19', '6015_FLARE2021', 'labelsTrv2', '6011_MSDSpleen', '58_CHAOST2', '10_Decathlon', '6008_MSDLung', 'cached_dataset2', '69_kits21', '6017_verse2020', 'labelv2', '56_Head-and-Neck-challenge', '6014_CT-ORG', '6018_COVID-19', 'example_cache', '66_kits23', '600_LITS2017', '75_3D-IRCADb', '601_Dataset023KiTS2023', '74_SLIVER07', 'nnUNet_raw', '59_SABS', '6002_AMOS', '6003_TotalSegmentator', '6001_AbdomenCT-1K', 'CHAOST2', '6009_MSDHepaticVessel', 'cifar', '70_Pancreas-CT', 'labelsTrTrprocess2', '60_Totalseg', 'Dataset033_LITS2017', '6004_Pancreas-CT', '54_CTPelvic1K', '51_ASOCA', 'labelsTrprocess1']

password = 'rf%^xFQYH0$0kik5j$LG2gBFMu&mOPnj'

def scp_transfer(data_name, password):
    scp_command = f'scp -P 30634 -r root@172.16.120.7:/quanquan/datasets/{data_name} {data_name}'
    child = pexpect.spawn(scp_command, logfile=sys.stdout.buffer, timeout=6000)
    child.expect('password:')
    child.sendline(password)
    child.expect(pexpect.EOF)

exist_names = set(os.listdir('.'))

for name in names:
    if name in exist_names:
        print(f'{name} exists, skip')
    else:
        print('start transfer', name)
        try:
            scp_transfer(name, password)
        except Exception as e:
            open('log.txt', 'a', encoding='utf-8').write('error happens when transfer ' + name + '\n')
