package sig.controladores;

import cdo.sgd.controladores.Sesion;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import sig.modelo.servicios.ServicioRed;
import waytech.modelo.beans.sgi.Red;

/**
 * Controlador asociado a los 4 combobox de líderes lanzados de una red
 * Se usa en varios maestros, incluyendo: célula, líder, discípulo, red.
 * Servicios usados: PersonaEnRed
 * @author Gabriel
 */
public class CtrlLideresLanzadosRed extends GenericForwardComposer {

  //widgets:
  Combobox cmbLider1;
  Combobox cmbLider2;
  Combobox cmbLider3;
  Combobox cmbLider4;
  A tbbLider1;
  A tbbLider2;
  A tbbLider3;
  A tbbLider4;
  Label etqMensaje;
  Label etqLider1;
  Label etqLider2;
  Label etqLider3;
  Label etqLider4;
  //data:
  ServicioRed servRed = new ServicioRed();
  List lideresLanzadosNombres = new ArrayList();
  ListModelList modelLideresLanzados;
  Red redSelecionada;
  int idRed = 0;
  int idLider1 = 0;
  int idLider2 = 0;
  int idLider3 = 0;
  int idLider4 = 0;
  String nombreRed = "";
  String nombreLider1 = "";
  String nombreLider2 = "";
  String nombreLider3 = "";
  String nombreLider4 = "";

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    buscarIdRed();
  }

  /**
   * obtiene todos los líderes lanzados de la red seleccionada
   * y los carga en los combobox de líderes
   */
  //TODO: quitar de la lista2, 3 y 4 al líder elegido en las listas anteriores
  private void cargarLideresLanzados() {
    System.out.println("CtrlLideresLanzadosRed.cargarLideresLanzados.INICIO");
    if (Sesion.getVariable("cmbRed.id") != null) {
      idRed = (Integer) Sesion.getVariable("cmbRed.id");
      System.out.println("CtrlLideresLanzadosRed. id de red=" + idRed);
      lideresLanzadosNombres = servRed.getLideresLanzadosNombres(idRed);
      //TODO: mensaje al usuario: si la red no tiene líderes lanzados, mostrar mensaje y ocultar combos
      modelLideresLanzados = new ListModelList();
      modelLideresLanzados.addAll(lideresLanzadosNombres);
      cmbLider1.setModel(modelLideresLanzados);
      cmbLider2.setModel(modelLideresLanzados);
      cmbLider3.setModel(modelLideresLanzados);
      cmbLider4.setModel(modelLideresLanzados);
    } else {
      System.out.println("CtrlLideresLanzadosRed. No hay red seleccionada o no tiene líderes lanzados");
    }
    System.out.println("CtrlLideresLanzadosRed.cargarLideresLanzados.FIN");
  }

  public void onSelect$cmbLider1() {
    mostrarValorSeleccionado(1, cmbLider1);
    String nombreNuevo = cmbLider1.getValue();
    if (nombreLider1.equals("")) {
      //no se hace nada
      return;
    }
    nombreLider1 = nombreNuevo;
    Sesion.setVariable("lider1.id", idLider1);
    Sesion.setVariable("lider1.nombre", nombreLider1);
  }

  //TODO: quitar de este combo a los líderes seleccionado en los otros combos
  public void onSelect$cmbLider2() {
    cargarLideresLanzados();
    mostrarValorSeleccionado(2, cmbLider2);
    nombreLider2 = cmbLider2.getValue();
    Sesion.setVariable("lider2.id", idLider1);
  }

  //TODO: quitar de este combo a los líderes seleccionado en los otros combos
  public void onSelect$cmbLider3() {
    cargarLideresLanzados();
    mostrarValorSeleccionado(3, cmbLider3);
    nombreLider3 = cmbLider3.getValue();
    Sesion.setVariable("lider3.id", idLider3);
  }

  //TODO: quitar de este combo a los líderes seleccionado en los otros combos
  public void onSelect$cmbLider4() {
    cargarLideresLanzados();
    mostrarValorSeleccionado(4, cmbLider4);
    nombreLider4 = cmbLider4.getValue();
    Sesion.setVariable("lider4.id", idLider4);
  }

  /**
   * método debug
   * obtiene y muestra todos los valores de los elemeentos de entrada
   * tal como serán usados para trabajar con la base de datos
   */
  public void mostrarValorSeleccionado(int pos, Combobox combo) {
    System.out.println("CtrlLideresLanzadosRed. Líder[" + pos + "] Seleccionado:");
    System.out.println("Líder.nombre=" + combo.getValue());
    System.out.println("Líder.id=" + servRed.getIdPersonaRed(combo.getValue()));
  }

  //busca la red del usuario logueado
  private void buscarIdRed() {
    idRed = (Integer) Sesion.getVariable("idRed");
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
  
}