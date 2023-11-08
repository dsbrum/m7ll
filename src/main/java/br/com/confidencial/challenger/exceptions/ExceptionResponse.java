package br.com.confidencial.challenger.exceptions;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private int status;
    private String mensagem;

    public ExceptionResponse(int status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
    }

}
