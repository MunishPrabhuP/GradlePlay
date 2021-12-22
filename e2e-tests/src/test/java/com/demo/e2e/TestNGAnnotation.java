package com.demo.e2e;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestNGAnnotation {
    @BeforeClass
    public void setUp() {
        System.out.println("In SetUp");
    }

    @Test
    public void testcase1() {
        System.out.println("In Test Case 1");
    }

    @Test
    public void testcase2() {
        System.out.println("In Test Case 2");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("In Tear Down");
    }
}
