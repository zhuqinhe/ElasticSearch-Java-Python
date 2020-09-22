package com.hoob.search.controller;

import java.util.List;

import javax.annotation.Resource;

import com.hoob.search.common.Constants;
import com.hoob.search.po.ContentResp;
import com.hoob.search.po.HotWordResp;
import com.hoob.search.po.SuggestPo;
import com.hoob.search.po.SuggestResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hoob.search.server.SearchServer;
import com.hoob.search.utils.ObjectUtils;
import com.hoob.search.utils.StringUtils;
import com.hoob.search.utils.UserInfoLogUtil;
import com.hoob.search.vo.ContentReq;
import com.hoob.search.vo.HotWordReportReq;
import com.hoob.search.vo.HotWordReq;
import com.hoob.search.vo.SuggestReq;

@RestController
public class SearchController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
	@Resource
	private SearchServer searchServer;

	/**
	 * 联想搜索输入提示
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/v1/suggest/search", consumes = {
			"application/json" }, produces = { "application/json;charset=utf-8" })
	public String simpleFuzzySuggestSearch(@RequestBody String reqJson) {
		try {
			long start = System.currentTimeMillis();

			SuggestReq req = JSON.parseObject(reqJson, SuggestReq.class);
			// 校验参数
			if (StringUtils.isEmpty(req.getQueryText())) {
				return JSON.toJSONString(
						new SuggestResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}
			if (ObjectUtils.isEmpty(req.getStart())) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}
			if (ObjectUtils.isEmpty(req.getSize())) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}

			// 校验长度
			if (!StringUtils.isGteLength(req.getQueryText(), 255)) {
				return JSON.toJSONString(
						new SuggestResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}

			SuggestResp resp = new SuggestResp();

			List<SuggestPo> suggestPos = searchServer.simpleFuzzySuggestSearch(req);
			resp.setResultCode(Constants.SUCCESS_CODE);
			resp.setDescription(Constants.SUCCESS_MSG);
			resp.setSuggestList(suggestPos);

			LOGGER.debug("simple fuzzy suggest search reqJson:[{}] cost time: {}ms", reqJson,
					(System.currentTimeMillis() - start));

			String returnStr = JSON.toJSONString(resp);
			UserInfoLogUtil.log("{\"request\":" + req.toString() + ",\"return\":" + returnStr + ", \"cost time\":\""
					+ (System.currentTimeMillis() - start) + "ms\"}");
			return returnStr;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return JSON.toJSONString(new SuggestResp(Constants.FAILURE_CODE, Constants.FAILURE_MSG));
	}

	/**
	 * 联想搜索输入高亮提示
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/v1/suggest/highlight/search", consumes = {
			"application/json" }, produces = { "application/json;charset=utf-8" })
	public String simpleFuzzyHighlightSuggestSearch(@RequestBody String reqJson) {
		try {
			long start = System.currentTimeMillis();

			SuggestReq req = JSON.parseObject(reqJson, SuggestReq.class);
			// 校验参数
			if (StringUtils.isEmpty(req.getQueryText())) {
				return JSON.toJSONString(
						new SuggestResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}
			if (ObjectUtils.isEmpty(req.getStart())) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}
			if (ObjectUtils.isEmpty(req.getSize())) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}

			// 校验长度
			if (!StringUtils.isGteLength(req.getQueryText(), 255)) {
				return JSON.toJSONString(
						new SuggestResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}

			SuggestResp resp = new SuggestResp();

			List<SuggestPo> suggestPos = searchServer.simpleFuzzyHighlightSuggestSearch(req);
			resp.setResultCode(Constants.SUCCESS_CODE);
			resp.setDescription(Constants.SUCCESS_MSG);
			resp.setSuggestList(suggestPos);

			LOGGER.debug("simple fuzzy suggest search reqJson:[{}] cost time: {}ms", reqJson,
					(System.currentTimeMillis() - start));

			String returnStr = JSON.toJSONString(resp);
			UserInfoLogUtil.log("{\"request\":" + req.toString() + ",\"return\":" + returnStr + ", \"cost time\":\""
					+ (System.currentTimeMillis() - start) + "ms\"}");
			return returnStr;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return JSON.toJSONString(new SuggestResp(Constants.FAILURE_CODE, Constants.FAILURE_MSG));
	}

	/**
	 * 多字段搜索
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/v1/multiline/search", consumes = {
			"application/json" }, produces = { "application/json;charset=utf-8" })
	public String simpleMultiLineHMatchSearch(@RequestBody String reqJson) {
		try {
			long start = System.currentTimeMillis();
			ContentReq req = JSON.parseObject(reqJson, ContentReq.class);
			// 校验参数
			if (StringUtils.isEmpty(req.queryText)) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}
			if (ObjectUtils.isEmpty(req.getStart())) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}
			if (ObjectUtils.isEmpty(req.getSize())) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}
			// 校验长度
			if (!StringUtils.isGteLength(req.getQueryText(), 255)) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}
			if (StringUtils.isNotEmpty(req.programType)) {
				if (!StringUtils.isGteLength(req.getProgramType(), 255)) {
					return JSON.toJSONString(
							new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
				}
			}
			ContentResp resp = searchServer.simpleMultiLineMatchSearch(req);
			resp.setResultCode(Constants.SUCCESS_CODE);
			resp.setDescription(Constants.SUCCESS_MSG);

			LOGGER.debug("simple multiline search reqJson:[{}] cost time: {}ms", reqJson,
					(System.currentTimeMillis() - start));
			String returnStr = JSON.toJSONString(resp, SerializerFeature.WriteMapNullValue);
			UserInfoLogUtil.log("{\"request\":" + req.toString() + ",\"return\":" + returnStr + ", \"cost time\":\""
					+ (System.currentTimeMillis() - start) + "ms\"}");

			return returnStr;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return JSON.toJSONString(new ContentResp(Constants.FAILURE_CODE, Constants.FAILURE_MSG));
	}

	/**
	 * 多字段高亮搜索
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/v1/multiline/highlight/search", consumes = {
			"application/json" }, produces = { "application/json;charset=utf-8" })
	public String simpleMultiLineHighlightMatchSearch(@RequestBody String reqJson) {
		try {
			long start = System.currentTimeMillis();
			ContentReq req = JSON.parseObject(reqJson, ContentReq.class);
			// 校验参数
			if (StringUtils.isEmpty(req.queryText)) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}
			if (ObjectUtils.isEmpty(req.getStart())) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}
			if (ObjectUtils.isEmpty(req.getSize())) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}

			// 校验长度
			if (!StringUtils.isGteLength(req.getQueryText(), 255)) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}

			if (StringUtils.isNotEmpty(req.programType)) {
				if (!StringUtils.isGteLength(req.getProgramType(), 255)) {
					return JSON.toJSONString(
							new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
				}
			}
			if (StringUtils.isNotEmpty(req.programType)) {
				if (!StringUtils.isGteLength(req.getProgramType(), 255)) {
					return JSON.toJSONString(
							new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
				}
			}

			ContentResp resp = searchServer.simpleMultiLineHighlightMatchSearch(req);
			resp.setResultCode(Constants.SUCCESS_CODE);
			resp.setDescription(Constants.SUCCESS_MSG);

			LOGGER.debug("simple multiline highlight search reqJson:[{}] cost time: {}ms", reqJson,
					(System.currentTimeMillis() - start));

			String returnStr = JSON.toJSONString(resp, SerializerFeature.WriteMapNullValue);
			UserInfoLogUtil.log("{\"request\":" + req.toString() + ",\"return\":" + returnStr + ", \"cost time\":\""
					+ (System.currentTimeMillis() - start) + "ms\"}");

			return returnStr;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return JSON.toJSONString(new ContentResp(Constants.FAILURE_CODE, Constants.FAILURE_MSG));
	}

	/**
	 * 热词排行搜索
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/v1/hotword/search", consumes = {
			"application/json" }, produces = { "application/json;charset=utf-8" })
	public String hotWordSearch(@RequestBody String reqJson) {
		try {
			long start = System.currentTimeMillis();
			HotWordReq req = JSON.parseObject(reqJson, HotWordReq.class);
			// 校验参数
			if (StringUtils.isEmpty(req.getStartTime()) || StringUtils.isEmpty(req.endTime)) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}

			HotWordResp resp = searchServer.hotWordSearch(req);
			resp.setResultCode(Constants.SUCCESS_CODE);
			resp.setDescription(Constants.SUCCESS_MSG);

			LOGGER.debug("simple hotWord search reqJson:[{}] cost time: {}ms", reqJson,
					(System.currentTimeMillis() - start));
			String returnStr = JSON.toJSONString(resp, SerializerFeature.WriteMapNullValue);
			UserInfoLogUtil.log("{\"request\":" + req.toString() + ",\"return\":" + returnStr + ", \"cost time\":\""
					+ (System.currentTimeMillis() - start) + "ms\"}");

			return returnStr;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return JSON.toJSONString(new ContentResp(Constants.FAILURE_CODE, Constants.FAILURE_MSG));
	}

	/**
	 * 热词搜索上报
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/v1/hotword/report", consumes = {
			"application/json" }, produces = { "application/json;charset=utf-8" })
	public String hotWordReport(@RequestBody String reqJson) {
		try {
			long start = System.currentTimeMillis();
			HotWordReportReq req = JSON.parseObject(reqJson, HotWordReportReq.class);
			// 校验参数
			if (StringUtils.isEmpty(req.name) || StringUtils.isEmpty(req.searchTime)
					|| StringUtils.isEmpty(req.searchWord) || StringUtils.isEmpty(req.contentId)) {
				return JSON.toJSONString(
						new ContentResp(Constants.INVALID_ARGUMENTS_CODE, Constants.INVALID_ARGUMENTS_MSG));
			}

			boolean resp = searchServer.hotWordReport(req);
			LOGGER.debug("report search result:[{}] cost time: {}ms", resp, (System.currentTimeMillis() - start));
			if (resp) {
				return JSON.toJSONString(new ContentResp(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG));
			} else {
				return JSON.toJSONString(new ContentResp(Constants.FAILURE_CODE, Constants.FAILURE_MSG));
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return JSON.toJSONString(new ContentResp(Constants.FAILURE_CODE, Constants.FAILURE_MSG));
	}
}
