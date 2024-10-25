package com.example.demo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<String> handleCouponNotFoundException(CouponNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found.");
    }

    @ExceptionHandler(CouponExpiredException.class)
    public ResponseEntity<String> handleCouponExpiredException(CouponExpiredException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body("Coupon is expired.");
    }

    @ExceptionHandler(CouponUsageException.class)
    public ResponseEntity<String> handleCouponUsageException(CouponUsageException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The coupon has been used all allowed times.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CouponHistoryNotFoundException.class)
    public ResponseEntity<String> handleCouponHistoryNotFoundException(CouponHistoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
