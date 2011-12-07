package cdo.sgd.controladores;

import cdo.sgd.controladores.widgets.BotonCelula;
import cdo.sgd.controladores.widgets.BotonDiscipulo;
import cdo.sgd.controladores.widgets.BotonLider;
import cdo.sgd.controladores.widgets.EtqNro;
import cdo.sgd.modelo.bd.simulador.*;
import java.util.ArrayList;
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
import org.zkoss.zul.Vbox;
import waytech.utilidades.UtilSIG;

/**
 * controlador asociado a vista: DiscipuloProceso/Listado
 * @author Gabriel
 */
public class CtrlDiscipuloProcesoListado extends GenericForwardComposer {

  //widgets:
  Grid grid;
  Label etqNro, etqDireccion, etqEstatusProceso;
  Toolbarbutton tbbTelefono, tbbEmail;
  BotonDiscipulo tbbNombre;
  BotonLider tbbLider1, tbbLider2;
  BotonCelula tbbCelula;
  //variable de control:
  String tipoUsuario; //{liderRed, liderCelula, administrador}
  int idRed;
  //datos:
  BD datos;
  ArrayList lista;
  //referencias
  Include vistaCentral;
  Include panelCentral;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  void inicio() {
    idRed = UtilSIG.buscarIdRed(this.getClass());
    mostrarDatos();
    notificarBarra();
  }

  public void mostrarDatos() {
    datos = new BD();

    //TODO:verificar tipo de usuario para ver qué data buscar y mostrar

    //TODO: cambiar
    lista = datos.getDiscipulosProcesoPorRed(idRed);
    if (lista == null) {
      System.out.println("lista de lideres celula listado nula");
    }
    ListModelList model = new ListModelList(lista);
    grid.setModel(model);
    grid.setRowRenderer(new RowRenderer() {

      public void render(Row row, Object data) throws Exception {
        //se extrae la data
        DiscipuloProcesoListado discipulo = (DiscipuloProcesoListado) data;

        /**/
        //se crean los widgets con la data
        etqNro = new EtqNro("" + discipulo.getNroItem());
        tbbNombre = new BotonDiscipulo("" + discipulo.getNombre());
        etqEstatusProceso = new Label("" + discipulo.getEstatusProceso());
        etqDireccion = new Label("" + discipulo.getDireccionCorta());
        tbbTelefono = new Toolbarbutton("" + discipulo.getTelefono());
        tbbEmail = new Toolbarbutton("" + discipulo.getEmail());
        tbbLider1 = new BotonLider("" + discipulo.getNombreLider1());
        tbbLider2 = new BotonLider("" + discipulo.getNombreLider2());
        tbbCelula = new BotonCelula("" + discipulo.getDescripcionCelula());

        //TODO: establecer atributos de estilo a los widgets

        //se establecen parámetros para navegación dinámica:
        final int idDiscipulo = discipulo.getId();
        String lider1 = discipulo.getNombreLider1();
        String lider2 = discipulo.getNombreLider2();
        final int idLider1 = discipulo.getIdLider1();
        final int idLider2 = discipulo.getIdLider2();
        final int idCelula = discipulo.getIdCelula();

        tbbNombre.setIdDiscipulo(idDiscipulo);
        tbbLider1.setIdLider(idLider1);
        tbbLider2.setIdLider(idLider2);
        tbbCelula.setIdCelula(idCelula);

        //se anexan los widgets:
        etqNro.setParent(row);
        tbbNombre.setParent(row);
        etqEstatusProceso.setParent(row);
        etqDireccion.setParent(row);
        tbbTelefono.setParent(row);
        tbbEmail.setParent(row);
        Vbox vbox = new Vbox();
        tbbLider1.setParent(vbox);
        tbbLider2.setParent(vbox);
        vbox.setParent(row);
        tbbCelula.setParent(row);
      }
    });

  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.DISCIPULO_PROCESO_LISTADO);
    Sesion.setModo("listado");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }
  
  public void onClick$btnNew() throws InterruptedException {
    Sesion.setVistaSiguiente(Vistas.DISCIPULO_PROCESO);
    Sesion.setModo("new");
    ctrlVista.forzarCambioVista_btnControl();
  }  
  
  CtrlVista ctrlVista = new CtrlVista();
  
}
