package com.fdjgs.training.hotelcuzco.repository;

import com.fdjgs.training.hotelcuzco.domain.Reservation;
import com.fdjgs.training.hotelcuzco.domain.Room;

import java.util.List;

/**
 * Created by Lotsys on 05/06/2019.
 */
public interface RoomRepository {

	Room save(Room room);

	List<Room> findAvailableRooms(Reservation requestReservation, int numberOfGuest);

	Room findOne(String roomNumber) throws RoomDoesNotExistException;

}
