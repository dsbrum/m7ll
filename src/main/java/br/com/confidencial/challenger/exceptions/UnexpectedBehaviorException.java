package br.com.confidencial.challenger.exceptions;

public class UnexpectedBehaviorException extends RuntimeException{
    public UnexpectedBehaviorException(String mensagem) {
        super(mensagem);
    }
}
