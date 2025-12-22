package ru.wikipedia.utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {
    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        
        // Добавляем дополнительные опции для стабильной работы
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        return driver;
    }

    public static AndroidDriver createAndroidDriver() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("appPackage", "org.wikipedia.alpha");
        caps.setCapability("appActivity", "org.wikipedia.main.MainActivity");
        caps.setCapability("noReset", false);
        
        // Добавляем дополнительные capabilities для стабильности
        caps.setCapability("newCommandTimeout", 300);
        caps.setCapability("autoGrantPermissions", true);
        
        return new AndroidDriver(new URL("http://127.0.0.1:4723"), caps);
    }
}
