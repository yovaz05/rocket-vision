package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Ciudad;
import waytech.modelo.beans.sgi.Zona;
import waytech.modelo.beans.sgi.ZonaInsert;
import waytech.modelo.beans.sgi.ZonaUpdate;
import waytech.modelo.interfaces.IsaCiudad;
import waytech.modelo.interfaces.IsaZona;
import waytech.utilidades.UtilidadSistema;

/**
 *
 * @author root
 */
public class SaZona implements IsaZona {
    
     UtilidadSistema utilidadSistema = new UtilidadSistema();
     IsaCiudad isaCiudad = new SaCiudad();
    
    private Zona rsZona(ResultSet rs, Zona zona) throws SQLException {        
        zona.setEstado(rs.getShort("estado"));
        zona.setIdZona(rs.getInt("id_zona"));                
        zona.setTraza(rs.getString("traza"));
        Ciudad ciudad = new Ciudad();
        ciudad = isaCiudad.getCiudadPorIdCiudad(rs.getInt("id_ciudad")).getCiudad();
        zona.setIdCiudad(ciudad);
        zona.setNombre(rs.getString("nombre"));        
        return zona;
    }        
    

    @Override
    public RspZona getZonaPorIdZona(int idZona) {                                                
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspZona rspZona = new RspZona();
        //INICIALIZAR VARIABLES
        rspZona.setEsConexionAbiertaExitosamente(false);
        rspZona.setEsConexionCerradaExitosamente(false);
        rspZona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspZona.setEsConexionAbiertaExitosamente(true);
            rspZona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM zona WHERE estado = 1 AND id_zona = '" + idZona + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspZona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspZona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getZonaPorIdZona(int idZona)", this.getClass().toString()));
                    if (rs.next()) {
                        Zona zona = new Zona();
                        zona = rsZona(rs, zona);
                        rspZona.setZona(zona);
                    }
                }
            } catch (SQLException e) {
                rspZona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getZonaPorIdZona(int idZona)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspZona.setEsConexionCerradaExitosamente(true);
                }
                rspZona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspZona;
            }
        } else {
            rspZona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspZona;
        }
    }

    @Override
    public RspZona listZona() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspZona rspZona = new RspZona();
        List<Zona> todosLosZonas = new ArrayList<Zona>();
        //INICIALIZAR VARIABLES
        rspZona.setEsConexionAbiertaExitosamente(false);
        rspZona.setEsConexionCerradaExitosamente(false);
        rspZona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspZona.setEsConexionAbiertaExitosamente(true);
            rspZona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM zona"
                    + " WHERE estado = 1"
                    + " ORDER BY nombre ASC";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspZona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspZona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listZona()", this.getClass().toString()));
                    while (rs.next()) {
                        Zona zona = new Zona();
                        zona = rsZona(rs, zona);
                        todosLosZonas.add(zona);                                
                    }
                }
            } catch (SQLException e) {
                rspZona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listZona()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspZona.setEsConexionCerradaExitosamente(true);
                }
                rspZona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspZona.setTodosLosZonas(todosLosZonas);
                return rspZona;
            }
        } else {
            rspZona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspZona;
        }
    }

    @Override
    public RspZona updateZona(ZonaUpdate zona) {
        RspZona rspZona = new RspZona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspZona.setEsConexionAbiertaExitosamente(false);
        rspZona.setEsConexionCerradaExitosamente(false);
        rspZona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspZona.setEsRolledBackIntentado(false);
        rspZona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspZona.setEsConexionAbiertaExitosamente(true);
            rspZona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);              
                String consultaSQL = "UPDATE zona SET nombre = '"+ zona.getNombre() +"', id_ciudad = '"+ zona.getIdCiudad() +"' WHERE id_zona = '"+ zona.getIdZona() +"'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspZona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateZona(ZonaUpdate zona)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspZona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspZona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateZona(ZonaUpdate zona)", this.getClass().toString()));
                try {
                    rspZona.setEsRolledBackIntentado(true);
                    rspZona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateZona(ZonaUpdate zona)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspZona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateZona(ZonaUpdate zona)", this.getClass().toString()));
                    rspZona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspZona.esRolledBackIntentado()) {
                    rspZona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspZona.setEsConexionCerradaExitosamente(true);
                }
                rspZona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspZona;
            }
        } else {
            rspZona.setEsRolledBackExitosamente(false);
            rspZona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspZona;
        }
    }

    @Override
    public RspZona insertZona(ZonaInsert zona) {
        RspZona rspZona = new RspZona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspZona.setEsConexionAbiertaExitosamente(false);
        rspZona.setEsConexionCerradaExitosamente(false);
        rspZona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspZona.setEsRolledBackIntentado(false);
        rspZona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspZona.setEsConexionAbiertaExitosamente(true);
            rspZona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);                
                String consultaSQL = "INSERT INTO zona ("
                        + "id_zona,"
                        + "id_ciudad,"
                        + "nombre,"                        
                        + "traza,"
                        + "estado)"
                        + " VALUES (?,?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setInt(2, zona.getIdCiudad());
                stmt.setString(3, zona.getNombre());                
                stmt.setShort(4, Short.valueOf("1"));
                stmt.setString(5, zona.getTraza());                
                rows = stmt.executeUpdate();
                stmt.close();
                rspZona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertZona(ZonaInsert zona)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspZona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspZona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertZona(ZonaInsert zona)", this.getClass().toString()));
                try {
                    rspZona.setEsRolledBackIntentado(true);
                    rspZona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertZona(ZonaInsert zona)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspZona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertZona(ZonaInsert zona)", this.getClass().toString()));
                    rspZona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspZona.esRolledBackIntentado()) {
                    rspZona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspZona.setEsConexionCerradaExitosamente(true);
                }
                rspZona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspZona;
            }
        } else {
            rspZona.setEsRolledBackExitosamente(false);
            rspZona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspZona;
        }
    }

    @Override
    public RspZona deleteZona(int idZona) {
        RspZona rspZona = new RspZona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspZona.setEsConexionAbiertaExitosamente(false);
        rspZona.setEsConexionCerradaExitosamente(false);
        rspZona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspZona.setEsRolledBackIntentado(false);
        rspZona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspZona.setEsConexionAbiertaExitosamente(true);
            rspZona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);              
                String consultaSQL = "UPDATE zona SET estado = '0' WHERE id_zona = '"+ idZona +"'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspZona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deleteZona(int idZona)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspZona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspZona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deleteZona(int idZona)", this.getClass().toString()));
                try {
                    rspZona.setEsRolledBackIntentado(true);
                    rspZona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deleteZona(int idZona)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspZona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deleteZona(int idZona)", this.getClass().toString()));
                    rspZona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspZona.esRolledBackIntentado()) {
                    rspZona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspZona.setEsConexionCerradaExitosamente(true);
                }
                rspZona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspZona;
            }
        } else {
            rspZona.setEsRolledBackExitosamente(false);
            rspZona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspZona;
        }
    }

    @Override
    public RspZona listZonaPorIdCiudad(int idCiudad) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspZona rspZona = new RspZona();
        List<Zona> todosLosZonas = new ArrayList<Zona>();
        //INICIALIZAR VARIABLES
        rspZona.setEsConexionAbiertaExitosamente(false);
        rspZona.setEsConexionCerradaExitosamente(false);
        rspZona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspZona.setEsConexionAbiertaExitosamente(true);
            rspZona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM zona WHERE estado = 1 AND id_ciudad = '"+ idCiudad +"'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspZona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspZona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listZonaPorIdCiudad(int idCiudad)", this.getClass().toString()));
                    while (rs.next()) {
                        Zona zona = new Zona();
                        zona = rsZona(rs, zona);
                        todosLosZonas.add(zona);                                
                    }
                }
            } catch (SQLException e) {
                rspZona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listZonaPorIdCiudad(int idCiudad)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspZona.setEsConexionCerradaExitosamente(true);
                }
                rspZona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspZona.setTodosLosZonas(todosLosZonas);
                return rspZona;
            }
        } else {
            rspZona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspZona;
        }
    }    
}
