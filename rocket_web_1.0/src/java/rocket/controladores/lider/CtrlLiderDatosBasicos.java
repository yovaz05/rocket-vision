package rocket.controladores.lider;

import rocket.controladores.general.Sesion;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import rocket.controladores.general.Constantes;
import rocket.modelo.bd.util.UsuarioUtil;
import rocket.modelo.servicios.ServicioPersona;
import rocket.modelo.servicios.ServicioRed;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.beans.sgi.Red;
import waytech.utilidades.Util;

/**
 *
 * @author Gabriel
 */
public class CtrlLiderDatosBasicos extends GenericForwardComposer {

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    System.out.println("CtrlliderDatosBasicos.inicio");
    cargarRedes();

    if (Sesion.modoIngresar()) {
      setVarSesionDefault();
    } else {
      getIdRed();
      getIdLider();
      setPermisosEdicion();
    }
  }

  /**
   * establece permisos de edición, según el usuario
   */
  private void setPermisosEdicion() {
    tipoUsuario = Util.buscarTipoUsuario(this.getClass());
    esLiderRed = (Boolean) Sesion.esLiderRed();
    if (tipoUsuario == UsuarioUtil.ADMINISTRADOR_CELULAS) {
      System.out.println("CtrlLider.usuario es administrador");
      usuarioPuedeEditarRed = true;
      usuarioPuedeEditarCedula = true;
      usuarioPuedeEditarNombre = true;
    } else {
      System.out.println("usuario no es administrador");
      usuarioPuedeEditarRed = false;
      usuarioPuedeEditarCedula = false;
      usuarioPuedeEditarNombre = false;
    }
  }

  /**
   * carga lista de redes
   */
  private void cargarRedes() {
    redesNombres = servicioRed.getRedesNombres();
    modelRedes.addAll(redesNombres);
    cmbRed.setModel(modelRedes);
  }

  /**
   * cuando se pierde el foco, se procesa el valor elegido
   * y se activa la carga de líderes
   */
  public void onBlur$cmbRed() {
    if (Sesion.modoIngresar()) {
      if (!redSeleccionada()) {//validación
        return;
      }
    }
    //modo editable:
    cancelarEditRed();
  }

  /**
   * maneja la selección del combo red
   */
  public void onSelect$cmbRed() {
    //** System.out.println("CtrlliderDatosBasicos.cmbRed.onSelect");
    procesarRed();
  }

  /**
   * manejo de cambio de red
   */
  void procesarRed() {
    if (!redSeleccionada()) {
      if (Sesion.modoEditable()) {
        cancelarEditRed();
      }
      return;
    }
    if (!procesarValorRed()) {
      cancelarEditRed();
      return;
    }

    /*+ si la red elegida no tiene líderes lanzados se puede evitar crear la célula?
    boolean redTieneLideresLanzados = cargarLideresLanzadosRed(); 
    if (!redTieneLideresLanzados) {
    cancelarEditRed();
    }
     */
    //si está en modo ingresar se crea la célula con la cédula, el nombre y la red
    if (Sesion.modoIngresar()) {
      System.out.println("CtrlliderDatosBasicos.procesarRed.modoIngresar");
      //chequear valores obligatorios: 
      notificarEvento("btnGuardar");
    } else if (Sesion.modoEditable()) {
      //modo edición
      actualizarRed();
      System.out.println("CtrlliderDatosBasicos.procesarRed.redCambiada. eliminando líderes anteriores:");
      /*
      al cambiar el valor de la red, se borran los líderes elegidos anteriormente
      y se activa la edición de los líderes
       */
      /*+
      eliminarLideresAnteriores();
      limpiarValoresLideres();
      setVarSesionLideres();
      mostrarLideresLinks(false);
      mostrarLideresEdit(false);
       */
    }
    /*+
    if (cargarLideresLanzadosRed()) {
    mostrarOpcionAgregarLider(true);
    } else {
    mostrarOpcionAgregarLider(false);
    }
    btnEditLideres.setVisible(false);
     */
    etqRed.setValue(nombreRed);
    cancelarEditRed();
    Sesion.setVariable("lider.nombreRed", nombreRed);
  }

  private boolean procesarValorRed() {
    String nombreRedElegida = cmbRed.getValue();
    if (Sesion.modoEditable()) {
      nombreRed = getVarSesionNombreRed();
      if (nombreRedElegida.equals(nombreRed)) {//no se cambió el valor
        return false;
      }
    }
    nombreRed = nombreRedElegida;

    //TODO: actualizar variable de sesion, sólo si se está en modo editable
    Sesion.setVariable("lider.red", nombreRed);
    servicioRed.setNombreRed(nombreRed);
    idRed = servicioRed.getIdRed();
    setVarSesionRed();
    return true;
    //** System.out.println("CtrlliderDatosBasicos - Red Seleccionada - nombre: " + nombreRed);
    //**System.out.println("CtrlliderDatosBasicos - Red seleccionada - id: " + cmbRed.getSelectedItem().getValue());
  }

  /**
   * valida que se haya seleccionado una red
   * @return 
   */
  boolean redSeleccionada() {
    if (!comboSeleccionado(cmbRed, "Selecciona la red")) {
      //set foco en widget de red para forzarlo a elegir un valor
      cmbRed.setVisible(true);
      cmbRed.setFocus(true);
      return false;
    }
    return true;
  }

  /**
   * actualiza el día de la célula en la base de datos
   */
  void actualizarRed() {
    if (servicioPersona.actualizarRed(getIdLider(), idRed)) {
      //-mensaje("Se cambió la célula de red. Y se quitaron los líderes asignados.");
      mensaje("Se pasó el líder a otra red");
    } else {
      mensaje("Error cambiando la red");
    }
  }

  private void setVarSesionRed() {
    Sesion.setVariable("lider.idRed", idRed);
  }

  private void setVarSesionLider() {
    Sesion.setVariable("idLider", idLider);
  }

  /**
   * guardar variables de sesión por defecto (al abrir la ventana)
   * para ser usado por los otros controladores
   * 
   */
  private void setVarSesionDefault() {
    setVarSesionRed();
  }

  /**
   * valida que un combo tenga un valor seleccionado
   */
  //TODO: MEJORACedula: sacar este método para una clase de utilería
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

  public void onClick$etqNombre() {
    nombre = etqNombre.getValue();
    if (Sesion.modoIngresar()) {
      activarEditNombre();
    }
    if (Sesion.modoEditable() && usuarioPuedeEditarNombre) {
      activarEditNombre();
    }
  }

  /**
   * activa la edición de Nombre
   */
  private void activarEditNombre() {
    etqNombre.setVisible(false);
    //-nombre = etqNombre.getValue();
    if (nombre.equals(Constantes.VALOR_EDITAR)) {
      nombre = "";
    }
    txtNombre.setValue(nombre);
    txtNombre.setVisible(true);
    txtNombre.setDisabled(false);
    txtNombre.setFocus(true);
  }

  /**
   * desactiva la edición de Nombre
   * y muestra el valor actual
   */
  private void cancelarEditNombre() {
    txtNombre.setVisible(false);
    etqNombre.setVisible(true);
    //-etqNombre.setFocus(true);
  }

  public void onBlur$txtNombre() {
    if (Sesion.modoIngresar() && !nombreIngresado(txtNombre.getValue())) {
      nombre = Constantes.VALOR_EDITAR;
      mostrarNombre();
      cancelarEditNombre();
      return;  //no se obliga que ingrese un valor... impide al usuario hacer otra cosa en el sistema
    }
    if (Sesion.modoEditable()) {
      //TODO: MEJORA: evitar actualizaciones duplicadas del nombre
      procesarNombre();
      cancelarEditNombre();
    }
  }

  //TODO: MEJORA: evitar actualizaciones duplicadas del nombre
  public void onOK$txtNombre() {
    procesarNombre();
    cancelarEditNombre();
  }

  private void procesarNombre() {
    ocultarMensaje();
    String nuevoValor = txtNombre.getValue();
    //quitar espacios en blanco
    nuevoValor = nuevoValor.trim();

    if (Sesion.modoIngresar() && !nombreIngresado(nuevoValor)) {//en modo ingresar dejó campo en blanco
      //forzar a usuario a tipear algo
      mensaje("Ingresa el nombre");
      txtNombre.setVisible(true);
      txtNombre.setFocus(true);
      return;
    }

    if (nuevoValor.isEmpty() || nuevoValor.equals(nombre)) {//no se cambió el valor
      return;
    }

    nombre = nuevoValor;

    //TODO: MEJORA: chequear si existe una persona con ese mismo nombre, y sugerir al usuario que agregue el segundo nombre o algo así
    if (Sesion.modoEditable()) {
      actualizarNombre();
      tituloVentana.setValue("Líder: " + nombre);
    }
    mostrarNombre();
    if (Sesion.modoIngresar()) {
      mensaje("Selecciona la red");
      activarEditRed();
    }
  }

  /**
   * devuelve true si se ingresó una cédula
   * false en caso contrario
   * @param valor
   * @return 
   */
  boolean nombreIngresado(String valor) {
    return !valor.isEmpty();
  }

  void mostrarCedula() {
    etqCedula.setValue(cedula);
  }

  void mostrarNombre() {
    etqNombre.setValue(nombre);
  }

  void mostrarRed() {
    etqRed.setValue(nombreRed);
  }

  /**
   * actualiza el nombre de la célula en la base de datos
   */
  void actualizarNombre() {
    if (servicioPersona.actualizarNombre(getIdLider(), nombre)) {
      mensaje("Se actualizó el nombre");
    } else {
      mensaje("Error actualizando el nombre");
    }
  }

  public void onClick$etqCedula() {
    if (Sesion.modoIngresar()) {
      activarEditCedula();
    }
    if (Sesion.modoEditable() && usuarioPuedeEditarCedula) {
      activarEditCedula();
    }
  }

  /**
   * activa la edición de la cédula
   */
  private void activarEditCedula() {
    cedula = etqCedula.getValue();
    etqCedula.setVisible(false);
    txtCedula.setValue(cedula);
    txtCedula.setVisible(true);
    txtCedula.setFocus(true);
  }

  /**
   * desactiva la edición de Cedula
   */
  void cancelarEditCedula() {
    txtCedula.setVisible(false);
    etqCedula.setVisible(true);
  }

  public void onBlur$txtCedula() {
    if (Sesion.modoIngresar()) {
      return;
      //no se obliga que ingrese un valor...
      //esto impediría al usuario hacer otra cosa en el sistema
    }
    //TODO: evaluar si este if hace falta
    //if para evitar actualización doble
    if (!cedulaProcesada) {
      procesarCedula();
    }
    cedulaProcesada = false;
  }

  public void onOK$txtCedula() {
    procesarCedula();
  }

  //procesamiento de valor de código (nuevo y edición)
  private void procesarCedula() {
    cedulaProcesada = false;
    ocultarMensaje();
    String valor = txtCedula.getValue();
    //quitar espacios en blanco
    valor = valor.trim();
    //modo ingresar:
    if (Sesion.modoIngresar()) {
      //validar cédula en uso
      if (!cedulaIngresada(valor) || cedulaEnUso(valor)) {//se ingresó valor vacío o repetido
        //forzar a usuario a tipear algo y que código no esté repetido
        txtCedula.setVisible(true);
        txtCedula.setFocus(true);
        return;
      }
      //código ingresado y válido
      cedula = valor.toUpperCase();
      mostrarCedula();
      cedulaProcesada = true;
      cancelarEditCedula();
      activarEditNombre();
      mensaje("Ingresa el nombre y presiona 'Enter'");
      return;
    }
    //modo edición
    if (Sesion.modoEditable()) {
      if (!cedulaIngresada(valor)) {//se ingresó valor vacío...
        cancelarEditCedula();//se deja el valor actual
        return;
      }
      if (valor.equals(cedula)) {//no se cambió el valor
        cancelarEditCedula();
        cedulaProcesada = true;
        return;
      }
      cedula = valor.toUpperCase();
      if (!cedulaEnUso(valor)) {
        actualizarCedula();
        cedulaProcesada = true;
        cancelarEditCedula();
        etqCedula.setValue(cedula);
      }
    }
  }

  /**
   * devuelve true si se ingresó una cédula
   * false en caso contrario
   * @param cedula
   * @return 
   */
  boolean cedulaIngresada(String cedula) {
    if (cedula.isEmpty()) {
      if (Sesion.modoIngresar()) {
        mensaje("Ingresa la cédula"); //obligar al usuario a tipear algo
      }
      return false;
    }
    return true;
  }

  /**
   * busca en la base de datos si la cédula ya está en uso
   * devuelve true o false si existe o no.
   * además muestra mensaje de error cuando aplica.
   * @return 
   */
  boolean cedulaEnUso(String cedula) {
    if (servicioPersona.existeCedula(cedula)) {
      mensaje("Cédula ya está registrada");
      //mostrar datos de líder, con la cédula ingresada:
      Persona p = servicioPersona.getPersona();
      mostrarDatosPersonaRegistrada(p);
      txtCedula.select();
      return true;
    } else {
      //limpiar valores, por si fueron usados en búsqueda anteriormente
      nombre = "";
      nombreRed = "";
      mostrarNombre();
      mostrarRed();
    }
    return false;
  }

  /**
   * hay 2 opciones:
   * A: usar los widgets de entrada -menos trabajo
   * (B): usar los widgets de view -más trabajo, xq hay q jugar con el valor ingresado
   **/
  //DOING
  private void mostrarDatosPersonaRegistrada(Persona p) {
    //txtNombre.setValue(p.getNombre());
    //cmbRed.setValue(p.getRed().getNombre());
    nombre = p.getNombre();
    nombreRed = p.getRed().getNombre();
    System.out.println("CtrlLiderDB. nombre = " + nombre);
    System.out.println("CtrlLiderDB. red = " + nombreRed);
    mostrarNombre();
    mostrarRed();
    cancelarEditNombre();
    cancelarEditRed();
  }

  /**
   * actualiza el código de la célula en la base de datos
   */
  void actualizarCedula() {
    if (servicioPersona.actualizarCedula(getIdLider(), cedula)) {
      mensaje("Se actualizó la cédula");
    } else {
      mensaje("Error actualizando la cédula");
    }
  }

  void activarEditRed() {
    //-ocultarMensajeLideres();
    tbbRed.setVisible(false);
    etqRed.setVisible(false);
    btnEditRed.setVisible(false);
    //+ btnEditLideres.setVisible(false);//bloquear edición de líderes

    //se habilita el widget
    cmbRed.setDisabled(false);
    cmbRed.setValue(nombreRed);
    cmbRed.setVisible(true);
    cmbRed.select();
    cmbRed.open();
    cmbRed.setFocus(true);
  }

  void cancelarEditRed() {
    cmbRed.setVisible(false);
    etqRed.setVisible(true);
    //- btnEditRed.setVisible(true); //sólo se usa este botón una vez
    //% btnEditLideres.setVisible(true);//permitir edición de líderes
  }

  public void onClick$btnEditRed() {
    //TODO: chequear permiso de edición: lider.red    
    getVarSesionNombreRed();
    activarEditRed();
  }

  public void onClick$etqRed() {
    //TODO: CODIGO: evaluar esta línea:
    getVarSesionNombreRed();
    if (Sesion.modoIngresar()) {
      activarEditRed();
    }
    if (Sesion.modoEditable() && usuarioPuedeEditarRed) {
      activarEditRed();
    }
  }

  public void onClick$etqTelefono() {
    activarEditTelefono();
  }

  /**
   * activa la edición de Nombre
   */
  private void activarEditTelefono() {
    etqTelefono.setVisible(false);
    telefono = etqTelefono.getValue();
    if (telefono.equals(Constantes.VALOR_EDITAR)) {
      telefono = "";
    }
    txtTelefono.setValue(telefono);
    txtTelefono.setVisible(true);
    txtTelefono.setFocus(true);
  }

  /**
   * desactiva la edición de Nombre
   * y muestra el valor actual
   */
  private void cancelarEditTelefono() {
    txtTelefono.setVisible(false);
    etqTelefono.setVisible(true);
    etqTelefono.setFocus(true);
  }

  public void onOK$txtTelefono() {
    procesarTelefono();
    cancelarEditTelefono();
  }

  public void onBlur$txtTelefono() {
    procesarTelefono();
    cancelarEditTelefono();
  }

  private void procesarTelefono() {
    ocultarMensaje();
    String nuevoValor = txtTelefono.getValue();
    //quitar espacios en blanco
    nuevoValor = nuevoValor.trim();

    if (nuevoValor.equals(telefono)) {//no se cambió el valor
      return;
    }

    telefono = nuevoValor;

    //TODO: MEJORA: chequear si existe una persona con ese mismo teléfono, y sugerir al usuario que ya está en uso
    if (Sesion.modoEditable()) {
      actualizarTelefono();
    }
    //si se dejó el valor en blanco, se usa etiqueta que permita edición posterior
    if (telefono.isEmpty()) {
      telefono = Constantes.VALOR_EDITAR;
    }
    etqTelefono.setValue(telefono);
  }

  /**
   * actualiza el telefono de la célula en la base de datos
   */
  void actualizarTelefono() {
    if (servicioPersona.actualizarTelefono(getIdLider(), telefono)) {
      mensaje("Se actualizó el teléfono");
    } else {
      mensaje("Error actualizando el teléfono");
    }
  }

  public void onClick$etqEmail() {
    activarEditEmail();
  }

  /**
   * activa la edición de Nombre
   */
  private void activarEditEmail() {
    etqEmail.setVisible(false);
    email = etqEmail.getValue();
    if (email.equals(Constantes.VALOR_EDITAR)) {
      email = "";
    }
    txtEmail.setValue(email);
    txtEmail.setVisible(true);
    txtEmail.setFocus(true);
  }

  /**
   * desactiva la edición de Nombre
   * y muestra el valor actual
   */
  private void cancelarEditEmail() {
    txtEmail.setVisible(false);
    etqEmail.setVisible(true);
    etqEmail.setFocus(true);
  }

  public void onOK$txtEmail() {
    procesarEmail();
    cancelarEditEmail();
  }

  public void onBlur$txtEmail() {
    procesarEmail();
    cancelarEditEmail();
  }

  private void procesarEmail() {
    ocultarMensaje();
    String nuevoValor = txtEmail.getValue();
    //quitar espacios en blanco
    nuevoValor = nuevoValor.trim();

    if (nuevoValor.equals(email)) {//no se cambió el valor
      return;
    }

    email = nuevoValor;

    //TODO: MEJORA: chequear si existe una persona con ese mismo teléfono, y sugerir al usuario que ya está en uso
    if (Sesion.modoEditable()) {
      actualizarEmail();
    }
    //si se dejó el valor en blanco, se usa etiqueta que permita edición posterior
    if (email.isEmpty()) {
      email = Constantes.VALOR_EDITAR;
    }
    etqEmail.setValue(email);
  }

  /**
   * actualiza el Email de la célula en la base de datos
   */
  void actualizarEmail() {
    if (servicioPersona.actualizarCorreo(getIdLider(), email)) {
      mensaje("Se actualizó la dirección de correo");
    } else {
      mensaje("Error actualizando dirección de correo");
    }
  }

  //TODO: MEJORA CODIGO: pasar a clase util
  private String getVarSesionNombreRed() {
    return "" + Sesion.getVariable("lider.nombreRed");
  }

  //TODO: MEJORA CODIGO: pasar a clase util
  private int getIdLider() {
    System.out.println("CtrlliderDatosBasicos.getIdLider");
    idLider = (Integer) Sesion.getVariable("idLider");
    return idLider;
  }

  //TODO: MEJORA CODIGO: pasar a clase util
  private int getIdRed() {
    System.out.println("CtrlliderDatosBasicos.getIdRed");
    idRed = (Integer) Sesion.getVariable("lider.idRed");
    return idRed;
  }

  private void notificarEvento(String idBoton) {
    Toolbarbutton boton = (Toolbarbutton) panelCentral.getFellow(idBoton);
    //simular un click del boton indicado a la ventana abierta
    //-Events.postEvent(1, "onClick", boton, null);
    Events.sendEvent("onClick", boton, null);
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
    etqMensaje.setValue("");//TODO: Cedula: línea parece redundante
    etqMensaje.setVisible(false);
    //-btnCerrarMensaje.setVisible(false);
    divMensaje.setVisible(false);
  }
  //widgets:
  Div divMensaje;
  Label etqMensaje;
  //- A btnCerrarMensaje;
  Label etqMensajeLideres;
  Textbox txtCedula, txtNombre, txtTelefono, txtEmail;
  A tbbRed;
  A tbbLider1, tbbLider2, tbbLider3, tbbLider4;
  Label etqRed;
  Label etqCedula, etqLider1, etqLider2, etqLider3, etqLider4, etqNombre, etqTelefono, etqEmail;
  Combobox cmbRed, cmbDia, cmbHora;
  Combobox cmbLider1, cmbLider2, cmbLider3, cmbLider4;
  Div opcionLider1, opcionLider2, opcionLider3, opcionLider4;
  Div opcionAgregarLider;
  //data:
  ServicioPersona servicioPersona = new ServicioPersona();
  ServicioRed servicioRed = new ServicioRed();
  List redesNombres = new ArrayList();
  //nombres de líderes lanzados disponibles para ser líderes de célula
  List lideresLanzadosNombres = new ArrayList();
  Red redSelecionada;
  ListModelList modelRedes = new ListModelList();
  ListModelList modelLideresLanzados = new ListModelList();
  private int idLider = 0;
  int idRed = 0;
  String nombreRed = "";
  private String cedula = "";
  A btnEditCedula;
  String nombre = "";
  String telefono = "";
  String email = "";
  A btnEditNombre;
  A btnEditRed;
  //referencias:
  private Include panelCentral;
  Label tituloVentana;
  String descripcionlider;
  //? variable para evitar doble actualización de cédula
  private boolean cedulaProcesada;
  private int tipoUsuario;
  private boolean esLiderRed;
  private boolean usuarioPuedeEditarNombre;
  private boolean usuarioPuedeEditarCedula;
  private boolean usuarioPuedeEditarRed;
}
