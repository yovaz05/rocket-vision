/*
 * Clase de Pruebas de Servicios.
 */
package rocket_servicios;

import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.AccesoInsert;
import waytech.modelo.beans.sgi.Celula;
import waytech.modelo.beans.sgi.CelulaInsert;
import waytech.modelo.interfaces.IsaAcceso;
import waytech.modelo.interfaces.IsaCelula;
import waytech.modelo.servicios.RspAcceso;
import waytech.modelo.servicios.RspCelula;
import waytech.modelo.servicios.SaAcceso;
import waytech.modelo.servicios.SaCelula;

/**
 *
 * @author root
 */
public class Rocket_Servicios {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    //prueba insert acceso:
    IsaAcceso isaAcceso = new SaAcceso();
    AccesoInsert accesoInsert = new AccesoInsert();
    accesoInsert.setCorreo("gerardo.montilla111@yahoo.es");
    accesoInsert.setFechaUltimoAcceso("2011-11-24 10:55:00");
    accesoInsert.setIdPersona(1);
    accesoInsert.setLogin("nuevologin111");
    accesoInsert.setPassword("nuevoPassword111");
    isaAcceso.insertAcceso(accesoInsert);
//        RspAcceso rspAcceso = new RspAcceso();        
//        System.out.println("EL NUEVO ID DE ACCESO ES  " + isaAcceso.ingresarAcceso(accesoInsert).getAcceso().getIdAcceso());
//        IsaCelula isaCelula = new SaCelula();
//        CelulaInsert celulaInsert = new CelulaInsert();
//        celulaInsert.setAnfitrion("Gerardo Montilla");
//        celulaInsert.setCodigo("CGC-05");
//        celulaInsert.setDia(1);
//        celulaInsert.setDireccion("NUEVA SEGOVIA");
//        celulaInsert.setHora(7);
//        celulaInsert.setIdRed(1);
//        celulaInsert.setIdZona(1);
//        celulaInsert.setNombre("NUEVA CELULA");
//        celulaInsert.setObservaciones("HAY MUCHA PLATA");
//        celulaInsert.setTelefono("04145155778");
//        System.out.println("EL ID DE LA CEL ES " + isaCelula.ingresarCelula(celulaInsert).getCelula().getIdCelula());
    getAllCelulas();
  }

  public static List getAllCelulas() {
    List<Celula> celulas = new ArrayList();
    IsaCelula isaCelula = new SaCelula();
    RspCelula rspCelula = new RspCelula();
    rspCelula = isaCelula.listCelula();
    System.out.println("INICIO DE LA CONEXION " + rspCelula.getRespuestaInicioDeConexion());
    celulas = rspCelula.getTodosLosCelulas();
    System.out.println("CIERRE DE LA CONEXION " + rspCelula.getRespuestaInicioDeConexion());
    if (celulas == null) {
      System.out.println("ListaCelulas=null");
    }
    System.out.println("TODAS LAS CELULAS:");
    for (Celula Celula : celulas) {
      System.out.println("Nombre: " + Celula.getNombre());
      System.out.println("Código: " + Celula.getCodigo());
      System.out.println("Id: " + Celula.getIdCelula());
      System.out.println("FechaApertura: " + Celula.getFechaApertura());
    }
    return celulas;
  }
}
