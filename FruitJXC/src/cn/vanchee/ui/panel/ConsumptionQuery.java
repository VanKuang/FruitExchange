package cn.vanchee.ui.panel;

import cn.vanchee.model.Consumption;
import cn.vanchee.model.Resource;
import cn.vanchee.model.User;
import cn.vanchee.ui.MainApp;
import cn.vanchee.ui.table.ConsumptionTableModel;
import cn.vanchee.util.Constants;
import cn.vanchee.util.DateChooser;
import cn.vanchee.util.MyColorTableCellRenderer;
import cn.vanchee.util.MyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author vanchee
 * @date 13-1-31
 * @package cn.vanchee.ui
 * @verson v1.0.0
 */
public class ConsumptionQuery extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(ConsumptionQuery.class);

    private MainApp mainApp;

    private JTextField showDateFrom;
    private JTextField showDateTo;
    private JLabel summary;

    private JPanel searchPanel;
    private JScrollPane dataPanel;

    private GridBagConstraints gbc;
    private GridBagLayout gbl;
    private int gridX, gridY, gridWidth, gridHeight, anchor, fill, iPadX, iPadY;
    private double weightX, weightY;
    private Insets insert = null;

    private List<Consumption> result;

    public ConsumptionQuery(MainApp mainApp) {
        this.mainApp = mainApp;
        getContent();
    }

    private void getContent() {
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        addSearchPanel();
        addDataPanel();
        this.setLayout(gbl);
    }

    private void addSearchPanel() {
        Dimension dateDimension = Constants.getDateDimension();

        searchPanel = new JPanel();
        searchPanel.setBorder(new TitledBorder("查询条件"));
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel jlDateFrom = new JLabel("日期");
        searchPanel.add(jlDateFrom);

        DateChooser dateChooserFrom = DateChooser.getInstance("yyyy-MM-dd");
        showDateFrom = new JTextField("开始日期");
        showDateFrom.setPreferredSize(dateDimension);
        dateChooserFrom.register(showDateFrom);
        searchPanel.add(showDateFrom);

        JLabel jlDateTo = new JLabel("——");
        searchPanel.add(jlDateTo);

        DateChooser dateChooserTo = DateChooser.getInstance("yyyy-MM-dd");
        showDateTo = new JTextField("结束日期");
        showDateTo.setPreferredSize(dateDimension);
        dateChooserTo.register(showDateTo);
        searchPanel.add(showDateTo);

        JButton searchBtn = new JButton("查询");
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        searchPanel.add(searchBtn);

        summary = new JLabel("");
        summary.setVisible(false);
        searchPanel.add(summary);

        gridX = 0; // X0
        gridY = 0; // Y0
        gridWidth = 1; // 横占一个单元格
        gridHeight = 1; // 列占三个单元格
        weightX = 1.0; // 当窗口放大时，长度随之放大
        weightY = 0.0; // 当窗口放大时，高度随之放大
        anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
        fill = GridBagConstraints.BOTH; // 当有剩余空间时，填充空间
        insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
        iPadX = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
        iPadY = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
        gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor,
                fill, insert, iPadX, iPadY);
        gbl.setConstraints(searchPanel, gbc);
        this.add(searchPanel);
    }

    private void addDataPanel() {

        long f = -1;
        String from = showDateFrom.getText();
        if (!from.equals("开始日期") && !"".equals(from)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = null;
            try {
                fromDate = sdf.parse(from);
            } catch (ParseException e) {
                log.error(e.getMessage());
            }
            f = fromDate.getTime();
        }

        long e = -1;
        String end = showDateTo.getText();
        if (!end.equals("结束日期") && !"".equals(end)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date endDate = null;
            try {
                endDate = sdf.parse(end);
            } catch (ParseException e1) {
                log.error(e1.getMessage());
            }
            e = endDate.getTime();
        }

        User user = MyFactory.getCurrentUser();
        int uid = -1;
        if (!MyFactory.getResourceService()
                .hasRight(MyFactory.getCurrentUser(), Resource.GET_OTHERS_DATA)) {
            uid = user.getId();
        }

        String[] columnNames = new String[]{"消费单号", "日期", "花费", "备注", "审核状态", "操作", "审核"};
        result = MyFactory.getConsumptionService().queryConsumption(f, e, uid);
        if (!MyFactory.getResourceService().hasRight(user, Resource.CENSORED)) {
            result = MyFactory.getConsumptionService().selectCensoredReverse(result, Constants.CENSORED_PASS);
            columnNames = new String[]{"消费单号", "日期", "花费", "备注", "审核状态", "操作"};
        }

        //summary
        int sum = 0;
        for (Consumption c : result) {
            sum += c.getMoney();
        }
        summary.setText("查询结果总数：" + sum);
        summary.setForeground(Color.red);
        summary.setVisible(true);

        ConsumptionTableModel consumptionTableModel =new ConsumptionTableModel(result, columnNames);
        JTable table = new JTable(consumptionTableModel);
        table.setAutoCreateRowSorter(true);

        int[] colors = new int[result.size()];
        for (int i = 0, length = result.size(); i < length; i++) {
            colors[i] = result.get(i).getColor();
        }
        TableColumnModel tableColumnModel = table.getColumnModel();
        MyColorTableCellRenderer renderer = new MyColorTableCellRenderer();
        renderer.setColor(colors);
        for (int i = 0, length = tableColumnModel.getColumnCount(); i < length; i++) {
            TableColumn tableColumn = tableColumnModel.getColumn(i);
            tableColumn.setCellRenderer(renderer);
        }

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = ((JTable) e.getSource()).getSelectedRow();
                int column = ((JTable) e.getSource()).getSelectedColumn();
                if (e.getClickCount() > 1) {
                    if (column != 5 && column != 6) {
                        toUpdate(row);
                        return;
                    }
                }
                if (column == 5) {
                    delete(row);
                    return;
                }
                if (column == 6) {
                    censored(row);
                    return;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        dataPanel = new JScrollPane(table);
        dataPanel.setViewportView(table);

        gridX = 0; // X1
        gridY = 1; // Y0
        gridWidth = 1; // 横占一个单元格
        gridHeight = 5; // 列占两个单元格
        weightX = 1.0; // 当窗口放大时，长度随之放大
        weightY = 1.0; // 当窗口放大时，高度随之放大
        anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
        fill = GridBagConstraints.BOTH; // 当格子有剩余空间时，填充空间
        insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
        iPadX = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
        iPadY = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
        gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor,
                fill, insert, iPadX, iPadY);
        gbl.setConstraints(dataPanel, gbc);
        this.add(dataPanel);
    }

    private void refreshData() {
        this.remove(dataPanel);
        addDataPanel();
        this.validate();
        this.repaint();
    }

    private void toUpdate(int row) {
        if (!MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CONSUMPTION_W)) {
            return;
        }
        Consumption selectedRow = result == null ? null : result.get(row);
        if (selectedRow != null) {
            ConsumptionAdd consumptionAdd = new ConsumptionAdd(mainApp);
            consumptionAdd.setValue(selectedRow);
            mainApp.changeRightPanel(consumptionAdd);
        }
    }

    private void delete(int row) {
        if (!MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CONSUMPTION_W)) {
            return;
        }
        Consumption selectedRow = result == null ? null : result.get(row);
        if (selectedRow != null) {
            if (JOptionPane.showConfirmDialog(null, "确定要删除？") == JOptionPane.OK_OPTION) {
                if (MyFactory.getConsumptionService().delete(selectedRow.getId())) {
                    JOptionPane.showMessageDialog(null, "删除成功！");
                    refreshData();
                }
            }
        }
    }

    private void censored(int row) {
        if (!MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            return;
        }
        Consumption selectedRow = result == null ? null : result.get(row);
        if (selectedRow != null) {
            int result = JOptionPane.showConfirmDialog(null, "点击“是”审核通过，“否”审核不通过，“取消”则不作任何操作");
            if (result != JOptionPane.CANCEL_OPTION) {
                if (MyFactory.getConsumptionService().censored(selectedRow.getId(),
                        result == JOptionPane.YES_OPTION ? true : false)) {
                    JOptionPane.showMessageDialog(null, "审核成功！");
                    refreshData();;
                }
            }
        }
    }
}
