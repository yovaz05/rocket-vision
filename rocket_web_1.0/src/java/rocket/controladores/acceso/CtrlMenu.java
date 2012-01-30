package rocket.controladores.acceso;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Include;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import rocket.controladores.general.Modo;
import rocket.controladores.general.Sesion;
import rocket.controladores.general.Vistas;
import rocket.modelo.bd.util.UsuarioUtil;
import waytech.utilidades.Util;

/**
 * muestra el menú correspondiente a todas las funcionalidades del sistema
 * controlador asociado a vistaMenu/MenuAll.zul
 * @author Gabriel
 */
public class CtrlMenu extends GenericForwardComposer {

  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //widgets:
  Label etqStatus;
  Menu menuAdmin;
  Menu menuLiderRed;
  Menuitem itemPerfil;
  Menuitem itemReporteCelulaListado;
  //variables de control:
  String vistaActual;
  String vistaSiguiente;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  /* TODO: validar sesión desde cualquier vista*/
  public void inicio() throws InterruptedException {
    try {
      if (!sesionActiva()) {
        Executions.sendRedirect("index.zul");
      }
    } catch (Exception e) {
    } finally {
      Sesion.setVistaCentral(vistaCentral);
      mostrarNombreUsuario();
      iniciarBarraNavegacion();
    }
  }

  /**
   * usado para efectos de desarrollo
   * @throws InterruptedException 
   */
  /* TODO: CODIGO: QUITAR */
  public void inicio2() throws InterruptedException {
    Sesion.setVistaCentral(vistaCentral);
    mostrarNombreUsuario();
    iniciarBarraNavegacion();
  }

  private boolean sesionActiva() {
    try {
      boolean usuarioLogueado = (Boolean) Sessions.getCurrent().getAttribute("usuarioLogueado");
      return usuarioLogueado;
    } catch (Exception e) {
      System.out.println("CtrMenu - No hay sesion activa: Redirigiendo a página de acceso");
    }
    return false;
  }

  public void logout() {
    System.out.println("CtrlBarraMenu:logout - antes de cerrar sesión");
    Sesion.setVariable("menu", null);
    Sesion.setVariable("idUsuario", null);
    Sesion.setVariable("usuarioLogueado", null);
    System.out.println("CtrlBarraMenu: - Sesión cerrada");
    Executions.sendRedirect("index.zul");
  }

  public void onClick$itemCerrarSesion(Event event) {
    logout();
  }

  void mostrarNombreUsuario() {
    /*TODO: descomentar*/
    String nombreUsuario = "";
    try {
      nombreUsuario = "" + Sessions.getCurrent().getAttribute("nombreUsuario");
    } catch (Exception e) {
      System.out.println("CtrlSesion - ERROR de SESION: recuperando 'nombreUsuario'");
    }
    if (nombreUsuario == null) {
      itemPerfil.setLabel("");
      //-menuUsuario.setLabel("");
      System.out.println("CtrlSesion - nombreUsuario=null");
    } else {
      System.out.println("CtrlSesion - nombreUsuario= " + nombreUsuario);
    }
    //-menuUsuario.setLabel(nombreUsuario);
    itemPerfil.setLabel(nombreUsuario);
  }

  public void onClick$itemLiderListadoAdmin() {
    cambiarVista(Vistas.LIDER_LISTADO_ADMIN);
  }

  public void onClick$itemLiderListadoAll() {
    cambiarVista(Vistas.LIDER_LISTADO_ALL);
  }

  public void onClick$itemLiderListadoRed() {
    cambiarVista(Vistas.LIDER_LISTADO_RED);
  }

  public void onClick$itemCelulaListadoAdmin() {
    cambiarVista(Vistas.CELULA_LISTADO_ADMIN);
  }

  public void onClick$itemCelulaListadoAll() {
    cambiarVista(Vistas.CELULA_LISTADO_ALL);
  }

  public void onClick$itemCelulaListadoRed() {
    cambiarVista(Vistas.CELULA_LISTADO_RED);
  }

