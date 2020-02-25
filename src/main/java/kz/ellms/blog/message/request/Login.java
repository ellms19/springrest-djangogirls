package kz.ellms.blog.message.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
public class Login {

    @NotNull(message = "Email address is required")
    @Email
    private String email;

    @NotNull(message = "Password is required")
    @Size(min=8, message = "Minimum password length is 8")
    private String password;
}
