package com.heybys.oddments.fooddelivery.domain.generic;

import com.heybys.oddments.base.domain.ValueObject;
import jakarta.persistence.Embeddable;
import java.time.LocalTime;
import lombok.Getter;

@Getter
@Embeddable
public class TimePeriod extends ValueObject<TimePeriod> {

    private LocalTime startTime;
    private LocalTime endTime;

    public static TimePeriod between(LocalTime startTime, LocalTime endTime) {
        return new TimePeriod(startTime, endTime);
    }

    public TimePeriod() {}

    public TimePeriod(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean contains(LocalTime datetime) {
        return (datetime.isAfter(startTime) || datetime.equals(startTime))
                && (datetime.isBefore(endTime) || datetime.equals(endTime));
    }

    public TimePeriod putOffHours(int hours) {
        return new TimePeriod(startTime.plusHours(hours), endTime.plusHours(hours));
    }

    public TimePeriod putOffHours(long hours) {
        return new TimePeriod(startTime.plusHours(hours), endTime.plusHours(hours));
    }
}
