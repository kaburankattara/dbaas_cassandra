package com.dbaas.cassandra.utils;

import static com.dbaas.cassandra.utils.StringUtils.isEmpty;
import static com.dbaas.cassandra.utils.StringUtils.isNotBlank;
import static com.dbaas.cassandra.utils.StringUtils.leftPad;
import static com.dbaas.cassandra.utils.StringUtils.substring;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;


/**
 * DateUtils
 */
@Component("DateUtils")
public final class DateUtils {

	/**
	 * コンストラクタ
	 */
    private DateUtils() {
    }

    /**
	 * 年月の桁位　日付から年月を切り取る用
	 */
	private static final int YYYY = 4;

    /**
	 * 年月の桁位　日付から年月を切り取る用
	 */
	private static final int MM = 6;

	/**
	 * 日に桁位 日付から日を切り取る用
	 */
	private static final int DD = 8;

    /**
     * 月加減算区分
     */
    private static final int DATE_KBN_MONTH = 1;

    /**
     * 日加減算区分
     */
    private static final int DATE_KBN_DAY = 2;

    /**
     * 年加減算区分
     */
    private static final int DATE_KBN_YEAR = 3;
    
    /** 日付フォーマット　yyyyMMdd */
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    /** 日付フォーマット　yyyyMMddHHmmss.SSSSSS */
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS_SSSSSS = "yyyyMMddHHmmss.SSSSSS";

	/**
	 * 引数で指定された日付の月末日を取得する。
	 * @param kijunYmd 基準日
	 * @return 月末日
	 */
	public static String getYmdEndOfMonth(String kijunYmd) {
		if (kijunYmd == null || kijunYmd.length() == 0 || !isDateFormat(kijunYmd)) {
			return null;
		}

		String year  = getYear(kijunYmd);
		String month = getMonth(kijunYmd);

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		return year + month + lastDay;
	}

	/**
	 * 引数で指定された日付の年月を取得する。
	 * @param kijunYmd 基準日
	 * @return 年月
	 */
	public static String getYearMonth(String kijunYmd) {
		if (isEmpty(kijunYmd) || !isDateFormat(kijunYmd)) {
			return null;
		}
		return substring(kijunYmd, 0, MM);
	}

	/**
	 * 引数で指定された日付の年を取得する。
	 * @param kijunYmd 基準日
	 * @return 年
	 */
	public static String getYear(String kijunYmd) {
		if (isEmpty(kijunYmd) || !isDateFormat(kijunYmd)) {
			return null;
		}
		return substring(kijunYmd, 0, YYYY);
	}

	/**
	 * 引数で指定された日付の月を取得する。
	 * @param kijunYmd 基準日
	 * @return 月
	 */
	public static String getMonth(String kijunYmd) {
		if (isEmpty(kijunYmd) || !isDateFormat(kijunYmd)) {
			return null;
		}
		return substring(kijunYmd, YYYY, MM);
	}

	/**
	 * 引数で指定された日付の日を取得する。
	 * @param kijunYmd 基準日
	 * @return 日
	 */
	public static String getDay(String kijunYmd) {
		if (isEmpty(kijunYmd) || !isDateFormat(kijunYmd)) {
			return null;
		}
		return substring(kijunYmd, MM, DD);
	}

    /**
     * 日単位加減算日付取得
     * @param kijunYmd 日付
     * @param days 数値（加算する日数数）
     * @return 加減算日付
     */
    public static String addDays(String kijunYmd, int days) {
        return addDate(kijunYmd, days, DATE_KBN_DAY);
    }

	/**
	 * 月単位加減算日付取得
	 * @param kijunYmd 日付
	 * @param months 数値（加算する月数）
	 * @return 加減算日付
	 */
	public static String addMonths(String kijunYmd, int months) {
        return addDate(kijunYmd, months, DATE_KBN_MONTH);
    }

    /**
     * 年単位加減算日付取得
     * @param kijunYmd 日付
     * @param years 数値（加算する年数）
     * @return 加減算日付
     */
    public static String addYears(String kijunYmd, int years) {
        return addDate(kijunYmd, years, DATE_KBN_YEAR);
    }

    /**
     * 加減算日付取得
     * @param kijunYmd 日付
     * @param addVal 数値（加算する値）
     * @param dateKbn 日付区分
     * @return 加減算日付
     */
    private static String addDate(String kijunYmd, int addVal, int dateKbn) {

        //必須項目の値が未設定　かつ　日付として正しくない場合 NULLを返却する。
        if (isEmpty(kijunYmd) || !isDateFormat(kijunYmd)) {
            return null;
        }

		//引数である日付から整数型として年、月、日を分離する。
		int year  = Integer.parseInt(getYear(kijunYmd));
		int month = Integer.parseInt(getMonth(kijunYmd));
		int day   = Integer.parseInt(getDay(kijunYmd));

		//JavaのAPI（Calendarクラスのメソッド）を使用して、月単位に加減算した年月日を求める。
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, day);

