package cn.vanchee.ui.panel;

import cn.vanchee.model.PaidVo;
import cn.vanchee.model.Resource;
import cn.vanchee.model.User;
import cn.vanchee.ui.MainApp;
import cn.vanchee.ui.table.MyPaidReportTableModel;
import cn.vanchee.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
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
public class MyPaidReport extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(MyPaidReport.class);

    private MainApp mainApp;

    private JTextField showDateFrom;
    private JTextField showDateTo;
    private JTextField jtfOwner;

    private JPanel searchPanel;
    private JScrollPane dataPanel;

    private GridBagConstraints gbc;
    private GridBagLayout gbl;
    private int gridX, gridY, gridWidth, gridHeight, anchor, fill, iPadX, iPadY;
    private double weightX, weightY;
    private Insets insert = null;

    private List<PaidVo> result;

    public MyPaidReport(MainApp mainApp) {
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
        Dimension inputDimension = Constants.getInputDimension();
        Dimension dateDimension = Constants.getDateDimension();

        searchPanel = new JPanel();
        searchPanel.setBorder(new TitledBorder("查询条件"));
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel jlOwner = new JLabel("货主");
        searchPanel.add(jlOwner);

        jtfOwner = new JTextField();
        jtfOwner.setPreferredSize(inputDimension);
        jtfOwner.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    refreshData();
                }
            }
        });
        String[] consumers = MyFactory.getConsumerService().listNames();
        AutoCompleteExtender consumerAce = new AutoCompleteExtender(jtfOwner, consumers, null);
        consumerAce.setMatchDataAsync(true);
        consumerAce.setSizeFitComponent();
        consumerAce.setMaxVisibleRows(10);
        consumerAce.setCommitListener(new AutoCompleteExtender.CommitListener() {
            public void commit(String value) {
                jtfOwner.setText(value);
            }
        });
        searchPanel.add(jtfOwner);

        JLabel jlDateFrom = new JLabel("日期");
        searchPanel.add(jlDateFrom);

        DateChooser dateChooserFrom = DateChooser.getInstance("yyyy-MM-dd");
        showDateFrom = new JTextField("开始日期");
        showDateFrom.setPreferredSize(dateDimension);
        dateChooserFrom.register(showDateFrom);
        searchPanel.add(showDateFrom);

        JLabel jlDateTo = new JLabel("—");
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

        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.PAID_EXPORT)) {
            JButton report = new JButton("导出");
            report.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    report();
                }
            });
            searchPanel.add(report);
        }

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
        int cid = MyFactory.getOwnerService().getIdByName4Query(jtfOwner.getText());
        Date f = null;
        String from = showDateFrom.getText();
        if (!from.equals("开始日期") && !"".equals(from)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                f = sdf.parse(from);
            } catch (ParseException e) {
                log.error(e.getMessage());
            }
        }

        Date e = null;
        String end = showDateTo.getText();
        if (!end.equals("结束日期") && !"".equals(end)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                e = sdf.parse(end);
            } catch (ParseException e1) {
                log.error(e1.getMessage());
            }
        }

        User user = MyFactory.getCurrentUser();
        int uid = -1;
        if (!MyFactory.getResourceService()
                .hasRight(MyFactory.getCurrentUser(), Resource.GET_OTHERS_DATA)) {
            uid = user.getId();
        }

        result = MyFactory.getInDetailService().getUnPaidOutDetail(cid, f, e, uid);
        String[] columnNames = new String[]{"货主", "货品", "日期", "应还款", "实际还款", "欠款"};
        MyPaidReportTableModel myPaidReportTableModel = new MyPaidReportTableModel(result, columnNames);
        JTable table = new JTable(myPaidReportTableModel);
        table.setAutoCreateRowSorter(true);
        TableColumnModel tableColumnModel = table.getColumnModel();
        MyTableCellRenderer renderer = new MyTableCellRenderer();
        for (int i = 0, length = tableColumnModel.getColumnCount(); i < length; i++) {
            TableColumn tableColumn = tableColumnModel.getColumn(i);
            tableColumn.setCellRenderer(renderer);
        }

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

    private void report() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
        String fileName = "我的欠款" + sdf.format(new Date()) + ".xls";
        String header[] = {"货主", "货品", "日期", "应还款", "实际还款", "欠款"};
        try {
            ExcelUtils.writeMyPaid(fileName, "我的欠款", header, result);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "导出失败，请重试");
        }
        if (JOptionPane.showConfirmDialog(null, "导出成功，请在report文件夹下查看，文件名是：" + fileName + "，打开该文件？")
                == JOptionPane.OK_OPTION) {
            File file = new File(ExcelUtils.getReportPath() + fileName);
            if (file.exists()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.open(file);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "打开失败，请手动打开");
                    log.error(e.getMessage());
                }
            }
        }
    }

}
