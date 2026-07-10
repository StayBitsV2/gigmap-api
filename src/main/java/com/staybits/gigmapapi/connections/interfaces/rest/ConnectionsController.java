package com.staybits.gigmapapi.connections.interfaces.rest;

import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.connections.domain.model.commands.AcceptConnectionRequestCommand;
import com.staybits.gigmapapi.connections.domain.model.commands.RejectConnectionRequestCommand;
import com.staybits.gigmapapi.connections.domain.services.ConnectionService;
import com.staybits.gigmapapi.connections.interfaces.rest.resources.ConnectionRequestResource;
import com.staybits.gigmapapi.connections.interfaces.rest.resources.ConnectionResource;
import com.staybits.gigmapapi.connections.interfaces.rest.resources.CreateConnectionRequestResource;
import com.staybits.gigmapapi.connections.interfaces.rest.transform.ConnectionRequestResourceFromEntityAssembler;
import com.staybits.gigmapapi.connections.interfaces.rest.transform.ConnectionResourceFromEntityAssembler;
import com.staybits.gigmapapi.connections.interfaces.rest.transform.CreateConnectionRequestCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/connections", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Connections", description = "Operations related to Connections")
public class ConnectionsController {

    private final ConnectionService connectionCommandService;
    private final ConnectionService connectionQueryService;
    private final UserRepository userRepository;

    public ConnectionsController(@Qualifier("connectionCommandServiceImpl") ConnectionService connectionCommandService,
            @Qualifier("connectionQueryServiceImpl") ConnectionService connectionQueryService,
            UserRepository userRepository) {
        this.connectionCommandService = connectionCommandService;
        this.connectionQueryService = connectionQueryService;
        this.userRepository = userRepository;
    }

    @PostMapping("/requests")
    @Operation(summary = "Create a connection request", description = "Creates a new connection request from the authenticated user to a target user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Connection request created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ConnectionRequestResource> createRequest(@RequestParam Long requesterId,
            @RequestBody CreateConnectionRequestResource resource) {
        var command = CreateConnectionRequestCommandFromResourceAssembler.toCommandFromResource(requesterId, resource);
        var request = connectionCommandService.createRequest(command);

        if (request == null || request.getId() == null || request.getId() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var response = ConnectionRequestResourceFromEntityAssembler.toResourceFromEntity(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/requests/incoming")
    @Operation(summary = "Get incoming connection requests", description = "Retrieve all pending connection requests for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requests found"),
            @ApiResponse(responseCode = "404", description = "No requests found")
    })
    public ResponseEntity<List<ConnectionRequestResource>> getIncomingRequests(@RequestParam Long userId) {
        var requests = connectionQueryService.getPendingRequestsForUser(userId);

        if (requests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resources = ConnectionRequestResourceFromEntityAssembler.toResourcesFromEntities(requests);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/requests/outgoing")
    @Operation(summary = "Get outgoing connection requests", description = "Retrieve all pending connection requests sent by a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requests found"),
            @ApiResponse(responseCode = "404", description = "No requests found")
    })
    public ResponseEntity<List<ConnectionRequestResource>> getOutgoingRequests(@RequestParam Long userId) {
        var requests = connectionQueryService.getPendingRequestsForUser(userId);

        if (requests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resources = ConnectionRequestResourceFromEntityAssembler.toResourcesFromEntities(requests);
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/requests/{requestId}/accept")
    @Operation(summary = "Accept a connection request", description = "Accept a pending connection request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request accepted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<ConnectionResource> acceptRequest(@PathVariable Long requestId,
            @RequestParam Long userId) {
        var request = connectionCommandService.acceptRequest(new AcceptConnectionRequestCommand(requestId));
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var resource = ConnectionResourceFromEntityAssembler.toResourceFromEntity(request, user);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/requests/{requestId}/reject")
    @Operation(summary = "Reject a connection request", description = "Reject a pending connection request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request rejected successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<Void> rejectRequest(@PathVariable Long requestId) {
        connectionCommandService.rejectRequest(new RejectConnectionRequestCommand(requestId));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Get user connections", description = "Retrieve all connections for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connections found"),
            @ApiResponse(responseCode = "404", description = "No connections found")
    })
    public ResponseEntity<List<ConnectionResource>> getUserConnections(@RequestParam Long userId) {
        var connections = connectionQueryService.getUserConnections(userId);

        if (connections.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var resources = ConnectionResourceFromEntityAssembler.toResourcesFromEntities(connections, user);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/check")
    @Operation(summary = "Check if users are connected", description = "Check if two users are connected")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection status retrieved")
    })
    public ResponseEntity<Boolean> areConnected(@RequestParam Long userId1, @RequestParam Long userId2) {
        var connected = connectionQueryService.areConnected(userId1, userId2);
        return ResponseEntity.ok(connected);
    }
}
