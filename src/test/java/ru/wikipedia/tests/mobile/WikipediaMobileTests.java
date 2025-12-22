package ru.wikipedia.tests.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.wikipedia.pages.mobile.WikipediaMobileMainPage;
import ru.wikipedia.utils.DriverFactory;

import java.time.Duration;

public class WikipediaMobileTests {

    private AndroidDriver driver;
    private WikipediaMobileMainPage appPage;
    private WebDriverWait wait;

    @BeforeMethod
    public void setup() throws Exception {
        driver = DriverFactory.createAndroidDriver();
        appPage = new WikipediaMobileMainPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Ждем загрузки основного экрана
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("org.wikipedia.alpha:id/search_container")));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1)
    public void testMainScreenSearchIsDisplayed() {
        boolean isDisplayed = appPage.isSearchContainerDisplayed();
        System.out.println("Поле поиска отображается: " + isDisplayed);
        Assert.assertTrue(isDisplayed);
    }

    @Test(priority = 2)
    public void testSearchAndOpenArticle() {
        appPage.searchForArticle("Appium");

        String title = appPage.getArticleTitle();
        System.out.println("Заголовок статьи: '" + title + "'");

        Assert.assertTrue(title != null && !title.isEmpty());
        Assert.assertTrue(title.toLowerCase().contains("appium"),
                "Заголовок должен содержать 'Appium'. Фактический: " + title);
    }

    @Test(priority = 3)
    public void testSearchAndNavigateBack() {
        appPage.searchForArticle("Selenium");

        String title = appPage.getArticleTitle();
        System.out.println("Открыта статья: " + title);

        appPage.navigateBack();

        boolean isDisplayed = appPage.isSearchContainerDisplayed();
        Assert.assertTrue(isDisplayed, "После возврата должно быть видно поле поиска");
    }
}
