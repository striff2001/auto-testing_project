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
    private WikipediaMainPage wikipediaMainPage;
    private static final String BASE_URL = "https://ru.wikipedia.org/";

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = DriverFactory.createDriver();
        driver.manage().window().maximize();
        wikipediaMainPage = new WikipediaMainPage(driver);
    }

    @Test
    public void testMainPageLoadAndElementsDisplay() {
        Assert.assertTrue(wikipediaMainPage.isMainPageContentDisplayed(), "Main Wikipedia page content is not displayed.");
    }

    @Test
    public void testSearchFunctionality() {
        String searchQuery = "Россия";
        String expectedArticleTitle = "Россия";

        wikipediaMainPage.searchFor(searchQuery);

        String heading = wikipediaMainPage.getFirstHeadingText();

        Assert.assertEquals(heading, expectedArticleTitle, "Search failed. Expected heading: '" + expectedArticleTitle + "', but got: " + heading);
    }

    @Test
    public void testRandomPageNavigation() {
        // Сохраняем URL главной страницы
        String mainPageUrl = "https://ru.wikipedia.org/wiki/Заглавная_страница";
        
        // Убеждаемся, что мы на главной странице
        wikipediaMainPage.isMainPageContentDisplayed();
        
        wikipediaMainPage.clickRandomPageLink();

        // Проверяем, что URL изменился (мы перешли на другую страницу)
        Assert.assertNotEquals(driver.getCurrentUrl(), mainPageUrl, "Transition to a random page did not occur (URL did not change).");
    }

    @Test
    public void testSearchInputInteractivity() {
        wikipediaMainPage.isMainPageContentDisplayed();

        Assert.assertTrue(wikipediaMainPage.isSearchInputDisplayedAndEnabled(), "Search input element is not displayed or enabled for interaction.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
