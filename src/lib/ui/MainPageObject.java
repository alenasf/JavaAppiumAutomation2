package lib.ui;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MainPageObject {

    protected  AppiumDriver driver ;
// obrashautsy vse metodu
    public MainPageObject(AppiumDriver driver)
    {
        this.driver = driver;
    }

    public WebElement waitForElementPresent(By by, String error_message, long timeoutinSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutinSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );

    }

    public WebElement waitForElementPresent(By by, String error_message) {
        return waitForElementPresent(by, error_message, 5);

    }

    public WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    public WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);

        return element;
    }

    public boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    public WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    //    Ex 2. Function: Check text present
    public void checkTextElement(WebElement element, String expected_text) {
        String actual_text = element.getText();
        Assert.assertEquals("Text isn't present on the screen ", expected_text, actual_text);
    }

    public void swipeUp(int timeOfSwipe) {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int start_y = (int) (size.height * 0.8);
        int end_y = (int) (size.height * 0.2);

        action
                .press(x, start_y)
                .waitAction(timeOfSwipe)
                .moveTo(x, end_y)
                .release()
                .perform();
    }

    public void swipeUpQuick() {
        swipeUp(200);
    }

    public void swipeUpToFindElement(By by, String error_message, int max_swipes) {
        int already_swiped = 0;
        while (driver.findElements(by).size() == 0) {

            if (already_swiped > max_swipes) {
                waitForElementPresent(by, "Cannot find element by swiping up.\n" + error_message, 0);
                return;
            }

            swipeUpQuick();
            ++already_swiped;

        }
    }

    public void swipeElementToLeft(By by, String error_message) {
        WebElement element = waitForElementPresent(
                by,
                error_message,
                10);
        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;

        TouchAction action = new TouchAction(driver);
        action
                .press(right_x, middle_y)
                .waitAction(300)
                .moveTo(left_x, middle_y)
                .release()
                .perform();
    }

    public int getAmountOfElements(By by) {
        List elements = driver.findElements(by);
        return elements.size();
    }

    public void assertElementNotPresent(By by, String error_message) {
        int amount_of_elements = getAmountOfElements(by);
        if (amount_of_elements > 0) {
            String default_message = "An element " + by.toString() + " supposed to be not present";
            throw new AssertionError(default_message + " " + error_message);
        }
    }

    public String waitForElementAndGetAttribute(By by, String attribute, String error_mesage, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_mesage, timeoutInSeconds);
        return element.getAttribute(attribute);

    }

    public void assertElementPresent(By by, String error_message) {
        int amount_of_elements = getAmountOfElements(by);
        if (amount_of_elements == 0) {
            String default_message = "An element " + by.toString() + " supposed to be present";
            throw new AssertionError(default_message + " " + error_message);

        }
    }
    public void startSearch(String search_string){
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                search_string,
                "Cannot find search input",
                5
        );
        waitForElementPresent(
                By.id("org.wikipedia:id/search_results_list"),
                "Cannot find search results list",
                15
        );

    }
    public void checkSearchResultAndOpen(String article_title){
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title']" +
                        "[@text='" + article_title + "']"),
                "Cannot find  article in results: '" + article_title + "' ",
                10
        );

        WebElement titleElement = waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15
        );
//       f w i u i
        checkTextElement(titleElement, article_title);
    }
    public void addArticleToNewReadingList(String reading_list_name){
        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc ='Add this article to a reading list']"),
                "Cannot find button for adding article to a reading list",
                5
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot find the onboarding button",
                5
        );
        waitForElementAndClear(
                By.id("org.wikipedia:id/text_input"),
                "Cannot find input field",
                5
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/text_input"),
                reading_list_name,
                "Cannot find input field for new reading list",
                5
        );
        waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Cannot press OK button",
                5
        );
        waitForElementNotPresent(
                By.xpath("//*[@text='OK']"),
                "OK button still exists",
                5
        );
    }

    public void closeArticle() {
        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot find close article button ",
                5
        );
        waitForElementPresent(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button"
        );
    }

    public void addArticleToSavedReadingList(String reading_list_name){

        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc ='Add this article to a reading list']"),
                "Cannot find button for adding article to a reading list",5
        );

        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text= '" + reading_list_name + "']"),
                "Cannot find existing reading list:" + reading_list_name,
                5
        );

        waitForElementNotPresent(
                By.xpath("//android.widget.TextView[@text= '" + reading_list_name + "']"),
                "Name of saved reading list still exists",
                5
        );
    }
    public void openSavedReadingList(String reading_list_name){
        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find 'My list' button",
                5
        );
        waitForElementPresent(
                By.xpath("//android.widget.TextView[@text='My lists']"),
                "Cannot find 'My list' in the header",
                5
        );
        waitForElementAndClick(
                By.xpath("//*[@text='" + reading_list_name  + "']"),
                "Cannot find saved reading list",
                5
        );
    }
    public void deleteSavedArticleFromReadingList(String article_title){
        swipeElementToLeft(
                By.xpath("//*[@text='" + article_title + "']"),
                "Cannot swipe the article"
        );
        waitForElementNotPresent(
                By.xpath("//*[text='" + article_title +  "']"),
                "Article title still presents",
                5
        );
    }
}
