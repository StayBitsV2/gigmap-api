package com.staybits.gigmapapi.authentication.domain.model.queries;

/**
 * Is user following artist query
 * <p>
 *     This class represents the query to check if a user is following an artist.
 * </p>
 * @param fanId the id of the fan (user who might be following)
 * @param artistId the id of the artist
 */
public record IsUserFollowingArtistQuery(Long fanId, Long artistId) {
}

