package avuuna.player.utils;

/**
 * Interface perteneciente al patron <i>Observador-Observado</i>, en donde esta es el <i>Observado</i>.
 * @author Avuuna, la Luz del Alba
 *
 */
public interface Sujeto {

	/**
	 * Adiciona un observador.
	 * @param o
	 */
	public void addObserver(Observador o);

	/**
	 * Quita un observador.
	 * @param o
	 */
	public void removeObserver(Observador o);

	/**
	 * Notifica a todos los observadores de los ultimos cambios.
	 */
	public void notifyObservers();
}
