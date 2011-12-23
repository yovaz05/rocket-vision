package waytech.modelo.interfaces;

import waytech.modelo.beans.sgi.PersonaEnCelula;
import waytech.modelo.beans.sgi.PersonaEnCelulaInsert;
import waytech.modelo.beans.sgi.PersonaEnCelulaUpdate;
import waytech.modelo.servicios.RspPersonaEnCelula;

/**
 * @since Lunes  21/11/2011 06:49 AM.
 * @version 1.0 Lunes  21/11/2011 06:49 AM.
 * @author Gerardo José Montilla Virgüez, Way Technologies Consulting Group C.A.
 * @see http://www.waytech.com.ve
 * Clase creada para el software SIG 
 */
public interface IsaPersonaEnCelula {

  public abstract RspPersonaEnCelula insertPersonaEnCelula(PersonaEnCelulaInsert personaEnCelula);

  public abstract RspPersonaEnCelula updatePersonaEnCelula(PersonaEnCelulaUpdate personaEnCelula);

  /**
   * Elimina un registro lógicamente
   * @param idPersonaEnCelula
   * @return 
   */
  public abstract RspPersonaEnCelula deletePersonaEnCelula(int idCelula, int idPersonaEnCelula);

  public abstract RspPersonaEnCelula getPersonaEnCelulaPorIdPersonaEnCelula(int idPersonaEnCelula);

  public abstract RspPersonaEnCelula getPersonaEnCelulaPorTraza(String traza);

  public abstract RspPersonaEnCelula listPersonaEnCelula();

  public abstract RspPersonaEnCelula listPersonaEnCelulaPorIdCelula(int idCelula);
}
