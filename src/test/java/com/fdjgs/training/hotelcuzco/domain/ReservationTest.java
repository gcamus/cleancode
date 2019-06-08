package com.fdjgs.training.hotelcuzco.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by Lotsys on 05/06/2019.
 */
@RunWith(JUnit4.class)
public class ReservationTest {

	private Reservation requestedReservation;

	@Before
	public void init() {
		requestedReservation = Reservation.of(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
	}

	@Test
	public void createValidReservation() {
		Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1));
	}

	@Test(expected = InvalidReservationDatesException.class)
	public void invalidReservation() {
		Reservation.of(LocalDate.now(), LocalDate.now());
	}

	@Test
	public void roomIsBookedForAllTheReservationPeriod() {
		// GIVEN : an existing reservation starting before the requested dates and ending after
		Reservation existingReservation = Reservation.of(LocalDate.now(), LocalDate.now().plusDays(5));

		assertFalse(existingReservation.isNotOverlapping(requestedReservation));
	}

	@Test
	public void roomHasAReservationStartingDuringTheRequestedDates() {
		// GIVEN : an existing reservation starting during the requested dates and ending after
		Reservation existingReservation = Reservation.of(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));

		assertFalse(existingReservation.isNotOverlapping(requestedReservation));
	}

	@Test
	public void roomHasAReservationEndingDuringTheRequestedDates() {
		// GIVEN : an existing reservation ending during the requested dates and ending after
		Reservation existingReservation = Reservation.of(LocalDate.now(), LocalDate.now().plusDays(2));

		assertFalse(existingReservation.isNotOverlapping(requestedReservation));
	}

	@Test
	public void roomIsBookedAfterTheRequestedReservation() {
		// GIVEN : an existing reservation starting after the requested dates
		Reservation existingReservation = Reservation.of(LocalDate.now().plusDays(4), LocalDate.now().plusDays(5));

		assertTrue(existingReservation.isNotOverlapping(requestedReservation));
	}

	@Test
	public void roomIsBookedBeforeTheRequestedReservation() {
		// GIVEN : an existing reservation ending before the requested dates
		Reservation existingReservation = Reservation.of(LocalDate.now().minusDays(3), LocalDate.now().minusDays(2));

		assertTrue(existingReservation.isNotOverlapping(requestedReservation));
	}
}
