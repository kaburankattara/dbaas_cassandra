package com.dbaas.cassandra.utils;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * ThreadUtils
 */
public final class ThreadUtils {

	/**
	 * コンストラクタ
	 */
    private ThreadUtils() {
    }

	/**
	 * sleep処理
	 */
	public static void sleep() {
		try {
			int sleepTime = 1000;
			MILLISECONDS.sleep(sleepTime);
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
	}
}
