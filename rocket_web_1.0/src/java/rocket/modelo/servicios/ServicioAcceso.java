/*
 * Capa que usa los servicios
 * para que los controladores accedan a la base de datos
 * de una manera más sencilla
 */
package rocket.modelo.servicios;

import rocket.modelo.bd.util.UsuarioUtil;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.servicios.RspAcceso;
import waytech.modelo.servicios.SaAcceso;

/**
 *
 * @author Gabriel
 */
public class ServicioAcceso {

  /**
   * busca usuario por su login y pasword
   * @param login el nombre de usuario
   * @param password la contraseña
   * @return el objeto Usuario
   */
  //TODO: sacar este método de aquí a clase ServicioAcceso o ServicioUsuario
  public static UsuarioUtil buscarUsuario(String login, String password) {
    SaAcceso saAcceso = new SaAcceso();
    RspAcceso rspAcceso = new RspAcceso();
    rspAcceso = saAcceso.esLoginPasswordValido(login, password);
    if (rspAcceso.esLoginPasswordValido()) {
      UsuarioUtil u = new UsuarioUtil();
      Persona p = rspAcceso.getAcceso().getIdPersona();
      u.setCedula(p.getCi());
      u.setNombre(p.getNombre());
      u.setId(p.getIdPersona());
      u.setIdRed(p.getRed().getIdRed());
      u.setNombreRed(p.getRed().getNombre());
      u.setTipo(p.getIdTipoPersona().getIdTipoPersona());
      System.out.println("ServicioAcceso.tipoUsuario(Antes)=" + p.getIdTipoPersona().getIdTipoPersona());
      System.out.println("ServicioAcceso.tipoUsuario(Después)=" + u.getTipo());
      u.setLiderRed(p.esLiderRed());
      u.setLiderCelula(p.esLiderCelula());
      u.setAnfitrionCelula(p.esAnfitrion());
      return u;
    } else {
      return null;//error
    }
  }
}