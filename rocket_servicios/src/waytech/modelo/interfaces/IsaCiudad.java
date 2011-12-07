package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.CiudadInsert;
import waytech.modelo.beans.sgi.CiudadUpdate;
import waytech.modelo.servicios.RspCiudad;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaCiudad {

    public abstract RspCiudad insertCiudad(CiudadInsert ciudad);

    public abstract RspCiudad updateCiudad(CiudadUpdate ciudad);

    /**
     * Elimina un registro lógicamente
     * @param idCiudad
     * @return 
     */
    public abstract RspCiudad deleteCiudad(int idCiudad);

    public abstract RspCiudad esNombreCiudadExistente(String nombre);

    public abstract RspCiudad getCiudadPorIdCiudad(int idCiudad);

    public abstract RspCiudad getCiudadPorTraza(String traza);

    public abstract RspCiudad listCiudad();

    public abstract RspCiudad listCiudadDondeHayCelulas();

    public abstract RspCiudad listCiudadDondeHayPersonas();

    public abstract RspCiudad listCiudadPorIdEstado(int idEstado);
}
