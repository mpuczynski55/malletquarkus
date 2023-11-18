package com.agh.mallet.domain.term.boundary;


import com.agh.api.TermBasicListDTO;
import com.agh.mallet.domain.term.control.service.TermService;
import io.quarkus.security.Authenticated;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Term Resource")
@Authenticated
@Path("term")
public class TermResource {

    private final TermService termService;

    public TermResource(TermService termService) {
        this.termService = termService;
    }


    @Operation(
            summary = "Get all terms by term name "
    )
    @GET
    public Response get(@QueryParam("term") String term,
                        @QueryParam("startPosition") @DefaultValue("0") int startPosition,
                        @QueryParam("limit") @DefaultValue("10") int limit) {
        TermBasicListDTO termBasicListDTO = termService.getByTerm(term, startPosition, limit);

        return Response.ok()
                .entity(termBasicListDTO)
                .build();
    }

}
