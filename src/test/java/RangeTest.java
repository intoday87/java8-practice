import org.junit.Test;

import java.util.stream.IntStream;

public class RangeTest {
    @Test
    public void rangeClosed() throws Exception {
        IntStream.rangeClosed(1, 5)
                .boxed()
                .flatMap(a ->
                        IntStream.rangeClosed(1, 5)
                                .mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)})
                )
                .filter(v -> v[2] % 1 == 0)
//                .forEach(v -> System.out.println(v[2]));
                .forEach(v -> System.out.println((int)v[0] + ", " + (int)v[1] + ", " + (int)v[2]));
    }
}