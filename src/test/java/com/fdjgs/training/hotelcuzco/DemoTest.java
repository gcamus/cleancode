package com.fdjgs.training.hotelcuzco;

import com.fdjgs.training.hotelcuzco.domain.BookingRequest;
import com.fdjgs.training.hotelcuzco.domain.HostelClosedException;
import com.fdjgs.training.hotelcuzco.domain.InvalidReservationDatesException;
import com.fdjgs.training.hotelcuzco.domain.PlannedMaintenance;
import com.fdjgs.training.hotelcuzco.domain.Reservation;
import com.fdjgs.training.hotelcuzco.domain.Room;
import com.fdjgs.training.hotelcuzco.domain.RoomUnavailableException;
import com.fdjgs.training.hotelcuzco.domain.Season;
import com.fdjgs.training.hotelcuzco.infrastructure.EmbeddedRoomRepositoryImpl;
import com.fdjgs.training.hotelcuzco.infrastructure.HotelCuzcoDatabase;
import com.fdjgs.training.hotelcuzco.infrastructure.RoomController;
import com.fdjgs.training.hotelcuzco.repository.RoomDoesNotExistException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Lotsys on 06/06/2019.
 */
@RunWith(JUnit4.class)
public class DemoTest {

	private RoomController controller;

	@Before
	public void init() {
		HotelCuzcoDatabase database = HotelCuzcoDatabase.defaultDatabase();
		controller = new RoomController(new EmbeddedRoomRepositoryImpl(database));
	}

	@Test(expected = InvalidReservationDatesException.class)
	public void testValidReservation() {
		controller.findAvailableRooms(LocalDate.now(), LocalDate.now(), 2);
	}

	@Test
	public void bookARoomNominalCase_TheRoomIsBooked() {
		// GIVEN : available rooms for a specific period
		LocalDate checkinDate = LocalDate.now();
		LocalDate checkoutDate = LocalDate.now().plusDays(3);
		int numberOfGuests = 3;
		List<Room> availableRooms = new ArrayList<>(controller.findAvailableRooms(checkinDate, checkoutDate, numberOfGuests).getAvailableRoomsWithPrice().keySet());

		// WHEN : booking the room
		Room requestedRoom = availableRooms.get(0);
		Room bookedRoom = controller.bookARoom(requestedRoom.getRoomNumber(), checkinDate, checkoutDate);

		// THEN : a reservation has been added to the room for the given dates
		assertEquals(1, bookedRoom.getReservations().size());
		assertEquals(Reservation.of(checkinDate, checkoutDate), bookedRoom.getReservations().get(0));
	}

	@Test
	public void bookARoomNominalCase_TheRoomIsNoLongerAvailable() {
		// GIVEN : available rooms for a specific period
		LocalDate checkinDate = LocalDate.now();
		LocalDate checkoutDate = LocalDate.now().plusDays(3);
		int numberOfGuests = 3;
		List<Room> availableRooms = new ArrayList<>(controller.findAvailableRooms(checkinDate, checkoutDate, numberOfGuests).getAvailableRoomsWithPrice().keySet());

		// WHEN : booking the room
		Room requestedRoom = availableRooms.get(0);
		controller.bookARoom(requestedRoom.getRoomNumber(), checkinDate, checkoutDate);

		// THEN : the room is no longer available for the dates
		availableRooms = new ArrayList<>(controller.findAvailableRooms(checkinDate, checkoutDate, numberOfGuests).getAvailableRoomsWithPrice().keySet());
		assertFalse(availableRooms.stream().anyMatch(room -> room.getRoomNumber().equals(requestedRoom.getRoomNumber())));
	}

	@Test(expected = RoomUnavailableException.class)
	public void bookAnAlreadyBookedRoom() {
		// GIVEN : a room with a reservation
		String requestedRoomNumber = "101";
		LocalDate checkinDate = LocalDate.now();
		LocalDate checkoutDate = LocalDate.now().plusDays(3);
		controller.bookARoom(requestedRoomNumber, checkinDate, checkoutDate);

		// WHEN : booking the same room for similar dates
		controller.bookARoom(requestedRoomNumber, checkinDate, checkoutDate);
	}

