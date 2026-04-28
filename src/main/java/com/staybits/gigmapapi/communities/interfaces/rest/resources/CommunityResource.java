package com.staybits.gigmapapi.communities.interfaces.rest.resources;

import java.util.List;

public record CommunityResource(Long id, String name, String description, String image, List<Long> posts, List<Long> members) {
}
