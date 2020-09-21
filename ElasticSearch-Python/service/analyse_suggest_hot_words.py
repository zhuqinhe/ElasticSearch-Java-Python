# -*- coding: utf-8 -*-
# !/usr/bin/env python
# author:zhuqinhe
# datetime:2020/09/21 0025 17:40 下午5:40
import re
import requests
import sys
import threading
import traceback
from importlib import reload

import pymysql
import threadpool

reload(sys)
from service.sync_elasticsearch_server import ElasticObj
from scarecrow.globals import runlog, increment_update_time_interval, increment_delete_time_interval, db_config, \
    db_name, suggest_index_type, \
    suggest_index_name
# sys.setdefaultencoding('utf8')
from utils.pinyin import hanzi2pinyin_split


def get_analyse_data():
    runlog.info(u'开始分析analysis data ...')
    pool = threadpool.ThreadPool(10)
    check_suggest_null = threading.Thread(target=check_suggest_null_keyword)
    check_suggest_null.start()
    check_repeat = threading.Thread(target=check_repeat_data)
    check_repeat.start()
    series_conn = pymysql.connect(**db_config)
    series_conn.autocommit(1)
    series_conn.select_db(db_name)
    cursor_conn = series_conn.cursor()
    try:
        sync_analyse_sql_count = '''
                SELECT
                count(*) as total
                FROM
                `series`
                WHERE
                `isDelete` = '0'
                AND
                `isAnalyse` = '0'
                '''
        cursor_conn.execute(sync_analyse_sql_count)
        results = cursor_conn.fetchall()

        total = results[0].get("total")
        if int(total) > 0:
            num = 100
            while (True):
                sync_analyse_sql = '''
                        SELECT
                        `SeriesId`,
                        `Name`,
                        `Actors`,
                        `Directors`,
                        `Tags`,
                        `Kind`
                        FROM
                        `series`
                        WHERE
                        `isDelete` = '0'
                        AND
                        `isAnalyse` = '0'
                        ORDER BY `insertDate`
                        LIMIT
                        %s;
                        ''' % (num)
                cursor_conn.execute(sync_analyse_sql)
                results = cursor_conn.fetchall()
                res_list = []
                if not results:
                    break
                for result in results:
                    sync_dict = {
                        "seriesId": result.get("SeriesId", "NULL"),
                        "name": result.get("Name", "null"),
                        "actors": result.get("Actors", "null"),
                        "directors": result.get("Directors", "null"),
                        "tags": result.get("Tags", "null"),
                        "kind": result.get("Kind", "null"),
                    }
                    res_list.append(sync_dict)

                if res_list:
                    requests = threadpool.makeRequests(sync_analyse_suggest_hot_words_thread, res_list)
                    [pool.putRequest(req) for req in requests]
                    pool.wait()
                series_conn.commit()


    except:
        runlog.error(traceback.format_exc())
    finally:
        # 关闭游标连接
        cursor_conn.close()
        series_conn.close()
        runlog.info(u'结束分析analysis data ...')
        global analyse_data
        analyse_data = threading.Timer(float(increment_update_time_interval), get_analyse_data)
        analyse_data.start()


