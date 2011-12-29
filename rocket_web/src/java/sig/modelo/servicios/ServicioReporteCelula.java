/*
 * Capa que usa los servicios
 * para que los controladores accedan a la base de datos
 * de una manera más sencilla
 */
package sig.modelo.servicios;

import cdo.sgd.modelo.bd.simulador.ReporteCelula;
import cdo.sgd.modelo.bd.util.ReporteCelulaListadoUtil;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.Celula;
import waytech.modelo.beans.sgi.PersonaEnCelula;
import waytech.modelo.servicios.RspCelula;
import waytech.modelo.servicios.SaCelula;
import waytech.modelo.servicios.SaPersonaEnCelula;

/**
 *
 * @author Gabriel
 */
public class ServicioReporteCelula {

  List<Celula> celulas = new ArrayList();
  List<String> nombres = new ArrayList();
  String nombreCelula = "";
  //tendrá los nombres de las Celulaes del 'estado elegido'
  List<String> nombreLideresCelula;
  List<PersonaEnCelula> lideresCelula;
  //TODO: OPTIMIZACION: sacar estas 2 líneas en todos los servicios
  SaCelula saCelula = new SaCelula();
  SaPersonaEnCelula saPersonaCelula = new SaPersonaEnCelula();
  List<ReporteCelula> listaReporteCelula = new ArrayList();
  List<ReporteCelulaListadoUtil> listadoCelulas = new ArrayList();
  String nombreLider1 = "";
  String nombreLider2 = "";
  String nombreLider3 = "";
  String nombreLider4 = "";
  int idLider1 = 0;
  int idLider2 = 0;
  int idLider3 = 0;
  int idLider4 = 0;
  int nLideresUsados = 1;
  //TODO: usar este id para las modificaciones
  int idCelula = 0;

  public List getAll() {
    RspCelula rspCelula = saCelula.listCelulaActiva();
    System.out.println("INICIO DE LA CONEXION " + rspCelula.getRespuestaInicioDeConexion());
    celulas = rspCelula.getTodosLosCelulas();
    if (celulas == null) {
      System.out.println("ServicioCelula. rspCelula.getTodosLosCelulas(). devuelve null");
    }
    System.out.println("CIERRE DE LA CONEXION " + rspCelula.getRespuestaInicioDeConexion());
    return celulas;
  }

  public List getNombres() {
    getAll();
    for (Celula Celula : celulas) {
      nombres.add(Celula.getNombre());
    }
    //**listarConsolaAll();
    return nombres;
  }

  /**
   * Convierte la lista de células a una lista de tipo listadoCelulas
   * tipo: 'Celula' a tipo 'CelulaListadoSimulador'
   * @return listado de células
   */
  //MEJORAR CODIGO CON generarReporteCelulaUtil
  public List<ReporteCelulaListadoUtil> getReporteCelulaListado() {
    getAll();
    int n = 0;
    for (Celula celulaBD : celulas) {
      ReporteCelulaListadoUtil celulaListado = new ReporteCelulaListadoUtil();
      idCelula = celulaBD.getIdCelula();
      //**System.out.println("ServicioCelula.getCelulasListado.celulaBD.id=" + idCelula);
      celulaListado.setId(idCelula);
      celulaListado.setNroItem(++n);
      String codigoCelula = celulaBD.getCodigo();
      celulaListado.setCodigo(codigoCelula);
      String ciudad = celulaBD.getZona().getIdCiudad().getNombre();
      String zona = celulaBD.getZona().getNombre();
      celulaListado.setDireccionCorta(generarDireccionCorta(ciudad, zona));
      //día
      if (celulaBD.getDia() == 0) {
        celulaListado.setDia("");
      } else {
        celulaListado.setDia("" + celulaBD.getDia());
      }
      //hora
      if (celulaBD.getHora() == 0) {
        celulaListado.setHora("");
      } else {
        celulaListado.setHora("" + celulaBD.getHora());
      }
      //otros datos:
      celulaListado.setAnfitrion(celulaBD.getAnfitrion());
      celulaListado.setFechaApertura(celulaBD.getFechaApertura());

      //líderes:
      List<PersonaEnCelula> listaPersonaCelula = new ArrayList<PersonaEnCelula>();
      listaPersonaCelula = saPersonaCelula.listLiderCelulaPorIdCelula(idCelula).getAllPersonaEnCelulas();

      int i = 0;

      for (PersonaEnCelula personaCelula : listaPersonaCelula) {
        i++;
        int idLider = personaCelula.getIdPersona().getIdPersona();
        String nombreLider = personaCelula.getIdPersona().getNombre();
        if (i == 1) {
          celulaListado.setIdLider1(idLider);
          celulaListado.setNombreLider1(nombreLider);
        } else if (i == 2) {
          celulaListado.setIdLider2(idLider);
          celulaListado.setNombreLider2(nombreLider);
        } else if (i == 3) {
          celulaListado.setIdLider3(idLider);
          celulaListado.setNombreLider3(nombreLider);
        } else if (i == 4) {
          celulaListado.setIdLider4(idLider);
          celulaListado.setNombreLider4(nombreLider);
        }
        celulaListado.setNumeroLideres(i);
      }
      celulaListado.setNombre(celulaBD.getNombre());
      celulaListado.setIdRed(celulaBD.getRed().getIdRed());
      celulaListado.setNombreRed(celulaBD.getRed().getNombre());
      celulaListado.setEstatus(celulaBD.getEstado());
      i++;
      listadoCelulas.add(celulaListado);
    }
    return listadoCelulas;
  }

  private String generarDireccionCorta(String ciudad, String zona) {
    if (zona.isEmpty()) {
      return "No asignada";
    }
    return zona + ", " + ciudad;
  }
}
