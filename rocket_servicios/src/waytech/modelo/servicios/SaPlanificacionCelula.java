package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Celula;
import waytech.modelo.beans.sgi.PlanificacionCelula;
import waytech.modelo.beans.sgi.PlanificacionCelulaInsert;
import waytech.modelo.beans.sgi.PlanificacionCelulaUpdate;
import waytech.modelo.beans.sgi.Semana;
import waytech.modelo.interfaces.IsaCelula;
import waytech.modelo.interfaces.IsaPlanificacionCelula;
import waytech.modelo.interfaces.IsaSemana;
import waytech.utilidades.UtilidadSistema;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaPlanificacionCelula implements IsaPlanificacionCelula {

  UtilidadSistema utilidadSistema = new UtilidadSistema();
  IsaCelula isaCelula = new SaCelula();
  IsaSemana isaSemana = new SaSemana();

  private PlanificacionCelula rsPlanificacionCelula(ResultSet rs, PlanificacionCelula planificacionCelula) throws SQLException {
    planificacionCelula.setIdPlanificacionCelula(rs.getInt("id_planificacion_celula"));
    Celula celula = new Celula();
    celula = isaCelula.getCelulaPorIdCelula(rs.getInt("id_celula")).getCelula();
    planificacionCelula.setIdCelula(celula);
    Semana semana = new Semana();
    semana = isaSemana.getSemanaPorIdSemana(rs.getInt("id_semana")).getSemana();
    planificacionCelula.setIdSemana(semana);
    planificacionCelula.setFecha(rs.getString("fecha"));
    planificacionCelula.setNuevosInvitados(rs.getInt("nuevos_invitados"));
    planificacionCelula.setReconciliados(rs.getInt("reconciliados"));
    planificacionCelula.setVisitas(rs.getInt("visitas"));
    planificacionCelula.setNumeroIntegrantes(rs.getInt("numero_integrantes"));
    planificacionCelula.setConvertidos(rs.getInt("convertidos"));
    planificacionCelula.setObservaciones(rs.getString("observaciones"));
    planificacionCelula.setEstado(rs.getShort("estado"));
    planificacionCelula.setTraza(rs.getString("traza"));
    return planificacionCelula;
  }

  @Override
  public RspPlanificacionCelula getPlanificacionCelulaPorIdPlanificacionCelula(int idPlanificacionCelula) {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    //INICIALIZAR VARIABLES
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM planificacion_celula WHERE estado = 1 AND id_planificacion_celula = '" + idPlanificacionCelula + "'";
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPlanificacionCelulaPorIdPlanificacionCelula(int idPlanificacionCelula)", this.getClass().toString()));
          if (rs.next()) {
            PlanificacionCelula PlanificacionCelula = new PlanificacionCelula();
            PlanificacionCelula = rsPlanificacionCelula(rs, PlanificacionCelula);
            rspPlanificacionCelula.setPlanificacionCelula(PlanificacionCelula);
          }
        }
      } catch (SQLException e) {
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPlanificacionCelulaPorIdPlanificacionCelula(int idPlanificacionCelula)", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula getPlanificacionCelulaPorIdCelula(int idCelula) {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    //INICIALIZAR VARIABLES
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM planificacion_celula"
              + " WHERE id_celula = '" + idCelula + "'";
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPlanificacionCelulaPorIdPlanificacionCelula(int idPlanificacionCelula)", this.getClass().toString()));
          if (rs.next()) {
            PlanificacionCelula PlanificacionCelula = new PlanificacionCelula();
            PlanificacionCelula = rsPlanificacionCelula(rs, PlanificacionCelula);
            rspPlanificacionCelula.setPlanificacionCelula(PlanificacionCelula);
          }
        }
      } catch (SQLException e) {
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPlanificacionCelulaPorIdPlanificacionCelula(int idPlanificacionCelula)", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula listPlanificacionCelula() {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    List<PlanificacionCelula> todosLosPlanificacionCelulas = new ArrayList<PlanificacionCelula>();
    //INICIALIZAR VARIABLES
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM planificacion_celula WHERE estado = 1";
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPlanificacionCelula()", this.getClass().toString()));
          while (rs.next()) {
            PlanificacionCelula PlanificacionCelula = new PlanificacionCelula();
            PlanificacionCelula = rsPlanificacionCelula(rs, PlanificacionCelula);
            todosLosPlanificacionCelulas.add(PlanificacionCelula);
          }
        }
      } catch (SQLException e) {
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPlanificacionCelula()", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        rspPlanificacionCelula.setTodosLosPlanificacionCelulas(todosLosPlanificacionCelulas);
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula updatePlanificacionCelula(PlanificacionCelulaUpdate planificacionCelula) {
    String metodo = "updatePlanificacionCelula(PlanificacionCelulaUpdate planificacionCelula)";
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE planificacion_celula SET fecha = '" + planificacionCelula.getFecha() + "', observaciones = '" + planificacionCelula.getObservaciones() + "', convertidos = '" + planificacionCelula.getConvertidos() + "', id_celula = '" + planificacionCelula.getIdCelula() + "', id_semana = '" + planificacionCelula.getIdSemana() + "', nuevos_invitados = '" + planificacionCelula.getNuevosInvitados() + "', numeros_integrantes = '" + planificacionCelula.getNumeroIntegrantes() + "', reconciliados= '" + planificacionCelula.getReconciliados() + "', visitas = '" + planificacionCelula.getVisitas() + "' WHERE id_planificacion_celula = '" + planificacionCelula.getIdPlanificacionCelula() + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula insertPlanificacionCelula(PlanificacionCelulaInsert planificacionCelula) {
    String metodo = "insertPlanificacionCelula(PlanificacionCelulaInsert planificacionCelula)";
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      String traza = utilidadSistema.generarTraza();
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "INSERT INTO planificacion_celula ("
                + "id_planificacion_celula,"
                + "id_celula,"
                + "id_semana,"
                + "fecha,"
                + "nuevos_invitados,"
                + "reconciliados,"
                + "visitas,"
                + "numero_integrantes,"
                + "convertidos,"
                + "observaciones,"
                + "traza,"
                + "estado)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        stmt.setInt(1, 0);
        stmt.setInt(2, planificacionCelula.getIdCelula());
        stmt.setInt(3, planificacionCelula.getIdSemana());
        stmt.setString(4, planificacionCelula.getFecha());
        stmt.setInt(5, planificacionCelula.getNuevosInvitados());
        stmt.setInt(6, planificacionCelula.getReconciliados());
        stmt.setInt(7, planificacionCelula.getVisitas());
        stmt.setInt(8, planificacionCelula.getNumeroIntegrantes());
        stmt.setInt(9, planificacionCelula.getConvertidos());
        stmt.setString(10, planificacionCelula.getObservaciones());
        stmt.setString(11, traza);
        stmt.setShort(12, Short.valueOf("1"));

        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
          rspPlanificacionCelula.setPlanificacionCelula(getPlanificacionCelulaPorTraza(traza).getPlanificacionCelula());
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula deletePlanificacionCelulaLogicamente(int idPlanificacionCelula) {
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE planificacion_celula SET estado = '0' WHERE id_planificacion_celula = '" + idPlanificacionCelula + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deletePlanificacionCelulaLogicamente(int idPlanificacionCelula)", this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deletePlanificacionCelulaLogicamente(int idPlanificacionCelula)", this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deletePlanificacionCelulaLogicamente(int idPlanificacionCelula)", this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deletePlanificacionCelulaLogicamente(int idPlanificacionCelula)", this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula getPlanificacionCelulaPorTraza(String traza) {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    //INICIALIZAR VARIABLES
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM planificacion_celula WHERE estado = 1 AND traza = '" + traza + "'";
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPlanificacionCelulaPorTraza(String traza)", this.getClass().toString()));
          if (rs.next()) {
            PlanificacionCelula PlanificacionCelula = new PlanificacionCelula();
            PlanificacionCelula = rsPlanificacionCelula(rs, PlanificacionCelula);
            rspPlanificacionCelula.setPlanificacionCelula(PlanificacionCelula);
          }
        }
      } catch (SQLException e) {
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPlanificacionCelulaPorTraza(String traza)", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  //TODO: agregar parámetro: idSemana
  @Override
  public RspPlanificacionCelula insertPlanificacionCelula(int idCelula) {
    String metodo = "insertPlanificacionCelulaDiaSemana(int idCelula)";
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      String traza = utilidadSistema.generarTraza();
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "INSERT INTO planificacion_celula ("
                + "id_planificacion_celula,"
                + "id_celula,"
                + "id_semana,"
                + "fecha,"
                + "nuevos_invitados,"
                + "reconciliados,"
                + "visitas,"
                + "numero_integrantes,"
                + "convertidos,"
                + "observaciones,"
                + "traza,"
                + "estado)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        stmt.setInt(1, 0);
        stmt.setInt(2, idCelula);
        stmt.setInt(3, 1);                //TODO: debe asignarse la semana actual
        stmt.setString(4, "1970-01-01");  //TODO: debe asignarse la fecha de hoy
        stmt.setInt(5, 0);
        stmt.setInt(6, 0);
        stmt.setInt(7, 0);
        stmt.setInt(8, 0);
        stmt.setInt(9, 0);
        stmt.setString(10, "NA");
        stmt.setString(11, traza);
        stmt.setShort(12, Short.valueOf("1"));

        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
          rspPlanificacionCelula.setPlanificacionCelula(getPlanificacionCelulaPorTraza(traza).getPlanificacionCelula());
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula updateIdCelula(int idPlanificacionCelula, int idCelula) {
    String metodo = "updateIdCelula(int idPlanificacionCelula, int idCelula)";
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE planificacion_celula SET id_celula = '" + idCelula + "' WHERE id_planificacion_celula = '" + idPlanificacionCelula + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula updateIdSemana(int idPlanificacionCelula, int idSemana) {
    String metodo = "updateIdSemana(int idPlanificacionCelula, int idSemana)";
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE planificacion_celula SET id_semana = '" + idSemana + "' WHERE id_planificacion_celula = '" + idPlanificacionCelula + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }

  }

  @Override
  public RspPlanificacionCelula updateFecha(int idPlanificacionCelula, String fecha) {
    String metodo = "updateFecha(int idPlanificacionCelula, String fecha)";
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE planificacion_celula SET fecha = '" + fecha + "' WHERE id_planificacion_celula = '" + idPlanificacionCelula + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula updateNuevosInvitados(int idPlanificacionCelula, int nuevosInvitados) {
    String metodo = "updateNuevosInvitados(int idPlanificacionCelula, int nuevosInvitados)";
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE planificacion_celula SET nuevos_invitados = '" + nuevosInvitados + "' WHERE id_planificacion_celula = '" + idPlanificacionCelula + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula updateReconciliados(int idPlanificacionCelula, int reconciliados) {
    String metodo = "updateReconciliados(int idPlanificacionCelula, int reconciliados)";
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE planificacion_celula SET reconciliados = '" + reconciliados + "' WHERE id_planificacion_celula = '" + idPlanificacionCelula + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula updateVisitas(int idPlanificacionCelula, int visitas) {
    String metodo = "updateVisitas(int idPlanificacionCelula, int visitas)";
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE planificacion_celula"
                + " SET visitas = '" + visitas + "'"
                + " WHERE id_planificacion_celula = '" + idPlanificacionCelula + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula updateNumeroIntegrantes(int idPlanificacionCelula, int numeroIntegrantes) {
    String metodo = "updateNumeroIntegrantes(int idPlanificacionCelula, int numeroIntegrantes)";
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE planificacion_celula SET numero_integrantes = '" + numeroIntegrantes + "' WHERE id_planificacion_celula = '" + idPlanificacionCelula + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula updateConvertidos(int idPlanificacionCelula, int convertidos) {
    String metodo = "updateConvertidos(int idPlanificacionCelula, int convertidos)";
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE planificacion_celula SET convertidos = '" + convertidos + "' WHERE id_planificacion_celula = '" + idPlanificacionCelula + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }

  @Override
  public RspPlanificacionCelula updateObservaciones(int idPlanificacionCelula, String observaciones) {
    String metodo = "updateObservaciones(int idPlanificacionCelula, String observaciones)";
    RspPlanificacionCelula rspPlanificacionCelula = new RspPlanificacionCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPlanificacionCelula.setEsConexionAbiertaExitosamente(false);
    rspPlanificacionCelula.setEsConexionCerradaExitosamente(false);
    rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPlanificacionCelula.setEsRolledBackIntentado(false);
    rspPlanificacionCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPlanificacionCelula.setEsConexionAbiertaExitosamente(true);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE planificacion_celula SET observaciones = '" + observaciones + "' WHERE id_planificacion_celula = '" + idPlanificacionCelula + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPlanificacionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPlanificacionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
        try {
          rspPlanificacionCelula.setEsRolledBackIntentado(true);
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPlanificacionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPlanificacionCelula.esRolledBackIntentado()) {
          rspPlanificacionCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPlanificacionCelula.setEsConexionCerradaExitosamente(true);
        }
        rspPlanificacionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPlanificacionCelula;
      }
    } else {
      rspPlanificacionCelula.setEsRolledBackExitosamente(false);
      rspPlanificacionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPlanificacionCelula;
    }
  }
}
