package br.com.confidencial.challenger.controller.dto;

public class ResponseResult<T> {
    private T data;
    private String errorMessage;

    public ResponseResult(T data) {
        this.data = data;
    }

    public ResponseResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
