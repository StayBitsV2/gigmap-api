package com.staybits.gigmapapi.authentication.interfaces.rest.resources;

import java.util.List;

public record ArtistStatsResource(
    Long artistId,
    List<WeeklyStatsResource> weeks,
    boolean hasHistoricalData,
    String message
) {}
