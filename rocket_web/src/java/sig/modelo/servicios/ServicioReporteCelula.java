/*
 * Capa que usa los servicios
 * para que los controladores accedan a la base de datos
 * de una manera más sencilla
 */
package sig.modelo.servicios;

import cdo.sgd.modelo.bd.simulador.Direccion;
import cdo.sgd.modelo.bd.simulador.ReporteCelula;
import cdo.sgd.modelo.bd.util.CelulaUtil;
import cdo.sgd.modelo.bd.util.ReporteCelulaListadoUtil;
import cdo.sgd.modelo.bd.util.ReporteCelulaUtil;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.Celula;
import waytech.modelo.beans.sgi.EjecucionCelula;
import waytech.modelo.beans.sgi.PersonaEnCelula;
import waytech.modelo.beans.sgi.PlanificacionCelula;
import waytech.modelo.beans.sgi.Zona;
import waytech.modelo.servicios.RspCelula;
import waytech.modelo.servicios.RspEjecucionCelula;
import waytech.modelo.servicios.RspPlanificacionCelula;
import waytech.modelo.servicios.SaCelula;
import waytech.modelo.servicios.SaEjecucionCelula;
import waytech.modelo.servicios.SaPersonaEnCelula;
import waytech.modelo.servicios.SaPlanificacionCelula;

/**
 *
 * @author Gabriel
 */
public class ServicioReporteCelula {

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
    RspCelula rspCelula = saCelula.listCelulaActivaOrdenadasPorEstatus();
    celulasReportes = rspCelula.getTodosLosCelulas();

    int n = 0;
    for (Celula celulaBD : celulasReportes) {
      ReporteCelulaListadoUtil celulaListado = new ReporteCelulaListadoUtil();
      idCelula = celulaBD.getIdCelula();
      //**System.out.println("ServicioCelula.getCelulasListado.celulaBD.id=" + idCelula);
      celulaListado.setIdCelula(idCelula);
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

  /**
   * busca en la base de datos, los datos del reporte de una célula
   * pasando como parámetro por el id proporcionado
   * devuelve los datos en un objeto ReporteCelulaUtil
   * @param idCelula
   * @return ReporteCelula reporte de celula, o un objeto vacío si no hay ejecución o planificación
   */
  public ReporteCelulaUtil getReporteCelula(int idCelula) {

    //traer datos de planificación
    //TODO: arreglar >> cambiar método a: getEjecucionCelulaSemanaActual(idCelula)
    RspPlanificacionCelula respuestaPlanificacion = saPlanificacionCelula.getPlanificacionCelulaPorIdCelula(idCelula);
    PlanificacionCelula planificacionCelulaBD = respuestaPlanificacion.getPlanificacionCelula();
    if (planificacionCelulaBD == null) {
      System.out.println("Error en ServicioReporteCelula.getReporteCelula:planificacionCelulaBD = NULL");
      return new ReporteCelulaUtil();//error, objeto vacío
    }
    System.out.println("ServicioReporteCelula.getReporteCelula.ejecucionCelulaBD:" + planificacionCelulaBD.toString());

    //traer datos de ejecución (resultados)
    RspEjecucionCelula respuestaEjecucion = saEjecucionCelula.getEjecucionCelulaPorIdEjecucionCelula(1);
    EjecucionCelula ejecucionCelulaBD = respuestaEjecucion.getEjecucionCelula();
    if (ejecucionCelulaBD == null) {
      System.out.println("Error en ServicioReporteCelula.getReporteCelula:ejecucionCelulaBD = NULL");
      return new ReporteCelulaUtil();//error, objeto vacío
    }
    System.out.println("ServicioReporteCelula.getReporteCelula.ejecucionCelulaBD:" + ejecucionCelulaBD.toString());
    ReporteCelulaUtil reporteCelulaUtil = generarReporteCelulaUtil(idCelula, planificacionCelulaBD, ejecucionCelulaBD);
    return reporteCelulaUtil;
  }

  //TODO: agregar 1 parámetro de tipo 'PlanificacionCelula' a este  método:
  ReporteCelulaUtil generarReporteCelulaUtil(int idCelula, PlanificacionCelula planifCelula, EjecucionCelula ejecucionCelula) {
    ReporteCelulaUtil reporte = new ReporteCelulaUtil();

    //datos básicos de la célula
    //-int idCelula = celula.getIdCelula();
    reporte.setIdCelula(idCelula);

    //ofrendas sin comprobación:
    reporte.setOfrendasMonto(ejecucionCelula.getOfrenda());
    reporte.setOfrendasEntregadas(false);

    //resultados:
    reporte.setResultadoInvitados(ejecucionCelula.getNuevosInvitados());
    reporte.setResultadoConvertidos(ejecucionCelula.getConvertidos());
    reporte.setResultadoReconciliados(ejecucionCelula.getReconciliados());
    reporte.setResultadoVisitas(ejecucionCelula.getVisitas());
    reporte.setResultadoAmigosSoloAsistenCelula(ejecucionCelula.getAmigosSoloAsistenGrupo());
    reporte.setResultadoCDO(ejecucionCelula.getIntegrantesCasaOracion());
    reporte.setResultadoOtrasIglesias(ejecucionCelula.getIntegrantesOtrasIglesias());
    reporte.setResultadoAsistenciaDomingoAnterior(ejecucionCelula.getAsistenciaDomingoAnterior());
    reporte.setEstatus(ejecucionCelula.getEstado());
    reporte.setObservaciones(ejecucionCelula.getObservaciones());

    reporte.setTotalAsistenciaCelula(
            ejecucionCelula.getNuevosInvitados()
            + ejecucionCelula.getAmigosSoloAsistenGrupo()
            + ejecucionCelula.getIntegrantesCasaOracion()
            + ejecucionCelula.getIntegrantesOtrasIglesias());

    //planificación
    reporte.setPlanificacionInvitados(planifCelula.getNuevosInvitados());
    reporte.setPlanificacionReconciliados(planifCelula.getReconciliados());
    reporte.setPlanificacionVisitas(planifCelula.getVisitas());
    reporte.setPlanificacionPersonasEnplanificacion(planifCelula.getNumeroIntegrantes());
    return reporte;
  }

  public int getIdCelula() {
    return idCelula;
  }

  public void setIdCelula(int idCelula) {
    this.idCelula = idCelula;
  }
  List<Celula> celulas = new ArrayList();
  List<Celula> celulasReportes = new ArrayList();
  List<String> nombres = new ArrayList();
  String nombreCelula = "";
  //tendrá los nombres de las Celulaes del 'estado elegido'
  List<String> nombreLideresCelula;
  List<PersonaEnCelula> lideresCelula;
  //TODO: OPTIMIZACION: sacar estas 2 líneas en todos los servicios
  SaCelula saCelula = new SaCelula();
  SaEjecucionCelula saEjecucionCelula = new SaEjecucionCelula();
  SaPlanificacionCelula saPlanificacionCelula = new SaPlanificacionCelula();
  SaPersonaEnCelula saPersonaCelula = new SaPersonaEnCelula();
  ServicioCelula servicioCelula = new ServicioCelula();
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
}
