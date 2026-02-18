import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

     @Test
     @DisplayName("LinkedListDeque has no fields besides nodes and primitives")
     void noNonTrivialFields() {
         Class<?> nodeClass = NodeChecker.getNodeClass(LinkedListDeque.class, true);
         List<Field> badFields = Reflection.getFields(LinkedListDeque.class)
                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(nodeClass) || f.isSynthetic()))
                 .toList();

         assertWithMessage("Found fields that are not nodes or primitives").that(badFields).isEmpty();
     }

     @Test
     /** In this test, we have three different assert statements that verify that addFirst works correctly. */
     public void addFirstTestBasic() {
         Deque<String> lld1 = new LinkedListDeque<>();

         lld1.addFirst("back"); // after this call we expect: ["back"]
         lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
         lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
     }

     @Test
     /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
      *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
     public void addLastTestBasic() {
         Deque<String> lld1 = new LinkedListDeque<>();

         lld1.addLast("front"); // after this call we expect: ["front"]
         lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
         lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
     }

     @Test
     /** This test performs interspersed addFirst and addLast calls. */
     public void addFirstAndAddLastTest() {
         Deque<Integer> lld1 = new LinkedListDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
         lld1.addLast(0);   // [0]
         lld1.addLast(1);   // [0, 1]
         lld1.addFirst(-1); // [-1, 0, 1]
         lld1.addLast(2);   // [-1, 0, 1, 2]
         lld1.addFirst(-2); // [-2, -1, 0, 1, 2]
         assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
     }

    // Below, you'll write your own tests for LinkedListDeque.

    @Test
    public void removeFirstTestBasic() {
        Deque<String> lld1 = new LinkedListDeque<>();

        lld1.addFirst("back"); // after this call we expect: ["back"]
        lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
        lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
        lld1.removeFirst();
        assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();
        lld1.removeFirst();
        assertThat(lld1.toList()).containsExactly("back").inOrder();
        lld1.removeFirst();
        assertThat(lld1.toList()).containsExactly().inOrder();
    }

    @Test
    public void removeLastTestBasic() {
        Deque<String> lld1 = new LinkedListDeque<>();
        lld1.addFirst("back"); // after this call we expect: ["back"]
        lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
        lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
        lld1.removeLast();
        assertThat(lld1.toList()).containsExactly("front", "middle").inOrder();
    }

    @Test
    public void removeFirstAndRemoveLastTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addLast(2);   // [0, 1, 2]
        lld1.removeFirst();
        assertThat(lld1.toList()).containsExactly(1, 2).inOrder();
        lld1.removeLast();
        assertThat(lld1.toList()).containsExactly(1).inOrder();
        lld1.removeLast();
        assertThat(lld1.toList()).containsExactly().inOrder();
        lld1.removeFirst();
        assertThat(lld1.toList()).containsExactly().inOrder();
    }

    @Test
    public void addAfterRemoveTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.removeFirst();
        lld1.addLast(0);   // [0]
        assertThat(lld1.toList()).containsExactly(0).inOrder();
        lld1.removeFirst();
        lld1.addFirst(0);   // [0]
        assertThat(lld1.toList()).containsExactly(0).inOrder();
        lld1.addLast(1);   // [0, 1]
        lld1.addLast(2);   // [0, 1, 2]
        lld1.removeFirst();
        assertThat(lld1.toList()).containsExactly(1, 2).inOrder();
        lld1.addFirst(-1);
        assertThat(lld1.toList()).containsExactly(-1, 1, 2).inOrder();
        lld1.removeLast();
        assertThat(lld1.toList()).containsExactly(-1, 1).inOrder();
        lld1.addLast(-2);
        assertThat(lld1.toList()).containsExactly(-1, 1, -2).inOrder();
    }

    @Test
    public void sizeTest() {
        Deque<String> lld1 = new LinkedListDeque<>();
        assertWithMessage("Empty list doesn't return size 0").that(lld1.size()).isEqualTo(0);
        lld1.addFirst("front");
        assertWithMessage("Not return size 1 when list has one item").that(lld1.size()).isEqualTo(1);
    }

    @Test
    public void isEmptyTest() {
        Deque<String> lld1 = new LinkedListDeque<>();
        assertWithMessage("Empty list return not empty").that(lld1.isEmpty()).isEqualTo(true);
        lld1.addFirst("front");
        assertWithMessage("Not empty list return empty").that(lld1.isEmpty()).isEqualTo(false);

    }

    @Test
    public void getTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();

        assertWithMessage("get: Empty list doesn't return null").that(lld1.get(0)).isEqualTo(null);
        lld1.addFirst(0); // [0]
        lld1.addFirst(1); // [0, 1]
        lld1.addFirst(2); // [0, 1, 2]

        assertWithMessage("get: Item in index 1 is not 1").that(lld1.get(1)).isEqualTo(1);
        assertWithMessage("get: Item in invalid index 4 is not null").that(lld1.get(4)).isEqualTo(null);
        assertWithMessage("get: Item in invalid index -1 is not null").that(lld1.get(-1)).isEqualTo(null);
    }

    @Test
    public void getRecursiveTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();

        assertWithMessage("getRecursive: Empty list doesn't return null").that(lld1.getRecursive(0)).isEqualTo(null);
        lld1.addFirst(0); // [0]
        lld1.addFirst(1); // [0, 1]
        lld1.addFirst(2); // [0, 1, 2]

        assertWithMessage("getRecursive: Item in index 1 is not 1").that(lld1.getRecursive(1)).isEqualTo(1);
        assertWithMessage("getRecursive: Item in invalid index 4 is not null").that(lld1.getRecursive(4)).isEqualTo(null);
        assertWithMessage("getRecursive: Item in invalid index -1 is not null").that(lld1.getRecursive(-1)).isEqualTo(null);
    }

    @Test
    /** This test performs interspersed addFirst and addLast calls. */
    public void getPointerConsistency() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(0);
        NodeChecker.assertNextPrevConsistency(lld1);
        NodeChecker.assertNextPrevConsistency(lld1);
    }

    @Test
    public void testIterable() {
        Deque<String> lld1 = new LinkedListDeque<>();
        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");
        for (String s : lld1) {
            assertThat(s);
        }
    }

    @Test
    public void testEquals() {
        Deque<String> lld1 = new LinkedListDeque<String>();
        Deque<String> lld2 = new LinkedListDeque<String>();
        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");
        lld2.addLast("front");
        lld2.addLast("middle");
        lld2.addLast("back");
        assertThat(lld1).isEqualTo(lld2);
        lld2.removeLast();
        assertThat(lld1).isNotEqualTo(lld2);
    }

    @Test
    public void testToString() {
        Deque<String> lld1 = new LinkedListDeque<>();
        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");
        assertThat(lld1.toString()).isEqualTo("[front, middle, back]");
        lld1.removeLast();
        assertThat(lld1.toString()).isNotEqualTo("[front, middle, back]");
    }
}