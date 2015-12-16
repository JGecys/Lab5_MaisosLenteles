package Lab5Gecys;

import javax.swing.*;
import java.awt.*;

/**
 * Klasė, skirta duomenų išvedimui į GUI
 */
public class KsSwing {

    private static int lineNr;
    private static boolean formatStartOfLine = true;

    private static String getStartOfLine() {
        return (formatStartOfLine) ? ++lineNr + "| " : "";
    }

    public static void setFormatStartOfLine(boolean bool) {
        formatStartOfLine = bool;
    }

    public static void oun(JTextArea ta, Object o) {
        if (o instanceof Iterable) {
            for (Object iter : (Iterable) o) {
                ta.append(getStartOfLine() + iter.toString() + System.lineSeparator());
            }
        } else {
            ta.append(getStartOfLine() + o.toString() + System.lineSeparator());
        }
    }

    public static void ou(JTextArea ta, Object o, String msg) {
        ta.append(getStartOfLine() + msg + ": ");
        boolean bool = formatStartOfLine;
        formatStartOfLine = false;
        oun(ta, o);
        formatStartOfLine = bool;
    }

    public static void oun(JTextArea ta, Object o, String msg) {
        ta.setBackground(Color.white);
        ta.append(getStartOfLine() + msg + ": " + System.lineSeparator());
        boolean bool = formatStartOfLine;
        formatStartOfLine = false;
        oun(ta, o);
        formatStartOfLine = bool;
    }

    public static void ounerr(JTextArea ta, Exception e) {
        ta.setBackground(Color.pink);
        ta.append(getStartOfLine() + e.toString() + System.lineSeparator());
        for (StackTraceElement ste : e.getStackTrace()) {
            ta.append(getStartOfLine() + ste.toString() + System.lineSeparator());
        }
        ta.append(System.lineSeparator());
    }

    public static void ounerr(JTextArea ta, String msg) {
        ta.setBackground(Color.pink);
        ta.append(getStartOfLine() + msg + ". " + System.lineSeparator());
    }

    public static void ounerr(JTextArea ta, String msg, String parameter) {
        ta.setBackground(Color.pink);
        ta.append(getStartOfLine() + parameter + ": " + msg + System.lineSeparator());
    }

    /**
     * Nuosava situacija, panaudota dialogo struktūrose įvedamų parametrų
     * tikrinimui.
     */
    public static class MyException extends Exception {

        // Situacijos kodas. Pagal jį programuojama programos reakcija į situaciją
        private int code;

        public MyException(String message) {
            // (-1) - susitariama, kad tai neutralus kodas.
            this(message, -1);
        }

        public MyException(String message, int code) {
            super(message);

            if (code < -1) {
                throw new IllegalArgumentException("Illegal code in MyException: " + code);
            }

            this.code = code;
        }

        public MyException(String message, Throwable th, int code) {
            super(message, th);

            if (code < -1) {
                throw new IllegalArgumentException("Illegal code in MyException: " + code);
            }

            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
