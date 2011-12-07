package cdo.sgd.controladores;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Toolbarbutton;
import waytech.utilidades.Util;
import waytech.utilidades.UtilSIG;

/**
 * controlador asociado a vistaPerfil/CambiarCorreo
 * @author Gabriel Pérez
 */
public class CtrlCelulaEstadisticas extends GenericForwardComposer {

  //widgtes:
  Datebox dateboxInicio;
  Datebox dateboxFin;
  Label etqMensaje;
  Tabbox tabbox;
  Tab tabPersonalizado;
  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //variables de control:
  private int idRed;
  private int id;
  private Calendar hoy;
  private Calendar fechaInicio;
  private Calendar fechaFin;
  private int tipoPeriodo;
  private final int PERIODO_SEMANA = 0;
  private final int PERIODO_MES = 1;
  private final int PERIODO_TRIMESTRE = 2;
  private final int PERIODO_AÑO = 3;
  private final int PERIODO_PERSONALIZADO = 4;
  private int tabSeleccionado;
  //para almacenar los valores actuales de los datebox
  private Date dateInicio;
  private Date dateFin;
  //para almacenar los valores del período actual que se está visualizando
  private int mes = 0;
  private int año = 0;
  private Calendar inicioTrimestre;
  private Date dateFinTrimestre;
  private Date dateInicioTrimestre;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    System.out.println("CtrlCelulaEstadisticas.inicio");
    buscarVarSesion();
    inicializarValoresFechaActual();
    actualizarPeriodo();
    notificarBarra();
  }

  private void getFechasDeVistaResumen() {
    calcularFechasUltimaSemana();
    hoy = Calendar.getInstance();
    mes = hoy.get(Calendar.MONTH);
    año = hoy.get(Calendar.YEAR);
  }

  private void inicializarValoresFechaActual() {
    if ((fechaInicio == null) || (fechaFin == null)) {
      calcularFechasUltimaSemana();
      hoy = Calendar.getInstance();
      mes = hoy.get(Calendar.MONTH);
      año = hoy.get(Calendar.YEAR);

    }
  }

  /**
   * recupera variables de sesión
   */
  private void buscarVarSesion() throws InterruptedException {
    idRed = UtilSIG.buscarIdRed(this.getClass());
    fechaInicio = (Calendar) Sesion.getVariable("fechaInicio");
    fechaFin = (Calendar) Sesion.getVariable("fechaFin");
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
    ocultarMensaje();
    if (tabSeleccionado != PERIODO_PERSONALIZADO) {
      tabPersonalizado.setVisible(false);
      calcularFechas();
      actualizarPeriodo();
    }
  }

  /**
   * muestra u oculta la pestaña de Período Personalizado
   */
  private void mostrarTabPersonalizado(boolean status) {
    tabPersonalizado.setVisible(status);
    seleccionarTab(4);
  }

  /**
   * selecciona la pestaña indicada por el parámetro
   */
  private void seleccionarTab(int i) {
    tabbox.setSelectedIndex(i);
    ocultarMensaje();
  }

  /**
   * captura las selección en dateboxInicio
   */
  public void onClick$btnAplicar() {
    //mostrarPeriodoPersonalizado();
    mostrarMensaje("En construcción");
  }

  /**
   * método debug
   * muestra un mensaje en label
   * @param msj 
   */
  private void mostrarMensaje(String msj) {
    etqMensaje.setValue(msj);
    System.out.println("msj");
  }

  private void ocultarMensaje() {
    etqMensaje.setValue("");
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
    if (tabSeleccionado == PERIODO_MES) {
      calcularFechasUltimoMes();
      tipoPeriodo = PERIODO_MES;
    }
    if (tabSeleccionado == PERIODO_TRIMESTRE) {
      calcularFechasUltimoTrimestre();
      tipoPeriodo = PERIODO_TRIMESTRE;
    }
    if (tabSeleccionado == PERIODO_AÑO) {
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
    Calendar domingo = Util.calcularDomingoAnterior();
    fechaInicio = domingo;

    //buscar sábado siguiente
    Calendar sabado = Util.calcularSabadoSiguiente();
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
    fechaInicio = Util.calcularMesInicio();
    fechaFin = hoy;
  }

  /**
   * ajusta período al último trimestre a partir del día de hoy
   */
  private void calcularFechasUltimoTrimestre() {
    inicioTrimestre = Util.calcularTrimestreInicio();
    fechaInicio = inicioTrimestre;
    fechaFin = hoy;
    dateInicioTrimestre = inicioTrimestre.getTime();
    dateFinTrimestre = hoy.getTime();
  }

  /**
   * ajusta período 1 año hacia atrás respecto al día de hoy
   */
  private void calcularFechasUltimoAño() {
    fechaInicio = Util.calcularInicioAñoActual();
    fechaFin = hoy;
  }

  /**
   * actualiza valores en datebox del período
   * dateDesde y dateHasta
   */
  void actualizarPeriodo() {
    mostrarMensaje("fechaInicio: " + fechaInicio);
    dateboxInicio.setValue(fechaInicio.getTime());
    dateboxFin.setValue(fechaFin.getTime());
    System.out.println("fecha inicio: " + Util.getFechaLarga(fechaInicio));
    System.out.println("fecha fin: " + Util.getFechaLarga(fechaFin));
    //guardar valores actuales, en objetos tipo Date:
    dateInicio = dateboxInicio.getValue();
    dateFin = dateboxFin.getValue();
  }

  /**
   * En construcción
   */
  private void mostrarPeriodoPersonalizado() {
    boolean cambios = false;
    Date dateDesde = dateboxInicio.getValue();
    Date dateHasta = dateboxFin.getValue();
    if (dateDesde != dateInicio) {
      //*mostrarMensaje("fecha inicio fue cambiada");
      cambios = true;
      fechaInicio = Util.getCalendar(dateDesde);
    }
    if (dateHasta != dateFin) {
      cambios = true;
      //*mostrarMensaje("fecha fin fue cambiada");
      fechaFin = Util.getCalendar(dateHasta);
    }
    if (cambios) {
      tipoPeriodo = PERIODO_PERSONALIZADO;
      actualizarPeriodo();
      mostrarTabPersonalizado(true);
    }
  }

  public void onClick$btnPeriodoAnterior() {
    ocultarMensaje();
//    if ((tipoPeriodo == PERIODO_SEMANA)
//            || (tipoPeriodo == PERIODO_SEMANA)
//            || (tipoPeriodo == PERIODO_SEMANA)) {
    mostrarMensaje("En construcción");
    //+calcularPeriodoAnterior();
  }

  public void onClick$btnPeriodoSiguiente() {
    ocultarMensaje();
    mostrarMensaje("En construcción");
    //+calcularPeriodoSiguiente();
  }

  private void calcularPeriodoSiguiente() {
    //se valida sin fecha fin es hoy, no hay un período siguiente
//    if (fechaFin == hoy) {
//      mostrarMensaje("fechaFin = hoy");
//      return;
//    } 
    if (tipoPeriodo == PERIODO_SEMANA) {
      //avanzarSemana();
    } else if (tipoPeriodo == PERIODO_MES) {
      //avanzarMes();
    } else if (tipoPeriodo == PERIODO_TRIMESTRE) {
      //avanzarTrimestre();
    } else if (tipoPeriodo == PERIODO_AÑO) {
      //avanzarAño();
    }
    actualizarPeriodo();
  }

  private void calcularPeriodoAnterior() {
    if (tipoPeriodo == PERIODO_SEMANA) {
      //avanzarSemana();
    } else if (tipoPeriodo == PERIODO_MES) {
      retrocederMes();
    } else if (tipoPeriodo == PERIODO_TRIMESTRE) {
      //avanzarTrimestre();
    } else if (tipoPeriodo == PERIODO_AÑO) {
      //avanzarAño();
    }
    actualizarPeriodo();
  }

  private void retrocederMes() {
    fechaInicio = Util.retrocederMes(fechaInicio);
    fechaFin = Util.retrocederMes(fechaFin);

  }

  private void avanzarMes() {
    fechaInicio = Util.avanzarMes(fechaInicio);
    fechaFin = Util.avanzarMes(fechaFin);
  }
}