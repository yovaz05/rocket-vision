package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Discipulo;
import waytech.modelo.beans.sgi.DiscipuloInsert;
import waytech.modelo.beans.sgi.DiscipuloUpdate;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.interfaces.IsaDiscipulo;
import waytech.modelo.interfaces.IsaPersona;
import waytech.utilidades.UtilidadSistema;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaDiscipulo implements IsaDiscipulo {

    UtilidadSistema utilidadSistema = new UtilidadSistema();
    IsaPersona isaPersona = new SaPersona();

    private Discipulo rsDiscipulo(ResultSet rs, Discipulo discipulo) throws SQLException {
        discipulo.setFechaInicio(rs.getString("fecha_inicio"));
        Persona persona1 = new Persona();
        persona1 = isaPersona.getPersonaPorId(rs.getInt("id_persona1")).getPersona();
        discipulo.setIdPersona1(persona1);
        Persona persona2 = new Persona();
        persona2 = isaPersona.getPersonaPorId(rs.getInt("id_persona2")).getPersona();
        discipulo.setIdPersona2(persona2);
        discipulo.setEstado(rs.getShort("estado"));
        discipulo.setTraza(rs.getString("traza"));
        discipulo.setIdDiscipulo(rs.getInt("id_discipulo"));
        return discipulo;
    }

    @Override
    public RspDiscipulo getDiscipuloPorIdDiscipulo(int idDiscipulo) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspDiscipulo rspDiscipulo = new RspDiscipulo();
        //INICIALIZAR VARIABLES
        rspDiscipulo.setEsConexionAbiertaExitosamente(false);
        rspDiscipulo.setEsConexionCerradaExitosamente(false);
        rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspDiscipulo.setEsConexionAbiertaExitosamente(true);
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM discipulo WHERE estado = 1 AND id_discipulo = '" + idDiscipulo + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getDiscipuloPorIdDiscipulo(int idDiscipulo)", this.getClass().toString()));
                    if (rs.next()) {
                        Discipulo Discipulo = new Discipulo();
                        Discipulo = rsDiscipulo(rs, Discipulo);
                        rspDiscipulo.setDiscipulo(Discipulo);
                    }
                }
            } catch (SQLException e) {
                rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getDiscipuloPorIdDiscipulo(int idDiscipulo)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspDiscipulo.setEsConexionCerradaExitosamente(true);
                }
                rspDiscipulo.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspDiscipulo;
            }
        } else {
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspDiscipulo;
        }
    }

    @Override
    public RspDiscipulo listDiscipulo() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspDiscipulo rspDiscipulo = new RspDiscipulo();
        List<Discipulo> allDiscipulos = new ArrayList<Discipulo>();
        //INICIALIZAR VARIABLES
        rspDiscipulo.setEsConexionAbiertaExitosamente(false);
        rspDiscipulo.setEsConexionCerradaExitosamente(false);
        rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspDiscipulo.setEsConexionAbiertaExitosamente(true);
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM discipulo WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listDiscipulo()", this.getClass().toString()));
                    while (rs.next()) {
                        Discipulo discipulo = new Discipulo();
                        discipulo = rsDiscipulo(rs, discipulo);
                        allDiscipulos.add(discipulo);
                    }
                }
            } catch (SQLException e) {
                rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listDiscipulo()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspDiscipulo.setEsConexionCerradaExitosamente(true);
                }
                rspDiscipulo.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspDiscipulo.setAllDiscipulos(allDiscipulos);
                return rspDiscipulo;
            }
        } else {
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspDiscipulo;
        }
    }

    @Override
    public RspDiscipulo updateDiscipulo(DiscipuloUpdate discipulo) {
        RspDiscipulo rspDiscipulo = new RspDiscipulo();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspDiscipulo.setEsConexionAbiertaExitosamente(false);
        rspDiscipulo.setEsConexionCerradaExitosamente(false);
        rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspDiscipulo.setEsRolledBackIntentado(false);
        rspDiscipulo.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspDiscipulo.setEsConexionAbiertaExitosamente(true);
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE discipulo SET id_persona1 = '" + discipulo.getIdPersona1() + "', id_persona2 = '" + discipulo.getIdPersona2() + "', fecha_inicio = '" + discipulo.getFechaInicio() + "' WHERE id_discipulo = '" + discipulo.getIdDiscipulo() + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateDiscipulo(DiscipuloUpdate discipulo)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateDiscipulo(DiscipuloUpdate discipulo)", this.getClass().toString()));
                try {
                    rspDiscipulo.setEsRolledBackIntentado(true);
                    rspDiscipulo.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateDiscipulo(DiscipuloUpdate discipulo)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspDiscipulo.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateDiscipulo(DiscipuloUpdate discipulo)", this.getClass().toString()));
                    rspDiscipulo.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspDiscipulo.esRolledBackIntentado()) {
                    rspDiscipulo.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspDiscipulo.setEsConexionCerradaExitosamente(true);
                }
                rspDiscipulo.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspDiscipulo;
            }
        } else {
            rspDiscipulo.setEsRolledBackExitosamente(false);
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspDiscipulo;
        }
    }

    @Override
    public RspDiscipulo insertDiscipulo(DiscipuloInsert discipulo) {
        RspDiscipulo rspDiscipulo = new RspDiscipulo();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspDiscipulo.setEsConexionAbiertaExitosamente(false);
        rspDiscipulo.setEsConexionCerradaExitosamente(false);
        rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspDiscipulo.setEsRolledBackIntentado(false);
        rspDiscipulo.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspDiscipulo.setEsConexionAbiertaExitosamente(true);
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO discipulo ("
                        + "id_discipulo,"
                        + "id_persona1,"
                        + "id_persona2,"
                        + "fecha_inicio,"
                        + "traza,"
                        + "estado)"
                        + " VALUES (?,?,?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setInt(2, discipulo.getIdPersona1());
                stmt.setInt(3, discipulo.getIdPersona2());
                stmt.setString(4, discipulo.getFechaInicio());
                stmt.setString(5, traza);
                stmt.setShort(6, Short.valueOf("1"));
                rows = stmt.executeUpdate();
                stmt.close();
                rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertDiscipulo(DiscipuloInsert discipulo)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertDiscipulo(DiscipuloInsert discipulo)", this.getClass().toString()));
                try {
                    rspDiscipulo.setEsRolledBackIntentado(true);
                    rspDiscipulo.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertDiscipulo(DiscipuloInsert discipulo)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspDiscipulo.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertDiscipulo(DiscipuloInsert discipulo)", this.getClass().toString()));
                    rspDiscipulo.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspDiscipulo.esRolledBackIntentado()) {
                    rspDiscipulo.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspDiscipulo.setEsConexionCerradaExitosamente(true);
                    rspDiscipulo.setDiscipulo(getDiscipuloPorTraza(traza).getDiscipulo());
                }
                rspDiscipulo.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspDiscipulo;
            }
        } else {
            rspDiscipulo.setEsRolledBackExitosamente(false);
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspDiscipulo;
        }
    }

    @Override
    public RspDiscipulo deleteDiscipulo(int idDiscipulo) {
        RspDiscipulo rspDiscipulo = new RspDiscipulo();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspDiscipulo.setEsConexionAbiertaExitosamente(false);
        rspDiscipulo.setEsConexionCerradaExitosamente(false);
        rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspDiscipulo.setEsRolledBackIntentado(false);
        rspDiscipulo.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspDiscipulo.setEsConexionAbiertaExitosamente(true);
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE discipulo SET estado = '0' WHERE id_discipulo = '" + idDiscipulo + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deleteDiscipulo(int idDiscipulo)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deleteDiscipulo(int idDiscipulo)", this.getClass().toString()));
                try {
                    rspDiscipulo.setEsRolledBackIntentado(true);
                    rspDiscipulo.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deleteDiscipulo(int idDiscipulo)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspDiscipulo.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deleteDiscipulo(int idDiscipulo)", this.getClass().toString()));
                    rspDiscipulo.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspDiscipulo.esRolledBackIntentado()) {
                    rspDiscipulo.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspDiscipulo.setEsConexionCerradaExitosamente(true);
                }
                rspDiscipulo.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspDiscipulo;
            }
        } else {
            rspDiscipulo.setEsRolledBackExitosamente(false);
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspDiscipulo;
        }
    }

    @Override
    public RspDiscipulo getDiscipuloPorTraza(String traza) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspDiscipulo rspDiscipulo = new RspDiscipulo();
        //INICIALIZAR VARIABLES
        rspDiscipulo.setEsConexionAbiertaExitosamente(false);
        rspDiscipulo.setEsConexionCerradaExitosamente(false);
        rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspDiscipulo.setEsConexionAbiertaExitosamente(true);
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM discipulo WHERE estado = 1 AND traza = '" + traza + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getDiscipuloPorTraza(String traza)", this.getClass().toString()));
                    if (rs.next()) {
                        Discipulo Discipulo = new Discipulo();
                        Discipulo = rsDiscipulo(rs, Discipulo);
                        rspDiscipulo.setDiscipulo(Discipulo);
                    }
                }
            } catch (SQLException e) {
                rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getDiscipuloPorTraza(String traza)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspDiscipulo.setEsConexionCerradaExitosamente(true);
                }
                rspDiscipulo.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspDiscipulo;
            }
        } else {
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspDiscipulo;
        }
    }

    @Override
    public RspDiscipulo listDiscipulo(int idDiscipulo) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspDiscipulo rspDiscipulo = new RspDiscipulo();
        List<Discipulo> allDiscipulos = new ArrayList<Discipulo>();
        //INICIALIZAR VARIABLES
        rspDiscipulo.setEsConexionAbiertaExitosamente(false);
        rspDiscipulo.setEsConexionCerradaExitosamente(false);
        rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspDiscipulo.setEsConexionAbiertaExitosamente(true);
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM discipulo WHERE estado = 1 AND id_discipulo = '"+ idDiscipulo +"'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspDiscipulo.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listDiscipulo(int idDiscipulo)", this.getClass().toString()));
                    while (rs.next()) {
                        Discipulo discipulo = new Discipulo();
                        discipulo = rsDiscipulo(rs, discipulo);
                        allDiscipulos.add(discipulo);
                    }
                }
            } catch (SQLException e) {
                rspDiscipulo.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listDiscipulo(int idDiscipulo)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspDiscipulo.setEsConexionCerradaExitosamente(true);
                }
                rspDiscipulo.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspDiscipulo.setAllDiscipulos(allDiscipulos);
                return rspDiscipulo;
            }
        } else {
            rspDiscipulo.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspDiscipulo;
        }
    }
}
