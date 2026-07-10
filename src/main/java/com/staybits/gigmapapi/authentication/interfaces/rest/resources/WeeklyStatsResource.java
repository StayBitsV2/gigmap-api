package com.staybits.gigmapapi.authentication.interfaces.rest.resources;

public record WeeklyStatsResource(
    String weekStart,
    String weekEnd,
    long newFollowers,
    long profileViews,
    ExternalLinkClicksResource externalLinkClicks
) {}
