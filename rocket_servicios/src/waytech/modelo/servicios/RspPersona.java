package waytech.modelo.servicios;

import java.util.List;
import waytech.modelo.beans.sgi.Persona;

/**
 * Respuesta Persona
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class RspPersona {

    private List<Persona> todasLasPersonas;
    private boolean esSentenciaSqlEjecutadaExitosamente;
    private boolean esConexionAbiertaExitosamente;
    private boolean esConexionCerradaExitosamente;
    private boolean esRolledBackIntentado;
    private boolean esRolledBackExitosamente;
    private boolean esCedulaExistente;
    private String respuestaInicioDeConexion;
    private String respuestaCierreDeConexion;
    private String respuestaServicio;
    private String respuestaRolledBack;
    private Persona persona;

    public boolean esCedulaExistente() {
        return esCedulaExistente;
    }

    public void setEsCedulaExistente(boolean esCedulaExistente) {
        this.esCedulaExistente = esCedulaExistente;
    }

    public List<Persona> getTodasLasPersonas() {
        return todasLasPersonas;
    }

    public void setTodasLasPersonas(List<Persona> todasLasPersonas) {
        this.todasLasPersonas = todasLasPersonas;
    }

    public String getRespuestaRolledBack() {
        return respuestaRolledBack;
    }

    public void setRespuestaRolledBack(String respuestaRolledBack) {
        this.respuestaRolledBack = respuestaRolledBack;
    }

    public boolean esRolledBackIntentado() {
        return esRolledBackIntentado;
    }

    public void setEsRolledBackIntentado(boolean esRolledBackIntentado) {
        this.esRolledBackIntentado = esRolledBackIntentado;
    }

    public boolean esRolledBackExitosamente() {
        return esRolledBackExitosamente;
    }

    public void setEsRolledBackExitosamente(boolean esRolledBackExitosamente) {
        this.esRolledBackExitosamente = esRolledBackExitosamente;
    }

    public Persona getPersona() {
        return persona;
    }

    void setPersona(Persona usuario) {
        this.persona = usuario;
    }

    public String getRespuestaServicio() {
        return respuestaServicio;
    }

    void setRespuestaServicio(String respuestaServicio) {
        this.respuestaServicio = respuestaServicio;
    }

    public boolean esConexionAbiertaExitosamente() {
        return esConexionAbiertaExitosamente;
    }

    void setEsConexionAbiertaExitosamente(boolean esConexionAbiertaExitosamente) {
        this.esConexionAbiertaExitosamente = esConexionAbiertaExitosamente;
    }

    public boolean esConexionCerradaExitosamente() {
        return esConexionCerradaExitosamente;
    }

    void setEsConexionCerradaExitosamente(boolean esConexionCerradaExitosamente) {
        this.esConexionCerradaExitosamente = esConexionCerradaExitosamente;
    }

    boolean esSentenciaSqlEjecutadaExitosamente() {
        return esSentenciaSqlEjecutadaExitosamente;
    }

    void setEsSentenciaSqlEjecutadaExitosamente(boolean esSentenciaSqlEjecutadaExitosamente) {
        this.esSentenciaSqlEjecutadaExitosamente = esSentenciaSqlEjecutadaExitosamente;
    }

    public String getRespuestaCierreDeConexion() {
        return respuestaCierreDeConexion;
    }

    void setRespuestaCierreDeConexion(String respuestaCierreDeConexion) {
        this.respuestaCierreDeConexion = respuestaCierreDeConexion;
    }

    public String getRespuestaInicioDeConexion() {
        return respuestaInicioDeConexion;
    }

    void setRespuestaInicioDeConexion(String respuestaInicioDeConexion) {
        this.respuestaInicioDeConexion = respuestaInicioDeConexion;
    }   
}
