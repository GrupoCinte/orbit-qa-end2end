package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class ConciliacionPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- SELECTORES ---
    private By loadingOverlay = By.id("contenedor_carga");
    private By contenedorMenu = By.id("intro");
    private By btnMenuLateral = By.cssSelector("button[data-bs-target='#intro']");
    private By linkCruceFacturacion = By.cssSelector("a[href*='crucefacturacion']");
    private By filtroClienteDropdown = By.id("tablaFacturacion:cliFac:clienteFilter");
    private By btnBuscar = By.xpath("//button[contains(text(),'Buscar')]");

    // Tabla y Edición
    private By btnMoverDerecha = By.xpath("//button[@title='Agregar Cruce Seleccionado']");
    // Selector ajustado para asegurar que lo encuentre
    private By iconoEditarFilaDerecha = By.xpath("//div[contains(@id,'CruTabAgr')]//a[contains(@class,'ui-row-editor-pencil')]");
    private By iconoGuardarFilaDerecha = By.xpath("//div[contains(@id,'CruTabAgr')]//a[contains(@class,'ui-row-editor-check')]");
    private By inputMontoEdicion = By.xpath("//div[contains(@id,'CruTabAgr')]//input[contains(@id,'input')]");

    // Modal Final
    private By btnAbrirModalAplicar = By.xpath("//button[contains(.,'Aplicar') and contains(.,'Cruce')]");
    private By textareaObservacionFinal = By.xpath("//form[contains(@id,'frmConteoCruce')]//textarea");
    private By btnConfirmarCruceFinal = By.xpath("//form[contains(@id,'frmConteoCruce')]//button[contains(.,'Aplicar Cruce')]");

    // Alertas (SweetAlert2)
    private By swalTitle = By.id("swal2-title");

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
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
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
            try { new Select(driver.findElement(filtroClienteDropdown)).selectByIndex(1); } catch (Exception ex) {}
        }
        List<WebElement> botones = driver.findElements(btnBuscar);
        if (!botones.isEmpty()) clickJS(botones.get(0));
    }

    public boolean verificarResultadosTabla(String texto) {
        esperarCarga();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
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
                Thread.sleep(500);
            }
        } catch (Exception e) {}
        try {
            WebElement btnMover = wait.until(ExpectedConditions.elementToBeClickable(btnMoverDerecha));
            clickJS(btnMover);
            esperarCarga();
        } catch (Exception e) {}
    }

    // --- MÉTODO CORREGIDO CON SCROLL ---
    public void ingresarMonto(String monto) {
        esperarCarga();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // 1. Buscamos el lápiz (usamos 'presence' por si no está visible en pantalla aún)
        WebElement lapiz = wait.until(ExpectedConditions.presenceOfElementLocated(iconoEditarFilaDerecha));

        // 2. SCROLL OBLIGATORIO: Bajamos la pantalla hasta el elemento
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", lapiz);
        try { Thread.sleep(500); } catch (InterruptedException e) {}

        // 3. Ahora que ya hicimos scroll, hacemos click (usando JS para asegurar)
        wait.until(ExpectedConditions.elementToBeClickable(lapiz));
        clickJS(lapiz);

        // Continuamos con la edición...
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputMontoEdicion));
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.BACK_SPACE);
        input.sendKeys(monto);
        input.sendKeys(Keys.TAB);

        WebElement check = wait.until(ExpectedConditions.elementToBeClickable(iconoGuardarFilaDerecha));
        clickJS(check);
        esperarCarga();
    }
    // -----------------------------------

    public void clicAplicarCrucePrincipal() {
        try {
            WebElement btnAbrir = wait.until(ExpectedConditions.elementToBeClickable(btnAbrirModalAplicar));
            clickJS(btnAbrir);
            wait.until(ExpectedConditions.visibilityOfElementLocated(swalTitle));
        } catch (Exception e) {}
    }

    public void ingresarObservacion(String texto) {
        try {
            WebElement btnAbrir = wait.until(ExpectedConditions.elementToBeClickable(btnAbrirModalAplicar));
            clickJS(btnAbrir);
            Thread.sleep(1000);
            WebElement txtArea = wait.until(ExpectedConditions.visibilityOfElementLocated(textareaObservacionFinal));
            txtArea.click();
            txtArea.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            txtArea.sendKeys(Keys.BACK_SPACE);
            txtArea.sendKeys(texto);
        } catch (Exception e) {}
    }

    public void guardar() {
        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnConfirmarCruceFinal));
            clickJS(btn);
            Thread.sleep(3000);
        } catch (Exception e) {}
    }

    public String obtenerMensajeAlerta() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(swalTitle)).getText();
        } catch (Exception e) {
            return "No se encontro mensaje";
        }
    }

    public String obtenerSaldoDeTabla(String idFactura) {
        esperarCarga();
        try {
            WebElement btnOk = driver.findElement(By.cssSelector("button.swal2-confirm"));
            if(btnOk.isDisplayed()) clickJS(btnOk);
        } catch (Exception e) {}
        try { Thread.sleep(2000); } catch (Exception e) {}
        return driver.findElement(By.xpath("//tr[contains(.,'" + idFactura + "')]")).getText();
    }

    private void esperarCarga() {
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingOverlay)); } catch (Exception e) {}
    }

    private void clickJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}