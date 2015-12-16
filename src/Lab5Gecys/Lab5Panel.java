package Lab5Gecys;

import Gui.KsSwing.MyException;
import Lab2Gecys.Telefonas;
import Lab2Gecys.TelefonuGeneratorius;
import studijosKTU.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

/**
 * Lab5 panelis
 *
 * @author darius.matulis@ktu.lt
 */
public class Lab5Panel extends JPanel implements ActionListener {

    private static final ResourceBundle rb = ResourceBundle.getBundle(Resources.class.getCanonicalName());
    private static final int TF_WIDTH = 6;
    private static final int NUMBER_OF_BUTTONS = 4;

    private HashType ht = HashType.DIVISION;
    private JLabel label1, label2, label3;
    private final JComboBox cmbCollisionTypes = new JComboBox();
    private final JComboBox cmbHashFunctions = new JComboBox();
    private final JTextArea taInput = new JTextArea();
    private final ExtendedTable table = new ExtendedTable();
    private final JScrollPane scrollTable = new JScrollPane(table);
    private final JPanel panParam123 = new JPanel();
    private final JScrollPane scrollParam123 = new JScrollPane(panParam123);
    private final JPanel panParam123Events = new JPanel();
    private final JTextArea taEvents = new JTextArea();
    private final JScrollPane scrollEvents = new JScrollPane(taEvents);
    private Panels panParam1, panParam2, panParam3, panParam4, panButtons;
    private final JPanel panLeft = new JPanel();
    private final JScrollPane scrollLeft = new JScrollPane(panLeft);
    private List<Telefonas> telArray;
    private MapADTx<String, Telefonas> map;
    private int sizeOfSet, sizeOfGenSet, colWidth, initialCapacity;
    private float loadFactor;
    private String delimiter = "";
    private final TelefonuGeneratorius gamyba = new TelefonuGeneratorius();

    //                          Lab5Panel (BorderLayout)
//  |--------West--------|---------------------Center-----------------------|
//  | |~~~~~~~~~~~~~~~~| | |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| |
//  | |   scrollLeft   | | |                                              | |
//  | |                | | |                                              | |
//  | |                | | |                 scrollTable                  | |
//  | |                | | |                                              | |
//  | |                | | |                                              | |
//  | |                | | |                                              | |
//  | |                | | |                                              | | 
//  | |                | | |                                              | | 
//  | | |------------| | | |                                              | |
//  | | | panButtons | | | |                                              | |
//  | | |            | | | |                                              | |
//  | | |------------| | | |                                              | | 
//  | |~~~~~~~~~~~~~~~~| | |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| | 
//  |-------------------------------South-----------------------------------|
//  | |~~~~~~~~~~~~~~~~~~~~~~~~~panParam123Events~~~~~~~~~~~~~~~~~~~~~~~~~| |
//  | | |----------scrollParam123-----------| |------scrollEvents-------| | |
//  | | | |~~~~~~~~~||~~~~~~~~~||~~~~~~~~~| | |                         | | | 
//  | | | |panParam1||panParam2||panParam4| | |                         | | |
//  | | | |         ||         ||         | | |                         | | |
//  | | | |~~~~~~~~~||~~~~~~~~~||~~~~~~~~~| | |                         | | |
//  | | |-----------------------------------| |-------------------------| | |
//  | |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| |
//  |-----------------------------------------------------------------------|   
    public Lab5Panel() {
    }

