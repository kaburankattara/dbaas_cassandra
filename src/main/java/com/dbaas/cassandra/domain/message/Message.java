package com.dbaas.cassandra.domain.message;

/**
 *  メッセージ
 */
public class Message {
	
	public static final String MESSAGE_KEY_INFO = "message";
	
	public static final String MESSAGE_KEY_WARNING = "warningMessage";
	
	public static final String MESSAGE_KEY_ERROR = "errorMessage";
	
	/** 認証に失敗しました。ユーザIDまたはパスワードが間違っています。 */
	public static final String MSG002E = "MSG002E";

	/** 入力されたユーザーIDは使用済です。 */
	public static final String MSG003E = "MSG003E";

	/** {0}の入力は必須です。 */
	public static final String MSG004E = "MSG004E";

	/** 登録処理後、テーブルの存在確認に失敗しました。正常に登録されているかご確認ください。 */
	public static final String MSG001W = "MSG001W";
		
}
