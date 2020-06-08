package com.dbaas.cassandra.utils;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Short.parseShort;
import static java.math.BigDecimal.ROUND_DOWN;
import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ROUND_UP;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.springframework.stereotype.Component;

/**
 * 数値Utils
 */
@Component("NumberUtils")
public final class NumberUtils {

	/**
	 * コンストラクタ
	 */
	private NumberUtils() {
	}

	/**
	 * 税率の小数点以下桁数
	 */
	private static final int ZEI_RATE_SHOSU_LENGTH = 3;

	/**
	 * 有効桁の次の桁の値
	 */
	public static final int DIGISTVALUE = 5;

	/**
	 * 数値丸め 四捨五入の場合
	 *
	 * @param number
	 *            数値
	 * @param scale
	 *            小数有効桁数
	 * @return 丸め値
	 */
	public static BigDecimal roundingNumericValueByShishagonyu(BigDecimal number, int scale) {
		try {
			argumentCheck(number, scale);
		} catch (Exception e) {
			return null;
		}
		BigDecimal rnv = null;
		DecimalFormat format = new DecimalFormat("#.#");
		// 小数点以下の最小値
		format.setMinimumFractionDigits(scale + 1);
		// 小数点以下の最大値
		format.setMaximumFractionDigits(scale + 1);

		String vStr = number.toPlainString();

		// 数値の引数.小数有効桁数の次桁数値を取得
		String digitStr = vStr.substring(vStr.length() - 1);
		int digitValue = Integer.parseInt(digitStr);

		if (digitValue < DIGISTVALUE) {
			rnv = roundingNumericValueByKirisute(number, scale);
		} else {
			rnv = roundingNumericValueByKiriage(number, scale);
		}
		return rnv;
	}

	/**
	 * 数値丸め 切り捨ての場合
	 *
	 * @param number
	 *            数値
	 * @param scale
	 *            小数有効桁数
	 * @return 丸め値
	 */
	public static BigDecimal roundingNumericValueByKirisute(BigDecimal number, int scale) {
		try {
			argumentCheck(number, scale);
		} catch (Exception e) {
			return null;
		}
		return number.setScale(scale, ROUND_DOWN);
	}

	/**
	 * 数値丸め 切り上げの場合
	 *
	 * @param number
	 *            数値
	 * @param scale
	 *            小数有効桁数
	 * @return 丸め値
	 */
	public static BigDecimal roundingNumericValueByKiriage(BigDecimal number, int scale) {
		try {
			argumentCheck(number, scale);
		} catch (Exception e) {
			return null;
		}
		return number.setScale(scale, ROUND_UP);
	}

	/**
	 * 引数チェック
	 *
	 * @param number
	 *            数値
	 * @param scale
	 *            小数有効桁数
	 */
	private static void argumentCheck(BigDecimal number, int scale) {
		// 引数の有効値チェック
		if (number == null) {
			throw new RuntimeException();
		}
		if (scale < 0) {
			throw new RuntimeException();
		}
	}

	/**
	 * 数値のカンマ付き文字列変換（9,999）
	 *
	 * @param number
	 *            変換対象の数値
	 * @return カンマ付き数値文字列
	 */
	public static String formatCommaNumeric(Integer number) {
		if (number == null) {
			return null;
		}

		// 3桁カンマ
		DecimalFormat df1 = new DecimalFormat("#,###");
		return df1.format(number);
	}

	/**
	 * 数値のカンマ付き文字列変換（9,999.99）
	 *
	 * @param number
	 *            変換対象の数値
	 * @return カンマ付き数値文字列
	 */
	public static String formatCommaNumeric(BigDecimal number) {
		if (number == null) {
			return null;
		}

		// 3桁カンマ
		DecimalFormat df1 = new DecimalFormat("#,###.##");
		return df1.format(number);
	}

	/**
	 * 数値のカンマ付き文字列変換（スケール指定有）
	 *
	 * @param number
	 *            変換対象の数値
	 * @param scale
	 *            小数有効桁数
	 * @return カンマ付き数値文字列
	 */
	public static String formatCommaNumeric(BigDecimal number, int scale) {
		if (number == null) {
			return null;
		}

		// 指定スケールでカンマ変数
		return new DecimalFormat("#,##0." + String.format("%0" + scale + "d", 0)).format(number);
	}

	/**
	 * 数値型の加算（Nullは0として算出）
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @return 算出値
	 */
	public static BigDecimal addNullZero(BigDecimal number1, BigDecimal number2) {
		if (number1 == null) {
			number1 = new BigDecimal(0);
		}
		if (number2 == null) {
			number2 = new BigDecimal(0);
		}

		return number1.add(number2);
	}

