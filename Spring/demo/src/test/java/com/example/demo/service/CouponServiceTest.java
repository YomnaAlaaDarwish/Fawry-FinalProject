package com.example.demo.service;

import com.example.demo.Exception.CouponHistoryNotFoundException;
import org.junit.jupiter.api.Test;
/*
@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
*/

import com.example.demo.Exception.CouponExpiredException;
import com.example.demo.Exception.CouponNotFoundException;
import com.example.demo.Exception.CouponUsageException;
import com.example.demo.Model.Coupon;
import com.example.demo.Model.CouponConsumptionHistory;
import com.example.demo.Repo.CouponConsumptionHistoryRepository;
import com.example.demo.Repo.CouponRepository;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CouponServiceTest {

	@Mock
	private CouponRepository couponRepository;

	@Mock
	private CouponConsumptionHistoryRepository consumptionHistoryRepository;

	@InjectMocks
	private CouponService couponService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// Test for successful coupon creation
	@Test
	public void givenValidCoupon_whenCreateCoupon_thenReturnSavedCoupon() {
		Coupon coupon = new Coupon();
		coupon.setCode("SAVE20");
		when(couponRepository.save(coupon)).thenReturn(coupon);

		Coupon result = couponService.createCoupon(coupon);

		Assertions.assertEquals(coupon, result);
		verify(couponRepository, times(1)).save(coupon);
	}

	// Test for successful coupon consumption
	@Test
	public void givenValidCouponCodeAndUsages_whenConsumeCoupon_thenReturnUpdatedCoupon() {
		Coupon coupon = new Coupon();
		coupon.setCode("DISCOUNT10");
		coupon.setExpiryDate(LocalDate.now().plusDays(1));
		coupon.setNumberOfUsages(5);

		when(couponRepository.findByCode("DISCOUNT10")).thenReturn(Optional.of(coupon));
		when(couponRepository.save(coupon)).thenReturn(coupon);

		Coupon result = couponService.consumeCoupon("DISCOUNT10", "customer123", "order456");

		Assertions.assertEquals(4, result.getNumberOfUsages());
		verify(consumptionHistoryRepository, times(1)).save(any(CouponConsumptionHistory.class));
	}

	// Test for coupon not found exception
	@Test
	public void givenInvalidCouponCode_whenConsumeCoupon_thenThrowCouponNotFoundException() {
		when(couponRepository.findByCode("INVALID")).thenReturn(Optional.empty());

		assertThrows(CouponNotFoundException.class, () ->
				couponService.consumeCoupon("INVALID", "customer123", "order456"));
	}

	// Test for expired coupon exception
	@Test
	public void givenExpiredCoupon_whenConsumeCoupon_thenThrowCouponExpiredException() {
		Coupon coupon = new Coupon();
		coupon.setCode("EXPIRED");
		coupon.setExpiryDate(LocalDate.now().minusDays(1));
		coupon.setNumberOfUsages(1);

		when(couponRepository.findByCode("EXPIRED")).thenReturn(Optional.of(coupon));

		assertThrows(CouponExpiredException.class, () ->
				couponService.consumeCoupon("EXPIRED", "customer123", "order456"));
	}

	// Test for coupon usage exception (no usages left)
	@Test
	public void givenCouponWithNoUsages_whenConsumeCoupon_thenThrowCouponUsageException() {
		Coupon coupon = new Coupon();
		coupon.setCode("USED_UP");
		coupon.setExpiryDate(LocalDate.now().plusDays(1));
		coupon.setNumberOfUsages(0);

		when(couponRepository.findByCode("USED_UP")).thenReturn(Optional.of(coupon));

		assertThrows(CouponUsageException.class, () ->
				couponService.consumeCoupon("USED_UP", "customer123", "order456"));
	}

	// Test for retrieving all coupons
	@Test
	public void whenGetAllCoupons_thenReturnListOfCoupons() {
		List<Coupon> coupons = List.of(new Coupon(), new Coupon());

		when(couponRepository.findAll()).thenReturn(coupons);

		List<Coupon> result = couponService.getAllCoupons();

		Assertions.assertEquals(coupons, result);
		verify(couponRepository, times(1)).findAll();
	}

	// Test for retrieving consumption history by coupon code
	@Test
	public void givenCouponCode_whenGetConsumptionHistory_thenReturnHistoryList() {
		List<CouponConsumptionHistory> history = List.of(new CouponConsumptionHistory(), new CouponConsumptionHistory());

		when(consumptionHistoryRepository.findByCouponCode("SAVE20")).thenReturn(history);

		List<CouponConsumptionHistory> result = couponService.getConsumptionHistory("SAVE20");

		Assertions.assertEquals(history, result);
		verify(consumptionHistoryRepository, times(1)).findByCouponCode("SAVE20");
	}

	// Test for retrieving consumption history with an invalid coupon code
	@Test
	public void givenInvalidCouponCode_whenGetConsumptionHistory_thenThrowCouponHistoryNotFoundException() {
		// Arrange
		String invalidCouponCode = "INVALID";
		when(consumptionHistoryRepository.findByCouponCode(invalidCouponCode))
				.thenReturn(Collections.emptyList());

		// Act & Assert
		CouponHistoryNotFoundException thrownException = assertThrows(
				CouponHistoryNotFoundException.class,
				() -> couponService.getConsumptionHistory(invalidCouponCode)
		);

		// Verify that the exception message is as expected
		Assertions.assertEquals("No consumption history found for coupon code: INVALID", thrownException.getMessage());
	}

}


