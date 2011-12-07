package waytech.modelo.servicios;
import java.util.List;
import waytech.modelo.beans.sgi.Semana;

/**
 * Respuesta Semana
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class RspSemana {
    
    private List<Semana> allSemana;    
    private boolean esSentenciaSqlEjecutadaExitosamente;
    private boolean esConexionAbiertaExitosamente;
    private boolean esConexionCerradaExitosamente;
    private boolean esRolledBackIntentado;
    private boolean esRolledBackExitosamente;
    private String respuestaInicioDeConexion;
    private String respuestaCierreDeConexion;
    private String respuestaServicio;
    private String respuestaRolledBack;
    private Semana semana;

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
            
    public Semana getSemana() {
        return semana;
    }

    void setSemana(Semana usuario) {
        this.semana = usuario;
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

    public List<Semana> getAllSemana() {
        return allSemana;
    }

    public void setAllSemana(List<Semana> allSemana) {
        this.allSemana = allSemana;
    }           
}
