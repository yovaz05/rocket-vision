package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Pareja;
import waytech.modelo.beans.sgi.ParejaInsert;
import waytech.modelo.beans.sgi.ParejaUpdate;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.interfaces.IsaPareja;
import waytech.modelo.interfaces.IsaPersona;
import waytech.utilidades.UtilidadSistema;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaPareja implements IsaPareja {

    UtilidadSistema utilidadSistema = new UtilidadSistema();
    IsaPersona isaPersona = new SaPersona();

    private Pareja rsPareja(ResultSet rs, Pareja pareja) throws SQLException {
        pareja.setEstado(rs.getShort("estado"));
        pareja.setTraza(rs.getString("traza"));
        pareja.setEsMatrimonio(rs.getBoolean("es_matrimonio"));
        Persona persona1 = new Persona();
        persona1 = isaPersona.getPersonaPorId(rs.getInt("id_persona1")).getPersona();
        Persona persona2 = new Persona();
        persona2 = isaPersona.getPersonaPorId(rs.getInt("id_persona2")).getPersona();
        pareja.setIdPersona1(persona1);
        pareja.setIdPersona2(persona2);
        pareja.setFechaInicio(rs.getString("fecha_inicio"));
        return pareja;
    }

    @Override
    public RspPareja getParejaPorIdPareja(int idPareja) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPareja rspPareja = new RspPareja();
        //INICIALIZAR VARIABLES
        rspPareja.setEsConexionAbiertaExitosamente(false);
        rspPareja.setEsConexionCerradaExitosamente(false);
        rspPareja.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPareja.setEsConexionAbiertaExitosamente(true);
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM pareja WHERE estado = 1 AND id_pareja = '" + idPareja + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPareja.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPareja.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getParejaPorIdPareja(int idPareja)", this.getClass().toString()));
                    if (rs.next()) {
                        Pareja Pareja = new Pareja();
                        Pareja = rsPareja(rs, Pareja);
                        rspPareja.setPareja(Pareja);
                    }
                }
            } catch (SQLException e) {
                rspPareja.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getParejaPorIdPareja(int idPareja)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPareja.setEsConexionCerradaExitosamente(true);
                }
                rspPareja.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPareja;
            }
        } else {
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPareja;
        }
    }

    @Override
    public RspPareja listPareja() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPareja rspPareja = new RspPareja();
        List<Pareja> allParejas = new ArrayList<Pareja>();
        //INICIALIZAR VARIABLES
        rspPareja.setEsConexionAbiertaExitosamente(false);
        rspPareja.setEsConexionCerradaExitosamente(false);
        rspPareja.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPareja.setEsConexionAbiertaExitosamente(true);
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM pareja WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPareja.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPareja.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPareja()", this.getClass().toString()));
                    while (rs.next()) {
                        Pareja pareja = new Pareja();
                        pareja = rsPareja(rs, pareja);
                        allParejas.add(pareja);
                    }
                }
            } catch (SQLException e) {
                rspPareja.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPareja()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPareja.setEsConexionCerradaExitosamente(true);
                }
                rspPareja.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPareja.setAllParejas(allParejas);
                return rspPareja;
            }
        } else {
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPareja;
        }
    }

    @Override
    public RspPareja updatePareja(ParejaUpdate pareja) {
        RspPareja rspPareja = new RspPareja();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPareja.setEsConexionAbiertaExitosamente(false);
        rspPareja.setEsConexionCerradaExitosamente(false);
        rspPareja.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPareja.setEsRolledBackIntentado(false);
        rspPareja.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPareja.setEsConexionAbiertaExitosamente(true);
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE pareja SET id_persona1 = '" + pareja.getIdPersona1() + "', id_persona2 = '" + pareja.getIdPersona2() + "', fecha_inicio = '" + pareja.getFechaInicio() + "', es_matrimonio = '" + pareja.esMatrimonio() + "' WHERE id_pareja = '" + pareja.getIdPareja() + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPareja.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updatePareja(ParejaUpdate pareja)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPareja.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPareja.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updatePareja(ParejaUpdate pareja)", this.getClass().toString()));
                try {
                    rspPareja.setEsRolledBackIntentado(true);
                    rspPareja.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updatePareja(ParejaUpdate pareja)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPareja.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updatePareja(ParejaUpdate pareja)", this.getClass().toString()));
                    rspPareja.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPareja.esRolledBackIntentado()) {
                    rspPareja.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPareja.setEsConexionCerradaExitosamente(true);
                }
                rspPareja.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPareja;
            }
        } else {
            rspPareja.setEsRolledBackExitosamente(false);
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPareja;
        }
    }

    @Override
    public RspPareja insertPareja(ParejaInsert pareja) {
        RspPareja rspPareja = new RspPareja();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPareja.setEsConexionAbiertaExitosamente(false);
        rspPareja.setEsConexionCerradaExitosamente(false);
        rspPareja.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPareja.setEsRolledBackIntentado(false);
        rspPareja.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPareja.setEsConexionAbiertaExitosamente(true);
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO pareja ("
                        + "id_pareja,"
                        + "id_persona1,"
                        + "id_persona2,"
                        + "fecha_inicio,"
                        + "es_matrimonio,"
                        + "traza,"
                        + "estado)"
                        + " VALUES (?,?,?,?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setInt(2, pareja.getIdPersona1());
                stmt.setInt(3, pareja.getIdPersona2());
                stmt.setString(4, pareja.getFechaInicio());
                stmt.setBoolean(5, pareja.esMatrimonio());
                stmt.setString(6, traza);
                stmt.setShort(7, Short.valueOf("1"));
                rows = stmt.executeUpdate();
                stmt.close();
                rspPareja.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertPareja(ParejaInsert pareja)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPareja.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPareja.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertPareja(ParejaInsert pareja)", this.getClass().toString()));
                try {
                    rspPareja.setEsRolledBackIntentado(true);
                    rspPareja.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertPareja(ParejaInsert pareja)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPareja.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertPareja(ParejaInsert pareja)", this.getClass().toString()));
                    rspPareja.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPareja.esRolledBackIntentado()) {
                    rspPareja.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPareja.setEsConexionCerradaExitosamente(true);
                    rspPareja.setPareja(getParejaPorTraza(traza).getPareja());
                }
                rspPareja.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPareja;
            }
        } else {
            rspPareja.setEsRolledBackExitosamente(false);
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPareja;
        }
    }

    @Override
    public RspPareja deletePareja(int idPareja) {
        RspPareja rspPareja = new RspPareja();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPareja.setEsConexionAbiertaExitosamente(false);
        rspPareja.setEsConexionCerradaExitosamente(false);
        rspPareja.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPareja.setEsRolledBackIntentado(false);
        rspPareja.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPareja.setEsConexionAbiertaExitosamente(true);
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE pareja SET estado = '0' WHERE id_pareja = '" + idPareja + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPareja.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deletePareja(int idPareja)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPareja.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPareja.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deletePareja(int idPareja)", this.getClass().toString()));
                try {
                    rspPareja.setEsRolledBackIntentado(true);
                    rspPareja.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deletePareja(int idPareja)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPareja.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deletePareja(int idPareja)", this.getClass().toString()));
                    rspPareja.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPareja.esRolledBackIntentado()) {
                    rspPareja.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPareja.setEsConexionCerradaExitosamente(true);
                }
                rspPareja.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPareja;
            }
        } else {
            rspPareja.setEsRolledBackExitosamente(false);
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPareja;
        }
    }

    @Override
    public RspPareja getParejaPorTraza(String traza) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPareja rspPareja = new RspPareja();
        //INICIALIZAR VARIABLES
        rspPareja.setEsConexionAbiertaExitosamente(false);
        rspPareja.setEsConexionCerradaExitosamente(false);
        rspPareja.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPareja.setEsConexionAbiertaExitosamente(true);
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM pareja WHERE estado = 1 AND traza = '" + traza + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPareja.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPareja.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getParejaPorTraza(String traza)", this.getClass().toString()));
                    if (rs.next()) {
                        Pareja Pareja = new Pareja();
                        Pareja = rsPareja(rs, Pareja);
                        rspPareja.setPareja(Pareja);
                    }
                }
            } catch (SQLException e) {
                rspPareja.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getParejaPorTraza(String traza)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPareja.setEsConexionCerradaExitosamente(true);
                }
                rspPareja.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPareja;
            }
        } else {
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPareja;
        }
    }

    @Override
    public RspPareja tienePareja(int idPersona) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPareja rspPareja = new RspPareja();
        //INICIALIZAR VARIABLES
        rspPareja.setEsConexionAbiertaExitosamente(false);
        rspPareja.setEsConexionCerradaExitosamente(false);
        rspPareja.setEsSentenciaSqlEjecutadaExitosamente(false);        
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPareja.setEsConexionAbiertaExitosamente(true);
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM pareja WHERE estado = 1 AND (id_persona1 = '" + idPersona + "' OR id_persona2 = '" + idPersona + "' )";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPareja.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPareja.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "tienePareja(int idPersona)", this.getClass().toString()));
                    if (rs.next()) {
                        rspPareja.setTienePareja(true);
                    }
                }
            } catch (SQLException e) {
                rspPareja.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "tienePareja(int idPersona)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPareja.setEsConexionCerradaExitosamente(true);
                }
                rspPareja.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPareja;
            }
        } else {
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPareja;
        }
    }

    @Override
    public RspPareja listParejaPorIdPersona(int idPersona) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPareja rspPareja = new RspPareja();
        List<Pareja> allParejas = new ArrayList<Pareja>();
        //INICIALIZAR VARIABLES
        rspPareja.setEsConexionAbiertaExitosamente(false);
        rspPareja.setEsConexionCerradaExitosamente(false);
        rspPareja.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPareja.setEsConexionAbiertaExitosamente(true);
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM pareja WHERE estado = 1 AND id_persona1 = '"+ idPersona +"'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPareja.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPareja.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listParejaPorIdPersona(int idPersona)", this.getClass().toString()));
                    while (rs.next()) {
                        Pareja pareja = new Pareja();
                        pareja = rsPareja(rs, pareja);
                        allParejas.add(pareja);
                    }
                }
            } catch (SQLException e) {
                rspPareja.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listParejaPorIdPersona(int idPersona)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPareja.setEsConexionCerradaExitosamente(true);
                }
                rspPareja.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPareja.setAllParejas(allParejas);
                return rspPareja;
            }
        } else {
            rspPareja.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPareja;
        }
    }
}
