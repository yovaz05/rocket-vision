package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.PersonaEnRed;
import waytech.modelo.beans.sgi.PersonaEnRedInsert;
import waytech.modelo.servicios.RspPersonaEnRed;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaPersonaEnRed {

    public abstract RspPersonaEnRed insertPersonaEnRed(PersonaEnRedInsert personaEnRed);

    public abstract RspPersonaEnRed updatePersonaEnRed(PersonaEnRed personaEnRed);

    /**
     * Elimina un registro lógicamente
     * @param idPersonaEnRed
     * @return 
     */
    public abstract RspPersonaEnRed deletePersonaEnRed(int idPersonaEnRed);

    public abstract RspPersonaEnRed getPersonaEnRedPorIdPersonaEnRed(int idPersonaEnRed);

    public abstract RspPersonaEnRed getPersonaEnRedPorTraza(String traza);

    public abstract RspPersonaEnRed listPersonaEnRed();

    public abstract RspPersonaEnRed listLideresLanzados(int idRed);
}
