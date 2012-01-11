/*
 * Capa que usa los servicios
 * para que los controladores accedan a la base de datos
 * de una manera más sencilla
 */
package sig.modelo.servicios;

import sig.controladores.Sesion;
import sig.modelo.bd.util.Direccion;
import sig.modelo.bd.util.LiderListadoUtil;
import sig.modelo.bd.util.LiderUtil;
import sig.modelo.bd.util.LiderUtil;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.Acceso;
import waytech.modelo.beans.sgi.Persona;
import waytech.modelo.beans.sgi.PersonaInsert;
import waytech.modelo.beans.sgi.Zona;
import waytech.modelo.interfaces.IsaAcceso;
import waytech.modelo.interfaces.IsaPersona;
import waytech.modelo.servicios.RspAcceso;
import waytech.modelo.servicios.RspPersona;
import waytech.modelo.servicios.SaAcceso;
import waytech.modelo.servicios.SaPersona;

/**
 *
 * @author Gabriel
 */
public class ServicioPersona {

  List<Persona> lideresLanzados = new ArrayList();
  List<String> nombres = new ArrayList();
  String nombreLider = "";
  List<LiderUtil> listado = new ArrayList();
  //tendrá los nombres de las Lideres del 'estado elegido'
  List<String> nombreLideresLider;
  List<Persona> lideresInmediatos;
  SaPersona saPersona = new SaPersona();
  SaAcceso saAcceso = new SaAcceso();
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
    //** listarConsolaAll();
    //** System.out.println("CIERRE DE LA CONEXION " + respuesta.getRespuestaInicioDeConexion());
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
    System.out.println("ServicioPersona.generarLiderUtil.persona=>" + p.toString());

    //data resumida del líder
    idLider = p.getIdPersona();
    lider.setId(idLider);
    //TODO: cambiar cédula en lider como string
    lider.setCedula(p.getCi());
    lider.setNombre(p.getNombre());
    //TODO: OJO, asignar apropiadamente el valor de estos cmapos
    lider.setIdRed(p.getRed().getIdRed());
    lider.setNombreRed(p.getRed().getNombre());
    //-es automática:
    //-lider.setDireccionCorta(p.getDireccionHabitacion());
    lider.setTelefono(p.getTelefonoMovil());
    lider.setEmail(p.getCorreo());
    
    /* TODO: OJO: DISCUTIR: no se obtiene el correo del acceso*/
    /*
    IsaAcceso servAcceso = new SaAcceso();
    Acceso acceso = servAcceso.getAccesoPorIdUsuario(idLider).getAcceso();
    lider.setEmail(acceso.getCorreo());    
    /**/
    
    //dirección:
    Zona zona = p.getIdZona();
    Direccion dir = new Direccion();
    dir.setIdZona(zona.getIdZona());
    dir.setIdCiudad(zona.getIdCiudad().getIdCiudad());
    dir.setIdEstado(zona.getIdCiudad().getIdEstado().getIdEstado());
    dir.setZona(zona.getNombre());
    dir.setCiudad(zona.getIdCiudad().getNombre());
    dir.setEstado(zona.getIdCiudad().getIdEstado().getNombre());
    //+dir.setTelefono(celula.getTelefono());
    dir.setDirDetallada(p.getDireccionHabitacion());
    lider.setDireccion(dir);

    //**
    System.out.println("ServicioPersona.generarLiderUtil.lider=>" + lider.toString());
    
