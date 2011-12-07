package waytech.modelo.bd;

import java.sql.Connection;

/**
* Clase para los parametros de una clase conectora a la base de datos
* @since Martes  31/08/2011 11:21 AM.
* @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
* @see http://www.waytech.com.ve
* @version 1.0 Viernes 11/03/2011 9:15 AM.
* @version 1.1 Martes  25/10/2011 1:05 AM.
*/


public class AtributosConector {

    private String driver;
    private String url;
    private String hostname;
    private String databaseName;
    private String user;
    private String password;    
    private Connection connection;
    private String respuestaInicioDeConexion;
    private String respuestaCierreDeConexion;

    public String getRespuestaCierreDeConexion() {
        return respuestaCierreDeConexion;
    }

    void setRespuestaCierreDeConexion(String answerCloseConnection) {
        this.respuestaCierreDeConexion = answerCloseConnection;
    }

    public String getRespuestaInicioConexion() {
        return respuestaInicioDeConexion;
    }

    void setRespuestaInicioConexion(String respuestaInicioConexion) {
        this.respuestaInicioDeConexion = respuestaInicioConexion;
    }        
        
    public Connection getConnection() {
        return connection;
    }

    void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDriver() {
        return driver;
    }

    void setDriver(String driver) {
        this.driver = driver;
    }

    public String getHostname() {
        return hostname;
    }

    void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    void setUser(String user) {
        this.user = user;
    }
    
}
