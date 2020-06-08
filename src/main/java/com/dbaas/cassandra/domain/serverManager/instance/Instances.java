package com.dbaas.cassandra.domain.serverManager.instance;

import java.io.Serializable;
import java.util.List;

/**
 * ec2インスタンスリスト
 * 
 */
public class Instances implements Serializable {
	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * インスタンスリスト
	 */
	private List<Instance> instanceList;
	
	public Instances(List<Instance> instanceList) {
		if (instanceList == null) {
			throw new NullPointerException("インスタンスリストが取得出来ていません");
		}
		this.instanceList = instanceList;
	}
	
	/**
	 * インスタンスリストを取得
	 * 
	 * @return インスタンスリスト
	 */
	public List<Instance> getInstanceList() {
		return instanceList;
	}
	
	/**
	 * インスタンスリストを設定
	 * 
	 * @param instanceList インスタンスリスト
	 */
	public void setInstanceList(List<Instance> instanceList) {
		this.instanceList = instanceList;
	}
	
	/**
	 * インスタンスを保持していないか判定
	 * 
	 * @return 判定結果
	 */
	public boolean isEmpty() {
		return instanceList == null || instanceList.isEmpty();
	}
	
	/**
	 * インスタンスを保持していないか判定
	 * 
	 * @return 判定結果
	 */
	public boolean hasPendingInstance() {
		for (Instance instance : instanceList) {
			if (instance.isPending()) {
				return true;
			}
		}
		return false;
	}
 }
