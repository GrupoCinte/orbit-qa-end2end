package org.example.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class BaseTest {

    protected static WebDriver driver;

    public static void inicializarDriver() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            // Argumento obligatorio para versiones recientes de Chrome
            options.addArguments("--remote-allow-origins=*");

            // --- DETECCION DE ENTORNO CI/CD ---
            // GitHub Actions inyecta la variable "CI=true"
            String ciEnv = System.getenv("CI");

            if (ciEnv != null && !ciEnv.isEmpty()) {
                System.out.println("--> MODO CI DETECTADO: Ejecutando Chrome Headless (Sin Pantalla)");
                options.addArguments("--headless=new"); // Modo sin interfaz
                options.addArguments("--no-sandbox"); // Necesario para Linux/Docker
                options.addArguments("--disable-dev-shm-usage"); // Evita errores de memoria compartida
                options.addArguments("--window-size=1920,1080"); // Simula tamaño de pantalla
            } else {
                System.out.println("--> MODO LOCAL: Ejecutando con Interfaz Grafica");
                options.addArguments("--start-maximized");
            }

            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }

    public static void cerrarDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }
}