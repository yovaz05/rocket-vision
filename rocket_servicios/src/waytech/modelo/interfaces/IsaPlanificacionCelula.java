package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.PlanificacionCelulaInsert;
import waytech.modelo.beans.sgi.PlanificacionCelulaUpdate;
import waytech.modelo.servicios.RspPlanificacionCelula;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaPlanificacionCelula {

    public abstract RspPlanificacionCelula insertPlanificacionCelula(PlanificacionCelulaInsert planificacionCelula);

    public abstract RspPlanificacionCelula updatePlanificacionCelula(PlanificacionCelulaUpdate planificacionCelula);

    /**
     * Elimina un registro lógicamente
     * @param idPlanificacionCelula
     * @return 
     */
    public abstract RspPlanificacionCelula deletePlanificacionCelulaLogicamente(int idPlanificacionCelula);

    public abstract RspPlanificacionCelula getPlanificacionCelulaPorIdPlanificacionCelula(int idPlanificacionCelula);

    public abstract RspPlanificacionCelula getPlanificacionCelulaPorTraza(String traza);

    public abstract RspPlanificacionCelula listPlanificacionCelula();
}
