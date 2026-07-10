package com.staybits.gigmapapi.connections.domain.services;

import com.staybits.gigmapapi.connections.domain.model.aggregates.Connection;
import com.staybits.gigmapapi.connections.domain.model.aggregates.ConnectionRequest;
import com.staybits.gigmapapi.connections.domain.model.commands.AcceptConnectionRequestCommand;
import com.staybits.gigmapapi.connections.domain.model.commands.CreateConnectionRequestCommand;
import com.staybits.gigmapapi.connections.domain.model.commands.RejectConnectionRequestCommand;

import java.util.List;
import java.util.Optional;

public interface ConnectionService {
    ConnectionRequest createRequest(CreateConnectionRequestCommand command);
    Connection acceptRequest(AcceptConnectionRequestCommand command);
    void rejectRequest(RejectConnectionRequestCommand command);
    List<ConnectionRequest> getPendingRequestsForUser(Long userId);
    List<Connection> getUserConnections(Long userId);
    boolean areConnected(Long userId1, Long userId2);
    Optional<ConnectionRequest> getRequestStatus(Long requesterId, Long targetId);
}
