
import io.appium.java_client.TouchAction;
import lib.CoreTestCase;
import lib.ui.MainPageObject;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;



public class FirstTest extends CoreTestCase {

    private MainPageObject MainpageObject;

    protected void setUp() throws Exception
    {
        super.setUp();

        MainpageObject = new MainPageObject(driver);
    }



    @Test
    public void testSearch()
    {
        MainpageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainpageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        MainpageObject.waitForElementPresent(
                By.xpath("//*[@resource-id ='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by 'Java' ",
                15
        );

    }

    @Test
    public void testCancelSearch() {
        MainpageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainpageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );
        MainpageObject.waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search field",
                5
        );

        MainpageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel",
                5
        );

        MainpageObject.waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "X is still present on the page",
                5
        );
    }

    @Test
    public void testCompareArticleTitle() {
        MainpageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainpageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        MainpageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id ='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find X to cancel",
                5
        );

        WebElement title_element = MainpageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15
        );
        String article_title = title_element.getAttribute("text");

        Assert.assertEquals(
                "We see unexpected title!",
                "Java (programming language)",
                article_title
        );
    }

    @Test
    public void testPlaceholderSearchPresent() {
        MainpageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        WebElement element = MainpageObject.waitForElementPresent(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find article title"
        );

        MainpageObject.checkTextElement(element, "Search…");


        MainpageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );
        MainpageObject.waitForElementPresent(
                By.xpath("//*[@resource-id ='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find search result: Java ",
                15
        );
//        System.out.println("Element present");

    }

    //    #Ex 3.Test for search -> check results ,then clear "search results" -> check empty results list
    @Test
    public void testCancelSearchAndCheckEmptyList() {

        MainpageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainpageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );
        WebElement searchResultList = MainpageObject.waitForElementPresent(
                By.id("org.wikipedia:id/search_results_list"),
                " Cannot find 'Search Result List'",
                15
        );

        List<WebElement> searchResults = searchResultList.findElements(By.className("android.widget.LinearLayout"));
        System.out.println("List size: " + searchResults.size());
        Assert.assertTrue("There is no search result", searchResults.size() > 0
        );

        MainpageObject.waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        WebElement searchEmptyMessage = MainpageObject.waitForElementPresent(
                By.id("org.wikipedia:id/search_empty_message"),
                "Cannot find empty message",
                5
        );
        MainpageObject.checkTextElement(searchEmptyMessage, "Search and read the free encyclopedia in your language");

        MainpageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find close button",
                5
        );

        MainpageObject.waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "X is still present on the page",
                5
        );

    }

    @Test
    public void testSwipeArticle() {
        MainpageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainpageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Appium",
                "Cannot find search input",
                5
        );

        MainpageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id ='org.wikipedia:id/page_list_item_title'][@text='Appium']"),
                "Cannot find 'Appium' article in search",
                5
        );

        MainpageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15
        );
        MainpageObject.swipeUpToFindElement(
                By.xpath("//*[@text='View page in browser']"),
                "Cannot find the end of the article",
                20
        );
    }

    @Test
    public void testSaveFirstArticleToMyList() {
        MainpageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainpageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        MainpageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id ='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find X to cancel",
                5
        );

        MainpageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15
        );

        MainpageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find button to open article options",
                5

        );

        MainpageObject.waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Cannot find option to add article to reading list",
                5
        );

        MainpageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot find 'Got it' tip overlay",
                5
        );

        MainpageObject.waitForElementAndClear(
                By.id("org.wikipedia:id/text_input"),
                "Cannot find input to set name of articles folder",
                5
        );

        String name_of_folder = "Learning programming";

        MainpageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/text_input"),
                name_of_folder,
                "Cannot put text into articles folder input",
                5
        );

        MainpageObject.waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Cannot press OK button",
                5
        );
        MainpageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot close article, cannot find X Link",
                5
        );
        MainpageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find navigation button to My List",
                5
        );

        MainpageObject.waitForElementAndClick(
                By.xpath("//*[@text='" + name_of_folder + "']"),
                "Cannot find created folder",
                5
        );
        MainpageObject.swipeElementToLeft(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot find saved article: swipe"
        );
        MainpageObject.waitForElementNotPresent(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot delete saved article",
                5
        );
    }

    @Test
    public void testAmountOfNotEmptySearch() {
        MainpageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        String search_line = "linkin Park Discography";

        MainpageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                search_line,
                "Cannot put text into articles folder input",
                5
        );

        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        MainpageObject.waitForElementPresent(
                By.xpath(search_result_locator),
                "Cannot find anything by the request " + search_line,
                15
        );


        int amount_of_search_results = MainpageObject.getAmountOfElements(
                By.xpath(search_result_locator)
        );
        Assert.assertTrue(
                "We found too few results",
                amount_of_search_results > 0
        );
    }

    @Test
    public void testAmountOfEmptySearch() {
        MainpageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );
        String search_line = "jhjkjhkjhkhkh";

        MainpageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                search_line,
                "Cannot put text into articles folder input",
                5
        );

        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        String empty_result_label = "//*[@text='No results found']";

        MainpageObject.waitForElementPresent(
                By.xpath(empty_result_label),
                "Cannot find empty result label by the request" + search_line,
                15
        );

        MainpageObject.assertElementNotPresent(
                By.xpath(search_result_locator),
                "We've found some results by request " + search_line
        );
    }

    @Test
    public void testChangeScreenOrientationOnSearchResults() {
        MainpageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );
        String search_line = "Java";

        MainpageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                search_line,
                "Cannot put text into articles folder input",
                5
        );

        MainpageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id ='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by " + search_line,
                15
        );

        String title_before_rotation = MainpageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );
        driver.rotate(ScreenOrientation.LANDSCAPE);

        String title_after_rotation = MainpageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );

        Assert.assertEquals(
                "Article title have been changed after screen rotation",
                title_before_rotation,
                title_after_rotation
        );

        driver.rotate(ScreenOrientation.PORTRAIT);

        String title_after_second_rotation = MainpageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );
        Assert.assertEquals(
                "Article title have been changed after screen rotation",
                title_before_rotation,
                title_after_second_rotation
        );
    }

    @Test
    public void testSearchArticleInBackground() {
        MainpageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainpageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        MainpageObject.waitForElementPresent(
                By.xpath("//*[@resource-id ='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Search Wikipedia' input",
                5
        );
        driver.runAppInBackground(2);

        MainpageObject.waitForElementPresent(
                By.xpath("//*[@resource-id ='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find article after returning from background",
                5
        );
    }

    // #Ex.6 Test:assert title
//     Написать тест, который открывает статью и убеждается, что у нее есть элемент title.
// Важно: тест не должен дожидаться появления title, проверка должна производиться сразу.
// Если title не найден - тест падает с ошибкой.
    @Test
    public void testCheckArticleTitlePresent() {
        MainpageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );
        MainpageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                5
        );
        MainpageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id ='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by ",
                5
        );
        MainpageObject.assertElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title immediately "
        );
    }
// Ex. 5 Тест: сохранение двух статей
    @Test
    public void testSaveTwoArticlesAndRemoveOne(){
        String first_search = "Appium";
        String first_search_title = "Appium";
        String second_search = "Java";
        String second_search_title = "Java";
        String reading_list_name = "My homework";

//        First Search
        MainpageObject.startSearch(first_search);
        MainpageObject.checkSearchResultAndOpen(first_search_title);
        MainpageObject.addArticleToNewReadingList(reading_list_name);
        MainpageObject.closeArticle();

//        Second Search
        MainpageObject.startSearch(second_search);
        MainpageObject.checkSearchResultAndOpen(second_search_title);
        MainpageObject.addArticleToSavedReadingList(reading_list_name);
        MainpageObject.closeArticle();

//        Delete first article
        MainpageObject.openSavedReadingList(reading_list_name);
        MainpageObject.deleteSavedArticleFromReadingList(first_search_title);
        MainpageObject.checkSearchResultAndOpen(second_search_title);

    }

}








