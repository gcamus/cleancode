package com.fdjgs.training.hotelcuzco.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by Lotsys on 06/06/2019.
 */
public class Unavailability {

	LocalDate beginDate;

	LocalDate endDate;

	public boolean isNotOverlapping(Unavailability requestedPeriod) {
		return requestedPeriod.beginDate.isAfter(this.endDate)
				|| requestedPeriod.endDate.isBefore(this.beginDate);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Unavailability that = (Unavailability) o;

		if (!Objects.equals(beginDate, that.beginDate)) {
			return false;
		}
		return Objects.equals(endDate, that.endDate);
	}

	@Override
	public int hashCode() {
		int result = beginDate != null ? beginDate.hashCode() : 0;
		result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
		return result;
	}
}
