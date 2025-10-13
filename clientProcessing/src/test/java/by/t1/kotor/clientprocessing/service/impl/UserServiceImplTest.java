package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.mapper.UserMapper;
import by.t1.kotor.clientprocessing.model.Role;
import by.t1.kotor.clientprocessing.model.User;
import by.t1.kotor.clientprocessing.model.dto.client.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.security.JwtAuthenticationDto;
import by.t1.kotor.clientprocessing.model.dto.security.RefreshTokenDto;
import by.t1.kotor.clientprocessing.model.dto.security.UserCredentialDto;
import by.t1.kotor.clientprocessing.model.enums.RoleEnum;
import by.t1.kotor.clientprocessing.repository.UserRepository;
import by.t1.kotor.clientprocessing.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.AuthenticationException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void create_shouldMapUserAndSave() {
        ClientRegistrationRequest request = mock(ClientRegistrationRequest.class);
        Set<Role> roles = Set.of(new Role(RoleEnum.CURRENT_CLIENT));
        User user = new User();
        User savedUser = new User();

        when(userMapper.toEntity(request, passwordEncoder)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);

        User result = service.create(request, roles);

        assertEquals(savedUser, result);
        assertEquals(roles, user.getRoles());
        verify(userMapper).toEntity(request, passwordEncoder);
        verify(userRepository).save(user);
    }

    @Test
    void signIn_shouldReturnJwt_whenCredentialsCorrect() throws Exception {
        UserCredentialDto dto = new UserCredentialDto("test@test.com", "pass");
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword("encodedPass");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);
        JwtAuthenticationDto jwt = mock(JwtAuthenticationDto.class);
        when(jwtService.generateAuthToken(user.getEmail())).thenReturn(jwt);

        JwtAuthenticationDto result = service.signIn(dto);

        assertEquals(jwt, result);
    }

    @Test
    void signIn_shouldThrow_whenCredentialsIncorrect() {
        UserCredentialDto dto = new UserCredentialDto("test@test.com", "wrongPass");
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> service.signIn(dto));
    }

    @Test
    void refreshToken_shouldReturnJwt_whenTokenValid() throws Exception {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto("validToken");
        User user = new User();
        user.setEmail("user@test.com");

        when(jwtService.validateJwtToken("validToken")).thenReturn(true);
        when(jwtService.getEmailFromToken("validToken")).thenReturn("user@test.com");
        when(userRepository.findByEmailWithRoles("user@test.com")).thenReturn(Optional.of(user));
        JwtAuthenticationDto jwt = mock(JwtAuthenticationDto.class);
        when(jwtService.refreshBaseToken("user@test.com", "validToken")).thenReturn(jwt);

        JwtAuthenticationDto result = service.refreshToken(refreshTokenDto);

        assertEquals(jwt, result);
    }

    @Test
    void refreshToken_shouldThrow_whenTokenInvalid() {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto("invalidToken");
        when(jwtService.validateJwtToken("invalidToken")).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> service.refreshToken(refreshTokenDto));
    }

}
