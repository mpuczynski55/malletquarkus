package com.agh.mallet.infrastructure.exception;

import org.jboss.resteasy.reactive.RestResponse;

public class MalletForbiddenException extends MalletException{
    public MalletForbiddenException(String message) {
        super(message, RestResponse.Status.FORBIDDEN);
    }
}
