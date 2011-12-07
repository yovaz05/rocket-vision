/*
 * Botón que muestra una célula y enlaza a la vista de detalles de la célula
 */
package cdo.sgd.controladores.widgets;

import cdo.sgd.controladores.CtrlVista;
import cdo.sgd.controladores.Sesion;
import cdo.sgd.controladores.Vistas;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;

/**
 *
 * @author Administrador
 */
public class BotonCelula extends A {

  int idCelula;
  CtrlVista ctrlVista = new CtrlVista();

  public BotonCelula() {
  }

  public BotonCelula(String etiqueta) {
    super(etiqueta);
    setEstilo();
  }

  private void programarEventoClick(final int idCelula) {
    this.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        System.out.println("BotonCelula -> idCelula = " + idCelula);

        //actualizar variables de sesión:
        Sesion.setVistaSiguiente(Vistas.CELULA);
        Sesion.setVariable("idCelula", idCelula);
        Sesion.setModo("ver");//modo visualización
        ctrlVista.forzarCambioVista_btnControl();
      }
    });
  }

  public void setIdCelula(int idCelula) {
    this.idCelula = idCelula;
    programarEventoClick(idCelula);
    asignarTooltip("Ver detalles... idCelula=" + idCelula);
  }

  public int getIdCelula() {
    return idCelula;
  }

  private void asignarTooltip(String texto) {
    this.setTooltiptext(texto);
  }

  private void setEstilo() {
    this.setStyle("linkNaveg");
  }
}
