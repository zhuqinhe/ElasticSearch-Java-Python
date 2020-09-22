package com.hoob.search.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.hoob.search.common.Constants;
import com.hoob.search.model.ProgramType;
import com.hoob.search.model.SuggestIndex;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hoob.search.dao.SearchDao;
import com.hoob.search.es.client.EsRestHighLevelClient;
import com.hoob.search.model.HotWordRowMapper;
import com.hoob.search.po.CastPo;
import com.hoob.search.po.ContentPo;
import com.hoob.search.po.ContentResp;
import com.hoob.search.po.HotWordPo;
import com.hoob.search.po.HotWordResp;
import com.hoob.search.po.SuggestPo;
import com.hoob.search.redis.utils.StringUtil;
import com.hoob.search.server.ProConfigService;
import com.hoob.search.server.RedisServer;
import com.hoob.search.utils.ObjectUtils;
import com.hoob.search.utils.StringUtils;
import com.hoob.search.vo.ContentReq;
import com.hoob.search.vo.HotWordReq;
import com.hoob.search.vo.OrderFile;
import com.hoob.search.vo.SuggestReq;

@Repository("searchDao")
public class SearchDaoImpl implements SearchDao {
	// private static final Logger logger =
	// LoggerFactory.getLogger(SearchDaoImpl.class);

	/**
	 * 参数配置类
	 */
	@Autowired
	private ProConfigService proConfigService;

	@Autowired
	private RedisServer redisServer;

	@Resource
	JdbcTemplate jdbcTemplate;

