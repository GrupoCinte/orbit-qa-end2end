package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class ConciliacionPage {
    private static final Logger logger = LoggerFactory.getLogger(ConciliacionPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- SELECTORES ---
    private final By loadingOverlay = By.id("contenedor_carga");
    private final By contenedorMenu = By.id("intro");
    private final By btnMenuLateral = By.cssSelector("button[data-bs-target='#intro']");
    private final By linkCruceFacturacion = By.cssSelector("a[href*='crucefacturacion']");
    private final By filtroClienteDropdown = By.id("tablaFacturacion:cliFac:clienteFilter");
    private final By btnBuscar = By.xpath("//button[contains(text(),'Buscar')]");

    // Tabla y Edición
    private final By btnMoverDerecha = By.xpath("//button[@title='Agregar Cruce Seleccionado']");
    private final By iconoEditarFilaDerecha = By.xpath("//div[contains(@id,'CruTabAgr')]//a[contains(@class,'ui-row-editor-pencil')]");
    private final By iconoGuardarFilaDerecha = By.xpath("//div[contains(@id,'CruTabAgr')]//a[contains(@class,'ui-row-editor-check')]");
    private final By inputMontoEdicion = By.xpath("//div[contains(@id,'CruTabAgr')]//input[contains(@id,'input')]");

    // Modal Final
    private final By btnAbrirModalAplicar = By.xpath("//button[contains(.,'Aplicar') and contains(.,'Cruce')]");
    private final By textareaObservacionFinal = By.xpath("//form[contains(@id,'frmConteoCruce')]//textarea");
    private final By btnConfirmarCruceFinal = By.xpath("//form[contains(@id,'frmConteoCruce')]//button[contains(.,'Aplicar Cruce')]");

    // Alertas (SweetAlert2)
    private final By swalTitle = By.id("swal2-title");
    private final By swalConfirm = By.cssSelector("button.swal2-confirm");

    public ConciliacionPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // --- ACCIONES ---

    public void irAlModulo() {
        esperarCarga();
        WebElement menu = driver.findElement(contenedorMenu);
        if (!menu.getAttribute("class").contains("show")) {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnMenuLateral));
            clickJS(btn);
            esperarAnimacion(1000);
        }
        WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(linkCruceFacturacion));
        clickJS(link);
    }

    public void filtrarCliente(String cliente) {
        esperarCarga();
        try {
            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(filtroClienteDropdown));
            Select select = new Select(dropdown);
            select.selectByVisibleText(cliente);
            esperarCarga();
        } catch (Exception e) {
            logger.warn("No se pudo filtrar por texto, intentando por índice. Error: {}", e.getMessage());
            try {
                new Select(driver.findElement(filtroClienteDropdown)).selectByIndex(1);
            } catch (Exception ex) {
                logger.error("Fallo crítico al seleccionar cliente", ex);
            }
        }
        List<WebElement> botones = driver.findElements(btnBuscar);
        if (!botones.isEmpty()) clickJS(botones.get(0));
    }

    public boolean verificarResultadosTabla(String texto) {
        esperarCarga();
        esperarAnimacion(1000);
        return driver.getPageSource().toLowerCase().contains(texto.toLowerCase());
    }

    public void clickCruceEnFactura(String idFactura) {
        esperarCarga();
        String xpathBtn = "//tr[contains(.,'" + idFactura + "')]//button[@title='Realizar cruce']";
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathBtn)));
        clickJS(btn);
    }

    public void seleccionarConsultor(String nombreConsultor) {
        esperarCarga();
        String xpathCheckbox = "//tbody//tr[contains(.,'" + nombreConsultor + "')]//div[contains(@class,'ui-chkbox-box')]";
        try {
            WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathCheckbox)));
            if (!checkbox.getAttribute("class").contains("ui-state-active")) {
                clickJS(checkbox);
                esperarAnimacion(500);
            }
        } catch (Exception e) {
            logger.debug("Consultor no encontrado o ya seleccionado: {}", e.getMessage());
        }
        try {
            WebElement btnMover = wait.until(ExpectedConditions.elementToBeClickable(btnMoverDerecha));
            clickJS(btnMover);
            esperarCarga();
        } catch (Exception e) {
            logger.debug("Botón mover no clickeable: {}", e.getMessage());
        }
    }

    public void ingresarMonto(String monto) {
        esperarCarga();
        esperarAnimacion(1000);

        WebElement lapiz = wait.until(ExpectedConditions.presenceOfElementLocated(iconoEditarFilaDerecha));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", lapiz);

        esperarAnimacion(500);

        wait.until(ExpectedConditions.elementToBeClickable(lapiz));
        clickJS(lapiz);

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputMontoEdicion));
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE, monto, Keys.TAB);

        WebElement check = wait.until(ExpectedConditions.elementToBeClickable(iconoGuardarFilaDerecha));
        clickJS(check);
        esperarCarga();
    }

    public void clicAplicarCrucePrincipal() {
        try {
            WebElement btnAbrir = wait.until(ExpectedConditions.elementToBeClickable(btnAbrirModalAplicar));
            clickJS(btnAbrir);
            wait.until(ExpectedConditions.visibilityOfElementLocated(swalTitle));
        } catch (Exception e) {
            logger.warn("No se pudo abrir el modal de cruce: {}", e.getMessage());
        }
    }

    public void ingresarObservacion(String texto) {
        try {
            WebElement btnAbrir = wait.until(ExpectedConditions.elementToBeClickable(btnAbrirModalAplicar));
            clickJS(btnAbrir);
            esperarAnimacion(1000);

            WebElement txtArea = wait.until(ExpectedConditions.visibilityOfElementLocated(textareaObservacionFinal));
            txtArea.click();
            txtArea.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE, texto);
        } catch (Exception e) {
            logger.error("Error al ingresar observación: {}", e.getMessage());
        }
    }

    public void guardar() {
        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnConfirmarCruceFinal));
            clickJS(btn);
            esperarAnimacion(3000);
        } catch (Exception e) {
            logger.error("Error al guardar el cruce: {}", e.getMessage());
        }
    }

    public String obtenerMensajeAlerta() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(swalTitle)).getText();
        } catch (Exception e) {
            logger.debug("Alerta no encontrada: {}", e.getMessage());
            return "No se encontro mensaje";
        }
    }

    public String obtenerSaldoDeTabla(String idFactura) {
        esperarCarga();
        try {
            List<WebElement> confirms = driver.findElements(swalConfirm);
            if (!confirms.isEmpty() && confirms.get(0).isDisplayed()) {
                clickJS(confirms.get(0));
            }
        } catch (Exception e) {
            logger.debug("No había alerta para cerrar: {}", e.getMessage());
        }
        esperarAnimacion(2000);
        return driver.findElement(By.xpath("//tr[contains(.,'" + idFactura + "')]")).getText();
    }

    private void esperarCarga() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingOverlay));
        } catch (Exception e) {
            logger.trace("El overlay de carga no desapareció o no existía");
        }
    }

    private void clickJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Método auxiliar para manejar sleeps inevitables sin ensuciar Sonar
    private void esperarAnimacion(long millis) {
        try {
            Thread.sleep(millis); // NOSONAR: Espera requerida por animación JS de la UI
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Espera interrumpida", e);
        }
    }
}