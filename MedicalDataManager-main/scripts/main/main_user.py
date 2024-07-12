import glob
import os


def main():
    scripts_dir = '../../scripts/user-test'

    # 确保目录存在
    if not os.path.exists(scripts_dir):
        print(f"The directory {scripts_dir} does not exist.")
    else:
        # 获取目录中的所有.py文件
        for filename in glob.glob(os.path.join(scripts_dir, '*.py')):
            print(f"Running {filename}")
            # 使用exec函数运行每个脚本
            exec(open(filename).read())


if __name__ == '__main__':
    main()
