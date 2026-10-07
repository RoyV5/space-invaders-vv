package nivel1;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import main.Board;
import nivel1.util.BoardFactory;
import nivel1.util.Reflect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;

/** Checks the workspace can reach the game before running the real cases. */
class SmokeTest {

    @Test
    @DisplayName("Smoke: new Player() is not null")
    void playerCanBeBuilt() {
        assertNotNull(new Player());
    }

    @Test
    @DisplayName("Smoke: a stopped Board has 24 aliens")
    void boardHas24Aliens() {
        assertEquals(24, BoardFactory.stopped().getAliens().size());
    }

    @Test
    @DisplayName("Smoke: reflective update() does not throw")
    void reflectiveUpdateWorks() {
        Board board = BoardFactory.stopped();
        assertDoesNotThrow(() -> Reflect.call(board, "update"));
    }
}
