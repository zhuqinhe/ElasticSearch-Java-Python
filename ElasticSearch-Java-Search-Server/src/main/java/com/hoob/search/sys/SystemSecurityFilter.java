package com.hoob.search.sys;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoob.search.common.Constants;
import com.hoob.search.common.StatusCode;
import com.hoob.search.common.vo.Response;
import com.hoob.search.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import com.hoob.search.utils.JsonUtils;
import com.hoob.search.utils.OsCheck;
import com.hoob.search.utils.StringUtils;


@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/rest/*", filterName = "security")
public class SystemSecurityFilter implements Filter {

	private Logger LOG = LoggerFactory.getLogger(SystemSecurityFilter.class);
	private List<String> whiteList = new ArrayList<String>();
	private Map<String, Long> fileModifyTimeMap = new HashMap<>();
	private static Properties proConfig = new Properties();
	private static String fileName;

	/**
	 * 支持正则
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		fileName = Constants.LIUNX_SYSTEM_FILE_NAME;

		File file = new File(fileName);
		try {
			Properties p = FileUtils.getResource(file);
			if (null != p) {
				proConfig = p;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		whiteList.add("/rest/v1/login");
		PropertiesConfigThread configThread = new PropertiesConfigThread();
		configThread.start();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		// 白名单

		String uri = req.getRequestURI().replaceFirst(req.getContextPath(), "");
		Iterator<String> ite = whiteList.iterator();
		while (ite.hasNext()) {
			String patternString = ite.next();
			// 正则规则
			Pattern pattern = Pattern.compile(patternString);
			// 被校验的字符串
			Matcher match = pattern.matcher(uri);
			if (match.matches()) {
				chain.doFilter(req, resp);
				return;
			}
		}

		// CommonsMultipartResolver multipartResolver = new
		// CommonsMultipartResolver(request.getServletContext());
		// if(multipartResolver.isMultipart(req)) chain.doFilter(req,
		// resp);//temp

		BodyReaderHttpServletRequestWrapper warpper = new BodyReaderHttpServletRequestWrapper(req);
		String body = warpper.getRequestBody();
		boolean check = getBoolean("CHECK_HMAC", false);
		if (check) {
			// Token校验
			// String[] params = token.split("\\:");//
			// userName:lastLoginTime:signature
			// if (null == params || params.length != 3) { // Token不合法
			// log.error("token is illega.");
			// close(resp, StatusCode.UI.UI_20001);
			// return;
			// }

			// HMAC校验
			if (!checkHMAC(req, body)) {
				LOG.error("hmac is error.");
				close(resp, StatusCode.UI.UI_40001);
				return;
			}

			// String userName = params[0]; // 接口请求用户
			// String lastLoginTime = params[1]; // 用户登录时间
			// String signature = params[2];// 平台颁发的数字证书
			// if (null == userName || null == lastLoginTime || null ==
			// signature) {
			// log.error("login info is error.");
			// close(resp, StatusCode.UI.UI_20008);
			// return;
			// }
			// 获取登录会话

			// UserSession session =
			// SessionManager.SESSION_REPOSITORY.get(token);
			// if (null == session) {
			// log.error("session is null.");
			// close(resp, StatusCode.UI.UI_20001);
			// return;
			// }
			// String password = session.getPassword();
			// // 校验签名
			// boolean valid = SecurityUtils.validateSignature(userName,
			// Long.parseLong(lastLoginTime), signature, password);
			// if (!valid) {
			// log.error("signature is error.");
			// close(resp, StatusCode.UI.UI_20001);
			// return;
			// }

			// 校验接口是否有授权
			// valid = this.checkIfPrivilege(session, uri);
			// if (!valid) {
			// close(resp, StatusCode.UI.UI_20005);
			// return;
			// }

			// session.setLastActiveTime(new Date()); // 更新会话的最新活跃时间
			// 检查是否登录
			String token = req.getHeader("Token");
			if (null == token) {
				LOG.error("token is null.");
				close(resp, StatusCode.UI.UI_40001);
				return;
			}
		}

		chain.doFilter(warpper, response);
	}

	@Override
	public void destroy() {

	}

	/**
	 * 校验接口授权
	 *
	 * @param session
	 * @param currentUri
	 * @return
	 */
	private boolean checkIfPrivilege(UserSession session, String currentUri) {
		if (null == session || null == session.getIfPrivileges() || null == currentUri) {
			return false;
		}
		for (String privilege : session.getIfPrivileges()) {
			if (null == privilege) {
				continue;
			}
			Pattern pattern = Pattern.compile(privilege);
			Matcher matcher = pattern.matcher(currentUri);
			if (matcher.matches()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * HMAC校验（防篡改）
	 *
	 * @param request
	 * @param requestBody
	 * @return
	 */
	private boolean checkHMAC(HttpServletRequest request, String requestBody) {

		if (null == request) {
			return false;
		}
		if (null == requestBody) {
			requestBody = "";
		}
		String clientHmac = request.getHeader("Hmac");
		if (null == clientHmac || clientHmac.trim().isEmpty()) {
			return true; // 认为不开启好了
		}

		String token = request.getHeader("Token");
		String terminalType = request.getHeader("Terminal-Type");
		String deviceId = request.getHeader("Device-Id");
		String timestamp = request.getHeader("Timestamp");
		String timezone = request.getHeader("Timezone-Offset");
		String random = request.getHeader("Random");
		String serviceId = request.getHeader("Service-Id");
		LOG.debug("hmac request:{}", request.toString());
		StringBuilder strb = new StringBuilder();
		strb.append("{\"Header\":{\"Token\":\"").append(token).append("\",").append("\"Terminal-Type\":\"")
				.append(terminalType).append("\",").append("\"Device-Id\":\"").append(deviceId).append("\",")
				.append("\"TS1\":\"").append(timestamp).append("\",").append("\"TSZ\":\"").append(timezone)
				.append("\",").append("\"RND\":\"").append(random).append("\",").append("\"Service-Id\":\"")
				.append(serviceId).append("\"}");
		strb.append(",");
		if (null == requestBody || requestBody.trim().isEmpty()) {
			strb.append("\"Body\":").append("{}");
		} else {
			LOG.debug("hmac body:{}", requestBody);
			strb.append("\"Body\":").append(requestBody).append("");
		}
		strb.append("}");
		// KEY 文件头加密 生成HMAC
		String key = null;
		try {
			key = SecurityUtils.createMD5(SecurityUtils.createMD5(token).toUpperCase() + token.substring(5, 10))
					.toUpperCase();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

		// 服务端产生hmac
		String hmac = SecurityUtils.calculateHMAC(strb.toString(), key);
		LOG.debug("serverHmac[{}]", hmac);
		return clientHmac.equals(hmac);
	}

	/**
	 * 拦截请求，反馈异常
	 *
	 * @param response
	 * @param resultCode
	 */
	private void close(HttpServletResponse response, int resultCode) {
		Response respVO = new Response();
		respVO.setResultCode(resultCode);
		PrintWriter pw;
		try {
			response.setStatus(200);
			response.setContentType("application/json");
			pw = response.getWriter();
			pw.write(JsonUtils.obj2Json(respVO));
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

	private class PropertiesConfigThread extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					File file = new File(fileName);
					Long lastModifyTime = file.lastModified();
					Long oldLastModifyTime = fileModifyTimeMap.get(fileName);
					if (oldLastModifyTime == null || lastModifyTime > oldLastModifyTime) {
						Properties p = FileUtils.getResource(file);
						if (null != p) {
							proConfig = p;
							fileModifyTimeMap.put(fileName, lastModifyTime);
							LOG.info("[{}] update success", fileName);
						}
					}
				} catch (Exception e) {
					LOG.error("load search.properties error:", e);
				}
				//
				try {
					Thread.sleep(30000L);
				} catch (Exception e) {
					LOG.error("propertiesConfigThread sleep throws exception:", e);
				}
			}
		}

	}

	public static String getString(String key, String defaultValue) {
		String value = null;
		if (null != proConfig) {
			value = proConfig.getProperty(key);
		}
		if (StringUtils.paramIsNull(value)) {
			return defaultValue;
		}
		return value.trim();
	}

	public static Boolean getBoolean(String key, Boolean defaultValue) {
		String value = null;
		if (null != proConfig) {
			value = proConfig.getProperty(key);
		}
		if (StringUtils.paramIsNull(value)) {
			return defaultValue;
		}
		return Boolean.valueOf(value);
	}
}
