package com.dbaas.cassandra.domain.cassandra.keyspace;

import java.util.ArrayList;
import java.util.List;

import com.dbaas.cassandra.domain.table.keyspaceRegistPlan.KeyspaceRegistPlanEntity;
import com.dbaas.cassandra.utils.ObjectUtils;

/**
 * キースペース登録予定リスト
 *
 */
public class KeyspaceRegistPlans {

	/**
	 * デフォルトコンストラクタ
	 */
	public KeyspaceRegistPlans() {
	}

	/**
	 * デフォルトコンストラクタ
	 * 
	 * @param keyspaceRegistPlanList
	 */
	public KeyspaceRegistPlans(List<KeyspaceRegistPlan> keyspaceRegistPlanList) {
		this.keyspaceRegistPlanList = keyspaceRegistPlanList;
	}
	
	private List<KeyspaceRegistPlan> keyspaceRegistPlanList;

	/**
	 * 空のキースペース登録予定情報を作成する
	 * 
	 * @return キースペース登録予定情報
	 */
	public static KeyspaceRegistPlans createEmptyKeyspaceRegistPlans() {
		return new KeyspaceRegistPlans(new ArrayList<KeyspaceRegistPlan>());
	}

	/**
	 * キースペース登録予定情報が空か判定
	 * 
	 * @return 判定結果
	 */
	public boolean isEmpty() {
		return ObjectUtils.isEmpty(keyspaceRegistPlanList) || keyspaceRegistPlanList.isEmpty();
	}

	/**
	 * キースペースリストを取得
	 * 
	 * @return キースペースリスト
	 */
	public List<String> getKeyspaceList() {
		if(isEmpty()) {
			return new ArrayList<String>();
		}
		
		List<String> keyspaceList = new ArrayList<String>();
		for (KeyspaceRegistPlanEntity entity : keyspaceRegistPlanList) {
			keyspaceList.add(entity.getKeyspace());
		}
		return keyspaceList;
	}
 }
