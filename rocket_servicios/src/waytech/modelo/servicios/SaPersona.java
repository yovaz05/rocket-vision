package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.beans.sgi.PersonaInsert;
import waytech.modelo.beans.sgi.PersonaUpdate;
import waytech.modelo.beans.sgi.Red;
import waytech.modelo.beans.sgi.TipoPersona;
import waytech.modelo.beans.sgi.Zona;
import waytech.modelo.interfaces.IsaPersona;
import waytech.modelo.interfaces.IsaRed;
import waytech.modelo.interfaces.IsaZona;
import waytech.utilidades.UtilidadSistema;

/** 
 * @since Lunes 21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaPersona implements IsaPersona {

    UtilidadSistema utilidadSistema = new UtilidadSistema();
    IsaRed isaRed = new SaRed();
    IsaZona isaZona = new SaZona();

    private Persona rsPersona(ResultSet rs, Persona persona) throws SQLException {
        persona.setApellido(rs.getString("apellido"));
        persona.setCi(rs.getString("ci"));
        Red red = new Red();
        red = isaRed.getRedPorIdRed(rs.getInt("id_red")).getRed();
        persona.setRed(red);
        persona.setDireccionHabitacion(rs.getString("direccion_habitacion"));
        persona.setDireccionTrabajo(rs.getString("direccion_trabajo"));
        persona.setEsAnfitrion(rs.getBoolean("es_anfitrion"));
        persona.setEsEstaca(rs.getBoolean("es_estaca"));
        persona.setEsLiderCelula(rs.getBoolean("es_lider_celula"));
        persona.setEsLiderRed(rs.getBoolean("es_lider_red"));
        persona.setEsMaestroAcademia(rs.getBoolean("es_maestro_academia"));
        persona.setEsSupervisor(rs.getBoolean("es_supervisor"));
        persona.setEsLiderLanzado(rs.getBoolean("es_lider_lanzado"));
        persona.setEsLiderSupervisor(rs.getBoolean("es_lider_supervisor"));
        persona.setEsDiscipuloEnProceso(rs.getBoolean("es_discipulo_en_proceso"));
        persona.setEstadoCivil(rs.getString("estado_civil"));
        persona.setFacebook(rs.getString("facebook"));
        persona.setIdPersona(rs.getInt("id_persona"));
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setIdTipoPersona(rs.getInt("id_tipo_persona"));
        persona.setIdTipoPersona(tipoPersona);
        Zona zona = new Zona();
        zona = isaZona.getZonaPorIdZona(rs.getInt("id_zona")).getZona();
        persona.setIdZona(zona);
        persona.setNombre(rs.getString("nombre"));
        persona.setProfesion(rs.getString("profesion"));
        persona.setTelefonoHabitacion(rs.getString("telefono_habitacion"));
        persona.setTelefonoMovil(rs.getString("telefono_movil"));
        persona.setTelefonoTrabajo(rs.getString("telefono_trabajo"));
        persona.setCorreo(rs.getString("correo"));
        persona.setTwitter(rs.getString("twitter"));
        persona.setFechaBautizo(rs.getString("fecha_bautizo"));
        persona.setFechaConversion(rs.getString("fecha_conversion"));
        persona.setFechaEncuentro(rs.getString("fecha_encuentro"));
        persona.setFechaGraduacionAcademia(rs.getString("fecha_graduacion_academia"));
        persona.setFechaLanzamiento(rs.getString("fecha_lanzamiento"));
        persona.setFechaNacimiento(rs.getString("fecha_nacimiento"));
        persona.setPorcentajeCompletadoPerfil(rs.getFloat("porcentaje_completado_perfil"));
        persona.setTraza(rs.getString("traza"));
        persona.setEstado(rs.getShort("estado"));
        return persona;
    }

    @Override
    public RspPersona getPersonaPorId(int idPersona) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND id_persona = '" + idPersona + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPersonaPorId(int idPersona)", this.getClass().toString()));
                    if (rs.next()) {
                        Persona persona = new Persona();
                        persona = rsPersona(rs, persona);
                        rspPersona.setPersona(persona);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPersonaPorId(int idPersona)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona getPersonaPorTraza(String traza) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND traza = '" + traza + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPersonaPorTraza(String traza)", this.getClass().toString()));
                    if (rs.next()) {
                        Persona persona = new Persona();
                        persona = rsPersona(rs, persona);
                        rspPersona.setPersona(persona);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPersonaPorTraza(String traza)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona insertPersona(PersonaInsert persona) {
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO persona ("
                        + "id_persona,"
                        + "id_tipo_persona,"
                        + "id_zona,"
                        + "ci,"
                        + "nombre,"
                        + "apellido,"
                        + "direccion_habitacion,"
                        + "direccion_trabajo,"
                        + "fecha_nacimiento,"
                        + "estado_civil,"
                        + "profesion,"
                        + "telefono_movil,"
                        + "telefono_habitacion,"
                        + "telefono_trabajo,"
                        + "facebook,"
                        + "twitter,"
                        + "fecha_conversion,"
                        + "fecha_encuentro,"
                        + "fecha_graduacion_academia,"
                        + "fecha_lanzamiento,"
                        + "fecha_bautizo,"
                        + "estado,"
                        + "traza,"
                        + "es_lider_red,"
                        + "es_lider_celula,"
                        + "es_supervisor,"
                        + "es_estaca,"
                        + "es_maestro_academia,"
                        + "es_anfitrion,"
                        + "es_lider_lanzado,"
                        + "es_lider_supervisor,"
                        + "es_discipulo_en_proceso,"
                        + "porcentaje_completado_perfil)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setInt(2, persona.getIdTipoPersona());
                stmt.setInt(3, persona.getIdZona());
                stmt.setString(4, persona.getCi());
                stmt.setString(5, persona.getNombre());
                stmt.setString(6, persona.getApellido());
                stmt.setString(7, persona.getDireccionHabitacion());
                stmt.setString(8, persona.getDireccionTrabajo());
                stmt.setString(9, persona.getFechaNacimiento());
                stmt.setString(10, persona.getEstadoCivil());
                stmt.setString(11, persona.getProfesion());
                stmt.setString(12, persona.getTelefonoMovil());
                stmt.setString(13, persona.getTelefonoHabitacion());
                stmt.setString(14, persona.getTelefonoTrabajo());
                stmt.setString(15, persona.getFacebook());
                stmt.setString(16, persona.getTwitter());
                stmt.setString(17, persona.getFechaConversion());
                stmt.setString(18, persona.getFechaEncuentro());
                stmt.setString(19, persona.getFechaGraduacionAcademia());
                stmt.setString(20, persona.getFechaLanzamiento());
                stmt.setString(21, persona.getFechaBautizo());
                stmt.setShort(22, Short.valueOf("1"));
                stmt.setString(23, traza);
                stmt.setBoolean(24, persona.esLiderRed());
                stmt.setBoolean(25, persona.esLiderCelula());
                stmt.setBoolean(26, persona.esSupervisor());
                stmt.setBoolean(27, persona.esEstaca());
                stmt.setBoolean(28, persona.esMaestroAcademia());
                stmt.setBoolean(29, persona.esAnfitrion());
                stmt.setBoolean(30, persona.esLiderLanzado());
                stmt.setBoolean(31, persona.esLiderSupervisor());
                stmt.setBoolean(32, persona.esDiscipuloEnProceso());
                stmt.setFloat(33, persona.getPorcentajeCompletadoPerfil());
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertPersona(PersonaInsert persona)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertPersona(PersonaInsert persona)", this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertPersona(PersonaInsert persona)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertPersona(PersonaInsert persona)", this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                    rspPersona.setPersona(getPersonaPorTraza(traza).getPersona());
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona updatePersona(PersonaUpdate persona) {
        String metodo = "updatePersona(PersonaUpdate persona)";
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona SET apellido = '" + persona.getApellido() + "', "
                        + "ci = '" + persona.getCi() + "', "
                        + "direccion_habitacion = '" + persona.getDireccionHabitacion() + "',  "
                        + "direccion_trabajo = '" + persona.getDireccionTrabajo() + "', "
                        + "estado_civil = '" + persona.getEstadoCivil() + "', "
                        + "facebook = '" + persona.getFacebook() + "', "
                        + "fecha_bautizo = '" + persona.getFechaBautizo() + "', "
                        + "fecha_conversion = '" + persona.getFechaConversion() + "', "
                        + "fecha_encuentro = '" + persona.getFechaEncuentro() + "', "
                        + "fecha_graduacion_academia = '" + persona.getFechaGraduacionAcademia() + "', "
                        + "fecha_lanzamiento = '" + persona.getFechaLanzamiento() + "', "
                        + "fecha_nacimiento = '" + persona.getFechaNacimiento() + "', "
                        + "nombre = '" + persona.getNombre() + "', "
                        + "profesion = '" + persona.getProfesion() + "', "
                        + "habitacion = '" + persona.getTelefonoHabitacion() + "', "
                        + "telefono_movil = '" + persona.getTelefonoMovil() + "', "
                        + "telefono_trabajo = '" + persona.getTelefonoTrabajo() + "', "
                        + "twitter = '" + persona.getTwitter() + "', "
                        + "es_lider_red = '" + persona.esLiderRed() + "', "
                        + "es_lider_celula = '" + persona.esLiderCelula() + "', "
                        + "es_supervisor = '" + persona.esSupervisor() + "', "
                        + "es_estaca = '" + persona.esEstaca() + "', "
                        + "es_maestro_academia = '" + persona.esMaestroAcademia() + "', "
                        + "es_anfitrion = '" + persona.esAnfitrion() + "', "
                        + "es_lider_lanzado = '" + persona.esLiderLanzado() + "', "
                        + "es_lider_supervisor = '" + persona.esLiderSupervisor() + "', "
                        + "es_discipulo_en_proceso = '" + persona.esLiderSupervisor() + "', "
                        + "porcentaje_compleatado_perfil = '" + persona.getPorcentajeCompletadoPerfil() + "', "
                        + "id_tipo_persona = '" + persona.getIdTipoPersona() + "', "
                        + "id_zona = '" + persona.getIdZona() + "' "
                        + "WHERE id_persona = '" + persona.getIdPersona() + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona deletePersona(int idPersona) {
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona SET estado = '0' "
                        + "WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), " deletePersona(int idPersona)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, " deletePersona(int idPersona)", this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", " deletePersona(int idPersona)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, " deletePersona(int idPersona)", this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona renovarPersona(int idPersona) {
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona SET estado = '1' "
                        + "WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), " renovarPersona(int idPersona)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, " renovarPersona(int idPersona)", this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", " renovarPersona(int idPersona)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, " renovarPersona(int idPersona)", this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersona() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersona()", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersona()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaPorLimite(int limiteDeRegistros) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaPorLimite(int limiteDeRegistros)", this.getClass().toString()));
                    int i = 0;
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                        i++;
                        if (i >= 50) {
                            break;
                        }
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaPorLimite(int limiteDeRegistros)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaFiltradoPorNombre(String nombre) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND nombre LIKE = '%" + nombre + "%'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaFiltradoPorNombre(String nombre)", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaFiltradoPorNombre(String nombre)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }

    }

    @Override
    public RspPersona listPersonaFiltradoPorApellido(String apellido) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND apellido LIKE = '%" + apellido + "%'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaFiltradoPorApellido(String apellido)", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaFiltradoPorApellido(String apellido)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaFiltradoPorCedula(String cedula) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND cedula LIKE = '%" + cedula + "%'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaFiltradoPorCedula(String cedula)", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaFiltradoPorCedula(String cedula)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaFiltradoPorDireccionHabitacion(String direccionHabitacion) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND direccion_habitacion LIKE = '%" + direccionHabitacion + "%'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaFiltradoPorDireccionHabitacion(String direccionHabitacion)", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaFiltradoPorDireccionHabitacion(String direccionHabitacion)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaFiltradoPorDireccionTrabajo(String direccionTrabajo) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND direccion_trabajo LIKE = '%" + direccionTrabajo + "%'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaFiltradoPorDireccionTrabajo(String direccionTrabajo)", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaFiltradoPorDireccionTrabajo(String direccionTrabajo)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaLideresDeRed() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND es_lider_red = 'TRUE'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaLideresDeRed()", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaLideresDeRed()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaLideresDeCelula() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND es_lider_celula = 'TRUE'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaLideresDeCelula()", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaLideresDeCelula()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaSupervisores() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND es_supervisor = 'TRUE'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaSupervisores()", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaSupervisores()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaEstaca() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND es_estaca = 'TRUE'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaEstaca()", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaEstaca()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaMaestrosDeAcademia() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND es_maestro_academia = 'TRUE'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaMaestrosDeAcademia()", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaMaestrosDeAcademia()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaAnfitriones() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND es_anfitrion = 'TRUE'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaAnfitriones()", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaAnfitriones()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaLideresLanzados() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND es_lider_lanzado = 'TRUE'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaLideresLanzados()", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaLideresLanzados()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaLiderSupervizor() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND es_lider_supervisor = 'TRUE'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaLiderSupervizor()", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaLiderSupervizor()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaDiscipulosEnProceso() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND es_discipulo_en_proceso = 'TRUE'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaDiscipulosEnProceso()", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaDiscipulosEnProceso()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona getLiderLanzadoPorIdPersona(int idPersona) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona"
                    + " WHERE estado = 1"
                    + " AND es_lider_lanzado = 1"
                    + " AND id_persona = '" + idPersona + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getLiderLanzadoPorIdPersona(int idPersona)", this.getClass().toString()));
                    if (rs.next()) {
                        Persona persona = new Persona();
                        persona = rsPersona(rs, persona);
                        rspPersona.setPersona(persona);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getLiderLanzadoPorIdPersona(int idPersona)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona esCedulaExistente(String cedula) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);

        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND ci = '" + cedula + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "esCedulaExistente(String cedula)", this.getClass().toString()));
                    if (rs.next()) {
                        rspPersona.setEsCedulaExistente(true);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "esCedulaExistente(String cedula)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona getPersonaPorCedula(String cedula) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND ci = '" + cedula + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPersonaPorCedula(String cedula)", this.getClass().toString()));
                    if (rs.next()) {
                        Persona persona = new Persona();
                        persona = rsPersona(rs, persona);
                        rspPersona.setPersona(persona);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPersonaPorCedula(String cedula)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaCualquierLider() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona"
                    + " WHERE estado = 1 AND(es_lider_red = 'TRUE' OR es_lider_celula = 'TRUE' OR es_supervisor  = 'TRUE' OR es_estaca  = 'TRUE' OR es_maestro_academia  = 'TRUE' OR es_anfitrion  = 'TRUE' OR es_lider_lanzado  = 'TRUE' OR es_lider_supervisor  = 'TRUE')";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaLideresLanzados()", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaLideresLanzados()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    /**
     * lista todos los líderes lanzados
     * @return 
     */
    @Override
    public RspPersona listPersonaLiderLanzado() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona"
                    + " WHERE estado = 1"
                    + " AND"
                    + " es_lider_lanzado = 1"
                    + " ORDER BY id_red ASC, nombre ASC";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaLideresLanzados()", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaLideresLanzados()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona listPersonaCualquierLiderNombre(String nombre) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        List<Persona> todosLosPersonas = new ArrayList<Persona>();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND(es_lider_red = 'TRUE' OR es_lider_celula = 'TRUE' OR es_supervisor  = 'TRUE' OR es_estaca  = 'TRUE' OR es_maestro_academia  = 'TRUE' OR es_anfitrion  = 'TRUE' OR es_lider_lanzado  = 'TRUE' OR es_lider_supervisor  = 'TRUE') AND nombre LIKE '%" + nombre + "%'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaCualquierLiderNombre(String nombre)", this.getClass().toString()));
                    while (rs.next()) {
                        Persona acceso = new Persona();
                        acceso = rsPersona(rs, acceso);
                        todosLosPersonas.add(acceso);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaCualquierLiderNombre(String nombre)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersona.setTodasLasPersonas(todosLosPersonas);
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona esTelefonoMovilExistente(String telefonoMovil) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RspPersona esTelefonoHabitacionExistente(String telefonoHabitacion) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RspPersona esTelefonoTrabajoExistente(String telefonoTrabajo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RspPersona esCodigoSecretoExistente(String cedula, String codigoSecreto) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        rspPersona.setEsCodigoExistente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND ci = '" + cedula + "' AND codigo_secreto = '" + codigoSecreto + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "esCodigoSecretoExistente(String cedula, String codigoSecreto)", this.getClass().toString()));
                    if (rs.next()) {
                        rspPersona.setEsCodigoExistente(true);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "esCodigoSecretoExistente(String cedula, String codigoSecreto)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona insertPersonaLiderBasico(String cedula, String nombre, int idRed) {
        String metodo = "insertPersonaLiderBasico(String cedula, String nombre, int idRed)";
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO persona ("
                        + "id_persona,"
                        + "id_tipo_persona,"
                        + "id_red,"
                        + "id_zona,"
                        + "ci,"
                        + "nombre,"
                        + "apellido,"
                        + "direccion_habitacion,"
                        + "direccion_trabajo,"
                        + "fecha_nacimiento,"
                        + "estado_civil,"
                        + "profesion,"
                        + "telefono_movil,"
                        + "telefono_habitacion,"
                        + "telefono_trabajo,"
                        + "correo,"
                        + "facebook,"
                        + "twitter,"
                        + "fecha_conversion,"
                        + "fecha_encuentro,"
                        + "fecha_graduacion_academia,"
                        + "fecha_lanzamiento,"
                        + "fecha_bautizo,"
                        + "estado,"
                        + "traza,"
                        + "es_lider_red,"
                        + "es_lider_celula,"
                        + "es_supervisor,"
                        + "es_estaca,"
                        + "es_maestro_academia,"
                        + "es_anfitrion,"
                        + "es_lider_lanzado,"
                        + "es_lider_supervisor,"
                        + "es_discipulo_en_proceso,"
                        + "porcentaje_completado_perfil,"
                        + "observaciones)"
                        + " VALUES ("
                        + "?,?,?,?,?,?,?,?,?,?,"
                        + "?,?,?,?,?,?,?,?,?,?,"
                        + "?,?,?,?,?,?,?,?,?,?,"
                        + "?,?,?,?,?,?)";
                System.out.println("SaPersona." + metodo + ".consultaSQL:");
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                int i = 1;
                stmt.setInt(i++, 0);
                stmt.setInt(i++, 3);
                stmt.setInt(i++, idRed);
                stmt.setInt(i++, 1);
                stmt.setString(i++, cedula);
                stmt.setString(i++, nombre);
                stmt.setString(i++, "");
                stmt.setString(i++, "");
                stmt.setString(i++, "");
                stmt.setString(i++, "1900-01-01");
                stmt.setString(i++, "");
                stmt.setString(i++, "");
                stmt.setString(i++, "");
                stmt.setString(i++, "");
                stmt.setString(i++, "");
                stmt.setString(i++, "");
                stmt.setString(i++, "");
                stmt.setString(i++, "");
                stmt.setString(i++, "1900-01-01");
                stmt.setString(i++, "1900-01-01");
                stmt.setString(i++, "1900-01-01");
                stmt.setString(i++, "1900-01-01");
                stmt.setString(i++, "1900-01-01");
                stmt.setShort(i++, Short.valueOf("1"));
                stmt.setString(i++, traza);
                stmt.setBoolean(i++, false);
                stmt.setBoolean(i++, false);
                stmt.setBoolean(i++, false);
                stmt.setBoolean(i++, false);
                stmt.setBoolean(i++, false);
                stmt.setBoolean(i++, false);
                stmt.setBoolean(i++, true);
                stmt.setBoolean(i++, false);
                stmt.setBoolean(i++, false);
                stmt.setFloat(i++, 5);
                stmt.setString(i++, "");
                System.out.println("" + consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                    rspPersona.setPersona(getPersonaPorTraza(traza).getPersona());
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona updateCedula(int idPersona, String cedula) {
        String metodo = "updateCedula(int idPersona, String Cedula)";
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona SET ci = '" + cedula + "'"
                        + " WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona updateNombre(int idPersona, String nombre) {
        String metodo = "updateNombre(int idPersona, String nombre)";
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona SET nombre = '" + nombre + "'"
                        + " WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona updateRed(int idPersona, int idRed) {
        String metodo = "updateRed(int idPersona, int idRed)";
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona"
                        + " SET id_red = '" + idRed + "'"
                        + " WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona updateTelefonoMovil(int idPersona, String telefonoMovil) {
        String metodo = "updateTelefonoMovil(int idPersona, String telefonoMovil)";
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona SET telefono_movil = '" + telefonoMovil + "'"
                        + " WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona updateCorreo(int idPersona, String correo) {
        String metodo = "updateCorreo(int idPersona, String correo)";
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona SET correo = '" + correo + "' "
                        + "WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona updateZona(int idPersona, int idZona) {
        String metodo = "updateZona(int idPersona, int idZona)";
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona SET id_zona = '" + idZona + "'"
                        + " WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona updateDireccion(int idPersona, String direccion) {
        String metodo = "updateDireccion(int idPersona, String direccion)";
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona"
                        + " SET direccion_habitacion = '" + direccion + "'"
                        + " WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona updateObservaciones(int idPersona, String observaciones) {
        String metodo = "updateObservaciones(int idPersona, String observaciones)";
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona"
                        + " SET observaciones = '" + observaciones + "'"
                        + " WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona esLiderRed(int idPersona) {
        String metodo = "esLiderRed(int idPersona)";
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);

        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND es_lider_red = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), metodo, this.getClass().toString()));
                    if (rs.next()) {
                        rspPersona.setEsCedulaExistente(true);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona esLiderCelula(int idPersona) {
        String metodo = "esLiderRed(int idPersona)";
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersona rspPersona = new RspPersona();
        //INICIALIZAR VARIABLES
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);

        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona WHERE estado = 1 AND es_lider_celula = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), metodo, this.getClass().toString()));
                    if (rs.next()) {
                        rspPersona.setEsCedulaExistente(true);
                    }
                }
            } catch (SQLException e) {
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona updatePersonaALiderCelula(int idPersona) {
        String metodo = "updatePersonaALiderCelula(int idPersona)";
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona"
                        + " SET es_lider_celula = '1' "
                        + " WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }

    @Override
    public RspPersona updatePersonaALiderRed(int idPersona) {
        String metodo = "updatePersonaALiderRed(int idPersona)";
        RspPersona rspPersona = new RspPersona();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersona.setEsConexionAbiertaExitosamente(false);
        rspPersona.setEsConexionCerradaExitosamente(false);
        rspPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersona.setEsRolledBackIntentado(false);
        rspPersona.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersona.setEsConexionAbiertaExitosamente(true);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona"
                        + " SET es_lider_red = '1' "
                        + " WHERE id_persona = '" + idPersona + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspPersona.setEsRolledBackIntentado(true);
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersona.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspPersona.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersona.esRolledBackIntentado()) {
                    rspPersona.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersona.setEsConexionCerradaExitosamente(true);
                }
                rspPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersona;
            }
        } else {
            rspPersona.setEsRolledBackExitosamente(false);
            rspPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersona;
        }
    }
}
