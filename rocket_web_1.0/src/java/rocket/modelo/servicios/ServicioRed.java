/*
 * Capa más arriba de los servicios
 */
package rocket.modelo.servicios;

import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.beans.sgi.Red;
import waytech.modelo.servicios.RspPersona;
import waytech.modelo.servicios.RspRed;
import waytech.modelo.servicios.SaPersona;
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
  //-List<Persona> lideresLanzados = new ArrayList();
  List<Persona> lideresLanzados = new ArrayList();
  List<String> lideresLanzadosNombres = new ArrayList();
  int idRed;
  String nombreRed = "";
  Red red;
  int nroLideresLanzados = 0;

  public List getTodas() {
    //**System.out.println("INICIO DE LA CONEXION " + rspRed.getRespuestaInicioDeConexion());
    SaRed saRed = new SaRed();
    RspRed rspRed = new RspRed();
    rspRed = saRed.listRed();
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
    System.out.println("ServicioRed. listarLideresLanzados. Número de Líderes: " + lideresLanzados.size());
    /*
    System.out.println("NOMBRE DE LOS LIDERES LANZADOS DE LA RED: " + nombreRed);
    for (Persona p : lideresLanzados) {
    System.out.println("Líder: " + p.getIdPersona().getNombre());
    }
     * 
     */
  }

  /**
   * devuelve el id del líder lanzado cuyo nombre es  pasadocomo parámetro
   * @return id del líder, devuelve 0 si la persona no existe
   */
  /*
  public int getIdPersonaRed(String nombrePersonaRed) {
    for (Persona p : lideresLanzados) {
      if (p.getIdPersona().getNombre().equals(nombrePersonaRed)) {
        return p.getIdPersona().getIdPersona();
      }
    }
    return 0;
  }
   */
  /**
   * devuelve el id del líder lanzado cuyo nombre es  pasado como parámetro
   * @return id del líder, devuelve 0 si la persona no existe
   */
  public int getIdPersona(String nombrePersona) {
    for (Persona p : lideresLanzados) {
      if (p.getNombre().equals(nombrePersona)) {
        return p.getIdPersona();
      }
    }
    return 0;
  }

  /**
   * 1ER ENFOQUE, USANDO TABLA PERSONA_EN_RED
   * @param idRed
   * @return 
   */
  /*-
  public List getLideresLanzados(int idRed) {
  SaPersona saPersonaRed = new SaPersona();
  RspPersona rspPersonaRed = new RspPersona();
  rspPersonaRed = saPersonaRed.listLideresLanzados(idRed);    
  System.out.println("INICIO DE LA CONEXION " + rspPersonaRed.getRespuestaInicioDeConexion());
  lideresLanzados = rspPersonaRed.getAllPersonas();
  System.out.println("CIERRE DE LA CONEXION " + rspPersonaRed.getRespuestaInicioDeConexion());
  return lideresLanzados;
  }
   */
  /**
   * lista todos los líderes lanzados de una red específica
   * 2do enfoque, usando campo 'id_red' en tabla 'persona'
   * @param idRed la red
   * @return 
   **/
  public List getLideresLanzados(int idRed) {
    SaPersona saPersona = new SaPersona();
    RspPersona respuesta = saPersona.listPersonaLiderLanzadoPorRed(idRed);
    System.out.println("INICIO DE LA CONEXION " + respuesta.getRespuestaInicioDeConexion());
    lideresLanzados = respuesta.getTodasLasPersonas();
    /**/ listarLideresLanzados();
    System.out.println("CIERRE DE LA CONEXION " + respuesta.getRespuestaInicioDeConexion());
    return lideresLanzados;
  }

  public List getLideresLanzadosNombres(int idRed) {
    lideresLanzadosNombres = new ArrayList();
    getLideresLanzados(idRed);
    for (Persona p : lideresLanzados) {
      lideresLanzadosNombres.add(p.getNombre());
    }
    return lideresLanzadosNombres;
  }

  public void setNombreRed(String nombreRed) {
    this.nombreRed = nombreRed;
  }

  /**
   * devuelve el id del líder lanzado, pasando como parámetro el nombre
   * @return 
   */
  public int getIdLider(String nombreLider) {
    for (Persona p : lideresLanzados) {
      if (p.getNombre().equals(nombreLider)) {
        return p.getIdPersona();
      }
    }
    return 0;
  }

  //TODO: no se pueden quitar de la lista general, sino crear una lista local
  /**
   * quita un líder de la lista
   * para que no pueda aparecer en los líderes disponibles
   * @param idLider el id del líder a quitar de la lista
   * @return true si fue quitado, false si no fue quitado
   */
  public boolean quitarLiderLista(int idLider) {
    System.out.print("ServicioRed.quitarLiderLista.idLider=" + idLider);
    for (Persona p : lideresLanzados) {
      if (p.getIdPersona() == idLider) {
        lideresLanzadosNombres.remove(p.getNombre());
        System.out.println("->DONE");
        return true;
      }
    }
    System.out.println("->ERROR");
    return false;
  }

  //métodos setter y getter
  public List<String> getLideresLanzadosNombres() {
    return lideresLanzadosNombres;
  }

  /**
   * agrega un líder a la lista
   * para que pueda aparecer en los líderes disponibles
   * @param idLider el id del líder a agregar de la lista
   * @return true/false si se pudo realizar la operación
   */
  public boolean agregarLiderLista(int idLider) {
    for (Persona p : lideresLanzados) {
      if (p.getIdPersona() == idLider) {
        lideresLanzadosNombres.add(p.getNombre());
        return true;
      }
    }
    return false;
  }

  public int getNroLideresLanzados() {
    nroLideresLanzados = lideresLanzados.size();
    return nroLideresLanzados;
  }
}
