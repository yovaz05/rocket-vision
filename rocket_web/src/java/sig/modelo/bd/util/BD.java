/*
 * Almacén de datos simulados
 */
package sig.modelo.bd.util;

import java.util.ArrayList;
import waytech.modelo.interfaces.IsaAcceso;
import waytech.modelo.servicios.RspAcceso;
import waytech.modelo.servicios.SaAcceso;

/**
 * Simulador de datos
 * @author Gabriel Pérez
 */
public final class BD {

  public ArrayList<Red> redes;
  public ArrayList<LiderUtil> lideres;
  public ArrayList<LiderCelula> lideresCelula;
  public ArrayList<LiderCelulaListado> lideresCelulaListado;
  public ArrayList<CelulaUtil> celulas;
  public ArrayList<CelulaListadoUtil> celulasListado;
  public ArrayList<LiderUtil> discipulosLanzados;
  public ArrayList<LiderListadoUtil> discipulosLanzadosListado;
  public ArrayList<Discipulo> discipulos;
  public ArrayList<DiscipuloProceso> discipulosProceso;
  public ArrayList<DiscipuloProcesoListado> discipulosProcesoListado;
  public ArrayList<ReporteCelulaUtil> reportesCelula;
  public ArrayList<ReporteCelulaListadoUtil> reportesCelulaListado;
  public ArrayList<Usuario> usuarios;
  //variable reusable:
  ArrayList lista;

  public BD() {
    crearRedes();
    crearLideres();
    asignarLideresRed();
    crearCelulas();
    crearCelulasListado();
    crearLideresCelula();
    crearLideresCelulaListado();
    crearReportesCelulas();
    crearReportesCelulaListado();
    crearLideresLanzados();
    crearLideresLanzadosListado();
    crearDiscipulosProceso();
    crearDiscipulosProcesoListado();
    crearUsuarios();
    conteoPorRedes();
  }

  //Métodos de creación de datos:
  public void crearRedes() {
    redes = new ArrayList<Red>();

    Red red0 = new Red("");
    red0.setId(0);
    redes.add(red0);

    Red red1 = new Red("Blood and Fire", "Viernes 7:00 p.m.", "Gabriel Pérez", "Jairo Rivera");
    red1.setId(1);
    //TODO: especificar números luego de creados los elementos
    red1.setNroDiscipulosLanzados(6);
    red1.setNroDiscipulosProceso(1);
    redes.add(red1);

    Red red2 = new Red("Fuerza Vida", "Jueves 7:00 p.m.", "Juan Carlos Godoy", "Hernán Zambrano");
    red2.setId(2);
    redes.add(red2);

    Red red3 = new Red("Dayana y Fabiola", "Martes 7:00 p.m.", "Dayana", "Fabiola");
    red3.setId(3);
    redes.add(red3);
  }

