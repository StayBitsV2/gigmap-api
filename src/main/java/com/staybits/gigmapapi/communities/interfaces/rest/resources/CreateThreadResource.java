package com.staybits.gigmapapi.communities.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateThreadResource(
    String title,
    String content,
    @JsonProperty(required = false) String imageUrl,
    Long communityId,
    Long userId
) {}
