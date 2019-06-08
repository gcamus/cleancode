package com.fdjgs.training.hotelcuzco.domain;

/**
 * Created by Lotsys on 07/06/2019.
 */
public enum RoomType {

	REGULAR(4000),
	QUEEN(8000),
	KING(15000);

	private Integer basePrice;

	RoomType(Integer basePrice) {
		this.basePrice = basePrice;
	}

	public Integer getBasePrice() {
		return this.basePrice;
	}
}
