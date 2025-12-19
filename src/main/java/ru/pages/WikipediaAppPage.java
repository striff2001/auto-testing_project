package ru.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WikipediaAppPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final By SEARCH_CONTAINER = By.id("org.wikipedia.alpha:id/search_container");
    private final By SEARCH_INPUT_FIELD = By.id("org.wikipedia.alpha:id/search_src_text");
    private final By FIRST_RESULT_TITLE = By.id("org.wikipedia.alpha:id/page_list_item_title");
    private final By NAVIGATE_UP_BUTTON = AppiumBy.accessibilityId("Navigate up");
    private final By SKIP_BUTTON = By.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button");
    private final By CLOSE_POPUP_BUTTON = By.id("org.wikipedia.alpha:id/closeButton");

    public WikipediaAppPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Уменьшил до 5 сек
    }

    public void skipOnboarding() {
        try {
            Thread.sleep(2000); // Даём время на появление
            if (isElementPresent(SKIP_BUTTON)) {
                driver.findElement(SKIP_BUTTON).click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
        }
    }

    public void closePopupIfPresent() {
        try {
            Thread.sleep(1000);
            if (isElementPresent(CLOSE_POPUP_BUTTON)) {
                driver.findElement(CLOSE_POPUP_BUTTON).click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            // Игнорируем
        }
    }

    private boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchContainerDisplayed() {
        try {
            skipOnboarding();
            Thread.sleep(2000);
            return driver.findElement(SEARCH_CONTAINER).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void searchForArticle(String query) {
        try {
            skipOnboarding();
            Thread.sleep(2000);

            driver.findElement(SEARCH_CONTAINER).click();
            Thread.sleep(1000);

            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT_FIELD));
            searchInput.sendKeys(query);

            Thread.sleep(3000);

            driver.findElement(FIRST_RESULT_TITLE).click();

            Thread.sleep(2000);
            closePopupIfPresent();

        } catch (Exception e) {
            System.out.println("Ошибка поиска: " + e.getMessage());
        }
    }

    public String getArticleTitle() {
        try {
            Thread.sleep(3000);
            closePopupIfPresent();

            WebElement titleElement = driver.findElement(By.xpath("//android.widget.TextView[1]"));
            return titleElement.getText();

        } catch (Exception e) {
            System.out.println("Не удалось получить заголовок, пробуем другой локатор...");

            try {
                WebElement titleElement = driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiSelector().className(\"android.widget.TextView\").instance(0)"));
                return titleElement.getText();
            } catch (Exception ex) {
                return "";
            }
        }
    }

    public void navigateBack() {
        try {
            Thread.sleep(1000);
            driver.findElement(NAVIGATE_UP_BUTTON).click();
            Thread.sleep(2000);
            closePopupIfPresent();
        } catch (Exception e) {
            driver.navigate().back();
        }
    }
}