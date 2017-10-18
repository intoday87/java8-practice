import jdk.management.resource.ResourceId;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class Main {
    static boolean isGreenApple(Apple apple) {
       return Color.GREEN.equals(apple.getColor());
    }

    static List<Apple> filterApple(List<Apple> inventory, Predicate<Apple> predicate) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory) {
           if (predicate.test(apple)) {
               result.add(apple);
           }
        }

        return result;
    }

    static List<String> list = new ArrayList<>();

    public static void main(String[] args) {
        sumMethodReference();
    }

    private static void anotherMain() {
        List<Apple> apples = new ArrayList<>();
        apples.add(Apple.builder().color(Color.GREEN).weight(5).build());
        apples.add(Apple.builder().color(Color.RED).weight(2).build());

        apples.stream();

        List<Apple> filteredByLambda = filterApple(apples, (Apple a) -> Color.RED.equals(a.getColor()));
        List<Apple> filteredByMethodRef = filterApple(apples, Main::isGreenApple);
        List<Apple> filteredByLib = apples.stream().filter((Apple a) -> Color.GREEN.equals(a.getColor())).collect(Collectors.toList());

        printApples(filteredByLambda);
        printApples(filteredByMethodRef);
        printApples(filteredByLib);

        List<Apple> filteredByAnonymousClass = filterApple(apples, new Predicate<Apple>() {
            @Override
            public boolean test(Apple apple) {
                return apple.getColor().equals(Color.GREEN);
            }
        });

        printApples(filteredByAnonymousClass);

        apples.stream().forEach(a -> System.out.printf("%s %d\n", a.getColor().name(), a.getWeight()));

        apples.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return Integer.valueOf(o1.getWeight()).compareTo(Integer.valueOf(o2.getWeight()));
            }
        });

        apples.stream().forEach(a -> System.out.printf("%s %d\n", a.getColor().name(), a.getWeight()));

        Comparator<Apple> byWeight = Comparator.comparing(Apple::getWeight);
        System.out.printf("compare apples with lambda : %s\n", byWeight.compare(apples.get(0), apples.get(1)));

        IntConsumer intConsumer = (int x) -> { System.out.printf("consume %d integer value\n", x); };
        intConsumer.accept(1);
        ResourceId resourceId = () -> {
            return "leedongho";
        };

        FunctionalInterface f = (int a) -> Integer.valueOf(a);
        f.accept(1).compareTo(f.accept(2));
        f.is(true);
        f.is(false);

        Runnable r = () -> System.out.println("run");
        r.run();

        List<String> filterList = filterList(Arrays.asList("a", "", "c"), s -> !s.isEmpty());
        forEach(filterList, s -> System.out.println(s));

        Function<String, Integer> func = s -> Integer.parseInt(s);
        if (func.apply("1") instanceof Integer) {
            System.out.println("Integer");
        }

        Predicate<Apple> predicateApple = (Apple a) -> a.getColor().equals(Color.GREEN);

        if (list.add("z")) {
            System.out.println("z is in");
        }
        Predicate<String> p = list::add;
        Consumer<String> c = list::add;
        p.test("a");
        list = new ArrayList<>();
        p.test("y");
        c.andThen(s -> list.add(s + 1)).accept("b");
        list.stream().forEach(System.out::println);

        int portNumber = 1337;
        Runnable runnable = () -> System.out.println(portNumber);
        runnable.run();
        //Error:(111, 54) java: local variables referenced from a lambda expression must be final or effectively final
