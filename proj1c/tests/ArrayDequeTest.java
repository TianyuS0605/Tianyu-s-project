import deque.Deque;
import deque.ArrayDeque;
import deque.LinkedListDeque;
import org.junit.jupiter.api.Test;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
public class ArrayDequeTest {
    @Test
    public void testIterable() {
        Deque<String> ad1 = new ArrayDeque<>();

        ad1.addLast("front");
        ad1.addLast("middle");
        ad1.addLast("back");
        for (String s : ad1) {
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
        assertThat(ad1.toString()).isEqualTo("");
        ad1.addLast("front");
        assertThat(ad1.toString()).isEqualTo("[front]");
        ad1.addLast("middle");
        ad1.addLast("back");
        assertThat(ad1.toString()).isEqualTo("[front, middle, back]");
        ad1.removeLast();
        assertThat(ad1.toString()).isEqualTo("[front, middle]");
    }

    @Test
    public void testEqualToLinkedListDeque() {
        Deque<String> ad1 = new ArrayDeque<>();
        Deque<String> lld1 = new LinkedListDeque<>();
        ad1.addLast("front");
        ad1.addLast("middle");
        ad1.addLast("back");
        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");
        assertThat(ad1).isEqualTo(lld1);
    }
}
