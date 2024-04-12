package com.sila.exception;

import com.sila.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleException {

  @ExceptionHandler(value = NotFoundException.class)
  public ResponseEntity<MessageResponse> handleNotFound(NotFoundException notFoundException) {
    MessageResponse messageResponse = new MessageResponse();
    messageResponse.setStatus(HttpStatus.NOT_FOUND.value());
    messageResponse.setMessage(notFoundException.getMessage());
    return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = BadRequestException.class)
  public ResponseEntity<MessageResponse> handleBadRequest(BadRequestException badRequestException) {
    MessageResponse messageResponse = new MessageResponse();
    messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    messageResponse.setMessage(badRequestException.getMessage());
    return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleErrorField(
      MethodArgumentNotValidException exception) {
    MessageResponse messageResponse = new MessageResponse();
    messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    StringBuilder str = new StringBuilder();
    var fieldErrors = exception.getBindingResult().getFieldErrors();
    /**
     for (var fieldError : fieldErrors) {
     str.append("{").append(fieldError.getField()).append(": ")
     .append(fieldError.getDefaultMessage()).append(" },");
     }
     **/

    str.append(fieldErrors.get(0).getField()).append(" : ").append(fieldErrors.get(0).getDefaultMessage());
    messageResponse.setMessage(str.toString());
    return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
  }
}
