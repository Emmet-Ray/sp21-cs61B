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
    public void randomizedAddSizeIsEmptyGetTest() {
        ArrayDeque<Integer> array = new ArrayDeque<>();
        int N = 1000;
        int randomChoice;
        int randomValue;
        int cnt = 0;
        for (int i = 0; i < N; i++) {
            //randomChoice = StdRandom.uniform(0, 5);
            randomChoice = 4;
            if (randomChoice == 0) {
                // TODO : not yet implement resize
                //if (array.size() == 8)
                    continue;
                //randomValue = StdRandom.uniform(0, 100);
                //array.addFirst(randomValue);
                //assertEquals((long)randomValue, (long)array.get(0));
                //cnt++;
            } else if (randomChoice == 1) {
                // TODO : not yet implement resize
                if (array.size() == 8)
                    continue;
                //randomValue = StdRandom.uniform(0, 100);
                //array.addLast(randomValue);
                //assertEquals((long)randomValue, (long)array.get(cnt));
                //cnt++;
            } else if (randomChoice == 2) {
                assertEquals(cnt, array.size());
            } else if (randomChoice == 3) {
                // remove first
                randomValue = StdRandom.uniform(0, 100);
                array.addFirst(randomValue);
                int result = array.removeFirst();
                assertEquals(randomValue, result);
            } else if (randomChoice == 4) {
                // remove last
                randomValue = StdRandom.uniform(0, 100);
                array.addLast(randomValue);
                int result = array.removeLast();
                assertEquals(randomValue, result);
            }
        }
    }

}
