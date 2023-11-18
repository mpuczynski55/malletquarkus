package com.agh.mallet.domain.common;

import com.agh.api.ErrorResponseDTO;
import com.agh.mallet.infrastructure.exception.MalletException;
import com.agh.mallet.infrastructure.mapper.ErrorResponseDTOMapper;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@SuppressWarnings("unused")
@Provider
public class ExceptionController implements ExceptionMapper<MalletException> {

    @Override
    public Response toResponse(MalletException malletException) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTOMapper.from(malletException);

        return Response.status(malletException.getHttpStatus())
                .entity(errorResponseDTO)
                .build();
    }
}
