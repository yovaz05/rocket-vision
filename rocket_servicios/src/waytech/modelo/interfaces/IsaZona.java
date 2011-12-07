package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.ZonaInsert;
import waytech.modelo.beans.sgi.ZonaUpdate;
import waytech.modelo.servicios.RspZona;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaZona {

    public abstract RspZona insertZona(ZonaInsert zona);

    public abstract RspZona updateZona(ZonaUpdate zona);

    /**
     * Elimina un registro lógicamente
     * @param idZona
     * @return 
     */
    public abstract RspZona deleteZona(int idZona);

    public abstract RspZona getZonaPorIdZona(int idZona);

    public abstract RspZona listZona();

    public abstract RspZona listZonaPorIdCiudad(int idCiudad);
}
