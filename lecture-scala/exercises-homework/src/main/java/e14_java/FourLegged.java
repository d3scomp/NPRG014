package e14_java;

public interface FourLegged extends HasLegs {
    default void eat() {
        System.out.println("FourLegged.eat");
        HasLegs.super.eat();
    }
}
