package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.AccesoInsert;
import waytech.modelo.beans.sgi.AccesoUpdate;
import waytech.modelo.servicios.RspAcceso;

/**
 * @since Viernes  28/10/2011 09:58 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @version 1.4 Jueves   25/11/2011 10:47 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI 
 */
public interface IsaAcceso {

    public abstract RspAcceso insertAcceso(AccesoInsert accesoInsert);

    public abstract RspAcceso updateAcceso(AccesoUpdate accesoModificar);

    /**
     * Actualiza la fecha automaticamente del último ingreso
     * @param idAcceso
     * @return 
     */
    public abstract RspAcceso updateFechaUltimoAcceso(int idAcceso);

    /**
     * Elimina un registro lógicamente
     * @param idAcceso
     * @return 
     */
    public abstract RspAcceso deleteAcceso(int idAcceso);

    public abstract RspAcceso getAccesoPorIdAcceso(int idAcceso);

    public abstract RspAcceso getAccesoPorTraza(String traza);

    public abstract RspAcceso getAccesoPorIdUsuario(int idUsuario);

    public abstract RspAcceso getAccesoPorLogin(String login);

    public abstract RspAcceso getAccesoPorPassword(String password);

    public abstract RspAcceso getAccesoPorCorreo(String correo);

    public abstract RspAcceso esLoginExistente(String login);

    public abstract RspAcceso esPasswordExistente(String password);
    
    public abstract RspAcceso esLoginPasswordValido(String login, String password);

    public abstract RspAcceso esCorreoExistente(String correo);
    
    public abstract RspAcceso esCorreoCoincidente(String correo, int id_persona);

    public abstract RspAcceso listAcceso();
    
    public abstract RspAcceso updateEstadoHabilitado(int idAcceso);
    
    public abstract RspAcceso updateEstadoRegistrado(int idAcceso);
    
    public abstract RspAcceso updateEstadoSolicitado(int idAcceso);
    
    public abstract RspAcceso updateEstadoHabilitadoPorIdPersona(int idPersona);
    
    public abstract RspAcceso updateEstadoRegistradoPorIdPersona(int idPersona);
    
    public abstract RspAcceso updateEstadoSolicitadoPorIdPersona(int idPersona);
    
    public abstract RspAcceso updateCorreo(String correo);
}
