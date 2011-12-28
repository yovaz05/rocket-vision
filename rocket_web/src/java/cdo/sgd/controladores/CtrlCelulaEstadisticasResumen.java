package cdo.sgd.controladores;

import sig.controladores.Sesion;
import sig.controladores.Vistas;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Toolbarbutton;
import waytech.utilidades.UtilFechas;

/**
 * controlador asociado a vistaPerfil/CambiarCorreo
 * @author Gabriel Pérez
 */
public class CtrlCelulaEstadisticasResumen extends GenericForwardComposer {

  //widgtes:
  Label etqFechaInicio;
  Label etqFechaFin;
  Label etqMensaje;
  Label etqPeriodo;
  Tabbox tabbox;
  Tab tabPersonalizado;
  //gestión de datos: NO USADO AUN
  //BD datos;  
  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //variables de control:
  private int id;
  private Calendar hoy;
  private Calendar fechaInicio;
  private Calendar fechaFin;
  private Calendar finMes;
  private int tipoPeriodo;
  private final int PERIODO_SEMANA = 0;
  private final int PERIODO_MES = 1;
  private final int PERIODO_TRIMESTRE = 2;
  private final int PERIODO_AÑO = 3;
  private final int PERIODO_PERSONALIZADO = 4;
  private int tabSeleccionado;
  CtrlVista ctrlVista = new CtrlVista();
  //para almacenar los valores actuales de los datebox
  private Date dateInicio;
  private Date dateFin;
  //para almacenar los valores del período actual que se está visualizando
  private int mes = 0;
  private int año = 0;
  private Date dateInicioSemana;
  private Date dateFinSemana;
  private Calendar inicioTrimestre;
  private Date dateFinTrimestre;
  private Date dateInicioTrimestre;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    //datos = new BD();
    buscarVarSesion();
    inicializarValoresFechaActual();
    actualizarPeriodo();
    //+seleccionarTab(0);
    notificarBarra();
  }

  private void inicializarValoresFechaActual() {
    calcularFechasUltimaSemana();
    mes = Calendar.getInstance().get(Calendar.MONTH);
    año = Calendar.getInstance().get(Calendar.YEAR);
  }

  /**
   * recupera variables de sesión
   */
  private void buscarVarSesion() throws InterruptedException {
    try {
      //TODO: evaluar qué se hará si no viene de navegación dinámica...
      if (Sessions.getCurrent().getAttribute("idRed") == null) {
        System.out.println("CtrlCambiarCorreo -> ERROR: variable de sesión 'idRed' = null");
        return;
      }
      id = (Integer) Sessions.getCurrent().getAttribute("idRed");
      if (id == 0) {
        System.out.println("CtrlCambiarCorreo -> ERROR: variable de sesión 'idRed' = 0");
        id = 0; //valor de prueba, datos en blanco
      }
    } catch (Exception e) {
      System.out.println("CtrlCambiarCorreo -> ERROR: variable de sesión 'idRed'");
    } finally {
      System.out.println("CtrlCambiarCorreo -> id = " + id);
    }
  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.CELULA_ESTADISTICAS_RESUMEN);
    Sesion.setModo("consulta");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }

  /**
   * captura cambios en selección de pestaña del tabbox
   */
  public void onSelect$tabbox() {
    tabSeleccionado = tabbox.getSelectedIndex();
    ocultarMensaje(etqMensaje);
    if (tabSeleccionado != PERIODO_PERSONALIZADO) {
      tabPersonalizado.setVisible(false);
      calcularFechas();
      actualizarPeriodo();
      mostrarMensajePeriodo();
    }
  }

  /**
   * selecciona la pestaña indicada por el parámetro
   */
  private void seleccionarTab(int i) {
    tabbox.setSelectedIndex(i);
    ocultarMensaje(etqMensaje);
  }



  /**
   * calcula las fechas del período,
   * de acuerdo a la pestaña seleccionada,
   * y/o si le da a los botones atrás y adelante
   */
  private void calcularFechas() {
    if (tabSeleccionado == PERIODO_SEMANA) {
      calcularFechasUltimaSemana();
      tipoPeriodo = PERIODO_SEMANA;
    }
    else if (tabSeleccionado == PERIODO_MES) {
      calcularFechasUltimoMes();
      tipoPeriodo = PERIODO_MES;
    }
    else if (tabSeleccionado == PERIODO_TRIMESTRE) {
      calcularFechasUltimoTrimestre();
      tipoPeriodo = PERIODO_TRIMESTRE;
    }
    else if (tabSeleccionado == PERIODO_AÑO) {
      calcularFechasUltimoAño();
      tipoPeriodo = PERIODO_AÑO;
    }
  }

  /**
   * calcula y muestra las fechas de la semana actual
   **/
  void calcularFechasUltimaSemana() {
    //buscar fecha de hoy
    hoy = new GregorianCalendar();

    //buscar domingo anterior:
    Calendar domingo = UtilFechas.calcularDomingoAnterior();
    fechaInicio = domingo;

    //buscar sábado siguiente
    Calendar sabado = UtilFechas.calcularSabadoSiguiente();
    //ajustar fecha final con sábado siguiente
    if (sabado.after(hoy)) {
      fechaFin = hoy;
    } else {
      fechaFin = sabado;
    }
  }

  /**
   * ajusta período al último mes a partir del día de hoy
   */
  private void calcularFechasUltimoMes() {
    fechaInicio = UtilFechas.calcularMesInicio();
    fechaFin = hoy;
  }

  /**
   * ajusta período al último trimestre a partir del día de hoy
   */
  private void calcularFechasUltimoTrimestre() {
    inicioTrimestre = UtilFechas.calcularTrimestreInicio();
    fechaInicio = inicioTrimestre;
    fechaFin = hoy;
    dateInicioTrimestre = inicioTrimestre.getTime();
    dateFinTrimestre = hoy.getTime();
  }

  /**
   * ajusta período 1 año hacia atrás respecto al día de hoy
   */
  private void calcularFechasUltimoAño() {
    fechaInicio = UtilFechas.retroceder1Año();
    fechaFin = hoy;
  }

  /**
   * actualiza valores en datebox del período
   * dateDesde y dateHasta
   */
  void actualizarPeriodo() {
    etqFechaInicio.setValue(UtilFechas.getFechaTextoSoloNumeros(fechaInicio));
    etqFechaFin.setValue(UtilFechas.getFechaTextoSoloNumeros(fechaFin));
    mostrarMensajePeriodo();
    System.out.println("fecha inicio: " + UtilFechas.getFechaLarga(fechaInicio));
    System.out.println("fecha fin: " + UtilFechas.getFechaLarga(fechaFin));
  }

  /**
   * Redirige a la vista de estadísticas detalladas, donde se pueden elegir las fechas a conveniencia
   */
  public void onClick$btnReporteDetallado() {
    Sesion.setVariable("fechaInicio", fechaInicio);
    Sesion.setVariable("fechaFin", fechaFin);
    Sesion.setVariable("tipoPeriodo", tipoPeriodo);
    Sesion.setVistaActual(Vistas.CELULA_ESTADISTICAS_RESUMEN);
    Sesion.setVistaSiguiente(Vistas.CELULA_ESTADISTICAS_DETALLES);
    //envia click al btnControl de CtrlBarraMenu, para que cambie la vista
    ctrlVista.forzarCambioVista_btnControl();
  }

  private void mostrarMensajePeriodo() {
    if (tipoPeriodo == PERIODO_SEMANA) {
      mostrarPeriodo("Viendo semana actual");
    }
    else if (tipoPeriodo == PERIODO_MES) {
      mostrarPeriodo("Viendo últimas 4 semanas");
    }
    else if (tipoPeriodo == PERIODO_TRIMESTRE) {
      mostrarPeriodo("Viendo un trimestre hacia atrás (12 semanas)");
    }
    else if (tipoPeriodo == PERIODO_AÑO) {
      mostrarPeriodo("Viendo un año hacia atrás (52 semanas)");
    }
  }
  
  /** método debug
   * muestra un mensaje en label
   * @param msj 
   */
  private void mostrarMensaje(String msj) {
    etqMensaje.setValue(msj);
    System.out.println("msj");
  }

  /**
   * limpia el mensaje de estado
   */
  private void ocultarMensaje(Label etq) {
    etq.setVisible(false);
    etq.setValue("");
  }

    /** método debug
   * muestra un mensaje en label
   * @param msj 
   */
  private void mostrarPeriodo(String msj) {
    etqPeriodo.setValue(msj);
    etqPeriodo.setVisible(true);
    System.out.println("msj");
  }

}