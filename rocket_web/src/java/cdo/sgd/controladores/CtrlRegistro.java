/**
 * CtrlRegistro.java
 */
package cdo.sgd.controladores;

import cdo.sgd.modelo.bd.simulador.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import waytech.modelo.beans.sgi.Acceso;
import waytech.modelo.beans.sgi.Pareja;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.interfaces.IsaAcceso;
import waytech.modelo.interfaces.IsaPareja;
import waytech.modelo.interfaces.IsaPersona;
import waytech.modelo.servicios.SaAcceso;
import waytech.modelo.servicios.SaPareja;
import waytech.modelo.servicios.SaPersona;

/**
 * Controlador de Vista: Registro.zul
 * @author Gabriel
 */
public class CtrlRegistro extends Window implements AfterCompose {

    protected Intbox txtCedula;
    protected Textbox txtTelefono;
    protected Textbox txtNombre;
    protected Textbox txtCorreo;
    protected Textbox txtPassword;
    protected Textbox txtRepetirPassword;
    protected Button btnIngresar;
    protected Button btnVerificarCedula;
    protected Listbox listboxPareja;
    
    protected Label etqInstrucciones;
    IsaPersona isaPersona = new SaPersona();
    Persona persona = new Persona();
    IsaAcceso isaAcceso = new SaAcceso();
    Acceso acceso = new Acceso();
    IsaPareja isaPareja = new SaPareja();

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        inicializar();
    }

    public void inicializar() {
        txtCedula.setFocus(true);
        llenarListaLider();
    }

    public void limpiarCampos() {
        txtCorreo.setText("");
        txtNombre.setText("");
        txtPassword.setText("");
        txtRepetirPassword.setText("");
        txtTelefono.setText("");
    }
    
    public void llenarListaLider() {
//        cmbLider.getItems().clear();
//        cmbLider.setReadonly(true);
        List<Persona> todosLosLideres = new ArrayList<Persona>();
        todosLosLideres = isaPersona.listPersonaCualquierLider().getTodasLasPersonas();
        
        for(Persona personaLider:todosLosLideres) {
            List<Pareja> todosLasParejas = new ArrayList<Pareja>();
            todosLasParejas = isaPareja.listParejaPorIdPersona(personaLider.getIdPersona()).getAllParejas();
            for(Pareja personaPareja:todosLasParejas) {
                agregarItemListboxPareja(personaPareja);
            }                                        
        }
    }
    
    
    public void agregarItemListboxPareja(Pareja pareja) {
        Listitem item                       = new Listitem();        
        Listcell listCellVer                = new Listcell();                
        Listcell listCellNombre             = new Listcell();
        Listcell listCellPareja             = new Listcell();        
        Button btnVer = new Button();
        btnVer.setImage("/Imagenes/Iconos/verModal16.png");
        listCellVer.appendChild(btnVer);        
        btnVer.addEventListener("onClick", new EventListener() {
            public void onEvent(org.zkoss.zk.ui.event.Event arg0) throws Exception {                                                                
                Listitem listItemEnCurso                = (Listitem) arg0.getTarget().getParent().getParent();                                                                                
                Listitem fila                           = new Listitem();
                fila                                    = listboxPareja.getItemAtIndex(listItemEnCurso.getIndex());
                List listaCelda                         = fila.getChildren();                                                
                Listcell listCellCodigoUsuario          = (Listcell) listaCelda.get(1);
                //SE DEBE OPTIMIZAR EL CÓDIGO
                Pareja pareja = new Pareja();
//                empleado = servicioAdministracionEmpleado.obtenerEmpleado(Integer.valueOf(listCellCodigoUsuario.getLabel()));                
//                txtApellido.setText(empleado.getApellidoEmpleado());
//                txtDireccion.setText(empleado.getDireccionHabitacion());
//                txtRif.setText(empleado.getRif());
//                AccesoEmpleado accesoEmpleado = new AccesoEmpleado();
//                accesoEmpleado = servicioAdministracionAccesoEmpleado.obtenerAccesoEmpleado1(empleado.getIdEmpleado());
//                txtLogin.setText(accesoEmpleado.getLogin());
//                txtNombre.setText(empleado.getNombreEmpleado());
//                txtTelefono.setText(empleado.getTelefono());
//                SucursalEmpleado sucursalEmpleado = new SucursalEmpleado();
//                sucursalEmpleado = servicioAdministracionSucursalEmpleado.obtenerSucursalEmpleado(empleado.getIdSucursalEmpleado().getIdSucursalEmpleado());
//                cmbSucursal.setText(sucursalEmpleado.getNombre());
//                dateboxFechaContratacion.setText(empleado.getFechaContratacion().toString());
//                dateboxFechaNacimiento.setText(empleado.getFechaNacimiento().toString()); 
//                txtRif.setReadonly(true);
//                visualizarVbox2();
            }
        });                                            
        listCellNombre.setLabel(pareja.getIdPersona1().getNombre());
        listCellPareja.setLabel(pareja.getIdPersona2().getNombre());        
        item.appendChild(listCellVer);        
        item.appendChild(listCellNombre);                
        item.appendChild(listCellPareja);        
        listboxPareja.appendChild(item);           
    }                    
    
    
    

    public void verificarCedula() {
        if (!txtCedula.getText().isEmpty()) {
            if (isaPersona.esCedulaExistente(txtCedula.getText()).esCedulaExistente()) {
                etqInstrucciones.setValue("La cédula ya está registrada...");
                persona = isaPersona.getPersonaPorCedula(txtCedula.getText().trim()).getPersona();
                acceso = isaAcceso.getAccesoPorIdUsuario(persona.getIdPersona()).getAcceso();
                txtCorreo.setText(acceso.getCorreo());
                txtNombre.setText(persona.getNombre());
                txtTelefono.setText(persona.getTelefonoHabitacion());
            } else {
                etqInstrucciones.setValue("OK...");
            }
        }
    }

    public void onClick$btnVerificarCedula(Event event) throws InterruptedException {
        verificarCedula();
    }
    
    public void onBlur$txtCedula(Event event) throws InterruptedException {
        verificarCedula();
    }
    
    public void onOK$txtCedula(Event event) throws InterruptedException {
        verificarCedula();
    }
}
