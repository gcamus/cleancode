package com.fdjgs.training.hotelcuzco.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.time.MonthDay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Lotsys on 05/06/2019.
 */
@RunWith(JUnit4.class)
public class RoomTest {

	Room room;

	@Before
	public void setup() {
		room = Room.createRoom("101", 2, "", 4, RoomType.QUEEN);
	}

	@Test
	public void bookAvailableRoom() {
		// GIVEN
		Reservation requestedReservation = Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1));

		// WHEN
		room.book(requestedReservation);

		//THEN
		assertTrue(room.getReservations().contains(requestedReservation));
	}

	@Test(expected = RoomUnavailableException.class)
	public void bookUnavailableRoom() {
		// GIVEN a room with a reservation
		Reservation requestedReservation = Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1));
		room.book(requestedReservation);

		// WHEN
		room.book(requestedReservation);
	}

	@Test(expected = RoomUnavailableException.class)
	public void bookInMaintenanceRoom() {
		// GIVEN a room with a planned maintenance
		PlannedMaintenance plannedMaintenance = PlannedMaintenance.of(LocalDate.now(), LocalDate.now().plusDays(1));
		room.planMaintenance(plannedMaintenance);
		Reservation requestedReservation = Reservation.of(LocalDate.now(), LocalDate.now().plusDays(2));

		// WHEN
		room.book(requestedReservation);
	}
}
