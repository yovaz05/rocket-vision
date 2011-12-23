package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Acceso;
import waytech.modelo.beans.sgi.AccesoInsert;
import waytech.modelo.beans.sgi.AccesoUpdate;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.interfaces.IsaAcceso;
import waytech.modelo.interfaces.IsaPersona;
import waytech.utilidades.UtilidadSistema;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaAcceso implements IsaAcceso {

    UtilidadSistema utilidadSistema = new UtilidadSistema();
    IsaPersona isaPersona = new SaPersona();

    private Acceso rsAcceso(ResultSet rs, Acceso acceso) throws SQLException {
        acceso.setCorreo(rs.getString("correo"));
        acceso.setEstado(rs.getShort("estado"));
        acceso.setIdAcceso(rs.getInt("id_acceso"));
        Persona persona = new Persona();
        persona = isaPersona.getPersonaPorId(rs.getInt("id_persona")).getPersona();
        acceso.setIdPersona(persona);
        acceso.setLogin(rs.getString("login"));
        acceso.setPassword(rs.getString("password"));
        acceso.setTraza(rs.getString("traza"));
        acceso.setFechaUltimoAccesoString(rs.getString("fecha_ultimo_acceso"));
        return acceso;
    }

    @Override
    public RspAcceso getAccesoPorIdAcceso(int idAcceso) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspAcceso rspAcceso = new RspAcceso();
        //INICIALIZAR VARIABLES
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM acceso WHERE estado = 1 AND id_acceso = '" + idAcceso + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getAccesoPorIdAcceso(int idAcceso)", this.getClass().toString()));
                    if (rs.next()) {
                        Acceso acceso = new Acceso();
                        acceso = rsAcceso(rs, acceso);
                        rspAcceso.setAcceso(acceso);
                    }
                }
            } catch (SQLException e) {
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getAccesoPorIdAcceso(int idAcceso)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso getAccesoPorIdUsuario(int idUsuario) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspAcceso rspAcceso = new RspAcceso();
        //INICIALIZAR VARIABLES
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM acceso WHERE estado = 1 AND id_persona = '" + idUsuario + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getAccesoPorIdUsuario(int idUsuario)", this.getClass().toString()));
                    if (rs.next()) {
                        Acceso acceso = new Acceso();
                        acceso = rsAcceso(rs, acceso);
                        rspAcceso.setAcceso(acceso);
                    }
                }
            } catch (SQLException e) {
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getAccesoPorIdUsuario(int idUsuario)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso getAccesoPorLogin(String login) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspAcceso rspAcceso = new RspAcceso();
        //INICIALIZAR VARIABLES
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM acceso WHERE estado = 1 AND login = '" + login + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getUsuarioLogin(String login)", this.getClass().toString()));
                    if (rs.next()) {
                        Acceso acceso = new Acceso();
                        acceso = rsAcceso(rs, acceso);
                        rspAcceso.setAcceso(acceso);
                    }
                }
            } catch (SQLException e) {
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getUsuarioLogin(String login)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso getAccesoPorPassword(String password) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspAcceso rspAcceso = new RspAcceso();
        //INICIALIZAR VARIABLES
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM acceso WHERE estado = 1 AND password = '" + password + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getAccesoPorPassword(String password)", this.getClass().toString()));
                    if (rs.next()) {
                        Acceso acceso = new Acceso();
                        acceso = rsAcceso(rs, acceso);
                        rspAcceso.setAcceso(acceso);
                    }
                }
            } catch (SQLException e) {
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getAccesoPorPassword(String password)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso getAccesoPorCorreo(String correo) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspAcceso rspAcceso = new RspAcceso();
        //INICIALIZAR VARIABLES
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM acceso WHERE estado = 1 AND correo = '" + correo + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getAccesoPorCorreo(String correo)", this.getClass().toString()));
                    if (rs.next()) {
                        Acceso acceso = new Acceso();
                        acceso = rsAcceso(rs, acceso);
                        rspAcceso.setAcceso(acceso);
                    }
                }
            } catch (SQLException e) {
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getAccesoPorCorreo(String correo)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso esLoginExistente(String login) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspAcceso rspAcceso = new RspAcceso();
        //INICIALIZAR VARIABLES
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspAcceso.setEsLoginExistente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM acceso WHERE estado = 1 AND login = '" + login + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "esLoginExistente(String login)", this.getClass().toString()));
                    if (rs.next()) {
                        rspAcceso.setEsLoginExistente(true);
                    }
                }
            } catch (SQLException e) {
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "esLoginExistente(String login)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso esPasswordExistente(String password) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspAcceso rspAcceso = new RspAcceso();
        //INICIALIZAR VARIABLES
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspAcceso.setEsLoginExistente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM acceso WHERE estado = 1 AND password = '" + password + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "esPasswordExistente(String password)", this.getClass().toString()));
                    if (rs.next()) {
                        rspAcceso.setEsPasswordExistente(true);
                    }
                }
            } catch (SQLException e) {
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "esPasswordExistente(String password)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso esCorreoExistente(String correo) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspAcceso rspAcceso = new RspAcceso();
        //INICIALIZAR VARIABLES
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspAcceso.setEsCorreoExistente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM acceso WHERE estado = 1 AND correo = '" + correo + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "esCorreoExistente(String correo)", this.getClass().toString()));
                    if (rs.next()) {
                        rspAcceso.setEsCorreoExistente(true);
                    }
                }
            } catch (SQLException e) {
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "esCorreoExistente(String correo)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso listAcceso() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspAcceso rspAcceso = new RspAcceso();
        List<Acceso> todosLosAccesos = new ArrayList<Acceso>();
        //INICIALIZAR VARIABLES
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM acceso WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listAcceso()", this.getClass().toString()));
                    while (rs.next()) {
                        Acceso acceso = new Acceso();
                        acceso = rsAcceso(rs, acceso);
                        todosLosAccesos.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listAcceso()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspAcceso.setTodosLosAccesos(todosLosAccesos);
                return rspAcceso;
            }
        } else {
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso updateAcceso(AccesoUpdate acceso) {
        RspAcceso rspAcceso = new RspAcceso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspAcceso.setEsRolledBackIntentado(false);
        rspAcceso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE acceso SET password = '" + acceso.getPassword() + "', correo = '" + acceso.getCorreo() + "' WHERE id_acceso = '" + acceso.getIdAcceso() + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateAcceso(AccesoModificar acceso)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateAcceso(AccesoModificar acceso)", this.getClass().toString()));
                try {
                    rspAcceso.setEsRolledBackIntentado(true);
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateAcceso(AccesoModificar acceso)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateAcceso(AccesoModificar acceso)", this.getClass().toString()));
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspAcceso.esRolledBackIntentado()) {
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setEsRolledBackExitosamente(false);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso insertAcceso(AccesoInsert accesoInsert) {
        RspAcceso rspAcceso = new RspAcceso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspAcceso.setEsRolledBackIntentado(false);
        rspAcceso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO acceso ("
                        + "id_acceso,"
                        + "id_persona,"
                        + "login,"
                        + "password,"
                        + "correo,"
                        + "estado,"
                        + "traza)"
                        + " VALUES (?,?,?,?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setInt(2, accesoInsert.getIdPersona());
                stmt.setString(3, accesoInsert.getLogin());
                stmt.setString(4, accesoInsert.getPassword());
                stmt.setString(5, accesoInsert.getCorreo());
                stmt.setShort(6, Short.valueOf("1"));
                stmt.setString(7, traza);
                rows = stmt.executeUpdate();
                stmt.close();
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertAcceso(AccesoInsert accesoInsert)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertAcceso(AccesoInsert accesoInsert)", this.getClass().toString()));
                try {
                    rspAcceso.setEsRolledBackIntentado(true);
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertAcceso(AccesoInsert accesoInsert)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertAcceso(AccesoInsert accesoInsert)", this.getClass().toString()));
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspAcceso.esRolledBackIntentado()) {
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                    rspAcceso.setAcceso(getAccesoPorTraza(traza).getAcceso());
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setEsRolledBackExitosamente(false);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso deleteAcceso(int idAcceso) {
        RspAcceso rspAcceso = new RspAcceso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspAcceso.setEsRolledBackIntentado(false);
        rspAcceso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE acceso SET estado = '0' WHERE id_acceso = '" + idAcceso + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deleteAcceso(int idAcceso)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deleteAcceso(int idAcceso)", this.getClass().toString()));
                try {
                    rspAcceso.setEsRolledBackIntentado(true);
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deleteAcceso(int idAcceso)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deleteAcceso(int idAcceso)", this.getClass().toString()));
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspAcceso.esRolledBackIntentado()) {
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setEsRolledBackExitosamente(false);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso updateFechaUltimoAcceso(int idAcceso) {
        RspAcceso rspAcceso = new RspAcceso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspAcceso.setEsRolledBackIntentado(false);
        rspAcceso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE acceso SET fecha_ultimo_acceso = NOW() WHERE id_acceso = '" + idAcceso + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateFechaUltimoAcceso(int idAcceso)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateFechaUltimoAcceso(int idAcceso)", this.getClass().toString()));
                try {
                    rspAcceso.setEsRolledBackIntentado(true);
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateFechaUltimoAcceso(int idAcceso)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateFechaUltimoAcceso(int idAcceso)", this.getClass().toString()));
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspAcceso.esRolledBackIntentado()) {
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setEsRolledBackExitosamente(false);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso getAccesoPorTraza(String traza) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspAcceso rspAcceso = new RspAcceso();
        //INICIALIZAR VARIABLES
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM acceso WHERE estado = 1 AND traza = '" + traza + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getAccesoPorTraza(String traza)", this.getClass().toString()));
                    if (rs.next()) {
                        Acceso acceso = new Acceso();
                        acceso = rsAcceso(rs, acceso);
                        rspAcceso.setAcceso(acceso);
                    }
                }
            } catch (SQLException e) {
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getAccesoPorTraza(String traza)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso esLoginPasswordValido(String login, String password) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspAcceso rspAcceso = new RspAcceso();
        //INICIALIZAR VARIABLES
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspAcceso.setEsLoginExistente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM acceso WHERE estado = 1 AND password = '" + password + "' AND login = '" + login + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "esLoginPasswordValido(String login, String password)", this.getClass().toString()));
                    if (rs.next()) {
                        rspAcceso.setEsLoginPasswordValido(true);
                    }
                }
            } catch (SQLException e) {
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "esLoginPasswordValido(String login, String password)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                    if (rspAcceso.esLoginPasswordValido()) {
                        rspAcceso.setAcceso(getAccesoPorLogin(login).getAcceso());
                    }
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso esCorreoCoincidente(String correo, int idPersona) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspAcceso rspAcceso = new RspAcceso();
        //INICIALIZAR VARIABLES
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspAcceso.setEsCorreoCoincidente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM acceso WHERE estado = 1 AND correo = '" + correo + "' AND id_persona = '"+ idPersona +"'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "esCorreoCoincidente(String correo, int idPersona)", this.getClass().toString()));
                    if (rs.next()) {
                        rspAcceso.setEsCorreoCoincidente(true);
                    }
                }
            } catch (SQLException e) {
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "esCorreoCoincidente(String correo, int idPersona)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso updateEstadoHabilitado(int idAcceso) {
        RspAcceso rspAcceso = new RspAcceso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspAcceso.setEsRolledBackIntentado(false);
        rspAcceso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE acceso SET estado = '1' WHERE id_acceso = '" + idAcceso + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateEstadoHabilitado(int idAcceso)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateEstadoHabilitado(int idAcceso)", this.getClass().toString()));
                try {
                    rspAcceso.setEsRolledBackIntentado(true);
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateEstadoHabilitado(int idAcceso)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateEstadoHabilitado(int idAcceso)", this.getClass().toString()));
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspAcceso.esRolledBackIntentado()) {
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setEsRolledBackExitosamente(false);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso updateEstadoRegistrado(int idAcceso) {
        RspAcceso rspAcceso = new RspAcceso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspAcceso.setEsRolledBackIntentado(false);
        rspAcceso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE acceso SET estado = '2' WHERE id_acceso = '" + idAcceso + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateEstadoRegistrado(int idAcceso)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateEstadoRegistrado(int idAcceso)", this.getClass().toString()));
                try {
                    rspAcceso.setEsRolledBackIntentado(true);
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateEstadoRegistrado(int idAcceso)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateEstadoRegistrado(int idAcceso)", this.getClass().toString()));
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspAcceso.esRolledBackIntentado()) {
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setEsRolledBackExitosamente(false);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso updateEstadoSolicitado(int idAcceso) {
        RspAcceso rspAcceso = new RspAcceso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspAcceso.setEsRolledBackIntentado(false);
        rspAcceso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE acceso SET estado = '3' WHERE id_acceso = '" + idAcceso + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updateEstadoSolicitado(int idAcceso)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updateEstadoSolicitado(int idAcceso)", this.getClass().toString()));
                try {
                    rspAcceso.setEsRolledBackIntentado(true);
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updateEstadoSolicitado(int idAcceso)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updateEstadoSolicitado(int idAcceso)", this.getClass().toString()));
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspAcceso.esRolledBackIntentado()) {
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setEsRolledBackExitosamente(false);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso updateCorreo(String correo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RspAcceso updateEstadoHabilitadoPorIdPersona(int idPersona) {
        String metodo = "updateEstadoHabilitadoPorIdPersona(int idPersona)";
        RspAcceso rspAcceso = new RspAcceso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspAcceso.setEsRolledBackIntentado(false);
        rspAcceso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE acceso SET estado = '1' WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspAcceso.setEsRolledBackIntentado(true);
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspAcceso.esRolledBackIntentado()) {
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setEsRolledBackExitosamente(false);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso updateEstadoRegistradoPorIdPersona(int idPersona) {
        String metodo = "updateEstadoRegistradoPorIdPersona(int idPersona)";
        RspAcceso rspAcceso = new RspAcceso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspAcceso.setEsRolledBackIntentado(false);
        rspAcceso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE acceso SET estado = '2' WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspAcceso.setEsRolledBackIntentado(true);
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspAcceso.esRolledBackIntentado()) {
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setEsRolledBackExitosamente(false);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }

    @Override
    public RspAcceso updateEstadoSolicitadoPorIdPersona(int idPersona) {
        String metodo = "updateEstadoSolicitadoPorIdPersona(int idPersona)";
        RspAcceso rspAcceso = new RspAcceso();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspAcceso.setEsConexionAbiertaExitosamente(false);
        rspAcceso.setEsConexionCerradaExitosamente(false);
        rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspAcceso.setEsRolledBackIntentado(false);
        rspAcceso.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspAcceso.setEsConexionAbiertaExitosamente(true);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE acceso SET estado = '3' WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspAcceso.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspAcceso.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspAcceso.setEsRolledBackIntentado(true);
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspAcceso.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspAcceso.esRolledBackIntentado()) {
                    rspAcceso.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspAcceso.setEsConexionCerradaExitosamente(true);
                }
                rspAcceso.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspAcceso;
            }
        } else {
            rspAcceso.setEsRolledBackExitosamente(false);
            rspAcceso.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspAcceso;
        }
    }
}
