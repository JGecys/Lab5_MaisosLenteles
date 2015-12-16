package Lab5Gecys;

import studijosKTU.Ks;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Apleto klasė
 *
 */
public class Applet extends JApplet {

    @Override
    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Lab5Panel lab5Panel = new Lab5Panel();
            lab5Panel.initComponents();
            SwingUtilities.invokeAndWait(() -> {
                add(lab5Panel);
            });
            this.setSize(800, 600);
            this.repaint();
        } catch (InterruptedException | InvocationTargetException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Ks.ou(ex.getMessage());
        }
    }
}
