package cdo.sgd.controladores;

import cdo.sgd.modelo.bd.simulador.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

/**
 * controlador asociado a Contacto.zul
 * @author Gabriel Pérez
 */
public class CtrlContacto extends GenericForwardComposer {

  //widgtes:
  Label txtComentario;
  Combobox cmbTipo;
  //variables de control:
  int idUsuario;
  //gestión de datos:
  BD datos;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    //- datos = new BD();
    buscarVarSesion();
    //mostrarEmailAnterior();
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
      idUsuario = (Integer) Sessions.getCurrent().getAttribute("idUsuario");
      if (idUsuario == 0) {
        System.out.println("CtrlCambiarCorreo -> ERROR: variable de sesión 'idUsuario' = 0");
        idUsuario = 0; //valor de prueba, datos en blanco
      }
    } catch (Exception e) {
      System.out.println("CtrlCambiarCorreo -> ERROR: variable de sesión 'idUsuario'");
    } finally {
      System.out.println("CtrlCambiarCorreo -> id = " + idUsuario);
    }
  }

  /**
   * muestra datos en la vista
   * por ahora son simulados  
   **
  public void mostrarEmailAnterior() {
    Lider lider = datos.buscarLider(idUsuario);
    System.out.println("id = " + idUsuario);
    if (lider != null) {
      System.out.println("lider != null");
    }
    String email = lider.getEmail();
    System.out.println("email= " + email);
    txtEmail.setValue("" + lider.getEmail());
  }
  /**/
  
  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.SISTEMA_CONTACTO);
    Sesion.setModo("operacion");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }   
  
  //referencias:
  Include vistaCentral;
  Include panelCentral;  
}