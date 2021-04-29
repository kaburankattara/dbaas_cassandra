package com.dbaas.cassandra.app.keySpaceUpdater.service;

import static com.dbaas.cassandra.utils.ObjectUtils.isNotEmpty;
import static com.dbaas.cassandra.utils.ThreadUtils.sleep;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.app.keySpaceUpdater.service.bean.KeySpaceUpdaterDeleteService;
import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.cassandra.table.Tables;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class KeySpaceUpdaterService {

	private KeySpaceUpdaterDeleteService deleteService;

	private ServerService serverService;

	private CassandraService cassandraService;

	@Autowired
	KeySpaceUpdaterService(KeySpaceUpdaterDeleteService deleteService, ServerService serverService,
			CassandraService cassandraService) {
		this.deleteService = deleteService;
		this.serverService = serverService;
		this.cassandraService = cassandraService;
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
				Instances instances = serverService.getInstances(user);

				// 保持しているサーバにpendingが存在すればrunningになるまで待つ
				if (instances.hasPendingInstance()) {
					sleep();
					continue;
				}

				// 保持しているサーバにpendingが無くなれば次処理を開始する
				isNotAllInstanceRunning = false;
			}

			// 入力されたkeySpaceを登録する
			Instances instances = serverService.getInstances(user);
			return cassandraService.findAllTableByKeySpace(instances, keySpace);

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
			Instances instances = serverService.getInstances(user);
			List<String> keySpaceList = cassandraService.findAllKeySpaceWithoutSysKeySpace(instances);
			if (isNotEmpty(keySpaceList)) {
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
