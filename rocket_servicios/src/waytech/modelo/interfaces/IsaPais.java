package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.Pais;
import waytech.modelo.beans.sgi.PaisInsert;
import waytech.modelo.beans.sgi.PaisUpdate;
import waytech.modelo.servicios.RspPais;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaPais {

    public abstract RspPais insertPais(PaisInsert pais);

    public abstract RspPais updatePais(PaisUpdate pais);

    /**
     * Elimina un registro lógicamente
     * @param idPais
     * @return 
     */
    public abstract RspPais deletePais(int idPais);

    public abstract RspPais getPaisPorIdPais(int idPais);

    public abstract RspPais getPaisPorTraza(String traza);

    public abstract RspPais listPais();
}
