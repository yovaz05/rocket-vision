package sig.controladores;

import cdo.sgd.controladores.CtrlVista;
import cdo.sgd.controladores.Sesion;
import cdo.sgd.controladores.Vistas;
import cdo.sgd.modelo.bd.simulador.CelulaUtil;
import java.util.Date;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import sig.modelo.servicios.ServicioCelula;

/**
 * controlador asociado a CelulaSimulador.zul
 * @author Gabriel
 */
public class CtrlCelula extends GenericForwardComposer {

  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //mensajes
  Div divMensaje;
  Label etqMensaje;
  //widgets:
  Label tituloVentana;
  //botones
  Toolbarbutton btnNew;
  Toolbarbutton btnGuardar;
  Toolbarbutton btnEditar;
  //tabbox:
  Tabbox tabbox;
  Tab tabDB;
  Tab tabDir;
  Tab tabOtros;
  Tab tabObs;
  A btnIngresarReporte;
  //pestaña "Datos Básicos"
  Column db$colData;
  Column db$colEdit;
  //inputs:
  Textbox db$txtCodigo;
  Textbox db$txtNombre;
  Label db$etqCodigo;
  Label db$etqNombre;
  Label db$etqRed;
  Label db$etqLider1;
  Label db$etqLider2;
  Label db$etqLider3;
  Label db$etqLider4;
  Label db$etqDia;
  Label db$etqHora;
  Combobox db$cmbRed;
  Combobox db$cmbDia;
  Combobox db$cmbHora;
  Combobox db$cmbLider1, db$cmbLider2, db$cmbLider3, db$cmbLider4;
  Div db$opcionLider1, db$opcionLider2, db$opcionLider3, db$opcionLider4;
  Div db$divLideresEdit;
  A db$tbbRed;
  A db$tbbLider1, db$tbbLider2, db$tbbLider3, db$tbbLider4;
  A db$btnEditRed, db$btnCancelarEditRed;
  A db$btnEditLideres, db$btnCancelarEditLideres;
  //pestaña "Dirección"
  Column dir$colView;
  Column dir$colEdit;
  Combobox dir$cmbEstado;
  Combobox dir$cmbCiudad;
  Combobox dir$cmbZona;
  Textbox dir$txtZona;
  Textbox dir$txtDetalle;
  Textbox dir$txtTelefono;
  Label dir$etqEstado;
  Label dir$etqCiudad;
  Label dir$etqZona;
  Label dir$etqDetalle;
  Label dir$etqTelefono;
  //usos posteriores:
  //pestaña "Otros datos"
  Column otros$colData;
  Datebox otros$dateboxFechaApertura;
  Label otros$etqFechaApertura;
  Label otros$etqAnfitrion;
  Textbox otros$txtAnfitrion;
  Toolbarbutton otros$btnCatAnfitrion;
  //pestaña "Observaciones"
  Label obs$etqObservaciones;
  Textbox obs$txtObservaciones;
  //variables de control:
  int tabSeleccionado;
  String modo;  //modo={new,edicion,ver,imprimir}
  String titulo = "Célula";
  CtrlVista ctrlVista = new CtrlVista();
  private String descripcionCelula;
  String fechaApertura = "";
  Date dateFechaApertura;
  int nLideres = 0;
  //gestión de datos
  //data en modificación:
  String codigo = "";
  String diaTexto = "";
  String horaTexto = "";
  int dia;
  int hora;
  int diaNumero = 1;  //día por defecto en combo: Lunes
  int horaNumero = 19;  //hora por defecto en combo: 7.00pm
  int idRed = 1;
  String nombreRed = "";
  //IDs de personas elegidas como líderes de célula
  int idLider1 = 0;
  int idLider2 = 0;
  int idLider3 = 0;
  int idLider4 = 0;
  int idEstado = 0;
  int idCiudad = 0;
  int idZona = 0;
  String nombreEstado = "";
  String nombreCiudad = "";
  String nombreZona = "";
  //fecha de apertura con formato para BD
  String fechaAperturaBD;
  private String anfitrion;
  //objeto con la data de la base de datos
  CelulaUtil celula = new CelulaUtil();
  //objeto con la data ingresada por el usuario
  CelulaUtil celulaActual = new CelulaUtil();

