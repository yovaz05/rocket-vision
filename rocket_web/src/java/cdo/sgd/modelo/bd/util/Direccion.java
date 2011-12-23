package cdo.sgd.modelo.bd.util;

public class Direccion {

  String estado = "";
  String ciudad = "";
  String zona = "";
  String dirDetallada = "";
  String telefono = "";
  int idEstado = 0;
  int idCiudad = 0;
  int idZona = 0;

  public Direccion() {
  }

  public Direccion(String estado, String ciudad, String zona, String dirDetallada, String telefono) {
    this.estado = estado;
    this.ciudad = ciudad;
    this.zona = zona;
    this.dirDetallada = dirDetallada;
    this.telefono = telefono;
  }

  @Override
  public String toString() {
    return "Direccion{" + "estado=" + estado + ", ciudad=" + ciudad + ", zona=" + zona + ", dirDetallada=" + dirDetallada + ", telefono=" + telefono + '}';
  }
  
  public String getDireccionCorta() {    
    return zona;
  }
  
  public String getCiudad() {
    return ciudad;
  }

  public void setCiudad(String ciudad) {
    this.ciudad = ciudad;
  }

  public String getDirDetallada() {
    return dirDetallada;
  }

  public void setDirDetallada(String dirDetallada) {
    this.dirDetallada = dirDetallada;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getZona() {
    return zona;
  }

  public void setZona(String zona) {
    this.zona = zona;
  }

  public int getIdCiudad() {
    return idCiudad;
  }

  public void setIdCiudad(int idCiudad) {
    this.idCiudad = idCiudad;
  }

  public int getIdEstado() {
    return idEstado;
  }

  public void setIdEstado(int idEstado) {
    this.idEstado = idEstado;
  }

  public int getIdZona() {
    return idZona;
  }

  public void setIdZona(int idZona) {
    this.idZona = idZona;
  }
  
  
}
