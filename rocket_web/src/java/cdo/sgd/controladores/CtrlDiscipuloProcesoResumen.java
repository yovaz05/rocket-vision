package cdo.sgd.controladores;

import cdo.sgd.modelo.bd.simulador.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

/**
 * controlador asociado a vistaDiscipuloProcesoResumen/Discipulo.zul
 * @author Gabriel Pérez
 */
public class CtrlDiscipuloProcesoResumen extends GenericForwardComposer {

  //widgets:
  Label etqNombre, etqDireccion, etqEdad;
  Toolbarbutton tbbTelefono, tbbEmail;
  A tbbRed, tbbLider1, tbbLider2, tbbVerMas;
  //BotonLider tbbLider1;
  //BotonLider tbbLider2;  
  //variables de control:
  CtrlVista ctrlVista;
  int id;
  //gestión de datos:
  BD datos;
  //referencias
  Include vistaCentral;
  Include panelCentral;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    datos = new BD();
    ctrlVista = new CtrlVista();
    buscarVarSesion();
    mostrarDatos();
    notificarBarra();
  }

  /**
   * recupera parámetro de sesión 'idDiscipulo' que viene de la vista llamante
   */
  private void buscarVarSesion() throws InterruptedException {
    try {
      id = (Integer) Sesion.getVariable("idDiscipulo");
      if (id == 0) {
        System.out.println("CtrlDiscipuloProcesoResumen -> ERROR: variable de sesión 'idDiscipulo' = 0");
      }
    } catch (Exception e) {
      System.out.println("CtrlDiscipuloProcesoResumen -> ERROR con variable de sesión 'idDiscipulo'");
    } finally {
      System.out.println("CtrlDiscipuloProcesoResumen -> variable de sesión idDiscipulo = " + id);
    }
  }

  /**
   * muestra datos en la vista, por ahora son simulados  
   **/
  public void mostrarDatos() {
    DiscipuloProceso discipulo = datos.buscarDiscipuloProceso(id);
    if (discipulo == null) {
      System.out.println("CtrlDiscipuloProcesoResumen - ERROR: Discipulo ES NULO");
    }
    if (discipulo.getNombre() == null) {
      System.out.println("CtrlDiscipuloProcesoResumen - ERROR: Discipulo.nombre es NULO");
      return;
    }
    if (discipulo.getNombreRed() == null) {
      System.out.println("CtrlDiscipuloProcesoResumen - ERROR: Discipulo.nombreRed es NULO");
      return;
    }
    final int idRed = discipulo.getIdRed();
    final int idLider1 = discipulo.getIdLider1();
    final int idLider2 = discipulo.getIdLider2();
    final String lider1 = discipulo.getNombreLider1();
    final String lider2 = discipulo.getNombreLider2();

    etqNombre.setValue("" + discipulo.getNombre());
    tbbRed.setLabel("" + discipulo.getNombreRed());
    tbbRed.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sessions.getCurrent().setAttribute("idRed", idRed);
        panelCentral.setSrc("vistaRed/Red.zul");
      }
    });

    tbbLider1.setLabel(lider1);
    tbbLider2.setLabel(lider2);

    tbbLider1.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sessions.getCurrent().setAttribute("idLider", idLider1);
        panelCentral.setSrc(Vistas.LIDER_RESUMEN);
        System.out.println("CtrlDiscipuloProceso -> idLider = " + idLider1);
      }
    });

    if ((idLider2 == 0) || (lider2 == null) || lider2.equals("") || lider2.equals("Pastora Irene de Mora")) {
      tbbLider2.setVisible(false);
    } else {
      tbbLider2.setLabel(lider2);
      tbbLider2.addEventListener(Events.ON_CLICK, new EventListener() {

        public void onEvent(Event event) throws Exception {
          Sesion.setVariable("idLider", idLider2);
          panelCentral.setSrc(Vistas.LIDER_RESUMEN);
        }
      });
    }

    etqDireccion.setValue(discipulo.getDireccionCorta());
    tbbTelefono.setLabel("" + discipulo.getTelefono());
    tbbEmail.setLabel("" + discipulo.getEmail());
    etqEdad.setValue("" + discipulo.getEdad());

    final int idDiscipulo = discipulo.getId();
    tbbVerMas.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        System.out.println("CtrlDiscipuloProcesoResumen -> idDiscipulo = " + idDiscipulo);
        Sesion.setVariable("idDiscipulo", idDiscipulo);
        Sesion.setVistaSiguiente(Vistas.DISCIPULO_PROCESO);
        Sesion.setModo("ver");
        ctrlVista.forzarCambioVista_btnControl();
      }
    });
  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.DISCIPULO_PROCESO_RESUMEN);
    Sesion.setModo("consulta");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }
}