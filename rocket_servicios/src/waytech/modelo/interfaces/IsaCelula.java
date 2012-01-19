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

    public abstract RspCelula updateIdRedCelula(int idCelula, int idRed);

    public abstract RspCelula updateCodigoCelula(int idCelula, String codigo);

    public abstract RspCelula updateNombreCelula(int idCelula, String nombre);

    public abstract RspCelula updateAnfitrionCelula(int idCelula, String anfitrion);

    public abstract RspCelula updateDireccionCelula(int idCelula, String direccion);

    public abstract RspCelula updateDiaCelula(int idCelula, int dia);

    public abstract RspCelula updateHoraCelula(int idCelula, int hora);

    public abstract RspCelula updateTelefonoCelula(int idCelula, String telefono);

    public abstract RspCelula updateObservacionesCelula(int idCelula, String observaciones);

    public abstract RspCelula updateIdZonaCelula(int idCelula, int idZona);

    public abstract RspCelula updateFechaAperturaCelula(int idCelula, String fechaApertura);
    
    /**
     * 1,2,3,4
     * @param idCelula
     * @param estado
     * @return 
     */
    public abstract RspCelula updateEstadoCelula(int idCelula, int estado);

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
    
    public abstract RspCelula esNombreCelulaExistente(String nombre);

    public abstract RspCelula esCelulaConIntegrantes(int idCelula);

    public abstract RspCelula listCelulaActiva();

    public abstract RspCelula listCelulaActivaOrdenEstatus();
    
    public abstract RspCelula listCelulaActivaOrdenEstatusPorRed(int idRed);

    public abstract RspCelula listCelulaActivaOrdenEstatusPorLider(int idLider);
    
    /**
     * 1,2,3,4
     * @param estado
     * @return 
     */
    public abstract RspCelula listCelulaPorEstado(int estado);

    public abstract RspCelula listCelulaPorRed(int idRed);    
    
}
