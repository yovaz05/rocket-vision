/**
TODO:
resolver lo de "nueva ciudad" y "nueva zona"
opción A: no usarlo por ahora
opción B: usarlo. guardar en variables de sesión, los valores ingresados.
cada controlador que use esto, debe grabar el nuevo registro? no me gusta esta opción
opción C: cuando se eliga "nuevo item", se abre una ventana popup donde se debe ingresar el valor, 
y esta ventana graba en la base de datos. la lista muestra el nuevo valor.
 */
package sig.controladores;

import cdo.sgd.controladores.Sesion;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import sig.modelo.servicios.ServicioCiudad;
import sig.modelo.servicios.ServicioEstado;
import sig.modelo.servicios.ServicioZona;

/**
 * controlador asociado a Direccion.zul
 * @author Gabriel
 */
public class CtrlDireccion extends GenericForwardComposer {

  /**
   *TODO:
   * 1. cuando eliga nueva ciudad: mensaje en la parte superior de la ventana
   * 2. mostrar item nueva ciudad en la carga inicial
   * 3. programar perdida de foco de los combos
   * 4. cuando se selecciona un combo, desplegarlo automáticamente
   * 4. quitar botones de edición, o cancelar edición
   ***/
  //widgets:
  Label etqEstado;
  Label etqCiudad;
  Label etqZona;
  Label etqDetalle;
  Label etqTelefono;
  Combobox cmbEstado;
  Combobox cmbCiudad;
  Combobox cmbZona;
  Textbox txtEstado;
  Textbox txtCiudad;
  Textbox txtZona;
  Textbox txtDetalle;
  Textbox txtTelefono;
  Comboitem itemZonaAdicional;
  Label etqMensaje;
  //widgets adicionales:
  private String NUEVO_ESTADO = "Agregar...";
  private String NUEVA_CIUDAD = "Agregar...";
  private String NUEVA_ZONA = "Agregar...";
  private String ELEGIR = "Elegir...";
  Comboitem itemAddCiudad = new Comboitem(NUEVA_CIUDAD);
  //variables de control:
  String modo; //{crear, editar}
  List listaCiudades = new ArrayList();
  List listaEstados = new ArrayList();
  List listaZonas = new ArrayList();
  //capa del modelo: base de datos
  ServicioEstado servEstado = new ServicioEstado();
  ServicioCiudad servCiudad = new ServicioCiudad();
  ServicioZona servZona = new ServicioZona();
  ListModelList modelEstados = new ListModelList();
  ListModelList modelCiudades = new ListModelList();
  ListModelList modelZonas = new ListModelList();
  //valores actuales:
  int idEstado = 1;
  int idCiudad = 1;
  int idZona = 1;
  String estado = "";
  String ciudad = "";
  String zona = "";
  String direccion = "";
  String telefono = "";
  //widgets para edición
  A btnEditEstado;
  A btnCancelarEditEstado;
  A btnEditCiudad;
  A btnCancelarEditCiudad;
  A btnEditZona;
  A btnCancelarEditZona;
  private String detalle = "";
  private boolean nuevaZona = false;
  private boolean nuevaCiudad = false;
  private boolean nuevoEstadoIngresado = false;
  private boolean nuevaCiudadIngresada = false;
  private boolean zonaNuevaIngresada = false;
  private boolean nuevoEstado = false;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    modo = "editar";
    cargarEstados();
    cargarCiudades();
    cargarZonas();
    valoresIniciales();
    setVariablesSesion();
  }

  /**
   * carga todas los estados existentes en la base de datos
   */
  private void cargarEstados() {
    listaEstados = servEstado.getNombres();
    System.out.println("CtrlDireccion- total de estados: " + listaEstados.size());

    //opción 'agregar...':
    listaEstados.add(NUEVO_ESTADO);

    modelEstados.addAll(listaEstados);
    cmbEstado.setModel(modelEstados);
  }

  void valoresIniciales() {
    cmbEstado.setValue("");
    cmbCiudad.setValue("");
    cmbZona.setValue("");
  }

  /**
   * carga todas las ciudades existentes en la base de datos
   */
  private void cargarCiudades() {
    listaCiudades = servCiudad.getNombres();
    System.out.println("ciudades: lista.size:" + listaCiudades.size());
    servCiudad.listarConsolaAll();
  }

  /**
   * carga todas las zonas existentes en la base de datos
   */
  private void cargarZonas() {
    listaZonas = servZona.getNombres();
    System.out.println("Zonas: lista.size:" + listaZonas.size());
    servZona.listarConsolaAll();
  }

  /** método debug
   * muestra las ciudades del estado 'Lara'
   */
  //NO USADO
  /*
  public void mostrarCiudadesPorDefecto() {
  System.out.println("CtrlDireccion. mostrarCiudadesPorDefecto - INICIO");
  idEstado = 1; //Lara
  listaCiudades = servCiudad.getCiudadesPorEstado(idEstado);
  listaCiudades.add(NUEVA_CIUDAD);
  modelCiudades = new ListModelList(listaCiudades);
  cmbCiudad.setModel(modelCiudades);
  }
   * 
   */
  /**
   * maneja el evento de seleccionar un estado de la lista
   * cuando se selecciona un estado, se buscan las ciudades de ese estado,
   * y se muestran en el combobox de ciudades
   */
  //DOING: manejar opción de nuevo estado
  public void onSelect$cmbEstado() {
    estado = cmbEstado.getValue();
    //**System.out.println("CtrlDireccion. estado seleccionado: " + estado);
    mostrarValorEstado();
    desactivarEditEstado();

    //limpiar las otras variables por si hay valores seleccionados
    ciudad = "";
    zona = "";

    if (estado.equals(NUEVO_ESTADO)) {
      System.out.println("CtrlDireccion. se seleccionó 'nuevo estado'");
      desactivarEditEstado();
      activarIngresoNuevoEstado();
      limpiarListaCiudad();
    } else {
      desactivarIngresoNuevoEstado();
      System.out.println("CtrlDireccion -> Estado elegido: " + estado);
      txtEstado.setVisible(false);
      mostrarValorEstado();
      cargarCiudadesPorEstado();
      //al cambiar el estado se obliga a cambiar la ciudad
      activarEditCiudad(); //al cambiar el estado, se obliga a cambiar la ciudad    
    }
  }

  void activarIngresoNuevoEstado() {
    nuevoEstado = true;
    txtEstado.setValue("");
    txtEstado.setVisible(true);
    txtEstado.setFocus(true);
    txtEstado.select();
  }

  void desactivarIngresoNuevoEstado() {
    nuevoEstado = false;
    txtEstado.setVisible(false);
  }

  public void onOK$txtEstado() {
    procesarNuevoEstado();
  }

  public void onBlur$txtEstado() {
    procesarNuevoEstado();
  }

  /**
   * procesar nueva ciudad, ingresada a través de txtCiudad
   */
  private void procesarNuevoEstado() {
    desactivarIngresoNuevoEstado();
    if (!estadoNuevoIngresado()) {
      estado = ELEGIR;
      ciudad = ELEGIR;
      zona = ELEGIR;
    } else {//zona ingresada
      //forzar ingreso de nueva ciudad:
      ciudad = NUEVA_CIUDAD;
      activarIngresoNuevaCiudad();
      /**
       * TODO: grabar el nuevo estado en la base de datos
       **/
    }
    mostrarValorEstado();
    mostrarValorCiudad();
    mostrarValorZona();
  }

  /**
   * devuelve true si se ingresó el nombre del nuevo estado
   * @return 
   */
  //TODO: chequear si el estado nuevo existe en la base de datos
  private boolean estadoNuevoIngresado() {
    estado = txtEstado.getValue();
    return (estado.isEmpty()) ? false : true;
  }

  /**
   * carga las ciudades del estado seleccionado
   * y llena el combo de ciudades
   */
  private void cargarCiudadesPorEstado() {
    idEstado = servEstado.getIdEstado(estado);
    //**System.out.println("CtrlDireccion. id de estado: " + idEstado);
    listaCiudades = servCiudad.getCiudadesPorEstado(idEstado);

    //opción 'agregar ciudad':
    listaCiudades.add(NUEVA_CIUDAD);

    modelCiudades = new ListModelList(listaCiudades);
    cmbCiudad.setModel(modelCiudades);
  }

  /**
   * maneja el evento de seleccionar un estado de la lista
   * cuando se selecciona una ciudad, se buscan las zonas correspondientes
   * y se muestran en la lista de zonas
   */
  public void onSelect$cmbCiudad() {
    ciudad = cmbCiudad.getValue();
    mostrarValorCiudad();
    desactivarEditCiudad();

    if (ciudad.equals(NUEVA_CIUDAD)) {
      System.out.println("CtrlDireccion. se seleccionó 'nueva ciudad'");
      desactivarEditCiudad();
      activarIngresoNuevaCiudad();
      limpiarListaZona();
    } else {
      desactivarIngresoNuevaCiudad();
      System.out.println("CtrlDireccion -> Ciudad elegida: " + ciudad);
      txtCiudad.setVisible(false);
      mostrarValorCiudad();
      cargarZonasDeCiudad();
      //al cambiar el estado se obliga a cambiar la zona
      zona = "";
      activarEditZona(); //al cambiar el estado se obliga a cambiar la ciudad    
    }
  }

  private void limpiarListaCiudad() {
    listaCiudades = new ArrayList();
    modelCiudades = new ListModelList(listaCiudades);
    cmbCiudad.setModel(modelCiudades);
  }

  private void limpiarListaZona() {
    listaZonas = new ArrayList();
    modelZonas = new ListModelList(listaZonas);
    cmbZona.setModel(modelZonas);
  }

  void activarIngresoNuevaCiudad() {
    nuevaCiudad = true;
    txtCiudad.setValue("");
    txtCiudad.setVisible(true);
    txtCiudad.setFocus(true);
    txtCiudad.select();
  }

  void desactivarIngresoNuevaCiudad() {
    nuevaCiudad = false;
    txtCiudad.setVisible(false);
  }

  /**
   * carga las zonas de la ciudad seleccionada
   * y llena el combo de zonas
   */
  private void cargarZonasDeCiudad() {
    idCiudad = servCiudad.getIdCiudad(ciudad);
    System.out.println("CtrlDireccion -> id Ciudad: " + idCiudad);
    listaZonas = servZona.getZonasPorCiudad(idCiudad);

    //item "agregar zona..."
    listaZonas.add(NUEVA_ZONA);

    modelZonas = new ListModelList(listaZonas);
    cmbZona.setModel(modelZonas);
    cmbZona.setValue(NUEVA_ZONA);
  }

  /** en desarrollo
   * maneja el evento de seleccionar una zona
   * cuando se selecciona una zona, se buscan las zonas correspondientes
   * y se muestran en la lista de zonas
   */
  public void onSelect$cmbZona() {
    zona = cmbZona.getValue();
    mostrarValorZona();
    System.out.println("CtrlDireccion -> zona elegida: " + zona);

    if (zona.equals(NUEVA_ZONA)) {
      //manejo de evento si selecciona "nueva zona":
      desactivarEditZona();
      activarIngresoNuevaZona();
    } else {
      desactivarIngresoNuevaZona();
      //*System.out.println("NO");
      //- txtZona.setVisible(false);

      //capturar valores:
      idZona = servZona.getIdZona(zona);
      setVariableSesionZona();
      System.out.println("CtrlDireccion.Zona.id=" + idZona);
      //activar edición de siguiente campo:
      activarEditDetalle();
    }
  }

  public void onOK$txtCiudad() {
    procesarNuevaCiudad();
  }

  public void onBlur$txtCiudad() {
    procesarNuevaCiudad();
  }

  /**
   * procesar nueva ciudad, ingresada a través de txtCiudad
   */
  private void procesarNuevaCiudad() {
    desactivarIngresoNuevaCiudad();
    if (!ciudadNuevaIngresada()) {
      ciudad = ELEGIR;
      zona = ELEGIR;
    } else {//zona ingresada
      //forzar ingreso de nueva zona:
      zona = NUEVA_ZONA;
      activarIngresoNuevaZona();
      /**
       * TODO: grabar la nueva ciudad en la base de datos
       **/
    }
    mostrarValorCiudad();
    mostrarValorZona();
  }

  /**
   * devuelve true si ingresó el nombre de la nueva ciudad
   * @return 
   */
  //TODO: validar si ciudad nueva no existe en la base de datos
  private boolean ciudadNuevaIngresada() {
    ciudad = txtCiudad.getValue();
    return (ciudad.isEmpty()) ? false : true;
  }

  public void onOK$txtZona() {
    procesarNuevaZona();
  }

  public void onBlur$txtZona() {
    procesarNuevaZona();
  }

  private void procesarNuevaZona() {
    desactivarIngresoNuevaZona();
    if (!zonaNuevaIngresada()) {
      zona = ELEGIR;
    }
    mostrarValorZona();
    /**
     * TODO: grabar la nueva zona en la base de datos
     **/
    //activar edición de próximo campo
    activarEditDetalle();
  }