    return lider;
  }

  /**
   * busca en la base de datos una persona por el id proporcionado como parámetro
   * devuelve los datos en un objeto LiderUtil
   * @param idLider
   * @return Lider la Lider con el id, o null si no existe
   */
  public LiderUtil getLider(int idLider) {
    //traer datos de la célula
    RspPersona respuesta = saPersona.getLiderLanzadoPorIdPersona(idLider);
    Persona liderBD = new Persona();
    liderBD = respuesta.getPersona();
    if (liderBD == null) {
      System.out.println("Error en ServicioLider.getLider:celulla NULL");
      System.out.println("ServicioLider.encontrado: ERROR");
      return null;//error
    }
    System.out.println("ServicioLider.getLider.datos del líder=" + liderBD.toString());
    LiderUtil liderUtil = generarLiderUtil(liderBD);
    return liderUtil;
  }

  /**
   * crear una célula en la base de datos
   * con sólo el código y la red
   * @param nuevaLider el objeto Lider con los datos
   * @return el id de la célula creada
   */
  public int ingresarLider(String cedula, String nombre, int idRed) {
    System.out.println("ServicioLider.crearLider.Cedula=" + cedula);
    System.out.println("ServicioLider.crearLider.nombre=" + nombre);
    System.out.println("ServicioLider.crearLider.idRed=" + idRed);

    RspPersona respuesta = saPersona.insertPersonaLiderBasico(cedula, nombre, idRed);
    if (respuesta.esSentenciaSqlEjecutadaExitosamente()) {
      idLider = respuesta.getPersona().getIdPersona();
      System.out.println("ServicioLider.crearLider.id=" + idLider);
    } else {
      idLider = 0;
    }
    return idLider;
  }

  /**
   * actualiza la cédula
   */
  public boolean actualizarCedula(int idPersona, String Cedula) {
    RspPersona respuesta = saPersona.updateCedula(idPersona, Cedula);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
    //return true;
    //TODO: devolver si hubo éxito o error
  }

  /**
   * actualiza el nombre
   */
  public boolean actualizarNombre(int idPersona, String nombre) {
    RspPersona respuesta = saPersona.updateNombre(idPersona, nombre);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza la red a la que pertenece la persona
   * @param idPersona
   * @param idRed
   * @return 
   */
  public boolean actualizarRed(int idPersona, int idRed) {
    RspPersona respuesta = saPersona.updateRed(idPersona, idRed);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza la zona (sector)
   */
  public boolean actualizarIdZona(int idPersona, int idZona) {
    RspPersona respuesta = saPersona.updateZona(idPersona, idZona);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza el detalle de la dirección
   */
  public boolean actualizarDireccionDetalle(int idPersona, String direccionDetalle) {
    RspPersona respuesta = saPersona.updateDireccion(idPersona, direccionDetalle);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza el teléfono móvil
   */
  public boolean actualizarTelefono(int idPersona, String telefono) {
    RspPersona respuesta = saPersona.updateTelefonoMovil(idPersona, telefono);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }
  
  /**
   * actualiza el correo
   */
  //TODO: discutir, se actualiza código de la persona y del acceso
  public boolean actualizarCorreo(int idPersona, String correo) {
    boolean ok, ok2;
    RspPersona respuesta = saPersona.updateCorreo(idPersona, correo);
    ok = respuesta.esSentenciaSqlEjecutadaExitosamente();
    RspAcceso respuesta2 = saAcceso.updateCorreo(idPersona, correo);
    ok2 = respuesta2.esSentenciaSqlEjecutadaExitosamente();
    return (ok & ok2);
  }

  /**
   * actualiza las observaciones
   */
  public boolean actualizarObservaciones(int idPersona, String observaciones) {
    RspPersona respuesta = saPersona.updateObservaciones(idLider, observaciones);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * elimina a una persona de la base de datos
   * @param idLider
   * @return 
   */
  public boolean eliminarPersona(int idPersona) {
    System.out.println("ServicioPersona.deleteLider.idLider=" + idPersona);
    RspPersona respuesta = saPersona.deletePersona(idPersona);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }
  
 public boolean existeCedula(String cedula) {
    RspPersona respuesta = saPersona.esCedulaExistente(cedula);
    boolean existe = true;
    if (respuesta.esSentenciaSqlEjecutadaExitosamente()) {
      existe = respuesta.esCedulaExistente();
    }
    return existe;
  }  
}