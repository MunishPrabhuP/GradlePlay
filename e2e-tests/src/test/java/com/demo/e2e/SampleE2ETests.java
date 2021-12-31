package com.demo.e2e;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SampleE2ETests {
    @BeforeClass
    public void setUp() {
        System.out.println("E2E Tests In SetUp");
    }

    @Test
    public void testcase1() {
        System.out.println("E2E Tests In Test Case 1");
    }

    @Test
    public void testcase2() {
        System.out.println("E2E Tests In Test Case 2");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("E2E Tests In Tear Down");
    }
}
