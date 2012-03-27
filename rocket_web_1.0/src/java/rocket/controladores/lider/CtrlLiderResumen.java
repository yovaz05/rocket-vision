package rocket.controladores.lider;

import rocket.controladores.general.CtrlVista;
import rocket.controladores.general.Sesion;
import rocket.controladores.general.Vistas;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;
import rocket.controladores.general.Modos;
import rocket.modelo.servicios.ServicioLider;
import rocket.modelo.bd.util.LiderUtil;

/**
 * controlador asociado a vistaLiderResumen/LiderCelula
 * @author Gabriel Pérez
 */
public class CtrlLiderResumen extends GenericForwardComposer {

  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //widgets:
  Label etqNombre, etqRed, etqDireccion, etqTelefono, etqEmail;
  A tbbRed, tbbLider1, tbbLider2;
  Toolbarbutton tbbTelefono, tbbEmail;
  //TODO: MEJORA CODIGO: usar clase BotonLider
  A tbbVerMas;
  //variables de control:
  CtrlVista ctrlVista = new CtrlVista();
  private int id = 0;
  String cedula = "";
  String nombre = "";
  int idRed = 1;
  String nombreRed = "";
  int idEstado = 0;
  int idCiudad = 0;
  int idZona = 0;
  String nombreEstado = "";
  String nombreCiudad = "";
  String nombreZona = "";  //gestión de datos:
  //-BD datos;
  ServicioLider servicio = new ServicioLider();
  LiderUtil lider = new LiderUtil();

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    buscarVarSesion();
    buscarData();
    mostrarData();
    notificarBarra();
  }

  //TODO: idUsuario debe ser seteado al momento del acceso del usuario
  /**
   * recupera parámetro de sesión 'idUsuario' que viene de la vista llamante
   */
  private void buscarVarSesion() throws InterruptedException {
    try {
      if (Sesion.getVariable("idLider") == null) {
        System.out.println("CtrlPerfilDatos -> ERROR: variable de sesión 'idLider' = null");
        return;
      }
      id = (Integer) Sesion.getVariable("idLider");
    } catch (Exception e) {
      id = 0; //valor para evitar error de valor nulo. TODO: Debe ser '1'
      System.out.println("CtrlPerfilDatos -> ERROR: variable de sesión 'idLider'");
    } finally {
      System.out.println("CtrlPerfil -> id = " + id);
    }
  }

  /**
   * obtiene la data de la base de datos
   * con el id de la persona
   */
  void buscarData() {
    lider = servicio.getPersona(id);
    if (lider == null) {
      System.out.println("CtrlPerfil.buscarData: NULL");
    } else {
      System.out.println("CtrlPerfil.buscarData.usuario=" + lider.toString());
    }
    setVariablesSesion();
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
    Sesion.setVariable("idLider", id);
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
   * muestra datos en la vista
   * por ahora son simulados  
   **/
  public void mostrarData() {
    /**/
    System.out.println("CtrlPerfil.id = " + id);
    if (lider != null) {
      System.out.println("lider != null");
    }
    etqNombre.setValue("" + lider.getNombre());
    //+ tbbRed.setLabel("" + usuario.getNombreRed());
    etqRed.setValue("" + lider.getNombreRed());

    idRed = lider.getIdRed();
    String lider1 = lider.getNombreLider1();
    String lider2 = lider.getNombreLider2();
    final int idLider1 = lider.getIdLider1();
    final int idLider2 = lider.getIdLider2();
    //**
    /*
    System.out.println("lider1= " + lider1);
    System.out.println("lider2= " + lider2);
     */


    /* + adelante
    tbbRed.setTooltiptext("Ver detalles... (idRed=" + idRed + ")");
    tbbRed.addEventListener(Events.ON_CLICK, new EventListener() {
    
    public void onEvent(Event event) throws Exception {
    Sesion.setVariable("idRed", idRed);
    panelCentral.setSrc("vistaRed/Red.zul");
    }
    });
     */

    /*+adelante
    tbbLider1.setTooltiptext("Ver detalles...");
    tbbLider2.setTooltiptext("Ver detalles...");
    tbbLider3.setTooltiptext("Ver detalles...");
    tbbLider4.setTooltiptext("Ver detalles...");
    
    if ((lider1 != null) && (lider1.equals(""))) {
    tbbLider1.setLabel("");
    tbbLider1.setVisible(false);
    tbbLider1.setHref(null);
    } else if (lider1.equals("Pastor Rogelio Mora") || lider1.equals("Pastora Irene de Mora")) {
    tbbLider1.setLabel(lider1);
    tbbLider1.setHref(null);
    tbbLider1.setTooltiptext("Enlace no funcional");
    tbbLider1.addEventListener(Events.ON_CLICK, new EventListener() {
    
    public void onEvent(Event event) throws Exception {
    //no hace nada, sin navegación
    }
    });
    } else {
    tbbLider1.setLabel("" + lider1);
    tbbLider1.addEventListener(Events.ON_CLICK, new EventListener() {
    
    public void onEvent(Event event) throws Exception {
    Sesion.setVariable("idLider", idLider1);
    panelCentral.setSrc("");
    panelCentral.setSrc("vistaLiderResumen/Lider.zul");
    }
    });
    }
    
    if ((lider2 != null) && (lider2.equals(""))) {
    tbbLider2.setLabel("");
    tbbLider2.setVisible(false);
    tbbLider2.setHref(null);
    } else {
    tbbLider2.setLabel(lider2);
    tbbLider2.addEventListener(Events.ON_CLICK, new EventListener() {
    
    public void onEvent(Event event) throws Exception {
    Sesion.setVariable("idLider", idLider2);
    panelCentral.setSrc("");
    panelCentral.setSrc("vistaLiderResumen/Lider.zul");
    }
    });
    }
     */

    etqDireccion.setValue(lider.getDireccionCorta());
    etqTelefono.setValue("" + lider.getTelefono());
    etqEmail.setValue("" + lider.getEmail());

    tbbVerMas.setTooltiptext("Ver detalles...");
    tbbVerMas.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        /*
        Sesion.setVariable("idLider", idUsuario);
        Sesion.setVariable("lider.idRed", idRed);
        Sesion.setVariable("modo", Modos.MODO_EDICION_DINAMICA);
        panelCentral.setSrc(Vistas.LIDER);
        System.out.println("CtrlPerfilDatos -> idLider = " + idUsuario);
         */
        Sesion.setVistaSiguiente(Vistas.LIDER);
        Sesion.setModo(Modos.EDICION_DINAMICA);
        Sesion.setVariable("idLider", id);
        Sesion.setVariable("lider.idRed", idRed);
        ctrlVista.forzarCambioVista_btnControl();
      }
    });
    /**/
  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.CUENTA_PERFIL);
    //TODO: settear modo q permita edición directa: 'permitir_edicion'
    //Sesion.setModo("permitir_edicion");
    Sesion.setModo("consulta");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }
  /**
  public void onClick$btnEditar() {
  //actualizar variables de sesión:
  Sesion.setVistaActual(Vistas.CUENTA_PERFIL);
  Sesion.setVariable("idLider", id);
  Sesion.setVistaSiguiente(Vistas.LIDER);
  Sesion.setModo("editar");  //modo edición
  //envia click al btnControl de CtrlBarraMenu, para que cambie la vista
  ctrlVista.forzarCambioVista_btnControl();
  }  
   */
}