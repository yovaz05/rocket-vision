package rocket.controladores.reportecelula;

import rocket.modelo.bd.util.CelulaUtil;
import rocket.modelo.bd.util.ReporteCelulaUtil;
import rocket.controladores.general.Sesion;
import rocket.controladores.general.Vistas;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
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
import rocket.controladores.general.Constantes;
import rocket.modelo.servicios.ServicioCelula;
import rocket.modelo.servicios.ServicioReporteCelula;
import waytech.utilidades.UtilFechas;

//TODO: MEJORAR EL MANEJO DE FOCOS DE LOS widgets
/**
 * controlador asociado a vistaReporteCelula/Reporte.zul
 * @author Gabriel
 */
public class CtrlReporteCelula extends GenericForwardComposer {

  //referencias:
  Include vistaCentral;
  Include panelCentral;
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
  Label db$etqCodigo;
  Label db$etqNombre;
  Label db$etqRed;
  Label db$etqDireccion;
  Label db$etqDiaHora;
  Label db$etqLider1;
  Label db$etqLider2;
  Label db$etqLider3;
  Label db$etqLider4;
  Grid db$gridLideres;
  /*
  Toolbarbutton db$tbbCodigo;
  Toolbarbutton db$tbbRed;
  Toolbarbutton db$tbbLider1;
  Toolbarbutton db$tbbLider2;
  Toolbarbutton db$tbbLider3;
  Toolbarbutton db$tbbLider4;
   */
  Toolbarbutton db$tbbDiaHora;
  //pestaña "Fechas"
  Label fechas$etqHoy;
  Label fechas$etqSemanaInicio;
  Label fechas$etqSemanaFin;
  Label fechas$etqDiaCelula;
  Label fechas$etqSemana;
//pestaña "Planificación"
  Spinner planif$spnInvitados;
  Spinner planif$spnReconciliados;
  Spinner planif$spnVisitas;
  Spinner planif$spnPersonasEnPlanif;
  Label planif$etqInvitados;
  Label planif$etqReconciliados;
  Label planif$etqVisitas;
  Label planif$etqPersonasEnPlanif;
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
  Spinner result$spnAsistenciaDomingoAnterior;
  Label result$etqTotalAsistencia;
  Label result$etqInvitados;
  Label result$etqConvertidos;
  Label result$etqAmigos;
  Label result$etqReconciliados;
  Label result$etqCDO;
  Label result$etqVisitas;
  Label result$etqOtrasIglesias;
  Label result$etqAsistenciaDomingoAnterior;
  //ofrendas (dentro de la pestaña resultados:
  Decimalbox result$txtOfrendasMonto;
  Label result$etqOfrendasMonto;
  Label result$etqOfrendasEntregadas;
  //pestaña Ofrendas:
  /*+
  Decimalbox +ofrendas+$txtOfrendasMonto;
  Checkbox +ofrendas+$chkEntregadas;
  Label +ofrendas+$etqOfrendasMonto;
  Label +ofrendas+$etqOfrendasEntregadas;
   */
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
  String diaCelula;
  String observaciones = "";
  String descripcionTitulo = "";
  private Tab tabSelected;
  //gestión de datos:
  //-BD datos;
  int idCelula = 0;
  int idReporte = 0;
  int estatusReporte = 0;
  ArrayList lista;
  CelulaUtil celula = new CelulaUtil();
  ReporteCelulaUtil reporte = new ReporteCelulaUtil();
  ServicioCelula servicioCelula = new ServicioCelula();
  ServicioReporteCelula servicioReporteCelula = new ServicioReporteCelula();

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  ///TODO: hacer este método simular a CtrlCelula.inicio()
  public void inicio() throws InterruptedException {
    //-if redundante:
    modo = Sesion.getModo();
    if (modo == null) {
      modo = "ver";
    }
    System.out.println("CtrlReporteCelula.modo: " + modo);
    getIdCelula();
    if (idCelula == 0) {
      mensaje("Error buscando datos. idCelula = 0");
      return;
    }
    buscarDataCelula();
    mostrarTabCelula(true);
    mostrarDatosCelula();
    mostrarFechas();
    //- mostrarTabsDatosReporte(false);
    if (modo.equals("ver")) {
      ocultarPregunta();
      estatusReporte = celula.getEstatusReporteSemanaActual();
      //**System.out.println("estado reporte: " + reporte.getDescripcionEstatus());
      //**System.out.println("celula estatus reporte semana Actual: " + estatusReporte);
      //reporte ingresado, célula realizada
      if (estatusReporte == ReporteCelulaUtil.REPORTE_INGRESADO) {
        idReporte = buscarDataReporte();
        setVarSesion();
        mostrarTabsDatosReporte(true);
        mostrarDatosReporteModoView();
        //pestaña seleccionada por defecto: 3="Resultados"
        seleccionarTab(tabResultados);
      } else if (estatusReporte == ReporteCelulaUtil.CELULA_NO_REALIZADA) {
        //célula NO realizada
        //**System.out.println("estatus = CELULA_NO_REALIZADA");
        ocultarPregunta();
        mensaje("Célula NO realizada");
        mostrarTabObservaciones(true);
        seleccionarTab(tabObservaciones);
        buscarDataObservaciones();
      }
      mostrarObservaciones();
    } else if (modo.equals("new-pregunta")) {
      mostrarPregunta();
      //- ocultarWidget(tabObservaciones);
    }
    notificarBarra();
    descripcionTitulo = celula.getCodigo() + ". Semana » " + semana;
    actualizarEstado();
  }

