package rocket.controladores.acceso;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.Textbox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import rocket.controladores.general.Sesion;
import rocket.modelo.bd.util.UsuarioUtil;
import rocket.modelo.servicios.ServicioAcceso;

/**
 * @since Viernes 15/12/2011 02:35 PM.
 * @version 1.0 Viernes 15/12/2011 02:35 PM.
 * @author Gerardo José Montilla Virgüez, Gabriel Pérez. Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software Rocket 
 */
public class CtrlAcceso extends GenericForwardComposer {

  //widgets:  
  Textbox txtLogin;
  Textbox txtPassword;
  Button btnIngresar;
  Label etqErrorLogin;
  Label etqErrorPassword;
  Checkbox checkRecordarSesion;
  //datos:
  //TODO: CODIGO: no usar la clase BD:
  UsuarioUtil usuario;
  int tipoUsuario = 0;
  String menu = "";
  //constantes:
  String ERROR_USUARIO = "Ingresa el usuario";
  String ERROR_MATCH = "Los datos de acceso son incorrectos";
  String INSTRUCCION_USUARIO = "Ingresa el usuario";
  String INSTRUCCION_CONTRASEÑA = "Ingresa tu contraseña";
  //referencias:
  Include panelCentral;
  Include vistaCentral;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
    //inicio2();
  }

  public void inicio() {
    try {
      boolean sesion = sesionActiva();
      if (sesion) {
        Executions.sendRedirect("index2.zul");
      }
    } catch (Exception e) {
      System.out.println("ERROR: Inicio - redirecionando a formulario de acceso");
    }
    limpiarEntradas();
    limpiarMensajesError();
    txtLogin.setFocus(true);
  }

  /**
   * usado para efectos de desarrollo
   */
  public void inicio2() {
    Executions.sendRedirect("index2.zul");
  }

  /**
   * devuelve false si el usuario no está logueado
   * o true si ya está logueado
   * @return 
   */
  private boolean sesionActiva() {
    boolean usuarioLogueado = (Boolean) Sessions.getCurrent().getAttribute("usuarioLogueado");
    System.out.println("usuarioLogueado: " + usuarioLogueado);
    return usuarioLogueado;
  }

  /**
   * Valida la información ingresada por el usuario para ingresar al sistema.
   * @throws InterruptedException 
   */
  public void ingresarAlSistema(String login, String password) throws InterruptedException {
    if (camposLlenos()) {
      limpiarMensajesError();
      if (!validarAcceso(login, password)) {
        mostrarMensajeError(2, ERROR_MATCH);
        txtPassword.setValue("");
        txtPassword.focus();
      } else if (usuario.getId() == 0) {
        System.out.println("ERROR - CtrlAcceso idUsuario = 0");
        return;
      } else {
        //- menu = buscarVistaMenu();
        menu = "MenuAll.zul";
        Sesion.setVariable("menu", menu);
        //TODO: CODIGO: mover estas líneas a método setVarSesionUsuario
        int idUsuario = usuario.getId();
        String nombre = usuario.getNombre();
        int idRed = usuario.getIdRed();
        tipoUsuario = usuario.getTipo();
        String cedula = usuario.getCedula();
        Sesion.setVariable("usuarioLogueado", true);
        Sesion.setVariable("idUsuario", idUsuario);
        Sesion.setVariable("nombreUsuario", nombre);
        Sesion.setVariable("cedulaUsuario", cedula);
        Sesion.setVariable("usuario.login", login);
        //Debería llamarse idRedUsuario
        Sesion.setVariable("idRed", idRed);
        //**
        System.out.println("CtrlAcceso.login: tipoUsuario = " + tipoUsuario);
        setVarSesionUsuario();
        //redirección a página principal del usuario:
        Executions.sendRedirect("index2.zul");
      }
    }
  }

  /**
   * establece variables de Sesión para el usuario
   * de acuerdo a sus roles en el sistema
   * @return 
   */
  public void setVarSesionUsuario() {
    Sesion.setVariable("tipoUsuario", tipoUsuario);
    Sesion.setVariable("usuarioEsLiderRed", usuario.isLiderRed());
    Sesion.setVariable("usuarioEsLiderCelula", usuario.isLiderCelula());
  }

  /**
   * Comprueba los datos de acceso, login y password con los valores de la base de datos
   * NOTA: el login es convertido a minúscula
   * @param login
   * @param password
   * @return si es usuario válido
   */
  public boolean validarAcceso(String login, String password) {
    usuario = ServicioAcceso.buscarUsuario(login, password);
    if (usuario == null) {
      return false;
    } else if (usuario.getId() == 0) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * obtiene la vista de menú correspondiente al usuario
   * @param login
   * @param password
   * @return el url de la vista correspondiente al tipo de usuario
   */
  //TODO: CODIGO: NO USADO, quitar
  public String buscarVistaMenu() {
    String vista = "";
    if (usuario.getTipo() == UsuarioUtil.LIDER_RED) {
      vista = "LiderRed.zul";
    } else if (usuario.getTipo() == UsuarioUtil.LIDER_CELULA) {
      vista = "LiderCelula.zul";
    } else if (usuario.getTipo() == UsuarioUtil.ADMINISTRADOR_CELULAS) {
      vista = "AdminCelulas.zul";
    }
    return vista;
  }

  public boolean camposLlenos() throws InterruptedException {
    limpiarMensajesError();
    if (txtLogin.getText().isEmpty()) {
      mostrarMensajeError(1, INSTRUCCION_USUARIO);
      return false;
    } else if (txtPassword.getText().isEmpty()) {
      mostrarMensajeError(2, INSTRUCCION_CONTRASEÑA);
      return false;
    }
    return true;
  }

  public void validarLogin() throws InterruptedException {
    limpiarMensajesError();
    if (!txtPassword.getText().isEmpty() && !txtLogin.getText().isEmpty()) {
      ingresarAlSistema(txtLogin.getText(), txtPassword.getText());
    } else if (txtLogin.getText().isEmpty()) {
      mostrarMensajeError(1, INSTRUCCION_USUARIO);
    } else if (txtPassword.getText().isEmpty()) {
      mostrarMensajeError(2, INSTRUCCION_CONTRASEÑA);
    }
  }

  public void validadPassword() throws InterruptedException {
    limpiarMensajesError();
    if (!txtPassword.getText().isEmpty() && !txtLogin.getText().isEmpty()) {
      ingresarAlSistema(txtLogin.getText(), txtPassword.getText());
    } else if (txtPassword.getText().isEmpty()) {
      mostrarMensajeError(2, INSTRUCCION_USUARIO);
    } else if (txtLogin.getText().equals("")) {
      mostrarMensajeError(1, INSTRUCCION_CONTRASEÑA);
    }
  }

  public void onOK$txtLogin() throws InterruptedException {
    validarLogin();
  }

  public void onOK$txtPassword() throws InterruptedException {

    validadPassword();
  }

  public void onClick$btnAcceso(Event event) throws InterruptedException {
    ingresarAlSistema(txtLogin.getText(), txtPassword.getText());
  }

  void limpiarEntradas() {
    //TODO: colocar valores en blanco
    txtLogin.setValue("");
    txtPassword.setValue("");
    checkRecordarSesion.setChecked(false);
  }

  void limpiarMensajesError() {
    etqErrorLogin.setValue("");
    etqErrorPassword.setValue("");
    etqErrorLogin.setVisible(false);
    etqErrorPassword.setVisible(false);
  }

  void mostrarMensajeError(int widgetInput, String msj) {
    if (widgetInput == 1) {
      etqErrorLogin.setValue(msj);
      etqErrorLogin.setVisible(true);
      txtLogin.setFocus(true);
    } else if (widgetInput == 2) {
      etqErrorPassword.setValue(msj);
      etqErrorPassword.setVisible(true);
      txtPassword.setFocus(true);
    }
  }
}
