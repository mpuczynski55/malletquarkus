package com.agh.mallet.domain.user.set.boundary;

import com.agh.api.SetBasicDTO;
import com.agh.api.SetCreateDTO;
import com.agh.mallet.domain.user.set.control.UserSetService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "User's Set Resource")
@Path("user/set")
public class UserSetResource {

    private final UserSetService userSetService;
    private final SecurityIdentity securityIdentity;

    public UserSetResource(UserSetService userSetService, SecurityIdentity securityIdentity) {
        this.userSetService = userSetService;
        this.securityIdentity = securityIdentity;
    }

    @Operation(
            summary = "Get all user's sets",
            description = "Get all user's sets. If the terms result exceeds limit param then next chunk uri is returned"
    )
    @GET
    public Response get(@QueryParam("startPosition") @DefaultValue("0") int startPosition,
                        @QueryParam("limit") @DefaultValue("10") int limit) {
        SetBasicDTO userSets = userSetService.get(startPosition, limit, securityIdentity.getPrincipal());

        return Response.ok()
                .entity(userSets)
                .build();
    }

    @Operation(
            summary = "Add set to user's set collection",
            description = "Add set to user's set collection providing setId"
    )
    @PUT
    public Response add(@QueryParam("id") long setId) {
        userSetService.add(securityIdentity.getPrincipal(), setId);

        return Response.ok()
                .build();
    }


    @Operation(
            summary = "Remove set from user's set collection",
            description = "Remove set from user's set collection providing setId"
    )
    @DELETE
    public Response remove(@QueryParam("id") long setId) {
        userSetService.remove(setId, securityIdentity.getPrincipal());

        return Response.ok()
                .build();
    }

    @Operation(
            summary = "Create user set"
    )
    @POST
    public Response createSet(@RequestBody SetCreateDTO setCreateDTO) {
        Long setId = userSetService.create(setCreateDTO, securityIdentity.getPrincipal());

        return Response.ok()
                .entity(setId)
                .build();
    }


}
