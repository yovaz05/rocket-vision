/**
 * DiscipuloEnProceso, bean que encapsula la data un discípulo en proceso
 */
package cdo.sgd.modelo.bd.util;

/**
 *
 * @author Gabriel Pérez
 */
public class DiscipuloProceso extends Discipulo {

  boolean tieneCelula = false;
  String estatusProceso = "";
  int idCelula = 0;
  String descripcionCelula = "";

  public DiscipuloProceso() {
    super();
  }

  /**
   * constructor con datos para resumen
   * @param nombre
   * @param nombreRed
   * @param direccionCorta
   * @param nombreLider1
   * @param nombreLider2
   * @param telefono
   * @param email
   * @param edad 
   */
  public DiscipuloProceso(String nombre, String nombreRed,
          String direccionCorta, String nombreLider1,
          String nombreLider2, String telefono,
          String email, int edad) {
    super.setNombre(nombre);
    super.setNombreRed(nombreRed);
    super.setDireccionCorta(direccionCorta);
    super.setNombreLider1(nombreLider1);
    super.setNombreLider2(nombreLider2);
    super.setTelefono(telefono);
    super.setEmail(email);
    super.setEdad(edad);
  }

  public DiscipuloProceso(Discipulo discipulo) {
    this.setReferencias(discipulo.getId(), discipulo.getIdRed(), discipulo.getIdLider1(), discipulo.getIdLider2());
    this.setNombre(discipulo.getNombre());
    this.setNombreRed(discipulo.getNombreRed());
    this.setDireccionCorta(discipulo.getDireccionCorta());
    this.setNombreLider1(discipulo.getNombreLider1());
    this.setNombreLider2(discipulo.getNombreLider2());
    this.setTelefono(discipulo.getTelefono());
    this.setEmail(discipulo.getEmail());
    this.setEdad(discipulo.getEdad());
  }

  public DiscipuloProceso(String nombre, String direccionCorta, String telefono, String email, int edad) {
    super(nombre, direccionCorta, telefono, email, edad);
  }

  public DiscipuloProceso(int id) {
    super.setId(id);
  }

  @Override
  public String toString() {
    String st = "DiscipuloProceso{"
            + "id: " + this.getId()
            + ", nombre: " + this.getNombre()
            + ", red: " + this.getNombreRed()
            + ", lider1: " + this.getNombreLider1()
            + ", lider2: " + this.getNombreLider2()
            + ", telefono: " + this.getTelefono()
            + ", email: " + this.getEmail()
            + " }";
    return st;
  }

  public String getDescripcionCelula() {
    return descripcionCelula;
  }

  public void setDescripcionCelula(String descripcionCelula) {
    this.descripcionCelula = descripcionCelula;
  }

  public int getIdCelula() {
    return idCelula;
  }

  public void setIdCelula(int idCelula) {
    this.idCelula = idCelula;
  }

  public String getEstatusProceso() {
    return estatusProceso;
  }

  public void setEstatusProceso(String statusProceso) {
    this.estatusProceso = statusProceso;
  }

  public boolean isTieneCelula() {
    return tieneCelula;
  }

  public void setTieneCelula(boolean tieneCelula) {
    this.tieneCelula = tieneCelula;
  }
}