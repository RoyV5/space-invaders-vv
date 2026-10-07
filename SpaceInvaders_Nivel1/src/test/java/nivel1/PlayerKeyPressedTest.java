package nivel1;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_UP;
import static nivel1.util.Keys.pressed;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.event.KeyEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;

/** Player.keyPressed(KeyEvent): classes on the key + event sequences. Effect observed via act(). */
class PlayerKeyPressedTest {

    /** Player at x=179 with no previous keys, receives the events, then act(); returns getX(). */
    private static int xAfter(KeyEvent... events) {
        Player player = new Player();
        player.setX(179);
        for (KeyEvent e : events) {
            player.keyPressed(e);
        }
        player.act();
        return player.getX();
    }

    @Test
    @DisplayName("KP-01 keyPressed(IZQUIERDA); luego act() -> getX()=177")
    void KP_01_left() {
        assertEquals(177, xAfter(pressed(VK_LEFT)), "getX()");
    }

    @Test
    @DisplayName("KP-02 keyPressed(DERECHA); luego act() -> getX()=181")
    void KP_02_right() {
        assertEquals(181, xAfter(pressed(VK_RIGHT)), "getX()");
    }

    @Test
    @DisplayName("KP-03 keyPressed(ARRIBA); luego act() -> getX()=179 (sin acción)")
    void KP_03_up() {
        assertEquals(179, xAfter(pressed(VK_UP)), "getX()");
    }

    @Test
    @DisplayName("KP-04 keyPressed(ABAJO); luego act() -> getX()=179")
    void KP_04_down() {
        assertEquals(179, xAfter(pressed(VK_DOWN)), "getX()");
    }

    @Test
    @DisplayName("KP-05 keyPressed(ESPACIO); luego act() -> getX()=179")
    void KP_05_space() {
        assertEquals(179, xAfter(pressed(VK_SPACE)), "getX()");
    }

    @Test
    @DisplayName("KP-06 keyPressed(letra A); luego act() -> getX()=179")
    void KP_06_letterA() {
        assertEquals(179, xAfter(pressed(VK_A)), "getX()");
    }

    @Test
    @DisplayName("KP-07 keyPressed(IZQUIERDA), keyPressed(DERECHA); luego act() -> getX()=181 (prevalece la última, sin soltar)")
    void KP_07_leftThenRight() {
        assertEquals(181, xAfter(pressed(VK_LEFT), pressed(VK_RIGHT)), "getX()");
    }

    @Test
    @DisplayName("KP-08 keyPressed(DERECHA), keyPressed(IZQUIERDA); luego act() -> getX()=177")
    void KP_08_rightThenLeft() {
        assertEquals(177, xAfter(pressed(VK_RIGHT), pressed(VK_LEFT)), "getX()");
    }

    @Test
    @DisplayName("KP-09 keyPressed(IZQUIERDA) dos veces; luego act() -> getX()=177 (variación de 2 en 2, no acumulada)")
    void KP_09_leftTwice() {
        assertEquals(177, xAfter(pressed(VK_LEFT), pressed(VK_LEFT)), "getX()");
    }

    @Test
    @DisplayName("KP-10 keyPressed(IZQUIERDA), keyPressed(ARRIBA); luego act() -> getX()=177 (otra tecla no cancela el movimiento)")
    void KP_10_leftThenUp() {
        assertEquals(177, xAfter(pressed(VK_LEFT), pressed(VK_UP)), "getX()");
    }
}
