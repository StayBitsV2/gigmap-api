package com.staybits.gigmapapi.connections.application;

import com.staybits.gigmapapi.connections.domain.model.aggregates.Connection;
import com.staybits.gigmapapi.connections.domain.model.aggregates.ConnectionRequest;
import com.staybits.gigmapapi.connections.domain.model.valueobjects.ConnectionRequestStatus;
import com.staybits.gigmapapi.connections.domain.services.ConnectionService;
import com.staybits.gigmapapi.connections.infrastructure.persistence.jpa.repositories.ConnectionRepository;
import com.staybits.gigmapapi.connections.infrastructure.persistence.jpa.repositories.ConnectionRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("connectionQueryServiceImpl")
public class ConnectionQueryServiceImpl implements ConnectionService {

    private final ConnectionRequestRepository connectionRequestRepository;
    private final ConnectionRepository connectionRepository;

    public ConnectionQueryServiceImpl(ConnectionRequestRepository connectionRequestRepository,
            ConnectionRepository connectionRepository) {
        this.connectionRequestRepository = connectionRequestRepository;
        this.connectionRepository = connectionRepository;
    }

    @Override
    public ConnectionRequest createRequest(
            com.staybits.gigmapapi.connections.domain.model.commands.CreateConnectionRequestCommand command) {
        throw new UnsupportedOperationException("Use ConnectionCommandServiceImpl for write operations");
    }

    @Override
    public Connection acceptRequest(
            com.staybits.gigmapapi.connections.domain.model.commands.AcceptConnectionRequestCommand command) {
        throw new UnsupportedOperationException("Use ConnectionCommandServiceImpl for write operations");
    }

    @Override
    public void rejectRequest(
            com.staybits.gigmapapi.connections.domain.model.commands.RejectConnectionRequestCommand command) {
        throw new UnsupportedOperationException("Use ConnectionCommandServiceImpl for write operations");
    }

    @Override
    public List<ConnectionRequest> getPendingRequestsForUser(Long userId) {
        return connectionRequestRepository.findByTargetIdAndStatus(userId, ConnectionRequestStatus.PENDING);
    }

    @Override
    public List<Connection> getUserConnections(Long userId) {
        return connectionRepository.findByUserId(userId);
    }

    @Override
    public boolean areConnected(Long userId1, Long userId2) {
        return connectionRepository.areConnected(userId1, userId2);
    }

    @Override
    public Optional<ConnectionRequest> getRequestStatus(Long requesterId, Long targetId) {
        return connectionRequestRepository.findByRequesterIdAndTargetIdAndStatus(
                requesterId, targetId, ConnectionRequestStatus.PENDING);
    }
}
