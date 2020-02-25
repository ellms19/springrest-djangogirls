package kz.ellms.blog.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User author;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Tittle can not exceed 200 characters")
    private String title;

    @NotNull(message = "Post text is required")
    @Size(min = 10, message = "Post text should be at least 10 characters long")
    private String text;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments;

    private LocalDateTime createdDate;

    private LocalDateTime publishedDate;

    @PrePersist
    private void createdAt() {
        createdDate = LocalDateTime.now();
    }
}
