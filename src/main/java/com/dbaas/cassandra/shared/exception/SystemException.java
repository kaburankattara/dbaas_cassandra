package com.dbaas.cassandra.shared.exception;

/**
 * システム例外クラス
 */
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 */
	public SystemException() {
		super();
	}

	/**
	 * コンストラクタ
	 * @param message メッセージ
	 */
	public SystemException(String message) {
		super(message);
	}

	/**
	 * コンストラクタ
	 * @param message メッセージ
	 * @param cause 原因
	 */
	public SystemException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * コンストラクタ
	 * @param cause 原因
	 */
	public SystemException(Throwable cause) {
		super(cause);
	}

	/**
	 * コンストラクタ
	 * @param message メッセージ
	 * @param cause 原因
	 * @param enableSuppression 抑制可能フラグ
	 * @param writableStackTrace スタックトレース書き込みフラグ
	 */
	protected SystemException(String message, Throwable cause,
							  boolean enableSuppression,
							  boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
