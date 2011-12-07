package waytech.utilidades;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

  /**
   * convierte diaNumero a formato de texto.
   * Ejemplo:
   * 1 >> 'Lunes'
   * 7 >> 'Domingo'
   * @param diaTexto
   * @return el día de la semana en texto
   */
  public static String convertirDiaSemanaTextoCompleto(String diaTexto) {
    int diaNumero = Integer.parseInt(diaTexto);
    if (diaNumero == 1) {
      return "Lunes";
    } else if (diaNumero == 2) {
      return "Martes";
    } else if (diaNumero == 3) {
      return "Miércoles";
    } else if (diaNumero == 4) {
      return "Jueves";
    } else if (diaNumero == 5) {
      return "Viernes";
    } else if (diaNumero == 6) {
      return "Sábado";
    } else if (diaNumero == 7) {
      return "Domingo";
    }
    return "";//error
  }

  /**
   * convierte horaNumero a formato de texto.
   * Ejemplo:
   * 8 >> '8.00 a.m.'
   * 20 >> '8.00 p.m.'
   * @param horaNumero
   * @return la hora en texto
   */
  public static String convertirHoraTextoCompleto(String horaTexto) {
    int horaNumero = Integer.parseInt(horaTexto);
    if (horaNumero == 8) {
      return "8:00 a.m.";
    } else if (horaNumero == 830) {
      return "8:30 a.m.";
    } else if (horaNumero == 9) {
      return "9:00 a.m.";
    } else if (horaNumero == 930) {
      return "9:30 a.m.";
    } else if (horaNumero == 10) {
      return "10:00 a.m.";
    } else if (horaNumero == 1030) {
      return "10:30 a.m.";
    } else if (horaNumero == 11) {
      return "11:00 a.m.";
    } else if (horaNumero == 1130) {
      return "11:30 a.m.";
    } else if (horaNumero == 12) {
      return "12:00 p.m.";
    } else if (horaNumero == 1230) {
      return "12:30 p.m.";
    } else if (horaNumero == 13) {
      return "1:00 p.m.";
    } else if (horaNumero == 1330) {
      return "1:30 p.m.";
    } else if (horaNumero == 14) {
      return "2:00 p.m.";
    } else if (horaNumero == 1430) {
      return "2:30 p.m.";
    } else if (horaNumero == 15) {
      return "3:00 p.m.";
    } else if (horaNumero == 1530) {
      return "3:30 p.m.";
    } else if (horaNumero == 16) {
      return "4:00 p.m.";
    } else if (horaNumero == 1630) {
      return "4:30 p.m.";
    } else if (horaNumero == 17) {
      return "5:00 p.m.";
    } else if (horaNumero == 1730) {
      return "5:30 p.m.";
    } else if (horaNumero == 18) {
      return "6:00 p.m.";
    } else if (horaNumero == 1830) {
      return "6:30 p.m.";
    } else if (horaNumero == 19) {
      return "7:00 p.m.";
    } else if (horaNumero == 1930) {
      return "7:30 p.m.";
    } else if (horaNumero == 20) {
      return "8:00 p.m.";
    }
    return "";//error
  }

  public String obtenerFechaHoy() {
    Calendar calendario = GregorianCalendar.getInstance();
    Date fecha = calendario.getTime();
    System.out.println(fecha);
    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd");
    String fechaStr = formatoDeFecha.format(fecha);
    return fechaStr;
  }

  /**
   * Método utilitarios usados por Rocket
   */
  /**
   * devuelve la fecha actual en el formato numérico
   * YYYYmmDD
   * usada como id de ReporteCelula
   * @return 
   */
  public static int fechaNum() {
    Calendar c = Calendar.getInstance();
    int month = c.get(Calendar.MONTH);
    String mes = (month < 10) ? "0" + month : "" + month;
    int date = c.get(Calendar.DATE);
    String dia = (date < 10) ? "0" + date : "" + date;
    int año = c.get(Calendar.YEAR);
    String fecha = "" + año + mes + dia;
    return Integer.parseInt(fecha);
  }

  /**
   * une idcelula y fecha para obtener el idReporteCelula
   */
  public static int calcularIdReporteCelula(int idCelula) {
    String idReporte = "" + idCelula + "" + Util.fechaNum();
    /**
    System.out.println("idCelula="+idCelula);
    System.out.println("fechaNum="+Util.fechaNum());
    System.out.println("IDReporte="+IDReporte);     
     */
    int id = Integer.parseInt(idReporte);
    return id;
  }

  /**
   * convierte un objeto java.util.Date en java.util.Calendar
   * @param date el objeto Date
   * @return el objeto Calendar
   */
  public static Calendar getCalendar(Date date) {
    date.getTime();
    Calendar cal = new GregorianCalendar(date.getYear() + 1900, date.getMonth(), date.getDate());
    return cal;
  }

  /**
   * devuelve fecha en formato dd/mm/yyyy
   * Ejemplo: 28/10/2011
   * @param cal el calendar con la fecha a convertir
   * @return 
   */
  public static String getFechaTextoSoloNumeros(Calendar cal) {
    int month = cal.get(Calendar.MONTH) + 1;
    String mes = (month < 10) ? "0" + month : "" + month;
    String dia = getDiaTextoCompletado(cal);
    String año = "" + cal.get(Calendar.YEAR);
    return "" + dia + "/" + mes + "/" + año;
  }

  /**
   * devuelve fecha en formato 'mes de año'
   * Ejemplo: 'Octubre de 2011'
   * @param cal el calendar que contiene la fecha a procesar
   * @return 
   */
  public static String getFechaTextoMesAño(Calendar cal) {
    int date = cal.get(Calendar.DATE);
    String mes = getFechaTextoMesLargo(cal);
    String año = "" + cal.get(Calendar.YEAR);
    return "" + mes + " de " + año;
  }

  /**
   * devuelve fecha, sólo mes en texto largo
   * Ejemplo: Noviembre
   * @param cal el calendar que contiene la fecha a procesar
   * @return 
   */
  public static String getFechaTextoMesLargo(Calendar cal) {
    int month = cal.get(Calendar.MONTH) + 1;
    String mes = getMesTextoLargo(month);
    return "" + mes;
  }

  /**
   * devuelve fecha, sólo mes en texto corto, abreviado
   * Ejemplo: Nov
   * @param cal el calendar que contiene la fecha a procesar
   * @return 
   */
  public static String getMesTextoCorto(Calendar cal) {
    int month = cal.get(Calendar.MONTH) + 1;
    String mes = getMesTextoCorto(month);
    return "" + mes;
  }

  /**
   * devuelve día en texto, con 0 a la izquierda si día es menor a 10
   * Ejemplo: si cal contiene 07/10/2011, el método devuelve el valor '07'
   * @param cal
   * @return 
   */
  public static String getDiaTextoCompletado(Calendar cal) {
    int date = getDia(cal);
    String dia = (date < 10) ? "0" + date : "" + date;
    return "" + dia;
  }

  /**
   * devuelve día en número, sin ceros a la izquierda
   * Ejemplo: si cal contiene 07/10/2011, el método devuelve el valor '7'
   * @param cal
   * @return 
   */
  public static int getDia(Calendar cal) {
    return cal.get(Calendar.DATE);
  }

  /**
   * devuelve día de la semana en texto
   * Ejemplo: si cal contiene Sábado 07/10/2011, el método devuelve el valor 'Sábado'
   * @param cal
   * @return 
   */
  public static String getDiaSemanaTexto(Calendar cal) {
    return "" + cal.get(Calendar.DAY_OF_WEEK);
  }

  /**
   * devuelve mes en número, sin ceros a la izquierda
   * Ejemplo: si cal contiene 07/09/2011, el método devuelve el valor '9'
   * @param cal
   * @return 
   */
  public static int getMes(Calendar cal) {
    return cal.get(Calendar.MONTH);
  }

  /**
   * devuelve valor de año
   * Ejemplo: si cal contiene 07/09/2011, el método devuelve el valor '2011'
   * @param cal
   * @return 
   */
  public static int getAño(Calendar cal) {
    return cal.get(Calendar.YEAR);
  }

  /**
   * devuelve fecha en formato DiaSemana dd/mm/yyyy
   * Ejemplo: Viernes  28/10/2011
   * @param cal el calendar con la fecha a convertir
   * @return 
   */
  public static String getFechaLarga(Calendar cal) {
    return "" + getDiaSemanaTexto(cal.get(Calendar.DAY_OF_WEEK)) + " "
            + getFechaTextoSoloNumeros(cal);
  }

  /**
   * devuelve fecha en formato 'DiaSemana dd de Mes de yyyy'
   * Ejemplo: Viernes  28 de Octubre de 2011
   * @param cal el calendar con la fecha a convertir
   * @return 
   */
  public static String getFechaLargaTexto(Calendar cal) {
    return "" + getDiaSemanaTexto(cal.get(Calendar.DAY_OF_WEEK))
            + " "
            + cal.get(Calendar.DAY_OF_WEEK)
            + " de "
            + getFechaTextoMesAño(cal);
  }

  /**
   * devuelve fecha en formato 'DiaSemana dd de Mes'
   * Ejemplo: Sábado 7 de Noviembre
   * @param cal el calendar con la fecha a convertir
   * @return 
   */
  public static String getFechaDiaMesTextoLargo(Calendar cal) {
    return "" + getDiaSemanaTexto(cal.get(Calendar.DAY_OF_WEEK))
            + " "
            + cal.get(Calendar.DAY_OF_WEEK)
            + " de "
            + getMesTextoLargo(cal.get(Calendar.MONTH));
  }

  /**
   * devuelve fecha en formato 'DiaSemana, dd MesAbreviado'
   * Ejemplo: Sábado 7 de Noviembre
   * @param cal el calendar con la fecha a convertir
   * @return 
   */
  public static String getFechaTextoCortoSinAño(Calendar cal) {
    return "" + getDiaSemanaTexto(cal)
            + " "
            + getDia(cal)
            + " "
            + getMesTextoCorto(cal);
  }

  /**
   * devuelve fecha en formato 'Día MesAbreviado'
   * Ejemplo: 7 de Noviembre
   * @param cal el calendar con la fecha a convertir
   * @return 
   */
  public static String getFechaTextoDiaMes(Calendar cal) {
    return "" + getDia(cal)
            + " "
            + getMesTextoCorto(cal);
  }

  /**
   * devuelve fecha en formato 'Día MesCorto, Año'
   * Ejemplo: Sábado 7 de Noviembre
   * @param cal el calendar con la fecha a convertir
   * @return 
   */
  public static String getFechaTextoDiaMesAñoAbreviado(Calendar cal) {
    return "" + getDia(cal)
            + " "
            + getMesTextoCorto(cal)
            + ", "
            + getAño(cal);
  }

  /**
   * devuelve dia de semana en texto: Ej: Domingo, Lunes, ... , sábado
   * @param diaSemana día de la semana en número: 1:domingo, ..., 6:sábado
   * @return 
   */
  public static String getDiaSemanaTexto(int diaSemana) {
    if (diaSemana == 1) {
      return "Domingo";
    } else if (diaSemana == 2) {
      return "Lunes";
    } else if (diaSemana == 3) {
      return "Martes";
    } else if (diaSemana == 4) {
      return "Miércoles";
    } else if (diaSemana == 5) {
      return "Jueves";
    } else if (diaSemana == 6) {
      return "Viernes";
    } else if (diaSemana == 7) {
      return "Sábado";
    }
    return "";//error
  }

  /**
   * devuelve dia de semana en texto completo: Ej: Enero, Febrero, Marzo,... Diciembre
   * @param mes número de mes en número: 1:ene, ..., 12:dic
   * @return 
   */
  public static String getMesTextoLargo(int mes) {
    String mesTexto = "";
    if (mes == 1) {
      mesTexto = "Enero";
    } else if (mes == 2) {
      mesTexto = "Febrero";
    } else if (mes == 3) {
      mesTexto = "Marzo";
    } else if (mes == 4) {
      mesTexto = "Abril";
    } else if (mes == 5) {
      mesTexto = "Mayo";
    } else if (mes == 6) {
      mesTexto = "Junio";
    } else if (mes == 7) {
      mesTexto = "Julio";
    } else if (mes == 8) {
      mesTexto = "Agosto";
    } else if (mes == 9) {
      mesTexto = "Septiembre";
    } else if (mes == 10) {
      mesTexto = "Octubre";
    } else if (mes == 11) {
      mesTexto = "Noviembre";
    } else if (mes == 12) {
      mesTexto = "Diciembre";
    }
    return mesTexto;
  }

  /**
   * devuelve dia de semana en texto corto: Ej: Ene, Feb, Mar,... Dic
   * @param mes número de mes en número: 1:ene, ..., 12:dic
   * @return 
   */
  public static String getMesTextoCorto(int mes) {
    String mesTexto = "";
    if (mes == 1) {
      mesTexto = "Ener";
    } else if (mes == 2) {
      mesTexto = "Feb";
    } else if (mes == 3) {
      mesTexto = "Mar";
    } else if (mes == 4) {
      mesTexto = "Abrl";
    } else if (mes == 5) {
      mesTexto = "May";
    } else if (mes == 6) {
      mesTexto = "Jun";
    } else if (mes == 7) {
      mesTexto = "Jul";
    } else if (mes == 8) {
      mesTexto = "Ago";
    } else if (mes == 9) {
      mesTexto = "Sep";
    } else if (mes == 10) {
      mesTexto = "Oct";
    } else if (mes == 11) {
      mesTexto = "Nov";
    } else if (mes == 12) {
      mesTexto = "Dic";
    }
    return mesTexto;
  }

  /**
   * calcula domingo anterior basado en el día de hoy
   * @return cal con fecha del domingo anterior al día de hoy
   */
  public static Calendar calcularDomingoAnterior() {
    Calendar cal = Calendar.getInstance();
    int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
    if (diaSemana != 1) {
      cal.add(Calendar.DAY_OF_MONTH, -(diaSemana - 1));
    }
    return cal;
  }

  /**
   * calcula sábado siguiente basado en el día de hoy
   * @return cal con fecha del sábado siguiente al día de hoy
   */
  public static Calendar calcularSabadoSiguiente() {
    Calendar cal = Calendar.getInstance();
    int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
    if (diaSemana != 7) { //7 = sabado
      cal.add(Calendar.DAY_OF_MONTH, (7 - diaSemana));
    }
    return cal;
  }

  /**
   * obtiene día de inicio del mes actual
   * @return cal calendar con fecha del inicio del mes actual
   */
  public static Calendar calcularMesInicio() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, -1);
    return cal;
  }

  /**
   * obtiene día de inicio del mes actual
   * @return cal calendar con fecha del inicio del mes actual
   */
  public static Calendar calcularMesInicio(Calendar cal) {
    cal.set(Calendar.DAY_OF_MONTH, 1);
    return cal;
  }

  /**
   * obtiene día de final del mes pasado como parámetro
   * @return cal fecha a convertir
   */
  public static Calendar calcularMesFin(Calendar cal) {
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    return cal;
  }

  /**
   * obtiene día de inicio del trimestre
   * @return cal calendar con fecha del inicio del mes actual
   */
  public static Calendar calcularTrimestreInicio() {
    Calendar cal = Calendar.getInstance();
    cal.roll(Calendar.MONTH, -3);
    return cal;
  }

  /**
   * devuelve un calendar del 1er día del año actual
   * @param cal
   * @return 
   */
  public static Calendar calcularInicioAñoActual() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.MONTH, 1);
    cal.set(Calendar.DATE, 1);
    return cal;
  }

  /**
   * devuelve 1 calendar 1 año hacia atrás
   * @param cal
   * @return 
   */
  public static Calendar retrocederAño() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -1);
    return cal;
  }

  /**
   * establece el 1er día del año actual
   * @param cal
   * @return 
   */
  public static Calendar calcularInicioAñoActual(Calendar cal) {
    cal.set(Calendar.MONTH, 1);
    cal.set(Calendar.DATE, 1);
    return cal;
  }

  /**
   * toma un calendar y setea los campos relacionados con la hora a 0
   * @param cal
   * @return 
   */
  public static Calendar quitarHora(Calendar cal) {
    cal.set(Calendar.HOUR, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal;
  }

  /**
   * compara fechas tomando en cuenta día, mes y año
   * sin tomar en cuenta la hora
   * @param a
   * @param b
   * @return 
   */
  public static boolean compararFechas(Calendar a, Calendar b) {
    if (a.get(Calendar.DATE) != b.get(Calendar.DATE)) {
      return false;
    } else if (a.get(Calendar.MONTH) != b.get(Calendar.MONTH)) {
      return false;
    } else if (a.get(Calendar.YEAR) != b.get(Calendar.YEAR)) {
      return false;
    }
    return true;
  }

  /**
   * retrocede un año del actual
   * @return 
   */
  public static Calendar retroceder1Año() {
    Calendar cal = Calendar.getInstance();
    cal.roll(Calendar.YEAR, -1);
    return cal;
  }

  /**
   * retrocede un mes a la fecha pasada como parámetro
   * @return 
   */
  public static Calendar retrocederMes(Calendar cal) {
    cal.add(Calendar.MONTH, -1);
    return cal;
  }

  /**
   * retrocede un mes a la fecha pasada como parámetro
   * @return 
   */
  public static Calendar avanzarMes(Calendar cal) {
    cal.add(Calendar.MONTH, 1);
    return cal;
  }

  public static String getFechaMySql(Date fecha) {
    Calendar cal = GregorianCalendar.getInstance();
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    return formato.format(fecha);
  }
}
