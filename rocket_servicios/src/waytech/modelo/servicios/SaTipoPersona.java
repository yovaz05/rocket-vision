/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waytech.modelo.servicios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.bd.ConectorBDMySQL;
import waytech.modelo.beans.sgi.TipoPersona;
import waytech.modelo.interfaces.IsaTipoPersona;
import waytech.utilidades.UtilidadSistema;

/**
 *
 * @author root
 */
public class SaTipoPersona implements IsaTipoPersona {
    
     UtilidadSistema utilidadSistema = new UtilidadSistema();
    
    private TipoPersona rsTipoPersona(ResultSet rs, TipoPersona tipoPersona) throws SQLException {        
        tipoPersona.setEstado(rs.getShort("estado"));
        tipoPersona.setIdTipoPersona(rs.getInt("id_tipo_persona"));                        
        tipoPersona.setTraza(rs.getString("traza"));
        tipoPersona.setNombre(rs.getString("nombre"));        
        return tipoPersona;
    }   

    @Override
    public RspTipoPersona modificarTipoPersona(TipoPersona tipoPersona) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RspTipoPersona ingresarTipoPersona(TipoPersona tipoPersona) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RspTipoPersona eliminarTipoPersonaLogicamente(int idTipoPersona) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RspTipoPersona getTipoPersonaPorIdTipoPersona(int idTipoPersona) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RspTipoPersona listTipoPersonas() {
        //INSTANCIAS DE LAS CLASES                
        ConectorBDMySQL conectorBD = new ConectorBDMySQL();
        RspTipoPersona rspTipoPersona = new RspTipoPersona();
        List<TipoPersona> todosLosTipoPersona = new ArrayList<TipoPersona>();
        //INICIALIZAR VARIABLES
        rspTipoPersona.setEsConexionAbiertaExitosamente(false);
        rspTipoPersona.setEsConexionCerradaExitosamente(false);
        rspTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(false);
        //INTENTA ESTABLECER LA CONEXIÓN CON LA BASE DE DATOS
        if (conectorBD.iniciarConexion()) {
            rspTipoPersona.setEsConexionAbiertaExitosamente(true);
            rspTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            String consultaSQL = "SELECT * FROM tipo_persona WHERE estado = 1";
            try {
                Statement sentencia = conectorBD.getConnection().createStatement();
                boolean bandera = sentencia.execute(consultaSQL);
                if (bandera) {
                    ResultSet rs = sentencia.getResultSet();
                    rspTipoPersona.setEsSentenciaSqlEjecutadaExitosamente(true);
                    rspTipoPersona.setRespuestaServicio(utilidadSistema.imprimirConsulta(sentencia.toString(), "listTipoPersonas()", this.getClass().toString()));
                    while (rs.next()) {
                        TipoPersona tipoPersona = new TipoPersona();
                        tipoPersona = rsTipoPersona(rs, tipoPersona);                        
                        todosLosTipoPersona.add(tipoPersona);                        
                    }
                }
            } catch (SQLException e) {
                rspTipoPersona.setRespuestaServicio(utilidadSistema.imprimirExcepcion(e, "listTipoPersonas()", this.getClass().toString()));
            } finally {
                if (conectorBD.cerrarConexion()) {
                    rspTipoPersona.setEsConexionCerradaExitosamente(true);
                }
                rspTipoPersona.setRespuestaCierreDeConexion(conectorBD.getAtributosConector().getRespuestaCierreDeConexion());
                rspTipoPersona.setAllTipoPersona(todosLosTipoPersona);
                return rspTipoPersona;
            }
        } else {
            rspTipoPersona.setRespuestaInicioDeConexion(conectorBD.getAtributosConector().getRespuestaInicioConexion());
            return rspTipoPersona;
        }
    }
    
}
