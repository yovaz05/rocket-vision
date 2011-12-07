package sig.controladores;

import cdo.sgd.controladores.CtrlVista;
import cdo.sgd.controladores.Sesion;
import cdo.sgd.controladores.Vistas;
import cdo.sgd.modelo.bd.simulador.BD;
import cdo.sgd.modelo.bd.simulador.CelulaUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import sig.modelo.servicios.ServicioCelula;
import waytech.modelo.beans.sgi.CelulaInsert;
import waytech.modelo.beans.sgi.PersonaEnCelulaInsert;
import waytech.utilidades.Util;

/**
 * controlador asociado a CelulaSimulador.zul
 * @author Gabriel
 */
public class CtrlCelula extends GenericForwardComposer {

  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //widgets:
  Label tituloVentana;
  Toolbarbutton btnNew;
  Toolbarbutton btnGuardar;
  Toolbarbutton btnEditar;
  Label etqMensaje;
  Tabbox tabbox;
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
  A db$tbbRed;
  A db$tbbLider1, db$tbbLider2, db$tbbLider3, db$tbbLider4;
  //pestaña "Dirección"
  Column dir$colEdit;
  Column dir$colView;
  Combobox dir$cmbEstado;
  Combobox dir$cmbCiudad;
  Combobox dir$cmbZona;
  Textbox dir$txtZona;
  Textbox dir$txtDirDetallada;
  Textbox dir$txtTelefono;
  Label dir$etqEstado;
  Label dir$etqCiudad;
  Label dir$etqZona;
  Label dir$etqDirDetallada;
  Toolbarbutton dir$linkZona;
  Toolbarbutton dir$tbbTelefono;
  //pestaña "Otros datos"
  Column otros$colEdit;
  Column otros$colView;
  Datebox otros$dateboxFechaApertura;
  Label otros$etqFechaApertura;
  Label otros$etqAnfitrion;
  Textbox otros$txtAnfitrion;
  Toolbarbutton otros$btnCatAnfitrion;
  //pestaña "Observaciones"
  Column obs$colEdit;
  Column obs$colView;
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
  int nLideresUsados = 0;
  //gestión de datos
  //data en modificación:
  String diaTexto = "";
  String horaTexto = "";
  int dia;
  int hora;
  int diaNumero = 1;  //día por defecto en combo: Lunes
  int horaNumero = 19;  //hora por defecto en combo: 7.00pm
  int idRed = 1;
  int idZona = 1;
  //fecha de apertura con formato para BD
  String fechaAperturaBD;
  //objeto con la data de la base de datos
  CelulaUtil celulaBD = new CelulaUtil();
  //objeto con la data ingresada por el usuario
  CelulaUtil celulaActual = new CelulaUtil();
  //objeto para insertar nueva célula
  CelulaInsert celulaInsert;
  ServicioCelula servicio = new ServicioCelula();
  private int idCelula;
  //IDs de personas elegidas como líderes de célula
  int idLider1 = 0;
  int idLider2 = 0;
  int idLider3 = 0;
  int idLider4 = 0;
  A db$btnCancelarEditRed;
  A db$btnEditRed;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    System.out.println("CtrlCelula.INICIO()");
    inicio();
  }

  public void inicio() throws InterruptedException {
    modo = Sesion.getModo();
    if (modo.equals("ver")) {
      System.out.println("CtrlCelula.modoActual = ver");
      getID();
      System.out.println("CtrlCelula.inicio().id = " + idCelula);
      buscarDataBD();
      mostrarDatosBD();
      selectTab(1);
    }
    actualizarEstado();
    notificarBarra();
  }

  /**
   * recupera parámetro idCelula que viene de la vista llamante
   */
  //TODO: evaluar qué se hará si no viene de navegación dinámica...
  //DOING:mejorando este método
  private void getID() {
    try {
      idCelula = (Integer) Sessions.getCurrent().getAttribute("idCelula");
      System.out.println("CtrlCelula.getId:id=" + idCelula);
    } catch (Exception e) {
      System.out.println("CtrlCelula -> ERROR: parámetro idCelula nulo... "
              + e);
    }
  }

  /**
   * obtiene la célula de la data actualmente cargada
   */
  void buscarDataBD() {
    celulaBD = servicio.getCelula(idCelula);
    if (celulaBD == null) {
      System.out.println("CtrlCelula.buscarCelulaBD: NULL");
    } else {
      System.out.println("CtrlCelula.Celula=" + celulaBD.toString());
    }
  }

  /**
   * muestra los datos actuales de la célula, desde la base de datos
   * por ahora desde el simulador de BD
   */
  public void mostrarDatosBD() throws InterruptedException {
    if (celulaBD == null) {
      Messagebox.show("Error buscando datos de la célula");
      return;
    }
    //llenar widgets con la data
    //datos básicos
    String codigo = celulaBD.getCodigo();
    db$etqCodigo.setValue(codigo);
    db$etqNombre.setValue(celulaBD.getNombre());
    db$tbbRed.setLabel(celulaBD.getNombreRed());

    diaTexto = celulaBD.getDia();
    horaTexto = celulaBD.getHora();
    db$etqDia.setValue(diaTexto);
    db$etqHora.setVisible(true);
    db$etqHora.setValue(horaTexto);

    db$tbbLider1.setLabel(celulaBD.getNombreLider1());
    db$tbbLider2.setLabel(celulaBD.getNombreLider2());
    db$tbbLider3.setLabel(celulaBD.getNombreLider3());
    db$tbbLider4.setLabel(celulaBD.getNombreLider4());

    //dirección:
    dir$etqEstado.setValue(celulaBD.getDireccion().getEstado());
    dir$etqCiudad.setValue(celulaBD.getDireccion().getCiudad());
    dir$etqZona.setValue(celulaBD.getDireccion().getZona());
    dir$etqDirDetallada.setValue(celulaBD.getDireccion().getDirDetallada());
    dir$tbbTelefono.setLabel(celulaBD.getDireccion().getTelefono());

    fechaApertura = celulaBD.getFechaApertura();
    //TODO: convertir fecha a formato legible: 'día de mes de año'
    //como en reporte de célula. pestaña 'Fechas'
    System.out.println("CtrlCelula.FechaApertura=" + celulaBD.getFechaApertura());
    otros$etqFechaApertura.setValue(fechaApertura);

    otros$etqAnfitrion.setValue(celulaBD.getAnfitrion());

    //TODO: configurar parámetros para navegación dinámica:
    /*
    final int idRed = celulaBD.getIdRed();
    final int idLider1 = celulaBD.getIdLider1();
    final int idLider2 = celulaBD.getIdLider2();
    final int idLider3 = celulaBD.getIdLider3();
    final int idLider4 = celulaBD.getIdLider4();
    String lider1 = celulaBD.getNombreLider1();
    String lider2 = celulaBD.getNombreLider2();
    String lider3 = celulaBD.getNombreLider3();
    String lider4 = celulaBD.getNombreLider4();
    
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

    obs$etqObservaciones.setValue(celulaBD.getObservaciones());

    //crear descripción de la célula, para el título:
    descripcionCelula = generarDescripcionCelula(celulaBD.getCodigo(), celulaBD.getDireccionCorta());
  }

  /**
   * actualiza el estado de los widgets, título de ventana y setea el foco
   */
  public void actualizarEstado() {
    if (modo.equals("new")) {
      tituloVentana.setValue(titulo + " » Ingresar");
      verColumnasData(true);
      mostrarColumnasVisualizacion(false);
      mostrarWidgetsView(false);
      mostrarWidgetsEdit(false);
      verBotonesEdicion(false);
      setFocoEdicion();
      //- btnIngresarReporte.setVisible(false);
    } else if (modo.equals("ver")) {
      tituloVentana.setValue(titulo + ": " + descripcionCelula);
      verColumnasData(false);
      mostrarColumnasVisualizacion(true);
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
      mostrarValoresEdit();
      mostrarWidgetsView(false);
      mostrarWidgetsEdit(true);
      verBotonesEdicion(true);
      setFocoEdicion();
      //- btnIngresarReporte.setVisible(false);
    }
  }

  public void onClick$btnNew() {
    modo = "new";
    Sesion.setModo(modo);
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
   * oculta o muestra las columnas donde están los widgets de entrada
   * @param status el estado true o false
   */
  public void verColumnasData(boolean status) {
    dir$colEdit.setVisible(status);
    otros$colEdit.setVisible(status);
    obs$colEdit.setVisible(status);
  }

  /**
   * oculta o muestra las columnas donde están los widgets de visualización
   * @param status el estado true o false
   */
  public void mostrarColumnasVisualizacion(boolean status) {
    dir$colView.setVisible(status);
    otros$colView.setVisible(status);
    obs$colView.setVisible(status);
  }

  /**
   * oculta o muestra la columna donde están los botones de edición
   * @param status el estado true o false
   */
  public void verBotonesEdicion(boolean status) {
    db$colEdit.setVisible(status);
  }

  public void onSelect$tabbox() {
    tabSeleccionado = tabbox.getSelectedIndex();
    //-Messagebox.show("tab selected: " + tabSeleccionado);
    if (modo.equals("editar")) {
      setFocoEdicion();
    }
  }

  /**
   * seleccionar tab de la posición i
   * @param i la posición del tab, comenzando con 1
   */
  private void selectTab(int i) {
    tabbox.setSelectedIndex(i - 1);
  }

  /**
   * le da el foco al primer elemento de entrada del tab actual
   **/
  public void setFocoEdicion() {
    if (tabSeleccionado == 0) {
      db$txtCodigo.setFocus(true);
      //-db$txtCodigo.select();
    } else if (tabSeleccionado == 1) {
      dir$cmbEstado.setFocus(true);
    } else if (tabSeleccionado == 2) {
      otros$dateboxFechaApertura.setFocus(true);
      //-otros$dateboxFechaApertura.select();
    } else if (tabSeleccionado == 3) {
      obs$txtObservaciones.setFocus(true);
      //TODO: ubicar cursor al final del texto, si se está en modo editar
    }
  }

  public void onClick$btnIngresarReporte() {
    //actualizar variables de sesión:
    Sesion.setVistaActual(Vistas.CELULA);
    Sesion.setVistaSiguiente(Vistas.REPORTE_CELULA);
    Sesion.setModo("new");  //modo ingresarDatosCelula
    //envia click al btnControl de CtrlBarraMenu, para que cambie la vista
    ctrlVista.forzarCambioVista_btnControl();
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
   * EN DESARROLLO
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
  
  db$cmbRed.setValue(celulaBD.getNombreRed());
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
  dir$txtDirDetallada.setValue(dir$etqDirDetallada.getValue());
  dir$txtTelefono.setValue(dir$tbbTelefono.getLabel());
  
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
  private void mostrarWidgetsView(boolean status) {
    //datos básicos:
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
   * muestra/oculta los widgets de edición
   */
  private void mostrarWidgetsEdit(boolean status) {
    //datos básicos:
    db$etqRed.setVisible(status);
    if (seUsaLider(1)) {
      db$opcionLider1.setVisible(true);
      db$etqLider1.setVisible(true);
    }
    if (seUsaLider(2)) {
      db$opcionLider2.setVisible(true);
      db$etqLider2.setVisible(true);
    }
    if (seUsaLider(3)) {
      db$opcionLider3.setVisible(true);
      db$etqLider3.setVisible(true);
    }
    if (seUsaLider(4)) {
      db$opcionLider4.setVisible(true);
      db$etqLider4.setVisible(true);
    }
  }

  /**
   * muestra los valores actuales en los widgets de entrada (captura de datos)
   */
  private void mostrarValoresEdit() {
    db$etqRed.setValue(celulaBD.getNombreRed());
    //líderes
    nLideresUsados = celulaBD.getNumeroLideres();
    if (seUsaLider(1)) {
      db$etqLider1.setValue(celulaBD.getNombreLider1());
    }
    if (seUsaLider(2)) {
      db$etqLider2.setValue(celulaBD.getNombreLider2());
    }
    if (seUsaLider(3)) {
      db$etqLider3.setValue(celulaBD.getNombreLider3());
    }
    if (seUsaLider(4)) {
      db$etqLider4.setValue(celulaBD.getNombreLider4());
    }
    /*
    //TODO: convertir día y hora a número acorde a los valores estándares de base de datos
    //getDiaHoraSeleccionada();
    db$cmbDia.setValue(diaTexto);
    db$cmbHora.setValue(horaTexto);
    
    //dirección:
    dir$cmbEstado.setValue(dir$etqEstado.getValue());
    dir$cmbCiudad.setValue(dir$etqCiudad.getValue());
    dir$txtZona.setValue(dir$etqZona.getValue());
    dir$txtDirDetallada.setValue(dir$etqDirDetallada.getValue());
    dir$txtTelefono.setValue(dir$tbbTelefono.getLabel());
    
    //Otros datos:
    otros$dateboxFechaApertura.setValue(new Date(fechaApertura));
    otros$txtAnfitrion.setValue(otros$etqAnfitrion.getValue());
    
    //Observaciones:
    obs$txtObservaciones.setValue(obs$etqObservaciones.getValue());
     */
  }

  /**
   * EN DESARROLLO
   * muestra los valores en los widgets de visualización
   * luego de grabar o actualizar el registro
   */
  private void mostrarValoresView() {
    //datos básicos
    db$etqCodigo.setValue(celulaInsert.getCodigo());
    db$etqNombre.setValue(celulaInsert.getNombre());
    db$tbbRed.setLabel(db$cmbRed.getValue());
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
    db$etqDia.setValue(diaTexto);
    db$etqHora.setValue(" - " + horaTexto);
    //TODO: faltan todos los demás campos
    dir$etqEstado.setValue(dir$cmbEstado.getValue());
    dir$etqCiudad.setValue(dir$cmbCiudad.getValue());
    dir$etqZona.setValue(dir$cmbZona.getValue());
    dir$etqDirDetallada.setValue(celulaInsert.getDireccion());
    dir$tbbTelefono.setLabel(celulaInsert.getTelefono());
    otros$etqFechaApertura.setValue(fechaApertura);
    otros$etqAnfitrion.setValue(celulaInsert.getAnfitrion());
    obs$etqObservaciones.setValue(celulaInsert.getObservaciones());
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

  //TODO: optimizar código relacionado a variable booleana ok
  public void onClick$btnGuardar() {
    boolean ok = false;
    //TODO: validar si es modo new: ingresarDatosCelula, si es modo edit usar: modificar
    if (modo.equals("editar")) {
      return;//modo editar aún no programado
    }
    ok = addCelula();
    if (ok) {
      Sesion.setVariable("resultOperacion", 1);
      //indica éxito, será usado por barraMenu para los mensajes
    } else {
      Sesion.setVariable("resultOperacion", -1);
      //indica error, será usado por barraMenu para los mensajes
    }
  }

  /**
   * Procesa el registro de una nueva célula.
   * incluye la validación de que se ingresen todos los valores obligatorios
   * @return 
   */
  boolean addCelula() {
    if (camposObligatoriosIngresados()) {
      ocultarMensaje();
      //ingresar nueva célula en la base de datos      
      if (ingresarCelula()) {//nueva célula registrada con éxito
        System.out.println("CtrlCelula. Nueva celula ingresada. código:" + celulaInsert.getCodigo());
        //mostrar descripción en titulo:
        descripcionCelula = generarDescripcionCelula(celulaInsert.getCodigo(), dir$cmbZona.getValue());
        modo = "ver";
        actualizarEstado();
        mostrarValoresView();
        System.out.println("Célula creada con éxito. id=" + idCelula);
        return true;
      }
    }
    return false;
  }

  private String generarDescripcionCelula(String codigo, String zona) {
    return codigo + ", " + zona;
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
    } else if (!textboxIngresado(db$txtNombre, "Ingresa el nombre")) {
      //set foco en tabPanel y widget correspondiente
      selectTab(1);
      db$txtNombre.setFocus(true);
      return false;
    } else if (!comboSeleccionado(db$cmbRed, "Selecciona la red")) {
      //set foco en tabPanel y widget correspondiente
      selectTab(1);
      db$cmbRed.setFocus(true);
      return false;
    } else if (!lideresIngresados()) {
      return false;
    } else if (!estadoSeleccionado()) {
      return false;
    } else if (!ciudadSeleccionada()) {
      return false;
    } else if (!zonaSeleccionada()) {
      return false;
    }
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
      mostrarMensaje(msjError);
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
    mostrarMensaje(msjError);
    combo.setSclass("textbox_error");
    return false;
  }

//TODO: MEJORACODIGO: sacar este método para una clase de utilería
  private boolean lideresIngresados() {
    //sacar número de líderes ingresados, desde variable de sesión en CtrlCelulaDatosBasicos
    nLideresUsados = (Integer) Sesion.getVariable("celula.nLideresUsados");
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
    if (posLider <= nLideresUsados) {
      return true;
    }
    return false;
  }

  /**
   * valida que se seleccionó un estado
   */
  //TODO: MEJORACODIGO: sacar este método para una clase de utilería
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

  /**
   * valida que se seleccionó una ciudad
   */
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

  /**
   * valida que se seleccionó una zona (Sector)
   */
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

  /**
   * se encarga de ingresar los datos de la celula nueva
   */
  private boolean ingresarCelula() {
    prepararCelulaInsert();
    idCelula = servicio.ingresarDatosCelula(celulaInsert);
    if (idCelula == 0) {//error, no se grabó
      return false;
    }
    ingresarLideresCelula();
    return true;
  }

  /**
   * obtiene todos los datos ingresados por el usuario en los widgets de entrada
   * y los guarda en el objeto 'celulaInsert'
   */
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

    idZona = (Integer) Sesion.getVariable("cmbZona.id");
    celulaInsert.setIdZona(idZona);

    celulaInsert.setDireccion(dir$txtDirDetallada.getValue());
    celulaInsert.setTelefono(dir$txtTelefono.getValue());

    prepararFechaApertura();
    System.out.println("CtrlCelula: fecha de apertura antes de grabar en bd=" + fechaAperturaBD);
    celulaInsert.setFechaApertura(fechaAperturaBD);
    celulaInsert.setAnfitrion(otros$txtAnfitrion.getValue());
    celulaInsert.setObservaciones(obs$txtObservaciones.getValue());
  }

  void prepararFechaApertura() {
    dateFechaApertura = otros$dateboxFechaApertura.getValue();
    if (dateFechaApertura != null) {
      Calendar cal = Util.getCalendar(dateFechaApertura);
      fechaAperturaBD = Util.getFechaMySql(dateFechaApertura);
      fechaApertura = Util.getFechaTextoDiaMesAñoAbreviado(cal);
    } else {
      fechaAperturaBD = "";
      fechaApertura = "";
    }
  }

  /**
   * busca los id's de los líderes elegidos
   */
  void getLideresCelulaElegidos() {
    idLider1 = buscarIdLider(1);
    System.out.println("CtrlCelula-lider1.id=" + idLider1);
    if (nLideresUsados >= 2) {
      idLider2 = buscarIdLider(2);
      System.out.println("CtrlCelula-lider2.id=" + idLider2);
    }
    if (nLideresUsados >= 3) {
      idLider3 = buscarIdLider(3);
      System.out.println("CtrlCelula-lider3.id=" + idLider3);
    }
    if (nLideresUsados == 4) {
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

  /**
   * prepara objeto PersonaEnCelulaInsert para grabar un líder de célula
   */
  private void ingresarLiderCelula(int idLider) {
    PersonaEnCelulaInsert persona = new PersonaEnCelulaInsert();
    persona.setIdCelula(idCelula);
    persona.setIdPersona(idLider);
    persona.setEsLiderCelula(true);
    if (servicio.ingresarLiderCelula(persona)) {
      System.out.println("CtrlCelula. Líder registrado. Célula.id=" + idCelula + ", Lider.id=" + idLider);
    }
  }

  /**
   * busca el id del líder, desde las variables de sesión   * 
   * @param nLider número de líder, {1,2,3,4}
   */
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

  /**
   * captura los valores de día y hora seleccionados por el usuario,
   * usando variables de sesión guardadas por la clase CtrlCelulaDatosBasicos
   */
  public void getDiaHoraSeleccionada() {
    diaTexto = "" + Sesion.getVariable("celula.dia.texto");
    horaTexto = "" + Sesion.getVariable("celula.hora.texto");
    diaNumero = (Integer) Sesion.getVariable("celula.dia.numero");
    horaNumero = (Integer) Sesion.getVariable("celula.hora.numero");
  }

  /** método debug
   * muestra un mensaje en label
   * @param msj 
   */
  private void mostrarMensaje(String msj) {
    etqMensaje.setValue(msj);
    etqMensaje.setVisible(true);
    System.out.println(this.getClass().toString() + msj);
  }

  /** 
   * limpia el mensaje de estado
   */
  private void ocultarMensaje() {
    etqMensaje.setVisible(false);
    etqMensaje.setValue("");
  }

  void activarEditRed() {
    db$cmbRed.setVisible(true);
    db$cmbRed.setValue(db$etqRed.getValue());
    db$tbbRed.setVisible(false);
    db$etqRed.setVisible(false);
    db$btnCancelarEditRed.setVisible(true);
    db$btnEditRed.setVisible(false);
  }

}