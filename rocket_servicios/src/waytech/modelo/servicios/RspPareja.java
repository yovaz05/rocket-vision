package waytech.modelo.servicios;

import java.util.List;
import waytech.modelo.beans.sgi.Pareja;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class RspPareja {

    private boolean esSentenciaSqlEjecutadaExitosamente;
    private boolean esConexionAbiertaExitosamente;
    private boolean esConexionCerradaExitosamente;
    private boolean esRolledBackIntentado;
    private boolean esRolledBackExitosamente;
    private boolean tienePareja;
    private List<Pareja> allParejas;
    private String respuestaRolledBack;
    private String respuestaInicioDeConexion;
    private String respuestaCierreDeConexion;
    private String respuestaServicio;
    private Pareja pareja;

    public boolean tienePareja() {
        return tienePareja;
    }

    public void setTienePareja(boolean tienePareja) {
        this.tienePareja = tienePareja;
    }  
    
    public List<Pareja> getAllParejas() {
        return allParejas;
    }

    public void setAllParejas(List<Pareja> allParejas) {
        this.allParejas = allParejas;
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

    public Pareja getPareja() {
        return pareja;
    }

    public void setPareja(Pareja pareja) {
        this.pareja = pareja;
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
}