  public void crearLideres() {
    lideres = new ArrayList<LiderUtil>();
    int id = 0;

    //TODO: tener registros sin datos, para pruebas y errores de ejecución
    LiderUtil lider0 = new LiderUtil("Pastor Rogelio Mora");
    lider0.setId(id++);
    lideres.add(lider0);

    LiderUtil lider1 = new LiderUtil("Pastora Irene de Mora");
    lider1.setId(id++);
    lideres.add(lider1);

    //RED 1:
    int idRed = 1;
    LiderUtil lider2 = new LiderUtil("Gabriel Pérez", "El Cercado", "0416-1041108", "gabrielperez77@gmail.com", 27);
    lider2.setCedula("16594651");
    lider2.setNombreRed(redes.get(idRed).nombre);
    lider2.setLideres(lider0.getNombre(), "");
    lider2.setReferencias(id++, idRed, lider0.getId(), 1);
    lider2.setFechaNacimiento("06/03/1984");
    lider2.setEdad(27);
    lider2.setEstadoCivil("Soltero");
    lider2.setProfesionOficio("Ingeniero en Informática");
    lider2.setDireccion(new Direccion("Lara", "Barquisimeto", "El Cercado", "Sector La Cancha, calle 2 entre carreras 2 y 3 , #3", "0251-2347980"));
    lider2.setTelefonoCelular("0416-1041108");
    lider2.setTelefonoHabitacion("0251-2347980");
    lider2.setTelefonoTrabajo("0416-1568151");
    lider2.setFacebook("gabrielperez77");
    lider2.setTwitter("gabrielperez77");
    lider2.setFechaConversion("03/01/2002");
    lider2.setFechaEncuentro("05/01/2007");
    lider2.setFechaGraduacionAcademia("09/01/2007");
    lider2.setFechaLanzamiento("02/01/2008");
    lider2.setFechaBautizo("01/01/2003");
    lider2.setLiderRed(true);
    lider2.setSupervisor(true);

    LiderUtil lider3 = new LiderUtil("Jairo Rivera", "Macuto", "0426-8566452", "abdiashalom@hotmail.com", 29);
    lider3.setCedula("12345678");
    lider3.setNombreRed(redes.get(idRed).nombre);
    lider3.setLideres(lider0.getNombre(), "");
    lider3.setReferencias(id++, idRed, lider0.getId(), 0);
    lider3.setFechaNacimiento("12/19/1982");
    lider3.setEdad(27);
    lider3.setProfesionOficio("Profesor de Biología");
    lider3.setDireccion(new Direccion("Lara", "Barquisimeto", "Macuto Arriba", "Calle 1 , casa #15", "0251-2336123"));
    lider3.setLiderRed(true);
    lider3.setSupervisor(true);

    asignarParejasMinisteriales(lider2, lider3);
    lideres.add(lider2);
    lideres.add(lider3);

    LiderUtil lider4 = new LiderUtil("Wilmer Hernández", "Ruezga Norte", "0416-1234567", "wilmerhernandez@hotmail.com", 19);
    lider4.setCedula("13456789");
    lider4.setNombreRed(redes.get(idRed).nombre);
    lider4.setLideres(lider2.nombre, lider3.nombre);
    lider4.setReferencias(id++, idRed, lider2.id, lider3.id);
    lider4.setProfesionOficio("Músico");
    lider4.setDireccion(new Direccion("Lara", "Barquisimeto", "Ruezga Norte", "Sector 2, casa #17", ""));
    lider4.setLiderCelula(true);

    LiderUtil lider5 = new LiderUtil("Leo Alvarado", "Barquisimeto Centro", "0416-1234567", "leoalvarado@hotmail.com", 19);
    lider5.setCedula("14567890");
    lider5.setNombreRed(redes.get(idRed).nombre);
    lider5.setLideres(lider2.nombre, lider3.nombre);
    lider5.setReferencias(id++, idRed, lider2.id, lider3.id);
    lider5.setDireccion(new Direccion("Lara", "Barquisimeto", "Centro", "Calle 29 esquina carrera 15, casa #29-11", ""));
    lider5.setLiderCelula(true);

    asignarParejasMinisteriales(lider4, lider5);
    lideres.add(lider4);
    lideres.add(lider5);

    LiderUtil lider6 = new LiderUtil("David Suárez", "Barquisimeto Centro", "0426-5524729", "davis_19@hotmail.com", 29);
    lider6.setCedula("16789012");
    lider6.setNombreRed(redes.get(idRed).nombre);
    lider6.setLideres(lider2.nombre, lider3.nombre);
    lider6.setReferencias(id++, idRed, lider2.id, lider3.id);
    lider6.setProfesionOficio("TSU Informática");
    lider6.setDireccion(new Direccion("Lara", "Barquisimeto", "Centro", "Carrera 28 con Av. Andrés Bello, casa #28-1", ""));
    lider6.setLiderCelula(true);

    LiderUtil lider7 = new LiderUtil("José Alvarado", "Ruezga Norte", "0426-4530251", "josealvarado@hotmail.com", 21);
    lider7.setCedula("15648790");
    lider7.setNombreRed(redes.get(idRed).nombre);
    lider7.setLideres(lider2.nombre, lider3.nombre);
    lider7.setReferencias(id++, idRed, lider2.id, lider3.id);
    lider7.setDireccion(new Direccion("Lara", "Barquisimeto", "Ruezga Norte", "Sector 4, calle 1, casa #2-3", ""));
    lider7.setLiderCelula(true);

    asignarParejasMinisteriales(lider6, lider7);
    lideres.add(lider6);
    lideres.add(lider7);

    LiderUtil lider8 = new LiderUtil("Jorge Méndez", "El Tostao", "0416-7024638", "jorgemendez@hotmail.com", 15);
    lider8.setCedula("15689735");
    lider8.setNombreRed(redes.get(idRed).nombre);
    lider8.setLideres(lider6.nombre, lider7.nombre);
    lider8.setReferencias(id++, idRed, lider6.id, lider7.id);
    lider8.setProfesionOficio("Estudiante, 4to año");
    lider8.setDireccion(new Direccion("Lara", "Barquisimeto", "El Tostao", "Sector 2, calle 3, casa #2-3", ""));
    lider8.setLiderCelula(true);
    lideres.add(lider8);

    LiderUtil lider9 = new LiderUtil("David Di Patre", "Calle 12, Barquisimeto", "0426-3306463", "daviddipatre@hotmail.com", 27);
    lider9.setCedula("14567821");
    lider9.setNombreRed(redes.get(idRed).nombre);
    lider9.setLideres(lider2.nombre, lider3.nombre);
    lider9.setReferencias(id++, idRed, lider2.id, lider3.id);
    lider9.setEstadoCivil("Casado");
    lider9.setDireccion(new Direccion("Lara", "Barquisimeto", "Centro", "Calle 12 con carreras 15 y 16, casa #12-4", ""));
    lider9.setLiderCelula(true);
    lideres.add(lider9);

    LiderUtil lider10 = new LiderUtil("Wilfredo Barrios", "El Tostao", "0416-7521851", "wilfredobarrios@hotmail.com", 19);
    lider10.setCedula("16597469");
    lider10.setNombreRed(redes.get(idRed).nombre);
    lider10.setLideres(lider6.nombre, lider7.nombre);
    lider10.setReferencias(id++, idRed, lider6.id, lider7.id);
    lider9.setDireccion(new Direccion("Lara", "Barquisimeto", "El Tostao", "", ""));
    lideres.add(lider10);

    /**/
    //RED 2:
    idRed = 2;

    LiderUtil lider11 = new LiderUtil("Juan Carlos Godoy", "Cabudare", "0416-6500969", "jcgodoy@casadeoracion.com.ve", 25);
    lider11.setCedula("17890123");
    lider11.setNombreRed(redes.get(2).nombre);
    lider11.setLideres(lider0.getNombre(), "");
    lider11.setReferencias(id++, idRed, lider0.id, 0);
    lider11.setDireccion(new Direccion("Lara", "Cabudare", "Tarabana", "", ""));
    lider11.setEstadoCivil("Casado");
    lider11.setLiderRed(true);
    lider11.setLiderCelula(true);
    lider11.setSupervisor(true);

    LiderUtil lider12 = new LiderUtil("Hernán Zambrano", "Cabudare", "0416-65456789", "hzambrano@casadeoracion.com.ve", 25);
    lider12.setCedula("18901234");
    lider12.setNombreRed(redes.get(2).nombre);
    lider12.setLideres(lider1.nombre, "");
    lider12.setReferencias(id++, idRed, 1, 0);
    lider12.setLiderRed(true);
    lider12.setSupervisor(true);
    lider12.setLiderCelula(true);

    asignarParejasMinisteriales(lider11, lider12);
    lideres.add(lider11);
    lideres.add(lider12);

    LiderUtil lider13 = new LiderUtil("Miguel Quiñones", "El Cercado", "0426-1234567", "miguelquiñones@hotmail.com", 29);
    lider13.setCedula("19012345");
    lider13.setNombreRed(redes.get(idRed).nombre);
    lider13.setLideres(lider11.nombre, lider12.nombre);
    lider13.setReferencias(id++, idRed, lider11.id, lider12.id);
    lider13.setLiderCelula(true);

    LiderUtil lider14 = new LiderUtil("Giovanny", "Calle 45, Barquisimeto", "0426-1234567", "giovanny@hotmail.com", 29);
    lider14.setCedula("20123456");
    lider14.setNombreRed(redes.get(idRed).nombre);
    lider14.setLideres(lider11.nombre, lider12.nombre);
    lider14.setReferencias(id++, idRed, lider11.id, lider12.id);
    lider14.setLiderCelula(true);

    asignarParejasMinisteriales(lider13, lider14);
    lideres.add(lider13);
    lideres.add(lider14);

    //Red 3:
    idRed = 3;

    LiderUtil lider15 = new LiderUtil("Dayana Zambrano", "La Mora", "0416-5555555", "dayanazambrano@hotmail.com", 25);
    lider15.setCedula("11234432");
    lider15.setNombreRed(redes.get(idRed).nombre);
    lider15.setLideres(lider1.nombre, "");
    lider15.setReferencias(id++, idRed, lider1.id, 0);
    lider15.setEstadoCivil("Soltera");
    lider15.setLiderRed(true);

    LiderUtil lider16 = new LiderUtil("Fabiola Mora", "La Mora", "0416-5555555", "fabiolamora@hotmail.com", 25);
    lider16.setCedula("12345654");
    lider16.setNombreRed(redes.get(idRed).nombre);
    lider16.setLideres(lider1.nombre, "");
    lider16.setReferencias(id++, idRed, lider1.id, 0);
    lider16.setEstadoCivil("Casada");
    lider16.setLiderRed(true);

    asignarParejasMinisteriales(lider15, lider16);
    lideres.add(lider15);
    lideres.add(lider16);

  }

