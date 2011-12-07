/**
 * bean CelulaSimulador,
 * contiene los datos de una célula
 */
package cdo.sgd.modelo.bd.simulador;


/**
 *
 * @author Gabriel Pérez
 */
public class CelulaUtil {

  protected int id = 0;
  protected String codigo = "";
  protected String nombre = "";
  protected String nombreRed = "";
  protected String nombreLider1 = "";
  protected String nombreLider2 = "";
  protected String nombreLider3 = "";
  protected String nombreLider4 = "";
  protected String dia = "";
  protected String hora = "";
  protected String direccionCorta = "";
  protected Direccion direccion = new Direccion("","","","","");
  protected String fechaApertura = "";
  protected String anfitrion = "";
  protected String observaciones = "";
  
  //para navegación integrada:
  protected int idRed = 0;
  protected int idLider1 = 0;
  protected int idLider2 = 0;
  protected int idLider3 = 0;
  protected int idLider4 = 0;
  //indica día de semana en que se realiza la célula:
  //1:domingo, 2:lunes, ... , 7:sábado
  private int diaSemana;
  private int numeroLideres;

  public CelulaUtil() {
  }

  public CelulaUtil(int id) {
    this.id = id;
  }

  /**
   * constructor de celula con datos de resumen
   */
  public CelulaUtil(String codigo, String nombre, String nombreRed, String nombreLider1, String nombreLider2, String dia, String hora) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.nombreRed = nombreRed;
    this.nombreLider1 = nombreLider1;
    this.nombreLider2 = nombreLider2;
    this.dia = dia;
    this.hora = hora;
  }

  public CelulaUtil(String codigo, String nombre, String dia, String hora) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.dia = dia;
    this.hora = hora;
  }

  @Override
  public String toString() {
    return "Celula{" + "id=" + id + ", codigo=" + codigo
            + ", nombre=" + nombre + ", Red=" + nombreRed
            + ", Lider1=" + nombreLider1 + ", Lider2=" + nombreLider2
            + ", Lider3=" + nombreLider3 + ", Lider4=" + nombreLider4
            + ", dia=" + dia
            + ", hora=" + hora
            + ", direccionCorta=" + direccion.getDireccionCorta()
            + ", direccionCompleta=" + direccion.toString()
            + ", fechaApertura=" + fechaApertura
            + ", anfitrion=" + anfitrion 
            + ", observaciones=" + observaciones
            + ", IDS: id-Red=" + idRed
            + ", id-Lider1=" + idLider1 + ", id-Lider2=" + idLider2
            + ", id-Lider2=" + idLider3 + ", idLider4=" + idLider4 + '}';
  }

  /**
   * configura IDs de referencias
   * @param idRed
   * @param idLider1
   * @param idLider2
   * @param idSupervisor1
   * @param idSupervisor2 
   */
  public void setReferencias(int idRed, int idLider1, int idLider2) {
    this.setIdRed(idRed);
    this.setIdLider1(idLider1);
    this.setIdLider2(idLider2);
  }

  public void setLideres(String nombreLider1, String nombreLider2) {
    this.nombreLider1 = nombreLider1;
    this.nombreLider2 = nombreLider2;
  }
  
  public String getDireccionCorta() {
    return direccionCorta;
  }

  /**
   * dirección corta por ahora es la zona
   * @param direccionCorta 
   */
  public void setDireccionCorta(String direccionCorta) {
    this.direccionCorta = direccionCorta;
  }

  public String getAnfitrion() {
    return anfitrion;
  }

  public void setAnfitrion(String anfitrion) {
    this.anfitrion = anfitrion;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getDia() {
    return dia;
  }

  public void setDia(String diaHora) {
    this.dia = diaHora;
  }
  
  public String getHora() {
    return hora;
  }

  public void setHora(String hora) {
    this.hora = hora;
  }

  //TODO: crear método facilitadores para direccion.zona,.estado, .ciudad, .dirDetallada, .telefono
  public Direccion getDireccion() {
    return direccion;
  }

  public void setDireccion(Direccion direccion) {
    this.direccion = direccion;
    //- updateDireccionCorta();
  }

  public String getFechaApertura() {
    return fechaApertura;
  }

  public void setFechaApertura(String fechaApertura) {
    this.fechaApertura = fechaApertura;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
  
  public int getIdLider1() {
    return idLider1;
  }

  public void setIdLider1(int idLider1) {
    this.idLider1 = idLider1;
    //updateNombreLider1();
  }

  //TODO: borrar o evaluar
  public void updateNombreLider1() {
    if (idLider1 == 1) {
      this.nombreLider1 = "Pastor Rogelio Mora";
      return;
    }
  }

  public int getIdLider2() {
    return idLider2;
  }

  public void setIdLider2(int idLider2) {
    this.idLider2 = idLider2;
    //updateNombreLider2();
  }
  
/*
  public void updateNombreLider2() {
    this.nombreLider2 = (idLider2 != 0) ? bd.buscarLider(idLider2).getNombre() : "";
  }
*/
  public int getIdRed() {
    return idRed;
  }

  public void setIdRed(int idRed) {
    this.idRed = idRed;
    //updateNombreRed();
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombreLider1() {
    return nombreLider1;
  }

  public void setNombreLider1(String nombreLider1) {
    this.nombreLider1 = nombreLider1;
  }

  public String getNombreLider2() {
    return nombreLider2;
  }

  public void setNombreLider2(String nombreLider2) {
    this.nombreLider2 = nombreLider2;
  }

  public String getNombreRed() {
    return nombreRed;
  }

  public void setNombreRed(String nombreRed) {
    this.nombreRed = nombreRed;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  /*
  private void updateNombreRed() {
    this.setNombreRed(bd.buscarRed(this.idRed).getNombre());
  }

  private void updateNombreSupervisor1() {
    Lider supervisor = bd.buscarLider(idSupervisor1);
    this.nombreSupervisor1 = supervisor.getNombre();
  }

  private void updateNombreSupervisor2() {
   Lider supervisor = bd.buscarLider(idSupervisor2);
    this.nombreSupervisor2 = supervisor.getNombre();
  }
/**/
  private void updateDireccionCorta() {
    setDireccionCorta(direccion.zona + " - " + direccion.ciudad);
  }
  /**/

  void setDiaSemana(int diaSemana) {
    this.diaSemana = diaSemana;
  }

  public int getDiaSemana() {
    return diaSemana;
  }

  public int getIdLider3() {
    return idLider3;
  }

  public void setIdLider3(int idLider3) {
    this.idLider3 = idLider3;
  }

  public int getIdLider4() {
    return idLider4;
  }

  public void setIdLider4(int idLider4) {
    this.idLider4 = idLider4;
  }

  public String getNombreLider3() {
    return nombreLider3;
  }

  public void setNombreLider3(String nombreLider3) {
    this.nombreLider3 = nombreLider3;
  }

  public String getNombreLider4() {
    return nombreLider4;
  }

  public void setNombreLider4(String nombreLider4) {
    this.nombreLider4 = nombreLider4;
  }

  public int getNumeroLideres() {
    return numeroLideres;
  }

  public void setNumeroLideres(int numeroLideres) {
    this.numeroLideres = numeroLideres;
  }

  
}