  public void onClick$itemReporteCelulaListadoAdmin() {
    cambiarVista(Vistas.REPORTE_CELULA_LISTADO_SEMANA_ADMINISTRADOR);
  }

  public void onClick$itemReporteCelulaListadoRed() {
    cambiarVista(Vistas.REPORTE_CELULA_LISTADO_SEMANA_RED);
  }

  public void onClick$itemReporteCelulaListado() {
    cambiarVista(Vistas.REPORTE_CELULA_LISTADO_SEMANA);
  }

  public void onClick$itemLiderNew() {
    Sesion.setVistaSiguiente(Vistas.LIDER);
    Sesion.setModo(Modo.INGRESAR);
    onClick$btnControl();//forzar cambio de vista
  }
  
  public void onClick$itemLiderBusqueda() {
    Sesion.setVistaSiguiente(Vistas.LIDER_BUSQUEDA);
    Sesion.setModo(Modo.CONSULTA);
    onClick$btnControl();//forzar cambio de vista
  }

  public void onClick$itemCelulaNew() {
    System.out.println("CtrlMenu.click itemCelulaNew");
    Sesion.setVistaSiguiente(Vistas.CELULA);
    Sesion.setModo(Modo.INGRESAR);
    onClick$btnControl();//forzar cambio de vista
  }

  public void onClick$itemInicio() {
    cambiarVista(Vistas.REPORTE_CELULA_LISTADO_SEMANA);
  }

  public void onClick$itemRed() {
    cambiarVista(Vistas.RED);
  }

  public void onClick$itemCelulaLiderListado() {
    cambiarVista(Vistas.CELULA_LIDER_LISTADO);
  }

  public void onClick$itemCelulaEstadisticas() {
    cambiarVista(Vistas.CELULA_ESTADISTICAS_RESUMEN);
    mostrarStatus("En construcción");
  }

  public void onClick$itemDiscipuloLanzadoListado() {
    cambiarVista(Vistas.LIDER_LISTADO_ALL);
  }

  public void onClick$itemDiscipuloProcesoListado() {
    cambiarVista(Vistas.DISCIPULO_PROCESO_LISTADO);
  }

  public void onClick$itemRedListado() {
    cambiarVista(Vistas.REDES);
  }

  public void onClick$itemPerfil() {
    cambiarVista(Vistas.CUENTA_PERFIL);
  }

  public void onClick$itemCambiarEmail() {
    cambiarVista(Vistas.CUENTA_CAMBIO_EMAIL);
  }

  public void onClick$itemCambiarPassword() {
    cambiarPanelCentral(Vistas.CUENTA_CAMBIO_PASSWORD);
  }

  public void onClick$itemAyuda() {
    cambiarVista(Vistas.SISTEMA_AYUDA);
    mostrarStatus("En construcción");
  }

  public void onClick$itemContacto() {
    cambiarVista(Vistas.SISTEMA_CONTACTO);
  }

  public void onClick$itemCondiciones() {
    cambiarVista(Vistas.SISTEMA_CONDICIONES);
    mostrarStatus("En construcción");
  }

  public void onClick$itemPrivacidad() {
    cambiarVista(Vistas.SISTEMA_PRIVACIDAD);
    mostrarStatus("En construcción");
  }

  public void onClick$itemCompatibilidad() {
    cambiarVista(Vistas.SISTEMA_COMPATIBILIDAD);
    mostrarStatus("En construcción");
  }

  public void onClick$itemAcercaDe() {
    cambiarVista(Vistas.SISTEMA_ACERCADE);
  }

  public void onClick$itemListadoAcceso() {
    cambiarVista(Vistas.LISTADO_ACCESO);
  }
  /**
   * codigo relacionado a barra de navegación
   **/
  //widgets:  
  Toolbarbutton btnRefresh;
  Toolbarbutton btnNew;
  Toolbarbutton btnEdit;
  Toolbarbutton btnSave;
  Toolbarbutton btnDelete;
  Toolbarbutton btnPrint;
  Toolbarbutton btnAtras;
  Toolbarbutton btnSiguiente;
  Toolbarbutton btnConfirmar; //botón para confirmar reporte
  //variables de control:  
  String modo;  //valores = {new,editar,ver,listado,imprimir,confirmado,operacion,borrar}
  Button btnControl;

