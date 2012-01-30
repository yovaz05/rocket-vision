package rocket.controladores.lider;

import rocket.controladores.general.CtrlVista;
import rocket.controladores.general.Sesion;
import rocket.controladores.general.Vistas;
import rocket.modelo.bd.util.LiderUtil;
import java.util.Date;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import rocket.controladores.general.Constantes;
import rocket.controladores.general.Modo;
import rocket.modelo.servicios.ServicioPersona;

/**
 * controlador asociado a LiderSimulador.zul
 * @author Gabriel
 */
public class CtrlLider extends GenericForwardComposer {

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
  Textbox db$txtCedula;
  Textbox db$txtNombre;
  Label db$etqCedula;
  Label db$etqNombre;
  Label db$etqTelefono;
  Label db$etqEmail;
  Row db$filaRed;
  Label db$etqRed;
  Combobox db$cmbRed;
  A db$tbbRed;
  A db$btnEditRed, db$btnCancelarEditRed;
  /*+serán usados en próximas versiones:
  Label db$etqLider1;
  Label db$etqLider2;
  Label db$etqLider3;
  Label db$etqLider4;
  Combobox db$cmbLider1, db$cmbLider2, db$cmbLider3, db$cmbLider4;
  Div db$opcionLider1, db$opcionLider2, db$opcionLider3, db$opcionLider4;
  Div db$divLideresEdit;
  A db$tbbLider1, db$tbbLider2, db$tbbLider3, db$tbbLider4;
  A db$btnEditLideres, db$btnCancelarEditLideres;
   */
  //pestaña "Dirección"
  Column dir$colData;
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
  //pestaña "Observaciones"
  Label obs$etqObservaciones;
  Textbox obs$txtObservaciones;
  //variables de control:
  int tabSeleccionado;
  String modo;  //modo={new,edicion,ver,imprimir}
  String titulo = "Líder";
  CtrlVista ctrlVista = new CtrlVista();
  private String descripcionLider;
  String fechaApertura = "";
  Date dateFechaApertura;
  int nLideres = 0;
  //gestión de datos
  //data en modificación:
  String cedula = "";
  String nombre = "";
  int idRed = 1;
  String nombreRed = "";
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
  LiderUtil lider = new LiderUtil();
  ServicioPersona servicio = new ServicioPersona();
  private int idLider;
  /*+
  //IDs de personas elegidas como líderes de célula
  int idLider1 = 0;
  int idLider2 = 0;
  int idLider3 = 0;
  int idLider4 = 0;
   */
  private Row db$rowTelefono;
  private Row db$rowCorreo;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    System.out.println("CtrlLider.INICIO()");
    modo = Sesion.getModo();
    if (modo == null) {
      modo = Modo.INGRESAR;
    }
    if (modo.equals(Modo.EDICION_DINAMICA)) {
      System.out.println("CtrlLider.modoActual = ver");
      getID();
      System.out.println("CtrlLider.inicio().id = " + idLider);
      buscarData();
      setVariablesSesion();
      mostrarData();
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
    Sesion.setVistaActual(Vistas.LIDER);
    Sesion.setModo(modo);
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }

  /**
   * recupera variable de sesión 'idLider'
   * que fue seteada por la vista llamante
   * sólo en modo ver
   */
  //TODO: MEJORA: evaluar forma de obtener este valor si viniera en el URL
  private void getID() {
    try {
      idLider = (Integer) Sesion.getVariable("idLider");
      System.out.println("CtrlLider.getId:id=" + idLider);
    } catch (Exception e) {
      System.out.println("CtrlLider -> ERROR: parámetro idLider nulo... "
              + e);
      idLider = 0;
    }
  }

  /**
   * obtiene la célula de la base de datos
   * con el id de la célula
   */
  void buscarData() {
    lider = servicio.getPersona(idLider);
    if (lider == null) {
      System.out.println("CtrlLider.buscarLiderBD: NULL");
    } else {
      System.out.println("CtrlLider.Lider=" + lider.toString());
    }
    System.out.println("CtrlLider.buscarData - nLideres=" + nLideres);
  }

