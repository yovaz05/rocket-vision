/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waytech.utilidades;

import sig.controladores.Sesion;
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
   * recupera parámetro de sesión 'idUsuario' que viene de la vista llamante
   */
  public static int buscarIdRed(Class claseInvocante) {
    int idRed;
    try {
      idRed = (Integer) Sesion.getVariable("idRed");
    } catch (Exception e) {
      System.out.println(claseInvocante + "." + " -> ERROR: variable de sesión 'idRed'");
      idRed = 0;
    }
    return idRed;
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
}
