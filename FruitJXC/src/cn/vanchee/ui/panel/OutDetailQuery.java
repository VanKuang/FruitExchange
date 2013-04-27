package cn.vanchee.ui.panel;

import cn.vanchee.model.OutDetail;
import cn.vanchee.model.Resource;
import cn.vanchee.model.User;
import cn.vanchee.ui.MainApp;
import cn.vanchee.ui.table.OutDetailTableModel;
import cn.vanchee.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
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
public class OutDetailQuery extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(OutDetailQuery.class);

    private MainApp mainApp;

    private JTextField jtfId;
    private JTextField jtfIid;
    private JTextField jtfOwner;
    private JTextField jtfConsumer;
    private JTextField jtfFruit;
    private JTextField showDateFrom;
    private JTextField showDateTo;
    private JComboBox jcbStatus;
    private JComboBox jcbCensored;

    private JPanel searchPanel;
    private JScrollPane dataPanel;

    private GridBagConstraints gbc;
    private GridBagLayout gbl;
    private int gridX, gridY, gridWidth, gridHeight, anchor, fill, iPadX, iPadY;
    private double weightX, weightY;
    private Insets insert = null;

    private boolean init = true;

    private List<OutDetail> result;

    public OutDetailQuery(MainApp mainApp) {
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

        JLabel jlId = new JLabel("销售单号");
        searchPanel.add(jlId);
        jtfId = new JTextField();
        jtfId.setPreferredSize(inputDimension);
        jtfId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!InputUtils.checkNum(e)) {
                    String value = jtfId.getText();
                    jtfId.setText(value.substring(0, value.length() - 1));
                }
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    refreshData();
                }
            }
        });
        searchPanel.add(jtfId);

        JLabel jlIid = new JLabel("货号");
        searchPanel.add(jlIid);
        jtfIid = new JTextField();
        jtfIid.setPreferredSize(inputDimension);
        jtfIid.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!InputUtils.checkNum(e)) {
                    String value = jtfIid.getText();
                    jtfIid.setText(value.substring(0, value.length() - 1));
                }
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    refreshData();
                }
            }
        });
        searchPanel.add(jtfIid);

        JLabel jlOwner = new JLabel("货主");
        searchPanel.add(jlOwner);

        jtfOwner = new JTextField();
        jtfOwner.setPreferredSize(inputDimension);
        jtfOwner.addKeyListener(new KeyAdapter() {
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


        JLabel jlStatus = new JLabel("还款状态");
        searchPanel.add(jlStatus);
        String status[] = new String[]{"全部", "未还钱", "未还清", "还清"};
        jcbStatus = new JComboBox(status);
        searchPanel.add(jcbStatus);

        JLabel jlCensored = new JLabel("审核状态");
        searchPanel.add(jlCensored);
        String censored[] = new String[]{"全部", "未审", "通过", "不通过"};
        jcbCensored = new JComboBox(censored);
        searchPanel.add(jcbCensored);

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
        weightY = 0; // 当窗口放大时，高度随之放大
        anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
        fill = GridBagConstraints.BOTH; // 当有剩余空间时，填充空间
        insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
        iPadX = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
        iPadY = 35; // 组件内部填充空间，即给组件的最小高度添加多大的空间
        gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor,
                fill, insert, iPadX, iPadY);
        gbl.setConstraints(searchPanel, gbc);
        this.add(searchPanel);
    }

    private void addDataPanel() {

        int id = -1;
        if (!init && !"".equals(jtfId.getText())) {
            try {
                id = Integer.parseInt(jtfId.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "销售单号必须是数字");
                return;
            }
        }
        int iid = -1;
        if (!init && !"".equals(jtfIid.getText())) {
            try {
                iid = Integer.parseInt(jtfIid.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "货号必须是数字");
                return;
            }
        }
        int ownerId = MyFactory.getOwnerService().getIdByName4Query(jtfOwner.getText());
        int cid = MyFactory.getConsumerService().getIdByName4Query(jtfConsumer.getText());
        int fid = MyFactory.getFruitService().getIdByName4Query(jtfFruit.getText());
        int status = init ? -1 : jcbStatus.getSelectedIndex() - 1;
        int censored = init ? -1 : jcbCensored.getSelectedIndex() - 1;

        long f = -1;
        String from = showDateFrom.getText();
        if (!from.equals("开始日期") && !"".equals(from)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = null;
            try {
                fromDate = sdf.parse(from);
            } catch (ParseException e) {
                log.error("date parse error:", e);
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
                log.error("date parse error:", e1);
            }
            e = endDate.getTime();
        }

        User user = MyFactory.getUserService().getCurrentUser();
        int uid = -1;
        if (!MyFactory.getResourceService()
                .hasRight(MyFactory.getUserService().getCurrentUser(), Resource.GET_OTHERS_DATA)) {
            uid = user.getId();
        }

        // if don't have censored right, then just only can see not censored data and refuse data;
        result = MyFactory.getOutDetailService().queryOutDetail(id, iid, ownerId, cid, fid, censored, f, e, status, uid);
        String[] columnNames = new String[]{"销售单号", "货号", "日期", "货主",
                "买家", "货品", "价钱", "数量", "应还款", "实际还款", "折扣", "还款状态", "审核状态", "审核"};
        if (!MyFactory.getResourceService().hasRight(user, Resource.CENSORED)) {
            result = MyFactory.getOutDetailService().selectCensoredReverse(result, Constants.CENSORED_PASS);
            columnNames = new String[]{"销售单号", "货号", "日期", "货主",
                    "买家", "货品", "价钱", "数量", "应还款", "实际还款", "折扣", "还款状态", "审核状态"};
        }
        OutDetailTableModel outDetailTableModel = new OutDetailTableModel(result, columnNames);
        final JTable table = new JTable(outDetailTableModel);
        table.setAutoCreateRowSorter(true);

        /*
        TableColumn moneyColumn = table.getColumn("应还款");
        DefaultTableCellRenderer moneyTableCellRenderer = new DefaultTableCellRenderer() {
            public void setValue(Object value) {
                setBackground(Color.pink);
                setHorizontalAlignment(JLabel.RIGHT);
                setText(NumberFormat.getInstance().format(Double.parseDouble(String.valueOf(value))));
            }
        };
        moneyColumn.setCellRenderer(moneyTableCellRenderer);

        TableColumn paidColumn = table.getColumn("实际还款");
        DefaultTableCellRenderer paidTableCellRenderer = new DefaultTableCellRenderer() {
            public void setValue(Object value) {
                setBackground(Color.GREEN);
                setHorizontalAlignment(JLabel.RIGHT);
                setToolTipText("点击增加还款");
                setText(NumberFormat.getInstance().format(Double.parseDouble(String.valueOf(value))));
            }
        };
        paidColumn.setCellRenderer(paidTableCellRenderer);

        TableColumn statusTableColumn = table.getColumn("状态");
        DefaultTableCellRenderer statusTableCellRenderer = new DefaultTableCellRenderer() {
            public void setValue(Object value) {
                String cellValue = value.toString();
                if ((Constants.OUT_STATUS_ORIGINAL_STR.equals(cellValue))) {
                    setBackground(Color.RED);
                } else if ((Constants.OUT_STATUS_PAID_NOT_ENOUGH_STR.equals(cellValue))) {
                    setBackground(Color.ORANGE);
                } else {
                    setBackground(Color.BLUE);
                }
                setText((value == null) ? "" : value.toString());
            }
        };
        statusTableColumn.setCellRenderer(statusTableCellRenderer);
        statusTableCellRenderer.setHorizontalAlignment(JLabel.RIGHT);

        TableColumn tc = table.getColumn("实际还款");
        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {
            public void setValue(Object value) {
                setFont(new Font("微软雅黑", Font.BOLD, 12));
                setToolTipText("点击增加还款");
                setText(NumberFormat.getInstance().format(Double.parseDouble(String.valueOf(value))));
            }
        };
        tc.setCellRenderer(tableCellRenderer);
        TableColumn tc1 = table.getColumn("审核");
        DefaultTableCellRenderer tableCellRenderer1 = new DefaultTableCellRenderer() {
            public void setValue(Object value) {
                setText("审核");
                setToolTipText("点击进行审核");
                setFont(new Font("微软雅黑", Font.BOLD, 12));
            }
        };
        tc1.setCellRenderer(tableCellRenderer1);
        */

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

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = ((JTable) e.getSource()).getSelectedRow();
                int column = ((JTable) e.getSource()).getSelectedColumn();
                if (e.getClickCount() > 1) {
                    toUpdate(row);
                    return;
                }
                if (column == 9) {
                    toPaid(row);
                    return;
                }
                if (column == 13) {
                    censored(row);
                    return;
                }
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

        init = false;
    }

    private void refreshData() {
        this.remove(dataPanel);
        addDataPanel();
        this.validate();
        this.repaint();
    }

    private void toUpdate(int row) {
        if (!MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.OUT_W)) {
            return;
        }
        OutDetail selectedRow = result == null ? null : result.get(row);
        if (selectedRow != null) {
            OutDetailAdd outDetailAdd = new OutDetailAdd(mainApp);
            outDetailAdd.setValue(selectedRow, true);
            mainApp.changeRightPanel(outDetailAdd);
        }
    }

    private void toPaid(int row) {
        if (!MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.PAID_W)) {
            return;
        }
        OutDetail selectedRow = result == null ? null : result.get(row);
        if (selectedRow != null) {
            PaidAdd paidAdd = new PaidAdd(mainApp);
            paidAdd.create(selectedRow.getId());
            PaidDetailPanel paidDetailPanel = new PaidDetailPanel(mainApp, paidAdd, selectedRow.getId());
            mainApp.changeRightPanel(paidDetailPanel);
        }
    }

    private void censored(int row) {
        if (!MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.CENSORED)) {
            return;
        }
        OutDetail selectedRow = result == null ? null : result.get(row);
        if (selectedRow != null) {
            int result = JOptionPane.showConfirmDialog(null, "点击“是”审核通过，“否”审核不通过，“取消”则不作任何操作");
            if (result != JOptionPane.CANCEL_OPTION) {
                if (MyFactory.getOutDetailService().censored(selectedRow.getId(),
                        result == JOptionPane.YES_OPTION ? true : false)) {
                    JOptionPane.showMessageDialog(null, "审核成功！");
                    refreshData();
                    ;
                }
            }
        }
    }
}
