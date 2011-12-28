package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.PersonaInsert;
import waytech.modelo.beans.sgi.PersonaUpdate;
import waytech.modelo.servicios.RspPersona;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaPersona {

    public abstract RspPersona insertPersona(PersonaInsert usuario);
    
    /**
     * Tipo de persona LIDER LANZADO ID = 3
     * ID ZONA = 1 = GENERICO
     * CREA COMO LIDER LANZADO
     * CREA UNA RELACION EN LA TABLA persona_en_red
     * @param cedula
     * @param nombre
     * @param idRed
     * @return RspPersona
     */
    public abstract RspPersona insertPersonaLiderBasico(String cedula, String nombre, int idRed);

    public abstract RspPersona updatePersona(PersonaUpdate usuario);
    
    public abstract RspPersona updateCedula(int idPersona, String Cedula);
    
    public abstract RspPersona updateNombre(int idPersona, String nombre);
    
    /**
     * Solo para el modelo donde una persona solo pertenece a una sola red.
     * @param idPersona
     * @param idRed
     * @return 
     */
    public abstract RspPersona updateRed(int idPersona, int idRed);
    
    public abstract RspPersona updateTelefonoMovil(int idPersona, String telefonoMovil);
    
    public abstract RspPersona updateCorreo(int idPersona, String correo);
    
    public abstract RspPersona updateZona(int idPersona, int idZona);
    
    public abstract RspPersona updateDireccion(int idPersona, String direccion);
    
    public abstract RspPersona updateObservaciones(int idPersona, String observaciones);

    /**
     * Elimina un registro lógicamente
     * @param idPersona
     * @return 
     */
    public abstract RspPersona deletePersona(int idPersona);

    public abstract RspPersona getPersonaPorId(int idPersona);

    public abstract RspPersona getPersonaPorTraza(String traza);

    public abstract RspPersona getLiderLanzadoPorIdPersona(int idPersona);
    
    public abstract RspPersona getPersonaPorCedula(String cedula);
    
    public abstract RspPersona esCedulaExistente(String cedula);
    
    public abstract RspPersona esCodigoSecretoExistente(String cedula, String codigoSecreto);
    
    public abstract RspPersona esTelefonoMovilExistente(String telefonoMovil);
    
    public abstract RspPersona esTelefonoHabitacionExistente(String telefonoHabitacion);
    
    public abstract RspPersona esTelefonoTrabajoExistente(String telefonoTrabajo);

    public abstract RspPersona renovarPersona(int idPersona);

    public abstract RspPersona listPersona();

    public abstract RspPersona listPersonaPorLimite(int limiteDeRegistros);

    public abstract RspPersona listPersonaFiltradoPorNombre(String nombre);

    public abstract RspPersona listPersonaFiltradoPorApellido(String apellido);

    public abstract RspPersona listPersonaFiltradoPorCedula(String cedula);

    public abstract RspPersona listPersonaFiltradoPorDireccionHabitacion(String direccionHabitacion);

    public abstract RspPersona listPersonaFiltradoPorDireccionTrabajo(String direccionTrabajo);

    public abstract RspPersona listPersonaLideresDeRed();

    public abstract RspPersona listPersonaLideresDeCelula();

    public abstract RspPersona listPersonaSupervisores();

    public abstract RspPersona listPersonaEstaca();

    public abstract RspPersona listPersonaMaestrosDeAcademia();

    public abstract RspPersona listPersonaAnfitriones();

    public abstract RspPersona listPersonaLideresLanzados();

    public abstract RspPersona listPersonaLiderSupervizor();

    public abstract RspPersona listPersonaDiscipulosEnProceso();
    
    public abstract RspPersona listPersonaCualquierLider();
    
    public abstract RspPersona listPersonaLiderLanzado();
    
    public abstract RspPersona listPersonaCualquierLiderNombre(String nombre);
    
    /*
     * Gabriel dice: métodos que se necesitan para Maestro de Líder:
     * 1. insertPersona(String cedula, String nombre, int idRed);
     * 2. updateCedula(String valor);
     * 3. updateNombre(String valor);
     * 4. updateRed(int idZona);
     * 5. updateTelefonoCelular(String valor);
     * 6. updateCorreo(String valor);
     * 7. updateCorreo(String valor);
     * 8. updateZona(int idZona);
     * 9. updateDireccionHabitacion(String valor);
     * 10. updateObservaciones(String valor);
     */
}
