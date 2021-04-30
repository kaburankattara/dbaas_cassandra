package com.dbaas.cassandra.app.login;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.DbaasCassandraApplication;
import com.dbaas.cassandra.config.DatasourceConfig;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;


@AutoConfigureMockMvc
//@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class)
@SpringBootTest(classes = DbaasCassandraApplication.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, 
    TransactionalTestExecutionListener.class, // テストトランザクションの制御。デフォルトで設定により、テスト後にDB状態はロールバックする
    DbUnitTestExecutionListener.class })
@Import(DatasourceConfig.class)
public class LoginControllerTest {

    // mockMvc TomcatサーバへデプロイすることなくHttpリクエスト・レスポンスを扱うためのMockオブジェクト
    @Autowired
    private MockMvc mockMvc;

    // getリクエストでviewを指定し、httpステータスでリクエストの成否を判定
    @Test
    void init処理が走って200が返る() throws Exception {
        // andDo(print())でリクエスト・レスポンスを表示
        this.mockMvc.perform(MockMvcRequestBuilders.get("/login")).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DatabaseSetups(@DatabaseSetup(value = "/static/dbunit/DB上に存在する利用者ユーザでログインできる_setup.xml", type = DatabaseOperation.CLEAN_INSERT))
    @Transactional
    void DB上に存在するユーザでログイン出来る() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/authenticate").param("userId", "aaa").param("password", "aaa")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/keyspaceList"));
    }

    @Test
    @Transactional
    void DB上に存在しないユーザではログイン出来ない() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/authenticate").param("userId", "aaa").param("password", "aaa")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginError"));
    }

    @Test
    void 認証に失敗した場合はログインエラー画面描画用のURLにフォワードして200が返る() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/authenticate").param("userId", "aaa").param("password", "aab")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginError"))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    void 認証に失敗した場合はエラーメッセー時を表示() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/loginError").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", "認証に失敗しました。ユーザIDまたはパスワードが間違っています。"));
    }
}
