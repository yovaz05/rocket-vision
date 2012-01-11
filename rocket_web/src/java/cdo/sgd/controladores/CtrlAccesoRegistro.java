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
 * @since Viernes 15/12/2011 02:35 PM.
 * @version 1.0 Viernes 15/12/2011 02:35 PM.
 * @version 1.1 Viernes 11/01/2012 05:35 PM.
 * @author Gerardo José Montilla Virgüez
 * @author Gabriel Pérez 
 * @author Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * @see gerardomontilla@waytech.com.ve
 * @see gabrielperez@waytech.com.ve 
 */
public class CtrlAccesoRegistro extends Window implements AfterCompose {

    protected Intbox txtCedula;
    protected Textbox txtTelefono;
    protected Textbox txtNombre;
    protected Textbox txtCorreo;
    protected Textbox txtPassword;
    protected Textbox txtRepetirPassword;
    protected Textbox txtBuscarLider;
    protected Textbox txtCodigoSecreto;
    protected Button btnIngresar;
    protected Button btnEnviar;
    protected Button btnVerificarCedula;
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
    }

    public boolean verificarCedula() {
        if (!txtCedula.getText().isEmpty()) {
            if (isaPersona.esCedulaExistente(txtCedula.getText()).esCedulaExistente()) {
                persona = isaPersona.getPersonaPorCedula(txtCedula.getText().trim()).getPersona();
                etqInstrucciones.setValue("La cédula registrada...");
                txtNombre.setText(persona.getNombre());
                txtCorreo.setFocus(true);
                return true;
            } else {
                etqInstrucciones.setValue("Cédula no registrada.");
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean verificarCorreo() {
        if (!txtCorreo.getText().isEmpty()) {
            //VERIFICA QUE COINCIDA.
            if (isaPersona.esCorreoExistente(txtCorreo.getText()).esCorreoExistente()) {
                //VERIFICA QUE EL CORREO NO ESTE REGISTRADO EN LA TABLA ACCESO.
                if (!esAccesoRegistrado()) {
                    etqInstrucciones.setValue("El correo electrónico es apropiado.");
                    txtCodigoSecreto.setFocus(true);
                    return true;
                } else {
                    etqInstrucciones.setValue("El correo electrónico no puede ser asociado.");
                    return false;
                }
            } else {
                etqInstrucciones.setValue("Correo electrónico no coincide...");
                return false;
            }
        } else {
            etqInstrucciones.setValue("El correo tiene un formato incorrecto, intentalo de nuevo...");
            return false;
        }
    }

    public boolean verificarCodigoSecreto() {
        if (!txtCedula.getText().isEmpty()) {
            if (!txtCodigoSecreto.getText().isEmpty()) {
                if (isaPersona.esCodigoSecretoExistente(persona.getCi(), txtCodigoSecreto.getText().trim()).esCodigoExistente()) {
                    etqInstrucciones.setValue("El codigo secreto es válido");
                    return true;
                } else {
                    etqInstrucciones.setValue("El codigo secreto no es coincidente");
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean verificarPassword() {
        if (!(txtPassword.getText().isEmpty() && txtRepetirPassword.getText().isEmpty())) {
            if (txtPassword.getText().trim().equals(txtRepetirPassword.getText().trim())) {
                if (registrarAcceso()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Verifica que el correo no esté registrado en la tabla de acceso.
     * @return 
     */
    public boolean esAccesoRegistrado() {
        if (!txtCorreo.getText().isEmpty()) {
            if (isaAcceso.esCorreoExistente(txtCorreo.getText()).esCorreoExistente()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Método que envia a la base de datos los campos necesarios para que el 
     * usuario pueda acceder al sistema.
     * Hace falta optimizar el código.
     * @return 
     */
    public boolean registrarAcceso() {
        if (verificarCodigoSecreto()) {
            if (!esAccesoRegistrado()) {
                if (!(txtPassword.getText().isEmpty() && txtRepetirPassword.getText().isEmpty())) {
                    if (txtPassword.getText().equals(txtRepetirPassword.getText())) {
                        //REGISTRA EL ACCESO.
                        AccesoInsert accesoInsert = new AccesoInsert();
                        accesoInsert.setCorreo(persona.getCorreo());
                        accesoInsert.setIdPersona(persona.getIdPersona());
                        accesoInsert.setLogin(persona.getCorreo());
                        accesoInsert.setPassword(txtPassword.getText());
                        RspAcceso rspAcceso = new RspAcceso();
                        rspAcceso = isaAcceso.insertAcceso(accesoInsert);
                        if (rspAcceso.esConexionAbiertaExitosamente() && rspAcceso.esConexionCerradaExitosamente() && rspAcceso.esSentenciaSqlEjecutadaExitosamente()) {
                            etqInstrucciones.setValue("La información se registró exitosamente, puedes ingresar al sistema");
                            txtCedula.setText("");
                            txtCodigoSecreto.setText("");
                            txtCorreo.setText("");
                            txtNombre.setText("");
                            txtPassword.setText("");
                            txtRepetirPassword.setText("");
                            txtCedula.setFocus(true);
                            //Colocar una espera por 3 segundos..
                            //Redirigir al sistema.

                            return true;
                        } else {
                            etqInstrucciones.setValue("Error al momento de registrar la información en base de datos..");
                            return false;
                        }
                    } else {
                        etqInstrucciones.setValue("Los Passwords no coinciden..");
                        txtPassword.setFocus(true);
                        return false;
                    }
                } else {
                    etqInstrucciones.setValue("El password debe tener caracteres");
                    txtPassword.setFocus(true);
                    return false;
                }
            } else {
                etqInstrucciones.setValue("Esta cuenta de correo ya tiene acceso al sistema");
                return false;
            }
        } else {
            return false;
        }
    }

    public void onClick$btnVerificarCedula(Event event) throws InterruptedException {
        verificarCedula();
    }

    public void onBlur$txtCedula(Event event) throws InterruptedException {
        verificarCedula();
    }

    public void onBlur$txtCorreo(Event event) throws InterruptedException {
        verificarCorreo();
    }

    public void onBlur$txtCodigoSecreto(Event event) throws InterruptedException {
        verificarCodigoSecreto();
    }

    public void onOK$txtCedula(Event event) throws InterruptedException {
        verificarCedula();
    }

    public void onOK$txtNombre(Event event) throws InterruptedException {
        txtCorreo.setFocus(true);
    }

    public void onOK$txtCorreo(Event event) throws InterruptedException {
        verificarCorreo();
    }

    public void onOK$txtPassword(Event event) throws InterruptedException {
        txtRepetirPassword.setFocus(true);
    }

    public void onOK$txtRepetirPassword(Event event) throws InterruptedException {
        verificarPassword();
        registrarAcceso();
    }

    public void onOK$txtCodigoSecreto(Event event) throws InterruptedException {
        verificarCodigoSecreto();
    }

    public void onClick$btnEnviar(Event event) throws InterruptedException {
        registrarAcceso();
    }
}
