package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.RedInsert;
import waytech.modelo.beans.sgi.RedUpdate;
import waytech.modelo.servicios.RspRed;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaRed {

    public abstract RspRed insertRed(RedInsert red);

    public abstract RspRed updateRed(RedUpdate red);

    /**
     * Elimina un registro lógicamente
     * @param idRed
     * @return 
     */
    public abstract RspRed deleteRed(int idRed);

    public abstract RspRed getRedPorIdRed(int idRed);

    public abstract RspRed getRedPorTraza(String traza);

    public abstract RspRed listRed();
}
