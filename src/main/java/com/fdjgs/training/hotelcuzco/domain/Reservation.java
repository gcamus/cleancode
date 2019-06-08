package com.fdjgs.training.hotelcuzco.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class Reservation extends Unavailability {

	private Reservation() {
		super();
	}

	public static Reservation of(LocalDate checkin, LocalDate checkout) {
		validateReservationDate(checkin, checkout);

		Reservation reservation = new Reservation();
		reservation.beginDate = checkin;
		reservation.endDate = checkout;
		return reservation;
	}

	private static void validateReservationDate(LocalDate checkin, LocalDate checkout) {
		if(ChronoUnit.DAYS.between(checkin, checkout) < 1 ) {
			throw new InvalidReservationDatesException("invalid reservation dates, at least one night reservation must be booked");
		}
	}
}
