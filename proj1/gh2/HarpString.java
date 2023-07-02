package gh2;

import deque.Deque;
import deque.LinkedListDeque;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class HarpString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public HarpString(double frequency) {
        buffer = new LinkedListDeque<>();
        int capacity = (int)Math.round(SR / frequency);
        for (int i = 0; i < capacity; i++) {
            buffer.addFirst(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
        double r;

        for (int i = 0; i < buffer.size(); i++) {
            r = Math.random() - 0.5;
            buffer.removeFirst();
            buffer.addLast(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double front = buffer.removeFirst();
        double first = buffer.get(0);
        double newDouble = ((front + first) / 2) * DECAY;
        newDouble *= -1;
        buffer.addLast(newDouble);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }
    private static double frequency(int index) {
        double expoent = (index - 24) / 12;

        return 440.0 * Math.pow(2, expoent);
    }
    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        HarpString[] string = new HarpString[37];
        for (int i = 0; i < 37; i++) {
            string[i] = new HarpString(frequency(i));
        }

        char key;
        int index = 0;
        double sample = 0;
        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                key = StdDraw.nextKeyTyped();
                index = keyboard.indexOf(key);
                if (index >= 0 && index < 37)
                    string[index].pluck();
            }

            /* compute the superposition of samples */
            if (index >= 0 && index < 37) {
                sample = string[index].sample();
                /* play the sample on standard audio */
                StdAudio.play(sample);
            }


            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < 37; i++) {
                string[i].tic();
            }
        }
    }
}