        switch(dateKbn) {
        case DATE_KBN_MONTH:
            cal.add(Calendar.MONTH, addVal);
            break;
        case DATE_KBN_DAY:
            cal.add(Calendar.DATE, addVal);
            break;
        case DATE_KBN_YEAR:
            cal.add(Calendar.YEAR, addVal);
            break;
        }

		String calYear = String.format("%04d", cal.get(Calendar.YEAR));
		String calMonth = String.format("%02d", cal.get(Calendar.MONTH) + 1);
		String calDay = String.format("%02d", cal.get(Calendar.DATE));

		//年＋月＋日をyyyyMMdd形式の文字列として返却する。
		return calYear + calMonth + calDay;
	}

	/**
	 * 日付チェック
	 * @param str チェック文字列
	 * @return チェック結果
	 */
	public static boolean isDateFormat(String str) {
		return isDate(str, DATE_FORMAT_YYYYMMDD);

	}

	/**
	 * 日付チェック(スラッシュ指定)
	 * @param str チェック文字列
	 * @return チェック結果
	 */
	public static boolean isDateFormatBySlash(String str) {
		return isDate(str, "yyyy/MM/dd");
	}

	/**
	 * 日付チェック(yyMMdd)
	 * @param str チェック文字列
	 * @return チェック結果
	 */
	public static boolean isDateFormatByYymmdd(String str) {
		return isDate(str, "yyMMdd");
	}

	/**
	 * 日付チェック(ハイフン指定)
	 * @param str チェック文字列
	 * @return チェック結果
	 */
	public static boolean isDateFormatByHyphenate(String str) {
		return isDate(str, "yyyy-MM-dd");

	}

	/**
	 * ｙｙｙｙMMｄｄ形式の文字列をSlash区切りにする
	 *
	 * @param yyyymmdd
	 *            年月日
	 * @return 区切版年月日
	 */
	public static String slashYyyymmdd(String yyyymmdd) {
		return separateYyyymmdd(yyyymmdd, "/");
	}

	/**
	 * ｙｙｙｙMMｄｄ形式の文字列をYYYY年MM月DD日にする
	 *
	 * @param yyyymmdd
	 *            年月日
	 * @return 区切版年月日
	 */
	public static String japaneseYyyymmdd(String yyyymmdd) {
		return isNotBlank(yyyymmdd) && isDate(yyyymmdd, DATE_FORMAT_YYYYMMDD)
				? getYear(yyyymmdd) + "年" + getMonth(yyyymmdd) + "月" + getDay(yyyymmdd) + "日"
				: yyyymmdd;
	}

	/**
	 * ｙｙｙｙMMｄｄ形式の文字列をハイフン区切りにする
	 *
	 * @param yyyymmdd
	 *            年月日
	 * @return 区切版年月日
	 */
	public static String hyphenateYyyymmdd(String yyyymmdd) {
		return separateYyyymmdd(yyyymmdd, "-");
	}

	/**
	 * ｙｙｙｙMMｄｄ形式の文字列を指定セパレータ区切りにする
	 *
	 * @param yyyymmdd
	 *            年月日
	 * @param sep
	 *            セパレータ
	 * @return 区切版年月日
	 */
	public static String separateYyyymmdd(String yyyymmdd, String sep) {
		return isNotBlank(yyyymmdd) && isDate(yyyymmdd, DATE_FORMAT_YYYYMMDD)
				? getYear(yyyymmdd) + sep + getMonth(yyyymmdd) + sep + getDay(yyyymmdd)
				: yyyymmdd;
	}

	/**
	 * 日付（LocalDateTime）をyyyyMMdd形式の文字列に変換する
	 * @param date 日付
	 * @return yyyyMMdd
	 */
	public static String formatYyyymmdd(LocalDateTime date) {
		return DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD).format(date);
	}

    /**
     * 日付（LocalDateTime）をyyyyMMddHHmmss形式の文字列に変換する
     * @param date 日付
     * @return yyyyMMddHHmmss
     */
    public static String formatYyyymmddhhmmss(LocalDateTime date) {
        return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(date);
    }

    /**
     * 日付（LocalDateTime）をyyyyMMddHHmmss.SSSSSS形式の文字列に変換する
     * @param date 日付
     * @return yyyyMMddHHmmss.SSSSSS
     */
    public static String formatYyyymmddhhmmssSSSSSS(LocalDateTime date) {
        return DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSSSSS").format(date);
    }

    /**
     * 日付（LocalDateTime）をHHmmssSSSS形式の文字列に変換する
     * @param date 日付
     * @return HHmmss.SSS
     */
    public static String formatHhmmssSSS(LocalDateTime date) {
        return DateTimeFormatter.ofPattern("HHmmssSSS").format(date);
    }

	/**
	 * 日付（LocalDateTime）をyyyy/MM/dd形式の文字列に変換する
	 * @param date 日付
	 * @return yyyy/MM/dd
	 */
	public static String formatSlashYyyymmdd(LocalDateTime date) {
		return DateTimeFormatter.ofPattern("yyyy/MM/dd").format(date);
	}

	/**
	 * 日付（LocalDateTime）をyy/MM/dd形式の文字列に変換する
	 * @param date 日付
	 * @return yy/MM/dd
	 */
	public static String formatSlashYymmdd(LocalDateTime date) {
		return DateTimeFormatter.ofPattern("yy/MM/dd").format(date);
	}

	/**
	 * 日付（LocalDateTime）をM/d形式の文字列に変換する
	 * @param date 日付
	 * @return yy/MM/dd
	 */
	public static String formatSlashMd(LocalDateTime date) {
		return DateTimeFormatter.ofPattern("M/d").format(date);
	}

	/**
	 * 日付（LocalDateTime）をyyyy-MM-dd形式の文字列に変換する
	 * @param date 日付
	 * @return yyyy-MM-dd
	 */
	public static String formatHyphenateYyyymmdd(LocalDateTime date) {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date);
	}

	/**
	 * 日付（LocalDateTime）をyyyy/MM/dd HH:mm:ss形式の文字列に変換する
	 * @param date 日付
	 * @return yyyy/MM/dd HH:mm:ss
	 */
	public static String formatSlashYyyymmddHms(LocalDateTime date) {
		return DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(date);
	}

	/**
	 * 日付（LocalDateTime）をyyyy/MM/dd HH:mm:ss.SSS形式の文字列に変換する
	 * @param date 日付
	 * @return yyyy/MM/dd HH:mm:ss.SSS
	 */
	public static String formatSlashYyyymmddTHmsS(LocalDateTime date) {
		return DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS").format(date);
	}

	/**
	 * 日付（String yyyyMMdd形式)をString(yyyy/MM/dd)に変換する
	 * @param date 日付
	 * @return yyyy/MM/dd
	 */
	public static String formatYyyymmddToSlash(String date) {
		return formatSlashYyyymmdd(formatYyyymmdd(date, DATE_FORMAT_YYYYMMDD));
	}

	/**
	 * 日付（String yyyyMMdd形式)をString(yy/MM/dd)に変換する
	 * @param date 日付
	 * @return yy/MM/dd
	 */
	public static String formatYymmddToSlash(String date) {
		return formatSlashYymmdd(formatYyyymmdd(date, DATE_FORMAT_YYYYMMDD));
	}

	/**
	 * 日付（String yyMMdd形式)をString(yyyyMMdd)に変換する
	 * @param date 日付
	 * @return yyyyMMdd
	 */
	public static String formatYymmddToYyyymmdd(String date) {
		return formatYyyymmdd(formatYyyymmdd(date, "yyMMdd"));
	}

	/**
	 * 日付（String yyyyMMdd形式)をString(M/d)に変換する
	 * @param date 日付
	 * @return M/d
	 */
	public static String formatMdToSlash(String date) {
		return formatSlashMd(formatYyyymmdd(date, DATE_FORMAT_YYYYMMDD));
	}

	/**
	 * 日付（String yyyyMMdd形式)をString(yyyy-MM-dd)に変換する
	 * @param date 日付
	 * @return yyyy-MM-dd
	 */
	public static String formatYyyymmddToHyphenate(String date) {
		return formatHyphenateYyyymmdd(formatYyyymmdd(date, DATE_FORMAT_YYYYMMDD));
	}

	/**
	 * 日付（String yyyy/MM/dd形式)をString(yyyyMMdd)に変換する
	 * @param date 日付
	 * @return yyyyMMdd
	 */
	public static String formatSlashToYyyymmdd(String date) {
		return formatYyyymmdd(formatYyyymmdd(date, "yyyy/MM/dd"));
	}

	/**
	 * 日付（String yyyy/MM/dd形式)をString(yyyy-MM-dd)に変換する
	 * @param date 日付
	 * @return yyyy-MM-dd
	 */
	public static String formatSlashToHyphenate(String date) {
		return formatHyphenateYyyymmdd(formatYyyymmdd(date, "yyyy/MM/dd"));
	}

	/**
	 * 日付（String yyyy-MM-dd形式)をString(yyyyMMdd)に変換する
	 * @param date 日付
	 * @return yyyyMMdd
	 */
	public static String formatHyphenateToYyyymmdd(String date) {
		return formatYyyymmdd(formatYyyymmdd(date, "yyyy-MM-dd"));
	}

	/**
	 * 日付（String yyyy-MM-dd形式)をString(yyyy/MM/dd)に変換する
	 * @param date 日付
	 * @return yyyy/MM/dd
	 */
	public static String formatHyphenateToSlash(String date) {
		return formatSlashYyyymmdd(formatYyyymmdd(date, "yyyy-MM-dd"));
	}

	/**
	 * 日付（String yyyy/MM/dd HH:mm:ss形式)をString(yyyyMMdd)に変換する
	 * @param date 日付
	 * @return yyyyMMdd
	 */
	public static String formatYyyymmddHmsToYyyymmdd(String date) {
		return formatYyyymmdd(formatYyyymmdd(date, "yyyy/MM/dd HH:mm:ss"));
	}

	/**
	 * 日付（String yyyy/MM/dd HH:mm:ss形式)をString(yyyy/MM/dd)に変換する
	 * @param date 日付
	 * @return yyyy/MM/dd
	 */
	public static String formatYyyymmddHmsToSlash(String date) {
		return formatSlashYyyymmdd(formatYyyymmdd(date, "yyyy/MM/dd HH:mm:ss"));
	}

	/**
	 * 日付（String yyyy/MM/dd HH:mm:ss形式)をString(yyyy-MM-dd)に変換する
	 * @param date 日付
	 * @return yyyy-MM-dd
	 */
	public static String formatYyyymmddHmsToHyphenate(String date) {
		return formatHyphenateYyyymmdd(formatYyyymmdd(date, "yyyy/MM/dd HH:mm:ss"));
	}

	/**
	 * 日付（String yyyy/MM/ddTHH:mm:ss.SSS形式)をString(yyyyMMdd)に変換する
	 * @param date 日付
	 * @return yyyyMMdd
	 */
	public static String formatYyyymmddTHmsSToYyyymmdd(String date) {
		return formatYyyymmdd(formatYyyymmdd(date, "yyyy/MM/dd'T'HH:mm:ss"));
	}

	/**
	 * 日付（String yyyy/MM/ddTHH:mm:ss.SSS形式)をString(yyyy/MM/dd)に変換する
	 * @param date 日付
	 * @return yyyy/MM/dd
	 */
	public static String formatYyyymmddTHmsSToSlash(String date) {
		return formatSlashYyyymmdd(formatYyyymmdd(date, "yyyy/MM/dd'T'HH:mm:ss"));
	}

	/**
	 * 日付（String yyyy/MM/ddTHH:mm:ss.SSS形式)をString(yyyy-MM-dd)に変換する
	 * @param date 日付
	 * @return yyyy-MM-dd
	 */
	public static String formatYyyymmddTHmsSToHyphenate(String date) {
		return formatHyphenateYyyymmdd(formatYyyymmdd(date, "yyyy/MM/dd'T'HH:mm:ss"));
	}

	/**
	 * 日付（String yyyy/MM/ddTHH:mm:ss.SSS形式)をString(yyyy/MM/dd HH:mm:ss)に変換する
	 * @param date 日付
	 * @return yyyy/MM/dd HH:mm:ss
	 */
	public static String formatYyyymmddTHmsSToYyyymmddHms(String date) {
		return formatSlashYyyymmddHms(formatYyyymmdd(date, "yyyy/MM/dd'T'HH:mm:ss"));
	}

	/**
	 * 日付（LocalDateTime）をyyyy/MM形式の文字列に変換する
	 * @param date 日付
	 * @return yyyy/MM
	 */
	public static String formatSlashYyyymm(LocalDateTime date) {
		return DateTimeFormatter.ofPattern("yyyy/MM").format(date);
	}

	/**
	 * yyyyMMdd形式の文字列をyyyy年MM月dd日にする
	 *
	 * @param yyyymmdd
	 *            年月
	 * @return 区切版年月日
	 */
	public static String formatYyyymmddToJp(String yyyymmdd) {
		return getYear(yyyymmdd) + "年" + getMonth(yyyymmdd) + "月" + getDay(yyyymmdd) + "日";
	}

	/**
	 * yyyyMM形式の文字列をyyyy年MM月にする
	 *
	 * @param yyyymm
	 *            年月
	 * @return 区切版年月
	 */
	public static String formatYyyymmToJp(String yyyymm) {
		String yyyymmdd = yyyymm + "01";
		return getYear(yyyymmdd) + "年" + getMonth(yyyymmdd) + "月";
	}

	/**
	 * 日付（String yyyyMMdd形式)をString(MM/dd)に変換する
	 * @param date 日付
	 * @return MM/dd
	 */
	public static String formatMmddToSlash(String date) {
		return DateTimeFormatter.ofPattern("MM/dd").format(formatYyyymmdd(date, DATE_FORMAT_YYYYMMDD));
	}

	/**
	 * 日付（String yyyyMMdd形式)をString(dd MMM(英語) yyyy)に変換する
	 * @param date 日付
	 * @return MM/dd
	 */
	public static String formatYyyymmddToEn(String date) {
		return DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH).format(formatYyyymmdd(date, DATE_FORMAT_YYYYMMDD));
	}

	/**
	 * 日付（String）を書式指定したLocalDateTimeに変換する
	 * @param date 日付
	 * @param format フォーマット
	 * @return LocalDateTime(書式指定)
	 */
	private static LocalDateTime formatYyyymmdd(String date, String format) {
		if (date.length() > "yyyy/MM/dd".length()) {
			LocalDateTime ldt = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(format));
			return ldt;
		} else {
			LocalDate ld = LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
			LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.of(0, 0, 0, 0));
			return ldt;
		}
	}

	/**
	 * 月、日の数値を文字列変換（2桁で0埋めした文字列）
	 * @param val 月、または日の数値
	 * @return 変換後の値
	 */
	public static String leftPadMDZero(short val) {
		return leftPad(String.valueOf(val), 2, '0');
	}

	/**
	 * 引数1の日付が引数2の日付以降か判定
	 *
	 * @param yyyymmdd1引数1
	 *            引数1
	 * @param yyyymmdd2
	 *            引数2
	 * @return 判定結果
	 */
	public static boolean isEqual(String yyyymmdd1, String yyyymmdd2) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD);
		LocalDate date1 = LocalDate.parse(yyyymmdd1, dtf);
		LocalDate date2 = LocalDate.parse(yyyymmdd2, dtf);
		return date1.isEqual(date2);
	}

	/**
	 * 引数1の日付が引数2の日付より後か判定（同日NG）
	 *
	 * @param yyyymmdd1引数1
	 *            引数1
	 * @param yyyymmdd2
	 *            引数2
	 * @return 判定結果
	 */
	public static boolean isAfter(String yyyymmdd1, String yyyymmdd2) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD);
		LocalDate date1 = LocalDate.parse(yyyymmdd1, dtf);
		LocalDate date2 = LocalDate.parse(yyyymmdd2, dtf);
		return date1.isAfter(date2);
	}

	/**
	 * 引数1の日付が引数2の日付以降か判定（同日OK）
	 *
	 * @param yyyymmdd1引数1
	 *            引数1
	 * @param yyyymmdd2
	 *            引数2
	 * @return 判定結果
	 */
	public static boolean isLaterthan(String yyyymmdd1, String yyyymmdd2) {
		return isEqual(yyyymmdd1, yyyymmdd2) || isAfter(yyyymmdd1, yyyymmdd2);
	}

	/**
	 * 日付チェック
	 *
	 * @param str チェック文字列
	 * @return チェック結果
	 */
	public static boolean isDate(String str) {
		return isDate(str, "yyyy/MM/dd") || isDate(str, DATE_FORMAT_YYYYMMDD);
	}

	/**
	 * 日付チェック(yyyyMMddHHmmss.SSSSSS)
	 *
	 * @param str チェック文字列
	 * @return チェック結果
	 */
	public static boolean isDateByYyyymmddhhmmssSSSSSS(String str) {
		// isDate()は秒単位での比較は出来ないため、フォーマット変換の可否で判断する
		// ※isDate()だと文字列への再パースで厳密な時間が変わってしまう
		try {
			formatYyyymmdd(str, DATE_FORMAT_YYYYMMDDHHMMSS_SSSSSS);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	/**
	 * 日付チェック(フォーマット指定)
	 *
	 * @param str チェック文字列
	 * @param format フォーマット
	 * @return チェック結果
	 */
	public static boolean isDate(String str, String format) {
		try {
			Date parsed = org.apache.commons.lang3.time.DateUtils.parseDate(str, new String[] {format});
			String formated = org.apache.commons.lang3.time.DateFormatUtils.format(parsed, format);
			return str.equals(formated);
		} catch (ParseException e) {
			return false;
		}
	}
}
