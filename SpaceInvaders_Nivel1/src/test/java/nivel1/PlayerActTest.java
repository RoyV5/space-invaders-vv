package nivel1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;
import nivel1.util.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;

/** Player.act(): partitions of dx x position region + boundary values at the borders. */
class PlayerActTest {

    /** Upper bound for x from assumption S3: BOARD_WIDTH - PLAYER_WIDTH. */
    private static final int MAX_X = 343;

    /** Player at x with heldKey pressed (no key when heldKey is 0). */
    private static Player playerAt(int x, int heldKey) {
        Player player = new Player();
        player.setX(x);
        if (heldKey != 0) {
            player.keyPressed(Keys.pressed(heldKey));
        }
        return player;
    }

    private static void actTimes(Player player, int times) {
        for (int i = 0; i < times; i++) {
            player.act();
        }
    }

    @Test
    @DisplayName("PA-01 x=179, sin tecla pulsada (dx=0), act() -> getX()=179")
    void PA_01_noKey() {
        Player player = playerAt(179, 0);
        player.act();
        assertEquals(179, player.getX(), "getX()");
    }

    @Test
    @DisplayName("PA-02 x=179, IZQUIERDA pulsada (dx=-2), act() -> getX()=177")
    void PA_02_left() {
        Player player = playerAt(179, KeyEvent.VK_LEFT);
        player.act();
        assertEquals(177, player.getX(), "getX()");
    }

    @Test
    @DisplayName("PA-03 x=179, DERECHA pulsada (dx=+2), act() -> getX()=181")
    void PA_03_right() {
        Player player = playerAt(179, KeyEvent.VK_RIGHT);
        player.act();
        assertEquals(181, player.getX(), "getX()");
    }

    @Test
    @DisplayName("PA-04 x=100, DERECHA pulsada, act() x3 -> getX()=106")
    void PA_04_rightThreeTimes() {
        Player player = playerAt(100, KeyEvent.VK_RIGHT);
        actTimes(player, 3);
        assertEquals(106, player.getX(), "getX()");
    }

    @Test
    @DisplayName("PA-05 x=100, IZQUIERDA pulsada, act() x3 -> getX()=94")
    void PA_05_leftThreeTimes() {
        Player player = playerAt(100, KeyEvent.VK_LEFT);
        actTimes(player, 3);
        assertEquals(94, player.getX(), "getX()");
    }

    @Test
    @DisplayName("PA-06 x=2, IZQUIERDA pulsada, act() -> getX() >= 0 (dentro del área)")
    void PA_06_leftNearBorder() {
        Player player = playerAt(2, KeyEvent.VK_LEFT);
        player.act();
        assertTrue(player.getX() >= 0, "expected getX() >= 0 but was " + player.getX());
    }

    @Test
    @DisplayName("PA-07 x=1, IZQUIERDA pulsada, act() -> getX() >= 0")
    void PA_07_leftOnePixelFromBorder() {
        Player player = playerAt(1, KeyEvent.VK_LEFT);
        player.act();
        assertTrue(player.getX() >= 0, "expected getX() >= 0 but was " + player.getX());
    }

    @Test
    @DisplayName("PA-08 x=0, IZQUIERDA pulsada, act() -> getX() >= 0 (no sale por la izquierda)")
    void PA_08_leftAtBorder() {
        Player player = playerAt(0, KeyEvent.VK_LEFT);
        player.act();
        assertTrue(player.getX() >= 0, "expected getX() >= 0 but was " + player.getX());
    }

    @Test
    @DisplayName("PA-09 x=341 (BOARD_WIDTH-PLAYER_WIDTH-2), DERECHA pulsada, act() -> getX() <= 343 (ver S3)")
    void PA_09_rightNearBorder() {
        Player player = playerAt(341, KeyEvent.VK_RIGHT);
        player.act();
        assertTrue(player.getX() <= MAX_X, "expected getX() <= 343 but was " + player.getX());
    }

    @Test
    @DisplayName("PA-10 x=342, DERECHA pulsada, act() -> getX() <= 343")
    void PA_10_rightOnePixelFromBorder() {
        Player player = playerAt(342, KeyEvent.VK_RIGHT);
        player.act();
        assertTrue(player.getX() <= MAX_X, "expected getX() <= 343 but was " + player.getX());
    }

    @Test
    @DisplayName("PA-11 x=343 (BOARD_WIDTH-PLAYER_WIDTH), DERECHA pulsada, act() -> getX() <= 343 (no sale por la derecha)")
    void PA_11_rightAtBorder() {
        Player player = playerAt(343, KeyEvent.VK_RIGHT);
        player.act();
        assertTrue(player.getX() <= MAX_X, "expected getX() <= 343 but was " + player.getX());
    }

    @Test
    @DisplayName("PA-12 x=179, IZQUIERDA mantenida, act() x200 -> en ningún ciclo getX() < 0")
    void PA_12_leftHeld() {
        Player player = playerAt(179, KeyEvent.VK_LEFT);
        for (int i = 1; i <= 200; i++) {
            player.act();
            int x = player.getX();
            assertTrue(x >= 0, "expected getX() >= 0 after call " + i + " but was " + x);
        }
    }

    @Test
    @DisplayName("PA-13 x=179, DERECHA mantenida, act() x200 -> en ningún ciclo getX() > 343")
    void PA_13_rightHeld() {
        Player player = playerAt(179, KeyEvent.VK_RIGHT);
        for (int i = 1; i <= 200; i++) {
            player.act();
            int x = player.getX();
            assertTrue(x <= MAX_X, "expected getX() <= 343 after call " + i + " but was " + x);
        }
    }

    @Test
    @DisplayName("PA-14 x=0, sin tecla pulsada, act() -> getX()=0 (posición límite válida se conserva)")
    void PA_14_stillAtLeftBorder() {
        Player player = playerAt(0, 0);
        player.act();
        assertEquals(0, player.getX(), "getX()");
    }
}
