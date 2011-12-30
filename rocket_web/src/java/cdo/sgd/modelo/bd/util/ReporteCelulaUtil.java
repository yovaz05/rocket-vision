package cdo.sgd.modelo.bd.util;

import waytech.utilidades.UtilFechas;


/**
 *
 * @author Gabriel
 */
public class ReporteCelulaUtil extends CelulaUtil {
  //id del reporte
  int idReporte = 0;
  //datos de resumen:
  int idCelula = 0;
  String descripcionCelula = "";
  int estatus = 0;
  String descripcionEstatus = "";
  //estatus de reporte:
  public static final int REPORTE_INGRESADO = 4;//INGRESADO, PERO NO CONFIRMADO
  //public static final int CONFIRMADO = 1;;
  public static final int REPORTE_NO_INGRESADO = 2;
  public static final int CELULA_NO_REALIZADA = 3;
  public static final String TOOLTIPTEXT_INGRESADO = "Reporte ingresado. Haga click para ver detalles";
  //public static final String STATUS_CONFIRMADO = "Confirmado";
  public static final String TOOLTIPTEXT_NO_INGRESADO = "Reporte no ingresado. Click para ver opciones";
  public static final String STATUS_NO_REALIZADA = "Célula NO Realizada";
  public static final String IMAGEN_INGRESADO = "/img/iconos/status_ok_16.gif";
  public static final String IMAGEN_NO_INGRESADO = "/img/iconos/status_not_16.png";
  public static final String IMAGEN_NO_REALIZADA = "/img/iconos/status_alert_16.gif";
  //public static final String IMAGE  N_CONFIRMADO = "/img/iconos/status_ok.gif";
  public static final String ESTILO_NO_INGRESADO = "background: url(/sig2/img/iconos/status_not_16.jpg) no-repeat; width:16px; height:16px";
  public static final String ACCION_VER_DETALLES = "Ver detalles";
  public static final String ACCION_INGRESAR = "Ingresar";
  public static final String ACCION_COMUNICAR_LIDERES = "Escribir a líderes";

  //TODO: usar esto:
  //public static final String STATUS_REVISADO = "Reporte Revisado";

  //datos completos:
  //planificación
  int planificacionInvitados = 0;
  int planificacionReconciliados = 0;
  int planificacionVisitas = 0;
  int personasEnPlanificacion = 0;
  //resultados
  int resultadoInvitados = 0;
  int resultadoConvertidos = 0;
  int resultadoReconciliados = 0;
  int resultadoAmigosNoAsistenIglesia = 0;
  int resultadoCDO = 0;
  int resultadoVisitas = 0;
  int resultadoOtrasIglesias = 0;
  int resultadoAsistenciaDomingoAnterior = 0;
  int totalAsistenciaCelula = 0;
  //Ofrendas
  double ofrendasMonto = 0.00;
  boolean ofrendasEntregadas = false;

  public ReporteCelulaUtil() {
    planificacionInvitados = 0;
    planificacionReconciliados = 0;
    planificacionVisitas = 0;
    personasEnPlanificacion = 0;
    resultadoInvitados = 0;
    resultadoConvertidos = 0;
    resultadoReconciliados = 0;
    resultadoAmigosNoAsistenIglesia = 0;
    resultadoCDO = 0;
    resultadoVisitas = 0;
    resultadoOtrasIglesias = 0;
    resultadoAsistenciaDomingoAnterior = 0;
    totalAsistenciaCelula = 0;
    ofrendasMonto = 0.00;
    ofrendasEntregadas = false;    
  }

  public ReporteCelulaUtil(int idCelula) {
    super(idCelula);
  }

  public ReporteCelulaUtil(CelulaUtil celula) {
    this.idCelula = celula.id;
    this.codigo = celula.codigo;
    this.descripcionCelula = celula.direccionCorta;
    this.dia = celula.dia;
    this.direccionCorta = celula.direccionCorta;
    this.nombreLider1 = celula.nombreLider1;
    this.nombreLider2 = celula.nombreLider2;
    this.nombre = celula.nombre;
    this.nombreRed = celula.nombreRed;
    this.setReferencias(celula.idRed, celula.idLider1, celula.idLider2);
    //this.calcularIdReporte();
    this.idReporte = celula.id;
    this.setPlanificacion(0, 0, 0, 0);
    this.setResultados(0, 0, 0, 0, 0, 0, 0, 0);
    this.setOfrendas(0.00, false);
    this.setEstatus(ReporteCelulaUtil.REPORTE_NO_INGRESADO);
  }

