package codes.biscuit.skyblockaddons;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Does reflection. Because I really don't want to deal with mixin or asm right now.
 */
public final class Reflector {
    public static Object getFieldValue(Class<?> clazz, Object classInstance, String field) {
        Object o;
        try {
            Field f = clazz.getDeclaredField(field);
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            o = f.get(classInstance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            o = null;
        }
        return o;
    }

    public static void setFieldValue(Class<?> clazz, Object classInstance, String field, Object value) {
        try {
            Field f = clazz.getDeclaredField(field);
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            f.set(classInstance, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object invoke(Class<?> clazz, Object classInstance, String method, Class<?>[] methodParamTypes, Object[] params) {
        Object o;
        try {
            Method m = clazz.getDeclaredMethod(method, methodParamTypes);
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            o = m.invoke(classInstance, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            o = null;
        }
        return o;
    }
}
