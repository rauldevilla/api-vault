package com.techandsolve.apivault.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ClassScannerClassLoaderBased {

    private static Logger logger = LoggerFactory.getLogger(ClassScannerClassLoaderBased.class);


    public List<Class<?>> findClassesWithAnnotation(Class<? extends Annotation> annotationClass) throws NoSuchFieldException, IllegalAccessException {

        Field field = ClassLoader.class.getDeclaredField("classes");
        field.setAccessible(true);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<Class<?>> anotatedClasses = new ArrayList<>();

        Vector<Class<?>> classes = (Vector<Class<?>>)field.get(classLoader);

        if (classes == null || classes.size() == 0) {
            throw new RuntimeException("NO CLASSES FOUND UN CLASSLOADER");
        }

        for (int index = 0; index < classes.size(); index++) {
            logger.debug("class -> " + classes.get(index).getName());
            if (classes.get(index).isAnnotationPresent(annotationClass)) {
                anotatedClasses.add(classes.get(index));
            }
        }

        return anotatedClasses.size() > 0 ? anotatedClasses : null;

    }
}
