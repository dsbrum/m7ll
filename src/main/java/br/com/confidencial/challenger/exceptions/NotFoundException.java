package br.com.confidencial.challenger.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String mensagem) {
        super(mensagem);
    }
}
