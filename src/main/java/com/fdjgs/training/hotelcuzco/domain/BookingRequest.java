package com.fdjgs.training.hotelcuzco.domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lotsys on 07/06/2019.
 */
public class BookingRequest {

	private LocalDate checkinDate;
	private LocalDate checkoutDate;
	Map<Room, Integer> availableRoomsWithPrice;

	private BookingRequest(LocalDate checkinDate, LocalDate checkoutDate, Map<Room, Integer> availableRoomsWithPrice) {
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.availableRoomsWithPrice = availableRoomsWithPrice;
	}

	public static BookingRequest buildBookingRequest(LocalDate checkinDate, LocalDate checkoutDate, List<Room> availableRooms) {
		List<Season> seasonsForDates = Season.findSeasonsForDates(checkinDate, checkoutDate);
		return new BookingRequest(checkinDate, checkoutDate, computeRoomPrices(checkinDate, checkoutDate, availableRooms, seasonsForDates));
	}

	private static Map<Room, Integer> computeRoomPrices(LocalDate beginDate, LocalDate endDate, List<Room> availableRooms, List<Season> seasonsForDates) {
		Map<Room, Integer> roomsWithPrice = new HashMap<>();
		for(Room room : availableRooms) {
			roomsWithPrice.put(room, computeTotalPrice(beginDate, endDate, room, seasonsForDates));
		}
		return roomsWithPrice;
	}

	private static int computeTotalPrice(LocalDate beginDate, LocalDate endDate, Room room, List<Season> seasonsForDates) {
		int totalPrice = 0;
		LocalDate currentDate = beginDate;
		while(currentDate.isBefore(endDate)) {
			totalPrice += room.getRoomType().getBasePrice() * getrCurrentDateSeasonDiscountFactor(currentDate, seasonsForDates);
			currentDate = currentDate.plusDays(1);
		}
		return totalPrice;
	}

	private static int getrCurrentDateSeasonDiscountFactor(LocalDate currentDate, List<Season> seasonsForDates) {
		for(Season season : seasonsForDates) {
			if(!season.isClosed() && season.checkDateInSeason(currentDate)) {
				return season.getDiscountFactor() / 10;
			}
		}
		return 0;
	}

	public Map<Room, Integer> getAvailableRoomsWithPrice() {
		return availableRoomsWithPrice;
	}
}
