package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.TipoPersona;
import waytech.modelo.servicios.RspTipoPersona;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaTipoPersona {

    public abstract RspTipoPersona obtenerTipoPersonaPorIdTipoPersona(int idTipoPersona);

    public abstract RspTipoPersona obtenerTodosLosTipoPersonas();

    public abstract RspTipoPersona modificarTipoPersona(TipoPersona tipoPersona);

    public abstract RspTipoPersona ingresarTipoPersona(TipoPersona tipoPersona);

    /**
     * Elimina un registro lógicamente
     * @param idTipoPersona
     * @return 
     */
    public abstract RspTipoPersona eliminarTipoPersonaLogicamente(int idTipoPersona);
}
