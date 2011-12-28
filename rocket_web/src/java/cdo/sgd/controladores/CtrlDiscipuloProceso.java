package cdo.sgd.controladores;

import sig.controladores.Sesion;
import sig.controladores.Vistas;
import cdo.sgd.modelo.bd.simulador.BD;
import cdo.sgd.modelo.bd.simulador.DiscipuloProceso;
import cdo.sgd.modelo.bd.simulador.Lider;
import java.util.ArrayList;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

/**
 * controlador asociado a vista de: DiscipuloProceso
 * @author Gabriel
 */
public class CtrlDiscipuloProceso extends GenericForwardComposer {

  //widgets:
  Label tituloVentana;
  Toolbarbutton btnNew;
  Toolbarbutton btnGuardar;
  Toolbarbutton btnEditar;
  Tabbox tabbox;
  //pestaña "Datos Básicos"
  Column db$col2;
  Column db$col3;
  Textbox db$txtCedula;
  Textbox db$txtNombre;
  Label db$etqNombre;
  Label db$etqCedula;
  A db$tbbRed;
  A db$tbbLider1;
  A db$tbbLider2;
  //pestaña "Datos Personales"
  Column dp$col2;
  Column dp$col3;
  Datebox dp$dboxFechaNacimiento;
  Label dp$etqFechaNacimiento;
  Label dp$etqEdad;
  Label dp$etqEstadoCivil;
  Label dp$etqProfesion;
  //pestaña "Dirección"
  Column dir$colEdit;
  Column dir$colView;
  Combobox dir$cmbEstado;
  Label dir$etqEstado;
  Label dir$etqCiudad;
  Label dir$etqZona;
  Label dir$etqDirDetallada;
  Toolbarbutton dir$tbbTelefono;
  //pestaña "Contacto"
  Grid contacto$gridView;
  Grid contacto$gridEdit;
  Toolbarbutton contacto$tbbTlfCelular;
  Toolbarbutton contacto$tbbTlfHabitacion;
  Toolbarbutton contacto$tbbTlfTrabajo;
  Toolbarbutton contacto$tbbEmail;
  Toolbarbutton contacto$tbbFacebook;
  Toolbarbutton contacto$tbbTwitter;
  //pestaña "Fechas"
  Column fechas$col2;
  Column fechas$col3;
  Label fechas$etqConversion;
  Label fechas$etqEncuentro;
  Label fechas$etqBautizo;
  Label fechas$etqGraduacionAcademia;
  Label fechas$etqLanzamiento;
  //pestaña "Roles"
  Column celula$col2;
  Column celula$col3;
  Label celula$etqEstacaCelula;
  Label celula$etqAnfitrionCelula;
  //pestaña "Observaciones"
  Column obs$colEdit;
  Column obs$colView;
  Label obs$etqObservaciones;
  Textbox obs$txtObservaciones;
  //variables de control:
  int tabSeleccionado;
  //modoActual = {new,edicion,ver,imprimir}
  String modo;
  String titulo = "Discípulo en Proceso";
  int id;
  //gestión de datos:
  BD datos;
  ArrayList<Lider> lista;
  //referencias:
  Include vistaCentral;
  Include panelCentral;
  private String nombre;

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    datos = new BD();
    /*TODO: mejorar este código*/
    modo = Sesion.getModo();
    if (modo == null) {
      modo = "ver";
    }
    if (modo.equals("ver")) {
      getID();
      mostrarDatos();
    }
    actualizarEstado();
    notificarBarra();
  }

  /**
   * recupera parámetro idCelula que viene de la vista llamante
   */
  //TODO: evaluar qué se hará si no viene de navegación dinámica...
  private void getID() throws InterruptedException {
    try {
      id = (Integer) Sessions.getCurrent().getAttribute("idDiscipulo");
      if (id != 0) {
        System.out.println("CtrlDiscipuloProceso -> id = " + id);
      }
    } catch (Exception e) {
      Messagebox.show("CtrlDiscipuloProceso -> ERROR: parámetro idDiscipulo nulo... " + e);
    }
  }

  /**
   * se muestran datos en la vista
   * por ahora son simulados  
   */
  public void mostrarDatos() {
    System.out.println("CtrlDiscipuloProceso.mostrarDatos >> idDiscipulo: " + id);

    //llenar widgets con la data
    DiscipuloProceso discipulo = (DiscipuloProceso) datos.buscarDiscipuloProceso(id);
    System.out.println("DISCIPULO: " + discipulo.toString());

    db$etqCedula.setValue("" + discipulo.getCedula());

    /**/
    nombre = discipulo.getNombre();
    db$etqNombre.setValue(nombre);
    db$tbbRed.setLabel(discipulo.getNombreRed());
    db$tbbLider1.setLabel(discipulo.getNombreLider1());

    /**/
    dp$etqFechaNacimiento.setValue(discipulo.getFechaNacimiento());
    dp$etqEdad.setValue("" + discipulo.getEdad());
    dp$etqEstadoCivil.setValue(discipulo.getEstadoCivil());
    dp$etqProfesion.setValue(discipulo.getProfesionOficio());

    /**/
    dir$etqEstado.setValue(discipulo.getDireccion().getEstado());
    dir$etqCiudad.setValue(discipulo.getDireccion().getCiudad());
    dir$etqZona.setValue(discipulo.getDireccion().getZona());
    dir$etqDirDetallada.setValue(discipulo.getDireccion().getDirDetallada());
    dir$tbbTelefono.setLabel(discipulo.getDireccion().getTelefono());

    contacto$tbbTlfCelular.setLabel(discipulo.getTelefonoCelular());
    contacto$tbbTlfHabitacion.setLabel(discipulo.getTelefonoHabitacion());
    contacto$tbbTlfTrabajo.setLabel(discipulo.getTelefonoTrabajo());
    contacto$tbbEmail.setLabel(discipulo.getEmail());
    contacto$tbbFacebook.setLabel(discipulo.getFacebook());
    contacto$tbbTwitter.setLabel(discipulo.getTwitter());

    /**/
    fechas$etqConversion.setValue("" + discipulo.getFechaConversion());
    fechas$etqEncuentro.setValue("" + discipulo.getFechaEncuentro());
    fechas$etqGraduacionAcademia.setValue("" + discipulo.getFechaGraduacionAcademia());
    fechas$etqLanzamiento.setValue("" + discipulo.getFechaLanzamiento());
    fechas$etqBautizo.setValue("" + discipulo.getFechaBautizo());

    /**/
    mostrarRol(celula$etqEstacaCelula, discipulo.isEstacaCelula());
    mostrarRol(celula$etqAnfitrionCelula, discipulo.isAnfitrionCelula());
    /**/

    obs$etqObservaciones.setValue(discipulo.getObservaciones());

    //se establecen parámetros para navegación dinámica:
    String lider1 = discipulo.getNombreLider1();
    String lider2 = discipulo.getNombreLider2();
    String red = discipulo.getNombreRed();
    final int idRed = discipulo.getIdRed();
    final int idLider1 = discipulo.getIdLider1();
    final int idLider2 = discipulo.getIdLider2();


    db$tbbRed.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sesion.setVariable("idRed", idRed);
        panelCentral.setSrc("vistaRed/Red.zul");
        System.out.println("CtrlDiscipuloProceso -> idRed = " + idRed);
      }
    });

    db$tbbLider1.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sesion.setVariable("idLider", idLider1);
        panelCentral.setSrc(Vistas.LIDER_RESUMEN);
        System.out.println("CtrlCelula -> idLider = " + idLider1);
      }
    });
    if ((idLider2 == 0) || (lider2 == null) || lider2.equals("") || lider2.equals("Pastora Irene de Mora")) {
      db$tbbLider2.setVisible(false);
    } else {
      db$tbbLider2.setLabel(lider2);
      db$tbbLider2.addEventListener(Events.ON_CLICK, new EventListener() {

        public void onEvent(Event event) throws Exception {
          Sesion.setVariable("idLider", idLider2);
          panelCentral.setSrc(Vistas.LIDER_RESUMEN);
        }
      });
    }
    obs$etqObservaciones.setValue(discipulo.getObservaciones());
  }

  /**
   * setea la etiqueta del rol con los valores correspondientes: sí/no,
   * dependiendo del status true/false
   * @param etqRol
   * @param status true o false
   */
  private void mostrarRol(Label etqRol, boolean status) {
    if (status) {
      etqRol.setValue("Sí");
      etqRol.setSclass("checksi");
    } else {
      etqRol.setValue("No");
      etqRol.setSclass("checkno");
    }
  }

  public void actualizarEstado() throws InterruptedException {
    if (modo.equals("new")) {
      //btnNew.setVisible(false);
      //btnEditar.setVisible(false);
      //btnGuardar.setVisible(true);
      verElementosEntrada(true);
      mostrarElementosVisualizacion(false);
      tituloVentana.setValue(titulo + " » Ingresar");
      setFocoEdicion();
    } else if (modo.equals("ver")) {
      //btnNew.setVisible(true);
      //btnGuardar.setVisible(false);
      //btnEditar.setVisible(true);
      verElementosEntrada(false);
      mostrarElementosVisualizacion(true);
      tituloVentana.setValue(titulo + ": " + nombre);
    } else if (modo.equals("editar")) {
      //btnNew.setVisible(false);
      //btnEditar.setVisible(false);
      //btnGuardar.setVisible(true);
      verElementosEntrada(true);
      mostrarElementosVisualizacion(false);
      setFocoEdicion();
      tituloVentana.setValue(titulo + ": " + nombre + " » Editando");
    }
  }

  public void onClick$btnNew() throws InterruptedException {
    modo = "new";
    actualizarEstado();
  }

  public void onClick$btnGuardar() throws InterruptedException {
    /**
     * modoActual = "procesando";
     * aquí va el procesamiento de los datos...
     */
    modo = "ver";
    actualizarEstado();
  }

  public void onClick$btnEditar() throws InterruptedException {
    modo = "editar";
    actualizarEstado();
  }

  public void onClick$btnImprimir() throws InterruptedException {
    Messagebox.show("Aquí llamaría un reporte 'imprimible' que muestre todos los datos de la célula");
  }

  /**
   * oculta o muestra la columna donde están los elementos de entrada
   * @param status el estado true o false
   */
  public void verElementosEntrada(boolean status) {
    db$col2.setVisible(status);
    dp$col2.setVisible(status);
    dir$colEdit.setVisible(status);
    fechas$col2.setVisible(status);
    celula$col2.setVisible(status);
    obs$colEdit.setVisible(status);
    contacto$gridEdit.setVisible(status);
  }

  /**
   * oculta o muestra la columna donde están los elementos de visualización
   * @param status el estado true o false
   */
  public void mostrarElementosVisualizacion(boolean status) {
    db$col3.setVisible(status);
    dp$col3.setVisible(status);
    dir$colView.setVisible(status);
    fechas$col3.setVisible(status);
    celula$col3.setVisible(status);
    obs$colView.setVisible(status);
    contacto$gridView.setVisible(status);
  }

  public void onSelect$tabbox() throws InterruptedException {
    tabSeleccionado = tabbox.getSelectedIndex();
    //-Messagebox.show("tab selected: " + tabSeleccionado);
    if (modo.equals("editar")) {
      setFocoEdicion();
    }
  }

  /*
   * le da el foco al primer elemento de entrada del tab actual
   */
  public void setFocoEdicion() {
    if (tabSeleccionado == 0) {
      db$txtNombre.setFocus(true);
      //db$txtNombre.select();
    } else if (tabSeleccionado == 1) {
      dp$dboxFechaNacimiento.setFocus(true);
    } else if (tabSeleccionado == 2) {
      dir$cmbEstado.setFocus(true);
    } else if (tabSeleccionado == 3) {
      contacto$tbbTlfCelular.setFocus(true);
    } else if (tabSeleccionado == 4) {
      fechas$etqConversion.setFocus(true);
    } else if (tabSeleccionado == 6) {
      obs$txtObservaciones.setFocus(true);
      //TODO: ubicar cursor al final del texto, si se está en modo editar
    }
  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.DISCIPULO_PROCESO);
    Sesion.setModo(modo);
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }
}
