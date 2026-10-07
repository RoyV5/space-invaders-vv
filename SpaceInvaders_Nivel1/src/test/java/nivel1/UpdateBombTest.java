package nivel1;

import static nivel1.util.Sprites.alienAt;
import static nivel1.util.Sprites.playerAt;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import main.Board;
import nivel1.util.BoardFactory;
import nivel1.util.Reflect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;

/**
 * Board.update_bomb(): equivalence classes + boundary values + a statistical case.
 * Deterministic cases make the bomb active first, so no random bomb is created.
 */
class UpdateBombTest {

    private Board board;
    private Alien alien;
    private Player player;

    /** Board with a player at (179,280) and one alien at (100,50) whose bomb is active at (bx,by). */
    private Alien.Bomb activeBombAt(int bx, int by) {
        board = BoardFactory.stopped();
        player = playerAt(179, 280);
        board.setPlayer(player);
        alien = alienAt(100, 50);
        board.setAliens(new ArrayList<>(List.of(alien)));
        Alien.Bomb bomb = alien.getBomb();
        bomb.setDestroyed(false);
        bomb.setX(bx);
        bomb.setY(by);
        return bomb;
    }

    private void updateBomb() {
        Reflect.call(board, "update_bomb");
    }

    @Test
    @DisplayName("UB-01 bomba activa en (100,100) -> bomba en (100,101) (BOMB_SPEED=1); no destruida; no se crea otra bomba")
    void UB_01_bombFalls() {
        Alien.Bomb bomb = activeBombAt(100, 100);
        updateBomb();
        assertAll(
                () -> assertSame(bomb, alien.getBomb(), "same bomb instance"),
                () -> assertEquals(100, bomb.getX(), "bomb getX()"),
                () -> assertEquals(101, bomb.getY(), "bomb getY()"),
                () -> assertFalse(bomb.isDestroyed(), "bomb isDestroyed()"));
    }

    @Test
    @DisplayName("UB-02 update_bomb() x5, bomba activa en (100,100) -> bomba en (100,105); no destruida")
    void UB_02_bombFallsFiveTimes() {
        Alien.Bomb bomb = activeBombAt(100, 100);
        for (int i = 0; i < 5; i++) {
            updateBomb();
        }
        assertAll(
                () -> assertEquals(100, bomb.getX(), "bomb getX()"),
                () -> assertEquals(105, bomb.getY(), "bomb getY()"),
                () -> assertFalse(bomb.isDestroyed(), "bomb isDestroyed()"));
    }

    @Test
    @DisplayName("UB-03 bomba activa en (100,283) -> bomba no destruida")
    void UB_03_aboveGround() {
        Alien.Bomb bomb = activeBombAt(100, 283);
        updateBomb();
        assertFalse(bomb.isDestroyed(), "bomb isDestroyed()");
    }

    @Test
    @DisplayName("UB-04 bomba activa en (100,285) (GROUND-BOMB_HEIGHT) -> bomba destruida (isDestroyed()=true)")
    void UB_04_reachesGround() {
        Alien.Bomb bomb = activeBombAt(100, 285);
        updateBomb();
        assertTrue(bomb.isDestroyed(), "bomb isDestroyed()");
    }

    @Test
    @DisplayName("UB-05 bomba activa en (100,290) -> bomba destruida")
    void UB_05_atGround() {
        Alien.Bomb bomb = activeBombAt(100, 290);
        updateBomb();
        assertTrue(bomb.isDestroyed(), "bomb isDestroyed()");
    }

    @Test
    @DisplayName("UB-06 alien invisible (die()) con bomba destruida en (100,100) -> la bomba permanece destruida y en (100,100)")
    void UB_06_invisibleAlienDoesNotBomb() {
        Alien.Bomb bomb = activeBombAt(100, 100);
        bomb.setDestroyed(true);
        alien.die();
        updateBomb();
        assertAll(
                () -> assertTrue(bomb.isDestroyed(), "bomb isDestroyed()"),
                () -> assertEquals(100, bomb.getX(), "bomb getX()"),
                () -> assertEquals(100, bomb.getY(), "bomb getY()"));
    }

    private void assertPlayerHit(int bx, int by) {
        activeBombAt(bx, by);
        assertTrue(player.isVisible(), "precondition: player isVisible()");
        updateBomb();
        assertTrue(player.isDying(), "player isDying()");
    }

    private void assertPlayerNotHit(int bx, int by) {
        activeBombAt(bx, by);
        assertTrue(player.isVisible(), "precondition: player isVisible()");
        updateBomb();
        assertFalse(player.isDying(), "player isDying()");
    }

    @Test
    @DisplayName("UB-07 Player en (179,280) visible; bomba activa en (185,282) -> jugador destruido (isDying()=true)")
    void UB_07_hitCentre() {
        assertPlayerHit(185, 282);
    }

    @Test
    @DisplayName("UB-08 Player en (179,280); bomba activa en (160,282) -> jugador no destruido (bomba a la izquierda)")
    void UB_08_missLeft() {
        assertPlayerNotHit(160, 282);
    }

    @Test
    @DisplayName("UB-09 Player en (179,280); bomba activa en (200,282) -> jugador no destruido (bomba a la derecha)")
    void UB_09_missRight() {
        assertPlayerNotHit(200, 282);
    }

    @Test
    @DisplayName("UB-10 Player en (179,280); bomba activa en (185,200) -> jugador no destruido (bomba por encima)")
    void UB_10_missAbove() {
        assertPlayerNotHit(185, 200);
    }

    @Test
    @DisplayName("UB-11 Player en (179,280); bomba activa en (178,282) -> jugador no destruido (1 px fuera por la izquierda)")
    void UB_11_justOutsideLeft() {
        assertPlayerNotHit(178, 282);
    }

    @Test
    @DisplayName("UB-12 Player en (179,280); bomba activa en (180,282) -> jugador destruido (1 px dentro por la izquierda)")
    void UB_12_justInsideLeft() {
        assertPlayerHit(180, 282);
    }

    @Test
    @DisplayName("UB-13 Player en (179,280); bomba activa en (193,282) -> jugador destruido (1 px dentro por la derecha: 179+PLAYER_WIDTH-1)")
    void UB_13_justInsideRight() {
        assertPlayerHit(193, 282);
    }

    @Test
    @DisplayName("UB-14 Player en (179,280); bomba activa en (195,282) -> jugador no destruido (1 px fuera por la derecha)")
    void UB_14_justOutsideRight() {
        assertPlayerNotHit(195, 282);
    }

    @Test
    @DisplayName("UB-15 update_bomb() hasta 1000 veces, alien visible en (100,50) con bomba destruida -> se crea bomba con getX()=100 y 50 <= getY() <= 51")
    void UB_15_bombIsEventuallyCreated() {
        Alien.Bomb bomb = activeBombAt(0, 0);
        bomb.setDestroyed(true);
        assertTrue(alien.isVisible(), "precondition: alien isVisible()");

        for (int i = 1; i <= 1000; i++) {
            updateBomb();
            Alien.Bomb current = alien.getBomb();
            if (!current.isDestroyed()) {
                int n = i;
                assertAll(
                        () -> assertEquals(100, current.getX(), "bomb getX() at call " + n),
                        () -> assertTrue(current.getY() >= 50 && current.getY() <= 51,
                                "expected 50 <= bomb getY() <= 51 at call " + n + " but was " + current.getY()));
                return;
            }
        }
        fail("expected a bomb to be created within 1000 calls but none was");
    }
}
