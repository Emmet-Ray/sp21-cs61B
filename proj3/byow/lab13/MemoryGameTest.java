package byow.lab13;

public class MemoryGameTest {

    public static void main(String[] args) throws InterruptedException {
        MemoryGame game = new MemoryGame(40, 40, 123);

        String test = game.generateRandomString(6);
        //game.flashSequence(test);
        //game.solicitNCharsInput(6);
        game.startGame();
    }
}
