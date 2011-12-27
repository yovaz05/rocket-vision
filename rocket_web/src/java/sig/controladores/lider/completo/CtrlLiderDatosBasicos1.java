package sig.controladores.lider.completo;

import cdo.sgd.controladores.Sesion;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import sig.modelo.servicios.ServicioRed;
import waytech.modelo.beans.sgi.Red;

/**
 * Controlador asociado a vistaLider/DatosBasicos.zul
 * Para validaciones independientes
 * @author Gabriel
 */
public class CtrlLiderDatosBasicos1 extends GenericForwardComposer {

  //widgets:
  Label etqMensaje;
  Textbox txtCedula;
  Textbox txtNombre;
  Label etqCedula;
  Label etqNombre;
  Label etqLider1;
  Label etqLider2;
  A tbbLider1;
  A tbbLider2;
  A tbbLider3;
  A tbbLider4;
  A tbbPareja;
  Label etqRed;
  A tbbRed;
  Combobox cmbDia;
  Combobox cmbHora;
  Combobox cmbRed;
  Combobox cmbLider1;
  Combobox cmbLider2;
  Combobox cmbLider3;
  Combobox cmbLider4;
  Combobox cmbPareja;
  //data:
  ServicioRed servRed = new ServicioRed();
  List redesNombres = new ArrayList();
  List lideresLanzadosNombres = new ArrayList();
  Red redSelecionada;
  ListModelList modelRedes = new ListModelList();
  ListModelList modelLideresLanzados = new ListModelList();
  int idRed = 0;
  String nombreRed = "Blood and Fire";

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    cargarRedes();
    //-resetVariablesSesion();
  }

  

  /**
   * carga lista de redes
   * TODO: Evaluar mejora: mostrar sólo las redes donde hay líderes lanzados registrados
   */
  private void cargarRedes() {
    redesNombres = servRed.getRedesNombres();
    modelRedes.addAll(redesNombres);
    cmbRed.setModel(modelRedes);
  }
 
  /**
   * obtiene todos los líderes lanzados de la red seleccionada
   * los lista para líderes y para pareja
   */
  //TODO: quitar de la lista2, 3 y 4 al líder elegido en las listas anteriores
  private void cargarLideresLanzados() {
    nombreRed = cmbRed.getValue();
    System.out.println("red value " + cmbRed.getValue());
    servRed.setNombreRed(nombreRed);
    idRed = servRed.getIdRed();
    System.out.println("cargarLideresLanzados. nombre red: " + nombreRed);
    System.out.println("cargarLideresLanzados. id red: " + idRed);
    lideresLanzadosNombres = servRed.getLideresLanzadosNombres(idRed);
    modelLideresLanzados = new ListModelList();
    modelLideresLanzados.addAll(lideresLanzadosNombres);
    cmbLider1.setModel(modelLideresLanzados);
    cmbLider2.setModel(modelLideresLanzados);
    cmbLider3.setModel(modelLideresLanzados);
    cmbLider4.setModel(modelLideresLanzados);
    cmbPareja.setModel(modelLideresLanzados);
  }
  
  /**
   * listar las posibles parejas
   */
  /**
   * TODO:
   * quitar de la lista de líderes lanzados a "this" persona
   * y a los líderes de éste
   **/ 
  private void cargarPosiblesParejas() {
  }

  public void onSelect$cmbRed() {
    System.out.println("CtrlLiderDatosBasicos.cmbRed.onSelect. INICIO");
    nombreRed = cmbRed.getValue();
    System.out.println("Red seleccionada.value: " + nombreRed);
    System.out.println("Red seleccionada.selecteditem.value: " + cmbRed.getSelectedItem().getValue());
    cmbLider1.setValue("");
    cmbLider2.setValue("");
    cmbLider3.setValue("");
    cmbLider4.setValue("");
    cargarLideresLanzados();
    cargarPosiblesParejas();
    Sesion.setVariable("lider.idRed", idRed);
    Sesion.setVariable("lider.nombreRed", nombreRed);
    System.out.println("CtrlLiderDatosBasicos.cmbRed.onSelect. FIN");
  }
  
  //quitar de la listas de pareja a la persona seleccionada aquí
  public void onSelect$cmbLider1() {
  }
  /**
   * 
   *TAREA: 
   * 1. datos por defecto:red = red a la que pertenece el usuario en curso
   * 2. si la red no tiene líderes lanzados, mostrar un mensaje en el combo
   **/
  
  /**
   * establece las variables de sesión
   * acorde a los valores seleccionado en los widgets de entrada
   */
  public void resetVariablesSesion() {
    Sesion.setVariable("celula.dia", cmbDia.getValue());
    Sesion.setVariable("celula.hora", cmbHora.getValue());
  }
  
 /** método debug
   * muestra un mensaje en un label, para interacción con el usuario
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
  
  /**
   * método debug
   * obtiene y muestra todos los valores de los elemeentos de entrada
   * tal como serán usados para trabajar con la base de datos
   */
  public void mostrarTodosValores() {
    System.out.println("LIDER.DATOSBASICOS. VALORES INGRESADOS:");
    System.out.println("Código: " + txtCedula.getValue());
    System.out.println("Nombre: " + txtCedula.getValue());
    System.out.println("Líder 1: nombre=" + cmbLider1.getValue());
    System.out.println("Líder 1: id=" + servRed.getIdPersonaRed(cmbLider1.getValue()));
    System.out.println("Día.label=" + cmbDia.getValue());
    System.out.println("Día.value=" + cmbDia.getSelectedItem().getValue());
    System.out.println("Hora.label: " + cmbHora.getValue());
    System.out.println("Hora.value: " + cmbHora.getSelectedItem().getValue());
  }

  
  
}