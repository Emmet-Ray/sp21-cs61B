package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Graph;

import java.awt.*;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.TreeSet;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public static final int ROOM_WIDTH = 10;
    public static final int ROOM_HEIGHT = 10;

    public static final int ROOM_NUMBER_MAX = 30;
    public static final int ROOM_NUMBER_MIN = 20;

    private Random random = null;

    private TreeSet<Room> existingRooms = new TreeSet<>();

    private enum DIRECTION {LEFT_ABOVE, RIGHT_ABOVE, LEFT_BELOW, RIGHT_BELOW};



    private class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object p) {
            Position position = (Position) p;
            return this.x == position.x && this.y == position.y;
        }

        @Override
        public String toString() {
            String result = "(" + x + ", " + y + ")";
            return result;
        }


    }

    /**
     *  class for rooms & hallways
     *  hallway is a specific type of rooms
     */
    private class Room implements Comparable<Room>{
        // position of anchor i.e. left bottom point of the room
        Position position;
        int width;
        int length;
        boolean hallway;
        boolean vertical; // for hallways, if true, it's vertical, otherwise, it's horizontal.

        Room(Position position, int width, int length, boolean hallway,boolean vertical) {
            this.position = position;
            this.width = width;
            this.length = length;
            this.hallway = hallway;
            this.vertical = vertical;
        }
        /*
        *  decide whether this room is overlapping with existing rooms or not
         */
        public boolean overlap(TreeSet<Room> existingRoom) {
            for (Room room : existingRoom) {
                if (overlapWithRoom(room)) {
                    return true;
                }
            }

            return false;
        }

        /**
         * @Tag : source : https://www.geeksforgeeks.org/find-two-rectangles-overlap/#
         * @param room
         * @return
         */
        private boolean overlapWithRoom(Room room) {
            if (this.position.x >= room.shiftX(room.width) || room.position.x >= this.shiftX(this.width)) {
                return false;
            }
            if (this.position.y >= room.shiftY(room.length) || room.position.y >= this.shiftY(this.length)) {
                return false;
            }
            return true;
        }

        /**
         * @param dx
         * @return x position after shift dx relative to the anchor
         */
        public int shiftX(int dx) {
            return this.position.x + dx;
        }
        // same as shiftX
        public int shiftY(int dy) {
            return this.position.y + dy;
        }

        @Override
        public int compareTo(Room o) {
            if (this.position.x == o.position.x) {
                return this.position.y - o.position.y;
            }
            return this.position.x - o.position.x;
        }

        /**
         *
         * @param room
         * @return (squared) distance between the middle points
         */
        public double distanceTo(Room room) {
            return  Math.pow((this.shiftX(this.width / 2) - room.shiftX(room.width / 2)), 2)
                    + Math.pow((this.shiftY(this.length / 2)- room.shiftY(room.length / 2)), 2);
        }

        public Position middlePoint() {
            Position p = new Position(this.shiftX(this.width / 2), this.shiftY(this.length / 2));
            return p;
        }
    }
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        Engine.fillOut(finalWorldFrame);

        // get seed from input string, for now, input just seed
        String seed = input.substring(1, input.length() - 1) ;
        System.out.println(seed);
        random = new Random(Long.parseLong(seed));

        drawRooms(finalWorldFrame);
        drawHallways(finalWorldFrame);
        return finalWorldFrame;
    }

    /**
     *  todo : need to decide the algorithm to generate hallways that link all rooms
     *          maybe : dog leg algorithm ???
     * @param world
     */
    private void drawHallways(TETile[][] world) {
        Room currentRoom = existingRooms.first();
        Room closest;
        while (!existingRooms.isEmpty()) {
            if (currentRoom.position.x == 27 || currentRoom.position.x == 28) {
                //world[currentRoom.position.x][currentRoom.position.y] = Tileset.FLOWER;
            }
            existingRooms.remove(currentRoom);
            if (!existingRooms.isEmpty()) {
                closest = connectWithClosestRoom(world, currentRoom);
                //System.out.println(currentRoom.position);
                //System.out.println(closest.position);
                currentRoom = closest;
            }
        }
    }

    /**
     *
     * @param world
     * @param currentRoom
     * @return the closest room to currentRoom
     */
    private Room connectWithClosestRoom(TETile[][] world, Room currentRoom) {
        Room closest = findClosest(currentRoom);
        connectWithClosestRoom_Helper(world, currentRoom, closest);
        return closest;
    }

    private Room findClosest(Room currentRoom) {
        Room closest = existingRooms.first();
        double closestDistance = currentRoom.distanceTo(closest);
        for (Room r : existingRooms) {
            if (r.distanceTo(currentRoom) < closestDistance) {
                closest = r;
                closestDistance = r.distanceTo(currentRoom);
            }
        }
        //System.out.println(closestDistance);
        return closest;
    }

    /**
     *  todo :
     *      different kinds of connections, different kind of position relationships
     */
    private void connectWithClosestRoom_Helper(TETile[][] world, Room currentRoom, Room closest) {
        if (verticalHallways(world, currentRoom, closest)) {
            return;
        } else if (horizontalHallways(world, currentRoom, closest)) {
            return;
        } else {
            L_shape_Hallway(world, currentRoom, closest);
        }

    }

    private void L_shape_Hallway(TETile[][] world, Room currentRoom, Room closest) {
        Position currentMiddle = currentRoom.middlePoint();
        Position closestMiddle = closest.middlePoint();
        // current middle point is on the left above direction of the closest middle point
        if (left_above(currentMiddle, closestMiddle)) {
            //System.out.println("left above");
            draw_L_shape(world, currentMiddle, closestMiddle, DIRECTION.LEFT_ABOVE);
        } else if (right_above(currentMiddle, closestMiddle)) {
            //System.out.println("right above");
            draw_L_shape(world, currentMiddle, closestMiddle, DIRECTION.RIGHT_ABOVE);
        } else if (left_below(currentMiddle, closestMiddle)) {
           // System.out.println("left below");
            draw_L_shape(world, closestMiddle, currentMiddle, DIRECTION.RIGHT_ABOVE);
        } else if (right_below(currentMiddle, closestMiddle)) {
            //System.out.println("right below");
            draw_L_shape(world, closestMiddle, currentMiddle, DIRECTION.LEFT_ABOVE);
        }

    }

    private void draw_L_shape(TETile[][] world, Position currentMiddle, Position closestMiddle, DIRECTION direction) {
        Position start, end;

        if (direction == DIRECTION.LEFT_ABOVE) {

            start = new Position(currentMiddle.x + 1, currentMiddle.y - 1);
            end = new Position(closestMiddle.x - 1, closestMiddle.y + 1);
            draw_L_Shape(world, start, end, Tileset.WALL, DIRECTION.LEFT_ABOVE);

            start = new Position(currentMiddle.x + 1, currentMiddle.y);
            end = new Position(closestMiddle.x, closestMiddle.y + 1);
            draw_L_Shape(world, start, end, Tileset.FLOOR, DIRECTION.LEFT_ABOVE);

            start = new Position(currentMiddle.x + 1, currentMiddle.y + 1);
            end = new Position(closestMiddle.x + 1, closestMiddle.y + 1);
            draw_L_Shape(world, start, end, Tileset.WALL, DIRECTION.LEFT_ABOVE);

        } else if (direction == DIRECTION.RIGHT_ABOVE) {
            start = new Position(closestMiddle.x + 1, closestMiddle.y + 1);
            end = new Position(currentMiddle.x - 1, currentMiddle.y - 1);
            draw_L_Shape(world, start, end, Tileset.WALL, DIRECTION.RIGHT_ABOVE);

            start = new Position(closestMiddle.x, closestMiddle.y + 1);
            end = new Position(currentMiddle.x - 1, currentMiddle.y);
            draw_L_Shape(world, start, end, Tileset.FLOOR, DIRECTION.RIGHT_ABOVE);

            start = new Position(closestMiddle.x - 1, closestMiddle.y + 1);
            end = new Position(currentMiddle.x - 1, currentMiddle.y + 1);
            draw_L_Shape(world, start, end, Tileset.WALL, DIRECTION.RIGHT_ABOVE);
        }
    }

    private void draw_L_Shape(TETile[][] world, Position start, Position end, TETile tile, DIRECTION direction) {
        if (direction == DIRECTION.LEFT_ABOVE) {
            Position horizontal_end = new Position(end.x + 1,  start.y);
            draw_L_Shape_Horizontal(world, start, horizontal_end, tile);

            Position vertical_start = end;
            Position vertical_end = new Position(end.x, start.y + 1);
            draw_L_Shape_Vertical(world, vertical_start, vertical_end, tile);
        } else if (direction == DIRECTION.RIGHT_ABOVE) {
            Position vertical_end = new Position(start.x, end.y + 1);
            draw_L_Shape_Vertical(world, start, vertical_end, tile);

            Position horizontal_start = new Position(start.x, end.y);
            draw_L_Shape_Horizontal(world, horizontal_start, end, tile);
        }
    }
    private void draw_L_Shape_Horizontal(TETile[][] world, Position start, Position end, TETile tile) {
        int y = start.y;
        for (int i = start.x; i < end.x; i++) {
            if (world[i][y].equals(Tileset.FLOOR)) {
                continue;
            }
            world[i][y] = tile;
        }
    }
    private void draw_L_Shape_Vertical(TETile[][] world, Position start, Position end, TETile tile) {
        int x = start.x;
        for (int i = start.y; i < end.y; i++) {
            if (world[x][i].equals(Tileset.FLOOR)) {
                continue;
            }
            world[x][i] = tile;
        }
    }
    private boolean left_above(Position current, Position closest) {
        return current.x < closest.x && current.y > closest.y;
    }
    private boolean right_above(Position current, Position closest) {
        return current.x > closest.x && current.y > closest.y;
    }
    private boolean left_below(Position current, Position closest) {
        return current.x < closest.x && current.y < closest.y;
    }
    private boolean right_below(Position current, Position closest) {
        return current.x > closest.x && current.y < closest.y;
    }
    private boolean horizontalHallways(TETile[][] world, Room currentRoom, Room closest) {
        int currentYPosition = currentRoom.shiftY(currentRoom.length / 2);
        if (withinBounds(currentYPosition, closest.shiftY(0), closest.shiftY(closest.length - 1))) {
            horizontalHallways_Helper(world, currentRoom, closest, currentYPosition);
            return true;
        }
        int closestYPosition = closest.shiftY(closest.length / 2);
        if (withinBounds(closestYPosition, currentRoom.shiftY(0), currentRoom.shiftY(currentRoom.length - 1))) {
            horizontalHallways_Helper(world, currentRoom, closest, closestYPosition);
            return true;
        }
        return false;
    }

    private void horizontalHallways_Helper(TETile[][] world, Room currentRoom, Room closest, int yPosition) {
        //System.out.println("in horizontal hallway helper");
        // closest is in the left of the current room
        if (closest.shiftX(closest.width - 1) < currentRoom.shiftX(0)) {
            //System.out.println("closest is on the left of the current room");
            Position start = new Position(closest.shiftX(closest.width - 1), yPosition);
            Position end = new Position(currentRoom.shiftX(0), yPosition);
            drawHorizontalHallway(world, start, end);
        } else {
            //System.out.println("closest is on the right of the current room");
            Position start = new Position(currentRoom.shiftX(currentRoom.width - 1), yPosition);
            Position end = new Position(closest.shiftX(0), yPosition);
            drawHorizontalHallway(world, start, end);
        }
    }

    private void drawHorizontalHallway(TETile[][] world, Position start, Position end) {
        int yPosition = start.y;
        for (int i = start.x; i <= end.x; i++) {
            if (!world[i][yPosition - 1].equals(Tileset.FLOOR)) {
                world[i][yPosition - 1] = Tileset.WALL;
            }
            world[i][yPosition] = Tileset.FLOOR;
            if (!world[i][yPosition + 1].equals(Tileset.FLOOR)) {
                world[i][yPosition + 1] = Tileset.WALL;
            }
        }
    }
    private boolean verticalHallways(TETile[][] world, Room currentRoom, Room closest) {
        int currenXPosition = currentRoom.shiftX(currentRoom.width / 2);
        if (xPositionWithinBounds(currenXPosition, closest)) {
            verticalHallway_Helper(world, currentRoom, closest, currenXPosition);
            return true;
        }
        int closestXPosition = closest.shiftX(closest.width / 2);
        if (xPositionWithinBounds(closestXPosition, currentRoom)) {
            verticalHallway_Helper(world, currentRoom, closest, closestXPosition);
            return true;
        }
        return false;
    }
    private void verticalHallway_Helper(TETile[][] world, Room currentRoom, Room closest, int xPosition) {
        //System.out.println("in vertical hallway helper");
        if (closest.shiftY(closest.length) <= currentRoom.shiftY(0) ) {
            Position start = new Position(xPosition, closest.shiftY(closest.length - 1));
            Position end = new Position(xPosition, currentRoom.shiftY(0));
            drawVerticalHallWay(world, start, end);
        } else {
            Position start = new Position(xPosition, currentRoom.shiftY(currentRoom.length - 1));
            Position end = new Position(xPosition, closest.shiftY(0));
            drawVerticalHallWay(world, start, end);
        }
    }
    private void drawVerticalHallWay(TETile[][] world, Position start, Position end) {
        int xPosition = start.x;
        for (int i = start.y; i <= end.y; i++) {
            if (!world[xPosition - 1][i].equals(Tileset.FLOOR)) {
                world[xPosition - 1][i] = Tileset.WALL;
            }
            world[xPosition][i] = Tileset.FLOOR;
            if (!world[xPosition + 1][i].equals(Tileset.FLOOR)) {
                world[xPosition + 1][i] = Tileset.WALL;
            }
        }
    }

    /*
       return true if bottom < x < top
     */
    private boolean withinBounds(int x, int bottom, int top) {
        return x > bottom && x < top;
    }
    private boolean xPositionWithinBounds(int x, Room room) {
        if (x > room.position.x && x < (room.position.x + room.width - 1)) {
            return true;
        }
        return false;
    }

    private void drawRooms(TETile[][] finalWorldFrame) {
        int x = RandomUtils.uniform(random, ROOM_NUMBER_MIN, ROOM_NUMBER_MAX);
        //System.out.println("number of room : " + x);
        for (int i = 0; i < x; i++) {
            drawRandomRoom(finalWorldFrame);
        }
    }
    /**
     * draw a random room : random position, random width, random height
     *
     * need to deal with overlapping room
     *
     * @param world
     */
    private void drawRandomRoom(TETile[][] world) {

        Room room = getRandomRoom(false);
        while (room.overlap(existingRooms)) {
            room = getRandomRoom(false);
        }
        existingRooms.add(room);
        drawHelper(world, room);
    }

    private void drawHelper(TETile[][] world, Room room) {
        for (int i = 0; i < room.width; i++) {
            for (int j = 0; j < room.length; j++) {
                if (i % (room.width - 1) == 0 || j % (room.length - 1) == 0) {
                    world[room.shiftX(i)][room.shiftY(j)] = Tileset.WALL;
                } else {
                    world[room.shiftX(i)][room.shiftY(j)] = Tileset.FLOOR;
                }
            }
        }
    }

    /**
     *
     * @return a random position
     */
    private Position getRandomPosition() {
        int x = RandomUtils.uniform(random, WIDTH - ROOM_WIDTH);
        int y = RandomUtils.uniform(random, HEIGHT - ROOM_HEIGHT);
        Position p = new Position(x, y);
        return p;
    }

    private Room getRandomRoom(boolean hallway) {
        Room room = null;
        Position p = getRandomPosition();
        int length = RandomUtils.uniform(random, 3, ROOM_HEIGHT);
        if (hallway) {
            int width = RandomUtils.uniform(random, 3, 4);
            // todo : vertical
            room = new Room(p, width,length, true, false);
        } else {
            int width = RandomUtils.uniform(random, 3, ROOM_WIDTH);
            room = new Room(p, width,length, false, false);
        }
        return room;
    }

    private static void fillOut(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        Engine engine = new Engine();
        String seed = "n123s";
        TETile[][] world = engine.interactWithInputString(seed);
        ter.renderFrame(world);

    }
}
