package com.staybits.gigmapapi.communities.interfaces.rest.resources;

import java.util.List;

public record ForumResource(Long id, String name, String description, String image, String genre, List<Long> posts, List<Long> members) {}
