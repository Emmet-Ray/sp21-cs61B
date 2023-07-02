package deque;

import java.util.Comparator;

public class MaxArrayDeque<type> extends ArrayDeque<type>{

    Comparator<type> defaultComparator;
    public MaxArrayDeque(Comparator<type> C) {
        defaultComparator = C;
    }

    public type max() {
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            if (defaultComparator.compare(get(i), get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }

        return get(maxIndex);
    }

    public type max(Comparator<type> comparator) {
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            if (comparator.compare(get(i), get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }

        return get(maxIndex);
    }
}
