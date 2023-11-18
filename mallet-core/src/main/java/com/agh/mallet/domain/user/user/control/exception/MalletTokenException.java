package com.agh.mallet.domain.user.user.control.exception;

import com.agh.mallet.infrastructure.exception.MalletException;
import org.jboss.resteasy.reactive.RestResponse;

public class MalletTokenException extends MalletException {
    public MalletTokenException(String message, RestResponse.Status status) {
        super(message, status);
    }
}
