package waytech.utilidades;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que contienen métodos de utilidad.
 * @since Jueves  01/09/2011 03:12 PM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * @version 1.0 Jueves  01/09/2011 03:12 PM.
 */
public class UtilidadSistema {

    public String imprimirConsulta(String consulta, String metodo, String clase) {
        String str = "OK->" + clase + "." + metodo + "-" + obtenerFechaDelSistema() + "-" + consulta;
        System.out.println(str);
        return str;
    }

    public String imprimirExcepcion(Exception ex, String metodo, String clase) {
        String str = "EX->" + clase + "." + metodo + "-" + obtenerFechaDelSistema() + "-" + ex.getMessage();
        System.out.println(str);
        return str;
    }

    public String obtenerNombreDelEquipo() {
        return System.getenv("COMPUTERNAME");
    }

    public String obtenerNombreDelusuario() {
        return System.getProperty("user.name");
    }

    public String obtenerNombreDelProcesador() {
        return System.getenv("PROCESSOR_IDENTIFIER");
    }

    public String obtenerNombreDelSistemaOperativo() {
        return System.getProperty("os.name");
    }

    public String obtenerNombreDeLaVersionDeLaMaquinaVirtual() {
        return System.getProperty("java.version");
    }

    public String obtenerDirectorioDelUsuario() {
        return System.getProperty("user.dir");
    }

    public String obtenerFechaDelSistema() {
        Calendar calendario = GregorianCalendar.getInstance();
        Date fecha = calendario.getTime();
        System.out.println(fecha);
        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss:ms");
        String fechaStr = formatoDeFecha.format(fecha);
        return fechaStr;
    }

    public String obtenerFechaDelSistemaMySql() {
        Calendar calendario = GregorianCalendar.getInstance();
        Date fecha = calendario.getTime();
        System.out.println(fecha);
        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        String fechaStr = formatoDeFecha.format(fecha);
        return fechaStr;
    }

    public String generarTraza() {
        Calendar calendario = GregorianCalendar.getInstance();
        Date fecha = calendario.getTime();
        System.out.println(fecha);
        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss:ms");
        String fechaStr = formatoDeFecha.format(fecha);
        Random rand = new Random();
        int x = rand.nextInt(1000);
        int x1 = rand.nextInt(1000);
        int x2 = rand.nextInt(1000);
        int x3 = rand.nextInt(1000);
        int x4 = rand.nextInt(1000);
        int x5 = rand.nextInt(1000);
        int x6 = rand.nextInt(1000);
        int x7 = rand.nextInt(1000);
        int x8 = rand.nextInt(1000);
        int x9 = rand.nextInt(1000);
        int x10 = rand.nextInt(1000);
        int x11 = rand.nextInt(1000);
        int x12 = rand.nextInt(1000);
        int x13 = rand.nextInt(1000);
        return fechaStr + "###" + String.valueOf(x + x1 + x2 + x3 + x4 + x5 + x6 + x7 + x8 + x9 + x10 + x11 + x12 + x13);
    }
}
