package by.t1.kotor.clientprocessing.security.jwt;

import by.t1.kotor.clientprocessing.security.CustomUserServiceImpl;
import by.t1.kotor.clientprocessing.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserServiceImpl customUserService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (token != null && jwtService.validateJwtToken(token)) {
            UserDetailsImpl userDetailsImpl = setUserDetailsImplToSecurityContextHolder(token);

            boolean isBlocked = userDetailsImpl.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_BLOCKED_CLIENT"));
            if (isBlocked) {
                log.warn("Blocked client attempted access: {}", userDetailsImpl.getUsername());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Your account is blocked");
                return;
            }
        }
        filterChain.doFilter(request, response);

    }

    private UserDetailsImpl setUserDetailsImplToSecurityContextHolder(String token) {
        String email = jwtService.getEmailFromToken(token);
        log.info("email from token {}", email);

        UserDetailsImpl userDetailsImpl = customUserService.loadUserByUsername(email);
        log.info("user details {}", userDetailsImpl);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl,
                null, userDetailsImpl.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return userDetailsImpl;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
