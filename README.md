A Maven-based Selenium automation framework using:

- Java 21
- Selenium WebDriver
- TestNG
- Allure Reporting
- WebDriverManager
- Page Object Model (POM)

---

## Project Structure

```text
src/
 ├── main/
 │   └── java/
 │       └── com/chatgpt/framework/
 │           ├── base/
 │           ├── pages/
 │           └── utils/
 │
 └── test/
     ├── java/
     │   └── com/chatgpt/framework/
     │       ├── tests/
     │       └── listeners/
     │
     └── resources/
         ├── config.properties
         ├── testdata/
         └── suites/
````

---

## Prerequisites

Make sure these are installed:

* Java 21
* Maven
* Allure Commandline

Check versions:

```bash
java --version
mvn --version
allure --version
```

---

## Run Tests

Run the full test suite:

```bash
mvn clean test
```

Run with a specific browser:

```bash
mvn clean test -Dbrowser=chrome
```

```bash
mvn clean test -Dbrowser=firefox
```

---

## Generate Allure Report

Generate the report:

```bash
mvn clean verify
```

---

## View Allure Report (Recommended)

Do **not** open `index.html` directly in the browser.

Serve the report using Allure:

```bash
allure serve target/site/allure-maven-plugin
```

This starts a local server and opens the report correctly.

---

## Important Note

Opening the report like this may cause errors:

```text
file:///.../index.html
```

Possible symptoms:

* `404`
* `[object Object]`
* missing test data

Always use:

```bash
allure serve target/site/allure-maven-plugin
```

---

## Report Location

Generated report files are available here:

```text
target/site/allure-maven-plugin/
```

Raw Allure result files are here:

```text
target/allure-results/
```

---

## Example Daily Workflow

```bash
mvn clean test
mvn clean verify
allure serve target/site/allure-maven-plugin
```

---

## Notes

* Browser is dynamic through system property.
* Default browser is controlled from `config.properties`.
* Test suite file is located in:

```text
src/test/resources/suites/testng.xml
```