  ServicioCelula servicio = new ServicioCelula();
  private int idCelula;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    System.out.println("CtrlCelula.INICIO()");
    modo = Sesion.getModo();
    if (modo == null) {
      modo = "new";
    }
    if (modo.equals("edicion-dinamica")) {
      System.out.println("CtrlCelula.modoActual = ver");
      getID();
      System.out.println("CtrlCelula.inicio().id = " + idCelula);
      buscarData();
      mostrarData();
      //TODO: valor por defecto: tab mostrada al abrir: 1
      selectTab(1);
    }
    actualizarEstado();
    notificarBarra();
  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.CELULA);
    Sesion.setModo(modo);
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }

  /**
   * recupera variable de sesión 'idCelula'
   * que fue seteada por la vista llamante
   * sólo en modo ver
   */
  //TODO: MEJORA: evaluar forma de obtener este valor si viniera en el URL
  private void getID() {
    try {
      idCelula = (Integer) Sesion.getVariable("idCelula");
      System.out.println("CtrlCelula.getId:id=" + idCelula);
    } catch (Exception e) {
      System.out.println("CtrlCelula -> ERROR: parámetro idCelula nulo... "
              + e);
      idCelula = 0;
    }
  }

  /**
   * obtiene la célula de la base de datos
   * con el id de la célula
   */
  void buscarData() {
    celula = servicio.getCelula(idCelula);
    if (celula == null) {
      System.out.println("CtrlCelula.buscarCelulaBD: NULL");
    } else {
      System.out.println("CtrlCelula.Celula=" + celula.toString());
    }
    System.out.println("CtrlCelula.buscarData - nLideres=" + nLideres);
    setVariablesSesion();
  }

  /**
   * guarda las variables de sesión
   * para ser usadas por otros controladores
   */
  private void setVariablesSesion() {
    idRed = celula.getIdRed();
    nombreRed = celula.getNombreRed();
    nLideres = celula.getNumeroLideres();
    idLider1 = celula.getIdLider1();
    idLider2 = celula.getIdLider2();
    idLider3 = celula.getIdLider3();
    idLider4 = celula.getIdLider4();
    idEstado = celula.getDireccion().getIdEstado();
    idCiudad = celula.getDireccion().getIdCiudad();
    idZona = celula.getDireccion().getIdZona();
    nombreEstado = celula.getDireccion().getEstado();
    nombreCiudad = celula.getDireccion().getCiudad();
    nombreZona = celula.getDireccion().getZona();
    //TODO: poner en sers
    Sesion.setVariable("idCelula", idCelula);
    Sesion.setVariable("celula.idRed", idRed);
    Sesion.setVariable("celula.nombreRed", nombreRed);
    Sesion.setVariable("celula.nLideres", nLideres);
    Sesion.setVariable("celula.idLider1", idLider1);
    Sesion.setVariable("celula.idLider2", idLider2);
    Sesion.setVariable("celula.idLider3", idLider3);
    Sesion.setVariable("celula.idLider4", idLider4);
    Sesion.setVariable("idEstado", idEstado);
    Sesion.setVariable("idCiudad", idCiudad);
    Sesion.setVariable("idZona", idZona);
    Sesion.setVariable("celula.nombreEstado", nombreEstado);
    Sesion.setVariable("celula.nombreCiudad", nombreCiudad);
    Sesion.setVariable("celula.nombreZona", nombreZona);
  }

  /**
   * muestra los datos actuales de la célula, desde la base de datos
   */
  public void mostrarData() throws InterruptedException {
    if (celula == null) {
      Messagebox.show("Error buscando datos de la célula");
      return;
    }
    //llenar widgets con la data
    //datos básicos
    codigo = celula.getCodigo();
    db$etqCodigo.setValue(codigo);
    String nombre = celula.getNombre();
    if (nombre.isEmpty()) {
      //si no hay valor, mostrar una etiqueta para permitir edición
      nombre = Constantes.VALOR_EDITAR;
    }
    db$etqNombre.setValue(nombre);

    db$tbbRed.setLabel(celula.getNombreRed());

    diaTexto = celula.getDia();
    if (diaTexto.isEmpty()) {
      //si no hay valor, mostrar una etiqueta para permitir edición
      diaTexto = Constantes.VALOR_EDITAR;
    }
    db$etqDia.setValue(diaTexto);

    horaTexto = celula.getHora();
    if (horaTexto.isEmpty()) {
      //si no hay valor, mostrar una etiqueta para permitir edición
      horaTexto = Constantes.VALOR_EDITAR;
    }
    db$etqHora.setValue(horaTexto);

    if (seUsaLider(1)) {
      db$tbbLider1.setLabel(celula.getNombreLider1());
    }
    if (seUsaLider(2)) {
      db$tbbLider2.setLabel(celula.getNombreLider2());
    }
    if (seUsaLider(3)) {
      db$tbbLider3.setLabel(celula.getNombreLider3());
    }
    if (seUsaLider(4)) {
      db$tbbLider4.setLabel(celula.getNombreLider4());
    }

    //dirección:
    idZona = celula.getDireccion().getIdZona();
    String estado;
    if (idZona == 1) {
      estado = Constantes.VALOR_EDITAR;
    } else {
      estado = celula.getDireccion().getEstado();
      dir$etqCiudad.setValue(celula.getDireccion().getCiudad());
      dir$etqZona.setValue(celula.getDireccion().getZona());
    }
    dir$etqEstado.setValue(estado);

    String detalle = celula.getDireccion().getDirDetallada();
    if (detalle.isEmpty()) {
      detalle = Constantes.VALOR_EDITAR;
    }
    dir$etqDetalle.setValue(detalle);

    String telefono = celula.getDireccion().getTelefono();
    if (telefono.isEmpty()) {
      telefono = Constantes.VALOR_EDITAR;
    }
    dir$etqTelefono.setValue(telefono);

    fechaApertura = celula.getFechaApertura();
    if (fechaApertura.isEmpty()) {
      fechaApertura = Constantes.VALOR_EDITAR;
    }
    //TODO: convertir fecha a formato legible: 'día mes, año'
    otros$etqFechaApertura.setValue(fechaApertura);

    anfitrion = celula.getAnfitrion();
    if (anfitrion.isEmpty()) {
      anfitrion = Constantes.VALOR_EDITAR;
    }
    otros$etqAnfitrion.setValue(anfitrion);

    String observaciones = celula.getObservaciones();
    if (observaciones.isEmpty()) {
      observaciones = Constantes.VALOR_EDITAR;
    }
    obs$etqObservaciones.setValue(observaciones);

    //crear descripción de la célula, para el título:
    //TODO: si hay direcció, agregarsela al título
    //+descripcionCelula = generarDescripcionCelula(celula.getCodigo(), celula.getDireccionCorta());
    descripcionCelula = celula.getCodigo();


    //TODO: configurar parámetros para navegación dinámica:
    /*
    final int idRed = celula.getIdRed();
    final int idLider1 = celula.getIdLider1();
    final int idLider2 = celula.getIdLider2();
    final int idLider3 = celula.getIdLider3();
    final int idLider4 = celula.getIdLider4();
    String lider1 = celula.getNombreLider1();
    String lider2 = celula.getNombreLider2();
    String lider3 = celula.getNombreLider3();
    String lider4 = celula.getNombreLider4();
    
    db$tbbRed.addEventListener(Events.ON_CLICK, new EventListener() {
    
    public void onEvent(Event event) throws Exception {
    Sessions.getCurrent().setAttribute("idRed", idRed);
    panelCentral.setSrc("vistaRed/Red.zul");
    }
    });
    
    db$tbbLider1.addEventListener(Events.ON_CLICK, new EventListener() {
    
    public void onEvent(Event event) throws Exception {
    Sessions.getCurrent().setAttribute("idLider", idLider1);
    panelCentral.setSrc("vistaLider/Resumen.zul");
    System.out.println("CtrlCelula -> idLider = " + idLider1);
    }
    });
    
    if ((idLider2 == 0) || (lider2 == null) || lider2.equals("") || lider2.equals("Pastora Irene de Mora")) {
    db$tbbLider2.setVisible(false);
    } else {
    db$tbbLider2.setLabel(lider2);
    db$tbbLider2.addEventListener(Events.ON_CLICK, new EventListener() {
    
    public void onEvent(Event event) throws Exception {
    Sesion.setVariable("idLider", idLider2);
    panelCentral.setSrc("vistaLider/Resumen.zul");
    System.out.println("CtrlCelula -> idLider = " + idLider2);
    }
    });
    }
     */

  }

  /**
   * actualiza el estado de los widgets, título de ventana y setea el foco
   */
  private void actualizarEstado() {
    if (modo.equals("new")) {
      tituloVentana.setValue(titulo + " » Ingresar");
      //- mostrarColumnasVisualizacion(false);
      mostrarWidgetsNew(true);
      mostrarTabsRestantes(false);
      mostrarWidgetsViewLink(false);
      mostrarWidgetsEdit(false);
      //- mostrarOpcionLider1();
      verBotonesEdicion(false);
      selectTab(1);
      setFocoEdicion();
      //- btnIngresarReporte.setVisible(false);
    } else if (modo.equals("edicion-dinamica")) {
      tituloVentana.setValue(titulo + ": " + descripcionCelula);
      mostrarWidgetsNew(false);
      mostrarTabsRestantes(true);
      mostrarWidgetsEdit(true);
      mostrarWidgetsViewLink(true);
      //TODO: PERMISOS: aquí se debe comprobar el permiso de edición para activar la siguiente línea
      verBotonesEdicion(true);
      setFocoEdicion();
      //-mostrarWidgetsEdit(false);
      //TODO: permisos, chequeo si se tiene permiso de edición
      //- btnIngresarReporte.setVisible(true);
      /*modo editar, funciona
      } else if (modo.equals("editar")) {
      tituloVentana.setValue(titulo + ": " + descripcionCelula + " » Editando");
      verColumnasData(true);
      mostrarColumnasVisualizacion(false);
      setFocoEdicion();
      //- btnIngresarReporte.setVisible(false);
      }
       * 
       */
      //modo editar, modificando, sólo mostrará columnas con botones de edición
    } else if (modo.equals("editar")) {
      tituloVentana.setValue(titulo + ": " + descripcionCelula + " » Editando");
      getValoresEdit();
      mostrarWidgetsViewLink(false);
      mostrarWidgetsEdit(true);
      //chequear permisos para ver si usuario puede editar la célula:
      verBotonesEdicion(true);
      setFocoEdicion();
      //- btnIngresarReporte.setVisible(false);
    }
  }

  public void onClick$btnNew() {
    modo = "new";
    Sesion.setModo(modo);
    Sesion.setVariable("idCelula", 0);
    mensaje("Ingresa el código y elige la red");
    actualizarEstado();
  }

  public void onClick$btnEditar() {
    modo = "editar";
    Sesion.setModo(modo);
    actualizarEstado();
  }

  /**
   * no usado actualmente
   */
  public void onClick$btnBorrar() {
    modo = "delete";
    actualizarEstado();
    //aquí va la operación de borrado
    //borrar el registro
    //cambiarVista a listado de células
    modo = "ver";
  }

  /**
   * no usado
   */
  public void onClick$btnImprimir() {
    //procesamiento de impresión
  }

  /**
   * oculta o muestra los botones de edición de campos
   * @param status el estado true o false
   */
  public void verBotonesEdicion(boolean status) {
    //-db$colEdit.setVisible(status);
    db$btnEditRed.setVisible(status);
    //-db$btnEditLideres.setVisible(status);
  }

  public void onSelect$tabbox() {
    tabSeleccionado = tabbox.getSelectedIndex();
    /*-
    if (modo.equals("editar")) {
    setFocoEdicion();
    }
     * 
     */
  }

  /**
   * seleccionar tab de la posición i
   * @param i la posición del tab, comenzando con 1
   * 1: datos básicos
   * 2: dirección
   * 3: otros datos
   * 4: observaciones
   */
  private void selectTab(int i) {
    tabbox.setSelectedIndex(i - 1);
    tabSeleccionado = i;
  }

  /**
   * le da el foco al primer elemento de entrada del tab actual
   **/
  public void setFocoEdicion() {
    if (tabSeleccionado == 1) {
      db$txtCodigo.setFocus(true);
      //-db$txtCodigo.select();
    } else if (tabSeleccionado == 2) {
      dir$cmbEstado.setFocus(true);
    } else if (tabSeleccionado == 3) {
      otros$dateboxFechaApertura.setFocus(true);
      //-otros$dateboxFechaApertura.select();
    } else if (tabSeleccionado == 4) {
      obs$txtObservaciones.setFocus(true);
      //TODO: ubicar cursor al final del texto, si se está en modo editar
    }
  }

  public void onClick$btnIngresarReporte() {
    //actualizar variables de sesión:
    Sesion.setVistaActual(Vistas.CELULA);
    Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA);
    Sesion.setModo("new");  //modo insertCelula
    //envia click al btnControl de CtrlBarraMenu, para que cambie la vista
    ctrlVista.forzarCambioVista_btnControl();
  }

  /**
   * EN DESUSO
   * muestra los valores actuales en los widgets de entrada (captura de datos)
   */
  //TODO: buscar en la base de datos, llenar los combos, y elegir los valores correspondientes al registro
  //VERSION ANTERIOR: saca los valores de los widgets de visualización
  /*
  private void mostrarValoresEdit() {
  //datos básicos:
  db$txtCodigo.setValue(db$etqCodigo.getValue());
  db$txtNombre.setValue(db$etqNombre.getValue());
  
  //TODO: convertir día y hora a número acorde a los valores estándares de base de datos
  //getDiaHoraSeleccionada();
  db$cmbDia.setValue(diaTexto);
  db$cmbHora.setValue(horaTexto);
  
  db$cmbRed.setValue(celula.getNombreRed());
  //- db$etqLider1.setValue(db$tbbLider1.getLabel());
  //- db$etqLider2.setValue(db$tbbLider2.getLabel());
  
  //TODO: asignar valores a combobox de líderes
  db$cmbLider1.setValue(db$tbbLider1.getLabel());
  
  String nombreLider2 = db$tbbLider2.getLabel();
  if (!nombreLider2.isEmpty()) {
  db$cmbLider2.setValue(nombreLider2);
  db$opcionLider2.setVisible(true);
  }
  
  //dirección:
  dir$cmbEstado.setValue(dir$etqEstado.getValue());
  dir$cmbCiudad.setValue(dir$etqCiudad.getValue());
  dir$txtZona.setValue(dir$etqZona.getValue());
  dir$txtDetalle.setValue(dir$etqDetalle.getValue());
  dir$txtTelefono.setValue(dir$etqTelefono.getLabel());
  
  //Otros datos:
  otros$dateboxFechaApertura.setValue(new Date(fechaApertura));
  otros$txtAnfitrion.setValue(otros$etqAnfitrion.getValue());
  
  //Observaciones:
  obs$txtObservaciones.setValue(obs$etqObservaciones.getValue());
  
  }
   * 
   */
  /**
   * muestra/oculta los widgets de visualización que no sirven para edición
   */
  private void mostrarWidgetsViewLink(boolean status) {
    db$tbbRed.setVisible(status);
    db$tbbLider1.setVisible(status);
    db$tbbLider2.setVisible(status);
    db$tbbLider3.setVisible(status);
    db$tbbLider4.setVisible(status);
  }

  /**
   * muestra/oculta los widgets de visualización que no sirven para edición
   */
  private void mostrarEtiquetasLider(boolean status) {
    db$etqLider1.setVisible(status);
    db$etqLider2.setVisible(status);
    db$etqLider3.setVisible(status);
    db$etqLider4.setVisible(status);
  }

  /**
   * muestra/oculta las etiquetas que permiten la edición
   */
  private void mostrarWidgetsEdit(boolean status) {
    //datos básicos:
    db$etqRed.setVisible(status);
    if (seUsaLider(1)) {
      db$opcionLider1.setVisible(status);
    }
    if (seUsaLider(2)) {
      db$opcionLider2.setVisible(status);
    }
    if (seUsaLider(3)) {
      db$opcionLider3.setVisible(status);
    }
    if (seUsaLider(4)) {
      db$opcionLider4.setVisible(status);
    }
    db$etqDia.setVisible(status);
    db$etqHora.setVisible(status);
    db$etqNombre.setVisible(status);
    //dirección
    dir$etqEstado.setVisible(status);
    dir$etqDetalle.setVisible(status);
    dir$etqTelefono.setVisible(status);
    //otros datos
    otros$etqAnfitrion.setVisible(status);
    otros$etqFechaApertura.setVisible(status);
    obs$etqObservaciones.setVisible(status);
  }

  /**
   * muestra los valores actuales en los widgets de entrada (captura de datos)
   */
  private void getValoresEdit() {
    db$etqRed.setValue(celula.getNombreRed());

    /*?
    //líderes
    if (seUsaLider(1)) {
    db$etqLider1.setValue(celula.getNombreLider1());
    }
    if (seUsaLider(2)) {
    db$etqLider2.setValue(celula.getNombreLider2());
    }
    if (seUsaLider(3)) {
    db$etqLider3.setValue(celula.getNombreLider3());
    }
    if (seUsaLider(4)) {
    db$etqLider4.setValue(celula.getNombreLider4());
    }
    ?*/

    /*
    //TODO: convertir día y hora a número acorde a los valores estándares de base de datos
    db$cmbDia.setValue(diaTexto);
    db$cmbHora.setValue(horaTexto);
    
    //dirección:
    dir$cmbEstado.setValue(dir$etqEstado.getValue());
    dir$cmbCiudad.setValue(dir$etqCiudad.getValue());
    dir$txtZona.setValue(dir$etqZona.getValue());
    dir$txtDetalle.setValue(dir$etqDetalle.getValue());
    dir$txtTelefono.setValue(dir$etqTelefono.getLabel());
    
    //Otros datos:
    otros$dateboxFechaApertura.setValue(new Date(fechaApertura));
    otros$txtAnfitrion.setValue(otros$etqAnfitrion.getValue());
    
    //Observaciones:
    obs$txtObservaciones.setValue(obs$etqObservaciones.getValue());
     */
  }

  /**
   * muestra etiquetas para los valores no seteados en los widgets de visualización
   * luego de grabar o actualizar el registro
   */
  private void mostrarValoresViewRecienCreada() {
    //datos básicos
    //-db$etqCodigo.setValue(codigo);
    db$tbbRed.setLabel(nombreRed);
    db$etqDia.setValue("Editar Día");
    db$etqHora.setValue("Editar Hora");
    db$etqNombre.setValue(Constantes.VALOR_EDITAR);
    dir$etqEstado.setValue(Constantes.VALOR_EDITAR);
    dir$etqCiudad.setValue("");
    dir$etqZona.setValue("");
    dir$etqDetalle.setValue(Constantes.VALOR_EDITAR);
    dir$etqTelefono.setValue(Constantes.VALOR_EDITAR);
    otros$etqFechaApertura.setValue(Constantes.VALOR_EDITAR);
    otros$etqAnfitrion.setValue(Constantes.VALOR_EDITAR);
    obs$etqObservaciones.setValue(Constantes.VALOR_EDITAR);
    /*
    db$tbbLider1.setLabel(db$cmbLider1.getValue());
    //TODO:mostrar los otros líderes sólo si son usados
    if (seUsaLider(2)) {
    db$tbbLider2.setLabel(db$cmbLider2.getValue());
    }
    if (seUsaLider(3)) {
    db$tbbLider3.setLabel(db$cmbLider3.getValue());
    }
    if (seUsaLider(4)) {
    db$tbbLider4.setLabel(db$cmbLider4.getValue());
    }
    db$etqNombre.setValue(celulaInsert.getNombre());
    db$etqDia.setValue(diaTexto);
    db$etqHora.setValue(" - " + horaTexto);
    dir$etqEstado.setValue(dir$cmbEstado.getValue());
    dir$etqCiudad.setValue(dir$cmbCiudad.getValue());
    dir$etqZona.setValue(dir$cmbZona.getValue());
    dir$etqDetalle.setValue(celulaInsert.getDireccion());
    dir$etqTelefono.setValue(celulaInsert.getTelefono());
    otros$etqFechaApertura.setValue(fechaApertura);
    otros$etqAnfitrion.setValue(celulaInsert.getAnfitrion());
    obs$etqObservaciones.setValue(celulaInsert.getObservaciones());
     * 
     */
  }

  /**
   * experimento
   * permite llamar al método grabar, cuando el usuario presiona Enter en cualquier textbox
   */
  public void onOK() {
    //**mostrarMensaje("Se presionó 'Enter' en algún lado");
    onClick$btnGuardar();
  }

  /**
   * une el día y la hora en forma de texto
   * @return 
   */
  private String getDiaHoraTexto() {
    return "" + db$cmbDia.getValue()
            + " - "
            + db$cmbHora.getValue();
  }

  /**
   * se encarga de crear la célula
   * setear la variable de resultado que será usada por barraMenu para los mensajes
   * y cambiar modo de edicion dinámica
   */
  //DEBERIA LLAMARSE CREAR
  public void onClick$btnGuardar() {
    if (modo.equals("new")) {
      if (ingresarNuevaCelula()) {
        //-Sesion.setVariable("resultOperacion", 1);//indica éxito
        Sesion.setModo(modo = "edicion-dinamica");
        mostrarValoresViewRecienCreada();
        actualizarEstado();
      } else {
        //- Sesion.setVariable("resultOperacion", -1);//indica error
      }
    }
  }

  /**
   * Procesa el registro de una nueva célula.
   * incluye la validación de que se ingresen todos los valores obligatorios
   * @return 
   */
  boolean ingresarNuevaCelula() {
    ocultarMensaje();
    if (!camposObligatoriosIngresados()) {
      return false;
    }
    //ingresar nueva célula en la base de datos      
    if (crearCelula()) {//nueva célula registrada con éxito
      System.out.println("CtrlCelula. Nueva celula ingresada. código:" + codigo);
      //mostrar descripción en titulo:
      //TODO: agregar la zona a la descripción
      //descripcionCelula = generarDescripcionCelula(celulaInsert.getCodigo(), dir$cmbZona.getValue());
      //modo = "ver";
      mensaje("Célula ingresada. Agrega el resto de la información");
      descripcionCelula = codigo;
      //**System.out.println("CtrlCelula.Célula creada con éxito:");
      //**System.out.println("id=" + idCelula);
      return true;
    }
    //error:
    mensaje("Error creando la célula. Vuelve a intentar.");
    return true;
  }

  /**
   * se encarga de crea un célula nueva,  con el código y el id de red
   */
  private boolean crearCelula() {
    codigo = db$txtCodigo.getValue();
    idRed = getIdRed();
    System.out.println("CtrlCelula.crearCelula.codigo=" + codigo);
    System.out.println("CtrlCelula.crearCelula.idRed=" + idRed);
    idCelula = servicio.crearCelula(codigo, idRed);
    System.out.println("CtrlCelula.crearCelula.idCelula=" + idCelula);
    Sesion.setVariable("idCelula", idCelula);
    Sesion.setVariable("celula.idRed", idRed);
    return (idCelula != 0) ? true : false;
  }

  private String generarDescripcionCelula(String codigo, String zona) {
    String desc = codigo;
    if (!zona.isEmpty()) {
      desc += ", " + zona;
    }
    return desc;
  }

  /**
   * Valida todos los campos obligatorios para crear una célula (que deben contener un valor)
   * @return true si todos los campos obligatorios son válidos
   */
  boolean camposObligatoriosIngresados() {
    if (!textboxIngresado(db$txtCodigo, "Ingresa el código")) {
      //set foco en tabPanel y widget correspondiente
      selectTab(1);
      db$txtCodigo.setFocus(true);
      return false;
    } else if (!comboSeleccionado(db$cmbRed, "Selecciona la red")) {
      //set foco en tabPanel y widget correspondiente
      selectTab(1);
      db$cmbRed.setFocus(true);
      return false;
    } /*
     * CAMBIO: estos campos no son necesarios hasta que cree la célula
     * TODO: los líderes también debe ser obligatorios para crear la célula?
    else if (!lideresIngresados()) {
    return false;
    } 
    else if (!textboxIngresado(db$txtNombre, "Ingresa el nombre")) {
    //set foco en tabPanel y widget correspondiente
    selectTab(1);
    db$txtNombre.setFocus(true);
    return false;
    } else if (!estadoSeleccionado()) {
    return false;
    } else if (!ciudadSeleccionada()) {
    return false;
    } else if (!zonaSeleccionada()) {
    return false;
    }
     */
    return true;
  }

  /**
   * valida el valor de un textbox
   * @param widget el textbox a evaluar
   * @return el resultado, true si no hay error, false si hay error
   */
  //TODO: MEJORACODIGO: sacar este método para una clase de utilería
  boolean textboxIngresado(Textbox textbox, String msjError) {
    boolean ok = true;//indica si hay algún error en la data a guardar
    String valor = textbox.getValue();
    if ((valor != null) && !valor.isEmpty() && !valor.equals("")) {
      //valor válido
      ocultarMensaje();
      textbox.setSclass("");
      ok = true;
    } else {
      //valor NO válido
      mensaje(msjError);
      textbox.setSclass("textbox_error");
      ok = false;
    }
    return ok;
  }

  /**
   * valida un combo que se haya seleccionado un valor
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

//TODO: MEJORACODIGO: sacar este método para una clase de utilería
  private boolean lideresIngresados() {
    //sacar número de líderes ingresados, desde variable de sesión en CtrlCelulaDatosBasicos
    nLideres = (Integer) Sesion.getVariable("celula.nLideres");
    if (!comboSeleccionado(db$cmbLider1, "Selecciona el líder")) {
      //set foco en tabPanel y widget correspondiente
      selectTab(1);
      db$cmbLider1.setFocus(true);
      return false;
    }
    if (seUsaLider(2) && !comboSeleccionado(db$cmbLider2, "Selecciona el líder")) {
      //set foco en tabPanel y widget correspondiente
      selectTab(1);
      db$cmbLider2.setFocus(true);
      return false;
    }
    if (seUsaLider(3) && !comboSeleccionado(db$cmbLider3, "Selecciona el líder")) {
      //set foco en tabPanel y widget correspondiente
      selectTab(1);
      db$cmbLider3.setFocus(true);
      return false;
    }
    if (seUsaLider(4) && !comboSeleccionado(db$cmbLider4, "Selecciona el líder")) {
      //set foco en tabPanel y widget correspondiente
      selectTab(1);
      db$cmbLider4.setFocus(true);
      return false;
    }
    return true;
  }

  /**
   * determina si un líder adicional (mayor a 2) es usado N: {3 o 4}
   * @param posLider
   * @return 
   */
  private boolean seUsaLider(int posLider) {
    if (posLider <= nLideres) {
      return true;
    }
    return false;
  }

  /**
   * MUESTRA EL CÓDIGO Y LA RED, que son los datos obligatorios para crear la célula,
   * pero la red está deshabilitada
   */
  private void mostrarWidgetsNew(boolean visible) {
    db$txtCodigo.setVisible(visible);
    db$cmbRed.setVisible(visible);
    //TODO: se mostrará el combo de red sólo cuando ingresa el código
    if (visible) {
      db$cmbRed.setDisabled(true);
    }
  }

  private int getIdRed() {
    return (Integer) Sesion.getVariable("cmbRed.id");
  }

  private void mostrarTabsRestantes(boolean visible) {
    tabDir.setVisible(visible);
    //+ tabOtros.setVisible(visible);
    tabObs.setVisible(visible);
  }

  /** método debug
   * muestra un mensaje en label
   * @param msj 
   */
  private void mensaje(String msj) {
    etqMensaje.setValue(msj);
    etqMensaje.setVisible(true);
    divMensaje.setVisible(true);
    System.out.println(this.getClass().toString() + msj);
  }

  /** 
   * limpia el mensaje de estado
   */
  private void ocultarMensaje() {
    etqMensaje.setVisible(false);
    etqMensaje.setValue("");
  }
}
//TAREAS: sacar estos métodos de validación a una utilería si hace falta:
//TODO: MEJORACODIGO: sacar este método para una clase de utilería
/**
 * valida que se seleccionó un estado
 */
