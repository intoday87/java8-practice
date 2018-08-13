import org.junit.Test;

import java.util.function.Function;

public class MethodReferenceTest {
    private String toString(Long value) {
        return value.toString();
    }

    @Test
    public void appropriateFunctionalInterfaceImplementation() {
        Function<Long, String> func = this::toString;
        String applied = func.apply(1L);
    }

}
