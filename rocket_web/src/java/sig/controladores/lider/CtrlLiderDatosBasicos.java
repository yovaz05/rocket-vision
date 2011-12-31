package sig.controladores.lider;

import sig.controladores.Sesion;
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
import sig.controladores.Constantes;
import sig.modelo.servicios.ServicioPersona;
import sig.modelo.servicios.ServicioRed;
import waytech.modelo.beans.sgi.Red;

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
    //TODO: si pierde el foco, desactiva la edición
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
    getVarSesionNombreRed();
    String nombreRedElegida = cmbRed.getValue();
    if (nombreRedElegida.equals(nombreRed)) {//no se cambió el valor
      return false;
    }
    nombreRed = nombreRedElegida;
    //TODO: MEJORA. no graba si no está en modo editable
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
    activarEditNombre();
  }

  /**
   * activa la edición de Nombre
   */
  private void activarEditNombre() {
    etqNombre.setVisible(false);
    nombre = etqNombre.getValue();
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
    etqNombre.setFocus(true);
  }

  public void onOK$txtNombre() {
    procesarNombre();
    cancelarEditNombre();
  }

  public void onBlur$txtNombre() {
    procesarNombre();
    cancelarEditNombre();
  }

  private void procesarNombre() {
    ocultarMensaje();
    String nuevoValor = txtNombre.getValue();
    //quitar espacios en blanco
    nuevoValor = nuevoValor.trim();

    if (nuevoValor.isEmpty() || nuevoValor.equals(nombre)) {//no se cambió el valor
      return;
    }

    nombre = nuevoValor;

    //TODO: MEJORA: chequear si existe una persona con ese mismo nombre, y sugerir al usuario que agregue el segundo nombre o algo así
    if (Sesion.modoEditable()) {
      actualizarNombre();
      tituloVentana.setValue("Líder: " + nombre);
    }
    etqNombre.setValue(nombre);
    if (Sesion.modoIngresar()) {
      activarEditRed();
    }
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
    //TODO: chequear permiso de edición de campo: lider.Cedula
    if (Sesion.modoEditable()) {
      cedula = etqCedula.getValue();
      activarEditCedula();
    }
  }

  /**
   * activa la edición de la cédula
   */
  private void activarEditCedula() {
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
    String nuevoCedula = txtCedula.getValue();
    //quitar espacios en blanco
    nuevoCedula = nuevoCedula.trim();
    //validar códigos en uso
    //modo ingresar:
    if (Sesion.modoIngresar()) {
      if (!CedulaIngresada(nuevoCedula) || cedulaEnUso(nuevoCedula)) {//se ingresó valor vacío o repetido
        //forzar a usuario a tipear algo y que código no esté repetido
        txtCedula.setVisible(true);
        txtCedula.setFocus(true);
        return;
      }
      //código ingresado y válido
      cedula = nuevoCedula.toUpperCase();
      etqCedula.setValue(cedula);
      cedulaProcesada = true;
      cancelarEditCedula();
      activarEditNombre();
      return;
    }
    //modo edición
    if (Sesion.modoEditable()) {
      if (!CedulaIngresada(nuevoCedula)) {//se ingresó valor vacío...
        cancelarEditCedula();//se deja el valor actual
        return;
      }
      if (nuevoCedula.equals(cedula)) {//no se cambió el valor
        cancelarEditCedula();
        cedulaProcesada = true;
        return;
      }
      cedula = nuevoCedula.toUpperCase();
      if (!cedulaEnUso(nuevoCedula)) {
        actualizarCedula();
        cedulaProcesada = true;
        cancelarEditCedula();
        etqCedula.setValue(cedula);
      }
    }
  }

  boolean CedulaIngresada(String cedula) {
    if (cedula.isEmpty()) {
      if (Sesion.modoIngresar()) {
        mensaje("Ingresa la cédula");
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
    //DOING
    if (servicioPersona.existeCedula(cedula)) {
      mensaje("Esta cédula ya está registrada." + cedula);
      return true;
    }
    return false;
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
    getVarSesionNombreRed();
    activarEditRed();
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

    if (nuevoValor.isEmpty() || nuevoValor.equals(email)) {//no se cambió el valor
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
  private void getVarSesionNombreRed() {
    nombreRed = "" + Sesion.getVariable("lider.nombreRed");
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
}
