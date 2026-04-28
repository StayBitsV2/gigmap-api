package com.staybits.gigmapapi.concerts.interfaces.rest;

import com.staybits.gigmapapi.concerts.domain.model.commands.DeleteConcertCommand;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetAllConcertsAttendedByUserIdQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetAllConcertsQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetConcertByIdQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetConcertsByGenreQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetConcertsByArtistQuery;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.Genre;
import com.staybits.gigmapapi.concerts.domain.services.ConcertCommandService;
import com.staybits.gigmapapi.concerts.domain.services.ConcertQueryService;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.AttendeeResource;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.ConcertResource;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.CreateConcertResource;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.UpdateConcertResource;
import com.staybits.gigmapapi.concerts.interfaces.rest.transform.AddAttendeeCommandFromResourceAssembler;
import com.staybits.gigmapapi.concerts.interfaces.rest.transform.ConcertResourceFromEntityAssembler;
import com.staybits.gigmapapi.concerts.interfaces.rest.transform.CreateConcertCommandFromResourceAssembler;
import com.staybits.gigmapapi.concerts.interfaces.rest.transform.RemoveAttendeeCommandFromResourceAssembler;
import com.staybits.gigmapapi.concerts.interfaces.rest.transform.UpdateConcertCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/concerts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Concerts", description = "Operations related to Concerts")
public class ConcertsController {

    private final ConcertCommandService concertCommandService;
    private final ConcertQueryService concertQueryService;

    public ConcertsController(ConcertCommandService concertCommandService, ConcertQueryService concertQueryService) {
        this.concertCommandService = concertCommandService;
        this.concertQueryService = concertQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new concert", description = "Creates a new concert with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Concert created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ConcertResource> createConcert(@RequestBody CreateConcertResource resource) {
        var command = CreateConcertCommandFromResourceAssembler.toCommandFromResource(resource);
        var concert = concertCommandService.handle(command);

        if (concert == null || concert.getId() == null || concert.getId() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var concertResponse = ConcertResourceFromEntityAssembler.toResourceFromEntity(concert);
        return new ResponseEntity<>(concertResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all concerts", description = "Retrieve all concerts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concerts found"),
            @ApiResponse(responseCode = "404", description = "No concerts found")
    })
    public ResponseEntity<List<ConcertResource>> getAllConcerts() {
        var concerts = concertQueryService.handle(new GetAllConcertsQuery());

        if (concerts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var concertResources = ConcertResourceFromEntityAssembler.toResourcesFromEntities(concerts);
        return ResponseEntity.ok(concertResources);
    }

    @GetMapping("/{concertId}")
    @Operation(summary = "Get concert by ID", description = "Retrieve a concert by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concert found"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public ResponseEntity<ConcertResource> getConcertById(@PathVariable Long concertId) {
        var concert = concertQueryService.handle(new GetConcertByIdQuery(concertId));

        if (concert.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var concertResource = ConcertResourceFromEntityAssembler.toResourceFromEntity(concert.get());
        return ResponseEntity.ok(concertResource);
    }

    @PutMapping("/{concertId}")
    @Operation(summary = "Update concert", description = "Update an existing concert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concert updated successfully"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public ResponseEntity<ConcertResource> updateConcert(@PathVariable Long concertId,
            @RequestBody UpdateConcertResource resource) {
        var command = UpdateConcertCommandFromResourceAssembler.toCommandFromResource(resource);
        var concert = concertCommandService.handle(command);

        if (concert == null) {
            return ResponseEntity.notFound().build();
        }

        var concertResource = ConcertResourceFromEntityAssembler.toResourceFromEntity(concert);
        return ResponseEntity.ok(concertResource);
    }

    @DeleteMapping("/{concertId}")
    @Operation(summary = "Delete concert", description = "Delete a concert by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concert deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public ResponseEntity<String> deleteConcert(@PathVariable Long concertId) {
        var command = new DeleteConcertCommand(concertId);
        boolean deleted = concertCommandService.handle(command);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Concert with given id successfully deleted");
    }

    @GetMapping("/genre/{genre}")
    @Operation(summary = "Get concerts by genre", description = "Retrieve concerts filtered by genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concerts found"),
            @ApiResponse(responseCode = "404", description = "No concerts found")
    })
    public ResponseEntity<List<ConcertResource>> getConcertsByGenre(@PathVariable String genre) {
        try {
            var genreEnum = Genre.valueOf(genre.toUpperCase());
            var concerts = concertQueryService.handle(new GetConcertsByGenreQuery(genreEnum));

            if (concerts.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var concertResources = ConcertResourceFromEntityAssembler.toResourcesFromEntities(concerts);
            return ResponseEntity.ok(concertResources);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/artist/{artistId}")
    @Operation(summary = "Get concerts by artist", description = "Retrieve concerts created by a specific artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concerts found"),
            @ApiResponse(responseCode = "404", description = "No concerts found")
    })
    public ResponseEntity<List<ConcertResource>> getConcertsByArtist(@PathVariable Long artistId) {
        var concerts = concertQueryService.handle(new GetConcertsByArtistQuery(artistId));

        if (concerts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var concertResources = ConcertResourceFromEntityAssembler.toResourcesFromEntities(concerts);
        return ResponseEntity.ok(concertResources);
    }

    @PostMapping("/attendees")
    @Operation(summary = "Add attendee to concert", description = "Confirms a user's attendance to a concert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendee added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Concert or user not found")
    })
    public ResponseEntity<ConcertResource> addAttendee(@RequestBody AttendeeResource resource) {
        var command = AddAttendeeCommandFromResourceAssembler.toCommandFromResource(resource);
        var concert = concertCommandService.handle(command);

        if (concert == null) {
            return ResponseEntity.notFound().build();
        }

        var concertResource = ConcertResourceFromEntityAssembler.toResourceFromEntity(concert);
        return ResponseEntity.ok(concertResource);
    }

    @DeleteMapping("/attendees")
    @Operation(summary = "Remove attendee from concert", description = "Removes a user's attendance from a concert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendee removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Concert or user not found")
    })
    public ResponseEntity<ConcertResource> removeAttendee(@RequestBody AttendeeResource resource) {
        var command = RemoveAttendeeCommandFromResourceAssembler.toCommandFromResource(resource);
        var concert = concertCommandService.handle(command);

        if (concert == null) {
            return ResponseEntity.notFound().build();
        }

        var concertResource = ConcertResourceFromEntityAssembler.toResourceFromEntity(concert);
        return ResponseEntity.ok(concertResource);
    }

    @GetMapping("/attended/{userId}")
    @Operation(summary = "Get all concerts attended by a User", description = "Get all concerts attended by a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concerts attended retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No concerts attended found for the given User")
    })
    public ResponseEntity<List<ConcertResource>> getAllConcertsAttendedByUserId(@PathVariable Long userId) {
        var concertsAttended = this.concertQueryService.handle(new GetAllConcertsAttendedByUserIdQuery(userId));

        if (concertsAttended.isEmpty())
            return ResponseEntity.notFound().build();

        var concertsAttendedResources = concertsAttended.stream()
                .map(ConcertResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(concertsAttendedResources);
    }
}
