package sig.controladores.celula;

import sig.controladores.Sesion;
import sig.controladores.Vistas;
import rocket.controladores.widgets.BotonCelula;
import rocket.controladores.widgets.BotonLider;
import rocket.controladores.widgets.EtqNro;
import sig.modelo.bd.util.CelulaListadoUtil;
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
import org.zkoss.zul.Vbox;
import sig.modelo.servicios.ServicioCelula;
import waytech.utilidades.Util;

/**
 * Controlador asociado a listado de células:
 * vistaCelula/Listado.zul
 * @author Gabriel
 */
public class CtrlCelulaListado extends GenericForwardComposer {

  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //widgets:
  Grid grid;
  Label etqNro, etqDireccion, etqDiaHora, etqNombre;
  BotonCelula tbbCelula;
  BotonLider tbbLider1, tbbLider2, tbbLider3, tbbLider4;
  //variables de control:
  int idRedUsuario;
  //gestión de datos:
  List<CelulaListadoUtil> lista = new ArrayList<CelulaListadoUtil>();
  ServicioCelula servicioCelula = new ServicioCelula();

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  void inicio() {
    //idRed se usará más adelante
    //+idRedUsuario = Util.buscarIdRed(this.getClass());
    //+TODO:verificar tipo de usuario para ver qué data buscar y mostrar
    buscarData();
    mostrarDatos();
    notificarBarra();
  }

  /**
   * Obtiene la lista de células de la base de datos
   */
  void buscarData() {
    lista = servicioCelula.getCelulasListado();
  }

  /**
   * se encarga de traer los datos, y mostrarlos en el grid
   */
  public void mostrarDatos() {
    ListModelList model = new ListModelList(lista);
    grid.setModel(model);
    grid.setRowRenderer(new RowRenderer() {

      //gestiona cada fila del grid, el objeto data contiene a cada elemento de la colección
      public void render(Row row, Object data) throws Exception {

        CelulaListadoUtil celula = (CelulaListadoUtil) data;

        //**System.out.println("CtrlCelulaListado.mostrarDatos.render(): celula=>" + celula.toString());

        //parámetros para navegación dinámica:
        final int idCelula = celula.getId();
        final int idLider1 = celula.getIdLider1();
        final int idLider2 = celula.getIdLider2();
        final int idLider3 = celula.getIdLider3();
        final int idLider4 = celula.getIdLider4();
        final int idRed = celula.getIdRed();

        //se crean los widgets con la data
        etqNro = new EtqNro("" + celula.getNroItem());
        etqDireccion = new Label(celula.getDireccionCorta());
        etqDiaHora = new Label(Util.getDiaHora(celula.getDia(), celula.getHora()));
        tbbCelula = new BotonCelula(celula.getCodigo());

        tbbCelula.setIdCelula(idCelula);
        tbbCelula.setIdRed(idRed);
        int nroLideres = celula.getNumeroLideres();
        tbbCelula.setNroLideres(nroLideres);

        tbbLider1 = new BotonLider(celula.getNombreLider1());
        tbbLider2 = new BotonLider(celula.getNombreLider2());
        tbbLider3 = new BotonLider(celula.getNombreLider3());
        tbbLider4 = new BotonLider(celula.getNombreLider4());
        tbbLider1.setIdLider(idLider1);
        tbbLider2.setIdLider(idLider2);
        tbbLider3.setIdLider(idLider3);
        tbbLider4.setIdLider(idLider4);

        String nombre = celula.getNombre();
        if (nombre.isEmpty()) {
          nombre = "No asignado";
        }
        etqNombre = new Label(nombre);

        //TODO: establecer atributos de estilo a los widgets, por ahora no ha hecho falta

        //se anexan los widgets a la fila del grid
        etqNro.setParent(row);
        tbbCelula.setParent(row);
        if (nroLideres == 0) {//célula no tiene líderes          
          Label etqNoTieneLideres = new Label("No asignados");
          etqNoTieneLideres.setParent(row);
        } else {
          Vbox vbox = new Vbox();
          tbbLider1.setParent(vbox);
          tbbLider2.setParent(vbox);
          tbbLider3.setParent(vbox);
          tbbLider4.setParent(vbox);
          vbox.setParent(row);
        }
        etqDireccion.setParent(row);
        etqDiaHora.setParent(row);
        etqNombre.setParent(row);
      }
    });
  }


  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.CELULA_LISTADO);
    Sesion.setModo("listado");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }
}
