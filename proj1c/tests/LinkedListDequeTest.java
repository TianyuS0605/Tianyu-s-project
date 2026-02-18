import deque.ArrayDeque;
import deque.Deque;
import deque.LinkedListDeque;
import org.junit.jupiter.api.Test;
import static com.google.common.truth.Truth.assertThat;

public class LinkedListDequeTest {
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
    public void testEqualToArrayDeque() {
        Deque<String> lld1 = new LinkedListDeque<>();
        Deque<String> ad1 = new ArrayDeque<>();
        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");
        ad1.addLast("front");
        ad1.addLast("middle");
        ad1.addLast("back");
        assertThat(lld1).isEqualTo(ad1);
    }

    @Test
    public void testToString() {
        Deque<String> lld1 = new LinkedListDeque<>();
        assertThat(lld1.toString()).isEqualTo("");
        lld1.addLast("front");
        assertThat(lld1.toString()).isEqualTo("[front]");
        lld1.addLast("middle");
        lld1.addLast("back");
        assertThat(lld1.toString()).isEqualTo("[front, middle, back]");
        lld1.removeLast();
        assertThat(lld1.toString()).isEqualTo("[front, middle]");
    }
}
