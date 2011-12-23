/**
 * bean LiderUtil,
 * representa un discípulo lanzado,
 * es la base para un líder de cualquier tipo: LiderRed, LiderCelula, DiscipuloLanzado, SupervisorCelula
 */
package cdo.sgd.modelo.bd.util;

/**
 *
 * @author Gabriel Pérez
 */
public class LiderUtil extends Discipulo {

  //pareja:
  protected int idParejaMinisterial = 0;
  protected String nombreParejaMinisterial = "No tiene";
  //roles:
  protected boolean liderRed = false;
  protected boolean supervisor = false;
  protected boolean liderCelula = false;
  protected boolean maestroAcademia = false;

  public LiderUtil(String nombre, String nombreRed, String direccionCorta, String nombreLider1, String nombreLider2, String telefono, String email, int edad) {
    super(nombre, nombreRed, direccionCorta, nombreLider1, nombreLider2, telefono, email, edad);
  }

  public LiderUtil(String nombre, String direccionCorta, String nombreLider1, String nombreLider2, String telefono, String email, int edad) {
    super(nombre, direccionCorta, nombreLider1, nombreLider2, telefono, email, edad);
  }

  public LiderUtil(String nombre, String direccionCorta, String telefono, String email, int edad) {
    super(nombre, direccionCorta, telefono, email, edad);
  }

  public LiderUtil(String nombre, String telefono, String email, int edad) {
    super(nombre, telefono, email, edad);
  }

  public LiderUtil() {
    super();
  }

  public LiderUtil(int id) {
    super(id);
  }

  public LiderUtil(String nombre) {
    super(nombre);
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("Lider{"
            + "id: " + this.getId()
            + ", cedula: " + this.getCedula()
            + ", nombre: " + this.getNombre()
            + ", red: " + this.getNombreRed()
            + ", direccion: " + this.getDireccionCorta()
            + ", lider1: " + this.getNombreLider1()
            + ", lider2: " + this.getNombreLider2()
            + ", telefono: " + this.getTelefono()
            + ", email: " + this.getEmail()
            + ", fechanac: " + this.getFechaNacimiento()
            + ", edad: " + this.getEdad()
            + ", estadocivil: " + this.getEstadoCivil()
            + ", profesion: " + this.getProfesionOficio()
            + ", pareja.id: " + this.getIdParejaMinisterial()
            + ", pareja.nombre: " + this.getNombreParejaMinisterial()
            + " }");
    return sb.toString();
  }

  String lider2() {
    String lider2 = this.getNombreLider2();
    return lider2.equals("") ? "" : (", lider2: " + lider2);
  }

  public void setLiderCelula(boolean liderCelula) {
    this.liderCelula = liderCelula;
  }

  public void setLiderRed(boolean liderRed) {
    this.liderRed = liderRed;
  }

  public void setMaestroAcademia(boolean maestroAcademia) {
    this.maestroAcademia = maestroAcademia;
  }

  public void setSupervisor(boolean supervisorCelula) {
    this.supervisor = supervisorCelula;
  }

  public boolean isLiderCelula() {
    return liderCelula;
  }

  public boolean isLiderRed() {
    return liderRed;
  }

  public boolean isMaestroAcademia() {
    return maestroAcademia;
  }

  public boolean isSupervisor() {
    return supervisor;
  }

  public int getIdParejaMinisterial() {
    return idParejaMinisterial;
  }

  public void setIdParejaMinisterial(int idParejaMinisterial) {
    this.idParejaMinisterial = idParejaMinisterial;
  }

  public String getNombreParejaMinisterial() {
    return nombreParejaMinisterial;
  }

  public void setNombreParejaMinisterial(String nombreParejaMinisterial) {
    this.nombreParejaMinisterial = nombreParejaMinisterial;
  }
}
