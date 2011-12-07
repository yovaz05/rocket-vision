package waytech.modelo.servicios;

import java.util.List;
import waytech.modelo.beans.sgi.Celula;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class RspCelula {

    private boolean esSentenciaSqlEjecutadaExitosamente;
    private boolean esConexionAbiertaExitosamente;
    private boolean esConexionCerradaExitosamente;
    private boolean esCodigoCelulaExistente;
    private boolean esCelulaConIntegrantes;
    private boolean esRolledBackIntentado;
    private boolean esRolledBackExitosamente;
    private List<Celula> todosLosCelulas;
    private String respuestaRolledBack;
    private String respuestaInicioDeConexion;
    private String respuestaCierreDeConexion;
    private String respuestaServicio;
    private Celula celula;
    private int numeroDeLideresCelula;
    private int numeroDeLideresLanzados;

    public Celula getCelula() {
        return celula;
    }

    public boolean esCelulaConIntegrantes() {
        return esCelulaConIntegrantes;
    }

    public void setEsCelulaConIntegrantes(boolean esCelulaConIntegrantes) {
        this.esCelulaConIntegrantes = esCelulaConIntegrantes;
    }

    public boolean esCodigoCelulaExistente() {
        return esCodigoCelulaExistente;
    }

    public void setEsCodigoCelulaExistente(boolean esCodigoCelulaExistente) {
        this.esCodigoCelulaExistente = esCodigoCelulaExistente;
    }

    public int getNumeroDeLideresCelula() {
        return numeroDeLideresCelula;
    }

    public void setNumeroDeLideresCelula(int numeroDeLideresCelula) {
        this.numeroDeLideresCelula = numeroDeLideresCelula;
    }

    public int getNumeroDeLideresLanzados() {
        return numeroDeLideresLanzados;
    }

    public void setNumeroDeLideresLanzados(int numeroDeLideresLanzados) {
        this.numeroDeLideresLanzados = numeroDeLideresLanzados;
    }

    public void setCelula(Celula celula) {
        this.celula = celula;
    }

    public boolean esConexionAbiertaExitosamente() {
        return esConexionAbiertaExitosamente;
    }

    public void setEsConexionAbiertaExitosamente(boolean esConexionAbiertaExitosamente) {
        this.esConexionAbiertaExitosamente = esConexionAbiertaExitosamente;
    }

    public boolean esConexionCerradaExitosamente() {
        return esConexionCerradaExitosamente;
    }

    public void setEsConexionCerradaExitosamente(boolean esConexionCerradaExitosamente) {
        this.esConexionCerradaExitosamente = esConexionCerradaExitosamente;
    }

    public boolean esRolledBackExitosamente() {
        return esRolledBackExitosamente;
    }

    public void setEsRolledBackExitosamente(boolean esRolledBackExitosamente) {
        this.esRolledBackExitosamente = esRolledBackExitosamente;
    }

    public boolean esRolledBackIntentado() {
        return esRolledBackIntentado;
    }

    public void setEsRolledBackIntentado(boolean esRolledBackIntentado) {
        this.esRolledBackIntentado = esRolledBackIntentado;
    }

    public boolean esSentenciaSqlEjecutadaExitosamente() {
        return esSentenciaSqlEjecutadaExitosamente;
    }

    public void setEsSentenciaSqlEjecutadaExitosamente(boolean esSentenciaSqlEjecutadaExitosamente) {
        this.esSentenciaSqlEjecutadaExitosamente = esSentenciaSqlEjecutadaExitosamente;
    }

    public String getRespuestaCierreDeConexion() {
        return respuestaCierreDeConexion;
    }

    public void setRespuestaCierreDeConexion(String respuestaCierreDeConexion) {
        this.respuestaCierreDeConexion = respuestaCierreDeConexion;
    }

    public String getRespuestaInicioDeConexion() {
        return respuestaInicioDeConexion;
    }

    public void setRespuestaInicioDeConexion(String respuestaInicioDeConexion) {
        this.respuestaInicioDeConexion = respuestaInicioDeConexion;
    }

    public String getRespuestaRolledBack() {
        return respuestaRolledBack;
    }

    public void setRespuestaRolledBack(String respuestaRolledBack) {
        this.respuestaRolledBack = respuestaRolledBack;
    }

    public String getRespuestaServicio() {
        return respuestaServicio;
    }

    public void setRespuestaServicio(String respuestaServicio) {
        this.respuestaServicio = respuestaServicio;
    }

    public List<Celula> getTodosLosCelulas() {
        return todosLosCelulas;
    }

    public void setTodosLosCelulas(List<Celula> todosLosCelulas) {
        this.todosLosCelulas = todosLosCelulas;
    }
}
