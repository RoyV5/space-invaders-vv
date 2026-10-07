package nivel1.util;

import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;
import space_invaders.sprites.Shot;

/**
 * Builds sprites at exact coordinates. Positions are forced with setX/setY so a
 * Board-level case does not depend on constructor behaviour tested elsewhere.
 */
public final class Sprites {

    private Sprites() {
    }

    public static Alien alienAt(int x, int y) {
        Alien alien = new Alien(x, y);
        alien.setX(x);
        alien.setY(y);
        return alien;
    }

    /** Shot(x, y) adds offsets, so the exact position is set afterwards. */
    public static Shot shotAt(int x, int y) {
        Shot shot = new Shot(x, y);
        shot.setX(x);
        shot.setY(y);
        return shot;
    }

    public static Player playerAt(int x, int y) {
        Player player = new Player();
        player.setX(x);
        player.setY(y);
        return player;
    }
}
