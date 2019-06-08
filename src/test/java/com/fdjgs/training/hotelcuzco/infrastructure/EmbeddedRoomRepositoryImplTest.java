package com.fdjgs.training.hotelcuzco.infrastructure;

import com.fdjgs.training.hotelcuzco.domain.Reservation;
import com.fdjgs.training.hotelcuzco.domain.Room;
import com.fdjgs.training.hotelcuzco.domain.RoomType;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class EmbeddedRoomRepositoryImplTest {

	private EmbeddedRoomRepositoryImpl repository;

	@Before
	public void init() {
		Map<String, Room> roomsDatasource = new HashMap<>();
		roomsDatasource.put("101", Room.createRoom("101", 1, "", 2, RoomType.QUEEN));
		roomsDatasource.put("102", Room.createRoom("102", 1, "", 4, RoomType.KING));
		HotelCuzcoDatabase database = HotelCuzcoDatabase.of(roomsDatasource);
		repository = new EmbeddedRoomRepositoryImpl(database);
	}

	@Test
	public void save() {
	}

	@Test
	public void findAvailableRoomWithMinimumGuests() {
		List<Room> availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1)), 2);
		assertEquals(2, availableRooms.size());
	}

	@Test
	public void findAvailableRoomWithGuestsBetweenCapacities() {
		List<Room> availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1)), 3);
		assertEquals(1, availableRooms.size());
	}

	@Test
	public void findAvailableRoomWithMaximumGuests() {
		List<Room> availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1)), 4);
		assertEquals(1, availableRooms.size());
	}

	@Test
	public void findAvailableRoomWithTooManyGuests() {
		List<Room> availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1)), 5);
		assertEquals(0, availableRooms.size());
	}

	@Test
	public void findAvailableRoomWithReservationStartingBefore() {
		repository.findOne("101").book(Reservation.of(LocalDate.now().minusDays(2), LocalDate.now().plusDays(5)));
		List<Room> availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now(), LocalDate.now().plusDays(1)), 2);
		assertEquals(1, availableRooms.size());
		assertFalse(availableRooms.stream().map(Room::getRoomNumber).collect(Collectors.toList()).contains("101"));
	}

	@Test
	public void findAvailableRoomWithReservationStartingDuring() {
		repository.findOne("101").book(Reservation.of(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5)));
		List<Room> availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now(), LocalDate.now().plusDays(3)), 2);
		assertEquals(1, availableRooms.size());
		assertFalse(availableRooms.stream().map(Room::getRoomNumber).collect(Collectors.toList()).contains("101"));
	}

	@Test
	public void findAvailableRoomWithReservationOutsideRequiredPeriod() {
		repository.findOne("101").book(Reservation.of(LocalDate.now().plusDays(6), LocalDate.now().plusDays(7)));
		List<Room> availableRooms = repository.findAvailableRooms(Reservation.of(LocalDate.now(), LocalDate.now().plusDays(3)), 2);
		assertEquals(2, availableRooms.size());
		assertTrue(availableRooms.stream().map(Room::getRoomNumber).collect(Collectors.toList()).containsAll(Arrays.asList("101", "102")));
	}
}