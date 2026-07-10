package com.staybits.gigmapapi.communities.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.staybits.gigmapapi.communities.domain.model.queries.*;
import com.staybits.gigmapapi.communities.domain.model.commands.*;
import com.staybits.gigmapapi.communities.domain.services.*;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.*;
import com.staybits.gigmapapi.communities.interfaces.rest.transform.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/forums", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Forums", description = "Operations related to Forums")
public class ForumsController {
    private final ForumCommandService forumCommandService;
    private final ForumQueryService forumQueryService;
    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;
    private final ReactionCommandService reactionCommandService;
    private final ReactionQueryService reactionQueryService;
    private final ReportCommandService reportCommandService;

    public ForumsController(ForumCommandService forumCommandService,
                            ForumQueryService forumQueryService,
                            CommentCommandService commentCommandService,
                            CommentQueryService commentQueryService,
                            ReactionCommandService reactionCommandService,
                            ReactionQueryService reactionQueryService,
                            ReportCommandService reportCommandService) {
        this.forumCommandService = forumCommandService;
        this.forumQueryService = forumQueryService;
        this.commentCommandService = commentCommandService;
        this.commentQueryService = commentQueryService;
        this.reactionCommandService = reactionCommandService;
        this.reactionQueryService = reactionQueryService;
        this.reportCommandService = reportCommandService;
    }

