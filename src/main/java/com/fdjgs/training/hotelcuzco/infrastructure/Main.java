package com.fdjgs.training.hotelcuzco.infrastructure;

import com.fdjgs.training.hotelcuzco.domain.Room;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class Main {

	public static RoomController controller;

	public static void main(String[] args) {
		Map<String, Room> roomsDatabase = initializeRoomsDatabase();

		controller = new RoomController(new EmbeddedRoomRepositoryImpl(roomsDatabase));

		System.out.println("Bienvenue dans le système de résevation de chamber");
		boolean continueBooking;
		do {
			continueBooking = bookRoomInterface();
		} while(continueBooking);
		System.out.println("Au revoir");

	}

	private static Map<String, Room> initializeRoomsDatabase() {
		Map<String, Room> roomsDatabase = new HashMap<>();
		Room room1 = Room.createRoom("101", 1, "", 2);
		Room room2 = Room.createRoom("102", 1, "", 4);
		Room room3 = Room.createRoom("103", 1, "", 3);
		Room room4 = Room.createRoom("201", 2, "", 2);
		Room room5 = Room.createRoom("202", 2, "", 2);
		roomsDatabase.put(room1.getRoomNumber(), room1);
		roomsDatabase.put(room2.getRoomNumber(), room2);
		roomsDatabase.put(room3.getRoomNumber(), room3);
		roomsDatabase.put(room4.getRoomNumber(), room4);
		roomsDatabase.put(room5.getRoomNumber(), room5);
		return roomsDatabase;
	}

	private static boolean bookRoomInterface() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Veuillez saisir vos dates de réservation(aaaa/mm/jj) et le nombre d'invité");
		System.out.println("Arrivée : ");
		LocalDate checkin = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
		System.out.println("Départ : ");
		LocalDate checkout = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
		System.out.println("Invités : ");
		Integer guest = Integer.parseInt(scanner.nextLine());

		List<Room> rooms = controller.displayAvailableRooms(checkin, checkout, guest);
		if(rooms.isEmpty()) {
			System.out.println("Aucune chambre disponible");
		} else {
			rooms.forEach(room -> {
				System.out.println(String.format("Chambre : %s, capacité %d", room.getRoomNumber(), room.getCapacity()));
			});
			System.out.println("Veuillez choisir votre chambre pour cette réservation : ");
			String roomNumber = scanner.nextLine();

			controller.bookARoom(roomNumber, checkin, checkout);
			System.out.println("Votre réservation a bien été prise en compte.");
		}

		System.out.println("Voulez effectuer une nouvelle réservation ? (true|false)");
		return scanner.nextBoolean();
	}
}
