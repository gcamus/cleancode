package com.fdjgs.training.hotelcuzco.domain;

/**
 * Created by Lotsys on 06/06/2019.
 */
public class InvalidReservationDatesException extends RuntimeException {

	public InvalidReservationDatesException(String message) {
		super(message);
	}
}