	@Override
	public List<SuggestPo> simpleFuzzySuggestSearch(SuggestReq req) throws Exception {
		List<SuggestPo> result = new ArrayList<SuggestPo>();

		String suggestIndexAlias = proConfigService.getString("suggest_index_alias", "ott_website_suggest");

		String cacheKey = StringUtils.printVal("suggest", req.getFileName(), req.getQueryText(),
				req.getStart().toString(), req.getSize().toString(), req.getTerminal());

		// 缓存获取
		String cacheValue = redisServer.getString(cacheKey);

		if (!StringUtils.isEmpty(cacheValue)) {
			List<SuggestPo> cacheSuggestResp = JSON.parseArray(cacheValue, SuggestPo.class);
			return cacheSuggestResp;
		}

		if (StringUtil.isEmpty(req.getFileName())) {
			String filename = "name,namekeyword";
			req.setFileName(filename);

		}

		if (StringUtils.isNotEmpty(req.getFileName())) {
			String filename = req.getFileName();

			BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(filename, req);

			SearchRequest searchRequest = new SearchRequest(suggestIndexAlias);

			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

			searchSourceBuilder.query(boolQueryBuilder).from(req.getStart()).size(req.getSize())
					.sort("_score", SortOrder.DESC).sort("createDate", SortOrder.DESC);

			searchRequest.source(searchSourceBuilder);

			SearchResponse searchResponse = EsRestHighLevelClient.getSingleClient().search(searchRequest,
					RequestOptions.DEFAULT);

			for (SearchHit searchHit : searchResponse.getHits().getHits()) {
				String json = searchHit.getSourceAsString();
				SuggestIndex suggest = JSON.parseObject(json, SuggestIndex.class);
				result.add(new SuggestPo(suggest.getName()));
			}
			// 写入缓存
			if (ObjectUtils.isNotEmpty(result)) {
				redisServer.setString(cacheKey, JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
			}

		}
		return result;

	}

	@Override
	public List<SuggestPo> simpleFuzzyHighlightSuggestSearch(SuggestReq req) throws Exception {
		List<SuggestPo> result = new ArrayList<SuggestPo>();

		String suggestIndexAlias = proConfigService.getString("suggest_index_alias", "ott_website_suggest");

		String cacheKey = StringUtils.printVal("highligth_suggest", req.getFileName(), req.getQueryText(),
				req.getStart().toString(), req.getSize().toString(), req.getTerminal());

		// 缓存获取
		String cacheValue = redisServer.getString(cacheKey);

		if (!StringUtils.isEmpty(cacheValue)) {
			List<SuggestPo> cacheSuggestResp = JSON.parseArray(cacheValue, SuggestPo.class);
			return cacheSuggestResp;
		}

		HighlightBuilder highlightBuilder = new HighlightBuilder();

		highlightBuilder.preTags(
				proConfigService.getString("elasticsearch_search_highlight_pre_tag", "<span style=\"color:red\">"));
		highlightBuilder.postTags(proConfigService.getString("elasticsearch_search_highlight_post_tag", "</span>"));

		if (StringUtil.isEmpty(req.getFileName())) {
			String filename = "name,namekeyword";
			req.setFileName(filename);

		}

		HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("*");
		highlightBuilder.field(highlightTitle);

		if (StringUtils.isNotEmpty(req.getFileName())) {
			String filename = req.getFileName();

			BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(filename, req);

			SearchRequest searchRequest = new SearchRequest(suggestIndexAlias);

			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

			searchSourceBuilder.query(boolQueryBuilder).from(req.getStart()).size(req.getSize())
					.sort("_score", SortOrder.DESC).sort("createDate", SortOrder.DESC).highlighter(highlightBuilder);

			searchRequest.source(searchSourceBuilder);

			SearchResponse searchResponse = EsRestHighLevelClient.getSingleClient().search(searchRequest,
					RequestOptions.DEFAULT);

			result = getSuggestHighlightFieldResp(searchResponse);

			// 写入缓存
			if (ObjectUtils.isNotEmpty(result)) {
				redisServer.setString(cacheKey, JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
			}

		}
		return result;
	}

	@Override
	public ContentResp simpleMultiLineHighlightMatchSearch(ContentReq req) throws Exception {
		List<ContentPo> reslist = new ArrayList<>();
		List<ProgramType> programType = new ArrayList<>();
		ContentResp contentResp = new ContentResp();

		SearchResponse searchResponse = null;
		BoolQueryBuilder boolQueryBuilder = null;

		String cacheKey = null;
		String cacheValue = null;

		HighlightBuilder highlightBuilder = new HighlightBuilder();

		highlightBuilder.preTags(
				proConfigService.getString("elasticsearch_search_highlight_pre_tag", "<span style=\"color:red\">"));
		highlightBuilder.postTags(proConfigService.getString("elasticsearch_search_highlight_post_tag", "</span>"));

		if (StringUtil.isEmpty(req.getFileName())) {
			String filename = proConfigService.getString("elasticsearch_search_file",
					"name,tags,actors,directors,namekeyword");

			if (Constants.ANDROID_CODE.equals(req.getTerminal())) {
				filename = proConfigService.getString("elasticsearch_search_android_file",
						"name,tags,actors,directors,namekeyword");
			}

			if (Constants.IOS_CODE.equals(req.getTerminal())) {
				filename = proConfigService.getString("elasticsearch_search_ios_file",
						"name,tags,actors,directors,namekeyword");
			}
			req.setFileName(filename);
		}

		if (!StringUtil.isEmpty(req.getFileName())) {

			HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("*");
			highlightBuilder.field(highlightTitle);
			// 获取缓存数据
			cacheKey = StringUtils.printVal("highlight", req.getFileName(), req.getQueryText(),
					req.getStart().toString(), req.getSize().toString(), req.getTerminal());

			cacheValue = redisServer.getString(cacheKey);

			if (!StringUtils.isEmpty(cacheValue)) {
				ContentResp cacheContentResp = JSON.parseObject(cacheValue, ContentResp.class);
				return cacheContentResp;
			}

			if (StringUtils.isNotEmpty(req.programType)) {
				boolQueryBuilder = QueryBuilders.boolQuery();
				boolQueryBuilder.must(QueryBuilders.termQuery("programType", req.programType));
				boolQueryBuilder.must(getBoolQueryBuilder(req.getFileName(), req));
			} else {
				boolQueryBuilder = getBoolQueryBuilder(req.getFileName(), req);
			}

			// 过滤栏目类型
			String excludePromgramType = proConfigService.getString("search_exclude_program_type", null);

			if (ObjectUtils.isNotEmpty(excludePromgramType)) {
				String[] excludePromgram = excludePromgramType.split(",");

				for (String type : excludePromgram) {
					boolQueryBuilder.mustNot(QueryBuilders.termQuery("programType", type));
				}
			}

			// 判断上下架状态
			if (proConfigService.getBoolean("elasticsearch_search_status_switch", true)) {
				boolQueryBuilder.mustNot(QueryBuilders.termQuery("status", "8"));
			}

			// 判断排序，ES中查询
			if (proConfigService.getBoolean("elasticsearch_search_order_switch", false)) {

				SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

				searchSourceBuilder.query(boolQueryBuilder).from(req.getStart()).size(req.getSize())
						.aggregation(AggregationBuilders.terms("programType").field("programType")
								.size(Integer.parseInt(proConfigService.getString("search_program_type_size", "20"))))
						.highlighter(highlightBuilder);
				SearchRequest searchRequest = new SearchRequest(
						proConfigService.getString("elasticsearch_search_index_alias", "produce_collection"));
				searchRequest.source(searchSourceBuilder);

				if (ObjectUtils.isNotEmpty(req.orderFile)) {
					for (OrderFile orderfile : req.orderFile) {
						if (StringUtils.isNotEmpty(orderfile.file)) {
							if (StringUtils.isNotEmpty(orderfile.type)) {
								if (orderfile.type.equalsIgnoreCase("ASC")) {
									searchSourceBuilder.sort(orderfile.file, SortOrder.ASC);
								} else {
									searchSourceBuilder.sort(orderfile.file, SortOrder.DESC);
								}
							} else {
								searchSourceBuilder.sort(orderfile.file, SortOrder.DESC);
							}
						}
					}

				} else {

					// 获取排序字段，设置排序
					String[] orderFiles = proConfigService.getString("elasticsearch_search_order_flie", "_score")
							.split(",");

					for (String orderFile : orderFiles) {
						searchSourceBuilder.sort(orderFile, SortOrder.DESC);
					}

				}
				searchResponse = EsRestHighLevelClient.getSingleClient().search(searchRequest, RequestOptions.DEFAULT);

			} else {

				SearchRequest searchRequest = new SearchRequest(
						proConfigService.getString("elasticsearch_search_index_alias", "produce_collection"));

				SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

				searchSourceBuilder.query(boolQueryBuilder).from(req.getStart()).size(req.getSize())
						.aggregation(AggregationBuilders.terms("programType").field("programType")
								.size(Integer.parseInt(proConfigService.getString("search_program_type_size", "20"))));

				searchRequest.source(searchSourceBuilder);

				searchResponse = EsRestHighLevelClient.getSingleClient().search(searchRequest, RequestOptions.DEFAULT);

			}

			long total = searchResponse.getHits().getTotalHits().value;

			reslist = getHighlightFieldResp(searchResponse);
			Terms terms = searchResponse.getAggregations().get("programType");

			for (Bucket b : terms.getBuckets()) {
				programType.add(new ProgramType(b.getKey().toString(), b.getDocCount()));
			}

			contentResp.setTotal(total);
			contentResp.setProgramType(programType);
			contentResp.setContenList(reslist);

			SearchRequest searchRequest = new SearchRequest(
					proConfigService.getString("cast_index_alias", "produce_collection"));

			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

			searchSourceBuilder
					.query(QueryBuilders.boolQuery()
							.should(QueryBuilders.termQuery("name", req.getQueryText().toLowerCase())))
					.from(req.getStart()).size(req.getSize())
					.aggregation(AggregationBuilders.terms("programType").field("programType")
							.size(Integer.parseInt(proConfigService.getString("search_program_type_size", "20"))))
					.highlighter(highlightBuilder);

			searchRequest.source(searchSourceBuilder);

			SearchResponse searchCastResponse = EsRestHighLevelClient.getSingleClient().search(searchRequest,
					RequestOptions.DEFAULT);

			List<CastPo> castList = getCastHighlightFieldResp(searchCastResponse);
			contentResp.setCastList(castList);
		}
		if (ObjectUtils.isNotEmpty(contentResp)) {
			redisServer.setString(cacheKey, JSON.toJSONString(contentResp, SerializerFeature.WriteMapNullValue));
		}
		return contentResp;

	}

	@Override
	public ContentResp simpleMultiLineMatchSearch(ContentReq req) throws Exception {

		List<ContentPo> reslist = new ArrayList<>();
		ContentResp contentResp = new ContentResp();
		List<ProgramType> programType = new ArrayList<>();

		SearchResponse searchResponse = null;
		BoolQueryBuilder boolQueryBuilder = null;
		String cacheKey = null;
		String cacheValue = null;

		if (StringUtil.isEmpty(req.getFileName())) {
			String filename = proConfigService.getString("elasticsearch_search_box_file",
					"name,tags,actors,directors,namekeyword");

			if (Constants.ANDROID_CODE.equals(req.getTerminal())) {
				filename = proConfigService.getString("elasticsearch_search_android_file",
						"name,tags,actors,directors,namekeyword");
			}

			if (Constants.IOS_CODE.equals(req.getTerminal())) {
				filename = proConfigService.getString("elasticsearch_search_ios_file",
						"name,tags,actors,directors,namekeyword");
			}
			req.setFileName(filename);
		}

		if (!StringUtil.isEmpty(req.getFileName())) {

			cacheKey = StringUtils.printVal("search", req.getFileName(), req.getQueryText(), req.getStart().toString(),
					req.getSize().toString(), req.getTerminal());
			// 获取缓存数据
			cacheValue = redisServer.getString(cacheKey);

			if (!StringUtils.isEmpty(cacheValue)) {
				ContentResp cacheContentResp = JSON.parseObject(cacheValue, ContentResp.class);
				return cacheContentResp;
			}

			if (StringUtils.isNotEmpty(req.programType)) {
				boolQueryBuilder = QueryBuilders.boolQuery();
				boolQueryBuilder.must(QueryBuilders.termQuery("programType", req.programType));
				boolQueryBuilder.must(getBoolQueryBuilder(req.getFileName(), req));
			} else {
				boolQueryBuilder = getBoolQueryBuilder(req.getFileName(), req);
			}

			// 过滤栏目类型
			String excludePromgramType = proConfigService.getString("search_exclude_program_type", null);

			if (ObjectUtils.isNotEmpty(excludePromgramType)) {
				String[] excludePromgram = excludePromgramType.split(",");

				for (String type : excludePromgram) {
					boolQueryBuilder.mustNot(QueryBuilders.termQuery("programType", type));
				}
			}

			// 判断上下线
			if (proConfigService.getBoolean("elasticsearch_search_status_switch", true)) {
				boolQueryBuilder.mustNot(QueryBuilders.termQuery("status", "8"));
			}

			if (proConfigService.getBoolean("elasticsearch_search_order_switch", false)) {

				SearchRequest searchRequest = new SearchRequest(
						proConfigService.getString("elasticsearch_search_index_alias", "produce_collection"));

				SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

				searchSourceBuilder.query(boolQueryBuilder).from(req.getStart()).size(req.getSize())
						.aggregation(AggregationBuilders.terms("programType").field("programType")
								.size(Integer.parseInt(proConfigService.getString("search_program_type_size", "20"))));

				// 前端传递排序
				if (ObjectUtils.isNotEmpty(req.orderFile)) {
					for (OrderFile orderfile : req.orderFile) {
						if (StringUtils.isNotEmpty(orderfile.file)) {
							if (StringUtils.isNotEmpty(orderfile.type)) {
								if (orderfile.type.equalsIgnoreCase("ASC")) {
									searchSourceBuilder.sort(orderfile.file, SortOrder.ASC);
								} else {
									searchSourceBuilder.sort(orderfile.file, SortOrder.DESC);
								}
							} else {
								searchSourceBuilder.sort(orderfile.file, SortOrder.DESC);
							}
						}
					}

				} else {

					// 获取排序字段，设置排序
					String[] orderFiles = proConfigService.getString("elasticsearch_search_order_flie", "_score")
							.split(",");

					for (String orderFile : orderFiles) {
						searchSourceBuilder.sort(orderFile, SortOrder.DESC);
					}

				}

				searchRequest.source(searchSourceBuilder);

				searchResponse = EsRestHighLevelClient.getSingleClient().search(searchRequest, RequestOptions.DEFAULT);

			} else {

				SearchRequest searchRequest = new SearchRequest(
						proConfigService.getString("elasticsearch_search_index_alias", "produce_collection"));

				SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

				searchSourceBuilder.query(boolQueryBuilder).from(req.getStart()).size(req.getSize())
						.aggregation(AggregationBuilders.terms("programType").field("programType").size(100));

				searchRequest.source(searchSourceBuilder);

				searchResponse = EsRestHighLevelClient.getSingleClient().search(searchRequest, RequestOptions.DEFAULT);

			}

			long total = searchResponse.getHits().getTotalHits().value;

			for (SearchHit searchHit : searchResponse.getHits().getHits()) {
				String json = searchHit.getSourceAsString();
				ContentPo contentPo = JSON.parseObject(json, ContentPo.class);
				reslist.add(contentPo);
			}

			Terms terms = searchResponse.getAggregations().get("programType");

			for (Bucket b : terms.getBuckets()) {
				programType.add(new ProgramType(b.getKey().toString(), b.getDocCount()));
			}
			contentResp.setTotal(total);
			contentResp.setContenList(reslist);
			contentResp.setProgramType(programType);
			SearchRequest searchRequest = new SearchRequest(
					proConfigService.getString("cast_index_alias", "produce_collection"));

			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

			searchSourceBuilder.query(boolQueryBuilder).from(req.getStart()).size(req.getSize())
					.aggregation(AggregationBuilders.terms("programType").field("programType").size(100));

			searchRequest.source(searchSourceBuilder);

			SearchResponse searchCastResponse = EsRestHighLevelClient.getSingleClient().search(searchRequest,
					RequestOptions.DEFAULT);

			List<CastPo> castList = getCastHighlightFieldResp(searchCastResponse);

			contentResp.setCastList(castList);
		}
		// 设置缓存
		if (ObjectUtils.isNotEmpty(contentResp)) {
			redisServer.setString(cacheKey, JSON.toJSONString(contentResp, SerializerFeature.WriteMapNullValue));
		}
		return contentResp;
	}

	public BoolQueryBuilder getBoolQueryBuilder(String filename, ContentReq req) {
		String[] files = filename.split(",");
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		String[] fileNameStrs = new String[files.length];
		String[] fileNameNgramStrs = new String[files.length * 2];
		Map<String, Float> boost = new HashMap<>();

		for (int x = 0; x < files.length; x++) {
			if (files[x].contains("^")) {
				fileNameStrs[x] = files[x].split("\\^")[0];

				fileNameNgramStrs[x] = files[x].split("\\^")[0];
				fileNameNgramStrs[files.length + x] = files[x].split("\\^")[0] + ".ngram";
				boost.put(files[x].split("\\^")[0], Float.parseFloat(files[x].split("\\^")[1]));
			} else {
				fileNameStrs[x] = files[x];

				fileNameNgramStrs[x] = files[x];
				fileNameNgramStrs[files.length + x] = files[x] + ".ngram";

			}
		}

		if (StringUtils.judgeContainsStr(req.getQueryText())) {

			for (int x = 0; x < fileNameStrs.length; x++) {

				if (fileNameStrs[x].contains("keyword")) {
					boolQueryBuilder
							.should(QueryBuilders.termQuery(files[x] + ".keyword", req.getQueryText()).boost(20))
							.should(QueryBuilders
									.matchPhraseQuery(fileNameStrs[x] + ".keyword", req.getQueryText().toLowerCase())
									.boost(10).analyzer("keyword"))
							.should(QueryBuilders
									.matchPhraseQuery(files[x] + ".ngram_index", req.getQueryText().toLowerCase())
									.slop(0).boost(10).analyzer("keyword"))
							.should(QueryBuilders
									.matchPhrasePrefixQuery(files[x] + ".ngram_index", req.getQueryText().toLowerCase())
									.slop(Integer
											.parseInt(proConfigService.getString("elasticsearch_search_slop", "3")))
									.boost(5).maxExpansions(0).analyzer("keyword"));
				} else {
					boolQueryBuilder.should(
							QueryBuilders.matchPhraseQuery(files[x] + ".pinyin", req.getQueryText().toLowerCase())
									.analyzer("pinyin").boost(10));
					boolQueryBuilder
							.should(QueryBuilders.matchQuery(files[x] + ".standard", req.getQueryText().toLowerCase())
									.analyzer("standard").boost(10).fuzziness(Fuzziness.AUTO));
					boolQueryBuilder.should(QueryBuilders.matchPhraseQuery(files[x], req.getQueryText().toLowerCase())
							.analyzer("keyword").boost(10));
				}
			}
			boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("name.suggest", req.getQueryText().toLowerCase())
					.analyzer("keyword").boost(10));

		} else {
			boolQueryBuilder.should(QueryBuilders.multiMatchQuery(req.getQueryText(), fileNameNgramStrs).fields(boost)
					.tieBreaker((float) 0.3).analyzer("keyword")
					.type(proConfigService.getString("elasticsearch_minimum_search_type", "best_fields"))
					.minimumShouldMatch(proConfigService.getString("elasticsearch_minimum_should_match", "100%")));
		}

		return boolQueryBuilder;
	}

	public BoolQueryBuilder getBoolQueryBuilderMust(String filename, ContentReq req) {
		String[] files = filename.split(",");
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		String[] fileNameStrs = new String[files.length];
		String[] fileNameNgramStrs = new String[files.length * 2];
		Map<String, Float> boost = new HashMap<>();

		for (int x = 0; x < files.length; x++) {
			if (files[x].contains("^")) {
				fileNameStrs[x] = files[x].split("\\^")[0];

				fileNameNgramStrs[x] = files[x].split("\\^")[0];
				fileNameNgramStrs[files.length + x] = files[x].split("\\^")[0] + ".ngram";
				boost.put(files[x].split("\\^")[0], Float.parseFloat(files[x].split("\\^")[1]));
			} else {
				fileNameStrs[x] = files[x];

				fileNameNgramStrs[x] = files[x];
				fileNameNgramStrs[files.length + x] = files[x] + ".ngram";

			}
		}

		if (StringUtils.judgeContainsStr(req.getQueryText())) {

			for (int x = 0; x < fileNameStrs.length; x++) {

				if (fileNameStrs[x].contains("keyword")) {
					boolQueryBuilder.must(QueryBuilders.termQuery(files[x] + ".keyword", req.getQueryText()).boost(20))
							.must(QueryBuilders
									.matchPhraseQuery(fileNameStrs[x] + ".keyword", req.getQueryText().toLowerCase())
									.boost(10).analyzer("keyword"))
							.must(QueryBuilders
									.matchPhraseQuery(files[x] + ".ngram_index", req.getQueryText().toLowerCase())
									.slop(0).boost(10).analyzer("keyword"))
							.must(QueryBuilders
									.matchPhrasePrefixQuery(files[x] + ".ngram_index", req.getQueryText().toLowerCase())
									.slop(Integer
											.parseInt(proConfigService.getString("elasticsearch_search_slop", "3")))
									.boost(5).maxExpansions(0).analyzer("keyword"));
				} else {
					boolQueryBuilder
							.must(QueryBuilders.matchPhraseQuery(files[x] + ".pinyin", req.getQueryText().toLowerCase())
									.analyzer("pinyin").boost(10));
					boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(files[x], req.getQueryText().toLowerCase())
							.analyzer("keyword").boost(10));
				}
			}
			boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("name.suggest", req.getQueryText().toLowerCase())
					.analyzer("keyword").boost(10));

		} else {
			boolQueryBuilder.must(QueryBuilders.multiMatchQuery(req.getQueryText(), fileNameNgramStrs).fields(boost)
					.tieBreaker((float) 0.3).analyzer("keyword")
					.type(proConfigService.getString("elasticsearch_minimum_search_type", "best_fields"))
					.minimumShouldMatch(proConfigService.getString("elasticsearch_minimum_should_match", "100%")));
		}

