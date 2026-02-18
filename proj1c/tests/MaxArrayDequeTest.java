import deque.MaxArrayDeque;
import org.junit.jupiter.api.*;
import java.util.Comparator;

import static com.google.common.truth.Truth.assertThat;

public class MaxArrayDequeTest {

    class SortByMaximumValue implements Comparator<Integer> {
        @Override
        public int compare(Integer item1, Integer item2) {
            return item1.compareTo(item2);
        }
    }

    class SortItemByAlphabet implements Comparator<String> {
        @Override
        public int compare(String item1, String item2) {
            return item1.compareTo(item2);
        }
    }

    class SortAbsoluteMaximumValue implements Comparator<Integer> {
        @Override
        public int compare(Integer item1, Integer item2) {
            Integer absItem1 = Math.abs(item1);
            return absItem1.compareTo(Math.abs(item2));
        }
    }

    @Test
    public void getMaximumItemTest() {
        MaxArrayDeque<Integer> ad1 = new MaxArrayDeque<>(new SortByMaximumValue());
        ad1.addLast(0);
        ad1.addLast(2);
        ad1.addLast(1);
        assertThat(ad1.max()).isEqualTo(2);
        ad1.addLast(2);
        assertThat(ad1.max()).isEqualTo(2);
        ad1.addLast(-3);
        assertThat(ad1.max()).isEqualTo(2);
        assertThat(ad1.max(new SortAbsoluteMaximumValue())).isEqualTo(-3);
        MaxArrayDeque<String> ad2 = new MaxArrayDeque<>(new SortItemByAlphabet());
        ad2.addLast("front");
        ad2.addLast("middle");
        ad2.addLast("last");
        assertThat(ad2.max()).isEqualTo("middle");
    }
}
