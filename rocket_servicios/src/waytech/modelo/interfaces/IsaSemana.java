package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.SemanaInsert;
import waytech.modelo.beans.sgi.SemanaUpdate;
import waytech.modelo.servicios.RspSemana;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaSemana {

    public abstract RspSemana insertSemana(SemanaInsert semana);

    public abstract RspSemana updateSemana(SemanaUpdate semana);

    /**
     * Elimina un registro lógicamente
     * @param idSemana
     * @return 
     */
    public abstract RspSemana deleteSemana(int idSemana);

    public abstract RspSemana getSemanaPorIdSemana(int idSemana);

    public abstract RspSemana listSemana();
}
