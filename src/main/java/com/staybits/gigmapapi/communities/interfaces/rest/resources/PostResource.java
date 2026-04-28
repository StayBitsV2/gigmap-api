package com.staybits.gigmapapi.communities.interfaces.rest.resources;

import java.util.List;

public record PostResource(Long id, Long communityId, Long userId, String content, String image, List<Long> likes) {
    
}
