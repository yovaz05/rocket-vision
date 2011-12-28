package cdo.sgd.controladores;

import sig.controladores.Sesion;
import sig.controladores.Vistas;
import cdo.sgd.modelo.bd.simulador.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;
import waytech.utilidades.UtilSIG;

/**
 * controlador asociado a vistaPerfil/CambiarCorreo
 * @author Gabriel Pérez
 */
public class CtrlCambiarCorreo extends GenericForwardComposer {

  //widgtes:
  Label txtEmail;
  //variables de control:
  int id;
  int idRed;
  //gestión de datos:
  BD datos;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    datos = new BD();
    buscarId();
    idRed = UtilSIG.buscarIdRed(this.getClass());
    mostrarEmailAnterior();
    notificarBarra();
  }


  
  //OJO: idUsuario debe ser seteado al momento del acceso del usuario
  /**
   * recupera parámetro de sesión 'idUsuario' que viene de la vista llamante
   */
  private void buscarId() throws InterruptedException {
    try {
      id = (Integer) Sesion.getVariable("idUsuario");
    } catch (Exception e) {
      System.out.println("CtrlCambiarCorreo -> ERROR: variable de sesión 'idUsuario'");
      id = 0;
    } finally {
      System.out.println("CtrlCambiarCorreo -> id = " + id);
    }
  }

  /**
   * muestra datos en la vista
   * por ahora son simulados  
   **/
  public void mostrarEmailAnterior() {
    Lider lider = datos.buscarLiderPorRed(idRed, id);
    /**/
    System.out.println("id = " + id);
    if (lider != null) {
      System.out.println("lider != null");
    }
    String email = lider.getEmail();
    System.out.println("email= " + email);
    txtEmail.setValue("" + lider.getEmail());
  }
  
  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.CUENTA_CAMBIAR_EMAIL);
    Sesion.setModo("operacion");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }   
  
  //referencias:
  Include vistaCentral;
  Include panelCentral;  
}