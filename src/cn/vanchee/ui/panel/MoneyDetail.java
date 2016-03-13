package cn.vanchee.ui.panel;

import cn.vanchee.model.Consumption;
import cn.vanchee.model.PaidDetail;
import cn.vanchee.model.Resource;
import cn.vanchee.model.User;
import cn.vanchee.ui.MainApp;
import cn.vanchee.ui.table.ConsumptionTableModel;
import cn.vanchee.ui.table.PaidTableModel;
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
public class MoneyDetail extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(MoneyDetail.class);

    private MainApp mainApp;

    private JScrollPane outScrollPane;
    private JScrollPane inScrollPane;
    private JScrollPane consumptionScrollPane;
    private JPanel searchPanel;

    private JTextField jtfOwner;
    private JTextField jtfFruit;
    private JTextField showDateFrom;
    private JTextField showDateTo;

    private GridBagConstraints gbc;
    private GridBagLayout gbl;
    private int gridX, gridY, gridWidth, gridHeight, anchor, fill, iPadX, iPadY;
    private double weightX, weightY;
    private Insets insert = null;

    private List<PaidDetail> myPaid;
    private List<PaidDetail> otherPaid;
    private List<Consumption> consumptions;

    public MoneyDetail(MainApp mainApp) {
        this.mainApp = mainApp;

        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        addSearchPanel();
        addDataPanel();
        this.setLayout(gbl);
    }

    private void refreshData() {
        this.remove(inScrollPane);
        this.remove(outScrollPane);
        this.remove(consumptionScrollPane);
        addDataPanel();
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
        String name = jtfOwner.getText();
        int oid = MyFactory.getOwnerService().getIdByName4Query(name);
        int fid = MyFactory.getFruitService().getIdByName4Query(jtfFruit.getText());

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

        myPaid = MyFactory.getPaidDetailService().queryMyPaidDetail(-1, -1, oid, fid, -1, f, e, uid);
        if (!MyFactory.getResourceService().hasRight(user, Resource.CENSORED)) {
            myPaid = MyFactory.getPaidDetailService().selectCensoredReverse(myPaid, Constants.CENSORED_PASS);
        }
        String[] columnNames = new String[]{"还款单号", "货号", "货主", "货品", "还款", "还款日期"};
        PaidTableModel paidTableModel = new PaidTableModel(myPaid, columnNames);
        JTable table = new JTable(paidTableModel);
        table.setAutoCreateRowSorter(true);
        TableColumnModel tableColumnModel = table.getColumnModel();
        MyTableCellRenderer renderer = new MyTableCellRenderer();
        for (int i = 0, length = tableColumnModel.getColumnCount(); i < length; i++) {
            TableColumn tableColumn = tableColumnModel.getColumn(i);
            tableColumn.setCellRenderer(renderer);
        }
        inScrollPane = new JScrollPane(table);
        inScrollPane.setBorder(new TitledBorder("我还款明细"));
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


        otherPaid = MyFactory.getPaidDetailService().queryPaidDetail(-1, -1, -1, oid, fid, -1, f, e, uid);
        String[] outColumn = new String[]{"还款单号", "销售单号", "货号", "货品", "买家", "还款", "折扣", "还款日期"};
        PaidTableModel outDetailTableModel = new PaidTableModel(otherPaid, outColumn);
        JTable outTable = new JTable(outDetailTableModel);
        outTable.setAutoCreateRowSorter(true);
        outTable.setAutoCreateRowSorter(true);
        tableColumnModel = outTable.getColumnModel();
        for (int i = 0, length = tableColumnModel.getColumnCount(); i < length; i++) {
            TableColumn tableColumn = tableColumnModel.getColumn(i);
            tableColumn.setCellRenderer(renderer);
        }
        outScrollPane = new JScrollPane(outTable);
        outScrollPane.setBorder(new TitledBorder("他人还款明细"));
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
        consumptionScrollPane = new JScrollPane(consumptionTable);
        consumptionScrollPane.setBorder(new TitledBorder("其他费用明细"));
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
}
