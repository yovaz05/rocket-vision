/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waytech.modelo.servicios;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    public RspTipoPersona obtenerTipoPersonaPorIdTipoPersona(int idTipoPersona) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RspTipoPersona obtenerTodosLosTipoPersonas() {
        throw new UnsupportedOperationException("Not supported yet.");
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
    
}
