package com.staybits.gigmapapi.communities.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.domain.model.commands.DeletePostCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.LikePostCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.UnlikePostCommand;
import com.staybits.gigmapapi.communities.domain.model.queries.GetAllLikedPostsByUserIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetPostByIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetPostsByCommunityIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetPostsQuery;
import com.staybits.gigmapapi.communities.domain.services.PostCommandService;
import com.staybits.gigmapapi.communities.domain.services.PostQueryService;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CreatePostResource;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.PostResource;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.UpdatePostResource;
import com.staybits.gigmapapi.communities.interfaces.rest.transform.CreatePostCommandFromResourceAssembler;
import com.staybits.gigmapapi.communities.interfaces.rest.transform.PostResourceFromEntityAssembler;
import com.staybits.gigmapapi.communities.interfaces.rest.transform.UpdatePostCommandFromResourceAssembler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/posts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Posts", description = "Operations related to Posts")
public class PostsController {
    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    public PostsController(PostCommandService postCommandService, PostQueryService postQueryService) {
        this.postCommandService = postCommandService;
        this.postQueryService = postQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new post", description = "Creates a new post with the provided details.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
    })
    public ResponseEntity<PostResource> createPost(@RequestBody CreatePostResource postResource) {
        var createdPostCommand = CreatePostCommandFromResourceAssembler.toCommandFromResource(postResource);
        var post = postCommandService.handle(createdPostCommand);

        if (post.getId() == null || post.getId() <= 0) return ResponseEntity.badRequest().build();

        var postResponse = PostResourceFromEntityAssembler.toResourceFromEntity(post);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a post", description = "Updates an existing post with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid post data"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<PostResource> updatePost(@PathVariable Long id, @RequestBody UpdatePostResource updatePostResource) {

        var updatePostCommand = UpdatePostCommandFromResourceAssembler.toCommandFromResource(id, updatePostResource);
        var updatedPost = postCommandService.handle(updatePostCommand);

        if (updatedPost.isEmpty())
            return ResponseEntity.notFound().build();

        var postResource = PostResourceFromEntityAssembler.toResourceFromEntity(updatedPost.get());
        return ResponseEntity.ok(postResource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Post", description = "Deletes an existing Post with the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        if (this.postQueryService.handle(new GetPostByIdQuery(id)).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        this.postCommandService.handle(new DeletePostCommand(id));
        return ResponseEntity.ok().build();
    }
    
    @GetMapping
    @Operation(summary = "Get all posts", description = "Get all posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Post not found")})
    public ResponseEntity<List<PostResource>> getAllPosts(@RequestParam(required = false) Long communityId) {
        List<Post> posts;

        if (communityId != null) {
            posts = postQueryService.handle(new GetPostsByCommunityIdQuery(communityId));
        } else {
            posts = postQueryService.handle(new GetPostsQuery());
        }

        if (posts.isEmpty()) return ResponseEntity.notFound().build();

        var postResources = posts.stream()
            .map(PostResourceFromEntityAssembler::toResourceFromEntity)
            .toList();

        return ResponseEntity.ok(postResources);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "Get post by id", description = "Get post by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Post not found")})
    public ResponseEntity<PostResource> getPostById(@PathVariable Long postId) {
        var post = postQueryService.handle(new GetPostByIdQuery(postId));
        if (post.isEmpty()) return ResponseEntity.notFound().build();
        var postEntity = post.get();
        var postResource = PostResourceFromEntityAssembler.toResourceFromEntity(postEntity);
        return ResponseEntity.ok(postResource);
    }

    @PostMapping("/{postId}/like")
    @Operation(summary = "Like a post", description = "User likes a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post liked successfully"),
            @ApiResponse(responseCode = "404", description = "Post or user not found")
    })
    public ResponseEntity<Void> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        postCommandService.handle(new LikePostCommand(postId, userId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/unlike")
    @Operation(summary = "Unlike a post", description = "User removes like from a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post unliked successfully"),
            @ApiResponse(responseCode = "404", description = "Post or user not found")
    })
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId, @RequestParam Long userId) {
        postCommandService.handle(new UnlikePostCommand(postId, userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/liked_by/{userId}")
    @Operation(
        summary = "Get all posts liked by a User",
        description = "Get all posts liked by a User"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liked posts retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No liked posts found for the given User")
    })
    public ResponseEntity<List<PostResource>> getAllLikedPostsByUserId(@PathVariable Long userId) {
        var likedPosts = this.postQueryService.handle(new GetAllLikedPostsByUserIdQuery(userId));

        if (likedPosts.isEmpty())
            return ResponseEntity.notFound().build();

        var likedPostsResources = likedPosts.stream()
                .map(PostResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(likedPostsResources);
    }
}