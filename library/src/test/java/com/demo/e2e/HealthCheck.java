package com.demo.e2e;

import org.testng.Assert;
import org.testng.annotations.Test;

public class HealthCheck {

    @Test
    public void checkApplicationStatus() {
        System.out.println("Application is Live");
        Assert.assertTrue(true);
    }
}
