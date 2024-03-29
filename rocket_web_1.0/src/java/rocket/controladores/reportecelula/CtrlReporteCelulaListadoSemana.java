package rocket.controladores.reportecelula;

import rocket.controladores.general.CtrlVista;
import rocket.controladores.general.Sesion;
import rocket.controladores.general.Vistas;
import rocket.controladores.widgets.BotonCelula;
import rocket.controladores.widgets.BotonLider;
import rocket.controladores.widgets.EtqNro;
import rocket.modelo.bd.util.ReporteCelulaListadoUtil;
import rocket.modelo.bd.util.ReporteCelulaUtil;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import rocket.modelo.servicios.ServicioReporteCelula;
import waytech.utilidades.Util;

/**
 * controlador asociado a vistaReporteCelula/ListadoSemana.zul
 * reportes de las células directas de cualquier líder
 * @author Gabriel
 */
public class CtrlReporteCelulaListadoSemana extends GenericForwardComposer {

  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //widgets:
  Div divDatos;
  Grid grid;
  Label etqNro, etqDireccion, etqDiaHora;
  Label etqMensajeNoData;
  BotonCelula tbbCodigo;
  BotonLider tbbLider1, tbbLider2, tbbLider3, tbbLider4;
  Toolbarbutton tbbEstatus;
  //variable de control:
  CtrlVista ctrlVista = new CtrlVista();
  int idUsuario;
  //datos:
  List<ReporteCelulaListadoUtil> lista = new ArrayList<ReporteCelulaListadoUtil>();
  ServicioReporteCelula servicioReporteCelula = new ServicioReporteCelula();

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() {
    idUsuario = Util.buscarIdUsuario(this.getClass());
    System.out.println("CtrlReporteCelulaListadoSemana.idUsuario=" + idUsuario);
    buscarData();
    if (lista.isEmpty()) {
      divDatos.setVisible(false);
      etqMensajeNoData.setVisible(true);
    } else {
      etqMensajeNoData.setVisible(false);
      divDatos.setVisible(true);
      mostrarData();
    }
    notificarBarra();
  }
  
  private void mensajeNoResultados(boolean visible) {
    etqMensajeNoData.setVisible(visible);
  }
  
  private void mostrarWidgetsData(boolean visible) {
    divDatos.setVisible(visible);
    grid.setVisible(visible);
  }  

  public void buscarData() {
    //TODO:verificar tipo de usuario para ver qué data buscar y mostrar
    lista = servicioReporteCelula.getReporteCelulaPorLider(idUsuario);
  }

