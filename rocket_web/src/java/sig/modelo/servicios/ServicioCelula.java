/*
 * Capa que usa los servicios
 * para que los controladores accedan a la base de datos
 * de una manera más sencilla
 */
package sig.modelo.servicios;

import cdo.sgd.controladores.Sesion;
import cdo.sgd.modelo.bd.simulador.CelulaListadoUtil;
import cdo.sgd.modelo.bd.simulador.CelulaUtil;
import cdo.sgd.modelo.bd.simulador.Direccion;
import java.util.ArrayList;
import java.util.List;
import waytech.modelo.beans.sgi.Celula;
import waytech.modelo.beans.sgi.CelulaInsert;
import waytech.modelo.beans.sgi.PersonaEnCelula;
import waytech.modelo.beans.sgi.PersonaEnCelulaInsert;
import waytech.modelo.beans.sgi.Zona;
import waytech.modelo.interfaces.IsaCelula;
import waytech.modelo.interfaces.IsaPersonaEnCelula;
import waytech.modelo.servicios.RspCelula;
import waytech.modelo.servicios.RspPersonaEnCelula;
import waytech.modelo.servicios.SaCelula;
import waytech.modelo.servicios.SaPersonaEnCelula;
import waytech.utilidades.Util;

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
  IsaCelula isaCelula = new SaCelula();
  RspCelula rspCelula = new RspCelula();
  IsaPersonaEnCelula isaPersonaCelula = new SaPersonaEnCelula();
  RspPersonaEnCelula rspPersonaCelula = new RspPersonaEnCelula();
  List<CelulaListadoUtil> listadoCelulas = new ArrayList();

  public List getAll() {
    rspCelula = isaCelula.listCelula();
    System.out.println("INICIO DE LA CONEXION " + rspCelula.getRespuestaInicioDeConexion());
    celulas = rspCelula.getTodosLosCelulas();
    if (celulas == null) {
      System.out.println("ServicioCelula. rspCelula.getTodosLosCelulas(). devuelve null");
    }
    listarConsolaAll();
    System.out.println("CIERRE DE LA CONEXION " + rspCelula.getRespuestaInicioDeConexion());
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
    System.out.println("NOMBRE DE TODAS LAS Celulas:");
    for (Celula celula : celulas) {
      System.out.println("Celula: " + celula.getNombre());
    }
  }

  /**
   * inserta los datos de la célula en la base de datos.
   * tabla: celula
   * @param nuevaCelula el objeto Celula con los datos
   * @return el id de la célula creada
   */
  public int ingresarDatosCelula(CelulaInsert celulaInsert) {
    int id = 0;
    rspCelula = isaCelula.insertCelula(celulaInsert);
    boolean ok = rspCelula.esSentenciaSqlEjecutadaExitosamente();
    if (ok) {
      id = rspCelula.getCelula().getIdCelula();
    }
    return id;
  }

  public boolean ingresarLiderCelula(PersonaEnCelulaInsert persona) {
    boolean ok = false;
    rspPersonaCelula = isaPersonaCelula.insertPersonaEnCelula(persona);
    ok = rspCelula.esSentenciaSqlEjecutadaExitosamente();
    return ok;
  }

  /**
   * Convierte la lista de células a una lista de tipo listadoCelulas
   * tipo: 'Celula' a tipo 'CelulaListadoSimulador'
   */
  //REALIZADO POR GERARDO MONTILLA
  public List<CelulaListadoUtil> getCelulasListado() {
    getAll();
    int n = 0;
    for (Celula celulaBD : celulas) {
      CelulaListadoUtil celulaListado = new CelulaListadoUtil();
      int idCelula = celulaBD.getIdCelula();
      System.out.println("ServicioCelula.getCelulasListado.celulaBD.id=" + idCelula);
      celulaListado.setId(idCelula);

      String codigoCelula = celulaBD.getCodigo();
      celulaListado.setCodigo(codigoCelula);

      //TODO: no hará falta este método, porque setDireccion ya hace la conversión
      celulaListado.setDireccionCorta(celulaBD.getZona().getNombre() + ", " + celulaBD.getZona().getIdCiudad().getNombre());
      celulaListado.setDia("" + celulaBD.getDia());
      celulaListado.setHora("" + celulaBD.getHora());
      celulaListado.setAnfitrion(celulaBD.getAnfitrion());
      celulaListado.setFechaApertura(celulaBD.getFechaApertura());

      List<PersonaEnCelula> listaPersonaCelula = new ArrayList<PersonaEnCelula>();
      listaPersonaCelula = isaPersonaCelula.listPersonaEnCelulaPorIdCelula(idCelula).getAllPersonaEnCelulas();

      String nombreLider1 = "";
      String nombreLider2 = "";
      String nombreLider3 = "";
      String nombreLider4 = "";
      int idLider1 = 0;
      int idLider2 = 0;
      int idLider3 = 0;
      int idLider4 = 0;
      int i = 0;

      System.out.println("ServicioCelula.CELULA.id=" + idCelula + ", codigo=" + codigoCelula);
      System.out.println("ServicioCelula.recorrido de líderes de la célula");
      for (PersonaEnCelula personaCelula : listaPersonaCelula) {
        if (personaCelula.esLiderCelula()) {
          i++;
          System.out.println("ServicioCelula.i=" + i);
          int idLider = personaCelula.getIdPersona().getIdPersona();
          String nombreLider = personaCelula.getIdPersona().getNombre();
          System.out.println("ServicioCelula.Lider" + i + ".nombre=" + nombreLider);
          System.out.println("ServicioCelula.Lider" + i + ".id=" + idLider);
          if (i == 1) {
            System.out.println("lider1");
            idLider1 = idLider;
            nombreLider1 = nombreLider;
            celulaListado.setIdLider1(idLider1);
            celulaListado.setNombreLider1(nombreLider1);
          } else if (i == 2) {
            System.out.println("if.i=2");
            idLider2 = idLider;
            nombreLider2 = nombreLider;
            celulaListado.setIdLider2(idLider2);
            celulaListado.setNombreLider2(nombreLider2);
          } else if (i == 3) {
            System.out.println("if.i=3");
            idLider3 = idLider;
            nombreLider3 = nombreLider;
            celulaListado.setIdLider3(idLider3);
            celulaListado.setNombreLider3(nombreLider3);
          } else if (i == 4) {
            System.out.println("if.i=4");
            idLider4 = idLider;
            nombreLider4 = nombreLider;
            celulaListado.setIdLider4(idLider4);
            celulaListado.setNombreLider4(nombreLider4);
          }
        }
      }
      celulaListado.setNombre(celulaBD.getNombre());
      celulaListado.setNombreRed(celulaBD.getRed().getNombre());
      celulaListado.setIdRed(celulaBD.getRed().getIdRed());
      n++;
      celulaListado.setNroItem(n);
      listadoCelulas.add(celulaListado);
    }
    System.out.println("ServicioCelula.listado.tamaño" + listadoCelulas.size());
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
    int idCelula = celula.getIdCelula();
    c.setId(idCelula);
    c.setCodigo(celula.getCodigo());
    c.setNombre(celula.getNombre());
    c.setNombreRed(celula.getRed().getNombre());
    c.setIdRed(celula.getRed().getIdRed());
    c.setDireccionCorta(celula.getZona().getNombre() + ", " + celula.getZona().getIdCiudad().getNombre());
    int diaNumero = celula.getDia();
    int horaNumero = celula.getHora();
    c.setDia(Util.convertirDiaSemanaTextoCompleto("" + diaNumero));
    c.setHora(Util.convertirHoraTextoCompleto("" + horaNumero));
    c.setAnfitrion(celula.getAnfitrion());

    c.setFechaApertura(celula.getFechaApertura());
    //**
    System.out.println("ServicioCelula.fechaApertura=" + celula.getFechaApertura());

    c.setObservaciones(celula.getObservaciones());

    //data de los líderes:
    List<PersonaEnCelula> listaPersonaCelula = new ArrayList<PersonaEnCelula>();
    listaPersonaCelula = isaPersonaCelula.listPersonaEnCelulaPorIdCelula(idCelula).getAllPersonaEnCelulas();
    int i = 0;
    System.out.println("ServicioCelula.CELULA.id=" + idCelula + ", codigo=" + c.getCodigo());
    System.out.println("ServicioCelula.recorrido de líderes de la célula");
    for (PersonaEnCelula personaCelula : listaPersonaCelula) {
      if (personaCelula.esLiderCelula()) {
        i++;
        System.out.println("ServicioCelula.i=" + i);
        int idLider = personaCelula.getIdPersona().getIdPersona();
        String nombreLider = personaCelula.getIdPersona().getNombre();
        System.out.println("ServicioCelula.Lider" + i + ".nombre=" + nombreLider);
        System.out.println("ServicioCelula.Lider" + i + ".id=" + idLider);
        if (i == 1) {
          System.out.println("lider1");
          idLider1 = idLider;
          nombreLider1 = nombreLider;
          c.setIdLider1(idLider1);
          c.setNombreLider1(nombreLider1);
          c.setNumeroLideres(1);
        } else if (i == 2) {
          System.out.println("if.i=2");
          idLider2 = idLider;
          nombreLider2 = nombreLider;
          c.setIdLider2(idLider2);
          c.setNombreLider2(nombreLider2);
          c.setNumeroLideres(2);
        } else if (i == 3) {
          System.out.println("if.i=3");
          idLider3 = idLider;
          nombreLider3 = nombreLider;
          c.setIdLider3(idLider3);
          c.setNombreLider3(nombreLider3);
          c.setNumeroLideres(3);
        } else if (i == 4) {
          System.out.println("if.i=4");
          idLider4 = idLider;
          nombreLider4 = nombreLider;
          c.setIdLider4(idLider4);
          c.setNombreLider4(nombreLider4);
          c.setNumeroLideres(4);
        }
      }
    }

    //data de dirección:
    Zona zona = celula.getZona();
    Direccion dir = new Direccion();
    dir.setZona(zona.getNombre());
    /**/
    dir.setCiudad(zona.getIdCiudad().getNombre());
    dir.setEstado(zona.getIdCiudad().getIdEstado().getNombre());
    dir.setTelefono(celula.getTelefono());
    dir.setDirDetallada(celula.getDireccion());
    /**/
    c.setDireccion(dir);
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
    //traer datos de la célula
    rspCelula = isaCelula.getCelulaPorIdCelula(idCelula);
    Celula celulaBD = new Celula();
    celulaBD = rspCelula.getCelula();
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
  String nombreLider1 = "";
  String nombreLider2 = "";
  String nombreLider3 = "";
  String nombreLider4 = "";
  int idLider1 = 0;
  int idLider2 = 0;
  int idLider3 = 0;
  int idLider4 = 0;
  int nLideresUsados = 1;
}