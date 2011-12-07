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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import sig.modelo.servicios.ServicioCiudad;
import sig.modelo.servicios.ServicioEstado;
import sig.modelo.servicios.ServicioZona;

/**
 * controlador asociado a Direccion.zul
 * @author Gabriel
 */
public class CtrlDireccion extends GenericForwardComposer {

  //widgets:
  Label etqEstado;
  Label etqCiudad;
  Label etqZona;
  Label etqDirDetallada;
  Combobox cmbEstado;
  Combobox cmbCiudad;
  Combobox cmbZona;
  Textbox txtCiudad;
  Textbox txtZona;
  Textbox txtDirDetallada;
  Textbox txtTelefono;
  Toolbarbutton tbbTelefono;
  Comboitem itemZonaAdicional;
  Label etqMensaje;
  //widgets adicionales:
  String etiquetaNuevaCiudad = "Nueva Ciudad...";
  String etiquetaNuevaZona = "Nuevo Sector...";
  Comboitem itemAddCiudad = new Comboitem(etiquetaNuevaCiudad);
  //variables de control:
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

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    cargarEstados();
    desmarcarEstado();
    cargarCiudades();
    cargarZonas();
    //-mostrarCiudadesPorDefecto();
    setVariablesSesionPorDefecto();
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
  
  void desmarcarEstado(){
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
    //ocultar txtCiudad
    cmbCiudad.setValue("");
    cmbZona.setValue("");
    txtCiudad.setVisible(false);
    txtZona.setVisible(false);

    //TODO: buscar id por nombre de la lista de estados
    estado = cmbEstado.getValue();
    idEstado = servEstado.getIdEstado(estado);
    System.out.println("CtrlDireccion. estado seleccionado: " + estado);
    System.out.println("CtrlDireccion. id estado seleccionado: " + idEstado);
    listaCiudades = servCiudad.getCiudadesPorEstado(idEstado);
    listaCiudades.add(etiquetaNuevaCiudad);
    modelCiudades = new ListModelList(listaCiudades);
    cmbCiudad.setModel(modelCiudades);

    /*
    if (listaCiudades.size() > 2) {
    //seleccionar primer valor en la lista:
    cmbCiudad.setSelectedIndex(0);
    cmbCiudad.setFocus(true);
    }
     */
  }

  /**
   * maneja el evento de seleccionar un estado de la lista
   * cuando se selecciona una ciudad, se buscan las zonas correspondientes
   * y se muestran en la lista de zonas
   */
  public void onSelect$cmbCiudad() {
    //quitar zona seleccionada
    cmbZona.setValue("");
    txtZona.setVisible(false);
    ciudad = cmbCiudad.getValue();
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
      idCiudad = servCiudad.getIdCiudad(ciudad);
      System.out.println("CtrlDireccion -> id Ciudad: " + idCiudad);
      System.out.println("Posición en la lista: " + cmbCiudad.getSelectedIndex());
      //buscar las zonas de la ciudad seleccionada:
      listaZonas = servZona.getZonasPorCiudad(idCiudad);
      /*
      //seleccionar primera zona en la lista: EN PROCESO
      if (listaZonas.size() > 2) {
      cmbZona.setSelectedIndex(0);
      cmbZona.setFocus(true);
      cmbZona.select();
      }
       */
    }
    //configurar cmbZona para nuevo sector
    listaZonas.add(etiquetaNuevaZona);
    modelZonas = new ListModelList(listaZonas);
    cmbZona.setModel(modelZonas);
    cmbZona.setValue(etiquetaNuevaZona);
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

      //mostrar txtZona para ingreso de nueva zona
      txtZona.setVisible(true);
      txtZona.setValue("");
      txtZona.setFocus(true);
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
      txtZona.setValue("");
      txtZona.setVisible(true);
      txtZona.setFocus(true);
    } else {
      System.out.println("NO");
      txtZona.setVisible(false);
      //foco en widget siguiente:
      txtDirDetallada.setFocus(true);
      //capturar valores:
      idZona = servZona.getIdZona(zona);
      System.out.println("CtrlDireccion.Zona.id=" + idZona);
      setVariableSesionZona();
    }
  }

  /**
   * maneja evento de Enter cuando la nueva ciudad es ingresada
   */
  public void onOK$txtZona() {
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
    validarZonaNueva();
    if (!validarZonaNueva()) {
      txtDirDetallada.setFocus(true);
    } else {
      txtZona.setFocus(true);
    }

  }

  public void onBlur$txtZona() {
    validarZonaNueva();
  }

  //TODO: validar si zona nueva no existe en la base de datos
  private boolean validarZonaNueva() {
    zona = txtZona.getValue();
    if (zona.isEmpty()) {
      return false;
    }
    return true;
  }

  /**
   * en desarrollo
   * se intenta mostrar un textbox dentro del combobox donde se ingrese la nueva 
   */
  private void definirItemCiudadAdicional() {
    itemAddCiudad.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Messagebox.show("JAX, botón dentro de un com-BOBO-x creado dinámicamente");
      }
    });
  }

  /**
   *TODO:
   * 1. cuando eliga nueva ciudad: mensaje en la parte superior de la ventana
   * 2. mostrar item nueva ciudad en la carga inicial
   * 3. hacer mètodos: buscarIdCiudad()  
   * buscarIdEstado()
   * buscarIdZona()
   ***/
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
  private void setVariablesSesionPorDefecto() {
    setVariableSesionZona();
  }
}
