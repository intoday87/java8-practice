import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import static org.hamcrest.CoreMatchers.equalTo;

import static org.junit.Assert.*;

public class NextWorkingDayTest {
    NextWorkingDay nextWorkingDay;

    @Before
    public void setUp() throws Exception {
        nextWorkingDay = new NextWorkingDay();
    }

    @Test
    public void testFriday() throws Exception {
        LocalDate date = LocalDate.of(2017, 10, 6);

        Temporal temporal = nextWorkingDay.adjustInto(date);

        assertThat(DayOfWeek.from(temporal), equalTo( DayOfWeek.MONDAY));
        assertThat(LocalDate.from(temporal).getDayOfMonth(), equalTo(LocalDate.of(2017, 10, 9).getDayOfMonth()));
    }

    @Test
    public void testSaturday() throws Exception {
        LocalDate date = LocalDate.of(2017, 10, 7);

        Temporal temporal = nextWorkingDay.adjustInto(date);

        assertThat(DayOfWeek.from(temporal), equalTo( DayOfWeek.MONDAY));
        assertThat(LocalDate.from(temporal).getDayOfMonth(), equalTo(LocalDate.of(2017, 10, 9).getDayOfMonth()));
    }
}