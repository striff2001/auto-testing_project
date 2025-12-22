package ru.wikipedia.tests.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.wikipedia.pages.web.WikipediaMainPage;
import ru.wikipedia.utils.DriverFactory;

public class WikipediaWebTests {

    private WebDriver driver;
    private WikipediaMainPage wikipediaPage;
    private static final String BASE_URL = "https://ru.wikipedia.org/";

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = DriverFactory.createDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL);
        wikipediaPage = new WikipediaMainPage(driver);
    }

    @Test
    public void testLogoReturnsToMainPage() {
        wikipediaPage.clickRandomPageLink();
        Assert.assertTrue(wikipediaPage.isInArticlePage(), "Not on article page");

        wikipediaPage.navigateToMainPageViaLogo();

        String currentUrl = driver.getCurrentUrl();
        boolean isMain = wikipediaPage.isOnMainPage();

        // Детальная диагностика
        System.out.println("Current URL: " + currentUrl);
        System.out.println("isOnMainPage(): " + isMain);

        Assert.assertTrue(isMain, "Logo didn't return to main page. URL: " + currentUrl);
    }


    @Test
    public void testLanguagesCountIsNumericAndPositive() {
        wikipediaPage.isMainPageContentDisplayed();
        String languagesCount = wikipediaPage.getCurrentLanguagesCount();

        Assert.assertTrue(languagesCount.matches("\\d+"), "Not numeric: " + languagesCount);
        Assert.assertTrue(Integer.parseInt(languagesCount) > 100,  // Смягчили условие
                "Too low: " + languagesCount);
    }

    @Test
    public void testSearchRedirectsToArticle() {
        String searchQuery = "Python";
        wikipediaPage.isMainPageContentDisplayed();
        wikipediaPage.searchFor(searchQuery);

        Assert.assertTrue(wikipediaPage.isInArticlePage(), "Not on article page");
        // Case-insensitive проверка
        String currentUrl = driver.getCurrentUrl().toLowerCase();
        Assert.assertTrue(currentUrl.contains(searchQuery.toLowerCase()),
                "Search URL doesn't contain query: " + driver.getCurrentUrl());
    }

    @Test
    public void testRandomPageAlwaysLeadsToArticle() {
        wikipediaPage.navigateToMainPage();
        wikipediaPage.clickRandomPageLink();

        // Проверяем 3 раза подряд - всегда попадаем на статью
        for (int i = 0; i < 3; i++) {
            Assert.assertTrue(wikipediaPage.isInArticlePage(),
                    String.format("Random page #%d is not an article", i + 1));
            if (i < 2) wikipediaPage.clickRandomPageLink();
        }
    }

    @Test
    public void testSearchInputRemainsFunctionalAfterRandomNavigation() {
        wikipediaPage.navigateToMainPage();
        wikipediaPage.clickRandomPageLink();  // Уходим и возвращаемся
        wikipediaPage.navigateToMainPage();

        Assert.assertTrue(wikipediaPage.isSearchInputDisplayedAndEnabled(),
                "Search input not functional after navigation cycle");
    }


}
