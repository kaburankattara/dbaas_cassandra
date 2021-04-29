package com.dbaas.cassandra.domain.server;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.getSysDateYyyymmddhhmmssSSSSSS;
import static com.dbaas.cassandra.domain.sysDate.SysDateContext.setSysDate;
import static com.dbaas.cassandra.utils.DateUtils.isDateByYyyymmddhhmmssSSSSSS;
import static com.github.springtestdbunit.annotation.DatabaseOperation.CLEAN_INSERT;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.DbaasCassandraApplication;
import com.dbaas.cassandra.config.DatasourceConfig;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.table.common.BaseDao;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.domain.user.UserService;
import com.dbaas.cassandra.utils.StringUtils;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@SpringBootTest(classes = DbaasCassandraApplication.class)
@ExtendWith(SpringExtension.class) // dbunit、DIコンテナを使用しないならこれだけで良い
@Transactional
@TestExecutionListeners({ MockitoTestExecutionListener.class, TransactionalTestExecutionListener.class // テストトランザクションの制御。デフォルトで設定により、テスト後にDB状態はロールバックする
		, DbUnitTestExecutionListener.class })
@Import(DatasourceConfig.class)
@DatabaseSetup(value = "/static/dbunit/domain/user/テストユーザー.xml", type = CLEAN_INSERT)
public class ServerServiceTest {
	
	private BaseDao baseDao;

	private UserService userService;

	private ServerService serverService;

	@Autowired
	public ServerServiceTest(ServerService serverService, BaseDao baseDao, UserService userService) {
		this.serverService = serverService;
		this.baseDao = baseDao;
		this.userService = userService;
	}

