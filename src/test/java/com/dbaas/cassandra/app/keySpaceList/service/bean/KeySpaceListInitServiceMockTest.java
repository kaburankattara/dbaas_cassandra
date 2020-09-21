package com.dbaas.cassandra.app.keySpaceList.service.bean;

import static com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans;
import static com.github.springtestdbunit.annotation.DatabaseOperation.CLEAN_INSERT;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlanService;
import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;
import com.dbaas.cassandra.domain.serverManager.instance.InstancesServiceForTest;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.domain.user.UserService;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@SpringBootTest(classes = DbaasCassandraApplication.class)
@ExtendWith(SpringExtension.class) // dbunit、DIコンテナを使用しないならこれだけで良い
@Transactional
@TestExecutionListeners({ MockitoTestExecutionListener.class, TransactionalTestExecutionListener.class // テストトランザクションの制御。デフォルトで設定により、テスト後にDB状態はロールバックする
		, DbUnitTestExecutionListener.class })
@Import(DatasourceConfig.class)
@DatabaseSetup(value = "/static/dbunit/domain/user/テストユーザー.xml", type = CLEAN_INSERT)
public class KeySpaceListInitServiceMockTest {

	@Mock
	private ServerManagerService serverManagerService;

	@Mock
	private CassandraManagerService cassandraManagerService;

	private UserService userService;

	private KeyspaceRegistPlanService keyspaceRegistPlanService;

	private InstancesServiceForTest instancesServiceForTest;

	@InjectMocks
	private KeySpaceListInitService keySpaceListInitService;

	@Autowired
	public KeySpaceListInitServiceMockTest(UserService userService, KeyspaceRegistPlanService keyspaceRegistPlanService,
			InstancesServiceForTest instancesServiceForTest) {
		this.userService = userService;
		this.keyspaceRegistPlanService = keyspaceRegistPlanService;
		this.instancesServiceForTest = instancesServiceForTest;
	}

	@Test
	public void findCreatedKeyspaceList_サーバが未構築の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		Instances instances = instancesServiceForTest.createMockInstances_サーバ未構築状態();

		// 条件
		// サーバが未構築である
		Mockito.when(serverManagerService.getInstances(user)).thenReturn(instances);

		// テストの実行
		List<String> createdKeySpaceList = keySpaceListInitService.findCreatedKeyspaceList(user);

