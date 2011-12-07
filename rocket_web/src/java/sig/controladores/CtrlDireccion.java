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
  Textbox txtCiudad;
  Textbox txtZona;
  Textbox txtDetalle;
  Textbox txtTelefono;
  Comboitem itemZonaAdicional;
  Label etqMensaje;
  //widgets adicionales:
  String etiquetaNuevoEstado = "Agregar...";
  String etiquetaNuevaCiudad = "Agregar...";
  String etiquetaNuevaZona = "Agregar...";
  Comboitem itemAddCiudad = new Comboitem(etiquetaNuevaCiudad);
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

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    modo = "editar";
    //+ cargarEstados();
    //+ cargarCiudades();
    //+ cargarZonas();
    desmarcarEstado();
    //-mostrarCiudadesPorDefecto();
    setVariablesSesion();
  }

  /**
   * carga todas los estados existentes en la base de datos
   */
  private void cargarEstados() {
    listaEstados = servEstado.getNombres();
    System.out.println("CtrlDireccion- estados: lista.size:" + listaEstados.size());
    servEstado.listarAllConsola();
    modelEstados.addAll(listaEstados);
    cmbEstado.setModel(modelEstados);
  }

  void desmarcarEstado() {
    cmbEstado.setValue("");
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

  /**
   * muestra las ciudades del estado 'Lara'
   */
  public void mostrarCiudadesPorDefecto() {
    System.out.println("CtrlDireccion. mostrarCiudadesPorDefecto - INICIO");
    idEstado = 1; //Lara
    listaCiudades = servCiudad.getCiudadesPorEstado(idEstado);
    listaCiudades.add(etiquetaNuevaCiudad);
    modelCiudades = new ListModelList(listaCiudades);
    cmbCiudad.setModel(modelCiudades);
  }

  /**
   * maneja el evento de seleccionar un estado de la lista
   * cuando se selecciona un estado, se buscan las ciudades de ese estado,
   * y se muestran en el combobox de ciudades
   */
  public void onSelect$cmbEstado() {
    estado = cmbEstado.getValue();
    //**System.out.println("CtrlDireccion. estado seleccionado: " + estado);

    //+ buscarCiudadesDeEstadoSeleccionado();

    //limpiar valores seleccionados
    cmbCiudad.setValue("");
    cmbZona.setValue("");

    //ocultar textbox por si fueron usados
    txtCiudad.setVisible(false);
    txtZona.setVisible(false);

    mostrarValorEstado();
    desactivarEditEstado();
    activarEditCiudad(); //al cambiar el estado se obliga a cambiar la ciudad
  }

  private void buscarCiudadesDeEstadoSeleccionado() {
    //- idEstado = servEstado.getIdEstado(estado);
    //**System.out.println("CtrlDireccion. id de estado: " + idEstado);
    //- listaCiudades = servCiudad.getCiudadesPorEstado(idEstado);

    //agregar item nuevo item:
    listaCiudades.add(etiquetaNuevaCiudad);

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

    //quitar valores seleccionados:
    cmbZona.setValue("");
    desactivarEditEstado(); //por si está activado
    desactivarEditZona(); //por si está activado

    if (ciudad.equals(etiquetaNuevaCiudad)) {
      System.out.println("CtrlDireccion. se seleccionó 'nueva ciudad'");
      txtCiudad.setValue("");
      txtCiudad.setVisible(true);
      txtCiudad.setFocus(true);
      txtCiudad.select();
      //borrar las zonas
      listaZonas = new ArrayList();
    } else {
      System.out.println("CtrlDireccion -> Ciudad elegida: " + ciudad);
      txtCiudad.setVisible(false);
      mostrarValorCiudad();
      buscarZonasDeCiudadSeleccionada();
    }
    desactivarEditCiudad();//al cambiar el estado se obliga a cambiar la zona
    activarEditZona(); //al cambiar el estado se obliga a cambiar la ciudad    
  }

  //buscar las zonas de la ciudad seleccionada:
  private void buscarZonasDeCiudadSeleccionada() {
    //- idCiudad = servCiudad.getIdCiudad(ciudad);
    System.out.println("CtrlDireccion -> id Ciudad: " + idCiudad);
    /*+
    listaZonas = servZona.getZonasPorCiudad(idCiudad);
    configurar cmbZona para nuevo sector
    listaZonas.add(etiquetaNuevaZona);
    modelZonas = new ListModelList(listaZonas);
    cmbZona.setModel(modelZonas);
    cmbZona.setValue(etiquetaNuevaZona);
     * 
     */
  }

  /**
   * maneja evento de Enter cuando la nueva ciudad es ingresada
   */
  public void onOK$txtCiudad() {
    /*
    //OPCION B: agregar nueva ciudad a la lista
    String ciudadNueva = txtCiudad.getValue();
    //se agregará la nueva ciudad
    int posicion = cmbCiudad.getItemCount() - 1;
    modelCiudades.add(posicion, ciudadNueva);
    cmbCiudad.setModel(modelCiudades);
    System.out.println("posicion de ciudad nueva en modelCiudades: " + modelCiudades.indexOf(ciudadNueva));
    System.out.println("tamaño de la lista: " + cmbCiudad.getItemCount());
    System.out.println("variable posicion: " + posicion);
    //seleccionar en la lista la ciudad agregada: EN PROCESO
    cmbCiudad.setSelectedIndex(posicion);
    txtCiudad.setVisible(false);
    cmbZona.setFocus(true);
    cmbZona.select();
     */

    /**
     * //OPCION A: Simplemente usar el valor que está en la caja de texto
     */
    ciudad = txtCiudad.getValue();
    if (!ciudad.isEmpty()) {
      //TODO: validar si la nueva ciudad no existe en base de datos
      activarIngresoNuevaZona();
    }
  }

  /** en desarrollo
   * maneja el evento de seleccionar una zona
   * cuando se selecciona una zona, se buscan las zonas correspondientes
   * y se muestran en la lista de zonas
   */
  public void onSelect$cmbZona() {
    zona = cmbZona.getValue();
    System.out.println("CtrlDireccion -> zona elegida: " + zona);
    //manejo de evento si selecciona "nueva zona":
    System.out.println("CtrlDireccion. se seleccionó 'nueva zona' ? ");
    if (zona.equals(etiquetaNuevaZona)) {
      System.out.println("SÍ");
      activarIngresoNuevaZona();
    } else {
      System.out.println("NO");
      txtZona.setVisible(false);
      //capturar valores:
      idZona = servZona.getIdZona(zona);
      System.out.println("CtrlDireccion.Zona.id=" + idZona);
      setVariableSesionZona();
      mostrarValorZona();
    }
    desactivarEditZona();
    activarEditDetalle();
  }

  public void onOK$txtZona() {
    procesarNuevaZona();
  }

  public void onBlur$txtZona() {
    procesarNuevaZona();
  }

  private void procesarNuevaZona() {
    if (zonaNuevaIngresada()) {
      desactivarEditZona();
      etqZona.setValue(zona);
      txtDetalle.setFocus(true);
    } else {
      txtZona.setFocus(true);
    }

    /* OPCION B: agregando opción a la lista:
    String zonaNueva = txtZona.getValue();
    //se agregará la nueva Zona
    int posicion = cmbZona.getItemCount() - 1;
    modelZonas.add(posicion, zonaNueva);
    cmbZona.setModel(modelZonas);
    
    System.out.println("posicion de Zona nueva en modelZonas: " + modelZonas.indexOf(zonaNueva));
    System.out.println("tamaño de la lista: " + cmbZona.getItemCount());
    
    //seleccionar en la lista la Zona agregada: EN PROCESO
    cmbZona.setSelectedIndex(posicion);
    txtZona.setVisible(false);
    
    cmbZona.setFocus(true);
    cmbZona.select();
    //TODO: grabar la nueva zona en la base de datos
     * 
     */
    /**
     * //OPCION A: Simplemente usar el valor que está en la caja de texto
     */
  }

  //TODO: validar si zona nueva no existe en la base de datos
  /**
   * devuelve true si ingresó el nombre de la zona nueva
   * @return 
   */
  private boolean zonaNuevaIngresada() {
    zona = txtZona.getValue();
    if (zona.isEmpty()) {
      return false;
    }
    return true;
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

  /**
   * botón de edición de estado
   */
  public void onClick$btnEditEstado() {
    activarEditEstado();
  }

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
  public void onClick$btnEditCiudad() {
    activarEditCiudad();
  }

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
    cmbCiudad.setValue(etqCiudad.getValue());
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
    btnCancelarEditCiudad.setVisible(false);
    cmbCiudad.setVisible(false);
    etqCiudad.setVisible(true);
    //- btnEditCiudad.setVisible(true);
  }

  /**
   * botón de edición de Zona
   */
  public void onClick$btnEditZona() {
    activarEditZona();
  }

  /**
   * botón para cancelar la edición de Zona
   */
  public void onClick$btnCancelarEditZona() {
    desactivarEditZona();
  }

  /**
   * activar edición de Zona
   */
  void activarEditZona() {
    etqZona.setVisible(false);
    cmbZona.setValue(etqZona.getValue());
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
    btnCancelarEditZona.setVisible(false);
    //- btnEditZona.setVisible(true);
  }

  boolean modoEditar() {
    return modo.equals("editar");
  }

  //mostrar txtZona para ingreso de nueva zona
  private void activarIngresoNuevaZona() {
    txtZona.setValue("");
    txtZona.setVisible(true);
    txtZona.setFocus(true);
    btnCancelarEditZona.setVisible(true);
  }

  private void mostrarValorEstado() {
    etqEstado.setValue(estado);
  }

  private void mostrarValorCiudad() {
    etqCiudad.setValue(ciudad);
  }

  private void mostrarValorZona() {
    etqZona.setValue(zona);
  }

  private void activarEditDetalle() {
    etqDetalle.setVisible(false);
    txtDetalle.setValue(etqDetalle.getValue());
    txtDetalle.setVisible(true);
    txtDetalle.setFocus(true);
  }

  private void desactivarEditDetalle() {
    txtDetalle.setVisible(false);
    etqDetalle.setFocus(true);
    etqDetalle.setVisible(true);
  }

  public void onClick$etqDetalle() {
    activarEditDetalle();
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
