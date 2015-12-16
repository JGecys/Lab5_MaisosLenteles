package Lab5Gecys;

import Gui.KsSwing;
import Gui.KsSwing.MyException;
import Gui.Resources;
import Lab2Gecys.Telefonas;
import Lab2Gecys.TelefonuGeneratorius;
import LaboraiDemo.AutoGamyba;
import LaboraiDemo.Automobilis;
import studijosKTU.HashType;
import studijosKTU.MapKTUx;
import studijosKTU.Timekeeper;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

/**
 * @author eimutis
 */
public class GreitaveikosTyrimas extends Thread {

    private static final ResourceBundle rb = ResourceBundle.getBundle(Resources.class.getCanonicalName());
    static JTextArea ta;
    static JButton[] btns;
    static String[] tyrimųVardai = {"add0.75", "add0.25", "rem0.75", "rem0.25", "get0.75", "get0.25"};
    static int[] tiriamiKiekiai = {10000, 20000, 40000, 80000};
    static MapKTUx<String, Telefonas> telAtvaizdis
            = new MapKTUx(new String(), new Telefonas(), 10, 0.75f, HashType.DIVISION);
    static MapKTUx<String, Telefonas> telAtvaizdis2
            = new MapKTUx(new String(), new Telefonas(), 10, 0.25f, HashType.DIVISION);
    static Queue<String> chainsSizes = new LinkedList<>();
    static String str;
    static TelefonuGeneratorius gamyba = new TelefonuGeneratorius();

    public GreitaveikosTyrimas(JTextArea ta, JButton[] btns) {
        GreitaveikosTyrimas.ta = ta;
        GreitaveikosTyrimas.btns = btns;
    }

    @Override
    public void run() {
        chainsSizes.clear();
        for (JButton btn : btns) {
            btn.setEnabled(false);
        }
        SisteminisTyrimas();
        for (JButton btn : btns) {
            btn.setEnabled(true);
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