/*
private boolean estadoSeleccionado() {
String valor = dir$cmbEstado.getValue();
//TODO: ojo con el valor de nueva zona
if ((valor != null) && !valor.equals("")) {
ocultarMensaje();
dir$cmbEstado.setSclass("");
return true;
}
//error:
mostrarMensaje("Selecciona el estado");
dir$cmbEstado.setSclass("textbox_error");
//set foco en tabPanel y widget correspondiente
selectTab(2);
dir$cmbEstado.setFocus(true);
return false;
}
 */
/**
 * valida que se seleccionó una ciudad
 */
/*
private boolean ciudadSeleccionada() {
String valor = dir$cmbCiudad.getValue();
//TODO: ojo con el valor de nueva zona
if ((valor != null) && !valor.equals("")) {
ocultarMensaje();
dir$cmbCiudad.setSclass("");
return true;
}
//error:
mostrarMensaje("Selecciona la ciudad");
dir$cmbCiudad.setSclass("textbox_error");
//set foco en tabPanel y widget correspondiente
selectTab(2);
dir$cmbCiudad.setFocus(true);
return false;
}
 */
/**
 * valida que se seleccionó una zona (Sector)
 */
/*
private boolean zonaSeleccionada() {
String valor = dir$cmbZona.getValue();
//TODO: ojo con el valor de nueva zona
if ((valor != null) && !valor.equals("") && !valor.contains("Nueva")) {
ocultarMensaje();
dir$cmbZona.setSclass("");
return true;
}
//error:
mostrarMensaje("Selecciona el sector");
dir$cmbZona.setSclass("textbox_error");
//set foco en tabPanel y widget correspondiente
selectTab(2);
dir$cmbZona.setFocus(true);
return false;
}
 */
