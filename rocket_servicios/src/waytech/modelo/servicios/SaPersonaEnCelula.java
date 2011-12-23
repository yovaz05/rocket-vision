package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Celula;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.beans.sgi.PersonaEnCelula;
import waytech.modelo.beans.sgi.PersonaEnCelulaInsert;
import waytech.modelo.beans.sgi.PersonaEnCelulaUpdate;
import waytech.modelo.interfaces.IsaCelula;
import waytech.modelo.interfaces.IsaPersona;
import waytech.modelo.interfaces.IsaPersonaEnCelula;
import waytech.utilidades.UtilidadSistema;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaPersonaEnCelula implements IsaPersonaEnCelula {

    UtilidadSistema utilidadSistema = new UtilidadSistema();
    IsaCelula isaCelula = new SaCelula();
    IsaPersona isaPersona = new SaPersona();

    private PersonaEnCelula rsPersonaEnCelula(ResultSet rs, PersonaEnCelula personaEnCelula) throws SQLException {
        personaEnCelula.setIdPersonaEnCelula(rs.getInt("id_persona_en_celula"));
        personaEnCelula.setEsLiderCelula(rs.getBoolean("es_lider_celula"));
        personaEnCelula.setTraza(rs.getString("traza"));
        personaEnCelula.setEstado(rs.getShort("estado"));
        Celula celula = new Celula();
        celula = isaCelula.getCelulaPorIdCelula(rs.getInt("id_celula")).getCelula();
        personaEnCelula.setIdCelula(celula);
        Persona persona = new Persona();
        persona = isaPersona.getPersonaPorId(rs.getInt("id_persona")).getPersona();
        personaEnCelula.setIdPersona(persona);
        return personaEnCelula;
    }

    @Override
    public RspPersonaEnCelula getPersonaEnCelulaPorIdPersonaEnCelula(int idPersonaEnCelula) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersonaEnCelula rspPersonaEnCelula = new RspPersonaEnCelula();
        //INICIALIZAR VARIABLES
        rspPersonaEnCelula.setEsConexionAbiertaExitosamente(false);
        rspPersonaEnCelula.setEsConexionCerradaExitosamente(false);
        rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersonaEnCelula.setEsConexionAbiertaExitosamente(true);
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona_en_celula WHERE estado = 1 AND id_persona_en_celula = '" + idPersonaEnCelula + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPersonaEnCelulaPorIdPersonaEnCelula(int idPersonaEnCelula)", this.getClass().toString()));
                    if (rs.next()) {
                        PersonaEnCelula personaEnCelula = new PersonaEnCelula();
                        personaEnCelula = rsPersonaEnCelula(rs, personaEnCelula);
                        rspPersonaEnCelula.setPersonaEnCelula(personaEnCelula);
                    }
                }
            } catch (SQLException e) {
                rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPersonaEnCelulaPorIdPersonaEnCelula(int idPersonaEnCelula)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersonaEnCelula.setEsConexionCerradaExitosamente(true);
                }
                rspPersonaEnCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersonaEnCelula;
            }
        } else {
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersonaEnCelula;
        }
    }

    @Override
    public RspPersonaEnCelula listPersonaEnCelula() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersonaEnCelula rspPersonaEnCelula = new RspPersonaEnCelula();
        List<PersonaEnCelula> todosLosPersonaEnCelulas = new ArrayList<PersonaEnCelula>();
        //INICIALIZAR VARIABLES
        rspPersonaEnCelula.setEsConexionAbiertaExitosamente(false);
        rspPersonaEnCelula.setEsConexionCerradaExitosamente(false);
        rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersonaEnCelula.setEsConexionAbiertaExitosamente(true);
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona_en_celula WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaEnCelula()", this.getClass().toString()));
                    while (rs.next()) {
                        PersonaEnCelula personaEnCelula = new PersonaEnCelula();
                        personaEnCelula = rsPersonaEnCelula(rs, personaEnCelula);
                        todosLosPersonaEnCelulas.add(personaEnCelula);
                    }
                }
            } catch (SQLException e) {
                rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaEnCelula()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersonaEnCelula.setEsConexionCerradaExitosamente(true);
                }
                rspPersonaEnCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersonaEnCelula.setAllPersonaEnCelulas(todosLosPersonaEnCelulas);
                return rspPersonaEnCelula;
            }
        } else {
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersonaEnCelula;
        }
    }

    @Override
    public RspPersonaEnCelula updatePersonaEnCelula(PersonaEnCelulaUpdate personaEnCelula) {
        RspPersonaEnCelula rspPersonaEnCelula = new RspPersonaEnCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersonaEnCelula.setEsConexionAbiertaExitosamente(false);
        rspPersonaEnCelula.setEsConexionCerradaExitosamente(false);
        rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersonaEnCelula.setEsRolledBackIntentado(false);
        rspPersonaEnCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersonaEnCelula.setEsConexionAbiertaExitosamente(true);
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE persona_en_celula SET id_celula = '" + personaEnCelula.getIdCelula() + "', id_persona = '" + personaEnCelula.getIdPersona() + "', es_lider_celula = '" + personaEnCelula.esLiderCelula() + "', WHERE id_persona_en_celula = '" + personaEnCelula.getIdPersonaEnCelula() + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "updatePersonaEnCelula(PersonaEnCelulaUpdate personaEnCelula)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "updatePersonaEnCelula(PersonaEnCelulaUpdate personaEnCelula)", this.getClass().toString()));
                try {
                    rspPersonaEnCelula.setEsRolledBackIntentado(true);
                    rspPersonaEnCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "updatePersonaEnCelula(PersonaEnCelulaUpdate personaEnCelula)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersonaEnCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "updatePersonaEnCelula(PersonaEnCelulaUpdate personaEnCelula)", this.getClass().toString()));
                    rspPersonaEnCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersonaEnCelula.esRolledBackIntentado()) {
                    rspPersonaEnCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersonaEnCelula.setEsConexionCerradaExitosamente(true);
                }
                rspPersonaEnCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersonaEnCelula;
            }
        } else {
            rspPersonaEnCelula.setEsRolledBackExitosamente(false);
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersonaEnCelula;
        }
    }

    @Override
    public RspPersonaEnCelula insertPersonaEnCelula(PersonaEnCelulaInsert personaEnCelula) {
        RspPersonaEnCelula rspPersonaEnCelula = new RspPersonaEnCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersonaEnCelula.setEsConexionAbiertaExitosamente(false);
        rspPersonaEnCelula.setEsConexionCerradaExitosamente(false);
        rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersonaEnCelula.setEsRolledBackIntentado(false);
        rspPersonaEnCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersonaEnCelula.setEsConexionAbiertaExitosamente(true);
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO persona_en_celula ("
                        + "id_persona_en_celula,"
                        + "id_celula,"
                        + "id_persona,"
                        + "es_lider_celula,"
                        + "traza,"
                        + "estado)"
                        + " VALUES (?,?,?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setInt(2, personaEnCelula.getIdCelula());
                stmt.setInt(3, personaEnCelula.getIdPersona());
                stmt.setBoolean(4, personaEnCelula.esLiderCelula());
                stmt.setString(5, traza);
                stmt.setShort(6, Short.valueOf("1"));
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "insertPersonaEnCelula(PersonaEnCelulaInsert personaEnCelula)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "insertPersonaEnCelula(PersonaEnCelulaInsert personaEnCelula)", this.getClass().toString()));
                try {
                    rspPersonaEnCelula.setEsRolledBackIntentado(true);
                    rspPersonaEnCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "insertPersonaEnCelula(PersonaEnCelulaInsert personaEnCelula)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersonaEnCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "insertPersonaEnCelula(PersonaEnCelulaInsert personaEnCelula)", this.getClass().toString()));
                    rspPersonaEnCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersonaEnCelula.esRolledBackIntentado()) {
                    rspPersonaEnCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersonaEnCelula.setEsConexionCerradaExitosamente(true);
                    rspPersonaEnCelula.setPersonaEnCelula(getPersonaEnCelulaPorTraza(traza).getPersonaEnCelula());
                }
                rspPersonaEnCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersonaEnCelula;
            }
        } else {
            rspPersonaEnCelula.setEsRolledBackExitosamente(false);
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersonaEnCelula;
        }
    }

    @Override
    public RspPersonaEnCelula deletePersonaEnCelula(int idCelula, int idPersona) {
        RspPersonaEnCelula rspPersonaEnCelula = new RspPersonaEnCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspPersonaEnCelula.setEsConexionAbiertaExitosamente(false);
        rspPersonaEnCelula.setEsConexionCerradaExitosamente(false);
        rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspPersonaEnCelula.setEsRolledBackIntentado(false);
        rspPersonaEnCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersonaEnCelula.setEsConexionAbiertaExitosamente(true);
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "DELETE FROM persona_en_celula"
                        + " WHERE id_persona = '" + idPersona + "'"
                        + " AND id_celula = '" + idCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deletePersonaEnCelula(int idPersonaEnCelula)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deletePersonaEnCelula(int idPersona, int idCelula)", this.getClass().toString()));
                try {
                    rspPersonaEnCelula.setEsRolledBackIntentado(true);
                    rspPersonaEnCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deletePersonaEnCelula(int idPersona, int idCelula)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspPersonaEnCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deletePersonaEnCelula(int idPersona, int idCelula)", this.getClass().toString()));
                    rspPersonaEnCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspPersonaEnCelula.esRolledBackIntentado()) {
                    rspPersonaEnCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspPersonaEnCelula.setEsConexionCerradaExitosamente(true);
                }
                rspPersonaEnCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersonaEnCelula;
            }
        } else {
            rspPersonaEnCelula.setEsRolledBackExitosamente(false);
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersonaEnCelula;
        }
    }

    @Override
    public RspPersonaEnCelula getPersonaEnCelulaPorTraza(String traza) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersonaEnCelula rspPersonaEnCelula = new RspPersonaEnCelula();
        //INICIALIZAR VARIABLES
        rspPersonaEnCelula.setEsConexionAbiertaExitosamente(false);
        rspPersonaEnCelula.setEsConexionCerradaExitosamente(false);
        rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersonaEnCelula.setEsConexionAbiertaExitosamente(true);
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona_en_celula WHERE estado = 1 AND traza = '" + traza + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getPersonaEnCelulaPorTraza(String traza)", this.getClass().toString()));
                    if (rs.next()) {
                        PersonaEnCelula personaEnCelula = new PersonaEnCelula();
                        personaEnCelula = rsPersonaEnCelula(rs, personaEnCelula);
                        rspPersonaEnCelula.setPersonaEnCelula(personaEnCelula);
                    }
                }
            } catch (SQLException e) {
                rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getPersonaEnCelulaPorTraza(String traza)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersonaEnCelula.setEsConexionCerradaExitosamente(true);
                }
                rspPersonaEnCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspPersonaEnCelula;
            }
        } else {
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersonaEnCelula;
        }
    }

    @Override
    public RspPersonaEnCelula listPersonaEnCelulaPorIdCelula(int idCelula) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspPersonaEnCelula rspPersonaEnCelula = new RspPersonaEnCelula();
        List<PersonaEnCelula> todosLosPersonaEnCelulas = new ArrayList<PersonaEnCelula>();
        //INICIALIZAR VARIABLES
        rspPersonaEnCelula.setEsConexionAbiertaExitosamente(false);
        rspPersonaEnCelula.setEsConexionCerradaExitosamente(false);
        rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspPersonaEnCelula.setEsConexionAbiertaExitosamente(true);
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM persona_en_celula WHERE estado = 1 AND id_celula = '" + idCelula + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspPersonaEnCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listPersonaEnCelulaPorIdCelula(int idCelula)", this.getClass().toString()));
                    while (rs.next()) {
                        PersonaEnCelula personaEnCelula = new PersonaEnCelula();
                        personaEnCelula = rsPersonaEnCelula(rs, personaEnCelula);
                        todosLosPersonaEnCelulas.add(personaEnCelula);
                    }
                }
            } catch (SQLException e) {
                rspPersonaEnCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listPersonaEnCelulaPorIdCelula(int idCelula)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspPersonaEnCelula.setEsConexionCerradaExitosamente(true);
                }
                rspPersonaEnCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspPersonaEnCelula.setAllPersonaEnCelulas(todosLosPersonaEnCelulas);
                return rspPersonaEnCelula;
            }
        } else {
            rspPersonaEnCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspPersonaEnCelula;
        }
    }

 }
