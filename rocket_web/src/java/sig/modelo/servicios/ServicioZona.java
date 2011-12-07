/*
 * Capa más arriba de los servicios
 */
package sig.modelo.servicios;

import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.Zona;
import waytech.modelo.interfaces.IsaZona;
import waytech.modelo.servicios.RspZona;
import waytech.modelo.servicios.SaZona;

/**
 *
 * @author Gabriel
 */
public class ServicioZona {

  List<Zona> todas = new ArrayList();
  List<String> nombres = new ArrayList();
  //Ciudad elegida
  String ciudad = "";
  //tendrá los nombres de las Zonas del 'estado elegido'
  List<String> zonasPorCiudad;

  public List getZonas() {
    //**System.out.println("INICIO DE LA CONEXION " + rspZona.getRespuestaInicioDeConexion());
    IsaZona isaZona = new SaZona();
    RspZona rspZona = new RspZona();
    rspZona = isaZona.listZona();
    todas = rspZona.getTodosLosZonas();
    //**listarConsolaAll();
    //**System.out.println("CIERRE DE LA CONEXION " + rspZona.getRespuestaInicioDeConexion());
    return todas;
  }

  //TODO: llamar este método en el controlador
  public List getNombres() {
    getZonas();
    for (Zona Zona : todas) {
      nombres.add(Zona.getNombre());
    }
    /**/ listarConsolaAll();
    return nombres;
  }

  public List getZonasPorCiudad(int idCiudad) {
    zonasPorCiudad = new ArrayList();
    for (Zona zona : todas) {
      if (zona.getIdCiudad().getIdCiudad() == idCiudad) {
        zonasPorCiudad.add(zona.getNombre());
      }
    }
    return zonasPorCiudad;
  }

  public List getZonasPorCiudad(String nombreCiudad) {
    zonasPorCiudad = new ArrayList();
    for (Zona zona : todas) {
      if (zona.getIdCiudad().getNombre().equals(nombreCiudad)) {
        zonasPorCiudad.add(zona.getNombre());
      }
    }
    return zonasPorCiudad;
  }

  public int getIdZona(String nombre) {
    for (Zona z : todas) {
      if (z.getNombre().equals(nombre)) {
        return z.getIdZona();
      }
    }
    return 0;
  }

  public void listarConsolaAll() {
    /*
    System.out.println("NOMBRE DE TODAS LAS Zonas");
    for (Zona zona : todas) {
    System.out.println("Zona: " + zona.getNombre());
    }
     * 
     */
  }

  public void listarConsolaPorCiudad() {
    /*
    System.out.println("NOMBRE DE TODAS LAS Zonas de la Ciudad: " + ciudad);
    for (String zona : zonasPorCiudad) {
    System.out.println("Zona: " + zona);
    }
     * 
     */
  }
}
