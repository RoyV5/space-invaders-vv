package nivel1.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Calls private methods of the game classes (e.g. Board.update_shots). */
public final class Reflect {

    private Reflect() {
    }

    /** getDeclaredMethod + setAccessible + invoke, rethrowing the method's own exception. */
    public static Object invoke(Object target, String name, Class<?>[] types, Object... args) {
        try {
            Method m = target.getClass().getDeclaredMethod(name, types);
            m.setAccessible(true);
            return m.invoke(target, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException re) {
                throw re;
            }
            if (cause instanceof Error err) {
                throw err;
            }
            throw new RuntimeException(cause);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot call " + name, e);
        }
    }

    /** Shortcut for a no-arg private method. */
    public static Object call(Object target, String name) {
        return invoke(target, name, new Class<?>[0]);
    }
}
