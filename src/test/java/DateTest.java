import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Locale;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

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

    @Test
    public void dateTimeFormatter() throws Exception {
        LocalDate date = LocalDate.of(2017, 1,18); // 월의 앞자리를 0으로 채우는지 확인하기 위해
        assertThat(date.format(DateTimeFormatter.BASIC_ISO_DATE), equalTo("20170118"));
        assertThat(date.format(DateTimeFormatter.ISO_LOCAL_DATE), equalTo("2017-01-18"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date1 = LocalDate.of(2017, 10, 18);
        String formattedDate = date1.format(formatter);
        LocalDate date2 = LocalDate.parse(formattedDate, formatter);

        assertThat(formattedDate, equalTo("18/10/2017"));
        assertTrue(date1.equals(date2));
        assertFalse(date1.equals(LocalDate.of(2017, 10, 19)));
    }

    @Test
    public void dateTimeFormatterOfPattern() throws Exception {
       DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.KOREA);
       LocalDate date = LocalDate.of(2017, 10, 18);
       String formattedDate = date.format(dateTimeFormatter);
       LocalDate date1 = LocalDate.parse(formattedDate, dateTimeFormatter);

       assertThat(formattedDate, equalTo("18. 10월 2017"));
       assertTrue(date.equals(date1));
    }

    @Test
    public void dateTimeFormatterBuilder() throws Exception {
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral(". ")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral(" ")
                .appendText(ChronoField.YEAR)
                .parseCaseSensitive()
                .toFormatter(Locale.KOREA);

        LocalDate date = LocalDate.of(2017, 10, 18);

        assertThat(date.format(dateTimeFormatter), equalTo("18. 10월 2017"));
    }

    @Test
    public void zoneId() throws Exception {
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        assertNotNull(seoulZoneId);

        LocalDateTime dateTime = LocalDateTime.of(2017, Month.APRIL, 10, 0, 0);
        dateTime.atZone(seoulZoneId).toInstant();

        Instant instant = Instant.now();
        LocalDateTime ofInstant = LocalDateTime.ofInstant(instant, seoulZoneId);
    }

    @Test
    public void zoneOffset() throws Exception {
        ZoneOffset of = ZoneOffset.of("-09:00");
        LocalDateTime now = LocalDateTime.now(of);

        System.out.println(now);
    }

    @Test
    public void offsetDateTime() throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(2017, Month.OCTOBER, 23, 22, 41);
        OffsetDateTime.of(dateTime, ZoneOffset.of("+09:00"));

        assertThat(dateTime.getHour(), equalTo(22));
    }
}