package pl.fitcore.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.fitcore.api.dto.ApiMessageResponse;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiMessageResponse forbidden(AccessDeniedException exception) {
        return new ApiMessageResponse(false, exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiMessageResponse unauthorized(BadCredentialsException exception) {
        return new ApiMessageResponse(false, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiMessageResponse badRequest(IllegalArgumentException exception) {
        return new ApiMessageResponse(false, exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiMessageResponse conflict(IllegalStateException exception) {
        return new ApiMessageResponse(false, exception.getMessage());
    }
}
