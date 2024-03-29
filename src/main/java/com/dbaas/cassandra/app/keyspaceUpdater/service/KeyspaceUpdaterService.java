package com.dbaas.cassandra.app.keyspaceUpdater.service;

import com.dbaas.cassandra.app.keyspaceUpdater.service.bean.KeyspaceUpdaterDeleteService;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;
import com.dbaas.cassandra.domain.cassandra.keyspace.service.KeyspaceService;
import com.dbaas.cassandra.domain.cassandra.table.Tables;
import com.dbaas.cassandra.domain.cassandra.table.service.TableService;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dbaas.cassandra.utils.ThreadUtils.sleep;

@Service
@Transactional
public class KeyspaceUpdaterService {

	private KeyspaceUpdaterDeleteService deleteService;

	private ServerService serverService;

	private KeyspaceService keyspaceService;

	private TableService tableService;

	@Autowired
	KeyspaceUpdaterService(KeyspaceUpdaterDeleteService deleteService, ServerService serverService
			, KeyspaceService keyspaceService , TableService tableService) {
		this.deleteService = deleteService;
		this.serverService = serverService;
		this.keyspaceService = keyspaceService;
		this.tableService = tableService;
	}

	/**
	 * テーブル一覧を取得
	 */
	public Tables findTable(LoginUser user, String keyspace) {
		try {
			// 保持しているサーバの状態が全てrunningになるまで待つ
			boolean isNotAllInstanceRunning = true;
			while (isNotAllInstanceRunning) {
				// ユーザに紐づくEC2リストを取得
				Instances instances = serverService.getInstances(user);

				// 保持しているサーバにpendingが存在すればrunningになるまで待つ
				if (instances.hasPendingInstance()) {
					sleep();
					continue;
				}

				// 保持しているサーバにpendingが無くなれば次処理を開始する
				isNotAllInstanceRunning = false;
			}

			// 入力されたkeyspaceを登録する
			Instances instances = serverService.getInstances(user);
			return tableService.findAllTableByKeyspace(instances, keyspace);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		return null;
	}

	/**
	 * キースペースを削除
	 */
	public void deleteKeyspace(LoginUser user, Keyspace keyspace) {
		try {
			// キースペースの削除
			deleteService.deleteKeyspace(user, keyspace);

			// 削除後も一つ以上のキースペースを保持しているか判定
			Instances instances = serverService.getInstances(user);
			Keyspaces keyspaces = keyspaceService.findAllKeyspaceWithoutSysKeyspace(instances);
			if (!keyspaces.isEmpty()) {
				return;
			}

			// 全てのキースペースを削除していたらサーバーも削除
			serverService.deleteAllServer(user);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			throw new RuntimeException();
		}
	}
}
