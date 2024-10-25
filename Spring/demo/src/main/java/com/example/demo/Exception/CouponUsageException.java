package com.example.demo.Exception;
public class CouponUsageException extends RuntimeException {
    public CouponUsageException(String message) {
        super(message);
    }
}