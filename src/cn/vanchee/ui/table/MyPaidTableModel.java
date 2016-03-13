package cn.vanchee.ui.table;

import cn.vanchee.model.MyPaid;
import cn.vanchee.util.Constants;

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
public class MyPaidTableModel implements TableModel {

    private List<MyPaid> paidDetailList;
    private String[] columnNames;
    private Class<?>[] columns;

    public MyPaidTableModel(List<MyPaid> paidDetailList, String[] columnNames) {
        this.paidDetailList = paidDetailList;
        this.columnNames = columnNames;
        columns = new Class[]{
                Integer.class, //ID
                Integer.class, //IID
                String.class, //OWNER
                String.class, //FRUIT
                Double.class, //money
                Long.class, //DATE
                String.class,
                String.class, //delete
                String.class
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
        MyPaid paidDetail = paidDetailList.get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        switch (columnIndex) {
            case 0:
                return paidDetail.getId();
            case 1:
                return paidDetail.getIid();
            case 2:
                return paidDetail.getOwnerName();
            case 3:
                return paidDetail.getFruitName();
            case 4:
                return paidDetail.getMoney();
            case 5:
                return sdf.format(new Date(paidDetail.getDate()));
            case 6:
                return Constants.getCensorName(paidDetail.getCensored());
            case 7:
                return "删除";
            case 8:
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