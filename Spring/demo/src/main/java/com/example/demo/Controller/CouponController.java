package com.example.demo.Controller;

import com.example.demo.Model.Coupon;
import com.example.demo.Model.CouponConsumptionHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.CouponService;

import java.util.List;
@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "http://localhost:4200") // Allow requests from Angular app

public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);
    }

    @PostMapping("/consume")
    public ResponseEntity<Coupon> consumeCoupon(@RequestParam String couponCode, @RequestParam String customerId, @RequestParam String orderId) {
        Coupon consumedCoupon = couponService.consumeCoupon(couponCode, customerId, orderId);
        return new ResponseEntity<>(consumedCoupon, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = couponService.getAllCoupons();
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<List<CouponConsumptionHistory>> getConsumptionHistory(@RequestParam String couponCode) {
        List<CouponConsumptionHistory> history = couponService.getConsumptionHistory(couponCode);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}
