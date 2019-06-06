package com.fdjgs.training.hotelcuzco.infrastructure;

import com.fdjgs.training.hotelcuzco.domain.Reservation;
import com.fdjgs.training.hotelcuzco.domain.Room;
import com.fdjgs.training.hotelcuzco.repository.RoomRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class EmbeddedRoomRepositoryImpl implements RoomRepository {

	private final Map<String, Room> roomsDatabase;

	public EmbeddedRoomRepositoryImpl(Map<String, Room>roomsDatabase) {
		this.roomsDatabase = roomsDatabase;
	}

	@Override
	public Room save(Room room) {
		return roomsDatabase.put(room.getRoomNumber(), room);
	}

	@Override
	public List<Room> findAvailableRooms(Reservation requestReservation, int numberOfGuest) {
		return roomsDatabase.values().stream()
				.filter(r -> r.getCapacity() >= numberOfGuest)
				.filter(r -> r.isBookable(requestReservation))
				.collect(Collectors.toList());
	}

	@Override public Room findOne(String roomNumber) {
		return this.roomsDatabase.get(roomNumber);
	}
}
