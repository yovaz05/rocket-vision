package sig.controladores;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import waytech.modelo.beans.sgi.Acceso;
import waytech.modelo.interfaces.IsaAcceso;
import waytech.modelo.servicios.SaAcceso;
import waytech.utilidades.UtilidadSistema;

/**
 * Clase ControladorUsuarioAdministradorPrincipal 
 * @since 28/02/2011
 * @version 1.1
 * @author (R)2011 Gerardo Jose Montilla Virguez
 */
public class CtrlAccesoListado extends Window implements AfterCompose {

    String estiloVbox = (String) Sessions.getCurrent().getAttribute("estiloVbox");
    String estiloLetras = (String) Sessions.getCurrent().getAttribute("estiloLetras");
    String estiloLabel = (String) Sessions.getCurrent().getAttribute("estiloLabel");
    String estiloNumerosAzules = (String) Sessions.getCurrent().getAttribute("estiloNumerosAzules");
    String estiloNumerosRojos = (String) Sessions.getCurrent().getAttribute("estiloNumerosRojos");
    protected Combobox cmbSucursal;
    protected Textbox txtRif;
    protected Textbox txtNombre;
    protected Textbox txtApellido;
    protected Textbox txtCorreo;
    protected Textbox txtPassword;
    protected Datebox dateboxFechaNacimiento;
    protected Datebox dateboxFechaContratacion;
    protected Textbox txtLogin;
    
    protected Textbox txtPassword1;
    protected Vbox vbox1;
    protected Vbox vbox2;
    protected Listheader listHeaderBotonVer;
    protected Listheader listheaderCodigoUsuario;
    protected Listheader listheaderNombreUsuario;
    
