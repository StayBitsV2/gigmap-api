package com.staybits.gigmapapi.authentication.interfaces.rest;

import com.staybits.gigmapapi.authentication.application.internal.queryservices.ArtistStatsQueryService;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.ArtistStatsResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/artists", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Artists", description = "Artist-specific endpoints")
public class ArtistsController {

    private final ArtistStatsQueryService artistStatsQueryService;

    public ArtistsController(ArtistStatsQueryService artistStatsQueryService) {
        this.artistStatsQueryService = artistStatsQueryService;
    }

    @GetMapping("/{artistId}/stats")
    @Operation(summary = "Get artist stats", description = "Returns weekly visibility metrics for an artist over the last 4 weeks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stats retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid artist ID"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - only the artist can view their own stats")
    })
    public ResponseEntity<ArtistStatsResource> getArtistStats(
            @PathVariable Long artistId,
            Authentication authentication) {
        var authenticatedUserId = getAuthenticatedUserId(authentication);
        if (authenticatedUserId == null || !authenticatedUserId.equals(artistId)) {
            return ResponseEntity.status(403).build();
        }
        try {
            var stats = artistStatsQueryService.getArtistStats(artistId);
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private Long getAuthenticatedUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
