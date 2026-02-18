import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    /** In this test, we have three different assert statements that verify that addFirst works correctly. */
    public void Test() {
        Deque<Integer> da1 = new ArrayDeque<>();
        int[] test1 = new int[] {0, 1, 2, 3, 4, 5};
        assertWithMessage("Empty Array!").that(da1.isEmpty()).isTrue();
        da1.addFirst(test1[0]);
        assertWithMessage("Should be 0!").that(da1.get(0)).isEqualTo(0);
        da1.addFirst(test1[3]);
        da1.addFirst(test1[2]);
        da1.addFirst(test1[1]);
        System.out.println(da1.toList());
        da1.addFirst(test1[5]);
        System.out.println(da1.toList());
        assertWithMessage("Size is 5!").that(da1.size()).isEqualTo(5);
        assertWithMessage("Should be 5!").that(da1.get(0)).isEqualTo(5);
        da1.addFirst(test1[4]);
        System.out.println(da1.toList());
        assertWithMessage("Should be 4!").that(da1.get(0)).isEqualTo(4);
        da1.removeFirst();
        System.out.println(da1.toList());
        assertWithMessage("Should be 5!").that(da1.get(0)).isEqualTo(5);
        assertThat(da1.toList()).containsExactly(5, 1, 2, 3, 0).inOrder();

        da1.addLast(test1[4]);
        System.out.println(da1.toList());
        assertWithMessage("Should be 4!").that(da1.get(5)).isEqualTo(4);
        assertThat(da1.toList()).containsExactly(5, 1, 2, 3, 0, 4).inOrder();
        da1.removeLast();
        assertWithMessage("Should be 0!").that(da1.get(4)).isEqualTo(0);
        assertThat(da1.toList()).containsExactly(5, 1, 2, 3, 0).inOrder();
    }
    @Test
    public void Test2() {
        Deque<Integer> da = new ArrayDeque<>();
        da.addLast(0);
        da.addLast(3);
        da.addLast(0);
        da.addLast(0);
        da.addLast(1);
        da.addLast(2);
        da.addLast(3);
        da.addLast(4);
        da.addLast(5);
        da.addLast(6);
        da.addLast(7);
        da.addLast(0);
        da.addLast(1);
        da.addLast(2);
        da.addLast(3);
        da.addLast(4);
        da.addLast(5);
        System.out.println(da.toList());
        System.out.println(da.get(0));
        da.removeFirst();
        da.removeLast();
        System.out.println(da.toList());
        while (!da.isEmpty()) {
            da.removeLast();
            System.out.println(da.toList());
        }
        System.out.println(da.isEmpty());
    }

    @Test
    public void Test3() {
        Deque<Integer> da2 = new ArrayDeque<>();
        assertWithMessage("It is empty!").that(da2.size()).isEqualTo(0);
        da2.addLast(9);
        da2.addLast(2);
        da2.addLast(3);
        da2.addLast(3);
        assertWithMessage("Size is now 4!").that(da2.size()).isEqualTo(4);
        da2.removeLast();
        assertWithMessage("Size is now 3!").that(da2.size()).isEqualTo(3);
        da2.addLast(0);
        da2.addLast(4);
        assertWithMessage("Size is now 5!").that(da2.size()).isEqualTo(5);
        assertWithMessage("Index too large!").that(da2.get(9)).isNull();
        assertWithMessage("Index can't be negative!").that(da2.get(-1)).isNull();
        assertWithMessage("It is 9!").that(da2.get(0)).isEqualTo(9);
        da2.addFirst(9);
        da2.addFirst(2);
        da2.addFirst(3);
        da2.addFirst(0);
        da2.addFirst(4);
        System.out.println(da2.toList());
        while (!da2.isEmpty()) {
            System.out.println(da2.removeFirst());
            System.out.println(da2.size());
        }
        assertWithMessage("It is empty NOW!").that(da2.size()).isEqualTo(0);
        da2.addLast(9);
        da2.addLast(2);
        da2.addLast(3);
        da2.addLast(0);
        da2.addLast(4);
        da2.addFirst(9);
        da2.addFirst(2);
        da2.addFirst(3);
        da2.addFirst(0);
        da2.addFirst(4);
        System.out.println(da2.toList());
        while (!da2.isEmpty()) {
            da2.removeLast();
        }
        assertWithMessage("It is empty NOW!").that(da2.size()).isEqualTo(0);
        da2.addFirst(9);
        da2.addFirst(2);
        da2.addFirst(3);
        da2.addFirst(0);
        da2.addFirst(4);
        System.out.println(da2.toList());
    }

    @Test
    public void Test4() {
        Deque<Integer> da3 = new ArrayDeque<>();
        for (int i = 1; i <= 40; i++) {
            System.out.println(da3.toList());
            if (i % 2 == 1) {
                da3.addFirst(i);
            }
            if (i % 2 == 0) {
                da3.addLast(i);
            }
            if (i == 6) {
                assertWithMessage("6 is removed!").that(da3.removeLast()).isEqualTo(6);
            }
            if (i % 3 == 0) {
                da3.removeLast();
            }
            if (i % 7 == 0) {
                da3.removeFirst();
            }
        }
    }

    @Test
    public void Test5() {
        Deque<Integer> da4 = new ArrayDeque<>();
        assertWithMessage("It is empty!").that(da4.size()).isEqualTo(0);
        da4.removeFirst();
        assertWithMessage("It is empty!").that(da4.size()).isEqualTo(0);
        da4.removeLast();
        assertWithMessage("It is empty!").that(da4.size()).isEqualTo(0);
    }

    @Test
    public void testIterable() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");
        for (String s : lld1) {
            assertThat(s);
        }
    }

    @Test
    public void testEquals() {
        Deque<String> ad1 = new ArrayDeque<>();
        Deque<String> ad2 = new ArrayDeque<>();
        ad1.addLast("front");
        ad1.addLast("middle");
        ad1.addLast("back");
        ad2.addLast("front");
        ad2.addLast("middle");
        ad2.addLast("back");
        assertThat(ad1).isEqualTo(ad2);
        ad2.removeLast();
        assertThat(ad1).isNotEqualTo(ad2);
    }

    @Test
    public void testToString() {
        Deque<String> ad1 = new ArrayDeque<>();
        ad1.addLast("front");
        ad1.addLast("middle");
        ad1.addLast("back");
        assertThat(ad1.toString()).isEqualTo("[front, middle, back]");
    }

}