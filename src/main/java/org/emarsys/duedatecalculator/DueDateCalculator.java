package org.emarsys.duedatecalculator;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DueDateCalculator {

    private static final LocalTime BEGINNING_OF_WORKING_DAY = LocalTime.of(9, 0);
    private static final LocalTime END_OF_WORKING_DAY = LocalTime.of(17, 0);

    public LocalDateTime calculateDueDate(LocalDateTime dateAndTimeOfSubmission, Integer turnaroundTime) {
        LocalDateTime result;
        validateDateAndTimeOfSubmission(dateAndTimeOfSubmission, turnaroundTime);
        result = addWorkingDays(dateAndTimeOfSubmission, turnaroundTime / 8);
        result = addRemainderHours(result,turnaroundTime % 8);
        return result;
    }

    public void validateDateAndTimeOfSubmission(LocalDateTime dateAndTimeOfSubmission, Integer turnaroundTime) {
        checkNullArguments(dateAndTimeOfSubmission, turnaroundTime);
        checkPositiveTurnaroundTime(turnaroundTime);
        checkDateOfSubmission(dateAndTimeOfSubmission);
        checkTimeOfSubmission(dateAndTimeOfSubmission);
    }

    public void checkDateOfSubmission(LocalDateTime dateAndTimeOfSubmission) throws IllegalArgumentException {
        if (dateAndTimeOfSubmission.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    || dateAndTimeOfSubmission.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            throw new IllegalArgumentException("Issues may only be reported on Monday, Tuesday, Wednesday, Thursday or Friday!");
        }
    }

    public void checkTimeOfSubmission(LocalDateTime dateAndTimeOfSubmission) throws IllegalArgumentException {
        if (dateAndTimeOfSubmission.toLocalTime().isBefore(BEGINNING_OF_WORKING_DAY)
                    || dateAndTimeOfSubmission.toLocalTime().isAfter(END_OF_WORKING_DAY)) {
            throw new IllegalArgumentException("Issues may only be reported between 9:00AM and 5PM!");
        }
    }

    public void checkNullArguments(LocalDateTime dateAndTimeOfSubmission, Integer turnaroundTime) throws IllegalArgumentException {
        if (dateAndTimeOfSubmission == null || turnaroundTime == null) {
            throw new IllegalArgumentException("DateAndTimeOfSubmission and turnaroundTime may not be null!");
        }
    }

    public void checkPositiveTurnaroundTime(Integer turnaroundTime) throws IllegalArgumentException {
        if (turnaroundTime <= 0) {
            throw new IllegalArgumentException("TurnaroundTime must be an integer greater than 0!");
        }
    }

    public LocalDateTime addWorkingDays(LocalDateTime dateOfSubmission, int daysToAdd) {
        LocalDateTime result = dateOfSubmission;
        int addedDays = 0;
        while (addedDays < daysToAdd) {
            result = result.plusDays(1);
            if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                addedDays++;
            }
        }
        return result;
    }

    private LocalDateTime addRemainderHours(LocalDateTime dueDate, int turnaroundHours) {
        LocalDateTime result = dueDate.plusHours(turnaroundHours);
        LocalTime tempTime = result.toLocalTime();
        if (tempTime.isAfter(END_OF_WORKING_DAY)) {
            LocalTime newTime = BEGINNING_OF_WORKING_DAY.plusMinutes(getRemainderMinutesAfterEndOfWorkingDay(tempTime));
            result = addWorkingDays(result, 1);
            result = result.with(newTime);
        }
        return result;
    }

    private int getRemainderMinutesAfterEndOfWorkingDay(LocalTime remainderTimeWithoutDays) {
        return (remainderTimeWithoutDays.getHour() - DueDateCalculator.END_OF_WORKING_DAY.getHour()) * 60 + remainderTimeWithoutDays.getMinute();
    }

}
