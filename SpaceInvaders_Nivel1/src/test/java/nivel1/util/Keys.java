package nivel1.util;

import java.awt.event.KeyEvent;
import javax.swing.JPanel;

/** Builds the key events passed to Player.keyPressed / keyReleased. */
public final class Keys {

    private Keys() {
    }

    public static KeyEvent pressed(int vk) {
        return new KeyEvent(new JPanel(), KeyEvent.KEY_PRESSED, 0, 0, vk, KeyEvent.CHAR_UNDEFINED);
    }

    public static KeyEvent released(int vk) {
        return new KeyEvent(new JPanel(), KeyEvent.KEY_RELEASED, 0, 0, vk, KeyEvent.CHAR_UNDEFINED);
    }
}