  public void asignarLideresRed() {
    redes.get(1).setIdLideres(2, 3);
    redes.get(2).setIdLideres(11, 12);
    redes.get(3).setIdLideres(15, 16);
  }

  public void crearCelulas() {
    celulas = new ArrayList<CelulaUtil>();
    int id = 0;

    CelulaUtil celula0 = new CelulaUtil(id++);
    celulas.add(celula0);

    //Red 1
    int idRed = 1;
    int idLider1 = 9;
    int idLider2 = 1; //OJO
    int idSuperv1 = 2;
    int idSuperv2 = 3;
    CelulaUtil celula = new CelulaUtil("GJG-I-01", "Blood and Fire 1", "Jueves", "7:00 p.m.");
    celula.setId(id++);
    celula.setNombreRed(redes.get(idRed).getNombre());
    celula.setLideres(buscarLider(idLider1).nombre, "");
    celula.setDireccion(new Direccion("Lara", "Barquisimeto", "La Feria", "Calle 12 entre 16 y 17, #16-54", "0251-7714502"));
    celula.setFechaApertura("02/07/2011");
    celula.setAnfitrion("David Di Patre");
    celula.setObservaciones("Se realiza en la casa de los padres de David Di Patre");
    celula.setReferencias(idRed, idLider1, idLider2);
    celula.setDiaSemana(5);
    celulas.add(celula);

    idLider1 = 4;
    idLider2 = 5;
    idSuperv1 = 2;
    idSuperv2 = 3;
    celula = new CelulaUtil("GJG-II-01", "Jóvenes con voz de estruendo", "Martes", "7:00 p.m.");
    celula.setId(id++);
    celula.setNombreRed(redes.get(idRed).getNombre());
    celula.setDireccion(new Direccion("Lara", "Barquisimeto", "Ruezga Norte", "Sector 4, calle 2", ""));
    celula.setLideres(buscarLider(idLider1).nombre, buscarLider(idLider2).nombre);
    celula.setFechaApertura("15/02/2010");
    celula.setObservaciones("Se realiza en casa del líder Wilmer Hernández");
    celula.setReferencias(idRed, idLider1, idLider2);
    celula.setDiaSemana(3);
    celulas.add(celula);

    idLider1 = 6;
    idLider2 = 7;
    idSuperv1 = 2;
    idSuperv2 = 3;
    celula = new CelulaUtil("GJG-III-01", "Bread of Live", "Martes", "6:30 p.m.");
    celula.setId(id++);
    celula.setNombreRed(redes.get(idRed).getNombre());
    celula.setLideres(buscarLider(idLider1).nombre, buscarLider(idLider2).nombre);
    celula.setDireccion(new Direccion("Lara", "Barquisimeto", "El Cercado", "Sector La Cancha, calle 1 entre carreras 2 y 3, casa s/n", "0251-2547980"));
    celula.setFechaApertura("26/07/2010");
    celula.setAnfitrion("Adolfo Rojas");
    celula.setReferencias(idRed, idLider1, idLider2);
    celula.setDiaSemana(3);
    celulas.add(celula);

    idLider1 = 8;
    idLider2 = 1; //OJO
    idSuperv1 = 6;
    idSuperv2 = 7;
    celula = new CelulaUtil("GJG-III-02", "Oasis", "Lunes", "7:00 p.m.");
    celula.setId(id++);
    celula.setNombreRed(redes.get(idRed).getNombre());
    celula.setDireccion(new Direccion("Lara", "Barquisimeto", "El Tostao", "", ""));
    celula.setLideres(buscarLider(idLider1).nombre, "");
    celula.setDiaSemana(2);
    celula.setReferencias(idRed, idLider1, 0);
    celulas.add(celula);

    //Red 2
    /**/
    idRed = 2;
    idSuperv1 = 11;
    idSuperv2 = 12;
    idLider1 = 11;
    idLider2 = 12; //OJO


    CelulaUtil celula5 = new CelulaUtil("GJH-I-01", "Super Vida", "Viernes", "6:30 p.m.");
    celula5.setId(id++);
    celula5.setNombreRed(redes.get(idRed).getNombre());
    celula5.setLideres(lideres.get(idLider1).nombre, lideres.get(idLider2).nombre);
    celula5.setDireccion(new Direccion("Lara", "Barquisimeto", "Agua Viva", "NA", "NA"));
    celula5.setReferencias(idRed, idLider1, idLider2);
    celulas.add(celula5);

    idLider1 += 2;
    idLider2 += 2;
    CelulaUtil celula6 = new CelulaUtil("GJH-II-01", "Super Fuerza", "Martes", "6:30 p.m.");
    celula6.setId(id++);
    celula6.setNombreRed(redes.get(idRed).getNombre());
    celula6.setLideres(lideres.get(idLider1).nombre, lideres.get(idLider2).nombre);
    celula6.setDireccion(new Direccion("Lara", "Barquisimeto", "Calle 25", "NA", "NA"));
    celula6.setReferencias(idRed, idLider1, idLider2);
    celulas.add(celula6);
    /**/
  }

