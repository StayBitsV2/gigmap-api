package com.staybits.gigmapapi.relatedevents.interfaces.rest;

import com.staybits.gigmapapi.relatedevents.domain.model.commands.DeleteRelatedEventCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.queries.GetRelatedEventByIdQuery;
import com.staybits.gigmapapi.relatedevents.domain.model.queries.GetRelatedEventsByConcertIdQuery;
import com.staybits.gigmapapi.relatedevents.domain.services.RelatedEventCommandService;
import com.staybits.gigmapapi.relatedevents.domain.services.RelatedEventQueryService;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.resources.CreateRelatedEventResource;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.resources.ParticipantResource;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.resources.RelatedEventResource;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.resources.UpdateRelatedEventResource;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.transform.AddParticipantCommandFromResourceAssembler;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.transform.CreateRelatedEventCommandFromResourceAssembler;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.transform.RelatedEventResourceFromEntityAssembler;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.transform.RemoveParticipantCommandFromResourceAssembler;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.transform.UpdateRelatedEventCommandFromResourceAssembler;
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
@RequestMapping(value = "/api/v1/related-events", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Related Events", description = "Operations related to Related Events")
public class RelatedEventsController {

    private final RelatedEventCommandService relatedEventCommandService;
    private final RelatedEventQueryService relatedEventQueryService;

    public RelatedEventsController(RelatedEventCommandService relatedEventCommandService,
                                  RelatedEventQueryService relatedEventQueryService) {
        this.relatedEventCommandService = relatedEventCommandService;
        this.relatedEventQueryService = relatedEventQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new related event", description = "Creates a new related event associated with a concert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Related event created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<RelatedEventResource> createRelatedEvent(@RequestBody CreateRelatedEventResource resource) {
        var command = CreateRelatedEventCommandFromResourceAssembler.toCommandFromResource(resource);
        var relatedEvent = relatedEventCommandService.handle(command);

        if (relatedEvent == null || relatedEvent.getId() == null || relatedEvent.getId() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var response = RelatedEventResourceFromEntityAssembler.toResourceFromEntity(relatedEvent);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{relatedEventId}")
    @Operation(summary = "Get related event by ID", description = "Retrieve a related event by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Related event found"),
            @ApiResponse(responseCode = "404", description = "Related event not found")
    })
    public ResponseEntity<RelatedEventResource> getRelatedEventById(@PathVariable Long relatedEventId) {
        var relatedEvent = relatedEventQueryService.handle(new GetRelatedEventByIdQuery(relatedEventId));

        if (relatedEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resource = RelatedEventResourceFromEntityAssembler.toResourceFromEntity(relatedEvent.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/concert/{concertId}")
    @Operation(summary = "Get related events by concert ID", description = "Retrieve all related events for a specific concert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Related events found"),
            @ApiResponse(responseCode = "404", description = "No related events found")
    })
    public ResponseEntity<List<RelatedEventResource>> getRelatedEventsByConcertId(@PathVariable Long concertId) {
        var relatedEvents = relatedEventQueryService.handle(new GetRelatedEventsByConcertIdQuery(concertId));

        if (relatedEvents.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resources = RelatedEventResourceFromEntityAssembler.toResourcesFromEntities(relatedEvents);
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{relatedEventId}")
    @Operation(summary = "Update related event", description = "Update an existing related event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Related event updated successfully"),
            @ApiResponse(responseCode = "404", description = "Related event not found")
    })
    public ResponseEntity<RelatedEventResource> updateRelatedEvent(@PathVariable Long relatedEventId,
                                                                   @RequestBody UpdateRelatedEventResource resource) {
        var command = UpdateRelatedEventCommandFromResourceAssembler.toCommandFromResource(resource);
        var relatedEvent = relatedEventCommandService.handle(command);

        if (relatedEvent == null) {
            return ResponseEntity.notFound().build();
        }

        var eventResource = RelatedEventResourceFromEntityAssembler.toResourceFromEntity(relatedEvent);
        return ResponseEntity.ok(eventResource);
    }

    @DeleteMapping("/{relatedEventId}")
    @Operation(summary = "Delete related event", description = "Delete a related event by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Related event deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Related event not found")
    })
    public ResponseEntity<String> deleteRelatedEvent(@PathVariable Long relatedEventId) {
        var command = new DeleteRelatedEventCommand(relatedEventId);
        boolean deleted = relatedEventCommandService.handle(command);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Related event with given id successfully deleted");
    }

    @PostMapping("/participants")
    @Operation(summary = "Add participant to related event", description = "Confirms a user's participation in a related event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participant added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Related event or user not found")
    })
    public ResponseEntity<RelatedEventResource> addParticipant(@RequestBody ParticipantResource resource) {
        var command = AddParticipantCommandFromResourceAssembler.toCommandFromResource(resource);
        var relatedEvent = relatedEventCommandService.handle(command);

        if (relatedEvent == null) {
            return ResponseEntity.notFound().build();
        }

        var eventResource = RelatedEventResourceFromEntityAssembler.toResourceFromEntity(relatedEvent);
        return ResponseEntity.ok(eventResource);
    }

    @DeleteMapping("/participants")
    @Operation(summary = "Remove participant from related event", description = "Removes a user's participation from a related event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participant removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Related event or user not found")
    })
    public ResponseEntity<RelatedEventResource> removeParticipant(@RequestBody ParticipantResource resource) {
        var command = RemoveParticipantCommandFromResourceAssembler.toCommandFromResource(resource);
        var relatedEvent = relatedEventCommandService.handle(command);

        if (relatedEvent == null) {
            return ResponseEntity.notFound().build();
        }

        var eventResource = RelatedEventResourceFromEntityAssembler.toResourceFromEntity(relatedEvent);
        return ResponseEntity.ok(eventResource);
    }
}
