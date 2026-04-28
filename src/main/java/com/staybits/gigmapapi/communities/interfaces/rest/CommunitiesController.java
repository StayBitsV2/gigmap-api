package com.staybits.gigmapapi.communities.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.staybits.gigmapapi.communities.domain.model.commands.DeleteCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.JoinCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.LeaveCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.queries.GetAllCommunitiesJoinedByUserIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetCommunitiesQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetCommunityByIdQuery;
import com.staybits.gigmapapi.communities.domain.services.CommunityCommandService;
import com.staybits.gigmapapi.communities.domain.services.CommunityQueryService;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CommunityResource;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CreateCommunityResource;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.UpdateCommunityResource;
import com.staybits.gigmapapi.communities.interfaces.rest.transform.CommunityResourceFromEntityAssembler;
import com.staybits.gigmapapi.communities.interfaces.rest.transform.CreateCommunityCommandFromResourceAssembler;
import com.staybits.gigmapapi.communities.interfaces.rest.transform.UpdateCommunityCommandFromResourceAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/communities", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Communities", description = "Operations related to Communities")
public class CommunitiesController {
    private final CommunityCommandService communityCommandService;
    private final CommunityQueryService communityQueryService;

    public CommunitiesController(CommunityCommandService communityCommandService,
            CommunityQueryService communityQueryService) {
        this.communityCommandService = communityCommandService;
        this.communityQueryService = communityQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new community", description = "Creates a new community with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Community created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
    })
    public ResponseEntity<CommunityResource> createCommunity(@RequestBody CreateCommunityResource communityResource) {
        var createdCommunityCommand = CreateCommunityCommandFromResourceAssembler
                .toCommandFromResource(communityResource);
        var community = communityCommandService.handle(createdCommunityCommand);
        if (community.getId() == null || community.getId() <= 0)
            return ResponseEntity.badRequest().build();
        var communityResponse = CommunityResourceFromEntityAssembler.toResourceFromEntity(community);
        return new ResponseEntity<>(communityResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all communities", description = "Get all communities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Communities found"),
            @ApiResponse(responseCode = "404", description = "Communities not found") })
    public ResponseEntity<List<CommunityResource>> getAllCommunities() {
        var communities = communityQueryService.handle(new GetCommunitiesQuery());
        if (communities.isEmpty())
            return ResponseEntity.notFound().build();
        var communityResources = communities.stream()
                .map(CommunityResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(communityResources);
    }

    @GetMapping("/{communityId}")
    @Operation(summary = "Get community by id", description = "Get community by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Community found"),
            @ApiResponse(responseCode = "404", description = "Community not found") })
    public ResponseEntity<CommunityResource> getCommunityById(@PathVariable Long communityId) {
        var community = communityQueryService.handle(new GetCommunityByIdQuery(communityId));
        if (community.isEmpty())
            return ResponseEntity.notFound().build();
        var communityEntity = community.get();
        var communityResource = CommunityResourceFromEntityAssembler.toResourceFromEntity(communityEntity);
        return ResponseEntity.ok(communityResource);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a community", description = "Updates an existing community with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Community updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Community not found")
    })
    public ResponseEntity<CommunityResource> updateCommunity(@PathVariable Long id,
            @RequestBody UpdateCommunityResource updateResource) {

        var updateCommand = UpdateCommunityCommandFromResourceAssembler.toCommandFromResource(id, updateResource);
        var updatedCommunity = communityCommandService.handle(updateCommand);

        if (updatedCommunity == null)
            return ResponseEntity.notFound().build();

        var communityResponse = CommunityResourceFromEntityAssembler.toResourceFromEntity(updatedCommunity.get());
        return ResponseEntity.ok(communityResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a community", description = "Deletes an existing community by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Community deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Community not found")
    })
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long id) {
        var community = communityQueryService.handle(new GetCommunityByIdQuery(id));

        if (community.isEmpty())
            return ResponseEntity.notFound().build();

        communityCommandService.handle(new DeleteCommunityCommand(id));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{communityId}/join")
    @Operation(summary = "Join to community", description = "Join to community")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Join to Community successfully"),
            @ApiResponse(responseCode = "404", description = "Join to Community failed") })
    public ResponseEntity<Void> joinCommunity(@PathVariable Long communityId, @RequestParam Long userId) {
        communityCommandService.handle(new JoinCommunityCommand(communityId, userId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{communityId}/leave")
    @Operation(summary = "Leave to community", description = "Leave to community")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave to Community successfully"),
            @ApiResponse(responseCode = "404", description = "Leave to Community failed") })
    public ResponseEntity<Void> leaveCommunity(@PathVariable Long communityId, @RequestParam Long userId) {
        communityCommandService.handle(new LeaveCommunityCommand(communityId, userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/joined/{userId}")
    @Operation(summary = "Get all communities joined by a User", description = "Get all communities joined by a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Communities joined retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No communities joined found for the given User")
    })
    public ResponseEntity<List<CommunityResource>> getAllCommunitiesJoinedByUserId(@PathVariable Long userId) {
        var communitiesJoined = this.communityQueryService.handle(new GetAllCommunitiesJoinedByUserIdQuery(userId));

        if (communitiesJoined.isEmpty())
            return ResponseEntity.notFound().build();

        var communitiesJoinedResources = communitiesJoined.stream()
                .map(CommunityResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(communitiesJoinedResources);
    }
}
