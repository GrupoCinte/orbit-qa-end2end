package org.example.pages;

import org.example.utils.ConfigReader; // Importamos el lector
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By usernameField = By.name("j_idt5:correo");
    private By passwordField = By.name("j_idt5:password");
    private By loginButton = By.name("j_idt5:button");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void login(String user, String pass) {
        // Leemos la URL del archivo config.properties
        String url = ConfigReader.get("app.url");
        driver.get(url);

        WebElement userElement = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        userElement.clear();
        userElement.sendKeys(user);

        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(pass);

        driver.findElement(loginButton).click();
    }
}