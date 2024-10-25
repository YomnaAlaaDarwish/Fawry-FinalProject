package com.example.demo.Repo;

import com.example.demo.Model.CouponConsumptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponConsumptionHistoryRepository extends JpaRepository<CouponConsumptionHistory, Long> {
    List<CouponConsumptionHistory> findByCouponCode(String couponCode);
}
