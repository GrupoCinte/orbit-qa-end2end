
package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.core.pages.WebElementFacade;
import pages.CruceFacturacionPage;

import java.util.List;

public class ResultadosMostrados implements Question<Boolean> {
    @Override
    public Boolean answeredBy(Actor actor) {
        // Consideramos "resultados" como que la tabla existe y tiene al menos una fila <tr> con <td>
        List<WebElementFacade> filas = CruceFacturacionPage.TABLA_RESULTADOS
                .resolveFor(actor)
                .thenFindAll(".//tr[td]");
        return filas != null && !filas.isEmpty();
    }
    public static ResultadosMostrados enPantalla() { return new ResultadosMostrados(); }
}
