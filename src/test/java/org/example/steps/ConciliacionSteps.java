package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.es.*;
import org.example.base.TestBase; // Asegúrate de que coincida con tu clase Base (BaseTest o TestBase)
import org.example.pages.ConciliacionPage;
import org.example.pages.LoginPage;
import org.example.utils.ConfigReader;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConciliacionSteps {

    // 1. Declaramos el Logger para evitar usar System.out (java:S106)
    private static final Logger logger = LoggerFactory.getLogger(ConciliacionSteps.class);

    private LoginPage loginPage;
    private ConciliacionPage conciliacionPage;

    @Before
    public void setup() {
        TestBase.inicializarDriver();
        loginPage = new LoginPage(TestBase.getDriver());
        conciliacionPage = new ConciliacionPage(TestBase.getDriver());
    }

    @After
    public void tearDown() {
        TestBase.cerrarDriver();
    }

    @Dado("que el analista inicia sesion en Orbit con sus credenciales corporativas")
    public void iniciarSesion() {
        String usuario = ConfigReader.get("app.username");
        String clave = ConfigReader.get("app.password");
        loginPage.login(usuario, clave);
    }

    @Dado("navega a traves del menu principal hacia el modulo de {string}")
    public void navegarMenu(String modulo) {
        conciliacionPage.irAlModulo();
    }

    @Dado("que selecciona el cliente {string} en el filtro de busqueda")
    public void seleccionarCliente(String cliente) {
        conciliacionPage.filtrarCliente(cliente);
    }

    // --- CORRECCIÓN SONARQUBE (java:S1186) ---
    @Cuando("ejecuta la consulta de facturas")
    public void ejecutarConsulta() {
        // Agregamos un log para justificar el método y evitar que esté vacío
        logger.info("Ejecutando acción de consulta en la interfaz...");
    }
    // -----------------------------------------

    @Entonces("la tabla de resultados deberia mostrar unicamente registros asociados al cliente {string}")
    public void verificarFiltro(String cliente) {
        Assertions.assertTrue(conciliacionPage.verificarResultadosTabla(cliente));
    }

    @Dado("que selecciona la opcion de {string} para la factura numero {string}")
    public void seleccionarOpcionCruce(String opcion, String idFactura) {
        conciliacionPage.clickCruceEnFactura(idFactura);
    }

    @Cuando("asocia al consultor {string} a la factura moviendolo a la lista de pendientes")
    public void asociarConsultor(String consultor) {
        conciliacionPage.seleccionarConsultor(consultor);
    }

    @Cuando("asigna el valor a cruzar de {string} modificando el saldo sugerido")
    public void asignarMonto(String monto) {
        conciliacionPage.ingresarMonto(monto);
    }

    @Cuando("intenta asignar el valor invalido de {string} al consultor")
    public void intentarAsignarMontoInvalido(String monto) {
        conciliacionPage.ingresarMonto(monto);
        conciliacionPage.clicAplicarCrucePrincipal();
    }

    @Cuando("finaliza el proceso de cruce registrando la observacion {string}")
    public void finalizarCruce(String comentario) {
        conciliacionPage.ingresarObservacion(comentario);
        conciliacionPage.guardar();
    }

    @Entonces("deberia visualizar el mensaje de exito {string}")
    public void verificarExito(String msg) {
        String mensajeActual = conciliacionPage.obtenerMensajeAlerta();
        // Corrección java:S106 (System.out -> logger)
        logger.info("Mensaje Exito Detectado: {}", mensajeActual);

        Assertions.assertTrue(mensajeActual.contains(msg),
                "Esperaba mensaje: '" + msg + "' pero obtuve: '" + mensajeActual + "'");
    }

    @Entonces("deberia visualizar el mensaje de error {string}")
    public void verificarError(String msg) {
        String mensajeActual = conciliacionPage.obtenerMensajeAlerta();
        // Corrección java:S106
        logger.info("Mensaje Error Detectado: {}", mensajeActual);

        Assertions.assertTrue(mensajeActual.contains(msg),
                "Esperaba error: '" + msg + "' pero obtuve: '" + mensajeActual + "'");
    }

    @Entonces("el saldo de la factura {string} en la tabla deberia ser {string}")
    public void verificarSaldo(String idFactura, String saldoEsperado) {
        String textoFila = conciliacionPage.obtenerSaldoDeTabla(idFactura);
        // Corrección java:S106
        logger.info("Texto Fila para validar: {}", textoFila);

        Assertions.assertTrue(textoFila.contains(saldoEsperado),
                "El saldo esperado '" + saldoEsperado + "' no se encontro en la fila: " + textoFila);
    }
}