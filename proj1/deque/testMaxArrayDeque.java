package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;


public class testMaxArrayDeque<type> {
    public class intComparator<type> implements Comparator<type> {

        @Override
        public int compare(type a, type b) {
           return (int)a - (int)b;
        }
    }

    public class Dog {

        private int size;
        private String name;

        private class sizeComparator implements Comparator<Dog> {
            @Override
            public int compare(Dog a, Dog b) {
                return a.size - b.size;
            }
        }
        private class nameComparator implements Comparator<Dog> {
            @Override
            public int compare(Dog a, Dog b) {
               return a.name.compareTo(b.name);
            }
        }
        public Dog(String name, int size) {
            this.size = size;
            this.name = name;
        }

        public  sizeComparator getSizeComparator() {
            return new sizeComparator();
        }
        public nameComparator getNameComparator() {
            return new nameComparator();
        }
    }
    @Test
    public void test() {
       MaxArrayDeque<Integer> maxArray = new MaxArrayDeque<>(new intComparator<>());
        int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        for (int i = 0; i < data.length; i++)
            maxArray.addFirst(data[i]);
        assertEquals((long)8, (long)maxArray.max());
    }

    @Test
    public void testMultipleComparator() {
        MaxArrayDeque<Dog> maxArray = new MaxArrayDeque<>(new intComparator<>());
        Dog a = new Dog("fido", 10);
        Dog b = new Dog("qiqi", 20);
        Dog c = new Dog("lucky", 100);
        maxArray.addFirst(a);
        maxArray.addFirst(b);
        maxArray.addFirst(c);

        assertEquals(c, maxArray.max(a.getSizeComparator()));
        assertEquals(b, maxArray.max(a.getNameComparator()));
    }
}
