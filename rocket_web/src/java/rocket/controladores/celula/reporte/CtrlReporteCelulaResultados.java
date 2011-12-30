package rocket.controladores.celula.reporte;

import cdo.sgd.modelo.bd.util.CelulaUtil;
import cdo.sgd.modelo.bd.util.ReporteCelulaUtil;
import sig.controladores.Sesion;
import java.util.ArrayList;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Spinner;
import sig.modelo.servicios.ServicioCelula;
import sig.modelo.servicios.ServicioReporteCelula;

/**
 * controlador asociado a vistaReporteCelula/Resultados.zul
 * para actualizar valores
 * @author Gabriel
 */
public class CtrlReporteCelulaResultados extends GenericForwardComposer {

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    if (Sesion.modoIngresar()) {
      //+ setVarSesionDefault();
    } else {
      getIdCelula();
    }
  }

  /**
   * muestra un mensaje en un label, para interacción con el usuario
   * @param msj 
   */
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
  private void ocultarMensaje() {
    etqMensaje.setValue("");//TODO: CODIGO: línea parece redundante
    etqMensaje.setVisible(false);
    //-btnCerrarMensaje.setVisible(false);
    divMensaje.setVisible(false);
  }

  public void onClick$divInvitados() {
    System.out.println("CtrlReporteCelulaResultados. click en divInvitados");    
    activarEditInvitados();
  }
  
  public void onClick$etqInvitados() {
    System.out.println("CtrlReporteCelulaResultados. click en etqInvitados");    
    activarEditInvitados();
  }

  /**
   * activa la edición de invitados
   */
  private void activarEditInvitados() {
    etqInvitados.setVisible(false);
    invitados = getValorEtiqueta(etqInvitados);
    spnInvitados.setValue(invitados);
    spnInvitados.setVisible(true);
    spnInvitados.setFocus(true);
  }
  
  //TODO: mejora código: pasar a clase de utilería
  int getValorSpinner(Spinner spinner){
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
  int getValorEtiqueta(Label etiqueta){
    int valor = 0;
    try {
      valor = Integer.parseInt(etiqueta.getValue());
    } catch (Exception e) {
      valor = 0;
      System.out.println("CtrlReporteCelulaResultados.activarEditInvitados");
    }
    return valor;
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
    procesarInvitados();
    cancelarEditInvitados();
  }

  private void procesarInvitados() {
    //- ocultarMensaje();
    int nuevoValor = getValorSpinner(spnInvitados);
    if (nuevoValor == 0 || nuevoValor == invitados) {
      //usuario dejó el valor vacío o dejó el valor anterior
      return;
    }
    //evaluar si es diferente de 0
    //se cambió el valor
    invitados = nuevoValor;
    actualizarInvitados();
  }
  
  void mostrarValorInvitados(){
    etqInvitados.setValue("" + invitados);  
  }

  /**
   * actualiza el invitados de la célula en la base de datos
   */
  void actualizarInvitados() {
    if (servicioReporteCelula.actualizarResultadoInvitados(invitados)) {
      mensaje("Se actualizó el resultado de invitados");
    } else {
      mensaje("Error actualizando el resultado de invitados");
    }
  }
  /**
   * trae valor idCelula de variable de sesión
   **/
  private void getIdCelula() {
    System.out.println("CtrlReporteCelulaResultados.getIdCelula");
    idCelula = (Integer) Sesion.getVariable("idCelula");
  }
  
  /**
   * atributos
   */
  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //widgets:
  Label tituloVentana;
  Label etqTotalAsistencia;
  Label etqInvitados;
  Label etqConvertidos;
  Label etqAmigos;
  Label etqReconciliados;
  Label etqCDO;
  Label etqVisitas;
  Label etqOtrasIglesias;
  Label etqDomingoAnterior;
  Spinner spnInvitados;
  Spinner spnConvertidos;
  Spinner spnAmigos;
  Spinner spnReconciliados;
  Spinner spnCDO;
  Spinner spnVisitas;
  Spinner spnOtrasIglesias;
  Spinner spnDomingoAnterior;
  //mensajes:
  Div divMensaje;
  Label etqMensaje;
  //variables de control:
  //modo = {new,edicion,ver,imprimir,confirmado}
  int invitados = 0;
  int convertidos = 0;
  int reconciliados = 0;
  int visitas = 0;
  int asistenciaDomingo = 0;
  int amigosSoloGrupo = 0;
  int cristianosEstaIglesia = 0;
  int cristianosOtrasIglesias = 0;
  int totalAsistencia = 0;
  //gestión de datos:
  int idCelula;
  ArrayList lista;
  CelulaUtil celula = new CelulaUtil();
  ReporteCelulaUtil reporte = new ReporteCelulaUtil();
  ServicioCelula servicioCelula = new ServicioCelula();
  ServicioReporteCelula servicioReporteCelula = new ServicioReporteCelula();
}

/**
 * TAREAS:
 * 1 . en futuras versiones: comprobar permiso de edición de reporte
 */
