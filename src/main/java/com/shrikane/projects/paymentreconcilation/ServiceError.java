package com.shrikane.projects.paymentreconcilation;

public final class ServiceError extends RuntimeException {
    public final int status;
    public final String code;

    public ServiceError(int status, String code) {
        super(code);
        this.status = status;
        this.code = code;
    }
}
