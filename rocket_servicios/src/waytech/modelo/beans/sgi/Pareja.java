
package waytech.modelo.beans.sgi;

/**
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @version 1.4 Jueves   25/11/2011 10:47 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class Pareja {

    private int idPareja;
    private String fechaInicio;
    private boolean esMatrimonio;
    private String traza;
    private short estado;
    private Persona idPersona2;
    private Persona idPersona1;

    public Pareja() {
    }

    public boolean esMatrimonio() {
        return esMatrimonio;
    }

    public void setEsMatrimonio(boolean esMatrimonio) {
        this.esMatrimonio = esMatrimonio;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getIdPareja() {
        return idPareja;
    }

    public void setIdPareja(int idPareja) {
        this.idPareja = idPareja;
    }

    public Persona getIdPersona1() {
        return idPersona1;
    }

    public void setIdPersona1(Persona idPersona1) {
        this.idPersona1 = idPersona1;
    }

    public Persona getIdPersona2() {
        return idPersona2;
    }

    public void setIdPersona2(Persona idPersona2) {
        this.idPersona2 = idPersona2;
    }

    public String getTraza() {
        return traza;
    }

    public void setTraza(String traza) {
        this.traza = traza;
    }
}
