package cdo.sgd.controladores;

import sig.controladores.Sesion;
import sig.controladores.Vistas;
import cdo.sgd.modelo.bd.simulador.BD;
import cdo.sgd.modelo.bd.simulador.CelulaUtil;
import cdo.sgd.modelo.bd.simulador.ReporteCelula;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import waytech.utilidades.UtilSIG;
import waytech.utilidades.UtilFechas;

//TODO: MEJORAR EL MANEJO DE FOCOS DE LOS widgets
/**
 * controlador asociado a vistaReporteCelula/Reporte.zul
 * @author Gabriel
 */
public class CtrlReporteCelula extends GenericForwardComposer {

  //widgets:
  Label tituloVentana;
  //botones:
  Toolbarbutton btnGuardar;
  Toolbarbutton btnSave;
  Toolbarbutton btnEditar;
  Toolbarbutton btnConfirmar;
  Toolbarbutton btnImprimir;
  Hbox hboxPreguntaCelulaRealizada;
  //tabbox:
  Tabbox tabbox;
  Tab tabCelula;
  Tab tabFechas;
  Tab tabPlanificacion;
  Tab tabResultados;
  Tab tabOfrendas;
  Tab tabObservaciones;
  Tabpanel tabPanelCelula;
  Tabpanel tabPanelFechas;
  Tabpanel tabPanelPlanificacion;
  Tabpanel tabPanelResultados;
  Tabpanel tabPanelOfrendas;
  Tabpanel tabPanelObservaciones;
  //pestaña "Datos Célula"
  Textbox db$txtNombre;
  Textbox db$txtRed;
  Textbox db$txtDiaHora;
  Label db$etqNombre;
  Label db$etqCodigo;
  Label db$etqDireccion;
  Label db$etqDiaHora;
  Grid db$gridLideres;
  Toolbarbutton db$tbbCodigo;
  Toolbarbutton db$btnCatLideres;
  Toolbarbutton db$tbbRed;
  Toolbarbutton db$tbbLider1;
  Toolbarbutton db$tbbLider2;
  Toolbarbutton db$tbbDiaHora;
  //pestaña "Fechas"
  Label fechas$etqHoy;
  Label fechas$etqSemanaInicio;
  Label fechas$etqSemanaFin;
  Label fechas$etqDiaCelula;
  Label fechas$etqSemana;
//pestaña "Planificación"
  Column planif$colEdit;
  Column planif$colView;
  Spinner planif$spnInvitados;
  Spinner planif$spnReconciliados;
  Spinner planif$spnVisitas;
  Spinner planif$spnPersonasEnPlanif;
  Toolbarbutton planif$tbbInvitados;
  Toolbarbutton planif$tbbReconciliados;
  Toolbarbutton planif$tbbVisitas;
  Toolbarbutton planif$tbbPersonasEnPlanif;
  //pestaña "Resultados"
  Grid result$gridEdit;
  Grid result$gridView;
  Spinner result$spnInvitados;
  Spinner result$spnConvertidos;
  Spinner result$spnAmigos;
  Spinner result$spnReconciliados;
  Spinner result$spnCDO;
  Spinner result$spnVisitas;
  Spinner result$spnOtrasIglesias;
  Spinner result$spnDomingoAnterior;
  Label result$etqTotalAsistencia;
  Label result$etqInvitados;
  Label result$etqConvertidos;
  Label result$etqAmigos;
  Label result$etqReconciliados;
  Label result$etqCDO;
  Label result$etqVisitas;
  Label result$etqOtrasIglesias;
  Label result$etqDomingoAnterior;
  //pestaña Ofrendas:
  Column ofrendas$colEdit;
  Column ofrendas$colView;
  Decimalbox ofrendas$txtOfrendasMonto;
  Checkbox ofrendas$chkEntregadas;
  Label ofrendas$etqOfrendasMonto;
  Label ofrendas$etqOfrendasEntregadas;
  //pestaña Observaciones:
  Column obs$colEdit;
  Column obs$colView;
  Textbox obs$txtObservaciones;
  Label obs$etqObservaciones;
  //interacción con usuario:
  Label etqMensaje;
  //variables de control:
  int tabSeleccionado;
  //modo = {new,edicion,ver,imprimir,confirmado}
  String modo;
  String titulo = "Reporte de Célula";
  String hoy;
  String domingo;
  String sabado;
  String semana;
  String diaCelula;  //gestión de datos
  int id;
  BD datos;
  ArrayList lista;
  ReporteCelula reporte = new ReporteCelula();
  CelulaUtil celula;
  //referencias:
  Include vistaCentral;
  Include panelCentral;
  private Tab tabSelected;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  ///TODO: hacer este método simular a CtrlCelula.inicio()
  public void inicio() throws InterruptedException {
    datos = new BD();
    modo = Sesion.getModo();
    if (modo == null) {
      modo = "ver";
    }
    System.out.println("CtrlReporteCelula.modo: " + modo);
    getIdCelula();
    celula = (CelulaUtil) datos.buscarCelula(id);
    reporte = (ReporteCelula) datos.buscarReporteCelula(id);
    ocultarPregunta();
    mostrarDatosCelula();
    mostrarFechas();
    mostrarObservaciones();
    if (modo.equals("ver")) {
      System.out.println("estado reporte: " + reporte.getDescripcionEstatus());
      if (reporte.getEstatus() == ReporteCelula.REPORTE_INGRESADO) {
        mostrarDatosReporte();
        seleccionarTab(tabResultados);
      } else if (reporte.getEstatus() == ReporteCelula.CELULA_NO_REALIZADA) {
        System.out.println("estatus = CELULA_NO_REALIZADA");
        ocultarPregunta();
        mostrarMensaje("La célula NO se realizó esta semana");
        mostrarTabsBasicos();
        ocultarTabsDatosReporte();
        seleccionarTab(tabObservaciones);
      }
      //+ seleccionarTab(tabResultados);
    } //pestaña seleccionada por defecto: 3="Resultados"
    else if (modo.equals("new-pregunta")) {
      mostrarPregunta();
      ocultarTabsDatosReporte();
      ocultarWidget(tabObservaciones);
    }
    notificarBarra();
    actualizarEstado();
  }

