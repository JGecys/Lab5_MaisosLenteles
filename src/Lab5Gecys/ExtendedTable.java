package Lab5Gecys;

import studijosKTU.MapKTUx.HashTableModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Klasėje specifikuota apibrėžtas savybes turinti lentelė. Naudojama maišos
 * lentelės atvaizdavimui.
 *
 * @author darius
 */
public class ExtendedTable extends JTable {

    private int colWidth; // Stulpelių plotis

    public void setModel(AbstractTableModel model, int colWidth) {
        if (model == null) {
            throw new IllegalArgumentException("AbstractTableModel is null");
        }

        if (colWidth <= 0) {
            throw new IllegalArgumentException("Table column width is <=0: " + colWidth);
        }

        this.colWidth = colWidth;
        setModel(model);
        appearance();
    }

    private void appearance() {
        setShowGrid(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // Celės stilius - pacentruojame
        DefaultTableCellRenderer toCenter = new DefaultTableCellRenderer();
        toCenter.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < getColumnCount(); i++) {
            if (i == 0) {
                getColumnModel().getColumn(i).setPreferredWidth(55);
                // Nustatome nulinio stulpelio celių stilių
                getColumnModel().getColumn(i).setCellRenderer(toCenter);
            } else if (i % 2 != 0) {
                getColumnModel().getColumn(i).setPreferredWidth(20);
                // Nustatome stulpelių su rodyklėmis celių stilių
                getColumnModel().getColumn(i).setCellRenderer(toCenter);
            } else {
                getColumnModel().getColumn(i).setMaxWidth(colWidth);
                getColumnModel().getColumn(i).setMinWidth(colWidth);
            }
        }
        setRowHeight(17);
        // Lentelės antraštės
        getTableHeader().setResizingAllowed(false);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        // Išcentruojamos antraštės
        ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        // Nustatomas tooltips'ų rodymas
        String value = (String) getValueAt(row, column);

        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            jc.setToolTipText(value);
        }

        // Morkine spalva nuspalvinamos celės, kuriose kas nors įrašyta, išskyrus rodyklę
        if (value != null && !value.equals("") && !value.equals("-->")) {
            c.setBackground(Color.ORANGE);//new Color(204, 255, 204));
        } //Baltai - likusias celes
        else {
            c.setBackground(Color.WHITE);
        }

        return c;
    }
}
