package com.framework.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);
    public static WebDriver driver;
    public static boolean headless;
    public static Properties prop;

    public static void loadConfig() {
        prop = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            prop.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load config.properties", e);
        }
    }

    public static void setup() {
        loadConfig();

        String browser = System.getProperty("browser");
        if (browser == null || browser.isBlank()) {
            browser = prop.getProperty("browser");
        }

        String headlessValue = System.getProperty("headless");
        if (headlessValue == null || headlessValue.isBlank()) {
            headlessValue = prop.getProperty("headless", "false");
        }

        headless = Boolean.parseBoolean(headlessValue);

        browser = browser.trim().toLowerCase();

        log.info("Initializing browser: {} | headless: {}", browser, headless);

        switch (browser) {

            case "firefox":
                WebDriverManager.firefoxdriver().setup();

                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }

                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }

                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(
                        Long.parseLong(prop.getProperty("implicit.wait"))
                )
        );

        String baseUrl = prop.getProperty("base.url");
        driver.get(baseUrl);

        log.info("Application opened successfully for '{}' on browser '{}' (headless={})",
                baseUrl, browser, headless);
    }

    public static void tearDown() {
        if (driver != null) {
            log.info("Closing browser");
            driver.quit();
            driver = null;
        }
    }
}