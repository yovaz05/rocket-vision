/*
 * Capa que usa los servicios
 * para que los controladores accedan a la base de datos
 * de una manera más sencilla
 */
package rocket.modelo.servicios;

import sig.controladores.Sesion;
import cdo.sgd.modelo.bd.util.CelulaListadoUtil;
import cdo.sgd.modelo.bd.util.CelulaUtil;
import cdo.sgd.modelo.bd.util.Direccion;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.Celula;
import waytech.modelo.beans.sgi.CelulaInsert;
import waytech.modelo.beans.sgi.PersonaEnCelula;
import waytech.modelo.beans.sgi.PersonaEnCelulaInsert;
import waytech.modelo.beans.sgi.Zona;
import waytech.modelo.servicios.RspCelula;
import waytech.modelo.servicios.RspPersonaEnCelula;
import waytech.modelo.servicios.SaCelula;
import waytech.modelo.servicios.SaPersonaEnCelula;
import waytech.utilidades.UtilFechas;

/**
 *
 * @author Gabriel
 */
public class ServicioCelula {

  List<Celula> celulas = new ArrayList();
  List<String> nombres = new ArrayList();
  String nombreCelula = "";
  //tendrá los nombres de las Celulaes del 'estado elegido'
  List<String> nombreLideresCelula;
  List<PersonaEnCelula> lideresCelula;
  //TODO: OPTIMIZACION: sacar estas 2 líneas en todos los servicios
  SaCelula saCelula = new SaCelula();
  SaPersonaEnCelula saPersonaCelula = new SaPersonaEnCelula();
  List<CelulaListadoUtil> listadoCelulas = new ArrayList();
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
  int idCelula = 0;
  Celula celula = new Celula();
          
  public List getAll() {
    RspCelula rspCelula = saCelula.listCelulaActiva();
    System.out.println("ServicioCelula.INICIO DE LA CONEXION BD" + rspCelula.getRespuestaInicioDeConexion());
    celulas = rspCelula.getTodosLosCelulas();
    if (celulas == null) {
      System.out.println("ServicioCelula. rspCelula.getTodosLosCelulas(). devuelve null");
    }
    listarConsolaAll();
    System.out.println("ServicioCelula.CIERRE DE LA CONEXION BD" + rspCelula.getRespuestaInicioDeConexion());
    return celulas;
  }

  public List getNombres() {
    getAll();
    for (Celula Celula : celulas) {
      nombres.add(Celula.getNombre());
    }
    //**listarConsolaAll();
    return nombres;
  }

  public void listarConsolaAll() {
    if (celulas == null) {
      System.out.println("ServicioCelula.listaTodas=null");
    }
    System.out.println("NOMBRE DE TODAS LAS Células:");
    for (Celula celula : celulas) {
      System.out.println("Celula: " + celula.getNombre());
    }
  }

  /**
   * crear una célula en la base de datos
   * con todos los datos en el objeto CelulaInsert
   * @param celulaInsert el objeto con los datos
   * @return el id de la célula creada, sino se insertó se devuelve 0
   */
  //TODO: MEJORA CODIGO: método no usado
  public int insertCelula(CelulaInsert celulaInsert) {
    RspCelula rspCelula = saCelula.insertCelula(celulaInsert);
    boolean ok = rspCelula.esSentenciaSqlEjecutadaExitosamente();
    if (ok) {
      idCelula = rspCelula.getCelula().getIdCelula();
      return idCelula;
    }
    return 0;
  }

  /**
   * crear una célula en la base de datos
   * con sólo el código y la red
   * @param nuevaCelula el objeto Celula con los datos
   * @return el id de la célula creada
   */
  public int ingresarCelula(String codigo, int idRed) {
    System.out.println("ServicioCelula.crearCelula.codigo=" + codigo);
    System.out.println("ServicioCelula.crearCelula.idRed=" + idRed);

    CelulaInsert celulaInsert = new CelulaInsert();
    //datos ingresados:
    celulaInsert.setCodigo(codigo);
    celulaInsert.setIdRed(idRed);
    //datos por defecto:
    celulaInsert.setDia(0);
    celulaInsert.setHora(0);
    celulaInsert.setNombre("");
    //TODO: Debe usarse una zona de utilería
    celulaInsert.setIdZona(1);
    celulaInsert.setDireccion("");
    celulaInsert.setTelefono("");
    celulaInsert.setFechaApertura("");
    celulaInsert.setAnfitrion("");
    celulaInsert.setObservaciones("");

    RspCelula respuesta = saCelula.insertCelula(celulaInsert);
    if (respuesta.esSentenciaSqlEjecutadaExitosamente()) {
      System.out.println("ServicioCelula.crearCelula.codigo=" + codigo);
      System.out.println("ServicioCelula.crearCelula.idRed=" + idRed);
      celula = respuesta.getCelula();
      if (celula == null) {
        System.out.println("respuesta.celula == null");
      }
      idCelula = celula.getIdCelula();
    } else {
      System.out.println("ERROR->ServicioCelula.crearCelula.codigo=");
      System.out.println(respuesta.getRespuestaServicio());
      idCelula = 0;
    }
    return idCelula;
  }