  /**
   * recupera parámetro idCelula que viene de la vista llamante
   */
  private void getIdCelula() throws InterruptedException {
    idCelula = 0;
    try {
      idCelula = (Integer) Sesion.getVariable("idCelula");
    } catch (Exception e) {
      System.out.println("CtrlReporteCelula -> ERROR: parámetro idCelula nulo... " + e);
      Messagebox.show("Error buscando datos de célula");
    } finally {
      //TODO:
      //id = UtilFechas.calcularIdReporteCelula(idCelula);
      System.out.println("CtrlReporteCelula -> idCelula = " + idCelula);
    }
  }

  private void buscarDataCelula() throws InterruptedException {
    celula = servicioCelula.getCelula(idCelula);
    //**System.out.println("CtrlReporteCelula.celula=" + celula.toString());
    //**
    System.out.println("CtrlReporteCelula.buscarData.reporte.estatus=" + reporte.getEstatus());
  }

  /**
   * busca datos del reporte (resultados y planificación
   * los almacena en variable reporte
   * devuelve id del reporte
   * @throws InterruptedException 
   */
  private int buscarDataReporte() throws InterruptedException {
    //TODO: falta pasar como parámetro la semana actual
    reporte = servicioReporteCelula.getReporteCelulaSemanaActual(idCelula);
    //**System.out.println("CtrlReporteCelula.buscarData.reporte.estatus=" + reporte.getEstatus());
    return reporte.getIdReporte();
  }

  /**
   * muestra datos de la célula y las fechas correspondiente a la semana actual
   */
  private void mostrarDatosCelula() {
    //* System.out.println("CtrlReporteCelula - célula:" + celula.toString());
    db$etqCodigo.setValue(celula.getCodigo());
    db$etqDireccion.setValue(celula.getDireccionCorta());

    String diaTexto, horaTexto, diaHora;
    diaTexto = celula.getDia();
    horaTexto = celula.getHora();
    if (diaTexto.isEmpty() || horaTexto.isEmpty()) {
      //si no hay valor, mostrar no asignados
      diaHora = "No asignados";
    } else {
      diaHora = diaTexto + " - " + horaTexto;
    }
    db$etqDiaHora.setValue(diaHora);

    db$etqRed.setValue(celula.getNombreRed());
    db$etqLider1.setValue(celula.getNombreLider1());
    db$etqLider2.setValue(celula.getNombreLider2());
    db$etqLider3.setValue(celula.getNombreLider3());
    db$etqLider4.setValue(celula.getNombreLider4());

    //parámetros para navegación dinámica:
    final int idLider1 = celula.getIdLider1();
    final int idLider2 = celula.getIdLider2();
    final int idLider3 = celula.getIdLider3();
    final int idLider4 = celula.getIdLider4();

    /*
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
     */
  }

