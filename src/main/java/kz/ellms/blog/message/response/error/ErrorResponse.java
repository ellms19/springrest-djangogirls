package kz.ellms.blog.message.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
}