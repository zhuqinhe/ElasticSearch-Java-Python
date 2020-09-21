# -*- coding: utf-8 -*-
# !/usr/bin/env python
# author:zhuqinhe
# datetime:2020/09/21 0025 17:40 下午5:40
import os
import sys
import time
import logging
import threading
import traceback
from logging import handlers
from utils import config
from utils.logsuite import logsuite_conf


def deleteLog(type):
    """删除创建时间最早的日志
        参数：
            param1:  日志类型('biz'为特殊日志，其它默认为普通日志)
        返回值：
            0：删除成功
            -1：删除失败
        异常抛出：
            OSError: 文件删除异常
    """

    createtime = time.time()
    firstfile = ''

    # 获取文件列表
    if type == 'biz':
        filepath = logsuite_conf.g_conf.biz_log_path
        filelist = os.listdir(logsuite_conf.g_conf.biz_log_path)
        filename = logsuite_conf.g_conf.biz_filename
    else:
        filepath = logsuite_conf.g_conf.nor_log_path
        filelist = os.listdir(logsuite_conf.g_conf.nor_log_path)
        filename = logsuite_conf.g_conf.nor_filename

    # 查找最早创建的日志文件
    for str in filelist:
        if filename in str:
            statinfo = os.stat(filepath + os.sep + str)
            if createtime > statinfo.st_ctime:
                createtime = statinfo.st_ctime
                firstfile = filepath + os.sep + str

    # 删除最早创建的日志文件
    try:
        os.remove(firstfile)
    except:
        logging.error(traceback.format_exc())
        return -1
    return 0


def getLogFileNum(type):
    """获取日志文件序号和文件个数

    参数：
        param1:  日志类型('biz'为特殊日志，其它默认为普通日志)
    返回值：
        fileno(日志文件最大序号)和filecount(日志文件个数)"""

    fileno = 0
    filecount = 0

    # 获取文件列表
    if type == 'biz':  # 特殊等级日志
        filelist = os.listdir(logsuite_conf.g_conf.biz_log_path)
        filename = logsuite_conf.g_conf.biz_filename
    else:  # 普通等级日志
        filelist = os.listdir(logsuite_conf.g_conf.nor_log_path)
        filename = logsuite_conf.g_conf.nor_filename

    ilen = len(filename) + 1  # 文件名长度

    # 获取日志文件个数和非日期保存模式下日志文件最大序号数
    if logsuite_conf.g_conf.day_mode != 1:
        for str in filelist:
            if filename in str:
                filecount += 1
                num = str[ilen:]
                if num.isdigit():
                    if int(num) > fileno:
                        fileno = int(num)
    else:
        for str in filelist:
            if filename in str:
                filecount += 1

    return fileno, filecount


def mkLogDir():
    """	初始化创建日志目录
        异常抛出：
        OSError：目录创建失败
    """

    # 初始化创建普通日志存放目录
    try:
        if not os.path.isdir(logsuite_conf.g_conf.nor_log_path):
            os.makedirs(logsuite_conf.g_conf.nor_log_path)

            # 初始化创建特殊日志存放目录
        if not os.path.isdir(logsuite_conf.g_conf.biz_log_path):
            os.makedirs(logsuite_conf.g_conf.biz_log_path)
    except:
        logging.error(traceback.format_exc())
        return -1

    return 0


