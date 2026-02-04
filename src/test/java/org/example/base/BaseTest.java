package org.example.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class BaseTest {

    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void inicializarDriver() {
        if (driver.get() == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");

            String ciEnv = System.getenv("CI");
            if (ciEnv != null && !ciEnv.isEmpty()) {
                System.out.println("--> MODO CI PARALELO: Chrome Headless");
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920,1080");
            } else {
                System.out.println("--> MODO LOCAL");
                options.addArguments("--start-maximized");
            }

            // Guardamos el driver en el ThreadLocal
            driver.set(new ChromeDriver(options));
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }

    public static void cerrarDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    // Actualizamos el getter para sacar el driver de la caja fuerte
    public static WebDriver getDriver() {
        return driver.get();
    }
}