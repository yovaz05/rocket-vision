package sig.controladores;

import cdo.sgd.controladores.Sesion;
import cdo.sgd.controladores.Vistas;
import cdo.sgd.controladores.widgets.BotonCelula;
import cdo.sgd.controladores.widgets.BotonLider;
import cdo.sgd.controladores.widgets.EtqNro;
import cdo.sgd.modelo.bd.simulador.CelulaListadoUtil;
import cdo.sgd.modelo.bd.simulador.BD;
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
import waytech.modelo.beans.sgi.Celula;
import waytech.modelo.beans.sgi.PersonaEnCelula;
import waytech.modelo.interfaces.IsaCelula;
import waytech.modelo.interfaces.IsaPersonaEnCelula;
import waytech.modelo.servicios.SaCelula;
import waytech.modelo.servicios.SaPersonaEnCelula;
import waytech.utilidades.Util;
import waytech.utilidades.UtilSIG;

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
  BD datos;
  List<CelulaListadoUtil> listaCelulaListado = new ArrayList<CelulaListadoUtil>();
  ServicioCelula servicio = new ServicioCelula();

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  void inicio() {
    idRedUsuario = UtilSIG.buscarIdRed(this.getClass());
//TODO:verificar tipo de usuario para ver qué data buscar y mostrar
    getListado();
    mostrarDatos();
    notificarBarra();
  }

  /**
   * Obtiene la listaCelulaListado de células de la base de datos
   */
  void getListado() {
    listaCelulaListado = servicio.getCelulasListado();
  }

  /**
   * se encarga de traer los datos, y mostrarlos en el grid
   */
  public void mostrarDatos() {
    ListModelList model = new ListModelList(listaCelulaListado);
    grid.setModel(model);
    grid.setRowRenderer(new RowRenderer() {

      public void render(Row row, Object data) throws Exception {

        CelulaListadoUtil celula = (CelulaListadoUtil) data;

        System.out.println("CtrlCelulaListado.mostrarDatos.render(): celula=>" + celula.toString());
        //se crean los widgets con la data
        etqNro = new EtqNro("" + celula.getNroItem());
        etqDireccion = new Label(celula.getDireccionCorta());
        etqDiaHora = new Label(getDiaHora(celula.getDia(), celula.getHora()));
        etqNombre = new Label(celula.getNombre());
        tbbCelula = new BotonCelula(celula.getCodigo());
        tbbLider1 = new BotonLider(celula.getNombreLider1());
        tbbLider2 = new BotonLider(celula.getNombreLider2());
        tbbLider3 = new BotonLider(celula.getNombreLider3());
        tbbLider4 = new BotonLider(celula.getNombreLider4());

        //parámetros para navegación dinámica:
        final int idCelula = celula.getId();
        final int idLider1 = celula.getIdLider1();
        final int idLider2 = celula.getIdLider2();
        final int idLider3 = celula.getIdLider3();
        final int idLider4 = celula.getIdLider4();
        final int idRed = celula.getIdRed();

        tbbCelula.setIdCelula(idCelula);
        tbbCelula.setIdRed(idRed);
        tbbCelula.setNroLideres(celula.getNumeroLideres());
        tbbLider1.setIdLider(idLider1);
        tbbLider2.setIdLider(idLider2);
        tbbLider3.setIdLider(idLider3);
        tbbLider4.setIdLider(idLider4);

        //TODO: establecer atributos de estilo a los widgets, por ahora no ha hecho falta

        //se anexan los widgets a la fila
        etqNro.setParent(row);
        tbbCelula.setParent(row);
        Vbox vbox = new Vbox();
        tbbLider1.setParent(vbox);
        tbbLider2.setParent(vbox);
        tbbLider3.setParent(vbox);
        tbbLider4.setParent(vbox);
        vbox.setParent(row);
        etqDireccion.setParent(row);
        etqDiaHora.setParent(row);
        etqNombre.setParent(row);
      }
    });
    /**/
  }

  /**
   * arman cadena día y hora de la célula
   * @param dia 1:lunes,..., 7:domingo
   * @param hora 8>>800am,..., 20>>8.00 pm
   * @return día y hora formateada
   */
  //TODO: hacer las conversiones necesarias
  String getDiaHora(String dia, String hora) {
    return Util.convertirDiaSemanaTextoCompleto(dia) + ", " + Util.convertirHoraTextoCompleto(hora);
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
