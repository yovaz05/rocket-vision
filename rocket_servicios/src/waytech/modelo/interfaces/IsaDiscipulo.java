package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.DiscipuloInsert;
import waytech.modelo.beans.sgi.DiscipuloUpdate;
import waytech.modelo.servicios.RspDiscipulo;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaDiscipulo {

    public abstract RspDiscipulo insertDiscipulo(DiscipuloInsert discipulo);

    public abstract RspDiscipulo updateDiscipulo(DiscipuloUpdate discipulo);

    /**
     * Elimina un registro lógicamente
     * @param idDiscipulo
     * @return 
     */
    public abstract RspDiscipulo deleteDiscipulo(int idDiscipulo);

    public abstract RspDiscipulo getDiscipuloPorIdDiscipulo(int idDiscipulo);

    public abstract RspDiscipulo getDiscipuloPorTraza(String traza);
    
    public abstract RspDiscipulo esPersonaConLider(int idPersona);

    public abstract RspDiscipulo listDiscipulo();
    
    public abstract RspDiscipulo listDiscipulo(int idLider);
}
