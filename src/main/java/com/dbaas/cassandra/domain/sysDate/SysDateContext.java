package com.dbaas.cassandra.domain.sysDate;

import static com.dbaas.cassandra.utils.DateUtils.formatYyyymmdd;
import static com.dbaas.cassandra.utils.DateUtils.formatYyyymmddhhmmssSSSSSS;

import java.time.LocalDateTime;

/**
 * システム日付用コンテキスト
 */
public final class SysDateContext {
	/**
	 * スレッドローカルによるシステム日付
	 */
	private static ThreadLocal<LocalDateTime> sysDateThreadLocal = new ThreadLocal<>();

	/**
	 * コンストラクタ
	 */
	private SysDateContext() {
	}

	/**
	 * システム日付の取得
	 * @return システム日付
	 */
    public static LocalDateTime getSysDate() {
        return sysDateThreadLocal.get();
    }

	/**
	 * システム日付をyyyyMMdd形式の文字列で取得
	 * @return システム日付
	 */
    public static String getSysDateYyyyMMdd() {
        return formatYyyymmdd(sysDateThreadLocal.get());
    }

	/**
	 * システム日付をYyyymmddhhmmssSSSSSS形式の文字列で取得
	 * @return システム日付
	 */
    public static String getSysDateYyyymmddhhmmssSSSSSS() {
        return formatYyyymmddhhmmssSSSSSS(sysDateThreadLocal.get());
    }

    /**
     * システム日付を設定
     * @param sysDate システム日付
     */
    public static void setSysDate(LocalDateTime sysDate) {
    	sysDateThreadLocal.set(sysDate);
    }

    /**
     * システム日付の削除
     */
    public static void removeSysDate() {
    	sysDateThreadLocal.remove();
    }
}