  public void crearCelulasListado() {
    celulasListado = new ArrayList<CelulaListadoUtil>();
    int i = 1;
    for (CelulaUtil celula : celulas) {
      if (celula.getId() != 0) {
        celulasListado.add(new CelulaListadoUtil(i++, celula));
      }
    }
  }

  public void crearLideresCelula() {
    lideresCelula = new ArrayList<LiderCelula>();
    int idCelula = 0;
    for (LiderUtil lider : lideres) {
      if ((lider.getId() == 0) || (lider.getId() == 1)) {
        continue;
      }
      LiderCelula liderCelula = new LiderCelula(lider);
      if (!lider.isLiderCelula()) {
        continue;
      }
      switch (lider.getId()) {
        case 2:
        case 3: {
          break;
        }
        case 4:
        case 5: {
          idCelula = 2;
          break;
        }
        case 6:
        case 7: {
          idCelula = 3;
          break;
        }
        case 8: {
          idCelula = 4;
          break;
        }
        case 9: {
          idCelula = 1;
          break;
        }
        case 11:
        case 12: {
          idCelula = 5;
          break;
        }
        case 13:
        case 14: {
          idCelula = 6;
          break;
        }
      }
      liderCelula.setIdCelula(idCelula);
      String celulaDesc = buscarCelula(idCelula).getDireccionCorta();
      liderCelula.setDescripcionCelula(celulaDesc);
      lideresCelula.add(liderCelula);
    }
  }

