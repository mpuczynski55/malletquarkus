package com.agh.mallet.domain.user.group.boundary;

import com.agh.api.GroupBasicDTO;
import com.agh.mallet.domain.user.group.contorl.UserGroupService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "User's Group Resource")

@Path("user/group")
public class UserGroupResource {

    private final UserGroupService userGroupService;

    private final SecurityIdentity securityIdentity;

    public UserGroupResource(UserGroupService userGroupService, SecurityIdentity securityIdentity) {
        this.userGroupService = userGroupService;
        this.securityIdentity = securityIdentity;
    }

    @Operation(
            summary = "Get all user's groups",
            description = "Get all user's groups. If the result exceeds limit param then next chunk uri is returned"
    )
    @GET

    public Response get(@QueryParam("startPosition") @DefaultValue("0") int startPosition,
                        @QueryParam("limit") @DefaultValue("10") int limit) {
        GroupBasicDTO groupBasicDTO = userGroupService.get(startPosition, limit, securityIdentity.getPrincipal());

        return Response.ok()
                .entity(groupBasicDTO)
                .build();
    }
}
