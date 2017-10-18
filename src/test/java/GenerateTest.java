import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.*;

public class GenerateTest {
    @Test
    public void name() throws Exception {
        Stream.generate(Math::random)
                .limit(5)
                .forEach(v -> System.out.println(v));
    }

    @Test
    public void infinityGenerate() throws Exception {
        Stream.generate(() -> 1)
                .sorted()
                .forEach(v -> System.out.println(v));
    }
}