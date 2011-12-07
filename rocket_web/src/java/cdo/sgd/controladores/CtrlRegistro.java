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
import waytech.modelo.interfaces.IsaPersona;
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
            }
            else {
                etqInstrucciones.setValue("OK...");
            }
        }
        
    }
}