  /**
   * agregar lider a la célula
   * @param idLider
   * @return 
   */
  public boolean agregarLiderCelula(int idLider) {
    PersonaEnCelulaInsert persona = new PersonaEnCelulaInsert();
    persona.setIdCelula(idCelula);
    persona.setIdPersona(idLider);
    persona.setEsLiderCelula(true);
    RspPersonaEnCelula respuesta = saPersonaCelula.insertPersonaEnCelula(persona);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * Convierte la lista de células a una lista de tipo listadoCelulas
   * tipo: 'Celula' a tipo 'CelulaListadoSimulador'
   * @return listado de células
   * @author Gerardo Montilla
   */
  //MEJORAR CODIGO CON generarReporteCelulaUtil  
  public List<CelulaListadoUtil> getCelulasListado() {
    getAll();
    int n = 0;
    for (Celula celulaBD : celulas) {
      CelulaListadoUtil celulaListado = new CelulaListadoUtil();
      idCelula = celulaBD.getIdCelula();
      //**System.out.println("ServicioCelula.getCelulasListado.celulaBD.id=" + idCelula);
      celulaListado.setId(idCelula);
      
      celulaListado.setNroItem(++n);
      String codigoCelula = celulaBD.getCodigo();
      celulaListado.setCodigo(codigoCelula);

      String ciudad = celulaBD.getZona().getIdCiudad().getNombre();
      String zona = celulaBD.getZona().getNombre();
      celulaListado.setDireccionCorta(generarDireccionCorta(ciudad, zona));

      if (celulaBD.getDia() == 0) {
        celulaListado.setDia("");
      } else {
        celulaListado.setDia("" + celulaBD.getDia());
      }
      if (celulaBD.getHora() == 0) {
        celulaListado.setHora("");
      } else {
        celulaListado.setHora("" + celulaBD.getHora());
      }

      celulaListado.setAnfitrion(celulaBD.getAnfitrion());
      celulaListado.setFechaApertura(celulaBD.getFechaApertura());

      List<PersonaEnCelula> listaPersonaCelula = new ArrayList<PersonaEnCelula>();
      listaPersonaCelula = saPersonaCelula.listLiderCelulaPorIdCelula(idCelula).getAllPersonaEnCelulas();

      int i = 0;

      for (PersonaEnCelula personaCelula : listaPersonaCelula) {
        i++;
        int idLider = personaCelula.getIdPersona().getIdPersona();
        String nombreLider = personaCelula.getIdPersona().getNombre();
        if (i == 1) {
          celulaListado.setIdLider1(idLider);
          celulaListado.setNombreLider1(nombreLider);
        } else if (i == 2) {
          celulaListado.setIdLider2(idLider);
          celulaListado.setNombreLider2(nombreLider);
        } else if (i == 3) {
          celulaListado.setIdLider3(idLider);
          celulaListado.setNombreLider3(nombreLider);
        } else if (i == 4) {
          celulaListado.setIdLider4(idLider);
          celulaListado.setNombreLider4(nombreLider);
        }
        celulaListado.setNumeroLideres(i);
      }
      celulaListado.setNombre(celulaBD.getNombre());
      celulaListado.setIdRed(celulaBD.getRed().getIdRed());
      celulaListado.setNombreRed(celulaBD.getRed().getNombre());
      listadoCelulas.add(celulaListado);
    }
    return listadoCelulas;
  }

  /**
   * genera objeto CelulaUtil a partir de un objeto Celula
   */
  CelulaUtil generarCelulaUtil(Celula celula) {
    CelulaUtil c = new CelulaUtil();

    //**
    System.out.println("ServicioCelula.celula=" + celula.toString());

    //data básica de la célula
    //-int idCelula = celula.getIdCelula();
    idCelula = celula.getIdCelula();
    c.setId(idCelula);
    c.setCodigo(celula.getCodigo());
    c.setNombre(celula.getNombre());
    c.setNombreRed(celula.getRed().getNombre());
    c.setIdRed(celula.getRed().getIdRed());
    c.setDireccionCorta(celula.getZona().getNombre() + ", " + celula.getZona().getIdCiudad().getNombre());
    int diaNumero = celula.getDia();
    int horaNumero = celula.getHora();
    c.setDia(UtilFechas.convertirDiaSemanaTextoCompleto("" + diaNumero));
    c.setHora(UtilFechas.convertirHoraTextoCompleto("" + horaNumero));
    c.setAnfitrion(celula.getAnfitrion());

    c.setFechaApertura(celula.getFechaApertura());
    //**
    System.out.println("ServicioCelula.fechaApertura=" + celula.getFechaApertura());

    c.setObservaciones(celula.getObservaciones());

    //data de los líderes:
    List<PersonaEnCelula> listaPersonaCelula = new ArrayList<PersonaEnCelula>();
    listaPersonaCelula = saPersonaCelula.listPersonaEnCelulaPorIdCelula(idCelula).getAllPersonaEnCelulas();
    int i = 0;
    System.out.println("ServicioCelula.CELULA.id=" + idCelula + ", codigo=" + c.getCodigo());
    System.out.println("ServicioCelula.recorrido de líderes de la célula");
    c.setNumeroLideres(0);
    for (PersonaEnCelula personaCelula : listaPersonaCelula) {
      if (personaCelula.esLiderCelula()) {
        i++;
        System.out.println("ServicioCelula.i=" + i);
        int idLider = personaCelula.getIdPersona().getIdPersona();
        String nombreLider = personaCelula.getIdPersona().getNombre();
        System.out.println("ServicioCelula.Lider" + i + ".nombre=" + nombreLider);
        System.out.println("ServicioCelula.Lider" + i + ".id=" + idLider);
        if (i == 1) {
          c.setIdLider1(idLider);
          c.setNombreLider1(nombreLider);
        } else if (i == 2) {
          c.setIdLider2(idLider);
          c.setNombreLider2(nombreLider);
        } else if (i == 3) {
          c.setIdLider3(idLider);
          c.setNombreLider3(nombreLider);
        } else if (i == 4) {
          c.setIdLider4(idLider);
          c.setNombreLider4(nombreLider);
        }
        //TODO: mejora de código:
        c.setNumeroLideres(i);
      }
    }

    //data de dirección:
    Zona zona = celula.getZona();
    Direccion dir = new Direccion();

    dir.setIdZona(zona.getIdZona());
    dir.setIdCiudad(zona.getIdCiudad().getIdCiudad());
    dir.setIdEstado(zona.getIdCiudad().getIdEstado().getIdEstado());

    dir.setZona(zona.getNombre());
    dir.setCiudad(zona.getIdCiudad().getNombre());
    dir.setEstado(zona.getIdCiudad().getIdEstado().getNombre());
    dir.setTelefono(celula.getTelefono());
    dir.setDirDetallada(celula.getDireccion());
    /**/
    c.setDireccion(dir);
    c.setEstatusReporteSemanaActual(celula.getEstado());
    return c;
  }

  /**
   * busca en la base de datos, la célula por el id proporcionado
   * como parámetro
   * devuelve los datos en un objeto CelulaUtil
   * @param idCelula
   * @return Celula la celula con el id, o null si no existe
   */
  public CelulaUtil getCelula(int idCelula) {
    setIdCelula(idCelula);
    //traer datos de la célula
    RspCelula respuesta = saCelula.getCelulaPorIdCelula(idCelula);
    Celula celulaBD = new Celula();
    celulaBD = respuesta.getCelula();
    if (celulaBD == null) {
      System.out.println("Error en ServicioCelula.getDatosCelula:celulla NULL");
      System.out.println("ServicioCelula.encontrada? ERROR");
      return null;//error
    }
    System.out.println("ServicioCelula.celulaBD=" + celulaBD.toString());
    CelulaUtil celulaUtil = generarCelulaUtil(celulaBD);
    setNumeroLideresUsados();
    return celulaUtil;
  }

  /**
   * Optimización de código:
   * 1 .getCelulaPorId debe hacer un llamada directo a la base de datos
   * 2. método para crear un objeto 'Celula' del simuladorBD a partir de una Celula bde la BD
   * 3. método para crear un objeto 'CelulaListadoSimulador' del simulador a partir de un objeto Celula del simuladorBD
   * 4. mejorar método getListadoCelulas con los métodos de las tareas anteriores
   **/
  /**
   * establece el número de líderes usados en la célula
   * en una variable de sesión
   * para ser usada por los controladores
   */
  void setNumeroLideresUsados() {
    nLideresUsados = 1;
    if (idLider2 != 0) {
      nLideresUsados = 2;
    } else if (idLider3 != 0) {
      nLideresUsados = 3;
    } else if (idLider4 != 0) {
      nLideresUsados = 4;
    }
    Sesion.setVariable("celula.nLideresUsados", nLideresUsados);
  }

  public int getIdCelula() {
    return idCelula;
  }

  public void setIdCelula(int idCelula) {
    this.idCelula = idCelula;
  }

  /**
   * actualiza el código de la célula en la base de datos
   */
  public boolean actualizarCodigo(String codigo) {
    RspCelula respuesta = saCelula.updateCodigoCelula(idCelula, codigo);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
    //return true;
    //TODO: devolver si hubo éxito o error
  }

  /**
   * actualiza el día de la célula
   */
  public boolean actualizarDia(int dia) {
    RspCelula respuesta = saCelula.updateDiaCelula(idCelula, dia);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza la hora de la célula
   */
  public boolean actualizarHora(int hora) {
    RspCelula respuesta = saCelula.updateHoraCelula(idCelula, hora);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza el nombre de la célula
   */
  public boolean actualizarNombre(String nombre) {
    RspCelula respuesta = saCelula.updateNombreCelula(idCelula, nombre);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza el id de la zona (o sector)
   */
  public boolean actualizarIdZona(int idCelula, int idZona) {
    RspCelula respuesta = saCelula.updateIdZonaCelula(idCelula, idZona);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza el detalle de la dirección de la célula
   */
  public boolean actualizarDireccionDetalle(int idCelula, String direccionDetalle) {
    RspCelula respuesta = saCelula.updateDireccionCelula(idCelula, direccionDetalle);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza el teléfono de la dirección de la célula
   */
  public boolean actualizarTelefono(int idCelula, String telefono) {
    RspCelula respuesta = saCelula.updateTelefonoCelula(idCelula, telefono);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza la fecha de apertura
   */
  public boolean actualizarFechaApertura(String fechaApertura) {
    RspCelula respuesta = saCelula.updateFechaAperturaCelula(idCelula, fechaApertura);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza el nombre del anfitrión
   */
  public boolean actualizarAnfitrion(String nombreAnfitrion) {
    RspCelula respuesta = saCelula.updateAnfitrionCelula(idCelula, nombreAnfitrion);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * actualiza las observaciones
   */
  public boolean actualizarObservaciones(int idCelula, String observaciones) {
    RspCelula respuesta = saCelula.updateObservacionesCelula(idCelula, observaciones);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  /**
   * elimina a un líder de la célula
   * @param idLider
   * @return 
   */
  public boolean eliminarLider(int idCelula, int idLider) {
    System.out.println("ServicioCelula.deleteLider.idLider=" + idLider);
    RspPersonaEnCelula respuesta = saPersonaCelula.deletePersonaEnCelula(idCelula, idLider);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean actualizarRed(int idCelula, int idRed) {
    RspCelula respuesta = saCelula.updateIdRedCelula(idCelula, idRed);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }

  public boolean existeCelula(String codigo) {
    RspCelula respuesta = saCelula.esCodigoCelulaExistente(codigo);
    boolean existe = false;
    if (respuesta.esSentenciaSqlEjecutadaExitosamente()) {
      existe = respuesta.esCodigoCelulaExistente();
    }
    return existe;
  }

  private String generarDireccionCorta(String ciudad, String zona) {
    if (zona.isEmpty()) {
      return "No asignada";
    }
    return zona + ", " + ciudad;
  }

  public boolean actualizarEstado(int estatus) {
    RspCelula respuesta = saCelula.updateEstadoCelula(idCelula, estatus);
    return respuesta.esSentenciaSqlEjecutadaExitosamente();
  }
}
