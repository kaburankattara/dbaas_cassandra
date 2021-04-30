package com.dbaas.cassandra.domain.cassandra.keyspace.dto;

import com.dbaas.cassandra.shared.validation.ValidateResult;

public class RegistKeyspaceResultDto {

	public RegistKeyspaceResultDto(ValidateResult validateResult) {
		this.validateResult = validateResult;
	}

	public static RegistKeyspaceResultDto createEmptyInstance(ValidateResult validateResult) {
		return new RegistKeyspaceResultDto(validateResult);
	}

	private ValidateResult validateResult;

	public ValidateResult getValidateResult() {
		return validateResult;
	}

	public boolean hasError() {
		return validateResult.hasErrors();
	}

}
