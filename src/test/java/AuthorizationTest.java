import io.qameta.allure.Description;
import utils.NewUserApi;
import utils.NecessaryLinks;
import utils.WebDriverFactor; // Импорт класса WebDriverFactor
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.github.javafaker.Faker; // Импорт JavaFaker
import org.openqa.selenium.WebDriver;
import pageobject.AuthorizationPage;
import pageobject.PageForgottenPassword;
import pageobject.MainPage;
import pageobject.RegistrationPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.Matchers.equalTo;

/**
 * Тесты для проверки функционала авторизации пользователя.
 */
@DisplayName("Авторизация пользователя")
public class AuthorizationTest {

    private WebDriver webDriver;
    private String browserName;
    private AuthorizationPage authorizationPage;
    private MainPage mainPage;
    private RegistrationPage registerPage;
    private PageForgottenPassword pageForgottenPassword;
    private String name, email, password;
    private NewUserApi newUserApi;

    /**
     * Подготовка тестового окружения: запуск браузера и создание тестовых данных.
     *
     * @throws IOException если произошла ошибка при чтении конфигурационного файла.
     */
    @Before
    @Step("Запуск браузера, подготовка тестовых данных")
    public void startUp() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(fis);
        }

        // Получаем имя браузера из файла config.properties
        browserName = properties.getProperty("browser", "chrome"); // Значение по умолчанию - "chrome"

        // Создаем драйвер с указанным именем браузера
        webDriver = WebDriverFactor.getWebDriver(browserName);
        webDriver.get(NecessaryLinks.URL_MAIN_PAGE);

        // Инициализация страниц
        authorizationPage = new AuthorizationPage(webDriver);
        mainPage = new MainPage(webDriver);
        registerPage = new RegistrationPage(webDriver);
        pageForgottenPassword = new PageForgottenPassword(webDriver);

        // Генерация тестовых данных с помощью JavaFaker
        Faker faker = new Faker();
        name = faker.name().firstName(); // Генерация имени
        email = faker.internet().emailAddress(); // Генерация email
        password = faker.internet().password(8, 16); // Генерация пароля

        // Создание нового пользователя через API
        newUserApi = new NewUserApi();
        newUserApi.createUser(name, email, password);
    }

    /**
     * Очистка тестового окружения: закрытие браузера и удаление тестового пользователя.
     */
    @After
    @Step("Закрытие браузера и очистка данных")
    public void tearDown() {
        webDriver.quit();
        newUserApi.deleteTestUser(email, password);
    }

    /**
     * Процесс авторизации пользователя.
     */
    @Step("Процесс авторизации")
    private void authUser() {
        authorizationPage.setEmail(email);
        authorizationPage.setPassword(password);
        authorizationPage.clickAuthButton();
        authorizationPage.waitFormSubmitted();
    }

    /**
     * Проверка входа через кнопку «Войти в аккаунт» на главной странице.
     */
    @Test
    @DisplayName("Вход по кнопке «Войти в аккаунт» на главной")
    @Description("Вход по кнопке «Войти в аккаунт» на главной")
    public void authFromMainIsSuccessTest() {
        Allure.parameter("Браузер", browserName);

        mainPage.clickAuthButton();
        authorizationPage.waitAuthFormVisible();
        authUser();

        MatcherAssert.assertThat(
                "Ожидается надпись «Оформить заказ» на кнопке в корзине",
                mainPage.getBasketButtonText(),
                equalTo("Оформить заказ")
        );
    }

    /**
     * Проверка входа через кнопку «Личный кабинет».
     */
    @Test
    @DisplayName("Вход через кнопку «Личный кабинет»")
    @Description("Вход через кнопку «Личный кабинет»")
    public void authFromLinkToProfileIsSuccessTest() {
        Allure.parameter("Браузер", browserName);

        mainPage.clickLinkToProfile();
        authorizationPage.waitAuthFormVisible();
        authUser();

        MatcherAssert.assertThat(
                "Ожидается надпись «Оформить заказ» на кнопке в корзине",
                mainPage.getBasketButtonText(),
                equalTo("Оформить заказ")
        );
    }

    /**
     * Проверка входа через ссылку в форме регистрации.
     */
    @Test
    @DisplayName("Вход через кнопку в форме регистрации")
    @Description("Вход через кнопку в форме регистрации")
    public void authLinkFromRegFormIsSuccessTest() {
        Allure.parameter("Браузер", browserName);

        webDriver.get(NecessaryLinks.URL_REGISTER_PAGE);
        registerPage.clickAuthLink();
        authorizationPage.waitAuthFormVisible();
        authUser();

        MatcherAssert.assertThat(
                "Ожидается надпись «Оформить заказ» на кнопке в корзине",
                mainPage.getBasketButtonText(),
                equalTo("Оформить заказ")
        );
    }

    /**
     * Проверка входа через ссылку в форме восстановления пароля.
     */
    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    @Description("Вход через кнопку в форме восстановления пароля")
    public void authLinkFromForgotPasswordFormIsSuccessTest() {
        Allure.parameter("Браузер", browserName);

        webDriver.get(NecessaryLinks.URL_FORGOT_PASSWORD_PAGE);
        pageForgottenPassword.clickAuthLink();
        authorizationPage.waitAuthFormVisible();
        authUser();

        MatcherAssert.assertThat(
                "Ожидается надпись «Оформить заказ» на кнопке в корзине",
                mainPage.getBasketButtonText(),
                equalTo("Оформить заказ")
        );
    }
}