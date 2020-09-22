package com.hoob.search.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.hoob.search.common.DbQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hoob.search.dao.ReportSearchDao;
import com.hoob.search.vo.HotWordReportReq;

@Repository("reportSearchDao")
public class ReportSearchDaoImpl implements ReportSearchDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportSearchDaoImpl.class);

	@Resource
	JdbcTemplate jdbcTemplate;

	@Override
	public boolean hotWordReport(HotWordReportReq req) throws Exception {

		try {
			DbQueue.getDBHotWordReportReqQueue().add(req);
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return false;
	}

	@Override
	public void batchHotWordReport(List<HotWordReportReq> hotWordList) throws Exception {
		try {
			String sql = "insert into search_report_words(`name`,`searchWord`,`contentId`,"
					+ "`userToken`,`searchTime`) values (?,?,?,?,?)";
			this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					// TODO Auto-generated method stub
					HotWordReportReq hotWordReport = hotWordList.get(i);
					ps.setString(1, hotWordReport.getName());
					ps.setString(2, hotWordReport.getSearchWord());
					ps.setString(3, hotWordReport.getContentId());
					ps.setString(4, hotWordReport.getUserToken());
					ps.setString(5, hotWordReport.getSearchTime());
				}

				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return hotWordList.size();
				}

			});

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

}
