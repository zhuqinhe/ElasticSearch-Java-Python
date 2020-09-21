# -*- coding: utf-8 -*-
# !/usr/bin/env python
# author:zhuqinhe
# datetime:2020/09/21 0025 17:40 下午5:40
import sys
import os.path
from importlib import reload

reload(sys)
# sys.setdefaultencoding('utf8')
from api.search_server_sync_handler import *


# 创建进程
def create_daemon():
    # fork进程
    try:
        if os.fork() > 0:
            sys.exit(0)
    except OSError as error:
        print('fork #1 failed: %d (%s)' % (error.errno, error.strerror))
        sys.exit(1)
    os.chdir('/')
    os.setsid()
    os.umask(0)

    try:
        pid = os.fork()
        if pid > 0:
            pid_file = open('/var/run/search_sync_data.pid', 'w')
            pid_file.write(str(pid))
            pid_file.close()
            print("run successfully!")
            sys.exit(0)
    except OSError as error:
        print('fork #2 failed: %d (%s)' % (error.errno, error.strerror))
        sys.exit(1)
    # 重定向标准IO
    sys.stdout.flush()
    sys.stderr.flush()
    si = open("/dev/null", 'r')
    so = open("/dev/null", 'a+')
    se = open("/dev/null", 'a+', 0)
    os.dup2(si.fileno(), sys.stdin.fileno())
    os.dup2(so.fileno(), sys.stdout.fileno())
    os.dup2(se.fileno(), sys.stderr.fileno())


if __name__ == '__main__':
    # windows 不适用
    # create_daemon()  # 守护进程
    sync_data()
