package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.EjecucionCelulaInsert;
import waytech.modelo.beans.sgi.EjecucionCelulaUpdate;
import waytech.modelo.servicios.RspEjecucionCelula;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaEjecucionCelula {

    public abstract RspEjecucionCelula insertEjecucionCelula(EjecucionCelulaInsert ejecucionCelula);

    /**
     * Ingresar reporte celula (idCelula). el servicio debe determinar cuál es la semana actual del sistema.
     * Por ahora solo inserta con la semana 1
     * @return 
     */
    public abstract RspEjecucionCelula insertEjecucionCelulaDiaSemana(int idCelula);

    public abstract RspEjecucionCelula updateEjecucionCelula(EjecucionCelulaUpdate ejecucionCelula);

    public abstract RspEjecucionCelula updateIdCelula(int idEjecucionCelula, int idCelula);

    public abstract RspEjecucionCelula updateIdSemana(int idEjecucionCelula, int idSemana);

    public abstract RspEjecucionCelula updateNumeroInvitados(int idEjecucionCelula, int nuevosInvitados);

    public abstract RspEjecucionCelula updateReconciliados(int idEjecucionCelula, int reconciliados);

    public abstract RspEjecucionCelula updateVisitas(int idEjecucionCelula, int visitas);

    public abstract RspEjecucionCelula updateNumeroIntegrantes(int idEjecucionCelula, int numeroIntegrantes);

    public abstract RspEjecucionCelula updateConvertidos(int idEjecucionCelula, int convertidos);

    public abstract RspEjecucionCelula updateObservaciones(int idEjecucionCelula, String observaciones);

    public abstract RspEjecucionCelula updateAmigosSoloAsistenGrupos(int idEjecucionCelula, int amigosSoloAsistenGrupo);

    public abstract RspEjecucionCelula updateIntegrantesCasaOracion(int idEjecucionCelula, int integrantesCasaOracion);

    public abstract RspEjecucionCelula updateIntegrantesOtrasIglesias(int idEjecucionCelula, int integrantesOtrasIglesias);

    public abstract RspEjecucionCelula updateAsistenciaDomingoAnterior(int idEjecucionCelula, int asistenciaDomingoAnterior);

    public abstract RspEjecucionCelula updateOfrenda(int idEjecucionCelula, Double ofrenda);

    /**
     * Elimina un registro lógicamente
     * @param idEjecucionCelula
     * @return 
     */
    public abstract RspEjecucionCelula deleteEjecucionCelula(int idEjecucionCelula);

    public abstract RspEjecucionCelula getEjecucionCelulaPorIdEjecucionCelula(int idEjecucionCelula);

    public abstract RspEjecucionCelula getEjecucionCelulaPorTraza(String traza);

    public abstract RspEjecucionCelula listEjecucionCelula();
}
