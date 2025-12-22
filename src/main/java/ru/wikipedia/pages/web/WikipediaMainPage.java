package ru.wikipedia.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WikipediaMainPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final String BASE_URL = "https://ru.wikipedia.org/wiki/Заглавная_страница";

    private final By WIKI_LOGO = By.id("p-logo");
    private final By SEARCH_INPUT = By.id("searchInput");
    private final By ARTICLE_HEADING = By.id("firstHeading");
    private final By RANDOM_PAGE_LINK = By.id("n-randompage");
    private final By BODY_CONTENT = By.id("bodyContent");

    public WikipediaMainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean isMainPageContentDisplayed() {
        driver.get(BASE_URL);
        try {
            // Ожидаем появления логотипа и основного контента
            wait.until(ExpectedConditions.visibilityOfElementLocated(WIKI_LOGO));
            WebElement bodyContent = wait.until(ExpectedConditions.visibilityOfElementLocated(BODY_CONTENT));
            return bodyContent.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void searchFor(String query) {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT));
        searchInput.clear();
        searchInput.sendKeys(query);
        searchInput.submit();
    }

    public String getFirstHeadingText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ARTICLE_HEADING)).getText().trim();
    }

    public void clickRandomPageLink() {
        WebElement randomLink = wait.until(ExpectedConditions.elementToBeClickable(RANDOM_PAGE_LINK));
        randomLink.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(BODY_CONTENT));
    }

    public boolean isSearchInputDisplayedAndEnabled() {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT));
        return searchInput.isDisplayed() && searchInput.isEnabled();
    }
}
