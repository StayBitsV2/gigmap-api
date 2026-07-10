package com.staybits.gigmapapi.connections.application;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.connections.domain.model.aggregates.Connection;
import com.staybits.gigmapapi.connections.domain.model.aggregates.ConnectionRequest;
import com.staybits.gigmapapi.connections.domain.model.commands.AcceptConnectionRequestCommand;
import com.staybits.gigmapapi.connections.domain.model.commands.CreateConnectionRequestCommand;
import com.staybits.gigmapapi.connections.domain.model.commands.RejectConnectionRequestCommand;
import com.staybits.gigmapapi.connections.domain.model.valueobjects.ConnectionRequestStatus;
import com.staybits.gigmapapi.connections.domain.services.ConnectionService;
import com.staybits.gigmapapi.connections.infrastructure.persistence.jpa.repositories.ConnectionRepository;
import com.staybits.gigmapapi.connections.infrastructure.persistence.jpa.repositories.ConnectionRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("connectionCommandServiceImpl")
@org.springframework.context.annotation.Primary
public class ConnectionCommandServiceImpl implements ConnectionService {

    private final ConnectionRequestRepository connectionRequestRepository;
    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    public ConnectionCommandServiceImpl(ConnectionRequestRepository connectionRequestRepository,
            ConnectionRepository connectionRepository,
            UserRepository userRepository) {
        this.connectionRequestRepository = connectionRequestRepository;
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ConnectionRequest createRequest(CreateConnectionRequestCommand command) {
        if (command.requesterId().equals(command.targetId())) {
            throw new IllegalArgumentException("Cannot create connection request to yourself");
        }

        if (connectionRepository.areConnected(command.requesterId(), command.targetId())) {
            throw new IllegalArgumentException("Users are already connected");
        }

        if (connectionRequestRepository.existsByRequesterIdAndTargetIdAndStatus(
                command.requesterId(), command.targetId(), ConnectionRequestStatus.PENDING)) {
            throw new IllegalArgumentException("A pending request already exists between these users");
        }

        User requester = userRepository.findById(command.requesterId())
                .orElseThrow(() -> new IllegalArgumentException("Requester not found"));
        User target = userRepository.findById(command.targetId())
                .orElseThrow(() -> new IllegalArgumentException("Target not found"));

        var request = new ConnectionRequest(requester, target);
        return connectionRequestRepository.save(request);
    }

    @Override
    @Transactional
    public Connection acceptRequest(AcceptConnectionRequestCommand command) {
        var request = connectionRequestRepository.findById(command.requestId())
                .orElseThrow(() -> new IllegalArgumentException("Connection request not found"));

        if (!request.isPending()) {
            throw new IllegalStateException("Connection request is not PENDING");
        }

        request.accept();
        connectionRequestRepository.save(request);

        var connection = Connection.createFrom(request);
        return connectionRepository.save(connection);
    }

    @Override
    @Transactional
    public void rejectRequest(RejectConnectionRequestCommand command) {
        var request = connectionRequestRepository.findById(command.requestId())
                .orElseThrow(() -> new IllegalArgumentException("Connection request not found"));

        if (!request.isPending()) {
            throw new IllegalStateException("Connection request is not PENDING");
        }

        request.reject();
        connectionRequestRepository.save(request);
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
