package sig.controladores.lider;

import cdo.sgd.controladores.CtrlVista;
import sig.controladores.Sesion;
import sig.controladores.Vistas;
import cdo.sgd.controladores.widgets.BotonLider;
import cdo.sgd.controladores.widgets.EtqNro;
import cdo.sgd.modelo.bd.util.*;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Toolbarbutton;
import sig.modelo.servicios.ServicioPersona;
import waytech.utilidades.UtilSIG;

/**
 * controlador asociado a vistaDiscipuloLanzado/Listado
 * @author Gabriel
 */
public class CtrlLiderListado extends GenericForwardComposer {

  //widgets:
  Label etqNro, etqDireccion;
  BotonLider tbbNombre, tbbLider1, tbbLider2;
  Toolbarbutton tbbTelefono, tbbEmail;
  Grid grid;
  //variable de control:
  String tipoUsuario; //{liderRed, liderCelula, administrador}
  int idRed;
  CtrlVista ctrlVista = new CtrlVista();
  //datos:
  ArrayList lista;
  //referencias
  Include vistaCentral;
  Include panelCentral;
  //gestión de datos:
  ServicioPersona servicioLider = new ServicioPersona();
  List<LiderListadoUtil> listaLiderListado = new ArrayList<LiderListadoUtil>();

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  void inicio() {
    idRed = UtilSIG.buscarIdRed(this.getClass());
    buscarData();
    mostrarData();
    notificarBarra();
  }

  void buscarData() {
    listaLiderListado = servicioLider.getTodosLideresLanzadosListado();
  }

  public void mostrarData() {
    /**
     * TODO:verificar tipo de usuario para ver qué data buscar y mostrar
     * y qué privilegios tiene
     **/
    lista = (ArrayList) listaLiderListado;
    if (lista == null) {
      System.out.println("lista de lideres celula listado nula");
    }
    ListModelList model = new ListModelList(lista);
    grid.setModel(model);
    grid.setRowRenderer(new RowRenderer() {

      public void render(Row row, Object data) throws Exception {
        //se extrae la data
        LiderListadoUtil lider = (LiderListadoUtil) data;

        //se crean los widgets con la data
        etqNro = new EtqNro("" + lider.getNroItem());
        tbbNombre = new BotonLider("" + lider.getNombre());
        etqDireccion = new Label("" + lider.getDireccionCorta());
        tbbTelefono = new Toolbarbutton("" + lider.getTelefono());
        tbbEmail = new Toolbarbutton("" + lider.getEmail());
        tbbLider1 = new BotonLider("" + lider.getNombreLider1());
        tbbLider2 = new BotonLider("" + lider.getNombreLider2());

        //TODO: establecer atributos de estilo a los widgets

        //se establecen parámetros para navegación dinámica:
        final int id = lider.getId();
        final int idRed = lider.getIdRed();
        final int idLider1 = lider.getIdLider1();
        final int idLider2 = lider.getIdLider2();

        tbbNombre.setIdRed(idRed);
        tbbNombre.setIdLider(id);
        tbbLider1.setIdLider(idLider1);
        tbbLider2.setIdLider(idLider2);

        //se anexan los widgets a la fila
        etqNro.setParent(row);
        tbbNombre.setParent(row);
        etqDireccion.setParent(row);
        tbbTelefono.setParent(row);
        tbbEmail.setParent(row);
        /*
        Vbox vbox = new Vbox();
        tbbLider1.setParent(vbox);
        tbbLider2.setParent(vbox);
        vbox.setParent(row);
        */
      }
    });
  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.DISCIPULO_LANZADO_LISTADO);
    Sesion.setModo("listado");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }

  public void onClick$btnNew() throws InterruptedException {
    Sesion.setVistaSiguiente(Vistas.LIDER);
    Sesion.setModo("new");
    ctrlVista.forzarCambioVista_btnControl();
  }

  //Será usado más adelante
  //Lista todos los discípulos de un líder específico
  /*
  void listarDiscipulosLanzadosPorLider(int idLider) {
    //CODIGO REALIZADO POR EL HERR
    //Solo se monstrarán los discipulos directos de ésta persona que son líderes lanzados.
    IsaDiscipulo servDiscipulo = new SaDiscipulo();
    IsaAcceso servAcceso = new SaAcceso();
    //AQUI VA POR VARIABLE DE SESIÓN EL ID DEL USUARIO LOGUEADO
    //ESTA PERSONA DEBE TENER SUS DISCIPULOS PARA QUE SE PUEDA MOSTRAR ALGO.
    int idPersonaLogeada = 3; // 3 = Líder Gabriel Pérez      
    List<waytech.modelo.beans.sgi.Discipulo> todosSusDiscipulosBD = new ArrayList<waytech.modelo.beans.sgi.Discipulo>();
    todosSusDiscipulosBD = servDiscipulo.listDiscipulo(idPersonaLogeada).getAllDiscipulos();
    int i = 0;
    List<LiderListadoUtil> discipuloLanzadoListados = new ArrayList<LiderListadoUtil>();
    for (waytech.modelo.beans.sgi.Discipulo discipuloBD : todosSusDiscipulosBD) {
      LiderListadoUtil discipuloLanzadoListado = new LiderListadoUtil();
      i++;
      discipuloLanzadoListado.setNroItem(i);
      discipuloLanzadoListado.setNombre(discipuloBD.getIdPersona2().getNombre());
      discipuloLanzadoListado.setDireccionCorta(discipuloBD.getIdPersona2().getDireccionHabitacion());
      discipuloLanzadoListado.setTelefono(discipuloBD.getIdPersona2().getTelefonoMovil());
      Acceso usuario = servAcceso.getAccesoPorIdUsuario(discipuloBD.getIdPersona2().getIdPersona()).getAcceso();
      //esta línea no está funcionando
      discipuloLanzadoListado.setEmail(usuario.getCorreo());
      discipuloLanzadoListados.add(discipuloLanzadoListado);
    }
    lista = (ArrayList) discipuloLanzadoListados;
  }
   * 
   */
}
