package cn.vanchee.ui.table;

import cn.vanchee.model.OutDetail;
import cn.vanchee.util.Constants;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * @author vanchee
 * @date 13-2-8
 * @package cn.vanchee.test
 * @verson v1.0.0
 */
public class OutDetailTableModel implements TableModel {

    private List<OutDetail> outDetailList;
    private String[] columnNames;
    private Class<?>[] columns;

    public OutDetailTableModel(List<OutDetail> outDetailList, String[] columnNames) {
        this.outDetailList = outDetailList;
        this.columnNames = columnNames;
        columns = new Class[]{
                Integer.class,
                Integer.class,
                Long.class,
                String.class,
                String.class,
                String.class,
                Double.class,
                Integer.class,
                Double.class,
                Double.class,
                Double.class,
                Integer.class,
                String.class,
                String.class};
    }

    public int getRowCount() {
        return outDetailList.size();
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
        OutDetail outDetail = outDetailList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return outDetail.getId();
            case 1:
                return outDetail.getIid();
            case 2:
                return outDetail.getCreateAt();
            case 3:
                return outDetail.getOwnerName();
            case 4:
                return outDetail.getConsumerName();
            case 5:
                return outDetail.getFruitName();
            case 6:
                return outDetail.getPrice();
            case 7:
                return outDetail.getNum();
            case 8:
                return outDetail.getMoney();
            case 9:
                return outDetail.getPaidMoneyNotIncludeDiscount();
            case 10:
                return outDetail.getPaidMoneyDiscount();
            case 11:
                return outDetail.getStatusName();
            case 12:
                return Constants.getCensorName(outDetail.getCensored());
            case 13:
                return "审核";
            default:
                return "";
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
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