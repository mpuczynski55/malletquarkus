package com.agh.mallet.domain.group.boundary;

import com.agh.api.GroupContributionDeleteDTO;
import com.agh.api.GroupCreateDTO;
import com.agh.api.GroupDTO;
import com.agh.api.GroupSetCreateDTO;
import com.agh.api.GroupSetDTO;
import com.agh.api.GroupUpdateAdminDTO;
import com.agh.api.GroupUpdateDTO;
import com.agh.mallet.domain.group.control.GroupService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Group Resource")
@Path("group")
public class GroupResource {

    private final GroupService groupService;
    private final SecurityIdentity securityIdentity;

    public GroupResource(GroupService groupService, SecurityIdentity securityIdentity) {
        this.groupService = groupService;
        this.securityIdentity = securityIdentity;
    }

    @Operation(
            summary = "Get group",
            description = "Get group providing id"
    )
    @GET
    public Response get(@QueryParam("id") long id) {
        GroupDTO groupDTO = groupService.get(id);

        return Response.ok()
                .entity(groupDTO)
                .build();
    }

    @Operation(
            summary = "Create group"
    )
    @POST
    public Response create(@RequestBody GroupCreateDTO groupCreateDTO) {
        Long groupId = groupService.create(groupCreateDTO, securityIdentity.getPrincipal());

        return Response.ok()
                .entity(groupId)
                .build();
    }

    @Operation(
            summary = "Update group's contributions",
            description = "Update group's contributions. Only admin (owner) of the group and contributor with GROUP_READ_WRITE permission can perform this operation"

    )
    @PUT
    @Path("/contribution")
    public Response updateContributions(@RequestBody GroupUpdateDTO groupUpdateDTO) {
        groupService.updateContributions(groupUpdateDTO, securityIdentity.getPrincipal());

        return Response.ok()
                .build();

    }

    @Operation(
            summary = "Delete group's contributions",
            description = "Delete group's contributions providing contributor ids. Only admin (owner) of the group can perform this operation"
    )
    @DELETE
    @Path("/contribution")
    public Response deleteContributions(@RequestBody GroupContributionDeleteDTO contributionDeleteDTO) {
        groupService.deleteContributions(contributionDeleteDTO, securityIdentity.getPrincipal());

        return Response.ok()
                .build();

    }

    @Operation(
            summary = "Delete group",
            description = "Delete group providing groupId. Only admin (owner) of the group can perform this operation"
    )
    @DELETE
    public Response delete(@QueryParam("id") long groupId) {
        groupService.delete(groupId, securityIdentity.getPrincipal());

        return Response.ok()
                .build();

    }

    @Operation(
            summary = "Update group admin",
            description = "Update group admin. Only admin (owner) of the group can perform this operation"
    )
    @PUT
    @Path("/admin")
    public Response updateGroupAdmin(@RequestBody GroupUpdateAdminDTO groupUpdateAdminDTO) {
        groupService.updateAdmin(groupUpdateAdminDTO, securityIdentity.getPrincipal());

        return Response.ok()
                .build();
    }

    @Operation(
            summary = "Add set to group",
            description = "Add set to group. Only user with set READ_WRITE permission can perform this operation"
    )
    @PUT
    @Path("/set")
    public Response addSet(@RequestBody GroupSetDTO groupSetDTO) {
        groupService.addSet(groupSetDTO, securityIdentity.getPrincipal());

        return Response.ok()
                .build();

    }

    @Operation(
            summary = "Remove set from group",
            description = "Remove set from group. Only user with set READ_WRITE permission can perform this operation"
    )
    @DELETE
    @Path("/set")
    public Response removeSet(@RequestBody GroupSetDTO groupSetDTO) {
        groupService.removeSet(groupSetDTO, securityIdentity.getPrincipal());

        return Response.ok()
                .build();
    }

    @Operation(
            summary = "Create set group",
            description = "Create set group. Only user with set READ_WRITE permission can perform this operation"
    )
    @POST
    @Path("set")
    public Response createSet(@RequestBody GroupSetCreateDTO groupSetCreateDTO) {
        groupService.createSet(groupSetCreateDTO, securityIdentity.getPrincipal());

        return Response.ok()
                .build();
    }

}
