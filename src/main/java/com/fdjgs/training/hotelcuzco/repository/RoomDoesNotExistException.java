package com.fdjgs.training.hotelcuzco.repository;

/**
 * Created by Lotsys on 06/06/2019.
 */
public class RoomDoesNotExistException extends RuntimeException {
	public RoomDoesNotExistException(String roomNumber) {
		super(String.format("The room %s does not exist", roomNumber));
	}
}
