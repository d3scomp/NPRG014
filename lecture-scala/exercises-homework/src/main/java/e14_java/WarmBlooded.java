package e14_java;

public interface WarmBlooded {
    default void eat() {
        System.out.println("WarmBlooded.eat");
    }
}
