package sig.controladores;

import cdo.sgd.controladores.Sesion;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import sig.modelo.servicios.ServicioRed;
import waytech.modelo.beans.sgi.Red;

/**
 * Controlador asociado a vistaCelula/DatosBasicos.zul
 * Se encarga de buscar la data para mostrarla en las listas, validaciones y cosas por el estilo
 * @author Gabriel
 */
public class CtrlCelulaDatosBasicos extends GenericForwardComposer {

  //widgets:
  Label etqMensaje;
  Textbox txtCodigo, txtNombre;
  A tbbRed;
  A tbbLider1, tbbLider2, tbbLider3, tbbLider4;
  Label etqRed;
  Label etqCodigo, etqLider1, etqLider2, etqLider3, etqLider4, etqNombre;
  Combobox cmbRed, cmbDia, cmbHora;
  Combobox cmbLider1, cmbLider2, cmbLider3, cmbLider4;
  Div opcionLider1, opcionLider2, opcionLider3, opcionLider4;
  Div opcionAddLider;
  //data:
  ServicioRed servRed = new ServicioRed();
  List redesNombres = new ArrayList();
  List lideresLanzadosNombres = new ArrayList();
  Red redSelecionada;
  ListModelList modelRedes = new ListModelList();
  ListModelList modelLideresLanzados = new ListModelList();
  int idRed = 0;
  String nombreRed = "Blood and Fire";
  String diaTexto = "";
  String horaTexto = "";
  int diaNumero = 1;//valor por defecto: lunes
  int horaNumero = 19;//valor por defecto: 7.00 p.m.
  //controles para agregar/quitar líderes
  int nLideresUsados = 0; //indica el número de líderes en uso actualmente
  A btnAddLider;
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
  Label etqNoHayLideres;
  A btnEditRed, btnCancelarEditRed;
  A btnEditLideres, btnCancelarEditLideres;
  private Div linksLideres;
  private Div etqsLideres;
  private Div combosLideres;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    cargarRedes();
    cargarLideresLanzados();
    setVariablesSesionValoresPorDefecto();
  }

  /** método debug
   * muestra un mensaje en un label, para interacción con el usuario
   * @param msj 
   */
  private void mostrarMensaje(String msj) {
    etqMensaje.setValue(msj);
    etqMensaje.setVisible(true);
    System.out.println(this.getClass().toString() + ": msj");
  }

  /** 
   * limpia el mensaje de estado
   */
  private void ocultarMensaje() {
    etqMensaje.setVisible(false);
    etqMensaje.setValue("");
  }

  /**
   * carga lista de redes
   */
  private void cargarRedes() {
    redesNombres = servRed.getRedesNombres();
    modelRedes.addAll(redesNombres);
    cmbRed.setModel(modelRedes);
    /**
     * TODO: EVALUAR - MEJORA DE USABILIDAD: mostrar sólo las redes donde hay líderes lanzados registrados
     */
  }
  //TODO: obtener id de la red seleccionada directamente del combobox
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
    mostrarLinksLideres(false);
    mostrarEtiquetasLider(false);
    System.out.println("red value " + cmbRed.getValue());
    idRed = servRed.getIdRed();
    System.out.println("cargarLideresLanzados. nombre red: " + nombreRed);
    System.out.println("cargarLideresLanzados. id red: " + idRed);
    lideresLanzadosNombres = servRed.getLideresLanzadosNombres(idRed);
    if (lideresLanzadosNombres.isEmpty()) {
      mostrarMensajeNoHayLideres(true);
      return;
    } else {
      mostrarMensajeNoHayLideres(false);
      activarEditLideres();
    }
    modelLideresLanzados = new ListModelList();
    modelLideresLanzados.addAll(lideresLanzadosNombres);
    cmbLider1.setModel(modelLideresLanzados);
    cmbLider2.setModel(modelLideresLanzados);
    cmbLider3.setModel(modelLideresLanzados);
    cmbLider4.setModel(modelLideresLanzados);
  }

  /**
   * método debug
   * obtiene y muestra todos los valores de los elemeentos de entrada
   * tal como serán usados para trabajar con la base de datos
   */
  public void mostrarTodosValores() {
    System.out.println("CELULA. DATOSBASICOS. VALORES INGRESADOS:");
    System.out.println("Código: " + txtCodigo.getValue());
    System.out.println("Nombre: " + txtNombre.getValue());
    System.out.println("Líder 1: nombre=" + cmbLider1.getValue());
    System.out.println("Líder 1: id=" + servRed.getIdPersonaRed(cmbLider1.getValue()));
    System.out.println("Día.label=" + cmbDia.getValue());
    System.out.println("Día.value=" + cmbDia.getSelectedItem().getValue());
    System.out.println("Hora.label: " + cmbHora.getValue());
    System.out.println("Hora.value: " + cmbHora.getSelectedItem().getValue());
  }

  /**
   * maneja la selección del combo red
   */
  public void onSelect$cmbRed() {
    //** System.out.println("CtrlCelulaDatosBasicos.cmbRed.onSelect");
    procesarValorRed();
    cancelarEditRed();
  }

  /**
   * maneja la perdida del foco del combo red
   * cuando se pierde el foco, se tomar el valor elegido
   */
  public void onBlur$cmbRed() {
    procesarValorRed();
    cancelarEditRed();
  }

  void limpiarLideresSeleccionados() {
    cmbLider1.setValue("");
    cmbLider2.setValue("");
    cmbLider3.setValue("");
    cmbLider4.setValue("");
  }

  /**
   * maneja la selección del líder 1
   */
  public void onSelect$cmbLider1() {
    nombreLider1 = cmbLider1.getValue();
    idLider1 = servRed.getIdLider(nombreLider1);
    System.out.println("CtrlCelulaDatosBasicos. Líder 1 seleccionado.nombre: " + nombreLider1);
    System.out.println("CtrlCelulaDatosBasicos. Líder 1 seleccionado.id: " + idLider1);
    setVariablesSesionLider1();
    habilitarOpcionAgregarLider();
  }

  /**
   * maneja la selección del líder 2
   */
  public void onSelect$cmbLider2() {
    nombreLider2 = cmbLider2.getValue();
    idLider2 = servRed.getIdLider(nombreLider2);
    System.out.println("CtrlCelulaDatosBasicos. Líder 2 seleccionado.nombre: " + nombreLider2);
    System.out.println("CtrlCelulaDatosBasicos. Líder 2 seleccionado.id: " + idLider2);
    setVariablesSesionLider2();

  }

  /**
   * maneja la selección del líder 3
   */
  public void onSelect$cmbLider3() {
    nombreLider3 = cmbLider3.getValue();
    idLider3 = servRed.getIdLider(nombreLider3);
    System.out.println("CtrlCelulaDatosBasicos. Líder 3 seleccionado");
    System.out.println("nombre: " + nombreLider3);
    System.out.println("id: " + idLider3);
    setVariablesSesionLider3();
  }

  /**
   * maneja la selección del líder 4
   */
  public void onSelect$cmbLider4() {
    nombreLider4 = cmbLider4.getValue();
    idLider4 = servRed.getIdLider(nombreLider4);
    System.out.println("CtrlCelulaDatosBasicos. Líder 4 seleccionado");
    System.out.println("nombre: " + nombreLider4);
    System.out.println("id: " + idLider4);
    setVariablesSesionLider4();
  }

  /**
   * 
   *TAREA: 
   * 1. mostrar como red por defecto, la red a la que pertenece el usuario en curso, en modo 'new'
   * 2. si la red no tiene líderes lanzados, mostrar un mensaje en el combo
   **/
  public void onSelect$cmbDia() {
    diaTexto = cmbDia.getValue();
    mostrarValorDia();
    cancelarEditDia();
    diaNumero = Integer.parseInt("" + cmbDia.getSelectedItem().getValue());
    setVariableSesionDia();

    //**
    System.out.println("cmbDia.label=" + diaTexto);
    System.out.println("cmbDia.value=" + cmbDia.getSelectedItem().getValue());
    //**
  }

  public void onSelect$cmbHora() {
    horaTexto = cmbHora.getValue();
    mostrarValorHora();
    cancelarEditHora();
    horaNumero = Integer.parseInt("" + cmbHora.getSelectedItem().getValue());
    setVariablesSesionHora();

    //**
    System.out.println("cmbHora.label=" + horaTexto);
    System.out.println("cmbHora.value=" + cmbHora.getSelectedItem().getValue());
    //**
  }

  /**
   * establece la variables de sesión de los líderes
   * para ser usado por los otros controladores
   */
  public void setVariablesSesionLideres() {
    //para setear los 4 líderes
  }

  /**
   * establece la variables de sesión del líder 1
   * para ser usado por los otros controladores
   */
  public void setVariablesSesionLider1() {
    Sesion.setVariable("celula.lider1.id", idLider1);
  }

  /**
   * establece la variables de sesión del líder 2
   * para ser usado por los otros controladores
   */
  public void setVariablesSesionLider2() {
    Sesion.setVariable("celula.lider2.id", idLider2);
  }

  /**
   * establece la variables de sesión del líder 3
   * para ser usado por los otros controladores
   */
  public void setVariablesSesionLider3() {
    Sesion.setVariable("celula.lider3.id", idLider3);
  }

  /**
   * establece la variables de sesión del líder 4
   * para ser usado por los otros controladores
   */
  public void setVariablesSesionLider4() {
    Sesion.setVariable("celula.lider4.id", idLider4);
  }

  /**
   * establece la variables de sesión de día
   * para ser usado por los otros controladores
   */
  public void setVariableSesionDia() {
    Sesion.setVariable("celula.dia.texto", diaTexto);
    Sesion.setVariable("celula.dia.numero", diaNumero);
  }

  /**
   * establece la variables de sesión de hora
   * para ser usado por los otros controladores
   */
  public void setVariablesSesionHora() {
    Sesion.setVariable("celula.hora.texto", horaTexto);
    Sesion.setVariable("celula.hora.numero", horaNumero);
  }

  /**
   * establece la variables de sesión de número de líderes usados
   * para ser usado por los otros controladores
   */
  private void setVariablesSesionNroLideresUsados() {
    Sesion.setVariable("celula.nLideresUsados", nLideresUsados);
  }

  /**
   * guardar variables de sesión con valores por defecto
   * para ser usado por los otros controladores
   */
  private void setVariablesSesionValoresPorDefecto() {
    //TODO: valores por defecto, para asegurar, se puede inicializar sólo en la declaración
    diaTexto = "Lunes";
    horaTexto = "7:00 p.m.";
    setVariableSesionDia();
    setVariablesSesionHora();
    setVariablesSesionNroLideresUsados();
    setVariablesSesionLideres();
  }

  public void onClick$btnAddLider() {
    añadirOpcionLider();
  }

  public void onOK$btnAddLider() {
    añadirOpcionLider();
  }

  public void onClick$opcionAddLider() {
    añadirOpcionLider();
  }

  public void onOK$opcionAddLider() {
    añadirOpcionLider();
  }

  public void onClick$btnQuitarLider2() {
    quitarOpcionLider(2);
  }

  public void onClick$btnQuitarLider3() {
    quitarOpcionLider(3);
  }

  public void onClick$btnQuitarLider4() {
    quitarOpcionLider(4);
  }

  public void onOK$btnQuitarLider2() {
    quitarOpcionLider(2);
  }

  public void onOK$btnQuitarLider3() {
    quitarOpcionLider(3);
  }

  public void onOK$btnQuitarLider4() {
    quitarOpcionLider(4);
  }

  /**
   * muestra un combo de líder N
   * maneja la visibilidad del botón Agregar
   */
  //TODO: validar, sólo se puede agregar otro líder, si en los combos anteriores hay 1 valor seleccionado
  void añadirOpcionLider() {
    int nLideres = nLideresUsados + 1;
    if (nLideres == 1) {
      añadirOpcionLider(1);
      nLideresUsados++;
    } else if (nLideres == 2) {
      if (añadirOpcionLider(2)) {
        nLideresUsados++;
      }
    } else if (nLideres == 3) {
      if (añadirOpcionLider(3)) {
        nLideresUsados++;
      }
    } else if (nLideres == 4) {
      if (añadirOpcionLider(4)) {
        nLideresUsados++;
      }
    }
    if (nLideresUsados == TOPE_LIDERES) {
      opcionAddLider.setVisible(false);
    }
    //**
    setVariablesSesionNroLideresUsados();
    System.out.println("líderes en uso: " + nLideresUsados);
  }

  /**
   * añade el combo N para elegir otro líder N = {1,2,3,4}
   * cumpliendo las condiciones:
   * 1. el combo de líder anterior tiene un valor elegido
   * 2. al combo agregado se le eliminan los valores seleccionados en los combos anteriores
   * devuelve true si se agregó el siguiente combo, false si no se dió
   */
  boolean añadirOpcionLider(int nLider) {
    if (nLider == 1) {
      opcionLider1.setVisible(true);
      return true;
    } else if (nLider == 2) {
      if (!cmbLider1.getValue().isEmpty()) {
        opcionLider2.setVisible(true);
        return true;
      }
    } else if (nLider == 3) {
      if (!cmbLider2.getValue().isEmpty()) {
        opcionLider3.setVisible(true);
        return true;
      }
    } else if (nLider == 4) {
      if (!cmbLider3.getValue().isEmpty()) {
        opcionLider4.setVisible(true);
        return true;
      }
    }
    return false;
  }

  /**
   * quitar el combo de líder N
   * maneja la visibilidad del botón Agregar
   */
  void quitarOpcionLider(int pos) {
    if (pos == 2) {
      opcionLider2.setVisible(false);
      cmbLider2.setValue("");
      idLider2 = 0;
    } else if (pos == 3) {
      opcionLider3.setVisible(false);
      cmbLider3.setValue("");
      idLider3 = 0;
    } else if (pos == 4) {
      opcionLider4.setVisible(false);
      cmbLider4.setValue("");
      idLider4 = 0;
    } else {//número no válido
      return;
    }
    nLideresUsados--;
    if (nLideresUsados < TOPE_LIDERES) {
      opcionAddLider.setVisible(true);
    }
    setVariablesSesionNroLideresUsados();
    System.out.println("líderes en uso: " + nLideresUsados);
  }

  /**
   * habilita la opción para agregar líderes adicionales
   */
  void habilitarOpcionAgregarLider() {
    opcionAddLider.setVisible(true);
  }

  /**
   * devuelve true si se elegió una red de la lista,
   * false en caso contrario
   * @return 
   */
  private boolean redElegida() {
    return !nombreRed.isEmpty();
  }

  //NO USADO
  /*
  void cancelarEditCodigo() {
  if (Sesion.modoEditar()) {
  btnCancelarEditCodigo.setVisible(false);
  btnEditCodigo.setVisible(true);
  }
  }
   * 
   */
  //NO USADO
  /*
  private void capturarValorHora() {
  etqHora.setValue(horaTexto);
  mostrarDiaHora(true);
  cancelarEditDiaHora();
  }
   * 
   */
  //NO USADO
  /*
  void cancelarEditDiaHora() {
  cmbDia.setVisible(false);
  cmbHora.setVisible(false);
  if (Sesion.modoEditar()) {
  btnCancelarEditDiaHora.setVisible(false);
  btnEditDiaHora.setVisible(true);
  }
  }
   * 
   */
  //NO USADO
  /*
  private void mostrarDiaHora(boolean status) {
  etqDia.setVisible(status);
  etqSeparadorDiaHora.setVisible(status);
  etqHora.setVisible(status);
  }
   * 
   */
  private void procesarValorRed() {
    nombreRed = cmbRed.getValue();
    servRed.setNombreRed(nombreRed);    
    System.out.println("CtrlCelulaDatosBasicos - Red Seleccionada - nombre: " + nombreRed);
    System.out.println("CtrlCelulaDatosBasicos - Red seleccionada - id: " + cmbRed.getSelectedItem().getValue());
    
    mostrarLinksLideres(false);
    mostrarEtiquetasLider(false);
    cargarLideresLanzados();
    
    limpiarLideresSeleccionados();
    Sesion.setVariable("celula.red", nombreRed);
    añadirOpcionLider(1);
  }

  private void mostrarMensajeNoHayLideres(boolean status) {
    etqNoHayLideres.setVisible(status);
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
    nombre = txtNombre.getValue();
    //TODO: alguna validación que sea necesaria
    //TODO: chequear si existe una célula con ese mismo nombre
    etqNombre.setValue(nombre);
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
  if (Sesion.modoEditar()) {
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
    cmbDia.open();
  }

  void cancelarEditDia() {
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
    cmbHora.open();
  }

  void cancelarEditHora() {
    etqHora.setVisible(false);
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
    cancelarEditDia();
  }

  /**
   * cuando se pierde el foco, se cancela la edición
   */
  public void onBlur$cmbHora() {
    cancelarEditHora();
  }

  public void onClick$etqCodigo() {
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

  public void onOK$txtCodigo() {
    procesarCodigo();
    cancelarEditCodigo();
  }

  public void onBlur$txtCodigo() {
    procesarCodigo();
    cancelarEditCodigo();
  }

  private void procesarCodigo() {
    codigo = txtCodigo.getValue();
    //TODO: alguna validación que sea necesaria
    //TODO: chequear si existe una célula con ese mismo Codigo
    etqCodigo.setValue(codigo);
  }

  /**
   * muestra/oculta los links de líderes
   */
  private void mostrarLinksLideres(boolean status) {
    tbbLider1.setVisible(status);
    tbbLider2.setVisible(status);
    tbbLider3.setVisible(status);
    tbbLider4.setVisible(status);
  }

  /**
   * muestra/oculta las etiquetas de líderes
   */
  private void mostrarEtiquetasLider(boolean status) {
    etqLider1.setVisible(status);
    etqLider2.setVisible(status);
    etqLider3.setVisible(status);
    etqLider4.setVisible(status);
  }

  void activarEditRed() {
    cmbRed.setValue(tbbRed.getLabel());
    cmbRed.setVisible(true);
    cmbRed.setFocus(true);
    cmbRed.select();
    cmbRed.open();
    tbbRed.setVisible(false);
    etqRed.setVisible(false);
    btnCancelarEditRed.setVisible(true);
    btnEditRed.setVisible(false);
    btnEditLideres.setVisible(false);//bloquear edición de líderes
  }

  void cancelarEditRed() {
    cmbRed.setVisible(false);
    etqRed.setVisible(true);
    btnCancelarEditRed.setVisible(false);
    btnEditRed.setVisible(true);
    btnEditLideres.setVisible(true);//permitir edición de líderes
  }

  void activarEditLideres() {
    btnEditRed.setVisible(false);//bloquear edición de red

    cmbLider1.setValue(etqLider1.getValue());
    cmbLider2.setValue(etqLider2.getValue());
    cmbLider3.setValue(etqLider3.getValue());
    cmbLider4.setValue(etqLider4.getValue());

    linksLideres.setVisible(false);
    etqsLideres.setVisible(true);
    combosLideres.setVisible(true);

    etqLider1.setVisible(true);
    btnCancelarEditLideres.setVisible(true);
    btnEditLideres.setVisible(false);
  }

  void cancelarEditLideres() {
    linksLideres.setVisible(true);
    etqsLideres.setVisible(true);
    combosLideres.setVisible(false);
    btnCancelarEditLideres.setVisible(false);
    btnEditLideres.setVisible(true);
    btnEditRed.setVisible(true);//permitir edición de red
  }

  public void onClick$btnEditRed() {
    activarEditRed();
  }

  public void onClick$etqRed() {
    activarEditRed();
  }

  public void onClick$btnCancelarEditRed() {
    cancelarEditRed();
  }

  public void onClick$btnEditLideres() {
    activarEditLideres();
  }

  public void onClick$btnCancelarEditLideres() {
    cancelarEditLideres();
  }
}
