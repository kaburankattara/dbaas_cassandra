package com.dbaas.cassandra.domain.keyspaceRegistPlan;

import com.dbaas.cassandra.domain.table.keyspaceManager.KeyspaceManagerEntity;
import com.dbaas.cassandra.utils.StringUtils;

/**
 * キースペース登録予定情報
 *
 */
public class KeyspaceRegistPlan extends KeyspaceManagerEntity {
	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * デフォルトコンストラクタ
	 */
	public KeyspaceRegistPlan() {
	}

	/**
	 * デフォルトコンストラクタ
	 */
	public KeyspaceRegistPlan(KeyspaceManagerEntity entity) {
		this.setUserId(entity.getUserId());
		this.setKeyspace(entity.getKeyspace());
	}

	/**
	 * 空のキースペース登録予定情報を作成する
	 * 
	 * @return キースペース登録予定情報
	 */
	public static KeyspaceRegistPlan createEmptyKeyspaceRegistPlan() {
		return new KeyspaceRegistPlan(new KeyspaceRegistPlan());
	}

	/**
	 * キースペース登録予定情報が空か判定
	 * 
	 * @return 判定結果
	 */
	public boolean isEmpty() {
		return StringUtils.isEmpty(getUserId());
	}
 }
