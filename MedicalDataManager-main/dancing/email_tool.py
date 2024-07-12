import os
from types import TracebackType
from typing import List
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from email.mime.image import MIMEImage

class SmtpEmailSender():
    def __init__(self, host: str, port: int, account: str=None, password: str=None, nickname: str=None) -> None:
        if account is None:
            account = os.environ['EMAIL_USER']
        if password is None:
            password = os.environ['EMAIL_PASSWORD']

        self.host = host
        self.port = port
        self.user = account
        self.password = password
        self.nickname = nickname if nickname is not None else account

        try:
            self.smtp_client = smtplib.SMTP()
            self.smtp_client.connect(host, port)
            self.smtp_client.login(account, password)
        except Exception as e:
            print('邮件初始化错误，原因如下：')
            print(e)
            exit()

    def send(self, receivers: str | List[str], title: str, content: str | None = None, content_type: str ='plain', attechments: List[MIMEText] | None = None):
        if isinstance(receivers, str):
            receivers: List[str] = [receivers]
        message_string = self.resolve_message_string(receivers, title, content, content_type, attechments)
        self.smtp_client.sendmail(self.user, receivers, message_string)

    def resolve_message_string(self, receivers: List[str], title: str, content: str | None = None, content_type: str ='plain', attechments: List[MIMEText] | None = None) -> str:
        assert len(receivers) > 0 and isinstance(receivers, list)

        mime_message = MIMEMultipart()
        mime_message['From'] = self.nickname
        mime_message['To'] = ';'.join(receivers)
        mime_message['Subject'] = title

        if content:
            content_message = MIMEText(content, content_type, 'utf-8')
            mime_message.attach(content_message)
        if attechments and isinstance(attechments, list) and len(attechments) > 0:
            for attechment in attechments:
                mime_message.attach(attechment)

        return mime_message.as_string()

    def make_image_attachment(self, image_path: str) -> MIMEText:
        assert os.path.exists(image_path), f'{image_path} 图像文件不存在'

        image_name = os.path.basename(image_path)
        with open(image_path, 'rb') as fp:
            mime = MIMEImage(fp.read())
            mime['Content-Type'] = 'application/octet-stream'
            mime['Content-Disposition'] = f'attachment;filename="{image_name}"'
        return mime


    def __exit__(self, exc_type: type[BaseException] | None, exc_value: BaseException | None, tb: TracebackType | None) -> None:
        super().__exit__(exc_type, exc_value, tb)
        self.close()

if __name__ == '__main__':
    email = SmtpEmailSender(
        host='email.ustc.edu.cn',
        port=25,
        account=os.environ['EMAIL_USER'],
        password=os.environ['EMAIL_PASSWORD']
    )
    email.send(
        receivers='1193466151@qq.com',
        title='试试新的outlook邮箱',
        content='新的邮箱看着不错呢！邮箱测试'
    )