  public void crearLideresCelulaListado() {
    lideresCelulaListado = new ArrayList<LiderCelulaListado>();
    int i = 1;
    for (LiderCelula liderCelula : lideresCelula) {
      if ((liderCelula.getId() != 0) && (liderCelula.getId() != 1)) {
        lideresCelulaListado.add(new LiderCelulaListado(i++, liderCelula));
      }
    }
  }

  public void crearLideresLanzados() {
    discipulosLanzados = new ArrayList<LiderUtil>();
    for (LiderUtil lider : lideres) {
      if ((lider.getId() == 1) || (lider.getId() == 0)) {
        continue;
      }
      if (lider.isLiderRed()) {
        continue;
      }
      discipulosLanzados.add(lider);
    }
  }

  public void crearLideresLanzadosListado() {
    discipulosLanzadosListado = new ArrayList<LiderListadoUtil>();
    int i = 1;
    for (LiderUtil liderLanzado : discipulosLanzados) {
      discipulosLanzadosListado.add(new LiderListadoUtil(i++, liderLanzado));
    }
  }

  public void crearDiscipulosProceso() {
    discipulosProceso = new ArrayList<DiscipuloProceso>();
    int id = 1;
    int idRed = 1;
    int idCelula = 1;
    int idLider1 = 9;
    int idLider2 = 0;

    //Red 1:
    DiscipuloProceso dp1 = new DiscipuloProceso("Elvis Hernández", "El Jebe", "0412-1743260", "elvishernandez@hotmail.com", 28);
    dp1.setCedula("15986735");
    dp1.setNombreRed(buscarRed(1).getNombre());
    dp1.setLideres(buscarLider(9).getNombre(), "");
    dp1.setDescripcionCelula(buscarCelula(1).getNombre());
    dp1.setReferencias(id++, idRed, idLider1, idLider2);
    dp1.setIdCelula(idCelula);
    dp1.setEstatusProceso("Encuentro");
    dp1.setEstacaCelula(true);
    discipulosProceso.add(dp1);

    DiscipuloProceso dp2 = new DiscipuloProceso("Richard Montero", "Ruezga Norte", "0426-8080675", "richardmontero@hotmail.com", 32);
    dp2.setCedula("13456789");
    dp2.setNombreRed(buscarRed(1).getNombre());
    dp2.setLideres(buscarLider(9).getNombre(), "");
    dp2.setDescripcionCelula(buscarCelula(1).getNombre());
    dp2.setReferencias(id++, idRed, idLider1, idLider2);
    dp2.setIdCelula(idCelula);
    dp2.setEstatusProceso("Sin Encuentro");
    discipulosProceso.add(dp2);

    //Red 2:
    idRed = 2;
    idCelula = 5;
    idLider1 = 11;
    idLider2 = 12;

    DiscipuloProceso dp3 = new DiscipuloProceso("Pablo Pérez", "Barquisimeto", "0426-5555555", "pabloperez@hotmail.com", 20);
    dp3.setCedula("156894652");
    dp3.setNombreRed(buscarRed(idRed).getNombre());
    dp3.setLideres(buscarLider(11).getNombre(), buscarLider(12).getNombre());
    dp3.setDescripcionCelula(buscarCelula(5).getNombre());
    dp3.setReferencias(id++, idRed, idLider1, idLider2);
    dp3.setIdCelula(idCelula);
    dp3.setEstatusProceso("Sin Encuentro");
    discipulosProceso.add(dp3);
  }

  public void crearDiscipulosProcesoListado() {
    discipulosProcesoListado = new ArrayList<DiscipuloProcesoListado>();
    int i = 1;
    for (DiscipuloProceso disc : discipulosProceso) {
      discipulosProcesoListado.add(new DiscipuloProcesoListado(i++, disc));
    }
  }

