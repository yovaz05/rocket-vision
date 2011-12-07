package sig.controladores;

import cdo.sgd.controladores.CtrlVista;
import cdo.sgd.controladores.Sesion;
import cdo.sgd.controladores.Vistas;
import cdo.sgd.modelo.bd.simulador.BD;
import cdo.sgd.modelo.bd.simulador.Lider;
import java.util.Calendar;
import java.util.Date;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import waytech.utilidades.Util;
import waytech.utilidades.UtilSIG;

/**
 * controlador asociado a Lider.zul
 * @author Gabriel
 */
public class CtrlLider extends GenericForwardComposer {
  //TODO: definir un atributo Lider, como el valor actual

  //referencias:
  Include vistaCentral;
  Include panelCentral;
  //widgets:
  Label tituloVentana;
  Toolbarbutton btnNew;
  Toolbarbutton btnGuardar;
  Toolbarbutton btnEditar;
  Tabbox tabbox;
  Label etqMensaje;
  //pestaña 'Datos Básicos'
  Column db$colEdit;
  Column db$colView;
  Label db$etqCedula;
  Label db$etqNombre;
  Label db$etqRed;
  Label db$etqLider1;
  Label db$etqLider2;
  Label db$etqPareja;
  Textbox db$txtCedula;
  Textbox db$txtNombre;
  A db$tbbRed;
  A db$tbbLider1;
  A db$tbbLider2;
  A db$tbbPareja;
  A db$btnCatRed;
  //pestaña 'Datos Personales'
  Column dp$col2;
  Column dp$col3;
  Datebox dp$dateFechaNacimiento;
  Label dp$etqFechaNacimiento;
  Label dp$etqEdad;
  Label dp$etqEstadoCivil;
  Label dp$etqProfesion;
  Spinner dp$spnEdad;
  Combobox dp$cmbEstadoCivil;
  Combobox dp$cmbProfesion;
  //pestaña 'Dirección'
  Column dir$colEdit;
  Column dir$colView;
  Label dir$etqEstado;
  Label dir$etqCiudad;
  Label dir$etqZona;
  Label dir$etqDirDetallada;
  Combobox dir$cmbEstado;
  Combobox dir$cmbCiudad;
  Textbox dir$txtZona;
  Textbox dir$txtDirDetallada;
  Textbox dir$txtTelefono;
  Toolbarbutton dir$tbbTelefono;
  Comboitem itemZonaAdicional;
  //pestaña 'Contacto'
  Hbox contacto$hboxView;
  Grid contacto$gridView;
  Grid contacto$gridEdit;
  Textbox contacto$txtTlfCelular;
  Textbox contacto$txtTlfHabitacion;
  Textbox contacto$txtTlfTrabajo;
  Textbox contacto$txtEmail;
  Textbox contacto$txtFacebook;
  Textbox contacto$txtTwitter;
  Toolbarbutton contacto$tbbTlfCelular;
  Toolbarbutton contacto$tbbTlfHabitacion;
  Toolbarbutton contacto$tbbTlfTrabajo;
  Toolbarbutton contacto$tbbEmail;
  Toolbarbutton contacto$tbbFacebook;
  Toolbarbutton contacto$tbbTwitter;
  //pestaña 'Fechas'
  Column fechas$col2;
  Column fechas$col3;
  Label fechas$etqConversion;
  Label fechas$etqEncuentro;
  Label fechas$etqBautizo;
  Label fechas$etqGraduacionAcademia;
  Label fechas$etqLanzamiento;
  Datebox fechas$dateConversion;
  Datebox fechas$dateEncuentro;
  Datebox fechas$dateGraduacionAcademia;
  Datebox fechas$dateLanzamiento;
  Datebox fechas$dateBautizo;
  //pestaña 'Roles'
  Column roles$col2;
  Column roles$col3;
  Row roles$rowLiderRed;
  Row roles$rowSupervisor;
  Row roles$rowLiderCelula;
  Row roles$rowEstaca;
  Row roles$rowAnfitrion;
  Row roles$rowMaestro;
  private Checkbox roles$checkLiderRed;
  private Checkbox roles$checkSupervisor;
  private Checkbox roles$checkLiderCelula;
  private Checkbox roles$checkEstacaCelula;
  private Checkbox roles$checkAnfitrionCelula;
  private Checkbox roles$checkMaestroAcademia;
  Label roles$etqLiderRed;
  Label roles$etqSupervisor;
  Label roles$etqLiderCelula;
  Label roles$etqEstacaCelula;
  Label roles$etqAnfitrionCelula;
  Label roles$etqMaestroAcademia;
  //pestaña "Observaciones"
  Column obs$colEdit;
  Column obs$colView;
  Label obs$etqObservaciones;
  Textbox obs$txtObservaciones;
  //gestión de datos
  int id;
  String nombre;
  String fechaNacimiento;
  String fechaConversion;
  String fechaBautizo;
  String fechaLanzamiento;
  String fechaEncuentro;
  String fechaGraduacionAcademia;
  BD simuladorBD;
  //variables de control:
  Lider lider;
  int tabSeleccionado;
  CtrlVista ctrlVista = new CtrlVista();
  //modoActual = {new,edicion,ver,imprimir}
  String modo;
  String titulo = "Líder";

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    simuladorBD = new BD();
    modo = Sesion.getModo();
    if (modo.equals("ver")) {
      getID();
      mostrarDatos();
    }
    actualizarEstado();
    notificarBarra();
  }

  /**
   * recupera parámetro idLider que viene de la vista llamante
   */
  //TODO: evaluar qué se hará si no viene de navegación dinámica...
  private void getID() throws InterruptedException {
    try {
      id = (Integer) Sesion.getVariable("idLider");
    } catch (Exception e) {
      System.out.println("CtrlLider -> ERROR con variable de sesión 'idLider' ");
    }
  }

  /**
   * se muestran datos en la vista, por ahora son simulados
   */
  public void mostrarDatos() {
    System.out.println("CtrlLider.mostrarDatos >> idLider: " + id);

    //llenar widgets con la data
    lider = (Lider) simuladorBD.buscarLider(id);
    System.out.println("LIDER: " + lider.toString());

    /**/
    db$etqCedula.setValue("" + lider.getCedula());
    nombre = lider.getNombre();
    db$etqNombre.setValue(nombre);
    db$tbbRed.setLabel(lider.getNombreRed());

    /**/
    fechaNacimiento = lider.getFechaNacimiento();
    dp$etqFechaNacimiento.setValue(fechaNacimiento);
    dp$etqEdad.setValue("" + lider.getEdad());
    dp$etqEstadoCivil.setValue(lider.getEstadoCivil());
    dp$etqProfesion.setValue(lider.getProfesionOficio());

    /**/
    dir$etqEstado.setValue(lider.getDireccion().getEstado());
    dir$etqCiudad.setValue(lider.getDireccion().getCiudad());
    dir$etqZona.setValue(lider.getDireccion().getZona());
    dir$etqDirDetallada.setValue(lider.getDireccion().getDirDetallada());
    dir$tbbTelefono.setLabel(lider.getDireccion().getTelefono());

    /**/
    contacto$tbbTlfCelular.setLabel(lider.getTelefonoCelular());
    contacto$tbbTlfHabitacion.setLabel(lider.getTelefonoHabitacion());
    contacto$tbbTlfTrabajo.setLabel(lider.getTelefonoTrabajo());
    contacto$tbbEmail.setLabel(lider.getEmail());
    contacto$tbbFacebook.setLabel(lider.getFacebook());
    contacto$tbbTwitter.setLabel(lider.getTwitter());

    /**/
    fechaConversion = lider.getFechaConversion();
    fechaEncuentro = lider.getFechaEncuentro();
    fechaGraduacionAcademia = lider.getFechaGraduacionAcademia();
    fechaLanzamiento = lider.getFechaLanzamiento();
    fechaBautizo = lider.getFechaBautizo();
    fechas$etqConversion.setValue("" + fechaConversion);
    fechas$etqBautizo.setValue("" + fechaBautizo);
    fechas$etqEncuentro.setValue("" + fechaEncuentro);
    fechas$etqGraduacionAcademia.setValue("" + fechaGraduacionAcademia);
    fechas$etqLanzamiento.setValue("" + fechaLanzamiento);

    /*
    UtilSIG.mostrarRol(roles$etqSupervisor, lider.isSupervisorCelula());
    UtilSIG.mostrarRol(roles$etqLiderCelula, lider.isLiderCelula());
    UtilSIG.mostrarRol(roles$etqEstaca, lider.isEstacaCelula());
    UtilSIG.mostrarRol(roles$etqAnfitrion, lider.isAnfitrionCelula());
    UtilSIG.mostrarRol(roles$etqMaestroAcademia, lider.isMaestroAcademia());
     * 
     */
    mostrarRoles();


    /**/
    obs$etqObservaciones.setValue(lider.getObservaciones());

    /**/
    //se establecen parámetros para navegación dinámica:
    String red = lider.getNombreRed();
    String lider1 = lider.getNombreLider1();
    String lider2 = lider.getNombreLider2();
    String pareja = lider.getNombreParejaMinisterial();
    final int idRed = lider.getIdRed();
    final int idLider1 = lider.getIdLider1();
    final int idLider2 = lider.getIdLider2();
    final int idPareja = lider.getIdParejaMinisterial();

    db$tbbRed.addEventListener(Events.ON_CLICK, new EventListener() {

      public void onEvent(Event event) throws Exception {
        Sessions.getCurrent().setAttribute("idRed", idRed);
        panelCentral.setSrc("vistaRed/Red.zul");
        System.out.println("CtrlLider -> idRed = " + idRed);
      }
    });

    //lider 1
    db$tbbLider1.setLabel(lider1);
    if ((idLider1 == 0) || (lider1 == null) || lider1.isEmpty()) {
      db$tbbLider1.setVisible(false);
    } else if (lider1.equals("Pastor Rogelio Mora") || lider1.equals("Pastora Irene de Mora")) {
      //TODO: evaluar esta línea
      db$tbbLider1.setHref(null);
      db$tbbLider1.setTooltiptext("Enlace no funcional");
    } else {
      db$tbbLider1.addEventListener(Events.ON_CLICK, new EventListener() {

        public void onEvent(Event event) throws Exception {
          Sesion.setVariable("idLider", idLider1);
          panelCentral.setSrc("vistaLider/Resumen.zul");
          System.out.println("CtrlLider -> idLider = " + idLider1);
        }
      });
    }

    //lider 2    
    if ((idLider2 == 0) || (lider2 == null) || lider2.equals("Pastora Irene de Mora")) {
      db$tbbLider2.setVisible(false);
    } else {
      db$tbbLider2.setLabel(lider2);
      db$tbbLider2.addEventListener(Events.ON_CLICK, new EventListener() {

        public void onEvent(Event event) throws Exception {
          Sesion.setVariable("idLider", idLider2);
          panelCentral.setSrc("vistaLider/Resumen.zul");
          System.out.println("CtrlLider -> idLider = " + idLider2);
        }
      });
    }

    //pareja:
    if ((idPareja == 0) || (pareja.isEmpty())) {
      db$tbbPareja.setLabel("No tiene");
    } else {
      db$tbbPareja.setLabel(pareja);
      db$tbbPareja.addEventListener(Events.ON_CLICK, new EventListener() {

        public void onEvent(Event event) throws Exception {
          Sesion.setVariable("idLider", idPareja);
          Sesion.setVistaSiguiente(Vistas.LIDER_RESUMEN);
          Sesion.setModo("consulta");
          ctrlVista.forzarCambioVista_btnControl();
        }
      });
    }
  }

  /**
   * actualiza estado de los widgets según el valor de'modo'
   * @throws InterruptedException 
   */
  public void actualizarEstado() throws InterruptedException {
    if (modo.equals("new")) {
      tituloVentana.setValue(titulo + " » Ingresar");
      verElementosEntrada(true);
      habilitarFilasRol(true);
      mostrarElementosVisualizacion(false);
      setFocoEdicion();
    } else if (modo.equals("ver")) {
      tituloVentana.setValue(titulo + " » " + nombre);
      verElementosEntrada(false);
      //mostrarRoles();
      mostrarElementosVisualizacion(true);
    } else if (modo.equals("editar")) {
      tituloVentana.setValue(titulo + " » " + nombre + " » Editando");
      verElementosEntrada(true);
      habilitarFilasRol(true);
      mostrarElementosVisualizacion(false);
      setFocoEdicion();
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
    //TODO: validar datos obligatorios:
    /* código con validación de cédula obligatoria:    
    String cedula = db$txtCedula.getValue();
    if ((cedula != null) && !cedula.isEmpty() && !cedula.equals("")) {
    Sesion.setVariable("resultOperacion", 1);//exito
    copiarValoresDeEntradaAVisualizacion();
    modo = "ver";
    actualizarEstado();
    } else {
    Sesion.setVariable("resultOperacion", 0);//error
    mostrarMensaje("Ingrese la cédula");
    db$txtCedula.setFocus(true);
    }
     * 
     */
    modo = "ver";
    actualizarEstado();
    mostrarValoresView();
    Sesion.setVariable("resultOperacion", 1);//indica éxito, será usado por la barraMenu      
  }

  public void onClick$btnEditar() throws InterruptedException {
    mostrarValoresEdit();
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
    db$colEdit.setVisible(status);
    dp$col2.setVisible(status);
    dir$colEdit.setVisible(status);
    fechas$col2.setVisible(status);
    roles$col2.setVisible(status);
    contacto$gridEdit.setVisible(status);
    obs$colEdit.setVisible(status);
  }

  /**
   * oculta o muestra la columna donde están los elementos de visualización
   * @param status el estado true o false
   */
  public void mostrarElementosVisualizacion(boolean status) {
    db$colView.setVisible(status);
    dp$col3.setVisible(status);
    dir$colView.setVisible(status);
    contacto$gridView.setVisible(status);
    fechas$col3.setVisible(status);
    roles$col3.setVisible(status);
    obs$colView.setVisible(status);
  }

  public void onSelect$tabbox() throws InterruptedException {
    tabSeleccionado = tabbox.getSelectedIndex();
    //-Messagebox.show("tab selected: " + tabSeleccionado);
    if (modo.equals("editar") || modo.equals("new")) {
      setFocoEdicion();
    }
  }

  /*
   * le da el foco al primer elemento de entrada del tab actual
   */
  public void setFocoEdicion() {
    if (tabSeleccionado == 0) {
      db$txtCedula.setFocus(true);
      //db$txtNombre.setFocus(true);
    } else if (tabSeleccionado == 1) {
      dp$dateFechaNacimiento.setFocus(true);
    } else if (tabSeleccionado == 2) {
      dir$cmbEstado.setFocus(true);
    } else if (tabSeleccionado == 3) {
      contacto$txtTlfCelular.setFocus(true);
    } else if (tabSeleccionado == 4) {
      fechas$dateConversion.setFocus(true);
    } else if (tabSeleccionado == 6) {
      obs$txtObservaciones.setFocus(true);
      //TODO: ubicar cursor al final del texto, si se está en modo editar
    }
  }

  //TODO: usar variable 'lider'
  private void capturarDatosWidgetsEntrada() {
  }

  /**
   * muestra los valores actuales en los widgets de entrada
   */
  private void mostrarValoresEdit() {
    //TODO: usar variable 'lider'
    //datos básicos
    db$txtCedula.setValue("" + db$etqCedula.getValue());
    db$txtNombre.setValue(db$etqNombre.getValue());
    //- db$etqRed.setValue(db$tbbRed.getLabel());
    //TODO: Elegir valor del combobox
    db$etqLider1.setValue(db$tbbLider1.getLabel());
    db$etqLider2.setValue(db$tbbLider2.getLabel());
    db$etqPareja.setValue(db$tbbPareja.getLabel());

    //datos personales
    if ((fechaNacimiento != null) && !fechaNacimiento.isEmpty()) {
      dp$dateFechaNacimiento.setValue(new Date(fechaNacimiento));
    }
    dp$spnEdad.setValue(Integer.parseInt(dp$etqEdad.getValue()));
    dp$cmbEstadoCivil.setValue(dp$etqEstadoCivil.getValue());
    dp$cmbProfesion.setValue(dp$etqProfesion.getValue());

    //dirección
    dir$cmbEstado.setValue(dir$etqEstado.getValue());
    dir$cmbCiudad.setValue(dir$etqCiudad.getValue());
    dir$txtZona.setValue(dir$etqZona.getValue());
    dir$txtDirDetallada.setValue(dir$etqDirDetallada.getValue());
    dir$txtTelefono.setValue(dir$tbbTelefono.getLabel());

    //Contacto
    contacto$txtTlfCelular.setValue(contacto$tbbTlfCelular.getLabel());
    contacto$txtTlfHabitacion.setValue(contacto$tbbTlfHabitacion.getLabel());
    contacto$txtTlfTrabajo.setValue(contacto$tbbTlfTrabajo.getLabel());
    contacto$txtEmail.setValue(contacto$tbbEmail.getLabel());
    contacto$txtFacebook.setValue(contacto$tbbFacebook.getLabel());
    contacto$txtTwitter.setValue(contacto$tbbTwitter.getLabel());

    //roles:
    UtilSIG.marcarRol(roles$checkLiderRed, lider.isLiderRed());
    UtilSIG.marcarRol(roles$checkSupervisor, lider.isSupervisor());
    UtilSIG.marcarRol(roles$checkLiderCelula, lider.isLiderCelula());
    UtilSIG.marcarRol(roles$checkEstacaCelula, lider.isEstacaCelula());
    UtilSIG.marcarRol(roles$checkAnfitrionCelula, lider.isAnfitrionCelula());
    UtilSIG.marcarRol(roles$checkMaestroAcademia, lider.isMaestroAcademia());

    //Observaciones
    obs$txtObservaciones.setValue(obs$etqObservaciones.getValue());

    //fechas:    
    if ((fechaConversion != null) && !fechaConversion.isEmpty()) {
      fechas$dateConversion.setValue(new Date(fechaConversion));
    }
    if ((fechaEncuentro != null) && !fechaEncuentro.isEmpty()) {
      fechas$dateEncuentro.setValue(new Date(fechaEncuentro));
    }
    if ((fechaGraduacionAcademia != null) && !fechaGraduacionAcademia.isEmpty()) {
      fechas$dateGraduacionAcademia.setValue(new Date(fechaGraduacionAcademia));
    }
    if ((fechaLanzamiento != null) && !fechaLanzamiento.isEmpty()) {
      fechas$dateLanzamiento.setValue(new Date(fechaLanzamiento));
    }
    if ((fechaBautizo != null) && !fechaBautizo.isEmpty()) {
      fechas$dateBautizo.setValue(new Date(fechaBautizo));
    }
  }

  /**
   * muestra los datos en los widgets tipo visualizaciòn
   */
    //TODO: usar variable 'lider'
  private void mostrarValoresView() {
    //datos básicos
    db$etqCedula.setValue(db$txtCedula.getValue());
    db$etqNombre.setValue(db$txtNombre.getValue());
    //- db$etqRed.setValue(db$etqRed.getValue());
    db$tbbLider1.setLabel(db$etqLider1.getValue());
    db$tbbLider2.setLabel(db$etqLider2.getValue());
    db$tbbPareja.setLabel(db$etqPareja.getValue());

    //datos personales
    Calendar cal = Calendar.getInstance();
    if (dp$dateFechaNacimiento.getValue() != null) {
      cal.setTime(dp$dateFechaNacimiento.getValue());
      fechaNacimiento = Util.getFechaTextoSoloNumeros(cal);
      dp$etqFechaNacimiento.setValue(fechaNacimiento);
    } else {
      //TODO: hacer en otra clase
      cal.setTime(new Date("1900/01/01"));
    }
    if (dp$spnEdad.getValue() != null) {
      int edad = dp$spnEdad.getValue().intValue();
      dp$etqEdad.setValue("" + edad);
    }
    if (dp$cmbEstadoCivil.getValue() != null) {
      String estadoCivil = dp$cmbEstadoCivil.getValue();
      dp$etqEstadoCivil.setValue("" + estadoCivil);
    }
    if (dp$cmbProfesion.getValue() != null) {
      String profesion = dp$cmbProfesion.getValue();
      dp$etqProfesion.setValue("" + profesion);
    }
    
    //dirección
    if (dir$cmbEstado.getValue() != null) {
      String estado = dir$cmbEstado.getValue();
      dir$etqEstado.setValue(estado);
    }
    if (dir$cmbCiudad.getValue() != null) {
      dir$etqCiudad.setValue(dir$cmbCiudad.getValue());
    }
    dir$etqZona.setValue(dir$txtZona.getValue());
    dir$etqDirDetallada.setValue(dir$txtDirDetallada.getValue());
    dir$tbbTelefono.setLabel(dir$txtTelefono.getValue());

    //Contacto
    contacto$tbbTlfCelular.setLabel(contacto$txtTlfCelular.getValue());
    contacto$tbbTlfHabitacion.setLabel(contacto$txtTlfHabitacion.getValue());
    contacto$tbbTlfTrabajo.setLabel(contacto$txtTlfTrabajo.getValue());
    contacto$tbbEmail.setLabel(contacto$txtEmail.getValue());
    contacto$tbbFacebook.setLabel(contacto$txtFacebook.getValue());
    contacto$tbbTwitter.setLabel(contacto$txtTwitter.getValue());

    //roles:
    lider.setLiderRed(roles$checkLiderRed.isChecked());
    lider.setSupervisor(roles$checkSupervisor.isChecked());
    lider.setLiderCelula(roles$checkLiderCelula.isChecked());
    lider.setEstacaCelula(roles$checkEstacaCelula.isChecked());
    lider.setAnfitrionCelula(roles$checkAnfitrionCelula.isChecked());
    lider.setMaestroAcademia(roles$checkMaestroAcademia.isChecked());
    mostrarRoles();

    //fechas:    
    if (dp$dateFechaNacimiento.getValue() != null) {
      Calendar calFechaNac = Calendar.getInstance();
      calFechaNac.setTime(dp$dateFechaNacimiento.getValue());
      fechaNacimiento = Util.getFechaTextoSoloNumeros(calFechaNac);
      dp$etqFechaNacimiento.setValue(fechaNacimiento);
    }

    fechas$etqConversion.setValue(getFechaDatebox(fechas$dateConversion));
    fechas$etqEncuentro.setValue(getFechaDatebox(fechas$dateEncuentro));
    fechas$etqGraduacionAcademia.setValue(getFechaDatebox(fechas$dateGraduacionAcademia));
    fechas$etqLanzamiento.setValue(getFechaDatebox(fechas$dateLanzamiento));
    fechas$etqBautizo.setValue(getFechaDatebox(fechas$dateBautizo));

    //Observaciones
    obs$etqObservaciones.setValue(obs$txtObservaciones.getValue());
  }

  /**
   * devuelve la fecha de un datebox en String
   * formato: mm/yyyy
   * @param datebox
   * @return 
   */
  private String getFechaDatebox(Datebox datebox) {
    String fecha = "";
    if (datebox.getValue() != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(datebox.getValue());
      fecha = Util.getFechaTextoMesAño(cal);
    }
    return fecha;
  }

  /**
   * habilita/deshabilita las filas de los roles
   */
  private void habilitarFilasRol(boolean status) {
    roles$rowLiderRed.setVisible(status);
    roles$rowSupervisor.setVisible(status);
    roles$rowLiderCelula.setVisible(status);
    roles$rowEstaca.setVisible(status);
    roles$rowAnfitrion.setVisible(status);
    roles$rowMaestro.setVisible(status);
  }

  /**
   * muestra todos los roles que sean 'sí'
   */
  void mostrarRoles() {
    habilitarFilasRol(false);
    //mostrando roles:
    if (lider.isLiderRed()) {
      roles$rowLiderRed.setVisible(true);
      UtilSIG.mostrarRol(roles$etqLiderRed, true);
    }
    if (lider.isSupervisor()) {
      roles$rowSupervisor.setVisible(true);
      UtilSIG.mostrarRol(roles$etqSupervisor, true);
    }
    if (lider.isLiderCelula()) {
      roles$rowLiderCelula.setVisible(true);
      UtilSIG.mostrarRol(roles$etqLiderCelula, true);
    }
    if (lider.isEstacaCelula()) {
      roles$rowEstaca.setVisible(true);
      UtilSIG.mostrarRol(roles$etqEstacaCelula, true);
    }
    if (lider.isAnfitrionCelula()) {
      roles$rowAnfitrion.setVisible(true);
      UtilSIG.mostrarRol(roles$etqAnfitrionCelula, true);
    }
    if (lider.isMaestroAcademia()) {
      roles$rowMaestro.setVisible(true);
      UtilSIG.mostrarRol(roles$etqMaestroAcademia, true);
    }

  }

  /**
   * notifica a barraMenu sobre la vista actual, para que setee los estados correspondientes
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.LIDER);
    Sesion.setModo(modo);
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }

  /** método debug
   * muestra un mensaje en label
   * @param msj 
   */
  private void mostrarMensaje(String msj) {
    etqMensaje.setValue(msj);
    etqMensaje.setVisible(true);
    System.out.println(this.getClass().toString() + ": msj");
  }

  /** 
   * limpia el mensaje de estado
   */
  private void ocultarMensaje() {
    etqMensaje.setVisible(false);
    etqMensaje.setValue("");
  }
}
