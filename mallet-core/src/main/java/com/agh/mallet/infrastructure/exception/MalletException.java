package com.agh.mallet.infrastructure.exception;

import org.jboss.resteasy.reactive.RestResponse;

public class MalletException extends RuntimeException {

    private RestResponse.Status httpStatus;

    public MalletException(String message, RestResponse.Status code) {
        super(message);
        this.httpStatus = code;
    }

    public RestResponse.Status getHttpStatus() {
        return httpStatus;
    }
}
