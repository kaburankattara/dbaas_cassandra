package com.dbaas.cassandra.app.userRegister.dto;

import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.message.Message;
import com.dbaas.cassandra.domain.message.MessageSourceService;

import java.util.List;

public class RegistUserResultDto {

	public RegistUserResultDto() {
	}

	public static RegistUserResultDto createEmptyInstance() {
		return new RegistUserResultDto();
	}

	private boolean isRegistedUser;
	
	public boolean isRegistedUser() {
		return isRegistedUser;
	}

	public void setIsRegistedUser() {
		isRegistedUser = true;
	}

	public boolean hasError() {
		return isRegistedUser;
	}
	
	public String getErrorMessage(MessageSourceService messageSource) {
		if (isRegistedUser) {
			return messageSource.getMessage(Message.MSG003E);
		}

		return "";
	}

}
