public class MyClass2 {

    public static void main(String[] args) {
        A aa = new A();
        A ab = new B();
        A ac = new C();
        B bc = new C();
        C cc = new C();
//        D da = new A();
//        aa.doSomething(ab);
//        aa.doSomething(cc);
        ab.doSomething();
        ((C)aa).doSomething(aa);
//        bc.doSomething(aa);
//        bc.doSomething(aa);
    }
}
