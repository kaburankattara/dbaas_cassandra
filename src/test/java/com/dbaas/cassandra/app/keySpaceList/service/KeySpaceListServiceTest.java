package com.dbaas.cassandra.app.keySpaceList.service;

import static com.dbaas.cassandra.app.keySpaceList.dto.KeySpaceListInitServiceResultDtoTest.getKeySpaceListServiceCassandraサーバ未構築_未登録;
import static com.dbaas.cassandra.app.keySpaceList.dto.KeySpaceListInitServiceResultDtoTest.getKeySpaceListServiceCassandraサーバ構築済_登録済;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import com.dbaas.cassandra.app.keySpaceList.dto.KeySpaceListInitServiceResultDto;
import com.dbaas.cassandra.app.keySpaceList.service.async.KeySpaceListAsyncService;
import com.dbaas.cassandra.app.keySpaceList.service.bean.KeySpaceListInitService;
import com.dbaas.cassandra.config.DatasourceConfig;
import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlanService;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.domain.user.User;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;

@SpringBootTest(classes = DbaasCassandraApplication.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ 
	MockitoTestExecutionListener.class,
    TransactionalTestExecutionListener.class, // テストトランザクションの制御。デフォルトで設定により、テスト後にDB状態はロールバックする
    DbUnitTestExecutionListener.class
    })
@Import(DatasourceConfig.class)
public class KeySpaceListServiceTest {
	
    @Mock
    private KeySpaceListInitService keySpaceListInitService;

    @Mock
	private KeySpaceListAsyncService keySpaceListAsyncService;
    
    @Mock
    private CassandraManagerService cassandraManagerService;
	
    @InjectMocks
    private KeySpaceListService keySpaceListService;

    @Autowired
	public KeySpaceListServiceTest(KeyspaceRegistPlanService keyspaceRegistPlanService) {
		this.keySpaceListService = new KeySpaceListService(keySpaceListInitService, keySpaceListAsyncService,
				cassandraManagerService, keyspaceRegistPlanService);
	}
    
    @BeforeAll
    static void initMocks() {
    	System.out.println();
    }

    @BeforeEach
    void beforeEach() {
    	System.out.println();
    }
    
    @Test
	public void cassandraサーバ未構築_未登録() throws Exception {
    	// 比較対象Dtoの生成
		KeySpaceListInitServiceResultDto compTarget = getKeySpaceListServiceCassandraサーバ未構築_未登録();

    	// テスト準備
    	LoginUser loginUser = new LoginUser(new User());
    	loginUser.setUserId("aaa");
    	loginUser.setUserName("test");
    	loginUser.setPassword("");
		Mockito.when(keySpaceListInitService.findCreatedKeyspaceList(loginUser)).thenReturn(compTarget.getCreatedKeyspaceList());

		// テスト対象の実行
		KeySpaceListInitServiceResultDto checkTarget = keySpaceListService.init(loginUser);

		// テスト対象の検証
		Assertions.assertThat(checkTarget.getKeyspaceList().size()).isEqualTo(compTarget.getKeyspaceList().size());
   		Assertions.assertThat(checkTarget.getCreatedKeyspaceList().size()).isEqualTo(compTarget.getCreatedKeyspaceList().size());
	}

    @Test
	public void cassandraサーバ未構築_登録形跡がある() {
		
	}
    
    @Test
	public void cassandraサーバ構築中_未登録() {
		
	}
    
    @Test
	public void cassandraサーバ構築中_登録形跡がある() {
		
	}
    
    @Test
	public void cassandraサーバ構築済_未登録() {
		
	}
    
    @Test
	public void cassandraサーバ構築済_登録形跡がある() {
		
	}
    
    /**
     * サーバが構築され、かつキースペースがcassandraサーバに登録済みの場合
     * 登録済みのキースペースリストを取得したDtoが返却されること
     */
    @Test
    @Transactional
    @DatabaseSetups(@DatabaseSetup(value = "/static/dbunit/app/keySpaceList/cassandraサーバ構築済_登録済_setup.xml", type = DatabaseOperation.CLEAN_INSERT))
	public void cassandraサーバ構築済_登録済() {
    	// 比較対象Dtoの生成
		KeySpaceListInitServiceResultDto compTarget = getKeySpaceListServiceCassandraサーバ構築済_登録済();
		
    	// テスト準備
    	User user = new User();
    	user.setUserId("aaa");
    	user.setUserName("test");
    	user.setPassword("");
    	LoginUser loginUser = new LoginUser(user);
		Mockito.when(keySpaceListInitService.findCreatedKeyspaceList(loginUser)).thenReturn(compTarget.getCreatedKeyspaceList());
		
		// テスト対象の実行
		KeySpaceListInitServiceResultDto checkTarget;
		try {
			checkTarget = keySpaceListService.init(loginUser);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
	   		Assertions.assertThat(true).isEqualTo(false);
	   		return;
		}
			
		// テスト対象の検証
   		Assertions.assertThat(checkTarget.getKeyspaceList().size()).isEqualTo(compTarget.getKeyspaceList().size());
   		Assertions.assertThat(checkTarget.getCreatedKeyspaceList().size()).isEqualTo(compTarget.getCreatedKeyspaceList().size());
	}

    @AfterEach
    void afterEach() {
        System.out.println("  JUnit5Test#afterEach()");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("JUnit5Test#afterAll()");
    }
}
