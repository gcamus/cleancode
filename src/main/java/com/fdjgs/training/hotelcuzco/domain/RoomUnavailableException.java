package com.fdjgs.training.hotelcuzco.domain;

/**
 * Created by Lotsys on 06/06/2019.
 */
public class RoomUnavailableException extends RuntimeException {

	public RoomUnavailableException(String roomNumber) {
		super(String.format("Room %s is unavailable at the period", roomNumber));
	}
}