  /**
   * recupera parámetro idCelula que viene de la vista llamante
   */
  private void getIdCelula() throws InterruptedException {
    id = 0;
    try {
      id = (Integer) Sesion.getVariable("idCelula");
    } catch (Exception e) {
      System.out.println("CtrlReporteCelula -> ERROR: parámetro idCelula nulo... " + e);
    } finally {
      //TODO:
      //id = UtilFechas.calcularIdReporteCelula(id);
      System.out.println("CtrlReporteCelula -> idCelula = " + id);
    }
  }

  /**
   * muestra datos de la célula y las fechas correspondiente a la semana actual
   */
  public void mostrarDatosCelula() {
    if (id == 0) {
      System.out.println("ERROR: CtrlReporteCelula.idCelula");
      return;
    }
    System.out.println("CtrlReporteCelule - célula:" + celula.toString());
    db$tbbCodigo.setLabel(celula.getCodigo());
    db$etqDireccion.setValue(celula.getDireccionCorta());
    db$etqDiaHora.setValue(celula.getDia());
    db$tbbRed.setLabel(celula.getNombreRed());
    db$tbbLider1.setLabel(celula.getNombreLider1());
    db$tbbLider2.setLabel(celula.getNombreLider2());
    //db$etqNombre.setValue(celula.getNombre());


    //parámetros para navegación dinámica:
    final int idCelula = celula.getId();
    final int idLider1 = celula.getIdLider1();
    final int idLider2 = celula.getIdLider2();

    db$tbbCodigo.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sessions.getCurrent().setAttribute("idCelula", idCelula);
        Sesion.setModo("ver");
        panelCentral.setSrc("vistaCelula/Celula.zul");
        System.out.println("CtrlReporteCelula -> idCelula = " + idCelula);
      }
    });

    db$tbbLider1.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sesion.setVariable("idLider", idLider1);
        panelCentral.setSrc("vistaLider/Resumen.zul");
        System.out.println("CtrlCelula -> idLider = " + idLider1);
      }
    });
    db$tbbLider2.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sesion.setVariable("idLider", idLider2);
        panelCentral.setSrc("vistaLider/Resumen.zul");
        System.out.println("CtrlCelula -> idLider2 = " + idLider2);
      }
    });
  }

  /**
   * muestra datos del reporte, por ahora son simulados  
   */
  public void mostrarDatosReporte() {
    //llenar widgets con la data
    if (reporte == null) {
      System.out.println("ERROR: CtrlReporteCelula - mostrarDatos: reporte == null");
    }
    //*System.out.println("CtrlReporteCelula - reporte:" + reporte.toString());
    //planif
    planif$spnInvitados.setValue(reporte.getPlanificacionInvitados());
    planif$spnReconciliados.setValue(reporte.getPlanificacionReconciliados());
    planif$spnVisitas.setValue(reporte.getPlanificacionVisitas());
    planif$spnPersonasEnPlanif.setValue(reporte.getPlanificacionPersonasEnplanificacion());
    //result
    result$spnInvitados.setValue(reporte.getResultadoInvitados());
    result$spnConvertidos.setValue(reporte.getResultadoConvertidos());
    result$spnAmigos.setValue(reporte.getResultadoAmigosNoAsistenIglesia());
    result$spnReconciliados.setValue(reporte.getResultadoReconciliados());
    result$spnCDO.setValue(reporte.getResultadoCDO());
    result$spnVisitas.setValue(reporte.getResultadoVisitas());
    result$spnOtrasIglesias.setValue(reporte.getResultadoOtrasIglesias());
    result$spnDomingoAnterior.setValue(reporte.getResultadoAsistenciaDomingoAnterior());
    result$etqTotalAsistencia.setValue("" + reporte.getTotalAsistenciaCelula());
    //ofrendas
    ofrendas$txtOfrendasMonto.setValue("" + reporte.getOfrendasMonto());
    ofrendas$chkEntregadas.setChecked(reporte.isOfrendasEntregadas());
  }

  /**
   * mostrar valor de 'observaciones'
   */
  public void mostrarObservaciones() {
    obs$txtObservaciones.setValue(reporte.getObservaciones());
  }

  /**
   * actualiza estado de widgets
   */
  public void actualizarEstado() {
    if (modo.equals("new")) {
      tituloVentana.setValue(titulo + " » Ingresar");
      //camposModoEdicion(true);
      verElementosEntrada(true);
      mostrarElementosVisualizacion(false);
      //btnConfirmar.setVisible(false);
      //btnEditar.setVisible(false);
      //btnGuardar.setVisible(true);
      setFocoEdicion();
    } else if (modo.equals("ver")) {
      tituloVentana.setValue(titulo);
      //camposModoEdicion(false);
      verElementosEntrada(false);
      mostrarElementosVisualizacion(true);
      //btnGuardar.setVisible(false);
      //btnEditar.setVisible(true);
      //btnConfirmar.setVisible(true);
      //+verElementosEntrada(false);
      //+mostrarElementosVisualizacion(true);      
      //seleccionarTab(3);  //tab resultados
    } else if (modo.equals("edit")) {
      tituloVentana.setValue(titulo + " » Editar");
      verElementosEntrada(true);
      mostrarElementosVisualizacion(false);
      //camposModoEdicion(true);
      setFocoEdicion();
      //btnEditar.setVisible(false);
      //btnGuardar.setVisible(true);
      //btnConfirmar.setVisible(false);
    } else if (modo.equals("confirmado")) {
      verElementosEntrada(false);
      mostrarElementosVisualizacion(true);
      //camposModoEdicion(false);
      //btnGuardar.setVisible(false);
      //btnEditar.setVisible(false);
      //btnConfirmar.setVisible(false);
      //btnSave.setDisabled(false);
      //notificarBarra();
    }
  }

  /**
   * habilitar/deshabilita la edición de los campos
   * TODO: mejorar evaluar el método
   * @param status el estado true o false
   */
  //NO USADO
  /*
  public void camposModoEdicion(boolean status) {
  planif$spnInvitados.setDisabled(!status);
  planif$spnReconciliados.setDisabled(!status);
  planif$spnVisitas.setDisabled(!status);
  planif$spnPersonasEnPlanif.setDisabled(!status);
  result$spnInvitados.setDisabled(!status);
  result$spnConvertidos.setDisabled(!status);
  result$spnAmigosNoAsistenIglesia.setDisabled(!status);
  result$spnReconciliados.setDisabled(!status);
  result$spnCDO.setDisabled(!status);
  result$spnVisitas.setDisabled(!status);
  result$spnOtrasIglesias.setDisabled(!status);
  result$spnAsistenciaDomingoAnterior.setDisabled(!status);
  result$etqTotalAsistencia.setDisabled(!status);
  ofrendas$txtOfrendasMonto.setDisabled(!status);
  ofrendas$chkEntregadas.setDisabled(!status);
  }
   */
  /**
   * oculta o muestra los elementos de entrada
   * @param status el estado del grid true o false
   */
  public void verElementosEntrada(boolean status) {
    result$gridEdit.setVisible(status);
    planif$colEdit.setVisible(status);
    ofrendas$colEdit.setVisible(status);
    obs$colEdit.setVisible(status);
  }

  /**
   * oculta o muestra la columna donde están los elementos de visualización
   * @param status el estado true o false
   */
  public void mostrarElementosVisualizacion(boolean status) {
    result$gridView.setVisible(status);
    planif$colView.setVisible(status);
    ofrendas$colView.setVisible(status);
    obs$colView.setVisible(status);
  }

  public void onClick$btnNew() throws InterruptedException {
    modo = "new";
    actualizarEstado();
  }

  public void onClick$btnGuardar() throws InterruptedException {
    /**
     * modoActual = "procesando";
     * aquí va el procesamiento de los datos...
     */
    if (reporteNoIngresado()) {
      reporte.setEstatus(ReporteCelula.REPORTE_INGRESADO);
    }
    copiarValoresDeEntradaAVisualizacion();
    modo = "ver";
    actualizarEstado();
  }

