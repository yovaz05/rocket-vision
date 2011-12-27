package sig.controladores.lider;

import cdo.sgd.controladores.Sesion;
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
 * Controlador asociado a vistaLider/DatosBasicos.zul
 * @author Gabriel
 */
public class CtrlLiderDatosBasicos extends GenericForwardComposer {

  //widgets:
  Div divMensaje;
  Label etqMensaje;
  //- A btnCerrarMensaje;
  Label etqMensajeLideres;
  Textbox txtCedula, txtNombre;
  A tbbRed;
  A tbbLider1, tbbLider2, tbbLider3, tbbLider4;
  Label etqRed;
  Label etqCedula, etqLider1, etqLider2, etqLider3, etqLider4, etqNombre;
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
  int idRed = 0;
  String nombreRed = "";
  String diaTexto = "";
  String horaTexto = "";
  int diaNumero = 1;//valor por defecto: lunes
  int horaNumero = 19;//valor por defecto: 7.00 p.m.
  //controles para agregar/quitar líderes
  int nLideres = 0; //indica el número de líderes en uso actualmente
  Label btnAgregarLider;
  int idLider1 = 0;
  int idLider2 = 0;
  int idLider3 = 0;
  int idLider4 = 0;
  String nombreLider1 = "";
  String nombreLider2 = "";
  String nombreLider3 = "";
  String nombreLider4 = "";
  final int TOPE_LIDERES = 4;
  private String cedula = "";
  A btnCancelarEditCedula;
  A btnEditCedula;
  Label etqDia, etqHora;
  Label etqSeparadorDiaHora;
  A btnEditDiaHora;
  A btnCancelarEditDiaHora;
  String nombre = "";
  A btnEditNombre;
  A btnCancelarEditNombre;
  A btnEditRed, btnCancelarEditRed;
  A btnEditLideres, btnCancelarEditLideres;
  private int idLider = 0;
  //TODO: MEJORA Cedula: pasar estas constantes a clase de constantes
  private static final String LIDER1_VACIO = "Líder 1";
  private static final String LIDER2_VACIO = "Líder 2";
  private static final String LIDER3_VACIO = "Líder 3";
  private static final String LIDER4_VACIO = "Líder 4";
  private A btnQuitarLider1;
  private A btnQuitarLider2;
  private A btnQuitarLider3;
  private A btnQuitarLider4;
  private Include panelCentral;
  Label tituloVentana;
  String descripcionlider;
  //? variable para evitar doble actualización de código
  private boolean CedulaProcesado;
  String msjNoHayLideres = "Esta red no tiene líderes lanzados";
  String msjNoMasLideresDisponibles = "No hay más líderes lanzados disponibles";

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    System.out.println("CtrlliderDatosBasicos.inicio");
    cargarRedes();

    /**/
    if (Sesion.modoIngresar()) {
      setVarSesionDefault();
    } else {
      getVarSesionIdRed();
      //+ cargarLideresLanzadosRed();
      //+ mostrarOpcionAgregarLider(false);
    }
    /**/
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

  /**
   * carga lista de redes
   */
  private void cargarRedes() {
    redesNombres = servicioRed.getRedesNombres();
    modelRedes.addAll(redesNombres);
    cmbRed.setModel(modelRedes);
  }

  /**
   * obtiene todos los líderes lanzados de la red seleccionada
   */
  //todo: MEJORA DE Cedula. un método que diga sólo si tiene líderes, y otro para traer los datos
  private boolean cargarLideresLanzadosRed() {
    if (idRed == 0) {//REDUNDANTE?
      return false;
    }
    lideresLanzadosNombres = servicioRed.getLideresLanzadosNombres(idRed);
    if (lideresLanzadosNombres.isEmpty()) {//la red elegida no tiene líderes lanzados que puedan ser líderes de células      
      mensajeLideres(msjNoHayLideres);
      mostrarLideresLinks(false);
      btnEditLideres.setVisible(false);
      return false;
    }
    //- ocultarMensajeLideres();
    btnEditLideres.setVisible(true);

    //cargar lista de líderes:
    modelLideresLanzados = new ListModelList();
    modelLideresLanzados.addAll(lideresLanzadosNombres);

    return true;
  }

