package deque;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    public T max() {
        if (size() == 0) {
            return null;
        }
        List<T> list = toList();
        Collections.sort(list, comparator);
        return list.get(list.size() - 1);
    }

    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        List<T> list = toList();
        Collections.sort(list, c);
        return list.get(list.size() - 1);
    }


}
