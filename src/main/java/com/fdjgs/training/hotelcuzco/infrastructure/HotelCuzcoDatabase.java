package com.fdjgs.training.hotelcuzco.infrastructure;

import com.fdjgs.training.hotelcuzco.domain.Room;
import com.fdjgs.training.hotelcuzco.domain.RoomType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lotsys on 06/06/2019.
 */
public class HotelCuzcoDatabase {

	Map<String, Room> roomsDatabase;

	public static HotelCuzcoDatabase defaultDatabase() {
		Map<String, Room> roomsDatabase = new HashMap<>();
		Room room2 = Room.createRoom("102", 1, "", 4, RoomType.KING);
		Room room3 = Room.createRoom("103", 1, "", 3, RoomType.QUEEN);
		Room room4 = Room.createRoom("201", 2, "", 2, RoomType.REGULAR);
		Room room5 = Room.createRoom("202", 2, "", 2, RoomType.REGULAR);
		Room room1 = Room.createRoom("101", 1, "", 2, RoomType.QUEEN);
		roomsDatabase.put(room1.getRoomNumber(), room1);
		roomsDatabase.put(room2.getRoomNumber(), room2);
		roomsDatabase.put(room3.getRoomNumber(), room3);
		roomsDatabase.put(room4.getRoomNumber(), room4);
		roomsDatabase.put(room5.getRoomNumber(), room5);
		return new HotelCuzcoDatabase(roomsDatabase);
	}

	public static HotelCuzcoDatabase of(Map<String, Room> roomsDatabase) {
		return new HotelCuzcoDatabase(roomsDatabase);
	}

	private HotelCuzcoDatabase(Map<String, Room> rooms) {
		roomsDatabase = rooms;
	}

	public Collection<Room> getAll() {
		return roomsDatabase.values();
	}

	public Room get(String roomNumber) {
		return roomsDatabase.get(roomNumber);
	}

	public Room persist(Room room) {
		return roomsDatabase.put(room.getRoomNumber(), room);
	}
}
