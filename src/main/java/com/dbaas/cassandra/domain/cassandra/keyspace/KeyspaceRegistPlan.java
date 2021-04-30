package com.dbaas.cassandra.domain.cassandra.keyspace;

import com.dbaas.cassandra.domain.table.keyspaceRegistPlan.KeyspaceRegistPlanEntity;
import com.dbaas.cassandra.utils.StringUtils;

/**
 * キースペース登録予定
 *
 */
public class KeyspaceRegistPlan extends KeyspaceRegistPlanEntity {
	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 空のキースペース登録予定を作成する
	 *
	 * @return キースペース登録予定
	 */
	public static KeyspaceRegistPlan createEmptyInstance() {
		return new KeyspaceRegistPlan(new KeyspaceRegistPlan());
	}

	/**
	 * 空のキースペース登録予定を作成する
	 *
	 * @return キースペース登録予定
	 */
	public static KeyspaceRegistPlan createInstance(String userId, String keyspace) {
		return new KeyspaceRegistPlan(userId, keyspace);
	}

	/**
	 * 空のキースペース登録予定を作成する
	 *
	 * @return キースペース登録予定
	 */
	public static KeyspaceRegistPlan createInstance(KeyspaceRegistPlanEntity entity) {
		return new KeyspaceRegistPlan(entity);
	}

	/**
	 * デフォルトコンストラクタ
	 */
	public KeyspaceRegistPlan() {
	}

	/**
	 * デフォルトコンストラクタ
	 */
	private KeyspaceRegistPlan(String userId, String keyspace) {
		this.setUserId(userId);
		this.setKeyspace(keyspace);
	}

	/**
	 * デフォルトコンストラクタ
	 */
	private KeyspaceRegistPlan(KeyspaceRegistPlanEntity entity) {
		this.setUserId(entity.getUserId());
		this.setKeyspace(entity.getKeyspace());
	}

	/**
	 * キースペース登録予定が空か判定
	 * 
	 * @return 判定結果
	 */
	public boolean isEmpty() {
		return StringUtils.isEmpty(getUserId());
	}
 }