  /**
   * notifica los eventos de boton a la ventana abierta actualmente ("nuevo","editar","guardar","eliminar")
   * para que se ejecuten los métodos correspondientes de la ventana
   * puede ser btnNew, btnEdit, btnSave, btnDelete, btnPrint, etc...
   */
  private void notificarEvento(String idBoton) {
    Toolbarbutton boton = (Toolbarbutton) panelCentral.getFellow(idBoton);
    //simular un click del boton indicado a la ventana abierta
    //-Events.postEvent(1, "onClick", boton, null);
    Events.sendEvent("onClick", boton, null);
  }

  public void onClick$btnNew() {
    borrarVariablesSesionResultado();
    ocultarStatus();
    //notificar evento a ventana actual:
    modo = Modo.INGRESAR;
    Sesion.setModo(Modo.INGRESAR);
    notificarEvento("btnNew");
    //actualizar estados de botones:
    actualizarEstadoBarra();
  }

  public void onClick$btnEdit() {
    ocultarStatus();
    notificarEvento("btnEditar");
    modo = "editar";
    actualizarEstadoBarra();
    borrarVariablesSesionResultado();
  }

  /**
   * botón usado para guardar cambios:
   * al crear y editar un registro
   */
  public void onClick$btnSave() {
    //+modoActual = "grabando";
    if (modo.equals("operacion")) {
      mostrarStatus("En construcción");
    } else if (modo.equals("new") || modo.equals("editar")) {
      System.out.println("CtrlMenu.modo=" + modo);
      notificarEvento("btnGuardar");
      //aquí va el procesamiento de los datos... por parte de la ventana correspondiente

      /*TODO: terminar esta validación y aplicar a todas las vistas que permitan edición
      //validación de datos obligatorios en los formularios
      //sólo se probó con CtrlLider y CtrlCelula
       */
      Integer resultado = (Integer) Sesion.getVariable("resultOperacion");
      if (resultado == null) {
        //no hay resultado de operación
        return;
      }
      if (resultado.intValue() == 1) {
        //**
        System.out.println("CtrlMenu.btnSave.resultado=OK");
        mostrarStatus("Datos guardados");
        //+ mensajeExitoEspecifico();
        modo = "ver";
        actualizarEstadoBarra();
      } else if (resultado.intValue() == -1) {
        System.out.println("CtrlMenu.btnSave.resultado Operacion=ERROR");
      }
      /**/
    }
  }

  /**
   * muestra mensajes de éxito de acuerdo al 'modo' y a la 'vista' que se está trabajando
   */
  //TODO: CODIGO. quitar
  /*
  private void mensajeExitoEspecifico() {
  String msj = "";
  if (modo.equals("new")) {
  if (vistaActual.equals(Vistas.CELULA)) {
  msj = "Célula ingresada";
  }
  } else if (modo.equals("editar")) {
  if (vistaActual.equals(Vistas.CELULA)) {
  msj = "Célula modificada con éxito";
  }
  }
  mostrarStatus(msj);
  }
   */
  public void onClick$btnDelete() {
    String modo_tmp = modo;
    modo = "borrar";
    mostrarStatus("Eliminar: En construcción");
    //...aquí va el procesamiento para imprimir
    modo = modo_tmp;
  }

  public void onClick$btnPrint() {
    String modo_tmp = modo;
    modo = "imprimir";
    mostrarStatus("Imprimir: En construcción");
    //...aquí va el procesamiento de imprimir
    modo = modo_tmp;
  }