  @Override
  public String toString() {
    return "ReporteCelula{"
            + "idReporte=" + idReporte + ",idCelula=" + idCelula + ", descripcionCelula=" + descripcionCelula
            + ", codigo=" + codigo + ", direccionCorta=" + direccionCorta + ", lider1=" + nombreLider1 + ", lider2=" + nombreLider2
            + ", diaHora=" + dia + ", nombre=" + nombre
            + ", estatus=" + estatus + ", descripcionEstatus=" + descripcionEstatus + '}';
  }

  public String toString(int modo) {
    String result = "";
    if (modo == 1) {//resumen - cascada desde clase ReporteCelulaListado
      result = "idReporte=" + idReporte + ", idCelula =" + idCelula + ", descripcionCelula=" + descripcionCelula
              + ", codigo=" + codigo + ", direccionCorta=" + direccionCorta
              + ", lider1=" + nombreLider1 + ", lider2=" + nombreLider2
              + ", diaHora=" + dia + ", nombre=" + nombre + ", estatus="
              + estatus + ", descripcionEstatus=" + descripcionEstatus;
    } else if (modo == 2) {//mostrar los números del reporte
      result = "idReporte=" + idReporte + ", idCelula =" + idCelula + ", descripcionCelula=" + descripcionCelula
              + ", codigo=" + codigo + ", direccionCorta=" + direccionCorta
              + ", lider1=" + nombreLider1 + ", lider2=" + nombreLider2
              + ", diaHora=" + dia + ", nombre=" + nombre + ", estatus="
              + estatus + ", descripcionEstatus=" + descripcionEstatus
              + ", planificacionInvitados=" + planificacionInvitados
              + ", planificacionReconciliados=" + planificacionReconciliados
              + ", planificacionVisitas=" + planificacionVisitas
              + ", personasEnPlanificacion=" + personasEnPlanificacion
              + ", resultadoInvitados=" + resultadoInvitados
              + ", resultadoConvertidos=" + resultadoConvertidos
              + ", resultadoReconciliados=" + resultadoReconciliados
              + ", resultadoAmigosNoAsistenIglesia=" + resultadoAmigosNoAsistenIglesia
              + ", resultadoCDO=" + resultadoCDO
              + ", resultadoVisitas=" + resultadoVisitas
              + ", resultadoOtrasIglesias=" + resultadoOtrasIglesias
              + ", resultadoAsistenciaDomingoAnterior=" + resultadoAsistenciaDomingoAnterior
              + ", totalAsistenciaCelula=" + totalAsistenciaCelula
              + ", ofrendasMonto=" + ofrendasMonto
              + ", ofrendasCheckEntregadas=" + ofrendasEntregadas + '}';
    }
    return result;
  }

  public final void setPlanificacion(int invitados, int reconciliados, int visitas, int personasEnPlanificacion) {
    this.planificacionInvitados = invitados;
    this.planificacionReconciliados = reconciliados;
    this.planificacionVisitas = visitas;
    this.personasEnPlanificacion = personasEnPlanificacion;
  }

  public final void setResultados(int invitados, int convertidos, int reconciliados,
          int amigosNoAsistenIglesia, int CDO, int otrasIglesias,
          int visitas, int asistenciaDomingoAnterior) {
    this.resultadoInvitados = invitados;
    this.resultadoConvertidos = convertidos;
    this.resultadoAmigosNoAsistenIglesia = amigosNoAsistenIglesia;
    this.resultadoReconciliados = reconciliados;
    this.resultadoCDO = CDO;
    this.resultadoVisitas = visitas;
    this.resultadoOtrasIglesias = otrasIglesias;
    this.resultadoAsistenciaDomingoAnterior = asistenciaDomingoAnterior;
    calcularTotalAsistenciaCelula();
  }

  public final void setOfrendas(double ofrendasMonto, boolean ofrendasEntregadas) {
    this.ofrendasMonto = ofrendasMonto;
    this.ofrendasEntregadas = ofrendasEntregadas;
  }

  private void calcularIdReporte() {
    setIdReporte(UtilFechas.calcularIdReporteCelula(idCelula));
  }

  public String getDescripcionCelula() {
    return descripcionCelula;
  }

  public void setDescripcionCelula(String descripcionCelula) {
    this.descripcionCelula = descripcionCelula;
  }

  public int getIdCelula() {
    return idCelula;
  }

  public void setIdCelula(int idCelula) {
    this.idCelula = idCelula;
  }

  public int getIdReporte() {
    return idReporte;
  }

  public void setIdReporte(int idReporte) {
    this.idReporte = idReporte;
  }

