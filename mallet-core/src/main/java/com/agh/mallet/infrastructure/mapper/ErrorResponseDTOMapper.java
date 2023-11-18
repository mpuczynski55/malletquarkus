package com.agh.mallet.infrastructure.mapper;

import com.agh.api.ErrorResponseDTO;
import com.agh.mallet.infrastructure.exception.MalletException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.resteasy.reactive.RestResponse;

public class ErrorResponseDTOMapper {

    private ErrorResponseDTOMapper() {}

    public static ErrorResponseDTO from(MalletException malletException) {
        RestResponse.Status httpStatus = malletException.getHttpStatus();

        return ErrorResponseDTO.builder()
                .httpCode(httpStatus.getStatusCode())
                .httpStatus(httpStatus.toString())
                .stackTrace(ExceptionUtils.getStackTrace(malletException))
                .message(malletException.getMessage())
                .build();
    }

}
