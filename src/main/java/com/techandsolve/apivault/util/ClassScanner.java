package com.techandsolve.apivault.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassScanner {

    private static Logger logger = LoggerFactory.getLogger(ClassScanner.class);

    private String basePackage;
    private String basePath;
    private ClassLoader classLoader;

    public ClassScanner() {
        this("");
    }

    public ClassScanner(String basePackage) {
        this.basePackage = basePackage;
        this.parseBasePath(basePackage);
        loadClassLoader();
    }

    private void parseBasePath(String basePackage) {
        if (basePackage == null) {
            throw new RuntimeException("basePackage is null");
        }
        this.basePath = basePackage.replace('.', '/');
    }

    private void loadClassLoader() {
        this.classLoader = Thread.currentThread().getContextClassLoader();

        if (this.classLoader == null) {
            throw new RuntimeException("CLASS LOADER NOT FOUND");
        }
    }

    private boolean classHasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return clazz.isAnnotationPresent(annotationClass);
    }

    private static String cutFileExtension(String fileName) {
        final int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex < 0) {
            return fileName;
        }
        return fileName.substring(0, lastIndex);
    }

    private static String formatPackageToScann(String packaToScann) {
        return packaToScann != null && packaToScann.trim().length() > 0 ? packaToScann.trim() + "." : "";
    }

    private void searchInResourceForClassesWithAnnotation(Class<? extends Annotation> annotationClass, File resource, String packageToScann, List<Class<?>> outputClasses) throws ClassNotFoundException {
        File base = resource;

        File[] files = base.listFiles();

        if (files == null || files.length == 0) {
            return;
        }

        for (File f : files) {
            if (f.isDirectory()) {
                searchInResourceForClassesWithAnnotation(annotationClass, f, formatPackageToScann(packageToScann) + f.getName(), outputClasses);
            } else if (f.getName().endsWith(".class")) {
                Class<?> clazz = Class.forName(
                                    packageToScann + "." + cutFileExtension(f.getName()),
                                  false,
                                          this.classLoader);
                if (classHasAnnotation(clazz, annotationClass)) {
                    outputClasses.add(clazz);
                }
            }
        }

    }

    public List<Class<?>> findClassesWithAnnotation(Class<? extends Annotation> annotationClass) throws IOException, ClassNotFoundException {
        Enumeration<URL> resources = this.classLoader.getResources(this.basePath);

        if (resources == null) {
            logger.warn("No resources found in classloader");
            return null;
        }

        URL resource = null;
        List<Class<?>> outputClasses = new ArrayList<>();

        if (!resources.hasMoreElements()) {
            logger.warn("Empty resources in classloader");
        }

        while (resources.hasMoreElements()) {
            resource = resources.nextElement();
            if (resource == null) {
                logger.warn("NULL RESOURCE FOUND !!");
                continue;
            }
            this.searchInResourceForClassesWithAnnotation(annotationClass, new File(resource.getFile()), this.basePackage, outputClasses);
        }

        return outputClasses.size() > 0 ? outputClasses : null;
    }

}
