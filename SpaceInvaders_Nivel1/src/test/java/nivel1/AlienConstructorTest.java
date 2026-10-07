package nivel1;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Alien;

/** Alien(int x, int y): equivalence classes + robust boundary values. */
class AlienConstructorTest {

    @ParameterizedTest(name = "{0} Alien(x={1}, y={2}) -> getX()={3}, getY()={4}")
    @CsvSource({
            "AL-01, 100, 100, 100, 100",
            "AL-02, -1, 100, 0, 100",
            "AL-03, 0, 100, 0, 100",
            "AL-04, 1, 100, 1, 100",
            "AL-05, 357, 100, 357, 100",
            "AL-06, 358, 100, 358, 100",
            "AL-07, 359, 100, 358, 100",
            "AL-08, 100, -1, 100, 0",
            "AL-09, 100, 0, 100, 0",
            "AL-10, 100, 1, 100, 1",
            "AL-11, 100, 349, 100, 349",
            "AL-12, 100, 350, 100, 350",
            "AL-13, 100, 351, 100, 350",
            "AL-14, -1, -1, 0, 0",
            "AL-15, 359, 351, 358, 350",
            "AL-16, -50, 500, 0, 350",
            "AL-17, 500, -50, 358, 0",
            "AL-18, -2147483648, -2147483648, 0, 0",
            "AL-19, 2147483647, 2147483647, 358, 350"
    })
    void constructorClampsToScreen(String id, int x, int y, int expectedX, int expectedY) {
        Alien alien = new Alien(x, y);

        assertAll(
                () -> assertEquals(expectedX, alien.getX(), id + " getX()"),
                () -> assertEquals(expectedY, alien.getY(), id + " getY()"));
    }
}
