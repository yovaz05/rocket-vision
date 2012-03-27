package rocket.controladores.celula;

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
import rocket.modelo.servicios.ServicioCelula;
import rocket.modelo.servicios.ServicioRed;
import waytech.modelo.beans.sgi.Red;
import waytech.utilidades.Util;

/**
 * Controlador asociado a vistaCelula/DatosBasicos.zul
 * Se encarga de buscar la data para mostrarla en las listas, validaciones y cosas por el estilo
 * @author Gabriel
 */
public class CtrlCelulaDatosBasicos extends GenericForwardComposer {

  //widgets:
  Div divMensaje;
  Label etqMensaje;
  //- A btnCerrarMensaje;
  Label etqMensajeLideres;
  Textbox txtCodigo, txtNombre;
  A tbbRed;
  A tbbLider1, tbbLider2, tbbLider3, tbbLider4;
  Label etqRed;
  Label etqCodigo, etqLider1, etqLider2, etqLider3, etqLider4, etqNombre;
  Combobox cmbRed, cmbDia, cmbHora;
  Combobox cmbLider1, cmbLider2, cmbLider3, cmbLider4;
  Div opcionLider1, opcionLider2, opcionLider3, opcionLider4;
  Div opcionAgregarLider;
  //data:
  ServicioCelula servicioCelula = new ServicioCelula();
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
  private String codigo = "";
  A btnCancelarEditCodigo;
  A btnEditCodigo;
  Label etqDia, etqHora;
  Label etqSeparadorDiaHora;
  A btnEditDiaHora;
  A btnCancelarEditDiaHora;
  String nombre = "";
  A btnEditNombre;
  A btnCancelarEditNombre;
  A btnEditRed, btnCancelarEditRed;
  A btnEditLideres, btnCancelarEditLideres;
  private int idCelula = 0;
  //TODO: MEJORA CODIGO: pasar estas constantes a clase de constantes
  private static final String LIDER1_VACIO = "Líder 1";
  private static final String LIDER2_VACIO = "Líder 2";
  private static final String LIDER3_VACIO = "Líder 3";
  private static final String LIDER4_VACIO = "Líder 4";
  private A btnQuitarLider1;
  private A btnQuitarLider2;
  private A btnQuitarLider3;
  private A btnQuitarLider4;
  private Include panelCentral;
  Label etqTituloVentana;
  String descripcionCelula;
  //? variable para evitar doble actualización de código
  private boolean codigoProcesado;
  String msjNoHayLideres = "Esta red no tiene líderes lanzados";
  String msjNoMasLideresDisponibles = "No hay más líderes lanzados disponibles";
  private int tipoUsuario;
  private boolean usuarioEsLiderRed;
  private boolean usuarioPuedeEditarLideres;
  private boolean usuarioPuedeEditarCodigo;
  private boolean usuarioPuedeEditarRed;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    //** System.out.println("CtrlCelulaDatosBasicos.inicio");
    cargarRedes();

