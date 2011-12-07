
package waytech.modelo.beans.sgi;

/**
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @version 1.4 Jueves   25/11/2011 10:47 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class DiscipuloUpdate {

    private String fechaInicio;
    private String traza;
    private int idPersona2;
    private int idPersona1;
    private int idDiscipulo;

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getIdPersona1() {
        return idPersona1;
    }

    public void setIdPersona1(int idPersona1) {
        this.idPersona1 = idPersona1;
    }

    public int getIdPersona2() {
        return idPersona2;
    }

    public void setIdPersona2(int idPersona2) {
        this.idPersona2 = idPersona2;
    }

    public String getTraza() {
        return traza;
    }

    public void setTraza(String traza) {
        this.traza = traza;
    }

    public int getIdDiscipulo() {
        return idDiscipulo;
    }

    public void setIdDiscipulo(int idDiscipulo) {
        this.idDiscipulo = idDiscipulo;
    }
}