	@Test(expected = HostelClosedException.class)
	public void noRoomAvailableDuringTheAnnualClosing() {
		//GIVEN Reservation during the annual closing
		LocalDate checkin = LocalDate.of(2019, 10, 18);
		LocalDate checkout = LocalDate.of(2019, 10, 20);

		//WHEN find available rooms
		controller.findAvailableRooms(checkin, checkout, 2);

	}

	@Test(expected = RoomUnavailableException.class)
	public void bookARoomWithAPlannedMaintenance() {
		// GIVEN : a room with a planned maintenance
		Room roomToMaintain = controller.getRoomRepository().findOne("101");
		roomToMaintain.planMaintenance(PlannedMaintenance.of(LocalDate.now(), LocalDate.now()));

		// WHEN : booking the room on the day the maintenance is planned
		controller.bookARoom(roomToMaintain.getRoomNumber(), LocalDate.now(), LocalDate.now().plusDays(3));
	}

	@Test(expected = RoomUnavailableException.class)
	public void bookARoomWithAPlannedMaintenanceAndAnOverlappingReservation() {
		// GIVEN : a room with a planned maintenance
		Room requestedRoom = controller.getRoomRepository().findOne("101");
		requestedRoom.book(Reservation.of(LocalDate.now().minusDays(1), LocalDate.now().plusDays(2)));
		requestedRoom.planMaintenance(PlannedMaintenance.of(LocalDate.now().plusDays(1), LocalDate.now().plusDays(6)));

		// WHEN : booking the room on the day the maintenance is planned
		controller.bookARoom(requestedRoom.getRoomNumber(), LocalDate.now(), LocalDate.now().plusDays(3));
	}

	@Test(expected = RoomDoesNotExistException.class)
	public void bookNonExistingRoom() {
		// WHEN : book a non existing room
		controller.bookARoom("", LocalDate.now(), LocalDate.now().plusDays(3));
	}

	@Test
	public void getRoomPriceForSeason() {
		// GIVEN : a 3 days stay
		int numberOfNights = 2;
		LocalDate checkin = LocalDate.of(2019, 3, 15);
		LocalDate checkout = checkin.plusDays(numberOfNights);
		List<Season> seasonsForDates = Season.findSeasonsForDates(checkin, checkout);

		//WHEN : getting the available rooms with their prices
		BookingRequest availableRooms = controller.findAvailableRooms(checkin, checkout, 2);

		//THEN : the booking is on only one season
		assertEquals(1, seasonsForDates.size());

		// AND : each room's price is its base price time the season's discount factor time the number of days
		Season currentSeason = seasonsForDates.get(0);
		for(Map.Entry<Room, Integer> roomIntegerEntry : availableRooms.getAvailableRoomsWithPrice().entrySet()) {
			assertEquals((Integer)(roomIntegerEntry.getKey().getRoomType().getBasePrice() * (currentSeason.getDiscountFactor() / 10) * numberOfNights),
					roomIntegerEntry.getValue());
		}
	}

	@Test
	public void getRoomPriceForDatesSpanningOnSeveralSeasons() {
		// GIVEN : a 3 days stay that overlaps 2 seasons
		int numberOfNights = 3;
		LocalDate checkin = LocalDate.of(2019, Season.LOW_SEASON.getEndPeriod().getMonth(), Season.LOW_SEASON.getEndPeriod().getDayOfMonth()).minusDays(1);
		LocalDate checkout = checkin.plusDays(numberOfNights);
		List<Season> seasonsForDates = Season.findSeasonsForDates(checkin, checkout);
		assertEquals(2, seasonsForDates.size());

		//WHEN : getting the available rooms with their prices
		BookingRequest availableRooms = controller.findAvailableRooms(checkin, checkout, 2);

		// THEN : each room's price is its base price time the season's discount factor time the number of days
		for(Map.Entry<Room, Integer> roomIntegerEntry : availableRooms.getAvailableRoomsWithPrice().entrySet()) {
			Integer roomPrice = ((roomIntegerEntry.getKey().getRoomType().getBasePrice() * (Season.LOW_SEASON.getDiscountFactor() / 10) * numberOfNights - 1) +
					(roomIntegerEntry.getKey().getRoomType().getBasePrice() * (Season.HIGH_SEASON.getDiscountFactor() / 10)));
			assertEquals(roomPrice,	roomIntegerEntry.getValue());
		}
	}
}
