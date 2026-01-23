
package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.Before;

import net.serenitybdd.annotations.Managed;
import org.openqa.selenium.WebDriver;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

import tasks.Autenticarse;
import tasks.AbrirCruceFacturacion;
import tasks.AplicarFiltrosCruce;
import tasks.EjecutarCruce;

import questions.PaginaCargada;
import questions.ResultadosMostrados;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class CruceFacturacionSteps {

    // Actor de negocio
    private final Actor usuarioOrbit = Actor.named("Usuario ORBIT");

    // WebDriver administrado por Serenity (respeta serenity.properties)
    @Managed(driver = "chrome")
    WebDriver browser;

    // URL base por defecto (permitimos override con -Dbase.url)
    private final String baseUrl = System.getProperty(
            "base.url",
            "http://node206897-orbitcinte.w1-us.cloudjiffy.net:8080/ORBIT/GC/crucefacturacion.xhtml"
    );

    @Before
    public void setUp() {
        OnStage.setTheStage(new OnlineCast());
        usuarioOrbit.can(BrowseTheWeb.with(browser));
    }

    // =============== LOGIN SIN PARÁMETROS ===============
    @Given("que el usuario inicia sesión en ORBIT")
    public void iniciarSesion() {
        // Si existe login.url úsala; si no, base.url (redirección al login)
        String loginUrl = System.getProperty("login.url", baseUrl);

        // Credenciales: -Dlogin.user/-Dlogin.pass o variables de entorno LOGIN_USER/LOGIN_PASS
        String user = System.getProperty("login.user", System.getenv("LOGIN_USER"));
        String pass = System.getProperty("login.pass", System.getenv("LOGIN_PASS"));

        if (user == null || pass == null) {
            throw new IllegalArgumentException(
                    "Faltan credenciales: pasa -Dlogin.user y -Dlogin.pass o variables de entorno LOGIN_USER/LOGIN_PASS"
            );
        }

        usuarioOrbit.attemptsTo(Autenticarse.enOrbit(loginUrl, user, pass));
    }

    // =============== NAVEGAR A CRUCE ====================
    @Given("que el usuario abre la pantalla de cruce de facturación")
    public void abrirPantallaCruceFacturacion() {
        usuarioOrbit.attemptsTo(AbrirCruceFacturacion.desde(baseUrl));
        usuarioOrbit.should(seeThat(PaginaCargada.correctamente(), is(true)));
    }

    // =============== APLICAR FILTROS ====================
    @When("aplica filtros de fecha {string} a {string} con estado {string}")
    public void aplicarFiltros(String fechaDesde, String fechaHasta, String estado) {
        usuarioOrbit.attemptsTo(AplicarFiltrosCruce.con(fechaDesde, fechaHasta, estado));
    }

    // =============== EJECUTAR CRUCE =====================
    @When("ejecuta el cruce con los parámetros por defecto")
    public void ejecutarCruce() {
        usuarioOrbit.attemptsTo(EjecutarCruce.ahora());
    }

    // =============== VALIDAR RESULTADOS =================
    @Then("debería visualizar resultados en la pantalla")
    public void validarResultados() {
        usuarioOrbit.should(seeThat(ResultadosMostrados.enPantalla(), is(true)));
    }
}
