package com.staybits.gigmapapi.authentication.application.internal.commandservices;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.commands.UpdateUserCommand;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)

class UserCommandServiceImplTest {
    @Mock UserRepository userRepository;

    @InjectMocks UserCommandServiceImpl userCommandServiceImpl;

    User user = new User("correo@prueba","usuario1","Roberto", Role.FAN);

    UpdateUserCommand command = new UpdateUserCommand(1L,"correo@prueba","usuarioprueba","Robertito", Role.FAN,"","",null,null,null,null,null,null);

    @Test
    void Test_UserUpdate() {
    when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    userCommandServiceImpl.handle(command);
    assertEquals("usuarioprueba",user.getUsername());
    assertEquals("Robertito",user.getName());
    }
}