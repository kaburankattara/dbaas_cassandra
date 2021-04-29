package com.dbaas.cassandra.shared.validation;

import com.dbaas.cassandra.domain.message.MessageSourceService;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

import static com.dbaas.cassandra.consts.SysConsts.EMPTY;

public class ValidateResult {

    public static ValidateResult createInstance(MessageSourceService messageSource, BindingResult bindingResult) {
        return new ValidateResult(messageSource, bindingResult);
    }

    private MessageSourceService messageSource;

    private BindingResult bindingResult;

    private ValidateResult(MessageSourceService messageSource, BindingResult bindingResult) {
        this.messageSource = messageSource;
        this.bindingResult = bindingResult;
    }

    public boolean hasErrors() {
        return bindingResult.hasErrors();
    }

    public String getFirstErrorMessage() {
        if (!hasErrors()) {
            return EMPTY;
        }

        if (bindingResult.hasFieldErrors()) {
            return bindingResult.getFieldErrors().get(0).getDefaultMessage();
        }

        if (bindingResult.hasErrors()) {
            String messageCd = bindingResult.getAllErrors().get(0).getCode();
            Object[] errorArgs = bindingResult.getAllErrors().get(0).getArguments();
            return getMessage(messageCd, errorArgs);
        }

        return EMPTY;
    }

    public List<String> getErrorMessages() {
        if (!hasErrors()) {
            return new ArrayList<String>();
        }

        List<String> errorMessageList = new ArrayList<String>();
        for(FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessageList.add(fieldError.getDefaultMessage());
        }
        return errorMessageList;
    }

    public void addError(String errorCode) {
        bindingResult.reject(errorCode);
    }

    public void addError(String errorCode, @Nullable Object[] errorArgs) {
        bindingResult.reject(errorCode, errorArgs, null);
    }

    public void addError(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage) {
        bindingResult.reject(errorCode, errorArgs, defaultMessage);
    }

    private String getMessage(String messageCd, @Nullable Object[] errorArgs) {
        return messageSource.getMessage(messageCd, errorArgs);
    }

}