def log_backup_judge(type, file, bufsize):
    """判断日志文件个数，超过配置文件中设置个数则删除创建时间最早的日志文件
       判断日志文件大小，超过配置文件中设置大小则进行备份

    参数：
        param1:  日志类型('biz'为特殊日志，其它默认为普通日志)
        param2:  日志文件名(文件为全路径)
        param3:  需要记录日志内容字节长度
    返回值：
        0：成功
        -1：删除最早的日志文件失败
        -2：文件备份失败
    异常抛出："""

    TIMEFORMAT = '%Y-%m-%d %X'

    # 初始化日志目录
    if mkLogDir() != 0:
        return -1

    # 判断日志文件是否存在
    try:
        if not os.path.exists(file):
            os.mknod(file)
            return 0
        if logsuite_conf.g_conf.day_mode != 3:

            # 获取文件大小
            filesize = os.path.getsize(file)
            # 判断文件大小，超过配置文件设置大小则进行备份

            fileMaxSize = logsuite_conf.g_conf.max_size * 1024 * 1024
            if fileMaxSize < filesize + bufsize:
                filenum = getLogFileNum(type)

                # 判断文件个数是否已经达到配置文件中设置的个数
                if filenum[1] >= logsuite_conf.g_conf.keep_count:
                    if deleteLog(type) != 0:
                        return -1
                        # 日志备份
                if logsuite_conf.g_conf.day_mode == 1:  # 按日期存储
                    timestr = time.strftime(TIMEFORMAT, time.localtime())
                    backupfile = file + '_' + timestr[0:10]
                    os.rename(file, backupfile)
                    os.mknod(file)
                elif logsuite_conf.g_conf.day_mode == 0:  # 按序号存储
                    backupfile = file + '_' + str(filenum[0] + 1)
                    os.rename(file, backupfile)
                    os.mknod(file)
    except:
        logging.error(traceback.format_exc())
        print -2

    return 0


class updateThread(threading.Thread):
    def __init__(self, conf_file, updateTime):
        threading.Thread.__init__(self)
        self.updateTime = updateTime
        self.conf_file = conf_file
        self.setDaemon(True)

    def run(self):
        while True:
            # 加载配置文件
            logsuite_conf.init_LogSuite_conf(self.conf_file)

            time.sleep(self.updateTime)


