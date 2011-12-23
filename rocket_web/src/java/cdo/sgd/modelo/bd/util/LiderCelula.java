/**
 * LiderCelula, encapsula la data un líder de célula
 */
package cdo.sgd.modelo.bd.util;

/**
 *
 * @author Gabriel Pérez
 */
public class LiderCelula extends Lider {

  protected String descripcionCelula;
  protected int idCelula;
  
  public LiderCelula() {
    super();
  }

  public LiderCelula(String nombre, String nombreRed, String direccionCorta, String nombreLider1, String nombreLider2, String telefono, String email, int edad) {
    super.setNombre(nombre);
    super.setNombreRed(nombreRed);
    super.setDireccionCorta(direccionCorta);
    super.setNombreLider1(nombreLider1);
    super.setNombreLider2(nombreLider2);
    super.setTelefono(telefono);
    super.setEmail(email);
    super.setEdad(edad);
  }
  
  public LiderCelula(Lider lider) {
    this.setId(lider.getId());
    this.setIdRed(lider.getIdRed());
    this.setNombre(lider.getNombre());
    this.setNombreRed(lider.getNombreRed());
    this.setDireccionCorta(lider.getDireccionCorta());
    this.setNombreLider1(lider.getNombreLider1());
    this.setNombreLider2(lider.getNombreLider2());
    this.setTelefono(lider.getTelefono());
    this.setEmail(lider.getEmail());
    this.setEdad(lider.getEdad());
    this.setIdParejaMinisterial(lider.getIdParejaMinisterial());
  }

  public LiderCelula(int id) {
    super.setId(id);
  }
  
  @Override
  public String toString(){
    StringBuffer sb = new StringBuffer();
    sb.append("LiderCelula{"
            + "id: " + this.getId()
            + ", nombre: " + this.getNombre()
            + ", red: " + this.getNombreRed()
            + ", lider1: " + this.getNombreLider1()
            + ", lider2: " + this.getNombreLider2()
            + ", telefono: " + this.getTelefono()
            + ", email: " + this.getEmail()
            + ", celulaId: " + this.getIdCelula()
            + ", celulaDesc: " + this.getDescripcionCelula()
            + " }"
            );
    return sb.toString();
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
    //+updateDescripcionCelula();
  }

  /*
  private void updateDescripcionCelula() {
    Celula celula = bd.buscarCelula(idCelula);
    setDescripcionCelula(celula.getDireccionCorta());
  }
*/
  
}