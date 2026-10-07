package nivel1;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Shot;

/** Shot() and Shot(int x, int y): minimal check + arithmetic oracle (x + H_SPACE, y - V_SPACE). */
class ShotConstructorTest {

    @Test
    @DisplayName("SH-01 new Shot() -> objeto no nulo sin lanzar excepción")
    void SH_01_defaultConstructor() {
        Shot shot = assertDoesNotThrow(() -> new Shot());
        assertNotNull(shot);
    }

    @ParameterizedTest(name = "{0} Shot(x={1}, y={2}) -> getX()={3} (x+H_SPACE), getY()={4} (y-V_SPACE)")
    @CsvSource({
            "SX-01, 100, 100, 106, 99",
            "SX-02, 0, 50, 6, 49",
            "SX-03, 50, 1, 56, 0",
            "SX-04, 1, 1, 7, 0",
            "SX-05, 300, 2, 306, 1",
            "SX-06, 179, 280, 185, 279"
    })
    void constructorAddsOffsets(String id, int x, int y, int expectedX, int expectedY) {
        Shot shot = new Shot(x, y);

        assertAll(
                () -> assertEquals(expectedX, shot.getX(), id + " getX()"),
                () -> assertEquals(expectedY, shot.getY(), id + " getY()"));
    }
}
