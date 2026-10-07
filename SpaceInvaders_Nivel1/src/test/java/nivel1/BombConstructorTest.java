package nivel1;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Alien;

/**
 * Alien.Bomb(int x, int y): equivalence classes + reduced boundary values.
 * Bomb is a non-static inner class, so it is built from an owner Alien.
 */
class BombConstructorTest {

    @ParameterizedTest(name = "{0} Bomb(x={1}, y={2}) -> getX()={3}, getY()={4}")
    @CsvSource({
            "BM-01, 100, 100, 100, 100",
            "BM-02, -1, 100, 0, 100",
            "BM-03, 0, 100, 0, 100",
            "BM-04, 358, 100, 358, 100",
            "BM-05, 359, 100, 358, 100",
            "BM-06, 100, -1, 100, 0",
            "BM-07, 100, 0, 100, 0",
            "BM-08, 100, 350, 100, 350",
            "BM-09, 100, 351, 100, 350",
            "BM-10, -1, -1, 0, 0",
            "BM-11, 359, 351, 358, 350",
            "BM-12, -2147483648, -2147483648, 0, 0",
            "BM-13, 2147483647, 2147483647, 358, 350"
    })
    void constructorClampsToScreen(String id, int x, int y, int expectedX, int expectedY) {
        Alien owner = new Alien(100, 100);
        Alien.Bomb bomb = owner.new Bomb(x, y);

        assertAll(
                () -> assertEquals(expectedX, bomb.getX(), id + " getX()"),
                () -> assertEquals(expectedY, bomb.getY(), id + " getY()"));
    }
}
