package com.demo.e2e;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SampleVisualTests {
    private WebDriver driver;

    @Test
    public void launchSite() throws MalformedURLException {
        DesiredCapabilities capabilities;
        String hostURL;

        Map<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("start-fullscreen");
        chromeOptions.addArguments("--ignore-ssl-errors=yes");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.setExperimentalOption("prefs", chromePrefs);
        capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        chromeOptions.merge(capabilities);
        hostURL = "http://localhost:4444/wd/hub";
        driver = new RemoteWebDriver(new URL(hostURL), chromeOptions);
        driver.get("https://accounts-staging.saas.appd-test.com/overview");
        System.out.println("Title:" + driver.getTitle());
    }
}
