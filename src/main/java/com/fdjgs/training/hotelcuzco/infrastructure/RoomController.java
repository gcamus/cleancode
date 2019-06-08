package com.fdjgs.training.hotelcuzco.infrastructure;

import com.fdjgs.training.hotelcuzco.domain.BookingRequest;
import com.fdjgs.training.hotelcuzco.domain.HostelClosedException;
import com.fdjgs.training.hotelcuzco.domain.Reservation;
import com.fdjgs.training.hotelcuzco.domain.Room;
import com.fdjgs.training.hotelcuzco.domain.Season;
import com.fdjgs.training.hotelcuzco.repository.RoomRepository;

import java.time.LocalDate;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class RoomController {

	private RoomRepository roomRepository;

	public RoomController(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public BookingRequest findAvailableRooms(LocalDate checkin, LocalDate checkout, int numberOfGuest) {
		checkHostelIsNotClosed(checkin, checkout);

		Reservation requestReservation = Reservation.of(checkin, checkout);

		return BookingRequest.buildBookingRequest(checkin, checkout, roomRepository.findAvailableRooms(requestReservation, numberOfGuest));
	}

	public Room bookARoom(String roomNumber, LocalDate checkin, LocalDate checkout) {
		checkHostelIsNotClosed(checkin, checkout);

		Room requestedRoom = roomRepository.findOne(roomNumber);

		Reservation requestedReservation = Reservation.of(checkin, checkout);
		requestedRoom.book(requestedReservation);

		return roomRepository.save(requestedRoom);
	}

	private void checkHostelIsNotClosed(LocalDate checkin, LocalDate checkout) {
		if(Season.findSeasonsForDates(checkin, checkout).stream().anyMatch(Season::isClosed)) {
			throw new HostelClosedException();
		}
	}

	public RoomRepository getRoomRepository() {
		return roomRepository;
	}
}
