package org.example.pages;

import org.example.utils.ConfigReader;
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

    public void login(String userLocal, String passLocal) {
        String url = ConfigReader.get("app.url");
        driver.get(url);

        // --- LÓGICA DE SEGURIDAD (SECRETS) ---

        String finalUser = System.getenv("APP_USERNAME");
        String finalPass = System.getenv("APP_PASSWORD");

        if (finalUser == null || finalUser.isEmpty()) {
            System.out.println("⚠️ Modo Local: Usando credenciales de configuración.");
            finalUser = userLocal;
        } else {
            System.out.println("🔒 Modo CI: Usando credenciales seguras de GitHub.");
        }

        if (finalPass == null || finalPass.isEmpty()) {
            finalPass = passLocal;
        }

        // --- INTERACCIÓN CON EL NAVEGADOR ---

        WebElement userElement = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        userElement.clear();
        userElement.sendKeys(finalUser);

        WebElement passElement = driver.findElement(passwordField);
        passElement.clear();
        passElement.sendKeys(finalPass);

        driver.findElement(loginButton).click();
    }
}