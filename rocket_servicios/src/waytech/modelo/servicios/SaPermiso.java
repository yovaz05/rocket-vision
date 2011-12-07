package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Permiso;
import waytech.modelo.beans.sgi.PermisoInsert;
import waytech.modelo.beans.sgi.PermisoUpdate;
import waytech.modelo.interfaces.IsaPermiso;
import waytech.utilidades.UtilidadSistema;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaPermiso implements IsaPermiso {

    UtilidadSistema utilidadSistema = new UtilidadSistema();

    private Permiso rsPermiso(ResultSet rs, Permiso permiso) throws SQLException {
        permiso.setIdPermiso(rs.getInt("id_permiso"));
        permiso.setNombre(rs.getString("nombre"));
        permiso.setCodigo(rs.getInt("codigo"));
        permiso.setNombre(rs.getString("nombre"));
        permiso.setEstado(rs.getShort("estado"));
        permiso.setTraza(rs.getString("traza"));
        return permiso;
    }

    @Override
    public RspPermiso getPermisoPorIdPermiso(int idPermiso) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPermiso rspPermiso = new RspPermiso();
        //INICIALIZAR VARIABLES
        rspPermiso.setEsConexionAbiertaExitosamente(false);
        rspPermiso.setEsConexionCerradaExitosamente(false);
        rspPermiso.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPermiso.setEsConexionAbiertaExitosamente(true);
            rspPermiso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM permiso WHERE estado = 1 AND id_permiso = '" + idPermiso + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPermiso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPermiso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPermisoPorIdPermiso(int idPermiso)", this.getClass().toString()));
                    if (rs.next()) {
                        Permiso permiso = new Permiso();
                        permiso = rsPermiso(rs, permiso);
                        rspPermiso.setPermiso(permiso);
                    }
                }
            } catch (SQLException e) {
                rspPermiso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPermisoPorIdPermiso(int idPermiso)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPermiso.setEsConexionCerradaExitosamente(true);
                }
                rspPermiso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPermiso;
            }
        } else {
            rspPermiso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPermiso;
        }
    }

    @Override
    public RspPermiso listPermiso() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPermiso rspPermiso = new RspPermiso();
        List<Permiso> todosLosPermisos = new ArrayList<Permiso>();
        //INICIALIZAR VARIABLES
        rspPermiso.setEsConexionAbiertaExitosamente(false);
        rspPermiso.setEsConexionCerradaExitosamente(false);
        rspPermiso.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPermiso.setEsConexionAbiertaExitosamente(true);
            rspPermiso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM permiso WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPermiso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPermiso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPermiso()", this.getClass().toString()));
                    while (rs.next()) {
                        Permiso permiso = new Permiso();
                        permiso = rsPermiso(rs, permiso);
                        todosLosPermisos.add(permiso);
                    }
                }
            } catch (SQLException e) {
                rspPermiso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPermiso()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPermiso.setEsConexionCerradaExitosamente(true);
                }
                rspPermiso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPermiso.setAllPermisos(todosLosPermisos);
                return rspPermiso;
            }
        } else {
            rspPermiso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPermiso;
        }
    }

    @Override
    public RspPermiso updatePermiso(PermisoUpdate permiso) {
        RspPermiso rspPermiso = new RspPermiso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPermiso.setEsConexionAbiertaExitosamente(false);
        rspPermiso.setEsConexionCerradaExitosamente(false);
        rspPermiso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPermiso.setEsRolledBackIntentado(false);
        rspPermiso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPermiso.setEsConexionAbiertaExitosamente(true);
            rspPermiso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE permiso SET nombre = '" + permiso.getNombre() + "', codigo = '" + permiso.getCodigo() + "' WHERE id_permiso = '" + permiso.getIdPermiso() + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPermiso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updatePermiso(PermisoUpdate permiso)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPermiso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPermiso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updatePermiso(PermisoUpdate permiso)", this.getClass().toString()));
                try {
                    rspPermiso.setEsRolledBackIntentado(true);
                    rspPermiso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updatePermiso(PermisoUpdate permiso)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPermiso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updatePermiso(PermisoUpdate permiso)", this.getClass().toString()));
                    rspPermiso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPermiso.esRolledBackIntentado()) {
                    rspPermiso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPermiso.setEsConexionCerradaExitosamente(true);
                }
                rspPermiso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPermiso;
            }
        } else {
            rspPermiso.setEsRolledBackExitosamente(false);
            rspPermiso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPermiso;
        }
    }

    @Override
    public RspPermiso insertPermiso(PermisoInsert permiso) {
        RspPermiso rspPermiso = new RspPermiso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPermiso.setEsConexionAbiertaExitosamente(false);
        rspPermiso.setEsConexionCerradaExitosamente(false);
        rspPermiso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPermiso.setEsRolledBackIntentado(false);
        rspPermiso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPermiso.setEsConexionAbiertaExitosamente(true);
            rspPermiso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO permiso ("
                        + "id_permiso,"
                        + "codigo,"
                        + "nombre,"                                                
                        + "traza,"
                        + "estado)"
                        + " VALUES (?,?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setInt(2, permiso.getCodigo());
                stmt.setString(3, permiso.getNombre());                                                
                stmt.setString(4, traza);
                stmt.setShort(5, Short.valueOf("1"));
                rows = stmt.executeUpdate();
                stmt.close();
                rspPermiso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertPermiso(PermisoInsert permiso)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPermiso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPermiso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertPermiso(PermisoInsert permiso)", this.getClass().toString()));
                try {
                    rspPermiso.setEsRolledBackIntentado(true);
                    rspPermiso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertPermiso(PermisoInsert permiso)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPermiso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertPermiso(PermisoInsert permiso)", this.getClass().toString()));
                    rspPermiso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPermiso.esRolledBackIntentado()) {
                    rspPermiso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPermiso.setEsConexionCerradaExitosamente(true);
                    rspPermiso.setPermiso(getPermisoPorTraza(traza).getPermiso());
                }
                rspPermiso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPermiso;
            }
        } else {
            rspPermiso.setEsRolledBackExitosamente(false);
            rspPermiso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPermiso;
        }
    }

    @Override
    public RspPermiso deletePermiso(int idPermiso) {
        RspPermiso rspPermiso = new RspPermiso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPermiso.setEsConexionAbiertaExitosamente(false);
        rspPermiso.setEsConexionCerradaExitosamente(false);
        rspPermiso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPermiso.setEsRolledBackIntentado(false);
        rspPermiso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPermiso.setEsConexionAbiertaExitosamente(true);
            rspPermiso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE permiso SET estado = '0' WHERE id_permiso = '" + idPermiso + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPermiso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deletePermisoLogicamente(int idPermiso)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPermiso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPermiso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deletePermisoLogicamente(int idPermiso)", this.getClass().toString()));
                try {
                    rspPermiso.setEsRolledBackIntentado(true);
                    rspPermiso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deletePermisoLogicamente(int idPermiso)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPermiso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deletePermisoLogicamente(int idPermiso)", this.getClass().toString()));
                    rspPermiso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPermiso.esRolledBackIntentado()) {
                    rspPermiso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPermiso.setEsConexionCerradaExitosamente(true);
                }
                rspPermiso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPermiso;
            }
        } else {
            rspPermiso.setEsRolledBackExitosamente(false);
            rspPermiso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPermiso;
        }
    }

    @Override
    public RspPermiso getPermisoPorTraza(String traza) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPermiso rspPermiso = new RspPermiso();
        //INICIALIZAR VARIABLES
        rspPermiso.setEsConexionAbiertaExitosamente(false);
        rspPermiso.setEsConexionCerradaExitosamente(false);
        rspPermiso.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPermiso.setEsConexionAbiertaExitosamente(true);
            rspPermiso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM permiso WHERE estado = 1 AND traza = '" + traza + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPermiso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPermiso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPermisoPorTraza(String traza)", this.getClass().toString()));
                    if (rs.next()) {
                        Permiso permiso = new Permiso();
                        permiso = rsPermiso(rs, permiso);
                        rspPermiso.setPermiso(permiso);
                    }
                }
            } catch (SQLException e) {
                rspPermiso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPermisoPorTraza(String traza)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPermiso.setEsConexionCerradaExitosamente(true);
                }
                rspPermiso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPermiso;
            }
        } else {
            rspPermiso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPermiso;
        }
    }
}
