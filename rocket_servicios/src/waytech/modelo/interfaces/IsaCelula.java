package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.CelulaInsert;
import waytech.modelo.beans.sgi.CelulaUpdate;
import waytech.modelo.servicios.RspCelula;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaCelula {

    public abstract RspCelula insertCelula(CelulaInsert celulaInsert);

    public abstract RspCelula updateCelula(CelulaUpdate celulaModificar);

    /**
     * Elimina un registro lógicamente
     * @param idCelula
     * @return 
     */
    public abstract RspCelula deleteCelulaLogicamente(int idCelula);

    public abstract RspCelula getCelulaPorIdCelula(int idCelula);

    public abstract RspCelula getCelulaTraza(String traza);

    public abstract RspCelula getNumeroLideresCelula(int idCelula);

    public abstract RspCelula esCodigoCelulaExistente(String codigo);

    public abstract RspCelula esCelulaConIntegrantes(int idCelula);

    public abstract RspCelula listCelula();
}
