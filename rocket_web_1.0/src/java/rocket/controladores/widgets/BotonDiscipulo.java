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

/**
 *
 * @author Administrador
 */
public class BotonDiscipulo extends A {

  int idDiscipulo;
  CtrlVista ctrlVista = new CtrlVista();

  public BotonDiscipulo() {
  }

  public BotonDiscipulo(String etiqueta) {
    super(etiqueta);
    setEstilo();
  }

  private void programarEventoClick(final int idDiscipulo) {
    this.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        System.out.println("BotonDiscipulo -> id = " + idDiscipulo);

        //actualizar variables de sesión:
        //TODO: OJO+: enviar a resumen
        Sesion.setVariable("idDiscipulo", idDiscipulo);
        Sesion.setVistaSiguiente(Vistas.DISCIPULO_PROCESO_RESUMEN);
        Sesion.setModo("consulta");//modo visualización
        /*
        //usado para efectos de desarrollo
        Sesion.setVistaSiguiente(Vistas.DISCIPULO_PROCESO);
        Sesion.setModo("ver");//modo visualización
         * 
         */
        ctrlVista.forzarCambioVista_btnControl();
      }
    });
  }

  public void setIdDiscipulo(int idDiscipulo) {
    this.idDiscipulo = idDiscipulo;
    programarEventoClick(idDiscipulo);
    asignarTooltip("Ver detalles...");
  }

  public int getIdDiscipulo() {
    return idDiscipulo;
  }

  public void asignarTooltip(String texto) {
    this.setTooltiptext(texto);
  }

  private void setEstilo() {
    this.setStyle("linkNaveg");
  }
}
