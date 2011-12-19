/**
 * CtrlRegistro.java
 */
package cdo.sgd.controladores;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import waytech.modelo.beans.sgi.TipoPersona;
import waytech.modelo.interfaces.IsaTipoPersona;
import waytech.modelo.servicios.SaTipoPersona;

/**
 * @since Viernes 15/12/2011 02:35 PM.
 * @version 1.0 Viernes 15/12/2011 02:35 PM.
 * @author Gerardo José Montilla Virgüez, Gabriel Pérez Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class CtrlTipoPersonaListado extends Window implements AfterCompose {

    protected Button btnPaso2;
    protected Button btnPaso1;
    protected Listbox listboxTipoPersona;
    protected Label etqInstrucciones;
    protected Paging pagingTipoPersona;
    protected Vbox vbox1;
    IsaTipoPersona isaTipoPersona = new SaTipoPersona();

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        inicializar();
    }

    public void inicializar() {
        llenarListaLider();
    }

    public void llenarListaLider() {
        listboxTipoPersona.setVisible(true);
        pagingTipoPersona.setVisible(true);
        listboxTipoPersona.getItems().clear();
        List<TipoPersona> todosLosTiposPersona = new ArrayList<TipoPersona>();
        todosLosTiposPersona = isaTipoPersona.listTipoPersonas().getAllTipoPersona();
        for (TipoPersona tipoPersona : todosLosTiposPersona) {
            agregarItemListboxTipoPersona(tipoPersona);
        }
    }

    public void agregarItemListboxTipoPersona(TipoPersona tipoPersona) {
        Listitem item = new Listitem();
        Listcell listCellVer = new Listcell();
        Listcell listCellTipoPersona = new Listcell();
        Button btnVer = new Button();
        btnVer.setImage("/Imagenes/Iconos/verModal16.png");
        listCellVer.appendChild(btnVer);
        btnVer.addEventListener("onClick", new EventListener() {

            public void onEvent(org.zkoss.zk.ui.event.Event arg0) throws Exception {
                //FALTA PROGRAMAR ESTA PARTE CON VARIAS FUNCIONALIDADES.                
            }
        });
        listCellTipoPersona.setLabel(tipoPersona.getNombre());
        item.appendChild(listCellVer);
        item.appendChild(listCellTipoPersona);
        listboxTipoPersona.appendChild(item);
    }
}
