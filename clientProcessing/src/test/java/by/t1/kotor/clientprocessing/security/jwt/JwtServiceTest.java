package by.t1.kotor.clientprocessing.security.jwt;

import by.t1.kotor.clientprocessing.model.dto.security.JwtAuthenticationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();

        String secret = "12345678901234567890123456789012";
        ReflectionTestUtils.setField(jwtService, "jwtSecret",
                Base64.getEncoder().encodeToString(secret.getBytes()));
        ReflectionTestUtils.setField(jwtService, "jwtTokenExpirationMs", 3600000);
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpirationMs", 7200000);
    }

    @Test
    void generateAuthToken_shouldReturnJwtAndRefreshToken() {
        String email = "user@test.com";

        JwtAuthenticationDto jwtDto = jwtService.generateAuthToken(email);

        assertNotNull(jwtDto.getToken(), "JWT token should not be null");
        assertNotNull(jwtDto.getRefreshToken(), "Refresh token should not be null");
    }

    @Test
    void refreshBaseToken_shouldUpdateJwtButKeepRefreshToken() {
        String email = "user@test.com";
        String oldRefresh = "refreshToken123";

        JwtAuthenticationDto jwtDto = jwtService.refreshBaseToken(email, oldRefresh);

        assertNotNull(jwtDto.getToken(), "New JWT token should not be null");
        assertEquals(oldRefresh, jwtDto.getRefreshToken(), "Refresh token should remain unchanged");
    }

    @Test
    void getEmailFromToken_shouldReturnEmail() {
        String email = "user@test.com";
        String token = jwtService.generateAuthToken(email).getToken();

        String extractedEmail = jwtService.getEmailFromToken(token);

        assertEquals(email, extractedEmail);
    }

    @Test
    void validateJwtToken_shouldReturnTrueForValidToken() {
        String email = "user@test.com";
        String token = jwtService.generateAuthToken(email).getToken();

        boolean valid = jwtService.validateJwtToken(token);

        assertTrue(valid);
    }

    @Test
    void validateJwtToken_shouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.token.value";

        boolean valid = jwtService.validateJwtToken(invalidToken);

        assertFalse(valid);
    }
}
