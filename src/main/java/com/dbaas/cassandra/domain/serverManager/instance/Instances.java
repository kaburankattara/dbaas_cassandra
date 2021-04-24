package com.dbaas.cassandra.domain.serverManager.instance;

import static java.util.Arrays.asList;

import java.io.Serializable;
import java.util.ArrayList;
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
	private List<Instance> instanceList = new ArrayList<Instance>();
	
	public Instances(List<Instance> instanceList) {
		if (instanceList == null) {
			throw new NullPointerException("インスタンスリストが取得出来ていません");
		}
		this.instanceList = instanceList;
	}
	
	public static Instances createInstance (Instance... instanceList) {
		return new Instances(new ArrayList<Instance>(asList(instanceList)));
	}
	
	public static Instances createEmptyInstance () {
		return createInstance();
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
	 * pending状態のインスタンスを保持していないか判定
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
	
	/**
	 * running状態のインスタンスを保持していないか判定
	 * 
	 * @return 判定結果
	 */
	public boolean hasRunningInstance() {
		for (Instance instance : instanceList) {
			if (instance.isRunning()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * running状態のインスタンスを保持していないか判定
	 * 
	 * @return 判定結果
	 */
	public boolean hasShuttingDownToTerminatedInstance() {
		for (Instance instance : instanceList) {
			if (instance.isShuttingDownToTerminated()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 保持ししているインスタンス数を取得
	 * 
	 * @return 判定結果
	 */
	public int getHasInstanceCount() {
		if (isEmpty()) {
			return 0;
		}
		return instanceList.size();
	}

	/**
	 * 全てのインスタンスがrunning状態か判定
	 *
	 * @return 判定結果
	 */
	public boolean isAllRunning() {
		if (isEmpty()) {
			return false;
		}

		for (Instance instance : instanceList) {
			if (!instance.isRunning()) {
				return false;
			}
		}
		return true;
	}
 }
