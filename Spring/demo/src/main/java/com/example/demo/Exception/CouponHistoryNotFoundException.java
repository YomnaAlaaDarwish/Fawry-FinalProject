package com.example.demo.Exception;

public class CouponHistoryNotFoundException extends RuntimeException {
    public CouponHistoryNotFoundException(String message) {
        super(message);
    }
}