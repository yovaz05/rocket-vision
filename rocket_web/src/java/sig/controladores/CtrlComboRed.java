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

/**
 * Controlador asociado a Combobox cmbRed, usado en varias vistas
 * Servicios usados: Red
 * @author Gabriel
 */
public class CtrlComboRed extends GenericForwardComposer {

  //widgets:
  Label etqRed;
  A tbbRed;
  Combobox cmbRed;
  //data:
  //red y líderes lanzados de esa red (para ser: líderes de célula, líderes de otro discípulo, líderes de red, etc...)
  ServicioRed servRed = new ServicioRed();
  List redesNombres = new ArrayList();
  ListModelList modelRedes = new ListModelList();
  int idRed = 0;
  String nombreRed = "";

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
    cargarDatosBD();
    //-resetVariablesSesion();
  }

  /**
   * carga lista de redes de la base de datos
   */
  private void cargarDatosBD() {
    redesNombres = servRed.getRedesNombres();
    modelRedes.addAll(redesNombres);
    cmbRed.setModel(modelRedes);
  }

  public void onSelect$cmbRed() {
    System.out.println("CtrlCatalogoRed.onSelect$cmbRed():");
    capturarValorRed();
  }

  /**
   * en desarrollo
   * TODO: si el combo pierde el foco, procesar el valor
   */
  public void onBlur$cmbRed() {
    System.out.println("CtrlCatalogoRed.onBlur$cmbRed():");
    capturarValorRed();
  }

  /**
   * método debug
   * obtiene y muestra todos los valores de los elemeentos de entrada
   * tal como serán usados para trabajar con la base de datos
   */
  public void mostrarValorSeleccionado() {
    etqRed.setValue(nombreRed);
    System.out.println("CtrlCatalogoRed- Red Elegida:");
    System.out.println("id: " + idRed);
    System.out.println("Nombre: " + nombreRed);
    System.out.println("Variable de sesion:cmbRed.id: " + Sesion.getVariable("cmbRed.id"));
  }

  /**
   * 
   *TAREA: 
   * 1. datos por defecto:red = red a la que pertenece el usuario logueado
   * 2. si la red no tiene líderes lanzados, mostrar un mensaje en el combo
   **/
  /**
   * guarda las variables de sesión de idRed y nombreRed
   * acorde al valor elegido en el combo
   * para usos por otros controladores
   */
  public void setVariablesSesion() {
    Sesion.setVariable("cmbRed.id", idRed);
    Sesion.setVariable("cmbRed.nombre", nombreRed);
  }

  private void capturarValorRed() {
    if (cmbRed.getValue().equals(nombreRed)) {
      return;//no hubo cambio
    }
    nombreRed = cmbRed.getValue();
    servRed.setNombreRed(nombreRed);
    idRed = servRed.getIdRed();
    setVariablesSesion();
    mostrarValorSeleccionado();
  }
}