def sync_analyse_suggest_hot_words_thread(suggest_hot_words_list):
    hot_conn = pymysql.connect(**db_config)
    hot_conn.autocommit(1)
    hot_conn.select_db(db_name)
    cursor_hot_conn = hot_conn.cursor()
    suggest_hot_words = dict(suggest_hot_words_list)
    try:
        try:
            if suggest_hot_words.get("name"):
                name = suggest_hot_words.get("name")
                name = name.strip()
                # 入库
                sync_suggest_hot_word_mysql(name, "series_name")

            if suggest_hot_words.get("actors"):
                actors_hot_word = str(suggest_hot_words.get("actors")) \
                    .replace(",", "|").replace("、", "|").replace(" ", "|").replace("//", "|").replace("，", "|").replace(
                    " ", "")
                actors = re.split(r"[,|;]", actors_hot_word)
                for actor in actors:
                    if actor:
                        actor = actor.strip()
                        sync_suggest_hot_word_mysql(actor, "series_actor")

            if suggest_hot_words.get("directors"):
                directors_hot_words = str(suggest_hot_words.get("directors")) \
                    .replace(",", "|").replace("、", "|").replace(" ", "|").replace("//", "|").replace("，", "|").replace(
                    " ", "")
                directors = re.split(r"[,|;]", directors_hot_words)
                for director in directors:
                    if director:
                        director = director.strip()
                        sync_suggest_hot_word_mysql(director, "series_director")

            if suggest_hot_words.get("tags"):
                tags_hot_words = str(suggest_hot_words.get("tags")).replace(",", "|") \
                    .replace("、", "|").replace(" ", "|").replace("//", "|").replace("，", "|").replace(" ", "")
                tags = re.split(r"[,|;]", tags_hot_words)
                for tag in tags:
                    if tag:
                        tag = tag.strip()
                        sync_suggest_hot_word_mysql(tag, "series_tag")

            if suggest_hot_words.get("kind"):
                tags_hot_words = str(suggest_hot_words.get("kind")).replace(",", "|") \
                    .replace("、", "|").replace(" ", "|").replace("//", "|").replace("，", "|").replace(" ", "")
                tags = re.split(r"[,|;]", tags_hot_words)
                for tag in tags:
                    if tag:
                        tag = tag.strip()
                        sync_suggest_hot_word_mysql(tag, "kind")
        except:
            runlog.error(traceback.format_exc())

        cursor_hot_conn.execute(
            "UPDATE series SET isAnalyse = 1  WHERE SeriesId = '%s'" % (suggest_hot_words.get("seriesId", "NULL")))

    except:
        runlog.error(traceback.format_exc())
        hot_conn.rollback()
    finally:
        hot_conn.commit()
        # 关闭游标连接
        cursor_hot_conn.close()
        hot_conn.close()


def sync_analyse_suggest_hot_words(suggest_hot_words_list):
    hot_conn = pymysql.connect(**db_config)
    hot_conn.autocommit(1)
    hot_conn.select_db(db_name)
    cursor_hot_conn = hot_conn.cursor()
    try:
        for suggest_hot_words in suggest_hot_words_list:
            try:
                if suggest_hot_words.get("name"):
                    name = suggest_hot_words.get("name")
                    name = name.strip()
                    # 入库
                    sync_suggest_hot_word_mysql(name, "series_name")

                if suggest_hot_words.get("actors"):
                    actors_hot_word = str(suggest_hot_words.get("actors")) \
                        .replace(",", "|").replace("、", "|").replace(" ", "|").replace("//", "|").replace("，", "|")
                    actors = re.split(r"[,|;]", actors_hot_word)
                    for actor in actors:
                        if actor:
                            actor = actor.strip()
                            sync_suggest_hot_word_mysql(actor, "series_actor")

                if suggest_hot_words.get("directors"):
                    directors_hot_words = str(suggest_hot_words.get("directors")) \
                        .replace(",", "|").replace("、", "|").replace(" ", "|").replace("//", "|").replace("，", "|")
                    directors = re.split(r"[,|;]", directors_hot_words)
                    for director in directors:
                        if director:
                            director = director.strip()
                            sync_suggest_hot_word_mysql(director, "series_director")

                if suggest_hot_words.get("tags"):
                    tags_hot_words = str(suggest_hot_words.get("tags")).replace(",", "|") \
                        .replace("、", "|").replace(" ", "|").replace("//", "|").replace("，", "|")
                    tags = re.split(r"[,|;]", tags_hot_words)
                    for tag in tags:
                        if tag:
                            tag = tag.strip()
                            sync_suggest_hot_word_mysql(tag, "series_tag")

                if suggest_hot_words.get("kind"):
                    tags_hot_words = str(suggest_hot_words.get("kind")).replace(",", "|") \
                        .replace("、", "|").replace(" ", "|").replace("//", "|").replace("，", "|")
                    tags = re.split(r"[,|;]", tags_hot_words)
                    for tag in tags:
                        if tag:
                            tag = tag.strip()
                            sync_suggest_hot_word_mysql(tag, "kind")
            except:
                runlog.error(traceback.format_exc())

            cursor_hot_conn.execute("UPDATE series SET isAnalyse = 1  WHERE SeriesId = '%s'" %
                                    (suggest_hot_words.get("seriesId", "NULL")))

    except:
        runlog.error(traceback.format_exc())
        hot_conn.rollback()
    finally:
        hot_conn.commit()
        # 关闭游标连接
        cursor_hot_conn.close()
        hot_conn.close()


