package ru.wikipedia.pages.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WikipediaMobileMainPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final By SEARCH_CONTAINER = By.id("org.wikipedia.alpha:id/search_container");
    private final By SEARCH_INPUT_FIELD = By.id("org.wikipedia.alpha:id/search_src_text");
    private final By FIRST_RESULT_TITLE = By.id("org.wikipedia.alpha:id/page_list_item_title");
    private final By NAVIGATE_UP_BUTTON = AppiumBy.accessibilityId("Navigate up");
    private final By SKIP_BUTTON = By.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button");
    private final By CLOSE_POPUP_BUTTON = By.id("org.wikipedia.alpha:id/closeButton");

    public WikipediaMobileMainPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void skipOnboarding() {
        try {
            // Ждем появления кнопки пропуска
            WebElement skipButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SKIP_BUTTON));
            if (skipButton != null && skipButton.isDisplayed()) {
                skipButton.click();
                // Ждем исчезновения кнопки или перехода дальше
                wait.until(ExpectedConditions.invisibilityOf(skipButton));
            }
        } catch (Exception e) {
            // Если кнопка не найдена, продолжаем выполнение
        }
    }

    public void closePopupIfPresent() {
        try {
            // Ждем немного, чтобы попап мог появиться
            Thread.sleep(500);
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(CLOSE_POPUP_BUTTON));
            if (closeButton != null && closeButton.isDisplayed()) {
                closeButton.click();
                // Ждем исчезновения кнопки
                wait.until(ExpectedConditions.invisibilityOf(closeButton));
            }
        } catch (Exception e) {
            // Если попап не найден, продолжаем выполнение
        }
    }

    private boolean isElementPresent(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchContainerDisplayed() {
        try {
            skipOnboarding();
            // Ждем появления контейнера поиска
            WebElement searchContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_CONTAINER));
            return searchContainer.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void searchForArticle(String query) {
        try {
            skipOnboarding();

            // Кликаем на контейнер поиска
            WebElement searchContainer = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_CONTAINER));
            searchContainer.click();

            // Вводим запрос в поле поиска
            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT_FIELD));
            searchInput.sendKeys(query);

            // Ждем появления результатов и кликаем на первый
            WebElement firstResult = wait.until(ExpectedConditions.elementToBeClickable(FIRST_RESULT_TITLE));
            firstResult.click();

            // Закрываем попап, если он появился
            closePopupIfPresent();

        } catch (Exception e) {
            System.out.println("Ошибка поиска: " + e.getMessage());
        }
    }

    public String getArticleTitle() {
        try {
            // Закрываем попап, если он появился
            closePopupIfPresent();

            // Используем более надежный способ поиска заголовка
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.androidUIAutomator(
                    "new UiSelector().className(\"android.widget.TextView\").index(0)")));
            
            return titleElement.getText();
        } catch (Exception e) {
            System.out.println("Не удалось получить заголовок: " + e.getMessage());
            return "";
        }
    }

    public void navigateBack() {
        try {
            // Ждем немного перед навигацией
            Thread.sleep(500);
            WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(NAVIGATE_UP_BUTTON));
            backButton.click();
            // Ждем завершения навигации
            Thread.sleep(1000);
            closePopupIfPresent();
        } catch (Exception e) {
            driver.navigate().back();
        }
    }
}
