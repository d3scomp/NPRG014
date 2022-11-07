package e14_java;

public interface Furry extends WarmBlooded {
    default void eat() {
        System.out.println("Furry.eat");
        WarmBlooded.super.eat();
    }
}
