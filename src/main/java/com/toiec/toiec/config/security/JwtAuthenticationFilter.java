package com.toiec.toiec.config.security;

import com.toiec.toiec.exception.token.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private  final JwtUtilities jwtUtilities ;
    private final CustomerUserDetailsService customerUserDetailsService ;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = jwtUtilities.getToken(request);
            if (token != null && jwtUtilities.validateToken(token)) {
                String username = jwtUtilities.extractUsername(token);
                UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                    log.info("authenticated user with email :{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }
            filterChain.doFilter(request, response);
        }catch (InvalidTokenException e){
            // Thiết lập mã trạng thái và tiêu đề phản hồi
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            String timestamp = now.format(formatter);
            // Tạo đối tượng JSON phản hồi với message lấy từ e.getMessage()
            String jsonResponse = String.format("{\"status\":401,\"message\":\"UnauthorizedException\",\"error\":\"%s\",\"code\":\"%s\",\"timestamp\":\"%s\"}", e.getMessage(),e.getCode(),timestamp);

            // Ghi đối tượng JSON vào phản hồi
            response.getWriter().write(jsonResponse);
        }
    }

}