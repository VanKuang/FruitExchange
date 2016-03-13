package cn.vanchee.util;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class MyAbstractTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    protected List<Object> datas = new ArrayList<Object>();
    protected Class<?>[] columns;
    protected String[] columnNames;

    protected abstract Object getProperty(Object obj, int columnIndex);

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @SuppressWarnings("unchecked")
    public void setData(List datas) {
        this.datas = datas;
    }

    public List<Object> list() {
        return datas;
    }

    public Object getObject(int rowIndex) {
        return datas.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return datas.size();
    }

    protected Class<?> getFieldType(Class<?> clz, String field) {
        try {
            Class<?> result = clz.getDeclaredField(field).getType();
            if (result.equals(int.class)) {
                result = Integer.class;
            } else if (result.equals(boolean.class)) {
                result = Boolean.class;
            } else if (result.equals(byte.class)) {
                result = Byte.class;
            } else if (result.equals(char.class)) {
                result = Character.class;
            } else if (result.equals(long.class)) {
                result = Long.class;
            } else if (result.equals(float.class)) {
                result = Float.class;
            } else if (result.equals(double.class)) {
                result = Double.class;
            }
            return result;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return String.class;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex < 0 || columnIndex > columns.length)
            return null;
        return columns[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public String getColumnName(int column) {
        if (column < 0 || column > columns.length)
            return null;
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex > getRowCount() || columnIndex > getColumnCount()
                || rowIndex < 0 || columnIndex < 0)
            return null;
        return getProperty(datas.get(rowIndex), columnIndex);
    }

}