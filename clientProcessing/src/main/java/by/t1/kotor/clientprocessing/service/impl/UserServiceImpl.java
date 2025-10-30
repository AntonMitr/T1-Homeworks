package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.mapper.UserMapper;
import by.t1.kotor.clientprocessing.model.Role;
import by.t1.kotor.clientprocessing.model.User;
import by.t1.kotor.clientprocessing.model.dto.client.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.security.JwtAuthenticationDto;
import by.t1.kotor.clientprocessing.model.dto.security.RefreshTokenDto;
import by.t1.kotor.clientprocessing.model.dto.security.UserCredentialDto;
import by.t1.kotor.clientprocessing.repository.UserRepository;
import by.t1.kotor.clientprocessing.security.jwt.JwtService;
import by.t1.kotor.clientprocessing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public JwtAuthenticationDto signIn(UserCredentialDto userCredentialDto) throws AuthenticationException {
        User user = findByCredentials(userCredentialDto);
        return jwtService.generateAuthToken(user.getEmail());
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception {
            String refreshToken = refreshTokenDto.getRefreshToken();
            if (refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
                User user = findByEmail(jwtService.getEmailFromToken(refreshToken));
                return jwtService.refreshBaseToken(user.getEmail(), refreshToken);
            }
            throw new AuthenticationException("Invalid refresh token");
    }

    @Override
    public User create(ClientRegistrationRequest request, Set<Role> roles) {
        User user = userMapper.toEntity(request, passwordEncoder);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    private User findByCredentials(UserCredentialDto userCredentialDto) throws AuthenticationException {
        Optional<User> optionalUser = userRepository.findByEmail(userCredentialDto.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(userCredentialDto.getPassword(), user.getPassword())) {
                return user;
            }
        }
        throw new AuthenticationException("Email or password is not correct");
    }

    private User findByEmail(String email) throws Exception {
        return userRepository.findByEmailWithRoles(email).orElseThrow(() -> new Exception(String.format("User with email %s not found", email)));
    }
}
