package rocket.controladores.reportecelula;

import rocket.modelo.bd.util.ReporteCelulaUtil;
import rocket.controladores.general.Sesion;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Spinner;
import rocket.modelo.servicios.ServicioCelula;
import rocket.modelo.servicios.ServicioReporteCelula;

/**
 * controlador asociado a vistaReporteCelula/Planificacion.zul
 * para actualizar valores
 * @author Gabriel
 */
public class CtrlReporteCelulaPlanificacion extends GenericForwardComposer {

  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //widgets:
  Label tituloVentana;
  Label etqInvitados;
  Label etqReconciliados;
  Label etqPersonasEnPlanif;
  Label etqVisitas;
  Label etqOtrasIglesias;
  Label etqAsistenciaDomingoAnterior;
  Label etqTotalAsistencia;
  Spinner spnInvitados;
  Spinner spnConvertidos;
  Spinner spnAmigos;
  Spinner spnReconciliados;
  Spinner spnPersonasEnPlanif;
  Spinner spnVisitas;
  Spinner spnOtrasIglesias;
  Spinner spnAsistenciaDomingoAnterior;
  //mensajes:
  Div divMensaje;
  Label etqMensaje;
  //variables de control:
  //modo = {new,edicion,ver,imprimir,confirmado}
  int invitados = 0;
  int reconciliados = 0;
  int visitas = 0;
  int asistenciaDomingoAnterior = 0;
  int personasEnPlanif = 0;
  //gestión de datos:
  int idReporteCelula;
  ReporteCelulaUtil reporte = new ReporteCelulaUtil();
  ServicioCelula servicioCelula = new ServicioCelula();
  ServicioReporteCelula servicioReporteCelula = new ServicioReporteCelula();
  private boolean datosRecuperadosPrimeraVez = false;

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
  int getValorNumericoEtiqueta(Label etiqueta) {
    int valor = 0;
    try {
      valor = Integer.parseInt(etiqueta.getValue());
    } catch (Exception e) {
      valor = 0;
      System.out.println("CtrlReporteCelulaResultados.activarEditInvitados");
    }
    return valor;
  }

  //procesar eventos de actualización
  public void onClick$divInvitados() {
    //**System.out.println("CtrlReporteCelulaResultados. click en divInvitados");
    activarEditInvitados();
  }

  /**
   * activa la edición de invitados
   */
  private void activarEditInvitados() {
    ocultarMensaje();
    etqInvitados.setVisible(false);
    invitados = getValorNumericoEtiqueta(etqInvitados);
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
    if (actualizarInvitados(nuevoValor)) {
      invitados = nuevoValor;
      mostrarValorInvitados();
    }
  }

  void mostrarValorInvitados() {
    etqInvitados.setValue("" + invitados);
  }

  /**
   * actualiza el invitados de la célula en la base de datos
   */
  boolean actualizarInvitados(int nuevoValor) {
    if (servicioReporteCelula.actualizarPlanificacionInvitados(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;
  }
  //procesar eventos de actualización

  public void onClick$divReconciliados() {
    //**System.out.println("CtrlReporteCelulaResultados. click en divInvitados");
    activarEditReconciliados();
  }

  /**
   * activa la edición de Reconciliados
   */
  private void activarEditReconciliados() {
    ocultarMensaje();
    etqReconciliados.setVisible(false);
    reconciliados = getValorNumericoEtiqueta(etqReconciliados);
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
    if (servicioReporteCelula.actualizarPlanificacionReconciliados(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;
  }

  public void onClick$divPersonasEnPlanif() {
    //**System.out.println("CtrlReporteCelulaResultados. click en divAmigos");
    activarEditPersonasEnPlanif();
  }

  /**
   * activa la edición de PersonasEnPlanif
   */
  private void activarEditPersonasEnPlanif() {
    ocultarMensaje();
    etqPersonasEnPlanif.setVisible(false);
    personasEnPlanif = getValorNumericoEtiqueta(etqPersonasEnPlanif);
    spnPersonasEnPlanif.setValue(2);
    spnPersonasEnPlanif.setVisible(true);
    spnPersonasEnPlanif.setFocus(true);
  }

  /**
   * desactiva la edición de PersonasEnPlanif
   * y muestra el valor actual
   */
  private void cancelarEditPersonasEnPlanif() {
    spnPersonasEnPlanif.setVisible(false);
    etqPersonasEnPlanif.setVisible(true);
    mostrarValorPersonasEnPlanif();
  }

  public void onChange$spnPersonasEnPlanif() {
    procesarPersonasEnPlanif();
  }

  public void onOK$spnPersonasEnPlanif() {
    procesarPersonasEnPlanif();
    cancelarEditPersonasEnPlanif();
  }

  public void onBlur$spnPersonasEnPlanif() {
    cancelarEditPersonasEnPlanif();
  }

  private void procesarPersonasEnPlanif() {
    //- ocultarMensaje();
    int nuevoValor = getValorSpinner(spnPersonasEnPlanif);
    if (nuevoValor == personasEnPlanif) {
      //usuario dejó el valor vacío o dejó el valor anterior
      return;
    }
    if (nuevoValor == 0) {
      //usuario dejó el valor vacío
      nuevoValor = 1; //valor mínimo de personas en planificación
    }
    //se cambió el valor
    if (actualizarPersonasEnPlanif(nuevoValor)) {
      personasEnPlanif = nuevoValor;
      mostrarValorPersonasEnPlanif();
    }
  }

  void mostrarValorPersonasEnPlanif() {
    etqPersonasEnPlanif.setValue("" + personasEnPlanif);
  }

  /**
   * actualiza el PersonasEnPlanif de la célula en la base de datos
   */
  boolean actualizarPersonasEnPlanif(int nuevoValor) {
    if (servicioReporteCelula.actualizarPlanificacionNumeroIntegrantes(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;

  }

  public void onClick$divVisitas() {
    //**System.out.println("CtrlReporteCelulaResultados. click en divAmigos");
    activarEditVisitas();
  }

  /**
   * activa la edición de Visitas
   */
  private void activarEditVisitas() {
    ocultarMensaje();
    etqVisitas.setVisible(false);
    visitas = getValorNumericoEtiqueta(etqVisitas);
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
    if (servicioReporteCelula.actualizarPlanificacionVisitas(getIdReporte(), nuevoValor)) {
      mensaje("Se guardaron los cambios");
      return true;
    }
    mensaje("Error guardando cambios");
    return false;
  }

}
/**
 * TAREAS:
 * >. en futuras versiones: comprobar permiso de edición de reporte
 */