    @GetMapping
    @Operation(summary = "Get all forums", description = "Get all forums (communities with genre)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forums found"),
            @ApiResponse(responseCode = "404", description = "Forums not found")
    })
    public ResponseEntity<List<ForumResource>> getAllForums() {
        var forums = forumQueryService.handle(new GetForumsQuery());
        if (forums.isEmpty())
            return ResponseEntity.notFound().build();
        var forumResources = forums.stream()
                .map(ForumResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(forumResources);
    }

    @GetMapping("/{forumId}")
    @Operation(summary = "Get forum by id", description = "Get forum by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forum found"),
            @ApiResponse(responseCode = "404", description = "Forum not found")
    })
    public ResponseEntity<ForumResource> getForumById(@PathVariable Long forumId) {
        var forums = forumQueryService.handle(new GetForumsQuery());
        var forum = forums.stream().filter(c -> c.getId().equals(forumId)).findFirst();
        if (forum.isEmpty())
            return ResponseEntity.notFound().build();
        var forumResource = ForumResourceFromEntityAssembler.toResourceFromEntity(forum.get());
        return ResponseEntity.ok(forumResource);
    }

    @GetMapping("/{forumId}/threads")
    @Operation(summary = "Get threads by forum", description = "Get all threads of a forum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Threads found"),
            @ApiResponse(responseCode = "404", description = "Threads not found")
    })
    public ResponseEntity<List<ThreadResource>> getThreadsByForumId(@PathVariable Long forumId) {
        var threads = forumQueryService.handle(new GetThreadsByForumIdQuery(forumId));
        if (threads.isEmpty())
            return ResponseEntity.notFound().build();
        var threadResources = threads.stream()
                .map(ThreadResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(threadResources);
    }

    @GetMapping("/threads/{threadId}")
    @Operation(summary = "Get thread detail", description = "Get thread detail with comments and reactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thread found"),
            @ApiResponse(responseCode = "404", description = "Thread not found")
    })
    public ResponseEntity<Map<String, Object>> getThreadDetail(@PathVariable Long threadId) {
        var thread = forumQueryService.handle(new GetThreadByIdQuery(threadId));
        if (thread.isEmpty())
            return ResponseEntity.notFound().build();

        var threadResource = ThreadResourceFromEntityAssembler.toResourceFromEntity(thread.get());
        var comments = commentQueryService.handle(new GetCommentsByThreadIdQuery(threadId)).stream()
                .map(CommentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        var reactions = reactionQueryService.handle(new GetReactionsByTargetQuery(threadId, null)).stream()
                .map(ReactionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        var response = Map.of(
            "thread", threadResource,
            "comments", comments,
            "reactions", reactions
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{forumId}/threads")
    @Operation(summary = "Create a thread", description = "Creates a new thread in a forum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Thread created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ThreadResource> createThread(@PathVariable Long forumId, @RequestBody CreateThreadResource threadResource) {
        var command = CreateThreadCommandFromResourceAssembler.toCommandFromResource(threadResource);
        var thread = forumCommandService.handle(command);
        if (thread.getId() == null || thread.getId() <= 0)
            return ResponseEntity.badRequest().build();
        var threadResponse = ThreadResourceFromEntityAssembler.toResourceFromEntity(thread);
        return new ResponseEntity<>(threadResponse, HttpStatus.CREATED);
    }

    @PostMapping("/threads/{threadId}/comments")
    @Operation(summary = "Add comment to thread", description = "Adds a comment to a thread")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<CommentResource> addComment(@PathVariable Long threadId, @RequestBody CreateCommentResource commentResource) {
        var command = CreateCommentCommandFromResourceAssembler.toCommandFromResource(commentResource);
        var comment = commentCommandService.handle(command);
        if (comment.getId() == null || comment.getId() <= 0)
            return ResponseEntity.badRequest().build();
        var commentResponse = CommentResourceFromEntityAssembler.toResourceFromEntity(comment);
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @PostMapping("/threads/{threadId}/reactions")
    @Operation(summary = "React to thread", description = "Adds a reaction to a thread")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ReactionResource> reactToThread(@PathVariable Long threadId, @RequestBody CreateReactionResource reactionResource) {
        var command = CreateReactionCommandFromResourceAssembler.toCommandFromResource(reactionResource);
        var reaction = reactionCommandService.handle(command);
        if (reaction.getId() == null || reaction.getId() <= 0)
            return ResponseEntity.badRequest().build();
        var reactionResponse = ReactionResourceFromEntityAssembler.toResourceFromEntity(reaction);
        return new ResponseEntity<>(reactionResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/threads/{threadId}/reactions/{reactionId}")
    @Operation(summary = "Remove reaction from thread", description = "Removes a reaction from a thread")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reaction removed successfully"),
            @ApiResponse(responseCode = "404", description = "Reaction not found")
    })
    public ResponseEntity<Void> removeThreadReaction(@PathVariable Long threadId, @PathVariable Long reactionId) {
        reactionCommandService.handle(new RemoveReactionCommand(reactionId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/threads/{threadId}/reports")
    @Operation(summary = "Report thread", description = "Reports a thread")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ReportResource> reportThread(@PathVariable Long threadId, @RequestBody CreateReportResource reportResource) {
        var command = CreateReportCommandFromResourceAssembler.toCommandFromResource(reportResource);
        var report = reportCommandService.handle(command);
        if (report.getId() == null || report.getId() <= 0)
            return ResponseEntity.badRequest().build();
        var reportResponse = ReportResourceFromEntityAssembler.toResourceFromEntity(report);
        return new ResponseEntity<>(reportResponse, HttpStatus.CREATED);
    }

    @PostMapping("/comments/{commentId}/reactions")
    @Operation(summary = "React to comment", description = "Adds a reaction to a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ReactionResource> reactToComment(@PathVariable Long commentId, @RequestBody CreateReactionResource reactionResource) {
        var command = CreateReactionCommandFromResourceAssembler.toCommandFromResource(reactionResource);
        var reaction = reactionCommandService.handle(command);
        if (reaction.getId() == null || reaction.getId() <= 0)
            return ResponseEntity.badRequest().build();
        var reactionResponse = ReactionResourceFromEntityAssembler.toResourceFromEntity(reaction);
        return new ResponseEntity<>(reactionResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}/reactions/{reactionId}")
    @Operation(summary = "Remove reaction from comment", description = "Removes a reaction from a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reaction removed successfully"),
            @ApiResponse(responseCode = "404", description = "Reaction not found")
    })
    public ResponseEntity<Void> removeCommentReaction(@PathVariable Long commentId, @PathVariable Long reactionId) {
        reactionCommandService.handle(new RemoveReactionCommand(reactionId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments/{commentId}/reports")
    @Operation(summary = "Report comment", description = "Reports a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ReportResource> reportComment(@PathVariable Long commentId, @RequestBody CreateReportResource reportResource) {
        var command = CreateReportCommandFromResourceAssembler.toCommandFromResource(reportResource);
        var report = reportCommandService.handle(command);
        if (report.getId() == null || report.getId() <= 0)
            return ResponseEntity.badRequest().build();
        var reportResponse = ReportResourceFromEntityAssembler.toResourceFromEntity(report);
        return new ResponseEntity<>(reportResponse, HttpStatus.CREATED);
    }
}
