package by.t1.kotor.clientprocessing.service;

import by.t1.kotor.clientprocessing.model.Role;
import by.t1.kotor.clientprocessing.model.User;
import by.t1.kotor.clientprocessing.model.dto.client.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.security.JwtAuthenticationDto;
import by.t1.kotor.clientprocessing.model.dto.security.RefreshTokenDto;
import by.t1.kotor.clientprocessing.model.dto.security.UserCredentialDto;

import javax.naming.AuthenticationException;
import java.util.Set;

public interface UserService {
    JwtAuthenticationDto signIn(UserCredentialDto userCredentialDto) throws AuthenticationException;
    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception;
    User create(ClientRegistrationRequest request, Set<Role> roles);
}
