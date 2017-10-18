public interface FunctionalInterface {
    Integer accept(int v);
    default boolean is(boolean v) {
        return v == true;
    }
    default boolean not(boolean v) {
        return v == false;
    }
}
