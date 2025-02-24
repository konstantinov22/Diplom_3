import io.qameta.allure.Description;
import utils.NecessaryLinks;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pageobject.MainPage;
import utils.WebDriverFactor;
import static org.hamcrest.Matchers.equalTo;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@DisplayName("Проверки конструктора (главной страницы)")
public class MainPageTest {

    private WebDriver driver;
    private String browserName;
    private MainPage mainPage;

    @Before
    @Step("Запуск браузера")
    public void startUp() {
        // Загружаем настройки из файла config.properties
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
        // Получаем имя браузера из файла
        browserName = properties.getProperty("browser", "chrome"); // Значение по умолчанию - "chrome"
        // Создаем драйвер, передавая имя браузера
        driver = WebDriverFactor.getWebDriver(browserName);
        driver.get(NecessaryLinks.URL_MAIN_PAGE);
        // Инициализируем страницу
        mainPage = new MainPage(driver);
    }

    @After
    @Step("Закрытие браузера")
    public void tearDown() {
        driver.quit();
    }

    @Test
    @Step("Нажатие на вкладку Булки")
    @DisplayName("Проверка работы вкладки Булки в разделе с ингредиентами")
    @Description("Проверка работы вкладки Булки в разделе с ингредиентами")
    public void checkNavBunsIsSuccessTest() {
        Allure.parameter("Браузер", browserName);

        int expectedLocation = getIngredientTitleExpectedLocation();
        clickToppingsButton();
        clickBunsButton();

        MatcherAssert.assertThat(
                "Ингредиенты не проскроллились до булок",
                getBunsLocation(),
                equalTo(expectedLocation)
        );
    }

    @Test
    @Step("Нажатие на вкладку Соусы")
    @DisplayName("Проверка работы вкладки Соусы в разделе с ингредиентами")
    @Description("Проверка работы вкладки Соусы в разделе с ингредиентами")
    public void checkNavToppingsIsSuccessTest() {
        Allure.parameter("Браузер", browserName);

        int expectedLocation = getIngredientTitleExpectedLocation();
        clickToppingsButton();

        MatcherAssert.assertThat(
                "Ингредиенты не проскроллились до соусов",
                getToppingsLocation(),
                equalTo(expectedLocation)
        );
    }

    @Test
    @Step("Нажатие на вкладку Начинки")
    @DisplayName("Проверка работы вкладки Начинки в разделе с ингредиентами")
    @Description("Проверка работы вкладки Начинки в разделе с ингредиентами")
    public void checkNavFillingsIsSuccessTest() {
        Allure.parameter("Браузер", browserName);

        int expectedLocation = getIngredientTitleExpectedLocation();
        clickFillingsButton();

        MatcherAssert.assertThat(
                "Ингредиенты не проскроллились до начинок",
                getFillingsLocation(),
                equalTo(expectedLocation)
        );
    }

    // Методы с аннотацией @Step для описания шагов
    @Step("Получение ожидаемой позиции заголовка ингредиентов")
    private int getIngredientTitleExpectedLocation() {
        return mainPage.getIngredientTitleExpectedLocation();
    }

    @Step("Клик на кнопку 'Соусы'")
    private void clickToppingsButton() {
        mainPage.clickToppingsButton();
    }

    @Step("Клик на кнопку 'Булки'")
    private void clickBunsButton() {
        mainPage.clickBunsButton();
    }

    @Step("Клик на кнопку 'Начинки'")
    private void clickFillingsButton() {
        mainPage.clickFillingsButton();
    }

    @Step("Получение текущей позиции раздела 'Булки'")
    private int getBunsLocation() {
        return mainPage.getBunsLocation();
    }

    @Step("Получение текущей позиции раздела 'Соусы'")
    private int getToppingsLocation() {
        return mainPage.getToppingsLocation();
    }

    @Step("Получение текущей позиции раздела 'Начинки'")
    private int getFillingsLocation() {
        return mainPage.getFillingsLocation();
    }
}