  /**
   * muestra datos del reporte en widgets que sólo permiten visualizar
   */
  private void mostrarDatosReporteModoView() {
    //llenar widgets con la data
    if (reporte == null) {
      System.out.println("ERROR: CtrlReporteCelula - mostrarDatos: reporte == null");
    }
    System.out.println("CtrlReporteCelula - reporte:" + reporte.toString());

    //planif
    /**/
    planif$etqInvitados.setValue("" + reporte.getPlanificacionInvitados());
    planif$etqReconciliados.setValue("" + reporte.getPlanificacionReconciliados());
    planif$etqVisitas.setValue("" + reporte.getPlanificacionVisitas());
    planif$etqPersonasEnPlanif.setValue("" + reporte.getPlanificacionPersonasEnplanificacion());
    /**/
    //result
    result$etqInvitados.setValue("" + reporte.getResultadoInvitados());
    result$etqConvertidos.setValue("" + reporte.getResultadoConvertidos());
    result$etqAmigos.setValue("" + reporte.getResultadoAmigosNoAsistenIglesia());
    result$etqReconciliados.setValue("" + reporte.getResultadoReconciliados());
    result$etqCDO.setValue("" + reporte.getResultadoCDO());
    result$etqVisitas.setValue("" + reporte.getResultadoVisitas());
    result$etqOtrasIglesias.setValue("" + reporte.getResultadoOtrasIglesias());
    result$etqAsistenciaDomingoAnterior.setValue("" + reporte.getResultadoAsistenciaDomingoAnterior());
    result$etqTotalAsistencia.setValue("" + reporte.getTotalAsistenciaCelula());
    //ofrendas
    result$etqOfrendasMonto.setValue("" + reporte.getOfrendasMonto());
    //+ ofrendas$chkEntregadas.setChecked(reporte.isOfrendasEntregadas());
  }

  /**
   * muestra datos del reporte en widgets que permiten edición
   */
  private void mostrarDatosReporteModoEditable() {
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
    result$spnAsistenciaDomingoAnterior.setValue(reporte.getResultadoAsistenciaDomingoAnterior());
    result$etqTotalAsistencia.setValue("" + reporte.getTotalAsistenciaCelula());
    //ofrendas
    result$etqOfrendasMonto.setValue("" + reporte.getOfrendasMonto());
    //+ ofrendas$chkEntregadas.setChecked(reporte.isOfrendasEntregadas());
  }

  /**
   * mostrar valor de 'observaciones'
   */
  public void mostrarObservaciones() {
    if (observaciones.isEmpty() || observaciones.equals("NA")) {
      observaciones = Constantes.VALOR_EDITAR;
    }
    obs$etqObservaciones.setValue(observaciones);
  }