    /**/
    if (Sesion.modoIngresar()) {
      setVarSesionDefault();
    } else {
      getVarSesionIdRed();
      mostrarOpcionAgregarLider(false);      
      setPermisosEdicion();
      actualizarEstado();
    }
  }

  /**
   * establece permisos de edición, según el usuario
   */
  private void setPermisosEdicion() {
    tipoUsuario = Util.buscarTipoUsuario(this.getClass());
    if (tipoUsuario == UsuarioUtil.ADMINISTRADOR_CELULAS) {
      usuarioPuedeEditarRed = true;
      usuarioPuedeEditarCodigo = true;
      usuarioPuedeEditarLideres = true;
    } else if (tipoUsuario == UsuarioUtil.LIDER_RED) {
      usuarioPuedeEditarRed = true;
      usuarioPuedeEditarCodigo = false;
      usuarioPuedeEditarLideres = true;
    } else {
      usuarioPuedeEditarRed = false;
      usuarioPuedeEditarCodigo = false;
      usuarioPuedeEditarLideres = false;
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

  /**
   * carga lista de redes
   */
  private void cargarRedes() {
    redesNombres = servicioRed.getRedesNombres();
    modelRedes.addAll(redesNombres);
    cmbRed.setModel(modelRedes);
  }

  public void onClick$etqCodigo() {
    if (usuarioPuedeEditarCodigo && Sesion.modoEditable()) {
      codigo = etqCodigo.getValue();
      activarEditCodigo();
    }
  }

  /**
   * activa la edición de código
   */
  private void activarEditCodigo() {
    etqCodigo.setVisible(false);
    txtCodigo.setDisabled(false);
    txtCodigo.setValue(codigo);
    txtCodigo.setVisible(true);
    txtCodigo.setFocus(true);
  }

  /**
   * desactiva la edición de Codigo
   */
  void cancelarEditCodigo() {
    txtCodigo.setVisible(false);
    etqCodigo.setVisible(true);
  }

  public void onBlur$txtCodigo() {
    if (Sesion.modoIngresar()) {
      return;
      //no se obliga que ingrese un valor...
      //esto impediría al usuario hacer otra cosa en el sistema

    }
    //TODO: evaluar si este if hace falta
    //if para evitar proceso doble
    if (!codigoProcesado) {
      procesarCodigo();
    }
    codigoProcesado = false;
  }

  public void onOK$txtCodigo() {
    procesarCodigo();
  }

  //procesamiento de valor de código (nuevo y edición)
  private void procesarCodigo() {
    codigoProcesado = false;
    ocultarMensaje();
    String valor = txtCodigo.getValue();
    //quitar espacios en blanco
    valor = valor.trim();
    //validar códigos en uso
    //modo ingresar:
    if (Sesion.modoIngresar()) {
      if (!codigoIngresado(valor) || codigoEnUso(valor)) {//se ingresó valor vacío o repetido
        //forzar a usuario a tipear algo y que código no esté repetido
        txtCodigo.setVisible(true);
        txtCodigo.setFocus(true);
        return;
      }
      //código ingresado y válido
      codigo = valor.toUpperCase();
      etqCodigo.setValue(codigo);
      codigoProcesado = true;

      notificarEvento("btnGuardar");//crear la célula      

      cancelarEditCodigo();
      //-activarEditRed();
      return;
    } //modo edición
    else if (Sesion.modoEditable()) {
      if (!codigoIngresado(valor)) {//se ingresó valor vacío...
        cancelarEditCodigo();//se deja el valor actual
        return;
      }
      if (valor.equals(codigo)) {//no se cambió el valor
        cancelarEditCodigo();
        codigoProcesado = true;
        return;
      }
      codigo = valor.toUpperCase();
      if (!codigoEnUso(valor)) {
        actualizarCodigo();
        codigoProcesado = true;
        cancelarEditCodigo();
        etqCodigo.setValue(codigo);
        etqTituloVentana.setValue("Célula: " + " " + codigo);
      }
    }
    /*
    String nuevoValor = txtCodigo.getValue();
    //quitar espacios en blanco
    nuevoValor = nuevoValor.trim();
    
    if (nuevoValor.isEmpty()) {
    txtCodigo.setVisible(true);
    txtCodigo.setFocus(true);
    return;
    }
    if (nuevoValor.equals(codigo)) {//no se cambió el valor
    return;
    }
    if (codigoEnUso(nuevoValor)) {
    txtCodigo.setVisible(true);
    txtCodigo.setFocus(true);
    return;
    }
    if (Sesion.modoIngresar()) {
    activarEditRed();
    }    
    codigo = nuevoValor;
    if (Sesion.modoEditable()) {
    actualizarCodigo();
    codigoEditado = true;
    }
    etqCodigo.setValue(codigo);    
    //TODO: MEJORA CODIGO: mover generación de titulo de ventana a clase de utilidad
    //actualizar título de ventana:
    descripcionCelula = codigo;
    tituloVentana.setValue("Célula: " + " " + descripcionCelula);
     */
  }

  boolean codigoIngresado(String codigoCelula) {
    if (codigoCelula.isEmpty()) {
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
  boolean codigoEnUso(String codigoCelula) {
    if (servicioCelula.existeCelula(codigoCelula)) {
      mensaje("Error: El código ya está en uso: " + codigoCelula);
      return true;
    }
    return false;
  }

  /**
   * actualiza el código de la célula en la base de datos
   */
  void actualizarCodigo() {
    getIdCelula(); //TODO: quitar, se debe hacer al cargar la pantall
    servicioCelula.setIdCelula(idCelula);
    if (servicioCelula.actualizarCodigo(codigo)) {
      mensaje("Se actualizó el código");
    } else {
      mensaje("Error actualizando el código");
    }
  }

  public void onClick$etqDia() {
    diaTexto = etqDia.getValue();
    activarEditDia();
  }

  void activarEditDia() {
    etqDia.setVisible(false);
    cmbDia.setValue(diaTexto);
    cmbDia.setVisible(true);
    cmbDia.setFocus(true);
    cmbDia.open();
  }

  void cancelarEditDia() {
    etqDia.setVisible(true);
    cmbDia.setVisible(false);
  }

  /**
   * cuando se pierde el foco, se cancela la edición
   */
  public void onBlur$cmbDia() {
    cancelarEditDia();
  }

  public void onSelect$cmbDia() {
    procesarDia();
  }

  void procesarDia() {
    if (cmbDia.getValue().equals(diaTexto)) { //no se cambió el valor
      return;
    }
    diaTexto = cmbDia.getValue();
    mostrarValorDia();
    diaNumero = Integer.parseInt("" + cmbDia.getSelectedItem().getValue());
    setVarSesionDia();

    //**
    System.out.println("cmbDia.label=" + diaTexto);
    System.out.println("cmbDia.value=" + cmbDia.getSelectedItem().getValue());
    //**

    //actualizar cambio en la bd:
    //TODO: MEJORA-CODIGO: este 'if' redundante
    if (Sesion.modoEditable()) {
      actualizarDia();
    }
    cancelarEditDia();
  }

  void mostrarValorDia() {
    etqDia.setValue(diaTexto);
    etqDia.setVisible(true);
  }

  /**
   * actualiza el día de la célula en la base de datos
   */
  void actualizarDia() {
    getIdCelula(); //TODO: quitar, se debe hacer al cargar la pantall
    servicioCelula.setIdCelula(idCelula);
    if (servicioCelula.actualizarDia(diaNumero)) {
      mensaje("Se actualizó el día");
    } else {
      mensaje("Error actualizando el día");
    }
  }

  public void onClick$etqHora() {
    horaTexto = etqHora.getValue();
    activarEditHora();
  }

  void activarEditHora() {
    etqHora.setVisible(false);
    cmbHora.setValue(horaTexto);
    cmbHora.setVisible(true);
    cmbHora.setFocus(true);
    cmbHora.open();
  }

  void cancelarEditHora() {
    etqHora.setVisible(true);
    cmbHora.setVisible(false);
  }

  /**
   * cuando se pierde el foco, se cancela la edición
   */
  public void onBlur$cmbHora() {
    cancelarEditHora();
  }

  public void onSelect$cmbHora() {
    procesarHora();
    cancelarEditHora();
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

    //TODO: MEJORA-CODIGO: este 'if' redundante
    if (Sesion.modoEditable()) {
      actualizarHora();
    }
    cancelarEditHora();
  }

  void mostrarValorHora() {
    System.out.println("CrtrlCelulaDatosBasicos.horaTexto=" + horaTexto);
    etqHora.setValue(horaTexto);
    etqHora.setVisible(true);
  }

  /**
   * actualiza la hora de la célula en la base de datos
   */
  void actualizarHora() {
    getIdCelula(); //TODO: quitar, se debe hacer al cargar la pantall
    servicioCelula.setIdCelula(idCelula);
    if (servicioCelula.actualizarHora(horaNumero)) {
      mensaje("Se actualizó la hora");
    } else {
      mensaje("Error actualizando la hora");
    }
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
    if (nombre.equals(Constantes.VALOR_EDITAR_NOMBRE_CELULA)) {
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
    System.out.println("CtrlCelulaDB.txtNombre.onOK");
    procesarNombre();
    cancelarEditNombre();
  }

  public void onBlur$txtNombre() {
    System.out.println("CtrlCelulaDB.txtNombre.onBlur");
    procesarNombre();
    cancelarEditNombre();
  }

  private void procesarNombre() {
    ocultarMensaje();
    String nuevoValor = txtNombre.getValue();
    //quitar espacios en blanco
    nuevoValor = nuevoValor.trim();

    if (nuevoValor.equals(nombre)) {//no se cambió el valor
      return;
    }

    nombre = nuevoValor;

    //TODO - MEJORA: chequear si existe una célula con ese mismo nombre, y sugerir al usuario que use otro.
    //No es obligatorio que sea único, pero es preferible
    if (Sesion.modoEditable()) {
      actualizarNombre();
    }
    if (nombre.equals("")) {
      nombre = Constantes.VALOR_EDITAR;
    }
    
    etqNombre.setValue(nombre);
  }

  /**
   * actualiza el nombre de la célula en la base de datos
   */
  void actualizarNombre() {
    getIdCelula(); //TODO: MEJORAR CODIGO
    servicioCelula.setIdCelula(idCelula);
    if (servicioCelula.actualizarNombre(nombre)) {
      mensaje("Se actualizó el nombre");
    } else {
      mensaje("Error actualizando el nombre");
    }
  }

  /**
   * obtiene todos los líderes lanzados de la red seleccionada
   */
  //TODO: MEJORA DE CODIGO. un método que diga sólo si tiene líderes, y otro para traer los datos de los líderes
  private boolean cargarLideresLanzadosRed() {
    /*
    if (idRed == 0) {//REDUNDANTE?
    return false;
    }
     */
    lideresLanzadosNombres = servicioRed.getLideresLanzadosNombres(idRed);
    if (lideresLanzadosNombres.isEmpty()) {//la red elegida no tiene líderes lanzados que puedan ser líderes de células      
      //-mensajeLideres(msjNoHayLideres);
      mostrarLideresLinks(false);
      btnEditLideres.setVisible(false);
      return false;
    }
    ocultarMensajeLideres();
    btnEditLideres.setVisible(true);

    //cargar lista de líderes:
    modelLideresLanzados = new ListModelList();
    modelLideresLanzados.addAll(lideresLanzadosNombres);

    return true;
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
    //** System.out.println("CtrlCelulaDatosBasicos.cmbRed.onSelect");
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
    etqRed.setValue(nombreRed);
    cancelarEditRed();

    /*+ si la red elegida no tiene líderes lanzados se puede evitar crear la célula?
    boolean redTieneLideresLanzados = cargarLideresLanzadosRed(); 
    if (!redTieneLideresLanzados) {
    cancelarEditRed();
    }
     */
    //si está en modo ingresar se crea la célula con el código y la red
    if (Sesion.modoIngresar()) {
      //- System.out.println("CtrlCelulaDatosBasicos.procesarRed.modoIngresar");
      //- notificarEvento("btnGuardar");
      activarEditCodigo();
    } else if (Sesion.modoEditable()) {
      //modo edición
      actualizarRed();
      System.out.println("CtrlCelulaDatosBasicos.procesarRed.redCambiada. eliminando líderes anteriores:");
      /*
      al cambiar el valor de la red, se borran los líderes elegidos anteriormente
      y se activa la edición de los líderes
       */
      eliminarLideresAnteriores();
      limpiarValoresLideres();
      setVarSesionLideres();
      mostrarLideresLinks(false);
      mostrarLideresEdit(false);
      btnEditLideres.setVisible(true);
    }
    /* TODO: MOVER PARA EDITAR LIDERES
    if (cargarLideresLanzadosRed()) {
    ocultarMensajeLideres();
    mostrarOpcionAgregarLider(true);
    } else {
    mensajeLideres(msjNoHayLideres);
    mostrarOpcionAgregarLider(false);
    }
     */
    Sesion.setVariable("celula.nombreRed", nombreRed);
  }

  private boolean procesarValorRed() {
    String nombreRedElegida = cmbRed.getValue();
    if (Sesion.modoEditable()) {
      nombreRed = getVarSesionNombreRed();
      if (nombreRedElegida.equals(nombreRed)) {//se dejó el valor anterior
        return false;
      }
    }
    nombreRed = nombreRedElegida;

    //TODO: actualizar variable de sesion, sólo si se está en modo editable
    Sesion.setVariable("celula.red", nombreRed);

    servicioRed.setNombreRed(nombreRed);
    idRed = servicioRed.getIdRed();
    setVarSesionRed();

    return true;
    //** System.out.println("CtrlCelulaDatosBasicos - Red Seleccionada - nombre: " + nombreRed);
    //**System.out.println("CtrlCelulaDatosBasicos - Red seleccionada - id: " + cmbRed.getSelectedItem().getValue());
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
    getIdCelula();
    if (servicioCelula.actualizarRed(idCelula, idRed)) {
      //-mensaje("Se cambió la célula de red. Y se quitaron los líderes asignados.");
      mensaje("Se cambió la célula a otra red");
    } else {
      mensaje("Error cambiando la célula a otra red");
    }
  }

  void limpiarLideresSeleccionados() {
    cmbLider1.setValue("");
    cmbLider2.setValue("");
    cmbLider3.setValue("");
    cmbLider4.setValue("");
  }

  /**
   * elimina los líderes anteriores de la célula,
   * cuando la célula es cambiada de red
   */
  void eliminarLideresAnteriores() {
    getVarSesionLideres();
    getIdCelula();
    if (seUsaLider(1)) {
      servicioCelula.eliminarLider(idCelula, idLider1);
    }
    if (seUsaLider(2)) {
      servicioCelula.eliminarLider(idCelula, idLider2);
    }
    if (seUsaLider(3)) {
      servicioCelula.eliminarLider(idCelula, idLider3);
    }
    if (seUsaLider(4)) {
      servicioCelula.eliminarLider(idCelula, idLider4);
    }
  }

  /**
   * establece la variables de sesión de los líderes
   * para ser usado por los otros controladores
   */
  //TODO: se usa cuando se cambia la red
  //setear los 4 líderes
  private void setVarSesionLideres() {
    setVarSesionNroLideres();
    setVarSesionLider1();
    setVarSesionLider2();
    setVarSesionLider3();
    setVarSesionLider4();
  }

  /**
   * establece la variables de sesión del líder 1
   * para ser usado por los otros controladores
   */
  public void setVarSesionLider1() {
    Sesion.setVariable("celula.idLider1", idLider1);
  }

  /**
   * establece la variables de sesión del líder 2
   * para ser usado por los otros controladores
   */
  public void setVarSesionLider2() {
    Sesion.setVariable("celula.idLider2", idLider2);
  }

  /**
   * establece la variables de sesión del líder 3
   * para ser usado por los otros controladores
   */
  public void setVarSesionLider3() {
    Sesion.setVariable("celula.idLider3", idLider3);
  }

  /**
   * establece la variables de sesión del líder 4
   * para ser usado por los otros controladores
   */
  public void setVarSesionLider4() {
    Sesion.setVariable("celula.idLider4", idLider4);
  }

  /**
   * establece la variables de sesión de día
   * para ser usado por los otros controladores
   */
  public void setVarSesionDia() {
    Sesion.setVariable("celula.dia.texto", diaTexto);
    Sesion.setVariable("celula.dia.numero", diaNumero);
  }

  /**
   * establece la variables de sesión de hora
   * para ser usado por los otros controladores
   */
  public void setVarSesionHora() {
    Sesion.setVariable("celula.hora.texto", horaTexto);
    Sesion.setVariable("celula.hora.numero", horaNumero);
  }

  /**
   * establece la variables de sesión de número de líderes usados
   * para ser usado por los otros controladores
   */
  private void setVarSesionNroLideres() {
    Sesion.setVariable("celula.nLideres", nLideres);
  }

  private void setVarSesionRed() {
    Sesion.setVariable("celula.idRed", idRed);
  }

  /**
   * guardar variables de sesión por defecto (al abrir la ventana)
   * para ser usado por los otros controladores
   * 
   */
  private void setVarSesionDefault() {
    setVarSesionRed();
    setVarSesionDia();
    setVarSesionDia();
    setVarSesionHora();
    setVarSesionNroLideres();
    setVarSesionLideres();
  }

  public void onClick$btnAgregarLider() {
    if (noHayMasLideresDisponibles()) {
      mensajeLideres(msjNoMasLideresDisponibles);
      mostrarOpcionAgregarLider(false);
      return;
    }
    activarOpcionLider();
  }

  public void onOK$btnAgregarLider() {
    activarOpcionLider();
  }

  public void onClick$opcionAgregarLider() {
    activarOpcionLider();
  }

  public void onOK$opcionAgregarLider() {
    activarOpcionLider();
  }

  //TODO: CODIGO: quitar método
  //No se puede eliminar al primer líder, debe existir al menos uno
  public void onClick$btnQuitarLider1() {
    quitarLider(1);
  }

  public void onClick$btnQuitarLider2() {
    quitarLider(2);
  }

  public void onClick$btnQuitarLider3() {
    quitarLider(3);
  }

  public void onClick$btnQuitarLider4() {
    quitarLider(4);
  }

  public void onOK$btnQuitarLider1() {
    quitarLider(1);
  }

  public void onOK$btnQuitarLider2() {
    quitarLider(2);
  }

  public void onOK$btnQuitarLider3() {
    quitarLider(3);
  }

  public void onOK$btnQuitarLider4() {
    quitarLider(4);
  }

  void quitarLider(int i) {
    //borrar líder de célula en la base de datos
    int idLider = 0;
    String nombreLider = "";
    if (i == 1) {
      idLider = idLider1 = (Integer) Sesion.getVariable("celula.idLider1");
      //-nombreLider = nombreLider1;
    } else if (i == 2) {
      idLider = idLider2 = (Integer) Sesion.getVariable("celula.idLider2");
      //-nombreLider = nombreLider2;
    } else if (i == 3) {
      idLider = idLider3 = (Integer) Sesion.getVariable("celula.idLider3");
      //-nombreLider = nombreLider3;
    } else if (i == 4) {
      idLider = idLider4 = (Integer) Sesion.getVariable("celula.idLider4");
    }
    getIdCelula();
    if (servicioCelula.eliminarLider(idCelula, idLider)) {
      mensaje("Se quitó un líder");
      quitarOpcionLider(i);//ocultar widgets del líder
    } else {
      mensaje("Error quitando líder");
    }
  }

  /**
   * muestra un combo de líder N
   * maneja la visibilidad del botón Agregar
   */
  //TODO: validar, sólo se puede agregar otro líder, si en los combos anteriores hay 1 valor seleccionado
  //TODO: ?MEJORA: se hace nLider++ cuando se elige un líder del combo
  //TODO: !ERROR, cuando no se han hecho cambios en los líderes, la opción agregar no hace nada
  void activarOpcionLider() {
    int nLideresTemp = nLideres + 1;
    if (nLideresTemp == 1) {
      activarOpcionLider(1);
    } else if (nLideresTemp == 2) {
      activarOpcionLider(2);
    } else if (nLideresTemp == 3) {
      activarOpcionLider(3);
    } else if (nLideresTemp == 4) {
      activarOpcionLider(4);
    }
    /*-
    if (this.nLideres == TOPE_LIDERES) {
    opcionAgregarLider.setVisible(false);
    }
     * 
     */
  }

  /**
   * agregar una opción para agregar el líder N
   * @param nLider 
   */
  void activarOpcionLider(int nLider) {
    System.out.println("CtrlCelulaDatosBasicos.añadirOpcionLider" + nLider);
    if (nLider == 1) {
      opcionLider1.setVisible(true);
      activarEditLider1();
    } else if (nLider == 2) {
      opcionLider2.setVisible(true);
      activarEditLider2();
    } else if (nLider == 3) {
      opcionLider3.setVisible(true);
      activarEditLider3();
    } else if (nLider == 4) {
      opcionLider4.setVisible(true);
      activarEditLider4();
    }
  }

  /**
   * quitar el combo de líder N
   * maneja la visibilidad del botón Agregar
   */
  //TODO: borrar la data de los combos
  void quitarOpcionLider(int pos) {
    if (pos == 1) {
      opcionLider1.setVisible(false);
      nombreLider1 = LIDER1_VACIO;
      //- etqLider1.setValue(nombreLider1);
      idLider1 = 0;
    } else if (pos == 2) {
      opcionLider2.setVisible(false);
      nombreLider2 = LIDER2_VACIO;
      //- etqLider2.setValue(nombreLider2);
      idLider2 = 0;
    } else if (pos == 3) {
      opcionLider3.setVisible(false);
      nombreLider3 = LIDER3_VACIO;
      //- etqLider3.setValue(nombreLider3);
      idLider3 = 0;
    } else if (pos == 4) {
      opcionLider4.setVisible(false);
      nombreLider4 = LIDER4_VACIO;
      //- etqLider4.setValue(nombreLider4);
      idLider4 = 0;
    } else {//número no válido
      System.out.println("CtrlCelulaDatosBasicos.quitarOpcionLider: error en parámetro pos=" + pos);
      return;
    }
    nLideres--;
    if (nLideres < TOPE_LIDERES) {
      opcionAgregarLider.setVisible(true);
    }
    setVarSesionNroLideres();
    System.out.println("líderes en uso: " + nLideres);
  }

  /**
   * habilita la opción para agregar líderes adicionales
   */
  void mostrarOpcionAgregarLider(boolean visible) {
    System.out.println("mostrarOpcionAgregarLider=" + visible);
    opcionAgregarLider.setVisible(visible);
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

  //Mejora código: sacar variables de mensajes de aquí
  private void ocultarMensajeLideres() {
    etqMensajeLideres.setVisible(false);
  }

  private void mensajeLideres(String msj) {
    System.out.println(this.getClass() + ".mensajeLideres: " + msj);
    etqMensajeLideres.setValue(msj);
    etqMensajeLideres.setVisible(true);
  }

  /**
   * determina el líder N es usado N: {2,3, 4}
   * @param posLider
   * @return 
   */
  private boolean seUsaLider(int posLider) {
    //**LINEA REDUNDANTE
    //get variable de sesión número de líderes
    //CtrlCelula cambia el valor en modo editar
    nLideres = (Integer) Sesion.getVariable("celula.nLideres");
    System.out.print("CtrlCelulaDatosBasicos.seUsaLider[" + posLider + "]?=");
    if (posLider <= nLideres) {
      //**
      System.out.println("true");
      return true;
    }
    //**
    System.out.println("false");
    return false;
  }

  /**
   * muestra/oculta los links de líderes
   */
  private void mostrarLideresLinks(boolean visible) {
    System.out.print("CtrlCelulaDatosBasicos.mostrarLinkLideres=" + visible + "");
    tbbLider1.setVisible(visible);
    tbbLider2.setVisible(visible);
    tbbLider3.setVisible(visible);
    tbbLider4.setVisible(visible);
  }

  /**
   * muestra/oculta los widgets que permiten la edición de líderes
   */
  private void mostrarLideresEdit(boolean visible) {
    System.out.println("CtrlCelulaDatosBasicos.mostrarLideresEdit=" + visible + "");
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
    //TODO: CODIGO: evaluar si se quita esta línea
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

  void activarEditRed() {
    ocultarMensajeLideres();
    tbbRed.setVisible(false);
    etqRed.setVisible(false);
    btnEditRed.setVisible(false);
    btnEditLideres.setVisible(false);//bloquear edición de líderes
    mostrarOpcionAgregarLider(false);

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
    //btnCancelarEditRed.setVisible(false);
  }

  void activarEditLideres() {
    System.out.println("CtrlCelulaDatosBasicos.EditLideres");
    mostrarLideresLinks(false);
    getDatosLideres();
    pasarDatosLideresEtiquetas();
    mostrarLideresEdit(true);
    cargarLideresLanzadosRed();
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
    //TODO: chequear permiso de edición: celula.red    
    getVarSesionNombreRed();
    activarEditRed();
  }

  //TODO: MEJORAR: si usuario no puede editar red, sólo puede aparecer el link, etqRed no aparecerá
  public void onClick$etqRed() {
    if (usuarioPuedeEditarRed) {
      getVarSesionNombreRed();
      activarEditRed();
    }
  }

  //NO USADO
  public void onClick$btnCancelarEditRed() {
    cancelarEditRed();
  }

  //TODO: chequear permiso de edición: celula.lideres
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
    System.out.println("CtrlCelulaDatosBasicos.getVariablesSesionLideres()");
    nLideres = (Integer) Sesion.getVariable("celula.nLideres");
    if (seUsaLider(1)) {
      idLider1 = (Integer) Sesion.getVariable("celula.idLider1");
    }
    if (seUsaLider(2)) {
      idLider2 = (Integer) Sesion.getVariable("celula.idLider2");
    }
    if (seUsaLider(3)) {
      idLider3 = (Integer) Sesion.getVariable("celula.idLider3");
    }
    if (seUsaLider(4)) {
      idLider4 = (Integer) Sesion.getVariable("celula.idLider4");
    }
  }

  /**
   * recupera variable de sesión 'idCelula'
   */
  //TODO: MEJORA: evaluar forma de obtener este valor si viniera en el URL
  private void getIdCelula() {
    //**System.out.println("CtrlCelulaDatosBasicos.getIdCelula");
    idCelula = (Integer) Sesion.getVariable("idCelula");
  }

  private void getVarSesionIdRed() {
    //** System.out.println("CtrlCelulaDatosBasicos.getIdRed");
    idRed = (Integer) Sesion.getVariable("celula.idRed");
  }

  /**
   * agrega un líder de célula a la base de datos
   * @param idLider
   * @return 
   */
  boolean agregarLiderCelula(int idLider) {
    getIdCelula(); //TODO: quitar, se debe hacer al cargar la pantall
    servicioCelula.setIdCelula(idCelula);
    return servicioCelula.agregarLiderCelula(idLider);
  }

  //TODO: poner este comentario en donde se muestran los botones de edición de líderes
  //chequear permiso de edición: celula.líderes
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
    System.out.println("CtrlCelulaDatosBasicos. Líder 1 seleccionado.nombre: " + nombreLider1);
    System.out.println("CtrlCelulaDatosBasicos. Líder 1 seleccionado.id: " + idLider1);

    if (agregarLiderCelula(idLider1)) {
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
      System.out.println("CtrlCelulaDatosBasicos.error agregando líder 1");
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
  //chequear permiso de edición: celula.líderes
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
    System.out.println("CtrlCelulaDatosBasicos. Líder 2 seleccionado.nombre: " + nombreLider2);
    System.out.println("CtrlCelulaDatosBasicos. Líder 2 seleccionado.id: " + idLider2);

    if (agregarLiderCelula(idLider2)) {
      mensaje("Líder agregado");
      etqLider2.setValue(nombreLider2);
      cancelarEditLider2();
      mostrarOpcionQuitarLider(2, true);
      mostrarOpcionAgregarLider(true);
      deshabilitarLiderEnUso(2);
    } else {
      mensaje("Error agregando líder");
      System.out.println("CtrlCelulaDatosBasicos.error agregando líder 2");
      quitarOpcionLider(2);
      return;
    }
  }

  //TODO: poner este comentario en donde se muestran los botones de edición de líderes
  //chequear permiso de edición: celula.líderes
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
    System.out.println("CtrlCelulaDatosBasicos. Líder 3 seleccionado.nombre: " + nombreLider3);
    System.out.println("CtrlCelulaDatosBasicos. Líder 3 seleccionado.id: " + idLider3);

    if (agregarLiderCelula(idLider3)) {
      mensaje("Líder agregado");
      etqLider3.setValue(nombreLider3);
      cancelarEditLider3();
      mostrarOpcionQuitarLider(3, true);
      mostrarOpcionAgregarLider(true);
      deshabilitarLiderEnUso(3);
    } else {
      mensaje("Error agregando líder");
      System.out.println("CtrlCelulaDatosBasicos.error agregando líder 3");
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
  //chequear permiso de edición: celula.líderes
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
    System.out.println("CtrlCelulaDatosBasicos. Líder 4 seleccionado.nombre: " + nombreLider4);
    System.out.println("CtrlCelulaDatosBasicos. Líder 4 seleccionado.id: " + idLider4);

    if (agregarLiderCelula(idLider4)) {
      mensaje("Líder agregado");
      etqLider4.setValue(nombreLider4);
      cancelarEditLider4();
      mostrarOpcionQuitarLider(4, true);
      mostrarOpcionAgregarLider(false);
    } else {
      mensaje("Error agregando líder");
      System.out.println("CtrlCelulaDatosBasicos.error agregando líder 4");
      quitarOpcionLider(4);
      return;
    }
  }

  private void mostrarOpcionQuitarLider(int i, boolean visible) {
    //TODO: MEJORA-CODIGO: quitar caso i=1: no se puede quitar al líder 1
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

  //TODO: MEJORA CODIGO: pasar a clase util
  private String getVarSesionNombreRed() {
    return "" + Sesion.getVariable("lider.nombreRed");
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
    System.out.println("CtrlCelulaDatosBasicos.quitarLideresUsados. número de líderes disponibles = " + lideresLanzadosNombres.size());
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

  private void actualizarEstado() {
    if (usuarioPuedeEditarRed) {
      btnEditRed.setVisible(true);
    }
    if (usuarioPuedeEditarLideres) {
      btnEditLideres.setVisible(true);
    }
  }
}
/**
 * TODO:
 * >>. actualizar titulo de ventana cuando cambia zona (sector)
 * >>. OJO con: métodos onBlur hacen que la actualización sea doble, sólo deben cancelar la edición, para textbox's
 * >>.
 * >>. OPCIONAL: mostrar como red por defecto, la red a la que pertenece el usuario en curso, en modo 'new'
 */
//GARBAGE
/**
 * TODO: EVALUAR - MEJORA USABILIDAD: mostrar sólo las redes donde hay líderes lanzados registrados
 */
//MEJORA CODIGO: obtener id de la red seleccionada directamente del combobox
  /*
 *     for (Red r : redes) {
int id = r.getIdRed();
String nombre = r.getNombre();
Comboitem item = new Comboitem(nombre);
item.setValue(id);
modelRedes.add(r);
}
cmbRed.setModel(modelRedes);

//DOING: se carga cada lista de líderes disponibles al clickear 'Agregar líder'
/*
cmbLider1.setModel(modelLideresLanzados);
cmbLider2.setModel(modelLideresLanzados);
cmbLider3.setModel(modelLideresLanzados);
cmbLider4.setModel(modelLideresLanzados);
 */
