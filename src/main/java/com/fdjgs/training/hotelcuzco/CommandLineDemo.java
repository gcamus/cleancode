package com.fdjgs.training.hotelcuzco;

import com.fdjgs.training.hotelcuzco.domain.Room;
import com.fdjgs.training.hotelcuzco.infrastructure.EmbeddedRoomRepositoryImpl;
import com.fdjgs.training.hotelcuzco.infrastructure.HotelCuzcoDatabase;
import com.fdjgs.training.hotelcuzco.infrastructure.RoomController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Lotsys on 05/06/2019.
 */
public class CommandLineDemo {

	public static RoomController controller;

	public static void main(String[] args) {
		HotelCuzcoDatabase database = HotelCuzcoDatabase.defaultDatabase();

		controller = new RoomController(new EmbeddedRoomRepositoryImpl(database));

		System.out.println("Bienvenue dans le système de résevation de chamber");
		boolean continueBooking;
		do {
			continueBooking = bookRoomInterface();
		} while(continueBooking);
		System.out.println("Au revoir");

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

		List<Room> rooms = new ArrayList<>(controller.findAvailableRooms(checkin, checkout, guest).getAvailableRoomsWithPrice().keySet());;
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
