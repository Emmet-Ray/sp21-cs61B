package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {


    private static double frequency(int index) {
        double expoent = (index - 24) / 12;

        return 440.0 * Math.pow(2, expoent);
    }
    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] string = new GuitarString[37];
        for (int i = 0; i < 37; i++) {
            string[i] = new GuitarString(frequency(i));
        }

        char key;
        int index = 0;
        double sample = 0;
        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                key = StdDraw.nextKeyTyped();
                index = keyboard.indexOf(key);
                if (index >=0 && index < 37)
                    string[index].pluck();
            }

            /* compute the superposition of samples */
            if (index >=0 && index < 37) {
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
