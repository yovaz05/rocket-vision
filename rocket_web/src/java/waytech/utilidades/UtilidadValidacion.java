/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waytech.utilidades;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author root
 */
public class UtilidadValidacion {

    public String validarComillasSimplesString1(String cadena) {
        String codigoAjustado = cadena.replaceAll("'", "''");
        return codigoAjustado;
    }

    public boolean sonSoloNumeros(String cadena) throws InterruptedException {
        boolean valor = false;
        Pattern patron = Pattern.compile("[0-9]");
        Matcher cargo = patron.matcher(cadena);
        if (cargo.find()) {
            valor = true;
        }
        return valor;
    }

    /**
     * @param String number
     * @return boolean
     * toma un String (números)
     * y lo amolda al formato numérico: ###,##0.00
     * coloca separadores de miles y decimales
     * ej: 15234.2 --> 15.234,20
     *     0       --> 0,00
     */
    /*public String formatoNumeroPuntosComasDosDecimales(double number) {
    double value;
    String numberFormat = "###,###,###,##0.00";
    DecimalFormat formatter = new DecimalFormat(numberFormat);
    try {
    value = number;
    } catch (Throwable t) {
    return null;
    }
    return formatter.format(value);
    }*/
    public String formatoNumeroPuntoDosDecimales(double number) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat form = (DecimalFormat) nf;
        form.applyPattern("0.00");
        return form.format(number);
        //System.out.println("string " + form.format(number));
        //System.out.println("double " + Double.valueOf(form.format(number)));
    }
    //VALIDAR NUMERO

    /**
     * @param String valor
     * @return boolean
     * toma de un string y verifica que se
     * ajuste al formato numérico (float)
     * @throws InterruptedException
     */
    public boolean esNumeroValidoFloat(String valor) throws InterruptedException {
        boolean valido = true;
        try {
            Float.valueOf(valor);
            valido = true;
        } catch (NumberFormatException e) {
            valido = false;
        }
        return valido;
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
    public static boolean esNumeroTelefonoCorrecto(String cadena) throws InterruptedException {
        boolean valor = true;
        Pattern patron = Pattern.compile("[^0-9() ]");
        Matcher cargo = patron.matcher(cadena);
        if (cargo.find()) {
            valor = false;
        }
        return valor;
    }

    public static boolean esRifCorrecto(String rif) {
        Pattern pat = null;
        Matcher mat = null;
        pat = Pattern.compile("^[V,E,G,J]\\-*\\-*");
        mat = pat.matcher(rif);
        if (mat.find()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean esCorreoValido(String correo) {
        boolean flag = true;
        String input = correo;
        // comprueba que no empieze por punto o @
        Pattern p;
        Matcher m;
//        p = Pattern.compile("^.|^@");
//        m = p.matcher(input);
//        if (m.find()) {
//            System.err.println("Las direcciones email no empiezan por punto o @");
//            flag = false;
//        }

        // comprueba que no empieze por www.
        p = Pattern.compile("^www.");
        m = p.matcher(input);
        if (m.find()) {
            System.out.println("Los emails no empiezan por www");
            flag = false;
        }

        // comprueba que contenga @
        p = Pattern.compile("@");
        m = p.matcher(input);
        if (!m.find()) {
            System.out.println("La cadena no tiene arroba");
            flag = false;
        }

        // comprueba que no contenga caracteres prohibidos	
        p = Pattern.compile("[^A-Za-z0-9.@_-~#]+");
        m = p.matcher(input);
        StringBuffer sb = new StringBuffer();
        boolean resultado = m.find();
        boolean caracteresIlegales = false;

        while (resultado) {
            caracteresIlegales = true;            
            m.appendReplacement(sb, "");
            resultado = m.find();
        }

        // Añade el ultimo segmento de la entrada a la cadena
        m.appendTail(sb);

        input = sb.toString();

        if (caracteresIlegales) {
            System.out.println("La cadena contiene caracteres ilegales");
            flag = false;
        }
        return flag;
    }
}
