package com.dbaas.cassandra.shared.validation;

import com.dbaas.cassandra.domain.message.MessageSourceService;
import org.springframework.lang.Nullable;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

public class Validator {

    private SmartValidator smartValidator;

    private MessageSourceService messageSource;

    public static Validator getInstance(SmartValidator smartValidator, MessageSourceService messageSource) {
        return new Validator(smartValidator, messageSource);
    }

    private Validator(SmartValidator smartValidator, MessageSourceService messageSource) {
        this.smartValidator = smartValidator;
        this.messageSource = messageSource;
    }

    public ValidateResult validate(@Nullable Object target, String className) {
        BindingResult bindingResult = new BeanPropertyBindingResult(target, className);
        smartValidator.validate(target, bindingResult);
        return ValidateResult.createInstance(messageSource, bindingResult);
    }
}
