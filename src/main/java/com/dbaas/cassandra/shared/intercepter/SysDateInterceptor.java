package com.dbaas.cassandra.shared.intercepter;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.removeSysDate;
import static com.dbaas.cassandra.domain.sysDate.SysDateContext.setSysDate;
import static com.dbaas.cassandra.utils.DateUtils.isDateFormat;
import static com.dbaas.cassandra.utils.NumberUtils.toInt;
import static com.dbaas.cassandra.utils.StringUtils.isNotEmpty;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.dbaas.cassandra.domain.sysDate.SysDate;
import com.dbaas.cassandra.domain.table.common.BaseDao;

/**
 * システム日付用Interceptor
 */
@Aspect
@Component
@Order(1)
public class SysDateInterceptor {

	/**
	 * 共通Dao
	 */
	private BaseDao baseDao;
	
	@Autowired
	public SysDateInterceptor(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	/**
	 * 処理タイミングの定義
	 */
	@Pointcut("(execution(* com.dbaas.cassandra.app.*..*Controller*.*(..)) "
			+ "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping) "
			+ "|| @annotation(org.springframework.web.bind.annotation.GetMapping) "
			+ "|| @annotation(org.springframework.web.bind.annotation.PostMapping))) "
			+ "|| execution(* com.dbaas.cassandra.task.*.*(..)) "
			+ "|| execution(* com.dbaas.cassandra.batch.*.*(..))")
	private void controllerMethods() {
	}

	/**
	 * Controller呼出し前処理
	 * @param point JoinPoint
	 * @throws Throwable Throwable
	 */
	@Before("controllerMethods()")
	public void before(JoinPoint point) throws Throwable {
		LocalDateTime sysDate = baseDao.getSysDate();
		setSysDate(getUpdateLocalDateTime(sysDate));
	}

	/**
	 * Controller呼出し後処理
	 * @param point JoinPoint
	 * @throws Throwable Throwable
	 */
	@After("controllerMethods()")
	public void after(JoinPoint point) throws Throwable {
		removeSysDate();
	}

	/**
	 * 更新したシステム日付を取得
	 *
	 * @param sysDate システム日付
	 * @return 更新したシステム日付
	 */
	private LocalDateTime getUpdateLocalDateTime(LocalDateTime sysDate) {
		// 検証、本番環境の場合
//		if (isEquals(AWS_ENV_KBN, AWS_ENB_KBN_KENSHO) || isEquals(AWS_ENV_KBN, AWS_ENB_KBN_HOMBAN)) {
//			String confStr = getenv("SYSDATE");
//			return updateLocalDateTime(sysDate, confStr);
//		}
		// ローカル環境の場合
        try (BufferedReader in = new BufferedReader(new FileReader(new File("C:/tmp/sysDateConf.txt")))){
            String confStr;
            while((confStr = in.readLine()) != null) {
            	return updateLocalDateTime(sysDate, confStr);
            }
        } catch (Exception e){ }
		return sysDate;
	}


	/**
	 * システム日付の更新
	 *
	 * @param sysDate システム日付
	 * @param sysDateConf システム日付設定情報
	 * @return 更新したシステム日付
	 */
	private LocalDateTime updateLocalDateTime(LocalDateTime sysDate, String conf) {
		if (isNotEmpty(conf) && isDateFormat(conf)) {
			SysDate sysDateConf = new SysDate(conf);
			sysDate = sysDate.withYear(toInt(sysDateConf.getYear()));
			sysDate = sysDate.withMonth(toInt(sysDateConf.getMonth()));
			sysDate = sysDate.withDayOfMonth(toInt(sysDateConf.getDay()));
		}
		return sysDate;
	}
}
