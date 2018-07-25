
import lib.CoreTestCase;
import lib.ui.*;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

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
        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.waitForSearchResult("Object-oriented programming language");
    }

    @Test
    public void testCancelSearch() {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.waitForCancelButtonToAppear();
        SearchPageObject.clickCancelSearch();
        SearchPageObject.waitForCancelButtonToAppear();

    }

    @Test
    public void testCompareArticleTitle() {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        String article_title = ArticlePageObject.getArticleTitle();


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
        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Appium");
        SearchPageObject.clickByArticleWithSubstring("Appium");

        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        ArticlePageObject.waitForTitleElement();
        ArticlePageObject.swipeToFooter();


    }

    @Test
    public void testSaveFirstArticleToMyList() {

        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        ArticlePageObject.waitForTitleElement();
        String article_title = ArticlePageObject.getArticleTitle();
        String name_of_folder = "Learning programming";
        ArticlePageObject.addArticleToMyList(name_of_folder);
        ArticlePageObject.closeArticle();

        NavigationUI NavigationUI = new NavigationUI(driver);
        NavigationUI.clickMyLists();

        MyListsPageObject MyListsPageObject = new MyListsPageObject(driver);
        MyListsPageObject.openFolderByName(name_of_folder);
        MyListsPageObject.swipeByArticleToDelete(article_title);

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








