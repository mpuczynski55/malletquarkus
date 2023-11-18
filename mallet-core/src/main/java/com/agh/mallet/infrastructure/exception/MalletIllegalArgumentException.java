package com.agh.mallet.infrastructure.exception;

import org.jboss.resteasy.reactive.RestResponse;

public class MalletIllegalArgumentException extends MalletException{

    public MalletIllegalArgumentException(String message) {
        super(message, RestResponse.Status.PRECONDITION_FAILED);
    }

}
