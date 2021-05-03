package com.dbaas.cassandra.app.keyspaceList.service.bean;

import static com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans;
import static com.github.springtestdbunit.annotation.DatabaseOperation.CLEAN_INSERT;

import java.util.ArrayList;
import java.util.List;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;
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
import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.cassandra.keyspace.service.KeyspaceService;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.server.instance.InstancesServiceForTest;
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
public class KeyspaceListInitServiceMockTest {

	@Mock
	private ServerService serverService;

	@Mock
	private CassandraService cassandraService;

	private UserService userService;

	private KeyspaceService keyspaceService;

	private InstancesServiceForTest instancesServiceForTest;

	@InjectMocks
	private KeyspaceListInitService keyspaceListInitService;

	@Autowired
	public KeyspaceListInitServiceMockTest(UserService userService, KeyspaceService keyspaceService,
										   InstancesServiceForTest instancesServiceForTest) {
		this.userService = userService;
		this.keyspaceService = keyspaceService;
		this.instancesServiceForTest = instancesServiceForTest;
	}

	@Test
	public void findCreatedKeyspaceList_サーバが未構築の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		Instances instances = instancesServiceForTest.createMockInstances_サーバ未構築状態();

		// 条件
		// サーバが未構築である
		Mockito.when(serverService.getInstances(user)).thenReturn(instances);

		// テストの実行
		Keyspaces createdKeyspaces= keyspaceListInitService.findCreatedKeyspaceList(user);

