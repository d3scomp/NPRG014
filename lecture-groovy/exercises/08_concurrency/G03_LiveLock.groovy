class Spoon {
    private volatile Diner owner;

    Spoon(Diner owner) { this.owner = owner; }

    public Diner getOwner() { return owner; }

    public synchronized void setOwner(Diner d) { owner = d; }

    public synchronized void use() {
        System.out.println(owner.name + " is eating.");
    }
}

class Diner {
    private final String name;
    private volatile boolean isHungry = true;

    Diner(String name) {
        this.name = name;
    }

    public void eatWith(Spoon spoon, Diner partner) {
        while (isHungry) {

            // politely give the spoon to the other diner if it's not ours
            if (spoon.getOwner() != this) {
                try { Thread.sleep(1); } catch (InterruptedException ignored) {}
                continue;
            }

            // if partner is hungry, be polite and let them go first
            if (partner.isHungry) {
                System.out.println(name + ": You go first, " + partner.name);
                spoon.setOwner(partner);
                continue;
            }

            // otherwise eat
            spoon.use();
            isHungry = false;
            System.out.println(name + " finished eating!");
            spoon.setOwner(partner);
        }
    }
}


Diner alice = new Diner("Alice");
Diner bob   = new Diner("Bob");
Spoon spoon = new Spoon(alice);

new Thread(() -> alice.eatWith(spoon, bob)).start();
new Thread(() -> bob.eatWith(spoon, alice)).start();


