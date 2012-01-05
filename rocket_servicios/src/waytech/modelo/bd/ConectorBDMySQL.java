package waytech.modelo.bd;
import java.sql.*;
import waytech.utilidades.UtilidadSistema;

/**
 * ConectorBDMySQL
 * @since Viernes 11/03/2011 9:15 AM.
 * @version 1.0 Viernes  11/03/2011 09:15 AM.
 * @version 1.1 Martes   31/08/2011 11:21 AM.
 * @version 1.2 Martes   25/10/2011 12:37 AM.
 * @version 1.3 Viernes  28/10/2011 09:58 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SGI
 * Pendiente programar toda la metadata de la base de datos.
 */
public final class ConectorBDMySQL {

    private UtilidadSistema utilidadSistema = new UtilidadSistema();
    private AtributosConector atributosConector = new AtributosConector();

    /**
     * Obtiene los atributos de la base de datos
     * @return AtributosConector
     */
    public AtributosConector getAtributosConector() {
        return atributosConector;
    }   
    
    /**
     * Constructor de la clase
     */
    public ConectorBDMySQL() {
        establecerElementosDeConexion();
    }
    
    /**
     * Establece los elementos para la conexión de la base de datos
     */
    private void establecerElementosDeConexion() {
//        atributosConector.setUser("waytech_root");
//        atributosConector.setPassword("Lux3.3Et7dm");
//        atributosConector.setHostname("localhost");
//        atributosConector.setDriver("com.mysql.jdbc.Driver");
//        atributosConector.setDatabaseName("waytech_rocket");
//        atributosConector.setUrl("jdbc:mysql://" + atributosConector.getHostname() + "/" + atributosConector.getDatabaseName());        
        atributosConector.setUser("root");
        atributosConector.setPassword("wt.admin");
        atributosConector.setHostname("192.168.1.101");
        atributosConector.setDriver("com.mysql.jdbc.Driver");
        atributosConector.setDatabaseName("rocket2");
        atributosConector.setUrl("jdbc:mysql://" + atributosConector.getHostname() + "/" + atributosConector.getDatabaseName());        
        
    }

    /**
     * Intenta establecer una conexión con la base de datos
     * @return boolean
     */
    public synchronized boolean iniciarConexion() {
        //INICIA LA CONEXIÓN EN FALSO
        boolean esConexionIniciadaExitosamente = true;
        try {
            //OBTIENE UNA NUEVA INSTANCIA DE UNA CONEXIÓN A TRAVÉS DEL DRIVER
            Class.forName(atributosConector.getDriver()).newInstance();
            //AJUSTA LA CONEXIÓN
            atributosConector.setConnection(DriverManager.getConnection(atributosConector.getUrl(), atributosConector.getUser(), atributosConector.getPassword()));
            //COLOCA UNA RESPUESTA DEL INICIO DE CONEXIÓN
        } catch (Exception e) {
            esConexionIniciadaExitosamente = false;
            atributosConector.setRespuestaInicioConexion((utilidadSistema.imprimirExcepcion(e, "iniciarConexion()", this.getClass().toString())));
        } finally {
            //SI NO HAY EXCEPCIONES LA CONEXIÓN ES EXITOSA
            if (esConexionIniciadaExitosamente) {
                atributosConector.setRespuestaInicioConexion((utilidadSistema.imprimirConsulta(atributosConector.getConnection().toString(), "iniciarConexion()", this.getClass().toString())));                
                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
     * Verifica si la conexión a la base de datos está cerrada
     * @return boolean
     */
    public synchronized boolean esConexionCerrada() {
        if (atributosConector.getConnection() == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Obtiene la conexión a la base de datos
     * @return Connection
     */
    public synchronized Connection getConnection() {
        return this.atributosConector.getConnection();
    }

    /**
     * Intenta cerrar la conexión a la base de datos
     * @return boolean
     */
    public synchronized boolean cerrarConexion() {
        boolean esConexionCerradaExitosamente = true;
        if (atributosConector.getConnection() != null) {
            try {
                atributosConector.getConnection().close();                
            } catch (Exception e) {                
                atributosConector.setRespuestaCierreDeConexion((utilidadSistema.imprimirExcepcion(e, "cerrarConexion()", this.getClass().toString())));
                esConexionCerradaExitosamente = false;
            } finally {
                if (esConexionCerradaExitosamente) {                    
                    atributosConector.setRespuestaCierreDeConexion((utilidadSistema.imprimirConsulta("Conexión cerrada", "cerrarConexion()", this.getClass().toString())));
                    return true;
                } else {                                        
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
