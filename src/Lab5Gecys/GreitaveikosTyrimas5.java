package Lab5Gecys;

import Gui.KsSwing;
import Gui.Resources;
import Lab2Gecys.Telefonas;
import Lab2Gecys.TelefonuGeneratorius;
import studijosKTU.HashType;
import studijosKTU.MapKTU;
import studijosKTU.MapKTUx;
import studijosKTU.Timekeeper;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * @author eimutis
 */
public class GreitaveikosTyrimas5 extends Thread {

    private static final ResourceBundle rb = ResourceBundle.getBundle(Resources.class.getCanonicalName());
    static JTextArea ta;
    static JButton[] btns;
    static String[] tyrimųVardai = {"addKtu", "addMano", "remKtu", "remMano", "getKtu", "getMano"};
    static int[] tiriamiKiekiai = {10000, 20000, 40000, 80000};
    static MapKTUx<String, Telefonas> telAtvaizdis
            = new MapKTUx(new String(), new Telefonas(), 10, 0.25f, HashType.DIVISION);
    static MapAtviraMaisa<String, Telefonas> telAtvaizdis2
            = new MapAtviraMaisa<>(new String(), new Telefonas(), 10, 0.25f, HashType.DIVISION);
    static MapKTUx<String, String> telAtvaizdis3 = new MapKTUx<>(new String(), new String(), 10, 0.25f, HashType.DIVISION);
    static MapAtviraMaisa<String, String> telAtvaizdis4 = new MapAtviraMaisa<>(new String(), new String(), 10, 0.25f, HashType.DIVISION);
    static Queue<String> chainsSizes = new LinkedList<>();
    static String str;
    static TelefonuGeneratorius gamyba = new TelefonuGeneratorius();

    public GreitaveikosTyrimas5(JTextArea ta, JButton[] btns) {
        GreitaveikosTyrimas5.ta = ta;
        GreitaveikosTyrimas5.btns = btns;
    }

    @Override
    public void run() {
        chainsSizes.clear();
        for (JButton btn : btns) {
            btn.setEnabled(false);
        }
        SisteminisTyrimas();
        SisteminisTyrimasIsFailo();
        for (JButton btn : btns) {
            btn.setEnabled(true);
        }
    }

    private void SisteminisTyrimasIsFailo() {
        try {
            File file = new File("C:\\Users\\jgecy\\Documents\\KTUProjektai\\1Semestras\\5Laboras\\Lab5_MaisosLenteles\\Duomenys\\zodynas.txt");
            FileReader fr = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fr);
            Stream<String> linesStream = bufferedReader.lines();
            List<String> linesList = new LinkedList<>();
            linesStream.forEach(s1 -> linesList.add(s1));
            String[] lines = new String[linesList.size()];
            lines = linesList.toArray(lines);
            Timekeeper tk = new Timekeeper(new int[]{lines.length}, ta);
            telAtvaizdis3.clear();
            telAtvaizdis4.clear();
            tk.startAfterPause();
            tk.start();
            for (String a : lines) {
                telAtvaizdis3.put(a, a);
            }
            tk.finish(tyrimųVardai[0]);
            str = "          " + telAtvaizdis3.getMaxChainSize();
            for (String a : lines) {
                telAtvaizdis4.put(a, a);
            }
            tk.finish(tyrimųVardai[1]);
            str += "         " + telAtvaizdis4.getMaxChainSize();

            for (String s : lines) {
                telAtvaizdis3.get(s);
            }
            tk.finish(tyrimųVardai[4]);
            for (String s : lines) {
                telAtvaizdis4.get(s);
            }
            tk.finish(tyrimųVardai[5]);

            for (String s : lines) {
                telAtvaizdis3.remove(s);
            }
            tk.finish(tyrimųVardai[2]);
            for (String s : lines) {
                telAtvaizdis4.remove(s);
            }
            tk.finish(tyrimųVardai[3]);
            tk.seriesFinish();
            KsSwing.oun(ta, "", rb.getStringArray("msgs")[7]);
            KsSwing.setFormatStartOfLine(false);
            KsSwing.oun(ta, chainsSizes);
            KsSwing.setFormatStartOfLine(true);
        } catch (FileNotFoundException e) {
            Lab5Gecys.KsSwing.ounerr(ta, e);
        }

    }

    public void SisteminisTyrimas() {
        Timekeeper tk = new Timekeeper(tiriamiKiekiai, ta);
        chainsSizes.add("   kiekis      " + tyrimųVardai[0] + "   " + tyrimųVardai[1]);
        for (int k : tiriamiKiekiai) {
            Telefonas[] autoArray = gamyba.generuotiMasyva(k);
            String[] autoIdArray = gamyba.formuotiIDMasyva(k);
            telAtvaizdis.clear();
            telAtvaizdis2.clear();
            tk.startAfterPause();
            tk.start();
            int iID = 0;
            for (Telefonas a : autoArray) {
                telAtvaizdis.put(autoIdArray[iID++], a);
            }
            tk.finish(tyrimųVardai[0]);
            iID = 0;
            str = "   " + k + "          " + telAtvaizdis.getMaxChainSize();
            for (Telefonas a : autoArray) {
                telAtvaizdis2.put(autoIdArray[iID++], a);
            }
            tk.finish(tyrimųVardai[1]);
            str += "         " + telAtvaizdis2.getMaxChainSize();
            chainsSizes.add(str);


            for (String s : autoIdArray) {
                telAtvaizdis2.get(s);
            }
            tk.finish(tyrimųVardai[4]);
            for (String s : autoIdArray) {
                telAtvaizdis2.get(s);
            }
            tk.finish(tyrimųVardai[5]);

            for (String s : autoIdArray) {
                telAtvaizdis.remove(s);
            }
            tk.finish(tyrimųVardai[2]);
            for (String s : autoIdArray) {
                telAtvaizdis2.remove(s);
            }
            tk.finish(tyrimųVardai[3]);
            tk.seriesFinish();
        }
        KsSwing.oun(ta, "", rb.getStringArray("msgs")[7]);
        KsSwing.setFormatStartOfLine(false);
        KsSwing.oun(ta, chainsSizes);
        KsSwing.setFormatStartOfLine(true);
    }
}
