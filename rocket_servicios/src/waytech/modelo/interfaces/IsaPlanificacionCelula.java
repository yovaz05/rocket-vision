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

    public abstract RspPlanificacionCelula insertPlanificacionCelulaDiaSemana(int idCelula);

    public abstract RspPlanificacionCelula updatePlanificacionCelula(PlanificacionCelulaUpdate planificacionCelula);

    public abstract RspPlanificacionCelula updateIdCelula(int idPlanificacionCelula, int idCelula);

    public abstract RspPlanificacionCelula updateIdSemana(int idPlanificacionCelula, int idSemana);

    public abstract RspPlanificacionCelula updateFecha(int idPlanificacionCelula, String fecha);

    public abstract RspPlanificacionCelula updateNuevosInvitados(int idPlanificacionCelula, int nuevosInvitados);

    public abstract RspPlanificacionCelula updateReconciliados(int idPlanificacionCelula, int reconciliados);

    public abstract RspPlanificacionCelula updateVisitas(int idPlanificacionCelula, int visitas);

    public abstract RspPlanificacionCelula updateNumeroIntegrantes(int idPlanificacionCelula, int numeroIntegrantes);

    public abstract RspPlanificacionCelula updateConvertidos(int idPlanificacionCelula, int convertidos);

    public abstract RspPlanificacionCelula updateObservaciones(int idPlanificacionCelula, String observaciones);

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