	/**
	 * 数値型の加算（Nullは0として算出）
	 *
	 * @param numbers 数値
	 *
	 * @return 算出値
	 */
	public static BigDecimal multiAddNullZero(BigDecimal... numbers) {
		BigDecimal returnValue = new BigDecimal("0");
		for (BigDecimal num : numbers) {
			returnValue = addNullZero(returnValue, num);
		}
		return returnValue;
	}

	/**
	 * 数値型の加算（Nullは0として算出）
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @return 算出値
	 */
	public static Integer addNullZero(Integer number1, Integer number2) {
		if (number1 == null) {
			number1 = 0;
		}
		if (number2 == null) {
			number2 = 0;
		}

		return number1 + number2;
	}

	/**
	 * 数値型の減算（Nullは0として算出）
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @return 算出値
	 */
	public static BigDecimal subtractNullZero(BigDecimal number1, BigDecimal number2) {
		if (number1 == null) {
			number1 = new BigDecimal(0);
		}
		if (number2 == null) {
			number2 = new BigDecimal(0);
		}

		return number1.subtract(number2);
	}

	/**
	 * 数値型の乗算（Nullは0として算出）
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @return 算出値
	 */
	public static BigDecimal multiplyNullZero(BigDecimal number1, BigDecimal number2) {
		if (number1 == null) {
			return new BigDecimal(0);
		}
		if (number2 == null) {
			return new BigDecimal(0);
		}

		return number1.multiply(number2);
	}

	/**
	 * 数値型の乗算（Nullは0として算出）
	 *
	 * @param numbers
	 *            数値
	 * @return 算出値
	 */
	public static BigDecimal multiMultiplyNullZero(BigDecimal... numbers) {
		BigDecimal returnValue = new BigDecimal("1");
		for (BigDecimal num : numbers) {
			returnValue = multiplyNullZero(returnValue, num);
		}
		return returnValue;
	}

	/**
	 * 数値型の除算（切上げ）（Nullは0として算出）
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @param scale
	 *            有効少数桁数
	 * @return 算出値
	 */
	public static BigDecimal divideNullZero(BigDecimal number1, BigDecimal number2, int scale) {
		if (number1 == null) {
			return new BigDecimal(0);
		}
		if (number2 == null) {
			return new BigDecimal(0);
		}

		return number1.divide(number2, scale, ROUND_UP);
	}

	/**
	 * 数値型の除算（四捨五入）（Nullは0として算出）
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @param scale
	 *            有効少数桁数
	 * @return 算出値
	 */
	public static BigDecimal divideNullZeroHalfUp(BigDecimal number1, BigDecimal number2, int scale) {
		if (number1 == null) {
			return new BigDecimal(0);
		}
		if (number2 == null) {
			return new BigDecimal(0);
		}

		return number1.divide(number2, scale, ROUND_HALF_UP);
	}

	/**
	 * 数値型の除算（切捨て）（Nullは0として算出）
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @param scale
	 *            有効少数桁数
	 * @return 算出値
	 */
	public static BigDecimal divideNullZeroRoundDown(BigDecimal number1, BigDecimal number2, int scale) {
		if (number1 == null) {
			return new BigDecimal(0);
		}
		if (number2 == null) {
			return new BigDecimal(0);
		}

		return number1.divide(number2, scale, ROUND_DOWN);
	}

	/**
	 * 数値型のNullを０に置換
	 *
	 * @param number
	 *            置換対象数値
	 * @return 置換後数値
	 */
	public static BigDecimal replaceNullToZero(BigDecimal number) {
		return number == null ? new BigDecimal(0) : number;
	}

	/**
	 * 文字列→BigDecimal
	 *
	 * @param number
	 *            変換対象文字列
	 * @return 変換後数値
	 */
	public static BigDecimal toDecimal(String number) {
		return isDecimal(number) ? new BigDecimal(number) : null;
	}

	/**
	 * 文字列→BigDecimal（Nullを０）
	 *
	 * @param number
	 *            変換対象文字列
	 * @return 変換後数値
	 */
	public static BigDecimal toDecimalNullZero(String number) {
		return isDecimal(number) ? new BigDecimal(number) : new BigDecimal(0);
	}

	/**
	 * int→BigDecimal
	 *
	 * @param number
	 *            数値
	 * @return 変換後数値
	 */
	public static BigDecimal toDecimal(Integer number) {
		return new BigDecimal(number);
	}

	/**
	 * 文字列→Integer（変換失敗時は０）
	 *
	 * @param number
	 *            変換対象文字列
	 * @return 変換後数値
	 */
	public static Integer toInteger(String number) {
		return isInt(number) ? new Integer(number) : null;
	}

	/**
	 * 文字列→int（変換失敗時は０）
	 *
	 * @param number
	 *            変換対象文字列
	 * @return 変換後数値
	 */
	public static int toInt(String number) {
		return isInt(number) ? parseInt(number) : 0;
	}

