package rocket.modelo.bd.util;


/**
 *
 * @author Gabriel
 */
public class CelulaListadoUtil extends CelulaUtil {

  protected int nroItem;

  public CelulaListadoUtil() {
  }
  
  public CelulaListadoUtil(int nroItem) {
    this.nroItem = nroItem;
  }

  public CelulaListadoUtil(int nroItem, CelulaUtil celula) {
    this.nroItem = nroItem;
    this.id = celula.getId();
    this.idRed = celula.getIdRed();
    this.idLider1 = celula.getIdLider1();
    this.idLider2 = celula.getIdLider2();
    this.idLider3 = celula.getIdLider3();
    this.idLider4 = celula.getIdLider4();
    this.codigo = celula.getCodigo();
    this.nombre = celula.getNombre();
    this.nombreRed = celula.getNombreRed();
    this.direccionCorta = celula.getDireccionCorta();
    this.nombreLider1 = celula.getNombreLider1();
    this.nombreLider2 = celula.getNombreLider2();
    this.nombreLider3 = celula.getNombreLider3();
    this.nombreLider4 = celula.getNombreLider4();
    this.dia = celula.getDia();
  }
  
  @Override
  public String toString(){
    StringBuffer sb = new StringBuffer();
    sb.append("CelulaListado{"
            + "nro: " + this.getNroItem()
            + " id: " + this.getId()
            + ", codigo: " + this.getCodigo()
            + ", nombre: " + this.getNombre()
            + ", red: " + this.getNombreRed()
            + ", direccionCorta: " + this.getDireccionCorta()
            + ", diaHora: " + this.getDia()
            + ", lider1: " + this.getNombreLider1()
            + ", lider2: " + this.getNombreLider2()
            + ", lider3: " + this.getNombreLider3()
            + ", lider4: " + this.getNombreLider4()
            + ", idLider1: " + this.getIdLider1()
            + ", idLider2: " + this.getIdLider2()
            + ", idLider3: " + this.getIdLider3()
            + ", idLider4: " + this.getIdLider4()
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
