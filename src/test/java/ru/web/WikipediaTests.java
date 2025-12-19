package ru.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pages.WikipediaPage;
import ru.utils.WebDriverFactory;

public class WikipediaTests {

    private WebDriver driver;
    private WikipediaPage wikipediaPage;
    private static final String BASE_URL = "https://ru.wikipedia.org/";

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = WebDriverFactory.createDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL);
        wikipediaPage = new WikipediaPage(driver);
    }

    @Test
    public void testMainPageLoadAndElementsDisplay() {
        Assert.assertTrue(wikipediaPage.isMainPageContentDisplayed(), "Main Wikipedia page content is not displayed.");
    }

    @Test
    public void testSearchFunctionality() {
        String searchQuery = "Россия";
        String expectedArticleTitle = "Россия";

        wikipediaPage.searchFor(searchQuery);

        String heading = wikipediaPage.getFirstHeadingText();

        Assert.assertEquals(heading, expectedArticleTitle, "Search failed. Expected heading: '" + expectedArticleTitle + "', but got: " + heading);
    }

    @Test
    public void testRandomPageNavigation() {
        wikipediaPage.isMainPageContentDisplayed();
        String originalUrl = driver.getCurrentUrl();

        wikipediaPage.clickRandomPageLink();

        Assert.assertNotEquals(driver.getCurrentUrl(), originalUrl, "Transition to a random page did not occur (URL did not change).");
    }

    @Test
    public void testSearchInputInteractivity() {
        wikipediaPage.isMainPageContentDisplayed();

        Assert.assertTrue(wikipediaPage.isSearchInputDisplayedAndEnabled(), "Search input element is not displayed or enabled for interaction.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}