package com.dbaas.cassandra.domain.cassandra;

import com.dbaas.cassandra.domain.cassandra.keyspace.service.KeyspaceService;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.shared.exception.SystemException;
import com.dbaas.cassandra.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dbaas.cassandra.utils.StringUtils.isNotEmpty;

@Service
@Transactional
public class CassandraService {

	private KeyspaceService keyspaceService;

	@Autowired
	public CassandraService(KeyspaceService keyspaceService) {
		this.keyspaceService = keyspaceService;
	}

	/**
	 * 全てのインスタンスに対し、cassandraをセットアップする
	 * c
	 * @param user ユーザー
	 * @param instances インスタンスリスト
	 */
	public void setup(LoginUser user, Instances instances) {
		for (Instance instance : instances.getInstanceList()) {
			setup(user, instance);
		}
	}

	public void setup(LoginUser user, Instance instance) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// cassandraをインストールする
//			cassandraServer.installCassandra();

		// casssandra.yamlを設定し、cassandraを起動する
		cassandraServer.setCassandraYaml(user, instance);

		// cassandraを実行する
		cassandraServer.execCassandra(instance);
	}

	public void execCassandra(Instance instance) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// cassandraを実行する
		cassandraServer.execCassandra(instance);
	}

	/**
	 * 起動確認まで考慮してcassandraを起動する
	 * 
	 * @param instances インスタンスリスト
	 */
	public void execCassandraByWait(Instances instances) {
		boolean hasNotExecCassandra = true;
		// TODO application.properties化する
		int execCassandraCount = instances.getHasInstanceCount() * 3;
		while (hasNotExecCassandra) {
			hasNotExecCassandra = false;
			
			// 未起動のcassandraを再起動
			for (Instance instance : instances.getInstanceList()) {
				// プロセスIDが取得出来なければcassandraをリスタートする
				if (!isExecCassandra(instance)) {
					execCassandra(instance);
					
					// 再起動のリトライ制御
					execCassandraCount--;
					if (execCassandraCount < 0) {
						// 実行対象cassandra数×3回再起動を試みたが起動仕切らない場合、システムエラーとする
						throw new SystemException();
					}
				}
			}
			
			// CQLが実行可能か確認
			for (Instance instance : instances.getInstanceList()) {
				// 試験的なcqlコマンドを定期実行し、実行可能になるまで待つ
				if (canExecCql(instance)) {
					continue;
				}
				hasNotExecCassandra = true;
				ThreadUtils.sleep();
				ThreadUtils.sleep();
				ThreadUtils.sleep();
				ThreadUtils.sleep();
				ThreadUtils.sleep();
			}
		}
	}

	public String getProcessIdByCassandra(Instance instance) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// cassandraの起動確認
		return cassandraServer.getProcessIdByCassandra(instance);
	}

	/**
	 * 全インスタンスがCQLの実行が可能か判定
	 * 
	 * @param instances インスタンスリスト
	 * @return 判定結果
	 */
	public boolean canAllExecCql(Instances instances) {
		if (instances.isEmpty()) {
			return false;
		}
		
		for (Instance instance : instances.getInstanceList()) {
			// CQL実行が出来ないインスタンスが存在すればfalse
			if (keyspaceService.findAllKeyspace(instance).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * CQLが実行可能か判定
	 * 
	 * @return 判定結果
	 */
	public boolean canExecCql(Instance instance) {
		// CQLでキースペース一覧が取得出来るか判定
		return !keyspaceService.findAllKeyspace(instance).isEmpty();
	}

	public boolean isExecAllCassandra(Instances instances) {
		for (Instance instance : instances.getInstanceList()) {
			if (!isExecCassandra(instance)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * casssandraが起動済みか判定
	 * 
	 * @param instance　インスタンス
	 * @return 判定結果
	 */
	public boolean isExecCassandra(Instance instance) {
		// cassandraのプロセスIDを取得し起動済みか判定
		return isNotEmpty(getProcessIdByCassandra(instance));
	}
}
