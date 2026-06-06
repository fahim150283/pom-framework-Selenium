package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.pages.LoginPage;
import com.framework.utils.ExcelUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

public class LoginTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(LoginPage.class);
    private LoginPage loginPage;
    private SoftAssert softAssert;

    @BeforeMethod
    public void start() {
        setup();
        softAssert = new SoftAssert();
        loginPage = new LoginPage();
        log.info("Test started");
    }

    @Test
    public void openLoginPage() {
        boolean textFound = driver.getTitle().contains("Swag");
        softAssert.assertTrue(textFound);
        log.info("Login page opened successfully");
        softAssert.assertAll();
    }

    @Test
    public void passedLogin() {
        loginPage.login("standard_user", "secret_sauce");

        softAssert.assertTrue(loginPage.isInventoryVisible());
        softAssert.assertEquals(loginPage.getFirstItemPrice(), "$29.99");
        log.info("Login successful");
        softAssert.assertAll();
    }

    @Test
    public void brokenLogin() {
        loginPage.login("standard_user", "secret_sau2ce");

        softAssert.assertTrue(
                loginPage.getLoginErrorMessage().contains("Username and password do not match lol")
        );
        log.info("Login failed for broken login");
        softAssert.assertAll();
    }

    @Test
    public void failedLogin() {
        loginPage.login("standard_user", "secret_sauce");

        softAssert.assertTrue(
                loginPage.getFirstItemPrice().contains("inventory")
        );
        log.info("Login failed");
        softAssert.assertAll();
    }

    @Test(dataProvider = "auth")
    public void DataDrivenLogin(String username, String password, String type) throws IOException, InvalidFormatException {
        loginPage.login(username, password);

        if (type.equals("fail")) {
            softAssert.assertTrue(false);
        }else if (type.equals("skip")) {
            throw new SkipException("Skipping test: Environment is offline.");
        }else {
            softAssert.assertTrue(true);
        }
        softAssert.assertAll();
    }

    @AfterMethod(alwaysRun = true)
    public void end() {
        tearDown();
        log.info("Test completed");
    }

    @DataProvider(name = "auth")
    public Object[][] loginDataProvider()  throws IOException, InvalidFormatException {
        // Provide the path to the Excel file and the sheet name
        return ExcelUtil.getTestData("auth.xlsx", "credentials");
    }
}