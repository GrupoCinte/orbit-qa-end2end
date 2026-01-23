
package tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Open;

public class AbrirCruceFacturacion implements Task {

    private final String url;

    public AbrirCruceFacturacion(String url) { this.url = url; }

    public static AbrirCruceFacturacion desde(String url) {
        return Tasks.instrumented(AbrirCruceFacturacion.class, url);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.url(url));
    }
}
