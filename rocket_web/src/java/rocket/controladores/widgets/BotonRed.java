/*
 * Botón que representa un enlace a una Red
 */
package rocket.controladores.widgets;

import cdo.sgd.controladores.CtrlVista;
import sig.controladores.Sesion;
import sig.controladores.Vistas;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Toolbarbutton;

/**
 *
 * @author Gabriel
 */
public class BotonRed extends Toolbarbutton {

  int idRed;
  CtrlVista ctrlVista = new CtrlVista();

  public BotonRed() {
  }

  public BotonRed(String etiqueta) {
    super(etiqueta);
  }

  public BotonRed(String etiqueta, String imagen) {
    super(etiqueta, imagen);
  }

  private void programarEventoClick(final int idRed) {
    this.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        System.out.println("BotonRed -> idRed = " + idRed);

        //actualizar variables de sesión:
        Sesion.setVistaSiguiente(Vistas.RED);
        Sesion.setVariable("idRed", idRed);
        Sesion.setModo("ver"); //modo visualización
        ctrlVista.forzarCambioVista_btnControl();
      }
    });
  }

  public void setIdRed(int idRed) {
    this.idRed = idRed;
    programarEventoClick(idRed);
    asignarTooltip("Ver detalles... idRed = " + idRed);
  }

  public int getIdRed() {
    return idRed;
  }

  public void asignarTooltip(String texto) {
    this.setTooltiptext(texto);
  }
}
