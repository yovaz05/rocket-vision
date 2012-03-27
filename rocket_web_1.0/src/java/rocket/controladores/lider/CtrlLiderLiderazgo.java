package rocket.controladores.lider;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import rocket.controladores.general.Sesion;
import rocket.modelo.bd.util.UsuarioUtil;
import rocket.modelo.servicios.ServicioLider;
import rocket.modelo.servicios.ServicioRed;
import waytech.utilidades.Util;

/**
 *
 * @author Gabriel
 */
public class CtrlLiderLiderazgo extends GenericForwardComposer {

  //widgets:
  Label etqPareja;
  Combobox cmbPareja;
  A btnEditPareja;
  //variables de control:
  private int idPareja = 0;
  private int tipoUsuario;
  private boolean usuarioPuedeEditarPareja;
  private boolean usuarioPuedeEditarLideres;
  private String nombrePareja;
  private int idRed;
  //datos:
  private List lideresLanzadosNombres = new ArrayList();
  private ServicioRed servicioRed;
  private ServicioLider servicioLider;
  private ListModelList modelLideresLanzados;
  //mensajes:
  private Div divMensaje;
  private Label etqMensaje;
  private int idLider = 0;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  private void inicio() {
    if (Sesion.modoEditable()) {
      //+ getVarSesionIdPareja();
      getIdRed();
      setPermisosEdicion();
      actualizarEstado();
    }
  }

  private void getVarSesionIdPareja() {
    idPareja = (Integer) Sesion.getVariable("lider.idPareja");
  }

  private void setPermisosEdicion() {
    tipoUsuario = Util.buscarTipoUsuario(this.getClass());
    if (tipoUsuario == UsuarioUtil.ADMINISTRADOR_CELULAS) {
      usuarioPuedeEditarPareja = true;
      usuarioPuedeEditarLideres = true;
    } else if (tipoUsuario == UsuarioUtil.LIDER_RED) {
      usuarioPuedeEditarPareja = true;
      usuarioPuedeEditarLideres = true;
    } else {
      usuarioPuedeEditarPareja = false;
      usuarioPuedeEditarLideres = false;
    }
  }

  private void actualizarEstado() {
    //+
  }

  public void onClick$btnEditPareja() {
    btnEditPareja.setVisible(false);
    cargarLideresLanzadosRed();
    cmbPareja.setModel(modelLideresLanzados);
    activarEditPareja();
  }

  public void onBlur$cmbPareja() {
    cancelarEditPareja();
  }

  private void activarEditPareja() {
    //+ linkPareja.setVisible(false);
    etqPareja.setVisible(false);

    //+ mostrarOpcionAgregarLider(false);

    cmbPareja.setValue(nombrePareja);
    cmbPareja.setVisible(true);
    cmbPareja.select();
    cmbPareja.open();
    cmbPareja.setFocus(true);
  }

  private void cancelarEditPareja() {
    cmbPareja.setVisible(false);
    etqPareja.setVisible(true);
    btnEditPareja.setVisible(true); //sólo se usa este botón una vez, por ahora, hasta que se active la navegación
  }

  /**
   * obtiene todos los líderes lanzados de la red seleccionada
   */
  //TODO: MEJORA DE CODIGO. un método que diga sólo si tiene líderes, y otro para traer los datos de los líderes
  private boolean cargarLideresLanzadosRed() {
    servicioRed = new ServicioRed();
    lideresLanzadosNombres = servicioRed.getLideresLanzadosNombres(idRed);
    if (lideresLanzadosNombres.isEmpty()) {//la red elegida no tiene líderes lanzados que puedan ser líderes de células      
      //-mensajeLideres(msjNoHayLideres);
      //+ mostrarLideresLinks(false);
      //+ btnEditLideres.setVisible(false);
      return false;
    }
    //+ ocultarMensajeLideres();
    //+ btnEditLideres.setVisible(true);

    //cargar lista de líderes:
    modelLideresLanzados = new ListModelList();
    modelLideresLanzados.addAll(lideresLanzadosNombres);
    return true;
  }

  private void getIdRed() {
    idRed = (Integer) Sesion.getVariable("lider.idRed");
  }

  public void onSelect$cmbPareja() {
    //** System.out.println("CtrlCelulaDatosBasicos.cmbPareja.onSelect");
    procesarPareja();
  }

  /**
   * maneja cambio de pareja
   */
  void procesarPareja() {
    cancelarEditPareja();
    if (!parejaSeleccionada() || !procesarValorPareja()) {
      return;
    }

    //valor cambiado
    etqPareja.setValue(nombrePareja);

    //actualizar en bd
    if (actualizarPareja()) {
      //** System.out.println("CtrlCelulaDatosBasicos.procesarRed.redCambiada. eliminando líderes anteriores:");
      Sesion.setVariable("lider.pareja.nombre", nombrePareja);
    }
  }

  private boolean procesarValorPareja() {
    String nombreParejaElegida = cmbPareja.getValue();
    nombrePareja = getVarSesionNombrePareja();
    if (nombreParejaElegida.equals(nombrePareja)) {//se dejó el valor anterior
      return false;
    }
    nombrePareja = nombreParejaElegida;

    Sesion.setVariable("lider.pareja.nombre", nombrePareja);

    //buscar id de la pareja:    
    idPareja = servicioRed.getIdPersonaRed(nombrePareja);
    setIdPareja();

    return true;
  }

  /**
   * valida que se haya seleccionado una red
   * @return 
   */
  boolean parejaSeleccionada() {
    if (!comboSeleccionado(cmbPareja, "Selecciona la red")) {
      //set foco en widget de red para forzarlo a elegir un valor
      cmbPareja.setVisible(true);
      cmbPareja.setFocus(true);
      return false;
    }
    return true;
  }

  /**
   * actualiza el día de la célula en la base de datos
   * @return true si se realizó la actualización, false en caso contrario
   */
  boolean actualizarPareja() {
    boolean ok = false;
    getIdLider();
    servicioLider = new ServicioLider();
    /* TODO: en proceso */
    ok = servicioLider.actualizarParejaMinisterial(idLider, idPareja);
    if (ok) {
      mensaje("Se cambió la pareja ministerial");
    } else {
      mensaje("Error cambiando la pareja ministerial");
    }
    return ok;
  }

  //TODO: MEJORA CODIGO: pasar a clase util
  private String getVarSesionNombrePareja() {
    return "" + Sesion.getVariable("lider.pareja.nombre");
  }

  /**
   * valida que un combo tenga un valor seleccionado
   */
  //TODO: MEJORACODIGO: sacar este método para una clase de utilería
  private boolean comboSeleccionado(Combobox combo, String msjError) {
    String valor = combo.getValue();
    if ((valor != null) && !valor.equals("")) {
      ocultarMensaje();
      combo.setSclass("");
      return true;
    }
    //error:
    mensaje(msjError);
    combo.setSclass("textbox_error");
    return false;
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

  /**
   * recupera variable de sesión 'idCelula'
   */
  //TODO: MEJORA: evaluar forma de obtener este valor si viniera en el URL
  private void getIdLider() {
    //TODO: aplicar estándar: lider.id
    idLider = (Integer) Sesion.getVariable("idLider");
  }

  private void mensaje(String msj) {
    etqMensaje.setValue(msj);
    etqMensaje.setVisible(true);
    //-btnCerrarMensaje.setVisible(true);
    divMensaje.setVisible(true);
    System.out.println(this.getClass().toString() + msj);
  }

  private void setIdPareja() {
    Sesion.setVariable("lider.pareja.id", idPareja);
  }
}