  public void crearReportesCelulas() {
    reportesCelula = new ArrayList<ReporteCelulaUtil>();
    int id = 0;
    int idRed = 1;

    //Datos de Resumen:
    CelulaUtil celula1 = buscarCelula(1);
    //-System.out.println("BD: celula1: " + celula1.toString());
    ReporteCelulaUtil reporte1 = new ReporteCelulaUtil(celula1);
    reporte1.setEstatus(ReporteCelulaUtil.REPORTE_INGRESADO);
    reporte1.setDescripcionEstatus(ReporteCelulaUtil.TOOLTIPTEXT_INGRESADO);

    CelulaUtil celula2 = buscarCelula(2);
    ReporteCelulaUtil reporte2 = new ReporteCelulaUtil(celula2);
    reporte2.setEstatus(ReporteCelulaUtil.REPORTE_NO_INGRESADO);
    reporte2.setDescripcionEstatus(ReporteCelulaUtil.TOOLTIPTEXT_NO_INGRESADO);

    CelulaUtil celula3 = buscarCelula(3);
    ReporteCelulaUtil reporte3 = new ReporteCelulaUtil(celula3);
    reporte3.setEstatus(ReporteCelulaUtil.REPORTE_INGRESADO);
    reporte3.setDescripcionEstatus(ReporteCelulaUtil.TOOLTIPTEXT_INGRESADO);

    CelulaUtil celula4 = buscarCelula(4);
    ReporteCelulaUtil reporte4 = new ReporteCelulaUtil(celula4);
    reporte4.setEstatus(ReporteCelulaUtil.CELULA_NO_REALIZADA);
    reporte4.setDescripcionEstatus(ReporteCelulaUtil.STATUS_NO_REALIZADA);
    reporte4.setObservaciones("Aquí se explicará el porqué no se realizó la célula");

    CelulaUtil celula5 = buscarCelula(5);
    ReporteCelulaUtil reporte5 = new ReporteCelulaUtil(celula5);
    reporte5.setEstatus(ReporteCelulaUtil.REPORTE_INGRESADO);
    reporte5.setDescripcionEstatus(ReporteCelulaUtil.TOOLTIPTEXT_INGRESADO);

    CelulaUtil celula6 = buscarCelula(6);
    ReporteCelulaUtil reporte6 = new ReporteCelulaUtil(celula6);
    reporte6.setEstatus(ReporteCelulaUtil.REPORTE_NO_INGRESADO);
    reporte6.setDescripcionEstatus(ReporteCelulaUtil.TOOLTIPTEXT_NO_INGRESADO);


    //Números de los Reportes:

    reporte1.setPlanificacion(2, 2, 0, 2);
    reporte1.setResultados(1, 1, 0, 2, 5, 0, 3, 4);
    reporte1.setOfrendas(50.00, true);

    //reporte2 no está ingresado
    reporte2.setPlanificacion(0, 0, 0, 0);
    reporte2.setResultados(0, 0, 0, 0, 0, 0, 0, 0);
    reporte2.setOfrendas(0.00, false);

    reporte3.setPlanificacion(3, 1, 0, 3);
    reporte3.setResultados(2, 1, 1, 2, 4, 0, 3, 3);
    reporte3.setOfrendas(75.00, false);

    reporte4.setPlanificacion(0, 0, 0, 0);
    reporte4.setResultados(0, 0, 0, 0, 0, 0, 0, 0);
    reporte4.setOfrendas(0.00, false);

    reporte5.setPlanificacion(3, 2, 1, 2);
    reporte5.setResultados(2, 3, 2, 4, 5, 2, 0, 3);
    reporte5.setOfrendas(60.00, false);

    reporte6.setPlanificacion(0, 0, 0, 0);
    reporte6.setResultados(0, 0, 0, 0, 0, 0, 0, 0);
    reporte6.setOfrendas(0.00, false);

    reportesCelula.add(reporte1);
    reportesCelula.add(reporte2);
    reportesCelula.add(reporte3);
    reportesCelula.add(reporte4);
    reportesCelula.add(reporte5);
    reportesCelula.add(reporte6);
  }

  public void crearReportesCelulaListado() {
    reportesCelulaListado = new ArrayList<ReporteCelulaListadoUtil>();
    int i = 1;
    for (ReporteCelulaUtil reporte : reportesCelula) {
      reportesCelulaListado.add(new ReporteCelulaListadoUtil(i, reporte));
      i++;
    }
  }

  public void crearUsuarios() {
    usuarios = new ArrayList<Usuario>();
    for (int i = 1; i < lideres.size(); i++) {
      LiderUtil lider = buscarLider(i);
      Usuario u = new Usuario(lider);
      int id = u.getId();
      //TODO: arreglar esto:
      if (id == 2) {
        u.setLogin("gabrielperez");
        u.setPassword("123");
        u.setTipo(Usuario.LIDER_RED);
      } else if (id == 3) {
        u.setLogin("jairorivera");
        u.setPassword("123");
        u.setTipo(Usuario.LIDER_RED);
      } else if (id == 6) {
        u.setLogin("davidsuarez");
        u.setPassword("123");
        u.setTipo(Usuario.LIDER_CELULA);
      } else if (id == 11) {
        u.setLogin("jcgodoy");
        u.setPassword("123");
        u.setTipo(Usuario.LIDER_RED);
      } else if (id == 12) {
        u.setLogin("hernanzambrano");
        u.setPassword("123");
        u.setTipo(Usuario.LIDER_RED);
      } //no usado
      else if (id == 15) {
        u.setLogin("dayanazambrano");
        u.setPassword("123");
        u.setTipo(Usuario.ADMIN_CELULAS);
      } else {
        continue;
      }
      usuarios.add(u);
    }
  }

