package com.dbaas.cassandra.domain.server.instance;

import static com.dbaas.cassandra.utils.StringUtils.isEquals;

import java.io.Serializable;

import com.amazonaws.services.ec2.model.Tag;

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
	
	private Instance() {
	}
	
	public static Instance CreateEmptyInstance() {
		return new Instance();
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
	 * インスタンス名を取得
	 * 
	 * @return インスタンス名
	 */
	public String getName() {
		for (Tag tag : instance.getTags()) {
			if (isEquals(tag.getKey(), "Name")) {
				return tag.getValue();
			}
		}
		return null;
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
	
	/**
	 * インスタンスの状態がrunningか判定
	 * 
	 * @return 判定結果
	 */
	public boolean isRunning() {
		return isEquals(instance.getState().getName(), "running");
	}
	
	/**
	 * インスタンスの状態がshutting-downか判定
	 * 
	 * @return 判定結果
	 */
	public boolean isShuttingDown() {
		return isEquals(instance.getState().getName(), "shutting-down");
	}
	
	/**
	 * インスタンスの状態がstoppingか判定
	 * 
	 * @return 判定結果
	 */
	public boolean isStopping() {
		return isEquals(instance.getState().getName(), "stopping");
	}
	
	/**
	 * インスタンスの状態がstoppedか判定
	 * 
	 * @return 判定結果
	 */
	public boolean isStopped() {
		return isEquals(instance.getState().getName(), "stopped");
	}
	
	/**
	 * インスタンスの状態がterminatedか判定
	 * 
	 * @return 判定結果
	 */
	public boolean isTerminated() {
		return isEquals(instance.getState().getName(), "terminated");
	}
	
	/**
	 * インスタンスの状態が「shutting-down」〜「terminated」か判定
	 * 
	 * @return 判定結果
	 */
	public boolean isShuttingDownToTerminated() {
		return isShuttingDown() || isStopping() || isStopped() || isTerminated();
	}
 }
