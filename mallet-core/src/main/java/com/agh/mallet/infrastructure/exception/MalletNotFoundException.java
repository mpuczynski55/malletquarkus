package com.agh.mallet.infrastructure.exception;

import org.jboss.resteasy.reactive.RestResponse;

public class MalletNotFoundException extends MalletException{
    public MalletNotFoundException(String message) {
        super(message, RestResponse.Status.NOT_FOUND);
    }

}
