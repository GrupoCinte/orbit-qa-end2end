
package tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import pages.CruceFacturacionPage;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class EjecutarCruce implements Task {

    public static EjecutarCruce ahora() {
        return Tasks.instrumented(EjecutarCruce.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        actor.attemptsTo(
                WaitUntil.the(
                        CruceFacturacionPage.BTN_EJECUTAR_CRUCE,
                        isVisible()
                ).forNoMoreThan(10).seconds(),

                Click.on(CruceFacturacionPage.BTN_EJECUTAR_CRUCE)
        );
    }
}
