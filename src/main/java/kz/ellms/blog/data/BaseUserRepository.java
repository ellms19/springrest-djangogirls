package kz.ellms.blog.data;

import org.springframework.data.repository.CrudRepository;
import kz.ellms.blog.domain.User;

import java.util.Optional;

public interface BaseUserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
