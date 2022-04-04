package com.demo.e2e;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PRCheck {

    @Test
    public void testCase1() {
        System.out.println("PR Check 1 Pass");
        Assert.assertTrue(true);
    }

    @Test
    public void testCase2() {
        System.out.println("PR Check 2 Fail");
        Assert.fail("PR Check failed");
    }
}
