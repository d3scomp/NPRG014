package e14_java;

public class Cat extends Animal implements Furry, FourLegged {
    public Cat() {
        System.out.println("Cat initialized");
    }

    public void eat() {
        System.out.println("Cat.eat");
        Furry.super.eat();
        FourLegged.super.eat();
        super.eat();
    }

}
