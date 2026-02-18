public class Request implements Comparable<Request> {
    public int paymentAmount;
    public boolean vip;

    public Request(int paymentAmount, boolean vip) {
        this.paymentAmount = paymentAmount;
        this.vip = vip;
    }
    @Override
    public int compareTo(Request o) {
        return o.paymentAmount - this.paymentAmount;
    }
}