		// 検証
		// 登録済みキースペースリストが空であること
		Assertions.assertThat(createdKeySpaceList.size()).isEqualTo(0);
	}

	@Test
	public void findCreatedKeyspaceList_サーバが構築中の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築中状態();

		// 条件
		// サーバが未構築である
		Mockito.when(serverManagerService.getInstances(user)).thenReturn(instances);

		// テストの実行
		List<String> createdKeySpaceList = keySpaceListInitService.findCreatedKeyspaceList(user);

		// 検証
		// 登録済みキースペースリストが空であること
		Assertions.assertThat(createdKeySpaceList.size()).isEqualTo(0);
	}

	@Test
	public void findCreatedKeyspaceList_サーバが構築済_かつキースペースが未登録の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築済状態();
		List<String> createdKeyspaceList = new ArrayList<String>();

		// 条件
		// サーバが未構築である
		Mockito.when(serverManagerService.getInstances(user)).thenReturn(instances);
		Mockito.when(cassandraManagerService.findAllKeySpaceWithoutSysKeySpace(instances))
				.thenReturn(createdKeyspaceList);

		// テストの実行
		List<String> resultList = keySpaceListInitService.findCreatedKeyspaceList(user);

		// 検証
		// 登録済みキースペースリストが空であること
		Assertions.assertThat(resultList.size()).isEqualTo(createdKeyspaceList.size());
	}

	@Test
	public void findCreatedKeyspaceList_サーバが構築済_かつキースペースが登録済の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築済状態();
		List<String> createdKeyspaceList = new ArrayList<String>();
		createdKeyspaceList.add("keyspace");

		// 条件
		// サーバが未構築である
		Mockito.when(serverManagerService.getInstances(user)).thenReturn(instances);
		Mockito.when(cassandraManagerService.findAllKeySpaceWithoutSysKeySpace(instances))
				.thenReturn(createdKeyspaceList);

		// テストの実行
		List<String> resultList = keySpaceListInitService.findCreatedKeyspaceList(user);

		// 検証
		// 登録済みキースペースリストの内容がcassandra管理Serviceから取得したキースペースリスト(sys系除く)と一致すること
		Assertions.assertThat(resultList.size()).isEqualTo(createdKeyspaceList.size());
		Assertions.assertThat(resultList.get(0)).isEqualTo(createdKeyspaceList.get(0));
	}

	@Test
	public void refreshCassandra_サーバが未構築_かつキースペースの登録予定がない場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		Instances emptyInstances = Instances.createEmptyInstance();
		KeyspaceRegistPlans keyspaceRegistPlans = KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans();

		// 条件
		// サーバが未構築である
		Mockito.when(serverManagerService.getInstances(user)).thenReturn(emptyInstances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraManagerService.canAllExecCql(emptyInstances)).thenReturn(false);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerManagerService.class)).waitCompleteCreateServer(user);
		// TODO whenで指定する引数の書き方を検証する
		// Mockito.doNothing().when(Mockito.mock(ServerManagerService.class)).waitCompleteCreateServer(Mockito.isA(LoginUser.class));
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class))
				.registKeySpaceByDuplicatIgnore(emptyInstances, keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).setup(user, emptyInstances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).execCassandraByWait(emptyInstances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class))
				.registKeySpaceByDuplicatIgnore(emptyInstances, keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keySpaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
			Assertions.assertThat(true).isEqualTo(true);
		} catch (Exception e) {
			Assertions.assertThat(true).isEqualTo(false);
		}
	}

	@Test
	@DatabaseSetup(value = "/static/dbunit/domain/table/keyspaceManager/keyspace登録予定有り.xml", type = CLEAN_INSERT)
	public void refreshCassandra_cassandraサーバ未構築_かつキースペースの登録予定がある() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		KeyspaceRegistPlans keyspaceRegistPlans = keyspaceRegistPlanService.findKeyspaceRegistPlanByUserId(user);
		Instances instances = instancesServiceForTest.createMockInstances_サーバ未構築状態();

		// 条件
		// サーバが未構築である
		Mockito.when(serverManagerService.getInstances(user)).thenReturn(instances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraManagerService.canAllExecCql(instances)).thenReturn(false);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerManagerService.class)).waitCompleteCreateServer(user);
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).registKeySpaceByDuplicatIgnore(instances,
				keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).setup(user, instances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).execCassandraByWait(instances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).registKeySpaceByDuplicatIgnore(instances,
				keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keySpaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
			Assertions.assertThat(true).isEqualTo(true);
		} catch (Exception e) {
			Assertions.assertThat(true).isEqualTo(false);
		}
	}

	@Test
	public void cassandraサーバ構築中_かつキースペースの登録予定がない() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		KeyspaceRegistPlans keyspaceRegistPlans = createEmptyKeyspaceRegistPlans();
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築中状態();

		// 条件
		// サーバが構築中である
		Mockito.when(serverManagerService.getInstances(user)).thenReturn(instances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraManagerService.canAllExecCql(instances)).thenReturn(false);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerManagerService.class)).waitCompleteCreateServer(user);
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).registKeySpaceByDuplicatIgnore(instances,
				keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).setup(user, instances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).execCassandraByWait(instances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).registKeySpaceByDuplicatIgnore(instances,
				keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keySpaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
			Assertions.assertThat(true).isEqualTo(true);
		} catch (Exception e) {
			Assertions.assertThat(true).isEqualTo(false);
		}
	}

	@Test
	public void cassandraサーバ構築中_かつキースペースの登録予定がある() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築中状態();
		KeyspaceRegistPlans keyspaceRegistPlans = keyspaceRegistPlanService.findKeyspaceRegistPlanByUserId(user);

		// 条件
		// サーバが構築中である
		Mockito.when(serverManagerService.getInstances(user)).thenReturn(instances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraManagerService.canAllExecCql(instances)).thenReturn(false);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerManagerService.class)).waitCompleteCreateServer(user);
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).registKeySpaceByDuplicatIgnore(instances,
				keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).setup(user, instances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).execCassandraByWait(instances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).registKeySpaceByDuplicatIgnore(instances,
				keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keySpaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
			Assertions.assertThat(true).isEqualTo(true);
		} catch (Exception e) {
			Assertions.assertThat(true).isEqualTo(false);
		}
	}

	@Test
	public void cassandraサーバ構築済_かつキースペースの登録予定がない() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築済状態();
		KeyspaceRegistPlans keyspaceRegistPlans = createEmptyKeyspaceRegistPlans();

		// 条件
		// サーバが構築中である
		Mockito.when(serverManagerService.getInstances(user)).thenReturn(instances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraManagerService.canAllExecCql(instances)).thenReturn(false);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerManagerService.class)).waitCompleteCreateServer(user);
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).registKeySpaceByDuplicatIgnore(instances,
				keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).setup(user, instances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).execCassandraByWait(instances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).registKeySpaceByDuplicatIgnore(instances,
				keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keySpaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
			Assertions.assertThat(true).isEqualTo(true);
		} catch (Exception e) {
			Assertions.assertThat(true).isEqualTo(false);
		}
	}

	@Test
	@DatabaseSetup(value = "/static/dbunit/domain/table/keyspaceManager/keyspace登録予定有り.xml", type = CLEAN_INSERT)
	public void cassandraサーバ構築済_かつキースペースの登録予定がある() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		KeyspaceRegistPlans keyspaceRegistPlans = keyspaceRegistPlanService.findKeyspaceRegistPlanByUserId(user);
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築済状態();

		// 条件
		// サーバが未構築である
		Mockito.when(serverManagerService.getInstances(user)).thenReturn(instances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraManagerService.canAllExecCql(instances)).thenReturn(false);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerManagerService.class)).waitCompleteCreateServer(user);
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).registKeySpaceByDuplicatIgnore(instances,
				keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).setup(user, instances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).execCassandraByWait(instances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).registKeySpaceByDuplicatIgnore(instances,
				keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keySpaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
			Assertions.assertThat(true).isEqualTo(true);
		} catch (Exception e) {
			Assertions.assertThat(true).isEqualTo(false);
		}
	}

	@Test
	@DatabaseSetup(value = "/static/dbunit/domain/table/keyspaceManager/keyspace登録予定有り.xml", type = CLEAN_INSERT)
	public void cassandraサーバ構築済_かつcassandra実行可能_かつキースペースの登録予定がある() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		KeyspaceRegistPlans keyspaceRegistPlans = keyspaceRegistPlanService.findKeyspaceRegistPlanByUserId(user);
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築済状態();

		// 条件
		// サーバが未構築である
		Mockito.when(serverManagerService.getInstances(user)).thenReturn(instances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraManagerService.canAllExecCql(instances)).thenReturn(true);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerManagerService.class)).waitCompleteCreateServer(user);
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).registKeySpaceByDuplicatIgnore(instances,
				keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).setup(user, instances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).execCassandraByWait(instances);
		Mockito.doNothing().when(Mockito.mock(CassandraManagerService.class)).registKeySpaceByDuplicatIgnore(instances,
				keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keySpaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
			Assertions.assertThat(true).isEqualTo(true);
		} catch (Exception e) {
			Assertions.assertThat(true).isEqualTo(false);
		}
	}

}
