package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO:  mengtaoli
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        int size = this.board.size();
        board.setViewingPerspective(side);
        // only up direction
        for (int col = 0; col < size; col ++) {
            if (perColumn(col))
                changed = true;
        }
        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /**
     *
     * @param col the particular column
     * @return true if the column is changed, otherwise false.
     */
    private boolean perColumn(int col) {
        boolean changed;
        changed = false;
        int size = this.board.size();
        // every column needs an array to log if some tile is a merged result.
        int[] merged = new int[size];

        // operation for every tile but the last one in the column
        for (int row = size - 2; row >= 0; row--) {
            Tile t = this.board.tile(col, row);
            if (t != null) {
                if (moveOneTile(col, row, merged))
                    changed = true;
            }
        }
        return changed;
        
    }

    /**
     * @param col
     * @param row
     * @return operate on the tile(col, row) that is not null
     */
    private boolean moveOneTile(int col, int row, int[] merged) {
        Tile destination, current;
        current = this.board.tile(col, row);

        // find the "destination" (the row) i.e. the one that is not null for the tile in (col, row)
        destination = findDestination(col, row);
        // move to destination position, may change may not change, BUT if not change, it doesn't matter if we return true(changed)
        moveToDestination(destination, current, merged, col);

        return true;
    }

    /**
     *
     * @param col
     * @param row
     * @return return the destination Tile in the col of the tile in (col, row)
     */
    private Tile findDestination(int col, int row) {
        int i;
        int size = this.board.size();
        Tile destination = null;

        for (i = row + 1; i < size; i++) {
            destination = this.board.tile(col, i);
            if (destination != null)
                break;
        }

        return destination;
    }


    private void moveToDestination(Tile destination, Tile current, int[] merged, int col) {
        int size = this.board.size();
        int destinationRow;
        if (destination != null) {
            // merge operation
            destinationRow = destination.row();
            if (current.value() == destination.value()) {
                if (merged[destinationRow] == 0) {
                    merged[destinationRow] = 1;
                    this.board.move(col, destinationRow, current);
                    this.score += 2 * current.value();
                } else {
                    this.board.move(col, destinationRow -1, current);
                }
            } else {
                this.board.move(col, destinationRow -1, current);
            }
        } else {
            this.board.move(col, size - 1, current);
        }
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        int size = b.size();
        int row, col;
        for (row = 0; row < size; row++) {
            for (col = 0; col < size; col++)
                if (b.tile(col, row) == null)
                    return true;
        }
        return false;
    }
    private static boolean exist(Board b, int x) {
        int size = b.size();
        int row, col;
        for (row = 0; row < size; row++) {
            for (col = 0; col < size; col++)
                if (b.tile(col, row) != null && b.tile(col, row).value() == x)
                    return true;
        }
        return false;
    }
    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        return exist(b, MAX_PIECE);
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        if (emptySpaceExists(b))
            return true;
        int size = b.size();
        int row, col;
        for (row = 0; row < size; row++) {
            for (col = 0; col < size; col++) {
                if(helperOfatLeastOneMoveExists(b, row, col))
                    return true;
            }
        }
        return false;
    }

    /**
     *
     * @param b
     * @param row
     * @param col
     * @return If tile(row, col) has at least one neighbourhood that has same value with it, return true; otherwise, return false.
     */
    private static boolean helperOfatLeastOneMoveExists(Board b, int row, int col) {
        int size = b.size();
        int value = b.tile(col, row).value();
        int i, j;

        i = row;
        j = col - 1;
        if (i >=0 && i < size && j >=0 && j < size)
           if (b.tile(j, i).value() == value)
               return true;

        j = col + 1;
        if (i >=0 && i < size && j >=0 && j < size)
            if (b.tile(j, i).value() == value)
                return true;

        i = row - 1;
        j = col;
        if (i >=0 && i < size && j >=0 && j < size)
            if (b.tile(j, i).value() == value)
                return true;

        i = row + 1;
        if (i >=0 && i < size && j >=0 && j < size)
            if (b.tile(j, i).value() == value)
                return true;
       /*
        for(int i = row - 1; i <= row + 1; i++) {
            if(i >= size || i < 0)
                continue;

            for(int j = col - 1; j <= col + 1; j++) {
                if (j >= 0 && j < size) {
                    // exclude tile(row, col)
                    if(!(i == row && j == col) && b.tile(j, i).value() == value)
                        return true;
                }
            }
        }*/
        return false;
    }

    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
