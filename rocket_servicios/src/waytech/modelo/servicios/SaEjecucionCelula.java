package waytech.modelo.servicios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.Celula;
import waytech.modelo.beans.sgi.EjecucionCelula;
import waytech.modelo.beans.sgi.EjecucionCelulaInsert;
import waytech.modelo.beans.sgi.EjecucionCelulaUpdate;
import waytech.modelo.beans.sgi.Semana;
import waytech.modelo.interfaces.IsaCelula;
import waytech.modelo.interfaces.IsaEjecucionCelula;
import waytech.modelo.interfaces.IsaSemana;
import waytech.utilidades.UtilidadSistema;

/** 
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public class SaEjecucionCelula implements IsaEjecucionCelula {

    UtilidadSistema utilidadSistema = new UtilidadSistema();
    IsaCelula isaCelula = new SaCelula();
    IsaSemana isaSemana = new SaSemana();

    private EjecucionCelula rsEjecucionCelula(ResultSet rs, EjecucionCelula ejecucionCelula) throws SQLException {
        ejecucionCelula.setIdEjecucionCelula(rs.getInt("id_ejecucion_celula"));
        Celula celula = new Celula();
        celula = isaCelula.getCelulaPorIdCelula(rs.getInt("id_celula")).getCelula();
        ejecucionCelula.setIdCelula(celula);
        Semana semana = new Semana();
        semana = isaSemana.getSemanaPorIdSemana(rs.getInt("id_semana")).getSemana();
        ejecucionCelula.setIdSemana(semana);
        ejecucionCelula.setFecha(rs.getString("fecha"));
        ejecucionCelula.setNuevosInvitados(rs.getInt("nuevos_invitados"));
        ejecucionCelula.setReconciliados(rs.getInt("reconciliados"));
        ejecucionCelula.setVisitas(rs.getInt("visitas"));
        ejecucionCelula.setNumeroIntegrantes(rs.getInt("numero_integrantes"));
        ejecucionCelula.setConvertidos(rs.getInt("convertidos"));
        ejecucionCelula.setObservaciones(rs.getString("observaciones"));
        ejecucionCelula.setEstado(rs.getShort("estado"));
        ejecucionCelula.setTraza(rs.getString("traza"));
        ejecucionCelula.setAmigosSoloAsistenGrupo(rs.getInt("amigos_solo_asisten_grupo"));
        ejecucionCelula.setIntegrantesCasaOracion(rs.getInt("integrantes_casa_oracion"));
        ejecucionCelula.setIntegrantesOtrasIglesias(rs.getInt("integrantes_otras_iglesias"));
        ejecucionCelula.setAsistenciaDomingoAnterior(rs.getInt("asistencia_domingo_anterior"));
        ejecucionCelula.setOfrenda(rs.getDouble("ofrenda"));
        return ejecucionCelula;
    }

    /**
     * Recupera una ejecución de célula por el id de célula,
     * sin importar el estado
     * @param idEjecucionCelula
     * @return 
     */
    @Override
    public RspEjecucionCelula getEjecucionCelulaPorIdEjecucionCelula(int idEjecucionCelula) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        //INICIALIZAR VARIABLES
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM ejecucion_celula"
                               + " WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getEjecucionCelulaPorIdEjecucionCelula(int idEjecucionCelula)", this.getClass().toString()));
                    if (rs.next()) {
                        EjecucionCelula EjecucionCelula = new EjecucionCelula();
                        EjecucionCelula = rsEjecucionCelula(rs, EjecucionCelula);
                        rspEjecucionCelula.setEjecucionCelula(EjecucionCelula);
                    }
                }
            } catch (SQLException e) {
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getEjecucionCelulaPorIdEjecucionCelula(int idEjecucionCelula)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula listEjecucionCelula() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        List<EjecucionCelula> todosLosEjecucionCelulas = new ArrayList<EjecucionCelula>();
        //INICIALIZAR VARIABLES
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM ejecucion_celula WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listEjecucionCelula()", this.getClass().toString()));
                    while (rs.next()) {
                        EjecucionCelula EjecucionCelula = new EjecucionCelula();
                        EjecucionCelula = rsEjecucionCelula(rs, EjecucionCelula);
                        todosLosEjecucionCelulas.add(EjecucionCelula);
                    }
                }
            } catch (SQLException e) {
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listEjecucionCelula()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspEjecucionCelula.setTodosLosEjecucionCelulas(todosLosEjecucionCelulas);
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateEjecucionCelula(EjecucionCelulaUpdate ejecucionCelula) {
        String metodo = "updateEjecucionCelula(EjecucionCelulaUpdate ejecucionCelula)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula SET fecha = '" + ejecucionCelula.getFecha() + "', observaciones = '" + ejecucionCelula.getObservaciones() + "', convertidos = '" + ejecucionCelula.getConvertidos() + "', id_celula = '" + ejecucionCelula.getIdCelula() + "', id_semana = '" + ejecucionCelula.getIdSemana() + "', nuevos_invitados = '" + ejecucionCelula.getNuevosInvitados() + "', numeros_integrantes = '" + ejecucionCelula.getNumeroIntegrantes() + "', reconciliados= '" + ejecucionCelula.getReconciliados() + "', visitas = '" + ejecucionCelula.getVisitas() + "', amigos_solo_asisten_grupo = '" + ejecucionCelula.getAmigosSoloAsistenGrupo() + "', integrantes_casa_oracion = '" + ejecucionCelula.getIntegrantesCasaOracion() + "', integrantes_otras_iglesias = '" + ejecucionCelula.getIntegrantesOtrasIglesias() + "',  asistencia_domingo_anterior = '" + ejecucionCelula.getAsistenciaDomingoAnterior() + "', ofrenda = '" + ejecucionCelula.getOfrenda() + "' WHERE id_ejecucion_celula = '" + ejecucionCelula.getIdEjecucionCelula() + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula insertEjecucionCelula(EjecucionCelulaInsert ejecucionCelula) {
        String metodo = "insertEjecucionCelula(EjecucionCelulaInsert ejecucionCelula)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO ejecucion_celula ("
                        + "id_ejecucion_celula,"
                        + "id_celula,"
                        + "id_semana,"
                        + "fecha,"
                        + "nuevos_invitados,"
                        + "reconciliados,"
                        + "visitas,"
                        + "numero_integrantes,"
                        + "convertidos,"
                        + "observaciones,"
                        + "traza,"
                        + "estado,"
                        + "amigos_solo_asisten_grupo,"
                        + "integrantes_casa_oracion,"
                        + "integrantes_otras_iglesia,"
                        + "asistencia_domingo_anterior,"
                        + "ofrenda)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setInt(2, ejecucionCelula.getIdCelula());
                stmt.setInt(3, ejecucionCelula.getIdSemana());
                stmt.setString(4, ejecucionCelula.getFecha());
                stmt.setInt(5, ejecucionCelula.getNuevosInvitados());
                stmt.setInt(6, ejecucionCelula.getReconciliados());
                stmt.setInt(7, ejecucionCelula.getVisitas());
                stmt.setInt(8, ejecucionCelula.getNumeroIntegrantes());
                stmt.setInt(9, ejecucionCelula.getConvertidos());
                stmt.setString(10, ejecucionCelula.getObservaciones());
                stmt.setString(11, traza);
                stmt.setShort(12, Short.valueOf("1"));
                stmt.setInt(13, ejecucionCelula.getAmigosSoloAsistenGrupo());
                stmt.setInt(14, ejecucionCelula.getIntegrantesCasaOracion());
                stmt.setInt(15, ejecucionCelula.getIntegrantesOtrasIglesias());
                stmt.setInt(16, ejecucionCelula.getAsistenciaDomingoAnterior());
                stmt.setDouble(17, ejecucionCelula.getOfrenda());
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                    rspEjecucionCelula.setEjecucionCelula(getEjecucionCelulaPorTraza(traza).getEjecucionCelula());
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula deleteEjecucionCelula(int idEjecucionCelula) {
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula SET estado = '0' WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), "deleteEjecucionCelula(int idEjecucionCelula)", this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "deleteEjecucionCelula(int idEjecucionCelula)", this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", "deleteEjecucionCelula(int idEjecucionCelula)", this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, "deleteEjecucionCelula(int idEjecucionCelula)", this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula getEjecucionCelulaPorTraza(String traza) {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        //INICIALIZAR VARIABLES
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM ejecucion_celula WHERE estado = 1 AND traza = '" + traza + "'";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "getEjecucionCelulaPorTraza(String traza)", this.getClass().toString()));
                    if (rs.next()) {
                        EjecucionCelula EjecucionCelula = new EjecucionCelula();
                        EjecucionCelula = rsEjecucionCelula(rs, EjecucionCelula);
                        rspEjecucionCelula.setEjecucionCelula(EjecucionCelula);
                    }
                }
            } catch (SQLException e) {
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "getEjecucionCelulaPorTraza(String traza)", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula insertEjecucionCelulaDiaSemana(int idCelula) {
        String metodo = "insertEjecucionCelulaDiaSemana(int idCelula)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            String traza = utilidadSistema.generarTraza();
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "INSERT INTO ejecucion_celula ("
                        + "id_ejecucion_celula,"
                        + "id_celula,"
                        + "id_semana,"
                        + "fecha,"
                        + "nuevos_invitados,"
                        + "reconciliados,"
                        + "visitas,"
                        + "numero_integrantes,"
                        + "convertidos,"
                        + "observaciones,"
                        + "traza,"
                        + "estado,"
                        + "amigos_solo_asisten_grupo,"
                        + "integrantes_casa_oracion,"
                        + "integrantes_otras_iglesia,"
                        + "asistencia_domingo_anterior,"
                        + "ofrenda)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                stmt.setInt(1, 0);
                stmt.setInt(2, idCelula);
                stmt.setInt(3, 1);
                stmt.setString(4, "1970-01-01");
                stmt.setInt(5, 0);
                stmt.setInt(6, 0);
                stmt.setInt(7, 0);
                stmt.setInt(8, 0);
                stmt.setInt(9, 0);
                stmt.setString(10, "NA");
                stmt.setString(11, traza);
                stmt.setShort(12, Short.valueOf("1"));
                stmt.setInt(13, 0);
                stmt.setInt(14, 0);
                stmt.setInt(15, 0);
                stmt.setInt(16, 0);
                stmt.setDouble(17, 0);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                    rspEjecucionCelula.setEjecucionCelula(getEjecucionCelulaPorTraza(traza).getEjecucionCelula());
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateIdCelula(int idEjecucionCelula, int idCelula) {
        String metodo = "updateIdCelula(int idEjecucionCelula, int idCelula)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula SET id_celula = '" + idCelula + "' WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateIdSemana(int idEjecucionCelula, int idSemana) {
        String metodo = "updateIdSemana(int idEjecucionCelula, int idSemana)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula SET id_semana = '" + idSemana + "' WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateNumeroInvitados(int idEjecucionCelula, int nuevosInvitados) {
        String metodo = "updateNumeroInvitados(int idEjecucionCelula, int nuevosInvitados)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula"
                        + " SET nuevos_invitados = '" + nuevosInvitados + "'"
                        + " WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateReconciliados(int idEjecucionCelula, int reconciliados) {
        String metodo = "updateReconciliados(int idEjecucionCelula, int reconciliados)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula"
                        + " SET reconciliados = '" + reconciliados + "'"
                        + " WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateVisitas(int idEjecucionCelula, int visitas) {
        String metodo = "updateVisitas(int idEjecucionCelula, int visitas)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula"
                        + " SET visitas = '" + visitas + "'"
                        + " WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateNumeroIntegrantes(int idEjecucionCelula, int numeroIntegrantes) {
        String metodo = "updateNumeroIntegrantes(int idEjecucionCelula, int numeroIntegrantes)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula"
                        + " SET numero_integrantes = '" + numeroIntegrantes + "'"
                        + " WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateConvertidos(int idEjecucionCelula, int convertidos) {
        String metodo = "updateConvertidos(int idEjecucionCelula, int convertidos)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula"
                        + " SET convertidos = '" + convertidos + "'"
                        + " WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateObservaciones(int idEjecucionCelula, String observaciones) {
        String metodo = "updateObservaciones(int idEjecucionCelula, String observaciones)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula"
                        + " SET observaciones = '" + observaciones + "'"
                        + " WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateAmigosSoloAsistenGrupo(int idEjecucionCelula, int amigosSoloAsistenGrupo) {
        String metodo = "updateAmigosSoloAsistenGrupos(int idEjecucionCelula, int amigosSoloAsistenGrupo)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula"
                        + " SET amigos_solo_asisten_grupo = '" + amigosSoloAsistenGrupo + "'"
                        + " WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateIntegrantesCasaOracion(int idEjecucionCelula, int integrantesCasaOracion) {
        String metodo = "updateIntegrantesCasaOracion(int idEjecucionCelula, int integrantesCasaOracion)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula"
                        + " SET integrantes_casa_oracion = '" + integrantesCasaOracion + "'"
                        + " WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateIntegrantesOtrasIglesias(int idEjecucionCelula, int integrantesOtrasIglesias) {
        String metodo = "updateIntegrantesOtrasIglesias(int idEjecucionCelula, int integrantesOtrasIglesias)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula"
                        + " SET integrantes_otras_iglesias = '" + integrantesOtrasIglesias + "'"
                        + " WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateAsistenciaDomingoAnterior(int idEjecucionCelula, int asistenciaDomingoAnterior) {
        String metodo = "updateAsistenciaDomingoAnterior(int idEjecucionCelula, int asistenciaDomingoAnterior)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula"
                        + " SET asistencia_domingo_anterior = '" + asistenciaDomingoAnterior + "'"
                        + " WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }

    @Override
    public RspEjecucionCelula updateOfrenda(int idEjecucionCelula, Double ofrenda) {
        String metodo = "updateOfrenda(int idEjecucionCelula, Double ofrenda)";
        RspEjecucionCelula rspEjecucionCelula = new RspEjecucionCelula();
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        rspEjecucionCelula.setEsConexionAbiertaExitosamente(false);
        rspEjecucionCelula.setEsConexionCerradaExitosamente(false);
        rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(true);
        rspEjecucionCelula.setEsRolledBackIntentado(false);
        rspEjecucionCelula.setEsRolledBackExitosamente(true);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspEjecucionCelula.setEsConexionAbiertaExitosamente(true);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            int rows;
            PreparedStatement stmt = null;
            try {
                conectorBD.getConnection().setAutoCommit(false);
                String consultaSQL = "UPDATE ejecucion_celula"
                        + " SET ofrenda = '" + ofrenda + "'"
                        + " WHERE id_ejecucion_celula = '" + idEjecucionCelula + "'";
                stmt = conectorBD.getConnection().prepareStatement(consultaSQL);
                rows = stmt.executeUpdate();
                stmt.close();
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirConsulta(stmt.toString(), metodo, this.getClass().toString()));
                conectorBD.getConnection().commit();
            } catch (Exception e) {
                rspEjecucionCelula.setEsSentenciaSqlEjecutadaExitosamente(false);
                rspEjecucionCelula.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, metodo, this.getClass().toString()));
                try {
                    rspEjecucionCelula.setEsRolledBackIntentado(true);
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirConsulta("Intentando Rollback", metodo, this.getClass().toString()));
                    conectorBD.getConnection().rollback();
                } catch (SQLException se2) {
                    rspEjecucionCelula.setRespuestaRolledBack(utilidadSistema.imprimirExcepcion(se2, metodo, this.getClass().toString()));
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
            } finally {
                if (!rspEjecucionCelula.esRolledBackIntentado()) {
                    rspEjecucionCelula.setEsRolledBackExitosamente(false);
                }
                if (conectorBD.cerrarConexion()) {
                    rspEjecucionCelula.setEsConexionCerradaExitosamente(true);
                }
                rspEjecucionCelula.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                return rspEjecucionCelula;
            }
        } else {
            rspEjecucionCelula.setEsRolledBackExitosamente(false);
            rspEjecucionCelula.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspEjecucionCelula;
        }
    }
}
