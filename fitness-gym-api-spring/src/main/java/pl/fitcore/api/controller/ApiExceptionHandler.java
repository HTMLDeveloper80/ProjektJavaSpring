package pl.fitcore.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.fitcore.api.dto.ApiMessageResponse;

@RestControllerAdvice
public class ApiExceptionHandler {
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
