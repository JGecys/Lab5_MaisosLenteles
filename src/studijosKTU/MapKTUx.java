package studijosKTU;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.table.AbstractTableModel;

public class MapKTUx<K, V> extends MapKTU<K, V> implements MapADTx<K, V> {

    private K baseKey;       // Bazinis objektas skirtas naujų kūrimui
    private V baseObj;       // Bazinis objektas skirtas naujų kūrimui

    /**
     * Konstruktorius su bazinio objekto fiksacija
     *
     * @param baseKey
     * @param baseObj
     */
    public MapKTUx(K baseKey, V baseObj) {
        this(baseKey, baseObj, DEFAULT_HASH_TYPE);
    }

    /**
     * Konstruktorius su bazinio objekto fiksacija
     *
     * @param baseKey
     * @param baseObj
     * @param ht
     */
    public MapKTUx(K baseKey, V baseObj, HashType ht) {
        this(baseKey, baseObj, DEFAULT_INITIAL_CAPACITY, ht);
    }

    /**
     * Konstruktorius su bazinio objekto fiksacija
     *
     * @param baseKey
     * @param baseObj
     * @param initialCapacity
     * @param ht
     */
    public MapKTUx(K baseKey, V baseObj, int initialCapacity, HashType ht) {
        this(baseKey, baseObj, initialCapacity, DEFAULT_LOAD_FACTOR, ht);
    }

    /**
     * Konstruktorius su bazinio objekto fiksacija
     *
     * @param baseKey
     * @param baseObj
     * @param initialCapacity
     * @param loadFactor
     * @param ht
     */
    public MapKTUx(K baseKey, V baseObj, int initialCapacity, float loadFactor, HashType ht) {
        super(initialCapacity, loadFactor, ht);
        this.baseKey = baseKey;     // fiksacija dėl naujų elementų kūrimo
        this.baseObj = baseObj;     // fiksacija dėl naujų elementų kūrimo
    }

    /**
     * Sukuria elementą iš String. Jei turime failą, kuriame saugomos
     * raktas-reikšmė poros, šią vietą reikėtų atitinkamai modifikuoti
     *
     * @param dataString
     * @return
     */
    @Override
    public V put(String dataString) {
        return put((K) dataString, (V) dataString);
    }

    /**
     * Suformuoja atvaizį iš fName failo
     *
     * @param fName
     */
    @Override
    public void load(String fName) {
        clear();
        if (fName.length() == 0) {
            return;
        }

        if ((baseKey == null) || (baseObj == null)) {          // elementų kūrimui reikalingas baseObj
            Ks.ern("Naudojant load-metodą, "
                    + "reikia taikyti konstruktorių = new ListKTU(new Data())");
            // System.exit(0);
        }

        String fN = "";
        try {
            new File(System.getProperty("user.dir")).mkdir();
            fN = System.getProperty("user.dir") + File.separatorChar + fName;
            try (BufferedReader fReader = new BufferedReader(new FileReader(new File(fN)))) {
                String dLine;
                while ((dLine = fReader.readLine()) != null && !dLine.trim().isEmpty()) {
                    put(dLine);
                }
            }
        } catch (FileNotFoundException e) {
            Ks.ern("Duomenų failas " + fN + " nerastas");
            // System.exit(0);
        } catch (IOException e) {
            Ks.ern("Failo " + fN + " skaitymo klaida");
            // System.exit(0);
        }
    }

    /**
     * Išsaugoja sąrašą faile fName tekstiniu formatu tinkamu vėlesniam
     * skaitymui
     *
     * @param fName
     */
    @Override
    public void save(String fName) {
        String fN = "";
        try {
            // jei vardo nėra - failas neformuojamas
            if (fName.equals("")) {
                return;
            }

            fN = System.getProperty("user.dir") + File.separatorChar + fName;
            try (PrintWriter fWriter = new PrintWriter(new FileWriter(new File(fN)))) {
                for (int i = 0; i < getModel("=").getRowCount(); i++) {
                    for (int j = 0; j < getModel("=").getColumnCount(); j++) {
                        String str = getModel("").getValueAt(i, j).toString();

                        if (!str.equals("")) {
                            fWriter.println(str);
                        }
                    }
                }
            }
        } catch (IOException e) {
            Ks.ern("!!! Klaida formuojant " + fN + " failą.");
            System.exit(0);
        }
    }

    /**
     * Atvaizdis spausdinamas į Ks.ouf("");
     */
    @Override
    public void println() {
        if (super.isEmpty()) {
            Ks.oun("Atvaizdis yra tuščias");
        } else {
            for (int i = 0; i < getModel("=").getRowCount(); i++) {
                for (int j = 0; j < getModel("=").getColumnCount(); j++) {
                    String format = (j == 0) ? "%7s" : "%15s";
                    Object value = getModel("=").getValueAt(i, j);
                    Ks.ouf(format, (value == null) ? "" : value + " ->");
                }
                Ks.oufln("");
            }
        }

        Ks.oufln("****** Bendras porų kiekis yra " + super.size());
    }

    /**
     * Spausdinant galima nurodyti antraštę
     *
     * @param title
     */
    @Override
    public void println(String title) {
        Ks.ounn("========" + title + "=======");
        println();
        Ks.ounn("======== Atvaizdžio pabaiga =======");
    }

    /**
     * Grąžina maišos lentelės modelį, skirtą atvaizdavimui JTable objekte
     *
     * @param delimiter Lentelės celės elemento teksto kirtiklis
     * @return Grąžina AbstractTableModel klasės objektą.
     */
    @Override
    public HashTableModel getModel(String delimiter) {
        return new HashTableModel(delimiter);
    }

    /**
     * Lentelės modelio klasė
     */
    public class HashTableModel extends AbstractTableModel {

        private final String delimiter;

        public HashTableModel(String delimiter) {
            this.delimiter = delimiter;
        }

        @Override
        public Object getValueAt(int row, int col) {
            if (col == 0) {
                return "[" + row + "]";
            }

            if ((row <= table.length) && (table[row] != null)) {
                int count = 1;
                MapKTU.Node n = table[row];
                while (n != null) {
                    if (count == col) {
                        return (count % 2 != 0) ? "-->" : split(n.toString(), delimiter);
                    }

                    if (count % 2 == 0) {
                        n = n.next;
                    }

                    count++;
                }
            }

            return null;
        }

        @Override
        public String getColumnName(int col) {
            if (col == 0) {
                return "#";
            }

            if (col % 2 == 0) {
                return "(" + (col / 2 - 1) + ")";
            }

            return "";
        }

        @Override
        public int getColumnCount() {
            return maxChainSize * 2 + 1;
        }

        @Override
        public int getRowCount() {
            return table.length;
        }

        private String split(String s, String delimiter) {
            int k = s.indexOf(delimiter);

            if (k <= 0) {
                return s;
            }

            return s.substring(0, k);
        }
    }
}
