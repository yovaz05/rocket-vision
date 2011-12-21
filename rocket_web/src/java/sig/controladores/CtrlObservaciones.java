/*
TODO:
resolver lo de "nueva ciudad" y "nueva zona"
opción A: no usarlo por ahora
opción B: usarlo. guardar en variables de sesión, los valores ingresados.
cada controlador que use esto, debe grabar el nuevo registro? no me gusta esta opción
opción C: cuando se eliga "nuevo item", se abre una ventana popup donde se debe ingresar el valor, 
y esta ventana graba en la base de datos. la lista muestra el nuevo valor.
 */
package sig.controladores;

import cdo.sgd.controladores.Sesion;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import sig.modelo.servicios.ServicioCelula;

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
  Div divMensaje;
  Label etqMensaje;
  //widgets:
  Label etqObservaciones;
  Textbox txtObservaciones;
  //variables de control:
  private String observaciones = "";
  ServicioCelula servicioCelula = new ServicioCelula();
  private int idCelula = 0;

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

  public void onBlur$txtObservaciones() {
    procesarObservaciones();
    cancelarEditObservaciones();
  }

  private void activarEditObservaciones() {
    etqObservaciones.setVisible(false);
    observaciones = etqObservaciones.getValue();
    if (observaciones.equals(Constantes.VALOR_EDITAR)) {
      observaciones = "";
    }
    txtObservaciones.setValue(observaciones);
    txtObservaciones.setVisible(true);
    txtObservaciones.setFocus(true);
  }

  private void cancelarEditObservaciones() {
    txtObservaciones.setVisible(false);
    etqObservaciones.setVisible(true);
  }

  private void procesarObservaciones() {
    String valorNuevo = txtObservaciones.getValue();
    //quitar espacios en blanco
    valorNuevo = valorNuevo.trim();
    if (valorNuevo.equals(observaciones) //no se cambió valor anterior
            || valorNuevo.equals(Constantes.VALOR_EDITAR)) {//no se cambió valor por defecto "Editar"
      return;
    }
    observaciones = valorNuevo;
    System.out.println("CtrlObservaciones. valor ingresado = " + observaciones);
    //actualizar valor en la base de datos:
    if (Sesion.esVistaCelula()) {
      System.out.println("CtrlObservaciones. es vistaCelula");
      //actualizar valor en la base de datos
      servicioCelula.setIdCelula(getIdCelula());
      if (servicioCelula.actualizarObservaciones(observaciones)) {
          mostrarMensaje("Se actualizaron las observaciones");
        } else {
          mostrarMensaje("Error actualizando las observaciones");
        }
    }

    if (observaciones.isEmpty()) {//quedó en blanco, se usa etiqueta que permita edición posterior
      observaciones = Constantes.VALOR_EDITAR;
    }
    etqObservaciones.setValue(observaciones);
  }

  private int getIdCelula() {
    System.out.println("CtrlDireccion.getIdCelula:");
    return (Integer) Sesion.getVariable("idCelula");
  }
  
  private void mostrarMensaje(String msj) {
    etqMensaje.setValue(msj);
    etqMensaje.setVisible(true);
    divMensaje.setVisible(true);
    System.out.println(this.getClass().toString() + msj);
  }

  /** 
   * limpia el mensaje de estado
   */
  private void ocultarMensaje() {
    etqMensaje.setValue("");//TODO: CODIGO: línea parece redundante
    etqMensaje.setVisible(false);
    divMensaje.setVisible(false);
  }  
  
}
