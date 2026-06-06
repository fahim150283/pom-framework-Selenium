package com.framework.utils;

import com.framework.pages.LoginPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EmailUtil {

    private static final Logger log = LogManager.getLogger(LoginPage.class);

    private static Properties prop;

    private static String SMTP_HOST;
    private static String SMTP_PORT;

    private static String USERNAME;
    private static String PASSWORD;

    private static String FROM;
    private static String TO;


    public static void loadConfig() {
        prop = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/email.properties")) {
            prop.load(fis);

            SMTP_HOST = prop.getProperty("SMTP_HOST");
            SMTP_PORT = prop.getProperty("SMTP_PORT");
            USERNAME = prop.getProperty("USERNAME");
            PASSWORD = prop.getProperty("PASSWORD");
            FROM = prop.getProperty("FROM");
            TO = prop.getProperty("TO");

        } catch (IOException e) {
            throw new RuntimeException("Unable to load email.properties", e);
        }
    }


    public static void sendExecutionReport(int total, int passed, int failed, int skipped) {

        loadConfig();
        String subject = "Automation Execution is Completed for " + System.getProperty("project.name");

        String body = String.format(
                """
                        Hello,
                        
                        %s.
                        
                        Total Executed : %d
                        Passed         : %d
                        Failed         : %d
                        Skipped         : %d
                        
                        
                        Regards,
                        Automated Tests from SQA
                        """,
                subject, total, passed, failed
        );

        sendMail(subject, body);
    }

    private static void sendMail(String subject, String body) {

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(
                props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                }
        );

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(FROM));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(TO)
            );

            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully for Execution report.");
            log.info("Email sent successfully for Execution report.");

        } catch (MessagingException e) {
            System.out.println("Failed to send email report.");
            log.error(e + " Failed to send email report for Execution report.");
        }
    }
}