    protected Listheader listheaderCorreo;
    protected Listheader listheaderPassword;
    UtilidadSistema utilidadSistema = new UtilidadSistema();
    protected Listbox listboxUsuario;

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        inicializarComponentes();
    }

    public void inicializarComponentes() {
        inicializarListboxUsuario();
        visualizarVbox1();
        
        vbox1.setStyle(estiloVbox);
        vbox2.setStyle(estiloVbox);
    }

    public void inicializarListboxUsuario() {
        inicializarListheadersUsuario();
        inicializarCeldasUsuario();
        llenarListboxUsuario();
    }

    public void llenarListboxUsuario() {
        IsaAcceso isaAcceso = new SaAcceso();
        List<Acceso> todosLosAccesos = new ArrayList<Acceso>();
        todosLosAccesos = isaAcceso.listAcceso().getTodosLosAccesos();        
        for (Acceso acceso : todosLosAccesos) {
            agregarItemListboxUsuario(acceso);
        }
    }

    public void inicializarListheadersUsuario() {
        String estilo = "color: #3399CC; font-weight:bold;";
        listHeaderBotonVer.setStyle(estilo);
        listheaderCodigoUsuario.setStyle(estilo);
        listheaderNombreUsuario.setStyle(estilo);
        
        listheaderCorreo.setStyle(estilo);
        listheaderPassword.setStyle(estilo);
    }

    public void inicializarCeldasUsuario() {
        String estilo = "color: #4682B4; font-weight:bold;";
    }

    public void agregarItemListboxUsuario(Acceso acceso) {
        Listitem item = new Listitem();
        Listcell listCellVer = new Listcell();
        Listcell listCellCodigoUsuario = new Listcell();
        Listcell listCellNombre = new Listcell();        
        Listcell listCellCorreo = new Listcell();
        Listcell listCellPassword = new Listcell();
        listCellVer.setStyle(estiloNumerosAzules);        
        listCellCodigoUsuario.setStyle(estiloNumerosAzules);
        listCellCorreo.setStyle(estiloNumerosAzules);
        listCellNombre.setStyle(estiloNumerosAzules);
        listCellPassword.setStyle(estiloNumerosAzules);
        Button btnVer = new Button();
        btnVer.setImage("/Imagenes/Iconos/verModal16.png");
        listCellVer.appendChild(btnVer);
        btnVer.addEventListener("onClick", new EventListener() {

            public void onEvent(org.zkoss.zk.ui.event.Event arg0) throws Exception {
                Listitem listItemEnCurso = (Listitem) arg0.getTarget().getParent().getParent();
                Listitem fila = new Listitem();
                fila = listboxUsuario.getItemAtIndex(listItemEnCurso.getIndex());
                List listaCelda = fila.getChildren();
                Listcell listCellCodigoUsuario = (Listcell) listaCelda.get(1);
                //SE DEBE OPTIMIZAR EL CÓDIGO
//                Empleado empleado = new Empleado();
//                empleado = servicioAdministracionEmpleado.obtenerEmpleado(Integer.valueOf(listCellCodigoUsuario.getLabel()));                
//                txtApellido.setText(empleado.getApellidoEmpleado());
//                txtCorreo.setText(empleado.getCorreo());
//                txtRif.setText(empleado.getRif());
//                AccesoEmpleado accesoEmpleado = new AccesoEmpleado();
//                accesoEmpleado = servicioAdministracionAccesoEmpleado.obtenerAccesoEmpleado1(empleado.getIdEmpleado());
//                txtLogin.setText(accesoEmpleado.getLogin());
//                txtNombre.setText(empleado.getNombreEmpleado());
//                txtPassword.setText(empleado.getPassword());
//                SucursalEmpleado sucursalEmpleado = new SucursalEmpleado();
//                sucursalEmpleado = servicioAdministracionSucursalEmpleado.obtenerSucursalEmpleado(empleado.getIdSucursalEmpleado().getIdSucursalEmpleado());
//                cmbSucursal.setText(sucursalEmpleado.getNombre());
//                dateboxFechaContratacion.setText(empleado.getFechaContratacion().toString());
//                dateboxFechaNacimiento.setText(empleado.getFechaNacimiento().toString()); 
//                txtRif.setReadonly(true);
//                visualizarVbox2();
            }
        });        
        listCellCodigoUsuario.setLabel(acceso.getIdPersona().getCi());
        listCellCorreo.setLabel(acceso.getCorreo());
        listCellNombre.setLabel(acceso.getIdPersona().getNombre());
        listCellPassword.setLabel(acceso.getPassword());
        item.appendChild(listCellVer);
        item.appendChild(listCellCodigoUsuario);
        item.appendChild(listCellNombre);        
        item.appendChild(listCellCorreo);
        item.appendChild(listCellPassword);
        listboxUsuario.appendChild(item);
    }

    public void visualizarVbox1() {
        vbox1.setVisible(true);
        vbox2.setVisible(false);
    }

    public void visualizarVbox2() {
        vbox1.setVisible(false);
        vbox2.setVisible(true);
    }

    public void onClick$btnAgregarUsuario(Event event) {
        visualizarVbox2();
        txtRif.setReadonly(false);
    }

    public void onBlur$txtRif(Event event) {
        if (!txtRif.getText().equals("")) {
//            if(servicioAdministracionEmpleado.esEmpleadoValido(txtRif.getText().trim())) {
//                txtRif.setReadonly(true);
//                Empleado empleado = new Empleado();
//                empleado = servicioAdministracionEmpleado.obtenerEmpleado1(txtRif.getText().trim());
//                txtApellido.setText(empleado.getApellidoEmpleado());
//                txtCorreo.setText(empleado.getCorreo());
//                AccesoEmpleado accesoEmpleado = new AccesoEmpleado();
//                accesoEmpleado = servicioAdministracionAccesoEmpleado.obtenerAccesoEmpleado1(empleado.getIdEmpleado());
//                txtLogin.setText(accesoEmpleado.getLogin());
//                txtNombre.setText(empleado.getNombreEmpleado());
//                txtPassword.setText(empleado.getPassword());
//                SucursalEmpleado sucursalEmpleado = new SucursalEmpleado();
//                sucursalEmpleado = servicioAdministracionSucursalEmpleado.obtenerSucursalEmpleado(empleado.getIdSucursalEmpleado().getIdSucursalEmpleado());
//                cmbSucursal.setText(sucursalEmpleado.getNombre());
//                dateboxFechaContratacion.setText(empleado.getFechaContratacion().toString());
//                dateboxFechaNacimiento.setText(empleado.getFechaNacimiento().toString());                
//            }
        }
    }

    public void onClick$btnRegistrar(Event event) throws InterruptedException {
        registrarUsuario();
    }

    public void onClick$btnActualizar(Event event) throws InterruptedException {
//        if (!txtApellido.getText().trim().equals("")) {
//            if (!txtCorreo.getText().trim().equals("")) {
//                if (!txtLogin.getText().trim().equals("")) {
//                    if (!txtNombre.getText().trim().equals("")) {
//                        if (!txtPassword.getText().trim().equals("")) {
//                            if (!txtPassword1.getText().trim().equals("")) {
//                                if (!txtRif.getText().trim().equals("")) {
//                                    if (!txtPassword.getText().trim().equals("")) {
//                                        if (txtPassword.getText().trim().equals(txtPassword1.getText().trim())) {
//                                            if (!cmbSucursal.getText().trim().equals("")) {
//                                                if (!dateboxFechaContratacion.getText().trim().equals("")) {
//                                                    if (!dateboxFechaNacimiento.getText().trim().equals("")) {
//                                                        if (servicioAdministracionEmpleado.esEmpleadoValido(txtRif.getText())) {
//                                                            txtRif.setReadonly(false);
//                                                            Empleado empleadoAntiguo = new Empleado();
//                                                            empleadoAntiguo = servicioAdministracionEmpleado.obtenerEmpleado1(txtRif.getText());
//                                                            AccesoEmpleado accesoEmpleadoAntiguo = new AccesoEmpleado();
//                                                            accesoEmpleadoAntiguo = servicioAdministracionAccesoEmpleado.obtenerAccesoEmpleado1(empleadoAntiguo.getIdEmpleado());
//                                                            Empleado empleadoNuevo = new Empleado();
//                                                            empleadoNuevo = empleadoAntiguo;
//                                                            empleadoNuevo.setCorreo(txtCorreo.getText());
//                                                            empleadoNuevo.setFechaContratacionSTR(dateboxFechaContratacion.getText());
//                                                            empleadoNuevo.setFechaNacimientoSTR(dateboxFechaNacimiento.getText());
//                                                            empleadoNuevo.setIdSucursalEmpleado(servicioAdministracionSucursalEmpleado.obtenerSucursalEmpleado(cmbSucursal.getText()));
//                                                            empleadoNuevo.setPassword(txtPassword.getText());
//                                                            AccesoEmpleado accesoEmpleadoNuevo = new AccesoEmpleado();
//                                                            accesoEmpleadoNuevo = accesoEmpleadoAntiguo;
//                                                            accesoEmpleadoNuevo.setLogin(txtLogin.getText());
//                                                            accesoEmpleadoNuevo.setPassword(txtPassword.getText());
//                                                            Sessions.getCurrent().setAttribute("accesoEmpleadoNuevo", accesoEmpleadoNuevo);
//                                                            Sessions.getCurrent().setAttribute("empleadoNuevo", empleadoNuevo);
//                                                            limpiarCamposFormulario();
//                                                            if ((!servicioAdministracionAccesoEmpleado.esLoginValido(txtLogin.getText())) || txtLogin.getText().equals(accesoEmpleadoAntiguo.getLogin())) {
//                                                                if ((Messagebox.show("¿Desea almancenar la información en la base de datos?", "Pregunta!", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
//
//                                                                    public void onEvent(Event evt) throws InterruptedException {
//                                                                        switch (((Integer) evt.getData()).intValue()) {
//                                                                            case Messagebox.YES:
//                                                                                Empleado empleadoNuevo = (Empleado) Sessions.getCurrent().getAttribute("empleadoNuevo");
//                                                                                AccesoEmpleado accesoEmpleadoNuevo = (AccesoEmpleado) Sessions.getCurrent().getAttribute("accesoEmpleadoNuevo");
//                                                                                servicioAdministracionEmpleado.actualizarEmpleado(empleadoNuevo);
//                                                                                servicioAdministracionAccesoEmpleado.actualizarAcceso(accesoEmpleadoNuevo);
//                                                                                Messagebox.show("ALMACENADA LA INFORMACION EXITOSAMENTE    ", "Información", Messagebox.OK, Messagebox.INFORMATION);
//                                                                                visualizarVbox1();
//                                                                                actualizarListaEmpleado();
//                                                                            case Messagebox.NO:
//                                                                        }
//                                                                    }
//                                                                }) == Messagebox.YES)) {
//                                                                }
//                                                            } else {
//                                                                Messagebox.show("EL LOGIN YA FUE REGISTRADO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                                                txtLogin.setFocus(true);
//                                                            }
//                                                        } else {
//                                                            Messagebox.show("ESTE CLIENTE NO ESTÁ REGISTRADO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                                        }
//                                                    } else {
//                                                        Messagebox.show("LA FECHA DE NACIMIENTO NO ES VÁLIDA", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                                        dateboxFechaNacimiento.setFocus(true);
//                                                    }
//                                                } else {
//                                                    Messagebox.show("LA FECHA DE CONTRATACIÓN NO ES VÁLIDA", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                                    dateboxFechaContratacion.setFocus(true);
//                                                }
//                                            } else {
//                                                Messagebox.show("LA SUCURSAL NO HA SIDO SELECCIONADA", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                                cmbSucursal.setFocus(true);
//                                            }
//                                        } else {
//                                            Messagebox.show("LOS PASSWORDS NO COINCIDEN", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                            txtPassword.setFocus(true);
//                                        }
//                                    } else {
//                                        Messagebox.show(" EL TELÉFONO NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                        txtPassword.setFocus(true);
//                                    }
//                                } else {
//                                    Messagebox.show("EL RIF NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                    txtRif.setFocus(true);
//                                }
//                            } else {
//                                Messagebox.show("EL PASSWORD NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                txtPassword1.setFocus(true);
//                            }
//                        } else {
//                            Messagebox.show("EL PASSWORD NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                            txtPassword.setFocus(true);
//                        }
//                    } else {
//                        Messagebox.show("EL NOMBRE NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                        txtNombre.setFocus(true);
//                    }
//                } else {
//                    Messagebox.show("EL LOGIN NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                    txtLogin.setFocus(true);
//                }
//            } else {
//                Messagebox.show("LA DIRECCIÓN NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                txtCorreo.setFocus(true);
//            }
//        } else {
//            Messagebox.show("EL APELLIDO NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//            txtApellido.setFocus(true);
//        }
    }

    public void limpiarCamposFormulario() {
        txtApellido.setText("");
        txtCorreo.setText("");
        txtLogin.setText("");
        txtNombre.setText("");
        txtPassword.setText("");
        txtPassword1.setText("");
        txtRif.setText("");
        txtPassword.setText("");
        dateboxFechaContratacion.setText("");
        dateboxFechaNacimiento.setText("");
    }

    public void registrarUsuario() throws InterruptedException {
//        if (!txtApellido.getText().trim().equals("")) {
//            if (!txtCorreo.getText().trim().equals("")) {
//                if (!txtLogin.getText().trim().equals("")) {
//                    if (!txtNombre.getText().trim().equals("")) {
//                        if (!txtPassword.getText().trim().equals("")) {
//                            if (!txtPassword1.getText().trim().equals("")) {
//                                if (!txtRif.getText().trim().equals("")) {
//                                    if (!txtPassword.getText().trim().equals("")) {
//                                        if (txtPassword.getText().trim().equals(txtPassword1.getText().trim())) {
//                                            if (!cmbSucursal.getText().trim().equals("")) {
//                                                if (!dateboxFechaContratacion.getText().trim().equals("")) {
//                                                    if (!dateboxFechaNacimiento.getText().trim().equals("")) {
//                                                        if (!servicioAdministracionEmpleado.esEmpleadoValido(txtRif.getText())) {
//                                                            if (!servicioAdministracionAccesoEmpleado.esLoginValido(txtLogin.getText())) {
//                                                                if ((Messagebox.show("¿Desea almancenar la información en la base de datos?", "Pregunta!", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
//
//                                                                    public void onEvent(Event evt) throws InterruptedException {
//                                                                        switch (((Integer) evt.getData()).intValue()) {
//                                                                            case Messagebox.YES:
//                                                                                Empleado empleado = new Empleado();
//                                                                                SucursalEmpleado sucursalEmpleado = new SucursalEmpleado();
//                                                                                sucursalEmpleado = servicioAdministracionSucursalEmpleado.obtenerSucursalEmpleado(cmbSucursal.getText());
//                                                                                empleado.setIdSucursalEmpleado(sucursalEmpleado);
//                                                                                empleado.setApellidoEmpleado(txtApellido.getText());
//                                                                                empleado.setCargo("Administrador");
//                                                                                empleado.setCorreo(txtCorreo.getText());
//                                                                                empleado.setEstado(true);
//                                                                                empleado.setFechaContratacionSTR(dateboxFechaContratacion.getText());
//                                                                                empleado.setFechaNacimientoSTR(dateboxFechaNacimiento.getText());
//                                                                                empleado.setFoto(null);
//                                                                                empleado.setIdEmpleado(1);
//                                                                                empleado.setNombreEmpleado(txtNombre.getText());
//                                                                                empleado.setRif(txtRif.getText());
//                                                                                empleado.setSuperiorInmediato(1);
//                                                                                empleado.setPassword(txtPassword.getText());
//                                                                                empleado.setTraza(utilidadSistema.generarTraza());
//                                                                                servicioAdministracionEmpleado.insertarEmpleado(empleado);
//                                                                                empleado.setIdEmpleado(servicioAdministracionEmpleado.obtenerEmpleado(empleado.getTraza()).getIdEmpleado());
//                                                                                AccesoEmpleado accesoEmpleado = new AccesoEmpleado();
//                                                                                accesoEmpleado.setEstado(true);
//                                                                                accesoEmpleado.setIdEmpleado(empleado);
//                                                                                accesoEmpleado.setLogin(txtLogin.getText());
//                                                                                accesoEmpleado.setPassword(txtPassword.getText());
//                                                                                servicioAdministracionAccesoEmpleado.insertarAccesoValido(accesoEmpleado);
//                                                                                limpiarCamposFormulario();
//                                                                                Messagebox.show("ALMACENADA LA INFORMACION EXITOSAMENTE    ", "Información", Messagebox.OK, Messagebox.INFORMATION);
//                                                                                visualizarVbox1();
//                                                                                actualizarListaEmpleado();
//                                                                            case Messagebox.NO:
//                                                                        }
//                                                                    }
//                                                                }) == Messagebox.YES)) {
//                                                                }
//                                                            } else {
//                                                                Messagebox.show("EL LOGIN YA FUE REGISTRADO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                                                txtLogin.setText("");
//                                                                txtLogin.setFocus(true);
//                                                            }
//                                                        } else {
//                                                            Messagebox.show("ESTE CLIENTE YA FUE REGISTRADO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                                        }
//                                                    } else {
//                                                        Messagebox.show("LA FECHA DE NACIMIENTO NO ES VÁLIDA", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                                        dateboxFechaNacimiento.setFocus(true);
//                                                    }
//                                                } else {
//                                                    Messagebox.show("LA FECHA DE CONTRATACIÓN NO ES VÁLIDA", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                                    dateboxFechaContratacion.setFocus(true);
//                                                }
//                                            } else {
//                                                Messagebox.show("LA SUCURSAL NO HA SIDO SELECCIONADA", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                                cmbSucursal.setFocus(true);
//                                            }
//                                        } else {
//                                            Messagebox.show("LOS PASSWORDS NO COINCIDEN", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                            txtPassword.setFocus(true);
//                                        }
//                                    } else {
//                                        Messagebox.show(" EL TELÉFONO NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                        txtPassword.setFocus(true);
//                                    }
//                                } else {
//                                    Messagebox.show("EL RIF NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                    txtRif.setFocus(true);
//                                }
//                            } else {
//                                Messagebox.show("EL PASSWORD NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                                txtPassword1.setFocus(true);
//                            }
//                        } else {
//                            Messagebox.show("EL PASSWORD NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                            txtPassword.setFocus(true);
//                        }
//                    } else {
//                        Messagebox.show("EL NOMBRE NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                        txtNombre.setFocus(true);
//                    }
//                } else {
//                    Messagebox.show("EL LOGIN NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                    txtLogin.setFocus(true);
//                }
//            } else {
//                Messagebox.show("LA DIRECCIÓN NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//                txtCorreo.setFocus(true);
//            }
//        } else {
//            Messagebox.show("EL APELLIDO NO ES VÁLIDO", "VERIFIQUE", Messagebox.OK, Messagebox.EXCLAMATION);
//            txtApellido.setFocus(true);
//        }
    }

    public void llenarComboSucursal() {
//        List<SucursalEmpleado> listaSucursales = new ArrayList<SucursalEmpleado>();
//        listaSucursales = servicioAdministracionSucursalEmpleado.obtenerListaSucursalEmpleado();
//        for (SucursalEmpleado sucursalEmpleado : listaSucursales) {
//            cmbSucursal.appendItem(sucursalEmpleado.getNombre());
//        }
//        cmbSucursal.setReadonly(true);
    }

    public void colocarValoresInicialesListHeader() {
        Listitem item = new Listitem();
    }

    public void onClick$btnCancelar(Event event) {
        limpiarCamposFormulario();
        txtRif.setReadonly(true);
        visualizarVbox1();
    }

    public void actualizarListaEmpleado() {
        listboxUsuario.getItems().clear();
        llenarListboxUsuario();
    }

    public void onClick$btnActualizarLista(Event event) {
        actualizarListaEmpleado();
    }
}