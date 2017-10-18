import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class FlatMapTest {

    @Test
    public void name() throws Exception {
        IntStream.rangeClosed(1, 5)
                .boxed()
                .flatMap(
                        a -> IntStream.rangeClosed(1, 2).
                                mapToObj(b -> new int[]{a, b})
                )
                .forEach(v -> System.out.println(v[0] + ", " + v[1]));
    }
}