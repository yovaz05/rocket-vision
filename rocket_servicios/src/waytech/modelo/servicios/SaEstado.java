package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Estado;
import waytech.modelo.beans.sgi.EstadoInsert;
import waytech.modelo.beans.sgi.EstadoUpdate;
import waytech.modelo.beans.sgi.Pais;
import waytech.modelo.interfaces.IsaEstado;
import waytech.modelo.interfaces.IsaPais;
import waytech.utilidades.UtilidadSistema;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaEstado implements IsaEstado {

    UtilidadSistema utilidadSistema = new UtilidadSistema();
    IsaPais isaPais = new SaPais();

    private Estado rsEstado(ResultSet rs, Estado estado) throws SQLException {
        estado.setEstado(rs.getShort("estado"));
        estado.setIdEstado(rs.getInt("id_estado"));
        estado.setTraza(rs.getString("traza"));
        Pais pais = new Pais();
        pais = isaPais.getPaisPorIdPais(rs.getInt("id_pais")).getPais();
        estado.setIdPais(pais);
        estado.setNombre(rs.getString("nombre"));
        return estado;
    }

    @Override
    public RspEstado getEstadoPorIdEstado(int idEstado) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspEstado rspEstado = new RspEstado();
        //INICIALIZAR VARIABLES
        rspEstado.setEsConexionAbiertaExitosamente(false);
        rspEstado.setEsConexionCerradaExitosamente(false);
        rspEstado.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEstado.setEsConexionAbiertaExitosamente(true);
            rspEstado.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM estado  WHERE estado = 1 AND id_estado = '" + idEstado + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspEstado.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspEstado.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getEstadoPorIdEstado(int idEstado)", this.getClass().toString()));
                    if (rs.next()) {
                        Estado estado = new Estado();
                        estado = rsEstado(rs, estado);
                        rspEstado.setEstado(estado);
                    }
                }
            } catch (SQLException e) {
                rspEstado.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getEstadoPorIdEstado(int idEstado)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspEstado.setEsConexionCerradaExitosamente(true);
                }
                rspEstado.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEstado;
            }
        } else {
            rspEstado.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEstado;
        }
    }

    @Override
    public RspEstado listEstado() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspEstado rspEstado = new RspEstado();
        List<Estado> todosLosEstados = new ArrayList<Estado>();
        //INICIALIZAR VARIABLES
        rspEstado.setEsConexionAbiertaExitosamente(false);
        rspEstado.setEsConexionCerradaExitosamente(false);
        rspEstado.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEstado.setEsConexionAbiertaExitosamente(true);
            rspEstado.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM estado WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspEstado.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspEstado.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listEstado()", this.getClass().toString()));
                    while (rs.next()) {
                        Estado estado = new Estado();
                        estado = rsEstado(rs, estado);
                        todosLosEstados.add(estado);
                    }
                }
            } catch (SQLException e) {
                rspEstado.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listEstado()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspEstado.setEsConexionCerradaExitosamente(true);
                }
                rspEstado.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspEstado.setTodosLosEstados(todosLosEstados);
                return rspEstado;
            }
        } else {
            rspEstado.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEstado;
        }
    }

    @Override
    public RspEstado updateEstado(EstadoUpdate estado) {
        RspEstado rspEstado = new RspEstado();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEstado.setEsConexionAbiertaExitosamente(false);
        rspEstado.setEsConexionCerradaExitosamente(false);
        rspEstado.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEstado.setEsRolledBackIntentado(false);
        rspEstado.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEstado.setEsConexionAbiertaExitosamente(true);
            rspEstado.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE estado SET nombre = '" + estado.getNombre() + "' WHERE id_estado = '" + estado.getIdEstado() + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEstado.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateEstado(EstadoUpdate estado)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEstado.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEstado.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateEstado(EstadoUpdate estado)", this.getClass().toString()));
                try {
                    rspEstado.setEsRolledBackIntentado(true);
                    rspEstado.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateEstado(EstadoUpdate estado)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEstado.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateEstado(EstadoUpdate estado)", this.getClass().toString()));
                    rspEstado.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEstado.esRolledBackIntentado()) {
                    rspEstado.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEstado.setEsConexionCerradaExitosamente(true);
                }
                rspEstado.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEstado;
            }
        } else {
            rspEstado.setEsRolledBackExitosamente(false);
            rspEstado.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEstado;
        }
    }

    @Override
    public RspEstado insertEstado(EstadoInsert estado) {
        RspEstado rspEstado = new RspEstado();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEstado.setEsConexionAbiertaExitosamente(false);
        rspEstado.setEsConexionCerradaExitosamente(false);
        rspEstado.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEstado.setEsRolledBackIntentado(false);
        rspEstado.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEstado.setEsConexionAbiertaExitosamente(true);
            rspEstado.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO estado ("
                        + "id_estado,"
                        + "nombre,"
                        + "estado,"
                        + "traza)"
                        + " VALUES (?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setString(2, estado.getNombre());
                stmt.setShort(3, Short.valueOf("1"));
                stmt.setString(4, traza);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEstado.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "ingresarEstado(Estado estado)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEstado.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEstado.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "ingresarEstado(Estado estado)", this.getClass().toString()));
                try {
                    rspEstado.setEsRolledBackIntentado(true);
                    rspEstado.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "ingresarEstado(Estado estado)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEstado.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "ingresarEstado(Estado estado)", this.getClass().toString()));
                    rspEstado.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEstado.esRolledBackIntentado()) {
                    rspEstado.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEstado.setEsConexionCerradaExitosamente(true);
                    rspEstado.setEstado(getEstadoPorTraza(traza).getEstado());
                }
                rspEstado.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEstado;
            }
        } else {
            rspEstado.setEsRolledBackExitosamente(false);
            rspEstado.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEstado;
        }
    }

    @Override
    public RspEstado deleteEstado(int idEstado) {
        RspEstado rspEstado = new RspEstado();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEstado.setEsConexionAbiertaExitosamente(false);
        rspEstado.setEsConexionCerradaExitosamente(false);
        rspEstado.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEstado.setEsRolledBackIntentado(false);
        rspEstado.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEstado.setEsConexionAbiertaExitosamente(true);
            rspEstado.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE estado SET estado = '0' WHERE id_estado = '" + idEstado + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEstado.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deleteEstado(int idEstado)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEstado.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEstado.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deleteEstado(int idEstado)", this.getClass().toString()));
                try {
                    rspEstado.setEsRolledBackIntentado(true);
                    rspEstado.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deleteEstado(int idEstado)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEstado.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deleteEstado(int idEstado)", this.getClass().toString()));
                    rspEstado.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEstado.esRolledBackIntentado()) {
                    rspEstado.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEstado.setEsConexionCerradaExitosamente(true);
                }
                rspEstado.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEstado;
            }
        } else {
            rspEstado.setEsRolledBackExitosamente(false);
            rspEstado.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEstado;
        }
    }

    @Override
    public RspEstado getEstadoPorTraza(String traza) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspEstado rspEstado = new RspEstado();
        //INICIALIZAR VARIABLES
        rspEstado.setEsConexionAbiertaExitosamente(false);
        rspEstado.setEsConexionCerradaExitosamente(false);
        rspEstado.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEstado.setEsConexionAbiertaExitosamente(true);
            rspEstado.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM estado  WHERE estado = 1 AND traza = '" + traza + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspEstado.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspEstado.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getEstadoPorTraza(String traza)", this.getClass().toString()));
                    if (rs.next()) {
                        Estado estado = new Estado();
                        estado = rsEstado(rs, estado);
                        rspEstado.setEstado(estado);
                    }
                }
            } catch (SQLException e) {
                rspEstado.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getEstadoPorTraza(String traza)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspEstado.setEsConexionCerradaExitosamente(true);
                }
                rspEstado.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEstado;
            }
        } else {
            rspEstado.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEstado;
        }
    }
}
