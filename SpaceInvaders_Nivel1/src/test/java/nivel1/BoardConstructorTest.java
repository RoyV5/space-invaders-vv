package nivel1;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import main.Board;
import nivel1.util.BoardFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;

/** Board(): expected value check on a freshly built board (timer stopped). */
class BoardConstructorTest {

    @Test
    @DisplayName("BC-01 new Board() -> getAliens().size()=24")
    void BC_01_aliens() {
        Board board = BoardFactory.stopped();
        assertEquals(24, board.getAliens().size(), "getAliens().size()");
    }

    @Test
    @DisplayName("BC-02 new Board() -> getPlayer() no nulo, en (179, 280)")
    void BC_02_player() {
        Player player = BoardFactory.stopped().getPlayer();
        assertNotNull(player, "getPlayer()");
        assertAll(
                () -> assertEquals(179, player.getX(), "player getX()"),
                () -> assertEquals(280, player.getY(), "player getY()"));
    }

    @Test
    @DisplayName("BC-03 new Board() -> getShot() no nulo")
    void BC_03_shot() {
        assertNotNull(BoardFactory.stopped().getShot(), "getShot()");
    }

    @Test
    @DisplayName("BC-04 new Board() -> getDeaths()=0")
    void BC_04_deaths() {
        assertEquals(0, BoardFactory.stopped().getDeaths(), "getDeaths()");
    }

    @Test
    @DisplayName("BC-05 new Board() -> isInGame()=true (la partida comienza)")
    void BC_05_inGame() {
        assertTrue(BoardFactory.stopped().isInGame(), "isInGame()");
    }
}
