package nivel1;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;

/** Player(): expected value check. */
class PlayerConstructorTest {

    @Test
    @DisplayName("PL-01 new Player() -> getX()=179 (BOARD_WIDTH/2), getY()=280 (GROUND-10) (ver S2)")
    void PL_01_initialPosition() {
        Player player = new Player();

        assertAll(
                () -> assertEquals(179, player.getX(), "getX()"),
                () -> assertEquals(280, player.getY(), "getY()"));
    }

    @Test
    @DisplayName("PL-02 act() justo tras construir -> getX()=179 (desplazamiento inicial 0)")
    void PL_02_noInitialMovement() {
        Player player = new Player();

        player.act();

        assertEquals(179, player.getX(), "getX()");
    }
}
