package cn.vanchee.ui.panel;

import cn.vanchee.model.OutDetail;
import cn.vanchee.model.PaidVo;
import cn.vanchee.model.Resource;
import cn.vanchee.model.User;
import cn.vanchee.ui.MainApp;
import cn.vanchee.ui.table.MyPaidReportTableModel;
import cn.vanchee.ui.table.OutDetailTableModel;
import cn.vanchee.ui.table.PaidReportTableModel;
import cn.vanchee.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author vanchee
 * @date 13-1-31
 * @package cn.vanchee.ui
 * @verson v1.0.0
 */
public class PaidReport extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(PaidReport.class);

    private MainApp mainApp;

    private JTextField showDateFrom;
    private JTextField showDateTo;
    private JTextField jtfConsumer;
    private JComboBox jcbStatus;

    private JPanel searchPanel;
    private JScrollPane allDataPanel;
    private JScrollPane detailDataPanel;

    private GridBagConstraints gbc;
    private GridBagLayout gbl;
    private int gridX, gridY, gridWidth, gridHeight, anchor, fill, iPadX, iPadY;
    private double weightX, weightY;
    private Insets insert = null;

    private List<PaidVo> paidVos;
    private List<OutDetail> outDetails;

    public PaidReport(MainApp mainApp) {
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

        JLabel jlConsumer = new JLabel("买家");
        searchPanel.add(jlConsumer);

        jtfConsumer = new JTextField();
        jtfConsumer.setPreferredSize(inputDimension);
        jtfConsumer.addKeyListener(new KeyListener() {
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
        AutoCompleteExtender consumerAce = new AutoCompleteExtender(jtfConsumer, consumers, null);
        consumerAce.setMatchDataAsync(true);
        consumerAce.setSizeFitComponent();
        consumerAce.setMaxVisibleRows(10);
        consumerAce.setCommitListener(new AutoCompleteExtender.CommitListener() {
            public void commit(String value) {
                jtfConsumer.setText(value);
            }
        });
        searchPanel.add(jtfConsumer);

        JLabel jlStatus = new JLabel("还款状态");
        searchPanel.add(jlStatus);
        String status[] = new String[]{"全部", "未还钱", "未还清", "还清"};
        jcbStatus = new JComboBox(status);
        searchPanel.add(jcbStatus);

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

        if (MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.PAID_EXPORT)) {
            JButton report1 = new JButton("导出总表");
            report1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    report1();
                }
            });
            searchPanel.add(report1);

            JButton report2 = new JButton("导出详细");
            report2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    report2();
                }
            });
            searchPanel.add(report2);
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
        int cid = MyFactory.getConsumerService().getIdByName4Query(jtfConsumer.getText());
        int status = jcbStatus.getSelectedIndex() - 1;
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

        User user = MyFactory.getUserService().getCurrentUser();
        int uid = -1;
        if (!MyFactory.getResourceService()
                .hasRight(MyFactory.getUserService().getCurrentUser(), Resource.GET_OTHERS_DATA)) {
            uid = user.getId();
        }

        paidVos = MyFactory.getOutDetailService().getUnPaidOutDetail(cid, f, e, uid);
        String[] columnNames = new String[]{"买家", "应还款", "实际还款", "欠款"};
        PaidReportTableModel paidReportTableModel =new PaidReportTableModel(paidVos, columnNames);
        JTable table = new JTable(paidReportTableModel);
        table.setAutoCreateRowSorter(true);
        int[] colors = new int[paidVos.size()];
        for (int i = 0, length = paidVos.size(); i < length; i++) {
            colors[i] = paidVos.get(i).getColor();
        }
        TableColumnModel tableColumnModel = table.getColumnModel();
        MyColorTableCellRenderer renderer = new MyColorTableCellRenderer();
        renderer.setColor(colors);
        for (int i = 0, length = tableColumnModel.getColumnCount(); i < length; i++) {
            TableColumn tableColumn = tableColumnModel.getColumn(i);
            tableColumn.setCellRenderer(renderer);
        }

        allDataPanel = new JScrollPane(table);
        allDataPanel.setBorder(new TitledBorder("欠款总表"));
        allDataPanel.setViewportView(table);

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
        gbl.setConstraints(allDataPanel, gbc);
        this.add(allDataPanel);


        String[] columnName = new String[]{"销售单号", "货号", "日期", "货主",
                "买家", "货品", "价钱", "数量", "应还款", "实际还款", "折扣", "还款状态", "审核状态", "审核"};
        if (jtfConsumer.getText() != null && !"".equals(jtfConsumer.getText())) {
            outDetails = MyFactory.getOutDetailService().queryOutDetail(-1, -1, -1, cid, -1, -1, f, e, status, uid);
        } else {
            outDetails = Collections.EMPTY_LIST;
        }

        OutDetailTableModel outDetailTableModel = new OutDetailTableModel(outDetails, columnName);
        final JTable table1 = new JTable(outDetailTableModel);
        table1.setAutoCreateRowSorter(true);

        if (jtfConsumer.getText() != null && !"".equals(jtfConsumer.getText())) {
            int[] colors1 = new int[outDetails.size()];
            for (int i = 0, length = outDetails.size(); i < length; i++) {
                colors1[i] = outDetails.get(i).getColor();
            }
            renderer.setColor(colors1);
            tableColumnModel = table1.getColumnModel();
            for (int i = 0, length = tableColumnModel.getColumnCount(); i < length; i++) {
                TableColumn tableColumn = tableColumnModel.getColumn(i);
                tableColumn.setCellRenderer(renderer);
            }
        }

        detailDataPanel = new JScrollPane(table1);
        detailDataPanel.setBorder(new TitledBorder("个人销售详细"));
        detailDataPanel.setViewportView(table1);

        gridX = 0; // X1
        gridY = 12; // Y0
        gridWidth = 1; // 横占一个单元格
        gridHeight = 2; // 列占两个单元格
        weightX = 1.0; // 当窗口放大时，长度随之放大
        weightY = 1.0; // 当窗口放大时，高度随之放大
        anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
        fill = GridBagConstraints.BOTH; // 当有剩余空间时，填充空间
        insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
        iPadX = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
        iPadY = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
        gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor,
                fill, insert, iPadX, iPadY);
        gbl.setConstraints(detailDataPanel, gbc);
        this.add(detailDataPanel);
    }

    private void refreshData() {
        this.remove(allDataPanel);
        if (detailDataPanel != null) {
            this.remove(detailDataPanel);
        }
        addDataPanel();
        this.validate();
        this.repaint();
    }

    private void report1() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
        String fileName = "他人欠款" + sdf.format(new Date()) + ".xls";
        String header[] = {"买家", "应还款", "实际还款", "欠款"};
        try {
            ExcelUtils.writePaid(fileName, header, paidVos);
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

    private void report2() {

        if (jtfConsumer.getText() == null || "".equals(jtfConsumer.getText())) {
            JOptionPane.showMessageDialog(null, "请先选择买家");
            return;
        }

        int cid = MyFactory.getConsumerService().getIdByName4Query(jtfConsumer.getText());
        int status = jcbStatus.getSelectedIndex() - 1;

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

        User user = MyFactory.getUserService().getCurrentUser();
        int uid = -1;
        if (!MyFactory.getResourceService()
                .hasRight(MyFactory.getUserService().getCurrentUser(), Resource.GET_OTHERS_DATA)) {
            uid = user.getId();
        }

        List<OutDetail> list =
                MyFactory.getOutDetailService().queryOutDetail(-1, -1, -1, cid, -1, -1, f, e, status, uid);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = jtfConsumer.getText() + "的欠款_" + sdf.format(new Date()) + ".xls";
        String header[] = {"销售单号", "货号", "日期", "货主",
                "买家", "货品", "价钱", "数量", "应还款", "实际还款", "折扣", "还款状态", "审核状态"};
        try {
            ExcelUtils.writePaidDetail(fileName, header, list);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "导出失败，请重试");
        }
        if (JOptionPane.showConfirmDialog(null, "导出成功，请在report文件夹下查看，文件名是：" + fileName + "，打开该文件？")
                == JOptionPane.OK_OPTION) {
            File file = new File(ExcelUtils.getReportPath() + fileName);
            if (file.exists()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.open(file);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "打开失败，请手动打开");
                    log.error("out put error:" + ex);
                }
            }
        }
    }
}
