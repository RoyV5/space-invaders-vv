package nivel1;

import static nivel1.util.Sprites.alienAt;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Alien;

/** Alien.act(int direction): classes on direction + boundary values of the position. */
class AlienActTest {

    @ParameterizedTest(name = "{0} Alien en ({1}, {2}), act({3}) -> getX()={4}, getY()={5}")
    @CsvSource({
            "AA-01, 100, 50, 1, 101, 50",
            "AA-02, 100, 50, -1, 99, 50",
            "AA-03, 100, 50, 0, 100, 50",
            "AA-04, 100, 50, 5, 105, 50",
            "AA-05, 100, 50, -5, 95, 50",
            "AA-06, 100, 50, 100, 200, 50",
            "AA-07, 0, 0, 1, 1, 0",
            "AA-08, 200, 350, -1, 199, 350"
    })
    void actMovesHorizontally(String id, int x, int y, int direction, int expectedX, int expectedY) {
        Alien alien = alienAt(x, y);

        alien.act(direction);

        assertAll(
                () -> assertEquals(expectedX, alien.getX(), id + " getX()"),
                () -> assertEquals(expectedY, alien.getY(), id + " getY()"));
    }

    @Test
    @DisplayName("AA-09 Alien en (100, 50), act(1) dos veces -> getX()=102, getY()=50 (desplazamiento acumulativo)")
    void AA_09_actTwiceAccumulates() {
        Alien alien = alienAt(100, 50);

        alien.act(1);
        alien.act(1);

        assertAll(
                () -> assertEquals(102, alien.getX(), "getX()"),
                () -> assertEquals(50, alien.getY(), "getY()"));
    }
}
