package com.agh.mallet.infrastructure.exception;

import org.jboss.resteasy.reactive.RestResponse;

public class MalletIllegalStateException extends MalletException{
    public MalletIllegalStateException(String message, RestResponse.Status status) {
        super(message, status);
    }
}