  //métodos de búsqueda:
  /**
   * búsqueda de red, por id
   * @param id
   * @return 
   */
  public Red buscarRed(int id) {
    if (id == 0) {
      return null;//error
    }
    for (Red r : redes) {
      if (r.getId() == id) {
        return r;
      }
    }
    return null;//error
  }

  /**
   * busca líder, sin importar la red
   * @param id
   * @return 
   */
  public LiderUtil buscarLider(int id) {
    if (id == 0) {
      return null;//error
    }
    for (LiderUtil lider : lideres) {
      if (lider.getId() == id) {
        return lider;
      }
    }
    return null;//error
  }

  /**
   * busca líder indicando la red
   * @param id
   * @return 
   */
  public LiderUtil buscarLiderPorRed(int idRed, int id) {
    if (id == 0) {
      return null;//error
    }
    if (idRed == 0) {
      return null;//error
    }
    for (LiderUtil lider : lideres) {
      if ((lider.getIdRed() == idRed) && (lider.getId() == id)) {
        return lider;
      }
    }
    return null;//error
  }

  public LiderUtil buscarLiderLanzado(int id) {
    if (id == 0) {
      return null;//error
    }
    for (LiderUtil lider : discipulosLanzados) {
      if (lider.getId() == id) {
        return lider;
      }
    }
    return null;//error
  }

  public CelulaUtil buscarCelula(int id) {
    if (id == 0) {
      return null;//error
    }
    for (CelulaUtil celula : celulas) {
      if (celula.getId() == id) {
        return celula;
      }
    }
    return null;//error
  }

  public LiderCelula buscarLiderCelula(int id) {
    if (id == 0) {
      return null;//error
    }
    for (LiderCelula liderCelula : lideresCelula) {
      if (liderCelula.getId() == id) {
        return liderCelula;
      }
    }
    return null;//error
  }

  public CelulaListadoUtil buscarCelulaListado(int id) {
    if (id == 0) {
      return null;//error
    }
    for (CelulaListadoUtil celulaListado : celulasListado) {
      if (celulaListado.getId() == id) {
        return celulaListado;
      }
    }
    return null;//error
  }

  public LiderCelulaListado buscarLiderCelulaListado(int id) {
    if (id == 0) {
      return null;//error
    }
    for (LiderCelulaListado liderCelulaListado : lideresCelulaListado) {
      if (liderCelulaListado.getId() == id) {
        return liderCelulaListado;
      }
    }
    return null;//error
  }

  public DiscipuloProceso buscarDiscipuloProceso(int id) {
    if (id == 0) {
      return null;//error
    }
    for (DiscipuloProceso discipulo : discipulosProceso) {
      if (discipulo.getId() == id) {
        return discipulo;
      }
    }
    return null;//error
  }

  public ReporteCelulaUtil buscarReporteCelula(int id) {
    if (id == 0) {
      return new ReporteCelulaUtil();
    }
    for (ReporteCelulaUtil r : reportesCelula) {
      if (r.getIdReporte() == id) {
        return r;
      }
    }
    return null;//error
  }

  public ReporteCelulaListadoUtil buscarReporteCelulaListado(int id) {
    if (id == 0) {
      return null;//error
    }
    for (ReporteCelulaListadoUtil r : reportesCelulaListado) {
      if (r.getId() == id) {
        return r;
      }
    }
    return null;//error
  }

  public ArrayList<LiderUtil> getLideres() {
    return lideres;
  }

  public ArrayList<LiderUtil> getLideresLanzados() {
    return discipulosLanzados;
  }

  public ArrayList<LiderCelulaListado> getLideresCelulaListado() {
    return lideresCelulaListado;
  }

  public ArrayList<LiderListadoUtil> getLideresLanzadosListado() {
    return discipulosLanzadosListado;
  }

  public ArrayList<LiderCelula> getLideresCelula() {
    return lideresCelula;
  }

  public ArrayList<CelulaUtil> getCelulas() {
    return celulas;
  }

  public ArrayList<CelulaListadoUtil> getCelulasListado() {
    return celulasListado;
  }

  public ArrayList<Red> getRedes() {
    return redes;
  }

  public ArrayList<Discipulo> getDiscipulos() {
    return discipulos;
  }

  public ArrayList<DiscipuloProceso> getDiscipulosProceso() {
    return discipulosProceso;
  }

  public ArrayList<DiscipuloProcesoListado> getDiscipulosProcesoListado() {
    return discipulosProcesoListado;
  }

  public ArrayList<LiderUtil> getDiscipulosLanzados() {
    return discipulosLanzados;
  }

  public ArrayList<LiderListadoUtil> getDiscipulosLanzadosListado() {
    return discipulosLanzadosListado;
  }

  public ArrayList<ReporteCelulaUtil> getReportesCelulas() {
    return reportesCelula;
  }

  public ArrayList<ReporteCelulaListadoUtil> getReportesCelulasListado() {
    return reportesCelulaListado;
  }

  public ArrayList<Usuario> getUsuarios() {
    return usuarios;
  }

