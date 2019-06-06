package com.fdjgs.training.hotelcuzco.infrastructure;

import com.fdjgs.training.hotelcuzco.domain.Reservation;
import com.fdjgs.training.hotelcuzco.domain.Room;
import com.fdjgs.training.hotelcuzco.repository.RoomRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class RoomController {

	private RoomRepository roomRepository;

	public RoomController(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public List<Room> displayAvailableRooms(LocalDate checkin, LocalDate checkout, int numberOfGuest) {
		Reservation requestReservation = Reservation.of(checkin, checkout);

		return roomRepository.findAvailableRooms(requestReservation, numberOfGuest);
	}

	public void bookARoom(String roomNumber, LocalDate checkin, LocalDate checkout) {
		Room requestedRoom = roomRepository.findOne(roomNumber);

		Reservation requestedReservation = Reservation.of(checkin, checkout);
		requestedRoom.book(requestedReservation);

		roomRepository.save(requestedRoom);
	}
}
