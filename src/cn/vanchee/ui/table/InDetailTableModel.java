package cn.vanchee.ui.table;

import cn.vanchee.model.InDetail;
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
public class InDetailTableModel implements TableModel {

    private List<InDetail> InDetailList;
    private String[] columnNames;
    private Class<?>[] columns;

    public InDetailTableModel(List<InDetail> InDetailList, String[] columnNames) {
        this.InDetailList = InDetailList;
        this.columnNames = columnNames;
        columns = new Class[]{
                Integer.class,
                Long.class,
                String.class,
                String.class,
                Double.class,
                Integer.class,
                Double.class,
                Double.class,
                Integer.class,
                Integer.class,
                String.class,
                String.class};
    }

    public int getRowCount() {
        return InDetailList.size();
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
        InDetail inDetail = InDetailList.get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        switch (columnIndex) {
            case 0:
                return inDetail.getId();
            case 1:
                return sdf.format(new Date(inDetail.getDate()));
            case 2:
                return inDetail.getOwnerName();
            case 3:
                return inDetail.getFruitName();
            case 4:
                return inDetail.getPrice();
            case 5:
                return inDetail.getNum();
            case 6:
                return inDetail.getPrice() * inDetail.getNum();
            case 7:
                return inDetail.getPaidMoney();
            case 8:
                return inDetail.getSale();
            case 9:
                return inDetail.getRemain();
            case 10:
                return Constants.getCensorName(inDetail.getCensored());
            case 11:
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