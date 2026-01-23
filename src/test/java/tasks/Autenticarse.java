
package tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Click;

import net.serenitybdd.screenplay.waits.WaitUntil;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;

import pages.LoginPage;

/**
 * Task de autenticación para ORBIT:
 * - Abre la URL de login (o base)
 * - Ingresa usuario y contraseña
 * - Presiona el botón "Ingresar"
 * - (Opcional) espera breve por AJAX/overlay si la página lo usa
 */
public class Autenticarse implements Task {

    private final String baseUrlLogin;
    private final String usuario;
    private final String clave;

    public Autenticarse(String baseUrlLogin, String usuario, String clave) {
        this.baseUrlLogin = baseUrlLogin;
        this.usuario = usuario;
        this.clave = clave;
    }

    public static Autenticarse enOrbit(String baseUrlLogin, String usuario, String clave) {
        return Tasks.instrumented(Autenticarse.class, baseUrlLogin, usuario, clave);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        actor.attemptsTo(
                // 1) Abrir la página (si redirige al login, igual funciona)
                Open.url(baseUrlLogin),

                // 2) Esperar a que el campo usuario esté visible (JSF puede tardar)
                WaitUntil.the(LoginPage.USUARIO, isVisible())
                        .forNoMoreThan(15).seconds(),

                // 3) Diligenciar credenciales
                Enter.theValue(usuario).into(LoginPage.USUARIO),
                Enter.theValue(clave).into(LoginPage.CLAVE),

                // 4) Click en "Ingresar"
                Click.on(LoginPage.BTN_INGRESAR)

                // 5) Si tu pantalla muestra overlay de PrimeFaces, puedes añadir un wait extra:
                // WaitUntil.the(LoginPage.OVERLAY_BLOQUEO, isNotVisible())
                //          .forNoMoreThan(20).seconds()
                //          .ignoringExceptions()
        );
    }
}
