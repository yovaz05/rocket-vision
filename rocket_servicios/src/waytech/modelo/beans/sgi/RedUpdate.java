package waytech.modelo.beans.sgi;

/**
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @version 1.4 Jueves   25/11/2011 10:47 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class RedUpdate {

    private String nombre;
    private int diaReunion;
    private int horaReunion;
    private int idRed;
    private boolean esMatrimonial;
    private String traza;

    public int getDiaReunion() {
        return diaReunion;
    }

    public void setDiaReunion(int diaReunion) {
        this.diaReunion = diaReunion;
    }

    public boolean esMatrimonial() {
        return esMatrimonial;
    }

    public void setEsMatrimonial(boolean esMatrimonial) {
        this.esMatrimonial = esMatrimonial;
    }

    public int getHoraReunion() {
        return horaReunion;
    }

    public void setHoraReunion(int horaReunion) {
        this.horaReunion = horaReunion;
    }

    public int getIdRed() {
        return idRed;
    }

    public void setIdRed(int idRed) {
        this.idRed = idRed;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTraza() {
        return traza;
    }

    public void setTraza(String traza) {
        this.traza = traza;
    }
}
