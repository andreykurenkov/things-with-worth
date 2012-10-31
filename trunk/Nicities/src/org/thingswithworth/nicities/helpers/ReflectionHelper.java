package org.thingswithworth.nicities.helpers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionHelper {

	public static <T> T attemptConstructorCallAndCast(Class<T> attemptOn, Object... params) {
		Object object = attemptConstructorCall(attemptOn, params);
		if (object != null) {
			try {
				return attemptOn.cast(object);
			} catch (ClassCastException e) {
				// TODO: logging?
				e.printStackTrace();
			}
		}
		return null;
	}

	// TODO: null safety and such
	public static Object attemptConstructorCall(Class<?> attemptOn, Object... params) {
		Class<?>[] paramTypes = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			paramTypes[i] = params[i].getClass();
		}
		Constructor<?> constructor = getMatchingConstructor(attemptOn, paramTypes);
		if (constructor != null) {
			try {
				return constructor.newInstance(params);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Constructor<?> getMatchingConstructor(Class<?> loadFrom, Class<?>... constructorParams) {
		Constructor<?>[] constructors = loadFrom.getConstructors();
		for (Constructor<?> constructor : constructors) {
			Class<?>[] params = constructor.getParameterTypes();
			if (params.length == constructorParams.length) {
				boolean valid = true;
				for (int i = 0; i < params.length; i++) {
					if (!params[i].getName().equals(constructorParams[i].getName())) {
						valid = false;
						break;
					}
				}
				if (!valid)
					continue;
				else {
					return constructor;
				}
			}
		}
		return null;
	}
}
