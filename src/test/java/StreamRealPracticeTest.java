import lombok.Getter;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class StreamRealPracticeTest {
    List<Transaction> transactions;

    @Before
    public void setUp() throws Exception {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 700),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
    }

    @Test
    public void filterTransactionWhenOccuredAt2011() throws Exception {
        Stream<Transaction> stream = transactions
                .stream()
                .filter(v -> (v.getYear() == 2011))
                .sorted(Comparator.comparing(Transaction::getValue));

        List<Transaction> transactions = stream.collect(Collectors.toList());

        assertThat("2011년 transaction은 2건 이다.", transactions.size(), equalTo(2));
        assertThat(transactions.get(0).getTrader().getName(), equalTo("Brian"));
        assertThat(transactions.get(1).getTrader().getName(), equalTo("Raoul"));
    }

    @Test
    public void distinctCity() throws Exception {
        List<String> list = transactions.stream()
                .map(v -> v.getTrader().getCity())
                .distinct()
                .collect(Collectors.toList());

        assertThat(list.size(), equalTo(2));
        assertThat(list.stream()
                        .filter(v -> "Cambridge".equals(v))
                        .collect(Collectors.counting()),
                equalTo(1L));
        assertThat(list.stream()
                        .filter(v -> "Milan".equals(v))
                        .collect(Collectors.counting()),
                equalTo(1L));
    }

    @Test
    public void sortNameWorkingInCambridge() throws Exception {
        List<String> list = transactions.stream()
                .map(v -> v.getTrader())
                .filter(v -> "Cambridge".equals(v.getCity()))
                .map(v -> v.getName())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        assertThat(list.size(), equalTo(3));
        assertThat(list.get(0), equalTo("Alan"));
        assertThat(list.get(1), equalTo("Brian"));
        assertThat(list.get(2), equalTo("Raoul"));
    }

    @Test
    public void sortAllOfTradersName() throws Exception {
        List<String> list = transactions.stream()
                .map(v -> v.getTrader().getName())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        assertThat(list.size(), equalTo(4));
        assertThat(list.get(0), equalTo("Alan"));
        assertThat(list.get(1), equalTo("Brian"));
        assertThat(list.get(2), equalTo("Mario"));
        assertThat(list.get(3), equalTo("Raoul"));
    }

    @Test
    public void haveAnyTransactionInMilan() throws Exception {
        boolean anyMatch = transactions.stream()
                .anyMatch(v -> "Milan".equals(v.getTrader().getCity()));

        assertThat(anyMatch, equalTo(true));
    }

    @Test
    public void extractValueLivingInCambridge() throws Exception {
        List<Integer> list = transactions.stream()
                .filter(v -> "Cambridge".equals(v.getTrader().getCity()))
                .map(v -> v.getValue())
                .collect(Collectors.toList());

        assertThat(list.size(), equalTo(4));
        assertTrue(list.contains(950));
        assertTrue(list.contains(1000));
        assertTrue(list.contains(300));
        assertTrue(list.contains(400));
        assertFalse(list.contains(700));
    }

    @Test
    public void maxValue() throws Exception {
        OptionalInt max = transactions.stream()
                .mapToInt(v -> v.getValue())
                .max();

        assertTrue(max.isPresent());
        assertThat(max.getAsInt(), equalTo(1000));
    }

    @Test
    public void maxValueByFolding() throws Exception {
        Optional<Integer> max = transactions.stream()
                .map(v -> v.getValue())
                .reduce((m, v) -> Integer.max(m, v));

        assertTrue(max.isPresent());
        assertThat(max.get(), equalTo(1000));
    }

    @Test
    public void minValue() throws Exception {
        OptionalInt min = transactions.stream()
                .mapToInt(v -> v.getValue())
                .min();

        assertTrue(min.isPresent());
        assertThat(min.getAsInt(), equalTo(300));
    }

    @Test
    public void groupByCurrency() throws Exception {
        Map<Integer, List<Transaction>> collect = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getYear));

        collect.entrySet().stream()
                .forEach(v -> {
                    System.out.println("key " + v.getKey());
                    v.getValue().stream()
                            .forEach(
                                    vv -> System.out.println("value" + vv.getValue())
                            );
                });
    }

    @Test
    public void collectMaxBy() throws Exception {
        Optional<Transaction> max = transactions.stream()
                .collect(Collectors.maxBy(Comparator.comparingInt(Transaction::getValue)));

        assertThat(max.get().getValue(), equalTo(1000));
    }


    @Test
    public void collectMinBy() throws Exception {
        Optional<Transaction> min = transactions.stream()
                .collect(Collectors.minBy(Comparator.comparingInt(Transaction::getValue)));

        assertThat(min.get().getValue(), equalTo(300));
    }

    @Test
    public void testOptional() throws Exception {
        List<Integer> list = new ArrayList<>(1);

        Optional<Integer> max = list.stream().max(Comparator.comparing(v -> v));

        try {
            max.get();
            fail("didn't occur NoSuchElementException");
        } catch (NoSuchElementException e) {
            assertTrue(NoSuchElementException.class.isInstance(e));
            return;
        }

        fail("didn't occur any kind of a exception");
    }

    @Test
    public void joiningByCallingTotoString() throws Exception {
        List<String> list = Arrays.asList("fuck", "fuck1");

        System.out.println(list.stream()
                .collect(Collectors.joining(",")));
    }

    @Test
    public void joining() throws Exception {
        List<String> list = new ArrayList<>();
        list.add(null);

//        try {
            String joined = list.stream()
                    .collect(Collectors.joining());
            assertThat(joined, equalTo("Null"));
//        } catch (RuntimeException e) {
//            assertTrue(true);
//            return;
//        }
//
//        fail("didn't occur any of a exception");
    }

    private class Buffer {
        @Getter
        private StringBuilder stringBuilder;

        public Buffer() {
            this.stringBuilder = new StringBuilder();
        }
    }

    @Test
    public void name() throws Exception {
        CharSequence collect = transactions.stream()
                .map(Transaction::getTrader)
                .collect(Collectors.reducing("", Trader::getName, (i, j) -> i == "" ? j : i + "," + j));

        System.out.println(collect);
    }

    @Test
    public void reduce() throws Exception {
        Stream<Integer> stream = Arrays.asList(1,2,3).stream();

        StringBuilder stringBuilder = stream.parallel().reduce(new StringBuilder(), (StringBuilder s, Integer v) -> {
            s.append(v);
            System.out.println("appended -> " + s.toString());
            return s;
        }, (StringBuilder s1, StringBuilder s2) -> {
            System.out.println("s1 is " + s1);
            System.out.println("s2 is " + s2);
            s1.append(s2.toString());
            return s1;
        });
        System.out.println(stringBuilder);
    }

    @Test
    public void sum() throws Exception {
        Optional<Integer> optional = Arrays.asList(1, 2, 3, 4).stream()
                .reduce(Integer::sum);

//                .collect(Collectors.reducing(0, v -> v, Integer::sum));

        assertThat(optional.get(), equalTo(1 + 2 + 3 + 4));
    }

    @Test
    public void biOperator() throws Exception {
        BiFunction<String, String, String> b = (i, j) -> i + j;
        BinaryOperator<String> o = (i, j) -> i + j;
    }
}