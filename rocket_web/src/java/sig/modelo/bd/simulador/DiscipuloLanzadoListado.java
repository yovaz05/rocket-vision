package sig.modelo.bd.simulador;

/**
 *
 * @author Gabriel Pérez
 */
public class DiscipuloLanzadoListado extends DiscipuloLanzado {

  protected int nroItem;

  public DiscipuloLanzadoListado(int nroItem) {
    this.nroItem = nroItem;
  }

  public DiscipuloLanzadoListado(int nroItem, DiscipuloLanzado lider) {
    this.nroItem = nroItem;
    this.setReferencias(lider.getId(),lider.getIdRed(),lider.getIdLider1(),lider.getIdLider2());
    this.nombre = lider.getNombre();
    this.setNombreRed(lider.getNombreRed());
    this.setDireccionCorta(lider.getDireccionCorta());
    this.setNombreLider1(lider.getNombreLider1());
    this.setNombreLider2(lider.getNombreLider2());
    this.setTelefono(lider.getTelefono());
    this.setEmail(lider.getEmail());
    this.setEdad(lider.getEdad());
  }

  public DiscipuloLanzadoListado() {
    super();
  }
  
  @Override
  public String toString(){
    StringBuffer sb = new StringBuffer();
    sb.append("LiderCelulaListado{"
            + "nro: " + this.getNroItem()
            + ", id: " + this.getId()
            + ", nombre: " + this.getNombre()
            + ", red: " + this.getNombreRed()
            + ", direccion: " + this.getDireccionCorta()
            + ", lider1: " + this.getNombreLider1()
            + ", lider2: " + this.getNombreLider2()
            + ", telefono: " + this.getTelefono()
            + ", email: " + this.getEmail()
            + ", idRed: " + this.getIdRed()
            + ", idLider1: " + this.getIdLider1()
            + ", idLider2: " + this.getIdLider2()
            + " }"
            );
    return sb.toString();
  }
  

  public int getNroItem() {
    return nroItem;
  }

  public void setNroItem(int nroItem) {
    this.nroItem = nroItem;
  }
  
}