	/**
	 * 文字列→short型（変換失敗時は０）
	 *
	 * @param number
	 *            変換対象文字列
	 * @return 変換後数値
	 */
	public static short toShort(String number) {
		return isShort(number) ? parseShort(number) : 0;
	}

	/**
	 * 文字列→short型（変換失敗時は０）
	 *
	 * @param number
	 *            変換対象文字列
	 * @return 変換後数値
	 */
	public static long toLong(String number) {
		return isLong(number) ? parseLong(number) : 0;
	}

	/**
	 * BigDecimal型→文字列
	 *
	 * @param number
	 *            数値
	 * @return 判定結果
	 */
	public static String toPlainString(BigDecimal number) {
		if (number == null) {
			return "";
		}
		return number.toPlainString();
	}

	/**
	 * 数値をパーセンテージ表記に編集
	 *
	 * @param number
	 *            編集対象数値
	 * @return 編集後の値
	 */
	public static String formatPercent(BigDecimal number) {

		if (number == null) {
			return "";
		}

		NumberFormat numberFmt = NumberFormat.getPercentInstance(); // パーセント形式
		numberFmt.setMaximumFractionDigits(ZEI_RATE_SHOSU_LENGTH);
		return numberFmt.format(number);
	}

	/**
	 * int型チェック
	 *
	 * @param number
	 *            対象数値
	 * @return 判定結果
	 */
	public static boolean isInt(String number) {
		try {
			parseInt(number);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * short型チェック
	 *
	 * @param number
	 *            対象数値
	 * @return 判定結果
	 */
	public static boolean isShort(String number) {
		try {
			parseShort(number);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * long型チェック
	 *
	 * @param number
	 *            対象数値
	 * @return 判定結果
	 */
	public static boolean isLong(String number) {
		try {
			parseLong(number);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * BigDecimal型チェック
	 *
	 * @param number
	 *            対象数値
	 * @return 判定結果
	 */
	public static boolean isDecimal(String number) {
		try {
			new BigDecimal(number);
			return true;
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * 比率をパーセンテージに変換
	 *
	 * @param number
	 *            対象数値
	 * @return 変換後数値
	 */
	public static BigDecimal convertToPercent(BigDecimal number) {
		if (number == null) {
			return null;
		}
		return number.multiply(new BigDecimal(100)).setScale(1, ROUND_DOWN);
	}

	/**
	 * パーセンテージを比率に変換
	 *
	 * @param number
	 *            対象数値
	 * @return 変換後数値
	 */
	public static BigDecimal convertToRate(BigDecimal number) {
		if (number == null) {
			return null;
		}
		return number.divide(new BigDecimal(100), ZEI_RATE_SHOSU_LENGTH, ROUND_DOWN);
	}

	/**
	 * BigDecimal型の比較
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @return 判定結果
	 */
	public static boolean isEqualsDecimal(BigDecimal number1, BigDecimal number2) {
		if (number1 == null && number2 == null) {
			return true;
		}
		if (number1 == null || number2 == null) {
			return false;
		}
		return number1.compareTo(number2) == 0;
	}

	/**
	 * BigDecimal型の比較 反転
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @return 判定結果
	 */
	public static boolean isNotEqualsDecimal(BigDecimal number1, BigDecimal number2) {
		return !isEqualsDecimal(number1, number2);
	}

	/**
	 * Short型の比較
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @return 判定結果
	 */
	public static boolean isEqualsShort(Short number1, Short number2) {
		if (number1 == null && number2 == null) {
			return true;
		}
		if (number1 == null || number2 == null) {
			return false;
		}
		return number1.equals(number2);
	}

	/**
	 * Short型の比較 反転
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @return 判定結果
	 */
	public static boolean isNotEqualsShort(Short number1, Short number2) {
		return !isEqualsShort(number1, number2);
	}

	/**
	 * BigDecimal型の大小比較 数値1が数値2より小さいか判定
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @return 判定結果
	 */
	public static boolean isSmallerDecimal(BigDecimal number1, BigDecimal number2) {
		if (number1 == null || number2 == null) {
			return false;
		}
		return number1.compareTo(number2) == -1;
	}

	/**
	 * BigDecimal型の大小比較 数値1が数値2より小さいか判定
	 *
	 * @param number1
	 *            数値1
	 * @param number2
	 *            数値2
	 * @return 判定結果
	 */
	public static boolean isSmallerEqualDecimal(BigDecimal number1, BigDecimal number2) {
		if (number1 == null || number2 == null) {
			return false;
		}
		return number1.compareTo(number2) == -1 || number1.compareTo(number2) == 0;
	}


	/**
	 * -1をかけた値を返す
	 *
	 * @param value
	 *            元の値
	 * @return -1をかけた値
	 */
	public static BigDecimal multiplyNegativeOne(BigDecimal value) {
		return value == null ? null : multiplyNullZero(value, new BigDecimal("-1"));
	}
}
