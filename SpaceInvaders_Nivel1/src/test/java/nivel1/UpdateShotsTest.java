package nivel1;

import static nivel1.util.Sprites.alienAt;
import static nivel1.util.Sprites.shotAt;
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
import space_invaders.sprites.Shot;

/** Board.update_shots(): classes on shot state and collision + boundary values. */
class UpdateShotsTest {

    /** Board holding only the given aliens and shot, deaths=0. */
    private static Board boardWith(Shot shot, Alien... aliens) {
        Board board = BoardFactory.stopped();
        board.setAliens(new ArrayList<>(List.of(aliens)));
        board.setShot(shot);
        board.setDeaths(0);
        return board;
    }

    private static Alien visibleAlien(int x, int y) {
        Alien alien = alienAt(x, y);
        assertTrue(alien.isVisible(), "precondition: alien isVisible()");
        return alien;
    }

    private static Shot visibleShot(int x, int y) {
        Shot shot = shotAt(x, y);
        assertTrue(shot.isVisible(), "precondition: shot isVisible()");
        return shot;
    }

    private static void updateShots(Board board) {
        Reflect.call(board, "update_shots");
    }

    @Test
    @DisplayName("US-01 alien (100,100), disparo visible en (200,200) -> disparo en (200,196); deaths=0")
    void US_01_shotMovesUp() {
        Shot shot = visibleShot(200, 200);
        Board board = boardWith(shot, visibleAlien(100, 100));
        updateShots(board);
        assertAll(
                () -> assertEquals(200, shot.getX(), "shot getX()"),
                () -> assertEquals(196, shot.getY(), "shot getY()"),
                () -> assertEquals(0, board.getDeaths(), "getDeaths()"));
    }

    @Test
    @DisplayName("US-02 alien (100,100), disparo invisible (die()) en (200,200) -> sin moverse: getY()=200; deaths=0")
    void US_02_invisibleShotDoesNotMove() {
        Shot shot = shotAt(200, 200);
        shot.die();
        Board board = boardWith(shot, visibleAlien(100, 100));
        updateShots(board);
        assertAll(
                () -> assertEquals(200, shot.getY(), "shot getY()"),
                () -> assertEquals(0, board.getDeaths(), "getDeaths()"));
    }

    @Test
    @DisplayName("US-03 alien (100,100), disparo visible en (200,5) -> getY()=1 y disparo visible")
    void US_03_shotNearTopStaysVisible() {
        Shot shot = visibleShot(200, 5);
        updateShots(boardWith(shot, visibleAlien(100, 100)));
        assertAll(
                () -> assertEquals(1, shot.getY(), "shot getY()"),
                () -> assertTrue(shot.isVisible(), "shot isVisible()"));
    }

    @Test
    @DisplayName("US-04 alien (100,100), disparo visible en (200,3) -> disparo eliminado (isVisible()=false); deaths=0")
    void US_04_shotLeavesTop() {
        Shot shot = visibleShot(200, 3);
        Board board = boardWith(shot, visibleAlien(100, 100));
        updateShots(board);
        assertAll(
                () -> assertFalse(shot.isVisible(), "shot isVisible()"),
                () -> assertEquals(0, board.getDeaths(), "getDeaths()"));
    }

    private static void assertHit(int shotX, int shotY) {
        Alien alien = visibleAlien(100, 100);
        Board board = boardWith(visibleShot(shotX, shotY), alien);
        updateShots(board);
        assertAll(
                () -> assertTrue(alien.isDying(), "alien isDying()"),
                () -> assertEquals(1, board.getDeaths(), "getDeaths()"));
    }

    private static void assertMiss(Alien alien, Shot shot) {
        Board board = boardWith(shot, alien);
        updateShots(board);
        assertAll(
                () -> assertFalse(alien.isDying(), "alien isDying()"),
                () -> assertEquals(0, board.getDeaths(), "getDeaths()"));
    }

    @Test
    @DisplayName("US-05 alien (100,100), disparo visible en (106,106) -> alien destruido (isDying()=true); deaths=1")
    void US_05_hitCentre() {
        assertHit(106, 106);
    }

    @Test
    @DisplayName("US-06 alien (100,100), disparo visible en (101,106) -> colisión: alien destruido; deaths=1")
    void US_06_hitLeftEdge() {
        assertHit(101, 106);
    }

    @Test
    @DisplayName("US-07 alien (100,100), disparo visible en (111,106) -> colisión: alien destruido; deaths=1")
    void US_07_hitRightEdge() {
        assertHit(111, 106);
    }

    @Test
    @DisplayName("US-08 alien (100,100), disparo visible en (99,106) -> sin colisión; deaths=0")
    void US_08_missLeft() {
        assertMiss(visibleAlien(100, 100), visibleShot(99, 106));
    }

    @Test
    @DisplayName("US-09 alien (100,100), disparo visible en (113,106) -> sin colisión; deaths=0")
    void US_09_missRight() {
        assertMiss(visibleAlien(100, 100), visibleShot(113, 106));
    }

    @Test
    @DisplayName("US-10 alien (100,100), disparo visible en (106,60) -> sin colisión (disparo por encima); deaths=0")
    void US_10_missAbove() {
        assertMiss(visibleAlien(100, 100), visibleShot(106, 60));
    }

    @Test
    @DisplayName("US-11 alien (100,100), disparo visible en (106,160) -> sin colisión (disparo por debajo); deaths=0")
    void US_11_missBelow() {
        assertMiss(visibleAlien(100, 100), visibleShot(106, 160));
    }

    @Test
    @DisplayName("US-12 alien invisible (die()) en (100,100), disparo visible en (106,106) -> deaths=0; alien no destruido")
    void US_12_invisibleAlienNotHit() {
        Alien alien = alienAt(100, 100);
        alien.die();
        assertMiss(alien, visibleShot(106, 106));
    }

    @Test
    @DisplayName("US-13 alien (100,100), disparo invisible en (106,106) -> deaths=0; alien no destruido")
    void US_13_invisibleShotDoesNotHit() {
        Shot shot = shotAt(106, 106);
        shot.die();
        assertMiss(visibleAlien(100, 100), shot);
    }

    @Test
    @DisplayName("US-14 aliens en (100,100) y (118,100), disparo visible en (106,106) -> solo el primero destruido; deaths=1")
    void US_14_onlyFirstAlienHit() {
        Alien first = visibleAlien(100, 100);
        Alien second = visibleAlien(118, 100);
        Board board = boardWith(visibleShot(106, 106), first, second);
        updateShots(board);
        assertAll(
                () -> assertTrue(first.isDying(), "first alien isDying()"),
                () -> assertFalse(second.isDying(), "second alien isDying()"),
                () -> assertEquals(1, board.getDeaths(), "getDeaths()"));
    }

    @Test
    @DisplayName("US-15 alien (100,100), deaths=5, disparo visible en (106,106) -> deaths=6 (contador incrementado en 1)")
    void US_15_deathsIncrementByOne() {
        Board board = boardWith(visibleShot(106, 106), visibleAlien(100, 100));
        board.setDeaths(5);
        updateShots(board);
        assertEquals(6, board.getDeaths(), "getDeaths()");
    }

    @Test
    @DisplayName("US-16 update_shots() dos veces, alien (100,100), disparo visible en (200,200) -> getY()=192 (SHOT_SPEED=4 por ciclo)")
    void US_16_twoCycles() {
        Shot shot = visibleShot(200, 200);
        Board board = boardWith(shot, visibleAlien(100, 100));
        updateShots(board);
        updateShots(board);
        assertEquals(192, shot.getY(), "shot getY()");
    }
}
