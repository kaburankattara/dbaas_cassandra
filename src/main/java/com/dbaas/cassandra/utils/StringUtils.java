package com.dbaas.cassandra.utils;

import org.apache.commons.lang3.RegExUtils;

/**
 * StringUtils
 */
public final class StringUtils {

	/**
	 * コンストラクタ
	 */
	private StringUtils() {
	}

	/**
	 * null、空チェック
	 * @param cs 文字列
	 * @return 判定結果
	 */
    public static boolean isEmpty(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isEmpty(cs);
    }

    /**
     * null、空チェック 反転
     * @param cs 文字列
     * @return 判定結果
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isNotEmpty(cs);
    }

    /**
     * 比較
     * @param cs1 文字列1
     * @param cs2 文字列2
     * @return 判定結果
     */
    public static boolean isEquals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == null && cs2 == null) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        return cs1.equals(cs2);
    }

    /**
     * 比較 反転
     * @param cs1 文字列1
     * @param cs2 文字列2
     * @return 判定結果
     */
    public static boolean isNotEquals(final CharSequence cs1, final CharSequence cs2) {
        return !isEquals(cs1, cs2);
    }

	/**
	 * String型の大小比較 数値1が数値2より小さいか判定
	 *
	 * @param string1
	 *            文字列1
	 * @param string2
	 *            文字列2
	 * @return 判定結果
	 */
	public static boolean isSmallerString(String string1, String string2) {
		if (string1 == null || string2 == null) {
			return false;
		}
		return string1.compareTo(string2) < 0;
	}

	/**
	 * String型の大小比較 数値1が数値2より大きいか判定
	 *
	 * @param string1
	 *            文字列1
	 * @param string2
	 *            文字列2
	 * @return 判定結果
	 */
	public static boolean isBiggerString(String string1, String string2) {
		if (string1 == null || string2 == null) {
			return false;
		}
		return string1.compareTo(string2) > 0;
	}

    /**
     * 空チェック
     * @param cs 文字列
     * @return 判定結果
     */
    public static boolean isBlank(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isBlank(cs);
    }

    /**
     * 空チェック
     * @param cs 文字列
     * @return 判定結果
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(cs);
    }

    /**
     * 空白除去
     * @param str 文字列
     * @return 処理結果
     */
    public static String trim(final String str) {
        return org.apache.commons.lang3.StringUtils.trim(str);
    }

    /**
     * 文字分割
     * @param str 文字列
     * @param start 開始位置
     * @return 処理結果
     */
    public static String substring(final String str, int start) {
        return org.apache.commons.lang3.StringUtils.substring(str, start);
    }

    /**
     * 文字分割
     * @param str 文字列
     * @param start 開始位置
     * @param end 終了位置
     * @return 処理結果
     */
    public static String substring(final String str, int start, int end) {
    	return org.apache.commons.lang3.StringUtils.substring(str, start, end);
    }

	/**
	 * 最終文字を取得
	 * @param str 文字列
	 * @return 処理結果
	 */
	public static String getEndChar(final String str) {
		return isEmpty(str) ? str : substring(str, str.length() - 1);
	}

    /**
     * 置換
     * @param text 文字列
     * @param regex 置換前文字列(正規表現可)
     * @param replacement 置換後文字列
     * @return 処理結果
     */
    public static String replaceAll(final String text, final String regex, final String replacement) {
		return RegExUtils.replaceAll(text, regex, replacement);
    }

    /**
     * 文字右埋め
     * @param str 文字列
     * @param size 処理後サイズ
     * @return 処理結果
     */
    public static String rightPad(final String str, final int size) {
        return org.apache.commons.lang3.StringUtils.rightPad(str, size, ' ');
    }

    /**
     * 文字右埋め
     * @param str 文字列
     * @param size 処理後サイズ
     * @param padChar 埋め文字
     * @return 処理結果
     */
    public static String rightPad(final String str, final int size, final char padChar) {
        return org.apache.commons.lang3.StringUtils.rightPad(str, size, padChar);
    }

    /**
     * 文字右埋め
     * @param str 文字列
     * @param size 処理後サイズ
     * @param padStr 埋め文字
     * @return 処理結果
     */
    public static String rightPad(final String str, final int size, String padStr) {
        return org.apache.commons.lang3.StringUtils.rightPad(str, size, padStr);
    }

    /**
     * 文字左埋め
     * @param str 文字列
     * @param size 処理後サイズ
     * @return 処理結果
     */
    public static String leftPad(final String str, final int size) {
        return org.apache.commons.lang3.StringUtils.leftPad(str, size, ' ');
    }

    /**
     * 文字左埋め
     * @param str 文字列
     * @param size 処理後サイズ
     * @param padChar 埋め文字
     * @return 処理結果
     */
    public static String leftPad(final String str, final int size, final char padChar) {
    	return org.apache.commons.lang3.StringUtils.leftPad(str, size, padChar);
    }

    /**
     * 文字左埋め
     * @param str 文字列
     * @param size 処理後サイズ
     * @param padStr 埋め文字
     * @return 処理結果
     */
    public static String leftPad(final String str, final int size, String padStr) {
        return org.apache.commons.lang3.StringUtils.leftPad(str, size, padStr);
    }

    /**
     * 文字数取得
     * @param cs 文字列
     * @return 文字数
     */
    public static int length(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.length(cs);
    }

    /**
     * 文字列検索
     * @param cs1 cs1
     * @param cs2 cs2
     * @return 一致位置
     */
    public static int indexOf(final CharSequence cs1, final CharSequence cs2) {
    	return org.apache.commons.lang3.StringUtils.indexOf(cs1, cs2);
    }

    /**
     * string型へ変更
     * @param obj obj
     * @return 文字列
     */
    public static String valueOf(Object obj) {
    	return (obj == null) ? null : obj.toString();
    }

    /**
     * 空白で配列に分割
     * @param str 文字列
     * @return 分割文字列
     */
    public static String[] split(final String str) {

    	return org.apache.commons.lang3.StringUtils.split(str);
    }

    /**
     * 指定文字で配列に分割
     * @param str 文字列
     * @param charcter 分割文字
     * @return 分割文字列
     */
    public static String[] split(final String str, final char charcter) {

    	return org.apache.commons.lang3.StringUtils.split(str, charcter);
    }

    /**
     * 空白で配列に分割
     * @param str 文字列
     * @param size 分割サイズ
     * @return 分割文字列
     */
    public static String[] split(final String str, int size) {

    	String[] str1 = org.apache.commons.lang3.StringUtils.split(str);

    	if (str1.length < size) {
    		size = str1.length;
    	}
    	String[] str2 =  new String[size];
    	for (int i = 0; i < size; i++) {
    		str2[i] = str1[i];
    	}
    	return str2;
    }

	/**
	 * Nullをブランクに変換
	 * @param target 対象文字
	 * @return 変換後文字列
	 */
	public static String replaceNullToBlank(String target) {
		if (target == null) {
			return "";
		}

		return target;
	}

	/**
	 * Nullをブランクに変換
	 * @param target 対象オブジェクト
	 * @return 変換後文字列
	 */
	public static String replaceNullToBlank(Object target) {
		if (target == null) {
			return "";
		}

		return target.toString();
	}

	/**
	 * 文字が含まれるか
	 */
	public static boolean isContains(String target, String... source) {
		for (int i = 0; i < source.length; i++) {
			if (isEquals(target, source[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 引数で指定された文字列を大文字に変換する
	 * 
	 * @param target
	 * @return 変換結果
	 */
	public static String toUpperCase(String target) {
		return isEmpty(target) ? target : target.toUpperCase();
	}

	/**
	 * 引数で指定された文字列を小文字に変換する
	 * 
	 * @param target
	 * @return 変換結果
	 */
	public static String toLowerCase(String target) {
		return isEmpty(target) ? target : target.toLowerCase();
	}
	
	
}
