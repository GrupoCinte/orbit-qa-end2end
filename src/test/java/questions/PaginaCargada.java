
package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Visibility;
import pages.CruceFacturacionPage;

public class PaginaCargada implements Question<Boolean> {

    @Override
    public Boolean answeredBy(Actor actor) {
        return Visibility.of(CruceFacturacionPage.BTN_EJECUTAR_CRUCE)
                .answeredBy(actor);
    }

    public static PaginaCargada correctamente() {
        return new PaginaCargada();
    }
}