class runlogapi(object):
    """运行日志调用api接口"""

    def loginit(self, log_conf_path, updatetime):
        """初始化配置文件，并派生线程定时扫描配置文件"""
        logsuite_conf.init_LogSuite_conf(log_conf_path)
        logthread = updateThread(log_conf_path, updatetime)
        logthread.start()

        return logthread

    def debug(self, message, executeTime=-1):
        """运行日志debug级别调用api
            参数:
                param1:  日志内容
                param2:  业务执行耗时(单位ms)，运行日志可默认为-1
            返回值:
                0：成功
                500：文件备份失败
        """
        TIMEFORMAT = '%Y-%m-%d %X'

        filename = logsuite_conf.g_conf.nor_log_path + os.sep + logsuite_conf.g_conf.nor_filename
        if log_backup_judge('nor', filename, len(message)) != 0:
            return 500

        # 日志打印
        if logsuite_conf.g_conf.level == 'DEBUG':
            printlevel = logging.DEBUG
        elif logsuite_conf.g_conf.level == 'INFO':
            printlevel = logging.INFO
        elif logsuite_conf.g_conf.level == 'WARN':
            printlevel = logging.WARNING
        elif logsuite_conf.g_conf.level == 'ERROR':
            printlevel = logging.ERROR
        else:
            printlevel = logging.CRITICAL

        # 设置日志打印等级、输出格式、日志文件写入文件名
        filestr = logsuite_conf.g_conf.nor_log_path + os.path.sep + logsuite_conf.g_conf.nor_filename
        logging.basicConfig(level=printlevel,
                            format='[%(asctime)s][%(levelname)s]%(message)s',
                            datefmt=TIMEFORMAT,
                            filename=filestr,
                            filemode='a')

        console = logging.StreamHandler()
        formatter = logging.Formatter('[%(asctime)s][%(levelname)s]%(message)s')
        console.setFormatter(formatter)
        console_logger = logging.getLogger('')
        console_logger.addHandler(console)
        console_logger.setLevel(printlevel)

        filelog = logging.FileHandler(filestr)
        filelog.setFormatter(formatter)
        logger = logging.getLogger('')
        logger.addHandler(filelog)
        logger.setLevel(printlevel)

        lineno = sys._getframe().f_back.f_lineno
        funcname = sys._getframe().f_back.f_code.co_name
        filename = os.path.basename(sys._getframe().f_back.f_code.co_filename)
        if executeTime > -1:
            logstr = '[%s][%s][line:%d][executeTime:%d]%s' % (filename, funcname, lineno, executeTime, message)
        else:
            logstr = '[%s][%s][line:%d]%s' % (filename, funcname, lineno, message)

        logging.debug(logstr)
        logger.removeHandler(filelog)
        console_logger.removeHandler(console)
        return 0

    def info(self, message, executeTime=-1):
        """运行日志info级别调用api
            参数:
                param1:  日志内容
                param2:  业务执行耗时(单位ms)，运行日志可默认为-1
            返回值:
                0：成功
                500：文件备份失败
        """
        TIMEFORMAT = '%Y-%m-%d %X'

        filename = logsuite_conf.g_conf.nor_log_path + os.sep + logsuite_conf.g_conf.nor_filename
        if log_backup_judge('nor', filename, len(message)) != 0:
            return 500

        # 日志打印
        if logsuite_conf.g_conf.level == 'DEBUG':
            printlevel = logging.DEBUG
        elif logsuite_conf.g_conf.level == 'INFO':
            printlevel = logging.INFO
        elif logsuite_conf.g_conf.level == 'WARN':
            printlevel = logging.WARNING
        elif logsuite_conf.g_conf.level == 'ERROR':
            printlevel = logging.ERROR
        else:
            printlevel = logging.CRITICAL

        # 设置日志打印等级、输出格式、日志文件写入文件名
        filestr = logsuite_conf.g_conf.nor_log_path + os.path.sep + logsuite_conf.g_conf.nor_filename

        logging.basicConfig(level=printlevel,
                            format='[%(asctime)s][%(levelname)s]%(message)s',
                            datefmt=TIMEFORMAT,
                            filename=filestr,
                            filemode='a'
                            )

        console = logging.StreamHandler()
        formatter = logging.Formatter('[%(asctime)s][%(levelname)s]%(message)s')
        console.setFormatter(formatter)
        console_logger = logging.getLogger('')
        console_logger.addHandler(console)
        console_logger.setLevel(printlevel)
        logger = logging.getLogger('')
        logger.setLevel(printlevel)
        if logsuite_conf.g_conf.day_mode == 3:
            logging.handlers.TimedRotatingFileHandler(
                filename=filestr, when='D',backupCount=logsuite_conf.g_conf.keep_count ,interval=1, encoding='utf-8')

        lineno = sys._getframe().f_back.f_lineno
        funcname = sys._getframe().f_back.f_code.co_name
        filename = os.path.basename(sys._getframe().f_back.f_code.co_filename)
        if executeTime > -1:
            logstr = '[%s][%s][line:%d][executeTime:%d]%s' % (filename, funcname, lineno, executeTime, message)
        else:
            logstr = '[%s][%s][line:%d]%s' % (filename, funcname, lineno, message)

        logging.info(logstr)
        console_logger.removeHandler(console)
        return 0

    def warn(self, message, executeTime=-1):
        """运行日志warn级别调用api
            参数：
                param1:  日志内容
                param2:  业务执行耗时(单位ms)，运行日志可默认为-1
            返回值:
                0：成功
                500：文件备份失败
        """
        TIMEFORMAT = '%Y-%m-%d %X'

        filename = logsuite_conf.g_conf.nor_log_path + os.sep + logsuite_conf.g_conf.nor_filename
        if log_backup_judge('nor', filename, len(message)) != 0:
            return 500

        # 日志打印
        if logsuite_conf.g_conf.level == 'DEBUG':
            printlevel = logging.DEBUG
        elif logsuite_conf.g_conf.level == 'INFO':
            printlevel = logging.INFO
        elif logsuite_conf.g_conf.level == 'WARN':
            printlevel = logging.WARNING
        elif logsuite_conf.g_conf.level == 'ERROR':
            printlevel = logging.ERROR
        else:
            printlevel = logging.CRITICAL

        # 设置日志打印等级、输出格式、日志文件写入文件名
        filestr = logsuite_conf.g_conf.nor_log_path + os.path.sep + logsuite_conf.g_conf.nor_filename
        logging.basicConfig(level=printlevel,
                            format='[%(asctime)s][%(levelname)s]%(message)s',
                            datefmt=TIMEFORMAT,
                            filename=filestr,
                            filemode='a')

        console = logging.StreamHandler()
        formatter = logging.Formatter('[%(asctime)s][%(levelname)s]%(message)s')
        console.setFormatter(formatter)
        console_logger = logging.getLogger('')
        console_logger.addHandler(console)
        console_logger.setLevel(printlevel)

        if logsuite_conf.g_conf.day_mode == 3:
            logging.handlers.TimedRotatingFileHandler(
                filename=filestr, when='D',backupCount=logsuite_conf.g_conf.keep_count ,interval=1, encoding='utf-8')

        filelog = logging.FileHandler(filestr)
        filelog.setFormatter(formatter)
        logger = logging.getLogger('')
        logger.addHandler(filelog)
        logger.setLevel(printlevel)

        lineno = sys._getframe().f_back.f_lineno
        funcname = sys._getframe().f_back.f_code.co_name
        filename = os.path.basename(sys._getframe().f_back.f_code.co_filename)
        if executeTime > -1:
            logstr = '[%s][%s][line:%d][executeTime:%d]%s' % (filename, funcname, lineno, executeTime, message)
        else:
            logstr = '[%s][%s][line:%d]%s' % (filename, funcname, lineno, message)

        logging.warning(logstr)
        logger.removeHandler(filelog)
        console_logger.removeHandler(console)
        return 0

    def error(self, message, executeTime=-1):
        """运行日志error级别调用api
            参数：
                param1:  日志内容
                param2:  业务执行耗时(单位ms)，运行日志可默认为-1
            返回值:
                0：成功
                500：文件备份失败
        """
        TIMEFORMAT = '%Y-%m-%d %X'

        filename = logsuite_conf.g_conf.nor_log_path + os.sep + logsuite_conf.g_conf.nor_filename
        if log_backup_judge('nor', filename, len(message)) != 0:
            return 500

        # 日志打印
        if logsuite_conf.g_conf.level == 'DEBUG':
            printlevel = logging.DEBUG
        elif logsuite_conf.g_conf.level == 'INFO':
            printlevel = logging.INFO
        elif logsuite_conf.g_conf.level == 'WARN':
            printlevel = logging.WARNING
        elif logsuite_conf.g_conf.level == 'ERROR':
            printlevel = logging.ERROR
        else:
            printlevel = logging.CRITICAL

        # 设置日志打印等级、输出格式、日志文件写入文件名
        filestr = logsuite_conf.g_conf.nor_log_path + os.path.sep + logsuite_conf.g_conf.nor_filename
        logging.basicConfig(level=printlevel,
                            format='[%(asctime)s][%(levelname)s]%(message)s',
                            datefmt=TIMEFORMAT,
                            filename=filestr,
                            filemode='a')

        console = logging.StreamHandler()
        formatter = logging.Formatter('[%(asctime)s][%(levelname)s]%(message)s')
        console.setFormatter(formatter)
        console_logger = logging.getLogger('')
        console_logger.addHandler(console)
        console_logger.setLevel(printlevel)


        filelog = logging.FileHandler(filestr)
        filelog.setFormatter(formatter)
        logger = logging.getLogger('')
        logger.addHandler(filelog)
        logger.setLevel(printlevel)

        lineno = sys._getframe().f_back.f_lineno
        funcname = sys._getframe().f_back.f_code.co_name
        filename = os.path.basename(sys._getframe().f_back.f_code.co_filename)
        if executeTime > -1:
            logstr = '[%s][%s][line:%d][executeTime:%d]%s' % (filename, funcname, lineno, executeTime, message)
        else:
            logstr = '[%s][%s][line:%d]%s' % (filename, funcname, lineno, message)

        logging.error(logstr)
        logger.removeHandler(filelog)
        console_logger.removeHandler(console)
        return 0

    def biz(self, message, executeTime):
        """业务日志调用api
            参数：
                param1:  日志内容
                param2:  操作耗时ms(int)
            返回值:
                0：成功
                500：文件备份失败
        """
        TIMEFORMAT = '%Y-%m-%d %X'

        filename = logsuite_conf.g_conf.biz_log_path + os.sep + logsuite_conf.g_conf.biz_filename
        if log_backup_judge('biz', filename, len(message)) != 0:
            return 500

        # 日志打印级别
        printlevel = logging.DEBUG

        # 设置日志打印等级、输出格式、日志文件写入文件名
        filestr = logsuite_conf.g_conf.biz_log_path + os.path.sep + logsuite_conf.g_conf.biz_filename

        logging.basicConfig(level=printlevel,
                            format='%(message)s',
                            datefmt=TIMEFORMAT,
                            filename=filestr,
                            filemode='a')

        lineno = sys._getframe().f_back.f_lineno
        funcname = sys._getframe().f_back.f_code.co_name
        filename = os.path.basename(sys._getframe().f_back.f_code.co_filename)
        logstr = '[%s][%s][line:%d][executeTime:%d]%s' % (filename, funcname, lineno, executeTime, message)

        console = logging.StreamHandler()
        formatter = logging.Formatter('[%(asctime)s][%(levelname)s]%(message)s')
        console.setFormatter(formatter)
        console_logger = logging.getLogger('')
        console_logger.addHandler(console)
        console_logger.setLevel(printlevel)

        if logsuite_conf.g_conf.day_mode == 3:
            logging.handlers.TimedRotatingFileHandler(
                filename=filestr, when='D',backupCount=logsuite_conf.g_conf.keep_count ,interval=1, encoding='utf-8')

        filelog = logging.FileHandler(filestr)
        filelog.setFormatter(formatter)
        logger = logging.getLogger('')
        logger.addHandler(filelog)
        logger.setLevel(printlevel)

        logging.info(logstr)
        logger.removeHandler(filelog)
        console_logger.removeHandler(console)
        return 0