    public void initComponents() {
        //======================================================================
        // Formuojamas rožinės spalvos panelis (kairėje pusėje)
        //======================================================================
        label1 = new JLabel(rb.getStringArray("lblNames")[0]);
        label2 = new JLabel(rb.getStringArray("lblNames")[1]);
        label3 = new JLabel(rb.getStringArray("lblNames")[2]);
        // Užpildomi JComboBox'ai
        for (String s : rb.getStringArray("cmbCollisionTypes")) {
            cmbCollisionTypes.addItem(s);
        }
        cmbCollisionTypes.addActionListener(this);
        for (String s : rb.getStringArray("cmbHashFunctions")) {
            cmbHashFunctions.addItem(s);
        }
        cmbHashFunctions.addActionListener(this);

        // Formuojamas mygtukų tinklelis (mėlynas). Naudojama klasė Panels.
        panButtons = new Panels(rb.getStringArray("btnLabels"), NUMBER_OF_BUTTONS, 0);
        for (JButton btn : panButtons.getButtons()) {
            btn.addActionListener(this);
        }
        enableButtons(false);

        // Viskas sudedama į vieną (rožinės spalvos) panelį
        panLeft.setLayout(new BoxLayout(panLeft, BoxLayout.Y_AXIS));
        for (JComponent comp : new JComponent[]{label1, cmbCollisionTypes, label2,
                cmbHashFunctions, label3, taInput, panButtons}) {
            comp.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            panLeft.add(Box.createRigidArea(new Dimension(0, 2)));
            panLeft.add(comp);
        }
        //======================================================================
        // Formuojama pirmoji parametrų lentelė (šviesiai žalia). Naudojama klasė Panels.
        //======================================================================        
        panParam1 = new Panels(rb.getStringArray("lblParams1"),
                rb.getStringArray("tfParams1"), TF_WIDTH); //5 - tf plotis
        // ..kai kurie prametrai plačiau paaiskinami tooltips'uose.. 
        panParam1.getTfOfTable()[2].setToolTipText(rb.getStringArray("toolTips")[0]);
        panParam1.getTfOfTable()[5].setToolTipText(rb.getStringArray("toolTips")[1]);

        // .. tikrinami ivedami parametrai dėl teisingumo. Negali būti neigiami.
        for (int i : new int[]{0, 1, 4}) {
            panParam1.getTfOfTable()[i].setInputVerifier(new MyInputVerifier());
        }

        // Tikrinamas įvedamas celės plotis. turi buti >0
        panParam1.getTfOfTable()[3].setInputVerifier(new InputVerifier() {
                                                         @Override
                                                         public boolean verify(JComponent input) {
                                                             String text = ((JTextField) input).getText();
                                                             try {
                                                                 if (Integer.valueOf(text) <= 0) {
                                                                     input.setBackground(Color.RED);
                                                                     return false;
                                                                 }

                                                                 input.setBackground(Color.WHITE);
                                                                 return true;
                                                             } catch (NumberFormatException e) {
                                                                 input.setBackground(Color.RED);
                                                                 return false;
                                                             }
                                                         }
                                                     }
        );

        // Tikrinamas įvedamas apkrovimo faktorius. Turi būti (0;1] ribose
        panParam1.getTfOfTable()[5].setInputVerifier(new InputVerifier() {

                                                         @Override
                                                         public boolean verify(JComponent input
                                                         ) {
                                                             String text = ((JTextField) input).getText().trim();
                                                             try {
                                                                 Float loadFactor = Float.valueOf(text);

                                                                 if (loadFactor <= 0.0 || loadFactor > 1.0) {
                                                                     input.setBackground(Color.RED);
                                                                     return false;
                                                                 }

                                                                 input.setBackground(Color.WHITE);
                                                                 return true;
                                                             } catch (NumberFormatException e) {
                                                                 input.setBackground(Color.RED);
                                                                 return false;
                                                             }
                                                         }
                                                     }
        );
        //======================================================================
        // Formuojama antroji parametrų lentelė (gelsva). Naudojame klasę Panels.
        //======================================================================        
        panParam2 = new Panels(rb.getStringArray("lblParams2"),
                rb.getStringArray("tfParams2"), TF_WIDTH);

        panParam3 = new Panels(rb.getStringArray("btnNames3"), rb.getStringArray("btnNames3").length, 1);
        for (JButton b : panParam3.getButtons()) {
            b.addActionListener(this::manoButtonPressed);
        }
        //======================================================================
        // Formuojama trečioji parametrų lentelė (šviesiai žalia). Naudojame klasę Panels.
        //======================================================================              
        panParam4 = new Panels(rb.getStringArray("lblParams3"),
                rb.getStringArray("tfParams3"), TF_WIDTH);
        //======================================================================
        // Visų trijų parametrų lentelių paneliai sudedami į šviesiai pilką panelį
        //======================================================================
        panParam123.add(panParam1);
        panParam123.add(panParam2);
        panParam123.add(panParam3);
        panParam123.add(panParam4);
        //======================================================================
        // Toliau suformuojamas panelis iš šviesiai pilko panelio ir programos
        // įvykių JTextArea
        //======================================================================
        GroupLayout gl = new GroupLayout(panParam123Events);
        panParam123Events.setLayout(gl);
        gl.setHorizontalGroup(
                gl.createSequentialGroup().
                        addComponent(scrollParam123,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                Short.MAX_VALUE).
                        addComponent(scrollEvents,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                Short.MAX_VALUE));
        gl.setVerticalGroup(
                gl.createSequentialGroup().
                        addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER).
                                addComponent(scrollParam123).
                                addComponent(scrollEvents)));

        // Kad prijungiant tekstą prie JTextArea vaizdas visada nušoktų į apačią
        scrollEvents.getVerticalScrollBar()
                .addAdjustmentListener((AdjustmentEvent e) -> {
                            taEvents.select(taEvents.getCaretPosition()
                                    * taEvents.getFont().getSize(), 0);
                        }
                );
        appearance();
        //======================================================================
        // Suformuojamas bendras Lab5 panelis
        //======================================================================
        setLayout(new BorderLayout());
        add(scrollLeft, BorderLayout.WEST);
        add(scrollTable, BorderLayout.CENTER);
        add(panParam123Events, BorderLayout.SOUTH);
    }

    private void manoButtonPressed(ActionEvent event) {
        switch (Arrays.asList(panParam3.getButtons()).indexOf(event.getSource())) {
            case 0: //remove by key
                Telefonas r = map.remove(panParam4.getTfOfTable()[0].getText());
                if (r != null) {
                    KsSwing.oun(taEvents, panParam4.getTfOfTable()[0].getText() + " -> " + r.toString(), "Pašalinta");
                } else {
                    KsSwing.ounerr(taEvents, "Toks raktas neegzistuoja");
                }
                panParam4.getTfOfTable()[0].setText("");
                updateParameters(false);
                break;
            case 1: //containsValue
                try {
                    Telefonas tel = Telefonas.FromString(panParam4.getTfOfTable()[1].getText());
                    boolean contains = map.containsValue(tel);
                    if (contains) {
                        KsSwing.oun(taEvents, tel, "Rasta");
                    } else {
                        KsSwing.ounerr(taEvents, "Elementas neegzistuoja");
                    }
                } catch (IllegalStateException | NoSuchElementException e) {
                    KsSwing.ounerr(taEvents, "Blogi duomenys");
                }

                break;

        }

        table.repaint();
    }

    /**
     * Kosmetika
     */
    private void appearance() {
        int counter = 0;
        // Objektų rėmeliai
        for (JComponent comp : new JComponent[]{panLeft, scrollTable,
                panParam123, scrollEvents}) {
            TitledBorder tb = new TitledBorder(rb.getStringArray("lblBorders")[counter++]);
            tb.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
            comp.setBorder(tb);
        }
        scrollTable.getViewport().setBackground(Color.white);
        panParam1.setBackground(new Color(204, 255, 204));// Šviesiai žalia
        panParam2.setBackground(new Color(255, 255, 153));// Gelsva
        panParam4.setBackground(new Color(204, 255, 204));// Šviesiai žalia
        panLeft.setBackground(Color.pink);
        panButtons.setBackground(new Color(112, 162, 255));// Blyškiai mėlyna
        panParam123.setBackground(Color.lightGray);
        cmbCollisionTypes.setEditable(false);
        cmbHashFunctions.setEditable(false);
        taInput.setBackground(Color.white);

        // Antra parametrų lentelė (gelsva) bus neredaguojama
        for (JTextField comp : panParam2.getTfOfTable()) {
            comp.setEditable(false);
        }
        for (JTextArea ta : new JTextArea[]{taInput, taEvents}) {
            ta.setBackground(Color.white);
            ta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        }
        taEvents.setEditable(false);
        scrollEvents.setPreferredSize(new Dimension(350, 0));

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            System.gc();
            System.gc();
            System.gc();
            taEvents.setBackground(Color.white);
            Object source = ae.getSource();

            if (source.equals(panButtons.getButtons()[0])) {
                mapGeneration(null);
            }

            if (source.equals(panButtons.getButtons()[1])) {
                mapAdd();
            }

            if (source.equals(panButtons.getButtons()[2])) {
                mapEfficiency();
            }

            if (source.equals(panButtons.getButtons()[3])) {
                mapEfficiency2();
            }

            if (source.equals(cmbCollisionTypes) || source.equals(cmbHashFunctions)) {
                enableButtons(false);
            }
        } catch (MyException e) {
            if ((e.getCode() >= 0) && (e.getCode() < panParam1.getTextsOfTable().length)) {
                panParam1.getTfOfTable()[e.getCode()].setBackground(Color.red);
            }

            // Specialus atvejis kai parametrais sugeneruotos aibės imtis nustatoma didesnė negu sugeneruota aibė
            if (e.getCode() == 100) {
                panParam1.getTfOfTable()[0].setBackground(Color.red);
                panParam1.getTfOfTable()[1].setBackground(Color.red);
            }
            taEvents.setBackground(Color.pink);
            KsSwing.oun(taEvents, e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            KsSwing.ounerr(taEvents, rb.getStringArray("msgs")[8]);
            e.printStackTrace(System.out);
        } catch (IllegalArgumentException e) {
            KsSwing.ounerr(taEvents, rb.getStringArray("msgs")[8]);
            e.printStackTrace(System.out);
        } catch (NullPointerException e) {
            KsSwing.ounerr(taEvents, rb.getStringArray("msgs")[8]);
            e.printStackTrace(System.out);
        } catch (UnsupportedOperationException e) {
            KsSwing.ounerr(taEvents, e.getMessage());
        } catch (Exception e) {
            KsSwing.ounerr(taEvents, rb.getStringArray("msgs")[8]);
            e.printStackTrace(System.out);
        }
    }

    private void mapEfficiency2() {
        KsSwing.oun(taEvents, "", rb.getStringArray("msgs")[2]);
        SwingUtilities.invokeLater(() -> {
            GreitaveikosTyrimas5 gt = new GreitaveikosTyrimas5(taEvents, panButtons.getButtons());
            gt.start();
        });
    }

    public void mapGeneration(String fName) throws MyException {
        enableButtons(false);
        // Duomenų nuskaitymas iš lentelių
        readParameters();
        switch (cmbCollisionTypes.getSelectedIndex()) {
            case 0:
                map = new MapKTUx<>(new String(), new Telefonas(), initialCapacity, loadFactor, ht);
                break;
            // ...
            // Programuojant kitus kolizijų sprendimo metodus reikia papildyti switch sakinį
            case 1:
                map = new MapAtviraMaisa<>(new String(), new Telefonas(), initialCapacity, loadFactor, ht);
                break;
            default:
                enableButtons(false);
                throw new MyException(rb.getStringArray("msgs")[0]);
        }
        // Išvedami rezultatai
        // Jei automobiliai generuojami
        if (fName == null) {
            telArray = gamyba.generuotiSarasa(sizeOfSet);
            for (Telefonas a : telArray) {
                map.put(gamyba.formuotiID() //raktas
                        , a);
                KsSwing.oun(taEvents, a, rb.getStringArray("msgs")[1]);
            }
            enableButtons(true);
        } else { // Jei automobiliai skaitomi iš failo
            map.load(fName);
            enableButtons(false);
            if (map.isEmpty()) {
                KsSwing.ounerr(taEvents, rb.getStringArray("msgs")[9], fName);
            } else {
                KsSwing.ou(taEvents, rb.getStringArray("msgs")[6], fName);

            }
        }

        table.setModel(map.getModel(delimiter), colWidth);
        updateParameters(false);
    }

    public void mapAdd() throws MyException {
        Telefonas a = gamyba.generuotiViena();
        map.put(
                gamyba.formuotiID() // Raktas
                , a);
        table.setModel(map.getModel(delimiter), colWidth);
        updateParameters(true);
        KsSwing.oun(taEvents, a, rb.getStringArray("msgs")[1]);
    }

    public void mapEfficiency() {
        KsSwing.oun(taEvents, "", rb.getStringArray("msgs")[2]);
        SwingUtilities.invokeLater(() -> {
            GreitaveikosTyrimas gt = new GreitaveikosTyrimas(taEvents, panButtons.getButtons());
            gt.start();
        });
    }

    private void enableButtons(boolean enable) {
        for (int i : new int[]{1}) {
            panButtons.getButtons()[i].setEnabled(enable);
        }
    }

    private void readParameters() throws MyException {
        int i = 0;

        JTextField[] parameters = panParam1.getTfOfTable();

        sizeOfSet = verifyParameter(parameters, i, Integer.class);
        sizeOfGenSet = verifyParameter(parameters, ++i, Integer.class);
        delimiter = parameters[++i].getText();
        colWidth = verifyParameter(parameters, ++i, Integer.class);
        initialCapacity = verifyParameter(parameters, ++i, Integer.class);
        loadFactor = verifyParameter(parameters, ++i, Float.class);

        switch (cmbHashFunctions.getSelectedIndex()) {
            case 0:
                ht = HashType.DIVISION;
                break;
            case 1:
                ht = HashType.MULTIPLICATION;
                break;
            case 2:
                ht = HashType.JCF7;
                break;
            case 3:
                ht = HashType.JCF8;
                break;
            default:
                ht = HashType.DIVISION;
                break;
        }
    }

    /**
     * Tikrina ar parametras parametrų lentelėje turi leistinas reikšmes.
     * Naudojamas textfieldams užsetintas InputVerifier klasės objektas
     *
     * @param <T>   tikrina Integer ar Float klasės parametrus
     * @param tfs
     * @param code
     * @param clasz
     * @return Integer arba Float klasės objektas. Gražinamas tipas priklauso
     * nuo clasz parametro
     * @throws Gui.KsSwing.MyException jei tikrinimas praėjo nesėkmingai
     */
    private <T> T verifyParameter(JTextField[] tfs, int code, Class<T> clasz) throws MyException {
        if (!clasz.equals(Integer.class) && !clasz.equals(Float.class)) {
            throw new IllegalArgumentException();
        }

        T result = null;
        JTextField tf = tfs[code];

        if (tf.getInputVerifier().verify(tf)) {
            if (clasz.equals(Integer.class)) {
                result = (T) Integer.valueOf(tf.getText());
            } else if (clasz.equals(Float.class)) {
                result = (T) Float.valueOf(tf.getText());
            }
        } else {
            throw new MyException(rb.getStringArray("errMsgs1")[code] + ": " + tf.getText(), code);
        }

        return result;
    }

    /**
     * Tikrina ar pasikeite parametro reikšmė nuo praeito karto. Jei pasikeite -
     * spalvina jo reikšmę raudonai
     *
     * @param colorize ar spalvinti parametrų reikšmes raudonai
     */
    private void updateParameters(boolean colorize) {
        String[] parameters = new String[]{
                String.valueOf(map.size()),
                String.valueOf(map.getTableCapacity()),
                String.valueOf(map.getMaxChainSize()),
                String.valueOf(map.getRehashesCounter()),
                String.valueOf(map.getLastUpdatedChain()),
                // Užimtų maišos lentelės elementų skaičius %
                String.format(map.getChainsCounter() < 0 ? "-1" : "%3.2f", (double) map.getChainsCounter() / map.getTableCapacity() * 100) + "%",
                // .. naujus parametrus tęsiame čia ..
                String.valueOf(map instanceof MapKTU ? ((MapKTU) map).averageNodeLength() : -1),
                String.valueOf(map instanceof MapKTU ? ((MapKTU) map).emptyElementCount() : -1)
        };
        for (int i = 0; i < parameters.length; i++) {
            String str = panParam2.getTfOfTable()[i].getText();

            if ((!str.equals(parameters[i]) && !str.equals("") && colorize)) {
                panParam2.getTfOfTable()[i].setForeground(Color.RED);
            } else {
                panParam2.getTfOfTable()[i].setForeground(Color.BLACK);
            }

            panParam2.getTfOfTable()[i].setText(parameters[i]);
        }
    }

    public JTextArea getTaEvents() {
        return taEvents;
    }

    /**
     * Klasė, skirta JTextField objekte įvedamo skaičiaus tikrinimui. Tikrinama
     * ar įvestas neneigiamas skaičius
     */
    private class MyInputVerifier extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            String text = ((JTextField) input).getText();
            try {
                int result = Integer.valueOf(text);
                input.setBackground(result >= 0 ? Color.WHITE : Color.RED);
                return result >= 0;
            } catch (NumberFormatException e) {
                input.setBackground(Color.RED);
                return false;
            }
        }
    }
}
