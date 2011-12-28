package sig.controladores.celula;

import sig.controladores.Sesion;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import sig.modelo.servicios.ServicioRed;
import waytech.modelo.beans.sgi.Red;
import waytech.utilidades.UtilFechas;

/**
 * Controlador asociado a vistaCelula/DatosBasicos.zul
 * Se encarga de buscar la data para mostrarla en las listas, validaciones y cosas por el estilo
 * @author Gabriel
 */
//  TODO: habilitar que en el txtAnfitrion busque las personas del sistemas, que puedan ser anfitriones de células
public class CtrlCelulaOtrosDatos extends GenericForwardComposer {

  //anfitrión:
  Label etqAnfitrion;
  Textbox txtAnfitrion;
  String anfitrion = "";
  //fecha apertura:
  Label etqFechaApertura;
  Datebox dateboxFechaApertura;
  private String fechaApertura = "01-01-1900"; //valor por defecto si no se elige ninguna fecha
  Date dateFechaApertura = new Date(); //fecha por defecto, si no ha sido elegida ninguna

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
  }
  //anfitrión:

  public void onClick$etqAnfitrion() {
    activarEditAnfitrion();
  }

  public void onOK$txtAnfitrion() {
    procesarTxtAnfitrion();
    desactivarEditAnfitrion();
  }

  public void onBlur$txtAnfitrion() {
    procesarTxtAnfitrion();
    desactivarEditAnfitrion();
  }

  private void activarEditAnfitrion() {
    etqAnfitrion.setVisible(false);
    anfitrion = etqAnfitrion.getValue();
    txtAnfitrion.setValue(anfitrion);
    txtAnfitrion.setVisible(true);
    txtAnfitrion.setFocus(true);
  }

  private void desactivarEditAnfitrion() {
    txtAnfitrion.setVisible(false);
    etqAnfitrion.setVisible(true);
  }

  private void procesarTxtAnfitrion() {
    anfitrion = txtAnfitrion.getValue();
    //TODO: alguna validación que sea necesaria
    etqAnfitrion.setValue(anfitrion);
  }

//Fecha de apertura:
  public void onClick$etqFechaApertura() {
    activarEditFechaApertura();
  }

  public void onBlur$dateboxFechaApertura() {
    procesarValorFechaApertura();
    desactivarEditFechaApertura();
  }

  public void onChange$dateboxFechaApertura() {
    procesarValorFechaApertura();
    desactivarEditFechaApertura();
  }

  private void activarEditFechaApertura() {
    etqFechaApertura.setVisible(false);
    //TODO: asignar al datebox fecha que está en la variable
    dateboxFechaApertura.setVisible(true);
    dateboxFechaApertura.setValue(dateFechaApertura);
    dateboxFechaApertura.setFocus(true);
    dateboxFechaApertura.open();
  }

  private void desactivarEditFechaApertura() {
    dateboxFechaApertura.setVisible(false);
    etqFechaApertura.setVisible(true);
  }

  private void procesarValorFechaApertura() {
    fechaApertura = procesarFechaApertura();
    //TODO: alguna validación que sea necesaria
    etqFechaApertura.setValue(fechaApertura);
  }

  /**
   * tomar valor de fecha del datebox
   **/
  String procesarFechaApertura() {
    dateFechaApertura = dateboxFechaApertura.getValue();
    if (dateFechaApertura != null) {
      fechaApertura = UtilFechas.getFechaTexto(dateFechaApertura);
    } else {
      fechaApertura = "";
    }
    return fechaApertura;
  }
}
