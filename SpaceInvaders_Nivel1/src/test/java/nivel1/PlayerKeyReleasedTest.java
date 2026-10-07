package nivel1;

import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_UP;
import static nivel1.util.Keys.pressed;
import static nivel1.util.Keys.released;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.event.KeyEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;

/** Player.keyReleased(KeyEvent): classes on the key + dx state transitions. Effect observed via act(). */
class PlayerKeyReleasedTest {

    /** Player at x=179 with no previous keys, receives the events in order, then act(); returns getX(). */
    private static int xAfter(KeyEvent... events) {
        Player player = new Player();
        player.setX(179);
        for (KeyEvent e : events) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                player.keyPressed(e);
            } else {
                player.keyReleased(e);
            }
        }
        player.act();
        return player.getX();
    }

    @Test
    @DisplayName("KR-01 keyPressed(IZQ.), keyReleased(IZQ.); luego act() -> getX()=179 (desplazamiento reiniciado a 0)")
    void KR_01_pressAndReleaseLeft() {
        assertEquals(179, xAfter(pressed(VK_LEFT), released(VK_LEFT)), "getX()");
    }

    @Test
    @DisplayName("KR-02 keyPressed(DER.), keyReleased(DER.); luego act() -> getX()=179")
    void KR_02_pressAndReleaseRight() {
        assertEquals(179, xAfter(pressed(VK_RIGHT), released(VK_RIGHT)), "getX()");
    }

    @Test
    @DisplayName("KR-03 keyPressed(IZQ.), keyReleased(ARRIBA); luego act() -> getX()=177 (otra tecla no produce acción)")
    void KR_03_releaseOtherKeyKeepsLeft() {
        assertEquals(177, xAfter(pressed(VK_LEFT), released(VK_UP)), "getX()");
    }

    @Test
    @DisplayName("KR-04 keyPressed(DER.), keyReleased(ESPACIO); luego act() -> getX()=181")
    void KR_04_releaseSpaceKeepsRight() {
        assertEquals(181, xAfter(pressed(VK_RIGHT), released(VK_SPACE)), "getX()");
    }

    @Test
    @DisplayName("KR-05 keyReleased(IZQ.) sin pulsación previa; luego act() -> getX()=179")
    void KR_05_releaseWithoutPress() {
        assertEquals(179, xAfter(released(VK_LEFT)), "getX()");
    }

    @Test
    @DisplayName("KR-06 keyPressed(IZQ.), keyReleased(IZQ.), keyPressed(DER.); luego act() -> getX()=181")
    void KR_06_leftReleasedThenRight() {
        assertEquals(181, xAfter(pressed(VK_LEFT), released(VK_LEFT), pressed(VK_RIGHT)), "getX()");
    }

    @Test
    @DisplayName("KR-07 keyPressed(DER.), keyReleased(DER.), keyPressed(IZQ.); luego act() -> getX()=177")
    void KR_07_rightReleasedThenLeft() {
        assertEquals(177, xAfter(pressed(VK_RIGHT), released(VK_RIGHT), pressed(VK_LEFT)), "getX()");
    }
}
