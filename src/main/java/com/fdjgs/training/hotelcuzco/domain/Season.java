package com.fdjgs.training.hotelcuzco.domain;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Lotsys on 07/06/2019.
 */
public enum Season {
	CLOSED_SEASON(MonthDay.of(10, 18), MonthDay.of(10, 28), 0, true),
	LOW_SEASON(MonthDay.of(1, 1), MonthDay.of(4, 30), 20, false),
	HIGH_SEASON(MonthDay.of(5, 1), MonthDay.of(8, 31), 80, false),
	REGULAR_SEASON_PART_1(MonthDay.of(9, 1), MonthDay.of(12, 31), 0, false)
	;

	private MonthDay beginPeriod;
	private MonthDay endPeriod;
	private Integer discountFactor;
	private boolean closed;

	Season(MonthDay beginPeriod, MonthDay endPeriod, Integer discountFactor, boolean closed) {
		this.beginPeriod = beginPeriod;
		this.endPeriod = endPeriod;
		this.discountFactor = discountFactor;
		this.closed = closed;
	}

	public MonthDay getBeginPeriod() {
		return beginPeriod;
	}

	public MonthDay getEndPeriod() {
		return endPeriod;
	}

	public Integer getDiscountFactor() {
		return discountFactor;
	}

	public boolean isClosed() {
		return closed;
	}

	public static List<Season> findSeasonsForDates(LocalDate beginDate, LocalDate endDate) {
		return Arrays.stream(Season.values()).filter(season ->
				season.checkDateInSeason(beginDate) || season.checkDateInSeason(endDate)
		).collect(Collectors.toList());
	}

	public boolean checkDateInSeason(LocalDate date) {
		return this.beginPeriod.atYear(date.getYear()).isBefore(date)
				&& this.endPeriod.atYear(date.getYear()).isAfter(date);
	}
}
