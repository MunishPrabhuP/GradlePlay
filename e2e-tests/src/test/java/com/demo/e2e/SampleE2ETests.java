package com.demo.e2e;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SampleE2ETests {
    @BeforeClass
    public void setUp() {
        System.out.println("E2E Tests In SetUp in Separate Branch");
    }

    @Test
    public void testcase1() {
        System.out.println("E2E Tests In Test Case 1 in Separate Branch");
    }

    @Test
    public void testcase2() {
        System.out.println("E2E Tests In Test Case 2 in Separate Branch");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("E2E Tests In Tear Down in Separate Branch");
    }
}
