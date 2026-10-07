package nivel1;

import static nivel1.util.Sprites.shotAt;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import main.Board;
import nivel1.util.BoardFactory;
import nivel1.util.Keys;
import nivel1.util.Reflect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Shot;

/** Board.update(): boundary values on deaths + integration case of the update sequence. */
class UpdateTest {

    private static Board updatedWithDeaths(int deaths) {
        Board board = BoardFactory.stopped();
        board.setDeaths(deaths);
        Reflect.call(board, "update");
        return board;
    }

    @Test
    @DisplayName("UP-01 update() con deaths=0 -> isInGame()=true")
    void UP_01_noDeaths() {
        assertTrue(updatedWithDeaths(0).isInGame(), "isInGame()");
    }

    @Test
    @DisplayName("UP-02 update() con deaths=1 -> isInGame()=true")
    void UP_02_oneDeath() {
        assertTrue(updatedWithDeaths(1).isInGame(), "isInGame()");
    }

    @Test
    @DisplayName("UP-03 update() con deaths=12 -> isInGame()=true")
    void UP_03_twelveDeaths() {
        assertTrue(updatedWithDeaths(12).isInGame(), "isInGame()");
    }

    @Test
    @DisplayName("UP-04 update() con deaths=23 (NUMBER_OF_ALIENS_TO_DESTROY-1) -> isInGame()=true; getMessage() != \"Game won!\"")
    void UP_04_oneBeforeWin() {
        Board board = updatedWithDeaths(23);
        assertAll(
                () -> assertTrue(board.isInGame(), "isInGame()"),
                () -> assertNotEquals("Game won!", board.getMessage(), "getMessage()"));
    }

    @Test
    @DisplayName("UP-05 update() con deaths=24 (NUMBER_OF_ALIENS_TO_DESTROY) -> isInGame()=false; getMessage()=\"Game won!\"")
    void UP_05_win() {
        Board board = updatedWithDeaths(24);
        assertAll(
                () -> assertFalse(board.isInGame(), "isInGame()"),
                () -> assertEquals("Game won!", board.getMessage(), "getMessage()"));
    }

    @Test
    @DisplayName("UP-06 update() con Board nuevo, direction=1, DERECHA pulsada, shot visible en (100,200) -> Player x=181; shot y=196; cada alien x+1 con la misma y")
    void UP_06_runsWholeSequence() {
        Board board = BoardFactory.stopped();
        board.setDirection(1);
        board.getPlayer().keyPressed(Keys.pressed(KeyEvent.VK_RIGHT));
        Shot shot = shotAt(100, 200);
        assertTrue(shot.isVisible(), "precondition: shot isVisible()");
        board.setShot(shot);
        List<int[]> before = new ArrayList<>();
        for (Alien a : board.getAliens()) {
            before.add(new int[] {a.getX(), a.getY()});
        }

        Reflect.call(board, "update");

        List<Executable> checks = new ArrayList<>();
        checks.add(() -> assertEquals(181, board.getPlayer().getX(), "player getX()"));
        checks.add(() -> assertEquals(196, board.getShot().getY(), "shot getY()"));
        List<Alien> aliens = board.getAliens();
        checks.add(() -> assertEquals(before.size(), aliens.size(), "getAliens().size()"));
        for (int i = 0; i < Math.min(before.size(), aliens.size()); i++) {
            int[] old = before.get(i);
            Alien a = aliens.get(i);
            int n = i;
            checks.add(() -> assertEquals(old[0] + 1, a.getX(), "alien " + n + " getX()"));
            checks.add(() -> assertEquals(old[1], a.getY(), "alien " + n + " getY()"));
        }
        assertAll(checks);
    }
}
