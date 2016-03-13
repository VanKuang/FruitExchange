package cn.vanchee.ui.table;

import cn.vanchee.model.PaidDetail;
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
public class PaidTableModel implements TableModel {

    private List<PaidDetail> paidDetailList;
    private String[] columnNames;
    private Class<?>[] columns;

    public PaidTableModel(List<PaidDetail> paidDetailList, String[] columnNames) {
        this.paidDetailList = paidDetailList;
        this.columnNames = columnNames;
        columns = new Class[]{
                Integer.class, //ID
                Integer.class, //OID
                Integer.class, //IID
                String.class, //FRUIT
                String.class, //CONSUMER
                Double.class, //money
                Double.class, //discount
                String.class,
                Long.class, //DATE
                String.class, //delete
                String.class //censored
        };
    }

    public int getRowCount() {
        return paidDetailList.size();
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
        PaidDetail paidDetail = paidDetailList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return paidDetail.getId();
            case 1:
                return paidDetail.getOid();
            case 2:
                return paidDetail.getIid();
            case 3:
                return paidDetail.getFruitName();
            case 4:
                return paidDetail.getConsumerName();
            case 5:
                return paidDetail.getMoney();
            case 6:
                return paidDetail.getDiscount();
            case 7:
                return Constants.getCensorName(paidDetail.getCensored());
            case 8:
                return paidDetail.getCreateAt();
            case 9:
                return "删除";
            case 10:
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