  /**
   * guarda las variables de sesión
   * para ser usadas por otros controladores
   */
  private void setVariablesSesion() {
    idRed = lider.getIdRed();
    nombreRed = lider.getNombreRed();
    /*+
    nLideres = lider.getNumeroLideres();
    idLider1 = lider.getIdLider1();
    idLider2 = lider.getIdLider2();
    idLider3 = lider.getIdLider3();
    idLider4 = lider.getIdLider4();
     */
    idEstado = lider.getDireccion().getIdEstado();
    idCiudad = lider.getDireccion().getIdCiudad();
    idZona = lider.getDireccion().getIdZona();
    nombreEstado = lider.getDireccion().getEstado();
    nombreCiudad = lider.getDireccion().getCiudad();
    nombreZona = lider.getDireccion().getZona();
    //TODO: poner en sers
    Sesion.setVariable("idLider", idLider);
    Sesion.setVariable("lider.idRed", idRed);
    Sesion.setVariable("lider.nombreRed", nombreRed);
    /*
    Sesion.setVariable("Lider.nLideres", nLideres);
    Sesion.setVariable("Lider.idLider1", idLider1);
    Sesion.setVariable("Lider.idLider2", idLider2);
    Sesion.setVariable("Lider.idLider3", idLider3);
    Sesion.setVariable("Lider.idLider4", idLider4);
     */
    Sesion.setVariable("idEstado", idEstado);
    Sesion.setVariable("idCiudad", idCiudad);
    Sesion.setVariable("idZona", idZona);
    Sesion.setVariable("lider.nombreEstado", nombreEstado);
    Sesion.setVariable("lider.nombreCiudad", nombreCiudad);
    Sesion.setVariable("lider.nombreZona", nombreZona);
  }

