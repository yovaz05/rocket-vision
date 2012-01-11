package cdo.sgd.controladores;

import sig.controladores.Sesion;
import sig.controladores.Vistas;
import sig.modelo.bd.simulador.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

/**
 * controlador asociado a vistaPerfil/CambiarCorreo
 * @author Gabriel Pérez
 */
public class CtrlCambiarPassword extends GenericForwardComposer {

  //widgets:
  Label txtEmail;
  //variables de control:
  int id;
  //gestión de datos:
  BD datos;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    datos = new BD();
    buscarVarSesion();
    notificarBarra();
  }

  //TODO: idUsuario debe ser seteado al momento del acceso del usuario
  /**
   * recupera parámetro de sesión 'idUsuario' que viene de la vista llamante
   */
  private void buscarVarSesion() throws InterruptedException {
    try {
      //TODO: evaluar qué se hará si no viene de navegación dinámica...
      if (Sessions.getCurrent().getAttribute("idUsuario") == null) {
        System.out.println("CtrlCambiarCorreo -> ERROR: variable de sesión 'idUsuario' = null");
        return;
      }
      id = (Integer) Sessions.getCurrent().getAttribute("idUsuario");
      if (id == 0) {
        System.out.println("CtrlCambiarCorreo -> ERROR: variable de sesión 'idUsuario' = 0");
        id = 0; //valor de prueba, datos en blanco
      }
    } catch (Exception e) {
      System.out.println("CtrlCambiarCorreo -> ERROR: variable de sesión 'idUsuario'");
    } finally {
      System.out.println("CtrlCambiarCorreo -> id = " + id);
    }
  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.CUENTA_CAMBIAR_PASSWORD);
    Sesion.setModo("operacion");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }   
  
  //referencias:
  Include vistaCentral;
  Include panelCentral;
}