//GARBAGE
/**
 * se encarga de ingresar los datos de la celula nueva
 * con TODOS LOS DATOS
 */
/*
private boolean ingresarCelula() {
prepararCelulaInsert();
idCelula = servicio.insertCelula(celulaInsert);
if (idCelula == 0) {//error, no se grabó
return false;
}
ingresarLideresCelula();
return true;
}
 */
/**
 * obtiene todos los datos ingresados por el usuario en los widgets de entrada
 * y los guarda en el objeto 'celulaInsert'
 */
/*
private void prepararCelulaInsert() {
celulaInsert = new CelulaInsert();
//datos básicos:
celulaInsert.setCodigo(db$txtCodigo.getValue());
celulaInsert.setNombre(db$txtNombre.getValue());

idRed = (Integer) Sesion.getVariable("cmbRed.id");
celulaInsert.setIdRed(idRed);

//TODO: en desarrollo todos los valores siguientes
//DOING: obteniendo valores día y hora seleccionados por el usuario:
getDiaHoraSeleccionada();
celulaInsert.setDia(diaNumero);
celulaInsert.setHora(horaNumero);

//obtener valor de la zona por si fue cambiado por en la vista 'Direccion'
idZona = (Integer) Sesion.getVariable("cmbZona.id");
celulaInsert.setIdZona(idZona);

celulaInsert.setDireccion(dir$txtDetalle.getValue());
celulaInsert.setTelefono(dir$txtTelefono.getValue());

prepararFechaApertura();
System.out.println("CtrlCelula: fecha de apertura antes de grabar en bd=" + fechaAperturaBD);
celulaInsert.setFechaApertura(fechaAperturaBD);
celulaInsert.setAnfitrion(otros$txtAnfitrion.getValue());
celulaInsert.setObservaciones(obs$txtObservaciones.getValue());
}
 */
