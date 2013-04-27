package cn.vanchee.ui.panel;

import cn.vanchee.model.InDetail;
import cn.vanchee.model.MyPaid;
import cn.vanchee.model.Resource;
import cn.vanchee.model.User;
import cn.vanchee.ui.MainApp;
import cn.vanchee.ui.table.MyPaidTableModel;
import cn.vanchee.util.MyColorTableCellRenderer;
import cn.vanchee.util.MyFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

/**
 * @author vanchee
 * @date 13-1-31
 * @package cn.vanchee.ui
 * @verson v1.0.0
 */
public class MyPaidPanel extends JPanel {

    private MainApp mainApp;

    private List<MyPaid> result;

    public MyPaidPanel(MainApp mainApp, JPanel panel, int iid) {
        InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);

        this.mainApp = mainApp;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(panel);
        panel.setBorder(new TitledBorder("增加还款"));

        JPanel sumPanel = new JPanel();
        sumPanel.setBorder(new TitledBorder("说明"));
        sumPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel shouldPaid = new JLabel("应还款：" + inDetail.getMoney());
        sumPanel.add(shouldPaid);
        JLabel actualPaid = new JLabel("实际还款：" + inDetail.getPaidMoney());
        sumPanel.add(actualPaid);
        this.add(sumPanel);

        User user = MyFactory.getUserService().getCurrentUser();
        int uid = -1;
        if (!MyFactory.getResourceService()
                .hasRight(MyFactory.getUserService().getCurrentUser(), Resource.GET_OTHERS_DATA)) {
            uid = user.getId();
        }

        result = MyFactory.getMyPaidService().queryMyPaid(-1, iid, -1, -1, -1, -1, -1, uid);
        String[] columnNames = new String[]{"还款单号", "货号", "货品", "卖家", "还款", "还款日期"};
        MyPaidTableModel paidTableModel =new MyPaidTableModel(result, columnNames);
        JTable table = new JTable(paidTableModel);
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

        JScrollPane dataPanel = new JScrollPane(table);
        dataPanel.setBorder(new TitledBorder("还款明细"));
        dataPanel.setViewportView(table);
        this.add(dataPanel);
    }
}
