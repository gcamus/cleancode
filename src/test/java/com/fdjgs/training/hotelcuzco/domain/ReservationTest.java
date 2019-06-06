package com.fdjgs.training.hotelcuzco.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.time.LocalDate;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by Lotsys on 05/06/2019.
 */
@RunWith(JUnit4.class)
public class ReservationTest {

	@Test
	public void createValidReservation() {
		Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1));
	}

	@Test(expected = RuntimeException.class)
	public void invalidReservation() {
		Reservation.of(LocalDate.now(), LocalDate.now());
	}

	@Test
	public void overlappingReservations() {
		Reservation requestResa = Reservation.of(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
		Reservation res1 = Reservation.of(LocalDate.now(), LocalDate.now().plusDays(5));
		Reservation res2 = Reservation.of(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));
		Reservation res3 = Reservation.of(LocalDate.now().plusDays(4), LocalDate.now().plusDays(5));
		Reservation res4 = Reservation.of(LocalDate.now().minusDays(3), LocalDate.now().minusDays(2));

		assertFalse(res1.isNotOverlapping(requestResa));
		assertFalse(res2.isNotOverlapping(requestResa));
		assertTrue(res3.isNotOverlapping(requestResa));
		assertTrue(res4.isNotOverlapping(requestResa));
	}
}
