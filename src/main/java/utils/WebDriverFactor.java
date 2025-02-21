package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverFactor {

    /**
     * Метод для создания драйвера на основе указанного имени браузера.
     *
     * @param browserName Имя браузера (например, "chrome", "yandex").
     * @return Экземпляр WebDriver.
     */
    public static WebDriver getWebDriver(String browserName) {
        // Проверяем, что имя браузера не пустое
        if (browserName == null || browserName.isEmpty()) {
            throw new IllegalArgumentException("Browser name cannot be null or empty");
        }
        // Создаем драйвер на основе значения из параметра
        return createDriver(browserName.toLowerCase());
    }

    /**
     * Приватный метод для создания драйвера на основе имени браузера.
     *
     * @param browserName Имя браузера (например, "chrome", "yandex").
     * @return Экземпляр WebDriver.
     */
    private static WebDriver createDriver(String browserName) {
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(false); // Запускаем браузер в обычном режиме (для отладки)
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        switch (browserName) {
            case "chrome":
                // Используем WebDriverManager для автоматической настройки ChromeDriver
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(options);

            case "yandex":
                // Для Yandex Browser используем WebDriverManager для ChromeDriver
                WebDriverManager.chromedriver().setup();
                options.setBinary("C:\\Program Files\\Yandex\\YandexBrowser\\browser.exe"); // Укажите правильный путь к Yandex Browser
                return new ChromeDriver(options);

            default:
                throw new RuntimeException("Incorrect browser name: " + browserName);
        }
    }
}