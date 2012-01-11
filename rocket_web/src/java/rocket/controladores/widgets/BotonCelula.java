/*
 * Botón que muestra una célula y enlaza a la vista de detalles de la célula
 */
package rocket.controladores.widgets;

import cdo.sgd.controladores.CtrlVista;
import sig.controladores.Sesion;
import sig.controladores.Vistas;
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
  int idRed;
  int nroLideres;
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
        Sesion.setVariable("celula.idRed", idRed);
        Sesion.setVariable("celula.nLideres", nroLideres);
        Sesion.setModo("edicion-dinamica");  //modo visualización, con edición dinámica
        ctrlVista.forzarCambioVista_btnControl();
      }
    });
  }

  public void setIdCelula(int idCelula) {
    this.idCelula = idCelula;
    programarEventoClick(idCelula);
    asignarTooltip("Ver detalles");
  }

  public int getIdCelula() {
    return idCelula;
  }

  public int getIdRed() {
    return idRed;
  }

  public void setIdRed(int idRed) {
    this.idRed = idRed;
  }

  public int getNroLideres() {
    return nroLideres;
  }

  public void setNroLideres(int nLideres) {
    this.nroLideres = nLideres;
  }
  
  private void asignarTooltip(String texto) {
    this.setTooltiptext(texto);
  }

  private void setEstilo() {
    this.setStyle("linkNaveg");
  }
}
