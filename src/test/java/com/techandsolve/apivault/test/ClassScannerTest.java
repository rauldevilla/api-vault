package com.techandsolve.apivault.test;

import com.techandsolve.apivault.annotations.SecurityConfiguration;
import com.techandsolve.apivault.util.ClassScanner;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ClassScannerTest {

    @Test
    public void test_findClassesWithAnnotation() throws IOException, ClassNotFoundException {

        ClassScanner scanner = new ClassScanner("com.techandsolve");
        List<Class<?>> classes = scanner.findClassesWithAnnotation(SecurityConfiguration.class);

        assertThat(classes).isNotNull();

        for (Class<?> c : classes) {
            System.out.println("---> Resultado: " + c.getName());
        }

        assertThat(classes.size()).isGreaterThan(0);
    }

}
