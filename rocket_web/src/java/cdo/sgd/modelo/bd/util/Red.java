package cdo.sgd.modelo.bd.util;

/**
 *
 * @author Gabriel Pérez
 */
public class Red {

  protected int id;
  protected String nombre;
  protected String diaHoraReunion;
  protected int idLider1;
  protected int idLider2;
  protected String nombreLider1;
  protected String nombreLider2;
  protected int nroCelulas;
  protected int nroLideresCelulas;
  protected int nroDiscipulosLanzados;
  protected int nroDiscipulosProceso;

  public Red() {
  }

  public Red(String nombre, String diaHoraReunion, String nombreLider1, String nombreLider2) {
    this.nombre = nombre;
    this.diaHoraReunion = diaHoraReunion;
    this.nombreLider1 = nombreLider1;
    this.nombreLider2 = nombreLider2;
  }

  public Red(String nombre, String diaHoraReunion) {
    this.nombre = nombre;
    this.diaHoraReunion = diaHoraReunion;
  }

  public Red(String nombre) {
    this.nombre = nombre;
  }

  @Override
  public String toString() {
    return "Red{" + "id=" + id + ", nombre=" + nombre + ", diaHoraReunion="
            + diaHoraReunion + ", idLider1=" + idLider1 + ", idLider2=" + idLider2
            + ", nombreLider1=" + nombreLider1 + ", nombreLider2=" + nombreLider2
            + ", nroCelulas=" + nroCelulas + ", nroLideresCelulas=" + nroLideresCelulas 
            + ", nroDiscipulosLanzados=" + nroDiscipulosLanzados
            + ", nroDiscipulosEnProceso=" + nroDiscipulosProceso + '}';
  }

  public void setNumeros(int nroCelulas, int nroLideresCelulas, int nroDiscipulosLanzados, int nroDiscipulosEnProceso) {
    this.nroCelulas = nroCelulas;
    this.nroLideresCelulas = nroLideresCelulas;
    this.nroDiscipulosLanzados = nroDiscipulosLanzados;
    this.nroDiscipulosProceso = nroDiscipulosEnProceso;
  }

  public String getDiaHoraReunion() {
    return diaHoraReunion;
  }

  public void setDiaHoraReunion(String diaHoraReunion) {
    this.diaHoraReunion = diaHoraReunion;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getIdLider1() {
    return idLider1;
  }

  public void setIdLider1(int idLider1) {
    this.idLider1 = idLider1;
    //+ updateNombreLider1();
  }

  public int getIdLider2() {
    return idLider2;
  }

  public void setIdLider2(int idLider2) {
    this.idLider2 = idLider2;
    //+ updateNombreLider2();
  }

  public void setIdLideres(int idLider1, int idLider2) {
    this.setIdLider1(idLider1);
    this.setIdLider2(idLider2);
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

  public int getNroCelulas() {
    return nroCelulas;
  }

  public void setNroCelulas(int nroCelulas) {
    this.nroCelulas = nroCelulas;
  }

  public int getNroDiscipulosProceso() {
    return nroDiscipulosProceso;
  }

  public void setNroDiscipulosProceso(int nroDiscipulosEnProceso) {
    this.nroDiscipulosProceso = nroDiscipulosEnProceso;
  }

  public int getNroDiscipulosLanzados() {
    return nroDiscipulosLanzados;
  }

  public void setNroDiscipulosLanzados(int nroDiscipulosLanzados) {
    this.nroDiscipulosLanzados = nroDiscipulosLanzados;
  }

  public int getNroLideresCelulas() {
    return nroLideresCelulas;
  }

  public void setNroLideresCelulas(int nroLideresCelulas) {
    this.nroLideresCelulas = nroLideresCelulas;
  }
}
