/*
 * Capa más arriba de los servicios
 */
package sig.modelo.servicios;

import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.Ciudad;
import waytech.modelo.interfaces.IsaCiudad;
import waytech.modelo.servicios.RspCiudad;
import waytech.modelo.servicios.SaCiudad;

/**
 *
 * @author root
 */
public class ServicioCiudad {

  List<Ciudad> todas = new ArrayList();
  List<String> nombres = new ArrayList();
  String estado = "";
  //tendrá los nombres de las ciudades del 'estado elegido'
  List<String> ciudadesPorEstado;

  public List getCiudades() {
    IsaCiudad isaCiudad = new SaCiudad();
    RspCiudad rspCiudad = new RspCiudad();
    rspCiudad = isaCiudad.listCiudad();
    //*System.out.println("INICIO DE LA CONEXION " + rspCiudad.getRespuestaInicioDeConexion());
    //*System.out.println("NOMBRE DE TODAS LAS Ciudades");
    todas = rspCiudad.getTodosLosCiudades();
    //*System.out.println("CIERRE DE LA CONEXION " + rspCiudad.getRespuestaInicioDeConexion());
    return todas;
  }

  public List getNombres() {
    getCiudades();
    for (Ciudad ciudad : todas) {
      nombres.add(ciudad.getNombre());
    }
    //**listarConsolaAll();
    return nombres;
  }

  /**
   * obtiene la lista de ciudad del estado cuyo id es pasado como parámetro
   * saca la data de todas las ciudades cargadas,
   * no hace consulta a la base de datos
   * @param idEstado
   * @return 
   */
  public List getCiudadesPorEstado(int idEstado) {
    ciudadesPorEstado = new ArrayList();
    for (Ciudad ciudad : todas) {
      if (ciudad.getIdEstado().getIdEstado() == idEstado) {
        ciudadesPorEstado.add(ciudad.getNombre());
      }
    }
    return ciudadesPorEstado;
  }

  public List getCiudadesPorEstado(String nombreEstado) {
    ciudadesPorEstado = new ArrayList();
    for (Ciudad ciudad : todas) {
      if (ciudad.getIdEstado().getNombre().equals(nombreEstado)) {
        ciudadesPorEstado.add(ciudad.getNombre());
      }
    }
    return ciudadesPorEstado;
  }

  public int getIdCiudad(String nombreCiudad) {
    int id = 0;
    for (Ciudad c : todas) {
      if (c.getNombre().equals(nombreCiudad)) {
        id = c.getIdCiudad();
      }
    }
    return id;
  }

  public void listarConsolaAll() {
    System.out.println("NOMBRE DE TODAS LAS CIUDADES");
    for (Ciudad Ciudad : todas) {
      System.out.println("Ciudad: " + Ciudad.getNombre());
    }
  }

  public void listarConsolaPorEstado() {
    System.out.println("NOMBRE DE TODAS LAS CIUDADES DEL ESTADO: " + estado);
    for (String ciudad : ciudadesPorEstado) {
      System.out.println("Ciudad: " + ciudad);
    }
  }
}
