package sig.modelo.bd.simulador;

/**
 *
 * @author Gabriel Pérez
 */
public class LiderCelulaListado extends LiderCelula {

  protected int nroItem;

  public LiderCelulaListado(int nroItem) {
    this.nroItem = nroItem;
  }

  public LiderCelulaListado(int nroItem, LiderCelula liderCelula) {
    this.nroItem = nroItem;
    this.id = liderCelula.getId();
    this.nombre = liderCelula.getNombre();
    this.idRed = liderCelula.getIdRed();
    this.nombreRed = liderCelula.getNombreRed();
    this.direccionCorta = liderCelula.getDireccionCorta();
    this.nombreLider1 = liderCelula.getNombreLider1();
    this.nombreLider2 = liderCelula.getNombreLider2();
    this.telefonoPrincipal = liderCelula.getTelefono();
    this.email = liderCelula.getEmail();
    this.edad = liderCelula.getEdad();
    this.idCelula = liderCelula.getIdCelula();
    this.descripcionCelula = liderCelula.getDescripcionCelula();
    this.idParejaMinisterial = liderCelula.getIdParejaMinisterial();
    this.nombreParejaMinisterial = liderCelula.getNombreParejaMinisterial();
  }

  public LiderCelulaListado() {
    super();
  }
  
  @Override
  public String toString(){
    StringBuffer sb = new StringBuffer();
    sb.append("LiderCelulaListado{"
            + "nro: " + this.getNroItem()
            + " id: " + this.getId()
            + ", nombre: " + this.getNombre()
            + ", red: " + this.getNombreRed()
            + ", direccion: " + this.getDireccionCorta()
            + ", lider1: " + this.getNombreLider1()
            + ", lider2: " + this.getNombreLider2()
            + ", telefono: " + this.getTelefono()
            + ", email: " + this.getEmail()
            + ", idCelula: " + this.getIdCelula()
            + ", descCelula: " + this.getDescripcionCelula()
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
