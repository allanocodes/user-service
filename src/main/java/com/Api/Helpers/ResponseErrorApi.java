package com.Api.Helpers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ResponseErrorApi<T>{
    private String status;
    private String message;
    private  T errors;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public T getErrors() {
        return this.errors;
    }

    public void setErrors(final T errors) {
        this.errors = errors;
    }
}
