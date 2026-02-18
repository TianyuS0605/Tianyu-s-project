public class Bank {
    public String userName;
    public static double atmFee = 1;
    private double total = 0;

    public Bank(String name) {
        this.userName = name;
        this.total = 0;
    }

    /*Adds amount to total, then subtracts the
         ATM fee.*/
    public void deposit(double amount) {
        //implementation
        total = total + amount - atmFee;
    }

    /*Subtracts the amount withdrawn AND the ATM
         fee, and returns the new total.*/
    public double withdraw(double amount) {
        //implementation
        total = total - amount - atmFee;
        return total;
    }

    public static void main(String[] args){
        Bank crystal = new Bank("Crystal");
        crystal.deposit(500);
        double newTotal = crystal.withdraw(10);
//        System.out.println(newTotal);
        Bank.atmFee = 2;
        newTotal = crystal.withdraw(5);
        System.out.println(newTotal);
        Bank laksith = new Bank("Laksith");
        laksith.atmFee = 10;
        newTotal = crystal.withdraw(10);
        System.out.println(newTotal);
    }
}
