/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waytech.utilidades;

import rocket.controladores.general.Sesion;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;

/**
 *
 * @author root
 */
public class Util {

  /**
   * setea un label de 'rol' de estado, true o false
   * con sí o no
   * dependiendo del status true/false
   * del segundo parámetro
   * @param etqRol
   * @param status true o false
   */
  public static void mostrarRol(Label etqRol, boolean status) {
    if (status) {
      etqRol.setValue("Sí");
      etqRol.setSclass("checksi");
    } else {
      //opción A:
      etqRol.setValue("No");
      etqRol.setSclass("checkno");
      //opción B:
      //etqRol.setVisible(false);
    }
  }

  /**
   * recupera parámetro de sesión 'idRed' que viene de la vista llamante
   */
  public static int buscarIdRed(Class claseInvocante) {
    int id;
    try {
      id = (Integer) Sesion.getVariable("idRed");
    } catch (Exception e) {
      System.out.println(claseInvocante + "." + " -> ERROR: variable de sesión 'idRed'");
      id = 1; //debería ser el id del usuario fantasma
    }
    return id;
  }

  /**
   * recupera parámetro de sesión 'idUsuario' que viene de la vista llamante
   */
  public static int buscarIdUsuario(Class claseInvocante) {
    int id;
    try {
      id = (Integer) Sesion.getVariable("idUsuario");
    } catch (Exception e) {
      System.out.println(claseInvocante + "." + " -> ERROR: variable de sesión 'Usuario'");
      id = 0; //debería ser el id del usuario fantasma
    }
    return id;
  }

  /**
   * recupera parámetro de sesión 'idUsuario' que viene de la vista llamante
   */
  public static int buscarTipoUsuario(Class claseInvocante) {
    int tipo;
    try {
      tipo = (Integer) Sesion.getVariable("tipoUsuario");
    } catch (Exception e) {
      System.out.println(claseInvocante + "." + " -> ERROR: variable de sesión 'tipoUsuario'");
      tipo = 0;
    }
    return tipo;
  }

  public static void marcarRol(Checkbox checkRol, boolean status) {
    if (status) {
      checkRol.setChecked(true);
    }
  }

  /**
   * concatena día y hora
   * @param dia 1:lunes,..., 7:domingo
   * @param hora 8>>800am,..., 20>>8.00 pm
   * @return día y hora formateada, ejemplo: "Jueves, 7:30 p.m."
   */
  //TODO: hacer las conversiones necesarias
  public static String getDiaHora(String dia, String hora) {
    if (dia.isEmpty() || hora.isEmpty()) {
      return "No asignados";
    }
    return UtilFechas.convertirDiaSemanaTextoCompleto(dia) + " - " + UtilFechas.convertirHoraTextoCompleto(hora);
  }

  public static String generarDireccionCorta(String ciudad, String zona) {
    if (zona.isEmpty()) {
      return "No asignada";
    }
    return zona + ", " + ciudad;
  }
}
