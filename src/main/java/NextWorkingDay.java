import java.time.DayOfWeek;
import java.time.temporal.*;

public class NextWorkingDay implements TemporalAdjuster{
    @Override
    public Temporal adjustInto(Temporal temporal) {
        if(!temporal.isSupported(ChronoField.DAY_OF_MONTH)) {
            throw new UnsupportedTemporalTypeException("fuck you");
        }

        DayOfWeek from = DayOfWeek.from(temporal);

        if (DayOfWeek.FRIDAY.equals(from)) {
            return temporal.plus(3, ChronoUnit.DAYS);
        } else if (DayOfWeek.SATURDAY.equals(from)) {
            return temporal.plus(2, ChronoUnit.DAYS);
        }

        return temporal.plus(1, ChronoUnit.DAYS);
    }
}
