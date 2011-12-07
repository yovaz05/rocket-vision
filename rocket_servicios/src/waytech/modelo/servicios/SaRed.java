package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Red;
import waytech.modelo.beans.sgi.RedInsert;
import waytech.modelo.beans.sgi.RedUpdate;
import waytech.modelo.interfaces.IsaRed;
import waytech.utilidades.UtilidadSistema;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaRed implements IsaRed {

    UtilidadSistema utilidadSistema = new UtilidadSistema();

    private Red rsRed(ResultSet rs, Red red) throws SQLException {
        red.setIdRed(rs.getInt("id_red"));
        red.setNombre(rs.getString("nombre"));
        red.setDiaReunion(rs.getInt("dia_reunion"));
        red.setHoraReunion(rs.getInt("hora_reunion"));
        red.setEsMatrimonial(rs.getBoolean("es_matrimonial"));
        red.setEstado(rs.getShort("estado"));
        red.setTraza(rs.getString("traza"));
        return red;
    }

    @Override
    public RspRed getRedPorIdRed(int idRed) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspRed rspRed = new RspRed();
        //INICIALIZAR VARIABLES
        rspRed.setEsConexionAbiertaExitosamente(false);
        rspRed.setEsConexionCerradaExitosamente(false);
        rspRed.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspRed.setEsConexionAbiertaExitosamente(true);
            rspRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM red WHERE estado = 1 AND id_red = '" + idRed + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspRed.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getRedPorIdRed(int idRed)", this.getClass().toString()));
                    if (rs.next()) {
                        Red red = new Red();
                        red = rsRed(rs, red);
                        rspRed.setRed(red);
                    }
                }
            } catch (SQLException e) {
                rspRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getRedPorIdRed(int idRed)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspRed.setEsConexionCerradaExitosamente(true);
                }
                rspRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspRed;
            }
        } else {
            rspRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspRed;
        }
    }

    @Override
    public RspRed listRed() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspRed rspRed = new RspRed();
        List<Red> todosLosReds = new ArrayList<Red>();
        //INICIALIZAR VARIABLES
        rspRed.setEsConexionAbiertaExitosamente(false);
        rspRed.setEsConexionCerradaExitosamente(false);
        rspRed.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspRed.setEsConexionAbiertaExitosamente(true);
            rspRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM red WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspRed.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listRed()", this.getClass().toString()));
                    while (rs.next()) {
                        Red red = new Red();
                        red = rsRed(rs, red);
                        todosLosReds.add(red);
                    }
                }
            } catch (SQLException e) {
                rspRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listRed()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspRed.setEsConexionCerradaExitosamente(true);
                }
                rspRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspRed.setAllReds(todosLosReds);
                return rspRed;
            }
        } else {
            rspRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspRed;
        }
    }

    @Override
    public RspRed updateRed(RedUpdate red) {
        RspRed rspRed = new RspRed();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspRed.setEsConexionAbiertaExitosamente(false);
        rspRed.setEsConexionCerradaExitosamente(false);
        rspRed.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspRed.setEsRolledBackIntentado(false);
        rspRed.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspRed.setEsConexionAbiertaExitosamente(true);
            rspRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE red SET nombre = '" + red.getNombre() + "', dia_reunion = '" + red.getDiaReunion() + "', hora_reunion = '" + red.getHoraReunion() + "', es_matrimonial = '" + red.esMatrimonial() + "' WHERE id_red = '" + red.getIdRed() + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateRed(RedUpdate red)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspRed.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateRed(RedUpdate red)", this.getClass().toString()));
                try {
                    rspRed.setEsRolledBackIntentado(true);
                    rspRed.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateRed(RedUpdate red)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspRed.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateRed(RedUpdate red)", this.getClass().toString()));
                    rspRed.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspRed.esRolledBackIntentado()) {
                    rspRed.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspRed.setEsConexionCerradaExitosamente(true);
                }
                rspRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspRed;
            }
        } else {
            rspRed.setEsRolledBackExitosamente(false);
            rspRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspRed;
        }
    }

    @Override
    public RspRed insertRed(RedInsert red) {
        RspRed rspRed = new RspRed();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspRed.setEsConexionAbiertaExitosamente(false);
        rspRed.setEsConexionCerradaExitosamente(false);
        rspRed.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspRed.setEsRolledBackIntentado(false);
        rspRed.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspRed.setEsConexionAbiertaExitosamente(true);
            rspRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO red ("
                        + "id_red,"
                        + "nombre,"
                        + "dia_reunion,"
                        + "hora_reunion,"
                        + "es_matrimonial,"
                        + "estado,"
                        + "traza)"
                        + " VALUES (?,?,?,?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setString(2, red.getNombre());
                stmt.setInt(3, red.getDiaReunion());
                stmt.setInt(4, red.getHoraReunion());
                stmt.setBoolean(5, red.esMatrimonial());
                stmt.setShort(6, Short.valueOf("1"));
                stmt.setString(7, traza);
                rows = stmt.executeUpdate();
                stmt.close();
                rspRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertRed(RedInsert red)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspRed.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertRed(RedInsert red)", this.getClass().toString()));
                try {
                    rspRed.setEsRolledBackIntentado(true);
                    rspRed.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertRed(RedInsert red)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspRed.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertRed(RedInsert red)", this.getClass().toString()));
                    rspRed.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspRed.esRolledBackIntentado()) {
                    rspRed.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspRed.setEsConexionCerradaExitosamente(true);
                    rspRed.setRed(getRedPorTraza(traza).getRed());
                }
                rspRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspRed;
            }
        } else {
            rspRed.setEsRolledBackExitosamente(false);
            rspRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspRed;
        }
    }

    @Override
    public RspRed deleteRed(int idRed) {
        RspRed rspRed = new RspRed();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspRed.setEsConexionAbiertaExitosamente(false);
        rspRed.setEsConexionCerradaExitosamente(false);
        rspRed.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspRed.setEsRolledBackIntentado(false);
        rspRed.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspRed.setEsConexionAbiertaExitosamente(true);
            rspRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE red SET estado = '0' WHERE id_red = '" + idRed + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deleteRedLogicamente(int idRed)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspRed.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deleteRedLogicamente(int idRed)", this.getClass().toString()));
                try {
                    rspRed.setEsRolledBackIntentado(true);
                    rspRed.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deleteRedLogicamente(int idRed)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspRed.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deleteRedLogicamente(int idRed)", this.getClass().toString()));
                    rspRed.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspRed.esRolledBackIntentado()) {
                    rspRed.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspRed.setEsConexionCerradaExitosamente(true);
                }
                rspRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspRed;
            }
        } else {
            rspRed.setEsRolledBackExitosamente(false);
            rspRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspRed;
        }
    }

    @Override
    public RspRed getRedPorTraza(String traza) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspRed rspRed = new RspRed();
        //INICIALIZAR VARIABLES
        rspRed.setEsConexionAbiertaExitosamente(false);
        rspRed.setEsConexionCerradaExitosamente(false);
        rspRed.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspRed.setEsConexionAbiertaExitosamente(true);
            rspRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM red WHERE estado = 1 AND traza = '" + traza + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspRed.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspRed.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getRedPorTraza(String traza)", this.getClass().toString()));
                    if (rs.next()) {
                        Red red = new Red();
                        red = rsRed(rs, red);
                        rspRed.setRed(red);
                    }
                }
            } catch (SQLException e) {
                rspRed.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getRedPorTraza(String traza)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspRed.setEsConexionCerradaExitosamente(true);
                }
                rspRed.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspRed;
            }
        } else {
            rspRed.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspRed;
        }
    }
}
