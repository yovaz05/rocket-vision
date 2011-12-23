/**
 * bean Lider,
 * representa un discípulo lanzado,
 * es la base para un líder de cualquier tipo: LiderRed, LiderCelula, DiscipuloLanzado, SupervisorCelula
 */
package cdo.sgd.modelo.bd.util;

/**
 *
 * @author Gabriel Pérez
 */
public class Discipulo {

  //datos deresumen:
  protected int cedula = 0;
  protected String nombre = "";
  protected String nombreRed = "";
  protected String direccionCorta = "";
  protected String nombreLider1 = "";
  protected String nombreLider2 = "";
  //puede elegir cualquiera de sus teléfonos como el principal
  protected String telefonoPrincipal = "";
  protected String email = "";
  protected int edad = 0;
  protected Direccion direccion = new Direccion("", "", "", "", "");
  ;
  //datos personales:
  protected String fechaNacimiento = "";
  protected String estadoCivil = "Soltero"; //soltero por defecto
  protected String profesionOficio = "";
  //TODO: agregar: Direccion completa
  //fechas:
  //estas fechas son guardadas en el formato 'mm/dd/yyyy'
  //pero serán visualizadas en el formato 'mes/año': 'Mmm/yyyy'
  protected String fechaConversion = "";
  protected String fechaBautizo = "";
  protected String fechaEncuentro = "";
  protected String fechaGraduacionAcademia = "";
  protected String fechaLanzamiento = "";
  //contacto:
  protected String telefonoCelular = "";
  protected String telefonoHabitacion = "";
  protected String telefonoTrabajo = "";
  protected String facebook = "";
  protected String twitter = "";
  //roles:
  //duda: aclarar esto!
  protected boolean estacaCelula = false;
  protected boolean anfitrionCelula = false;
  //observaciones:
  protected String observaciones = "";
  //datos a nivel de sistema, los id's se usan en navegación dinámica:
  protected int id;
  protected int idLider1;
  protected int idLider2;
  protected int idRed;

  Discipulo() {
  }

  Discipulo(int id) {
    this.id = id;
  }

  Discipulo(String nombre) {
    this.nombre = nombre;
  }

  /**
   * @param nombre
   * @param nombreRed
   * @param DireccionCorta
   * @param nombreLider1
   * @param nombreLider2
   * @param telefono
   * @param email 
   * @param edad
   */
  public Discipulo(String nombre, String nombreRed, String direccionCorta, String nombreLider1, String nombreLider2, String telefono, String email, int edad) {
    this.nombre = nombre;
    this.nombreRed = nombreRed;
    this.direccionCorta = direccionCorta;
    this.nombreLider1 = nombreLider1;
    this.nombreLider2 = nombreLider2;
    this.telefonoPrincipal = telefono;
    this.email = email;
    this.edad = edad;
  }

  /**
   * 
   * @param nombre
   * @param direccionCorta
   * @param nombreLider1
   * @param nombreLider2
   * @param telefono
   * @param email
   * @param edad 
   */
  public Discipulo(String nombre, String direccionCorta, String nombreLider1, String nombreLider2, String telefono, String email, int edad) {
    this.nombre = nombre;
    this.direccionCorta = direccionCorta;
    this.nombreLider1 = nombreLider1;
    this.nombreLider1 = nombreLider2;
    this.telefonoPrincipal = telefono;
    this.email = email;
    this.edad = edad;
  }

  public Discipulo(String nombre, String direccionCorta, String telefono, String email, int edad) {
    this.nombre = nombre;
    this.direccionCorta = direccionCorta;
    this.telefonoPrincipal = telefono;
    this.email = email;
    this.edad = edad;
  }

  public Discipulo(String nombre, String telefono, String email, int edad) {
    this.nombre = nombre;
    this.telefonoPrincipal = telefono;
    this.email = email;
    this.edad = edad;
  }

  /**
   * configurar los IDs de referencias
   */
  void setReferencias(int id, int idRed, int idLider1, int idLider2) {
    this.id = id;
    this.idRed = idRed;
    this.idLider1 = idLider1;
    this.idLider2 = idLider2;
  }

  public void setLideres(String nombreLider1, String nombreLider2) {
    this.nombreLider1 = nombreLider1;
    this.nombreLider2 = nombreLider2;
  }

  public int getCedula() {
    return cedula;
  }

  public void setCedula(int cedula) {
    this.cedula = cedula;
  }

  public String getDireccionCorta() {
    return direccionCorta;
  }

  public void setDireccionCorta(String direccionCorta) {
    this.direccionCorta = direccionCorta;
  }

  public void updateDireccionCorta() {
    this.direccionCorta = direccion.zona + " - " + direccion.ciudad;
  }

  public int getEdad() {
    return edad;
  }

