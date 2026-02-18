public class MyClass {

    public static void main(String[] args) {
        System.out.println(Panda.food);                   // Print statement 1
        Panda p = new Panda(1);
        PandaList lst = new PandaList(p);
        lst.rest = new PandaList(p);
        lst.rest.rest = lst;
        p = new Panda(-10);
        PandaList temp = lst;
        lst = new PandaList(p);
        lst.rest = temp;
        lst.rest.rest.rest.item.food = "Bamboo";
        temp.rest.rest.item.happy = true;
        temp.item.food = "shoots";
        lst.rest.rest.rest = lst;
        lst.rest.rest.rest.item.happy = false;
        lst.rest.rest.item.happy = true;
        System.out.println(temp.rest.item.age);            // Print statement 2
        System.out.println(temp.rest.rest.rest.item.food); // Print statement 3
        System.out.println(lst.rest.item.happy);           // Print statement 4
        System.out.println(temp.rest.rest.item.happy);     // Print statement 5
        System.out.println(lst.rest.rest.item.age);        // Print statement 6
    }

}
