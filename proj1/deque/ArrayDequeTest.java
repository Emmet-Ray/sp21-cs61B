package deque;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;

public class ArrayDequeTest {


    @Test
    public void addSizeIsEmptyGetTest() {
        ArrayDeque<Integer> array = new ArrayDeque<>();
        int[] data = new int[]{1, 2, 3, 4};
        assertEquals(0, array.size());
        assertTrue(array.isEmpty());

        array.addFirst(data[0]);
        assertEquals(1, array.size());
        assertFalse(array.isEmpty());
        assertEquals((long)data[0], (long)array.get(0));

        array.addLast(data[1]);
        assertEquals(2, array.size());
        assertFalse(array.isEmpty());
        assertEquals((long)data[1], (long)array.get(1));

    }

    @Test
    public void removeTest() {
        ArrayDeque<Integer> array = new ArrayDeque<>();
        int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        for (int i = 0; i < data.length; i++)
            array.addFirst(data[i]);

        for (int i = 0; i < data.length; i++)
            if (i % 2 == 0) {
                array.removeFirst();
            } else {
                array.removeLast();
            }

    }

    @Test
    public void resizeTest() {
       int N = 100;
       ArrayDeque<Integer> array = new ArrayDeque<>();
       for (int i = 0; i < N; i++) {
            if (i % 2 == 0) {
                array.addFirst(0);
            } else {
                array.addLast(1);
            }
       }
       assertEquals(N, array.size());
    }

    @Test
    public void randomizedAddSizeIsEmptyGetTest() {
        ArrayDeque<Integer> array = new ArrayDeque<>();
        int N = 100000;
        int randomChoice;
        int randomValue;
        int cnt = 0;
        int actual;
        int expected;
        for (int i = 0; i < N; i++) {
            randomChoice = StdRandom.uniform(0, 5);
            if (randomChoice == 0) {
                randomValue = StdRandom.uniform(0, 100);
                array.addFirst(randomValue);
                assertEquals((long)randomValue, (long)array.get(0));
                cnt++;
            } else if (randomChoice == 1) {
                randomValue = StdRandom.uniform(0, 100);
                array.addLast(randomValue);
                assertEquals((long)randomValue, (long)array.get(cnt));
                cnt++;
            } else if (randomChoice == 2) {
                assertEquals(cnt, array.size());
            } else if (randomChoice == 3) {
                if (array.size() != 0) {
                    expected = array.get(0);
                    actual = array.removeFirst();
                    assertEquals(expected, actual);
                    cnt--;
                }
            } else if (randomChoice == 4) {
                if (array.size() != 0) {
                    expected = array.get(array.size() - 1);
                    actual = array.removeLast();
                    assertEquals(expected, actual);
                    cnt--;
                }
            }
        }
    }

    @Test
    public void testIterator() {
        ArrayDeque<Integer> array = new ArrayDeque<>();
        int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        for (int i = 0; i < data.length; i++)
            array.addFirst(data[i]);

        for (int x : array) {
            System.out.println(x);
        }
        array.printDeque();
    }

    @Test
    public void testEquals() {
        ArrayDeque<Integer> array = new ArrayDeque<>();
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        for (int i = 0; i < data.length; i++) {
            array.addFirst(data[i]);
            lld.addFirst(data[i]);
        }

        ArrayDeque<Integer> array2 = new ArrayDeque<>();
        for (int i = 0; i < data.length; i++)
            array2.addFirst(data[i]);

        ArrayDeque<Integer> array3 = new ArrayDeque<>();

        assertTrue("", array.equals(array2));
        assertFalse("", array.equals(array3));

        assertTrue("array deque and linked list deque should be same with same elements", array.equals(lld));
        assertTrue("array deque and linked list deque should be same with same elements", lld.equals(array));
    }

}
