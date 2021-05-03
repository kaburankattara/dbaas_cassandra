package com.dbaas.cassandra.domain.cassandra.table.dto;

import com.dbaas.cassandra.shared.validation.ValidateResult;

public class RegistTableResultDto {

	public RegistTableResultDto(ValidateResult validateResult) {
		this.validateResult = validateResult;
	}

	public static RegistTableResultDto createEmptyInstance(ValidateResult validateResult) {
		return new RegistTableResultDto(validateResult);
	}

	private ValidateResult validateResult;

	public ValidateResult getValidateResult() {
		return validateResult;
	}

	public boolean hasError() {
		return validateResult.hasErrors();
	}

}