/**
 * genera fecha de apertura en formato para base de datos,
 * y lo guarda en la variable fechaAperturaBD
 */
/*
void prepararFechaApertura() {
dateFechaApertura = otros$dateboxFechaApertura.getValue();
if (dateFechaApertura != null) {
Calendar cal = UtilFechas.getCalendar(dateFechaApertura);
fechaAperturaBD = UtilFechas.getFechaMySql(dateFechaApertura);
fechaApertura = UtilFechas.getFechaTextoDiaMesAñoAbreviado(cal);
} else {
fechaAperturaBD = "";
fechaApertura = "";
}
}
 */
/**
 * busca los id's de los líderes elegidos
 */
/*
void getLideresCelulaElegidos() {
idLider1 = buscarIdLider(1);
System.out.println("CtrlCelula-lider1.id=" + idLider1);
if (nLideres >= 2) {
idLider2 = buscarIdLider(2);
System.out.println("CtrlCelula-lider2.id=" + idLider2);
}
if (nLideres >= 3) {
idLider3 = buscarIdLider(3);
System.out.println("CtrlCelula-lider3.id=" + idLider3);
}
if (nLideres == 4) {
idLider4 = buscarIdLider(4);
System.out.println("CtrlCelula-lider4.id=" + idLider4);
}
}

void ingresarLideresCelula() {
getLideresCelulaElegidos();
if (idLider1 != 0) {
ingresarLiderCelula(idLider1);
}
if (idLider2 != 0) {
ingresarLiderCelula(idLider2);
}
if (idLider3 != 0) {
ingresarLiderCelula(idLider3);
}
if (idLider4 != 0) {
ingresarLiderCelula(idLider4);
}
}
 */
