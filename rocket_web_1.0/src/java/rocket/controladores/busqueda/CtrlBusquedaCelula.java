package rocket.controladores.busqueda;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import rocket.controladores.general.Modo;
import rocket.controladores.general.Sesion;
import rocket.controladores.widgets.BotonLider;
import rocket.controladores.widgets.EtqNro;
import rocket.modelo.bd.util.CelulaListadoUtil;
import rocket.modelo.bd.util.LiderListadoUtil;
import rocket.modelo.bd.util.UsuarioUtil;
import rocket.modelo.servicios.ServicioCelula;
import rocket.modelo.servicios.ServicioLider;
import rocket.modelo.servicios.ServicioRed;
import waytech.modelo.beans.sgi.Celula;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.beans.sgi.Red;
import waytech.utilidades.Util;

/**
 *
 * @author Gabriel
 */
public class CtrlBusquedaCelula extends GenericForwardComposer {

  Grid grid;
  Label etqInstrucciones;
  Label etqInstruccionesCodigo;
  Label etqInstruccionesNombreLider;
  Label etqMensajeNoData;
  //widgets:
  Div divMensaje;
  Label etqMensaje;
  //- A btnCerrarMensaje;
  Label etqMensajeLideres;
  Textbox txtCodigo, txtNombreLider, txtTelefono, txtEmail;
  A tbbRed;
  A tbbLider1, tbbLider2, tbbLider3, tbbLider4;
  Label etqRed;
  Label etqCedula, etqLider1, etqLider2, etqLider3, etqLider4, etqNombre, etqTelefono, etqEmail;
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
  private int idLider = 0;
  int idRed = 0;
  String nombreRed = "";
  private String cedula = "";
  A btnEditCedula;
  String codigo = "";
  String telefono = "";
  String email = "";
  A btnEditNombre;
  A btnEditRed;
  //referencias:
  private Include panelCentral;
  Label tituloVentana;
  String descripcionlider;
  private int tipoUsuario;
  private boolean usuarioPuedeVerLiderCompleto = false;
  private boolean esLiderRed = false;
  List<CelulaListadoUtil> lista;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    System.out.println("CtrlLiderBusqueda.inicio");
    limpiarValores();
    txtNombreLider.setFocus(true);
    setPermisos();
  }

  void limpiarValores() {
    txtCodigo.setValue("");
    txtNombreLider.setValue("");
  }

  public void onFocus$txtNombreLider() {
    //- ocultarMensajes();
    //- limpiarValores();
    //- mensajeInstruccionesNombreLider(true);
  }

  public void onFocus$txtCodigo() {
    //- ocultarMensajes();
    //- limpiarValores();
    //- mensajeInstruccionesCedula(true);
  }

  public void onBlur$txtCodigo() {
    //-mensajeInstruccionesCodigo(false);
  }

  public void onBlur$txtNombreLider() {
    mensajeInstruccionesNombreLider(false);
  }

  public void onOK$txtNombreLider() {
    procesarNombre();
  }

  private void procesarNombre() {
    ocultarMensajes();
    String valor = txtNombreLider.getValue();
    //quitar espacios en blanco
    valor = valor.trim();

    if (!nombreIngresado(valor)) {//en modo ingresar dejó campo en blanco
      mensaje("Escribe el nombre del líder");
      return;
    }

    if (valor.isEmpty() || valor.equals(codigo)) {//no se cambió el valor
      return;
    }

    codigo = valor;
    //buscar persona por nombre:

    buscarPorNombre(codigo);

    //- mostrarNombre();
  }

  /**
   * devuelve true si se ingresó una cédula
   * false en caso contrario
   * @param valor
   * @return 
   */
  boolean nombreIngresado(String valor) {
    return !valor.isEmpty();
  }

  public void onOK$txtCodigo() {
    procesarCedula();
  }

  public void onClick$btnBuscarCedula() {
    procesarCedula();
  }

  //procesamiento de valor de código (nuevo y edición)
  private void procesarCedula() {
    ocultarMensajes();
    String valor = txtCodigo.getValue();
    //quitar espacios en blanco
    valor = valor.trim();
    //modo edición
    if (!cedulaIngresada(valor)) {//se ingresó valor vacío...
      return;
    }
    if (valor.equals(cedula)) {//no se cambió el valor
      return;
    }
    cedula = valor.toUpperCase();
    if (codigoEncontrada(valor)) {
      //- mensajeNoResultados("Cédula encontrada");
      //+ mostrar datos básicos: nombre, red, teléfono, correo
      //+ mostrar link que lleva a ver y editar registro completo...
    } else {
      mensajeNoResultados(true);
    }
    txtCodigo.select();
    mensajeInstruccionesCodigo(false);
  }

  /**
   * devuelve true si se ingresó una cédula
   * false en caso contrario
   * @param cedula
   * @return 
   */
  boolean cedulaIngresada(String cedula) {
    if (cedula.isEmpty()) {
      mensaje("Ingresa la cédula"); //obligar al usuario a tipear algo
      return false;
    }
    return true;
  }

  /**
   * busca en la base de datos si la cédula ya está en uso
   * devuelve true o false si existe o no.
   * además muestra mensaje de error cuando aplica.
   * @return 
   */
  boolean codigoEncontrada(String codigo) {
    if (servicioCelula.existeCelula(codigo)) {
      //mostrar datos de líder, con la cédula ingresada:
      /*DOING TODO: TERMINAR
      Celula c = servicioCelula.getCelula();
      mostrarDatosCelulaRegistrada(c);
       **/
      return true;
    } else {
      //limpiar valores, por si fueron usados en búsqueda anteriormente
      this.codigo = "";
    }
    return false;
  }

  /**
   * busca en la base de datos si la cédula ya está en uso
   * devuelve true o false si existe o no.
   * además muestra mensaje de error cuando aplica.
   * @return 
   */
  private void buscarPorNombre(String nombre) {
    lista = servicioCelula.getCelulasPorRed(idRed);
    if (lista.isEmpty()) {
      System.out.println("CtrlLiderListado. no hay datos");
      //no hay data
      mensajeNoResultados(true);
      mensajeInstrucciones(false);
      mostrarGrid(false);
      return;
    } else {
      //sí hay data
      mensajeNoResultados(false);
      mostrarGrid(true);
      mostrarData();
    }
  }

  public void mostrarData() {
    ListModelList model = new ListModelList(lista);
    grid.setModel(model);
    grid.setRowRenderer(new RowRenderer() {

      public void render(Row row, Object data) throws Exception {
        //se extrae la data
        LiderListadoUtil lider = (LiderListadoUtil) data;

        //se crean los widgets con la data
        Label etqNro = new EtqNro("" + lider.getNroItem());
        BotonLider linkNombre = new BotonLider("" + lider.getNombre());
        Label etqRed = new Label("" + lider.getNombreRed());
        Label etqDireccion = new Label("" + lider.getDireccionCorta());
        Label etqTelefono = new Label("" + lider.getTelefono());
        Label etqEmail = new Label("" + lider.getEmail());
        /*+
        tbbLider1 = new BotonLider("" + lider.getNombreLider1());
        tbbLider2 = new BotonLider("" + lider.getNombreLider2());
         */

        //TODO: establecer atributos de estilo a los widgets

        //se establecen parámetros para navegación dinámica:
        final int id = lider.getId();
        final int idRed = lider.getIdRed();

        /*+
        final int idLider1 = lider.getIdLider1();
        final int idLider2 = lider.getIdLider2();
         */

        if (usuarioPuedeVerLiderCompleto) {
          linkNombre.setModo(Modo.EDICION_DINAMICA);
        } else {
          linkNombre.setModo(Modo.CONSULTA);
        }

        linkNombre.setIdRed(idRed);
        linkNombre.setIdLider(id);

        /*+
        tbbLider1.setIdLider(idLider1);
        tbbLider2.setIdLider(idLider2);
         */

        //se anexan los widgets a la fila
        etqNro.setParent(row);
        linkNombre.setParent(row);
        etqRed.setParent(row);
        etqTelefono.setParent(row);
        etqEmail.setParent(row);
        etqDireccion.setParent(row);
        /*
        Vbox vbox = new Vbox();
        tbbLider1.setParent(vbox);
        tbbLider2.setParent(vbox);
        vbox.setParent(row);
         */
      }
    });
  }

  private void mostrarGrid(boolean visible) {
    grid.setVisible(visible);
  }

  private void mensajeNoResultados(boolean visible) {
    etqMensajeNoData.setVisible(visible);
  }

  private void mensajeInstrucciones(boolean visible) {
    etqInstrucciones.setVisible(visible);
  }

  private void mensajeInstruccionesCodigo(boolean visible) {
    etqInstruccionesCodigo.setVisible(visible);
  }

  private void mensajeInstruccionesNombreLider(boolean visible) {
    etqInstruccionesNombreLider.setVisible(visible);
  }

  /**
   * hay 2 opciones:
   * A: usar los widgets de entrada -menos trabajo
   * (B): usar los widgets de view -más trabajo, xq hay q jugar con el valor ingresado
   **/
  //DOING
  private void mostrarDatosCelulaRegistrada(Celula c) {
    codigo = c.getCodigo();
    //- nombreRed = c.getRed().getNombre();
    //** System.out.println("CtrlLiderDB. nombre = " + codigo);
    //** System.out.println("CtrlLiderDB. red = " + nombreRed);
    mostrarNombre();
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
    etqMensaje.setValue("");//TODO: Cedula: línea parece redundante
    etqMensaje.setVisible(false);
    //-btnCerrarMensaje.setVisible(false);
    divMensaje.setVisible(false);
  }

  private void ocultarMensajes() {
    ocultarMensaje();
    mensajeNoResultados(false);
    mensajeInstruccionesCodigo(false);
    mensajeInstruccionesNombreLider(false);
  }

  private void mostrarNombre() {
    txtNombreLider.setValue(codigo);
  }

  /**
   * establece permisos de lo que el usuario puede ver
   */
  private void setPermisos() {
    tipoUsuario = Util.buscarTipoUsuario(this.getClass());
    esLiderRed = (Boolean) Sesion.esLiderRed();
    if (tipoUsuario == UsuarioUtil.ADMINISTRADOR_CELULAS || esLiderRed) {
      usuarioPuedeVerLiderCompleto = true;
    } else {
      usuarioPuedeVerLiderCompleto = false;
    }
    System.out.println("CtrlLiderBusqueda.Permisos=usuarioPuedeVerLiderCompleto" + usuarioPuedeVerLiderCompleto);
  }
}
