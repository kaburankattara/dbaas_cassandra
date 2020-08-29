package com.dbaas.cassandra.app.keySpaceList.service;

import static com.dbaas.cassandra.app.keySpaceList.dto.KeySpaceListInitServiceResultDtoTest.getKeySpaceListServiceCassandraサーバ未構築_未登録;
import static com.dbaas.cassandra.app.keySpaceList.dto.KeySpaceListInitServiceResultDtoTest.getKeySpaceListServiceCassandraサーバ構築済_登録済;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dbaas.cassandra.app.keySpaceList.dto.KeySpaceListInitServiceResultDto;
import com.dbaas.cassandra.app.keySpaceList.service.async.KeySpaceListAsyncService;
import com.dbaas.cassandra.app.keySpaceList.service.bean.KeySpaceListInitService;
import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.table.keyspaceManager.KeyspaceManagerDao;
import com.dbaas.cassandra.domain.user.User;

@ExtendWith(SpringExtension.class)
public class  KeySpaceListServiceTest {
	
    @Mock
    private KeySpaceListInitService keySpaceListInitService;

    @Mock
	private KeySpaceListAsyncService keySpaceListAsyncService;
	
    @Mock
	private KeyspaceManagerDao keyspaceManagerDao;
    
    @InjectMocks
    private KeySpaceListService keySpaceListService;

//    @Before
//    private void initMocks() {
//        // @Beforeが付与されているメソッドのなかにinitMocksメソッドを書くことで、各テストメソッドを実行する前に毎回実行できる
//        MockitoAnnotations.initMocks(this);
//    }
    
    @Test
	public void cassandraサーバ未構築_未登録() throws Exception {
    	// 比較対象Dtoの生成
		KeySpaceListInitServiceResultDto compTarget = getKeySpaceListServiceCassandraサーバ未構築_未登録();

    	// テスト準備
		User user = new User();
    	user.setUserId("aaa");
    	user.setUserName("test");
    	user.setPassword("");
    	LoginUser loginUser = new LoginUser(user, null);
		Mockito.when(keySpaceListInitService.findCreatedKeyspaceList(loginUser)).thenReturn(compTarget.getCreatedKeyspaceList());
		Mockito.when(keyspaceManagerDao.findAllKeyspaceByUserId(loginUser)).thenReturn(compTarget.getKeyspaceList());
				
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
	public void cassandraサーバ構築済_登録済() {
    	// 比較対象Dtoの生成
		KeySpaceListInitServiceResultDto compTarget = getKeySpaceListServiceCassandraサーバ構築済_登録済();
		
    	// テスト準備
    	User user = new User();
    	user.setUserId("aaa");
    	user.setUserName("test");
    	user.setPassword("");
    	LoginUser loginUser = new LoginUser(user, null);
		Mockito.when(keySpaceListInitService.findCreatedKeyspaceList(loginUser)).thenReturn(compTarget.getCreatedKeyspaceList());
		Mockito.when(keyspaceManagerDao.findAllKeyspaceByUserId(loginUser)).thenReturn(compTarget.getKeyspaceList());
		
		// テスト対象の実行
		KeySpaceListInitServiceResultDto checkTarget = keySpaceListService.init(loginUser);
		
		// テスト対象の検証
   		Assertions.assertThat(checkTarget.getKeyspaceList().size()).isEqualTo(compTarget.getKeyspaceList().size());
   		Assertions.assertThat(checkTarget.getCreatedKeyspaceList().size()).isEqualTo(compTarget.getCreatedKeyspaceList().size());
	}
}
