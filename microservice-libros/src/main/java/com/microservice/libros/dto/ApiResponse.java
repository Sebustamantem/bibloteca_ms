package com.microservice.libros.dto;

public class ApiResponse<T> {

    private boolean success;
    private int status;
    private String message;
    private T data;
    private Long size;

    public ApiResponse(boolean success, int status, String message, T data, Long size) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
        this.size = size;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    public Long getSize() {
        return size;
    }
    public void setSize(Long size) {
        this.size = size;
    }
}