  /**
   * actualiza estado de widgets
   */
  public void actualizarEstado() {
    if (modo.equals("new-pregunta")) {
      tituloVentana.setValue(titulo + " » " + descripcionTitulo);
      //camposModoEdicion(true);
      //-verElementosEntrada(true);
      //-mostrarElementosVisualizacion(false);
      //btnConfirmar.setVisible(false);
      //btnEditar.setVisible(false);
      //btnGuardar.setVisible(true);
      //-setFocoEdicion();
    } else if (modo.equals("new")) {
      //-tituloVentana.setValue(titulo + " » Ingresar: " + descripcionTitulo);
      //camposModoEdicion(true);
      //-verElementosEntrada(true);
      //-mostrarElementosVisualizacion(false);
      //btnConfirmar.setVisible(false);
      //btnEditar.setVisible(false);
      //btnGuardar.setVisible(true);
      //-setFocoEdicion();
    } else if (modo.equals("ver")) {
      //TODO: mostrar código en titulo
      //tituloVentana.setValue(titulo + " " + celula.getCodigo() + " << 'TAREA: Agregar fechas aquí'");
      tituloVentana.setValue(titulo + " » " + descripcionTitulo);
      //camposModoEdicion(false);
      //-verElementosEntrada(false);
      //-mostrarElementosVisualizacion(true);
      //btnGuardar.setVisible(false);
      //TODO: dependiendo de los permisos y el usuario que está consultando, se muestra el botón de edición
      btnEditar.setVisible(false);
      //btnConfirmar.setVisible(true);
      //+verElementosEntrada(false);
      //+mostrarElementosVisualizacion(true);      
      //seleccionarTab(3);  //tab resultados
    } else if (modo.equals("edit")) { //TODO: MEJORA CODIGO: IF NO USADO
      tituloVentana.setValue(titulo + " » Editar");
      //-verElementosEntrada(true);
      //-mostrarElementosVisualizacion(false);
      //camposModoEdicion(true);
      //-setFocoEdicion();
      //btnEditar.setVisible(false);
      //btnGuardar.setVisible(true);
      //btnConfirmar.setVisible(false);
    } else if (modo.equals("confirmado")) {//TODO: MEJORA CODIGO: IF NO USADO
      //-verElementosEntrada(false);
      //-mostrarElementosVisualizacion(true);
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
  result$etqOfrendasMonto.setDisabled(!status);
  ofrendas$chkEntregadas.setDisabled(!status);
  }
   */
  /**
   * oculta o muestra los elementos de entrada
   * @param status el estado del grid true o false
   */
  /*DEPRECATED
  public void verElementosEntrada(boolean status) {
  result$gridEdit.setVisible(status);
  planif$colEdit.setVisible(status);
  ofrendas$colEdit.setVisible(status);
  }
   */
  /**
   * oculta o muestra la columna donde están los elementos de visualización
   * @param status el estado true o false
   */
  /*DEPRECATED
  public void mostrarElementosVisualizacion(boolean status) {
  result$gridView.setVisible(status);
  planif$colView.setVisible(status);
  ofrendas$colView.setVisible(status);
  }
   */
  public void onClick$btnNew() throws InterruptedException {
    modo = "new";
    actualizarEstado();
  }

  /*
  //TODO: debe borrarse este botón
  public void onClick$btnGuardar() throws InterruptedException {
  if (reporteNoIngresado()) {
  reporte.setEstatus(ReporteCelulaUtil.REPORTE_INGRESADO);
  }
  //-copiarValoresDeEntradaAVisualizacion();
  modo = "ver";
  actualizarEstado();
  }
   * 
   */
//TODO: Mejorar: elegir automáticamente el tab más apropiado de acuerdo al modo y estatus del reporte
  //TODO: no usado
  public void onClick$btnEditar() {
    mensaje("Este botón no debería estar al");
    /*
    modo = "edit";
    copiarValoresDeVisualizacionAEntrada();
    actualizarEstado();
     */
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
      result$txtOfrendasMonto.setFocus(true);
      result$txtOfrendasMonto.select();
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
//      result$etqOfrendasMonto.setFocus(true);
//      result$etqOfrendasMonto.select();
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
  //TODO: NO USADO
  /*
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
  result$spnAsistenciaDomingoAnterior.setValue(Integer.parseInt(result$etqAsistenciaDomingoAnterior.getValue()));
  //ofrendas
  result$etqOfrendasMonto.setValue("" + Double.parseDouble(result$etqOfrendasMonto.getValue()));
  //+Util.mostrarRol(ofrendas$etqOfrendasEntregadas, ofrendas$chkEntregadas.isChecked());
  //Observaciones
  obs$txtObservaciones.setValue(obs$etqObservaciones.getValue());
  }
   * 
   */
  //TODO: Mejorar código: NO USADO
  /*
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
  result$etqAsistenciaDomingoAnterior.setValue("" + result$spnAsistenciaDomingoAnterior.getValue());
  result$etqTotalAsistencia.setValue("" + getTotalAsistencia());
  //ofrendas
  result$etqOfrendasMonto.setValue("" + result$etqOfrendasMonto.getValue());
  //+ Util.marcarRol(ofrendas$chkEntregadas, reporte.isOfrendasEntregadas());
  //Observaciones
  obs$etqObservaciones.setValue(obs$txtObservaciones.getValue());
  }
   * 
   */
  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    Sesion.setVistaActual(Vistas.REPORTE_CELULA);
    Sesion.setModo(modo);
    System.out.println("CtrlReporteCelula.notificarBarra.modo=" + modo);
    vistaCentral = Sesion.getVistaCentral();
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

  private void mostrarTabsDatosReporte(boolean visible) {
    if (visible) {
      mostrarWidget(tabPlanificacion);
      mostrarWidget(tabResultados);
      //+ mostrarWidget(tabOfrendas);
      mostrarWidget(tabPanelPlanificacion);
      mostrarWidget(tabPanelResultados);
      //+ mostrarWidget(tabPanelOfrendas);
    } else {
      ocultarWidget(tabPlanificacion);
      ocultarWidget(tabResultados);
      //-ocultarWidget(tabOfrendas);
      ocultarWidget(tabPanelPlanificacion);
      ocultarWidget(tabPanelResultados);
      //-ocultarWidget(tabPanelOfrendas);
    }
  }

  private void ocultarPregunta() {
    ocultarWidget(hboxPreguntaCelulaRealizada);
  }

  /**
   * método llamado cuando el usuario responde SÍ a la pregunta:
   * Sí fue realizada esta semana
   */
  //TODO: validar que fechaCelula sea menor a hoy y menor a domingo
  public void onClick$btnCelulaRealizada() {
    System.out.println("llamada a CtrlReporteCelula.btnCelulaRealizada");
    estatusReporte = 4;
    if (ingresarReporteCelula()) {
      ocultarPregunta();
      mensaje("Ingrese la planificación y los resultados de la célula");
      mostrarTabsDatosReporte(true);
      mostrarWidget(tabObservaciones);
      reporte.setEstatus(ReporteCelulaUtil.REPORTE_NO_INGRESADO);
      seleccionarTab(tabResultados);//Resultados
      activarEditDatosReporte();
      modo = "new";
      actualizarEstado();
      //- notificarBarra();
    }
  }

  /**
   * método llamado cuando el usuario responde NO a la pregunta:
   * NO fue realizada esta semana
   */
  public void onClick$btnCelulaNoRealizada() {
    //**System.out.println("llamada a CtrlReporteCelula.btnCelulaRealizada");
    estatusReporte = 3;
    if (ingresarReporteCelula()) {
      mensaje("Explica por qué no se realizó la célula");
      ocultarPregunta();
      reporte.setEstatus(ReporteCelulaUtil.CELULA_NO_REALIZADA);
      mostrarWidget(tabObservaciones);
      seleccionarTab(tabObservaciones);//tabObservaciones
      activarEditObservaciones();
      modo = "new";
      actualizarEstado();
      //- notificarBarra();
    } else {
      mensaje("Error creando reporte. Por favor intenta de nuevo");
    }
  }

  private boolean ingresarReporteCelula() {
    //crear reporte vacío, con estado
    idReporte = servicioReporteCelula.ingresarReporteCelula(idCelula, estatusReporte);
    System.out.println("ERROR-> CtrlReporteCelula.ingresarReporteCelula. idReporte " + idReporte);
    if (idReporte == 0) {
      System.out.println("ERROR-> CtrlReporteCelula.ingresarReporteCelula. error ingresando reporte");
      return false;
    }
    //cambiar estado de célula, que refleja la ejecución de esta semana
    if (!servicioCelula.actualizarEstado(estatusReporte)) {
      System.out.println("error actualizando estado de célula");
      return false;
    }
    System.out.println("actualizado el estado de célula");
    setVarSesion();
    return true;
  }

  private void activarEditObservaciones() {
    obs$etqObservaciones.setVisible(false);
    obs$txtObservaciones.setValue("");
    obs$txtObservaciones.setVisible(true);
    obs$txtObservaciones.setFocus(true);
  }

  /** método debug
   * muestra un mensaje en label
   * @param msj 
   */
  private void mensaje(String msj) {
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
      semana = domingo + " - " + sabado + " "
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

    //**
    /*
    System.out.println("hoy: " + hoy);
    System.out.println("domingo anterior: " + domingo);
    System.out.println("sabado: " + sabado);
    System.out.println("periodo mostrado a usuario: " + semana);
    System.out.println("dia-celula: " + diaCelula);
     */
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

  private void mostrarTabCelula(boolean visible) {
    mostrarWidget(tabCelula);
    mostrarWidget(tabPanelCelula);
  }
  
  private void mostrarTabObservaciones(boolean visible) {
    mostrarWidget(tabObservaciones);
    mostrarWidget(tabPanelObservaciones);

  }

  private boolean celulaNoRealizada() {
    return (reporte.getEstatus() == ReporteCelulaUtil.CELULA_NO_REALIZADA);
  }

  private boolean reporteNoIngresado() {
    return (reporte.getEstatus() == ReporteCelulaUtil.REPORTE_NO_INGRESADO);
  }

  private boolean reporteIngresado() {
    return (reporte.getEstatus() == ReporteCelulaUtil.REPORTE_INGRESADO);
  }

  /**
   * guarda las variables de sesión
   * para ser usadas por otros controladores
   */
  private void setVarSesion() {
    Sesion.setVariable("idReporteCelula", idReporte);
  }

  private void activarEditDatosReporte() {
    result$etqInvitados.setVisible(false);
    result$spnInvitados.setVisible(true);
  }

  private void buscarDataObservaciones() {
    observaciones = servicioReporteCelula.getObservacionesReporte(idCelula);
  }
}


/**
 * TODO: nroSemanaActual estará en variable de sesión
 * y se buscará una vez ingresado al sistema
 */