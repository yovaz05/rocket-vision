/*
 * Capa más arriba de los servicios
 */
package sig.modelo.servicios;

import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.PersonaEnRed;
import waytech.modelo.beans.sgi.Red;
import waytech.modelo.interfaces.IsaPersonaEnRed;
import waytech.modelo.interfaces.IsaRed;
import waytech.modelo.servicios.RspPersonaEnRed;
import waytech.modelo.servicios.RspRed;
import waytech.modelo.servicios.SaPersonaEnRed;
import waytech.modelo.servicios.SaRed;

/**
 *
 * @author root
 */
public class ServicioRed {

  List<Red> redes = new ArrayList();
  //tendrá los RedesNombres de las redes
  List<String> RedesNombres = new ArrayList();
  //tendrá los RedesNombres de todos los líderes lanzados de la red (quienes pueden tener célula)
  List<PersonaEnRed> lideresLanzadosRed = new ArrayList();
  List<String> lideresLanzadosRedNombres = new ArrayList();
  int idRed;
  String nombreRed = "";
  Red red;


  public List getTodas() {
    //**System.out.println("INICIO DE LA CONEXION " + rspRed.getRespuestaInicioDeConexion());
    IsaRed isaRed = new SaRed();
    RspRed rspRed = new RspRed();
    rspRed = isaRed.listRed();
    redes = rspRed.getAllReds();
    //**System.out.println("CIERRE DE LA CONEXION " + rspRed.getRespuestaInicioDeConexion());
    //**listarRedesConsola();
    return redes;
  }

  public List getRedesNombres() {
    getTodas();
    for (Red r : redes) {
      RedesNombres.add(r.getNombre());
    }
    return RedesNombres;
  }

  /**
   * devuelve el id de la red seleccionada
   * @return 
   */
  public int getIdRed() {
    for (Red r : redes) {
      if (r.getNombre().equals(nombreRed)) {
        return r.getIdRed();
      }
    }
    return 0;
  }

  
  
  public Red getRed(int idRed) {
    for (Red r : redes) {
      if (r.getIdRed() == idRed) {
        return r;
      }
    }
    return null;
  }

  public void listarRedesConsola() {
    /*
    System.out.println("NOMBRE DE TODAS LAS REDES:");
    for (Red Red : redes) {
      System.out.println("Red: " + Red.getNombre());
    }
    System.out.println("ServicioRed. Número de Redes: " + redes.size());
     * 
     */
  }
  
  public void listarLideresLanzados() {
    /*
    System.out.println("NOMBRE DE LOS LIDERES LANZADOS DE LA RED: " + nombreRed);
    System.out.println("Número de Líderes: " + lideresLanzadosRed.size());
    for (PersonaEnRed p : lideresLanzadosRed) {
      System.out.println("Líder: " + p.getIdPersona().getNombre());
    }
     * 
     */
  }
  
  /**
   * devuelve el id del líder lanzado cuyo nombre es  pasadocomo parámetro
   * @return id del líder, devuelve 0 si la persona no existe
   */
  public int getIdPersonaRed(String nombrePersonaRed) {
    for (PersonaEnRed p : lideresLanzadosRed) {
      if (p.getIdPersona().getNombre().equals(nombrePersonaRed)) {
        return p.getIdPersona().getIdPersona();
      }
    }
    return 0;
  }
  
  public List getLideresLanzados(int idRed) {
    IsaPersonaEnRed isaPersonaRed = new SaPersonaEnRed();
    RspPersonaEnRed rspPersonaRed = new RspPersonaEnRed();
    rspPersonaRed = isaPersonaRed.listLideresLanzados(idRed);
    System.out.println("INICIO DE LA CONEXION " + rspPersonaRed.getRespuestaInicioDeConexion());
    lideresLanzadosRed = rspPersonaRed.getAllPersonaEnReds();
    /**/    listarLideresLanzados();
    System.out.println("CIERRE DE LA CONEXION " + rspPersonaRed.getRespuestaInicioDeConexion());
    return lideresLanzadosRed;
  }  
  
  public List getLideresLanzadosNombres(int idRed) {
    lideresLanzadosRedNombres = new ArrayList();
    getLideresLanzados(idRed);
    for (PersonaEnRed p : lideresLanzadosRed) {
      lideresLanzadosRedNombres.add(p.getIdPersona().getNombre());
    }
    return lideresLanzadosRedNombres;
  }

  public void setNombreRed(String nombreRed) {
    this.nombreRed = nombreRed;
  }
  
  /**
   * devuelve el id del líder lanzado, pasando como parámetro el nombre
   * @return 
   */
  public int getIdLider(String nombreLider) {
    for (PersonaEnRed p : lideresLanzadosRed) {
      if (p.getIdPersona().getNombre().equals(nombreLider)) {
        return p.getIdPersona().getIdPersona();
      }
    }
    return 0;
  }
  
}
