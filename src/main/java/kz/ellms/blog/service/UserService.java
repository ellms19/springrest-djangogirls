package kz.ellms.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import kz.ellms.blog.data.BaseUserRepository;

@Service
public class UserService implements UserDetailsService {

    private final BaseUserRepository userRepository;

    @Autowired
    public UserService(BaseUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .<UsernameNotFoundException>orElseThrow(() ->
                        { throw new UsernameNotFoundException(String.format("Username with email %s not found", username));} );
    }
}
