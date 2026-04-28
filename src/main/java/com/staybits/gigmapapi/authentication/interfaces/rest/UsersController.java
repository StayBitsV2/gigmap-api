package com.staybits.gigmapapi.authentication.interfaces.rest;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.queries.GetAllUsersQuery;
import com.staybits.gigmapapi.authentication.domain.model.queries.GetUserByIdQuery;
import com.staybits.gigmapapi.authentication.domain.model.queries.GetUserDetailsByIdQuery;
import com.staybits.gigmapapi.authentication.domain.model.queries.GetUsersByCommunityIdQuery;
import com.staybits.gigmapapi.authentication.domain.services.UserCommandService;
import com.staybits.gigmapapi.authentication.domain.services.UserQueryService;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.UpdateUserResource;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.UserDetailsResource;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.UserResource;
import com.staybits.gigmapapi.authentication.interfaces.rest.transform.UpdateUserCommandFromResourceAssembler;
import com.staybits.gigmapapi.authentication.interfaces.rest.transform.UserDetailsResourceFromEntityAssembler;
import com.staybits.gigmapapi.authentication.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class is a REST controller that exposes the users resource.
 * It includes the following operations:
 * - GET /api/v1/users: returns all the users
 * - GET /api/v1/users/{userId}: returns the user with the given id
 **/
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Available User Endpoints")
public class UsersController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public UsersController(UserQueryService userQueryService, UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    /**
     * This method returns all the users.
     * @return a list of user resources
     * @see UserResource
     */
    @GetMapping
    @Operation(
        summary = "Get users",
        description = "Get all users or users belonging to a specific community if communityId is provided."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized.")
    })
    public ResponseEntity<List<UserResource>> getUsers(@RequestParam(required = false) Long communityId) {
        List<User> users;

        if (communityId != null) {
            users = userQueryService.handle(new GetUsersByCommunityIdQuery(communityId));
        } else {
            users = userQueryService.handle(new GetAllUsersQuery());
        }

        var userResources = users.stream()
            .map(UserResourceFromEntityAssembler::toResourceFromEntity)
            .toList();

        return ResponseEntity.ok(userResources);
    }

    /**
     * This method returns the user with the given id.
     * @param userId the user id
     * @return the user resource with the given id
     * @throws RuntimeException if the user is not found
     * @see UserResource
     */
    @GetMapping(value = "/{userId}")
    @Operation(summary = "Get user by id", description = "Get the user with the given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")})
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @GetMapping(value = "/{userId}/details")
    @Operation(summary = "Get user details by id", description = "Get the user details with the given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")})
    public ResponseEntity<UserDetailsResource> getUserDetailsById(@PathVariable Long userId) {
        var getUserDetailsByIdQuery = new GetUserDetailsByIdQuery(userId);
        var user = userQueryService.handle(getUserDetailsByIdQuery);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var userDetailsResource = UserDetailsResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userDetailsResource);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user", description = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResource> updateUser(@RequestBody UpdateUserResource resource, @PathVariable Long userId) {
        var updateUserCommand = UpdateUserCommandFromResourceAssembler.toCommandFromResource(resource, userId);
        var updatedUser = userCommandService.handle(updateUserCommand);
        if (updatedUser.isEmpty()) return ResponseEntity.notFound().build();
        var updatedUserEntity = updatedUser.get();
        var updatedUserResource = UserResourceFromEntityAssembler.toResourceFromEntity(updatedUserEntity);
        return ResponseEntity.ok(updatedUserResource);
    }
}
