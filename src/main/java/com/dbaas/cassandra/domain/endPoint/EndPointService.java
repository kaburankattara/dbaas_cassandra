package com.dbaas.cassandra.domain.endPoint;

import static com.dbaas.cassandra.consts.SysConsts.EMPTY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class EndPointService {

	private ServerService serverService;

	private CassandraService cassandraService;

    @Autowired
    public EndPointService(ServerService serverService, CassandraService cassandraService) {
		this.serverService = serverService;
		this.cassandraService = cassandraService;
    }
    
    public String getEndPoint(LoginUser user) {

    	// インスタンスを取得
		Instances instances = serverService.getInstances(user);

		// 全インスタンスがCQLの実行が可能か判定
		boolean canAllExecCql = cassandraService.canAllExecCql(instances);

		// 全インスタンスがCQLの実行が不可の場合、エンドポイント未作成扱いとする
		if (!canAllExecCql) {
			return EMPTY;
		}

		// 全インスタンスがCQLの実行が可能の場合、エンドポイントとなるIPアドレスを返す
		// TODO マルチノードにしたら修正する
		for (Instance instance : instances.getInstanceList()) {
			return instance.getPublicIpAddress();
		}

		// 本来はありえないパターンのため、エンドポイント未作成扱いとする
		return EMPTY;
    }
}
