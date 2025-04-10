package com.sila.exception;


import com.sila.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleException {

    // Handle FileValidationException
    @ExceptionHandler(value = FileValidationException.class)
    public ResponseEntity<MessageResponse> handleFileValidationException(FileValidationException ex) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        messageResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle AccessDeniedException
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<MessageResponse> handAccessDenied(AccessDeniedException accessDeniedException) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(HttpStatus.FORBIDDEN.value());
        messageResponse.setMessage(accessDeniedException.getMessage());
        return new ResponseEntity<>(messageResponse, HttpStatus.FORBIDDEN);
    }

    // Handle ServerErrorException
    @ExceptionHandler(value = ServerErrorException.class)
    public ResponseEntity<MessageResponse> handleNotFound(ServerErrorException serverErrorException) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        messageResponse.setMessage(serverErrorException.getMessage());
        return new ResponseEntity<>(messageResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle NotFoundException
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<MessageResponse> handleNotFound(NotFoundException notFoundException) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(HttpStatus.NOT_FOUND.value());
        messageResponse.setMessage(notFoundException.getMessage());
        return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
    }

    // Handle BadRequestException
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<MessageResponse> handleBadRequest(BadRequestException badRequestException) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        messageResponse.setMessage(badRequestException.getMessage());
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle MethodArgumentNotValidException
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationException(MethodArgumentNotValidException exception) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

        // Build a custom message for the first field error
        StringBuilder str = new StringBuilder();
        var fieldErrors = exception.getBindingResult().getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            str.append(fieldErrors.get(0).getField()).append(": ")
                    .append(fieldErrors.get(0).getDefaultMessage());
        } else {
            str.append("Validation failed.");
        }
        messageResponse.setMessage(str.toString());

        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }
}
