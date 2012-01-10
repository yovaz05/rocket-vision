/*
 * Capa que usa los servicios relacionados a 'Reportes de Célula'
 * para que los controladores accedan a la base de datos
 * de una manera más sencilla
 */
package sig.modelo.servicios;

import cdo.sgd.modelo.bd.simulador.ReporteCelula;
import cdo.sgd.modelo.bd.util.ReporteCelulaListadoUtil;
import cdo.sgd.modelo.bd.util.ReporteCelulaUtil;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.Celula;
import waytech.modelo.beans.sgi.PersonaEnCelula;
import waytech.modelo.beans.sgi.EjecucionCelula;
import waytech.modelo.beans.sgi.PlanificacionCelula;
import waytech.modelo.servicios.RspCelula;
import waytech.modelo.servicios.RspEjecucionCelula;
import waytech.modelo.servicios.RspPlanificacionCelula;
import waytech.modelo.servicios.SaCelula;
import waytech.modelo.servicios.SaEjecucionCelula;
import waytech.modelo.servicios.SaPersonaEnCelula;
import waytech.modelo.servicios.SaPlanificacionCelula;
import waytech.utilidades.Util;

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
      celulaListado.setDireccionCorta(Util.generarDireccionCorta(ciudad, zona));
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
      celulaListado.setObservaciones(celulaBD.getObservaciones());
      i++;
      listadoCelulas.add(celulaListado);
    }
    return listadoCelulas;
  }

  /**
   * Ingresa una ejecución de célula en la base de datos
   * con sólo el id de la célula y el estado
   * @param idCelula el id de la célula
   * @param estado, 3: célula no realizada, 4: célula realizada
   * @return el id de la ejecución de célula insertada
   */
  public int ingresarReporteCelula(int idCelula, int status) {
    System.out.println("ServicioReporteCelula.ingresarReporteCelula. id célula=" + idCelula);
    System.out.println("ServicioReporteCelula.ingresarReporteCelula. estado=" + status);
    //crear ejecución
    RspEjecucionCelula respuesta = saEjecucionCelula.insertEjecucionCelula(idCelula, status);
    if (respuesta.esSentenciaSqlEjecutadaExitosamente()) {
      idReporteCelula = respuesta.getEjecucionCelula().getIdEjecucionCelula();
      //crear planificación:
//      RspPlanificacionCelula respuesta2 = saPlanificacionCelula.insertPlanificacionCelula(idCelula);
      if (!respuesta.esSentenciaSqlEjecutadaExitosamente()) {
        System.out.println("ERROR -> ServicioReporteCelula.ingresarReporteCelula.planificación = ERROR ingresando registro en planificación");
        idReporteCelula = 0;
        System.out.println(">>>" + respuesta.getRespuestaServicio());
      }
    } else {
      System.out.println("ServicioReporteCelula.ingresarReporteCelula.resultado=ERROR");
      idReporteCelula = 0;
    }
    return idReporteCelula;
  }

  /**
   * busca en la base de datos, los datos del reporte de una célula
   * pasando como parámetro por el id proporcionado
   * devuelve los datos en un objeto ReporteCelulaUtil
   * @param idCelula
   * @return ReporteCelula reporte de celula, o un objeto vacío si no hay ejecución o planificación
   */
  //TODO: agregar parámetro idSemana
  //mejorar gestión de errores
  //TODO: nro de semana actual, para pasar como parámetro o buscar dentro de este método
  public ReporteCelulaUtil getReporteCelulaSemanaActual(int idCelula) {
    EjecucionCelula ejecucionCelula;
    PlanificacionCelula planificacionCelula;

    //traer datos de ejecución (resultados)
    System.out.println("ServicioReporteCelula.getReporteCelulaSemanaActual.idCelula= " + idCelula);
    System.out.println("ServicioReporteCelula.getReporteCelulaSemanaActual.Semana= " + 1);

    RspEjecucionCelula respuestaEjecucion = saEjecucionCelula.getEjecucionCelula(idCelula, 1);
    ejecucionCelula = respuestaEjecucion.getEjecucionCelula();

    //**System.out.println("ServicioReporteCelula.respuestaEjecucion.getEjecucionCelula().estado=" + ejecucionCelulaBD.getEstado());

    if (respuestaEjecucion.getEjecucionCelula() == null) {
      System.out.println("Error -> ServicioReporteCelula.getReporteCelula: ejecucionCelulaBD == NULL");
      return new ReporteCelulaUtil();//error, objeto vacío
    }
    //**System.out.println("ServicioReporteCelula.getReporteCelulaSemanaActual.ejecucionCelulaBD:" + ejecucionCelulaBD.toString());

    System.out.println("ServicioReporteCelula.antes de IF");
    /**/
    if (ejecucionCelula.getEstado() == ReporteCelulaUtil.CELULA_NO_REALIZADA) {
      System.out.println("ServicioReporteCelula.respuestaEjecucion.getEjecucionCelula().estado=CELULA_NO_REALIZADA");
      //no hay datos que buscar, datos de planificación y ejecución en '0'
      ejecucionCelula = new EjecucionCelula();
      planificacionCelula = new PlanificacionCelula();
      return generarReporteCelulaUtil(idCelula, planificacionCelula, ejecucionCelula);
    }
    /**/

    //traer datos de planificación:
    RspPlanificacionCelula respuestaPlanificacion = saPlanificacionCelula.getPlanificacionCelulaPorIdCelula(idCelula);
    planificacionCelula = respuestaPlanificacion.getPlanificacionCelula();
    if (planificacionCelula == null) {
      System.out.println("Error en ServicioReporteCelula.getReporteCelula:planificacionCelulaBD = NULL");
      //+return new ReporteCelulaUtil();//error, objeto vacío
      planificacionCelula = new PlanificacionCelula();
    }
    //**System.out.println("ServicioReporteCelula.getReporteCelula.ejecucionCelulaBD:" + planificacionCelulaBD.toString());

    return generarReporteCelulaUtil(idCelula, planificacionCelula, ejecucionCelula);
  }

  public String getObservacionesReporte(int idCelula) {
    RspEjecucionCelula respuestaEjecucion = saEjecucionCelula.getEjecucionCelula(idCelula, 1);
    EjecucionCelula ejecucionCelula = respuestaEjecucion.getEjecucionCelula();
    System.out.println("ejecucion celula. " + ejecucionCelula.toString());
    return ejecucionCelula.getObservaciones();
  }

  ReporteCelulaUtil generarReporteCelulaUtil(int idCelula, PlanificacionCelula planificacion, EjecucionCelula ejecucion) {
    ReporteCelulaUtil reporte = new ReporteCelulaUtil();

    //datos básicos de la célula:
    reporte.setIdReporte(ejecucion.getIdEjecucionCelula());
    reporte.setIdCelula(idCelula);

    //planificación:
    reporte.setPlanificacionInvitados(planificacion.getNuevosInvitados());
    reporte.setPlanificacionReconciliados(planificacion.getReconciliados());
    reporte.setPlanificacionVisitas(planificacion.getVisitas());
    reporte.setPlanificacionPersonasEnplanificacion(planificacion.getNumeroIntegrantes());

    //resultados:
    reporte.setResultadoInvitados(ejecucion.getNuevosInvitados());
    reporte.setResultadoConvertidos(ejecucion.getConvertidos());
    reporte.setResultadoReconciliados(ejecucion.getReconciliados());
    reporte.setResultadoVisitas(ejecucion.getVisitas());
    reporte.setResultadoAmigosSoloAsistenCelula(ejecucion.getAmigosSoloAsistenGrupo());
    reporte.setResultadoCDO(ejecucion.getIntegrantesCasaOracion());
    reporte.setResultadoOtrasIglesias(ejecucion.getIntegrantesOtrasIglesias());
    reporte.setResultadoAsistenciaDomingoAnterior(ejecucion.getAsistenciaDomingoAnterior());
    reporte.setOfrendasMonto(ejecucion.getOfrenda());
    reporte.setEstatus(ejecucion.getEstado());
    reporte.setObservaciones(ejecucion.getObservaciones());

    reporte.setTotalAsistenciaCelula(
            ejecucion.getNuevosInvitados()
            + ejecucion.getAmigosSoloAsistenGrupo()
            + ejecucion.getIntegrantesCasaOracion()
            + ejecucion.getIntegrantesOtrasIglesias());

    //ofrendas (sin comprobación):
    reporte.setOfrendasMonto(ejecucion.getOfrenda());
    reporte.setOfrendasEntregadas(false);

    return reporte;
  }

  public boolean actualizarResultadoInvitados(int idReporteCelula, int valor) {
    RspEjecucionCelula respuesta = saEjecucionCelula.updateNumeroInvitados(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarResultadoConvertidos(int idReporteCelula, int valor) {
    RspEjecucionCelula respuesta = saEjecucionCelula.updateConvertidos(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarResultadoReconciliados(int idReporteCelula, int valor) {
    RspEjecucionCelula respuesta = saEjecucionCelula.updateReconciliados(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarResultadoVisitas(int idReporteCelula, int valor) {
    RspEjecucionCelula respuesta = saEjecucionCelula.updateVisitas(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarResultadoAmigos(int idReporteCelula, int valor) {
    RspEjecucionCelula respuesta = saEjecucionCelula.updateAmigosSoloAsistenGrupo(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarResultadoIntegrantesEstaIglesia(int idReporteCelula, int valor) {
    RspEjecucionCelula respuesta = saEjecucionCelula.updateIntegrantesCasaOracion(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarResultadoIntegrantesOtrasIglesias(int idReporteCelula, int valor) {
    RspEjecucionCelula respuesta = saEjecucionCelula.updateIntegrantesOtrasIglesias(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarResultadoAsistenciaDomingoAnterior(int idReporteCelula, int valor) {
    RspEjecucionCelula respuesta = saEjecucionCelula.updateAsistenciaDomingoAnterior(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarResultadoTotalAsistencia(int idReporteCelula, int valor) {
    RspEjecucionCelula respuesta = saEjecucionCelula.updateNumeroIntegrantes(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarResultadoOfrendas(int idReporteCelula, double valor) {
    RspEjecucionCelula respuesta = saEjecucionCelula.updateOfrenda(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarPlanificacionInvitados(int idReporteCelula, int valor) {
    RspPlanificacionCelula respuesta = saPlanificacionCelula.updateNuevosInvitados(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarPlanificacionReconciliados(int idReporteCelula, int valor) {
    RspPlanificacionCelula respuesta = saPlanificacionCelula.updateReconciliados(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarPlanificacionVisitas(int idReporteCelula, int valor) {
    RspPlanificacionCelula respuesta = saPlanificacionCelula.updateVisitas(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarPlanificacionNumeroIntegrantes(int idReporteCelula, int valor) {
    RspPlanificacionCelula respuesta = saPlanificacionCelula.updateNumeroIntegrantes(idReporteCelula, valor);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza las observaciones
   */
  public boolean actualizarObservaciones(int idReporteCelula, String observaciones) {
    RspEjecucionCelula respuesta = saEjecucionCelula.updateObservaciones(idReporteCelula, observaciones);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }
  /**
   * atributos
   */
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
  //id del reporte que se está trabajando actualmente, servirá para EjecucionCelula y PlanificacionCelula
  //TODO: buscar este valor
  int idReporteCelula = 1;
}