def sync_suggest_hot_word_mysql(value, content):
    hot_conn = pymysql.connect(**db_config)
    hot_conn.autocommit(1)
    hot_conn.select_db(db_name)
    cursor_hot_conn = hot_conn.cursor()

    try:
        if value:
            try:
                suggest_old_sql_count = '''
                            SELECT
                            count(*) as total
                            FROM
                            `suggest`
                            WHERE
                            `name` = '%s'
                            ''' % (value)
                cursor_hot_conn.execute(suggest_old_sql_count)
                results = cursor_hot_conn.fetchall()

                total = results[0].get("total")
                if int(total) == 0:
                    keywords = ""
                    try:
                        pinyinkey = value.replace(' ', '').replace("·", '').replace('.', '').replace("-", '')
                        pinyinkey = u'%s' % pinyinkey
                        pinyinkey = formatByWidth(pinyinkey.decode('utf-8'), 20)
                        keywords = hanzi2pinyin_split(string=pinyinkey, split="", firstcode=True)
                        keywords = keywords.lower()
                    except:
                        runlog.debug(traceback.format_exc())
                        pass
                    try:
                        sync_suggest_name_insert = '''
                                               INSERT
                                               `suggest`
                                               (`name`,`describe`,`keywords`)
                                               VALUES
                                               ('%s','%s','%s');
                                               ''' % (value, content, keywords)
                        cursor_hot_conn.execute(sync_suggest_name_insert)
                    except:
                        runlog.debug(traceback.format_exc())
                        pass
                else:
                    sync_suggest_name_update = '''
                                                  UPDATE suggest 
                                                  SET isSync = 0,isDelete=0 
                                                  WHERE
                                                  `name`='%s'
                                                   ''' % (value)
                    cursor_hot_conn.execute(sync_suggest_name_update)
            except:
                hot_conn.rollback()
                runlog.debug(traceback.format_exc())
                pass

            try:
                cursor_hot_conn.execute("delete from ik_hot_words where words='%s'" % (value))
                sync_suggest_name_insert = '''
                                       INSERT
                                       `ik_hot_words`
                                       (`words`)
                                       VALUES
                                       ('%s');
                                       ''' % (value)
                cursor_hot_conn.execute(sync_suggest_name_insert)
            except:
                hot_conn.rollback()
                runlog.debug(traceback.format_exc())
                pass

    except:
        runlog.error(traceback.format_exc())
        hot_conn.rollback()
    finally:
        # 关闭游标连接
        hot_conn.commit()
        cursor_hot_conn.close()
        hot_conn.close()


def delete_analyse_data():
    runlog.info(u"开始分析 deleted data ...")
    series_conn = pymysql.connect(**db_config)
    series_conn.autocommit(1)
    series_conn.select_db(db_name)
    cursor_conn = series_conn.cursor()
    try:
        sync_analyse_sql_count = '''
                SELECT
                count(*) as total
                FROM
                `series`
                WHERE
                `isDelete` = '1'
                AND
                `isAnalyse` = '0'
                '''
        cursor_conn.execute(sync_analyse_sql_count)
        results = cursor_conn.fetchall()

        total = results[0].get("total")
        if int(total) > 0:
            num = 100
            while (True):
                sync_analyse_sql = '''
                        SELECT
                        `SeriesId`,
                        `Name`,
                        `Actors`,
                        `Directors`,
                        `Tags`,
                        `Kind`
                        FROM
                        `series`
                        WHERE
                        `isDelete` = '1'
                        AND
                        `isAnalyse` = '0'
                        ORDER BY `insertDate`
                        LIMIT
                        %s;
                        ''' % (num)
                cursor_conn.execute(sync_analyse_sql)
                results = cursor_conn.fetchall()
                res_list = []
                if not results:
                    break
                if results:
                    for result in results:
                        sync_dict = {
                            "seriesId": result.get("SeriesId", "null"),
                            "name": result.get("Name", "null"),
                            "actors": result.get("Actors", "null"),
                            "directors": result.get("Directors", "null"),
                            "tags": result.get("Tags", "null"),
                            "kind": result.get("Kind", "null"),
                        }
                        res_list.append(sync_dict)

                if res_list:
                    delete_analyse_suggest_hot_words(res_list)

                # if num > int(total):
                #     break
                #
                # num = num + 100

    except:
        runlog.error(traceback.format_exc())
    finally:
        # 关闭游标连接
        cursor_conn.close()
        series_conn.close()
        runlog.info(u"结束分析 deleted data ...")
        global delete_analyse
        delete_analyse = threading.Timer(float(increment_delete_time_interval), delete_analyse_data)
        delete_analyse.start()


