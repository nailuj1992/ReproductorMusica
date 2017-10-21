package avuuna.player.utils;

import java.io.*;
import java.util.logging.*;

import javax.swing.*;

import avuuna.player.exception.*;

/**
 *
 * @author Avuuna, la Luz del Alba
 * 
 */
public class Utils {

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

	public static void log(String name, Exception ex) {
		Logger.getLogger(name).log(Level.SEVERE, null, ex);
//		System.err.println(getStackTrace(ex));
	}

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

	public static String getStackTrace(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
}