  //TODO: pasar para tabla celula.estatus
  public int getEstatus() {
    return estatus;
  }

  public void setEstatus(int estatus) {
    this.estatus = estatus;
//    TODO: llamar a método setDescripcionEstatus
  }

  public String getDescripcionEstatus() {
    return descripcionEstatus;
  }

  public void setDescripcionEstatus(String descripcionEstatus) {
    this.descripcionEstatus = descripcionEstatus;
  }

  public double getOfrendasMonto() {
    return ofrendasMonto;
  }

  public void setOfrendasMonto(double ofrendasMonto) {
    this.ofrendasMonto = ofrendasMonto;
  }

  public int getPlanificacionInvitados() {
    return planificacionInvitados;
  }

  public void setPlanificacionInvitados(int planificacionInvitados) {
    this.planificacionInvitados = planificacionInvitados;
  }

  public int getPlanificacionPersonasEnplanificacion() {
    return personasEnPlanificacion;
  }

  public void setPlanificacionPersonasEnplanificacion(int planificacionPersonasEnplanificacion) {
    this.personasEnPlanificacion = planificacionPersonasEnplanificacion;
  }

  public int getPlanificacionReconciliados() {
    return planificacionReconciliados;
  }

  public void setPlanificacionReconciliados(int planificacionReconciliados) {
    this.planificacionReconciliados = planificacionReconciliados;
  }

  public int getPlanificacionVisitas() {
    return planificacionVisitas;
  }

  public void setPlanificacionVisitas(int planificacionVisitas) {
    this.planificacionVisitas = planificacionVisitas;
  }

  public int getResultadoAmigosNoAsistenIglesia() {
    return resultadoAmigosNoAsistenIglesia;
  }

  public void setResultadoAmigosSoloAsistenCelula(int resultadoAmigosNoAsistenIglesia) {
    this.resultadoAmigosNoAsistenIglesia = resultadoAmigosNoAsistenIglesia;
  }

  public int getResultadoAsistenciaDomingoAnterior() {
    return resultadoAsistenciaDomingoAnterior;
  }

  public void setResultadoAsistenciaDomingoAnterior(int resultadoAsistenciaDomingoAnterior) {
    this.resultadoAsistenciaDomingoAnterior = resultadoAsistenciaDomingoAnterior;
  }

  public int getResultadoCDO() {
    return resultadoCDO;
  }

  public void setResultadoCDO(int resultadoCDO) {
    this.resultadoCDO = resultadoCDO;
  }

  public int getResultadoConvertidos() {
    return resultadoConvertidos;
  }

  public void setResultadoConvertidos(int resultadoConvertidos) {
    this.resultadoConvertidos = resultadoConvertidos;
  }

  public int getResultadoInvitados() {
    return resultadoInvitados;
  }

  public void setResultadoInvitados(int resultadoInvitados) {
    this.resultadoInvitados = resultadoInvitados;
  }

  public int getResultadoOtrasIglesias() {
    return resultadoOtrasIglesias;
  }

  public void setResultadoOtrasIglesias(int resultadoOtrasIglesias) {
    this.resultadoOtrasIglesias = resultadoOtrasIglesias;
  }

  public int getResultadoReconciliados() {
    return resultadoReconciliados;
  }

  public void setResultadoReconciliados(int resultadoReconciliados) {
    this.resultadoReconciliados = resultadoReconciliados;
  }

  public int getTotalAsistenciaCelula() {
    return totalAsistenciaCelula;
  }

  public void setTotalAsistenciaCelula(int totalAsistenciaCelula) {
    this.totalAsistenciaCelula = totalAsistenciaCelula;
  }

  public void calcularTotalAsistenciaCelula() {
    this.totalAsistenciaCelula = this.resultadoInvitados + this.resultadoAmigosNoAsistenIglesia + this.resultadoCDO + this.resultadoOtrasIglesias;
  }

  public int getResultadoVisitas() {
    return resultadoVisitas;
  }

  public void setResultadoVisitas(int resultadoVisitas) {
    this.resultadoVisitas = resultadoVisitas;
  }

  public boolean isOfrendasEntregadas() {
    return ofrendasEntregadas;
  }

  public void setOfrendasEntregadas(boolean ofrendasEntregadas) {
    this.ofrendasEntregadas = ofrendasEntregadas;
  }

  public int getPersonasEnPlanificacion() {
    return personasEnPlanificacion;
  }

  public void setPersonasEnPlanificacion(int personasEnPlanificacion) {
    this.personasEnPlanificacion = personasEnPlanificacion;
  }
}
