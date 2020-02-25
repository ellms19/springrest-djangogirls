package kz.ellms.blog.data;

import kz.ellms.blog.domain.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseCommentRepository extends CrudRepository<Comment, Long> {

}
