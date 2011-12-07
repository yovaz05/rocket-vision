
package waytech.modelo.beans.sgi;

/**
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @version 1.4 Jueves   25/11/2011 10:47 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class PersonaEnCelula {

    private int idPersonaEnCelula;
    private boolean esLiderCelula;
    private String traza;
    private short estado;
    private Persona idPersona;
    private Celula idCelula;

    public PersonaEnCelula() {
    }

    public boolean esLiderCelula() {
        return esLiderCelula;
    }

    public void setEsLiderCelula(boolean esLiderCelula) {
        this.esLiderCelula = esLiderCelula;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public Celula getIdCelula() {
        return idCelula;
    }

    public void setIdCelula(Celula idCelula) {
        this.idCelula = idCelula;
    }

    public Persona getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Persona idPersona) {
        this.idPersona = idPersona;
    }

    public int getIdPersonaEnCelula() {
        return idPersonaEnCelula;
    }

    public void setIdPersonaEnCelula(int idPersonaEnCelula) {
        this.idPersonaEnCelula = idPersonaEnCelula;
    }

    public String getTraza() {
        return traza;
    }

    public void setTraza(String traza) {
        this.traza = traza;
    }
}
