
package waytech.modelo.beans.sgi;

/**
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @version 1.4 Jueves   25/11/2011 10:47 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class PersonaInsert {

    private String ci;
    private String nombre;
    private String apellido;
    private String direccionHabitacion;
    private String direccionTrabajo;
    private String fechaNacimiento;
    private String estadoCivil;
    private String profesion;
    private String telefonoMovil;
    private String telefonoHabitacion;
    private String telefonoTrabajo;
    private String facebook;
    private String twitter;
    private String fechaConversion;
    private String fechaEncuentro;
    private String fechaGraduacionAcademia;
    private String fechaLanzamiento;
    private String fechaBautizo;
    private boolean esLiderRed;
    private boolean esLiderCelula;
    private boolean esSupervisor;
    private boolean esEstaca;
    private boolean esMaestroAcademia;
    private boolean esAnfitrion;
    private boolean esLiderLanzado;
    private boolean esLiderSupervisor;
    private boolean esDiscipuloEnProceso;
    private float porcentajeCompletadoPerfil;
    private int idTipoPersona;
    private int idZona;

    public PersonaInsert() {
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getDireccionHabitacion() {
        return direccionHabitacion;
    }

    public void setDireccionHabitacion(String direccionHabitacion) {
        this.direccionHabitacion = direccionHabitacion;
    }

    public String getDireccionTrabajo() {
        return direccionTrabajo;
    }

    public void setDireccionTrabajo(String direccionTrabajo) {
        this.direccionTrabajo = direccionTrabajo;
    }

    public boolean esAnfitrion() {
        return esAnfitrion;
    }

    public void setEsAnfitrion(boolean esAnfitrion) {
        this.esAnfitrion = esAnfitrion;
    }

    public boolean esDiscipuloEnProceso() {
        return esDiscipuloEnProceso;
    }

    public void setEsDiscipuloEnProceso(boolean esDiscipuloEnProceso) {
        this.esDiscipuloEnProceso = esDiscipuloEnProceso;
    }

    public boolean esEstaca() {
        return esEstaca;
    }

    public void setEsEstaca(boolean esEstaca) {
        this.esEstaca = esEstaca;
    }

    public boolean esLiderCelula() {
        return esLiderCelula;
    }

    public void setEsLiderCelula(boolean esLiderCelula) {
        this.esLiderCelula = esLiderCelula;
    }

    public boolean esLiderLanzado() {
        return esLiderLanzado;
    }

    public void setEsLiderLanzado(boolean esLiderLanzado) {
        this.esLiderLanzado = esLiderLanzado;
    }

    public boolean esLiderRed() {
        return esLiderRed;
    }

    public void setEsLiderRed(boolean esLiderRed) {
        this.esLiderRed = esLiderRed;
    }

    public boolean esLiderSupervisor() {
        return esLiderSupervisor;
    }

    public void setEsLiderSupervisor(boolean esLiderSupervisor) {
        this.esLiderSupervisor = esLiderSupervisor;
    }

    public boolean esMaestroAcademia() {
        return esMaestroAcademia;
    }

    public void setEsMaestroAcademia(boolean esMaestroAcademia) {
        this.esMaestroAcademia = esMaestroAcademia;
    }

    public boolean esSupervisor() {
        return esSupervisor;
    }

    public void setEsSupervisor(boolean esSupervisor) {
        this.esSupervisor = esSupervisor;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFechaBautizo() {
        return fechaBautizo;
    }

    public void setFechaBautizo(String fechaBautizo) {
        this.fechaBautizo = fechaBautizo;
    }

    public String getFechaConversion() {
        return fechaConversion;
    }

    public void setFechaConversion(String fechaConversion) {
        this.fechaConversion = fechaConversion;
    }

    public String getFechaEncuentro() {
        return fechaEncuentro;
    }

    public void setFechaEncuentro(String fechaEncuentro) {
        this.fechaEncuentro = fechaEncuentro;
    }

    public String getFechaGraduacionAcademia() {
        return fechaGraduacionAcademia;
    }

    public void setFechaGraduacionAcademia(String fechaGraduacionAcademia) {
        this.fechaGraduacionAcademia = fechaGraduacionAcademia;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(String fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getIdTipoPersona() {
        return idTipoPersona;
    }

    public void setIdTipoPersona(int idTipoPersona) {
        this.idTipoPersona = idTipoPersona;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPorcentajeCompletadoPerfil() {
        return porcentajeCompletadoPerfil;
    }

    public void setPorcentajeCompletadoPerfil(float porcentajeCompletadoPerfil) {
        this.porcentajeCompletadoPerfil = porcentajeCompletadoPerfil;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getTelefonoHabitacion() {
        return telefonoHabitacion;
    }

    public void setTelefonoHabitacion(String telefonoHabitacion) {
        this.telefonoHabitacion = telefonoHabitacion;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getTelefonoTrabajo() {
        return telefonoTrabajo;
    }

    public void setTelefonoTrabajo(String telefonoTrabajo) {
        this.telefonoTrabajo = telefonoTrabajo;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
