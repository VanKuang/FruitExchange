package cn.vanchee.ui.table;

import cn.vanchee.model.User;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * @author vanchee
 * @date 13-2-8
 * @package cn.vanchee.test
 * @verson v1.0.0
 */
public class UserTableModel implements TableModel {

    private List<User> userList;
    private String[] columnNames;
    private Class<?>[] columns;

    public UserTableModel(List<User> UserList, String[] columnNames) {
        this.userList = UserList;
        this.columnNames = columnNames;
        columns = new Class[]{
                String.class,
                String.class,
                String.class};
    }

    public int getRowCount() {
        return userList.size();
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
        User user = userList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return user.getName();
            case 1:
                return user.getPassword();
            case 2:
                return "删除";
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