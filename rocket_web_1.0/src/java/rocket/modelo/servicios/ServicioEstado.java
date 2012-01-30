/*
 * Capa por arriba de Servicio Estado
 */
package rocket.modelo.servicios;

import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.Estado;
import waytech.modelo.interfaces.IsaEstado;
import waytech.modelo.servicios.RspEstado;
import waytech.modelo.servicios.SaEstado;

/**
 *
 * @author root
 */
public class ServicioEstado {

  List<Estado> estados = new ArrayList();
  List<String> nombresEstados = new ArrayList();  

  public List getEstados() {
    IsaEstado isaEstado = new SaEstado();
    RspEstado rspEstado = new RspEstado();
    rspEstado = isaEstado.listEstado();
    //** System.out.println("INICIO DE LA CONEXION " + rspEstado.getRespuestaInicioDeConexion());    System.out.println("NOMBRE DE LOS ESTADOS");
    estados = rspEstado.getTodosLosEstados();
    //** listarAllConsola();
    //** System.out.println("CIERRE DE LA CONEXION " + rspEstado.getRespuestaInicioDeConexion());
    return estados;
  }

  public List getNombres() {
    getEstados();
    for (Estado estado: estados) {
      nombresEstados.add(estado.getNombre());
    }
    return nombresEstados;
  }
  
  public int getIdEstado(String nombreEstado) {
    int id = 0;
    for (Estado e : estados) {
      if (e.getNombre().equals(nombreEstado)) {
        id = e.getIdEstado();
      }
    }
    return id;
  }  

  public void listarAllConsola() {
    System.out.println("NOMBRE DE LOS ESTADOS");
    for (Estado estado : estados) {
      System.out.println("ESTADO: " + estado.getNombre());
    }
  }
}
