package waytech.modelo.servicios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Semana;
import waytech.modelo.interfaces.IsaSemana;
import waytech.utilidades.UtilidadSistema;
import java.sql.PreparedStatement;
import waytech.modelo.beans.sgi.SemanaInsert;
import waytech.modelo.beans.sgi.SemanaUpdate;

/**
 *
 * @author root
 */
public class SaSemana implements IsaSemana {
    
     UtilidadSistema utilidadSistema = new UtilidadSistema();
    
    private Semana rsSemana(ResultSet rs, Semana semana) throws SQLException {        
        semana.setIdSemana(rs.getInt("id_semana"));        
        semana.setDesde(rs.getString("desde"));
        semana.setHasta(rs.getString("hasta"));
        semana.setNumero(rs.getInt("numero"));        
        semana.setEstado(rs.getShort("estado"));        
        semana.setTraza(rs.getString("traza"));
        return semana;
    }

    @Override
    public RspSemana getSemanaPorIdSemana(int idSemana) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspSemana rspSemana = new RspSemana();
        //INICIALIZAR VARIABLES
        rspSemana.setEsConexionAbiertaExitosamente(false);
        rspSemana.setEsConexionCerradaExitosamente(false);
        rspSemana.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspSemana.setEsConexionAbiertaExitosamente(true);
            rspSemana.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM semana WHERE estado = 1 AND id_semana = '" + idSemana + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspSemana.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspSemana.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getSemanaPorIdSemana(int idSemana)", this.getClass().toString()));
                    if (rs.next()) {
                        Semana semana = new Semana();
                        semana = rsSemana(rs, semana);
                        rspSemana.setSemana(semana);
                    }
                }
            } catch (SQLException e) {
                rspSemana.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getSemanaPorIdSemana(int idSemana)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspSemana.setEsConexionCerradaExitosamente(true);
                }
                rspSemana.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspSemana;
            }
        } else {
            rspSemana.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspSemana;
        }
    }

    @Override
    public RspSemana listSemana() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspSemana rspSemana = new RspSemana();
        List<Semana> allSemana = new ArrayList<Semana>();
        //INICIALIZAR VARIABLES
        rspSemana.setEsConexionAbiertaExitosamente(false);
        rspSemana.setEsConexionCerradaExitosamente(false);
        rspSemana.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspSemana.setEsConexionAbiertaExitosamente(true);
            rspSemana.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM semana WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspSemana.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspSemana.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listSemana()", this.getClass().toString()));
                    while (rs.next()) {
                        Semana semana = new Semana();
                        semana = rsSemana(rs, semana);
                        allSemana.add(semana);                                
                    }
                }
            } catch (SQLException e) {
                rspSemana.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listSemana()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspSemana.setEsConexionCerradaExitosamente(true);
                }
                rspSemana.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspSemana.setAllSemana(allSemana);
                return rspSemana;
            }
        } else {
            rspSemana.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspSemana;
        }
    }

    @Override
    public RspSemana updateSemana(SemanaUpdate semana) {
        RspSemana rspSemana = new RspSemana();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspSemana.setEsConexionAbiertaExitosamente(false);
        rspSemana.setEsConexionCerradaExitosamente(false);
        rspSemana.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspSemana.setEsRolledBackIntentado(false);
        rspSemana.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspSemana.setEsConexionAbiertaExitosamente(true);
            rspSemana.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);              
                String consultaSQL = "UPDATE semana SET desde = '"+ semana.getDesde() +"', hasta = '"+ semana.getHasta() +"', numero = '"+ semana.getNumero() +"' WHERE id_semana = '"+ semana.getIdSemana() +"'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspSemana.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateSemana(Semana semana)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspSemana.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspSemana.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateSemana(Semana semana)", this.getClass().toString()));
                try {
                    rspSemana.setEsRolledBackIntentado(true);
                    rspSemana.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateSemana(Semana semana)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspSemana.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateSemana(Semana semana)", this.getClass().toString()));
                    rspSemana.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspSemana.esRolledBackIntentado()) {
                    rspSemana.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspSemana.setEsConexionCerradaExitosamente(true);
                }
                rspSemana.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspSemana;
            }
        } else {
            rspSemana.setEsRolledBackExitosamente(false);
            rspSemana.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspSemana;
        }
    }

    @Override
    public RspSemana insertSemana(SemanaInsert semana) {
        RspSemana rspSemana = new RspSemana();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspSemana.setEsConexionAbiertaExitosamente(false);
        rspSemana.setEsConexionCerradaExitosamente(false);
        rspSemana.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspSemana.setEsRolledBackIntentado(false);
        rspSemana.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspSemana.setEsConexionAbiertaExitosamente(true);
            rspSemana.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);                
                String consultaSQL = "INSERT INTO semana ("
                        + "id_semana,"
                        + "desde,"
                        + "hasta,"
                        + "numero,"                        
                        + "estado,"
                        + "traza)"
                        + " VALUES (?,?,?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setString(2, semana.getDesde());
                stmt.setString(3, semana.getHasta());
                stmt.setInt(4, semana.getNumero());                
                stmt.setShort(5, Short.valueOf("1"));
                stmt.setString(6, traza);                
                rows = stmt.executeUpdate();
                stmt.close();
                rspSemana.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertSemana(SemanaInsert semana)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspSemana.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspSemana.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertSemana(SemanaInsert semana)", this.getClass().toString()));
                try {
                    rspSemana.setEsRolledBackIntentado(true);
                    rspSemana.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertSemana(SemanaInsert semana)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspSemana.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertSemana(SemanaInsert semana)", this.getClass().toString()));
                    rspSemana.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspSemana.esRolledBackIntentado()) {
                    rspSemana.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspSemana.setEsConexionCerradaExitosamente(true);
                }
                rspSemana.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspSemana;
            }
        } else {
            rspSemana.setEsRolledBackExitosamente(false);
            rspSemana.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspSemana;
        }
    }

    @Override
    public RspSemana deleteSemana(int idSemana) {
        RspSemana rspSemana = new RspSemana();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspSemana.setEsConexionAbiertaExitosamente(false);
        rspSemana.setEsConexionCerradaExitosamente(false);
        rspSemana.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspSemana.setEsRolledBackIntentado(false);
        rspSemana.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspSemana.setEsConexionAbiertaExitosamente(true);
            rspSemana.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);              
                String consultaSQL = "UPDATE semana SET estado = '0' WHERE id_semana = '"+ idSemana +"'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspSemana.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deleteSemana(int idSemana)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspSemana.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspSemana.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deleteSemana(int idSemana)", this.getClass().toString()));
                try {
                    rspSemana.setEsRolledBackIntentado(true);
                    rspSemana.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deleteSemana(int idSemana)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspSemana.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deleteSemana(int idSemana)", this.getClass().toString()));
                    rspSemana.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspSemana.esRolledBackIntentado()) {
                    rspSemana.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspSemana.setEsConexionCerradaExitosamente(true);
                }
                rspSemana.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspSemana;
            }
        } else {
            rspSemana.setEsRolledBackExitosamente(false);
            rspSemana.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspSemana;
        }
    }
    
}
