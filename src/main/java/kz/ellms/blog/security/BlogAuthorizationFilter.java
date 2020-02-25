package kz.ellms.blog.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import kz.ellms.blog.service.UserService;
import kz.ellms.blog.message.response.error.ErrorResponse;

public class BlogAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserService userService;

    BlogAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String token = request.getHeader(JWTGenUtil.HEADER);

        if (token != null && token.startsWith(JWTGenUtil.BEARER)) {
            try {
                String email = Jwts.parser()
                        .setSigningKey(JWTGenUtil.SECRET)
                        .parseClaimsJws(token.replace(JWTGenUtil.BEARER, ""))
                        .getBody()
                        .getSubject();
                UserDetails user = userService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);

            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException exception) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write(
                        buildErrorResponse(exception.getMessage()));
            }
        }
        else{
            filterChain.doFilter(request, response);
        }
    }

    private String buildErrorResponse(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(
                new ErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), message)
        );
    }
}
