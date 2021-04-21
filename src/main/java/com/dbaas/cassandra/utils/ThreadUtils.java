package com.dbaas.cassandra.utils;

import com.dbaas.cassandra.shared.applicationProperties.ApplicationProperties;
import com.dbaas.cassandra.shared.exception.SystemException;

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
		ApplicationProperties ap = ApplicationProperties.createInstance();
		try {
			int sleepTime = ap.getSleepTime();
			MILLISECONDS.sleep(sleepTime);
		} catch (InterruptedException e) {
			throw new SystemException();
		}
	}
}
