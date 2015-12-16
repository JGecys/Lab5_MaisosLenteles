package Lab5Gecys;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Klasės objektu galima sukurti du panelius: parametrų lentelę ir mygtukų
 * tinklelį. Panelyje talpinamų objektų kiekis nustatomas parametrais.
 *
 * @author darius.matulis@ktu.lt
 */
public class Panels extends JPanel {

    private String[] lblTexts = null;
    private String[] tfTexts = null;
    private String[] btnNames = null;
    private JTextField[] tfs = null;
    private JButton[] btns = null;
    private JLabel[] lbls = null;
    private int columnWidth = 1;
    private int gridX = 1;
    private int gridY = 1;
    private SpringLayout.Constraints textFieldCons, labelCons;
    private static int spacing = 5;

    // |------------------------------|
    // |                |-----------| |
    // |   lblText[0]   | tfText[0] | |
    // |                |-----------| |
    // |                              |
    // |                |-----------| |
    // |   lblText[1]   | tfText[1] | |
    // |                |-----------| |
    // |     ...             ...      |
    // |------------------------------|
    /**
     * Sukuriama parametrų lentelė (GridBag išdėstymo dėsnis)
     *
     * @param lblTexts
     * @param tfTexts
     * @param columnWidth
     */
    public Panels(String[] lblTexts, String[] tfTexts, int columnWidth) {
        if (lblTexts == null || tfTexts == null) {
            throw new IllegalArgumentException();
        }

        this.lblTexts = lblTexts;
        this.columnWidth = columnWidth;

        if (lblTexts.length > tfTexts.length) {
            int i = tfTexts.length;
            tfTexts = Arrays.copyOf(tfTexts, lblTexts.length);
            Arrays.fill(tfTexts, i, lblTexts.length, "");
        }

        this.tfTexts = tfTexts;
        initTableOfParameters();
        // Tokia pati parametrų lentelė, padaryta naudojant SpringLayout išdėstymo dėsnį
        // initTableOfParametersWithSpringLayout();
    }

    // |-------------------------------------|
    // | |-------------| |-------------|     |
    // | | btnNames[0] | | btnNames[1] | ... |
    // | |-------------| |-------------|     |
    // |                                     |
    // | |-------------| |-------------|     |
    // | | btnNames[2] | | btnNames[3] | ... |
    // | |-------------| |-------------|     |
    // |       ...              ...          |
    // |-------------------------------------|
    /**
     * Sukuriamas mygtukų tinklelis (GridLayout išdėstymo dėsnis)
     *
     * @param btnNames
     * @param gridX
     * @param gridY
     */
    public Panels(String[] btnNames, int gridX, int gridY) {
        if (btnNames == null) {
            throw new IllegalArgumentException();
        }

        this.btnNames = btnNames;
        this.gridX = gridX;
        this.gridY = gridY;
        initGridOfButtons();
    }

