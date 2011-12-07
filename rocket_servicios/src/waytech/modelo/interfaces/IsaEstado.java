package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.EstadoInsert;
import waytech.modelo.beans.sgi.EstadoUpdate;
import waytech.modelo.servicios.RspEstado;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaEstado {

    public abstract RspEstado insertEstado(EstadoInsert estado);

    public abstract RspEstado updateEstado(EstadoUpdate estado);

    /**
     * Elimina un registro lógicamente
     * @param idEstado
     * @return 
     */
    public abstract RspEstado deleteEstado(int idEstado);

    public abstract RspEstado getEstadoPorIdEstado(int idEstado);

    public abstract RspEstado getEstadoPorTraza(String traza);

    public abstract RspEstado listEstado();
}
