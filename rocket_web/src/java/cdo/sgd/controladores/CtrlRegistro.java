package cdo.sgd.controladores;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import waytech.modelo.beans.sgi.Acceso;
import waytech.modelo.beans.sgi.AccesoInsert;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.beans.sgi.PersonaInsert;
import waytech.modelo.interfaces.IsaAcceso;
import waytech.modelo.interfaces.IsaDiscipulo;
import waytech.modelo.interfaces.IsaPareja;
import waytech.modelo.interfaces.IsaPersona;
import waytech.modelo.servicios.RspAcceso;
import waytech.modelo.servicios.RspPersona;
import waytech.modelo.servicios.SaAcceso;
import waytech.modelo.servicios.SaDiscipulo;
import waytech.modelo.servicios.SaPareja;
import waytech.modelo.servicios.SaPersona;
import waytech.utilidades.UtilidadSistema;
import waytech.utilidades.UtilidadValidacion;

/**
 * @since Viernes 15/12/2011 02:35 PM.
 * @version 1.0 Viernes 15/12/2011 02:35 PM.
 * @author Gerardo José Montilla Virgüez
 * @author Gabriel Pérez 
 * @author Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * @see gerardomontilla@waytech.com.ve
 * @see gabrielperez@waytech.com.ve 
 */
public class CtrlRegistro extends Window implements AfterCompose {

    protected Intbox txtCedula;
    protected Textbox txtTelefono;
    protected Textbox txtNombre;
    protected Textbox txtCorreo;
    protected Textbox txtPassword;
    protected Textbox txtRepetirPassword;
    protected Textbox txtBuscarLider;
    protected Button btnIngresar;
    protected Button btnPaso1;
    protected Button btnVerificarCedula;
    protected Button btnBuscarLider;
    protected Label etqInstrucciones;
    IsaPersona isaPersona = new SaPersona();
    Persona persona = new Persona();
    IsaAcceso isaAcceso = new SaAcceso();
    Acceso acceso = new Acceso();
    IsaPareja isaPareja = new SaPareja();
    IsaDiscipulo isaDiscipulo = new SaDiscipulo();
    UtilidadSistema utilidadSistema = new UtilidadSistema();
    UtilidadValidacion utilidadValidacion = new UtilidadValidacion();

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        inicializarComponentes();
    }

    public void inicializarComponentes() {
        txtCedula.setText("");
        txtCedula.setFocus(true);
        btnPaso1.setVisible(true);
    }

    public void verificarCedula() {
        if (!txtCedula.getText().isEmpty()) {
            if (isaPersona.esCedulaExistente(txtCedula.getText()).esCedulaExistente()) {
                persona = isaPersona.getPersonaPorCedula(txtCedula.getText().trim()).getPersona();                                
                etqInstrucciones.setValue("La cédula registrada...");                
            } else {
                etqInstrucciones.setValue("Cédula no registrada.");                
            }
        }
    }   

    public void verificarCorreo() {
        if (!txtCorreo.getText().isEmpty() && utilidadValidacion.esCorreoValido(txtCorreo.getText())) {
            if (isaAcceso.esCorreoExistente(txtCorreo.getText()).esCorreoExistente()) {
                etqInstrucciones.setValue("El correo ya está registrado, intentalo de nuevo...");
                txtCorreo.setText("");
                txtCorreo.setFocus(true);
            } else {
                etqInstrucciones.setValue("Correo electrónico válido");
            }
        } else {
            etqInstrucciones.setValue("El correo tiene un formato incorrecto, intentalo de nuevo...");
        }
    }

    public void verificarTelefono() throws InterruptedException {
        if (utilidadValidacion.sonSoloNumeros(txtTelefono.getText())) {
            txtPassword.setFocus(true);
            etqInstrucciones.setValue("Número telefonico válido");
        } else {
            etqInstrucciones.setValue("El telefono no tiene el formato correcto, use solo numeros.");

        }
    }

    public void onClick$btnBuscarLider(Event event) throws InterruptedException {
    }

    public void onClick$btnVerificarCedula(Event event) throws InterruptedException {
        verificarCedula();
    }

    public void onClick$btnPaso1(Event event) throws InterruptedException {        
    }

    public void onBlur$txtCedula(Event event) throws InterruptedException {
        verificarCedula();
    }

    public void onBlur$txtCorreo(Event event) throws InterruptedException {
        verificarCorreo();
    }

    public void onBlur$txtTelefono(Event event) throws InterruptedException {
        verificarTelefono();
    }

    public void onOK$txtCedula(Event event) throws InterruptedException {
        verificarCedula();
    }

    public void onOK$txtNombre(Event event) throws InterruptedException {
        txtCorreo.setFocus(true);
    }

    public void onOK$txtCorreo(Event event) throws InterruptedException {
        txtTelefono.setFocus(true);
    }

    public void onOK$txtTelefono(Event event) throws InterruptedException {
        verificarTelefono();
    }

    public void onOK$txtPassword(Event event) throws InterruptedException {
        txtRepetirPassword.setFocus(true);
    }

    public void onOK$txtRepetirPassword(Event event) throws InterruptedException {        
    }
}
