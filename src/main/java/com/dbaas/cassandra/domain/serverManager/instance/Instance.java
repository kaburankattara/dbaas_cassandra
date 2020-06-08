package com.dbaas.cassandra.domain.serverManager.instance;

import static com.dbaas.cassandra.utils.StringUtils.isEquals;

import java.io.Serializable;

/**
 * ec2インスタンス
 * 
 */
public class Instance implements Serializable {
	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * インスタンスリスト
	 */
	private com.amazonaws.services.ec2.model.Instance instance;
	
	public Instance(com.amazonaws.services.ec2.model.Instance instance) {
		if (instance == null) {
			throw new NullPointerException("ec2インスタンスが取得出来ていません。");
		}
		this.instance = instance;
	}
	
	/**
	 * インスタンスリストを取得
	 * 
	 * @return インスタンスリスト
	 */
	public com.amazonaws.services.ec2.model.Instance getInstanceList() {
		return instance;
	}
	
	/**
	 * インスタンスを設定
	 * 
	 * @param instance インスタンス
	 */
	public void setInstance(com.amazonaws.services.ec2.model.Instance instance) {
		this.instance = instance;
	}
	
	/**
	 * インスタンスが空か判定
	 * 
	 * @return 判定結果
	 */
	public boolean isEmpty() {
		return instance == null;
	}
	
	/**
	 * インスタンスIDを取得
	 * 
	 * @return インスタンスID
	 */
	public String getInstanceId() {
		return instance.getInstanceId();
	}
	
	/**
	 * IPv4のパブリックIPアドレスを取得
	 * 
	 * @return IPv4のパブリックIPアドレス
	 */
	public String getPublicIpAddress() {
		return instance.getPublicIpAddress();
	}
	
	/**
	 * IPv4のプライベートIPアドレスを取得
	 * 
	 * @return IPv4のプライベートIPアドレス
	 */
	public String getPrivateIpAddress() {
		return instance.getPrivateIpAddress();
	}
	
	/**
	 * インスタンスの状態がpendingか判定
	 * 
	 * @return 判定結果
	 */
	public boolean isPending() {
		return isEquals(instance.getState().getName(), "pending");
	}
 }
