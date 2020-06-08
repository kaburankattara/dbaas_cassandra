package com.dbaas.cassandra.domain.sysDate;

import java.io.Serializable;

import com.dbaas.cassandra.utils.DateUtils;

/**
 * システム日付設定情報
 */
public class SysDate implements Serializable {
	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * デフォルトコンストラクタ
	 */
	public SysDate() {
	}

	/**
	 * デフォルトコンストラクタ
	 */
	public SysDate(String conf) {
		this.year = DateUtils.getYear(conf);
		this.month = DateUtils.getMonth(conf);
		this.day = DateUtils.getDay(conf);
	}

    /**
     * 年
     */
    private String year;

    /**
     * 月
     */
    private String month;

    /**
     * 日
     */
    private String day;

    /**
     * 年を取得
     *
     * @return 年
     */
    public String getYear() {
        return year;
    }

    /**
     * 年を設定
     *
     * @param year 年
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * 月を取得
     *
     * @return 月
     */
    public String getMonth() {
        return month;
    }

    /**
     * 月を設定
     *
     * @param month 月
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * 日を取得
     *
     * @return 日
     */
    public String getDay() {
        return day;
    }

    /**
     * 日を設定
     *
     * @param day 日
     */
    public void setDay(String day) {
        this.day = day;
    }
}
