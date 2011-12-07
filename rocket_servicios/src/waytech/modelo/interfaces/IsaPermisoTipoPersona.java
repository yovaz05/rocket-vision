package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.PermisoTipoPersonaInsert;
import waytech.modelo.beans.sgi.PermisoTipoPersonaUpdate;
import waytech.modelo.servicios.RspPermisoTipoPersona;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaPermisoTipoPersona {

    public abstract RspPermisoTipoPersona insertPermisoTipoPersona(PermisoTipoPersonaInsert permisoTipoPersona);

    public abstract RspPermisoTipoPersona updatePermisoTipoPersona(PermisoTipoPersonaUpdate permisoTipoPersona);

    /**
     * Elimina un registro lógicamente
     * @param idPermisoTipoPersona
     * @return 
     */
    public abstract RspPermisoTipoPersona deletePermisoTipoPersona(int idPermisoTipoPersona);

    public abstract RspPermisoTipoPersona getPermisoTipoPersonaPorIdPermisoTipoPersona(int idPermisoTipoPersona);

    public abstract RspPermisoTipoPersona getPermisoTipoPersonaPorTraza(String traza);

    public abstract RspPermisoTipoPersona listPermisoTipoPersona();
}
