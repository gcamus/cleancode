package com.fdjgs.training.hotelcuzco.domain;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class Room {

	String roomNumber;

	Integer floor;

	String description;

	Integer capacity;

	RoomType roomType;

	List<Unavailability> unavailabilities;

	private Room() {}

	public static Room createRoom(String roomNumber, Integer floor, String description, Integer capacity, RoomType roomType) {
		Room room = new Room();
		room.capacity = capacity;
		room.description = description;
		room.floor = floor;
		room.roomNumber = roomNumber;
		room.roomType = roomType;
		room.unavailabilities = new ArrayList<>();
		return room;
	}

	public void book(Reservation reservation) {
		if(!this.isBookable(reservation)) {
			throw new RoomUnavailableException(roomNumber);
		}
		unavailabilities.add(reservation);
	}

	public boolean isBookable(Reservation requestReservation) {
		return unavailabilities.isEmpty()
				|| unavailabilities.stream().allMatch(reservation -> reservation.isNotOverlapping(requestReservation));
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public List<Reservation> getReservations() {
		return getTypedUnavailability(Reservation.class);
	}

	public List<PlannedMaintenance> getPlannedMaintenances() {
		return getTypedUnavailability(PlannedMaintenance.class);
	}

	private <T extends Unavailability> List<T> getTypedUnavailability(Class<T> requestedClass) {
		return unavailabilities.stream()
				.filter(unavailability -> unavailability.getClass().isAssignableFrom(requestedClass))
				.map(unavailability -> (T) unavailability)
				.collect(toList());
	}

	public void planMaintenance(PlannedMaintenance plannedMaintenance) {
		unavailabilities.add(plannedMaintenance);
	}
}
