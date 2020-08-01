package com.dbaas.cassandra.domain.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageSourceService {

	/**
	 * spring message source
	 */
	@Autowired
	private org.springframework.context.MessageSource messageSource;

	/**
	 * {@inheritDoc}
	 */
	public String getMessage(String msgCode) {
		return this.messageSource.getMessage(msgCode, null, LocaleContextHolder.getLocale());
	}

	/**
	 * {@inheritDoc}
	 */
	public String getMessage(String msgCode, Object... args) {
		return this.messageSource.getMessage(msgCode, args, LocaleContextHolder.getLocale());
	}
}
