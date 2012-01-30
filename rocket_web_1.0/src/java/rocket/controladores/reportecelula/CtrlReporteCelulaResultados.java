package rocket.controladores.reportecelula;

import rocket.modelo.bd.util.CelulaUtil;
import rocket.modelo.bd.util.ReporteCelulaUtil;
import rocket.controladores.general.Sesion;
import java.util.ArrayList;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Spinner;
import rocket.modelo.servicios.ServicioCelula;
import rocket.modelo.servicios.ServicioReporteCelula;

/**
 * controlador asociado a vistaReporteCelula/Resultados.zul
 * para actualizar valores
 * @author Gabriel
 */
public class CtrlReporteCelulaResultados extends GenericForwardComposer {

  private boolean datosRecuperadosPrimeraVez = false;
  //para evitar doble actualización (de método onOK y onBlur para txtOfrendasMonto)
  private boolean ofrendasProcesadas = false;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    if (Sesion.modoIngresar()) {
      //+ setVarSesionDefault();
    } else {
      //- getIdReporte();
      //traer variables de sesión de valores de resultados
    }
  }

  /**
   * trae valor idReporteCelula de variable de sesión
   **/
  private int getIdReporte() {
    System.out.println("CtrlReporteCelulaResultados.getIdReporteCelula");
    idReporteCelula = (Integer) Sesion.getVariable("idReporteCelula");
    return idReporteCelula;
  }

  /**
   * muestra un mensaje en un label, para interacción con el usuario
   * @param msj 
   */
  //TODO: mejora código: pasar a clase de utilería
  private void mensaje(String msj) {
    etqMensaje.setValue(msj);
    etqMensaje.setVisible(true);
    //-btnCerrarMensaje.setVisible(true);
    divMensaje.setVisible(true);
    System.out.println(this.getClass().toString() + msj);
  }

  /** 
   * limpia el mensaje de estado
   */
  //TODO: mejora código: pasar a clase de utilería
  private void ocultarMensaje() {
    etqMensaje.setValue("");//TODO: CODIGO: línea parece redundante
    etqMensaje.setVisible(false);
    //-btnCerrarMensaje.setVisible(false);
    divMensaje.setVisible(false);
  }

  //TODO: mejora código: pasar a clase de utilería
  int getValorSpinner(Spinner spinner) {
    int valor = 0;
    try {
      valor = spinner.getValue();
    } catch (Exception e) {
      valor = 0;
      System.out.println("CtrlReporteCelulaResultados.activarEditInvitados");
    }
    return valor;
  }

  //TODO: mejora código: pasar a clase de utilería
  double getValorDecimalbox(Decimalbox textbox) {
    double valor = 0;
    try {
      valor = textbox.getValue().doubleValue();
    } catch (Exception e) {
      valor = 0;
      System.out.println("CtrlReporteCelulaResultados.activarEditInvitados");
    }
    return valor;
  }

  //TODO: mejora código: pasar a clase de utilería
  int getValorEnteroEtiqueta(Label etiqueta) {
    int valor = 0;
    try {
      valor = Integer.parseInt(etiqueta.getValue());
    } catch (Exception e) {
      valor = 0;
      System.out.println("CtrlReporteCelulaResultados.activarEditInvitados");
    }
    return valor;
  }

  //TODO: mejora código: pasar a clase de utilería
  double getValorDoubleEtiqueta(Label etiqueta) {
    double monto = 0;
    try {
      String valor = etiqueta.getValue();
      if (valor.equals("0,00")) {
        return monto = 0.00;
      }
      return monto = Double.parseDouble(etiqueta.getValue());
    } catch (Exception e) {
      monto = 0.00;
      System.out.println("CtrlReporteCelulaResultados.activarEditInvitados");
    }
    return monto;
  }

  //procesar eventos de actualización
  public void onClick$divInvitados() {
    System.out.println("CtrlReporteCelulaResultados. click en divInvitados");
    activarEditInvitados();
  }

  /**
   * activa la edición de invitados
   */
  private void activarEditInvitados() {
    ocultarMensaje();
    etqInvitados.setVisible(false);
    invitados = getValorEnteroEtiqueta(etqInvitados);
    spnInvitados.setValue(invitados);
    spnInvitados.setVisible(true);
    spnInvitados.setFocus(true);
  }

  /**
   * desactiva la edición de invitados
   * y muestra el valor actual
   */
  private void cancelarEditInvitados() {
    spnInvitados.setVisible(false);
    etqInvitados.setVisible(true);
    mostrarValorInvitados();
  }

  public void onChange$spnInvitados() {
    procesarInvitados();
  }

  public void onOK$spnInvitados() {
    procesarInvitados();
    cancelarEditInvitados();
  }

  public void onBlur$spnInvitados() {
    cancelarEditInvitados();
  }

  private void procesarInvitados() {
    //- ocultarMensaje();
    int nuevoValor = getValorSpinner(spnInvitados);
    if (nuevoValor == invitados) {
      //usuario dejó el valor vacío o dejó el valor anterior
      return;
    }
    //se cambió el valor
    if (!validoDesgloseInvitados(true, false, false)) {
      return;
    }
    if (actualizarInvitados(nuevoValor)) {
      invitados = nuevoValor;
      mostrarValorInvitados();
      actualizarTotalAsistencia();
    }
  }

  void mostrarValorInvitados() {
    etqInvitados.setValue("" + invitados);
  }

  /**
   * actualiza el invitados de la célula en la base de datos
   */
  boolean actualizarInvitados(int nuevoValor) {
    if (servicioReporteCelula.actualizarResultadoInvitados(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;
  }

  public void onClick$divConvertidos() {
    System.out.println("CtrlReporteCelulaResultados. click en divInvitados");
    activarEditConvertidos();
  }

  /**
   * activa la edición de Convertidos
   */
  private void activarEditConvertidos() {
    ocultarMensaje();
    etqConvertidos.setVisible(false);
    convertidos = getValorEnteroEtiqueta(etqConvertidos);
    spnConvertidos.setValue(convertidos);
    spnConvertidos.setVisible(true);
    spnConvertidos.setFocus(true);
  }

  /**
   * desactiva la edición de Convertidos
   * y muestra el valor actual
   */
  private void cancelarEditConvertidos() {
    spnConvertidos.setVisible(false);
    etqConvertidos.setVisible(true);
    mostrarValorConvertidos();
  }

  public void onChange$spnConvertidos() {
    procesarConvertidos();
  }

  public void onOK$spnConvertidos() {
    procesarConvertidos();
    cancelarEditConvertidos();
  }

  public void onBlur$spnConvertidos() {
    cancelarEditConvertidos();
  }

  private void procesarConvertidos() {
    //- ocultarMensaje();
    int nuevoValor = getValorSpinner(spnConvertidos);
    if (nuevoValor == convertidos) {
      //usuario dejó el valor vacío o dejó el valor anterior
      return;
    }
    //se cambió el valor
    if (!validoDesgloseInvitados(false, true, false)) {
      return;
    }
    if (actualizarConvertidos(nuevoValor)) {
      convertidos = nuevoValor;
      mostrarValorConvertidos();
    }
  }

  void mostrarValorConvertidos() {
    etqConvertidos.setValue("" + convertidos);
  }

  /**
   * actualiza Convertidos de la célula en la base de datos
   */
  boolean actualizarConvertidos(int nuevoValor) {
    if (servicioReporteCelula.actualizarResultadoConvertidos(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;
  }

  public void onClick$divAmigos() {
    System.out.println("CtrlReporteCelulaResultados. click en divAmigos");
    activarEditAmigos();
  }

  /**
   * activa la edición de Amigos
   */
  private void activarEditAmigos() {
    ocultarMensaje();
    etqAmigos.setVisible(false);
    amigos = getValorEnteroEtiqueta(etqAmigos);
    spnAmigos.setValue(amigos);
    spnAmigos.setVisible(true);
    spnAmigos.setFocus(true);
  }

  /**
   * desactiva la edición de Amigos
   * y muestra el valor actual
   */
  private void cancelarEditAmigos() {
    spnAmigos.setVisible(false);
    etqAmigos.setVisible(true);
    mostrarValorAmigos();
  }

  public void onChange$spnAmigos() {
    procesarAmigos();
  }

  public void onOK$spnAmigos() {
    procesarAmigos();
    cancelarEditAmigos();
  }

  public void onBlur$spnAmigos() {
    procesarAmigos();
    cancelarEditAmigos();
  }

  private void procesarAmigos() {
    //- ocultarMensaje();
    int nuevoValor = getValorSpinner(spnAmigos);
    if (nuevoValor == amigos) {
      //usuario dejó el valor vacío o dejó el valor anterior
      return;
    }
    //se cambió el valor
    if (actualizarAmigos(nuevoValor)) {
      amigos = nuevoValor;
      mostrarValorAmigos();
      actualizarTotalAsistencia();
    }
  }

  void mostrarValorAmigos() {
    etqAmigos.setValue("" + amigos);
  }

  /**
   * actualiza el Amigos de la célula en la base de datos
   */
  boolean actualizarAmigos(int nuevoValor) {
    if (servicioReporteCelula.actualizarResultadoAmigos(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;
  }

  public void onClick$divReconciliados() {
    System.out.println("CtrlReporteCelulaResultados. click en divAmigos");
    activarEditReconciliados();
  }

  /**
   * activa la edición de Reconciliados
   */
  private void activarEditReconciliados() {
    ocultarMensaje();
    etqReconciliados.setVisible(false);
    reconciliados = getValorEnteroEtiqueta(etqReconciliados);
    spnReconciliados.setValue(reconciliados);
    spnReconciliados.setVisible(true);
    spnReconciliados.setFocus(true);
  }

  /**
   * desactiva la edición de Reconciliados
   * y muestra el valor actual
   */
  private void cancelarEditReconciliados() {
    spnReconciliados.setVisible(false);
    etqReconciliados.setVisible(true);
    mostrarValorReconciliados();
  }

  public void onChange$spnReconciliados() {
    procesarReconciliados();
  }

  public void onOK$spnReconciliados() {
    procesarReconciliados();
    cancelarEditReconciliados();
  }

  public void onBlur$spnReconciliados() {
    cancelarEditReconciliados();
  }

  private void procesarReconciliados() {
    //- ocultarMensaje();
    int nuevoValor = getValorSpinner(spnReconciliados);
    if (nuevoValor == reconciliados) {
      //usuario dejó el valor vacío o dejó el valor anterior
      return;
    }
    //se cambió el valor
    if (!validoDesgloseInvitados(false, false, true)) {
      return;
    }
    if (actualizarReconciliados(nuevoValor)) {
      reconciliados = nuevoValor;
      mostrarValorReconciliados();
    }
  }

  void mostrarValorReconciliados() {
    etqReconciliados.setValue("" + reconciliados);
  }

  /**
   * actualiza el Reconciliados de la célula en la base de datos
   */
  boolean actualizarReconciliados(int nuevoValor) {
    if (servicioReporteCelula.actualizarResultadoReconciliados(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;
  }

  public void onClick$divCDO() {
    System.out.println("CtrlReporteCelulaResultados. click en divAmigos");
    activarEditCDO();
  }

  /**
   * activa la edición de CDO
   */
  private void activarEditCDO() {
    ocultarMensaje();
    etqCDO.setVisible(false);
    integrantesEstaIglesia = getValorEnteroEtiqueta(etqCDO);
    spnCDO.setValue(integrantesEstaIglesia);
    spnCDO.setVisible(true);
    spnCDO.setFocus(true);
  }

  /**
   * desactiva la edición de CDO
   * y muestra el valor actual
   */
  private void cancelarEditCDO() {
    spnCDO.setVisible(false);
    etqCDO.setVisible(true);
    mostrarValorCDO();
  }

  public void onChange$spnCDO() {
    procesarCDO();
  }

  public void onOK$spnCDO() {
    procesarCDO();
    cancelarEditCDO();
  }

  public void onBlur$spnCDO() {
    cancelarEditCDO();
  }

  private void procesarCDO() {
    //- ocultarMensaje();
    int nuevoValor = getValorSpinner(spnCDO);
    if (nuevoValor == integrantesEstaIglesia) {
      //usuario dejó el valor vacío o dejó el valor anterior
      return;
    }
    //se cambió el valor
    if (actualizarCDO(nuevoValor)) {
      integrantesEstaIglesia = nuevoValor;
      mostrarValorCDO();
      actualizarTotalAsistencia();
    }
  }

  void mostrarValorCDO() {
    etqCDO.setValue("" + integrantesEstaIglesia);
  }

  /**
   * actualiza el CDO de la célula en la base de datos
   */
  boolean actualizarCDO(int nuevoValor) {
    if (servicioReporteCelula.actualizarResultadoIntegrantesEstaIglesia(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;

  }

  public void onClick$divVisitas() {
    System.out.println("CtrlReporteCelulaResultados. click en divAmigos");
    activarEditVisitas();
  }

  /**
   * activa la edición de Visitas
   */
  private void activarEditVisitas() {
    ocultarMensaje();
    etqVisitas.setVisible(false);
    visitas = getValorEnteroEtiqueta(etqVisitas);
    spnVisitas.setValue(visitas);
    spnVisitas.setVisible(true);
    spnVisitas.setFocus(true);
  }

  /**
   * desactiva la edición de Visitas
   * y muestra el valor actual
   */
  private void cancelarEditVisitas() {
    spnVisitas.setVisible(false);
    etqVisitas.setVisible(true);
    mostrarValorVisitas();
  }

  public void onChange$spnVisitas() {
    procesarVisitas();
  }

  public void onOK$spnVisitas() {
    procesarVisitas();
    cancelarEditVisitas();
  }

  public void onBlur$spnVisitas() {
    cancelarEditVisitas();
  }

  private void procesarVisitas() {
    //- ocultarMensaje();
    int nuevoValor = getValorSpinner(spnVisitas);
    if (nuevoValor == visitas) {
      //usuario dejó el valor vacío o dejó el valor anterior
      return;
    }
    if (actualizarVisitas(nuevoValor)) {
      //se cambió el valor
      visitas = nuevoValor;
      mostrarValorVisitas();
    }
  }

  void mostrarValorVisitas() {
    etqVisitas.setValue("" + visitas);
  }

  /**
   * actualiza el Visitas de la célula en la base de datos
   */
  boolean actualizarVisitas(int nuevoValor) {
    if (servicioReporteCelula.actualizarResultadoVisitas(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;
  }

  public void onClick$divOtrasIglesias() {
    System.out.println("CtrlReporteCelulaResultados. click en divAmigos");
    activarEditOtrasIglesias();
  }

  /**
   * activa la edición de OtrasIglesias
   */
  private void activarEditOtrasIglesias() {
    ocultarMensaje();
    etqOtrasIglesias.setVisible(false);
    integrantesOtrasIglesias = getValorEnteroEtiqueta(etqOtrasIglesias);
    spnOtrasIglesias.setValue(integrantesOtrasIglesias);
    spnOtrasIglesias.setVisible(true);
    spnOtrasIglesias.setFocus(true);
  }

  /**
   * desactiva la edición de OtrasIglesias
   * y muestra el valor actual
   */
  private void cancelarEditOtrasIglesias() {
    spnOtrasIglesias.setVisible(false);
    etqOtrasIglesias.setVisible(true);
    mostrarValorOtrasIglesias();
  }

  public void onChange$spnOtrasIglesias() {
    procesarOtrasIglesias();
  }

  public void onOK$spnOtrasIglesias() {
    procesarOtrasIglesias();
    cancelarEditOtrasIglesias();
  }

  public void onBlur$spnOtrasIglesias() {
    cancelarEditOtrasIglesias();
  }

  private void procesarOtrasIglesias() {
    //- ocultarMensaje();
    int nuevoValor = getValorSpinner(spnOtrasIglesias);
    if (nuevoValor == integrantesOtrasIglesias) {
      //usuario dejó el valor vacío o dejó el valor anterior
      return;
    }
    //se cambió el valor
    integrantesOtrasIglesias = nuevoValor;
    if (actualizarOtrasIglesias(nuevoValor)) {
      mostrarValorOtrasIglesias();
      actualizarTotalAsistencia();
    }

  }

  void mostrarValorOtrasIglesias() {
    etqOtrasIglesias.setValue("" + integrantesOtrasIglesias);
  }

  /**
   * actualiza el OtrasIglesias de la célula en la base de datos
   */
  boolean actualizarOtrasIglesias(int nuevoValor) {
    if (servicioReporteCelula.actualizarResultadoIntegrantesOtrasIglesias(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;
  }

  public void onClick$divAsistenciaDomingoAnterior() {
    System.out.println("CtrlReporteCelulaResultados. click en divAmigos");
    activarEditDomingoAnterior();
  }

  /**
   * activa la edición de DomingoAnterior
   */
  private void activarEditDomingoAnterior() {
    ocultarMensaje();
    etqAsistenciaDomingoAnterior.setVisible(false);
    asistenciaDomingoAnterior = getValorEnteroEtiqueta(etqAsistenciaDomingoAnterior);
    spnAsistenciaDomingoAnterior.setValue(asistenciaDomingoAnterior);
    spnAsistenciaDomingoAnterior.setVisible(true);
    spnAsistenciaDomingoAnterior.setFocus(true);
  }

  /**
   * desactiva la edición de DomingoAnterior
   * y muestra el valor actual
   */
  private void cancelarEditDomingoAnterior() {
    spnAsistenciaDomingoAnterior.setVisible(false);
    etqAsistenciaDomingoAnterior.setVisible(true);
    mostrarValorDomingoAnterior();
  }

  public void onChange$spnAsistenciaDomingoAnterior() {
    procesarDomingoAnterior();
  }

  public void onOK$spnAsistenciaDomingoAnterior() {
    procesarDomingoAnterior();
    cancelarEditDomingoAnterior();
  }

  public void onBlur$spnAsistenciaDomingoAnterior() {
    procesarDomingoAnterior();
    cancelarEditDomingoAnterior();
  }

  private void procesarDomingoAnterior() {
    //- ocultarMensaje();
    int nuevoValor = getValorSpinner(spnAsistenciaDomingoAnterior);
    if (nuevoValor == asistenciaDomingoAnterior) {
      //usuario dejó el valor vacío o dejó el valor anterior
      return;
    }
    //se cambió el valor
    if (actualizarDomingoAnterior(nuevoValor)) {
      asistenciaDomingoAnterior = nuevoValor;
      mostrarValorDomingoAnterior();
    }
  }

  void mostrarValorDomingoAnterior() {
    etqAsistenciaDomingoAnterior.setValue("" + asistenciaDomingoAnterior);
  }

  /**
   * actualiza el DomingoAnterior de la célula en la base de datos
   */
  boolean actualizarDomingoAnterior(int nuevoValor) {
    if (servicioReporteCelula.actualizarResultadoAsistenciaDomingoAnterior(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;
  }

  /**
   * actualiza total de asistencia para mostrarlo en la vista
   */
  private void actualizarTotalAsistencia() {
    if (!datosRecuperadosPrimeraVez) {
      getValoresPrimeraVez();
    }
    totalAsistencia = invitados + amigos + integrantesEstaIglesia + integrantesOtrasIglesias;
    etqTotalAsistencia.setValue("" + totalAsistencia);
    servicioReporteCelula.actualizarResultadoTotalAsistencia(getIdReporte(), totalAsistencia);
  }

  /**
   * traer valores actuales
   * usado para antes de primera modificación
   */
  private void getValoresPrimeraVez() {
    System.out.println("getValoresPrimeraVez()");
    invitados = getValorEnteroEtiqueta(etqInvitados);
    convertidos = getValorEnteroEtiqueta(etqConvertidos);
    reconciliados = getValorEnteroEtiqueta(etqReconciliados);
    amigos = getValorEnteroEtiqueta(etqAmigos);
    integrantesEstaIglesia = getValorEnteroEtiqueta(etqCDO);
    integrantesOtrasIglesias = getValorEnteroEtiqueta(etqOtrasIglesias);
    totalAsistencia = invitados + amigos + integrantesEstaIglesia + integrantesOtrasIglesias;
    datosRecuperadosPrimeraVez = true;
  }

  /**
   * valida que se cumpla la siguiente condición:
   * (convertidos + reconciliados) <= invitados
   */
  private boolean validoDesgloseInvitados(boolean cambiandoInvitados, boolean cambiandoConvertidos, boolean cambiandoReconciliados) {
    if (!datosRecuperadosPrimeraVez) {
      getValoresPrimeraVez();
    }
    int invitadosTemp = invitados;
    int convertidosTemp = convertidos;
    int reconciliadosTemp = reconciliados;
    if (cambiandoInvitados) {
      invitadosTemp = getValorSpinner(spnInvitados);
    } else if (cambiandoConvertidos) {
      convertidosTemp = getValorSpinner(spnConvertidos);
    } else if (cambiandoReconciliados) {
      reconciliadosTemp = getValorSpinner(spnReconciliados);
    }
    //chequeo de suma
    boolean ok = false;
    if ((convertidosTemp + reconciliadosTemp) <= invitadosTemp) {
      ok = true;
    }
    if (ok) {
      //ocultar mensaje de error
      rowMsjError.setVisible(false);
    } else {
      //mostrar mensaje de error
      rowMsjError.setVisible(true);
      //TODO: resaltar campos involucrados con colores
    }
    return ok;
  }

  public void onClick$divOfrendasMonto() {
    System.out.println("CtrlReporteCelulaResultados. click en divAmigos");
    activarEditOfrendas();
  }

  /**
   * activa la edición de Ofrendas
   */
  private void activarEditOfrendas() {
    ocultarMensaje();
    ofrendasProcesadas = false;
    etqOfrendasMonto.setVisible(false);
    txtOfrendasMonto.setVisible(true);
    txtOfrendasMonto.setFocus(true);
    ofrendas = getValorDoubleEtiqueta(etqOfrendasMonto);
    txtOfrendasMonto.setValue("" + ofrendas);
  }

  /**
   * desactiva la edición de Ofrendas
   * y muestra el valor actual
   */
  private void cancelarEditOfrendas() {
    txtOfrendasMonto.setVisible(false);
    etqOfrendasMonto.setVisible(true);
    mostrarValorOfrendas();
  }

  public void onOK$txtOfrendasMonto() {
    procesarOfrendas();
    cancelarEditOfrendas();
  }

  public void onBlur$txtOfrendasMonto() {
    procesarOfrendas();
    cancelarEditOfrendas();
  }

  private void procesarOfrendas() {
    if (ofrendasProcesadas) {
      return;
    }
    //- ocultarMensaje();
    double nuevoValor = getValorDecimalbox(txtOfrendasMonto);
    ofrendasProcesadas = true;
    if (nuevoValor == ofrendas) {
      //usuario dejó el valor vacío o dejó el valor anterior
      return;
    }
    if (actualizarOfrendas(nuevoValor)) {
      //se cambió el valor
      ofrendas = nuevoValor;
      mostrarValorOfrendas();
    }
  }

  void mostrarValorOfrendas() {
    etqOfrendasMonto.setValue("" + ofrendas);
  }

  /**
   * actualiza el Ofrendas de la célula en la base de datos
   */
  boolean actualizarOfrendas(double nuevoValor) {
    if (servicioReporteCelula.actualizarResultadoOfrendas(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;
  }
  /**
   * atributos
   */
  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //widgets:
  Label tituloVentana;
  Label etqInvitados;
  Label etqConvertidos;
  Label etqAmigos;
  Label etqReconciliados;
  Label etqCDO;
  Label etqVisitas;
  Label etqOtrasIglesias;
  Label etqAsistenciaDomingoAnterior;
  Label etqTotalAsistencia;
  Label etqOfrendasMonto;
  Spinner spnInvitados;
  Spinner spnConvertidos;
  Spinner spnAmigos;
  Spinner spnReconciliados;
  Spinner spnCDO;
  Spinner spnVisitas;
  Spinner spnOtrasIglesias;
  Spinner spnAsistenciaDomingoAnterior;
  Decimalbox txtOfrendasMonto;
  //mensajes:
  Div divMensaje;
  Label etqMensaje;
  //variables de control:
  //modo = {new,edicion,ver,imprimir,confirmado}
  int invitados = 0;
  int convertidos = 0;
  int reconciliados = 0;
  int visitas = 0;
  int asistenciaDomingoAnterior = 0;
  int amigos = 0;
  int integrantesEstaIglesia = 0;
  int integrantesOtrasIglesias = 0;
  int totalAsistencia = 0;
  double ofrendas = 0.00;
  Row rowMsjError;
  //gestión de datos:
  int idReporteCelula;
  ArrayList lista;
  CelulaUtil celula = new CelulaUtil();
  ReporteCelulaUtil reporte = new ReporteCelulaUtil();
  ServicioCelula servicioCelula = new ServicioCelula();
  ServicioReporteCelula servicioReporteCelula = new ServicioReporteCelula();
}
/**
 * TAREAS:
 * >. agregar parámetro 'idReporte' para todos los métodos de actualización
 * >. en futuras versiones: comprobar permiso de edición de reporte
 */