  /**
   * método debug
   * obtiene y muestra todos los valores de los elemeentos de entrada
   * tal como serán usados para trabajar con la base de datos
   */
  public void mostrarTodosValoresIngresados() {
    System.out.println("lider. DATOSBASICOS. VALORES INGRESADOS:");
    System.out.println("Código: " + txtCedula.getValue());
    System.out.println("Nombre: " + txtNombre.getValue());
    System.out.println("Líder 1: nombre=" + cmbLider1.getValue());
    System.out.println("Líder 1: id=" + servicioRed.getIdPersonaRed(cmbLider1.getValue()));
    System.out.println("Día.label=" + cmbDia.getValue());
    System.out.println("Día.value=" + cmbDia.getSelectedItem().getValue());
    System.out.println("Hora.label: " + cmbHora.getValue());
    System.out.println("Hora.value: " + cmbHora.getSelectedItem().getValue());
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
    
    //valor de red cambiado
    btnEditLideres.setVisible(true);

    /*+ si la red elegida no tiene líderes lanzados se puede evitar crear la célula?
    boolean redTieneLideresLanzados = cargarLideresLanzadosRed(); 
    if (!redTieneLideresLanzados) {
    cancelarEditRed();
    }
     */
    //si está en modo ingresar se crea la célula con el código y la red
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
    nombreRed = cmbRed.getValue();
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
    getIdlider();
    if (servicioPersona.actualizarRed(idLider, idRed)) {
      //-mensaje("Se cambió la célula de red. Y se quitaron los líderes asignados.");
      mensaje("Se cambió la célula de red");
    } else {
      mensaje("Error actualizando la célula a otra red");
    }
  }

  private void setVarSesionRed() {
    Sesion.setVariable("lider.idRed", idRed);
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

    //TODO: MEJORA: chequear si existe una célula con ese mismo nombre, y sugerir al usuario que use otro.
    //No es obligatorio que sea único
    if (Sesion.modoEditable()) {
      actualizarNombre();
    }
    etqNombre.setValue(nombre);
  }

  /**
   * actualiza el nombre de la célula en la base de datos
   */
  void actualizarNombre() {
    getIdlider(); //TODO: quitar, se debe hacer al cargar la pantalla
    if (servicioPersona.actualizarNombre(idLider, nombre)) {
      mensaje("Se actualizó el nombre");
    } else {
      mensaje("Error actualizando el nombre");
    }
  }
  
  public void onClick$etqCedula() {
    //TODO: chequear permiso de edición de campo: lider.Cedula
    if (Sesion.modoEditable()) {//sólo se permite modo editable
      cedula = etqCedula.getValue();
      activarEditCedula();
    }
  }

  /**
   * activa la edición de código
   */
  private void activarEditCedula() {
    txtCedula.setValue(cedula);
    txtCedula.setVisible(true);
    txtCedula.setFocus(true);
    etqCedula.setVisible(false);
  }

  /**
   * desactiva la edición de Cedula
   */
  void cancelarEditCedula() {
    txtCedula.setVisible(false);
    etqCedula.setVisible(true);
  }

  public void onBlur$txtCedula() {
    //if para evitar proceso doble
    if (!CedulaProcesado) {
      procesarCedula();
    }
    CedulaProcesado = false;
  }

  public void onOK$txtCedula() {
    procesarCedula();
  }