def delete_analyse_suggest_hot_words(suggest_hot_words_list):
    hot_conn = pymysql.connect(**db_config)
    hot_conn.autocommit(1)
    hot_conn.select_db(db_name)
    cursor_hot_conn = hot_conn.cursor()
    try:
        for suggest_hot_words in suggest_hot_words_list:
            try:
                if suggest_hot_words.get("name"):
                    name = suggest_hot_words.get("name")
                    name = name.strip()
                    delete_suggest_hot_word_mysql(name, "series_name")

                if suggest_hot_words.get("actors"):
                    actors_hot_word = str(suggest_hot_words.get("actors")).replace(",", "|").replace("、", "|").replace(
                        " ", "|")
                    actors = re.split(r"[,|]", actors_hot_word)
                    for actor in actors:
                        if actor:
                            actor = actor.strip()
                            delete_suggest_hot_word_mysql(actor, "series_actor")

                if suggest_hot_words.get("directors"):
                    directors_hot_words = str(suggest_hot_words.get("directors")).replace(",", "|").replace("、",
                                                                                                            "|").replace(
                        " ", "|")
                    directors = re.split(r"[,|]", directors_hot_words)
                    for director in directors:
                        if actor:
                            actor = actor.strip()
                            delete_suggest_hot_word_mysql(director, "series_director")

                if suggest_hot_words.get("tags"):
                    tags_hot_words = str(suggest_hot_words.get("tags")).replace(",", "|").replace("、", "|").replace(" ",
                                                                                                                    "|")
                    tags = re.split(r"[,|]", tags_hot_words)
                    for tag in tags:
                        if tag:
                            tag = tag.strip()
                            delete_suggest_hot_word_mysql(tag, "series_tag")

                if suggest_hot_words.get("kind"):
                    tags_hot_words = str(suggest_hot_words.get("kind")).replace(",", "|").replace("、", "|").replace(" ",
                                                                                                                    "|")
                    tags = re.split(r"[,|]", tags_hot_words)
                    for tag in tags:
                        if tag:
                            tag = tag.strip()
                            delete_suggest_hot_word_mysql(tag, "series_kind")
            except:
                runlog.error(traceback.format_exc())

            cursor_hot_conn.execute(
                "UPDATE series SET isAnalyse = 1  WHERE SeriesId = '%s'" % (suggest_hot_words.get("seriesId", "NULL")))


    except:
        runlog.error(traceback.format_exc())
        hot_conn.rollback()
    finally:
        hot_conn.commit()
        # 关闭游标连接
        cursor_hot_conn.close()
        hot_conn.close()


def delete_suggest_hot_word_mysql(value, content):
    hot_conn = pymysql.connect(**db_config)
    hot_conn.autocommit(1)
    hot_conn.select_db(db_name)
    cursor_hot_conn = hot_conn.cursor()
    try:
        if value:
            if content == "series_name":
                sync_suggest_name_update = '''
                                       UPDATE
                                       `suggest`
                                       SET
                                       `isDelete` = 1,`isSync` = 0
                                       WHERE
                                       `name`='%s' 
                                       AND
                                       `describe`='%s';
                                       ''' % (value, content)
                cursor_hot_conn.execute(sync_suggest_name_update)

                sync_suggest_name_delete = '''
                                       DELETE
                                       FROM
                                       `ik_hot_words`
                                       WHERE
                                       `words` = '%s';
                                       ''' % (value)
                cursor_hot_conn.execute(sync_suggest_name_delete)


    except:
        hot_conn.rollback()
        runlog.error(traceback.format_exc())
    finally:
        # 关闭游标连接
        hot_conn.commit()
        cursor_hot_conn.close()
        hot_conn.close()


