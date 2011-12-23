/*
 * Capa que usa los servicios
 * para que los controladores accedan a la base de datos
 * de una manera más sencilla
 */
package sig.modelo.servicios;

import cdo.sgd.controladores.Sesion;
import cdo.sgd.modelo.bd.util.LiderListadoUtil;
import cdo.sgd.modelo.bd.util.LiderUtil;
import cdo.sgd.modelo.bd.util.Direccion;
import cdo.sgd.modelo.bd.util.LiderUtil;
import cdo.sgd.modelo.bd.util.Usuario;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.Acceso;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.beans.sgi.PersonaInsert;
import waytech.modelo.beans.sgi.Zona;
import waytech.modelo.interfaces.IsaAcceso;
import waytech.modelo.interfaces.IsaPersona;
import waytech.modelo.servicios.RspPersona;
import waytech.modelo.servicios.SaAcceso;
import waytech.modelo.servicios.SaPersona;
import waytech.utilidades.UtilFechas;

/**
 *
 * @author Gabriel
 */
public class ServicioLider {

  List<Persona> lideresLanzados = new ArrayList();
  List<String> nombres = new ArrayList();
  String nombreLider = "";
  List<LiderUtil> listado = new ArrayList();
  //tendrá los nombres de las Lideres del 'estado elegido'
  List<String> nombreLideresLider;
  List<Persona> lideresInmediatos;
  SaPersona saPersona = new SaPersona();
  String nombreLider1 = "";
  String nombreLider2 = "";
  String nombreLider3 = "";
  String nombreLider4 = "";
  int idLider1 = 0;
  int idLider2 = 0;
  int idLider3 = 0;
  int idLider4 = 0;
  int nLideresUsados = 1;
  //TODO: usar este id para las modificaciones
  int idLider = 0;

  public List getTodosLideresLanzados() {
    RspPersona respuesta = saPersona.listPersonaLiderLanzado();
    //**System.out.println("INICIO DE LA CONEXION " + respuesta.getRespuestaInicioDeConexion());
    lideresLanzados = respuesta.getTodasLasPersonas();
    if (lideresLanzados == null) {
      System.out.println("ServicioLider. rspLider.getTodosLosLiders(). devuelve null");
    }
    listarConsolaAll();
    //**System.out.println("CIERRE DE LA CONEXION " + respuesta.getRespuestaInicioDeConexion());
    return lideresLanzados;
  }

  /**
   * genera lista de líderes lanzados del tipo LiderListadoUtil
   * @return 
   */
  public List getTodosLideresLanzadosListado() {
    List<LiderListadoUtil> listaLiderListado = new ArrayList<LiderListadoUtil>();
    int i = 0;
    getTodosLideresLanzados();
    for (Persona p : lideresLanzados) {
      LiderUtil lider = generarLiderUtil(p);
      LiderListadoUtil liderListado = new LiderListadoUtil(++i, lider);
      listaLiderListado.add(liderListado);
    }
    return listaLiderListado;
  }

  public List getTodosLideresLanzadosNombres() {
    getTodosLideresLanzados();
    for (Persona lider : lideresLanzados) {
      nombres.add(lider.getNombre());
    }
    return nombres;
  }

  public void listarConsolaAll() {
    if (lideresLanzados == null) {
      System.out.println("ServicioLider.listaTodas=null");
    }
    System.out.println("NOMBRE DE TODAS LAS Células:");
    for (Persona lider : lideresLanzados) {
      System.out.println("Lider: " + lider.getNombre());
    }
  }

  /**
   * genera objeto LiderUtil a partir de una persona
   * @param p
   * @return 
   */
  LiderUtil generarLiderUtil(Persona p) {
    LiderUtil lider = new LiderUtil();

    //**
    System.out.println("ServicioLider.Lider=" + lider.toString());

    //data resumida del líder
    idLider = p.getIdPersona();
    lider.setId(idLider);
    //TODO: cambiar cédula en lider como string
    lider.setCedula(p.getCi());
    lider.setNombre(p.getNombre());
    //TODO: OJO, asignar apropiadamente el valor de estos cmapos
    lider.setIdRed(1);
    lider.setDireccionCorta(p.getDireccionHabitacion());
    lider.setTelefonoPrincipal(p.getTelefonoMovil());
    /* pendiente: obtener el correo
    IsaAcceso servAcceso = new SaAcceso();
    Acceso acceso = servAcceso.getAccesoPorIdUsuario(idLider).getAcceso();
    lider.setEmail(acceso.getCorreo());    
     * 
     */
    lider.setEmail("");
    return lider;
  }
}