package com.fdjgs.training.hotelcuzco.infrastructure;

import com.fdjgs.training.hotelcuzco.domain.Reservation;
import com.fdjgs.training.hotelcuzco.domain.Room;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class EmbeddedRoomRepositoryImplTest {

	private EmbeddedRoomRepositoryImpl repository;

	@Test
	public void save() {
	}

	@Test
	public void testRoomAvailabilityByCapacity() {
		Map<String, Room> roomsDatabase = new HashMap<>();
		Room room1 = Room.createRoom("101", 1, "", 2);
		Room room2 = Room.createRoom("102", 1, "", 4);
		roomsDatabase.put(room1.getRoomNumber(), room1);
		roomsDatabase.put(room2.getRoomNumber(), room2);

		EmbeddedRoomRepositoryImpl repository = new EmbeddedRoomRepositoryImpl(roomsDatabase);

		List<Room> availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1)), 2);
		assertEquals(2, availableRooms.size());
		availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1)), 3);
		assertEquals(1, availableRooms.size());
		availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1)), 4);
		assertEquals(1, availableRooms.size());
		availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1)), 5);
		assertEquals(0, availableRooms.size());
	}

	@Test
	public void testRoomAvailabilityByReservationDates() {
		Map<String, Room> roomsDatabase = new HashMap<>();
		Room room1 = Room.createRoom("101", 1, "", 5);
		room1.book(Reservation.of(LocalDate.now(), LocalDate.now().plusDays(5)));
		roomsDatabase.put(room1.getRoomNumber(), room1);

		EmbeddedRoomRepositoryImpl repository = new EmbeddedRoomRepositoryImpl(roomsDatabase);

		List<Room> availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now().minusDays(2), LocalDate.now().plusDays(1)), 2);
		assertEquals(0, availableRooms.size());

		availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now().plusDays(2), LocalDate.now().plusDays(3)), 2);
		assertEquals(0, availableRooms.size());

		availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now().plusDays(6), LocalDate.now().plusDays(7)), 2);
		assertEquals(1, availableRooms.size());

	}
}