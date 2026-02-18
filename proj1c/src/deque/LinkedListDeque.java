package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkedListDeque<T> implements Deque<T> {

    public class Node<T> {
        T item;
        Node prev;
        Node next;

        Node(T itemValue, Node prevNode, Node nextNode) {
            item = itemValue;
            prev = prevNode;
            next = nextNode;
        }

        Node(T itemValue) {
            item = itemValue;
            prev = this;
            next = this;
        }
    }

    private Node sentinel;
    private int size;
    public LinkedListDeque() {
        sentinel = new Node<T>(null);
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        Node newNode = new Node<T>(x, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size++;
    }

    @Override
    public void addLast(T x) {
        Node newNode = new Node<T>(x, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> newList = new ArrayList<T>();
        Node<T> pointer = sentinel.next;
        while (pointer != sentinel) {
            newList.add(pointer.item);
            pointer = pointer.next;
        }
        return newList;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node<T> tempNode = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return tempNode.item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node<T> tempNode = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return tempNode.item;
    }

    @Override
    public T get(int index) {
        if (isEmpty() || index < 0) {
            return null;
        }
        Node<T> pointer = sentinel;
        for (int i = 0; i <= index; i++) {
            pointer = pointer.next;
            if (pointer == sentinel) {
                return null;
            }
        }
        return pointer.item;
    }

    @Override
    public T getRecursive(int index) {
        if (isEmpty() || index < 0) {
            return null;
        }
        return (T) recursiveHelper(index, 0, sentinel.next);
    }


    private T recursiveHelper(int index, int curIndex, Node<T> curPointer) {
        if (curPointer == sentinel) {
            return null;
        }
        if (curIndex == index) {
            return curPointer.item;
        }
        return (T) recursiveHelper(index, curIndex + 1, curPointer.next);
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
        if (!(o instanceof Deque<?>)) {
            return false;
        }
        Deque<T> lld = (Deque<T>) o;
        if (size() != lld.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!get(i).equals(lld.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String result = "";
        if (size == 0 || !(get(0) instanceof String)) {
            return result;
        }
        result = "[" + String.join(", ", (List<String>) toList()) + "]";
        return result;
    }

}
