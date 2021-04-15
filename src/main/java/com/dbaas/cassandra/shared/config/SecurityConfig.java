package com.dbaas.cassandra.shared.config;

import com.dbaas.cassandra.consts.UrlConsts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	public void configure(WebSecurity web) {
		// セキュリティ対策外の指定
		web.ignoring()
		//.antMatchers("/resources/**");
		.antMatchers("/admin-act/**");
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
        // ログイン処理の認証ルールを設定		
        http.authorizeRequests()
                .antMatchers("/", "/login", "/redirectLogin", "/authenticate", "/userRegister/**", "/css/**", "/js/**").permitAll() // 認証なしでアクセス可能なパス
                .anyRequest().authenticated() // それ以外は認証が必要
                .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/authenticate") // ログインフォームのアクションに指定したURL[action="@{/login}"]を設定
                .usernameParameter("userId") // ログインフォームのユーザー欄のname属性を設定
				.passwordParameter("password") // ログインフォームのパスワード欄のname属性を設定
				.defaultSuccessUrl(UrlConsts.URL_KEY_SPACE_LIST) // ログイン成功時に遷移するURL
//                .successForwardUrl("/menu")
                .failureUrl("/loginError") // ログイン失敗時に遷移するURL
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout")
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//		// フォーム認証を有効にする
//		http.formLogin()
//		
//		// ログインパラメータのカスタム
//		// デフォルトの「username」から「userId」に変更
//		// パスワードはそのまま
//		.usernameParameter("userId")
//		.passwordParameter("password")
//		
//		// ログイン画面の指定
//		// 未ログインユーザが認証を必要とするリソースにアクセスすると
//		// ログイン画面にリダイレクトするようになる
//		.loginPage("/login")
//		// ログイン処理を行うリクエスト先
//		.loginProcessingUrl("/authenticate")
//		
//		// 認証成功時の遷移先のデフォルト
//		.defaultSuccessUrl("/keySpaceList/index")
//		// 認証失敗時の遷移先
//		.failureUrl("/loginFailure")
//		.permitAll();
//		http.authorizeRequests().anyRequest().authenticated();
//		
//		http.logout().permitAll();
//		
//	}
	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		// 認証の設定
//		http.authorizeRequests()
//		// 認証対象外のパスの設定（CSS、js等）
//		.antMatchers("/css/**", "/js/**", "/img/**", "/login*", "/favicon.ico", "/authenticate", "/error").permitAll()
//		// その他のリクエストには認証が必要
//		.anyRequest().authenticated()
//		// CSRFエラー時の遷移
//		.and().exceptionHandling().accessDeniedPage("/");
//
//		// フォームログインの設定
//		http.formLogin()
//		// ログイン画面の設定
//		.loginPage("/login")
//		// 認証処理のURLの設定
//		.loginProcessingUrl("/authenticate")
//		// 認証失敗時のフォワード先
//		.failureForwardUrl("/error")
//		// ログイン成功時の画面設定
//		.defaultSuccessUrl("/keySpaceList/index", true)
//		// 認証情報のパラメーター名の設定
//		.usernameParameter("userId")
//		.passwordParameter("password")
//		.permitAll();
//
//		// ログアウトの設定
//		http.logout()
//		.invalidateHttpSession(false) //Login時に新SessionIDは発行される模様
//		.permitAll();
//
//		http.sessionManagement()
//		.sessionFixation()
//		.newSession();
//	}

//	@Autowired
//	void configureAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(authenticationUserDetailsService).passwordEncoder(passwordEncoder());
//	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 認証時に呼び出すサービスの設定
		auth.userDetailsService(userDetailsService)
		// パスワードの暗号化機能の設定
		.passwordEncoder(passwordEncoder());
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
