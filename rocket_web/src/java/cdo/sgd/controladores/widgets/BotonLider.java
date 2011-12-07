/**
 * Botón que representa un enlace a un líder
 **/
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
public class BotonLider extends A {

  int idLider;
  CtrlVista ctrlVista = new CtrlVista();

  public BotonLider() {
  }

  /**
   * crea un botón de líder
   * @param etiqueta usado para el nombre del líder
   */public BotonLider(String etiqueta) {
    super(etiqueta);
    setEstilo();
  }

  private void programarEventoClick(final int id) {
    this.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        System.out.println("BotonLider -> id = " + id);

        //set variables de sesión:
        //TODO: descomentar esto, dirigir a LIDER_RESUMEN
//        Sesion.setVistaSiguiente(Vistas.LIDER_RESUMEN);
//        Sesion.setModo("consulta");
        Sesion.setVistaSiguiente(Vistas.LIDER);
        Sesion.setModo("ver");
        Sesion.setVariable("idLider", id);
        ctrlVista.forzarCambioVista_btnControl();
      }
    });
  }

  public void setIdLider(int id) {
    this.idLider = id;
    if ((idLider == 0)) {
      this.setVisible(false);
    } else {
      programarEventoClick(id);
      asignarTooltip("Ver detalles... idLider="+idLider);
    }
  }

  public void asignarTooltip(String texto) {
    this.setTooltiptext(texto);
  }

  //TODO: evaluar quitar 
  public int getIdLider() {
    return idLider;
  }

  /**
   * establece el estilo 'css' del botón
   */
  private void setEstilo() {
    this.setStyle("linkNaveg");
  }
}
