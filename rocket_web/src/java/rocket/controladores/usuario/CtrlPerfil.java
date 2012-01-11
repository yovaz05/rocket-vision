package rocket.controladores.usuario;

import cdo.sgd.controladores.CtrlVista;
import sig.controladores.Sesion;
import sig.controladores.Vistas;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;
import rocket.modelo.servicios.ServicioPersona;
import sig.modelo.bd.util.LiderUtil;

/**
 * controlador asociado a vistaLiderResumen/LiderCelula
 * @author Gabriel Pérez
 */
public class CtrlPerfil extends GenericForwardComposer {

  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //widgets:
  Label etqNombre, etqRed, etqDireccion, etqTelefono, etqEmail;
  A tbbRed, tbbLider1, tbbLider2;
  Toolbarbutton tbbTelefono, tbbEmail;
  A tbbVerMas;
  //variables de control:
  private int idUsuario = 0;
  //gestión de datos:
  //-BD datos;
  ServicioPersona servicio = new ServicioPersona();
  LiderUtil usuario = new LiderUtil();

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    //- crearDatos();
    //- datos = new BD();
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
      //TODO: evaluar qué se hará si no viene de navegación dinámica...
      if (Sessions.getCurrent().getAttribute("idUsuario") == null) {
        System.out.println("CtrlPerfilDatos -> ERROR: variable de sesión 'idUsuario' = null");
        return;
      }
      idUsuario = (Integer) Sesion.getVariable("idUsuario");
      if (idUsuario == 0) {
        System.out.println("CtrlPerfilDatos -> ERROR: variable de sesión 'idUsuario' = 0");
      }
    } catch (Exception e) {
      System.out.println("CtrlPerfilDatos -> ERROR: variable de sesión 'idUsuario'");
      idUsuario = 0; //valor de prueba, datos en blanco
    } finally {
      System.out.println("CtrlPerfil -> id = " + idUsuario);
    }
  }

  /**
   * obtiene la data de la base de datos
   * con el id de la persona
   */
  void buscarData() {
    usuario = servicio.getPersona(idUsuario);
    if (usuario == null) {
      System.out.println("CtrlPerfil.buscarData: NULL");
    } else {
      System.out.println("CtrlPerfil.buscarData.usuario=" + usuario.toString());
    }
    //+ setVariablesSesion();
  }

  /**
   * muestra datos en la vista
   * por ahora son simulados  
   **/
  public void mostrarData() {
    /**/
    System.out.println("CtrlPerfil.id = " + idUsuario);
    if (usuario != null) {
      System.out.println("lider != null");
    }
    etqNombre.setValue("" + usuario.getNombre());
    //+ tbbRed.setLabel("" + usuario.getNombreRed());
    etqRed.setValue("" + usuario.getNombreRed());

    final int idRed = usuario.getIdRed();
    String lider1 = usuario.getNombreLider1();
    String lider2 = usuario.getNombreLider2();
    final int idLider1 = usuario.getIdLider1();
    final int idLider2 = usuario.getIdLider2();
    //**
    /*
    System.out.println("lider1= " + lider1);
    System.out.println("lider2= " + lider2);
     */


    /* + adelante
    tbbRed.setTooltiptext("Ver detalles... (idRed=" + idRed + ")");
    tbbRed.addEventListener(Events.ON_CLICK, new EventListener() {
    
    public void onEvent(Event event) throws Exception {
    Sessions.getCurrent().setAttribute("idRed", idRed);
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
    Sessions.getCurrent().setAttribute("idLider", idLider1);
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
    Sessions.getCurrent().setAttribute("idLider", idLider2);
    panelCentral.setSrc("");
    panelCentral.setSrc("vistaLiderResumen/Lider.zul");
    }
    });
    }
     */

    etqDireccion.setValue(usuario.getDireccionCorta());
    etqTelefono.setValue("" + usuario.getTelefono());
    etqEmail.setValue("" + usuario.getEmail());

    tbbVerMas.setTooltiptext("Ver detalles...");
    tbbVerMas.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sessions.getCurrent().setAttribute("idLider", idUsuario);
        Sessions.getCurrent().setAttribute("modo", "ver");
        panelCentral.setSrc("vistaLider/Lider.zul");
        System.out.println("CtrlPerfilDatos -> idLider = " + idUsuario);
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
  CtrlVista ctrlVista = new CtrlVista();
}