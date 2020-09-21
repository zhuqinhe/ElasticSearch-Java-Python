# -*- coding: utf-8 -*-
# !/usr/bin/env python
# author:zhuqinhe
# datetime:2020/09/21 0025 17:40 下午5:40
from importlib import reload

import pymysql,datetime,sys,traceback
from scarecrow.globals import runlog,db_config,db_name,old_delete_switch,old_delete_day

reload(sys)
#sys.setdefaultencoding('utf8')

def analyse_hot_words():
    today_date = datetime.datetime.now().strftime('%Y-%m-%d')+" 00:00:00"
    tomorrow_date = (datetime.datetime.now()+datetime.timedelta(days=1)).strftime("%Y-%m-%d")+" 00:00:00"
    delete_date = (datetime.datetime.now()+datetime.timedelta(days=-(old_delete_day))).strftime("%Y-%m-%d")+" 00:00:00"
    series_conn = pymysql.connect(**db_config)
    series_conn.autocommit(1)
    series_conn.select_db(db_name)
    cursor_conn = series_conn.cursor()

    try:
        if old_delete_switch == "1":
            old_words_delete_sql = '''
                                  DELETE 
                                  FROM
                                  search_report_words 
                                  WHERE
                                  searchTime < '%s' 
                                ''' % (delete_date)
            cursor_conn.execute(old_words_delete_sql)

        words_group_sql = '''SELECT 
                              NAME,
                              contentId,
                              COUNT(1) AS conuts 
                              FROM
                                  search_report_words 
                              WHERE
                                  searchTime >= '%s' 
                                  AND 
                                  searchTime < '%s' 
                              GROUP BY
                              NAME,contentId''' % (today_date,tomorrow_date)

        cursor_conn.execute(words_group_sql)
        results = cursor_conn.fetchall()

        for result in results:
            delete_words_old_analyse_sql = '''
                           DELETE
                           FROM
                           `search_hot_words`
                           WHERE
                           `words` = '%s'
                           AND
                            `contentId` = '%s'
                           AND
                           `searchDate` >= '%s'
                            AND
                           `searchDate` < '%s'
                           ''' % (
                                  result.get("NAME", "null"),
                                  result.get("contentId","null"),
                                  today_date,
                                  tomorrow_date)

            cursor_conn.execute(delete_words_old_analyse_sql)
            words_new_analyse_sql = '''
                                   INSERT
                                   INTO
                                   `search_hot_words`
                                   (`words`,`Counts`,`contentId`,`searchDate`)
                                   values
                                   ('%s','%s','%s','%s')
                                   ''' % (
                                          result.get("NAME", "null"),
                                          result.get("conuts","null"),
                                          result.get("contentId","null"),
                                          today_date)
            cursor_conn.execute(words_new_analyse_sql)


    except:
        runlog.error(traceback.format_exc())
    finally:
        # 关闭游标连接
        cursor_conn.close()
        series_conn.close()