package com.hackathon.KCThack.config;

import com.hackathon.KCThack.service.UserDetailsImpl;
import com.hackathon.KCThack.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
public class TokenFilter extends OncePerRequestFilter {


    private static final Logger log = LoggerFactory.getLogger(TokenFilter.class);
    @Autowired
    private JwtCore jwtCore;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = null;
        String userId = null;

        try {
            String headerAuth = request.getHeader("Authorization");
            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                jwt = headerAuth.substring(7);

            }

            if (jwt != null) {
                    userId = jwtCore.getIdFromJwt(jwt);
                    log.debug("Authenticating user with id: {}", userId);
                    Claims claims = Jwts.parser()
                            .verifyWith(jwtCore.getSigningKey())
                            .build()
                            .parseSignedClaims(jwt)
                            .getPayload();
                    Integer tokenVersion = claims.get("tv", Integer.class);


                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userService.loadUserById(userId);

                    if (userDetails instanceof UserDetailsImpl) {
                        int currentVersion = ((UserDetailsImpl) userDetails).getTokenVersion();
                        if (tokenVersion != currentVersion) {
                            throw new AccessDeniedException("Token version mismatch - invalidated");
                        }
                    }

                    if (!userDetails.isEnabled() || !userDetails.isAccountNonLocked()) {
                        throw new AccessDeniedException("User is not active or blocked");
                    }


                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);



                }
            }
        } catch (Exception e) {
            System.out.println("Cannot set user authentication: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}