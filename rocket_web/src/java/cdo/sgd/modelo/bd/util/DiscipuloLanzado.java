/**
 * LiderCelula, encapsula la data un líder de célula
 */
package cdo.sgd.modelo.bd.util;

/**
 *
 * @author Gabriel Pérez
 */
public class DiscipuloLanzado extends Discipulo {

  boolean  tieneCelula = false;
   
  public DiscipuloLanzado() {
    super();
  }

  public DiscipuloLanzado(String nombre, String nombreRed, String direccionCorta, String nombreLider1, String nombreLider2, String telefono, String email, int edad) {
    super.setNombre(nombre);
    super.setNombreRed(nombreRed);
    super.setDireccionCorta(direccionCorta);
    super.setNombreLider1(nombreLider1);
    super.setNombreLider2(nombreLider2);
    super.setTelefono(telefono);
    super.setEmail(email);
    super.setEdad(edad);
  }
  
  public DiscipuloLanzado(Lider lider) {
    this.setReferencias(lider.getId(),lider.getIdRed(),lider.getIdLider1(),lider.getIdLider2());
    this.setNombre(lider.getNombre());
    this.setNombreRed(lider.getNombreRed());
    this.setDireccionCorta(lider.getDireccionCorta());
    this.setNombreLider1(lider.getNombreLider1());
    this.setNombreLider2(lider.getNombreLider2());
    this.setTelefono(lider.getTelefono());
    this.setEmail(lider.getEmail());
    this.setEdad(lider.getEdad());
}

  public DiscipuloLanzado(int id) {
    super.setId(id);
  }
  
  @Override
  public String toString(){
    StringBuffer sb = new StringBuffer();
    sb.append("LiderLanzado{"
            + "id: " + this.getId()
            + ", nombre: " + this.getNombre()
            + ", red: " + this.getNombreRed()
            + ", lider1: " + this.getNombreLider1()
            + ", lider2: " + this.getNombreLider2()
            + ", telefono: " + this.getTelefono()
            + ", email: " + this.getEmail()
            + " }"
            );
    return sb.toString();
  }

}