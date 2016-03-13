package cn.vanchee.ui.panel;

import cn.vanchee.model.*;
import cn.vanchee.ui.MainApp;
import cn.vanchee.ui.table.ConsumptionTableModel;
import cn.vanchee.ui.table.InDetailTableModel;
import cn.vanchee.ui.table.OutDetailTableModel;
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
public class InAndOutDetail extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(InAndOutDetail.class);

    private MainApp mainApp;

    private JScrollPane outScrollPane;
    private JScrollPane inScrollPane;
    private JScrollPane consumptionScrollPane;
    private JPanel searchPanel;
    private JPanel analyzePanel;

    private JTextField jtfOwner;
    private JTextField jtfFruit;
    private JTextField showDateFrom;
    private JTextField showDateTo;

    private GridBagConstraints gbc;
    private GridBagLayout gbl;
    private int gridX, gridY, gridWidth, gridHeight, anchor, fill, iPadX, iPadY;
    private double weightX, weightY;
    private Insets insert = null;

    private List<InDetail> ins;
    private List<OutDetail> outs;
    private List<Consumption> consumptions;

    public InAndOutDetail(MainApp mainApp) {
        this.mainApp = mainApp;

        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        addSearchPanel();
        addDataPanel();
        addAnalyzePanel();
        this.setLayout(gbl);
    }

    private void refreshData() {
        this.remove(inScrollPane);
        this.remove(outScrollPane);
        this.remove(analyzePanel);
        this.remove(consumptionScrollPane);
        addDataPanel();
        addAnalyzePanel();
        this.validate();
        this.repaint();
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
        String[] owners = MyFactory.getOwnerService().listNames();
        AutoCompleteExtender autoCompleteExtender = new AutoCompleteExtender(jtfOwner, owners, null);
        autoCompleteExtender.setMatchDataAsync(true);
        autoCompleteExtender.setSizeFitComponent();
        autoCompleteExtender.setMaxVisibleRows(10);
        autoCompleteExtender.setCommitListener(new AutoCompleteExtender.CommitListener() {
            public void commit(String value) {
                jtfOwner.setText(value);
            }
        });
        searchPanel.add(jtfOwner);

        JLabel jlFruit = new JLabel("货品");
        searchPanel.add(jlFruit);

        jtfFruit = new JTextField();
        jtfFruit.setPreferredSize(inputDimension);
        jtfFruit.addKeyListener(new KeyListener() {
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
        String[] fruits = MyFactory.getFruitService().listNames();
        AutoCompleteExtender fruitAce = new AutoCompleteExtender(jtfFruit, fruits, null);
        fruitAce.setMatchDataAsync(true);
        fruitAce.setSizeFitComponent();
        fruitAce.setMaxVisibleRows(10);
        fruitAce.setCommitListener(new AutoCompleteExtender.CommitListener() {
            public void commit(String value) {
                jtfFruit.setText(value);
            }
        });
        searchPanel.add(jtfFruit);

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

        JButton reportBtn1 = new JButton("导出表1");
        reportBtn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                report(1);
            }
        });
        searchPanel.add(reportBtn1);

        JButton reportBtn2 = new JButton("导出表2");
        reportBtn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                report(2);
            }
        });
        searchPanel.add(reportBtn2);

        JButton reportBtn3 = new JButton("导出表3");
        reportBtn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                report(3);
            }
        });
        searchPanel.add(reportBtn3);


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

    private void addAnalyzePanel() {
        analyzePanel = new JPanel();
        analyzePanel.setBorder(new TitledBorder("分析"));
        analyzePanel.setLayout(new BoxLayout(analyzePanel, BoxLayout.Y_AXIS));
        double inSumMoney = 0;
        double outSumMoney = 0;
        double consumptionSumMoney = 0;
        double paidMoney = 0;
        double discountMoney = 0;
        for (InDetail inDetail : ins) {
            inSumMoney += inDetail.getMoney();
        }
        for (OutDetail outDetail : outs) {
            outSumMoney += outDetail.getMoney();
            paidMoney += outDetail.getPaidMoneyIncludeDiscount();
            discountMoney += outDetail.getPaidMoneyDiscount();
        }
        for (Consumption consumption : consumptions) {
            consumptionSumMoney += consumption.getMoney();
        }

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel inSum = new JLabel("进货总额：" + inSumMoney + "元");
        JLabel outSum = new JLabel("|  销售总额：" + outSumMoney + "元");
        JLabel consumption = new JLabel("|  其他花费总额：" + consumptionSumMoney + "元");
        JLabel paidSum = new JLabel("|  还款总额：" + outSumMoney + "元");
        JLabel discountSum = new JLabel("|  折扣总额：" + discountMoney + "元");
        p1.add(inSum);
        p1.add(outSum);
        p1.add(paidSum);
        p1.add(discountSum);
        p1.add(consumption);
        analyzePanel.add(p1);

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel sum1 = new JLabel("利润1(还款总额 - 进货总额)：" + (paidMoney - inSumMoney) + "元");
        JLabel sum2 = new JLabel("利润2(还款总额 - 折扣总额 - 进货总额)：" + (paidMoney - discountMoney - inSumMoney) + "元");
        JLabel sum3 = new JLabel("|  利润3(销售总额 - 进货总额)：" + (outSumMoney - inSumMoney) + "元");
        p2.add(sum1);
        p2.add(sum2);
        p2.add(sum3);
        analyzePanel.add(p2);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel sum4 = new JLabel("利润4(还款总额 - 进货总额 - 其他花费)：" + (paidMoney - inSumMoney - consumptionSumMoney) + "元");
        JLabel sum5 = new JLabel("|  利润5(销售总额 - 进货总额 - 其他花费)：" + (outSumMoney - inSumMoney - consumptionSumMoney) + "元");
        p3.add(sum4);
        p3.add(sum5);
        analyzePanel.add(p3);

        JPanel p4 = new JPanel();
        p4.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel sum6 = new JLabel("利润4(还款总额 - 折扣总额 - 进货总额 - 其他花费)：" + (paidMoney - discountMoney - inSumMoney - consumptionSumMoney) + "元");
        JLabel sum7 = new JLabel("|  利润5(销售总额 - 折扣总额 - 进货总额 - 其他花费)：" + (outSumMoney - discountMoney - inSumMoney - consumptionSumMoney) + "元");
        p4.add(sum6);
        p4.add(sum7);
        analyzePanel.add(p4);

        gridX = 0; // X0
        gridY = 1; // Y0
        gridWidth = 1; // 横占一个单元格
        gridHeight = 2; // 列占三个单元格
        weightX = 1.0; // 当窗口放大时，长度随之放大
        weightY = 0.0; // 当窗口放大时，高度随之放大
        anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
        fill = GridBagConstraints.BOTH; // 当有剩余空间时，填充空间
        insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
        iPadX = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
        iPadY = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
        gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor,
                fill, insert, iPadX, iPadY);
        gbl.setConstraints(analyzePanel, gbc);
        this.add(analyzePanel);
    }

    private void addDataPanel() {
        String name = jtfOwner.getText();
        int oid = MyFactory.getOwnerService().getIdByName4Query(name);
        int fid = MyFactory.getFruitService().getIdByName4Query(jtfFruit.getText());

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

        // if don't have censored right, then just only can see not censored data and refuse data;
        ins = MyFactory.getInDetailService().queryInDetail(-1, oid, fid, -1, f, e, uid);
        if (!MyFactory.getResourceService().hasRight(user, Resource.CENSORED)) {
            ins = MyFactory.getInDetailService().selectCensoredReverse(ins, Constants.CENSORED_PASS);
        }
        String[] columnNames = new String[]{"货号", "日期", "货主", "货品", "价钱", "数量", "总价", "还款", "销售数量", "库存"};
        InDetailTableModel inDetailTableModel = new InDetailTableModel(ins, columnNames);
        JTable table = new JTable(inDetailTableModel);
        table.setAutoCreateRowSorter(true);
        int[] colors = new int[ins.size()];
        for (int i = 0, length = ins.size(); i < length; i++) {
            colors[i] = ins.get(i).getColor();
        }
        TableColumnModel tableColumnModel = table.getColumnModel();
        MyColorTableCellRenderer renderer = new MyColorTableCellRenderer();
        renderer.setColor(colors);
        for (int i = 0, length = tableColumnModel.getColumnCount(); i < length; i++) {
            TableColumn tableColumn = tableColumnModel.getColumn(i);
            tableColumn.setCellRenderer(renderer);
        }
        inScrollPane = new JScrollPane(table);
        inScrollPane.setBorder(new TitledBorder("进货明细"));
        inScrollPane.setViewportView(table);

        gridX = 0; // X1
        gridY = 4; // Y0
        gridWidth = 1; // 横占一个单元格
        gridHeight = 3; // 列占两个单元格
        weightX = 1.0; // 当窗口放大时，长度随之放大
        weightY = 1.0; // 当窗口放大时，高度随之放大
        anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
        fill = GridBagConstraints.BOTH; // 当有剩余空间时，填充空间
        insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
        iPadX = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
        iPadY = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
        gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor,
                fill, insert, iPadX, iPadY);
        gbl.setConstraints(inScrollPane, gbc);
        this.add(inScrollPane);

        outs = MyFactory.getOutDetailService().queryOutDetail(-1, -1, oid, -1, fid, -1, f, e, -1, uid);
        // if don't have censored right, then just only can see not censored data and refuse data;
        if (!MyFactory.getResourceService().hasRight(user, Resource.CENSORED)) {
            outs = MyFactory.getOutDetailService().selectCensoredReverse(outs, Constants.CENSORED_PASS);
        }
        String[] outColumn = new String[]{"销售单号", "进货货号", "日期", "货主",
                "买家", "货品", "价钱", "数量", "应还款", "实际还款", "折扣", "还款状态", "审核状态"};
        OutDetailTableModel outDetailTableModel = new OutDetailTableModel(outs, outColumn);
        JTable outTable = new JTable(outDetailTableModel);
        outTable.setAutoCreateRowSorter(true);
        int[] colors2 = new int[outs.size()];
        for (int i = 0, length = outs.size(); i < length; i++) {
            colors2[i] = outs.get(i).getColor();
        }
        TableColumnModel tableColumnModel2 = outTable.getColumnModel();
        MyColorTableCellRenderer renderer2 = new MyColorTableCellRenderer();
        renderer2.setColor(colors2);
        for (int i = 0, length = tableColumnModel2.getColumnCount(); i < length; i++) {
            TableColumn tableColumn = tableColumnModel2.getColumn(i);
            tableColumn.setCellRenderer(renderer2);
        }
        outScrollPane = new JScrollPane(outTable);
        outScrollPane.setBorder(new TitledBorder("销售明细"));
        outScrollPane.setViewportView(outTable);

        gridX = 0; // X1
        gridY = 8; // Y0
        gridWidth = 1; // 横占一个单元格
        gridHeight = 3; // 列占两个单元格
        weightX = 1.0; // 当窗口放大时，长度随之放大
        weightY = 1.0; // 当窗口放大时，高度随之放大
        anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
        fill = GridBagConstraints.BOTH; // 当有剩余空间时，填充空间
        insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
        iPadX = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
        iPadY = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
        gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor,
                fill, insert, iPadX, iPadY);
        gbl.setConstraints(outScrollPane, gbc);
        this.add(outScrollPane);

        consumptions = MyFactory.getConsumptionService().queryConsumption(f, e, uid);
        String[] consumptionColumn = new String[]{"消费单号", "日期", "花费", "备注"};
        ;
        ConsumptionTableModel consumptionTableModel = new ConsumptionTableModel(consumptions, consumptionColumn);
        JTable consumptionTable = new JTable(consumptionTableModel);
        consumptionTable.setAutoCreateRowSorter(true);
        int[] colors3 = new int[consumptions.size()];
        for (int i = 0, length = consumptions.size(); i < length; i++) {
            colors3[i] = consumptions.get(i).getColor();
        }
        TableColumnModel tableColumnModel3 = consumptionTable.getColumnModel();
        MyColorTableCellRenderer renderer3 = new MyColorTableCellRenderer();
        renderer3.setColor(colors3);
        for (int i = 0, length = tableColumnModel3.getColumnCount(); i < length; i++) {
            TableColumn tableColumn = tableColumnModel3.getColumn(i);
            tableColumn.setCellRenderer(renderer3);
        }
        consumptionScrollPane = new JScrollPane(consumptionTable);
        consumptionScrollPane.setBorder(new TitledBorder("其他费用"));
        consumptionScrollPane.setViewportView(consumptionTable);

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
        gbl.setConstraints(consumptionScrollPane, gbc);
        this.add(consumptionScrollPane);
    }

    private void report(int type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
        String fileName = null;
        switch (type) {
            case 1:
                String fileName1 = "进货明细" + sdf.format(new Date()) + ".xls";
                fileName = fileName1;
                String header1[] = {"货号", "日期", "货主", "货品", "价钱", "数量", "总价", "还款", "销售数量", "库存"};
                try {
                    ExcelUtils.reportInDetails(fileName1, "进货明细", header1, ins);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "导出失败，请重试");
                }
                break;
            case 2:
                String fileName2 = "销售明细" + sdf.format(new Date()) + ".xls";
                fileName = fileName2;
                String header2[] = {"销售单号", "进货货号", "日期", "货主",
                        "买家", "货品", "价钱", "数量", "应还款", "实际还款", "折扣", "还款状态", "审核状态"};
                try {
                    ExcelUtils.reportOutDetails(fileName2, "销售明细", header2, outs);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "导出失败，请重试");
                }
                break;
            case 3:
                String fileName3 = "其他费用" + sdf.format(new Date()) + ".xls";
                fileName = fileName3;
                String header3[] = {"消费单号", "日期", "花费", "备注"};
                try {
                    ExcelUtils.reportConsumptions(fileName3, "其他消费", header3, consumptions);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "导出失败，请重试");
                }
                break;
            default:
                break;
        }

        if (fileName != null && JOptionPane.showConfirmDialog(null, "导出成功，请在report文件夹下查看，文件名是：" + fileName + "，打开该文件？")
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
