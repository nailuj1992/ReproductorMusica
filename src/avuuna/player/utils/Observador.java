package avuuna.player.utils;

/**
 * Interface perteneciente al patron <i>Observador-Observado</i>, en donde esta incluye a todos los <i>Observadores</i>.
 * @author Avuuna, la Luz del Alba
 * 
 */
public interface Observador {

	/**
	 * Al ver un cambio que realizo el <code>Sujeto</code>, hace lo que tenga que hacer como observador.
	 */
    public void update();
}