  /**
   * busca usuario por su login
   * @param login el nombre de usuario
   * @return el objeto Usuario
   */
  public Usuario buscarUsuario(String login) {
    for (Usuario u : usuarios) {
      if (u.getLogin().equals(login)) {
        return u;
      }
    }
    return null;//error
  }

  /**
   * busca usuario por su id
   * @param id el id del usuario
   * @return el objeto Usuario
   */
  public Usuario buscarUsuario(int id) {
    if (id == 0) {
      return null;//error
    }
    for (Usuario u : usuarios) {
      if (u.getId() == id) {
        return u;
      }
    }
    return null;//error
  }

  /**
   * busca usuario por su login y pasword
   * @param login el nombre de usuario
   * @param password la contraseña
   * @return el objeto Usuario
   */
  public Usuario buscarUsuario(String login, String password) {
    IsaAcceso isaAcceso = new SaAcceso();
    RspAcceso rspAcceso = new RspAcceso();
    rspAcceso = isaAcceso.esLoginPasswordValido(login, password);
    if (rspAcceso.esLoginPasswordValido()) {
      Usuario u = new Usuario();
      u.setAnfitrionCelula(rspAcceso.getAcceso().getIdPersona().esAnfitrion());
      u.setCedula(rspAcceso.getAcceso().getIdPersona().getCi());
//            u.setDireccion(null);
//            u.setDireccionCorta(password);
//            u.setEdad(edad);
//            u.setEmail(login);
//            u.setEstacaCelula(true);
//            u.setEstadoCivil(login);
//            u.setFacebook(password);
//            u.setFechaBautizo(password);
//            u.setFechaConversion(login);
//            u.setFechaEncuentro(password);
//            u.setFechaGraduacionAcademia(password);
//            u.setFechaLanzamiento(login);
//            u.setFechaNacimiento(login);
//            u.setId(id);
//            u.setIdLider1(idLider1);
//            u.setIdLider2(idLider2);
//            u.setIdParejaMinisterial(idParejaMinisterial);
//            u.setIdRed(idRed);
//            u.setLiderCelula(true);
//            u.setLideres(login, login);
//            u.setLogin(login);
//            u.setMaestroAcademia();
      u.setNombre(rspAcceso.getAcceso().getIdPersona().getNombre());
      u.setId(rspAcceso.getAcceso().getIdPersona().getIdPersona());
//            u.setNombreLider1(login);
//            u.setNombreLider2(login);
//            u.setNombreParejaMinisterial(password);
//            u.set
      return u;
    } else {
      return null;//error
    }
  }

  public ArrayList getCelulasPorRed(int idRed) {
    lista = new ArrayList<CelulaListadoUtil>();
    for (CelulaListadoUtil celulaListado : celulasListado) {
      if ((celulaListado.getIdRed() == idRed)) {
        lista.add(celulaListado);
      }
    }
    return lista;
  }

  public ArrayList getLideresCelulaPorRed(int idRed) {
    lista = new ArrayList<LiderCelulaListado>();
    for (LiderCelulaListado item : lideresCelulaListado) {
      if ((item.getIdRed() == idRed)) {
        lista.add(item);
      }
    }
    return lista;
  }

  public ArrayList getReportesCelulaPorRed(int idRed) {
    lista = new ArrayList<ReporteCelulaListadoUtil>();
    for (ReporteCelulaListadoUtil item : reportesCelulaListado) {
      if ((item.getIdRed() == idRed)) {
        lista.add(item);
      }
    }
    return lista;
  }

  public ArrayList getDiscipulosLanzadosPorRed(int idRed) {
    lista = new ArrayList<LiderListadoUtil>();
    for (LiderListadoUtil item : discipulosLanzadosListado) {
      if ((item.getIdRed() == idRed)) {
        lista.add(item);
      }
    }
    return lista;
  }

  public ArrayList getDiscipulosProcesoPorRed(int idRed) {
    lista = new ArrayList<DiscipuloProcesoListado>();
    for (DiscipuloProcesoListado item : discipulosProcesoListado) {
      if ((item.getIdRed() == idRed)) {
        lista.add(item);
      }
    }
    return lista;
  }

  private void conteoPorRedes() {
    int idRed;
    for (Red red : redes) {
      idRed = red.getId();
      red.setNroCelulas(getCelulasPorRed(idRed).size());
      red.setNroLideresCelulas(getLideresCelulaPorRed(idRed).size());
      red.setNroDiscipulosLanzados(getDiscipulosLanzadosPorRed(idRed).size());
      red.setNroDiscipulosProceso(getDiscipulosProcesoPorRed(idRed).size());
    }
  }

  private void asignarParejasMinisteriales(LiderUtil lider1, LiderUtil lider2) {
    lider1.setIdParejaMinisterial(lider2.getId());
    lider2.setIdParejaMinisterial(lider1.getId());
    lider1.setNombreParejaMinisterial(lider2.getNombre());
    lider2.setNombreParejaMinisterial(lider1.getNombre());
  }
}
