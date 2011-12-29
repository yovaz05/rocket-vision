package cdo.sgd.controladores;

import sig.controladores.Sesion;
import sig.controladores.Vistas;
import cdo.sgd.controladores.widgets.BotonCelula;
import cdo.sgd.controladores.widgets.BotonLider;
import cdo.sgd.controladores.widgets.EtqNro;
import cdo.sgd.modelo.bd.simulador.BD;
import cdo.sgd.modelo.bd.simulador.ReporteCelula;
import cdo.sgd.modelo.bd.simulador.ReporteCelulaListado;
import java.util.ArrayList;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import waytech.utilidades.Util;

/**
 * controlador asociado a vistaReporteCelula/Listado.zul
 * @author Gabriel
 */
public class CtrlReporteCelulaListadoAnteriores extends GenericForwardComposer {

  //widgets:
  Grid grid;
  Label etqNro, etqDireccion, etqDiaHora, etqNombre;
  BotonCelula tbbCodigo;
  BotonLider tbbLider1, tbbLider2;
  Component tbbEstatus1;
  Component tbbEstatus2;
  Component tbbEstatus3;
  Component tbbEstatus4;
  Component tbbEstatus5;
  //Column colEstadosReporte;
  //datos:
  ArrayList lista;
  BD bd;
  //variable de control:
  int idRed;
  int idCelula;
  int idLider1;
  int idLider2;
  int estatus;
  CtrlVista ctrlVista = new CtrlVista();
  //referencias:
  Include vistaCentral;
  Include panelCentral;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() {
    idRed = Util.buscarIdRed(this.getClass());
    mostrarDatos();
    notificarBarra();
  }

