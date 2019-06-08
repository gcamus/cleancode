package com.fdjgs.training.hotelcuzco.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class PlannedMaintenance extends Unavailability {

	private PlannedMaintenance() {
		super();
	}

	public static PlannedMaintenance of(LocalDate checkin, LocalDate checkout) {
		PlannedMaintenance reservation = new PlannedMaintenance();
		reservation.beginDate = checkin;
		reservation.endDate = checkout;
		return reservation;
	}
}
