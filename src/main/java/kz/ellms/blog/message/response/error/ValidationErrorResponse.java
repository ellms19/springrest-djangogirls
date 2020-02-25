package kz.ellms.blog.message.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public final class ValidationErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final List<String> messages;
}