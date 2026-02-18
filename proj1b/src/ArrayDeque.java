import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class ArrayDeque<T> implements Deque<T> {

    private T[] items;
    private int size;
    private int front;
    private int next;
    private static int RFACTOR = 2;
    private static double CAPACITY_RATIO = 0.25;

    private static final int FREE_SIZE = 16;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        front = 4;
        next = 4;
    }

    private void resize(int capacity) {
        if (capacity > items.length) {
            expand(capacity);
        } else {
            shrink(capacity);
        }
    }

    private void expand(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int i = front;
        for (int s = 0; s < size; s++) {
            if (i < items.length) {
                a[i] = items[i];
            } else {
                a[i] = items[i - items.length];
            }
            i++;
        }
        items = a;
        next = front + size;
    }

    private void shrink(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int i = front;
        for (int s = 0; s < size; s++) {
            if (i < a.length) {
                a[i] = items[i];
            } else {
                if (i == front) {
                    front -= a.length;
                }
                a[i - a.length] = items[i];
            }
            i++;
        }
        items = a;
        if (front + size > items.length) {
            next = front + size - items.length;
        } else {
            next = front + size;
        }
    }

    @Override
    public void addFirst(T x) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        if (front == 0) {
            front = items.length;
        }
        front -= 1;
        items[front] = x;
        size++;
    }

    @Override
    public void addLast(T x) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        items[next] = x;
        next++;
        if (next == items.length) {
            next = 0;
        }
        size++;
    }

    @Override
    public List toList() {
        List<T> returnList = new ArrayList<>();
        int curr = front;
        int s = 0;
        while (s < size) {
            returnList.add(items[curr]);
            s++;
            curr++;
            if (curr == items.length) {
                curr = 0;
            }
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (!isEmpty()) {
            size -= 1;
        }
        T removeItem = items[front];
        items[front] = null;
        if (front == items.length - 1) {
            front = 0;
        } else {
            front += 1;
        }
        if (size < items.length * CAPACITY_RATIO && items.length > FREE_SIZE) {
            resize(items.length / RFACTOR);
        }
        return removeItem;
    }

    @Override
    public T removeLast() {
        if (!isEmpty()) {
            size -= 1;
        }
        if (next == 0) {
            next = items.length;
        }
        T removeItem = items[next - 1];
        items[next - 1] = null;
        next -= 1;
        if (size < items.length * CAPACITY_RATIO && items.length > FREE_SIZE) {
            resize(items.length / RFACTOR);
        }
        return removeItem;
    }

    @Override
    public T get(int index) {
        if (isEmpty() || index >= size || index < 0) {
            return null;
        }
        int idx = front + index;
        if (idx >= items.length) {
            return items[idx - items.length];
        } else {
            return items[idx];
        }
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<T> it = new Iterator<T>() {
            int curIndex = 0;
            @Override
            public boolean hasNext() {
                return curIndex < size;
            }
            @Override
            public T next() {
                return curIndex == size ? null : get(curIndex++);
            }
        };
        return it;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ArrayDeque<?>)) {
            return false;
        }
        ArrayDeque<T> ad = (ArrayDeque<T>) o;
        if (size() != ad.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (get(i) != ad.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String result = String.join(", ", (List<String>)toList());
        if (result != "") {
            result = "[" + result + "]";
        }
        return result;
    }
}

