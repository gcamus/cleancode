package com.fdjgs.training.hotelcuzco.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class Room {

	String roomNumber;

	Integer floor;

	String description;

	Integer capacity;

	List<Reservation> reservations;

	private Room() {}

	public static Room createRoom(String roomNumber, Integer floor, String description, Integer capacity) {
		Room room = new Room();
		room.capacity = capacity;
		room.description = description;
		room.floor = floor;
		room.roomNumber = roomNumber;
		room.reservations = new ArrayList<>();
		return room;
	}

	public void book(Reservation reservation) {
		reservations.add(reservation);
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public boolean isBookable(Reservation requestReservation) {
		return reservations.isEmpty()
				|| reservations.stream().allMatch(reservation -> reservation.isNotOverlapping(requestReservation));
	}
}
