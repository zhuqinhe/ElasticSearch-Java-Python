# -*- coding: utf-8 -*-
# !/usr/bin/env python
# author:zhuqinhe
# datetime:2020/09/21 0025 17:40 下午5:40
from importlib import reload

__version__ = '0.9'
__all__ = ["PinYin"]

import os.path,sys
reload(sys)
#sys.setdefaultencoding('utf8')


def load_word(dict_file):
    word_dict = {}
    if not os.path.exists(dict_file):
        raise IOError("NotFoundFile")

    with open(dict_file) as f_obj:
        for f_line in f_obj.readlines():
            try:
                line = f_line.split('    ')
                word_dict[line[0]] = line[1]
            except:
                line = f_line.split('   ')
                word_dict[line[0]] = line[1]
    return word_dict

word_dict = load_word('./utils/word.data')

def hanzi2pinyin(string="", firstcode=False):
    result = []

    if not isinstance(string, unicode):
        try:
            string = string.decode('utf8')
        except:
            try:
                string = string.decode('gbk')
            except:
                try:
                    string = string.decode('"iso-8859-1"')
                except:
                    print('unknown coding')
                    return

    for char in string:
        key = '%X' % ord(char)
        value = word_dict.get(key, char)
        try:
            outpinyin = str(value).split()[0][:-1].lower()
            if not outpinyin:
                outpinyin = char
            if firstcode:
                result.append(outpinyin[0])
            else:
                result.append(outpinyin)
        except:
            pass

    return result

def replace_zh(text):
    try:

        with open('./utils/special', 'rb') as f:
            try:
                punctuation = f.read()
                punctuation = punctuation.split(",")
            except:
                print("read special except!")
            finally:
                f.close()

        if not punctuation:
            punctuation = ['！', '？', '｡', '＂', '＃', '＄', '％', '＆', '＇', '（', '）', '＊', '＋', '，', '－', '／', '：', '；',
                           '＜', '＝', '＞', '＠', '［', '＼', '］', '＾', '＿', '｀', '｛', '｜', '｝', '～', '｟', '｠', '｢', '｣',
                           '､', '、', '〃', '《', '》', '[', ']', '「', '」', '『', '』', '【', '】', '〔', '〕', '〖', '〗', '〘',
                           '〙', '〚', '〛', '〜', '〝', '〞', '〟', '〰', '〾', '〿', '–', '—', '‘', '’', '‛', '“', '”', '„',
                           '‟', '…‧', '﹏', '.', '"', ')', '(', '!', '•', '-', '・', '…', '・', '=',
                           'Ⅰ', 'Ⅱ', 'Ⅲ', 'Ⅳ', 'Ⅴ', 'Ⅵ', 'Ⅶ', 'Ⅷ', 'Ⅸ', 'Ⅹ', 'Ⅺ', 'Ⅻ']
        for i in punctuation:
            text = text.replace(i, '')
        text = text.replace(" ","")
        text = text.replace(" ","")
    except:
        pass
    return text

def hanzi2pinyin_split(string="", split="", firstcode=False):
    """提取中文的拼音
    @param string:要提取的中文
    @param split:分隔符
    @param firstcode: 提取的是全拼还是首字母？如果为true表示提取首字母，默认为False提取全拼
    """

    try:
        string = replace_zh(string)
        result = hanzi2pinyin(string=string, firstcode=firstcode)
        res = split.join(result)
        res = res.encode("utf8")
    except:
        res = main(string)
    return res

def multi_get_letter(str_input):
    if isinstance(str_input, unicode):
        unicode_str = str_input
    else:
        try:
            unicode_str = str_input.decode('utf8')
        except:
            try:
                unicode_str = str_input.decode('gbk')
            except:
                return
    return_list = []
    for one_unicode in unicode_str:
        return_list.append(single_get_first(one_unicode))
    return return_list

def single_get_first(unicode1):
    try:
        str1 = unicode1.encode('gbk')
    except:
        try:
            str1 = unicode1.encode('utf8')
        except:
            str1 = unicode1
    try:
        ord(str1)
        return str1
    except:
        asc = ord(str1[0]) * 256 + ord(str1[1]) - 65536
        if asc >= -20319 and asc <= -20284:
            return 'a'
        if asc >= -20283 and asc <= -19776:
            return 'b'
        if asc >= -19775 and asc <= -19219:
            return 'c'
        if asc >= -19218 and asc <= -18711:
            return 'd'
        if asc >= -18710 and asc <= -18527:
            return 'e'
        if asc >= -18526 and asc <= -18240:
            return 'f'
        if asc >= -18239 and asc <= -17923:
            return 'g'
        if asc >= -17922 and asc <= -17418:
            return 'h'
        if asc >= -17417 and asc <= -16475:
            return 'j'
        if asc >= -16474 and asc <= -16213:
            return 'k'
        if asc >= -16212 and asc <= -15641:
            return 'l'
        if asc >= -15640 and asc <= -15166:
            return 'm'
        if asc >= -15165 and asc <= -14923:
            return 'n'
        if asc >= -14922 and asc <= -14915:
            return 'o'
        if asc >= -14914 and asc <= -14631:
            return 'p'
        if asc >= -14630 and asc <= -14150:
            return 'q'
        if asc >= -14149 and asc <= -14091:
            return 'r'
        if asc >= -14090 and asc <= -13119:
            return 's'
        if asc >= -13118 and asc <= -12839:
            return 't'
        if asc >= -12838 and asc <= -12557:
            return 'w'
        if asc >= -12556 and asc <= -11848:
            return 'x'
        if asc >= -11847 and asc <= -11056:
            return 'y'
        if asc >= -11055 and asc <= -10247:
            return 'z'
        return ''

def main(str_input):
    a = multi_get_letter(str_input)
    b = ''
    for i in a:
        b = b + i
    return b

