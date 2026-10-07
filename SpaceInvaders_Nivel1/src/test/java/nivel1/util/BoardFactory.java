package nivel1.util;

import main.Board;

/** Creates a Board whose Swing timer cannot change state during a test. */
public final class BoardFactory {

    private BoardFactory() {
    }

    public static Board stopped() {
        Board board = new Board();
        board.getTimer().stop();
        return board;
    }
}
