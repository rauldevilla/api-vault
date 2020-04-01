package com.techandsolve.apivault.test;

import com.techandsolve.apivault.annotations.SecurityConfiguration;
import static org.assertj.core.api.Assertions.*;

import com.techandsolve.apivault.util.ClassScanner;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ClassScannerTest {

    @Test
    public void test_findClassesWithAnnotation() throws IOException, ClassNotFoundException {
        ClassScanner scanner = new ClassScanner();
        List<Class<?>> classes = scanner.findClassesWithAnnotation(SecurityConfiguration.class);
        assertThat(classes).isNotNull();
        assertThat(classes.size()).isGreaterThan(0);
    }


}