  public void onClick$btnConfirmar() throws InterruptedException {
    ocultarStatus();
    if (vistaActual.equals(Vistas.REPORTE_CELULA)) {
      final String modo_tmp = modo;
      String mensaje = "Esta operación finalizará el ingreso del reporte. \n ¿Está seguro?";
      Messagebox.show(mensaje, "Confirmación de reporte",
              Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
              new org.zkoss.zk.ui.event.EventListener() {

                public void onEvent(Event e) throws InterruptedException {
                  if ("onYes".equals(e.getName())) {
                    System.out.println("CtrlReporteCelula, reporte confirmado: TRUE");
                    modo = "confirmado";
                    System.out.println("CtrlReporteCelula, modo: " + modo);
                    //aquí va la llamada a la función correspondiente de CtrlReporteCelula: notificarEvento("btnConfirmar");
                    mostrarStatus("Reporte confirmado");
                    actualizarEstadoBarra();
                    notificarEvento("btnConfirmar");
                  } else if ("onNo".equals(e.getName())) {
                    System.out.println("CtrlReporteCelula, reporte confirmado: FALSE");
                    //} 
                  }
                }
              });
    }
  }

  public void onClick$btnRefresh() {
    //*System.out.println("CtrlBarraNavegacion: se intenta refrescar la ventana");
    String tmp = panelCentral.getSrc();
    panelCentral.setSrc("");
    panelCentral.setSrc(tmp);
    ocultarStatus();
  }

  //TODO: programar adecuadamente  
  public void onClick$btnAtras() {
    //* es un prueba
    mostrarStatus("Navegación: En construcción");
  }

  //TODO: programar adecuadamente
  public void onClick$btnSiguiente() {
    //* es un prueba
    mostrarStatus("Navegación: En construcción");
  }

  /**
   * método debug
   * muestra la vista actual
   */
  public void mostrarVistaActual(String metodo) {
    Sesion.mostrarVistaActual(this.getClass().toString(), metodo);
  }

  public void mostrarStatus(String mensaje) {
    etqStatus.setValue(mensaje);
    etqStatus.setVisible(true);
    //-divStatus.setVisible(true);
  }

  public void ocultarStatus() {
    etqStatus.setValue("");
    etqStatus.setVisible(false);
    //-divStatus.setVisible(false);
  }

  public void iniciarBarraNavegacion() {
    //TODO: CODIGO: quitar if, parece redundante
    if (modo == null) {
      modo = "consulta";
    }
    habilitarOpcionesUsuario();
    //** mostrarVistaActual("CtrlMenu.inicio");    
    actualizarModo();
    actualizarEstadoBarra();
  }

  /**
   * TODO - Mejora de código:
   * evaluar cambiar este método o limitar a sólo algunas vistas especiales
   * @param vista 
   */
  public void cambiarVista(String vista) {
    ocultarStatus();
    //+ actualizarModo();
    Sesion.setVistaActual(vista);
    actualizarEstadoBarra();
    cambiarPanelCentral(vista);
  }

  /**
   * actualiza el 'modo' de acuerdo a la vista actual
   */
  /**
   * TODO: en próxima versión, ajustar de acuerdo al usuario, por los permisos se activarán las opciones
   */
  //TODO: quitar este método, usando los métodos notificarBarra y los métodos de CtrlVista
  public void actualizarModo() {
    vistaActual = Sesion.getVistaActual();
    if (vistaActual.equals(Vistas.RED)) {
      modo = "consulta";
    } else if (vistaActual.equals(Vistas.CELULA_LISTADO_ALL)) {
      modo = "listado";
    } else if (vistaActual.equals(Vistas.REPORTE_CELULA)) {
      modo = "new";
    } else if (vistaActual.equals(Vistas.CELULA)) {
      modo = "ver";
    } else if (vistaActual.equals(Vistas.LIDER_RESUMEN)) {
      modo = "consulta";
    } else if (vistaActual.equals(Vistas.LIDER)) {
      modo = "ver";
    } else if (vistaActual.equals(Vistas.LIDER_LISTADO_ALL)) {
      modo = "listado";
    } else if (vistaActual.equals(Vistas.DISCIPULO_PROCESO_LISTADO)) {
      modo = "listado";
    } else if (vistaActual.equals(Vistas.DISCIPULO_PROCESO_RESUMEN)) {
      modo = "consulta";
    } else if (vistaActual.equals(Vistas.DISCIPULO_PROCESO)) {
      modo = "ver";
    } else if (vistaActual.equals(Vistas.CUENTA_CAMBIO_EMAIL)) {
      modo = "operacion";
    } else if (vistaActual.equals(Vistas.CUENTA_CAMBIO_PASSWORD)) {
      modo = "operacion";
    } else {
      modo = "consulta";
    }
  }