//        portNumber = 1000;

        List<String> str = Arrays.asList("a", "b", "c", "d");
        str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        str.sort(String::compareToIgnoreCase);

        Function<String, Integer> stringToInteger = Integer::parseInt;
        BiPredicate<List<String>, String> contains = List::contains;

        Supplier<List<String>> listSupplier =  ArrayList::new;
        List<String> list = listSupplier.get();

        Function<Integer, FineApple> appleFunction = FineApple::new;
        FineApple fa = appleFunction.apply(10);
        System.out.println(fa.getWeight() == 10);

        BiFunction<Integer, Color, FineApple> fineAppleBiFunction = FineApple::new;
        FineApple bifa = fineAppleBiFunction.apply(20, Color.RED);
        System.out.println(bifa.getWeight() == 20);
        System.out.println(Color.RED.equals(bifa.getColor()));

        BiFunction<Integer, Color, FineApple> fineAppleCreator = FineApple::new;
        List<FineApple> integerList = Arrays.asList(
                fineAppleCreator.apply(10, Color.RED),
                fineAppleCreator.apply(1, Color.GREEN),
                fineAppleCreator.apply(3, Color.BLUE),
                fineAppleCreator.apply(7, Color.RED),
                fineAppleCreator.apply(7, Color.GREEN)
        );
        integerList.sort(Comparator.comparing(FineApple::getWeight).reversed().thenComparing(FineApple::getColor));
        integerList.forEach(i -> System.out.print(i.getWeight() + " / " + i.getColor().name() + " | "));
        System.out.println("");
        testNegate();
        testPredicateOr();
        testFunctionAndThen();
        testFunctionCompose();
    }

    public static void testNegate() {
        Function<Color, FineApple> fineAppleFunction = FineApple::new;
        List<FineApple> fineApples = Arrays.asList(
                fineAppleFunction.apply(Color.BLUE),
                fineAppleFunction.apply(Color.RED),
                fineAppleFunction.apply(Color.GREEN)
        );
        Predicate<FineApple> fineApplePredicate = a -> Color.RED.equals(a.getColor());
        fineApples
                .stream()
                .filter(fineApplePredicate.negate()).collect(Collectors.toList())
                .forEach(a -> System.out.println(a.getColor()));
    }

    public static void testPredicateOr() {
        Map<Integer, Boolean> map = new HashMap<>();
       BiFunction<Integer, Color, FineApple> fineAppleBiFunction = FineApple::new;
        List<FineApple> fineApples = Arrays.asList(
                fineAppleBiFunction.apply(10, Color.RED),
                fineAppleBiFunction.apply(15, Color.GREEN),
                fineAppleBiFunction.apply(1, Color.BLUE),
                fineAppleBiFunction.apply(11, Color.BLUE)
        );
        Predicate<FineApple> fineApplePredicate = a -> Color.BLUE.equals(a.getColor());
        fineApples
                .stream()
                .filter(fineApplePredicate.and(a -> a.getWeight() > 10).or(a -> Color.RED.equals(a.getColor())))
                .forEach(a -> System.out.println(a.getColor().name() + " color and " + a.getWeight() + " weight"));
    }

    public static void testFunctionAndThen() {
        Function<Integer, Integer> f = x -> x + 1;
        int result = f.andThen(x -> x * 2).apply(10);
        System.out.println(result == 22);
    }

    public static void testFunctionCompose() {
        Function<Integer, Double> f = x -> x + 0.1;
        Function<Integer, Integer> f1 = x -> x + 1;
        Double result = f.compose(f1).apply(1);
        System.out.println(result == 2.1);
    }

    public static <T> void forEach(List<T> list, Consumer<T> c) {
        for(T i : list) {
            c.accept(i);
        }
    }

    public static <T> List<T> filterList(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T s : list) {
            if (p.test(s)) {
                result.add(s);
            }
        }
        return result;
    }

    private static void printApples(List<Apple> apples) {
        System.out.println(apples.size() == 1);
        System.out.println(apples.get(0).getColor().name());
    }

    public static void pureFunction() {
        String s = "a";
        Predicate<String> a = (String v) -> {
//            s = "e"; // error occured! not pure function
            return s.equals(v) ? true : false;
        };

        System.out.println(a.test("b"));
    }

    public static void sumMethodReference() {
        Integer[] ints = new Integer[] {1, 2, 3, 4};
        List<Integer> integerList = Arrays.asList(ints);

        Optional<Integer> optional = integerList.stream().reduce(Integer::sum);

        System.out.println(optional.isPresent());
        if (optional.isPresent()) {
            System.out.println(optional.get());
        }
    }
}
