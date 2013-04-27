package cn.vanchee.ui.table;

import cn.vanchee.model.Consumption;
import cn.vanchee.util.Constants;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author vanchee
 * @date 13-2-8
 * @package cn.vanchee.test
 * @verson v1.0.0
 */
public class ConsumptionTableModel implements TableModel {

    private List<Consumption> consumptionList;
    private String[] columnNames;
    private Class<?>[] columns;

    public ConsumptionTableModel(List<Consumption> ConsumptionList, String[] columnNames) {
        this.consumptionList = ConsumptionList;
        this.columnNames = columnNames;
        columns = new Class[] {
                Integer.class,
                Long.class,
                Double.class,
                String.class,
                String.class,
                String.class,
                String.class};
    }

    public int getRowCount() {
        return consumptionList.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex < 0 || columnIndex > columns.length) {
            return null;
        }
        return columns[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Consumption consumption = consumptionList.get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        switch (columnIndex) {
            case 0:
                return consumption.getId();
            case 1:
                return sdf.format(new Date(consumption.getDate()));
            case 2:
                return consumption.getMoney();
            case 3:
                return consumption.getDesc();
            case 4:
                return Constants.getCensorName(consumption.getCensored());
            case 5:
                return "删除";
            case 6:
                return "审核";
            default:
                return "";
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex != 0) {
            return false;
        }
        return false;
    }

    public String getColumnName(int column) {
        if (column < 0 || column > columns.length)
            return null;
        return columnNames[column];
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }
}