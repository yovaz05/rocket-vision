package waytech.modelo.beans.sgi;

/**
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @version 1.4 Jueves   25/11/2011 10:47 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class PersonaEnCelulaInsert {

    private int idPersona;
    private int idCelula;
    private boolean esLiderCelula;

    public PersonaEnCelulaInsert() {
    }

    public boolean esLiderCelula() {
        return esLiderCelula;
    }

    public void setEsLiderCelula(boolean esLiderCelula) {
        this.esLiderCelula = esLiderCelula;
    }

    public int getIdCelula() {
        return idCelula;
    }

    public void setIdCelula(int idCelula) {
        this.idCelula = idCelula;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }
}