//TODO: validar si zona nueva no existe en la base de datos

  /**
   * devuelve true si ingresó el nombre de la zona nueva
   * @return 
   */
  private boolean zonaNuevaIngresada() {
    zona = txtZona.getValue();
    return (zona.isEmpty()) ? false : true;
  }

  /**
   * establece la variables de sesión de hora
   * para ser usado por los otros controladores
   */
  public void setVariableSesionZona() {
    Sesion.setVariable("cmbZona.id", idZona);
  }

  /**
   * guardar variables de sesión con valores por defecto
   * para ser usado por los otros controladores
   */
  private void setVariablesSesion() {
    setVariableSesionZona();
  }

  /**
   * click en la etiqueta de estado activa la edición
   */
  public void onClick$etqEstado() {
    desactivarEditCiudad();//por si está activado
    desactivarEditZona();//por si está activado
    activarEditEstado();
  }

  /**
   * click en la etiqueta de ciudad activa la edición
   */
  public void onClick$etqCiudad() {
    desactivarEditEstado();//por si está activado
    desactivarEditZona();//por si está activado
    activarEditCiudad();
  }

  /**
   * click en la etiqueta de ciudad activa la edición
   */
  public void onClick$etqZona() {
    desactivarEditEstado();//por si está activado
    desactivarEditCiudad();//por si está activado
    activarEditZona();
  }

  public void onBlur$cmbEstado() {
    desactivarEditEstado();
  }

  public void onBlur$cmbCiudad() {
    desactivarEditCiudad();
  }

  public void onBlur$cmbZona() {
    if (!nuevaZona) {
      desactivarEditZona();
    }
  }

  /**
   * botón de edición de estado
   */
  //NO USADO
  /*
  public void onClick$btnEditEstado() {
  activarEditEstado();
  }
   */
  /**
   * botón para cancelar la edición de estado
   */
  public void onClick$btnCancelarEditEstado() {
    desactivarEditEstado();
  }

  /**
   * activar edición de estado
   */
  void activarEditEstado() {
    etqEstado.setVisible(false);
    cmbEstado.setValue(etqEstado.getValue());
    cmbEstado.setVisible(true);
    cmbEstado.open();
    cmbEstado.setFocus(true);
    cmbEstado.select();
    //- btnEditEstado.setVisible(false);
    //- btnCancelarEditEstado.setVisible(true);
  }

  /**
   * activar edición de estado
   */
  void desactivarEditEstado() {
    cmbEstado.setVisible(false);
    etqEstado.setVisible(true);
    //- btnCancelarEditEstado.setVisible(false);
    //- btnEditEstado.setVisible(true);
  }

  /**
   * botón de edición de Ciudad
   */
  //NO USADO
  /*
  public void onClick$btnEditCiudad() {
  activarEditCiudad();
  }
   */
  /**
   * botón para cancelar la edición de Ciudad
   */
  public void onClick$btnCancelarEditCiudad() {
    desactivarEditCiudad();
  }

  /**
   * activar edición de Ciudad
   */
  void activarEditCiudad() {
    etqCiudad.setVisible(false);
    cmbCiudad.setValue(ciudad);
    cmbCiudad.setVisible(true);
    cmbCiudad.setFocus(true);
    cmbCiudad.select();
    cmbCiudad.open();
    //- btnEditCiudad.setVisible(false);
    //- btnCancelarEditCiudad.setVisible(true);
  }

  /**
   * activar edición de Ciudad
   */
  void desactivarEditCiudad() {
    //- btnCancelarEditCiudad.setVisible(false);
    cmbCiudad.setVisible(false);
    //% txtCiudad.setVisible(false);
    etqCiudad.setVisible(true);
    //- btnEditCiudad.setVisible(true);
  }

  /**
   * botón de edición de Zona
   */
  //NO USADO
  /*
  public void onClick$btnEditZona() {
  activarEditZona();
  }
   */
  /**
   * botón para cancelar la edición de Zona
   */
  public void onClick$btnCancelarEditZona() {
    desactivarIngresoNuevaZona();
    zona = NUEVA_ZONA;
    mostrarValorZona();
  }

  /**
   * activar edición de Zona
   */
  void activarEditZona() {
    etqZona.setVisible(false);
    cmbZona.setValue(zona);
    cmbZona.setVisible(true);
    cmbZona.open();
    cmbZona.setFocus(true);
    cmbZona.select();
    //- btnEditZona.setVisible(false);
  }

  /**
   * activar edición de Zona
   */
  void desactivarEditZona() {
    etqZona.setVisible(true);
    cmbZona.setVisible(false);
    txtZona.setVisible(false);
    //- btnCancelarEditZona.setVisible(false);
    //- btnEditZona.setVisible(true);
  }

  boolean modoEditar() {
    return modo.equals("editar");
  }

  /**
   * activación de ingreso de nueva zona,
   * mediante un textbox
   **/
  private void activarIngresoNuevaZona() {
    nuevaZona = true;
    txtZona.setValue("");
    txtZona.setVisible(true);
    txtZona.setFocus(true);
    //- btnCancelarEditZona.setVisible(true);
  }

  void desactivarIngresoNuevaZona() {
    nuevaZona = false;
    txtZona.setVisible(false);
    //- btnCancelarEditZona.setVisible(false);
  }

  private void mostrarValorEstado() {
    etqEstado.setValue(estado);
    etqEstado.setVisible(true);
  }

  private void mostrarValorCiudad() {
    etqCiudad.setValue(ciudad);
    etqCiudad.setVisible(true);
  }

  private void mostrarValorZona() {
    etqZona.setValue(zona);
    etqZona.setVisible(true);
  }

  public void onClick$etqDetalle() {
    activarEditDetalle();
  }
  
  /**
   * activa la edición de detalle
   */
  private void activarEditDetalle() {
    etqDetalle.setVisible(false);
    txtDetalle.setValue(etqDetalle.getValue());
    txtDetalle.setVisible(true);
    txtDetalle.setFocus(true);
  }

  /**
   * desactiva la edición de detalle
   * y muestra el valor actual
   */
  private void desactivarEditDetalle() {
    txtDetalle.setVisible(false);
    etqDetalle.setFocus(true);
    etqDetalle.setVisible(true);
  }

  public void onOK$txtDetalle() {
    procesarDetalle();
    desactivarEditDetalle();
  }

  public void onBlur$txtDetalle() {
    procesarDetalle();
    desactivarEditDetalle();
  }

  private void procesarDetalle() {
    detalle = txtDetalle.getValue();
    //TODO: alguna validación que sea necesaria
    etqDetalle.setValue(detalle);
  }

  private void activarEditTelefono() {
    etqTelefono.setVisible(false);
    txtTelefono.setValue(etqTelefono.getValue());
    txtTelefono.setVisible(true);
    txtTelefono.setFocus(true);
  }

  private void desactivarEditTelefono() {
    txtTelefono.setVisible(false);
    etqTelefono.setFocus(true);
    etqTelefono.setVisible(true);
  }

  public void onClick$etqTelefono() {
    activarEditTelefono();
  }

  public void onOK$txtTelefono() {
    procesarTelefono();
    desactivarEditTelefono();
  }

  public void onBlur$txtTelefono() {
    procesarTelefono();
    desactivarEditTelefono();
  }

  private void procesarTelefono() {
    telefono = txtTelefono.getValue();
    //TODO: alguna validación que sea necesaria
    etqTelefono.setValue(telefono);
  }
}
/**
 * TODO:
 * 1. grabar los nuevos valores en la base de datos: estado, ciudad, zona
 * 2. manejar los eventos para valores nuevos:
 *  si es nuevo estado, no se listan las ciudades ni zonas
 *  si es nueva ciudad, no se listan las zonas
 */
