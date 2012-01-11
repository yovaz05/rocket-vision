/**
 * DiscipuloProcesoListado, usado para encapsular la data a mostrar en el listado de discípulos en proceso
 */
package sig.modelo.bd.util;

/**
 *
 * @author Gabriel Pérez
 */
public class DiscipuloProcesoListado extends DiscipuloProceso {

  protected int nroItem;

  public DiscipuloProcesoListado(int nroItem) {
    this.nroItem = nroItem;
  }

  public DiscipuloProcesoListado(int nroItem, DiscipuloProceso discipulo) {
    this.nroItem = nroItem;
    this.setReferencias(discipulo.getId(),discipulo.getIdRed(),discipulo.getIdLider1(),discipulo.getIdLider2());
    this.setNombre(discipulo.getNombre());
    this.setNombreRed(discipulo.getNombreRed());
    this.setDireccionCorta(discipulo.getDireccionCorta());
    this.setNombreLider1(discipulo.getNombreLider1());
    this.setNombreLider2(discipulo.getNombreLider2());
    this.setTelefono(discipulo.getTelefono());
    this.setEmail(discipulo.getEmail());
    this.setEdad(discipulo.getEdad());
    this.setEstatusProceso(discipulo.getEstatusProceso());
    this.setIdCelula(discipulo.getIdCelula());
    this.setDescripcionCelula(discipulo.getDescripcionCelula());
  }

  public DiscipuloProcesoListado() {
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
            + ", estatus-proceso: " + this.getIdLider2()
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
