package kz.ellms.blog.message.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SuccessResponse<T> {
    private final int status;
    private final String message;
    private final T body;
}