  /**
   * actualiza estados de botones,
   * de acuerdo al valor de la variable 'modo'
   * habilitando/deshabilitando los botones
   */
  /*TODO: hacer más eficiente este método,
  y cambiar estado de botones sólo si 'modo' cambió
   */
  /**
   * TODO: mejorar, ya que los estados operacion y consulta son iguales
   */
  //NO USADO POR EL MOMENTO, reemplazado por el método actualizarEstadoBarra
  /*
  public void actualizarEstado2() {
  if (modo.equals("new")) {
  btnNew.setDisabled(true);
  btnEdit.setDisabled(true);
  btnSave.setDisabled(false);
  btnDelete.setDisabled(true);
  //btnPrint.setDisabled(true);
  } else if (modo.equals("ver")) {
  btnNew.setDisabled(false);
  btnEdit.setDisabled(false);
  btnSave.setDisabled(true);
  //btnPrint.setDisabled(false);
  btnDelete.setDisabled(false);
  } else if (modo.equals("editar")) {
  btnNew.setDisabled(true);
  btnEdit.setDisabled(true);
  btnSave.setDisabled(false);
  btnDelete.setDisabled(true);
  //btnPrint.setDisabled(true);
  } else if (modo.equals("consulta")) {
  btnNew.setDisabled(true);
  btnEdit.setDisabled(true);
  btnSave.setDisabled(true);
  btnDelete.setDisabled(true);
  //btnPrint.setDisabled(false);
  } else if (modo.equals("listado")) {
  btnNew.setDisabled(false);
  btnEdit.setDisabled(true);
  btnSave.setDisabled(true);
  btnDelete.setDisabled(true);
  //btnPrint.setDisabled(false);
  } else if (modo.equals("permitir_edicion")) {
  btnNew.setDisabled(true);
  btnEdit.setDisabled(false);
  btnSave.setDisabled(true);
  btnDelete.setDisabled(true);
  //btnPrint.setDisabled(false);
  } else if (modo.equals("operacion")) {
  btnNew.setDisabled(true);
  btnEdit.setDisabled(true);
  btnSave.setDisabled(true);
  btnDelete.setDisabled(true);
  //btnPrint.setDisabled(true);
  }
  }
   */
  /**
   * actualiza estados de botones,
   * de acuerdo al valor de la variable 'modo'
   * mostrando y ocultando los botones según corresponda
   */
  /*TODO: hacer más eficiente este método,
  y cambiar estado de botones sólo si 'modo' cambió
   */
  /**
   * TODO: mejorar, ya que los estados operacion y consulta son iguales
   */
  public void actualizarEstadoBarra() {
    //-ocultarStatus();
    //TODO: simplificar código de manejo de estados con reporte_celula
    if (vistaActual.equals(Vistas.REPORTE_CELULA)) {
      if (modo.equals("new-pregunta")) {//mostrando la pregunta
        btnSave.setVisible(false);
      } else if (modo.equals("ver")) {
        btnEdit.setVisible(false);
        btnSave.setVisible(false);
        btnDelete.setVisible(false);
        btnNew.setVisible(false);
        //-btnConfirmar.setVisible(true);
      } else if (modo.equals("editar")) {
        btnEdit.setVisible(false);
        btnSave.setVisible(true);
        //-btnConfirmar.setVisible(false);
      } else if (modo.equals("confirmado")) {
        btnEdit.setVisible(false);
        //-btnConfirmar.setVisible(false);
      }
    } else if (modo.equals("new")) {
      btnNew.setVisible(false);
      btnEdit.setVisible(false);
      btnSave.setVisible(false);
      btnDelete.setVisible(false);
      //TODO: falta botón cancelar
    } else if (modo.equals("ver")) {
      btnNew.setVisible(false);
      btnEdit.setVisible(true);
      btnSave.setVisible(false);
      btnDelete.setVisible(true);
    } else if (modo.equals("edicion-dinamica")) {
      btnNew.setVisible(false);
      btnEdit.setVisible(false);
      btnSave.setVisible(false);
      btnDelete.setVisible(true);
    } else if (modo.equals("editar")) {
      btnNew.setVisible(false);
      btnEdit.setVisible(false);
      btnSave.setVisible(true);
      btnDelete.setVisible(false);
    } else if (modo.equals("consulta")) {
      btnNew.setVisible(false);
      btnEdit.setVisible(false);
      btnSave.setVisible(false);
      btnDelete.setVisible(false);
    } else if (modo.equals("listado")) {
      btnNew.setVisible(true);
      btnEdit.setVisible(false);
      btnSave.setVisible(false);
      btnDelete.setVisible(false);
    } else if (modo.equals("permitir_edicion")) {
      btnNew.setVisible(false);
      btnEdit.setVisible(true);
      btnSave.setVisible(false);
      btnDelete.setVisible(false);
    } else if (modo.equals("operacion")) {
      btnNew.setVisible(false);
      btnEdit.setVisible(false);
      btnSave.setVisible(true);
      btnDelete.setVisible(false);
    }
  }