		return boolQueryBuilder;
	}

	public BoolQueryBuilder getBoolQueryBuilder(String filename, SuggestReq req) {
		String[] files = filename.split(",");
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		String[] fileNameStrs = new String[files.length];
		String[] fileNameNgramStrs = new String[files.length * 2];
		Map<String, Float> boost = new HashMap<>();
		// 获取搜索列
		for (int x = 0; x < files.length; x++) {
			if (files[x].contains("^")) {
				fileNameStrs[x] = files[x].split("\\^")[0];
				fileNameNgramStrs[x] = files[x].split("\\^")[0];
				fileNameNgramStrs[files.length + x] = files[x].split("\\^")[0] + ".ngram_index";
				boost.put(files[x].split("\\^")[0], Float.parseFloat(files[x].split("\\^")[1]));
			} else {
				fileNameStrs[x] = files[x];
				fileNameNgramStrs[x] = files[x];
				fileNameNgramStrs[files.length + x] = files[x] + ".ngram_index";
			}
		}

		// 包含字母
		if (StringUtils.judgeContainsStr(req.getQueryText())) {

			for (int x = 0; x < fileNameStrs.length; x++) {

				if (fileNameStrs[x].contains("keyword")) {
					boolQueryBuilder
							.should(QueryBuilders.termQuery(files[x] + ".keyword", req.getQueryText()).boost(20))
							.should(QueryBuilders
									.matchPhraseQuery(fileNameStrs[x] + ".keyword", req.getQueryText().toLowerCase())
									.boost(10).analyzer("keyword"))
							.should(QueryBuilders
									.matchPhraseQuery(files[x] + ".ngram_index", req.getQueryText().toLowerCase())
									.slop(0).boost(10).analyzer("keyword"))
							.should(QueryBuilders
									.matchPhrasePrefixQuery(files[x] + ".ngram_index", req.getQueryText().toLowerCase())
									.slop(Integer
											.parseInt(proConfigService.getString("elasticsearch_search_slop", "3")))
									.boost(5).maxExpansions(0).analyzer("keyword"));
				} else {
					boolQueryBuilder.should(
							QueryBuilders.matchPhraseQuery(files[x] + ".pinyin", req.getQueryText().toLowerCase())
									.analyzer("pinyin").boost(10));
					boolQueryBuilder.should(QueryBuilders.matchPhraseQuery(files[x] + ".suggest", req.getQueryText())
							.analyzer("keyword").boost(10));
				}
			}

		} else {
			boolQueryBuilder
					.should(QueryBuilders.multiMatchQuery(req.getQueryText(), fileNameNgramStrs).fields(boost)
							.type("best_fields").tieBreaker((float) 0.3).analyzer("keyword"))
					.should(QueryBuilders.matchPhrasePrefixQuery("name.pinyin", req.getQueryText()).analyzer("pinyin")
							.boost((float) 0.8).maxExpansions(0))
					.should(QueryBuilders.matchPhrasePrefixQuery("name", req.getQueryText()).analyzer("ik_smart")
							.boost((float) 0.8).maxExpansions(0));
		}

		return boolQueryBuilder;
	}

	public List<SuggestPo> getSuggestHighlightFieldResp(SearchResponse searchResponse) {
		List<SuggestPo> reslist = new ArrayList<>();
		for (SearchHit searchHit : searchResponse.getHits().getHits()) {
			String json = searchHit.getSourceAsString();
			SuggestIndex suggest = JSON.parseObject(json, SuggestIndex.class);
			HighlightField name = searchHit.getHighlightFields().get("name");
			if (name != null) {
				Text[] text = name.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				suggest.setName(title);
			}

			HighlightField nameEnglish = searchHit.getHighlightFields().get("name.english");
			if (nameEnglish != null) {
				Text[] text = nameEnglish.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				suggest.setName(title);
			}
			HighlightField nameSuggest = searchHit.getHighlightFields().get("name.suggest");
			if (nameSuggest != null) {
				Text[] text = nameSuggest.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				suggest.setName(title);
			}
			reslist.add(new SuggestPo(suggest.getName()));
		}

		return reslist;
	}

	public List<ContentPo> getHighlightFieldResp(SearchResponse searchResponse) {
		List<ContentPo> reslist = new ArrayList<>();
		for (SearchHit searchHit : searchResponse.getHits().getHits()) {
			String json = searchHit.getSourceAsString();
			ContentPo contentPo = JSON.parseObject(json, ContentPo.class);

			HighlightField namestandard = searchHit.getHighlightFields().get("name.standard");
			if (namestandard != null) {
				Text[] text = namestandard.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				contentPo.setName(title);
			}

			HighlightField name = searchHit.getHighlightFields().get("name");
			if (name != null) {
				Text[] text = name.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				contentPo.setName(title);
			}

			HighlightField nameSuggest = searchHit.getHighlightFields().get("name.suggest");
			if (nameSuggest != null) {
				Text[] text = nameSuggest.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				contentPo.setName(title);
			}

			HighlightField actors = searchHit.getHighlightFields().get("actors");
			if (actors != null) {
				Text[] text = actors.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				contentPo.setActors(title);
			}

			HighlightField actorsStandard = searchHit.getHighlightFields().get("actors.standard");
			if (actorsStandard != null) {
				Text[] text = actorsStandard.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				contentPo.setActors(title);
			}

			HighlightField tagsStandard = searchHit.getHighlightFields().get("tags.standard");
			if (tagsStandard != null) {
				Text[] text = tagsStandard.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				contentPo.setTags(title);
			}

			HighlightField tags = searchHit.getHighlightFields().get("tags");
			if (tags != null) {
				Text[] text = tags.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				contentPo.setTags(title);
			}

			HighlightField directorsStandard = searchHit.getHighlightFields().get("directors.standard");
			if (directorsStandard != null) {
				Text[] text = directorsStandard.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				contentPo.setDirectors(title);
			}

			HighlightField directors = searchHit.getHighlightFields().get("directors");
			if (directors != null) {
				Text[] text = directors.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				contentPo.setDirectors(title);
			}

			HighlightField kindStandard = searchHit.getHighlightFields().get("kind.standard");
			if (kindStandard != null) {
				Text[] text = kindStandard.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				contentPo.setKind(title);
			}

			HighlightField kind = searchHit.getHighlightFields().get("kind");
			if (kind != null) {
				Text[] text = kind.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				contentPo.setKind(title);
			}

			reslist.add(contentPo);
		}
		return reslist;
	}

	public List<CastPo> getCastHighlightFieldResp(SearchResponse searchResponse) {
		List<CastPo> reslist = new ArrayList<>();
		for (SearchHit searchHit : searchResponse.getHits().getHits()) {
			String json = searchHit.getSourceAsString();
			CastPo castPo = JSON.parseObject(json, CastPo.class);
			HighlightField name = searchHit.getHighlightFields().get("name");
			if (name != null) {
				Text[] text = name.getFragments();
				String title = "";
				for (Text str : text) {
					title += str.string();
				}
				castPo.setName(title);
			}

			reslist.add(castPo);
		}
		return reslist;
	}

	@Override
	public HotWordResp hotWordSearch(HotWordReq req) throws Exception {
		String cacheKey = StringUtils.printVal("hotword", req.getStartTime(), req.getEndTime(),
				req.getStart().toString(), req.getSize().toString());

		String cacheValue = redisServer.getString(cacheKey);

		if (!StringUtils.isEmpty(cacheValue)) {
			HotWordResp cacheContentResp = JSON.parseObject(cacheValue, HotWordResp.class);
			return cacheContentResp;
		}

		HotWordResp resp = new HotWordResp();
		List<HotWordPo> hotwordList = new ArrayList<HotWordPo>();
		StringBuffer sql = new StringBuffer();
		ArrayList<Object> param = new ArrayList<Object>();
		sql.append(
				" SELECT A.words,A.Counts,A.contentId,B.PosterPicUrl,B.DetailPicUrl,B.TotalNum,B.UpdateNum,B.ProgramType ");
		sql.append(" FROM search_hot_words A LEFT JOIN ");
		sql.append(
				" ( SELECT contentId,SeriesId,ProgramType,PosterPicUrl,DetailPicUrl,TotalNum,Status,UpdateNum FROM series ) B  ");
		sql.append(" ON B.contentId = A.contentId WHERE 1=1 and B.Status != 8 ");

		// 过滤栏目类型
		String excludePromgramType = proConfigService.getString("search_exclude_program_type", null);

		if (ObjectUtils.isNotEmpty(excludePromgramType)) {
			String[] excludePromgram = excludePromgramType.split(",");

			for (String type : excludePromgram) {
				sql.append(" and B.ProgramType != ? ");
				param.add(type);
			}
		}

		if (StringUtils.isNotEmpty(req.getStartTime())) {
			sql.append(" and A.searchDate >= ? ");
			param.add(req.getStartTime());
		}
		if (StringUtils.isNotEmpty(req.getEndTime())) {
			sql.append(" and A.searchDate < ? ");
			param.add(req.getEndTime());
		}

		sql.append(" ORDER BY A.Counts DESC ");
		sql.append(" limit ?,? ");
		param.add(req.getStart());
		param.add(req.getSize());

		hotwordList = this.jdbcTemplate.query(sql.toString(), param.toArray(), new HotWordRowMapper());

		if (hotwordList.size() < req.getSize()) {
			StringBuffer appendSql = new StringBuffer();
			ArrayList<Object> appendParam = new ArrayList<Object>();
			List<HotWordPo> appendHotWordList = new ArrayList<HotWordPo>();
			appendSql.append("SELECT name as words,Counts,contentId,SeriesId,ProgramType,PosterPicUrl,"
					+ "DetailPicUrl, TotalNum, UpdateNum FROM series where 1=1 and Status != 8 ");
			// 过滤栏目类型
			if (ObjectUtils.isNotEmpty(excludePromgramType)) {
				String[] excludePromgram = excludePromgramType.split(",");

				for (String type : excludePromgram) {
					appendSql.append(" and ProgramType != ? ");
					appendParam.add(type);
				}
			}

			if (hotwordList.size() == 1) {
				appendSql.append(" and contentId not in ('" + hotwordList.get(0).contentId + "') ");
				appendSql.append(" order by Counts limit ?");
				appendParam.add(req.getSize() - hotwordList.size());
				appendHotWordList = this.jdbcTemplate.query(appendSql.toString(), appendParam.toArray(),
						new HotWordRowMapper());
			} else {
				List<String> comtentIds = new ArrayList<String>();
				for (HotWordPo hotword : hotwordList) {
					comtentIds.add(hotword.contentId);
				}

				appendSql.append(" and contentId not in ('" + StringUtils.printValComma(comtentIds) + "') ");
				appendSql.append(" order by Counts limit ?");
				appendParam.add(req.getSize() - hotwordList.size());
				appendHotWordList = this.jdbcTemplate.query(appendSql.toString(), appendParam.toArray(),
						new HotWordRowMapper());
			}
			for (HotWordPo appendHotWord : appendHotWordList) {
				hotwordList.add(appendHotWord);
			}

			hotwordList.sort((min, max) -> max.getCount() - min.getCount());
		}

		resp.setHotwordList(hotwordList);

		if (ObjectUtils.isNotEmpty(resp)) {
			redisServer.setString(cacheKey, JSON.toJSONString(resp, SerializerFeature.WriteMapNullValue));
		}

		return resp;
	}

}
