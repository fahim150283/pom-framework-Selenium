package com.framework.pages;

import com.framework.base.BaseTest;
import com.framework.utils.ElementTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    private static final Logger log = LogManager.getLogger(LoginPage.class);
    @FindBy(id = "user-name")
    private WebElement usernameInput;
    @FindBy(id = "password")
    private WebElement passwordInput;
    @FindBy(id = "login-button")
    private WebElement loginButton;
    @FindBy(css = "#inventory_container .inventory_item:first-child .inventory_item_price")
    private WebElement firstItemPrice;
    @FindBy(css = "[data-test='error']")
    private WebElement loginErrorMessage;

    public LoginPage() {
        PageFactory.initElements(BaseTest.driver, this);
    }

    public void enterUsername(String username) {
        ElementTracker.track(usernameInput);
        usernameInput.clear();
        usernameInput.sendKeys(username);
        log.info("Username entered: " + username);
    }

    public void enterPassword(String password) {
        ElementTracker.track(passwordInput);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        log.info("Password entered: " + password);
    }

    public void clickLogin() {
        ElementTracker.track(loginButton);
        loginButton.click();
        log.info("Login button clicked");
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        log.info("Login completed");
    }

    public String getFirstItemPrice() {
        return firstItemPrice.getText();
    }

    public boolean isInventoryVisible() {
        return firstItemPrice.isDisplayed();
    }

    public String getLoginErrorMessage() {
        return loginErrorMessage.getText();
    }
}