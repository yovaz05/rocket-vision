package cdo.sgd.controladores;

import cdo.sgd.modelo.bd.simulador.BD;
import cdo.sgd.modelo.bd.simulador.Red;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import waytech.utilidades.UtilSIG;

/**
 * controlador asociado a vistaRed
 * @author Gabriel
 */
public class CtrlRed extends GenericForwardComposer {

  //widgets:
  Label etqNombre, etqDiaHora;
  //Toolbarbutton tbbLider1, tbbLider2;
  A tbbLider1, tbbLider2;
  A tbbNroCelulas, tbbNroLideresCelula, tbbNroDiscipulosLanzados, tbbNroDiscipulosProceso;
  A btnResumen;
  Vbox vboxEstadisticas;
  //variables de control
  int id;
  //gestión de datos:
  BD bd;
  //referencias:
  Include vistaCentral;
  Include panelCentral;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    bd = new BD();
    id = UtilSIG.buscarIdRed(this.getClass());
    mostrarDatos();
    actualizarBoton();
    notificarBarra();
  }

  /**
   * muestra datos en la vista
   * por ahora son simulados  
   */
  public void mostrarDatos() throws InterruptedException {
    //llenar widgets con la data
    Red red = bd.buscarRed(id);
    //**
    if (red == null) {
      Messagebox.show("CtrlRed -> ERROR: red es null");
    }
    //**
    etqNombre.setValue(red.getNombre());
    tbbLider1.setLabel(red.getNombreLider1());
    tbbLider2.setLabel(red.getNombreLider2());
    etqDiaHora.setValue(red.getDiaHoraReunion());
    tbbNroCelulas.setLabel("" + red.getNroCelulas());
    tbbNroLideresCelula.setLabel("" + red.getNroLideresCelulas());
    tbbNroDiscipulosLanzados.setLabel("" + red.getNroDiscipulosLanzados());

    tbbNroDiscipulosProceso.setLabel("" + red.getNroDiscipulosProceso());

    /**/
    //se establecen parámetros para navegación dinámica:
    final int idLider1 = red.getIdLider1();
    final int idLider2 = red.getIdLider2();

    tbbLider1.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sesion.setVariable("idLider", idLider1);
        panelCentral.setSrc("vistaLider/Resumen.zul");
        System.out.println("CtrlRed -> idLider = " + idLider1);
      }
    });

    tbbLider2.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sesion.setVariable("idLider", idLider2);
        panelCentral.setSrc("vistaLider/Resumen.zul");
        System.out.println("CtrlRed -> idLider = " + idLider2);
      }
    });

    tbbNroCelulas.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        panelCentral.setSrc("vistaCelula/Listado.zul");
      }
    });
    tbbNroLideresCelula.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        panelCentral.setSrc("vistaLiderCelula/Listado.zul");
      }
    });
    tbbNroDiscipulosLanzados.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        panelCentral.setSrc("vistaLider/Listado.zul");
      }
    });
    tbbNroDiscipulosProceso.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        panelCentral.setSrc("vistaDiscipuloProceso/Listado.zul");
      }
    });
    /**/
  }

  /**
   * muestra u oculta el panel de Resumen
   */
  public void onClick$btnResumen() {
    vboxEstadisticas.setVisible(!vboxEstadisticas.isVisible());
    actualizarBoton();
  }

  private void actualizarBoton() {
    if (vboxEstadisticas.isVisible()) {
      btnResumen.setLabel("Ocultar Resumen");
    } else {
      btnResumen.setLabel("Ver Resumen");
    }
  }

  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.RED);
    Sesion.setModo("consulta");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }
  CtrlVista ctrlVista = new CtrlVista();
}
