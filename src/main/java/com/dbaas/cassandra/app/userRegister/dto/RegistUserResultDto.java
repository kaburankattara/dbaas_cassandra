package com.dbaas.cassandra.app.userRegister.dto;

import com.dbaas.cassandra.shared.validation.ValidateResult;

public class RegistUserResultDto {

	public RegistUserResultDto() {
	}

	public static RegistUserResultDto createEmptyInstance() {
		return new RegistUserResultDto();
	}

	private ValidateResult validateResult;

	public ValidateResult getValidateResult() {
		return validateResult;
	}

	public void setValidateResult(ValidateResult validateResult) {
		this.validateResult = validateResult;
	}

	public boolean hasError() {
		return validateResult.hasErrors();
	}

}
