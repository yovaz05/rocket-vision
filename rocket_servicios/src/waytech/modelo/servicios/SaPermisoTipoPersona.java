package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.PermisoTipoPersona;
import waytech.modelo.beans.sgi.PermisoTipoPersonaInsert;
import waytech.modelo.beans.sgi.PermisoTipoPersonaUpdate;
import waytech.modelo.interfaces.IsaPermiso;
import waytech.modelo.interfaces.IsaPermisoTipoPersona;
import waytech.modelo.interfaces.IsaTipoPersona;
import waytech.utilidades.UtilidadSistema;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaPermisoTipoPersona implements IsaPermisoTipoPersona {

    UtilidadSistema utilidadSistema = new UtilidadSistema();
    IsaPermiso isaPermiso = new SaPermiso();
    IsaTipoPersona isaTipoPersona = new SaTipoPersona();

    private PermisoTipoPersona rsPermisoTipoPersona(ResultSet rs, PermisoTipoPersona permisoTipoPersona) throws SQLException {
        permisoTipoPersona.setIdPermisoTipoPersona(rs.getInt("id_permiso_tipo_persona"));
        permisoTipoPersona.setPermiso(isaPermiso.getPermisoPorIdPermiso(rs.getInt("id_permiso")).getPermiso());
//        permisoTipoPersona.setTipoPersona(isaTipoPersona.obtenerTipoPersonaPorIdTipoPersona(rs.getInt("id_tipo_persona")).getTipoPersona());
        permisoTipoPersona.setEstado(rs.getShort("estado"));
        permisoTipoPersona.setTraza(rs.getString("traza"));
        return permisoTipoPersona;
    }

    @Override
    public RspPermisoTipoPersona getPermisoTipoPersonaPorIdPermisoTipoPersona(int idPermisoTipoPersona) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPermisoTipoPersona rspPermisoTipoPersona = new RspPermisoTipoPersona();
        //INICIALIZAR VARIABLES
        rspPermisoTipoPersona.setEsConexionAbiertaExitosamente(false);
        rspPermisoTipoPersona.setEsConexionCerradaExitosamente(false);
        rspPermisoTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPermisoTipoPersona.setEsConexionAbiertaExitosamente(true);
            rspPermisoTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM permiso_tipo_persona WHERE estado = 1 AND id_permiso_tipo_persona = '" + idPermisoTipoPersona + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPermisoTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPermisoTipoPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPermisoTipoPersonaPorIdPermisoTipoPersona(int idPermisoTipoPersona)", this.getClass().toString()));
                    if (rs.next()) {
                        PermisoTipoPersona permisoTipoPersona = new PermisoTipoPersona();
                        permisoTipoPersona = rsPermisoTipoPersona(rs, permisoTipoPersona);
                        rspPermisoTipoPersona.setPermisoTipoPersona(permisoTipoPersona);
                    }
                }
            } catch (SQLException e) {
                rspPermisoTipoPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPermisoTipoPersonaPorIdPermisoTipoPersona(int idPermisoTipoPersona)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPermisoTipoPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPermisoTipoPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPermisoTipoPersona;
            }
        } else {
            rspPermisoTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPermisoTipoPersona;
        }
    }

    @Override
    public RspPermisoTipoPersona listPermisoTipoPersona() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPermisoTipoPersona rspPermisoTipoPersona = new RspPermisoTipoPersona();
        List<PermisoTipoPersona> todosLosPermisoTipoPersonas = new ArrayList<PermisoTipoPersona>();
        //INICIALIZAR VARIABLES
        rspPermisoTipoPersona.setEsConexionAbiertaExitosamente(false);
        rspPermisoTipoPersona.setEsConexionCerradaExitosamente(false);
        rspPermisoTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPermisoTipoPersona.setEsConexionAbiertaExitosamente(true);
            rspPermisoTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM permiso_tipo_persona WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPermisoTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPermisoTipoPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPermisoTipoPersona()", this.getClass().toString()));
                    while (rs.next()) {
                        PermisoTipoPersona permisoTipoPersona = new PermisoTipoPersona();
                        permisoTipoPersona = rsPermisoTipoPersona(rs, permisoTipoPersona);
                        todosLosPermisoTipoPersonas.add(permisoTipoPersona);
                    }
                }
            } catch (SQLException e) {
                rspPermisoTipoPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPermisoTipoPersona()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPermisoTipoPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPermisoTipoPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPermisoTipoPersona.setAllPermisoTipoPersona(todosLosPermisoTipoPersonas);
                return rspPermisoTipoPersona;
            }
        } else {
            rspPermisoTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPermisoTipoPersona;
        }
    }

    @Override
    public RspPermisoTipoPersona updatePermisoTipoPersona(PermisoTipoPersonaUpdate permisoTipoPersona) {
        RspPermisoTipoPersona rspPermisoTipoPersona = new RspPermisoTipoPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPermisoTipoPersona.setEsConexionAbiertaExitosamente(false);
        rspPermisoTipoPersona.setEsConexionCerradaExitosamente(false);
        rspPermisoTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPermisoTipoPersona.setEsRolledBackIntentado(false);
        rspPermisoTipoPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPermisoTipoPersona.setEsConexionAbiertaExitosamente(true);
            rspPermisoTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE permiso_tipo_persona SET id_permiso = '" + permisoTipoPersona.getIdPermiso() + "', id_tipo_persona = '" + permisoTipoPersona.getIdTipoPersona() + "' WHERE id_permiso_tipo_persona = '" + permisoTipoPersona.getIdPermisoTipoPersona() + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPermisoTipoPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updatePermisoTipoPersona(PermisoTipoPersonaUpdate permisoTipoPersona)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPermisoTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPermisoTipoPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updatePermisoTipoPersona(PermisoTipoPersonaUpdate permisoTipoPersona)", this.getClass().toString()));
                try {
                    rspPermisoTipoPersona.setEsRolledBackIntentado(true);
                    rspPermisoTipoPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updatePermisoTipoPersona(PermisoTipoPersonaUpdate permisoTipoPersona)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPermisoTipoPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updatePermisoTipoPersona(PermisoTipoPersonaUpdate permisoTipoPersona)", this.getClass().toString()));
                    rspPermisoTipoPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPermisoTipoPersona.esRolledBackIntentado()) {
                    rspPermisoTipoPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPermisoTipoPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPermisoTipoPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPermisoTipoPersona;
            }
        } else {
            rspPermisoTipoPersona.setEsRolledBackExitosamente(false);
            rspPermisoTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPermisoTipoPersona;
        }
    }

    @Override
    public RspPermisoTipoPersona insertPermisoTipoPersona(PermisoTipoPersonaInsert permisoTipoPersona) {
        RspPermisoTipoPersona rspPermisoTipoPersona = new RspPermisoTipoPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPermisoTipoPersona.setEsConexionAbiertaExitosamente(false);
        rspPermisoTipoPersona.setEsConexionCerradaExitosamente(false);
        rspPermisoTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPermisoTipoPersona.setEsRolledBackIntentado(false);
        rspPermisoTipoPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPermisoTipoPersona.setEsConexionAbiertaExitosamente(true);
            rspPermisoTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO permiso_tipo_persona ("
                        + "id_permiso_tipo_persona,"
                        + "id_permiso,"
                        + "id_tipo_persona,"                                                
                        + "traza,"
                        + "estado)"
                        + " VALUES (?,?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setInt(2, permisoTipoPersona.getIdPermiso());
                stmt.setInt(3, permisoTipoPersona.getIdTipoPersona());
                stmt.setString(4, traza);
                stmt.setShort(5, Short.valueOf("1"));
                rows = stmt.executeUpdate();
                stmt.close();
                rspPermisoTipoPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertPermisoTipoPersona(PermisoTipoPersonaInsert permisoTipoPersona)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPermisoTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPermisoTipoPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertPermisoTipoPersona(PermisoTipoPersonaInsert permisoTipoPersona)", this.getClass().toString()));
                try {
                    rspPermisoTipoPersona.setEsRolledBackIntentado(true);
                    rspPermisoTipoPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertPermisoTipoPersona(PermisoTipoPersonaInsert permisoTipoPersona)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPermisoTipoPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertPermisoTipoPersona(PermisoTipoPersonaInsert permisoTipoPersona)", this.getClass().toString()));
                    rspPermisoTipoPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPermisoTipoPersona.esRolledBackIntentado()) {
                    rspPermisoTipoPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPermisoTipoPersona.setEsConexionCerradaExitosamente(true);
                    rspPermisoTipoPersona.setPermisoTipoPersona(getPermisoTipoPersonaPorTraza(traza).getPermisoTipoPersona());
                }
                rspPermisoTipoPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPermisoTipoPersona;
            }
        } else {
            rspPermisoTipoPersona.setEsRolledBackExitosamente(false);
            rspPermisoTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPermisoTipoPersona;
        }
    }

    @Override
    public RspPermisoTipoPersona deletePermisoTipoPersona(int idPermisoTipoPersona) {
        RspPermisoTipoPersona rspPermisoTipoPersona = new RspPermisoTipoPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPermisoTipoPersona.setEsConexionAbiertaExitosamente(false);
        rspPermisoTipoPersona.setEsConexionCerradaExitosamente(false);
        rspPermisoTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPermisoTipoPersona.setEsRolledBackIntentado(false);
        rspPermisoTipoPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPermisoTipoPersona.setEsConexionAbiertaExitosamente(true);
            rspPermisoTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE permiso_tipo_persona SET estado = '0' WHERE id_permiso_tipo_persona = '" + idPermisoTipoPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPermisoTipoPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deletePermisoTipoPersonaLogicamente(int idPermisoTipoPersona)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPermisoTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPermisoTipoPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deletePermisoTipoPersonaLogicamente(int idPermisoTipoPersona)", this.getClass().toString()));
                try {
                    rspPermisoTipoPersona.setEsRolledBackIntentado(true);
                    rspPermisoTipoPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deletePermisoTipoPersonaLogicamente(int idPermisoTipoPersona)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPermisoTipoPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deletePermisoTipoPersonaLogicamente(int idPermisoTipoPersona)", this.getClass().toString()));
                    rspPermisoTipoPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPermisoTipoPersona.esRolledBackIntentado()) {
                    rspPermisoTipoPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPermisoTipoPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPermisoTipoPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPermisoTipoPersona;
            }
        } else {
            rspPermisoTipoPersona.setEsRolledBackExitosamente(false);
            rspPermisoTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPermisoTipoPersona;
        }
    }

    @Override
    public RspPermisoTipoPersona getPermisoTipoPersonaPorTraza(String traza) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPermisoTipoPersona rspPermisoTipoPersona = new RspPermisoTipoPersona();
        //INICIALIZAR VARIABLES
        rspPermisoTipoPersona.setEsConexionAbiertaExitosamente(false);
        rspPermisoTipoPersona.setEsConexionCerradaExitosamente(false);
        rspPermisoTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPermisoTipoPersona.setEsConexionAbiertaExitosamente(true);
            rspPermisoTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM permiso_tipo_persona WHERE estado = 1 AND traza = '" + traza + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPermisoTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPermisoTipoPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPermisoTipoPersonaPorTraza(String traza)", this.getClass().toString()));
                    if (rs.next()) {
                        PermisoTipoPersona permisoTipoPersona = new PermisoTipoPersona();
                        permisoTipoPersona = rsPermisoTipoPersona(rs, permisoTipoPersona);
                        rspPermisoTipoPersona.setPermisoTipoPersona(permisoTipoPersona);
                    }
                }
            } catch (SQLException e) {
                rspPermisoTipoPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPermisoTipoPersonaPorTraza(String traza)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPermisoTipoPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPermisoTipoPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPermisoTipoPersona;
            }
        } else {
            rspPermisoTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPermisoTipoPersona;
        }
    }
}
