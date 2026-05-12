package com.staybits.gigmapapi.authentication.application.internal.queryservices;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.queries.*;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)

class UserQueryServiceImplTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks UserQueryServiceImpl userQueryServiceImpl;
    User user = new User("correo@prueba","usuario1","Roberto", Role.FAN);
    User user2 = new User("correo2@prueba","usuario2","Juan", Role.FAN);
    GetAllUsersQuery queryAll = new GetAllUsersQuery();
    GetUserByIdQuery queryById = new GetUserByIdQuery(1L);
    GetUserByUsernameQuery queryByUsername = new GetUserByUsernameQuery("usuario1");
    GetUsersByCommunityIdQuery queryCommunity = new GetUsersByCommunityIdQuery(1L);
    GetUserDetailsByIdQuery queryDetails = new GetUserDetailsByIdQuery(1L);
    List<User> users = List.of(user,user2);
    @Test
    void Test_AllUsers() {
    when(userRepository.findAll()).thenReturn(users);
    List<User> users = userQueryServiceImpl.handle(queryAll);
    assertEquals(2,users.size());

    }

    @Test
    void Test_UserById() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        Optional<User> result = userQueryServiceImpl.handle(queryById);

        assertTrue(result.isPresent());
        assertEquals("Roberto", result.get().getName());

    }


    @Test
    void Test_UserByUsername() {

        when(userRepository.findByUsername("usuario1"))
                .thenReturn(Optional.of(user));

        Optional<User> result = userQueryServiceImpl.handle(queryByUsername);

        assertTrue(result.isPresent());
        assertEquals("correo@prueba", result.get().getEmail());
    }


    @Test
    void Test_UserDetailsById() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        Optional<User> result = userQueryServiceImpl.handle(queryDetails);

        assertTrue(result.isPresent());
        assertEquals("usuario1", result.get().getUsername());
    }
}