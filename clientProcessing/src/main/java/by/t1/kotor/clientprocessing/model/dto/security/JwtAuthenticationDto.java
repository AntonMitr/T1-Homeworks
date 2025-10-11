package by.t1.kotor.clientprocessing.model.dto.security;

import lombok.Data;

@Data
public class JwtAuthenticationDto {
    private String token;
    private String refreshToken;
}