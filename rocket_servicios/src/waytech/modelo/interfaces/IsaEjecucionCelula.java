package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.EjecucionCelulaInsert;
import waytech.modelo.beans.sgi.EjecucionCelulaUpdate;
import waytech.modelo.servicios.RspEjecucionCelula;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaEjecucionCelula {

    public abstract RspEjecucionCelula insertEjecucionCelula(EjecucionCelulaInsert ejecucionCelula);

    public abstract RspEjecucionCelula updateEjecucionCelula(EjecucionCelulaUpdate ejecucionCelula);

    /**
     * Elimina un registro lógicamente
     * @param idEjecucionCelula
     * @return 
     */
    public abstract RspEjecucionCelula deleteEjecucionCelula(int idEjecucionCelula);

    public abstract RspEjecucionCelula getEjecucionCelulaPorIdEjecucionCelula(int idEjecucionCelula);

    public abstract RspEjecucionCelula getEjecucionCelulaPorTraza(String traza);

    public abstract RspEjecucionCelula listEjecucionCelula();
}
