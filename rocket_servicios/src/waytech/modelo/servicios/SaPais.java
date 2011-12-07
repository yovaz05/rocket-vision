package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Pais;
import waytech.modelo.beans.sgi.PaisInsert;
import waytech.modelo.beans.sgi.PaisUpdate;
import waytech.modelo.interfaces.IsaPais;
import waytech.utilidades.UtilidadSistema;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaPais implements IsaPais {

    UtilidadSistema utilidadSistema = new UtilidadSistema();

    private Pais rsPais(ResultSet rs, Pais pais) throws SQLException {
        pais.setEstado(rs.getShort("estado"));
        pais.setIdPais(rs.getInt("id_pais"));
        pais.setTraza(rs.getString("traza"));
        pais.setNombre(rs.getString("nombre"));
        return pais;
    }

    @Override
    public RspPais getPaisPorIdPais(int idPais) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPais rspPais = new RspPais();
        //INICIALIZAR VARIABLES
        rspPais.setEsConexionAbiertaExitosamente(false);
        rspPais.setEsConexionCerradaExitosamente(false);
        rspPais.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPais.setEsConexionAbiertaExitosamente(true);
            rspPais.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM pais WHERE estado = 1 AND id_pais = '" + idPais + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPais.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPais.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPaisPorIdPais(int idPais)", this.getClass().toString()));
                    if (rs.next()) {
                        Pais pais = new Pais();
                        pais = rsPais(rs, pais);
                        rspPais.setPais(pais);
                    }
                }
            } catch (SQLException e) {
                rspPais.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPaisPorIdPais(int idPais)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPais.setEsConexionCerradaExitosamente(true);
                }
                rspPais.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPais;
            }
        } else {
            rspPais.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPais;
        }
    }

    @Override
    public RspPais listPais() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPais rspPais = new RspPais();
        List<Pais> todosLosPaises = new ArrayList<Pais>();
        //INICIALIZAR VARIABLES
        rspPais.setEsConexionAbiertaExitosamente(false);
        rspPais.setEsConexionCerradaExitosamente(false);
        rspPais.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPais.setEsConexionAbiertaExitosamente(true);
            rspPais.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM pais WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPais.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPais.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPais()", this.getClass().toString()));
                    while (rs.next()) {
                        Pais pais = new Pais();
                        pais = rsPais(rs, pais);
                        todosLosPaises.add(pais);
                    }
                }
            } catch (SQLException e) {
                rspPais.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPais()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPais.setEsConexionCerradaExitosamente(true);
                }
                rspPais.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPais.setTodosLosPaises(todosLosPaises);
                return rspPais;
            }
        } else {
            rspPais.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPais;
        }
    }

    @Override
    public RspPais updatePais(PaisUpdate pais) {
        RspPais rspPais = new RspPais();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPais.setEsConexionAbiertaExitosamente(false);
        rspPais.setEsConexionCerradaExitosamente(false);
        rspPais.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPais.setEsRolledBackIntentado(false);
        rspPais.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPais.setEsConexionAbiertaExitosamente(true);
            rspPais.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE pais SET nombre = '" + pais.getNombre() + "' WHERE id_pais = '" + pais.getIdPais() + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPais.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updatePais(PaisUpdate pais)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPais.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPais.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updatePais(PaisUpdate pais)", this.getClass().toString()));
                try {
                    rspPais.setEsRolledBackIntentado(true);
                    rspPais.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updatePais(PaisUpdate pais)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPais.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updatePais(PaisUpdate pais)", this.getClass().toString()));
                    rspPais.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPais.esRolledBackIntentado()) {
                    rspPais.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPais.setEsConexionCerradaExitosamente(true);
                }
                rspPais.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPais;
            }
        } else {
            rspPais.setEsRolledBackExitosamente(false);
            rspPais.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPais;
        }
    }

    @Override
    public RspPais insertPais(PaisInsert pais) {
        RspPais rspPais = new RspPais();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPais.setEsConexionAbiertaExitosamente(false);
        rspPais.setEsConexionCerradaExitosamente(false);
        rspPais.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPais.setEsRolledBackIntentado(false);
        rspPais.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPais.setEsConexionAbiertaExitosamente(true);
            rspPais.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO pais ("
                        + "id_pais,"
                        + "nombre,"
                        + "estado,"
                        + "traza)"
                        + " VALUES (?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setString(2, pais.getNombre());
                stmt.setShort(3, Short.valueOf("1"));
                stmt.setString(4, traza);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPais.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertPais(PaisInsert pais)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPais.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPais.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertPais(PaisInsert pais)", this.getClass().toString()));
                try {
                    rspPais.setEsRolledBackIntentado(true);
                    rspPais.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertPais(PaisInsert pais)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPais.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertPais(PaisInsert pais)", this.getClass().toString()));
                    rspPais.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPais.esRolledBackIntentado()) {
                    rspPais.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPais.setEsConexionCerradaExitosamente(true);
                    rspPais.setPais(getPaisPorTraza(traza).getPais());
                }
                rspPais.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPais;
            }
        } else {
            rspPais.setEsRolledBackExitosamente(false);
            rspPais.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPais;
        }
    }

    @Override
    public RspPais deletePais(int idPais) {
        RspPais rspPais = new RspPais();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPais.setEsConexionAbiertaExitosamente(false);
        rspPais.setEsConexionCerradaExitosamente(false);
        rspPais.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPais.setEsRolledBackIntentado(false);
        rspPais.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPais.setEsConexionAbiertaExitosamente(true);
            rspPais.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE pais SET estado = '0' WHERE id_pais = '" + idPais + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPais.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deletePais(int idPais)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPais.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPais.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deletePais(int idPais)", this.getClass().toString()));
                try {
                    rspPais.setEsRolledBackIntentado(true);
                    rspPais.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deletePais(int idPais)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPais.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deletePais(int idPais)", this.getClass().toString()));
                    rspPais.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPais.esRolledBackIntentado()) {
                    rspPais.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPais.setEsConexionCerradaExitosamente(true);
                }
                rspPais.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPais;
            }
        } else {
            rspPais.setEsRolledBackExitosamente(false);
            rspPais.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPais;
        }
    }

    @Override
    public RspPais getPaisPorTraza(String traza) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPais rspPais = new RspPais();
        //INICIALIZAR VARIABLES
        rspPais.setEsConexionAbiertaExitosamente(false);
        rspPais.setEsConexionCerradaExitosamente(false);
        rspPais.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPais.setEsConexionAbiertaExitosamente(true);
            rspPais.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM pais WHERE estado = 1 AND traza = '" + traza + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPais.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPais.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPaisPorTraza(String traza)", this.getClass().toString()));
                    if (rs.next()) {
                        Pais pais = new Pais();
                        pais = rsPais(rs, pais);
                        rspPais.setPais(pais);
                    }
                }
            } catch (SQLException e) {
                rspPais.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPaisPorTraza(String traza)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPais.setEsConexionCerradaExitosamente(true);
                }
                rspPais.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPais;
            }
        } else {
            rspPais.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPais;
        }
    }
}