  //procesamiento de valor de código (nuevo y edición)
  private void procesarCedula() {
    CedulaProcesado = false;
    ocultarMensaje();
    String nuevoCedula = txtCedula.getValue();
    //quitar espacios en blanco
    nuevoCedula = nuevoCedula.trim();
    //validar códigos en uso
    //modo ingresar:
    if (Sesion.modoIngresar()) {
      if (!CedulaIngresada(nuevoCedula) || CedulaEnUso(nuevoCedula)) {//se ingresó valor vacío o repetido
        //forzar a usuario a tipear algo y que código no esté repetido
        txtCedula.setVisible(true);
        txtCedula.setFocus(true);
        return;
      }
      //código ingresado y válido
      cedula = nuevoCedula.toUpperCase();
      etqCedula.setValue(cedula);
      CedulaProcesado = true;
      cancelarEditCedula();
      activarEditRed();
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
        CedulaProcesado = true;
        return;
      }
      cedula = nuevoCedula.toUpperCase();
      if (!CedulaEnUso(nuevoCedula)) {
        actualizarCedula();
        CedulaProcesado = true;
        cancelarEditCedula();
        etqCedula.setValue(cedula);
        tituloVentana.setValue("Célula: " + " " + cedula);
      }
    }
  }

  boolean CedulaIngresada(String cedula) {
    if (cedula.isEmpty()) {
      mensaje("Ingresa el código");
      return false;
    }
    return true;
  }

  /**
   * busca en la base de datos si el código ya está en uso
   * devuelve true o false si existe o no.
   * además muestra mensaje de error cuando aplica.
   * @return 
   */
  boolean CedulaEnUso(String cedula) {
    if (servicioPersona.existeCedula(cedula)) {
      mensaje("Error: Esta cédula ya está registrada: " + cedula);
      return true;
    }
    return false;
  }

  /**
   * actualiza el código de la célula en la base de datos
   */
  void actualizarCedula() {
    getIdlider(); //TODO: quitar, se debe hacer al cargar la pantall
    if (servicioPersona.actualizarCedula(idLider, cedula)) {
      mensaje("Se actualizó el código");
    } else {
      mensaje("Error actualizando el código");
    }
  }

  /**
   * determina el líder N es usado N: {2,3, 4}
   * @param posLider
   * @return 
   */
  /*+
  private boolean seUsaLider(int posLider) {
    //**LINEA REDUNDANTE
    //get variable de sesión número de líderes
    //Ctrllider cambia el valor en modo editar
    nLideres = (Integer) Sesion.getVariable("lider.nLideres");
    System.out.print("CtrlliderDatosBasicos.seUsaLider[" + posLider + "]?=");
    if (posLider <= nLideres) {
      //**
      System.out.println("true");
      return true;
    }
    //**
    System.out.println("false");
    return false;
  }
   */

  /**
   * muestra/oculta los links de líderes
   */
  /*+
  private void mostrarLideresLinks(boolean visible) {
    System.out.print("CtrlliderDatosBasicos.mostrarLinkLideres=" + visible + "");
    tbbLider1.setVisible(visible);
    tbbLider2.setVisible(visible);
    tbbLider3.setVisible(visible);
    tbbLider4.setVisible(visible);
  }
   */

  /**
   * muestra/oculta los widgets que permiten la edición de líderes
   */
  /*+
  private void mostrarLideresEdit(boolean visible) {
    System.out.println("CtrlliderDatosBasicos.mostrarLideresEdit=" + visible + "");
    if (!visible) {
      opcionLider1.setVisible(false);
      opcionLider2.setVisible(false);
      opcionLider3.setVisible(false);
      opcionLider4.setVisible(false);
      etqLider1.setVisible(false);
      etqLider2.setVisible(false);
      etqLider3.setVisible(false);
      etqLider4.setVisible(false);
      btnQuitarLider1.setVisible(false);
      btnQuitarLider2.setVisible(false);
      btnQuitarLider3.setVisible(false);
      btnQuitarLider4.setVisible(false);
      return;
    }
    //TODO: Cedula: evaluar si se quita esta línea
    mostrarOpcionAgregarLider(visible);
    //true:
    if (seUsaLider(1)) {
      opcionLider1.setVisible(true);
      etqLider1.setVisible(true);
      btnQuitarLider1.setVisible(true);
    }
    if (seUsaLider(2)) {
      opcionLider2.setVisible(true);
      etqLider2.setVisible(true);
      btnQuitarLider2.setVisible(true);
    }
    if (seUsaLider(3)) {
      opcionLider3.setVisible(true);
      etqLider3.setVisible(true);
      btnQuitarLider3.setVisible(true);
    }
    if (seUsaLider(4)) {
      opcionLider4.setVisible(true);
      etqLider4.setVisible(true);
      mostrarOpcionAgregarLider(false);
      btnQuitarLider4.setVisible(true);
    }
  }
   */

  void activarEditRed() {
    //-ocultarMensajeLideres();
    tbbRed.setVisible(false);
    etqRed.setVisible(false);
    btnEditRed.setVisible(false);
    btnEditLideres.setVisible(false);//bloquear edición de líderes

    //se habilita el combo de red
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
    //-btnCancelarEditRed.setVisible(false);
    //% btnEditLideres.setVisible(true);//permitir edición de líderes
  }

  void activarEditLideres() {
    System.out.println("CtrlliderDatosBasicos.EditLideres");
    mostrarLideresLinks(false);
    getDatosLideres();
    pasarDatosLideresEtiquetas();
    mostrarLideresEdit(true);
    //-cargarLideresLanzadosRed();
    btnEditLideres.setVisible(false);

  }

  void cancelarEditLideres() {
    mostrarLideresEdit(false);
    mostrarLideresLinks(true);
    //-btnEditLideres.setVisible(true);
    btnEditRed.setVisible(true);
    //DOC: permitir edición de red, red y líderes tienen dependencia, se debe bloquear edición simultánea
  }

  /**
   * limpia las variables relacionadas a líderes
   */
  private void limpiarValoresLideres() {
    nLideres = 0;
    nombreLider1 = "";
    nombreLider2 = "";
    nombreLider3 = "";
    nombreLider4 = "";
    idLider1 = 0;
    idLider2 = 0;
    idLider3 = 0;
    idLider4 = 0;
  }

  private void getDatosLideres() {
    nombreLider1 = tbbLider1.getLabel();
    nombreLider2 = tbbLider2.getLabel();
    nombreLider3 = tbbLider3.getLabel();
    nombreLider4 = tbbLider4.getLabel();
  }

  private void pasarDatosLideresEtiquetas() {
    etqLider1.setValue(nombreLider1);
    etqLider2.setValue(nombreLider2);
    etqLider3.setValue(nombreLider3);
    etqLider4.setValue(nombreLider4);
  }

  private void pasarDatosLideresCombos() {
    cmbLider1.setValue(nombreLider1);
    cmbLider2.setValue(nombreLider2);
    cmbLider3.setValue(nombreLider3);
    cmbLider4.setValue(nombreLider4);
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

  //NO USADO
  public void onClick$btnCancelarEditRed() {
    cancelarEditRed();
  }

  //TODO: chequear permiso de edición: lider.lideres
  public void onClick$btnEditLideres() {
    //se traen variables de sesión, por si es el primer cambio
    getVarSesionLideres();
    getVarSesionIdRed();
    activarEditLideres();
    actualizarLideresEnUso();
  }

  //NO USADO
  public void onClick$btnCancelarEditLideres() {
    cancelarEditLideres();
  }

  /**
   * obtiene los valores de la variable de sesión de los líderes
   */
  private void getVarSesionLideres() {
    System.out.println("CtrlliderDatosBasicos.getVariablesSesionLideres()");
    nLideres = (Integer) Sesion.getVariable("lider.nLideres");
    if (seUsaLider(1)) {
      idLider1 = (Integer) Sesion.getVariable("lider.idLider1");
    }
    if (seUsaLider(2)) {
      idLider2 = (Integer) Sesion.getVariable("lider.idLider2");
    }
    if (seUsaLider(3)) {
      idLider3 = (Integer) Sesion.getVariable("lider.idLider3");
    }
    if (seUsaLider(4)) {
      idLider4 = (Integer) Sesion.getVariable("lider.idLider4");
    }
  }

  private void procesarHora() {
    if (cmbHora.getValue().equals(horaTexto)) { //no se cambió el valor
      return;
    }
    horaTexto = cmbHora.getValue();
    mostrarValorHora();
    cancelarEditHora();
    horaNumero = Integer.parseInt("" + cmbHora.getSelectedItem().getValue());
    setVarSesionHora();

    //**
    System.out.println("cmbHora.label=" + horaTexto);
    System.out.println("cmbHora.value=" + cmbHora.getSelectedItem().getValue());
    //**

    //TODO: MEJORA-Cedula: este 'if' redundante
    if (Sesion.modoEditable()) {
      actualizarHora();
    }
    cancelarEditHora();
  }

  /**
   * actualiza la hora de la célula en la base de datos
   */
  void actualizarHora() {
    getIdlider(); //TODO: quitar, se debe hacer al cargar la pantall
    servicioPersona.setIdlider(idLider);
    if (servicioPersona.actualizarHora(horaNumero)) {
      mensaje("Se actualizó la hora");
    } else {
      mensaje("Error actualizando la hora");
    }
  }

  /**
   * recupera variable de sesión 'idlider'
   */
  //TODO: MEJORA: evaluar forma de obtener este valor si viniera en el URL
  private void getIdlider() {
    System.out.println("CtrlliderDatosBasicos.getIdlider");
    idLider = (Integer) Sesion.getVariable("idlider");
  }

  private void getVarSesionIdRed() {
    System.out.println("CtrlliderDatosBasicos.getIdRed");
    idRed = (Integer) Sesion.getVariable("lider.idRed");
  }

  /**
   * agrega un líder de célula a la base de datos
   * @param idLider
   * @return 
   */
  boolean agregarLiderlider(int idLider) {
    getIdlider(); //TODO: quitar, se debe hacer al cargar la pantall
    servicioPersona.setIdlider(idLider);
    return servicioPersona.agregarLiderlider(idLider);
  }

  //TODO: poner este comentario en donde se muestran los botones de edición de líderes
  //chequear permiso de edición: lider.líderes
  public void onClick$etqLider1() {
    //-activarEditLider1();
  }

  /**
   * maneja la selección del líder 1
   */
  public void onSelect$cmbLider1() {
    procesarLider1();
  }

  /**
   * cuando se pierde el foco, se procesa el valor elegido
   */
  public void onBlur$cmbLider1() {
    cancelarEditLider1();
    /**
     * TODO: 
     * if no eligió nada
     * deshabilitar combo
     * mostrar botón agregar líder
     */
    //-procesarLider1();
  }

  /**
   * habilita combo de líder para selección
   */
  private void activarEditLider1() {
    mostrarOpcionAgregarLider(false);
    etqLider1.setVisible(false);
    nombreLider1 = etqLider1.getValue();
    cmbLider1.setModel(modelLideresLanzados);
    cmbLider1.setVisible(true);
    cmbLider1.setValue(nombreLider1);
    cmbLider1.open();
    cmbLider1.setFocus(true);
    mostrarOpcionQuitarLider(1, false);
  }

  private void cancelarEditLider1() {
    cmbLider1.setVisible(false);
    etqLider1.setVisible(true);
    if (idLider1 != 0) {
      mostrarOpcionQuitarLider(1, true);
    }
    mostrarOpcionAgregarLider(true);
  }

  private void procesarLider1() {
    String nombreLider = cmbLider1.getValue();
    if (nombreLider.equals(nombreLider1)) {//no se cambió el valor
      System.out.println("nombreLider1 NO cambió");
      return;//no se hace nada
    }
    nombreLider1 = nombreLider;
    if (nombreLider1.isEmpty() || nombreLider1.equals(LIDER1_VACIO)) {
      //Validar si eligió algo
      System.out.println("nombreLider1 NO elegido");
      quitarOpcionLider(1);
      return;
    }
    idLider1 = servicioRed.getIdLider(nombreLider1);
    setVarSesionLider1();

    //**
    System.out.println("CtrlliderDatosBasicos. Líder 1 seleccionado.nombre: " + nombreLider1);
    System.out.println("CtrlliderDatosBasicos. Líder 1 seleccionado.id: " + idLider1);

    if (agregarLiderlider(idLider1)) {
      mensaje("Se agregó un líder");
      etqLider1.setValue(nombreLider1);
      cancelarEditLider1();
      mostrarOpcionQuitarLider(1, true);
      nLideres++;
      setVarSesionNroLideres();
      //**
      System.out.println("líderes en uso: " + this.nLideres);
      deshabilitarLiderEnUso(1);
    } else {
      mensaje("Error agregando líder");
      System.out.println("CtrlliderDatosBasicos.error agregando líder 1");
      quitarOpcionLider(1);
      return;
    }
  }

  /**
   * maneja la selección del líder 1
   */
  public void onSelect$cmbLider2() {
    procesarLider2();
  }

  /**
   * cuando se pierde el foco, se procesa el valor elegido
   */
  public void onBlur$cmbLider2() {
    cancelarEditLider2();
    //-procesarLider2();
  }

  //TODO: poner este comentario en donde se muestran los botones de edición de líderes
  //chequear permiso de edición: lider.líderes
  public void onClick$etqLider2() {
    //- activarEditLider2();
  }

  /**
   * habilita combo de líder para selección
   */
  private void activarEditLider2() {
    mostrarOpcionAgregarLider(false);
    cmbLider2.setModel(modelLideresLanzados);
    etqLider2.setVisible(false);
    cmbLider2.setVisible(true);
    cmbLider2.setValue(nombreLider2);
    cmbLider2.open();
    cmbLider2.setFocus(true);
    mostrarOpcionQuitarLider(2, false);
  }

  private void cancelarEditLider2() {
    cmbLider2.setVisible(false);
    etqLider2.setVisible(true);
    if (idLider2 != 0) {
      mostrarOpcionQuitarLider(2, true);
    }
    mostrarOpcionAgregarLider(true);
  }

  private void procesarLider2() {
    nombreLider2 = cmbLider2.getValue();
    if (nombreLider2.isEmpty() || nombreLider2.equals(LIDER2_VACIO)) {
      //Validar si eligió algo
      quitarOpcionLider(2);
      return;
    }
    idLider2 = servicioRed.getIdLider(nombreLider2);
    setVarSesionLider2();

    //**
    System.out.println("CtrlliderDatosBasicos. Líder 2 seleccionado.nombre: " + nombreLider2);
    System.out.println("CtrlliderDatosBasicos. Líder 2 seleccionado.id: " + idLider2);

    if (agregarLiderlider(idLider2)) {
      mensaje("Líder agregado");
      etqLider2.setValue(nombreLider2);
      cancelarEditLider2();
      mostrarOpcionQuitarLider(2, true);
      mostrarOpcionAgregarLider(true);
      deshabilitarLiderEnUso(2);
    } else {
      mensaje("Error agregando líder");
      System.out.println("CtrlliderDatosBasicos.error agregando líder 2");
      quitarOpcionLider(2);
      return;
    }
  }

  //TODO: poner este comentario en donde se muestran los botones de edición de líderes
  //chequear permiso de edición: lider.líderes
  public void onClick$etqLider3() {
    //- activarEditLider3();
  }

  /**
   * maneja la selección del líder 1
   */
  public void onSelect$cmbLider3() {
    procesarLider3();
  }

  /**
   * cuando se pierde el foco, se procesa el valor elegido
   */
  public void onBlur$cmbLider3() {
    cancelarEditLider3();
    //-procesarLider3();
  }

  /**
   * habilita combo de líder para selección
   */
  private void activarEditLider3() {
    mostrarOpcionAgregarLider(false);
    cmbLider3.setModel(modelLideresLanzados);

    etqLider3.setVisible(false);
    cmbLider3.setVisible(true);
    cmbLider3.setValue(nombreLider3);
    cmbLider3.open();
    cmbLider3.setFocus(true);
    mostrarOpcionQuitarLider(3, false);
  }

  private void cancelarEditLider3() {
    cmbLider3.setVisible(false);
    etqLider3.setVisible(true);
    if (idLider3 != 0) {
      mostrarOpcionQuitarLider(3, true);
    }
    mostrarOpcionAgregarLider(true);
  }

  private void procesarLider3() {
    nombreLider3 = cmbLider3.getValue();
    if (nombreLider3.isEmpty() || nombreLider3.equals(LIDER3_VACIO)) {
      //Validar si eligió algo
      quitarOpcionLider(3);
      return;
    }
    idLider3 = servicioRed.getIdLider(nombreLider3);
    setVarSesionLider3();

    //**
    System.out.println("CtrlliderDatosBasicos. Líder 3 seleccionado.nombre: " + nombreLider3);
    System.out.println("CtrlliderDatosBasicos. Líder 3 seleccionado.id: " + idLider3);

    if (agregarLiderlider(idLider3)) {
      mensaje("Líder agregado");
      etqLider3.setValue(nombreLider3);
      cancelarEditLider3();
      mostrarOpcionQuitarLider(3, true);
      mostrarOpcionAgregarLider(true);
      deshabilitarLiderEnUso(3);
    } else {
      mensaje("Error agregando líder");
      System.out.println("CtrlliderDatosBasicos.error agregando líder 3");
      quitarOpcionLider(3);
      return;
    }
  }

  /**
   * maneja la selección del líder 1
   */
  public void onSelect$cmbLider4() {
    procesarLider4();
  }

  /**
   * cuando se pierde el foco, se procesa el valor elegido
   */
  public void onBlur$cmbLider4() {
    cancelarEditLider4();
    //-procesarLider4();
  }

  //TODO: poner este comentario en donde se muestran los botones de edición de líderes
  //chequear permiso de edición: lider.líderes
  public void onClick$etqLider4() {
    //- activarEditLider4();
  }

  /**
   * habilita combo de líder para selección
   */
  private void activarEditLider4() {
    mostrarOpcionAgregarLider(false);
    cmbLider4.setModel(modelLideresLanzados);
    etqLider4.setVisible(false);
    opcionLider4.setVisible(true);//opcionLider incluye el botón para eliminar el líder
    cmbLider4.setVisible(true);
    cmbLider4.setValue(nombreLider4);
    cmbLider4.open();
    cmbLider4.setFocus(true);
  }

  private void cancelarEditLider4() {
    cmbLider4.setVisible(false);
    etqLider4.setVisible(true);
    if (idLider4 != 0) {
      mostrarOpcionQuitarLider(4, true);
    }
  }

  private void procesarLider4() {
    nombreLider4 = cmbLider4.getValue();
    if (nombreLider4.isEmpty() || nombreLider4.equals(LIDER4_VACIO)) {
      //Validar si eligió algo
      quitarOpcionLider(4);
      deshabilitarLiderEnUso(4);
      return;
    }
    idLider4 = servicioRed.getIdLider(nombreLider4);
    setVarSesionLider4();

    //**
    System.out.println("CtrlliderDatosBasicos. Líder 4 seleccionado.nombre: " + nombreLider4);
    System.out.println("CtrlliderDatosBasicos. Líder 4 seleccionado.id: " + idLider4);

    if (agregarLiderlider(idLider4)) {
      mensaje("Líder agregado");
      etqLider4.setValue(nombreLider4);
      cancelarEditLider4();
      mostrarOpcionQuitarLider(4, true);
      mostrarOpcionAgregarLider(false);
    } else {
      mensaje("Error agregando líder");
      System.out.println("CtrlliderDatosBasicos.error agregando líder 4");
      quitarOpcionLider(4);
      return;
    }
  }

  private void mostrarOpcionQuitarLider(int i, boolean visible) {
    //TODO: MEJORA-Cedula: quitar caso i=1: no se puede quitar al líder 1
    if (i == 1) {
      btnQuitarLider1.setVisible(visible);
    } else if (i == 2) {
      btnQuitarLider2.setVisible(visible);
    } else if (i == 3) {
      btnQuitarLider3.setVisible(visible);
    } else if (i == 4) {
      btnQuitarLider4.setVisible(visible);
    }
  }

  private void notificarEvento(String idBoton) {
    Toolbarbutton boton = (Toolbarbutton) panelCentral.getFellow(idBoton);
    //simular un click del boton indicado a la ventana abierta
    //-Events.postEvent(1, "onClick", boton, null);
    Events.sendEvent("onClick", boton, null);
  }

  private void getVarSesionNombreRed() {
    nombreRed = "" + Sesion.getVariable("lider.nombreRed");
  }

  /**
   * deshabilita al líder 'n' de las listas de líderes disponibles 
   * esto para que en las otras listas de líderes no aparezcan los ya usados
   */
  private void deshabilitarLiderEnUso(int n) {
    nLideres = n;
    System.out.println("actualizarLideresEnUso.nLideres=" + nLideres);
    if (n == 1) {
      servicioRed.quitarLiderLista(idLider1);
    } else if (n == 2) {
      servicioRed.quitarLiderLista(idLider2);
    } else if (n == 3) {
      servicioRed.quitarLiderLista(idLider3);
    } else if (n == 4) {
      servicioRed.quitarLiderLista(idLider4);
    }
    //actualizar lista de nombres disponibles
    lideresLanzadosNombres = servicioRed.getLideresLanzadosNombres();
    modelLideresLanzados = new ListModelList();
    modelLideresLanzados.addAll(lideresLanzadosNombres);
    System.out.println("CtrlliderDatosBasicos.quitarLideresUsados. número de líderes disponibles = " + lideresLanzadosNombres.size());
  }

  /**
   * 
   */
  void actualizarLideresEnUso() {
    if (seUsaLider(1)) {
      deshabilitarLiderEnUso(1);
    }
    if (seUsaLider(2)) {
      deshabilitarLiderEnUso(2);
    }
    if (seUsaLider(3)) {
      deshabilitarLiderEnUso(3);
    }
    if (seUsaLider(4)) {
      deshabilitarLiderEnUso(4);
    }
  }

  boolean noHayMasLideresDisponibles() {
    if (nLideres == servicioRed.getNroLideresLanzados()) {
      return true;
    }
    return false;
  }
}
/**
 * TODO:
 * >>. actualizar titulo de ventana cuando cambia el nombre del líder
 * >>. OJO con: métodos onBlur hacen que la actualización sea doble, sólo deben cancelar la edición, para textbox's
 * >>. OPCIONAL: mostrar como red por defecto, la red a la que pertenece el usuario en curso, en modo 'new'
 */
//GARBAGE
/**
 * TODO: EVALUAR - MEJORA USABILIDAD: mostrar sólo las redes donde hay líderes lanzados registrados
 */
//MEJORA Cedula: obtener id de la red seleccionada directamente del combobox
  /*
 *     for (Red r : redes) {
int id = r.getIdRed();
String nombre = r.getNombre();
Comboitem item = new Comboitem(nombre);
item.setValue(id);
modelRedes.add(r);
}
cmbRed.setModel(modelRedes);