  public void setEdad(int edad) {
    this.edad = edad;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getIdLider1() {
    return idLider1;
  }

  public void setIdLider1(int idLider1) {
    this.idLider1 = idLider1;
    //updateNombreLider1();
  }

  //TODO: evaluar si se debe quitar este método
  public void updateNombreLider1() {
//    System.out.println("idLider1: " + idLider1);
    if (idLider1 == 1) {
      this.nombreLider1 = "Pastor Rogelio Mora";
      return;
    } else if (idLider1 == 0) {
      this.nombreLider1 = "Pastora Irene de Mora";
    } else {
      //String nombreLider = BD.buscarLider(idLider1).getNombre();
      //this.nombreLider1 = nombreLider;
//      System.out.println("nombreLider1: " + nombreLider);
    }
  }

  public int getIdLider2() {
    return idLider2;
  }

  public void setIdLider2(int idLider2) {
    this.idLider2 = idLider2;
    //updateNombreLider2();
  }

  public void updateNombreLider2() {
    //String nombre = SimuladorDatos.buscarLider(idLider2).getNombre();
    this.nombreLider2 = (idLider2 == 0) ? "" : nombreLider2;
//    System.out.println("nombreLider2: " + nombre);
  }

  public int getIdRed() {
    return idRed;
  }

  public void setIdRed(int idRed) {
    this.idRed = idRed;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombreLider1() {
    return nombreLider1;
  }

  public void setNombreLider1(String nombreLider1) {
    this.nombreLider1 = nombreLider1;
  }

  public String getNombreLider2() {
    return nombreLider2;
  }

  public void setNombreLider2(String nombreLider2) {
    this.nombreLider2 = nombreLider2;
  }

  public String getNombreRed() {
    return nombreRed;
  }

  public void setNombreRed(String nombreRed) {
    this.nombreRed = nombreRed;
  }

  public String getTelefono() {
    return telefonoPrincipal;
  }

  public void setTelefono(String telefono) {
    this.telefonoPrincipal = telefono;
  }


  public boolean isAnfitrionCelula() {
    return anfitrionCelula;
  }

  public void setAnfitrionCelula(boolean anfitrionCelula) {
    this.anfitrionCelula = anfitrionCelula;
  }

  public boolean isEstacaCelula() {
    return estacaCelula;
  }

  public void setEstacaCelula(boolean estacaCelula) {
    this.estacaCelula = estacaCelula;
  }

  public String getEstadoCivil() {
    return estadoCivil;
  }

  public void setEstadoCivil(String estadoCivil) {
    this.estadoCivil = estadoCivil;
  }

  public String getFacebook() {
    return facebook;
  }

  public void setFacebook(String facebook) {
    this.facebook = facebook;
  }

  public String getFechaGraduacionAcademia() {
    return fechaGraduacionAcademia;
  }

  public void setFechaGraduacionAcademia(String fechaAcademiaGraduacion) {
    this.fechaGraduacionAcademia = fechaAcademiaGraduacion;
  }

  public String getFechaBautizo() {
    return fechaBautizo;
  }

  public void setFechaBautizo(String fechaBautizo) {
    this.fechaBautizo = fechaBautizo;
  }

  public String getFechaConversion() {
    return fechaConversion;
  }

  public void setFechaConversion(String fechaConversion) {
    this.fechaConversion = fechaConversion;
  }

  public String getFechaEncuentro() {
    return fechaEncuentro;
  }

  public void setFechaEncuentro(String fechaEncuentro) {
    this.fechaEncuentro = fechaEncuentro;
  }

  public String getFechaLanzamiento() {
    return fechaLanzamiento;
  }

  public void setFechaLanzamiento(String fechaLanzamiento) {
    this.fechaLanzamiento = fechaLanzamiento;
  }

  public String getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(String fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getTelefonoCelular() {
    return telefonoCelular;
  }

  public void setTelefonoCelular(String telefonoCelular) {
    this.telefonoCelular = telefonoCelular;
  }

  public String getTelefonoHabitacion() {
    return telefonoHabitacion;
  }

  public void setTelefonoHabitacion(String telefonoHabitacion) {
    this.telefonoHabitacion = telefonoHabitacion;
  }

  public String getTelefonoPrincipal() {
    return telefonoPrincipal;
  }

  public void setTelefonoPrincipal(String telefonoPrincipal) {
    this.telefonoPrincipal = telefonoPrincipal;
  }

  public String getTelefonoTrabajo() {
    return telefonoTrabajo;
  }

  public void setTelefonoTrabajo(String telefonoTrabajo) {
    this.telefonoTrabajo = telefonoTrabajo;
  }

  public String getTwitter() {
    return twitter;
  }

  public void setTwitter(String twitter) {
    this.twitter = twitter;
  }

  public Direccion getDireccion() {
    return direccion;
  }

  public void setDireccion(Direccion direccion) {
    this.direccion = direccion;
    updateDireccionCorta();
  }

  public String getProfesionOficio() {
    return profesionOficio;
  }

  public void setProfesionOficio(String profesionOficio) {
    this.profesionOficio = profesionOficio;
  }
  
  
}
