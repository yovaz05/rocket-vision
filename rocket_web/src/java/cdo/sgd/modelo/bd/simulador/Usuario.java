/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cdo.sgd.modelo.bd.simulador;

import java.util.Date;

/**
 *
 * @author Gabriel
 */
public class Usuario extends Lider {

  //indica si está logueado 
  private boolean registrado = false;
  //nombre de usuario, por defecto será la cédula, luego el usuario lo puede cambiar
  private String login = "";
  private String password = "";
  private Date ultimaVisita;
  private int tipo = 0;
  public static int ADMIN_SISTEMA = 1;
  public static int LIDER_RED = 2;
  public static int LIDER_CELULA = 3;
  public static int ADMIN_CELULAS = 4;

  public Usuario() {
  }

  public Usuario(Lider lider) {
    this.setReferencias(lider.getId(), lider.getIdRed(), lider.getIdLider1(), lider.getIdLider2());
    this.setNombre(lider.getNombre());
    this.setNombreRed(lider.getNombreRed());
    this.setDireccionCorta(lider.getDireccionCorta());
    this.setNombreLider1(lider.getNombreLider1());
    this.setNombreLider2(lider.getNombreLider2());
    this.setTelefono(lider.getTelefono());
    this.setEmail(lider.getEmail());
    this.setEdad(lider.getEdad());
  }

  public Usuario(int cedula) {
    this.cedula = cedula;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public Date getUltimaVisita() {
    return ultimaVisita;
  }

  public void setUltimaVisita(Date ultimaVisita) {
    this.ultimaVisita = ultimaVisita;
  }

  public boolean isRegistrado() {
    return registrado;
  }

  public void setRegistrado(boolean estado) {
    this.registrado = estado;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getTipo() {
    return tipo;
  }

  public void setTipo(int tipoUsuario) {
    this.tipo = tipoUsuario;
  }

  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Usuario)) {
      return false;
    }
    Usuario other = (Usuario) object;
    if ((this.cedula == 0 && other.cedula != 0) || (this.cedula != 0 && this.cedula != other.cedula)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Usuario{" + "id=" + id + ", login=" + login + ", password=" + password + ", tipo=" + tipo + ":" + getDescripcionTipo() + ", registrado=" + registrado + ", ultimaVisita=" + ultimaVisita + ", nombre=" + nombre + '}';
  }

  String getDescripcionTipo() {
    String desc = "";
    if (tipo == LIDER_RED) {
      desc = "Líder-Red";
    } else if (tipo == LIDER_CELULA) {
      desc = "Líder-Célula";
    } else if (tipo == ADMIN_CELULAS) {
      desc = "Admin-Sistema";
    }
    return desc;
  }
}
