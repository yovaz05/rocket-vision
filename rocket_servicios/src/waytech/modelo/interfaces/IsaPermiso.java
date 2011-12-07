package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.PermisoInsert;
import waytech.modelo.beans.sgi.PermisoUpdate;
import waytech.modelo.servicios.RspPermiso;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaPermiso {

    public abstract RspPermiso insertPermiso(PermisoInsert permiso);

    public abstract RspPermiso updatePermiso(PermisoUpdate permiso);

    /**
     * Elimina un registro lógicamente
     * @param idPermiso
     * @return 
     */
    public abstract RspPermiso deletePermiso(int idPermiso);

    public abstract RspPermiso getPermisoPorIdPermiso(int idPermiso);

    public abstract RspPermiso getPermisoPorTraza(String traza);

    public abstract RspPermiso listPermiso();
}
