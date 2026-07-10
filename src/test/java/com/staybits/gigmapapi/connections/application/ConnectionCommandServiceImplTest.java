package com.staybits.gigmapapi.connections.application;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.connections.domain.model.aggregates.Connection;
import com.staybits.gigmapapi.connections.domain.model.aggregates.ConnectionRequest;
import com.staybits.gigmapapi.connections.domain.model.commands.AcceptConnectionRequestCommand;
import com.staybits.gigmapapi.connections.domain.model.commands.CreateConnectionRequestCommand;
import com.staybits.gigmapapi.connections.domain.model.commands.RejectConnectionRequestCommand;
import com.staybits.gigmapapi.connections.domain.model.valueobjects.ConnectionRequestStatus;
import com.staybits.gigmapapi.connections.infrastructure.persistence.jpa.repositories.ConnectionRepository;
import com.staybits.gigmapapi.connections.infrastructure.persistence.jpa.repositories.ConnectionRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectionCommandServiceImplTest {

    @Mock
    ConnectionRequestRepository connectionRequestRepository;

    @Mock
    ConnectionRepository connectionRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ConnectionCommandServiceImpl connectionCommandService;

    @Test
    void Test_CreateRequest_Success() {
        User requester = new User("requester@test.com", "requester", "Requester", Role.FAN);
        User target = new User("target@test.com", "target", "Target", Role.FAN);

        when(connectionRepository.areConnected(1L, 2L)).thenReturn(false);
        when(connectionRequestRepository.existsByRequesterIdAndTargetIdAndStatus(1L, 2L, ConnectionRequestStatus.PENDING)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(requester));
        when(userRepository.findById(2L)).thenReturn(Optional.of(target));

        ConnectionRequest requestToSave = new ConnectionRequest(requester, target);
        when(connectionRequestRepository.save(any(ConnectionRequest.class))).thenReturn(requestToSave);

        var result = connectionCommandService.createRequest(new CreateConnectionRequestCommand(1L, 2L));

        assertNotNull(result);
        assertEquals(ConnectionRequestStatus.PENDING, result.getStatus());
        assertEquals(requester, result.getRequester());
        assertEquals(target, result.getTarget());
    }

    @Test
    void Test_CreateRequest_SelfRequest_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> connectionCommandService.createRequest(new CreateConnectionRequestCommand(1L, 1L)));
    }

    @Test
    void Test_CreateRequest_AlreadyConnected_Throws() {
        when(connectionRepository.areConnected(1L, 2L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> connectionCommandService.createRequest(new CreateConnectionRequestCommand(1L, 2L)));
    }

    @Test
    void Test_CreateRequest_DuplicatePending_Throws() {
        when(connectionRepository.areConnected(1L, 2L)).thenReturn(false);
        when(connectionRequestRepository.existsByRequesterIdAndTargetIdAndStatus(1L, 2L, ConnectionRequestStatus.PENDING)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> connectionCommandService.createRequest(new CreateConnectionRequestCommand(1L, 2L)));
    }

    @Test
    void Test_AcceptRequest_Success() {
        User requester = mock(User.class);
        User target = mock(User.class);
        when(requester.getId()).thenReturn(3L);
        when(target.getId()).thenReturn(5L);

        ConnectionRequest request = new ConnectionRequest(requester, target);
        when(connectionRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(connectionRequestRepository.save(any(ConnectionRequest.class))).thenReturn(request);
        when(connectionRepository.save(any(Connection.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = connectionCommandService.acceptRequest(new AcceptConnectionRequestCommand(1L));

        assertNotNull(result);
        assertEquals(ConnectionRequestStatus.ACCEPTED, request.getStatus());
        verify(connectionRequestRepository).save(request);
        verify(connectionRepository).save(any(Connection.class));
    }

    @Test
    void Test_AcceptRequest_NotFound_Throws() {
        when(connectionRequestRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> connectionCommandService.acceptRequest(new AcceptConnectionRequestCommand(999L)));
    }

    @Test
    void Test_RejectRequest_Success() {
        User requester = mock(User.class);
        User target = mock(User.class);
        ConnectionRequest request = new ConnectionRequest(requester, target);

        when(connectionRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(connectionRequestRepository.save(any(ConnectionRequest.class))).thenReturn(request);

        connectionCommandService.rejectRequest(new RejectConnectionRequestCommand(1L));

        assertEquals(ConnectionRequestStatus.REJECTED, request.getStatus());
        verify(connectionRequestRepository).save(request);
    }

    @Test
    void Test_RejectRequest_NotFound_Throws() {
        when(connectionRequestRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> connectionCommandService.rejectRequest(new RejectConnectionRequestCommand(999L)));
    }
}