  /**
   * muestra los datos actuales de la célula, desde la base de datos
   */
  public void mostrarData() throws InterruptedException {
    if (lider == null) {
      mensaje("Error recuperando datos del líder");
      return;
    }
    //llenar widgets con la data
    //datos básicos
    cedula = lider.getCedula();
    db$etqCedula.setValue(cedula);
    nombre = lider.getNombre();
    if (nombre.isEmpty()) {
      //si no hay valor, mostrar una etiqueta para permitir edición
      nombre = Constantes.VALOR_EDITAR;
    }
    db$etqNombre.setValue(nombre);

    String telefono = lider.getTelefono();
    if (telefono.isEmpty()) {
      //si no hay valor, mostrar una etiqueta para permitir edición
      telefono = Constantes.VALOR_EDITAR;
    }
    db$etqTelefono.setValue(telefono);

    String email = lider.getEmail();
    if (email.isEmpty()) {
      //si no hay valor, se muestra una etiqueta para permitir edición
      email = Constantes.VALOR_EDITAR;
    }
    db$etqEmail.setValue(email);

    db$etqRed.setValue(lider.getNombreRed());
    //usado en próximas versiones:
    //+db$tbbRed.setLabel(lider.getNombreRed());

    //dirección:
    idZona = lider.getDireccion().getIdZona();
    String estado = "", ciudad = "", zona = "";
    if (idZona == 1) {
      estado = Constantes.VALOR_EDITAR;
    } else {
      estado = lider.getDireccion().getEstado();
      ciudad = lider.getDireccion().getCiudad();
      zona = lider.getDireccion().getZona();
      dir$etqCiudad.setValue(ciudad);
      dir$etqZona.setValue(zona);
    }
    dir$etqEstado.setValue(estado);

    String detalle = lider.getDireccion().getDirDetallada();
    if (detalle.isEmpty()) {
      detalle = Constantes.VALOR_EDITAR;
    }
    dir$etqDetalle.setValue(detalle);

    String telefonoHabit = lider.getDireccion().getTelefono();
    if (telefonoHabit.isEmpty()) {
      telefonoHabit = Constantes.VALOR_EDITAR;
    }
    dir$etqTelefono.setValue(telefonoHabit);

    String observaciones = lider.getObservaciones();
    if (observaciones.isEmpty()) {
      observaciones = Constantes.VALOR_EDITAR;
    }
    obs$etqObservaciones.setValue(observaciones);

    //crear descripción de la célula, para el título:
    //TODO: si hay direcció, agregarsela al título
    //+descripcionLider = generarDescripcionLider(lider.getCedula(), lider.getDireccionCorta());
    descripcionLider = lider.getNombre();


    //TODO: configurar parámetros para navegación dinámica:
    /*
    final int idRed = lider.getIdRed();
    final int idLider1 = lider.getIdLider1();
    final int idLider2 = lider.getIdLider2();
    final int idLider3 = lider.getIdLider3();
    final int idLider4 = lider.getIdLider4();
    String lider1 = lider.getNombreLider1();
    String lider2 = lider.getNombreLider2();
    String lider3 = lider.getNombreLider3();
    String lider4 = lider.getNombreLider4();
    
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
    System.out.println("CtrlLider -> idLider = " + idLider1);
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
    System.out.println("CtrlLider -> idLider = " + idLider2);
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
      mostrarWidgetsRestantes(false);
      mostrarWidgetsViewLink(false);
      mostrarWidgetsEdit(false);
      //- mostrarOpcionLider1();
      verBotonesEdicion(false);
      selectTab(1);
      db$txtCedula.setFocus(true);
      mensaje("Ingresa la cédula y presiona 'Enter'");
      //- btnIngresarReporte.setVisible(false);
    } else if (modo.equals("edicion-dinamica")) {
      tituloVentana.setValue(titulo + ": " + descripcionLider);
      mostrarWidgetsNew(false);
      mostrarWidgetsRestantes(true);
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
      tituloVentana.setValue(titulo + ": " + descripcionLider + " » Editando");
      verColumnasData(true);
      mostrarColumnasVisualizacion(false);
      setFocoEdicion();
      //- btnIngresarReporte.setVisible(false);
      }
       * 
       */
      //modo editar, modificando, sólo mostrará columnas con botones de edición
    } else if (modo.equals("editar")) {
      tituloVentana.setValue(titulo + ": " + descripcionLider + " » Editando");
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
    Sesion.setVariable("idLider", 0);
    mensaje("Ingresa el código:");
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
      db$txtCedula.setFocus(true);
      //-db$txtCedula.select();
    } else if (tabSeleccionado == 2) {
      dir$cmbEstado.setFocus(true);
    } else if (tabSeleccionado == 4) {
      obs$txtObservaciones.setFocus(true);
      //TODO: ubicar cursor al final del texto, si se está en modo editar
    }
  }

  /*+se usará en próximas versiones:
  public void onClick$btnIngresarReporte() {
  //actualizar variables de sesión:
  Sesion.setVistaActual(Vistas.lider);
  Sesion.setVistaSiguiente(Vistas.REPORTE_Lider);
  Sesion.setModo("new");  //modo insertLider
  //envia click al btnControl de CtrlBarraMenu, para que cambie la vista
  ctrlVista.forzarCambioVista_btnControl();
  }
   */
  /**
   * muestra/oculta los widgets de visualización que no sirven para edición
   */
  private void mostrarWidgetsViewLink(boolean status) {
    db$tbbRed.setVisible(status);
    /*
    db$tbbLider1.setVisible(status);
    db$tbbLider2.setVisible(status);
    db$tbbLider3.setVisible(status);
    db$tbbLider4.setVisible(status);
     */
  }

  /**
   * muestra/oculta los widgets de visualización que no sirven para edición
   */
  private void mostrarEtiquetasLider(boolean status) {
    /*
    db$etqLider1.setVisible(status);
    db$etqLider2.setVisible(status);
    db$etqLider3.setVisible(status);
    db$etqLider4.setVisible(status);
     */
  }

  /**
   * muestra/oculta las etiquetas que permiten la edición
   */
  private void mostrarWidgetsEdit(boolean status) {
    //datos básicos:
    db$etqCedula.setVisible(status);
    db$etqNombre.setVisible(status);
    db$etqRed.setVisible(status);
    db$etqTelefono.setVisible(status);
    db$etqEmail.setVisible(status);
    /*
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
     */
    //dirección
    dir$etqEstado.setVisible(status);
    dir$etqDetalle.setVisible(status);
    dir$etqTelefono.setVisible(status);
    //observaciones    
    obs$etqObservaciones.setVisible(status);
  }

  /**
   * muestra los valores actuales en los widgets de entrada (captura de datos)
   */
  private void getValoresEdit() {
    db$etqRed.setValue(lider.getNombreRed());

    /*?
    //líderes
    if (seUsaLider(1)) {
    db$etqLider1.setValue(lider.getNombreLider1());
    }
    if (seUsaLider(2)) {
    db$etqLider2.setValue(lider.getNombreLider2());
    }
    if (seUsaLider(3)) {
    db$etqLider3.setValue(lider.getNombreLider3());
    }
    if (seUsaLider(4)) {
    db$etqLider4.setValue(lider.getNombreLider4());
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
    //-db$etqCedula.setValue(cedula);
    //- db$etqNombre.setValue(Constantes.VALOR_EDITAR);
    db$tbbRed.setLabel(nombreRed);
    db$etqTelefono.setValue(Constantes.VALOR_EDITAR);
    db$etqEmail.setValue(Constantes.VALOR_EDITAR);
    dir$etqEstado.setValue(Constantes.VALOR_EDITAR);
    dir$etqCiudad.setValue("");
    dir$etqZona.setValue("");
    dir$etqDetalle.setValue(Constantes.VALOR_EDITAR);
    dir$etqTelefono.setValue(Constantes.VALOR_EDITAR);
    obs$etqObservaciones.setValue(Constantes.VALOR_EDITAR);
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
   * se encarga de crear la célula
   * setear la variable de resultado que será usada por barraMenu para los mensajes
   * y cambiar modo de edicion dinámica
   */
  //TODO: MEJORA CODIGO: cambiar nombre de botón a btnCrear
  public void onClick$btnGuardar() {
    if (modo.equals("new")) {
      if (ingresarLider()) {
        //-Sesion.setVariable("resultOperacion", 1);//indica éxito
        Sesion.setModo(modo = "edicion-dinamica");
        mostrarValoresViewRecienCreada();
        actualizarEstado();
        //TODO: crear acceso al líder        
        //DOING
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
  boolean ingresarLider() {
    ocultarMensaje();
    if (!camposObligatoriosIngresados()) {
      return false;
    }
    //ingresar nueva célula en la base de datos      
    if (ingresarLiderBD()) {//nueva célula registrada con éxito
      System.out.println("CtrlLider. Nueva Lider ingresada. código:" + cedula);
      //mostrar descripción en titulo:
      //TODO: agregar la zona a la descripción
      //descripcionLider = generarDescripcionLider(LiderInsert.getCedula(), dir$cmbZona.getValue());
      //modo = "ver";
      mensaje("Líder ingresado. Puedes agregar el resto de la información");
      descripcionLider = nombre;
      //**System.out.println("CtrlLider.Célula creada con éxito:");
      //**System.out.println("id=" + idLider);
      return true;
    }
    //error:
    mensaje("Error creando el líder. Vuelve a intentar.");
    return true;
  }

  /**
   * se encarga de crea un líder en la base de datos,
   * con los datos básicos: código, nombre, id-red
   */
  private boolean ingresarLiderBD() {
    cedula = db$txtCedula.getValue();
    nombre = db$txtNombre.getValue();
    idRed = getIdRed();
    //**System.out.println("CtrlLider.crearLider.Cedula=" + cedula);
    System.out.println("CtrlLider.crearLider.idRed=" + idRed);
    idLider = servicio.ingresarLider(cedula, nombre, idRed);
    //**System.out.println("CtrlLider.crearLider.idLider=" + idLider);
    Sesion.setVariable("idLider", idLider);
    Sesion.setVariable("Lider.idRed", idRed);
    return (idLider != 0) ? true : false;
  }

  /**
   * Valida todos los campos obligatorios para crear una célula (que deben contener un valor)
   * @return true si todos los campos obligatorios son válidos
   */
  boolean camposObligatoriosIngresados() {
    //TODO: mejorar, validación no hace falta, ya que se hace escalada
    /*
    if (!textboxIngresado(db$txtCedula, "Ingresa la cédula")) {
    //set foco en tabPanel y widget correspondiente
    selectTab(1);
    db$txtCedula.setFocus(true);
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
    }
     */
    return true;
  }

  /**
   * valida el valor de un textbox
   * @param widget el textbox a evaluar
   * @return el resultado, true si no hay error, false si hay error
   */
  //TODO: MEJORACedula: sacar este método para una clase de utilería
  //TODO: quitar método, no usado
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
  //TODO: MEJORACedula: sacar este método para una clase de utilería
  //TODO: quitar método, no usado
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

  /*+
   * será usado en próximas versiones
   * @return 
   */
  /*
  private boolean lideresIngresados() {
  //sacar número de líderes ingresados, desde variable de sesión en CtrlLiderDatosBasicos
  nLideres = (Integer) Sesion.getVariable("Lider.nLideres");
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
   */
  /**
   * determina si un líder adicional (mayor a 2) es usado N: {3 o 4}
   * @param posLider
   * @return 
   */
  /*+usado en próximas versiones
  private boolean seUsaLider(int posLider) {
  if (posLider <= nLideres) {
  return true;
  }
  return false;
  }
   */
  /**
   * MUESTRA los widgets de los campos obligatorios para el registro
   */
  private void mostrarWidgetsNew(boolean visible) {
    db$txtCedula.setVisible(visible);
    db$txtNombre.setVisible(visible);
    //TODO: se mostrará el campo de nombre deshabilitado, se habilita cuando ingresa la cédula
    if (visible) {
      db$txtNombre.setDisabled(true);
    }
    db$cmbRed.setVisible(visible);
    //TODO: se mostrará el campo de red deshabilitado, se habilita cuando ingresa el nombre
    if (visible) {
      db$cmbRed.setDisabled(true);
    }
  }

  private int getIdRed() {
    return (Integer) Sesion.getVariable("lider.idRed");
  }

  /**
   * mostrar widgets no usados en 'ingresar'
   * @param visible 
   */
  private void mostrarWidgetsRestantes(boolean visible) {
    db$rowTelefono.setVisible(visible);
    db$rowCorreo.setVisible(visible);
    tabDir.setVisible(visible);
    //+ tabObs.setVisible(visible);
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
//TODO: MEJORACedula: sacar este método para una clase de utilería
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
 * se encarga de ingresar los datos de la lider nueva
 * con TODOS LOS DATOS
 */
/*
private boolean ingresarLider() {
prepararLiderInsert();
idLider = servicio.insertLider(LiderInsert);
if (idLider == 0) {//error, no se grabó
return false;
}
ingresarLideresLider();
return true;
}
 */
/**
 * obtiene todos los datos ingresados por el usuario en los widgets de entrada
 * y los guarda en el objeto 'LiderInsert'
 */
/*
private void prepararLiderInsert() {
LiderInsert = new LiderInsert();
//datos básicos:
LiderInsert.setCedula(db$txtCedula.getValue());
LiderInsert.setNombre(db$txtNombre.getValue());

idRed = (Integer) Sesion.getVariable("cmbRed.id");
LiderInsert.setIdRed(idRed);

//TODO: en desarrollo todos los valores siguientes
//DOING: obteniendo valores día y hora seleccionados por el usuario:
getDiaHoraSeleccionada();
LiderInsert.setDia(diaNumero);
LiderInsert.setHora(horaNumero);

//obtener valor de la zona por si fue cambiado por en la vista 'Direccion'
idZona = (Integer) Sesion.getVariable("cmbZona.id");
LiderInsert.setIdZona(idZona);

LiderInsert.setDireccion(dir$txtDetalle.getValue());
LiderInsert.setTelefono(dir$txtTelefono.getValue());

prepararFechaApertura();
System.out.println("CtrlLider: fecha de apertura antes de grabar en bd=" + fechaAperturaBD);
LiderInsert.setFechaApertura(fechaAperturaBD);
LiderInsert.setAnfitrion(otros$txtAnfitrion.getValue());
LiderInsert.setObservaciones(obs$txtObservaciones.getValue());
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
void getLideresLiderElegidos() {
idLider1 = buscarIdLider(1);
System.out.println("CtrlLider-lider1.id=" + idLider1);
if (nLideres >= 2) {
idLider2 = buscarIdLider(2);
System.out.println("CtrlLider-lider2.id=" + idLider2);
}
if (nLideres >= 3) {
idLider3 = buscarIdLider(3);
System.out.println("CtrlLider-lider3.id=" + idLider3);
}
if (nLideres == 4) {
idLider4 = buscarIdLider(4);
System.out.println("CtrlLider-lider4.id=" + idLider4);
}
}

void ingresarLideresLider() {
getLideresLiderElegidos();
if (idLider1 != 0) {
ingresarLiderLider(idLider1);
}
if (idLider2 != 0) {
ingresarLiderLider(idLider2);
}
if (idLider3 != 0) {
ingresarLiderLider(idLider3);
}
if (idLider4 != 0) {
ingresarLiderLider(idLider4);
}
}
 */
/**
 * agregar lider a la célula
 * @param idLider id del líder
 */
/*
private void ingresarLiderLider(int idLider) {
if (servicio.agregarLiderLider(idLider)) {
System.out.println("CtrlLider. Líder registrado. Célula.id=" + idLider + ", Lider.id=" + idLider);
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
id = (Integer) Sesion.getVariable("lider.lider1.id");
} else if (nLider == 2) {
id = (Integer) Sesion.getVariable("lider.lider2.id");
} else if (nLider == 3) {
id = (Integer) Sesion.getVariable("lider.lider3.id");
} else if (nLider == 4) {
id = (Integer) Sesion.getVariable("lider.lider4.id");
}
return id;
}
 */
/**
 * captura los valores de día y hora seleccionados por el usuario,
 * usando variables de sesión guardadas por la clase CtrlLiderDatosBasicos
 */
/*
public void getDiaHoraSeleccionada() {
diaTexto = "" + Sesion.getVariable("lider.dia.texto");
horaTexto = "" + Sesion.getVariable("lider.hora.texto");
diaNumero = (Integer) Sesion.getVariable("lider.dia.numero");
horaNumero = (Integer) Sesion.getVariable("lider.hora.numero");
}
 */
/*
private void mostrarOpcionLider1() {
db$opcionLider1.setVisible(true);
}
//objeto para insertar nueva célula
LiderInsert LiderInsert;
 */
