package com.agh.mallet.domain.user.term.boundary;


import com.agh.mallet.domain.user.term.control.UserTermService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Set;

@Tag(name = "User's Term Resource")
@Authenticated
@Path("user/term")
public class UserTermResource {

    private static final String ADD_PATH = "/add";

    private final UserTermService userTermService;
    private final SecurityIdentity securityIdentity;

    public UserTermResource(UserTermService userTermService, SecurityIdentity securityIdentity) {
        this.userTermService = userTermService;
        this.securityIdentity = securityIdentity;
    }

    @Operation(
            summary = "Add user known terms",
            description = "Add terms to user known terms providing ids of the terms"
    )
    @PUT
    @Path(ADD_PATH)
    public void addUserKnownTerms(@RequestBody Set<Long> userKnownTermIds) {
        userTermService.updateKnown(userKnownTermIds, securityIdentity.getPrincipal());
    }

}
