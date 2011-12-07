package waytech.modelo.beans.sgi;

/**
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @version 1.4 Jueves   25/11/2011 10:47 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class EjecucionCelulaUpdate {

    private int idEjecucionCelula;
    private String fecha;
    private int nuevosInvitados;
    private int reconciliados;
    private int visitas;
    private int numeroIntegrantes;
    private int convertidos;
    private String observaciones;
    private int amigosSoloAsistenGrupo;
    private int integrantesCasaOracion;
    private int integrantesOtrasIglesias;
    private int asistenciaDomingoAnterior;
    private double ofrenda;
    private int idSemana;
    private int idCelula;

    public int getAmigosSoloAsistenGrupo() {
        return amigosSoloAsistenGrupo;
    }

    public void setAmigosSoloAsistenGrupo(int amigosSoloAsistenGrupo) {
        this.amigosSoloAsistenGrupo = amigosSoloAsistenGrupo;
    }

    public int getAsistenciaDomingoAnterior() {
        return asistenciaDomingoAnterior;
    }

    public void setAsistenciaDomingoAnterior(int asistenciaDomingoAnterior) {
        this.asistenciaDomingoAnterior = asistenciaDomingoAnterior;
    }

    public int getConvertidos() {
        return convertidos;
    }

    public void setConvertidos(int convertidos) {
        this.convertidos = convertidos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdCelula() {
        return idCelula;
    }

    public void setIdCelula(int idCelula) {
        this.idCelula = idCelula;
    }

    public int getIdEjecucionCelula() {
        return idEjecucionCelula;
    }

    public void setIdEjecucionCelula(int idEjecucionCelula) {
        this.idEjecucionCelula = idEjecucionCelula;
    }

    public int getIdSemana() {
        return idSemana;
    }

    public void setIdSemana(int idSemana) {
        this.idSemana = idSemana;
    }

    public int getIntegrantesCasaOracion() {
        return integrantesCasaOracion;
    }

    public void setIntegrantesCasaOracion(int integrantesCasaOracion) {
        this.integrantesCasaOracion = integrantesCasaOracion;
    }

    public int getIntegrantesOtrasIglesias() {
        return integrantesOtrasIglesias;
    }

    public void setIntegrantesOtrasIglesias(int integrantesOtrasIglesias) {
        this.integrantesOtrasIglesias = integrantesOtrasIglesias;
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

    public double getOfrenda() {
        return ofrenda;
    }

    public void setOfrenda(double ofrenda) {
        this.ofrenda = ofrenda;
    }

    public int getReconciliados() {
        return reconciliados;
    }

    public void setReconciliados(int reconciliados) {
        this.reconciliados = reconciliados;
    }

    public int getVisitas() {
        return visitas;
    }

    public void setVisitas(int visitas) {
        this.visitas = visitas;
    }
}
