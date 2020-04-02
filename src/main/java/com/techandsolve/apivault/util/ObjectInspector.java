package com.techandsolve.apivault.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectInspector {

    private static Logger logger = LoggerFactory.getLogger(ObjectInspector.class);

    private final Object[] EMPTY_OBJECT_ARRAY = {};
    private final Class[] EMPTY_CLASS_ARRAY = {};

    private Object object;

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

}
