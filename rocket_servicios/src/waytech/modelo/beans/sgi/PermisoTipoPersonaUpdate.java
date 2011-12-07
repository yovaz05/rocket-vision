package waytech.modelo.beans.sgi;

/**
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @version 1.4 Jueves   25/11/2011 10:47 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class PermisoTipoPersonaUpdate {

    private int idPermisoTipoPersona;
    private int idTipoPersona;
    private int idPermiso;

    public PermisoTipoPersonaUpdate() {
    }

    public int getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso = idPermiso;
    }

    public int getIdPermisoTipoPersona() {
        return idPermisoTipoPersona;
    }

    public void setIdPermisoTipoPersona(int idPermisoTipoPersona) {
        this.idPermisoTipoPersona = idPermisoTipoPersona;
    }

    public int getIdTipoPersona() {
        return idTipoPersona;
    }

    public void setIdTipoPersona(int idTipoPersona) {
        this.idTipoPersona = idTipoPersona;
    }
}
