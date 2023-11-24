package br.com.confidencial.challenger.exceptions;

public class CsvProcessingException extends RuntimeException{
    public CsvProcessingException(String message) {
        super(message);
    }
}
