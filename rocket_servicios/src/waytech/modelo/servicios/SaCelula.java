package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Celula;
import waytech.modelo.beans.sgi.CelulaInsert;
import waytech.modelo.beans.sgi.CelulaUpdate;
import waytech.modelo.beans.sgi.Red;
import waytech.modelo.beans.sgi.Zona;
import waytech.modelo.interfaces.IsaCelula;
import waytech.modelo.interfaces.IsaRed;
import waytech.modelo.interfaces.IsaZona;
import waytech.utilidades.UtilidadSistema;

/**
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @version 1.4 Jueves   25/11/2011 10:47 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public class SaCelula implements IsaCelula {

  UtilidadSistema utilidadSistema = new UtilidadSistema();
  IsaRed isaRed = new SaRed();
  IsaZona isaZona = new SaZona();

  private Celula rsCelula(ResultSet rs, Celula celula) throws SQLException {
    celula.setIdCelula(rs.getInt("id_celula"));
    Red red = new Red();
    red = isaRed.getRedPorIdRed(rs.getInt("id_red")).getRed();
    celula.setRed(red);
    celula.setCodigo(rs.getString("codigo"));
    celula.setNombre(rs.getString("nombre"));
    celula.setAnfitrion(rs.getString("anfitrion"));
    celula.setDireccion(rs.getString("direccion"));
    celula.setTraza(rs.getString("traza"));
    celula.setEstado(rs.getShort("estado"));
    celula.setDia(rs.getInt("dia"));
    celula.setHora(rs.getInt("hora"));
    celula.setTelefono(rs.getString("telefono"));
    celula.setFechaApertura(rs.getString("fecha_apertura"));
    System.out.println("SaCelula.rsCelula.fechaApertura=" + celula.getFechaApertura());
    celula.setObservaciones(rs.getString("observaciones"));
    Zona zona = new Zona();
    zona = isaZona.getZonaPorIdZona(rs.getInt("id_zona")).getZona();
    celula.setZona(zona);
    return celula;
  }

  @Override
  public RspCelula getCelulaPorIdCelula(int idCelula) {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspCelula rspCelula = new RspCelula();
    //INICIALIZAR VARIABLES
    rspCelula.setEsConexionAbiertaExitosamente(false);
    rspCelula.setEsConexionCerradaExitosamente(false);
    rspCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspCelula.setEsConexionAbiertaExitosamente(true);
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM celula WHERE estado = 1 AND id_celula= '" + idCelula + "'";
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getCelulaPorIdCelula(int idCelula)", this.getClass().toString()));
          if (rs.next()) {
            Celula celula = new Celula();
            celula = rsCelula(rs, celula);
            rspCelula.setCelula(celula);
          }
        }
      } catch (SQLException e) {
        rspCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getCelulaPorIdCelula(int idCelula)", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspCelula.setEsConexionCerradaExitosamente(true);
        }
        rspCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspCelula;
      }
    } else {
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspCelula;
    }
  }

  @Override
  public RspCelula esCodigoCelulaExistente(String codigo) {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspCelula rspCelula = new RspCelula();
    //INICIALIZAR VARIABLES
    rspCelula.setEsConexionAbiertaExitosamente(false);
    rspCelula.setEsConexionCerradaExitosamente(false);
    rspCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspCelula.setEsConexionAbiertaExitosamente(true);
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM celula WHERE estado = 1 AND codigo = '" + codigo + "'";
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "esCodigoCelulaExistente(String codigo)", this.getClass().toString()));
          if (rs.next()) {
            rspCelula.setEsCodigoCelulaExistente(true);
          }
        }
      } catch (SQLException e) {
        rspCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "esCodigoCelulaExistente(String codigo)", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspCelula.setEsConexionCerradaExitosamente(true);
        }
        rspCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspCelula;
      }
    } else {
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspCelula;
    }
  }

  @Override
  public RspCelula updateCelula(CelulaUpdate celula) {
    RspCelula rspCelula = new RspCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspCelula.setEsConexionAbiertaExitosamente(false);
    rspCelula.setEsConexionCerradaExitosamente(false);
    rspCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspCelula.setEsRolledBackIntentado(false);
    rspCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspCelula.setEsConexionAbiertaExitosamente(true);
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        if (celula.getFechaApertura().isEmpty() || celula.getFechaApertura() == null) {
          celula.setFechaApertura("1900-01-01 00:00:00");
        }
        String consultaSQL = "UPDATE celula SET codigo = '" + celula.getCodigo() + "', "
                + " anfitrion = '" + celula.getAnfitrion() + "', "
                + " codigo = '" + celula.getCodigo() + "', "
                + " direccion = '" + celula.getDireccion() + "', "
                + " nombre = '" + celula.getNombre() + "', "
                + " observaciones = '" + celula.getObservaciones() + "', "
                + " telefono = '" + celula.getTelefono() + "', "
                + " dia = '" + celula.getDia() + "', "
                + " hora = '" + celula.getHora() + "', "
                + " id_zona = '" + celula.getIdZona() + "', "
                + " id_red = '" + celula.getIdRed() + "', "
                + " fecha_apertura = '" + celula.getFechaApertura() + "' "
                + "WHERE id_celula = '" + celula.getIdCelula() + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateCelula(CelulaUpdate celula)", this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateCelula(CelulaUpdate celula)", this.getClass().toString()));
        try {
          rspCelula.setEsRolledBackIntentado(true);
          rspCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateCelula(CelulaUpdate celula)", this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateCelula(CelulaUpdate celula)", this.getClass().toString()));
          rspCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspCelula.esRolledBackIntentado()) {
          rspCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspCelula.setEsConexionCerradaExitosamente(true);
        }
        rspCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspCelula;
      }
    } else {
      rspCelula.setEsRolledBackExitosamente(false);
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspCelula;
    }
  }

  @Override
  public RspCelula insertCelula(CelulaInsert celula) {
    RspCelula rspCelula = new RspCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspCelula.setEsConexionAbiertaExitosamente(false);
    rspCelula.setEsConexionCerradaExitosamente(false);
    rspCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspCelula.setEsRolledBackIntentado(false);
    rspCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspCelula.setEsConexionAbiertaExitosamente(true);
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      String traza = utilidadSistema.generarTraza();
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "INSERT INTO celula ("
                + "id_celula,"
                + "id_red,"
                + "codigo,"
                + "nombre,"
                + "anfitrion,"
                + "direccion,"
                + "traza,"
                + "estado,"
                + "dia,"
                + "hora,"
                + "telefono,"
                + "observaciones,"
                + "id_zona,"
                + "fecha_apertura)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        stmt.setInt(1, 0);
        stmt.setInt(2, celula.getIdRed());
        stmt.setString(3, celula.getCodigo());
        stmt.setString(4, celula.getNombre());
        stmt.setString(5, celula.getAnfitrion());
        stmt.setString(6, celula.getDireccion());
        stmt.setString(7, traza);
        stmt.setShort(8, Short.valueOf("1"));
        stmt.setInt(9, celula.getDia());
        stmt.setInt(10, celula.getHora());
        stmt.setString(11, celula.getTelefono());
        stmt.setString(12, celula.getObservaciones());
        stmt.setInt(13, celula.getIdZona());
        if (celula.getFechaApertura().isEmpty() || celula.getFechaApertura() == null) {
          celula.setFechaApertura("1900-01-01 00:00:00");
        }
        stmt.setString(14, celula.getFechaApertura());
        rows = stmt.executeUpdate();
        stmt.close();
        rspCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertCelula(CelulaInsert celula)", this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertCelula(CelulaInsert celula)", this.getClass().toString()));
        try {
          rspCelula.setEsRolledBackIntentado(true);
          rspCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertCelula(CelulaInsert celula)", this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertCelula(CelulaInsert celula)", this.getClass().toString()));
          rspCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspCelula.esRolledBackIntentado()) {
          rspCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspCelula.setEsConexionCerradaExitosamente(true);
          rspCelula.setCelula(getCelulaTraza(traza).getCelula());
        }
        rspCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspCelula;
      }
    } else {
      rspCelula.setEsRolledBackExitosamente(false);
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspCelula;
    }
  }

  @Override
  public RspCelula deleteCelulaLogicamente(int idCelula) {
    RspCelula rspCelula = new RspCelula();
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    rspCelula.setEsConexionAbiertaExitosamente(false);
    rspCelula.setEsConexionCerradaExitosamente(false);
    rspCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
    rspCelula.setEsRolledBackIntentado(false);
    rspCelula.setEsRolledBackExitosamente(true);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspCelula.setEsConexionAbiertaExitosamente(true);
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      int rows;
      PreparedStatement stmt = null;
      try {
        conectorBD.getConnection().setAutoCommit(false);
        String consultaSQL = "UPDATE celula SET estado = '0' WHERE id_celula = '" + idCelula + "'";
        stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
        rows = stmt.executeUpdate();
        stmt.close();
        rspCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deleteCelulaLogicamente(int idCelula)", this.getClass().toString()));
        conectorBD.getConnection().commit();
      } catch (Exception e) {
        rspCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deleteCelulaLogicamente(int idCelula)", this.getClass().toString()));
        try {
          rspCelula.setEsRolledBackIntentado(true);
          rspCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deleteCelulaLogicamente(int idCelula)", this.getClass().toString()));
          conectorBD.getConnection().rollback();
        } catch (SQLException se2) {
          rspCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deleteCelulaLogicamente(int idCelula)", this.getClass().toString()));
          rspCelula.setEsRolledBackExitosamente(false);
        }
      } finally {
        if (!rspCelula.esRolledBackIntentado()) {
          rspCelula.setEsRolledBackExitosamente(false);
        }
        if (conectorBD.cerrarConexion()) {
          rspCelula.setEsConexionCerradaExitosamente(true);
        }
        rspCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspCelula;
      }
    } else {
      rspCelula.setEsRolledBackExitosamente(false);
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspCelula;
    }
  }

  @Override
  public RspCelula listCelula() {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspCelula rspCelula = new RspCelula();
    List<Celula> todosLosCelulas = new ArrayList<Celula>();
    //INICIALIZAR VARIABLES
    rspCelula.setEsConexionAbiertaExitosamente(false);
    rspCelula.setEsConexionCerradaExitosamente(false);
    rspCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspCelula.setEsConexionAbiertaExitosamente(true);
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM celula WHERE estado = 1";
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listCelula()", this.getClass().toString()));
          while (rs.next()) {
            Celula celula = new Celula();
            celula = rsCelula(rs, celula);
            todosLosCelulas.add(celula);
          }
        }
      } catch (SQLException e) {
        rspCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listCelula()", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspCelula.setEsConexionCerradaExitosamente(true);
        }
        rspCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        rspCelula.setTodosLosCelulas(todosLosCelulas);
        return rspCelula;
      }
    } else {
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspCelula;
    }
  }

  @Override
  public RspCelula esCelulaConIntegrantes(int idCelula) {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspCelula rspCelula = new RspCelula();
    //INICIALIZAR VARIABLES
    rspCelula.setEsConexionAbiertaExitosamente(false);
    rspCelula.setEsConexionCerradaExitosamente(false);
    rspCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspCelula.setEsConexionAbiertaExitosamente(true);
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM persona_en_celula WHERE estado = 1 AND id_celula = '" + idCelula + "'";
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "esCelulaConIntegrantes(int idCelula)", this.getClass().toString()));
          if (rs.next()) {
            rspCelula.setEsCelulaConIntegrantes(true);
          }
        }
      } catch (SQLException e) {
        rspCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "esCelulaConIntegrantes(int idCelula)", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspCelula.setEsConexionCerradaExitosamente(true);
        }
        rspCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspCelula;
      }
    } else {
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspCelula;
    }
  }

  @Override
  public RspCelula getCelulaTraza(String traza) {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspCelula rspCelula = new RspCelula();
    //INICIALIZAR VARIABLES
    rspCelula.setEsConexionAbiertaExitosamente(false);
    rspCelula.setEsConexionCerradaExitosamente(false);
    rspCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspCelula.setEsConexionAbiertaExitosamente(true);
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM celula WHERE estado = 1 AND traza= '" + traza + "'";
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getCelulaTraza(String traza)", this.getClass().toString()));
          if (rs.next()) {
            Celula celula = new Celula();
            celula = rsCelula(rs, celula);
            rspCelula.setCelula(celula);
          }
        }
      } catch (SQLException e) {
        rspCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getCelulaTraza(String traza)", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspCelula.setEsConexionCerradaExitosamente(true);
        }
        rspCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspCelula;
      }
    } else {
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspCelula;
    }
  }

  @Override
  public RspCelula getNumeroLideresCelula(int idCelula) {
    //INSTANCIAS DE LAS CLASES                
    ConectorBDMySQL conectorBD = new ConectorBDMySQL();
    RspCelula rspCelula = new RspCelula();
    //INICIALIZAR VARIABLES
    rspCelula.setEsConexionAbiertaExitosamente(false);
    rspCelula.setEsConexionCerradaExitosamente(false);
    rspCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
    //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
    if (conectorBD.iniciarConexion()) {
      rspCelula.setEsConexionAbiertaExitosamente(true);
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      String consultaSQL = "SELECT * FROM persona_en_celula WHERE estado = 1 AND es_lider_celula = TRUE AND id_celula = '" + idCelula + "'";
      int i = 0;
      try {
        Statement sentencia = conectorBD.getConnection().createStatement();
        boolean bandera = sentencia.execute(consultaSQL);
        if (bandera) {
          ResultSet rs = sentencia.getResultSet();
          rspCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
          rspCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getNumeroLideresCelula(int idCelula)", this.getClass().toString()));
          while (rs.next()) {
            i++;
          }
        }
      } catch (SQLException e) {
        rspCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getNumeroLideresCelula(int idCelula)", this.getClass().toString()));
      } finally {
        if (conectorBD.cerrarConexion()) {
          rspCelula.setEsConexionCerradaExitosamente(true);
          rspCelula.setNumeroDeLideresCelula(i);
        }
        rspCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
        return rspCelula;
      }
    } else {
      rspCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
      return rspCelula;
    }
  }
}