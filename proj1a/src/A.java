public class A {

    public static void doSomething() {
        System.out.println("A");
    }

    public void doSomething(A a) {
        System.out.println("Aa");
    }

    public void doSomething(B b) {
        System.out.println("Ab");
    }
}
