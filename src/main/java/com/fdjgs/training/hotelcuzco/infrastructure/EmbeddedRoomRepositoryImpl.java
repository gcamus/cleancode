package com.fdjgs.training.hotelcuzco.infrastructure;

import com.fdjgs.training.hotelcuzco.domain.Reservation;
import com.fdjgs.training.hotelcuzco.domain.Room;
import com.fdjgs.training.hotelcuzco.repository.RoomDoesNotExistException;
import com.fdjgs.training.hotelcuzco.repository.RoomRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class EmbeddedRoomRepositoryImpl implements RoomRepository {

	private final HotelCuzcoDatabase database;

	public EmbeddedRoomRepositoryImpl(HotelCuzcoDatabase database) {
		this.database = database;
	}

	@Override
	public Room save(Room room) {
		return database.persist(room);
	}

	@Override
	public List<Room> findAvailableRooms(Reservation requestReservation, int numberOfGuest) {
		return database.getAll().stream()
				.filter(r -> r.getCapacity() >= numberOfGuest)
				.filter(r -> r.isBookable(requestReservation))
				.collect(Collectors.toList());
	}

	@Override
	public Room findOne(String roomNumber) {
		Room room = this.database.get(roomNumber);
		if(room == null) {
			throw new RoomDoesNotExistException(roomNumber);
		}
		return room;
	}
}
