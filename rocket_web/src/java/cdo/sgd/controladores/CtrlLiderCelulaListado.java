package cdo.sgd.controladores;

import sig.controladores.Sesion;
import sig.controladores.Vistas;
import cdo.sgd.controladores.widgets.BotonCelula;
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
import waytech.utilidades.UtilSIG;

/**
 * controlador asociado a vistaLiderResumen/Listado
 * @author Gabriel
 */
public class CtrlLiderCelulaListado extends GenericForwardComposer {

  //widgets:
  Label etqNro, etqDireccion;
  Toolbarbutton tbbTelefono, tbbEmail;
  BotonLider tbbNombre;
  BotonCelula tbbCelula;
  Grid grid;
  //variable de control:
  int idRed;
  //datos:
  ArrayList<LiderCelulaListado> lista;
  BD datos;

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
    //TODO:verificar tipo de usuario para ver qué data buscar y mostrar
    datos = new BD();
    lista = datos.getLideresCelulaPorRed(idRed);
    if (lista == null) {
      System.out.println("lista de lideres celula listado nula");
    }
    ListModelList model = new ListModelList(lista);
    grid.setModel(model);
    grid.setRowRenderer(new RowRenderer() {

      public void render(Row row, Object data) throws Exception {
        //se extrae la data
        LiderCelulaListado liderCelula = (LiderCelulaListado) data;

        //se crean los widgets con la data
        etqNro = new EtqNro("" + liderCelula.getNroItem());
        tbbNombre = new BotonLider("" + liderCelula.getNombre());
        etqDireccion = new Label("" + liderCelula.getDireccionCorta());
        tbbTelefono = new Toolbarbutton("" + liderCelula.getTelefono());
        tbbEmail = new Toolbarbutton("" + liderCelula.getEmail());
        //tbbCelula = new Toolbarbutton("" + liderCelula.getDescripcionCelula());
        tbbCelula = new BotonCelula("" + liderCelula.getDescripcionCelula());

        //TODO: establecer atributos de estilo a los widgets

        //se establecen parámetros de los widgets:
        final int idLider = liderCelula.getId();
        final int idCelula = liderCelula.getIdCelula();
        tbbNombre.setIdLider(idLider);
        tbbCelula.setIdCelula(idCelula);

        //se anexan los widgets a la fila
        etqNro.setParent(row);
        tbbNombre.setParent(row);
        etqDireccion.setParent(row);
        tbbTelefono.setParent(row);
        tbbEmail.setParent(row);
        tbbCelula.setParent(row);
      }
    });
  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.CELULA_LIDER_LISTADO);
    Sesion.setModo("consulta");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }
  //referencias:
  Include vistaCentral;
  Include panelCentral;
}