  public void mostrarData() {
    System.out.println("CtrlReporteListado.mostrarDatos() -> lista de datos. size = " + lista.size());
    ListModelList model = new ListModelList(lista);
    grid.setModel(model);
    grid.setRowRenderer(new RowRenderer() {

      public void render(Row row, Object data) throws Exception {
        ReporteCelulaListadoUtil reporte = (ReporteCelulaListadoUtil) data;

        //parámetros para navegación dinámica:
        final int idCelula = reporte.getIdCelula();
        int nroLideres = reporte.getNumeroLideres();
        final int idLider1 = reporte.getIdLider1();
        final int idLider2 = reporte.getIdLider2();
        final int idLider3 = reporte.getIdLider3();
        final int idLider4 = reporte.getIdLider4();

        //se crean los widgets con la data
        etqNro = new EtqNro("" + reporte.getNroItem());
        etqDireccion = new Label("" + reporte.getDireccionCorta());
        etqDiaHora = new Label(Util.getDiaHora(reporte.getDia(), reporte.getHora()));
        tbbCodigo = new BotonCelula("" + reporte.getCodigo());
        tbbLider1 = new BotonLider("" + reporte.getNombreLider1());
        tbbLider2 = new BotonLider("" + reporte.getNombreLider2());
        tbbLider3 = new BotonLider("" + reporte.getNombreLider3());
        tbbLider4 = new BotonLider("" + reporte.getNombreLider4());
        tbbLider1.setIdLider(idLider1);
        tbbLider2.setIdLider(idLider2);
        tbbLider3.setIdLider(idLider3);
        tbbLider4.setIdLider(idLider4);


        //TODO: asociar eventos de acuerdo al estatus
        tbbEstatus = new Toolbarbutton();
        int status = reporte.getEstatus();
        if (status == ReporteCelulaUtil.REPORTE_INGRESADO) {
          tbbEstatus.setTooltiptext(ReporteCelulaUtil.TOOLTIPTEXT_INGRESADO);
          tbbEstatus.setImage(ReporteCelulaUtil.IMAGEN_INGRESADO);
          tbbEstatus.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event event) throws Exception {
              Sesion.setVariable("idCelula", idCelula);
              Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA);
              Sesion.setModo("ver");
              ctrlVista.forzarCambioVista_btnControl();
            }
          });
        } else if (status == ReporteCelulaUtil.REPORTE_NO_INGRESADO) {
          tbbEstatus.setLabel("Ingresar Reporte");
          tbbEstatus.setTooltiptext(ReporteCelulaUtil.TOOLTIPTEXT_NO_INGRESADO);
          tbbEstatus.setImage(ReporteCelulaUtil.IMAGEN_NO_INGRESADO);
          tbbEstatus.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event event) throws Exception {
              Sesion.setVariable("idCelula", idCelula);
              Sesion.setModo("new-pregunta");
              Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA);
              System.out.println("CtrlReporteCelulaListado.btnEstatus.modo: new-pregunta");
              ctrlVista.forzarCambioVista_btnControl();
            }
          });
        } else if (status == ReporteCelulaUtil.CELULA_NO_REALIZADA) {
          tbbEstatus.setTooltiptext(ReporteCelulaUtil.STATUS_NO_REALIZADA);
          tbbEstatus.setImage(ReporteCelulaUtil.IMAGEN_NO_REALIZADA);
          tbbEstatus.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event event) throws Exception {
              Sesion.setVariable("idCelula", idCelula);
              Sesion.setModo("ver");
              Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA);
              ctrlVista.forzarCambioVista_btnControl();
            }
          });
        }
        /*+posiblemente se use este código + adelante:
        else if (status == ReporteCelula.CONFIRMADO) {
        tbbEstatus.setTooltiptext(ReporteCelula.STATUS_CONFIRMADO);
        tbbEstatus.setImage(ReporteCelula.IMAGEN_CONFIRMADO);
        tbbEstatus.addEventListener(Events.ON_CLICK, new EventListener() {
        
        public void onEvent(Event event) throws Exception {
        Sesion.setVariable("idCelula", idCelula);
        Sesion.setModo("confirmado");
        panelCentral.setSrc(Vistas.REPORTE_CELULA);
        }
        });
        }
         */

        tbbCodigo.setIdCelula(idCelula);

        //se anexan los widgets a la fila
        etqNro.setParent(row);
        tbbCodigo.setParent(row);
        /**/
        if (nroLideres == 0) {//célula no tiene líderes          
          Label etqNoTieneLideres = new Label("No asignados");
          etqNoTieneLideres.setParent(row);
        } else {
          Vbox vbox = new Vbox();
          tbbLider1.setParent(vbox);
          tbbLider2.setParent(vbox);
          tbbLider3.setParent(vbox);
          tbbLider4.setParent(vbox);
          vbox.setParent(row);
        }
        etqDireccion.setParent(row);
        etqDiaHora.setParent(row);
        /**/
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
  //+: será usado en versiones próximas
  public void onClick$btnReportesAnteriores() {
    Sesion.setVistaActual(Vistas.REPORTE_CELULA_LISTADO_SEMANA);
    Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA_LISTADO_ANTERIORES);
    //forzar cambio de vista:
    ctrlVista.forzarCambioVista_btnControl();
  }
}