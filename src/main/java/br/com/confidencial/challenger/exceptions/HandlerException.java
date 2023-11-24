package br.com.confidencial.challenger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex) {
        ExceptionResponse erroResposta = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(erroResposta, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UnexpectedBehaviorException.class)
    public ResponseEntity<ExceptionResponse> handleUnexpectedBehaviorException(NotFoundException ex) {
        ExceptionResponse erroResposta = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(erroResposta, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CsvProcessingException.class)
    public ResponseEntity<ExceptionResponse> handleCsvProcessingException(NotFoundException ex) {
        ExceptionResponse erroResposta = new ExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
        return new ResponseEntity<>(erroResposta, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