    private void initTableOfParameters() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // Spacing'as tarp komponentų
        c.insets = new Insets(3, 6, 3, 4);
        // Lygiavimas į kairę
        c.anchor = GridBagConstraints.WEST;
        // Pasirenkamas pirmas stulpelis..
        c.gridx = 0;
        // ..ir į jį sudedami labeliai
        for (String lblText : lblTexts) {
            add(new JLabel(lblText), c);
        }
        // Pasirenkamas antras stulpelis..
        c.gridx = 1;
        // ..ir į jį sudedami textfieldai
        tfs = new JTextField[lblTexts.length];
        for (int i = 0; i < tfs.length; i++) {
            tfs[i] = new JTextField(tfTexts[i], columnWidth);
            tfs[i].setHorizontalAlignment(JTextField.CENTER);
            tfs[i].setBackground(Color.WHITE);
            add(tfs[i], c);
        }
    }

    // Kam įdomu - tokia pati parametrų lentelė, padaryta naudojant SpringLayout išdėstymo dėsnį
    private void initTableOfParametersWithSpringLayout() {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        Spring panelWidth = layout.getConstraint("East", this);
        lbls = new JLabel[lblTexts.length];
        tfs = new JTextField[lblTexts.length];
        for (int i = 0; i < lblTexts.length; i++) {
            lbls[i] = new JLabel(lblTexts[i]);
            add(lbls[i]);
            tfs[i] = new JTextField(tfTexts[i], columnWidth);
            tfs[i].setHorizontalAlignment(JTextField.CENTER);
            add(tfs[i]);
        }
        Spring maxLabel = Spring.constant(0);
        for (JLabel lbl : lbls) {
            SpringLayout.Constraints con = layout.getConstraints(lbl);
            maxLabel = Spring.max(maxLabel, con.getWidth());
        }
        for (int i = 0; i < lblTexts.length; i++) {
            labelCons = layout.getConstraints(lbls[i]);
            textFieldCons = layout.getConstraints(tfs[i]);
            int pad = (textFieldCons.getHeight().getValue() - labelCons.getHeight().getValue()) / 2;

            labelCons.setX(Spring.constant(spacing));
            textFieldCons.setX(Spring.sum(Spring.constant(spacing * 2), maxLabel));

            if (i == 0) {
                labelCons.setY(Spring.constant(spacing + pad));
                textFieldCons.setY(Spring.constant(spacing));
            } else {
                SpringLayout.Constraints tfConstraintsLast = layout.getConstraints(tfs[i - 1]);
                labelCons.setY(Spring.sum(Spring.constant(spacing + pad), tfConstraintsLast.getConstraint(SpringLayout.SOUTH)));
                textFieldCons.setY(Spring.sum(Spring.constant(spacing), tfConstraintsLast.getConstraint(SpringLayout.SOUTH)));
            }

            if (i != tfs.length - 1) {
                int reiksme = maxLabel.getValue();
                textFieldCons.setWidth(Spring.sum(Spring.constant(-reiksme - (3 * spacing)), panelWidth));
            }
        }
        SpringLayout.Constraints consParent = layout.getConstraints(this);
        consParent.setConstraint("East",
                Spring.sum(Spring.constant(spacing), textFieldCons.getConstraint(SpringLayout.EAST)));
        consParent.setConstraint("South",
                Spring.sum(Spring.constant(spacing), textFieldCons.getConstraint(SpringLayout.SOUTH)));
    }

    private void initGridOfButtons() {
        setLayout(new GridLayout(gridX, gridY, 3, 3));
        btns = new JButton[btnNames.length];
        for (int i = 0; i < btns.length; i++) {
            add(btns[i] = new JButton(btnNames[i]));
        }
    }

    /**
     * Gražinamas mygtukų tinklelio JButton objektų masyvas.
     *
     * @return Gražinamas mygtukų tinklelio JButton objektų masyvas
     * @throws IllegalArgumentException Jei mygtukų tinklelis nebuvo sukurtas.
     */
    public JButton[] getButtons() {
        if (btns == null) {
            throw new IllegalArgumentException("Null buttons array");
        } else {
            return btns;
        }
    }

    /**
     * Gražinamas parametrų lentelės parametrų String masyvas.
     *
     * @return Gražinamas parametrų lentelės parametrų String masyvas.
     * @throws IllegalArgumentException Jei parametrų lentelė nebuvo sukurta.
     */
    public String[] getTextsOfTable() {
        if (tfs == null) {
            throw new IllegalArgumentException("Null table of parameters");
        } else {
            for (int i = 0; i < tfs.length; i++) {
                tfTexts[i] = tfs[i].getText();
            }
        }

        return tfTexts;
    }

    /**
     * Gražinamas parametrų lentelės JTextField objektų masyvas.
     *
     * @return Gražinamas parametrų lentelės JTextField objektų masyvas.
     * @throws IllegalArgumentException Jei parametrų lentelė nebuvo sukurta.
     */
    public JTextField[] getTfOfTable() {
        if (tfs == null) {
            throw new IllegalArgumentException("Null table of parameters");
        } else {
            return tfs;
        }
    }
}
