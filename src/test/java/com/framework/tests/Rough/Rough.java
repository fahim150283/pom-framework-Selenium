package com.framework.tests.Rough;

import com.framework.base.BaseTest;
import com.framework.utils.ExcelUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class Rough extends BaseTest {

    @DataProvider(name = "rough")
    public Object[][] provideLoginData() throws IOException, InvalidFormatException {
        // Provide the path to the Excel file and the sheet name
        return ExcelUtil.getTestData("rough.xlsx", "credentials");
    }

    @Test(dataProvider = "rough")
    public void rough(String username, String password) {

        System.out.println("The Username is " + username + " and the Password is " + password);
    }
}
