package waytech.modelo.beans.sgi;

/**
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @version 1.4 Jueves   25/11/2011 10:47 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class PersonaEnRed {

    private int idPersonaEnRed;
    private boolean esLiderRed;
    private String traza;
    private short estado;
    private Red idRed;
    private Persona idPersona;

    public PersonaEnRed() {
    }

    public boolean esLiderRed() {
        return esLiderRed;
    }

    public void setEsLiderRed(boolean esLiderRed) {
        this.esLiderRed = esLiderRed;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public Persona getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Persona idPersona) {
        this.idPersona = idPersona;
    }

    public int getIdPersonaEnRed() {
        return idPersonaEnRed;
    }

    public void setIdPersonaEnRed(int idPersonaEnRed) {
        this.idPersonaEnRed = idPersonaEnRed;
    }

    public Red getIdRed() {
        return idRed;
    }

    public void setIdRed(Red idRed) {
        this.idRed = idRed;
    }

    public String getTraza() {
        return traza;
    }

    public void setTraza(String traza) {
        this.traza = traza;
    }
}
