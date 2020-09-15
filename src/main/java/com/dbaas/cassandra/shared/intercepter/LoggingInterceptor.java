package com.dbaas.cassandra.shared.intercepter;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.UnexpectedRollbackException;

import com.dbaas.cassandra.domain.log.Logger;
import com.dbaas.cassandra.domain.message.MessageSourceService;
import com.dbaas.cassandra.domain.user.LoginUser;

@Aspect
@Component
public class LoggingInterceptor {
	
	@Autowired
	public LoggingInterceptor(MessageSourceService messageSource) {
		this.messageSource = messageSource;
	}
	/**
	 * ロガー
	 */
	private Logger logger = new Logger(this.getClass());

	private MessageSourceService messageSource;

	/**
	 * ログ出力処理を行います。
	 * 全てのコントローラーの@RequestMappingが付与されたメソッドの前後で出力します。
	 * @param point	ジョインポイント
	 * @return	オブジェクト
	 * @throws Throwable
	 */
	@Around("execution(* com.dbaas.cassandra.app.controller..*.*(..)) && (@annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)) || execution(* com.dbaas.cassandra.app.batch.*.*(..))")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		LoginUser user = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
				.map(auth -> auth.getPrincipal())
				.filter(princimal -> princimal instanceof LoginUser)
				.map(princimal -> (LoginUser) princimal)
				.orElse(null);

		Signature signature = point.getSignature();
		String typeName = signature.getDeclaringTypeName();
		String methodName = signature.getName();
		try {
			// コントローラーのメソッド実行前にログを出力
			this.logger.info(this.formatMessage(user, "action.begin", typeName, methodName));
			// コントローラーのメソッドを実行
			return point.proceed();
		} catch (AccessDeniedException | UnexpectedRollbackException | CannotCreateTransactionException ex) {
			// 障害メール通知対象外Exception
			// コントローラーのメソッドで例外が発生した際にログを出力
			this.logger.error(this.formatMessage(user, "action.exception", typeName, methodName), ex);
			throw ex;
		} catch (Throwable ex) {
			// コントローラーのメソッドで例外が発生した際にログを出力
			this.logger.error(this.formatMessage(user, "action.exception", typeName, methodName), ex);

			throw ex;
		} finally {
			// このメソッドの処理が終わる前にログを出力
			this.logger.info(this.formatMessage(user, "action.end", typeName, methodName));
		}
	}

	private String formatMessage(LoginUser user, String msgKey, String typeName, String methodName) {
		String msg = this.messageSource.getMessage(msgKey, typeName, methodName);
		if (user != null) {
			msg = this.messageSource.getMessage("action.format", user.getUserId(), msg);
		}
		return msg;
	}
}
