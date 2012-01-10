package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Ciudad;
import waytech.modelo.beans.sgi.CiudadInsert;
import waytech.modelo.beans.sgi.CiudadUpdate;
import waytech.modelo.beans.sgi.Estado;
import waytech.modelo.interfaces.IsaCiudad;
import waytech.modelo.interfaces.IsaEstado;
import waytech.utilidades.UtilidadSistema;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaCiudad implements IsaCiudad {

    UtilidadSistema utilidadSistema = new UtilidadSistema();
    IsaEstado isaEstado = new SaEstado();

    private Ciudad rsCiudad(ResultSet rs, Ciudad ciudad) throws SQLException {
        ciudad.setEstado(rs.getShort("estado"));
        ciudad.setIdCiudad(rs.getInt("id_ciudad"));
        Estado estado = new Estado();
        estado = isaEstado.getEstadoPorIdEstado(rs.getInt("id_estado")).getEstado();
        ciudad.setIdEstado(estado);
        ciudad.setNombre(rs.getString("nombre"));
        ciudad.setTraza(rs.getString("traza"));
        return ciudad;
    }

    @Override
    public RspCiudad getCiudadPorIdCiudad(int idCiudad) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspCiudad rspCiudad = new RspCiudad();
        //INICIALIZAR VARIABLES
        rspCiudad.setEsConexionAbiertaExitosamente(false);
        rspCiudad.setEsConexionCerradaExitosamente(false);
        rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspCiudad.setEsConexionAbiertaExitosamente(true);
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM ciudad  WHERE estado = 1 AND id_ciudad = '" + idCiudad + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspCiudad.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getCiudadPorIdCiudad(int idCiudad)", this.getClass().toString()));
                    if (rs.next()) {
                        Ciudad ciudad = new Ciudad();
                        ciudad = rsCiudad(rs, ciudad);
                        rspCiudad.setCiudad(ciudad);
                    }
                }
            } catch (SQLException e) {
                rspCiudad.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getCiudadPorIdCiudad(int idCiudad)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspCiudad.setEsConexionCerradaExitosamente(true);
                }
                rspCiudad.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspCiudad;
            }
        } else {
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspCiudad;
        }
    }

    @Override
    public RspCiudad esNombreCiudadExistente(String nombre) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspCiudad rspCiudad = new RspCiudad();
        //INICIALIZAR VARIABLES
        rspCiudad.setEsConexionAbiertaExitosamente(false);
        rspCiudad.setEsConexionCerradaExitosamente(false);
        rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspCiudad.setEsNombreCiudadExistente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspCiudad.setEsConexionAbiertaExitosamente(true);
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM ciudad WHERE estado = 1 AND nombre = '" + nombre + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspCiudad.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "esNombreCiudadExistente(String nombre)", this.getClass().toString()));
                    if (rs.next()) {
                        rspCiudad.setEsNombreCiudadExistente(true);
                    }
                }
            } catch (SQLException e) {
                rspCiudad.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "esNombreCiudadExistente(String nombre)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspCiudad.setEsConexionCerradaExitosamente(true);
                }
                rspCiudad.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspCiudad;
            }
        } else {
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspCiudad;
        }
    }

    @Override
    public RspCiudad listCiudad() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspCiudad rspCiudad = new RspCiudad();
        List<Ciudad> todosLosCiudades = new ArrayList<Ciudad>();
        //INICIALIZAR VARIABLES
        rspCiudad.setEsConexionAbiertaExitosamente(false);
        rspCiudad.setEsConexionCerradaExitosamente(false);
        rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspCiudad.setEsConexionAbiertaExitosamente(true);
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM ciudad"
                    + " WHERE estado = 1"
                    + " ORDER BY nombre ASC";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspCiudad.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "obtenerTodosLosCiudades()", this.getClass().toString()));
                    while (rs.next()) {
                        Ciudad ciudad = new Ciudad();
                        ciudad = rsCiudad(rs, ciudad);
                        todosLosCiudades.add(ciudad);
                    }
                }
            } catch (SQLException e) {
                rspCiudad.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "obtenerTodosLosCiudades()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspCiudad.setEsConexionCerradaExitosamente(true);
                }
                rspCiudad.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspCiudad.setTodosLosCiudades(todosLosCiudades);
                return rspCiudad;
            }
        } else {
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspCiudad;
        }
    }

    @Override
    public RspCiudad updateCiudad(CiudadUpdate ciudad) {
        RspCiudad rspCiudad = new RspCiudad();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspCiudad.setEsConexionAbiertaExitosamente(false);
        rspCiudad.setEsConexionCerradaExitosamente(false);
        rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspCiudad.setEsRolledBackIntentado(false);
        rspCiudad.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspCiudad.setEsConexionAbiertaExitosamente(true);
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ciudad SET nombre = '" + ciudad.getNombre() + "' WHERE id_ciudad = '" + ciudad.getIdCiudad() + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspCiudad.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateCiudad(Ciudad ciudad)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspCiudad.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateCiudad(Ciudad ciudad)", this.getClass().toString()));
                try {
                    rspCiudad.setEsRolledBackIntentado(true);
                    rspCiudad.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateCiudad(Ciudad ciudad)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspCiudad.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateCiudad(Ciudad ciudad)", this.getClass().toString()));
                    rspCiudad.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspCiudad.esRolledBackIntentado()) {
                    rspCiudad.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspCiudad.setEsConexionCerradaExitosamente(true);
                }
                rspCiudad.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspCiudad;
            }
        } else {
            rspCiudad.setEsRolledBackExitosamente(false);
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspCiudad;
        }
    }

    @Override
    public RspCiudad insertCiudad(CiudadInsert ciudad) {
        RspCiudad rspCiudad = new RspCiudad();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspCiudad.setEsConexionAbiertaExitosamente(false);
        rspCiudad.setEsConexionCerradaExitosamente(false);
        rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspCiudad.setEsRolledBackIntentado(false);
        rspCiudad.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS        
        if (conectorBD.iniciarConexion()) {
            rspCiudad.setEsConexionAbiertaExitosamente(true);
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO ciudad ("
                        + "id_ciudad,"
                        + "nombre,"
                        + "ciudad,"
                        + "traza)"
                        + " VALUES (?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setString(2, ciudad.getNombre());
                stmt.setShort(3, Short.valueOf("1"));
                stmt.setString(4, traza);
                rows = stmt.executeUpdate();
                stmt.close();
                rspCiudad.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertCiudad(Ciudad ciudad)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspCiudad.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertCiudad(Ciudad ciudad)", this.getClass().toString()));
                try {
                    rspCiudad.setEsRolledBackIntentado(true);
                    rspCiudad.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertCiudad(Ciudad ciudad)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspCiudad.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertCiudad(Ciudad ciudad)", this.getClass().toString()));
                    rspCiudad.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspCiudad.esRolledBackIntentado()) {
                    rspCiudad.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspCiudad.setEsConexionCerradaExitosamente(true);
                    rspCiudad.setCiudad(getCiudadPorTraza(traza).getCiudad());
                }
                rspCiudad.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspCiudad;
            }
        } else {
            rspCiudad.setEsRolledBackExitosamente(false);
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspCiudad;
        }
    }

    @Override
    public RspCiudad deleteCiudad(int idCiudad) {
        RspCiudad rspCiudad = new RspCiudad();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspCiudad.setEsConexionAbiertaExitosamente(false);
        rspCiudad.setEsConexionCerradaExitosamente(false);
        rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspCiudad.setEsRolledBackIntentado(false);
        rspCiudad.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspCiudad.setEsConexionAbiertaExitosamente(true);
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ciudad SET ciudad = '0' WHERE id_ciudad = '" + idCiudad + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspCiudad.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateCiudad(Ciudad ciudad)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspCiudad.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateCiudad(Ciudad ciudad)", this.getClass().toString()));
                try {
                    rspCiudad.setEsRolledBackIntentado(true);
                    rspCiudad.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateCiudad(Ciudad ciudad)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspCiudad.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateCiudad(Ciudad ciudad)", this.getClass().toString()));
                    rspCiudad.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspCiudad.esRolledBackIntentado()) {
                    rspCiudad.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspCiudad.setEsConexionCerradaExitosamente(true);
                }
                rspCiudad.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspCiudad;
            }
        } else {
            rspCiudad.setEsRolledBackExitosamente(false);
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspCiudad;
        }
    }

    @Override
    public RspCiudad listCiudadPorIdEstado(int idEstado) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspCiudad rspCiudad = new RspCiudad();
        List<Ciudad> todasLasCiudades = new ArrayList<Ciudad>();
        //INICIALIZAR VARIABLES
        rspCiudad.setEsConexionAbiertaExitosamente(false);
        rspCiudad.setEsConexionCerradaExitosamente(false);
        rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspCiudad.setEsConexionAbiertaExitosamente(true);
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM ciudad WHERE estado = 1 AND id_estado = '" + idEstado + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspCiudad.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listCiudadPorIdEstado(int idEstado)", this.getClass().toString()));
                    while (rs.next()) {
                        Ciudad ciudad = new Ciudad();
                        ciudad = rsCiudad(rs, ciudad);
                        todasLasCiudades.add(ciudad);
                    }
                }
            } catch (SQLException e) {
                rspCiudad.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listCiudadPorIdEstado(int idEstado)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspCiudad.setEsConexionCerradaExitosamente(true);
                }
                rspCiudad.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspCiudad.setTodosLosCiudades(todasLasCiudades);
                return rspCiudad;
            }
        } else {
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspCiudad;
        }
    }

    @Override
    public RspCiudad getCiudadPorTraza(String traza) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspCiudad rspCiudad = new RspCiudad();
        //INICIALIZAR VARIABLES
        rspCiudad.setEsConexionAbiertaExitosamente(false);
        rspCiudad.setEsConexionCerradaExitosamente(false);
        rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspCiudad.setEsConexionAbiertaExitosamente(true);
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM ciudad  WHERE estado = 1 AND traza = '" + traza + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspCiudad.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspCiudad.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getCiudadPorTraza(String traza)", this.getClass().toString()));
                    if (rs.next()) {
                        Ciudad ciudad = new Ciudad();
                        ciudad = rsCiudad(rs, ciudad);
                        rspCiudad.setCiudad(ciudad);
                    }
                }
            } catch (SQLException e) {
                rspCiudad.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getCiudadPorTraza(String traza)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspCiudad.setEsConexionCerradaExitosamente(true);
                }
                rspCiudad.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspCiudad;
            }
        } else {
            rspCiudad.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspCiudad;
        }
    }

    @Override
    public RspCiudad listCiudadDondeHayCelulas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RspCiudad listCiudadDondeHayPersonas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
