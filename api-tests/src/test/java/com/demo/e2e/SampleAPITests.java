package com.demo.e2e;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SampleAPITests {
    @BeforeClass
    public void setUp() {
        System.out.println("API Tests In SetUp in Separate Branch");
    }

    @Test
    public void testcase1() {
        System.out.println("API Tests In Test Case 1 in Separate Branch");
    }

    @Test
    public void testcase2() {
        System.out.println("API Tests In Test Case 2 in Separate Branch");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("API Tests In Tear Down in Separate Branch");
    }
}
