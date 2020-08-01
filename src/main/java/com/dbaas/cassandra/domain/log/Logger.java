package com.dbaas.cassandra.domain.log;

import org.slf4j.LoggerFactory;

public class Logger implements ILogger {

	/**
	 * slf4jL Logger
	 */
	private org.slf4j.Logger logger;

	/**
	 * コンストラクタ
	 * @param clazz 型
	 */
	public Logger(Class< ? > clazz) {
		this.logger = LoggerFactory.getLogger(clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String msg) {
		this.logger.trace(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String msg, Throwable t) {
		this.logger.trace(msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String format, Object... args) {
		this.logger.trace(format, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String msg) {
		this.logger.debug(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String msg, Throwable t) {
		this.logger.debug(msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String format, Object... args) {
		this.logger.debug(format, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String msg) {
		this.logger.info(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String msg, Throwable t) {
		this.logger.info(msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String format, Object... args) {
		this.logger.info(format, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String msg) {
		this.logger.warn(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String msg, Throwable t) {
		this.logger.warn(msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String format, Object... args) {
		this.logger.warn(format, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String msg) {
		this.logger.error(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String msg, Throwable t) {
		this.logger.error(msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String format, Object... args) {
		this.logger.error(format, args);
	}

}
