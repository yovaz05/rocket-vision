package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.ParejaInsert;
import waytech.modelo.beans.sgi.ParejaUpdate;
import waytech.modelo.servicios.RspPareja;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes    21/11/2011 06:49 AM.
 * @version 1.1 Viernes  25/11/2011 08:58 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaPareja {

    public abstract RspPareja insertPareja(ParejaInsert pareja);

    public abstract RspPareja updatePareja(ParejaUpdate pareja);
    /**
     * Elimina un registro lógicamente
     * @param idPareja
     * @return 
     */
    public abstract RspPareja deletePareja(int idPareja);

    public abstract RspPareja getParejaPorIdPareja(int idPareja);

    public abstract RspPareja getParejaPorTraza(String traza);

    public abstract RspPareja listPareja();
}
