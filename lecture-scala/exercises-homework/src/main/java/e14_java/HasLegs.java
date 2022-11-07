package e14_java;

public interface HasLegs extends WarmBlooded {
    default void eat() {
        System.out.println("HasLegs.eat");
        WarmBlooded.super.eat();
    }
}
