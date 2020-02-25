package kz.ellms.blog.api;

import kz.ellms.blog.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import kz.ellms.blog.message.response.exceptionhandler.NotFoundException;
import kz.ellms.blog.domain.Post;
import kz.ellms.blog.message.response.success.SuccessResponse;
import kz.ellms.blog.data.BasePostRepository;

@RestController
@CrossOrigin(origins = "*")
public class PostApi {

    private final BasePostRepository postRepository;

    @Autowired
    public PostApi(BasePostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<SuccessResponse<Iterable<Post>>> getPosts() {
        SuccessResponse<Iterable<Post>> response = new SuccessResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                postRepository.findByPublishedDateIsNotNullOrderByPublishedDateAsc()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping(value="post/{postId:\\d+}", produces = "application/json")
    public ResponseEntity<SuccessResponse<Post>> getPost(@PathVariable("postId") long id) {
        Post post = postRepository.findByIdAndPublishedDateIsNotNull(id)
                .<RuntimeException>orElseThrow(() -> {throw new NotFoundException(String.format("Post with id %d not found", id));});
        SuccessResponse<Post> response = new SuccessResponse<>(
            HttpStatus.OK.value(),
            HttpStatus.OK.getReasonPhrase(),
            post
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(value="post/new", consumes="application/json")
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post, Principal principal) {
        UserDetails user= (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setAuthor((User)user);
        return ResponseEntity.status(HttpStatus.CREATED).body(postRepository.save(post));
    }

    @PutMapping(value = "post/{postId:\\d+}/publish", produces = "application/json")
    public ResponseEntity<SuccessResponse<Post>> publishPost(@PathVariable("postId") long id) {
        Post post = postRepository.findByIdAndPublishedDateIsNull(id)
                .<RuntimeException>orElseThrow(() -> {throw new NotFoundException(String.format("Unpublished post with id %d not found", id));});
        post.setPublishedDate(LocalDateTime.now());
        SuccessResponse<Post> response = new SuccessResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                postRepository.save(post)
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "post/{postId:\\d+}/remove", produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePost(@PathVariable("postId") long id) {
        Post post = postRepository.findById(id)
                .<RuntimeException>orElseThrow(() -> {throw new NotFoundException(String.format("Post with id %d not found", id));});
        postRepository.delete(post);
    }

    @GetMapping(value = "/drafts", produces = "application/json")
    public ResponseEntity<SuccessResponse<Iterable<Post>>> getDrafts() {
        SuccessResponse<Iterable<Post>> response = new SuccessResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                postRepository.findByPublishedDateIsNull()
        );
        return ResponseEntity.ok(response);
    }
}
