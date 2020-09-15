package com.dbaas.cassandra.app.keySpaceUpdater.service;

import static com.dbaas.cassandra.utils.ObjectUtils.isNotEmpty;
import static com.dbaas.cassandra.utils.ThreadUtils.sleep;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.app.keySpaceUpdater.service.bean.KeySpaceUpdaterDeleteService;
import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.cassandra.table.Tables;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class KeySpaceUpdaterService {

	private KeySpaceUpdaterDeleteService deleteService;

	private ServerManagerService serverManagerService;

	private CassandraManagerService cassandraManagerService;

	@Autowired
	KeySpaceUpdaterService(KeySpaceUpdaterDeleteService deleteService, ServerManagerService serverManagerService,
			CassandraManagerService cassandraManagerService) {
		this.deleteService = deleteService;
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
	}

	/**
	 * テーブル一覧を取得
	 */
	public Tables findTable(LoginUser user, String keySpace) {
		try {
			// 保持しているサーバの状態が全てrunningになるまで待つ
			boolean isNotAllInstanceRunning = true;
			while (isNotAllInstanceRunning) {
				// ユーザに紐づくEC2リストを取得
				Instances instances = serverManagerService.getInstances(user);

				// 保持しているサーバにpendingが存在すればrunningになるまで待つ
				if (instances.hasPendingInstance()) {
					sleep();
					continue;
				}

				// 保持しているサーバにpendingが無くなれば次処理を開始する
				isNotAllInstanceRunning = false;
			}

			// 入力されたkeySpaceを登録する
			Instances instances = serverManagerService.getInstances(user);
			return cassandraManagerService.findAllTableByKeySpace(instances, keySpace);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		return null;
	}

	/**
	 * キースペースを削除
	 */
	public void deleteKeySpace(LoginUser user, String keySpace) {
		try {
			// キースペースの削除
			deleteService.deleteKeySpace(user, keySpace);

			// 削除後も一つ以上のキースペースを保持しているか判定
			Instances instances = serverManagerService.getInstances(user);
			List<String> keySpaceList = cassandraManagerService.findAllKeySpaceWithoutSysKeySpace(instances);
			if (isNotEmpty(keySpaceList)) {
				return;
			}

			// 全てのキースペースを削除していたらサーバーも削除
			serverManagerService.deleteAllServer(user);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			throw new RuntimeException();
		}
	}
}
