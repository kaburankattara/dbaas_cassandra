package com.dbaas.cassandra.domain.serverManager;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.getSysDateYyyymmddhhmmssSSSSSS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.ResourceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TagSpecification;
import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;

@Service
@Transactional
public class  ServerManagerService {
	
    @Autowired
    public ServerManagerService(){
    }
	
    /**
     * サーバを保有しているか判定
     * 
     * @param user ユーザー
     * @return 判定結果
     */
	public boolean hasServer(LoginUser user) {		
		return !getInstances(user).isEmpty();
	}
	
    /**
     * ユーザーが保有しているインスタンスリストを取得
     * 
     * @param user ユーザー
     * @return 判定結果
     */
	public Instances getAllInstances(LoginUser user) {
		List<Filter> filterList = new ArrayList<Filter>();
		List<String> tagNameCond = new ArrayList<String>();
		tagNameCond.add(user.getUserIdForInstanceNamePrefix() + "*");
		filterList.add(new Filter("tag:Name", tagNameCond));
		
		return getInstances(user, filterList);
	}

    /**
     * ユーザーが保有しているインスタンスリストを取得
     * 
     * @param user ユーザー
     * @return 判定結果
     */
	public Instances getInstances(LoginUser user) {
		List<Filter> filterList = new ArrayList<Filter>();
		List<String> tagNameCond = new ArrayList<String>();
		tagNameCond.add(user.getUserIdForInstanceNamePrefix() + "*");
		filterList.add(new Filter("tag:Name", tagNameCond));

		List<String> stateCond = new ArrayList<String>();
		stateCond.add("pending");
		stateCond.add("running");
//		stateCond.add("shutting-down");
//		stateCond.add("stopping");
//		stateCond.add("stopped");
//		stateCond.add("terminated");
		filterList.add(new Filter("instance-state-name", stateCond));

		return getInstances(user, filterList);

	}

	/**
	 * ユーザーが保有しているインスタンスリストを取得
	 * 
	 * @param user ユーザー
	 * @param filterList フィルターリスト
	 * @return インスタンスリスト
	 */
	private Instances getInstances(LoginUser user, List<Filter> filterList) {
		// EC2リスト取得
		AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		DescribeInstancesResult instancesResult = ec2
				.describeInstances(new DescribeInstancesRequest().withFilters(filterList));
		
		// EC2の存在有無を判定
		List<Instance> instanceList = new ArrayList<>();
		for (Reservation reservation : instancesResult.getReservations()) {
			for (com.amazonaws.services.ec2.model.Instance instance : reservation.getInstances()) {
				instanceList.add(new Instance(instance));
			}
		}
		return new Instances(instanceList);
	}
	
	/**
	 * サーバーを構築する
	 */
	public void createServer(LoginUser user) {
		RunInstancesRequest runInstancesRequest =
				   new RunInstancesRequest();
		
		Tag tag = new Tag();
		tag.setKey("Name");
		tag.setValue(user.getUserIdForInstanceNamePrefix() + getSysDateYyyymmddhhmmssSSSSSS());
		
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(tag);
		
		TagSpecification tag2 = new TagSpecification();
		tag2.setTags(tags);
		tag2.setResourceType(ResourceType.Instance);
		
		List<TagSpecification> tagSpecifications = new ArrayList<TagSpecification>();
		tagSpecifications.add(tag2);

		runInstancesRequest
			.withImageId("ami-0f310fced6141e627")
			.withInstanceType(InstanceType.T2Micro)
			.withMinCount(1)
			.withMaxCount(1)
			.withKeyName("cassandra2")
			.withSecurityGroupIds("sg-0a70b2ba5f42d9e7f")
			.withSubnetId("subnet-b3ddfcfa")
			.setTagSpecifications(tagSpecifications);
		
		AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		ec2.runInstances(runInstancesRequest);
	}
	
	/**
	 * サーバーを削除する
	 */
	public void deleteServer(String instanceId) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		StopInstancesRequest request = new StopInstancesRequest()
		    .withInstanceIds(instanceId);
		ec2.stopInstances(request);
	}
	
	/**
	 * サーバーを削除する
	 */
	public void deleteAllServer(LoginUser user) {
		for (Instance instance : getInstances(user).getInstanceList()) {
			deleteServer(instance.getInstanceId());
		}
	}
}
