package com.dbaas.cassandra.shared.validation.validator;

import com.dbaas.cassandra.shared.validation.constraints.MaxLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.dbaas.cassandra.utils.StringUtils.isEmpty;

public class MaxLengthValidator implements ConstraintValidator<MaxLength, String> {

	private int max;

	@Override
	public void initialize(MaxLength constraintAnnotation) {
		this.max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (isEmpty(value)) {
			return true;
		}
		return this.isMaxLengthValid(value);
	}

	private boolean isMaxLengthValid(String value) {
		return value.length() <= this.max;
	}
}
