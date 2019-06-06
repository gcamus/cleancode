package com.fdjgs.training.hotelcuzco.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class Reservation {

	LocalDate checkin;

	LocalDate checkout;

	private Reservation() {}

	public static Reservation of(LocalDate checkin, LocalDate checkout) {

		validateReservationDate(checkin, checkout);

		Reservation reservation = new Reservation();
		reservation.checkin = checkin;
		reservation.checkout = checkout;
		return reservation;
	}

	private static void validateReservationDate(LocalDate checkin, LocalDate checkout) {
		if(ChronoUnit.DAYS.between(checkin, checkout) < 1 ) {
			throw new RuntimeException("invalid reservation dates, at least one night reservation must be booked");
		}
	}

	public boolean isNotOverlapping(Reservation requestReservation) {
		return requestReservation.checkin.isAfter(this.checkout)
				|| requestReservation.checkout.isBefore(this.checkin);
	}
}
