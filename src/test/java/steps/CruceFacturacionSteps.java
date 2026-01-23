package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.Before;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.annotations.Managed;
import org.openqa.selenium.WebDriver;

import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

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

    // WebDriver administrado por Serenity
    @Managed
    WebDriver browser;

    // URL (permite override con -Dbase.url=...)
    private final String baseUrl = System.getProperty(
            "base.url",
            "http://node206897-orbitcinte.w1-us.cloudjiffy.net:8080/ORBIT/GC/crucefacturacion.xhtml"
    );

    // ===== Hook Cucumber: antes de cada escenario =====
    @Before
    public void setUp() {
        OnStage.setTheStage(new OnlineCast());
        usuarioOrbit.can(BrowseTheWeb.with(browser));
    }

    // ========================= GIVEN =========================
    @Given("que el usuario abre la pantalla de cruce de facturación")
    public void abrirPantallaCruceFacturacion() {
        usuarioOrbit.attemptsTo(AbrirCruceFacturacion.desde(baseUrl));
        usuarioOrbit.should(seeThat(PaginaCargada.correctamente(), is(true)));
    }

    // ========================= WHEN =========================
    @When("aplica filtros de fecha {string} a {string} con estado {string}")
    public void aplicarFiltros(String fechaDesde, String fechaHasta, String estado) {
        usuarioOrbit.attemptsTo(AplicarFiltrosCruce.con(fechaDesde, fechaHasta, estado));
    }

    @When("ejecuta el cruce con los parámetros por defecto")
    public void ejecutarCruce() {
        usuarioOrbit.attemptsTo(EjecutarCruce.ahora());
    }

    // ========================= THEN =========================
    @Then("debería visualizar resultados en la pantalla")
    public void validarResultados() {
        usuarioOrbit.should(seeThat(ResultadosMostrados.enPantalla(), is(true)));
    }
}
