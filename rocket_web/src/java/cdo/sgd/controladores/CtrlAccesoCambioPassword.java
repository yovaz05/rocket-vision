package cdo.sgd.controladores;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import waytech.modelo.beans.sgi.Acceso;
import waytech.modelo.beans.sgi.AccesoInsert;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.interfaces.IsaAcceso;
import waytech.modelo.interfaces.IsaDiscipulo;
import waytech.modelo.interfaces.IsaPareja;
import waytech.modelo.interfaces.IsaPersona;
import waytech.modelo.servicios.RspAcceso;
import waytech.modelo.servicios.SaAcceso;
import waytech.modelo.servicios.SaDiscipulo;
import waytech.modelo.servicios.SaPareja;
import waytech.modelo.servicios.SaPersona;
import waytech.utilidades.UtilidadSistema;
import waytech.utilidades.UtilidadValidacion;

/**
 * @since Miercoles 11/01/2012 02:35 PM.
 * @version 1.0 Miercoles 11/01/2012 02:35 PM.
 * @author Gerardo José Montilla Virgüez 
 * @author Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * @see gerardomontilla@waytech.com.ve 
 */
public class CtrlAccesoCambioPassword extends Window implements AfterCompose {

    protected Label etqInstrucciones;
    protected Textbox txtCorreo;
    protected Textbox txtPasswordAnterior;
    protected Textbox txtPassword;
    protected Textbox txtPassword1;
    protected Button btnCambiarPassword;
    IsaAcceso isaAcceso = new SaAcceso();

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
    }

    public void cambiarPassword() {
        if (!txtPasswordAnterior.getText().isEmpty()) {
            if (!txtCorreo.getText().isEmpty()) {
                RspAcceso rspAcceso = new RspAcceso();
                rspAcceso = isaAcceso.esLoginPasswordValido(txtCorreo.getText(), txtPasswordAnterior.getText());
                if (rspAcceso.esLoginPasswordValido()) {
                    if (!txtPassword.getText().isEmpty()) {
                        if (!txtPassword1.getText().isEmpty()) {
                            if (txtPassword1.getText().equals(txtPassword.getText())) {
                                RspAcceso rspAcceso1 = new RspAcceso();
                                rspAcceso1 = isaAcceso.updatePassword(txtCorreo.getText(), txtPassword.getText());
                                if (rspAcceso1.esSentenciaSqlEjecutadaExitosamente()) {
                                    etqInstrucciones.setValue("La contraseña ha sido cambiada exitosamente");
                                    txtCorreo.setText("");
                                    txtPassword.setText("");
                                    txtPasswordAnterior.setText("");
                                    txtPassword1.setText("");
                                    txtCorreo.setFocus(true);
                                } else {
                                    etqInstrucciones.setValue("Error !!! La contraseña no pudo ser cambiada");
                                }
                            } else {
                                etqInstrucciones.setValue("Las contraseñas deben coincidir");
                                txtPassword.setFocus(true);
                            }
                        } else {
                            etqInstrucciones.setValue("El campo de repetir el Password debe estar completo");
                            txtPassword1.setFocus(true);
                        }
                    } else {
                        etqInstrucciones.setValue("El campo de password debe estar completo");
                        txtPassword.setFocus(true);
                    }
                }
            }
        }
    }

    public void onClick$btnCambiarPassword(Event event) {
        cambiarPassword();
    }

    public void onOK$btnCambiarPassword(Event event) {
        cambiarPassword();
    }

    public void onOK$txtPasswordAnterior(Event event) {
        cambiarPassword();
    }
}