	@Test
	public void getAllInstances_ユーザーに紐づくEC2インスタンスが未構築の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくインスタンスが無いことを検証するため、テストユーザーのIDに日時を付加する
		// ※条件付加方法は不完全のため、JUnit実施前にインスタンス構築状況を確認すること
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());

		// テストの実行
		Instances instances = serverService.getAllInstances(user);
		
		// 検証
		// 取得出来たインスタンスが0であること
		Assertions.assertThat(instances.isEmpty()).isEqualTo(true);
	}
	
	@Test
	public void getAllInstances_ユーザーに紐づくEC2インスタンスが構築中の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくインスタンスが作成中であることを検証するため、テストユーザーのIDに日時を付加する
		// ※条件付加方法は不完全のため、JUnit実施前にインスタンス構築状況を確認すること
		// 作成中はアプリケーション側で制御が出来ないため、ブレークポイントで必ず止めないこと！！
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());
		serverService.createServer(user);

		// テストの実行
		Instances instances = serverService.getAllInstances(user);
		
		// 検証
		// 取得出来たインスタンスが`1以上であること
		Assertions.assertThat(instances.isEmpty()).isEqualTo(false);
		// 取得結果にpendingステータスのインスタンスが含まれていること
		Assertions.assertThat(instances.hasPendingInstance()).isEqualTo(true);
		
		// 後処理
		// テストで作成したインスタンスは削除しておく
		serverService.deleteAllServer(user);
	}

	@Test
	public void getAllInstances_ユーザーに紐づくEC2インスタンスが構築済の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくインスタンスが作成中であることを検証するため、テストユーザーのIDに日時を付加する
		// ※条件付加方法は不完全のため、JUnit実施前にインスタンス構築状況を確認すること
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());
		serverService.createServer(user);
		// サーバ構築完了まで待つ
		serverService.waitCompleteCreateServer(user);

		// テストの実行
		Instances instances = serverService.getAllInstances(user);
		
		// 検証
		// 取得出来たインスタンスが`1以上であること
		Assertions.assertThat(instances.isEmpty()).isEqualTo(false);
		// 取得結果にrunningステータスのインスタンスが含まれていること
		Assertions.assertThat(instances.hasRunningInstance()).isEqualTo(true);
		
		// 後処理
		// テストで作成したインスタンスは削除しておく
		serverService.deleteAllServer(user);
	}

	@Test
	public void getAllInstances_ユーザーに紐づくEC2インスタンスが削除済の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくインスタンスが作成中であることを検証するため、テストユーザーのIDに日時を付加する
		// ※条件付加方法は不完全のため、JUnit実施前にインスタンス構築状況を確認すること
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());
		serverService.createServer(user);
		// サーバ構築完了まで待ち、サーバを削除
		serverService.waitCompleteCreateServer(user);
		serverService.deleteAllServer(user);

		// テストの実行
		Instances instances = serverService.getAllInstances(user);
		
		// 検証
		// 取得出来たインスタンスが`1以上であること
		Assertions.assertThat(instances.isEmpty()).isEqualTo(false);
		// 取得結果に削除系ステータスのインスタンスが含まれていること
		Assertions.assertThat(instances.hasShuttingDownToTerminatedInstance()).isEqualTo(true);
	}

	@Test
	public void getInstances_ユーザーに紐づくEC2インスタンスが未構築の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくインスタンスが無いことを検証するため、テストユーザーのIDに日時を付加する
		// ※条件付加方法は不完全のため、JUnit実施前にインスタンス構築状況を確認すること
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());

		// テストの実行
		Instances instances = serverService.getInstances(user);
		
		// 検証
		// 取得出来たインスタンスが0であること
		Assertions.assertThat(instances.isEmpty()).isEqualTo(true);
	}

	@Test
	public void getInstances_ユーザーに紐づくEC2インスタンスが構築中の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくインスタンスが作成中であることを検証するため、テストユーザーのIDに日時を付加する
		// ※条件付加方法は不完全のため、JUnit実施前にインスタンス構築状況を確認すること
		// 作成中はアプリケーション側で制御が出来ないため、ブレークポイントで必ず止めないこと！！
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());
		serverService.createServer(user);

		// テストの実行
		Instances instances = serverService.getInstances(user);
		
		// 検証
		// 取得出来たインスタンスが`1以上であること
		Assertions.assertThat(instances.isEmpty()).isEqualTo(false);
		// 取得結果にpendingステータスのインスタンスが含まれていること
		Assertions.assertThat(instances.hasPendingInstance()).isEqualTo(true);
		
		// 後処理
		// テストで作成したインスタンスは削除しておく
		serverService.deleteAllServer(user);
	}

	@Test
	public void getInstances_ユーザーに紐づくEC2インスタンスが構築済の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくインスタンスが作成中であることを検証するため、テストユーザーのIDに日時を付加する
		// ※条件付加方法は不完全のため、JUnit実施前にインスタンス構築状況を確認すること
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());
		serverService.createServer(user);
		// サーバ構築完了まで待つ
		serverService.waitCompleteCreateServer(user);

		// テストの実行
		Instances instances = serverService.getInstances(user);
		
		// 検証
		// 取得出来たインスタンスが`1以上であること
		Assertions.assertThat(instances.isEmpty()).isEqualTo(false);
		// 取得結果にrunningステータスのインスタンスが含まれていること
		Assertions.assertThat(instances.hasRunningInstance()).isEqualTo(true);
		
		// 後処理
		// テストで作成したインスタンスは削除しておく
		serverService.deleteAllServer(user);
	}

	@Test
	public void getInstances_ユーザーに紐づくEC2インスタンスが削除済の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくインスタンスが作成中であることを検証するため、テストユーザーのIDに日時を付加する
		// ※条件付加方法は不完全のため、JUnit実施前にインスタンス構築状況を確認すること
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());
		serverService.createServer(user);
		// サーバ構築完了まで待ち、サーバを削除
		serverService.waitCompleteCreateServer(user);
		serverService.deleteAllServer(user);

		// テストの実行
		Instances instances = serverService.getInstances(user);
		
		// 検証
		// 取得出来たインスタンスが`1以上であること
		Assertions.assertThat(instances.isEmpty()).isEqualTo(true);
		// 取得結果に削除系ステータスのインスタンスが含まれていること
		Assertions.assertThat(instances.hasShuttingDownToTerminatedInstance()).isEqualTo(false);
	}
	
	@Test
	public void createServer_サーバを構築した場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくインスタンスが作成中であることを検証するため、テストユーザーのIDに日時を付加する
		// ※条件付加方法は不完全のため、JUnit実施前にインスタンス構築状況を確認すること
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());

		// テストの実行
		serverService.createServer(user);
		
		// 検証
		// インスタンスが1以上取得出来ること
		Instances instances = serverService.getInstances(user);
		Assertions.assertThat(instances.isEmpty()).isEqualTo(false);
		// インスタンスIDの形式を検証
		for (Instance instance : instances.getInstanceList()) {
			// インスタンスIDが「ユーザーID_システム日時」となっていること
			String[] instanceNameParse = StringUtils.split(instance.getName(), '_');
			Assertions.assertThat(user.getUserId()).isEqualTo(instanceNameParse[0]);
			Assertions.assertThat(isDateByYyyymmddhhmmssSSSSSS(instanceNameParse[1])).isEqualTo(true);
			// 作成したインスタンスのステータスがPendingまたはRunningであること
			Assertions.assertThat(instance.isPending() || instance.isRunning()).isEqualTo(true);
		}
		
		// 後処理
		// テストで作成したインスタンスは削除しておく
		serverService.deleteAllServer(user);
	}
	
	@Test
	public void waitCompleteCreateServer_EC2インスタンスがない場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// インスタンスが存在しない。または削除中の場合
		serverService.deleteAllServer(user);

		// テストの実行
		serverService.waitCompleteCreateServer(user);
		
		// 検証
		// 例外を発生させることなく処理が終了し、取得出来るインスタンスが0であること
		Instances instances = serverService.getInstances(user);
		Assertions.assertThat(instances.isEmpty()).isEqualTo(true);
	}
	
	@Test
	public void waitCompleteCreateServer_サーバ起動直後の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくEC2インスタンスを作成する
		// ※テストがしやすいようにユーザーIDにシステム日付を付加しておく
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());
		serverService.createServer(user);

		// テストの実行
		serverService.waitCompleteCreateServer(user);
		
		// 検証
		// インスタンスが1以上取得出来ること
		Instances instances = serverService.getInstances(user);
		Assertions.assertThat(instances.isEmpty()).isEqualTo(false);
		// pendingステータスのインスタンスが存在しないこと
		Assertions.assertThat(instances.hasPendingInstance()).isEqualTo(false);
		
		// 後処理
		// テストで作成したインスタンスは削除しておく
		serverService.deleteAllServer(user);
	}
	
	@Test
	public void waitCompleteCreateServer_サーバがRunningになってからの場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくEC2インスタンスを作成する
		// ※テストがしやすいようにユーザーIDにシステム日付を付加しておく
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());
		serverService.createServer(user);
		// EC2インスタンスがRunnigステータスになるまで待つ
		serverService.waitCompleteCreateServer(user);

		// テストの実行
		serverService.waitCompleteCreateServer(user);
		
		// 検証
		// インスタンスが1以上取得出来ること
		Instances instances = serverService.getInstances(user);
		Assertions.assertThat(instances.isEmpty()).isEqualTo(false);
		// pendingステータスのインスタンスが存在しないこと
		Assertions.assertThat(instances.hasPendingInstance()).isEqualTo(false);
		
		// 後処理
		// テストで作成したインスタンスは削除しておく
		serverService.deleteAllServer(user);
	}
	
	@Test
	public void deleteServer_複数のインスタンスが存在する中_特定のインスタンスを削除した場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくEC2インスタンスを削除用、削除しない用で2つ作成する
		// ※テストがしやすいようにユーザーIDにシステム日付を付加しておく
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());
		serverService.createServer(user);
		serverService.createServer(user);
		serverService.waitCompleteCreateServer(user);
		Instances instances = serverService.getInstances(user);
		Instance deleteInstance = instances.getInstanceList().get(0);
		Instance notDeleteInstance = instances.getInstanceList().get(1);

		// テストの実行
		serverService.deleteServer(deleteInstance);
		
		// 検証
		// インスタンスが1以上取得出来ること
		Instances resultInstances = serverService.getInstances(user);
		Assertions.assertThat(resultInstances.isEmpty()).isEqualTo(false);
		// 各インスタンスごとの検証
		for (Instance resultInstance : resultInstances.getInstanceList()) {
			// 削除対象のインスタンスは存在しないこと
			Assertions.assertThat(StringUtils.isEquals(resultInstance.getInstanceId(), deleteInstance.getInstanceId())).isEqualTo(false);
			// 削除対象外のインスタンスは存在すること
			Assertions.assertThat(resultInstance.getInstanceId()).isEqualTo(notDeleteInstance.getInstanceId());
		}
		
		// 後処理
		// テストで作成したインスタンスは削除しておく
		serverService.deleteAllServer(user);
	}
	
	@Test
	public void deleteAllServer() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		setSysDate(baseDao.getSysDate());
		
		// 条件
		// ユーザーに紐づくEC2インスタンスを2つ作成する
		// ※テストがしやすいようにユーザーIDにシステム日付を付加しておく
		user.setUserId(user.getUserId() + getSysDateYyyymmddhhmmssSSSSSS());
		serverService.createServer(user);
		serverService.createServer(user);
		serverService.waitCompleteCreateServer(user);
		Instances instances = serverService.getInstances(user);

		// テストの実行
		serverService.deleteAllServer(user);
		
		// 検証
		// テスト対象メソッド実行前はEC2インスタンス数が2つであること
		Assertions.assertThat(instances.getInstanceList().size()).isEqualTo(2);
		// テスト対象メソッド実行後はEC2インスタンス数が0であること
		Instances resultInstances = serverService.getInstances(user);
		Assertions.assertThat(resultInstances.isEmpty()).isEqualTo(true);
	}
}
