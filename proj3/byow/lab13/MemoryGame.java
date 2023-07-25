package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.StdRandom;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();

    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        // Initialize random number generato
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("string length [n] should be bigger than 0.");
        }
        // Generate random string of letters of length n
        String result = "";
        int index;
        for (int i = 0; i < n; i++) {
           index = rand.nextInt(CHARACTERS.length);
           result += CHARACTERS[index];
        }
        return result;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);

        StdDraw.text(5, 39, "Round : " + round);
        StdDraw.text(33, 39, "good job!");
        StdDraw.text(width / 2, height / 2, s);

        StdDraw.show();
        //TODO: If game is not over, display relevant game information at the top of the screen
    }

    public void flashSequence(String letters) throws InterruptedException {
        // Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(letters.substring(i, i + 1));
            breakFor(1000);
            drawFrame("");
            breakFor(500);
        }
    }

    private void breakFor(int time) throws InterruptedException {
        Thread.sleep(time);
    }
    public String solicitNCharsInput(int n) {
        // Read n letters of player input
        char c;
        int count = 0;
        String result = "";
        while (count < n) {
            if (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();
                result += c;
                drawFrame(result);
                count += 1;
            }
        }
        return result;
    }

    public void startGame() throws InterruptedException {
        //TODO: Set any relevant variables before the game starts
        round = 1;
        gameOver = false;
        String randomString;
        String playerInput;
        String roundString;
        //TODO: Establish Engine loop
        while (!gameOver) {
            roundString = "Round : " + round;
            drawFrame(roundString);
            breakFor(1000);

            randomString = generateRandomString(round);
            flashSequence(randomString);
            playerInput = solicitNCharsInput(round);

            if (playerInput.equals(randomString)) {
                String right = "you are right!";
                drawFrame(right);
                breakFor(1000);
                round += 1;
            } else {
                String gameover = "Game Over! You made it to round: " + round;
                drawFrame(gameover);
                breakFor(1000);
                gameOver = true;
            }
        }
    }

}
