package com.dbaas.cassandra.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

/**
 * HttpUtils
 *
 */
@Component("HttpUtils")
public final class HttpUtils {

	/**
	 * 内部コンストラクタ
	 */
	private HttpUtils() {

	}

	/**
	 * refererを取得
	 * 
	 * @param request
	 * @return referer
	 */
	public static String getReferer(HttpServletRequest request) {
		return request.getHeader("referer");
	}
}
