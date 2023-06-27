package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        int N = 1000;
        int M = 10000;
        int i, j;
        Stopwatch watch;
        double time;

        SLList<Integer> testList = new SLList<>();

        for (i = 0; i < 7; i++, N *= 2) {

            for (j = 0; j < N; j++)
                testList.addLast(14);

            watch = new Stopwatch();
            for (j = 0 ; j < M; j++)
               testList.addLast(14);

            time = watch.elapsedTime();

            Ns.addLast(N);
            times.addLast(time);
            opCounts.addLast(M);
        }
        printTimingTable(Ns, times, opCounts);
    }

}
