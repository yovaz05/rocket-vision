package rocket.controladores.acceso;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import rocket.controladores.general.Sesion;
import rocket.controladores.general.Vistas;
import waytech.modelo.servicios.RspAcceso;
import waytech.modelo.servicios.SaAcceso;

/**
 * @since Miercoles 11/01/2012 02:35 PM.
 * @version 1.0 Miercoles 11/01/2012 02:35 PM.
 * @author Gerardo José Montilla Virgüez 
 * @author Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * @see gerardomontilla@waytech.com.ve 
 */
public class CtrlAccesoCambioPassword extends GenericForwardComposer {

  protected Label etqMensajes;
  protected Textbox txtCorreo;
  protected Textbox txtPasswordActual;
  protected Textbox txtPassword;
  protected Textbox txtPasswordRepeat;
  SaAcceso saAcceso = new SaAcceso();
  private String login = "";

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
    inicio();
  }

  private void inicio() {
    notificarBarra();
    txtPasswordActual.setFocus(true);
  }

  public void cambiarPassword() {
    if (!txtPasswordActual.getText().isEmpty()) {
      RspAcceso rspAcceso = new RspAcceso();
      rspAcceso = saAcceso.esLoginPasswordValido(login, txtPasswordActual.getText());
      if (rspAcceso.esLoginPasswordValido()) {
        if (!txtPassword.getText().isEmpty()) {
          if (!txtPasswordRepeat.getText().isEmpty()) {
            if (txtPasswordRepeat.getText().equals(txtPassword.getText())) {
              RspAcceso rspAcceso1 = new RspAcceso();
              rspAcceso1 = saAcceso.updatePassword(login, txtPassword.getText());
              if (rspAcceso1.esSentenciaSqlEjecutadaExitosamente()) {
                mensaje("Contraseña cambiada exitosamente");
                //-txtCorreo.setText("");
                txtPassword.setText("");
                txtPasswordActual.setText("");
                txtPasswordRepeat.setText("");
                //- txtCorreo.setFocus(true);
                etqMensajes.setFocus(true);
              } else {
                mensaje("Error: La contraseña no pudo ser cambiada");
              }
            } else {
              mensaje("Las contraseñas deben coincidir");
              txtPassword.setFocus(true);
            }
          } else {
            mensaje("Repita la contraseña");
            txtPasswordRepeat.setFocus(true);
          }
        } else {
          mensaje("Ingrese la contraseña");
          txtPassword.setFocus(true);
        }
      }
    }
  }

  public void onClick$btnCambiarPassword() {
    login = "" + Sesion.getVariable("usuario.login");
    cambiarPassword();
  }

  public void onOK$btnCambiarPassword() {
    cambiarPassword();
  }

  public void onOK$txtPasswordAnterior() {
    cambiarPassword();
  }

  /**
   * notifica a barraMenu sobre la vista actual, para el manejo de estados
   */
  private void notificarBarra() {
    vistaCentral = Sesion.getVistaCentral();
    Sesion.setVistaActual(Vistas.LIDER);
    Sesion.setModo("consulta");
    Toolbarbutton btnControl2 = (Toolbarbutton) vistaCentral.getFellow("btnControl2");
    Events.postEvent(1, "onClick", btnControl2, null);
  }
  Include vistaCentral;

  /** método debug
   * muestra un mensaje en label
   * @param msj 
   */
  private void mensaje(String msj) {
    etqMensajes.setValue(msj);
    etqMensajes.setVisible(true);
    System.out.println(this.getClass().toString() + msj);
  }

  /** 
   * limpia el mensaje de estado
   */
  private void ocultarMensaje() {
    etqMensajes.setVisible(false);
    etqMensajes.setValue("");
  }
}
