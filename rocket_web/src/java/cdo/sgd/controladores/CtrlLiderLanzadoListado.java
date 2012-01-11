package cdo.sgd.controladores;

import sig.controladores.Sesion;
import sig.controladores.Vistas;
import rocket.controladores.widgets.BotonLider;
import rocket.controladores.widgets.EtqNro;
import cdo.sgd.modelo.bd.simulador.*;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import waytech.modelo.interfaces.IsaAcceso;
import waytech.modelo.interfaces.IsaDiscipulo;
import waytech.modelo.servicios.SaAcceso;
import waytech.modelo.servicios.SaDiscipulo;
import waytech.utilidades.Util;

/**
 * controlador asociado a vistaDiscipuloLanzado/Listado
 * @author Gabriel
 */
public class CtrlLiderLanzadoListado extends GenericForwardComposer {

  //widgets:
  Label etqNro, etqDireccion;
  BotonLider tbbNombre, tbbLider1, tbbLider2;
  Toolbarbutton tbbTelefono, tbbEmail;
  Grid grid;
  //variable de control:
  String tipoUsuario; //{liderRed, liderCelula, administrador}
  int idRed;
  //datos:
  ArrayList lista;
  BD datos = new BD();
  //referencias
  Include vistaCentral;
  Include panelCentral;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  void inicio() {
    idRed = Util.buscarIdRed(this.getClass());
    mostrarDatos();
    notificarBarra();
  }

  public void mostrarDatos() {
    //limpiar variables de sesión:
    Sessions.getCurrent().setAttribute("idLider", 0);
    Sessions.getCurrent().setAttribute("idCelula", 0);
//CODIGO REALIZADO POR EL HERR
        //TODO:verificar tipo de usuario para ver qué data buscar y mostrar
        //Solo se monstrarán los discipulos directos de ésta persona que son 
        //lideres lanzados.
        IsaDiscipulo isaDiscipulo = new SaDiscipulo();
        IsaAcceso isaAcceso = new SaAcceso();
        //AQUI VA POR VARIABLE DE SESIÓN EL ID DE LA PERSONA QUE SE LOGEA.
        //ESTA PERSONA DEBE TENER SUS DISCIPULOS PARA QUE SE PUEDA MOSTRAR ALGO.        
        int idPersonaLogeada = 1;
        List<waytech.modelo.beans.sgi.Discipulo> todosSusDiscipulosBD = new ArrayList<waytech.modelo.beans.sgi.Discipulo>();
        todosSusDiscipulosBD = isaDiscipulo.listDiscipulo(idPersonaLogeada).getAllDiscipulos();
        int i = 0;
        List<DiscipuloLanzadoListado> discipuloLanzadoListados = new ArrayList<DiscipuloLanzadoListado>();
        for(waytech.modelo.beans.sgi.Discipulo discipuloBD:todosSusDiscipulosBD) {
            DiscipuloLanzadoListado discipuloLanzadoListado = new DiscipuloLanzadoListado();
            i++;
            discipuloLanzadoListado.setNroItem(i);
            discipuloLanzadoListado.setNombre(discipuloBD.getIdPersona2().getNombre() + " " + discipuloBD.getIdPersona2().getApellido());
            discipuloLanzadoListado.setDireccionCorta(discipuloBD.getIdPersona2().getDireccionHabitacion());
            discipuloLanzadoListado.setTelefono(discipuloBD.getIdPersona2().getTelefonoMovil());
            discipuloLanzadoListado.setEmail(isaAcceso.getAccesoPorIdUsuario(discipuloBD.getIdPersona2().getIdPersona()).getAcceso().getCorreo());
            discipuloLanzadoListados.add(discipuloLanzadoListado);
        }
        
        

        lista = (ArrayList) discipuloLanzadoListados;
    if (lista == null) {
      System.out.println("lista de lideres celula listado nula");
    }
    ListModelList model = new ListModelList(lista);
    grid.setModel(model);
    grid.setRowRenderer(new RowRenderer() {

      public void render(Row row, Object data) throws Exception {
        //se extrae la data
        DiscipuloLanzadoListado lider = (DiscipuloLanzadoListado) data;

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
        final int idDiscipuloLanzado = lider.getId();
        final int idLider1 = lider.getIdLider1();
        final int idLider2 = lider.getIdLider2();

        tbbNombre.setIdLider(idDiscipuloLanzado);
        tbbLider1.setIdLider(idLider1);
        tbbLider2.setIdLider(idLider2);

        //se anexan los widgets a la fila
        etqNro.setParent(row);
        tbbNombre.setParent(row);
        etqDireccion.setParent(row);
        tbbTelefono.setParent(row);
        tbbEmail.setParent(row);
        Vbox vbox = new Vbox();
        tbbLider1.setParent(vbox);
        tbbLider2.setParent(vbox);
        vbox.setParent(row);
      }
    });
  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.LIDER_LISTADO);
    Sesion.setModo("listado");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }
  
  public void onClick$btnNew() throws InterruptedException {
    Sesion.setVistaSiguiente(Vistas.LIDER);
    Sesion.setModo("new");
    ctrlVista.forzarCambioVista_btnControl();
  }  
  
  CtrlVista ctrlVista = new CtrlVista();  
}
