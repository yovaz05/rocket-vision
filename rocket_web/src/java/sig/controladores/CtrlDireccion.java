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

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.Div;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import sig.modelo.servicios.ServicioCelula;
import sig.modelo.servicios.ServicioCiudad;
import sig.modelo.servicios.ServicioEstado;
import sig.modelo.servicios.ServicioPersona;
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
  Div divMensaje;
  Label etqMensaje;
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
  //valores por defecto:
  int idEstado = 0;
  int idCiudad = 0;
  int idZona = 0;
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
  private int idCelula = 0;
  private int idLider = 0;
  ServicioCelula servicioCelula = new ServicioCelula();
  ServicioPersona servicioPersona = new ServicioPersona();

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

    //opción 'agregar...':, será implementado en lanzamientos futuros
    //+ listaEstados.add(NUEVO_ESTADO);

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
    cancelarEditEstado();

    //limpiar las otras variables por si hay valores seleccionados
    ciudad = "";
    zona = "";

    if (estado.equals(NUEVO_ESTADO)) {
      System.out.println("CtrlDireccion. Se seleccionó 'nuevo estado'");
      cancelarEditEstado();
      activarIngresoNuevoEstado();
      limpiarListaCiudad();
    } else {
      desactivarIngresoNuevoEstado();
      System.out.println("CtrlDireccion -> Estado elegido: " + estado);
      txtEstado.setVisible(false);
      mostrarValorEstado();
      //busca id de estado mediante el servicio, por el nombre de estado elegido
      idEstado = servEstado.getIdEstado(estado);
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
    } else {//nombre de nuevo estado ingresado
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
    //**System.out.println("CtrlDireccion. id de estado: " + idEstado);
    listaCiudades = servCiudad.getCiudadesPorEstado(idEstado);

    //opción 'agregar ciudad': será usado en próximos lanzamientos
    //+ listaCiudades.add(NUEVA_CIUDAD);

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
    cancelarEditCiudad();

    if (ciudad.equals(NUEVA_CIUDAD)) {
      System.out.println("CtrlDireccion. se seleccionó 'nueva ciudad'");
      cancelarEditCiudad();
      activarIngresoNuevaCiudad();
      limpiarListaZona();
    } else {
      desactivarIngresoNuevaCiudad();
      System.out.println("CtrlDireccion -> Ciudad elegida: " + ciudad);
      txtCiudad.setVisible(false);
      mostrarValorCiudad();
      //busca id de ciudad mediante el servicio, por el nombre elegido
      idCiudad = servCiudad.getIdCiudad(ciudad);
      cargarZonasPorCiudad();
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
  private void cargarZonasPorCiudad() {
    System.out.println("CtrlDireccion -> id Ciudad: " + idCiudad);
    listaZonas = servZona.getZonasPorCiudad(idCiudad);

    //item "agregar zona...", será implementado en lanzamientos futuros
    //+ listaZonas.add(NUEVA_ZONA);

    modelZonas = new ListModelList(listaZonas);
    cmbZona.setModel(modelZonas);
    //- cmbZona.setValue(NUEVA_ZONA);
  }

  /** en desarrollo
   * maneja el evento de seleccionar una zona
   * cuando se selecciona una zona, se buscan las zonas correspondientes
   * y se muestran en la lista de zonas
   */
  public void onSelect$cmbZona() {
    String zonaElegida = cmbZona.getValue();
    if (zonaElegida.equals(zona)) {//no hubo cambio, se eligió el mismo valor anterior
      cancelarEditZona();
      return;
    }

    zona = zonaElegida;
    mostrarValorZona();
    System.out.println("CtrlDireccion -> zona elegida: " + zona);

    if (zona.equals(NUEVA_ZONA)) {
      //manejo de evento si selecciona "nueva zona":
      cancelarEditZona();
      activarIngresoNuevaZona();
      return;
    } else {//se eligió una zona existente:
      desactivarIngresoNuevaZona();
      //capturar valores:
      idZona = servZona.getIdZona(zona);
      setVariableSesionZona();
      //**System.out.println("CtrlDireccion.Zona.id=" + idZona);

      //Actualizar observaciones de célula
      if (actualizarZona()) {
        mostrarMensaje("Se actualizó el sector");
      } else {
        mostrarMensaje("Error actualizando el sector");
      }

      //activar edición de siguiente campo: detalle
      //TODO: evaluar: si cambió la zona, debería borrarse el valor del detalle, ya que debería ser diferente
      activarEditDetalle();
    }
  }

  /**
   * actualiza valor de zona en la base de datos
   */
  boolean actualizarZona() {
    boolean ok = false;
    if (Sesion.esVistaCelula()) {
      //actualizar detalle de dirección de célula
      getIdCelula();
      ok = servicioCelula.actualizarIdZona(idCelula, idZona);
    } else if (Sesion.esVistaLider()) {
      //actualizar detalle de dirección de líder
      getIdLider();
      ok = servicioPersona.actualizarIdZona(idLider, idZona);
    }
    return ok;
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
    cancelarEditZona();
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
    activarEditEstado();
  }

  /**
   * click en la etiqueta de ciudad activa la edición
   */
  public void onClick$etqCiudad() {
    //se busca id de estado de la sesión y se cargan las ciudades del estado
    idEstado = (Integer) Sesion.getVariable("idEstado");
    cargarCiudadesPorEstado();
    //se trae la zona actual para seleccionar en la lista
    ciudad = etqCiudad.getValue();
    activarEditCiudad();
  }

  /**
   * click en la etiqueta de ciudad activa la edición
   */
  public void onClick$etqZona() {
    //se busca id de ciudad de la sesión y se cargan las zonas de la ciudad
    idCiudad = (Integer) Sesion.getVariable("idCiudad");
    cargarZonasPorCiudad();
    //se trae la zona actual para seleccionar en la lista
    zona = etqZona.getValue();
    activarEditZona();
  }

  public void onBlur$cmbEstado() {
    cancelarEditEstado();
  }

  public void onBlur$cmbCiudad() {
    cancelarEditCiudad();
    cancelarEditEstado();//por si fue activada y no se seleccionó nada
    //-cancelarEditZona();//por si fue activada y no se seleccionó nada
  }

  public void onBlur$cmbZona() {
    cancelarEditZona();
    if (zona.isEmpty()) {
      estado = Constantes.VALOR_EDITAR;
      ciudad = "";
      mostrarValorEstado();
      mostrarValorCiudad();
    }
    /*TODO: para próximas versiones manejar las nuevas zonas directamente:
    if (!nuevaZona) {
    cancelarEditZona();
    }
     */
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
  }

  /**
   * activar edición de estado
   */
  void cancelarEditEstado() {
    cmbEstado.setVisible(false);
    etqEstado.setVisible(true);
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
  }

  /**
   * activar edición de Ciudad
   */
  void cancelarEditCiudad() {
    cmbCiudad.setVisible(false);
    //- txtCiudad.setVisible(false);
    etqCiudad.setVisible(true);
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
  }

  /**
   * activar edición de Zona
   */
  void cancelarEditZona() {
    etqZona.setVisible(true);
    cmbZona.setVisible(false);
    txtZona.setVisible(false);
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
  }

  void desactivarIngresoNuevaZona() {
    nuevaZona = false;
    txtZona.setVisible(false);
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
    detalle = etqDetalle.getValue();
    if (detalle.equals(Constantes.VALOR_EDITAR)) {
      detalle = "";
    }
    txtDetalle.setValue(detalle);
    txtDetalle.setVisible(true);
    txtDetalle.setFocus(true);
  }

  /**
   * desactiva la edición de detalle
   * y muestra el valor actual
   */
  private void cancelarEditDetalle() {
    txtDetalle.setVisible(false);
    etqDetalle.setFocus(true);
    etqDetalle.setVisible(true);
  }

  public void onOK$txtDetalle() {
    procesarDetalle();
    cancelarEditDetalle();
  }

  public void onBlur$txtDetalle() {
    procesarDetalle();
    cancelarEditDetalle();
  }

  private void procesarDetalle() {
    String nuevoValor = txtDetalle.getValue();
    //quitar espacios en blanco
    nuevoValor = nuevoValor.trim();
    if (nuevoValor.equals(detalle) //no se cambió valor anterior
            || nuevoValor.equals(Constantes.VALOR_EDITAR)) {//no se cambió valor por defecto "Editar"
      return;
    }
    detalle = nuevoValor;

    //actualizar valor en la base de datos:
    if (actualizarDetalleDireccion()) {
      mostrarMensaje("Se actualizó la dirección");
    } else {
      mostrarMensaje("Error actualizando la dirección");
    }

    if (detalle.isEmpty()) {//quedó en blanco, se usa etiqueta que permita edición posterior
      etqDetalle.setValue(Constantes.VALOR_EDITAR);
    } else {
      etqDetalle.setValue(detalle);
    }
  }

  /**
   * actualiza valor en la base de datos
   */
  boolean actualizarDetalleDireccion() {
    boolean ok = false;
    if (Sesion.esVistaCelula()) {
      //actualizar detalle de dirección de célula
      getIdCelula();
      ok = servicioCelula.actualizarDireccionDetalle(idCelula, detalle);
    } else if (Sesion.esVistaLider()) {
      //actualizar detalle de dirección de líder
      getIdLider();
      ok = servicioPersona.actualizarDireccionDetalle(idLider, detalle);
    }
    return ok;
  }

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

  private void cancelarEditTelefono() {
    txtTelefono.setVisible(false);
    etqTelefono.setFocus(true);
    etqTelefono.setVisible(true);
  }

  public void onClick$etqTelefono() {
    activarEditTelefono();
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
    String nuevoValor = txtTelefono.getValue();
    //quitar espacios en blanco
    nuevoValor = nuevoValor.trim();

    if (nuevoValor.equals(telefono) //no se cambió valor anterior
            || nuevoValor.equals(Constantes.VALOR_EDITAR)) {//no se cambió valor por defecto "Editar"
      return;
    }
    telefono = nuevoValor;

    //actualizar valor en la base de datos:
    if (Sesion.esVistaCelula()) {
      //actualizar teléfono de la célula
      getIdCelula();
      if (servicioCelula.actualizarTelefono(idCelula, telefono)) {
        mostrarMensaje("Se actualizó el teléfono");
      } else {
        mostrarMensaje("Error actualizando el teléfono");
      }
      //TODO: mensaje de éxito/error
    } else if (Sesion.esVistaLider()) {
      getIdCelula();
      //TODO: se usará para el formulario de líder
      //+ servicioLider.setIdCelula(idCelula);
      //+ servicioLider.actualizarTelefono(telefono);
    }
    if (telefono.isEmpty()) {//quedó en blanco, se usa etiqueta que permita edición posterior
      telefono = Constantes.VALOR_EDITAR;
    }
    etqTelefono.setValue(telefono);
  }

  /**
   * recupera variable de sesión 'idCelula'
   */
  //TODO: MEJORA: sacar método a clase de utileria
  private void getIdCelula() {
    System.out.println("CtrlDireccion.getIdCelula:");
    idCelula = (Integer) Sesion.getVariable("idCelula");
  }

  //TODO: MEJORA: sacar método a clase de utileria
  private void getIdLider() {
    System.out.println("CtrlDireccion.getIdLider:");
    idLider = (Integer) Sesion.getVariable("idLider");
  }

  /**
   * muestra un mensaje en un label, para interacción con el usuario
   * @param msj 
   */
  private void mostrarMensaje(String msj) {
    etqMensaje.setValue(msj);
    etqMensaje.setVisible(true);
    divMensaje.setVisible(true);
    System.out.println(this.getClass().toString() + msj);
  }

  /** 
   * limpia el mensaje de estado
   */
  private void ocultarMensaje() {
    etqMensaje.setValue("");//TODO: CODIGO: línea parece redundante
    etqMensaje.setVisible(false);
    divMensaje.setVisible(false);
  }
}
/**
 * TODO:
 * 1. grabar los nuevos valores en la base de datos: estado, ciudad, zona
 * 2. manejar los eventos para valores nuevos:
 *  si es nuevo estado, no se listan las ciudades ni zonas
 *  si es nueva ciudad, no se listan las zonas
 */
