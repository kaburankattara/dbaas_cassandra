package com.dbaas.cassandra.domain.server.instance;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ec2インスタンスリストのテスト用Service
 * 
 */
@Service
@Transactional
public class InstancesServiceForTest {

	public Instances createMockInstances_サーバ未構築状態() {
		return Instances.createEmptyInstance();
	}

	public Instances createMockInstances_サーバ構築中状態() {
		return Instances.createInstance(createInstanceForPending());
	}

	public Instances createMockInstances_サーバ構築済状態() {
		return Instances.createInstance(createInstanceForRunning());
	}
	
	/**
	 * Pending用のec2インスタンスを作成
	 * 
	 * @return ec2インスタンスリスト
	 */
	public Instance createInstanceForPending() {
		com.amazonaws.services.ec2.model.InstanceState state = new com.amazonaws.services.ec2.model.InstanceState();
		state.setCode(0);
		state.setName("pending");
		com.amazonaws.services.ec2.model.Instance ec2Instance = new com.amazonaws.services.ec2.model.Instance();
		ec2Instance.setState(state);
		return new Instance(ec2Instance);
	}
	
	/**
	 * Running用のec2インスタンスを作成
	 * 
	 * @return ec2インスタンスリスト
	 */
	public Instance createInstanceForRunning() {
		com.amazonaws.services.ec2.model.InstanceState state = new com.amazonaws.services.ec2.model.InstanceState();
		state.setCode(16);
		state.setName("running");
		com.amazonaws.services.ec2.model.Instance ec2Instance = new com.amazonaws.services.ec2.model.Instance();
		ec2Instance.setState(state);
		return new Instance(ec2Instance);
	}
 }
