package ru.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.pages.WikipediaAppPage;
import ru.utils.WebDriverFactory;

public class WikipediaMobileTests {

    private AndroidDriver driver;
    private WikipediaAppPage appPage;

    @BeforeMethod
    public void setup() throws Exception {
        driver = WebDriverFactory.createAndroidDriver();
        appPage = new WikipediaAppPage(driver);
        Thread.sleep(3000);
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
    public void testSearchAndOpenArticle() throws InterruptedException {
        appPage.searchForArticle("Appium");
        Thread.sleep(3000);

        String title = appPage.getArticleTitle();
        System.out.println("Заголовок статьи: '" + title + "'");

        Assert.assertTrue(title != null && !title.isEmpty());
        Assert.assertTrue(title.toLowerCase().contains("appium"),
                "Заголовок должен содержать 'Appium'. Фактический: " + title);
    }

    @Test(priority = 3)
    public void testSearchAndNavigateBack() throws InterruptedException {
        appPage.searchForArticle("Selenium");
        Thread.sleep(3000);

        String title = appPage.getArticleTitle();
        System.out.println("Открыта статья: " + title);

        appPage.navigateBack();
        Thread.sleep(2000);

        boolean isDisplayed = appPage.isSearchContainerDisplayed();
        Assert.assertTrue(isDisplayed, "После возврата должно быть видно поле поиска");
    }
}


//@Test для поиска ID
//public void testFindRealElements() throws InterruptedException {
//    Thread.sleep(15000);
//
//    String source = driver.getPageSource();
//    System.out.println("=== PAGE SOURCE ===");
//    System.out.println(source.substring(0, Math.min(2000, source.length())));
//
//    System.out.println("\n=== ALL ELEMENTS ===");
//    var elements = driver.findElements(By.xpath("//*"));
//    for (var el : elements) {
//        String id = el.getAttribute("resource-id");
//        String text = el.getText();
//        String className = el.getAttribute("class");
//        if (id != null && !id.isEmpty()) {
//            System.out.println("ID: " + id + " | Class: " + className + " | Text: " + text);
//        }
//    }
//}