		// 検証
		// 登録済みキースペースリストが空であること
		Assertions.assertThat(createdKeyspaces.toStringList().size()).isEqualTo(0);
	}

	@Test
	public void findCreatedKeyspaceList_サーバが構築中の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築中状態();

		// 条件
		// サーバが未構築である
		Mockito.when(serverService.getInstances(user)).thenReturn(instances);

		// テストの実行
		Keyspaces createdKeyspaces = keyspaceListInitService.findCreatedKeyspaceList(user);

		// 検証
		// 登録済みキースペースリストが空であること
		Assertions.assertThat(createdKeyspaces.toStringList().size()).isEqualTo(0);
	}

	@Test
	public void findCreatedKeyspaceList_サーバが構築済_かつキースペースが未登録の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築済状態();
		Keyspaces createdKeyspaces = Keyspaces.createEmptyInstance();

		// 条件
		// サーバが未構築である
		Mockito.when(serverService.getInstances(user)).thenReturn(instances);
		Mockito.when(keyspaceService.findAllKeyspaceWithoutSysKeyspace(instances))
				.thenReturn(createdKeyspaces);

		// テストの実行
		Keyspaces resultList = keyspaceListInitService.findCreatedKeyspaceList(user);

		// 検証
		// 登録済みキースペースリストが空であること
		Assertions.assertThat(resultList.toStringList().size()).isEqualTo(createdKeyspaces.toStringList().size());
	}

	@Test
	public void findCreatedKeyspaceList_サーバが構築済_かつキースペースが登録済の場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築済状態();
		List<Keyspace> keyspaceList = new ArrayList<Keyspace>();
		keyspaceList.add(Keyspace.createInstance("keyspace"));
		Keyspaces createdKeyspaces = Keyspaces.createInstance(keyspaceList);

		// 条件
		// サーバが未構築である
		Mockito.when(serverService.getInstances(user)).thenReturn(instances);
		Mockito.when(keyspaceService.findAllKeyspaceWithoutSysKeyspace(instances))
				.thenReturn(createdKeyspaces);

		// テストの実行
		Keyspaces resultList = keyspaceListInitService.findCreatedKeyspaceList(user);

		// 検証
		// 登録済みキースペースリストの内容がcassandra管理Serviceから取得したキースペースリスト(sys系除く)と一致すること
		Assertions.assertThat(resultList.toStringList().size()).isEqualTo(createdKeyspaces.toStringList().size());
		Assertions.assertThat(resultList.toStringList().get(0)).isEqualTo(createdKeyspaces.toStringList().get(0));
	}

	@Test
	public void refreshCassandra_サーバが未構築_かつキースペースの登録予定がない場合() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		Instances emptyInstances = Instances.createEmptyInstance();
		KeyspaceRegistPlans keyspaceRegistPlans = KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans();

		// 条件
		// サーバが未構築である
		Mockito.when(serverService.getInstances(user)).thenReturn(emptyInstances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraService.canAllExecCql(emptyInstances)).thenReturn(false);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerService.class)).waitCompleteCreateServer(user);
		// TODO whenで指定する引数の書き方を検証する
		// Mockito.doNothing().when(Mockito.mock(ServerManagerService.class)).waitCompleteCreateServer(Mockito.isA(LoginUser.class));
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class))
				.registKeyspaceByDuplicateIgnore(emptyInstances, keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).setup(user, emptyInstances);
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).execCassandraByWait(emptyInstances);
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class))
				.registKeyspaceByDuplicateIgnore(emptyInstances, keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keyspaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
			Assertions.assertThat(true).isEqualTo(true);
		} catch (Exception e) {
			Assertions.assertThat(true).isEqualTo(false);
		}
	}

	@Test
	@DatabaseSetup(value = "/static/dbunit/domain/table/keyspaceRegistPlan/keyspace登録予定有り.xml", type = CLEAN_INSERT)
	public void refreshCassandra_cassandraサーバ未構築_かつキースペースの登録予定がある() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		KeyspaceRegistPlans keyspaceRegistPlans = keyspaceService.findKeyspaceRegistPlanByUserId(user);
		Instances instances = instancesServiceForTest.createMockInstances_サーバ未構築状態();

		// 条件
		// サーバが未構築である
		Mockito.when(serverService.getInstances(user)).thenReturn(instances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraService.canAllExecCql(instances)).thenReturn(false);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerService.class)).waitCompleteCreateServer(user);
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class)).registKeyspaceByDuplicateIgnore(instances,
				keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).setup(user, instances);
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).execCassandraByWait(instances);
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class)).registKeyspaceByDuplicateIgnore(instances,
				keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keyspaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
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
		Mockito.when(serverService.getInstances(user)).thenReturn(instances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraService.canAllExecCql(instances)).thenReturn(false);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerService.class)).waitCompleteCreateServer(user);
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class)).registKeyspaceByDuplicateIgnore(instances,
				keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).setup(user, instances);
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).execCassandraByWait(instances);
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class)).registKeyspaceByDuplicateIgnore(instances,
				keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keyspaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
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
		KeyspaceRegistPlans keyspaceRegistPlans = keyspaceService.findKeyspaceRegistPlanByUserId(user);

		// 条件
		// サーバが構築中である
		Mockito.when(serverService.getInstances(user)).thenReturn(instances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraService.canAllExecCql(instances)).thenReturn(false);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerService.class)).waitCompleteCreateServer(user);
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class)).registKeyspaceByDuplicateIgnore(instances,
				keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).setup(user, instances);
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).execCassandraByWait(instances);
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class)).registKeyspaceByDuplicateIgnore(instances,
				keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keyspaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
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
		Mockito.when(serverService.getInstances(user)).thenReturn(instances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraService.canAllExecCql(instances)).thenReturn(false);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerService.class)).waitCompleteCreateServer(user);
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class)).registKeyspaceByDuplicateIgnore(instances,
				keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).setup(user, instances);
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).execCassandraByWait(instances);
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class)).registKeyspaceByDuplicateIgnore(instances,
				keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keyspaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
			Assertions.assertThat(true).isEqualTo(true);
		} catch (Exception e) {
			Assertions.assertThat(true).isEqualTo(false);
		}
	}

	@Test
	@DatabaseSetup(value = "/static/dbunit/domain/table/keyspaceRegistPlan/keyspace登録予定有り.xml", type = CLEAN_INSERT)
	public void cassandraサーバ構築済_かつキースペースの登録予定がある() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		KeyspaceRegistPlans keyspaceRegistPlans = keyspaceService.findKeyspaceRegistPlanByUserId(user);
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築済状態();

		// 条件
		// サーバが未構築である
		Mockito.when(serverService.getInstances(user)).thenReturn(instances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraService.canAllExecCql(instances)).thenReturn(false);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerService.class)).waitCompleteCreateServer(user);
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class)).registKeyspaceByDuplicateIgnore(instances,
				keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).setup(user, instances);
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).execCassandraByWait(instances);
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class)).registKeyspaceByDuplicateIgnore(instances,
				keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keyspaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
			Assertions.assertThat(true).isEqualTo(true);
		} catch (Exception e) {
			Assertions.assertThat(true).isEqualTo(false);
		}
	}

	@Test
	@DatabaseSetup(value = "/static/dbunit/domain/table/keyspaceRegistPlan/keyspace登録予定有り.xml", type = CLEAN_INSERT)
	public void cassandraサーバ構築済_かつcassandra実行可能_かつキースペースの登録予定がある() {
		// テスト用データ作成
		LoginUser user = userService.findUserByUserId("aaa");
		KeyspaceRegistPlans keyspaceRegistPlans = keyspaceService.findKeyspaceRegistPlanByUserId(user);
		Instances instances = instancesServiceForTest.createMockInstances_サーバ構築済状態();

		// 条件
		// サーバが未構築である
		Mockito.when(serverService.getInstances(user)).thenReturn(instances);
		// 「全cassandraの実行」判定は不可である
		Mockito.when(cassandraService.canAllExecCql(instances)).thenReturn(true);

		// 補足
		// サーバ起動完了待ちメソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(ServerService.class)).waitCompleteCreateServer(user);
		// キースペース登録メソッドはvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class)).registKeyspaceByDuplicateIgnore(instances,
				keyspaceRegistPlans);
		// cassandranのセットアップ〜キースペース登録メソッドまではvoidのため処理しない
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).setup(user, instances);
		Mockito.doNothing().when(Mockito.mock(CassandraService.class)).execCassandraByWait(instances);
		Mockito.doNothing().when(Mockito.mock(KeyspaceService.class)).registKeyspaceByDuplicateIgnore(instances,
				keyspaceRegistPlans);

		// テスト対象の実行と検証
		// voidメソッドのmockテストのため、Exceptionが発生しなければ明示的な検証は無し
		try {
			keyspaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
			Assertions.assertThat(true).isEqualTo(true);
		} catch (Exception e) {
			Assertions.assertThat(true).isEqualTo(false);
		}
	}

}
