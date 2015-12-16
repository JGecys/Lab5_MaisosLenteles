package Lab5Gecys;

import studijosKTU.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.util.Arrays;

/**
 * Created by jgecy on 2015-12-15.
 */
public class MapAtviraMaisa<K, V> implements MapADTx<K, V> {


    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    public static final HashType DEFAULT_HASH_TYPE = HashType.DIVISION;

    // Maišos lentelė
    protected Node<K, V>[] table;
    // Specialus elementas
    protected Node<K, V> removed = new Node<>();
    // Lentelėje esančių raktas-reikšmė porų kiekis
    protected int size = 0;
    // Apkrovimo faktorius
    protected float loadFactor;
    // Maišos metodas
    protected HashType ht;
    //--------------------------------------------------------------------------
    //  Maišos lentelės įvertinimo parametrai
    //--------------------------------------------------------------------------
    // Permaišymų kiekis
    protected int rehashesCounter = 0;
    // Einamas poros indeksas maišos lentelėje
    protected int index = 0;


    private K baseKey;       // Bazinis objektas skirtas naujų kūrimui
    private V baseObj;       // Bazinis objektas skirtas naujų kūrimui

    /**
     * Konstruktorius su bazinio objekto fiksacija
     *
     * @param baseKey
     * @param baseObj
     */
    public MapAtviraMaisa(K baseKey, V baseObj) {
        this(baseKey, baseObj, DEFAULT_HASH_TYPE);
    }

    /**
     * Konstruktorius su bazinio objekto fiksacija
     *
     * @param baseKey
     * @param baseObj
     * @param ht
     */
    public MapAtviraMaisa(K baseKey, V baseObj, HashType ht) {
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
    public MapAtviraMaisa(K baseKey, V baseObj, int initialCapacity, HashType ht) {
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
    public MapAtviraMaisa(K baseKey, V baseObj, int initialCapacity, float loadFactor, HashType ht) {
        this(initialCapacity, loadFactor, ht);
        this.baseKey = baseKey;     // fiksacija dėl naujų elementų kūrimo
        this.baseObj = baseObj;     // fiksacija dėl naujų elementų kūrimo
    }


    /* Klasėje sukurti 4 perkloti konstruktoriai, nustatantys atskirus maišos
     * lentelės parametrus. Jei kuris nors parametras nėra nustatomas -
     * priskiriama standartinė reikšmė.
     */
    public MapAtviraMaisa() {
        this(DEFAULT_HASH_TYPE);
    }

    public MapAtviraMaisa(HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, ht);
    }

    public MapAtviraMaisa(int initialCapacity, HashType ht) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, ht);
    }

    public MapAtviraMaisa(float loadFactor, HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, loadFactor, ht);
    }

    public MapAtviraMaisa(int initialCapacity, float loadFactor, HashType ht) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: "
                    + initialCapacity);
        }

        if ((loadFactor <= 0.0) || (loadFactor > 1.0)) {
            throw new IllegalArgumentException("Illegal load factor: "
                    + loadFactor);
        }

        this.table = new Node[initialCapacity];
        this.loadFactor = loadFactor;
        this.ht = ht;
    }

    /**
     * Patikrinama ar atvaizdis yra tuščias.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Grąžinamas atvaizdyje esančių porų kiekis.
     *
     * @return Grąžinamas atvaizdyje esančių porų kiekis.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Išvalomas atvaizdis.
     */
    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        index = 0;
        rehashesCounter = 0;
    }

    /**
     * Patikrinama ar pora egzistuoja atvaizdyje.
     *
     * @param key raktas.
     * @return Patikrinama ar pora egzistuoja atvaizdyje.
     */
    @Override
    public boolean contains(K key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && table[i] != removed && table[i].value.equals(value)) {
                return true;
            }
        }
        return false;
    }


    public int emptyElementCount() {
        int cnt = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * Atvaizdis papildomas nauja pora.
     *
     * @param key   raktas,
     * @param value reikšmė.
     * @return Atvaizdis papildomas nauja pora.
     */
    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value is null in put(Key key, Value value)");
        }

        index = findPosition(key, true);
        if (index < 0) {
            rehash();
            index = findPosition(key, true);
        }

        table[index] = new Node<>(key, value);


        return value;
    }

    /**
     * Grąžinama atvaizdžio poros reikšmė.
     *
     * @param key raktas.
     * @return Grąžinama atvaizdžio poros reikšmė.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in get(Key key)");
        }

        index = findPosition(key, false);
        if (index < 0 || table[index] == null)
            return null;
        return table[index].value;
    }

    /**
     * Pora pašalinama iš atvaizdžio.
     *
     * @param key Pora pašalinama iš atvaizdžio.
     * @return key raktas.
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in remove(Key key)");
        }
        index = findPosition(key, false);
        if(index < 0 || table[index] == null)
            return null;
        V value = table[index].value;
        table[index] = removed;
        return value;
    }

    /**
     * Permaišymas
     */
    private void rehash() {
        MapAtviraMaisa mapKTU = new MapAtviraMaisa(table.length * 2, loadFactor, ht);
        for (int i = 0; i < table.length; i++) {
            mapKTU.put(table[i].key, table[i].value);
        }
        table = mapKTU.table;
        rehashesCounter++;
    }

    /**
     * Maišos funkcijos skaičiavimas: pagal rakto maišos kodą apskaičiuojamas
     * atvaizdžio poros indeksas maišos lentelės masyve
     *
     * @param key
     * @param hashType
     * @return
     */
    private int hash(K key, HashType hashType) {
        int h = key.hashCode();
        switch (hashType) {
            case DIVISION:
                return Math.abs(h) % table.length;
            case MULTIPLICATION:
                double k = (Math.sqrt(5) - 1) / 2;
                return (int) (((k * Math.abs(h)) % 1) * table.length);
            case JCF7:
                h ^= (h >>> 20) ^ (h >>> 12);
                h = h ^ (h >>> 7) ^ (h >>> 4);
                return h & (table.length - 1);
            case JCF8:
                h = h ^ (h >>> 16);
                return h & (table.length - 1);
            default:
                return Math.abs(h) % table.length;
        }
    }

    private int findPosition(K key, boolean canBeEmpty) {
        int index = hash(key, ht);
        int index0 = index;
        int i = 0;
        for (int j = 0; j < table.length; j++) {
            if ((table[index] != removed && (table[index] == null || table[index].key.equals(key)))
                    || (canBeEmpty && table[index] == removed)) {
                return index;
            }
            i++;
            index = (index0 + i) % table.length;
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Node<K, V> node : table) {
            if (node != null) {
                result.append(node.toString()).append(System.lineSeparator());
            }
        }
        return result.toString();
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
        if (isEmpty()) {
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

        Ks.oufln("****** Bendras porų kiekis yra " + size());
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

    @Override
    public int getMaxChainSize() {
        return 1;
    }

    @Override
    public int getRehashesCounter() {
        return -1;
    }

    @Override
    public int getTableCapacity() {
        return table.length;
    }

    @Override
    public int getLastUpdatedChain() {
        return -1;
    }

    @Override
    public int getChainsCounter() {
        return -1;
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
                if(table[row] == removed)
                    return "";
                return table[row].toString();
            }

            return "";
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
            return getMaxChainSize() * 2 + 1;
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

    protected class Node<Key, Value> {

        // Raktas
        protected Key key;
        // Reikšmė
        protected Value value;

        protected Node() {
        }

        protected Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
