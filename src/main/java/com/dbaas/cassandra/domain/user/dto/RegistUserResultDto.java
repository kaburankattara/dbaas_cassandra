package com.dbaas.cassandra.domain.user.dto;

import com.dbaas.cassandra.shared.validation.ValidateResult;

public class RegistUserResultDto {

	public RegistUserResultDto(ValidateResult validateResult) {
		this.validateResult = validateResult;
	}

	public static RegistUserResultDto createEmptyInstance(ValidateResult validateResult) {
		return new RegistUserResultDto(validateResult);
	}

	private ValidateResult validateResult;

	public ValidateResult getValidateResult() {
		return validateResult;
	}

	public boolean hasError() {
		return validateResult.hasErrors();
	}

}
