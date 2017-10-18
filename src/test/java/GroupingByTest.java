import com.google.gson.Gson;
import lombok.Getter;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class GroupingByTest {
    private enum Type {
        FISH,
        MEAT,
        OTHER
    }

    @Getter
    private class Dish {
        private Type type;
        private String name;
        private int calories;

        public Dish(Type type, String name, int calories) {
            this.type = type;
            this.name = name;
            this.calories = calories;
        }

        @Override
        public String toString() {
            return "Dish{" +
                    "type=" + type +
                    ", name='" + name + '\'' +
                    ", calories=" + calories +
                    '}';
        }
    }

    private enum CaloricLevel {
        DIET,
        NORMAL,
        FAT
    }

    List<Dish> menues;

    @Before
    public void setUp() throws Exception {
        menues = Arrays.asList(
                new Dish(Type.FISH, "salmon", 100),
                new Dish(Type.FISH, "prawns", 120),
                new Dish(Type.MEAT, "pork", 600),
                new Dish(Type.MEAT, "beef", 700),
                new Dish(Type.MEAT, "chicken", 800),
                new Dish(Type.OTHER, "french fries", 1000),
                new Dish(Type.OTHER, "rice", 600),
                new Dish(Type.OTHER, "season fruit", 700),
                new Dish(Type.OTHER, "pizza", 1200)
        );
    }

    @Test
    public void multiGroupingBy() throws Exception {
        Map<Type, Map<CaloricLevel, List<Dish>>> collect = menues.stream()
                .collect(groupingBy(Dish::getType,
                        groupingBy(dish -> {
                            if (dish.getCalories() <= 400) {
                                return CaloricLevel.DIET;
                            } else if (dish.getCalories() <= 700) {
                                return CaloricLevel.NORMAL;
                            } else {
                                return CaloricLevel.FAT;
                            }
                        })
                ));

        Map<CaloricLevel, List<Dish>> fish = collect.get(Type.FISH);
        assertNotNull(fish.get(CaloricLevel.DIET));
        assertNull(fish.get(CaloricLevel.NORMAL));
        assertNull(fish.get(CaloricLevel.FAT));

        Map<CaloricLevel, List<Dish>> meat = collect.get(Type.MEAT);
        assertNull(meat.get(CaloricLevel.DIET));
        assertThat(meat.get(CaloricLevel.NORMAL).size(), equalTo(2));
        assertThat(meat.get(CaloricLevel.FAT).size(), equalTo(1));

        Map<CaloricLevel, List<Dish>> other = collect.get(Type.OTHER);
        assertNull(other.get(CaloricLevel.DIET));
        assertThat(other.get(CaloricLevel.NORMAL).size(), equalTo(2));
        assertThat(other.get(CaloricLevel.FAT).size(), equalTo(2));
    }

    @Test
    public void duplicatedCollector() throws Exception {
        Map<Type, Dish> collect = menues.stream()
                .collect(
                        groupingBy(
                                Dish::getType,
                                collectingAndThen(
                                        maxBy(Comparator.comparing(Dish::getCalories)),
                                        Optional::get
                                )
                        )
                );

        collect.entrySet().stream()
                .forEach(v -> {
                    System.out.println(v.getKey());
                    System.out.println(v.getValue().getName());
                });

    }

    @Test
    public void testMaxBy() throws Exception {
        Optional<Integer> collect = Stream.iterate(1, v -> v + 1)
                .limit(10)
                .collect(maxBy(Comparator.comparing(v -> v)));

        assertThat(collect.get(), equalTo(10));
    }

    @Test
    public void set() throws Exception {
        Set<CaloricLevel> caloricLevels = new HashSet<>();
        caloricLevels.add(CaloricLevel.DIET);
        caloricLevels.add(CaloricLevel.DIET);

        assertThat(caloricLevels.size(), equalTo(1));
    }

    @Test
    public void partitionedBy() throws Exception {
        Map<Boolean, Map<Boolean, List<Dish>>> collect = menues.stream()
                .collect(
                        partitioningBy(
                                dish -> dish.getCalories() > 500,
                                partitioningBy(
                                        d -> Type.FISH.equals(d.getType())
                                )
                        )
                );

        System.out.println(new Gson().toJson(collect));
    }

    @Test
    public void range() throws Exception {
        IntStream
                .range(0, 2)
                .forEach(value -> System.out.println(value + ", "));
    }

    @Test
    public void whetherPrime() throws Exception {
        Predicate<Integer> predicatePrime = candidate -> IntStream
                .range(2, candidate)
                .noneMatch(value -> candidate % value == 0);

        assertTrue(predicatePrime.test(11));
        assertTrue(predicatePrime.test(2));
        assertTrue(predicatePrime.test(1));
        assertFalse(predicatePrime.test(8));
    }

    @Test
    public void partitionedByPrime() throws Exception {
        Predicate<Integer> predicatePrime = candidate -> IntStream
                .range(2, candidate)
                .noneMatch(value -> candidate % value == 0);
    }

    @Test
    public void customCollector() throws Exception {
        ArrayList<Object> collect = IntStream.range(1, 100)
                .parallel()
                .boxed()
                .collect(Collector.of(
                        ArrayList::new,
                        ArrayList::add,
                        (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        },
                        Function.identity(),
                        Collector.Characteristics.IDENTITY_FINISH,
                        Collector.Characteristics.CONCURRENT
                ));

        System.out.println(new Gson().toJson(collect));
    }

    @Test
    public void noImplCustomCollector() throws Exception {
        ArrayList<Object> collect = IntStream.range(1, 10)
                .parallel()
                .collect(
                        ArrayList::new,
                        (list, value) -> list.add(value),
                        (list1, list2) -> {
                            Gson gson = new Gson();
                            System.out.println("list1 is " + gson.toJson(list1));
                            System.out.println("list2 is " + gson.toJson(list2));
                            list1.addAll(list2);
                        }
                );

        System.out.println(new Gson().toJson(collect));
    }
}