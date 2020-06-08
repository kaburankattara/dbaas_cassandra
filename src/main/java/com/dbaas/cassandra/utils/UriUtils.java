package com.dbaas.cassandra.utils;

/**
 * UriUtils
 */
public final class UriUtils {

	/**
	 * コンストラクタ
	 */
    private UriUtils() {
    }

	/**
	 * リダイレクト用URI作成
	 *
	 * @param relativeUriStr 相対パスURI
	 * @return リダイレクト用URI
	 */
	public static String createRedirectUri(String relativeUriStr) {
		return "redirect:" + relativeUriStr;
	}
}
