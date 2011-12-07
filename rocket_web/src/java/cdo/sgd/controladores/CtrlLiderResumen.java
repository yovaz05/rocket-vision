package cdo.sgd.controladores;

import cdo.sgd.modelo.bd.simulador.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

/**
 * controlador asociado a vistaLider/Resumen.zul
 * @author Gabriel Pérez
 */
public class CtrlLiderResumen extends GenericForwardComposer {

  //widgets:
  Label etqNombre, etqDireccion, etqEdad;
  A tbbRed;
  A tbbLider1, tbbLider2, tbbPareja;
  Toolbarbutton tbbTelefono, tbbEmail;
  A tbbVerMas;
  //variables de control:
  CtrlVista ctrlVista = new CtrlVista();
  int id;
  //gestión de datos:
  BD datos = new BD();
  //referencias
  Include vistaCentral;
  Include panelCentral;
  A linkPareja;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    buscarVarSesion();
    mostrarDatos();
    notificarBarra();
  }

  /**
   * recupera parámetro de sesión 'idLider' que viene de la vista llamante
   */
  private void buscarVarSesion() throws InterruptedException {
    try {
      id = (Integer) Sesion.getVariable("idLider");
      if (id == 0) {
        System.out.println("CtrlLiderResumen -> ERROR: variable de sesión 'idLider' = 0");
      }
    } catch (Exception e) {
      System.out.println("CtrlLiderResumen -> ERROR con variable de sesión 'idLider'");
    } finally {
      System.out.println("CtrlLiderResumen -> variable de sesión idLider = " + id);
    }
  }

  /**
   * muestra datos en la vista
   * por ahora son simulados  
   **/
  public void mostrarDatos() {
    Lider lider = datos.buscarLider(id);
    //*
    if (lider != null) {
      System.out.println("lider != null");
    }
    //**
    String red = lider.getNombreRed();
    String lider1 = lider.getNombreLider1();
    String lider2 = lider.getNombreLider2();
    String pareja = lider.getNombreParejaMinisterial();
    final int idRed = lider.getIdRed();
    final int idLider1 = lider.getIdLider1();
    final int idLider2 = lider.getIdLider2();
    final int idPareja = lider.getIdParejaMinisterial();

    System.out.println("lider1: " + lider1);
    System.out.println("lider2: " + lider2);
    System.out.println("red: " + red);
    
    /*
    if (1 == 1) {
      System.out.println("SALIDA FORZADA");
      return;
    }
     * 
     */

    etqNombre.setValue("" + lider.getNombre());
    tbbRed.setLabel("" + red);
    tbbRed.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sesion.setVariable("idRed", idRed);
        panelCentral.setSrc(Vistas.RED);
      }
    });

    if ((lider1 == null) || (lider1.isEmpty())) {
      tbbLider1.setLabel("");
      tbbLider1.setVisible(false);
    } else if (lider1.equals("Pastor Rogelio Mora") || lider1.equals("Pastora Irene de Mora")) {
      tbbLider1.setLabel(lider1);
      tbbLider1.setHref(null);
      tbbLider1.setTooltiptext("Enlace no funcional");
    } else {
      tbbLider1.setLabel(lider1);
      tbbLider1.addEventListener(Events.ON_CLICK, new EventListener() {

        public void onEvent(Event event) throws Exception {
          Sesion.setVariable("idLider", idLider1);
          Sesion.setVistaSiguiente(Vistas.LIDER_RESUMEN);
          Sesion.setModo("consulta");
          ctrlVista.forzarCambioVista_btnControl();
        }
      });
    }

    if ((lider2 == null) || (lider2.isEmpty())) {
      tbbLider2.setLabel("");
      tbbLider2.setVisible(false);
    } else {
      tbbLider2.setLabel(lider2);
      tbbLider2.addEventListener(Events.ON_CLICK, new EventListener() {

        public void onEvent(Event event) throws Exception {
          Sesion.setVariable("idLider", idLider2);
          Sesion.setVistaSiguiente(Vistas.LIDER_RESUMEN);
          Sesion.setModo("consulta");
          ctrlVista.forzarCambioVista_btnControl();
        }
      });
    }

    //pareja:
    if ((idPareja == 0) || (pareja.isEmpty())) {
      tbbPareja.setLabel("No tiene");
    } else {
      tbbPareja.setLabel(pareja);
      tbbPareja.addEventListener(Events.ON_CLICK, new EventListener() {

        public void onEvent(Event event) throws Exception {
          Sesion.setVariable("idLider", idPareja);
          Sesion.setVistaSiguiente(Vistas.LIDER_RESUMEN);
          Sesion.setModo("consulta");
          ctrlVista.forzarCambioVista_btnControl();
        }
      });
    }

    //prueba con widget tipo A:
    if ((idPareja == 0) || (pareja.isEmpty())) {
      linkPareja.setLabel("No tiene");
    } else {
      linkPareja.setLabel(pareja);
      linkPareja.addEventListener(Events.ON_CLICK, new EventListener() {

        public void onEvent(Event event) throws Exception {
          Sesion.setVariable("idLider", idPareja);
          Sesion.setVistaSiguiente(Vistas.LIDER_RESUMEN);
          Sesion.setModo("consulta");
          ctrlVista.forzarCambioVista_btnControl();
        }
      });
    }

    etqDireccion.setValue(lider.getDireccionCorta());
    tbbTelefono.setLabel("" + lider.getTelefono());
    tbbEmail.setLabel("" + lider.getEmail());
    etqEdad.setValue("" + lider.getEdad());

    final int idLider = lider.getId();
    tbbVerMas.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sesion.setVariable("idLider", idLider);
        Sesion.setVistaSiguiente(Vistas.LIDER);
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
    Sesion.setVistaActual(Vistas.LIDER_RESUMEN);
    Sesion.setModo("consulta");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }
}