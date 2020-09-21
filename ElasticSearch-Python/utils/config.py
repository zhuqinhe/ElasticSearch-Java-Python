# -*- coding: utf-8 -*-
# !/usr/bin/env python
# author:zhuqinhe
# datetime:2020/09/21 0025 17:40 下午5:40
import os
import logging

# Modified the get option value case-insensitive question.
import configparser


def optionxform(optionstr):
    return optionstr


class Config(object):
    def __init__(self):
        self.cf = configparser.ConfigParser()
        self.mtime = None
        self.path = None

    def __new__(cls, *args, **kwargs):
        if not hasattr(cls, '_instance'):
            org = super(Config, cls)
            cls._instance = org.__new__(cls)
        return cls._instance

    def set_config(self, path):
        if os.path.isfile(path):
            self.path = path
            self.mtime = os.path.getmtime(path)
            self.cf.read(path, encoding='utf-8')
        else:
            logging.error('config file[%s] is not exist.' % path)

    def reload(self):
        self.mtime = os.path.getmtime(self.path)
        self.cf.read(self.path)

    def get_sections(self):
        try:
            return self.cf.sections()
        except:
            logging.error('getSections error.')
            return []

    def get_items(self, section):
        try:
            return self.cf.items(section)
        except:
            logging.error('getItems[%s] error.' % section)
            return []

    def get_options(self, section):
        try:
            return self.cf.options(section)
        except:
            logging.error('getOptions[%s]error.' % section)
            return []

    def get(self, field, key, default='', field_type='str'):
        try:
            if os.path.getmtime(self.path) != self.mtime:
                self.reload()
            if field_type == 'int':
                result = self.cf.getint(field, key)
            elif field_type == 'boolean':
                result = self.cf.getboolean(field, key)
            elif field_type == 'float':
                result = self.cf.getfloat(field, key)
            else:
                result = self.cf.get(field, key)
        except:
            logging.error('get field[%s] key[%s] failed.' % (field, key))
            result = default
        return result

    def add_section(self, section):
        self.cf.add_section(section)

    def set(self, field, key, value):
        try:
            self.cf.set(field, key, value)
            self.cf.write(open(self.path, 'w'))
        except:
            logging.error('set field[%s] key[%s] value[%s] failed.' % (field, key, value))
            return False
        return True
