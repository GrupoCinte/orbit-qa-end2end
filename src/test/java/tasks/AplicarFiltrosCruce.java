
package tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import pages.CruceFacturacionPage;

public class AplicarFiltrosCruce implements Task {

    private final String desde;
    private final String hasta;
    private final String estado;

    public AplicarFiltrosCruce(String desde, String hasta, String estado) {
        this.desde = desde;
        this.hasta = hasta;
        this.estado = estado;
    }

    public static AplicarFiltrosCruce con(String desde, String hasta, String estado) {
        return Tasks.instrumented(AplicarFiltrosCruce.class, desde, hasta, estado);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(desde).into(CruceFacturacionPage.FECHA_DESDE),
                Enter.theValue(hasta).into(CruceFacturacionPage.FECHA_HASTA),
                SelectFromOptions.byVisibleText(estado).from(CruceFacturacionPage.ESTADO)
        );
    }
}
