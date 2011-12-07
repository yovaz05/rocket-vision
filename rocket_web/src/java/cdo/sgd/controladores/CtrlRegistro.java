/**
 * CtrlRegistro.java
 */
package cdo.sgd.controladores;

import cdo.sgd.modelo.bd.simulador.Usuario;
import java.util.ArrayList;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import waytech.modelo.beans.sgi.Acceso;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.interfaces.IsaAcceso;
import waytech.modelo.interfaces.IsaPersona;
import waytech.modelo.servicios.SaAcceso;
import waytech.modelo.servicios.SaPersona;

/**
 * Controlador de Vista: Registro.zul
 * @author Gabriel
 */
public class CtrlRegistro extends Window implements AfterCompose {

    protected Intbox txtCedula;
    protected Intbox txtTelefono;
    protected Textbox txtNombre;
    protected Textbox txtCorreo;
    protected Textbox txtPassword;
    protected Textbox txtRepetirPassword;
    protected Button btnIngresar;
    protected Button btnVerificarCedula;
    protected Combobox cmbLider;
    protected Label etqInstrucciones;
    IsaPersona isaPersona = new SaPersona();
    Persona persona = new Persona();
    IsaAcceso isaAcceso = new SaAcceso();
    Acceso acceso = new Acceso();

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        inicializar();
    }

    public void inicializar() {
        txtCedula.setFocus(true);
    }

    public void onClick$btnVerificarCedula(Event event) throws InterruptedException {
        if(!txtCedula.getText().isEmpty()) {
            if(isaPersona.esCedulaExistente(txtCedula.getText()).esCedulaExistente()) {
                etqInstrucciones.setValue("La cédula ya está registrada...");
                persona = isaPersona.getPersonaPorCedula(txtCedula.getText().trim()).getPersona();
                acceso = isaAcceso.getAccesoPorIdUsuario(persona.getIdPersona()).getAcceso();
                txtCorreo.setText(acceso.getCorreo());
                txtNombre.setText(persona.getNombre());
                txtTelefono.setText(persona.getTelefonoHabitacion());
                
            }
            else {
                etqInstrucciones.setValue("OK...");
            }
        }
        
    }
}
