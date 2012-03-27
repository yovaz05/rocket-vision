/**
 * Botón que representa un enlace a un líder
 **/
package rocket.controladores.widgets;

import rocket.controladores.general.CtrlVista;
import rocket.controladores.general.Sesion;
import rocket.controladores.general.Vistas;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import rocket.controladores.general.Modos;

/**
 *
 * @author Administrador
 */
public class BotonLider extends A {

  int idLider;
  int idRed;
  CtrlVista ctrlVista = new CtrlVista();
  String vistaTarget;  //vista a la que llevará el botón
  String modo; //modo de la vista target

  public BotonLider() {
  }

  /**
   * crea un botón de líder que dirigirá a vista 'maestro líder'
   * @param etiqueta usado para el nombre del líder
   */
  public BotonLider(String etiqueta) {
    super(etiqueta);
    //- setEstilo();
    //target y modo por defecto:
    vistaTarget = Vistas.LIDER;
    modo = Modos.EDICION_DINAMICA;
  }

  private void programarEventoClick(final int id) {
    this.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        //** System.out.println("BotonLider -> id = " + id);
        //set variables de sesión a ser usadas por vistaTarget
        Sesion.setVistaSiguiente(vistaTarget);
        Sesion.setModo(modo);
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

  public String getModo() {
    return modo;
  }

  public void setModo(String modo) {
    this.modo = modo;
    if (modo.equals(Modos.EDICION_DINAMICA)) {
      setVistaTarget(Vistas.LIDER); //maestro líder que permite edición
    } else if (modo.equals(Modos.CONSULTA)) {
      setVistaTarget(Vistas.LIDER_RESUMEN); //sólo resumen
    }
  }

  public String getVistaTarget() {
    return vistaTarget;
  }

  public void setVistaTarget(String vistaTarget) {
    this.vistaTarget = vistaTarget;
  }

  /**
   * establece el estilo 'css' del botón
   */
  //TODO: EVALUAR: ver si hace falta
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
