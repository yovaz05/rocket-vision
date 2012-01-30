/*
 * Clase de Utilería
 */
package rocket.controladores.general;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Include;
import org.zkoss.zul.Toolbarbutton;

/**
 *
 * @author Gabriel
 */
public class CtrlVista {

  //simula un click al botón btnControl
  //el cual hace el cambio de ventana en panelCentral
  //2da versión, usando metodo de Sesion

  public void forzarCambioVista_btnControl() {
    Include vistaCentral = Sesion.getVistaCentral();
    Toolbarbutton btnControl = (Toolbarbutton) vistaCentral.getFellow("btnControl");
    Events.postEvent(1, "onClick", btnControl, null);
  }

  public void notificarCambioVista_btnControl2() {
    Include vistaCentral = Sesion.getVistaCentral();
    Toolbarbutton btnControl = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl, null);
  }

}
