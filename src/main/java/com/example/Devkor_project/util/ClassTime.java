package com.example.Devkor_project.util;

import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
public enum ClassTime
{
    PERIOD_1(1, LocalTime.of(9, 0), LocalTime.of(10, 15)),
    PERIOD_2(2, LocalTime.of(10, 30), LocalTime.of(11, 45)),
    PERIOD_3(3, LocalTime.of(12, 0), LocalTime.of(13, 15)),
    PERIOD_4(4, LocalTime.of(13, 30), LocalTime.of(14, 45)),
    PERIOD_5(5, LocalTime.of(15, 0), LocalTime.of(16, 15)),
    PERIOD_6(6, LocalTime.of(16, 30), LocalTime.of(17, 45)),
    PERIOD_7(7, LocalTime.of(18, 0), LocalTime.of(19, 15)),
    PERIOD_8(8, LocalTime.of(19, 30), LocalTime.of(20, 45));

    private final int period;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public static ClassTime fromPeriod(int period) {
        for (ClassTime classTime : values()) {
            if (classTime.getPeriod() == period) {
                return classTime;
            }
        }
        throw new AppException(ErrorCode.INVALID_PERIOD, period);
    }
}

