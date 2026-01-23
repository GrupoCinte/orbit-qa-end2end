
package pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class CruceFacturacionPage extends PageObject {

    public static final Target BTN_EJECUTAR_CRUCE = Target.the("botón ejecutar cruce")
            .located(By.xpath("//button[.//span[contains(normalize-space(.),'Ejecutar')]]"));

    public static final Target TABLA_RESULTADOS = Target.the("tabla de resultados")
            .located(By.xpath("//table[contains(@id,'resultado')]"));

    // 🆕 FILTROS

    public static final Target FECHA_DESDE = Target.the("campo fecha desde")
            .located(By.xpath("//input[contains(@id,'fechaDesde')]"));

    public static final Target FECHA_HASTA = Target.the("campo fecha hasta")
            .located(By.xpath("//input[contains(@id,'fechaHasta')]"));

    public static final Target ESTADO = Target.the("selector estado")
            .located(By.xpath("//select[contains(@id,'estado')]"));

}
