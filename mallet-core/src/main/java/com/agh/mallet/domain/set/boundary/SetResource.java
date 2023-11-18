package com.agh.mallet.domain.set.boundary;


import com.agh.api.SetBasicDTO;
import com.agh.api.SetDetailDTO;
import com.agh.api.SetUpdateDTO;
import com.agh.mallet.domain.set.control.service.SetService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Set;

@Tag(name = "Set Resource")
@Path( "set")
public class SetResource {

    private static final String BASIC_PATH = "basic";

    private final SetService setService;
    private final SecurityIdentity securityIdentity;

    public SetResource(SetService setService, SecurityIdentity securityIdentity) {
        this.setService = setService;
        this.securityIdentity = securityIdentity;
    }

    @Operation(
            summary = "Get sets basic information",
            description = "Get sets basic information providing primary language." +
                    "If ids parameter is provided and result exceeds limit param then next chunk uri is returned"
    )
    @GET
    @Path(BASIC_PATH)
    public Response getBasics(@QueryParam( "ids") Set<Long> ids,
                              @QueryParam("topic") String topic,
                              @QueryParam( "startPosition") int startPosition,
                              @QueryParam( "limit") int limit,
                              @QueryParam( "language") @DefaultValue("EN") String primaryLanguage) {
        SetBasicDTO setBasicDTO = setService.getBasics(ids, topic, startPosition, limit, primaryLanguage);

        return Response.ok()
                .entity(setBasicDTO)
                .build();
    }

    @Operation(
            summary = "Get set detail information",
            description = "Get set detail information providing primary language and set id. If the terms result exceeds termLimit param then next chunk uri is returned"
    )
    @GET
    public Response get(@QueryParam( "id") long id,
                        @QueryParam( "termStartPosition") @DefaultValue("0") int termStartPosition,
                        @QueryParam( "termLimit") @DefaultValue("20") int termLimit,
                        @QueryParam( "language") String primaryLanguage) {
        SetDetailDTO setDTO = setService.get(id, termStartPosition, termLimit, primaryLanguage);

        return Response.ok()
                .entity(setDTO)
                .build();
    }

    @Operation(
            summary = "Sync set"
    )
    @PUT
    public Response syncSet(@RequestBody SetUpdateDTO setUpdateDTO) {
        setService.syncSet(setUpdateDTO, securityIdentity.getPrincipal());

        return Response.ok()
                .build();
    }

}
