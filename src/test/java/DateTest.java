import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class DateTest {
    @Test
    public void localDate() throws Exception {
        LocalDate now = LocalDate.now();

        assertThat(now.getYear(), equalTo(2017));
        assertThat(now.getMonthValue(), equalTo(10));
        assertThat(now.getMonth(), equalTo(Month.OCTOBER));
        assertThat(now.getDayOfMonth(), equalTo(15));
        assertThat(now.getDayOfWeek(), equalTo(DayOfWeek.SUNDAY));

        assertThat(now.get(ChronoField.YEAR), equalTo(now.getYear()));
    }

    @Test
    public void localTime() throws Exception {
        try {
            LocalTime parse = LocalTime.parse("11:59:59");
        } catch (DateTimeParseException e) {
            fail("DateTimeParseException");
        }

        LocalTime now = LocalTime.now();
        assertThat(now.getHour(), equalTo(17));
    }

    @Test
    public void localDateTime() throws Exception {
        LocalDateTime parse = LocalDateTime.parse("2018-04-21t00:02:14", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime localDateTime = LocalDate.now().atTime(LocalTime.now());
        LocalDateTime localDateTime2 = LocalDate.now().atTime(0, 12, 13);
        LocalDateTime localDateTime3 = LocalTime.now().atDate(LocalDate.now());

        localDateTime.toLocalDate();
        localDateTime.toLocalTime();

        System.out.println(localDateTime2);
    }

    @Test
    public void instantOfEpochSecond() throws Exception {
        Instant instant = Instant.ofEpochSecond(3, 1L);
        System.out.println(instant);
    }

    @Test
    public void instantNowGetDayOfMonthOccurUnsupportedTemporalTypeException() throws Exception {
        try {
            Instant.now().get(ChronoField.DAY_OF_MONTH);
            fail("not occur exception");
        } catch (UnsupportedTemporalTypeException e) {
            return;
        }

        fail();
    }

    @Test
    public void duration() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        long hours = 1L;
        LocalDateTime plusHours = now.plusHours(hours);
        Duration between = Duration.between(now, plusHours);

        assertThat(between.toHours(), equalTo(hours));
    }

    @Test
    public void period() throws Exception {
        LocalDate now = LocalDate.now();
        int daysToAdd = 1;
        LocalDate plusOneDay = now.plusDays(daysToAdd);
        Period period = Period.between(now, plusOneDay);

        assertThat(period.getDays(), equalTo(daysToAdd));
    }

    @Test
    public void durationFactory() throws Exception {
        Duration duration = Duration.ofHours(3);
        System.out.println(duration);
    }

    @Test
    public void temporalAdjuster() throws Exception {
        LocalDate localDate = LocalDate.of(2017, 10, 16);
        LocalDate adjusted = localDate.with(new NextWorkingDay());

        assertThat(adjusted.getDayOfWeek(), equalTo(DayOfWeek.TUESDAY));
        assertThat(adjusted.getDayOfMonth(), equalTo(17));
    }
}