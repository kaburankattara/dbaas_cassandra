package com.dbaas.cassandra.utils;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * ListUtils
 *
 */
@Component("ListUtils")
public final class ListUtils {

	/**
	 * 内部コンストラクタ
	 */
	private ListUtils() {

	}

	/**
	 * 配列からリストに変換する
	 * 
	 * @param array
	 * @return リスト
	 */
	public static List<Object> toList(Object[] array) {
		return new ArrayList<Object>(asList(array));
	}
}
