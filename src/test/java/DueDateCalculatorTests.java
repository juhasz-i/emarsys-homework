import org.emarsys.duedatecalculator.DueDateCalculator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DueDateCalculatorTests {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DueDateCalculator dueDateCalculator = new DueDateCalculator();

    @Test
    public void testNullSubmitDate_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> dueDateCalculator.calculateDueDate(null, 1));
    }

    @Test
    public void testNullTurnaroundTime_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> dueDateCalculator.calculateDueDate(LocalDateTime.parse("2022-05-26 16:52", dateTimeFormatter), null));
    }

    @Test
    public void testNullSubmitDateAndNullTurnaroundTime_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> dueDateCalculator.calculateDueDate(null, null));
    }

    @Test
    public void testZeroTurnaroundTime_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> dueDateCalculator.calculateDueDate(LocalDateTime.parse("2022-05-26 16:52", dateTimeFormatter), 0));
    }

    @Test
    public void testNegativeTurnaroundTime_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> dueDateCalculator.calculateDueDate(LocalDateTime.parse("2022-05-26 16:52", dateTimeFormatter), -1));
    }

    @Test
    public void testBeforeWorkingHoursSubmitDate_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> dueDateCalculator.calculateDueDate(LocalDateTime.parse("2022-05-26 06:52", dateTimeFormatter), 10));
    }

    @Test
    public void testAfterWorkingHoursSubmitDate_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> dueDateCalculator.calculateDueDate(LocalDateTime.parse("2022-05-26 17:52", dateTimeFormatter), 10));
    }

    @Test
    public void testWeekendSubmitDate_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> dueDateCalculator.calculateDueDate(LocalDateTime.parse("2022-05-28 10:52", dateTimeFormatter), 10));
    }

    @Test
    public void testSubmitDateAndTurnaroundTime_withExpectedDueDateAtTheNextDay_shouldPassTest() {
        LocalDateTime submitDate = LocalDateTime.parse("2022-05-26 15:52", dateTimeFormatter);
        LocalDateTime expectedDueDate = LocalDateTime.parse("2022-05-27 09:52", dateTimeFormatter);
        assertEquals(expectedDueDate, dueDateCalculator.calculateDueDate(submitDate, 2));
    }

    @Test
    public void testSubmitDateAndTurnaroundTime_withExpectedDueDateAtTheSameDay_shouldPassTest() {
        LocalDateTime submitDate = LocalDateTime.parse("2022-05-27 15:52", dateTimeFormatter);
        LocalDateTime expectedDueDate = LocalDateTime.parse("2022-05-27 16:52", dateTimeFormatter);
        assertEquals(expectedDueDate, dueDateCalculator.calculateDueDate(submitDate, 1));
    }

    @Test
    public void testFridayAfternoonSubmitDate_withExpectedMondayMorningDueDate_shouldPassTest() {
        LocalDateTime submitDate = LocalDateTime.parse("2022-05-27 16:52", dateTimeFormatter);
        LocalDateTime expectedDueDate = LocalDateTime.parse("2022-05-30 09:52", dateTimeFormatter);
        assertEquals(expectedDueDate, dueDateCalculator.calculateDueDate(submitDate, 1));
    }

}
