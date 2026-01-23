
package pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class LoginPage extends PageObject {

    public static final Target USUARIO = Target.the("campo usuario")
            .located(By.xpath(
                    "//input[ contains(@id,'usuario') or contains(@id,'user') or contains(@name,'usuario') ]"
            ));

    public static final Target CLAVE = Target.the("campo clave")
            .located(By.xpath(
                    "//input[ (@type='password') and (contains(@id,'clave') or contains(@id,'password') or contains(@name,'clave')) ]"
            ));

    public static final Target BTN_INGRESAR = Target.the("botón ingresar")
            .located(By.xpath(
                    "//button[ .//span[contains(normalize-space(.),'Ingresar')] or contains(normalize-space(.),'Ingresar') ]"
            ));

    // Si confirmas que hay overlay de carga tras el login, descomenta y ajusta:
    // public static final Target OVERLAY_BLOQUEO = Target.the("overlay de carga")
    //     .located(By.xpath("//div[contains(@class,'ui-blockui') and contains(@style,'display: block')]"));
}