def check_suggest_null_keyword():
    runlog.info(u'check suggest null keyword ...')
    hot_conn = pymysql.connect(**db_config)
    hot_conn.autocommit(1)
    hot_conn.select_db(db_name)
    cursor_hot_conn = hot_conn.cursor()
    try:
        sync_suggest_count = '''
                        SELECT
                        count(*) as total
                        FROM
                        `series`
                        WHERE keywords = ''
                    '''

        cursor_hot_conn.execute(sync_suggest_count)
        results = cursor_hot_conn.fetchall()
        total = results[0].get("total")
        ii = 0

        while True:
            sync_sugges = '''
                                  SELECT
                                  name
                                  FROM
                                  `suggest`
                                  WHERE keywords = ''
                                  limit %s,%s
                               ''' % (ii, ii + 100)
            ii = ii + 100

            cursor_hot_conn.execute(sync_sugges)
            results = cursor_hot_conn.fetchall()

            for result in results:
                try:
                    keywords = ""
                    try:
                        pinyinkey = result.get("name")
                        pinyinkey = pinyinkey.replace(' ', '').replace("·", '').replace('.', '').replace("-", '')
                        pinyinkey = u'%s' % pinyinkey
                        pinyinkey = formatByWidth(pinyinkey.decode('utf-8'), 20)
                        keywords = hanzi2pinyin_split(string=pinyinkey, split="", firstcode=True)
                    except:
                        runlog.debug(traceback.format_exc())
                    cursor_hot_conn.execute(
                        "UPDATE suggest SET isSync = 0, keywords = '%s' WHERE name = '%s'"
                        % (keywords, result.get("name")))
                    cursor_hot_conn.fetchall()
                except:
                    pass

            if ii > total:
                break
    except:
        pass


def check_repeat_data():
    runlog.info(u'check repeat data ...')
    hot_conn = pymysql.connect(**db_config)
    hot_conn.autocommit(1)
    hot_conn.select_db(db_name)
    cursor_hot_conn = hot_conn.cursor()
    try:
        try:
            suggest_repeat_sql = '''
            SELECT id,name FROM suggest WHERE name in 
            (SELECT name FROM  suggest GROUP BY `Name` HAVING Count(`Name`) >1) 
            and id in (select min(id) FROM  suggest GROUP BY `Name` HAVING Count(`Name`) >1
            )
            '''
            cursor_hot_conn.execute(suggest_repeat_sql)
            results = cursor_hot_conn.fetchall()
            if requests:
                for result in results:
                    suggest_id = result.get("id")
                    if suggest_index_name:
                        for index_name in suggest_index_name:
                            ElasticObj().Set_Mysql_Repeat_Delete_Suggest(suggest_id, index_name, suggest_index_type)
        except:
            pass

        try:
            ik_hot_words_repeat_sql = '''
            SELECT
                id,
                words 
            FROM
                ik_hot_words 
            WHERE
                words IN ( SELECT words FROM ik_hot_words GROUP BY `words` HAVING Count( `words` ) > 1 ) 
                AND id IN ( SELECT min( id ) FROM ik_hot_words GROUP BY `words` HAVING Count( `words` ) > 1 )
            '''
            cursor_hot_conn.execute(ik_hot_words_repeat_sql)
            results = cursor_hot_conn.fetchall()
            if requests:
                for result in results:
                    suggest_id = result.get("id")
                    cursor_hot_conn.execute("delete from ik_hot_words where id = '%s'" % (suggest_id))

        except:
            pass


    except:
        pass


def isAscii(ch):
    return ch <= u'\u007f'


def formatByWidth(text, width):
    count = 0
    for u in text:
        if not isAscii(u):
            count += 1
    return text + " " * (width - count - len(text))
