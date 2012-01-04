package waytech.modelo.beans.sgi;

/**
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @version 1.4 Jueves   25/11/2011 10:47 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class PlanificacionCelula {

    private int idPlanificacionCelula = 0;
    private String fecha = "";
    private int nuevosInvitados = 0;
    private int reconciliados = 0;
    private int visitas = 0;
    private int numeroIntegrantes = 0;
    private int convertidos = 0;
    private String observaciones = "";
    private String traza = "";
    private short estado = 0;
    private Semana idSemana = new Semana();
    private Celula idCelula = new Celula();

    public PlanificacionCelula() {
    }

    public int getConvertidos() {
        return convertidos;
    }

    public void setConvertidos(int convertidos) {
        this.convertidos = convertidos;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Celula getIdCelula() {
        return idCelula;
    }

    public void setIdCelula(Celula idCelula) {
        this.idCelula = idCelula;
    }

    public int getIdPlanificacionCelula() {
        return idPlanificacionCelula;
    }

    public void setIdPlanificacionCelula(int idPlanificacionCelula) {
        this.idPlanificacionCelula = idPlanificacionCelula;
    }

    public Semana getIdSemana() {
        return idSemana;
    }

    public void setIdSemana(Semana idSemana) {
        this.idSemana = idSemana;
    }

    public int getNuevosInvitados() {
        return nuevosInvitados;
    }

    public void setNuevosInvitados(int nuevosInvitados) {
        this.nuevosInvitados = nuevosInvitados;
    }

    public int getNumeroIntegrantes() {
        return numeroIntegrantes;
    }

    public void setNumeroIntegrantes(int numeroIntegrantes) {
        this.numeroIntegrantes = numeroIntegrantes;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getReconciliados() {
        return reconciliados;
    }

    public void setReconciliados(int reconciliados) {
        this.reconciliados = reconciliados;
    }

    public String getTraza() {
        return traza;
    }

    public void setTraza(String traza) {
        this.traza = traza;
    }

    public int getVisitas() {
        return visitas;
    }

    public void setVisitas(int visitas) {
        this.visitas = visitas;
    }
}