class Logger(logging.Logger):
    #日志输出地址
    def __init__(self, log_config_path):
        super(Logger, self).__init__(self)

        logsuite_conf.init_LogSuite_conf(log_config_path)
        logthread = updateThread(log_config_path, 30)
        logthread.start()

        # 日志打印
        if logsuite_conf.g_conf.level == 'DEBUG':
            printlevel = logging.DEBUG
        elif logsuite_conf.g_conf.level == 'INFO':
            printlevel = logging.INFO
        elif logsuite_conf.g_conf.level == 'WARN':
            printlevel = logging.WARNING
        elif logsuite_conf.g_conf.level == 'ERROR':
            printlevel = logging.ERROR
        else:
            printlevel = logging.CRITICAL

        # 日志文件名
        filename = logsuite_conf.g_conf.nor_log_path + os.path.sep + logsuite_conf.g_conf.nor_filename
        dir = logsuite_conf.g_conf.nor_log_path + os.path.sep
        if not os.path.exists(dir):
            os.makedirs(dir)
        self.filename = filename

        fh = handlers.TimedRotatingFileHandler(filename=self.filename, when='D',
                                               interval=1, backupCount=logsuite_conf.g_conf.keep_count,
                                               encoding='utf-8')
        # 以日期分隔，每天一份，保留15天

        ch = logging.StreamHandler()  # 屏幕输出
        #    fh=logging.FileHandler(logUrl,'a') #文件输出，默认为‘a’
        ch_formatter = logging.Formatter("[%(asctime)s][%(filename)s][line:%(lineno)d][%(levelname)s] %(message)s")
        fh_formatter = logging.Formatter('[%(asctime)s][%(filename)s][line:%(lineno)d][%(levelname)s] %(message)s')
        ch.setFormatter(ch_formatter)  # 添加到输出流
        fh.setFormatter(fh_formatter)  # 添加到输出流 #创建一个log并添加定义的输出流
        self.setLevel(printlevel)
        self.addHandler(ch)
        self.addHandler(fh)

