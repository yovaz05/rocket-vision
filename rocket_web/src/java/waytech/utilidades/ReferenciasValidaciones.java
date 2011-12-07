/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package waytech.utilidades;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author fabiolaperales
 */
public class ReferenciasValidaciones {

    //SOLO LETRAS
    public static boolean campoSoloLetras(String texto){
        boolean valor = true;
        Pattern patron = Pattern.compile("[^a-zA-Z ]");
        Matcher cargo = patron.matcher(texto);
        if(cargo.find()){
            valor = false;
        }
        return valor;
    }
    //SOLO NUMEROS
    public static boolean campoSoloNumeros(String cadena) throws InterruptedException{
        boolean valor = true;
        Pattern patron = Pattern.compile("[^0-9 ]");
        Matcher cargo = patron.matcher(cadena);
        if(cargo.find()){
            valor = false;
        }
        return valor;
    }

    //VALIDAR NUMERO
    /**
     * @param String valor
     * @return boolean
     * toma de un string y verifica que se
     * ajuste al formato numérico (float)
     * @throws InterruptedException
     */
    public static boolean validarNumero(String valor) throws InterruptedException{
        boolean valido = true;
        try{
            Float.valueOf(valor);
            valido= true;
        }
        catch(NumberFormatException e){
            valido=false;
        }
        return valido;
    }

    //LE DA FORMATO A UN MUMERO
    /**
     * @param String number
     * @return boolean
     * toma un String (números)
     * y lo amolda al formato numérico: ###,##0.00
     * coloca separadores de miles y decimales
     * ej: 15234.2 --> 15.234,20
     *     0       --> 0,00
     */
    public String formatoNumero(String number) {
        double value;
        String numberFormat = "###,###,###,##0.00";
        DecimalFormat formatter = new DecimalFormat(numberFormat);
        try {
            value = Double.parseDouble(number);
        } catch (Throwable t) {
            return null;
        }
        return formatter.format(value);
    }

    public String formatoNumeroDouble(String number) {
        double value;
        String numberFormat = "###,###,###,##0.00";
        DecimalFormat formatter = new DecimalFormat(numberFormat);
        try {
            value = Double.parseDouble(number);
        } catch (Throwable t) {
            return null;
        }
        return formatter.format(value);
    }

    public static String formatoNumeroConTodosLosDecimales(String number) {
        double value;
        String numberFormat = "###,###,###,##0.########";
        DecimalFormat formatter = new DecimalFormat(numberFormat);
        try {
            value = Double.parseDouble(number);
        } catch (Throwable t) {
            return null;
        }
        return formatter.format(value);
    }


    public static String formatoTelefono(String numeroTelefono) throws InterruptedException{
        String primerDigito = numeroTelefono.substring(0,1);
        if (!primerDigito.equals("0")){
            numeroTelefono = "0"+ numeroTelefono;
        }
        String numero="";
        String codigo ="" ;
        String cuerpoNumero ="" ;
        int lengthCodigo = numeroTelefono.length()-7;
        codigo = numeroTelefono.substring(0, lengthCodigo);
        cuerpoNumero = numeroTelefono.substring(lengthCodigo, numeroTelefono.length());
        numero = "("+codigo+") "+cuerpoNumero;
        return numero;
    }

    public static boolean esEmailCorrecto(String correo) {
        Pattern pat = null;
        Matcher mat = null;
        pat = Pattern.compile("^[\\w-\\.]+\\@[\\w\\.-]+\\.[a-z]{2,4}$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            return true;
        }else{
            return false;
        }
    }

    //VALIDAR DOS CONTRASEÑAS
    /**
     * 
     * @param password1
     * @param password2
     * @return
     *      true - si password1 y password2 son iguales
     *      false - si password1 y password2 difieren
     * @throws InterruptedException
     */
     public static  boolean compararPassword(String password1,String password2) throws InterruptedException{
        boolean igual = false;
        if (!(password1.equals(password2))){
            igual= false;
        }else{
            igual = true;
        }
        return igual;
    }


    //VERIFICA NUMEROS DE TELEFONO
    /**
     * @param cadena
     * @return boolean
     *        Verifica que el numero de telefono dado
     *        se ajuste al formato (0000) 0000000
     *
     * @throws InterruptedException
     */
    public static boolean  esNumeroTelefonoCorrecto(String cadena) throws InterruptedException{
        boolean valor = true;
        Pattern patron = Pattern.compile("[^0-9() ]");
        Matcher cargo = patron.matcher(cadena);
        if(cargo.find()){
            valor = false;
        }
        return valor;
    }

    //VALIDAR DIRECCION WEB
    public static boolean esDireccionWebCorrecta(String web){
        Pattern pat = null;
        Matcher mat = null;
        pat = Pattern.compile("^www\\.\\.*\\.\\.*");
        mat = pat.matcher(web);
        if (mat.find()) {
            return true;
        }else{
            return false;
        }
    }

    //VERIFICA QUE SEA TEXTO
    public static boolean TextoCorrecto(String texto){
        boolean textoCorrecto = false;
        String cadena = texto; //recoges el contenido del campo
        boolean algunDigito = false;
        boolean algunaLetra = false;
        for (int i = 0; i < cadena.length(); i++) {
            if(Character.isDigit(cadena.charAt(i))) {
                algunDigito = true;
            }else {
                algunaLetra = true;
            }
        }
        if(algunDigito && !algunaLetra){
            //TODO DIGITOS
            textoCorrecto = false;
        }else if (algunDigito){
           //ALFNUMERICOS
            textoCorrecto = false;
        }else{
           //TODO LETRAS
            textoCorrecto = true;
        }
        return textoCorrecto;
    }

    //RETORNA LA FECHA DEL SISTEMA COMO UN STRING
    public static  String  AsignarFechaDia(){
        Calendar calendar = Calendar.getInstance();
        String dia = Integer.toString(calendar.get(Calendar.DATE));
        int mess=calendar.get(Calendar.MONTH);
        int masMess=mess+1;
	String  mes = Integer.toString(masMess);
	String  annio = Integer.toString(calendar.get(Calendar.YEAR));
        String fecha = (dia + "/" +mes + "/"  + annio);
        return fecha;
    }

}
