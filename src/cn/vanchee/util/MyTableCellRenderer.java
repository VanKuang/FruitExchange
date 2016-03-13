package cn.vanchee.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author vanchee
 * @date 13-3-8
 * @package cn.vanchee.util
 * @verson v1.0.0
 */
public class MyTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        setHorizontalAlignment(SwingConstants.LEADING); // 水平居中
        setHorizontalTextPosition(column == 0 ? SwingConstants.CENTER : SwingConstants.LEADING); // 垂直居中
        return super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
    }
}
