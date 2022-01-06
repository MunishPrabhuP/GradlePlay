package com.demo.e2e;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SampleAPITests {
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        System.out.println("API Tests In SetUp");
    }

    @Test
    public void testcase1() {
        System.out.println("API Tests In Test Case 1");
    }

    @Test(groups = {"sanity"})
    public void testcase2() {
        System.out.println("API Tests In Test Case 2");
    }

    @Test
    public void testcase3() {
        System.out.println("API Tests In Test Case 3");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        System.out.println("API Tests In Tear Down");
    }
}
