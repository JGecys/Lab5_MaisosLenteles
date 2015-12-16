package studijosKTU;

import javax.swing.JTextArea;

/**
 * @author eimutis
 */
public class Timekeeper {

    int[] tyrimoImtis;
    private final JTextArea ta;
    private long startTime, finishTime;
    private final long startTimeTot;
    private long finishTimeTot;
    private double sumTime;
    private int tyrimoInd;
    private int kiekioInd;
    private int tyrimųN;
    private final int tyrimųNmax = 30;
    private final int kiekioN;
    double[][] laikai;
    private String tyrimųEilutė;
    private final String duomFormatas = "%9.4f ";
    private final String normFormatas = "%9.2f ";
    private final String vardoFormatas = "%9s ";
    private final String kiekioFormatas = "%8d(%2d) ";
    private String antraštė = "  kiekis(*k) ";

    public Timekeeper(int[] kiekiai, JTextArea ta) {
        this.tyrimoImtis = kiekiai;
        this.ta = ta;
        kiekioN = tyrimoImtis.length;
        laikai = new double[kiekioN][tyrimųNmax];
        startTimeTot = System.nanoTime();
    }

    public void start() {
        tyrimoInd = 0;

        if (kiekioInd >= kiekioN) {
            ta.append("Duomenų kiekis keičiamas daugiau kartų nei buvo planuota");
            // System.exit(0);
        }

        tyrimųEilutė = String.format(kiekioFormatas, tyrimoImtis[kiekioInd],
                tyrimoImtis[kiekioInd] / tyrimoImtis[0]);
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
        startTime = System.nanoTime();
    }

    public void startAfterPause() {
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
        startTime = System.nanoTime();
    }

    public void finish(String vardas) {
        finishTime = System.nanoTime();
        double t1 = (finishTime - startTime) / 1e9;
        sumTime += t1;

        if (startTime == 0) {
            ta.append("Metodas finish panaudotas be start metodo !!!" + System.lineSeparator());
            //   System.exit(0);
        }

        if (kiekioInd == 0) {
            antraštė += String.format(vardoFormatas, vardas);
        }

        if (tyrimoInd >= tyrimųNmax) {
            ta.append("Jau atlikta daugiau tyrimų nei numatyta  !!!" + System.lineSeparator());
            //  System.exit(0);
        }

        laikai[kiekioInd][tyrimoInd++] = t1;
        tyrimųEilutė += String.format(duomFormatas, t1);
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
        startTime = System.nanoTime();
    }

    public void seriesFinish() {
        if (kiekioInd == 0) {
            ta.append(antraštė + System.lineSeparator());
        }

        ta.append(tyrimųEilutė + System.lineSeparator());
        kiekioInd++;
        tyrimųN = tyrimoInd;

        if (kiekioInd == (kiekioN)) {
            summary();
        }
    }

    private void summary() {
        finishTimeTot = System.nanoTime();
        double totTime = (finishTimeTot - startTimeTot) / 1e9;
        ta.append(String.format("       Bendras tyrimo laikas %8.3f sekundžių", totTime) + System.lineSeparator());
        ta.append(String.format("    Išmatuotas tyrimo laikas %8.3f sekundžių", sumTime) + System.lineSeparator());
        ta.append(String.format("    t.y. %5.1f%% sudaro pagalbiniai darbai",
                (totTime - sumTime) / totTime * 100) + System.lineSeparator());
        ta.append(System.lineSeparator());
        ta.append("Normalizuota (santykinė) laikų lentelė" + System.lineSeparator());
        ta.append(antraštė + System.lineSeparator());
        double d1 = laikai[0][0];
        for (int i = 0; i < kiekioN; i++) {
            tyrimųEilutė = String.format(kiekioFormatas, tyrimoImtis[i],
                    tyrimoImtis[i] / tyrimoImtis[0]);
            for (int j = 0; j < tyrimųN; j++) {
                tyrimųEilutė += String.format(normFormatas, laikai[i][j] / d1);
            }
            ta.append(tyrimųEilutė + System.lineSeparator());
        }
        ta.append(System.lineSeparator());
    }
}