  /**
   * Recibe la notificación de las ventanas,
   * para el cambio de vista indicado en variable de sesión vistaSiguiente
   * y modifica los estado de la barra correspondiente a la nueva vista
   */
  public void onClick$btnControl() {
    System.out.println("CtrlMenu");
    System.out.println(":llamada a btnControl");
    vistaActual = Sesion.getVistaActual();
    System.out.println("CtrlMenu.vistaActual: " + vistaActual);
    vistaSiguiente = Sesion.getVistaSiguiente();
    modo = Sesion.getModo();
    System.out.println("CtrlMenu.vistaSiguiente: " + vistaSiguiente);
    //si vistaSiguiente es igual a vistaActual sólo simular un click al botón Refresh      
    cambiarVista(vistaSiguiente);
    System.out.println("CtrlMenu.vistaActual: " + vistaActual);
  }

  /**
   * Recibe la notificación de la vista actual, para cambios de estados de acuerdo a la vista
   * antes de llamar este método,
   * el controlador correspondiente debe setear la variable de sesión 'modo'
   */
  public void onClick$btnControl2() {
    vistaActual = Sesion.getVistaActual();
    modo = Sesion.getModo();
    System.out.println("CtrlMenuLiderRed.btnControl2-modo: " + modo);
    System.out.println("CtrlMenuLiderRed.vistaActual: " + vistaActual);
    actualizarEstadoBarra();
  }

  private void cambiarPanelCentral(String vista) {
    //esta línea permite refrescar la vista, por si la nueva es igual a la actual
    panelCentral.setSrc("");
    panelCentral.setSrc(vista);
  }

  private void borrarVariablesSesionResultado() {
    Sesion.setVariable("resultOperacion", null);
  }

  /**
   * habilita opciones adicionales del menú,
   * de acuerdo al tipo de usuario
   */
  private void habilitarOpcionesUsuario() {
    esLiderCelula = (Boolean) Sesion.esLiderCelula();
    esLiderRed = (Boolean) Sesion.esLiderRed();
    tipoUsuario = Util.buscarTipoUsuario(this.getClass());
    if (esLiderCelula) {
      itemReporteCelulaListado.setVisible(true);
    }
    if (esLiderRed) {
      menuLiderRed.setVisible(true);
    }
    if (tipoUsuario == UsuarioUtil.ADMINISTRADOR_CELULAS) {
      System.out.println("usuario es administrador");
      menuAdmin.setVisible(true);
    } else {
      System.out.println("usuario no es administrador");
    }
  }
  /**
   * TODO - Mejora de código:
   * evaluar mejoras en estos método:
   * btnControl
   * btnControl2
   * cambiarVista
   * actualizarEstadoBarra
   * actualizarModo
   * notificarBarra (de cada vista)
   * @param vista 
   */
  int tipoUsuario = 1;
  boolean esLiderCelula = false;
  boolean esLiderRed = false;
}
