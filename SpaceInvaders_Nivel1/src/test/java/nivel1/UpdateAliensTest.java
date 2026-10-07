package nivel1;

import static nivel1.util.Sprites.alienAt;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import main.Board;
import nivel1.util.BoardFactory;
import nivel1.util.Reflect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;

/**
 * Board.update_aliens(): equivalence classes + boundary values on the borders and
 * the lower limit. At the borders only direction and GO_DOWN descent are asserted (S6).
 */
class UpdateAliensTest {

    /** Runs update_aliens on a board holding only the given aliens, with the given direction. */
    private static Board updated(int direction, Alien... aliens) {
        Board board = BoardFactory.stopped();
        board.setAliens(new ArrayList<>(List.of(aliens)));
        board.setDirection(direction);
        board.setInGame(true);
        Reflect.call(board, "update_aliens");
        return board;
    }

    private static Alien visibleAlien(int x, int y) {
        Alien alien = alienAt(x, y);
        assertTrue(alien.isVisible(), "precondition: alien isVisible()");
        return alien;
    }

    /** Border cases: only the resulting direction and y are asserted. */
    private static void assertBorder(int direction, int x, int expectedDirection, int expectedY) {
        Alien alien = visibleAlien(x, 50);
        Board board = updated(direction, alien);
        assertAll(
                () -> assertEquals(expectedDirection, board.getDirection(), "getDirection()"),
                () -> assertEquals(expectedY, alien.getY(), "alien getY()"));
    }

    @Test
    @DisplayName("UA-01 direction=1; alien visible en (100,50) -> alien en (101,50); direction=1; isInGame()=true")
    void UA_01_moveRight() {
        Alien alien = visibleAlien(100, 50);
        Board board = updated(1, alien);
        assertAll(
                () -> assertEquals(101, alien.getX(), "alien getX()"),
                () -> assertEquals(50, alien.getY(), "alien getY()"),
                () -> assertEquals(1, board.getDirection(), "getDirection()"),
                () -> assertTrue(board.isInGame(), "isInGame()"));
    }

    @Test
    @DisplayName("UA-02 direction=-1; alien visible en (100,50) -> alien en (99,50); direction=-1")
    void UA_02_moveLeft() {
        Alien alien = visibleAlien(100, 50);
        Board board = updated(-1, alien);
        assertAll(
                () -> assertEquals(99, alien.getX(), "alien getX()"),
                () -> assertEquals(50, alien.getY(), "alien getY()"),
                () -> assertEquals(-1, board.getDirection(), "getDirection()"));
    }

    @Test
    @DisplayName("UA-03 direction=1; aliens visibles en (100,50), (118,50), (136,50) -> cada alien avanza 1 en x; y=50")
    void UA_03_allAliensMove() {
        Alien a = visibleAlien(100, 50);
        Alien b = visibleAlien(118, 50);
        Alien c = visibleAlien(136, 50);
        updated(1, a, b, c);
        assertAll(
                () -> assertEquals(101, a.getX(), "alien 1 getX()"),
                () -> assertEquals(119, b.getX(), "alien 2 getX()"),
                () -> assertEquals(137, c.getX(), "alien 3 getX()"),
                () -> assertEquals(50, a.getY(), "alien 1 getY()"),
                () -> assertEquals(50, b.getY(), "alien 2 getY()"),
                () -> assertEquals(50, c.getY(), "alien 3 getY()"));
    }

    @Test
    @DisplayName("UA-04 direction=1; alien invisible (die()) en (100,50) y visible en (118,50) -> invisible x=100; visible x=119")
    void UA_04_invisibleAlienDoesNotMove() {
        Alien dead = alienAt(100, 50);
        dead.die();
        Alien alive = visibleAlien(118, 50);
        updated(1, dead, alive);
        assertAll(
                () -> assertEquals(100, dead.getX(), "invisible alien getX()"),
                () -> assertEquals(119, alive.getX(), "visible alien getX()"));
    }

    @Test
    @DisplayName("UA-05 direction=1; alien en (328,50) (BOARD_WIDTH-BORDER_RIGHT) -> direction=-1; getY()=65 (GO_DOWN=15) (ver S6)")
    void UA_05_rightBorder() {
        assertBorder(1, 328, -1, 65);
    }

    @Test
    @DisplayName("UA-06 direction=1; alien en (326,50) -> direction=1; getY()=50 (aún sin llegar al borde)")
    void UA_06_beforeRightBorder() {
        assertBorder(1, 326, 1, 50);
    }

    @Test
    @DisplayName("UA-07 direction=1; alien en (340,50) -> direction=-1; getY()=65")
    void UA_07_pastRightBorder() {
        assertBorder(1, 340, -1, 65);
    }

    @Test
    @DisplayName("UA-08 direction=-1; alien en (5,50) (BORDER_LEFT) -> direction=1; getY()=65")
    void UA_08_leftBorder() {
        assertBorder(-1, 5, 1, 65);
    }

    @Test
    @DisplayName("UA-09 direction=-1; alien en (7,50) -> direction=-1; getY()=50")
    void UA_09_beforeLeftBorder() {
        assertBorder(-1, 7, -1, 50);
    }

    @Test
    @DisplayName("UA-10 direction=-1; alien en (2,50) -> direction=1; getY()=65")
    void UA_10_pastLeftBorder() {
        assertBorder(-1, 2, 1, 65);
    }

    @Test
    @DisplayName("UA-11 direction=1; aliens en (328,50), (200,50), (100,80) -> direction=-1; todos descienden 15: y=65, 65, 95")
    void UA_11_allAliensGoDown() {
        Alien a = visibleAlien(328, 50);
        Alien b = visibleAlien(200, 50);
        Alien c = visibleAlien(100, 80);
        Board board = updated(1, a, b, c);
        assertAll(
                () -> assertEquals(-1, board.getDirection(), "getDirection()"),
                () -> assertEquals(65, a.getY(), "alien 1 getY()"),
                () -> assertEquals(65, b.getY(), "alien 2 getY()"),
                () -> assertEquals(95, c.getY(), "alien 3 getY()"));
    }

    @Test
    @DisplayName("UA-12 direction=1; alien visible en (100,303) -> isInGame()=false; getMessage()=\"Invasion!\"")
    void UA_12_invasion() {
        Board board = updated(1, visibleAlien(100, 303));
        assertAll(
                () -> assertFalse(board.isInGame(), "isInGame()"),
                () -> assertEquals("Invasion!", board.getMessage(), "getMessage()"));
    }

    @Test
    @DisplayName("UA-13 direction=1; alien visible en (100,302) (GROUND+ALIEN_HEIGHT) -> isInGame()=true (el límite no se supera estrictamente) (*)")
    void UA_13_atGroundLimit() {
        assertTrue(updated(1, visibleAlien(100, 302)).isInGame(), "isInGame()");
    }

    @Test
    @DisplayName("UA-14 direction=1; alien visible en (100,301) -> isInGame()=true")
    void UA_14_aboveGroundLimit() {
        assertTrue(updated(1, visibleAlien(100, 301)).isInGame(), "isInGame()");
    }
}
