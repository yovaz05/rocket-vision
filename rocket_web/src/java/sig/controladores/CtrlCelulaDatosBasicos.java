package sig.controladores;

import cdo.sgd.controladores.Sesion;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import sig.modelo.servicios.ServicioCelula;
import sig.modelo.servicios.ServicioRed;
import waytech.modelo.beans.sgi.Red;

/**
 * Controlador asociado a vistaCelula/DatosBasicos.zul
 * Se encarga de buscar la data para mostrarla en las listas, validaciones y cosas por el estilo
 * @author Gabriel
 */
public class CtrlCelulaDatosBasicos extends GenericForwardComposer {

  //widgets:
  Hbox divMensaje;
  Label etqMensaje;
  A btnCerrarMensaje;
  Label etqMensajeNoHayLideres;
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
  private A btnQuitarLider2;
  private A btnQuitarLider3;
  private A btnQuitarLider4;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    System.out.println("CtrlCelulaDatosBasicos.inicio");
    cargarRedes();

    /**/
    if (Sesion.modoIngresar()) {
      setVarSesionDefault();
    }
    cargarLideresLanzados();
    /**/
  }

  /**
   * muestra un mensaje en un label, para interacción con el usuario
   * @param msj 
   */
  private void mostrarMensaje(String msj) {
    etqMensaje.setValue(msj);
    etqMensaje.setVisible(true);
    btnCerrarMensaje.setVisible(true);
    divMensaje.setVisible(true);
    System.out.println(this.getClass().toString() + msj);
  }

  /** 
   * limpia el mensaje de estado
   */
  private void ocultarMensaje() {
    etqMensaje.setValue("");//TODO: CODIGO: línea parece redundante
    etqMensaje.setVisible(false);
    btnCerrarMensaje.setVisible(false);
    divMensaje.setVisible(false);
  }

  /**
   * carga lista de redes
   */
  private void cargarRedes() {
    redesNombres = servicioRed.getRedesNombres();
    modelRedes.addAll(redesNombres);
    cmbRed.setModel(modelRedes);
    /**
     * TODO: EVALUAR - MEJORA USABILIDAD: mostrar sólo las redes donde hay líderes lanzados registrados
     */
  }

  //TODO: MEJORA CODIGO: obtener id de la red seleccionada directamente del combobox
  /*
   *     for (Red r : redes) {
  int id = r.getIdRed();
  String nombre = r.getNombre();
  Comboitem item = new Comboitem(nombre);
  item.setValue(id);
  modelRedes.add(r);
  }
  cmbRed.setModel(modelRedes);
  
   */
  /**
   * obtiene todos los líderes lanzados de la red seleccionada
   */
  //TODO: quitar de la lista2, 3 y 4 al líder elegido en las listas anteriores
  private void cargarLideresLanzados() {
    if (idRed == 0) {
      return;
    }
    lideresLanzadosNombres = servicioRed.getLideresLanzadosNombres(idRed);
    if (lideresLanzadosNombres.isEmpty()) {
      mostrarMensajeNoHayLideres(true);
      mostrarLideresEdit(false);
      mostrarOpcionAgregarLider(false);
      //-btnEditLideres.setVisible(false);
      //-btnCancelarEditLideres.setVisible(false);
      return;
    } else {
      mostrarMensajeNoHayLideres(false);
      //- mostrarLideresEdit(true);
      //- mostrarOpcionAgregarLider(true);
      modelLideresLanzados = new ListModelList();
      modelLideresLanzados.addAll(lideresLanzadosNombres);
      cmbLider1.setModel(modelLideresLanzados);

      //TODO: quitar, se carga la lista de líderes disponibles al clickear 'Agregar líder'
      cmbLider2.setModel(modelLideresLanzados);
      cmbLider3.setModel(modelLideresLanzados);
      cmbLider4.setModel(modelLideresLanzados);
    }
  }

  /**
   * método debug
   * obtiene y muestra todos los valores de los elemeentos de entrada
   * tal como serán usados para trabajar con la base de datos
   */
  public void mostrarTodosValoresIngresados() {
    System.out.println("CELULA. DATOSBASICOS. VALORES INGRESADOS:");
    System.out.println("Código: " + txtCodigo.getValue());
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
  //TODO: terminar
  public void onBlur$cmbRed() {
    //TODO: si pierde el foco, se verifica si eligió algo, si no eligió algo, se coloca: 'Elegir...' y se desactiva la edición
    /*
    if (cmbRed.getValue().isEmpty() || cmbRed.getValue() == nombreRed) {
    return;//no se eligió ningún valor o es el anterior
    }
    procesarRed();
     */
  }

  /**
   * maneja la selección del combo red
   */
  public void onSelect$cmbRed() {
    //** System.out.println("CtrlCelulaDatosBasicos.cmbRed.onSelect");
    if (!redSeleccionada()) {
      return;
    }
    procesarRed();
  }

  /**
   * 
   */
  void procesarRed() {
    procesarValorRed();
    cancelarEditRed();
    /*
    //DOING: al cambiar el valor de la red, se borran los líderes elegidos anteriormente y se activa la edición de los líderes
     *
     */
    limpiarValoresLideres();
    setVarSesionLideres();
    activarEditLideres();
    /**
     * 
     * TODO: 2da fase
     * 1. mostrar como red por defecto, la red a la que pertenece el usuario en curso, en modo 'new'
     **/
  }

  void limpiarLideresSeleccionados() {
    cmbLider1.setValue("");
    cmbLider2.setValue("");
    cmbLider3.setValue("");
    cmbLider4.setValue("");
  }

  public void onSelect$cmbDia() {
    procesarDia();
  }

  void procesarDia() {
    if (cmbDia.getValue() == diaTexto) { //no se cambió el valor
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

  public void onSelect$cmbHora() {
    procesarHora();
    cancelarEditHora();
  }

  /**
   * actualiza el día de la célula en la base de datos
   */
  void actualizarDia() {
    getIdCelula(); //TODO: quitar, se debe hacer al cargar la pantall
    servicioCelula.setIdCelula(idCelula);
    if (servicioCelula.actualizarDia(diaNumero)) {
      mostrarMensaje("Se cambió el día");
    } else {
      mostrarMensaje("Error cambiando el día");
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
    Sesion.setVariable("celula.lider1.id", idLider1);
  }

  /**
   * establece la variables de sesión del líder 2
   * para ser usado por los otros controladores
   */
  public void setVarSesionLider2() {
    Sesion.setVariable("celula.lider2.id", idLider2);
  }

  /**
   * establece la variables de sesión del líder 3
   * para ser usado por los otros controladores
   */
  public void setVarSesionLider3() {
    Sesion.setVariable("celula.lider3.id", idLider3);
  }

  /**
   * establece la variables de sesión del líder 4
   * para ser usado por los otros controladores
   */
  public void setVarSesionLider4() {
    Sesion.setVariable("celula.lider4.id", idLider4);
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
    agregarOpcionLider();
  }

  public void onOK$btnAgregarLider() {
    agregarOpcionLider();
  }

  public void onClick$opcionAgregarLider() {
    agregarOpcionLider();
  }

  public void onOK$opcionAgregarLider() {
    agregarOpcionLider();
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
      //-nombreLider = nombreLider4;
    }
    getIdCelula();
    servicioCelula.setIdCelula(idCelula);
    if (servicioCelula.deleteLider(idLider)) {
      mostrarMensaje("Se quitó un líder de célula");
      //-mostrarMensaje("Se quitó como líder de esta célula a " + nombreLider);
      quitarOpcionLider(i);//ocultar widgets
    } else {
      mostrarMensaje("Error quitando líder de célula");
    }
  }

  /**
   * muestra un combo de líder N
   * maneja la visibilidad del botón Agregar
   */
  //TODO: validar, sólo se puede agregar otro líder, si en los combos anteriores hay 1 valor seleccionado
  //TODO: ?MEJORA: se hace nLider++ cuando se elige un líder del combo
  //TODO: !ERROR, cuando no se han hecho cambios en los líderes, la opción agregar no hace nada
  void agregarOpcionLider() {
    int nLideresTemp = this.nLideres + 1;
    if (nLideresTemp == 1) {
      agregarOpcionLider(1);
      nLideres++;
    } else if (nLideresTemp == 2) {
      agregarOpcionLider(2);
      nLideres++;
    } else if (nLideresTemp == 3) {
      agregarOpcionLider(3);
      nLideres++;
    } else if (nLideresTemp == 4) {
      agregarOpcionLider(4);
      nLideres++;
    }
    if (this.nLideres == TOPE_LIDERES) {
      opcionAgregarLider.setVisible(false);
    }
    //**
    setVarSesionNroLideres();
    System.out.println("líderes en uso: " + this.nLideres);
  }

  /**
   * agregar una opción para agregar el líder N
   * @param nLider 
   */
  void agregarOpcionLider(int nLider) {
    System.out.println("CtrlCelulaDatosBasicos.añadirOpcionLider" + nLider);
    if (nLider == 1) {
      opcionLider1.setVisible(true);
      activarEditLider1();
      mostrarOpcionAgregarLider(false);
    } else if (nLider == 2) {
      opcionLider2.setVisible(true);
      activarEditLider2();
      mostrarOpcionAgregarLider(false);
    } else if (nLider == 3) {
      opcionLider3.setVisible(true);
      activarEditLider3();
      mostrarOpcionAgregarLider(false);
    } else if (nLider == 4) {
      opcionLider4.setVisible(true);
      activarEditLider4();
      mostrarOpcionAgregarLider(false);
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
    opcionAgregarLider.setVisible(visible);
    //? btnAgregarLider.setVisible(visible);
  }

  /**
   * devuelve true si se elegió una red de la lista,
   * false en caso contrario
   * @return 
   */
  /*NO USADO
  private boolean redSeleccionada() {
  return !nombreRed.isEmpty();
  }
   * 
   */
  private void procesarValorRed() {
    if (cmbRed.getValue().equals(nombreRed)) {//no se cambió el valor
      return;
    }
    nombreRed = cmbRed.getValue();
    //TODO: MEJORA. no graba si no está en modo editable
    Sesion.setVariable("celula.red", nombreRed);
    servicioRed.setNombreRed(nombreRed);
    idRed = servicioRed.getIdRed();
    setVarSesionRed();
    cargarLideresLanzados();
    //** System.out.println("CtrlCelulaDatosBasicos - Red Seleccionada - nombre: " + nombreRed);
    //**System.out.println("CtrlCelulaDatosBasicos - Red seleccionada - id: " + cmbRed.getSelectedItem().getValue());
  }

  /**
   * valida que se haya seleccionado una red
   * @return 
   */
  boolean redSeleccionada() {
    if (!comboSeleccionado(cmbRed, "Selecciona la red")) {
      //set foco en tabPanel y widget correspondiente
      cmbRed.setFocus(true);
      return false;
    }
    return true;
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
    mostrarMensaje(msjError);
    combo.setSclass("textbox_error");
    return false;
  }

  private void mostrarMensajeNoHayLideres(boolean visible) {
    String msjError = "La red elegida no tiene líderes registrados";
    String msjOK = "La red elegida sí tiene líderes registrados";
    etqMensajeNoHayLideres.setValue(msjError);
    etqMensajeNoHayLideres.setVisible(visible);
    if (visible) {
      System.out.println(this.getClass().toString() + msjError);
    }//**
    else {
      System.out.println(this.getClass().toString() + msjOK);
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
    if (txtNombre.getValue().equals(nombre)) {//no se cambió el valor
      return;
    }
    nombre = txtNombre.getValue();
    ocultarMensaje();
    //TODO: Mejora, chequear si existe una célula con ese mismo nombre, y sugerir al usuario que use otro.
    //No es obligatorio que sea único
    //TODO: MEJORA-CODIGO: este 'if' redundante
    if (Sesion.modoEditable()) {
      actualizarNombre();
    }
    etqNombre.setValue(nombre);
  }

  /**
   * actualiza el nombre de la célula en la base de datos
   */
  void actualizarNombre() {
    getIdCelula(); //TODO: quitar, se debe hacer al cargar la pantall
    servicioCelula.setIdCelula(idCelula);
    if (servicioCelula.actualizarNombre(nombre)) {
      mostrarMensaje("Se cambió el nombre");
    } else {
      mostrarMensaje("Error cambiando el nombre");
    }
  }

  /*
  void procesarNombre() {
  nombre = txtNombre.getValue();
  //obligar a usuario a ingresar algo
  if (nombre.isEmpty()) {
  txtNombre.setFocus(true);
  return;
  }
  //**
  System.out.println("celula.Nombre=" + codigo);
  //tomar valor del textbox, darselo a la etiqueta, y ocultar el txtCodigo
  tomarValorNombre();
  }
  
  private void tomarValorNombre() {
  txtNombre.setVisible(false);
  etqNombre.setValue(nombre);
  etqNombre.setVisible(true);
  cancelarEditNombre();
  }
  
  void cancelarEditNombre() {
  if (Sesion.modoEditable()) {
  btnCancelarEditNombre.setVisible(false);
  btnEditNombre.setVisible(true);
  }
  }
   */
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

  void mostrarValorDia() {
    etqDia.setValue(diaTexto);
    etqDia.setVisible(true);
  }

  void mostrarValorHora() {
    System.out.println("CrtrlCelulaDatosBasicos.horaTexto=" + horaTexto);
    etqHora.setValue(horaTexto);
    etqHora.setVisible(true);
  }

  /**
   * cuando se pierde el foco, se cancela la edición
   */
  public void onBlur$cmbDia() {
    procesarDia();
  }

  /**
   * cuando se pierde el foco, se cancela la edición
   */
  public void onBlur$cmbHora() {
    procesarHora();
  }

  public void onClick$etqCodigo() {
    //TODO: chequear permiso de edición de campo: celula.codigo
    codigo = etqCodigo.getValue();
    activarEditCodigo();
  }

  /**
   * activa la edición de código
   */
  private void activarEditCodigo() {
    txtCodigo.setValue(codigo);
    txtCodigo.setVisible(true);
    txtCodigo.setFocus(true);
    etqCodigo.setVisible(false);
  }

  /**
   * desactiva la edición de Codigo
   */
  void cancelarEditCodigo() {
    txtCodigo.setVisible(false);
    etqCodigo.setVisible(true);
  }

  //procesamiento de txtCódigo en modo edición
  public void onOK$txtCodigo() {
    if (Sesion.modoIngresar()) {
      cmbRed.setFocus(true);
    }
    if (procesarCodigo()) {
      cancelarEditCodigo();
    }
  }

  //procesamiento de txtCódigo en modo edición
  public void onBlur$txtCodigo() {
    if (procesarCodigo()) {
      cancelarEditCodigo();
    }
  }

  private boolean procesarCodigo() {
    codigo = txtCodigo.getValue();
    if (codigo.isEmpty()) {
      //+ validaciones, activar más adelante cuando funcione lo básico
      /*
      txtCodigo.setFocus(true);
      txtCodigo.setVisible(true);
      mostrarMensaje("Ingresa el código");
       */
      return false;
    }
    if (!codigoValido()) {
      return false;
    }
    ocultarMensaje();
    etqCodigo.setValue(codigo);
    //TODO: MEJORA-CODIGO: este 'if' redundante
    if (Sesion.modoEditable()) {
      actualizarCodigo();
    }
    return true;
  }

  /**
   * validaciones del código
   * @return 
   */
  boolean codigoValido() {
    if (celulaExiste(codigo)) {
      mostrarMensaje("Este código ya está en uso");
      return false;
    }
    return true;
  }

  /**
   * actualiza el código de la célula en la base de datos
   */
  void actualizarCodigo() {
    getIdCelula(); //TODO: quitar, se debe hacer al cargar la pantall
    servicioCelula.setIdCelula(idCelula);
    if (servicioCelula.actualizarCodigo(codigo)) {
      mostrarMensaje("Se cambió el código");
    } else {
      mostrarMensaje("Error cambiando el código");
    }
  }

  /**
   * determina el líder N es usado N: {2,3, 4}
   * @param posLider
   * @return 
   */
  private boolean seUsaLider(int posLider) {
    //**
    //get variabl de sesión número de líderes
    //en modo editar CtrlCelula cambia el valor
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
  private void mostrarLinksLideres(boolean visible) {
    System.out.print("CtrlCelulaDatosBasicos.mostrarLinkLideres=" + visible + "");
    tbbLider1.setVisible(visible);
    tbbLider2.setVisible(visible);
    tbbLider3.setVisible(visible);
    tbbLider4.setVisible(visible);
  }

  /**
   * muestra/oculta las etiquetas de líderes que permiten la edición
   */
  private void mostrarLideresEdit(boolean visible) {
    System.out.print("CtrlCelulaDatosBasicos.mostrarLideresEdit=" + visible + "");
    if (visible = false) {
      opcionLider1.setVisible(false);
      opcionLider2.setVisible(false);
      opcionLider3.setVisible(false);
      opcionLider4.setVisible(false);
      etqLider1.setVisible(false);
      etqLider2.setVisible(false);
      etqLider3.setVisible(false);
      etqLider4.setVisible(false);
      btnQuitarLider2.setVisible(false);
      btnQuitarLider2.setVisible(false);
      btnQuitarLider3.setVisible(false);
      btnQuitarLider4.setVisible(false);
      mostrarOpcionAgregarLider(false);
      return;
    }
    //true
    mostrarOpcionAgregarLider(true);
    if (seUsaLider(1)) {
      opcionLider1.setVisible(true);
      etqLider1.setVisible(true);
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
    cmbRed.setValue(tbbRed.getLabel());
    cmbRed.setVisible(true);
    cmbRed.setFocus(true);
    cmbRed.select();
    cmbRed.open();
    tbbRed.setVisible(false);
    etqRed.setVisible(false);
    //- btnCancelarEditRed.setVisible(true);
    btnEditRed.setVisible(false);
    btnEditLideres.setVisible(false);//bloquear edición de líderes
  }

  void cancelarEditRed() {
    cmbRed.setVisible(false);
    etqRed.setVisible(true);
    btnEditRed.setVisible(true);
    //-btnCancelarEditRed.setVisible(false);
    //- btnEditLideres.setVisible(true);//permitir edición de líderes
  }

  //DOING: cambiando, da error de valor nulo
  void activarEditLideres() {
    System.out.println("CtrlCelulaDatosBasicos.EditLideres");
    getDatosLideres();
    pasarDatosLideresEtiquetas();
    mostrarLinksLideres(false);
    mostrarLideresEdit(true);
    btnEditLideres.setVisible(false);
  }

  void cancelarEditLideres() {
    mostrarLideresEdit(false);
    mostrarLinksLideres(true);
    btnEditLideres.setVisible(true);
    btnEditRed.setVisible(true);
    //DOC: permitir edición de red, red y líderes tienen dependencia, se debe bloquear edición simultánea
  }

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

  /*
  //NO USADO
  private void mostrarLideresEdit2(boolean visible) {
  System.out.println("CtrlCelulaDatosBasicos.mostrarLideresEdit=" + visible);
  //? divLideresEdit.setVisible(status);
  //DOING: mostrar etiquetas o combos?
  if (seUsaLider(1)) {
  etqLider1.setVisible(visible);
  opcionLider1.setVisible(visible);
  }
  if (seUsaLider(2)) {
  opcionLider2.setVisible(visible);
  etqLider2.setVisible(visible);
  }
  if (seUsaLider(3)) {
  etqLider3.setVisible(visible);
  opcionLider3.setVisible(visible);
  }
  if (seUsaLider(4)) {
  etqLider4.setVisible(visible);
  opcionLider4.setVisible(visible);
  mostrarOpcionAgregarLider(false);
  }
  }
   */
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
    activarEditRed();
  }

  public void onClick$etqRed() {
    activarEditRed();
  }

  public void onClick$btnCancelarEditRed() {
    cancelarEditRed();
  }

  //TODO: chequear permiso de edición: celula.lideres
  public void onClick$btnEditLideres() {
    //se traen variables de sesión, pues este controlador no ha gestionado los cambios
    if (Sesion.modoEditable()) {
      getVarSesionLideres();
    }
    activarEditLideres();
  }

  public void onClick$btnCancelarEditLideres() {
    cancelarEditLideres();
  }

  private void setIdRed(int id) {
    idRed = id;
  }

  /**
   * obtiene los valores de la variable de sesión
   */
  private void getVariablesSesionGenerales() {
    System.out.println("CtrlCelulaDatosBasicos.getVariablesSesion()");
    getIdCelula();
    getIdRed();
    //+ nombreRed = "" + Sesion.getVariable("celula.nombreRed");
  }

  /**
   * obtiene los valores de la variable de sesión de los líderes
   */
  private void getVarSesionLideres() {
    System.out.println("CtrlCelulaDatosBasicos.getVariablesSesionLideres()");
    nLideres = (Integer) Sesion.getVariable("celula.nLideres");
    idLider1 = (Integer) Sesion.getVariable("celula.idLider1");
    idLider2 = (Integer) Sesion.getVariable("celula.idLider2");
    idLider3 = (Integer) Sesion.getVariable("celula.idLider3");
    idLider4 = (Integer) Sesion.getVariable("celula.idLider4");
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

  /**
   * actualiza la hora de la célula en la base de datos
   */
  void actualizarHora() {
    getIdCelula(); //TODO: quitar, se debe hacer al cargar la pantall
    servicioCelula.setIdCelula(idCelula);
    if (servicioCelula.actualizarHora(horaNumero)) {
      mostrarMensaje("Se cambió la hora");
    } else {
      mostrarMensaje("Error cambiando la hora");
    }
  }

  //TODO:buscar en la base de datos si el código ya está en uso
  private boolean celulaExiste(String codigo) {
    return false;
  }

  /**
   * recupera variable de sesión 'idCelula'
   */
  //TODO: MEJORA: evaluar forma de obtener este valor si viniera en el URL
  private void getIdCelula() {
    try {
      System.out.println("CtrlCelulaDatosBasicos.getIdCelula");
      idCelula = (Integer) Sesion.getVariable("idCelula");
    } catch (Exception e) {
      idCelula = 0;
      System.out.println("CtrlCelula -> ERROR recuperando variable de sesión 'idCelula': "
              + e);
    }
  }

  private void getIdRed() {
    try {
      System.out.println("CtrlCelulaDatosBasicos.getIdRed");
      idRed = (Integer) Sesion.getVariable("celula.idRed");
    } catch (Exception e) {
      idRed = 0;
      System.out.println("CtrlCelula -> ERROR recuperando variable de sesión 'idRed': "
              + e);
    }
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
    activarEditLider1();
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
    etqLider1.setVisible(false);

    cmbLider1.setVisible(true);
    cmbLider1.setValue(nombreLider1);
    cmbLider1.open();
    cmbLider1.setFocus(true);

    //-btnQuitarLider1.setVisible(false);
  }

  private void cancelarEditLider1() {
    cmbLider1.setVisible(false);
    etqLider1.setVisible(true);
  }

  private void procesarLider1() {
    nombreLider1 = cmbLider1.getValue();
    if (nombreLider1.isEmpty() || nombreLider1.equals(LIDER1_VACIO)) {
      //Validar si eligió algo
      quitarOpcionLider(1);
      return;
    }
    idLider1 = servicioRed.getIdLider(nombreLider1);
    setVarSesionLider1();

    //**
    System.out.println("CtrlCelulaDatosBasicos. Líder 1 seleccionado.nombre: " + nombreLider1);
    System.out.println("CtrlCelulaDatosBasicos. Líder 1 seleccionado.id: " + idLider1);

    if (agregarLiderCelula(idLider1)) {
      mostrarMensaje("Líder agregado");
      etqLider1.setValue(nombreLider1);
      cancelarEditLider1();
      mostrarOpcionQuitarLider(1, true);
      mostrarOpcionAgregarLider(true);
    } else {
      mostrarMensaje("Error agregando líder");
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
    //-procesarLider2();
  }

  //TODO: poner este comentario en donde se muestran los botones de edición de líderes
  //chequear permiso de edición: celula.líderes
  public void onClick$etqLider2() {
    activarEditLider2();
  }

  /**
   * habilita combo de líder para selección
   */
  private void activarEditLider2() {
    etqLider2.setVisible(false);
    cmbLider2.setVisible(true);
    cmbLider2.setValue(nombreLider2);
    cmbLider2.open();
    cmbLider2.setFocus(true);
    btnQuitarLider2.setVisible(false);
  }

  private void cancelarEditLider2() {
    cmbLider2.setVisible(false);
    etqLider2.setVisible(true);
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
      mostrarMensaje("Líder agregado");
      etqLider2.setValue(nombreLider2);
      cancelarEditLider2();
      mostrarOpcionQuitarLider(2, true);
      mostrarOpcionAgregarLider(true);
    } else {
      mostrarMensaje("Error agregando líder");
      System.out.println("CtrlCelulaDatosBasicos.error agregando líder 2");
      quitarOpcionLider(2);
      return;
    }
  }

  //TODO: poner este comentario en donde se muestran los botones de edición de líderes
  //chequear permiso de edición: celula.líderes
  public void onClick$etqLider3() {
    activarEditLider3();
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
    //-procesarLider3();
  }

  /**
   * habilita combo de líder para selección
   */
  private void activarEditLider3() {
    etqLider3.setVisible(false);
    cmbLider3.setVisible(true);
    cmbLider3.setValue(nombreLider3);
    cmbLider3.open();
    cmbLider3.setFocus(true);
    btnQuitarLider3.setVisible(false);
  }

  private void cancelarEditLider3() {
    cmbLider3.setVisible(false);
    etqLider3.setVisible(true);
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
      mostrarMensaje("Líder agregado");
      etqLider3.setValue(nombreLider3);
      cancelarEditLider3();
      mostrarOpcionQuitarLider(3, true);
      mostrarOpcionAgregarLider(true);
    } else {
      mostrarMensaje("Error agregando líder");
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
    //-procesarLider4();
  }

  //TODO: poner este comentario en donde se muestran los botones de edición de líderes
  //chequear permiso de edición: celula.líderes
  public void onClick$etqLider4() {
    activarEditLider4();
  }

  /**
   * habilita combo de líder para selección
   */
  private void activarEditLider4() {
    etqLider4.setVisible(false);
    opcionLider4.setVisible(true);//opcionLider incluye el botón para eliminar el líder
    cmbLider4.setVisible(true);
    cmbLider4.setValue(nombreLider4);
    cmbLider4.open();
    cmbLider4.setFocus(true);
    btnQuitarLider4.setVisible(false);
    //no se pueden agregar más
    mostrarOpcionAgregarLider(false);
  }

  private void cancelarEditLider4() {
    cmbLider4.setVisible(false);
    etqLider4.setVisible(true);
  }

  private void procesarLider4() {
    nombreLider4 = cmbLider4.getValue();
    if (nombreLider4.isEmpty() || nombreLider4.equals(LIDER4_VACIO)) {
      //Validar si eligió algo
      quitarOpcionLider(4);
      return;
    }
    idLider4 = servicioRed.getIdLider(nombreLider4);
    setVarSesionLider4();

    //**
    System.out.println("CtrlCelulaDatosBasicos. Líder 4 seleccionado.nombre: " + nombreLider4);
    System.out.println("CtrlCelulaDatosBasicos. Líder 4 seleccionado.id: " + idLider4);

    if (agregarLiderCelula(idLider4)) {
      mostrarMensaje("Líder agregado");
      etqLider4.setValue(nombreLider4);
      cancelarEditLider4();
      mostrarOpcionQuitarLider(4, true);
      mostrarOpcionAgregarLider(false);
    } else {
      mostrarMensaje("Error agregando líder");
      System.out.println("CtrlCelulaDatosBasicos.error agregando líder 4");
      quitarOpcionLider(4);
      return;
    }
  }

  private void mostrarOpcionQuitarLider(int i, boolean visible) {
    //TODO: MEJORA-CODIGO: quitar caso i=1: no se puede quitar al líder 1
    if (i == 1) {
      //- btnQuitarLider1.setVisible(visible);
    } else if (i == 2) {
      btnQuitarLider2.setVisible(visible);
    } else if (i == 3) {
      btnQuitarLider3.setVisible(visible);
    } else if (i == 4) {
      btnQuitarLider4.setVisible(visible);
    }
  }
}
/**
 * TODO:
 * 1. quitar condicionales de operaciones:
 * if (Sesion.modoEditable()) {
 * 2. métodos onBlur hacen que la actualización sea doble
 * 
 */
