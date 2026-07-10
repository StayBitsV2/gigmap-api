package com.staybits.gigmapapi.authentication.interfaces.rest.resources;

public record ExternalLinkClicksResource(
    long spotify,
    long instagram,
    long youtube
) {}
