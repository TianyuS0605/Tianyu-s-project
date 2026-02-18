import java.util.*;


public class Requests implements Iterable<Request> {
    public List<Request> requests;

    public Requests(List<Request> requests) {
        this.requests = requests;
    }

    @Override
    public Iterator<Request> iterator() {
        return new RequestIterator(requests);
    }

    private static class RequestIterator implements Iterator<Request> {

        public int tracker = 0;
        public List<Request> requests;
        public RequestIterator(List<Request> requests) {
            this.requests = requests;
        }

        @Override
        public boolean hasNext() {
            while (tracker < requests.size() && !requests.get(tracker).vip) {
                tracker++;
            }
            return tracker < requests.size();
        }

        @Override
        public Request next() {
            Request nextRequest = requests.get(tracker);
            tracker++;
            return nextRequest;
        }
    }

    public static void main(String[] args) {
        Request Anniyat = new Request(30, false);
        Request Teresa = new Request(10, true);
        Request Kevin = new Request(5, false);
        Request Ali = new Request(15, false);
        List<Request> requests = new ArrayList<>(List.of(Anniyat, Teresa, Kevin, Ali));
        Collections.sort(requests);
        for (Request r: requests) {
            System.out.println(r.paymentAmount);
        }
        Requests queue = new Requests(requests);
        for (Request r : queue) {
            System.out.println(r.paymentAmount);
        }
    }
}