//TODO: Mejorar: elegir automáticamente el tab más apropiado de acuerdo al modo y estatus del reporte
  public void onClick$btnEditar() {
    modo = "edit";
    copiarValoresDeVisualizacionAEntrada();
    actualizarEstado();
  }

  public void onClick$btnImprimir() throws InterruptedException {
    modo = "imprimir";
    Messagebox.show("Aquí aparece un reporte 'imprimible' que muestre todos los datos de la célula");
    //...aquí va el procesamiento para imprimir
    modo = "ver";
  }

  //No usado
  public void onClick$btnConfirmar() {
    System.out.println("CtrlReporteCelula. llamada a botón Confirmar");
    System.out.println("funcionalidad por desarrollar");
    //reporte.setEstatus(ReporteCelula.CONFIRMADO);
    //notificarBarra();
  }

  /**
   * le da el foco al primer elemento de entrada del tab actual
   **/
  public void setFocoEdicion() {
    tabSelected = tabbox.getSelectedTab();
    System.out.println("CtrlReporteCelula.setFocoEdicion: tabSelected: " + tabSelected.getLabel());
    System.out.println("CtrlReporteCelula.estado reporte: " + reporte.getDescripcionEstatus());
    if ((tabSelected == tabCelula) || (tabSelected == tabFechas)) {
      if (celulaNoRealizada()) {
        seleccionarTab(tabObservaciones);
      } else {
        seleccionarTab(tabResultados);
      }
    } else if (tabSelected == tabPlanificacion) {
      //TODO: no funciona
      planif$spnInvitados.select();
      planif$spnInvitados.setFocus(true);
    } else if (tabSelected == tabResultados) {
      //TODO: no funciona
      result$spnInvitados.select();
      result$spnInvitados.setFocus(true);
    } else if (tabSelected == tabOfrendas) {
      //TODO: no funciona
      ofrendas$txtOfrendasMonto.setFocus(true);
      ofrendas$txtOfrendasMonto.select();
    } else if (tabSelected == tabObservaciones) {
      obs$txtObservaciones.setFocus(true);
      obs$txtObservaciones.select();

    }
//    if ((tabSeleccionado == 0) && reporteIngresado()) {
//      seleccionarTab(tabResultados);
//    } else if ((tabSeleccionado == 1) && reporteIngresado()) {
//      seleccionarTab(tabResultados);
//    } else if (tabSeleccionado == 2) {
//      //TODO: no funciona
//      planif$spnInvitados.select();
//      planif$spnInvitados.setFocus(true);
//    } else if (tabSeleccionado == 3) {
//      //TODO: no funciona
//      result$spnInvitados.select();
//      result$spnInvitados.setFocus(true);
//    } else if (tabSeleccionado == 4) {
//      //TODO: no funciona
//      ofrendas$txtOfrendasMonto.setFocus(true);
//      ofrendas$txtOfrendasMonto.select();
//    } else if (tabSeleccionado == 5) {
//      obs$txtObservaciones.setFocus(true);
//    }
  }

  /**
   * calcula total de asistencia según entrada del usuario en resultados,
   * no es usado, se elegió usar lo que está en el <zcript>
   **/
  public int getTotalAsistencia() {
    int total = 0;
    int valor1 = result$spnInvitados.getValue().intValue();
    int valor2 = result$spnAmigos.getValue().intValue();
    int valor3 = result$spnCDO.getValue().intValue();
    int valor4 = result$spnOtrasIglesias.getValue().intValue();
    total = valor1 + valor2 + valor3 + valor4;
    return total;
  }

  private void seleccionarTab(Tab tab) {
    tabbox.setSelectedTab(tab);
  }

  /**
   * pasa valores de elementosz de visualización a elementos de entrada
   * 
   */
  private void copiarValoresDeVisualizacionAEntrada() {
    //TODO: usar variable 'reporte'       
    //planif
    planif$spnInvitados.setValue(Integer.parseInt(planif$tbbInvitados.getLabel()));
    planif$spnReconciliados.setValue(Integer.parseInt(planif$tbbReconciliados.getLabel()));
    planif$spnVisitas.setValue(Integer.parseInt(planif$tbbVisitas.getLabel()));
    planif$spnPersonasEnPlanif.setValue(Integer.parseInt(planif$tbbPersonasEnPlanif.getLabel()));
    //result
    result$spnInvitados.setValue(Integer.parseInt(result$etqInvitados.getValue()));
    result$spnConvertidos.setValue(Integer.parseInt(result$etqConvertidos.getValue()));
    result$spnAmigos.setValue(Integer.parseInt(result$etqAmigos.getValue()));
    result$spnReconciliados.setValue(Integer.parseInt(result$etqReconciliados.getValue()));
    result$spnCDO.setValue(Integer.parseInt(result$etqCDO.getValue()));
    result$spnVisitas.setValue(Integer.parseInt(result$etqVisitas.getValue()));
    result$spnOtrasIglesias.setValue(Integer.parseInt(result$etqOtrasIglesias.getValue()));
    result$spnDomingoAnterior.setValue(Integer.parseInt(result$etqDomingoAnterior.getValue()));
    //ofrendas
    ofrendas$txtOfrendasMonto.setValue("" + Double.parseDouble(ofrendas$etqOfrendasMonto.getValue()));
    UtilSIG.mostrarRol(ofrendas$etqOfrendasEntregadas, ofrendas$chkEntregadas.isChecked());
    //Observaciones
    obs$txtObservaciones.setValue(obs$etqObservaciones.getValue());
  }

  private void copiarValoresDeEntradaAVisualizacion() {
    //TODO: usar variable 'reporte'    
    //planif
    planif$tbbInvitados.setLabel("" + planif$spnInvitados.getValue());
    planif$tbbReconciliados.setLabel("" + planif$spnReconciliados.getValue());
    planif$tbbVisitas.setLabel("" + planif$spnVisitas.getValue());
    planif$tbbPersonasEnPlanif.setLabel("" + planif$spnPersonasEnPlanif.getValue());
    //result
    result$etqInvitados.setValue("" + result$spnInvitados.getValue());
    result$etqConvertidos.setValue("" + result$spnConvertidos.getValue());
    result$etqAmigos.setValue("" + result$spnAmigos.getValue());
    result$etqReconciliados.setValue("" + result$spnReconciliados.getValue());
    result$etqCDO.setValue("" + result$spnCDO.getValue());
    result$etqVisitas.setValue("" + result$spnVisitas.getValue());
    result$etqOtrasIglesias.setValue("" + result$spnOtrasIglesias.getValue());
    result$etqDomingoAnterior.setValue("" + result$spnDomingoAnterior.getValue());
    result$etqTotalAsistencia.setValue("" + getTotalAsistencia());
    //ofrendas
    ofrendas$etqOfrendasMonto.setValue("" + ofrendas$txtOfrendasMonto.getValue());
    UtilSIG.marcarRol(ofrendas$chkEntregadas, reporte.isOfrendasEntregadas());
    //Observaciones
    obs$etqObservaciones.setValue(obs$txtObservaciones.getValue());
  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.REPORTE_CELULA);
    Sesion.setModo(modo);
    System.out.println("CtrlReporteCelula.notificarBarra.modo=" + modo);
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }

  /**
   * maneja cambios del tabbox por parte del usuario
   */
  public void onSelect$tabbox() {
    tabSeleccionado = tabbox.getSelectedIndex();
    tabSelected = tabbox.getSelectedTab();
    /*
    if (modo.equals("edit")) {
    setFocoEdicion();
    }
     * 
     */
  }

  /**
   * muestra un widget
   */
  public void mostrarWidget(Component widget) {
    widget.setVisible(true);
  }

  /**
   * oculta un tab del tabbox
   */
  public void ocultarWidget(Component widget) {
    widget.setVisible(false);
  }

  private void mostrarPregunta() {
    mostrarWidget(hboxPreguntaCelulaRealizada);
  }

  private void mostrarTabsDatosReporte() {
    mostrarWidget(tabPlanificacion);
    mostrarWidget(tabResultados);
    mostrarWidget(tabOfrendas);
    mostrarWidget(tabPanelPlanificacion);
    mostrarWidget(tabPanelResultados);
    mostrarWidget(tabPanelOfrendas);
  }

  private void ocultarTabsDatosReporte() {
    ocultarWidget(tabPlanificacion);
    ocultarWidget(tabResultados);
    ocultarWidget(tabOfrendas);
    ocultarWidget(tabPanelPlanificacion);
    ocultarWidget(tabPanelResultados);
    ocultarWidget(tabPanelOfrendas);
  }

  private void ocultarPregunta() {
    ocultarWidget(hboxPreguntaCelulaRealizada);
  }

  //TODO: validar que fechaCelula sea menor a hoy y menor a domingo
  public void onClick$btnCelulaRealizada() {
    System.out.println("llamada a CtrlReporteCelula.btnCelulaRealizada");
    ocultarPregunta();
    mostrarTabsDatosReporte();
    mostrarWidget(tabObservaciones);
    mostrarMensaje("Ingrese la planificación y los resultados de la célula");
    reporte.setEstatus(ReporteCelula.REPORTE_NO_INGRESADO);
    seleccionarTab(tabResultados);//Resultados    
    modo = "new";
    actualizarEstado();
    notificarBarra();
  }

  public void onClick$btnCelulaNoRealizada() {
    System.out.println("llamada a CtrlReporteCelula.btnCelulaRealizada");
    ocultarPregunta();
    reporte.setEstatus(ReporteCelula.CELULA_NO_REALIZADA);
    mostrarWidget(tabObservaciones);
    seleccionarTab(tabObservaciones);//tabObservaciones
    obs$txtObservaciones.setFocus(true);
    mostrarMensaje("Explique por qué no se realizó la célula");
    modo = "new";
    actualizarEstado();
    notificarBarra();
  }

  /** método debug
   * muestra un mensaje en label
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
  //TODO: optimizar código, de mostrar y ocultar widgets

  /**
   * calcula las fechas correspondientes a la semana
   **/
  void calcularFechas() {
    Calendar cal = new GregorianCalendar();

    //buscar fecha de hoy
    int diaSem = cal.get(Calendar.DAY_OF_WEEK);
    hoy = UtilFechas.getFechaLarga(cal);

    //buscar domingo anterior y sábado siguiente:
    Calendar calDomingoAnterior = UtilFechas.calcularDomingoAnterior();
    Calendar calSabado = UtilFechas.calcularSabadoSiguiente();

    if (igualMes(calDomingoAnterior, calSabado)) {
      domingo = "" + UtilFechas.getDia(calDomingoAnterior);
      sabado = "" + UtilFechas.getDia(calSabado);
      semana = domingo + " — " + sabado + " "
              + UtilFechas.getMesTextoCorto(cal) + ", " + UtilFechas.getAño(cal);
    } else {//semana abarca 2 meses diferentes
      if (igualAño(calDomingoAnterior, calSabado)) {
        domingo = UtilFechas.getFechaTextoDiaMes(calDomingoAnterior);
        sabado = UtilFechas.getFechaTextoDiaMes(calSabado);
        semana = domingo + " — " + sabado + ", " + UtilFechas.getAño(cal);
      } else {//semana abarca 2 años diferentes
        domingo = UtilFechas.getFechaTextoDiaMesAñoAbreviado(calDomingoAnterior);
        sabado = UtilFechas.getFechaTextoDiaMesAñoAbreviado(calSabado);
        semana = domingo + " — " + sabado;
      }
    }


    //buscar dia de célula esta semana:
    int diaSemana = celula.getDiaSemana();
    Calendar cal4 = getDiaCelula(diaSemana);
    //TODO: poner mes en texto corto
    diaCelula = UtilFechas.getFechaTextoCortoSinAño(cal4) + ", " + UtilFechas.getAño(cal);

    System.out.println("hoy: " + hoy);
    System.out.println("domingo anterior: " + domingo);
    System.out.println("sabado: " + sabado);
    System.out.println("periodo mostrado a usuario: " + semana);
    System.out.println("dia-celula: " + diaCelula);
  }

  /**
   * muestra las fechas en los widgets correspondientes
   **/
  void mostrarFechas() {
    calcularFechas();
    fechas$etqHoy.setValue(hoy);
    fechas$etqSemanaInicio.setValue(domingo);
    fechas$etqSemanaFin.setValue(sabado);
    fechas$etqDiaCelula.setValue(diaCelula);
    fechas$etqSemana.setValue(semana);
  }

  //TODO: pasar a clase de Utilería
  /**
   * obtiene un Calendar del día de la célula de esta semana
   * @param hoy el día de la semana
   * @return un calendar con el día de la célula
   */
  Calendar getDiaCelula(int diaSemana) {
    Calendar cal = new GregorianCalendar();
    cal.set(Calendar.DAY_OF_WEEK, diaSemana);
    return cal;
  }

  //TODO: pasar a clase de Utilería
  private boolean igualMes(Calendar calDomingoAnterior, Calendar calSabado) {
    return (calDomingoAnterior.get(Calendar.MONTH) == calSabado.get(Calendar.MONTH));
  }

  //TODO: pasar a clase de Utilería
  private boolean igualAño(Calendar calDomingoAnterior, Calendar calSabado) {
    return (calDomingoAnterior.get(Calendar.YEAR) == calSabado.get(Calendar.YEAR));
  }

  private void mostrarTabsBasicos() {
    mostrarWidget(tabCelula);
    mostrarWidget(tabFechas);
    mostrarWidget(tabObservaciones);
    mostrarWidget(tabPanelCelula);
    mostrarWidget(tabPanelFechas);
    mostrarWidget(tabPanelObservaciones);

  }

  private boolean celulaNoRealizada() {
    return (reporte.getEstatus() == ReporteCelula.CELULA_NO_REALIZADA);
  }

  private boolean reporteNoIngresado() {
    return (reporte.getEstatus() == ReporteCelula.REPORTE_NO_INGRESADO);
  }

  private boolean reporteIngresado() {
    return (reporte.getEstatus() == ReporteCelula.REPORTE_INGRESADO);
  }
}
