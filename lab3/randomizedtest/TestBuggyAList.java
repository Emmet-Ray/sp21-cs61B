package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE


    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> a = new AListNoResizing<>();
        BuggyAList<Integer> b = new BuggyAList<>();

        a.addLast(4);
        b.addLast(4);
        a.addLast(5);
        b.addLast(5);
        a.addLast(6);
        b.addLast(6);

        int aRemove =  a.removeLast();
        int bRemove = b.removeLast();
        assertEquals(aRemove, bRemove);
        aRemove = a.removeLast();
        bRemove = b.removeLast();
        assertEquals(aRemove, bRemove);
        aRemove = a.removeLast();
        bRemove = b.removeLast();
        assertEquals(aRemove, bRemove);
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int bSize = B.size();
                assertEquals(size, bSize);
            } else if (operationNumber ==2 && L.size() != 0) {
                int last = L.getLast();
                int bLast = B.getLast();
                assertEquals(last, bLast);
            } else if (operationNumber ==3 && L.size() != 0) {
                int removeLast = L.removeLast();
                int bRemoveLast = B.removeLast();
                assertEquals(removeLast, bRemoveLast);
            }
        }
    }
}
