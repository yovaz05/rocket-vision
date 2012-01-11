/**
 * Botón que representa un enlace a un líder
 **/
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
public class BotonLider extends A {

  int idLider;
  int idRed;
  CtrlVista ctrlVista = new CtrlVista();

  public BotonLider() {
  }

  /**
   * crea un botón de líder
   * @param etiqueta usado para el nombre del líder
   */
  public BotonLider(String etiqueta) {
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
        Sesion.setModo("edicion-dinamica");
        Sesion.setVariable("idLider", id);
        Sesion.setVariable("lider.idRed", idRed);
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
      asignarTooltip("Ver detalles");
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

  public int getIdRed() {
    return idRed;
  }

  public void setIdRed(int idRed) {
    this.idRed = idRed;
  }
}
