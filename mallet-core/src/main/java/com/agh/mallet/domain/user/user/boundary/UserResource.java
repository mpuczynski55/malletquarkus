package com.agh.mallet.domain.user.user.boundary;


import com.agh.api.UserDTO;
import com.agh.api.UserDetailDTO;
import com.agh.api.UserLogInDTO;
import com.agh.api.UserRegistrationDTO;
import com.agh.mallet.domain.user.user.control.service.ConfirmationTokenService;
import com.agh.mallet.domain.user.user.control.service.UserService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.vertx.core.cli.annotations.Hidden;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Tag(name = "User Resource")
@Path("user")
public class UserResource {

    private static final String REGISTRATION_PATH = "/registration";
    private static final String EMAIL_CONFIRMATION_PATH = REGISTRATION_PATH + "/confirm";
    private static final String LOGIN_PATH = "/login";

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final SecurityIdentity securityIdentity;

    public UserResource(UserService userService, ConfirmationTokenService confirmationTokenService, SecurityIdentity securityIdentity) {
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.securityIdentity = securityIdentity;
    }

    @Operation(
            summary = "Register user"
    )
    @Path(REGISTRATION_PATH)
    @POST
    public Response register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        userService.signUp(userRegistrationDTO);

        return Response.ok()
                .build();
    }

    @Hidden
    @Path(EMAIL_CONFIRMATION_PATH)
    @PUT
    public void confirm(@QueryParam("token") String token) {
        confirmationTokenService.confirmToken(token);

        //TODO ZWRACANIE HTMLA
      /*  modelAndView.setViewName("accountConfirmed");
        return modelAndView;*/
    }

    @Operation(
            summary = "Log in user"
    )
    @Path(LOGIN_PATH)
    @Authenticated
    @POST
    public Response logIn(@RequestBody UserLogInDTO userLogInDTO) {
        UserDetailDTO userDetailDTO = userService.logIn(userLogInDTO);

        return Response.ok()
                .entity(userDetailDTO)
                .build();
    }

    @Operation(
            summary = "Get users providing username"
    )
    @GET
    public Response get(@QueryParam("username") String username) {
        List<UserDTO> userDTOS = userService.get(username);

        return Response.ok()
                .entity(userDTOS)
                .build();
    }

}
