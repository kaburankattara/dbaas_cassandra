package com.dbaas.cassandra.shared.validation.constraints;

import com.dbaas.cassandra.shared.validation.validator.MaxLengthValidator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(MaxLength.List.class)
@Documented
@Constraint(validatedBy = {MaxLengthValidator.class})
public @interface MaxLength {
	String message() default "{shared.validation.constraints.MaxLength.message}";

	int max();

	Class< ? >[] groups() default { };

	Class< ? extends Payload>[] payload() default { };

	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		MaxLength[] value();
	}
}
