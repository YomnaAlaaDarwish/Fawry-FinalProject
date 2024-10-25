package com.example.demo.service;

import com.example.demo.Exception.CouponExpiredException;
import com.example.demo.Exception.CouponHistoryNotFoundException;
import com.example.demo.Exception.CouponNotFoundException;
import com.example.demo.Exception.CouponUsageException;
import com.example.demo.Model.Coupon;
import com.example.demo.Model.CouponConsumptionHistory;
import com.example.demo.Repo.CouponConsumptionHistoryRepository;
import com.example.demo.Repo.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponConsumptionHistoryRepository consumptionHistoryRepository;

    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public Coupon consumeCoupon(String couponCode, String customerId, String orderId) {
        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CouponExpiredException("Coupon is expired");
        }

        if (coupon.getNumberOfUsages() <= 0) {
            throw new CouponUsageException("The coupon has used all allowed times");
        }

        coupon.setNumberOfUsages(coupon.getNumberOfUsages() - 1);
        couponRepository.save(coupon);

        CouponConsumptionHistory consumptionHistory = new CouponConsumptionHistory();
        consumptionHistory.setCouponCode(couponCode);
        consumptionHistory.setCustomerId(customerId);
        consumptionHistory.setOrderId(orderId);
        consumptionHistory.setConsumedAt(LocalDateTime.now());
        consumptionHistoryRepository.save(consumptionHistory);

        return coupon;
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public List<CouponConsumptionHistory> getConsumptionHistory(String couponCode) {
        List<CouponConsumptionHistory> history = consumptionHistoryRepository.findByCouponCode(couponCode);
        if (history.isEmpty()) {
            throw new CouponHistoryNotFoundException("No consumption history found for coupon code: " + couponCode);
        }
        return history;
    }
}