/**
 * agregar lider a la célula
 * @param idLider id del líder
 */
/*
private void ingresarLiderCelula(int idLider) {
if (servicio.agregarLiderCelula(idLider)) {
System.out.println("CtrlCelula. Líder registrado. Célula.id=" + idCelula + ", Lider.id=" + idLider);
}
}
 */
/**
 * busca el id del líder, desde las variables de sesión   * 
 * @param nLider número de líder, {1,2,3,4}
 */
/*
private int buscarIdLider(int nLider) {
int id = 0;
if (nLider == 1) {
id = (Integer) Sesion.getVariable("celula.lider1.id");
} else if (nLider == 2) {
id = (Integer) Sesion.getVariable("celula.lider2.id");
} else if (nLider == 3) {
id = (Integer) Sesion.getVariable("celula.lider3.id");
} else if (nLider == 4) {
id = (Integer) Sesion.getVariable("celula.lider4.id");
}
return id;
}
 */
/**
 * captura los valores de día y hora seleccionados por el usuario,
 * usando variables de sesión guardadas por la clase CtrlCelulaDatosBasicos
 */
/*
public void getDiaHoraSeleccionada() {
diaTexto = "" + Sesion.getVariable("celula.dia.texto");
horaTexto = "" + Sesion.getVariable("celula.hora.texto");
diaNumero = (Integer) Sesion.getVariable("celula.dia.numero");
horaNumero = (Integer) Sesion.getVariable("celula.hora.numero");
}
 */
/*
private void mostrarOpcionLider1() {
db$opcionLider1.setVisible(true);
}
   //objeto para insertar nueva célula
  CelulaInsert celulaInsert;
 */
