package kz.ellms.blog.api;

import kz.ellms.blog.data.BaseCommentRepository;
import kz.ellms.blog.data.BasePostRepository;
import kz.ellms.blog.domain.Post;
import kz.ellms.blog.message.response.exceptionhandler.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kz.ellms.blog.domain.Comment;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class CommentApi {

    private final BaseCommentRepository commentRepository;
    private final BasePostRepository postRepository;

    @Autowired
    public CommentApi(BaseCommentRepository commentRepository,
                      BasePostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @PostMapping(value = "post/{postId:\\d+}/comment")
    public ResponseEntity<Comment> commentPost(@RequestBody @Valid Comment comment, @PathVariable("postId") long postId) {
        Post post = postRepository.findByIdAndPublishedDateIsNotNull(postId).
                <RuntimeException>orElseThrow(() -> {throw new NotFoundException(String.format("Post with id %d not found", postId));});
        comment.setPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentRepository.save(comment));
    }

    @PostMapping(value = "comment/{commentId:\\d+}/approve")
    @ResponseStatus(HttpStatus.OK)
    public void approveComment(@PathVariable("commentId") long id) {
        Comment comment = commentRepository.findById(id).
                <RuntimeException>orElseThrow(() -> {throw new NotFoundException(String.format("Comment with id %d not found", id));});
        comment.setApprovedComment(true);
        commentRepository.save(comment);
    }

    @DeleteMapping(value = "comment/{commentId:\\d+}/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeComment(@PathVariable("commentId") long id) {
        Comment comment = commentRepository.findById(id).
                <RuntimeException>orElseThrow(() -> {throw new NotFoundException(String.format("Comment with id %d not found", id));});
        commentRepository.delete(comment);
    }

}
