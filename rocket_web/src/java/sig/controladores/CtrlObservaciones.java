/**
TODO:
resolver lo de "nueva ciudad" y "nueva zona"
opción A: no usarlo por ahora
opción B: usarlo. guardar en variables de sesión, los valores ingresados.
cada controlador que use esto, debe grabar el nuevo registro? no me gusta esta opción
opción C: cuando se eliga "nuevo item", se abre una ventana popup donde se debe ingresar el valor, 
y esta ventana graba en la base de datos. la lista muestra el nuevo valor.
 */
package sig.controladores;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

/**
 * controlador asociado a Direccion.zul
 * @author Gabriel
 */
public class CtrlObservaciones extends GenericForwardComposer {

  /**
   *TODO:
   * 1. cuando eliga nueva ciudad: mensaje en la parte superior de la ventana
   * 2. mostrar item nueva ciudad en la carga inicial
   * 3. programar perdida de foco de los combos
   * 4. cuando se selecciona un combo, desplegarlo automáticamente
   * 4. quitar botones de edición, o cancelar edición
   ***/
  //widgets:
  Label etqObservaciones;
  Textbox txtObservaciones;
  //variables de control:
  private String observaciones = "";

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  public void inicio() throws InterruptedException {
  }

  /**
   * carga todas los estados existentes en la base de datos
   */

  public void onClick$etqObservaciones() {
    activarEditObservaciones();
  }

  public void onOK$txtObservaciones() {
    procesarObservaciones();
    desactivarEditObservaciones();
  }

  public void onBlur$txtObservaciones() {
    procesarObservaciones();
    desactivarEditObservaciones();
  }
  
  private void activarEditObservaciones() {
    etqObservaciones.setVisible(false);
    txtObservaciones.setValue(etqObservaciones.getValue());
    txtObservaciones.setVisible(true);
    txtObservaciones.setFocus(true);
  }

  private void desactivarEditObservaciones() {
    txtObservaciones.setVisible(false);
    etqObservaciones.setVisible(true);
  }
  

  private void procesarObservaciones() {
    observaciones = txtObservaciones.getValue();
    //TODO: alguna validación que sea necesaria
    etqObservaciones.setValue(observaciones);
  }
}
