package com.dbaas.cassandra.app.keyspaceList.service;

import static com.dbaas.cassandra.app.keyspaceList.dto.KeyspaceListInitServiceResultDtoTest.getKeyspaceListServiceCassandraサーバ未構築_未登録;
import static com.dbaas.cassandra.app.keyspaceList.dto.KeyspaceListInitServiceResultDtoTest.getKeyspaceListServiceCassandraサーバ構築済_登録済;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dbaas.cassandra.app.keyspaceList.dto.KeyspaceListInitServiceResultDto;
import com.dbaas.cassandra.app.keyspaceList.service.async.KeyspaceListAsyncService;
import com.dbaas.cassandra.app.keyspaceList.service.bean.KeyspaceListInitService;
import com.dbaas.cassandra.domain.cassandra.keyspace.service.KeyspaceService;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.domain.user.User;

@ExtendWith(SpringExtension.class)
public class KeyspaceListServiceMockTest {
	
    @Mock
    private KeyspaceListInitService keyspaceListInitService;

    @Mock
	private KeyspaceListAsyncService keyspaceListAsyncService;
	
    @Mock
	private KeyspaceService keyspaceService;
    
    @InjectMocks
    private KeyspaceListService keyspaceListService;

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
		KeyspaceListInitServiceResultDto compTarget = getKeyspaceListServiceCassandraサーバ未構築_未登録();

    	// テスト準備
    	LoginUser loginUser = new LoginUser(new User());
    	loginUser.setUserId("aaa");
    	loginUser.setUserName("test");
    	loginUser.setPassword("");
		Mockito.when(keyspaceListInitService.findCreatedKeyspaceList(loginUser)).thenReturn(compTarget.getCreatedKeyspaces());
		Mockito.when(keyspaceService.findKeyspaceRegistPlanByUserId(loginUser)).thenReturn(compTarget.getKeyspaceRegistPlans());
				
		// テスト対象の実行
		KeyspaceListInitServiceResultDto checkTarget = keyspaceListService.init(loginUser);

		// テスト対象の検証
		Assertions.assertThat(checkTarget.getKeyspaceList().size()).isEqualTo(compTarget.getKeyspaceList().size());
   		Assertions.assertThat(checkTarget.getCreatedKeyspaces().toStringList().size()).isEqualTo(compTarget.getCreatedKeyspaces().toStringList().size());
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
	public void cassandraサーバ構築済_登録済() {
    	// 比較対象Dtoの生成
		KeyspaceListInitServiceResultDto compTarget = getKeyspaceListServiceCassandraサーバ構築済_登録済();
		
    	// テスト準備
    	User user = new User();
    	user.setUserId("aaa");
    	user.setUserName("test");
    	user.setPassword("");
    	LoginUser loginUser = new LoginUser(user);
		Mockito.when(keyspaceListInitService.findCreatedKeyspaceList(loginUser)).thenReturn(compTarget.getCreatedKeyspaces());
		Mockito.when(keyspaceService.findKeyspaceRegistPlanByUserId(loginUser)).thenReturn(compTarget.getKeyspaceRegistPlans());
		
		// テスト対象の実行
		KeyspaceListInitServiceResultDto checkTarget;
		try {
			checkTarget = keyspaceListService.init(loginUser);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
	   		Assertions.assertThat(true).isEqualTo(false);
	   		return;
		}
		
		// テスト対象の検証
   		Assertions.assertThat(checkTarget.getKeyspaceList().size()).isEqualTo(compTarget.getKeyspaceList().size());
   		Assertions.assertThat(checkTarget.getCreatedKeyspaces().toStringList().size()).isEqualTo(compTarget.getCreatedKeyspaces().toStringList().size());
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