  public void mostrarDatos() {
    //TODO:verificar tipo de usuario para ver qué data buscar y mostrar
    bd = new BD();
    lista = bd.getReportesCelulaPorRed(idRed);
    System.out.println("CtrlReporteListado.mostrarDatos() -> lista de datos. size = " + lista.size());
    ListModelList model = new ListModelList(lista);
    grid.setModel(model);
    grid.setRowRenderer(new RowRenderer() {

      public void render(Row row, Object data) throws Exception {
        ReporteCelulaListado reporte = (ReporteCelulaListado) data;
        //se crean los widgets con la data
        etqNro = new EtqNro("" + reporte.getNroItem());
        etqDireccion = new Label("" + reporte.getDireccionCorta());
        etqDiaHora = new Label("" + reporte.getDia());
        etqNombre = new Label("" + reporte.getNombre());
        tbbCodigo = new BotonCelula("" + reporte.getCodigo());
        tbbLider1 = new BotonLider("" + reporte.getNombreLider1());
        tbbLider2 = new BotonLider("" + reporte.getNombreLider2());

        //se establecen parámetros para navegación dinámica:
        idCelula = reporte.getIdCelula();
        idLider1 = reporte.getIdLider1();
        idLider2 = reporte.getIdLider2();

        tbbCodigo.setIdCelula(idCelula);
        tbbLider1.setIdLider(idLider1);
        tbbLider2.setIdLider(idLider2);

        estatus = reporte.getEstatus();
        tbbEstatus1 = configurarEstatus(tbbEstatus1, estatus);
        tbbEstatus2 = configurarEstatus(tbbEstatus2, estatus);
        tbbEstatus3 = configurarEstatus(tbbEstatus3, estatus);
        tbbEstatus4 = configurarEstatus(tbbEstatus4, estatus);
        tbbEstatus5 = configurarEstatus(tbbEstatus5, estatus);

        //se anexan los widgets a la fila
        etqNro.setParent(row);
        tbbCodigo.setParent(row);
        etqDireccion.setParent(row);
        Vbox vbox = new Vbox();
        tbbLider1.setParent(vbox);
        tbbLider2.setParent(vbox);
        vbox.setParent(row);
        //etqDiaHora.setParent(row);
        tbbEstatus1.setParent(row);
        tbbEstatus2.setParent(row);
        tbbEstatus3.setParent(row);
        tbbEstatus4.setParent(row);
        tbbEstatus5.setParent(row);
      }
    });
  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.REPORTE_CELULA_LISTADO_SEMANA);
    Sesion.setModo("consulta");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }

  /**
   * Redirige a la vista de estadísticas detalladas, donde se pueden elegir las fechas a conveniencia
   */
  public void onClick$btnReportesAnteriores() {
    Sesion.setVistaActual(Vistas.REPORTE_CELULA_LISTADO_SEMANA);
    Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA_LISTADO_ANTERIORES);
    //forzar cambio de vista:
    ctrlVista.forzarCambioVista_btnControl();
  }

  /**
   * configura el botón o etiqueta de estado
   * botón para los estados: ReporteIngresado y CelulaNoRealizada
   * etiqueta para: ReportesNoIngresado
   * @param tbbEstatus
   * @param status
   * @return 
   */
  public Component configurarEstatus(Component widgetEstatus, int status) {
    //TODO: asociar eventos de acuerdo al estatus
    Toolbarbutton tbbEstatus = new Toolbarbutton();
    if (status == ReporteCelula.REPORTE_INGRESADO) {
      tbbEstatus = new Toolbarbutton();
      tbbEstatus.setTooltiptext(ReporteCelula.STATUS_INGRESADO);
      tbbEstatus.setImage(ReporteCelula.IMAGEN_INGRESADO);
      tbbEstatus.addEventListener(Events.ON_CLICK, new EventListener() {

        public void onEvent(Event event) throws Exception {
          /*
          Sesion.setVariable("idCelula", idCelula);
          Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA);
          Sesion.setModo("ver");
          ctrlVista.forzarCambioVista_btnControl();
           */
          mostrarMensaje("En construcción");
        }
      });
    } else if (status == ReporteCelula.CELULA_NO_REALIZADA) {
      tbbEstatus = new Toolbarbutton();
      tbbEstatus.setTooltiptext(ReporteCelula.STATUS_NO_REALIZADA);
      tbbEstatus.setImage(ReporteCelula.IMAGEN_NO_REALIZADA);
      tbbEstatus.addEventListener(Events.ON_CLICK, new EventListener() {

        public void onEvent(Event event) throws Exception {
          /*
          Sesion.setVariable("idCelula", idCelula);
          Sesion.setModo("ver");
          Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA);
          ctrlVista.forzarCambioVista_btnControl();
           */
          mostrarMensaje("En construcción");
        }
      });
    }else if (status == ReporteCelula.REPORTE_NO_INGRESADO) {
      tbbEstatus.setTooltiptext(ReporteCelula.STATUS_NO_INGRESADO);
      tbbEstatus.setImage(ReporteCelula.IMAGEN_NO_INGRESADO);
      tbbEstatus.addEventListener(Events.ON_CLICK, new EventListener() {

        public void onEvent(Event event) throws Exception {
          /*
          Sesion.setVariable("idCelula", idCelula);
          Sesion.setModo("new-pregunta");
          Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA);
          System.out.println("CtrlReporteCelulaListado.btnEstatus.modo: new-pregunta");
          ctrlVista.forzarCambioVista_btnControl();
           */
          mostrarMensaje("En construcción");
        }
      });
    } 
    return tbbEstatus;
  }

  /**
   * muestra un mensaje en etqMensaje
   * @param msj 
   */
  private void mostrarMensaje(String msj) {
    etqMensaje.setValue(msj);
    etqMensaje.setVisible(true);
    System.out.println(this.getClass().toString() + ": msj");
  }

  /** 
   * limpia el mensaje de estado
   */
  private void ocultarMensaje() {
    etqMensaje.setVisible(false);
    etqMensaje.setValue("");
  }
  Label etqMensaje;
}