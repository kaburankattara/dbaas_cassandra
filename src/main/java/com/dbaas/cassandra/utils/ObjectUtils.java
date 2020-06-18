package com.dbaas.cassandra.utils;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * ObjectUtils
 */
@Component("ObjectUtils")
public final class ObjectUtils {

	/**
	 * コンストラクタ
	 */
	private ObjectUtils() {
	}

	/**
	 * 空チェック
	 * @param array リストオブジェクト
	 * @return 判定結果
	 */
	public static boolean isEmpty(@Nullable Object[] array) {
		return org.springframework.util.ObjectUtils.isEmpty(array);
	}

	/**
	 * 空チェック 反転
	 * @param array リストオブジェクト
	 * @return 判定結果
	 */
	public static boolean isNotEmpty(@Nullable Object[] array) {
		return !ObjectUtils.isEmpty(array);
	}

	/**
	 * 空チェック
	 * @param obj オブジェクト
	 * @return 判定結果
	 */
	public static boolean isEmpty(@Nullable Object obj) {
		return ObjectUtils.isEmpty(obj);
	}

	/**
	 * 空チェック 反転
	 * @param obj オブジェクト
	 * @return 判定結果
	 */
	public static boolean isNotEmpty(@Nullable Object obj) {
		return !ObjectUtils.isEmpty(obj);
	}
}
