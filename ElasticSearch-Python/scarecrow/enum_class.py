# -*- coding: utf-8 -*-
# !/usr/bin/env python
# author:zhuqinhe
# datetime:2020/09/21 0025 17:40 下午5:40
from enum import Enum
class Action(Enum):
    '''
    事件操作
    1：新增
    2：修改
    3: 删除
    '''
    RESOURCE_ADD = 1
    RESOURCE_MODIFIED = 2
    RESOURCE_DELETE = 3

class AssertResult(Enum):
    '''
    仲裁结果
    0:正常
    1：新增恶意资源
    2：资源被篡改
    3：资源被删除
    '''
    RESOURCE_NORMAL = 0
    RESOURCE_IllEGAL_ADD = 1
    RESOURCE_IllEGAL_MODIFIED = 2
    RESOURCE_IllEGAL_DELETE = 3

