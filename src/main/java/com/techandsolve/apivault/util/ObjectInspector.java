package com.techandsolve.apivault.util;

import com.techandsolve.apivault.annotations.AccessValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ObjectInspector {

    private static Logger logger = LoggerFactory.getLogger(ObjectInspector.class);

    private final Object[] EMPTY_OBJECT_ARRAY = {};
    private final Class[] EMPTY_CLASS_ARRAY = {};

    private Object object;

    public static Constructor getDefaultConstructor(Class<?> clazz) {
        for (Constructor c : clazz.getDeclaredConstructors()) {
            if (c.getParameterCount() == 0) {
                return c;
            }
        }
        return null;
    }

    public static Object getNewInstanceUsingDefaultConstrutor(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.
                forName(className,
                        true,
                        Thread.currentThread().getContextClassLoader());
        Constructor defaulConstructor = getDefaultConstructor(clazz);

        if (defaulConstructor == null) {
            throw new NoSuchMethodException("Class " + className + " doesn't have a default constructor");
        }

        return defaulConstructor.newInstance(new Object[]{});
    }

    public ObjectInspector(Object object) {
        this.object = object;
    }

    public Object getObjectAnnotationAttributeValue(Class<? extends Annotation> annotationType, String attributeName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (!this.object.getClass().isAnnotationPresent(annotationType)) {
            return null;
        }
        Annotation annotation = this.object.getClass().getAnnotation(annotationType);
        Method method = annotation.getClass().getMethod(attributeName, EMPTY_CLASS_ARRAY);
        Object attributeValue = method.invoke(annotation, EMPTY_OBJECT_ARRAY);
        return attributeValue;
    }

    public List<Method> getAnnotatedMethods(Class<? extends Annotation> annotationType) {

        Method[] methods = this.object.getClass().getMethods();
        if (methods == null || methods.length == 0) {
            return null;
        }

        List<Method> annotatedMethods = new ArrayList<>();
        for (Method m : methods) {
            if (m.isAnnotationPresent(annotationType)) {
                annotatedMethods.add(m);
            }
        }

        return annotatedMethods.size() > 0 ? annotatedMethods : null;
    }

    public Object invoke(Method method, Object[] params) throws IllegalAccessException, InvocationTargetException {
        return method.invoke(this.object, params != null ? params : EMPTY_OBJECT_ARRAY);
    }
}
