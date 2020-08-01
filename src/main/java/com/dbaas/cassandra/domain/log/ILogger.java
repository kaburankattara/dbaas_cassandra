package com.dbaas.cassandra.domain.log;

interface ILogger {

	/**
	 * トレースログ出力
	 * @param msg メッセージ
	 */
    void trace(String msg);

    /**
     * トレースログ出力
     * @param msg メッセージ
     * @param t 例外
     */
    void trace(String msg, Throwable t);

    /**
     * トレースログ出力
     * @param format フォーマット
     * @param args 可変パラメータ
     */
    void trace(String format, Object... args);

    /**
     * デバッグログ出力
     * @param msg メッセージ
     */
    void debug(String msg);

    /**
     * デバッグログ出力
     * @param msg メッセージ
     * @param t 例外
     */
    void debug(String msg, Throwable t);

    /**
     * デバッグログ出力
     * @param format フォーマット
     * @param args 可変パラメータ
     */
    void debug(String format, Object... args);

    /**
     * インフォログ出力
     * @param msg メッセージ
     */
    void info(String msg);

    /**
     * インフォログ出力
     * @param msg メッセージ
     * @param t 例外
     */
    void info(String msg, Throwable t);

    /**
     * インフォログ出力
     * @param format フォーマット
     * @param args 可変パラメータ
     */
    void info(String format, Object... args);

    /**
     * 警告ログ出力
     * @param msg メッセージ
     */
    void warn(String msg);

    /**
     * 警告ログ出力
     * @param msg メッセージ
     * @param t 例外
     */
    void warn(String msg, Throwable t);

    /**
     * 警告ログ出力
     * @param format フォーマット
     * @param args 可変パラメータ
     */
    void warn(String format, Object... args);

    /**
     * エラーログ出力
     * @param msg メッセージ
     */
    void error(String msg);

    /**
     * エラーログ出力
     * @param msg メッセージ
     * @param t 例外
     */
    void error(String msg, Throwable t);

    /**
     * エラーログ出力
     * @param format フォーマット
     * @param args 可変パラメータ
     */
    void error(String format, Object... args);
}
