package cdo.sgd.controladores;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Include;

/**
 * gestiona set y get de variables de sesión
 * @author Gabriel
 */
public class Sesion {

  /**
   * setea el valor de una variable de sesión
   */
  public static void setVariable(String nombre, Object valor) {
    Sessions.getCurrent().setAttribute(nombre, valor);
    System.out.println("SET variable sesión [" + nombre + "]=" + valor);
  }

  /**
   * obtiene el valor de una variable de sesión específica
   * @return 
   */
  public static Object getVariable(String nombre) {
    System.out.println("GET variable sesión [" + nombre + "]=" + Sessions.getCurrent().getAttribute(nombre));
    return Sessions.getCurrent().getAttribute(nombre);
  }

  /**
   * obtiene la variable de sesión idRed
   * @return 
   */
  public static int getIdRed() {
    return (Integer) Sesion.getVariable("idRed");
  }

  /**
   * guarda en la variable de sesión 'vistaCentral' la referencia al widget correspondiente
   */
  public static void setVistaCentral(Include refVistaCentral) {
    Sessions.getCurrent().setAttribute("vistaCentral", refVistaCentral);
  }

  /**
   * obtiene el valor de la variable de sesión 'vistaCentral'
   * @return referencia al widget 'vistaCentral'
   */
  public static Include getVistaCentral() {
    return (Include) Sessions.getCurrent().getAttribute("vistaCentral");
  }

  /**
   * setea la variable de sesión 'modo'
   */
  public static void setModo(String valor) {
    Sessions.getCurrent().setAttribute("modo", valor);
  }

  /**
   * obtiene el valor de la variable de sesión 'modo'
   * @return el valor de la variable de sesión 'modo'
   */
  public static String getModo() {
    return "" + Sessions.getCurrent().getAttribute("modo");
  }

  /**
   * setea la variable de sesión 'vistaActual'
   */
  public static void setVistaActual(String vista) {
    Sessions.getCurrent().setAttribute("vistaActual", vista);
  }

  /**
   * obtiene la variable de sesión 'vistaActual'
   * @return 
   */
  public static String getVistaActual() {
    return "" + Sessions.getCurrent().getAttribute("vistaActual");
  }

  /**
   * setea la variable de sesión 'vistaSiguiente'
   */
  public static void setVistaSiguiente(String vista) {
    Sessions.getCurrent().setAttribute("vistaSiguiente", vista);
  }

  /**
   * obtiene la variable de sesión 'vistaSiguiente'
   * @return 
   */
  public static String getVistaSiguiente() {
    return "" + Sessions.getCurrent().getAttribute("vistaSiguiente");
  }

  /**
   * método re-usable para mostrar variable de sesión
   */
  public static void mostrarVariableSesion(Object claseInvocante, String metodo, String variable, Object valor) {
    System.out.println(claseInvocante.getClass() + "- " + metodo + ": SESION: " + variable + "=" + valor);
  }

  /**
   * método debug
   */
  public static void mostrarVistaActual(Object claseInvocante, String metodo) {
    mostrarVariableSesion(claseInvocante, metodo, "vistaActual", getVistaActual());
  }

  /**
   * método debug
   */
  public static void mostrarVistaSiguiente(Object claseInvocante, String metodo) {
    mostrarVariableSesion(claseInvocante, metodo, "vistaSiguiente", getVistaSiguiente());
  }

  /**
   * método debug
   */
  public static void mostrarModo(Class clase, String metodo) {
    mostrarVariableSesion(clase, metodo, "modo", getModo());
  }
  
  public static boolean modoEditable(){
    return Sesion.getModo().equals("ver-modificable");
  }
  
  public static boolean modoIngresar(){
    return Sesion.getModo().equals("new");
  }
}
