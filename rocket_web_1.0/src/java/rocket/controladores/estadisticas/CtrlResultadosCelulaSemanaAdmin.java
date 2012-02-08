package rocket.controladores.estadisticas;

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
import org.zkoss.zul.Combobox;
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
import waytech.modelo.beans.sgi.EjecucionCelula;
import waytech.utilidades.Util;

/**
 * controlador asociado a vistaReporteCelula/Listado.zul
 * @author Gabriel
 */
public class CtrlResultadosCelulaSemanaAdmin extends GenericForwardComposer {

  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //widgets:
  Div divDatos;
  Grid grid;
  Label etqNro, etqRed, etqConvertidos, etqReconciliados, etqOfrendaMonto;
  BotonCelula tbbCodigo;
  BotonLider tbbLider1, tbbLider2, tbbLider3, tbbLider4;
  Toolbarbutton tbbEstatus;
  Combobox cmbRed; //para filtrar data por red
  Label etqInstrucciones, etqMensajeNoData;
//variable de control:
  CtrlVista ctrlVista = new CtrlVista();
  //será usado para usuario normal
  int idRedUsuario;
  //datos:
  List<EjecucionCelula> lista = new ArrayList<EjecucionCelula>();
  ServicioReporteCelula servicioReporteCelula = new ServicioReporteCelula();
  String MENSAJE_NO_DATA = "Esta red no tiene células registradas";
  //mensajes
  Div divMensaje;
  Label etqMensaje;  
  
  
  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }
  
  public void inicio() {
    //-idRed = Util.buscarIdRed(this.getClass());
    buscarDataTodasRedes();
    mostrarData();
    mensajeInstrucciones(true);
    mostrarWidgetsData(true);
    notificarBarra();
  }

  /**
   * Obtiene información de todas las redes
   */
  void buscarDataTodasRedes() {
    lista = servicioReporteCelula.getResultadosCelulaTodasRedes();
    if (lista.isEmpty()) {
      mensajeNoResultados(true);
    }else{
      mensajeNoResultados(false);
    }
  }

  /**
   * Obtiene información de una red específica
   */
  void buscarDataPorRed(int idRed) {
    //- lista = servicioReporteCelula.getReporteCelulaPorRed(idRed);
  }
  
  public void onSelect$cmbRed() {
    Object red = cmbRed.getSelectedItem().getValue();
    //**System.out.println("Listado células. red seleccionada = " + red);
    if (red.equals("*")) {
      buscarDataTodasRedes();
    } else {
      int idRed = Integer.parseInt("" + red);
      buscarDataPorRed(idRed);
    }
    if (lista.isEmpty()) {
      System.out.println("CtrlReporteCelulaListadoSemanaAdministrador. no hay datos");
      //no hay data
      mensajeNoResultados(true);
      mensajeInstrucciones(false);
      mostrarWidgetsData(false);
      return;
    } else {
      //sí hay data
      mensajeNoResultados(false);
      mostrarWidgetsData(true);
      mostrarData();
    }
  }
  
  public void onOpen$cmbRed() {
    mensajeInstrucciones(false);
  }
  
  private void mensajeInstrucciones(boolean visible) {
    etqInstrucciones.setVisible(visible);
  }
  
  private void mensajeNoResultados(boolean visible) {
    etqMensajeNoData.setVisible(visible);
  }
  
  private void mostrarWidgetsData(boolean visible) {
    divDatos.setVisible(visible);
    grid.setVisible(visible);
  }
  
  public void mostrarData() {
    //** System.out.println("CtrlReporteListado.mostrarDatos() -> lista de datos. size = " + lista.size());
    ListModelList model = new ListModelList(lista);
    grid.setModel(model);
    grid.setRowRenderer(new RowRenderer() {
      
      public void render(Row row, Object data) throws Exception {
        EjecucionCelula reporte = (EjecucionCelula) data;

        /*
        //parámetros para navegación dinámica:
        final int idCelula = reporte.getIdCelula();
        int nroLideres = reporte.getNumeroLideres();
        final int idLider1 = reporte.getIdLider1();
        final int idLider2 = reporte.getIdLider2();
        final int idLider3 = reporte.getIdLider3();
        final int idLider4 = reporte.getIdLider4();
         */

        //se crean los widgets con la data
        //TODO: pendiente: nro de red:
        //+ etqNro = new EtqNro("" + reporte.getNroItem());        
        
        //TODO: cambiar nombres de widgets:
        etqRed = new Label("" + reporte.getObservaciones());
        etqConvertidos = new Label("" + reporte.getConvertidos());
        etqReconciliados = new Label("" + reporte.getReconciliados());
        
        /*
        tbbCodigo = new BotonCelula("" + reporte.getCodigo());
        tbbLider1 = new BotonLider("" + reporte.getNombreLider1());
        tbbLider2 = new BotonLider("" + reporte.getNombreLider2());
        tbbLider3 = new BotonLider("" + reporte.getNombreLider3());
        tbbLider4 = new BotonLider("" + reporte.getNombreLider4());
        tbbLider1.setIdLider(idLider1);
        tbbLider2.setIdLider(idLider2);
        tbbLider3.setIdLider(idLider3);
        tbbLider4.setIdLider(idLider4);
         */

        //se anexan los widgets a la fila
        /*
        etqNro.setParent(row);
        tbbCodigo.setParent(row);
        /**/
        etqRed.setParent(row);
        etqConvertidos.setParent(row);
        etqReconciliados.setParent(row);
        etqOfrendaMonto.setParent(row);
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
  
/**
   * muestra un mensaje para interacción con usuario
   * @param msj 
   */
  private void mensaje(String msj) {
    etqMensaje.setValue(msj);
    etqMensaje.setVisible(true);
    divMensaje.setVisible(true);
    System.out.println(this.getClass().toString() + msj);
  }

  /** 
   * limpia el mensaje de estado
   */
  private void ocultarMensaje() {
    etqMensaje.setVisible(false);
    etqMensaje.setValue("");
  }
  
}
