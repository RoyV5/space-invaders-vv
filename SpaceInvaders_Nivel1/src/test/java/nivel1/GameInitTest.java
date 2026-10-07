package nivel1;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import main.Board;
import main.Commons;
import nivel1.util.BoardFactory;
import nivel1.util.Reflect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;

/** Board.gameInit(): output against the specification + re-initialisation. */
class GameInitTest {

    /** {(ALIEN_INIT_X + 18*c, ALIEN_INIT_Y + 18*f) | f in 0..3, c in 0..5}. */
    private static Set<String> expectedPositions() {
        Set<String> positions = new HashSet<>();
        for (int f = 0; f < Commons.ALIEN_ROWS; f++) {
            for (int c = 0; c < Commons.ALIEN_COLUMNS; c++) {
                positions.add((Commons.ALIEN_INIT_X + Commons.ALIEN_SEPARATOR * c)
                        + "," + (Commons.ALIEN_INIT_Y + Commons.ALIEN_SEPARATOR * f));
            }
        }
        return positions;
    }

    private static Set<String> positionsOf(List<Alien> aliens) {
        Set<String> positions = new HashSet<>();
        for (Alien a : aliens) {
            positions.add(a.getX() + "," + a.getY());
        }
        return positions;
    }

    private static Board initialised() {
        Board board = BoardFactory.stopped();
        Reflect.call(board, "gameInit");
        return board;
    }

    @Test
    @DisplayName("GI-01 gameInit() -> getAliens().size()=24 (ALIEN_ROWS x ALIEN_COLUMNS = 4x6)")
    void GI_01_alienCount() {
        assertEquals(24, initialised().getAliens().size(), "getAliens().size()");
    }

    @Test
    @DisplayName("GI-02 gameInit() -> posiciones = {(150+18c, 5+18f) | f in 0..3, c in 0..5}; 24 posiciones distintas")
    void GI_02_alienPositions() {
        List<Alien> aliens = initialised().getAliens();
        Set<String> actual = positionsOf(aliens);

        assertAll(
                () -> assertEquals(24, aliens.size(), "getAliens().size()"),
                () -> assertEquals(24, actual.size(), "distinct positions"),
                () -> assertEquals(expectedPositions(), actual, "alien positions"));
    }

    @Test
    @DisplayName("GI-03 gameInit() -> todos los aliens son visibles (isVisible()=true)")
    void GI_03_aliensVisible() {
        List<Alien> aliens = initialised().getAliens();
        for (int i = 0; i < aliens.size(); i++) {
            assertTrue(aliens.get(i).isVisible(), "alien " + i + " isVisible()");
        }
    }

    @Test
    @DisplayName("GI-04 gameInit() -> getPlayer() no nulo, en (179, 280)")
    void GI_04_player() {
        Player player = initialised().getPlayer();
        assertNotNull(player, "getPlayer()");
        assertAll(
                () -> assertEquals(179, player.getX(), "player getX()"),
                () -> assertEquals(280, player.getY(), "player getY()"));
    }

    @Test
    @DisplayName("GI-05 gameInit() -> getShot() no nulo")
    void GI_05_shot() {
        assertNotNull(initialised().getShot(), "getShot()");
    }

    @Test
    @DisplayName("GI-06 gameInit() por segunda vez con 10 aliens eliminados y deaths=10 -> 24 aliens en las posiciones de GI-02")
    void GI_06_reinitialises() {
        Board board = BoardFactory.stopped();
        List<Alien> fewer = new ArrayList<>(board.getAliens());
        fewer.subList(0, 10).clear();
        board.setAliens(fewer);
        board.setDeaths(10);

        Reflect.call(board, "gameInit");

        List<Alien> aliens = board.getAliens();
        assertAll(
                () -> assertEquals(24, aliens.size(), "getAliens().size()"),
                () -> assertEquals(expectedPositions(), positionsOf(aliens), "alien positions"));
    }
}
