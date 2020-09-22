package com.hoob.search.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hoob.search.po.HotWordPo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

public class HotWordRowMapper implements RowMapper<HotWordPo>{

	@Nullable
    @Override
	public HotWordPo mapRow(ResultSet rs, int rowNum) throws SQLException {
		HotWordPo jc = new HotWordPo();
        jc.setHotword(rs.getString("words"));
        jc.setCount(rs.getInt("Counts"));
        jc.setContentId(rs.getString("contentId"));
        jc.setPosterPicUrl(rs.getString("PosterPicUrl"));
        jc.setDetailPicUrl(rs.getString("DetailPicUrl"));
        jc.setTotalNum(rs.getInt("TotalNum"));
        jc.setUpdateNum(rs.getInt("UpdateNum"));
        jc.setProgramType(rs.getString("ProgramType"));
        return jc;
	}

}
