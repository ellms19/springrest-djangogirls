package kz.ellms.blog.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kz.ellms.blog.domain.User;
import kz.ellms.blog.message.request.Login;

public class BlogAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    BlogAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/accounts/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Login userCredentials = new ObjectMapper().readValue(request.getInputStream(), Login.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userCredentials.getEmail(), userCredentials.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = ((User)authResult.getPrincipal()).getEmail();
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, JWTGenUtil.SECRET)
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis()+JWTGenUtil.EXPIRATION_TIME))
                .compact();
        response.addHeader(JWTGenUtil.HEADER, JWTGenUtil.BEARER+token);
    }
}
