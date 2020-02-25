package kz.ellms.blog.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import kz.ellms.blog.domain.Post;

@Repository
public interface BasePostRepository extends CrudRepository<Post, Long> {

    Iterable<Post> findByPublishedDateIsNotNullOrderByPublishedDateAsc();

    Optional<Post> findByIdAndPublishedDateIsNotNull(long id);

    Iterable<Post> findAllByOrderByCreatedDateDesc();

    Iterable<Post> findByPublishedDateIsNull();

    Optional<Post> findByIdAndPublishedDateIsNull(long id);


}
