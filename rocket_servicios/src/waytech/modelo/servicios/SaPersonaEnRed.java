package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.PersonaEnRed;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.beans.sgi.PersonaEnRedInsert;
import waytech.modelo.beans.sgi.Red;
import waytech.modelo.interfaces.IsaPersona;
import waytech.modelo.interfaces.IsaPersonaEnRed;
import waytech.modelo.interfaces.IsaRed;
import waytech.utilidades.UtilidadSistema;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaPersonaEnRed implements IsaPersonaEnRed {

  UtilidadSistema utilidadSistema = new UtilidadSistema();
  IsaPersona isaPersona = new SaPersona();
  IsaRed isaRed = new SaRed();

  private PersonaEnRed rsPersonaEnRed(ResultSet rs, PersonaEnRed personaEnPersonaEnRed) throws SQLException {
    personaEnPersonaEnRed.setIdPersonaEnRed(rs.getInt("id_persona_en_red"));
    Red red = new Red();
    red = isaRed.getRedPorIdRed((rs.getInt("id_red"))).getRed();
    personaEnPersonaEnRed.setIdRed(red);
    Persona persona = new Persona();
    persona.setIdPersona(rs.getInt("id_persona"));
    persona = isaPersona.getLiderLanzadoPorIdPersona(rs.getInt("id_persona")).getPersona();
    personaEnPersonaEnRed.setIdPersona(persona);
    personaEnPersonaEnRed.setEsLiderRed(rs.getBoolean("es_lider_red"));
    personaEnPersonaEnRed.setTraza(rs.getString("traza"));
    personaEnPersonaEnRed.setEstado(rs.getShort("estado"));
    return personaEnPersonaEnRed;
  }

  @Override
  public RspPersonaEnRed getPersonaEnRedPorIdPersonaEnRed(int idPersonaEnRed) {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspPersonaEnRed rspPersonaEnRed = new RspPersonaEnRed();
    //INICIALIZAR VARIABLES
    rspPersonaEnRed.setEsConexionAbiertaExitosamente(false);
    rspPersonaEnRed.setEsConexionCerradaExitosamente(false);
    rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPersonaEnRed.setEsConexionAbiertaExitosamente(true);
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM persona_en_red WHERE estado = 1 AND id_persona_en_red = '" + idPersonaEnRed + "'";
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPersonaEnRedPorIdPersonaEnRed(int idPersonaEnRed)", this.getClass().toString()));
          if (rs.next()) {
            PersonaEnRed personaEnRed = new PersonaEnRed();
            personaEnRed = rsPersonaEnRed(rs, personaEnRed);
            rspPersonaEnRed.setPersonaEnRed(personaEnRed);
          }
        }
      } catch (SQLException e) {
        rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPersonaEnRedPorIdPersonaEnRed(int idPersonaEnRed)", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspPersonaEnRed.setEsConexionCerradaExitosamente(true);
        }
        rspPersonaEnRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPersonaEnRed;
      }
    } else {
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPersonaEnRed;
    }
  }

  @Override
  public RspPersonaEnRed listPersonaEnRed() {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspPersonaEnRed rspPersonaEnRed = new RspPersonaEnRed();
    List<PersonaEnRed> todosLosPersonaEnReds = new ArrayList<PersonaEnRed>();
    //INICIALIZAR VARIABLES
    rspPersonaEnRed.setEsConexionAbiertaExitosamente(false);
    rspPersonaEnRed.setEsConexionCerradaExitosamente(false);
    rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPersonaEnRed.setEsConexionAbiertaExitosamente(true);
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM persona_en_red WHERE estado = 1";
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaEnRed()", this.getClass().toString()));
          while (rs.next()) {
            PersonaEnRed personaEnRed = new PersonaEnRed();
            personaEnRed = rsPersonaEnRed(rs, personaEnRed);
            todosLosPersonaEnReds.add(personaEnRed);
          }
        }
      } catch (SQLException e) {
        rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaEnRed()", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspPersonaEnRed.setEsConexionCerradaExitosamente(true);
        }
        rspPersonaEnRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        rspPersonaEnRed.setAllPersonaEnReds(todosLosPersonaEnReds);
        return rspPersonaEnRed;
      }
    } else {
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPersonaEnRed;
    }
  }

  @Override
  public RspPersonaEnRed updatePersonaEnRed(PersonaEnRed personaEnRed) {
    RspPersonaEnRed rspPersonaEnRed = new RspPersonaEnRed();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPersonaEnRed.setEsConexionAbiertaExitosamente(false);
    rspPersonaEnRed.setEsConexionCerradaExitosamente(false);
    rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPersonaEnRed.setEsRolledBackIntentado(false);
    rspPersonaEnRed.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPersonaEnRed.setEsConexionAbiertaExitosamente(true);
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE persona_en_red SET id_red = '" + personaEnRed.getIdRed().getIdRed() + "', id_persona = '" + personaEnRed.getIdPersona().getIdPersona() + "', es_lider_red = '" + personaEnRed.esLiderRed() + "',  WHERE id_personaEnRed = '" + personaEnRed.getIdPersonaEnRed() + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updatePersonaEnRed(PersonaEnRed personaEnRed)", this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updatePersonaEnRed(PersonaEnRed personaEnRed)", this.getClass().toString()));
        try {
          rspPersonaEnRed.setEsRolledBackIntentado(true);
          rspPersonaEnRed.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updatePersonaEnRed(PersonaEnRed personaEnRed)", this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPersonaEnRed.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updatePersonaEnRed(PersonaEnRed personaEnRed)", this.getClass().toString()));
          rspPersonaEnRed.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPersonaEnRed.esRolledBackIntentado()) {
          rspPersonaEnRed.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPersonaEnRed.setEsConexionCerradaExitosamente(true);
        }
        rspPersonaEnRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPersonaEnRed;
      }
    } else {
      rspPersonaEnRed.setEsRolledBackExitosamente(false);
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPersonaEnRed;
    }
  }

  @Override
  public RspPersonaEnRed insertPersonaEnRed(PersonaEnRedInsert personaEnRed) {
    RspPersonaEnRed rspPersonaEnRed = new RspPersonaEnRed();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPersonaEnRed.setEsConexionAbiertaExitosamente(false);
    rspPersonaEnRed.setEsConexionCerradaExitosamente(false);
    rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPersonaEnRed.setEsRolledBackIntentado(false);
    rspPersonaEnRed.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPersonaEnRed.setEsConexionAbiertaExitosamente(true);
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      String traza = utilidadSistema.generarTraza();
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "INSERT INTO persona_en_red ("
                + "id_persona_en_red,"
                + "id_red,"
                + "es_lider_red,"
                + "traza,"
                + "estado)"
                + " VALUES (?,?,?,?,?)";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        stmt.setInt(1, 0);
        stmt.setInt(2, personaEnRed.getIdRed());
        stmt.setInt(3, personaEnRed.getIdPersona());
        stmt.setBoolean(4, personaEnRed.esLiderRed());
        stmt.setString(5, traza);
        stmt.setShort(6, Short.valueOf("1"));
        rows = stmt.executeUpdate();
        stmt.close();
        rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "ingresarPersonaEnRed(PersonaEnRed personaEnRed)", this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "ingresarPersonaEnRed(PersonaEnRed personaEnRed)", this.getClass().toString()));
        try {
          rspPersonaEnRed.setEsRolledBackIntentado(true);
          rspPersonaEnRed.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "ingresarPersonaEnRed(PersonaEnRed personaEnRed)", this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPersonaEnRed.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "ingresarPersonaEnRed(PersonaEnRed personaEnRed)", this.getClass().toString()));
          rspPersonaEnRed.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPersonaEnRed.esRolledBackIntentado()) {
          rspPersonaEnRed.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPersonaEnRed.setEsConexionCerradaExitosamente(true);
          rspPersonaEnRed = getPersonaEnRedPorTraza(traza);
        }
        rspPersonaEnRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPersonaEnRed;
      }
    } else {
      rspPersonaEnRed.setEsRolledBackExitosamente(false);
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPersonaEnRed;
    }
  }

  @Override
  public RspPersonaEnRed deletePersonaEnRed(int idPersonaEnRed) {
    RspPersonaEnRed rspPersonaEnRed = new RspPersonaEnRed();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspPersonaEnRed.setEsConexionAbiertaExitosamente(false);
    rspPersonaEnRed.setEsConexionCerradaExitosamente(false);
    rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspPersonaEnRed.setEsRolledBackIntentado(false);
    rspPersonaEnRed.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPersonaEnRed.setEsConexionAbiertaExitosamente(true);
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE persona_en_red SET estado = '0' WHERE id_persona_en_red = '" + idPersonaEnRed + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deletePersonaEnRed(int idPersonaEnRed)", this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deletePersonaEnRed(int idPersonaEnRed)", this.getClass().toString()));
        try {
          rspPersonaEnRed.setEsRolledBackIntentado(true);
          rspPersonaEnRed.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deletePersonaEnRed(int idPersonaEnRed)", this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspPersonaEnRed.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deletePersonaEnRed(int idPersonaEnRed)", this.getClass().toString()));
          rspPersonaEnRed.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspPersonaEnRed.esRolledBackIntentado()) {
          rspPersonaEnRed.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspPersonaEnRed.setEsConexionCerradaExitosamente(true);
        }
        rspPersonaEnRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPersonaEnRed;
      }
    } else {
      rspPersonaEnRed.setEsRolledBackExitosamente(false);
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPersonaEnRed;
    }
  }

  /**
   * lista todos los líderes lanzados de una red específica
   * @param idRed
   * @return 
   */
  @Override
  public RspPersonaEnRed listLideresLanzados(int idRed) {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspPersonaEnRed rspPersonaEnRed = new RspPersonaEnRed();
    List<PersonaEnRed> todosLosPersonaEnReds = new ArrayList<PersonaEnRed>();
    //INICIALIZAR VARIABLES
    rspPersonaEnRed.setEsConexionAbiertaExitosamente(false);
    rspPersonaEnRed.setEsConexionCerradaExitosamente(false);
    rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPersonaEnRed.setEsConexionAbiertaExitosamente(true);
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM persona"
              + " WHERE estado = 1"
              + " AND es_lider_lanzado = 1"
              + " AND id_red = '" + idRed + "'";
      System.out.println("SaPersonaEnRed.listLideresLanzados(" + idRed + ")");
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listLideresLanzados(int idRed)", this.getClass().toString()));
          while (rs.next()) {
            PersonaEnRed personaEnRed = new PersonaEnRed();
            personaEnRed = rsPersonaEnRed(rs, personaEnRed);
            todosLosPersonaEnReds.add(personaEnRed);
          }
        }
      System.out.println("SaPersonaEnRed.listLideresLanzados(" + idRed + ")=nroItems="+todosLosPersonaEnReds.size());
      } catch (SQLException e) {
        rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listLideresLanzados(int idRed)", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspPersonaEnRed.setEsConexionCerradaExitosamente(true);
        }
        rspPersonaEnRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        rspPersonaEnRed.setAllPersonaEnReds(todosLosPersonaEnReds);
        return rspPersonaEnRed;
      }
    } else {
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPersonaEnRed;
    }
  }

  @Override
  public RspPersonaEnRed getPersonaEnRedPorTraza(String traza) {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspPersonaEnRed rspPersonaEnRed = new RspPersonaEnRed();
    //INICIALIZAR VARIABLES
    rspPersonaEnRed.setEsConexionAbiertaExitosamente(false);
    rspPersonaEnRed.setEsConexionCerradaExitosamente(false);
    rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspPersonaEnRed.setEsConexionAbiertaExitosamente(true);
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM persona_en_red WHERE estado = 1 AND traza = '" + traza + "'";
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspPersonaEnRed.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPersonaEnRedPorIdPersonaEnRed(int idPersonaEnRed)", this.getClass().toString()));
          if (rs.next()) {
            PersonaEnRed personaEnRed = new PersonaEnRed();
            personaEnRed = rsPersonaEnRed(rs, personaEnRed);
            rspPersonaEnRed.setPersonaEnRed(personaEnRed);
          }
        }
      } catch (SQLException e) {
        rspPersonaEnRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPersonaEnRedPorIdPersonaEnRed(int idPersonaEnRed)", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspPersonaEnRed.setEsConexionCerradaExitosamente(true);
        }
        rspPersonaEnRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspPersonaEnRed;
      }
    } else {
      rspPersonaEnRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspPersonaEnRed;
    }
  }
}
