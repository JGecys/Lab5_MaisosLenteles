package studijosKTU;

import studijosKTU.MapKTUx.HashTableModel;

import javax.swing.table.AbstractTableModel;

/**
 * @param <K>
 * @param <V>
 */
public interface MapADTx<K, V> extends MapADTp<K, V> {

    V put(String dataString);

    void load(String fName);

    void save(String fName);

    void println();

    void println(String title);
    /**
     * Grąžina maišos lentelės modelį, skirtą atvaizdavimui JTable objekte
     *
     * @param delimiter Celės elemento kirtiklis
     * @return Grąžina HashTableModel klasės objektą.
     */
    AbstractTableModel getModel(String delimiter);
}
