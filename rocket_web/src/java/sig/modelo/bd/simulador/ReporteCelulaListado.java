package sig.modelo.bd.simulador;

/**
 *
 * @author Gabriel
 */
public class ReporteCelulaListado extends ReporteCelula{
  int nroItem;
  
  public ReporteCelulaListado() {
    super();
  }

  public ReporteCelulaListado(int idCelula) {
    super(idCelula);
  }

  public ReporteCelulaListado(int nroItem, ReporteCelula reporte) {
    this.nroItem = nroItem;
    this.id = reporte.id;
    this.idReporte = reporte.idReporte;
    this.idRed = reporte.idRed;
    this.codigo = reporte.codigo;
    this.descripcionCelula = reporte.descripcionCelula;
    this.dia = reporte.dia;
    this.direccionCorta = reporte.direccionCorta;
    this.estatus = reporte.estatus;
    this.descripcionEstatus = reporte.descripcionEstatus;
    this.idCelula = reporte.idCelula;
    this.nombreLider1 = reporte.nombreLider1;
    this.nombreLider2 = reporte.nombreLider2;
    this.nombre = reporte.nombre;
    this.setReferencias(reporte.idRed, reporte.idLider1, reporte.idLider2);
}

  public ReporteCelulaListado(int idCelula, int nroItem) {
    super(idCelula);
    this.nroItem = nroItem;
  }

  @Override
  public String toString() {
    return "ReporteCelulaListado{"
            + "nroItem=" + nroItem
            + ", "
            + super.toString()
            + '}';
  }

  public String toString(int modo) {
    return "ReporteCelulaListado{"
            + "nroItem=" + nroItem
            + ", "
            + super.toString(modo)
            + '}';
  }

  public int getNroItem() {
    return nroItem;
  }

  public void setNroItem(int nroItem) {
    this.nroItem = nroItem;
  }

  
}
