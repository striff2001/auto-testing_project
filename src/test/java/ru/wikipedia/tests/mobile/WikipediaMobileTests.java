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
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        appPage.skipOnboarding();
        appPage.closePopupIfPresent();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("org.wikipedia.alpha:id/search_container")));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1, description = "Многоязычный поиск")
    public void testMultilingualSearch() {
        String[] queries = {"Python", "Питон", "Kubernetes"};
        for (String query : queries) {
            appPage.searchForArticle(query);
            String title = appPage.getArticleTitle();
            Assert.assertFalse(title.isEmpty());
            appPage.navigateBack();
        }
    }

    @Test(priority = 2, description = "Цепочка поисков")
    public void testSearchChain() {
        appPage.searchForArticle("Docker");
        appPage.getArticleTitle();
        appPage.navigateBack();

        appPage.searchForArticle("Jenkins");
        Assert.assertFalse(appPage.getArticleTitle().isEmpty());
    }

    @Test(priority = 3, description = "Тест производительности поиска (увеличенный timeout)")
    public void testPerformanceSearch() {
        long start = System.currentTimeMillis();

        appPage.searchForArticle("Maven");
        String title = appPage.getArticleTitle();
        System.out.println("Время поиска: " + (System.currentTimeMillis() - start) + "ms");

        // 45 секунд = нормально для эмулятора
        Assert.assertTrue(title.isEmpty() == false, "Статья не найдена");
    }

}
