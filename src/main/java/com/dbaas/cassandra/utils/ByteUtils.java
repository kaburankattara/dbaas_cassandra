package com.dbaas.cassandra.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * ByteUtils
 */
public final class ByteUtils {

	/**
	 * コンストラクタ
	 */
    private ByteUtils() {
    }
    
    public static byte[] toBytes(String str) {
		byte[] byteArray = null;
		try {
			Charset charset = StandardCharsets.UTF_8;
			CharsetEncoder encoder = charset.newEncoder();
			CharBuffer buffer = CharBuffer.wrap(str);
			ByteBuffer byteBuffer = encoder.encode(buffer);
			byteArray = byteBuffer.array();
		} catch (CharacterCodingException e) {
			throw new RuntimeException("byte型変換に失敗しました。");
		}
		return byteArray;
    }
}
