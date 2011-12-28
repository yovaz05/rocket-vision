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
import waytech.utilidades.UtilSIG;

/**
 * controlador asociado a vistaReporteCelula/Listado.zul
 * @author Gabriel
 */
public class CtrlReporteCelulaListadoSemana extends GenericForwardComposer {

  //widgets:
  Grid grid;
  Label etqNro, etqDireccion, etqDiaHora, etqNombre;
  BotonCelula tbbCodigo;
  BotonLider tbbLider1, tbbLider2;
  Toolbarbutton tbbEstatus;
  //Column colEstadosReporte;
  //datos:
  ArrayList lista;
  BD bd;
  //variable de control:
  int idRed;
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
    idRed = UtilSIG.buscarIdRed(this.getClass());
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
        final int idCelula = reporte.getIdCelula();
        final int idLider1 = reporte.getIdLider1();
        final int idLider2 = reporte.getIdLider2();

        //TODO: asociar eventos de acuerdo al estatus
        tbbEstatus = new Toolbarbutton();
        int status = reporte.getEstatus();
        if (status == ReporteCelula.REPORTE_INGRESADO) {
          tbbEstatus.setTooltiptext(ReporteCelula.STATUS_INGRESADO);
          tbbEstatus.setImage(ReporteCelula.IMAGEN_INGRESADO);
          tbbEstatus.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event event) throws Exception {
              Sesion.setVariable("idCelula", idCelula);
              Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA);
              Sesion.setModo("ver");
              ctrlVista.forzarCambioVista_btnControl();
            }
          });
        } else if (status == ReporteCelula.REPORTE_NO_INGRESADO) {
          tbbEstatus.setTooltiptext(ReporteCelula.STATUS_NO_INGRESADO);
          tbbEstatus.setImage(ReporteCelula.IMAGEN_NO_INGRESADO);
          tbbEstatus.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event event) throws Exception {
              Sesion.setVariable("idCelula", idCelula);
              Sesion.setModo("new-pregunta");
              Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA);
              System.out.println("CtrlReporteCelulaListado.btnEstatus.modo: new-pregunta");
              ctrlVista.forzarCambioVista_btnControl();
            }
          });
        } else if (status == ReporteCelula.CELULA_NO_REALIZADA) {
          tbbEstatus.setTooltiptext(ReporteCelula.STATUS_NO_REALIZADA);
          tbbEstatus.setImage(ReporteCelula.IMAGEN_NO_REALIZADA);
          tbbEstatus.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event event) throws Exception {
              Sesion.setVariable("idCelula", idCelula);
              Sesion.setModo("ver");
              Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA);
              ctrlVista.forzarCambioVista_btnControl();
            }
          });
        }/* else if (status == ReporteCelula.CONFIRMADO) {
        tbbEstatus.setTooltiptext(ReporteCelula.STATUS_CONFIRMADO);
        tbbEstatus.setImage(ReporteCelula.IMAGEN_CONFIRMADO);
        tbbEstatus.addEventListener(Events.ON_CLICK, new EventListener() {
        
        public void onEvent(Event event) throws Exception {
        Sesion.setVariable("idCelula", idCelula);
        Sesion.setModo("confirmado");
        panelCentral.setSrc(Vistas.REPORTE_CELULA);
        }
        });
        }*/


        tbbCodigo.setIdCelula(idCelula);
        tbbLider1.setIdLider(idLider1);
        tbbLider2.setIdLider(idLider2);

        //se anexan los widgets a la fila
        etqNro.setParent(row);
        tbbCodigo.setParent(row);
        etqDireccion.setParent(row);
        Vbox vbox = new Vbox();
        tbbLider1.setParent(vbox);
        tbbLider2.setParent(vbox);
        vbox.setParent(row);
        etqDiaHora.setParent(row);
        //-etqNombre.setParent(row);
        tbbEstatus.setParent(row);
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
  
  
}