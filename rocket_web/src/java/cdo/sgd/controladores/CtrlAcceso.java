package cdo.sgd.controladores;

import cdo.sgd.modelo.bd.simulador.BD;
import cdo.sgd.modelo.bd.simulador.Usuario;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.Textbox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;

/**
 * @since Viernes 15/12/2011 02:35 PM.
 * @version 1.0 Viernes 15/12/2011 02:35 PM.
 * @author Gerardo José Montilla Virgüez, Gabriel Pérez Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class CtrlAcceso extends GenericForwardComposer {

    //widgets:  
    Textbox txtLogin;
    Textbox txtPassword;
    Button btnIngresar;
    Label etqErrorLogin;
    Label etqErrorPassword;
    Checkbox checkRecordarSesion;
    //datos:
    BD bd;
    Usuario usuario;
    String menu = "";
    //constantes:
    String ERROR_USUARIO = "Introduce tu nombre de usuario";
    String ERROR_MATCH = "El nombre de usuario o la contraseña introducidos no son correctos";
    String INSTRUCCION_USUARIO = "Introduce tu nombre de usuario";
    String INSTRUCCION_CONTRASEÑA = "Introduce tu contraseña";
    //referencias:
    Include panelCentral;
    Include vistaCentral;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        inicio();
        //inicio2();
    }

    public void inicio() {
        try {
            boolean sesion = sesionActiva();
            if (sesion) {
                Executions.sendRedirect("index2.zul");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Inicio - redirecionando a formulario de acceso");
        }
        bd = new BD();
        limpiarEntradas();
        limpiarMensajesError();
        txtLogin.setFocus(true);
    }

    /**
     * usado para efectos de desarrollo
     */
    public void inicio2() {
        Executions.sendRedirect("index2.zul");
    }

    /**
     * devuelve false si el usuario no está logueado
     * o true si ya está logueado
     * @return 
     */
    private boolean sesionActiva() {
        boolean usuarioLogueado = (Boolean) Sessions.getCurrent().getAttribute("usuarioLogueado");
        System.out.println("usuarioLogueado: " + usuarioLogueado);
        return usuarioLogueado;
    }

    public void login() throws InterruptedException {
        if (camposLlenos()) {
            limpiarMensajesError();
            String login = txtLogin.getText();
            String password = txtPassword.getText();
            if (!validarAcceso(login, password)) {
                mostrarMensajeError(2, ERROR_MATCH);
                txtPassword.setValue("");
                txtPassword.focus();
            } else if (usuario.getId() == 0) {
                System.out.println("ERROR - CtrlAcceso idUsuario = 0");
                return;
            } else {
                menu = buscarVistaMenu();
                Sessions.getCurrent().setAttribute("menu", menu);
                int idUsuario = usuario.getId();
                String nombreUsuario = usuario.getNombre();
                int idRed = usuario.getIdRed();
                Sessions.getCurrent().setAttribute("idUsuario", idUsuario);
                Sessions.getCurrent().setAttribute("nombreUsuario", nombreUsuario);
                Sessions.getCurrent().setAttribute("usuarioLogueado", true);
                Sessions.getCurrent().setAttribute("idRed", idRed);
                System.out.println("CtrlAcceso.login: idRed = " + idRed);
                System.out.println("CtrlAcceso.login: idUsuario = " + idUsuario);
                System.out.println("CtrlAcceso.login: nombreUsuario = " + nombreUsuario);
                //redirección a página principal del usuario:
                Executions.sendRedirect("index2.zul");
            }
        }
    }

    /**
     * Comprueba los datos de acceso, login y password con los valores de la base de datos
     * NOTA: el login es convertido en minúscula
     * @param login
     * @param password
     * @return si es usuario válido
     */
    public boolean validarAcceso(String login, String password) {
        usuario = bd.buscarUsuario(login.toLowerCase(), password);
        if (usuario == null) {
            return false;
        } else if (usuario.getId() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * obtiene la vista de menú correspondiente al usuario
     * @param login
     * @param password
     * @return el url de la vista correspondiente al tipo de usuario
     */
    public String buscarVistaMenu() {
        String vista = "";
        if (usuario.getTipo() == Usuario.LIDER_RED) {
            vista = "LiderRed.zul";
        } else if (usuario.getTipo() == Usuario.LIDER_CELULA) {
            vista = "LiderCelula.zul";
        } else if (usuario.getTipo() == Usuario.ADMIN_CELULAS) {
            vista = "AdminCelulas.zul";
        }
        return vista;
    }

    public boolean camposLlenos() throws InterruptedException {
        limpiarMensajesError();
        if (txtLogin.getText().isEmpty()) {
            mostrarMensajeError(1, INSTRUCCION_USUARIO);
            return false;
        } else if (txtPassword.getText().isEmpty()) {
            mostrarMensajeError(2, INSTRUCCION_CONTRASEÑA);
            return false;
        }
        return true;
    }

    public void onOK$txtLogin() throws InterruptedException {
        limpiarMensajesError();
        if (!txtPassword.getText().isEmpty() && !txtLogin.getText().isEmpty()) {
            login();
        } else if (txtLogin.getText().isEmpty()) {
            mostrarMensajeError(1, INSTRUCCION_USUARIO);
        } else if (txtPassword.getText().isEmpty()) {
            mostrarMensajeError(2, INSTRUCCION_CONTRASEÑA);
        }
    }

    public void onOK$txtPassword() throws InterruptedException {
        limpiarMensajesError();
        if (!txtPassword.getText().isEmpty() && !txtLogin.getText().isEmpty()) {
            login();
        } else if (txtPassword.getText().isEmpty()) {
            mostrarMensajeError(2, INSTRUCCION_USUARIO);
        } else if (txtLogin.getText().equals("")) {
            mostrarMensajeError(1, INSTRUCCION_CONTRASEÑA);
        }
    }

    public void onClick$btnAcceso(Event event) throws InterruptedException {
        login();
    }

    void limpiarEntradas() {
        //TODO: colocar valores en blanco
        txtLogin.setValue("");
        txtPassword.setValue("");
        checkRecordarSesion.setChecked(false);
    }

    void limpiarMensajesError() {
        etqErrorLogin.setValue("");
        etqErrorPassword.setValue("");
        etqErrorLogin.setVisible(false);
        etqErrorPassword.setVisible(false);
    }

    void mostrarMensajeError(int elementoEntrada, String msj) {
        if (elementoEntrada == 1) {
            etqErrorLogin.setValue(msj);
            etqErrorLogin.setVisible(true);
            txtLogin.setFocus(true);
        } else if (elementoEntrada == 2) {
            etqErrorPassword.setValue(msj);
            etqErrorPassword.setVisible(true);
            txtPassword.setFocus(true);
        }
    }
}
