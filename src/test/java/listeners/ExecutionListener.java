package listeners;

import com.framework.base.BaseTest;
import com.framework.pages.LoginPage;
import com.framework.utils.ElementTracker;
import com.framework.utils.EmailUtil;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.testng.IExecutionListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ExecutionListener extends BaseTest implements IExecutionListener, ITestListener {

    private static final Logger log = LogManager.getLogger(LoginPage.class);
    private static int passed = 0;
    private static int failed = 0;
    private static int skipped = 0;


    @Override
    public void onTestSuccess(ITestResult result) {
        passed++;
        log.info("Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failed++;
        System.out.println("Test Failed: Capturing Screenshot...");
        log.info("Test Failed: Capturing Screenshot...");

        WebDriver driver = BaseTest.driver;

        if (driver == null) return;

        byte[] screenshot = captureScreenshot(driver);

        if (screenshot != null && screenshot.length > 0) {
            Allure.getLifecycle().addAttachment(
                    "Failure Screenshot",
                    "image/png",
                    "png",
                    screenshot
            );
        }
    }

    /**
     * Captures screenshot + optional element highlight
     */
    public byte[] captureScreenshot(WebDriver driver) {
        try {
            byte[] raw = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(raw));

            if (image == null) return raw;

            Graphics2D g = image.createGraphics();

            WebElement element = ElementTracker.get();

            if (element != null) {
                try {
                    Point p = element.getLocation();
                    Dimension d = element.getSize();

                    g.setStroke(new BasicStroke(4));
                    g.setColor(Color.RED);
                    g.drawRect(p.getX(), p.getY(), d.getWidth(), d.getHeight());

                    g.drawString("Issue here", p.getX(), Math.max(15, p.getY() - 5));

                } catch (Exception ignored) {
                    // ignore element drawing issues
                }
            }

            g.dispose();

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(image, "png", output);

            log.info("Screenshot captured successfully.");
            return output.toByteArray();

        } catch (Exception e) {
            log.error("Failed to capture screenshot.");
            e.printStackTrace();
            return null;
        } finally {
            ElementTracker.clear();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skipped++;
    }

    @Override
    public void onExecutionFinish() {
        int total = passed + failed + skipped;

        serveAllureReport();

        try {
            EmailUtil.sendExecutionReport(total, passed, failed, skipped);
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }
    }

    private void serveAllureReport() {
        try {
            loadConfig();  // this is for reading the config.properties file
            String allurebatPath = prop.getProperty("allure.bat.path");

            // 1. First, generate the physical folder structure
            ProcessBuilder generateBuilder = new ProcessBuilder(
                    allurebatPath, "generate", "target/allure-results", "-o", "target/allure-report", "--clean"
            );
            generateBuilder.inheritIO().start().waitFor();

            // 2. Extract and print your absolute path
            java.io.File reportDir = new java.io.File("target/allure-report");
            String absolutePath = reportDir.getAbsolutePath();
            System.out.println("=========================================================================");
            log.info("Allure Report generated locally at: " + absolutePath);
            System.out.println("=========================================================================");

            // 3. Open a local web server explicitly targeting that folder so the data loads
            ProcessBuilder openBuilder = new ProcessBuilder(
                    allurebatPath, "open", "target/allure-report"
            );
            openBuilder.start(); // Do not use .waitFor() here, or your test suite execution won't finish!

        } catch (Exception e) {
            log.error("Failed to process Allure report.", e);
        }
    }
}