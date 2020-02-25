package kz.ellms.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    @JsonIgnore
    private Post post;

    @NotBlank(message = "Author name is required")
    @Size(max = 200, message = "Author name can not exceed 100 characters")
    private String author;

    @NotBlank(message = "Comment text is required")
    @Size(min = 1, message = "Comment should have at least 1 character")
    private String text;

    private LocalDateTime createdDate;

    private boolean approvedComment;

    @PrePersist
    private void defaultValues() {
        createdDate = LocalDateTime.now();
        approvedComment = false;
    }
}
