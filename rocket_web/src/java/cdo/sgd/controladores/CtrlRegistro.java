/**
 * CtrlRegistro.java
 */
package cdo.sgd.controladores;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import waytech.modelo.beans.sgi.Acceso;
import waytech.modelo.beans.sgi.AccesoInsert;
import waytech.modelo.beans.sgi.DiscipuloInsert;
import waytech.modelo.beans.sgi.Pareja;
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
 * @author Gerardo José Montilla Virgüez, Gabriel Pérez Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
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
    protected Button btnPaso2;
    protected Button btnPaso1;
    protected Button btnVerificarCedula;
    protected Button btnBuscarLider;
    protected Listbox listboxPareja;
    protected Label etqInstrucciones;
    protected Paging pagingPareja;
    protected Hbox hbox1;
    protected Vbox vbox2;
    protected Vbox vbox3;
    IsaPersona isaPersona = new SaPersona();
    Persona persona = new Persona();
    IsaAcceso isaAcceso = new SaAcceso();
    Acceso acceso = new Acceso();
    IsaPareja isaPareja = new SaPareja();
    IsaDiscipulo isaDiscipulo = new SaDiscipulo();
    private int idLider1 = 0;
    private int idLider2 = 0;
    UtilidadSistema utilidadSistema = new UtilidadSistema();
    UtilidadValidacion utilidadValidacion = new UtilidadValidacion();
    
    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        inicializar();
    }
    
    public void inicializar() {
        vbox3.setVisible(false);
        vbox2.setVisible(true);
        hbox1.setVisible(true);
        limpiarCampos();
        llenarListaLider("");
        listboxPareja.setVisible(false);
        pagingPareja.setVisible(false);
        txtCedula.setText("");
        txtCedula.setFocus(true);
        btnPaso1.setVisible(true);
        btnPaso2.setVisible(false);
    }
    
    public void limpiarCampos() {
        txtCorreo.setText("");
        txtNombre.setText("");
        txtPassword.setText("");
        txtRepetirPassword.setText("");
        txtTelefono.setText("");
        txtBuscarLider.setText("");
    }
    
    public void llenarListaLider(String nombre) {
        listboxPareja.setVisible(true);
        pagingPareja.setVisible(true);
        listboxPareja.getItems().clear();
        if (nombre.isEmpty()) {
            List<Persona> todosLosLideres = new ArrayList<Persona>();
            todosLosLideres = isaPersona.listPersonaCualquierLider().getTodasLasPersonas();
            for (Persona personaLider : todosLosLideres) {
                List<Pareja> todosLasParejas = new ArrayList<Pareja>();
                todosLasParejas = isaPareja.listParejaPorIdPersona(personaLider.getIdPersona()).getAllParejas();
                for (Pareja personaPareja : todosLasParejas) {
                    agregarItemListboxPareja(personaPareja);
                }
            }
        } else {
            List<Persona> todosLosLideres = new ArrayList<Persona>();
            todosLosLideres = isaPersona.listPersonaCualquierLiderNombre(nombre).getTodasLasPersonas();
            for (Persona personaLider : todosLosLideres) {
                List<Pareja> todosLasParejas = new ArrayList<Pareja>();
                todosLasParejas = isaPareja.listParejaPorIdPersona(personaLider.getIdPersona()).getAllParejas();
                for (Pareja personaPareja : todosLasParejas) {
                    agregarItemListboxPareja(personaPareja);
                }
            }
        }
    }
    
    public void agregarItemListboxPareja(Pareja pareja) {
        Listitem item = new Listitem();
        Listcell listCellVer = new Listcell();
        
        Listcell listCellIdPersona1 = new Listcell();
        Listcell listCellIdPersona2 = new Listcell();
        
        Listcell listCellNombre = new Listcell();
        Listcell listCellPareja = new Listcell();
        Button btnVer = new Button();
        btnVer.setImage("/Imagenes/Iconos/verModal16.png");
        listCellVer.appendChild(btnVer);
        btnVer.addEventListener("onClick", new EventListener() {
            
            public void onEvent(org.zkoss.zk.ui.event.Event arg0) throws Exception {
                Listitem listItemEnCurso = (Listitem) arg0.getTarget().getParent().getParent();
                Listitem fila = new Listitem();
                fila = listboxPareja.getItemAtIndex(listItemEnCurso.getIndex());
                List listaCelda = fila.getChildren();
                Listcell listCellIdPersona1 = (Listcell) listaCelda.get(1);
                Listcell listCellIdPersona2 = (Listcell) listaCelda.get(2);
                Listcell listCellNombre = (Listcell) listaCelda.get(3);
                Listcell listCellPareja = (Listcell) listaCelda.get(4);
                idLider1 = Integer.valueOf(listCellIdPersona1.getLabel());
                idLider2 = Integer.valueOf(listCellIdPersona2.getLabel());
                listboxPareja.setVisible(false);
                pagingPareja.setVisible(false);
                etqInstrucciones.setValue(listCellNombre.getLabel() + ", " + listCellPareja.getLabel() + " fueron seleccionados...");
                txtBuscarLider.setText(listCellNombre.getLabel() + ", " + listCellPareja.getLabel());
                btnPaso2.setFocus(true);
            }
        });
        listCellNombre.setLabel(pareja.getIdPersona1().getNombre());
        listCellPareja.setLabel(pareja.getIdPersona2().getNombre());
        listCellIdPersona1.setLabel(String.valueOf(pareja.getIdPersona1().getIdPersona()));
        listCellIdPersona2.setLabel(String.valueOf(pareja.getIdPersona2().getIdPersona()));
        item.appendChild(listCellVer);
        item.appendChild(listCellIdPersona1);
        item.appendChild(listCellIdPersona2);
        item.appendChild(listCellNombre);
        item.appendChild(listCellPareja);
        listboxPareja.appendChild(item);
    }
    
    public void verificarCedula() {
        limpiarCampos();
        if (!txtCedula.getText().isEmpty()) {
            if (isaPersona.esCedulaExistente(txtCedula.getText()).esCedulaExistente()) {
                persona = isaPersona.getPersonaPorCedula(txtCedula.getText().trim()).getPersona();
                acceso = isaAcceso.getAccesoPorIdUsuario(persona.getIdPersona()).getAcceso();
                if (!isaDiscipulo.esPersonaConLider(persona.getIdPersona()).esPersonaConLider()) {
                    //AQUI VA LA PARTE DONDE COMPLETA EL REGISTRO.
                    etqInstrucciones.setValue("Falta registrar tus lideres...");
                    vbox2.setVisible(false);
                    vbox3.setVisible(true);
                    btnPaso1.setVisible(false);
                    btnPaso2.setVisible(true);
                    txtBuscarLider.setFocus(true);
                } else {
                    etqInstrucciones.setValue("La cédula ya está registrada...");
                    txtCorreo.setText(acceso.getCorreo());
                    txtNombre.setText(persona.getNombre());
                    txtTelefono.setText(persona.getTelefonoHabitacion());
                    txtCedula.setFocus(true);
                    txtCorreo.setDisabled(true);
                    txtNombre.setDisabled(true);
                    txtPassword.setDisabled(true);
                    txtRepetirPassword.setDisabled(true);
                    txtTelefono.setDisabled(true);
                    btnPaso1.setVisible(false);
                    btnPaso2.setVisible(false);
                }
            } else {
                etqInstrucciones.setValue("LLena tus datos básicos.");
                txtCorreo.setDisabled(false);
                txtNombre.setDisabled(false);
                txtPassword.setDisabled(false);
                txtRepetirPassword.setDisabled(false);
                txtTelefono.setDisabled(false);
                txtNombre.setFocus(true);
                btnPaso1.setVisible(true);
                btnPaso2.setVisible(false);
            }
        }
    }
    
    public void registrarDatosPaso1() throws InterruptedException {
        if (!txtCedula.getText().isEmpty()) {
            if (!txtCorreo.getText().isEmpty() && utilidadValidacion.esCorreoValido(txtCorreo.getText())) {
                if (!txtNombre.getText().isEmpty()) {
                    if (!txtPassword.getText().isEmpty()) {
                        if (!txtRepetirPassword.getText().isEmpty()) {
                            if (txtPassword.getText().equals(txtRepetirPassword.getText())) {
                                if (!txtTelefono.getText().isEmpty() && utilidadValidacion.sonSoloNumeros(txtTelefono.getText())) {                                    
                                    PersonaInsert personaInsert = new PersonaInsert();
                                    personaInsert.setApellido("NA");
                                    personaInsert.setCi(txtCedula.getText());
                                    personaInsert.setDireccionHabitacion("NA");
                                    personaInsert.setDireccionTrabajo("NA");
                                    personaInsert.setEsAnfitrion(false);
                                    personaInsert.setEsDiscipuloEnProceso(false);
                                    personaInsert.setEsEstaca(false);
                                    personaInsert.setEsLiderCelula(false);
                                    personaInsert.setEsLiderLanzado(false);
                                    personaInsert.setEsLiderRed(false);
                                    personaInsert.setEsLiderSupervisor(false);
                                    personaInsert.setEsMaestroAcademia(false);
                                    personaInsert.setEsSupervisor(false);
                                    personaInsert.setEstadoCivil("NA");
                                    personaInsert.setFacebook("NA");
                                    personaInsert.setFechaBautizo("1900-01-01");
                                    personaInsert.setFechaConversion("1900-01-01");
                                    personaInsert.setFechaEncuentro("1900-01-01");
                                    personaInsert.setFechaGraduacionAcademia("1900-01-01");
                                    personaInsert.setFechaLanzamiento("1900-01-01");
                                    personaInsert.setFechaNacimiento("1900-01-01");
                                    personaInsert.setIdTipoPersona(1);
                                    personaInsert.setIdZona(1);
                                    personaInsert.setNombre(txtNombre.getText());
                                    //EN ESTE PASO NO SE HAN REGISTRADO SUS LIDERES, 20%
                                    personaInsert.setPorcentajeCompletadoPerfil(20);
                                    personaInsert.setProfesion("NA");
                                    personaInsert.setTelefonoHabitacion(txtTelefono.getText());
                                    personaInsert.setTelefonoMovil(txtTelefono.getText());
                                    personaInsert.setTelefonoTrabajo(txtTelefono.getText());
                                    personaInsert.setTwitter("NA");
                                    RspPersona rspPersona = new RspPersona();
                                    rspPersona = isaPersona.insertPersona(personaInsert);
                                    AccesoInsert accesoInsert = new AccesoInsert();
                                    accesoInsert.setCorreo(txtCorreo.getText());
                                    accesoInsert.setFechaUltimoAcceso("1900-01-01");
                                    accesoInsert.setIdPersona(rspPersona.getPersona().getIdPersona());
                                    accesoInsert.setLogin(txtCorreo.getText());
                                    accesoInsert.setPassword(txtPassword.getText());
                                    RspAcceso rspAcceso = new RspAcceso();
                                    rspAcceso = isaAcceso.insertAcceso(accesoInsert);
                                    if (rspAcceso.esSentenciaSqlEjecutadaExitosamente() || rspPersona.esSentenciaSqlEjecutadaExitosamente()) {
                                        persona = rspPersona.getPersona();
                                        etqInstrucciones.setValue("Registros iniciales almacenados exitosamente..");
                                        vbox2.setVisible(false);
                                        vbox3.setVisible(true);
                                        btnPaso1.setVisible(false);
                                        btnPaso2.setVisible(true);
                                        txtBuscarLider.setFocus(true);
                                    } else {
                                        etqInstrucciones.setValue("Registros iniciales no fueron almacenados exitosamente..");
                                    }
                                } else {
                                    etqInstrucciones.setValue("Verifica los datos del telefono..");
                                    txtTelefono.setFocus(true);
                                }
                            } else {
                                etqInstrucciones.setValue("Los passwords no coinciden...");
                                txtPassword.setText("");
                                txtRepetirPassword.setText("");
                                txtPassword.setFocus(true);
                            }
                        } else {
                            etqInstrucciones.setValue("Revise el campo del password");
                            txtPassword.setFocus(true);
                        }
                    } else {
                        etqInstrucciones.setValue("Revise el campo del password..");
                        txtRepetirPassword.setFocus(true);
                    }
                } else {
                    etqInstrucciones.setValue("Revise el campo del nombre..");
                    txtNombre.setFocus(true);
                    
                }
            } else {
                etqInstrucciones.setValue("Revise el campo de correo..");
                txtCorreo.setFocus(true);
            }
        } else {
            etqInstrucciones.setValue("Revise el campo de cédula..");
            txtCedula.setFocus(true);
        }
    }
    
    public void registrarDatosPaso2() {
        if (idLider2 != 0 && idLider1 != 0) {
            DiscipuloInsert discipuloInsert = new DiscipuloInsert();
            discipuloInsert.setFechaInicio("1900-01-01");
            discipuloInsert.setIdPersona1(idLider1);
            discipuloInsert.setIdPersona2(persona.getIdPersona());
            if (isaDiscipulo.insertDiscipulo(discipuloInsert).esSentenciaSqlEjecutadaExitosamente()) {
                etqInstrucciones.setValue("Información almacenada apropiadamente..");
                inicializar();
            } else {
                etqInstrucciones.setValue("Ah ocurrido un error al guardar los datos..");
            }
        } else {
            etqInstrucciones.setValue("No se ha elegido la pareja, intenta de nuevo..");
            txtBuscarLider.setText("");
            txtBuscarLider.setFocus(true);
            llenarListaLider(txtBuscarLider.getText());
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
        llenarListaLider(txtBuscarLider.getText());
    }
    
    public void onClick$btnVerificarCedula(Event event) throws InterruptedException {
        verificarCedula();
    }
    
    public void onClick$btnPaso1(Event event) throws InterruptedException {
        registrarDatosPaso1();
    }
    
    public void onClick$btnPaso2(Event event) throws InterruptedException {
        registrarDatosPaso2();
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
    
    public void onOK$txtBuscarLider(Event event) throws InterruptedException {
        llenarListaLider(txtBuscarLider.getText());
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
        registrarDatosPaso1();
    }
}
