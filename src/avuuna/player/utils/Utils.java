package avuuna.player.utils;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import avuuna.player.exception.*;

/**
 * Clase que contiene varias funcionalidades basicas e interesantes.
 * @author Avuuna, la Luz del Alba
 * 
 */
public class Utils {

	public static final String nimbusLookAndFeel = "Nimbus";
	public static final String windowsLookAndFeel = "Windows";

	public static void display() {
		System.out.println();
	}

	public static void display(Object msg) {
		System.out.println(msg);
	}

	public static void display(String msg) {
		System.out.println(msg);
	}

	public static void display(boolean msg) {
		System.out.println(msg);
	}

	public static void display(char msg) {
		System.out.println(msg);
	}

	public static void display(char[] msg) {
		System.out.println(msg);
	}

	public static void display(double msg) {
		System.out.println(msg);
	}

	public static void display(float msg) {
		System.out.println(msg);
	}

	public static void display(int msg) {
		System.out.println(msg);
	}

	public static void display(long msg) {
		System.out.println(msg);
	}

	/**
	 * Recolecta la excepcion y la muestra en el Log.
	 * @param name Nombre relevante. Generalmente es el nombre de la clase/metodo en la que se capturo la excepcion.
	 * @param ex Excepcion capturada.
	 */
	public static void log(String name, Exception ex) {
		Logger.getLogger(name).log(Level.SEVERE, null, ex);
//		System.err.println(getStackTrace(ex));
	}

	/**
	 * Cambia la apariencia de toda la aplicacion.<br><br>
	 * Estilos bastante presentables:
	 * <li><i>Nimbus</i></li>
	 * <li><i>Windows</i></li>
	 * @param lookAndFeel Estilo a implementar.
	 * @throws LookAndFeelException Error al cambiar la apariencia.
	 */
	public static void setLookAndFeel(String lookAndFeel) throws LookAndFeelException {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if (lookAndFeel.equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			throw new LookAndFeelException(ex.getMessage());
		}
	}

	/**
	 * Convierte los milisegundos a formato <b>HH:mm:ss</b>.
	 * @param miliseconds
	 * @return Tiempo en el formato descrito antes.
	 */
	public static String formatTime(long miliseconds) {
		int seconds = (int) (miliseconds / 1000000);
		int minutes = seconds / 60;
		seconds %= 60;
		String resp = "";
		if (minutes < 10) {
			resp += "0";
		}
		resp += minutes + ":";
		if (seconds < 10) {
			resp += "0";
		}
		resp += seconds;
		return resp;
	}

	/**
	 * Obtiene el <code>stackTrace</code> de una excepcion.
	 * @param ex Excepcion capturada.
	 * @return Una cadena con el <code>stackTrace</code> de la excepcion.
	 */
	public static String getStackTrace(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
	
	/**
	 * Obtiene un numero aleatorio entre el intervalo cerrado ingresado.
	 * @param start es el extremo inferior del intervalo.
	 * @param end es el extremo superior del intervalo.
	 * @param exclude son las cantidades a excluir, dentro del calculo del numero aleatorio.
	 * @return
	 */
	public static int getRandomWithExclusion(int start, int end, int... exclude) {
		Random rnd = new Random();
		int random = start + rnd.nextInt(end - start + 1 - exclude.length);
		for (int ex : exclude) {
			if (random < ex) {
				break;
			}
			random++;
		}
		return random;
	}

}
