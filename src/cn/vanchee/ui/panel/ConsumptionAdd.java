package cn.vanchee.ui.panel;

import cn.vanchee.model.Consumption;
import cn.vanchee.ui.MainApp;
import cn.vanchee.util.Constants;
import cn.vanchee.util.DateChooser;
import cn.vanchee.util.DigitalTextField;
import cn.vanchee.util.MyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author vanchee
 * @date 13-1-31
 * @package cn.vanchee.ui
 * @verson v1.0.0
 */
public class ConsumptionAdd extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(ConsumptionAdd.class);

    private MainApp mainApp;

    private JTextField date_jtf;
    private DigitalTextField money_jtf;
    private JTextArea desc_jta;
    private JTextField color;

    private int cid = -1;
    private int uid = -1;

    public ConsumptionAdd(final MainApp mainApp) {

        this.mainApp = mainApp;

        Dimension inputDimension = Constants.getInputDimension();
        Dimension dateDimension = Constants.getDateDimension();

        this.setLayout(new FlowLayout());

        JPanel p1 = new JPanel();
        JLabel date = new JLabel("日期：");
        p1.add(date);
        DateChooser dateChooserFrom = DateChooser.getInstance("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_jtf = new JTextField(sdf.format(new Date()));
        date_jtf.setPreferredSize(dateDimension);
        dateChooserFrom.register(date_jtf);
        p1.add(date_jtf);
        p1.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p1);

        JPanel p2 = new JPanel();
        JLabel price = new JLabel("花费：");
        p2.add(price);
        money_jtf = new DigitalTextField();
        money_jtf.setPreferredSize(inputDimension);
        p2.add(money_jtf);

        JLabel jbl_color = new JLabel("颜色：");
        p2.add(jbl_color);
        color = new JTextField();
        color.setEnabled(false);
        color.setBackground(Color.WHITE);
        color.setPreferredSize(inputDimension);
        color.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((JTextField) e.getSource()).setBackground(chooseColor((JTextField) e.getSource()));
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
        p2.add(color);
        p2.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p2);

        JPanel p3 = new JPanel();
        JLabel desc = new JLabel("备注：");
        p3.add(desc);
        desc_jta = new JTextArea();
        desc_jta.setColumns(45);
        desc_jta.setRows(5);
        JScrollPane jScrollPane = new JScrollPane(desc_jta);
        jScrollPane.setViewportView(desc_jta);
        p3.add(jScrollPane);
        this.add(p3);

        JPanel p4 = new JPanel();
        JButton save = new JButton("保存");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        p4.add(save);
        JButton reset = new JButton("重置");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        p4.add(reset);
        JButton back = new JButton("返回");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.back();
            }
        });
        p4.add(back);
        p4.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p4);
    }

    private void save() {
        String date = date_jtf.getText();
        if ("".equals(date)) {
            JOptionPane.showMessageDialog(null, "日期必须填");
            return;
        }
        String price = money_jtf.getText();
        if ("".equals(price)) {
            JOptionPane.showMessageDialog(null, "花费必须填");
            return;
        }
        String desc = desc_jta.getText();

        Consumption consumption = new Consumption();
        consumption.setCreateAt(date);
        try {
            consumption.setMoney(Double.parseDouble(price));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "花费必须是数字");
            return;
        }
        consumption.setDesc(desc);
        consumption.setColor(color.getBackground().getRGB());

        boolean flag = false;
        if (cid == -1) {
            flag = MyFactory.getConsumptionService().create(consumption);
        } else {
            consumption.setId(cid);
            consumption.setUid(uid);
            flag = MyFactory.getConsumptionService().update(consumption);
        }

        if (flag) {
            if (cid != -1) {
                JOptionPane.showMessageDialog(null, "修改成功");
            } else {
                int result = JOptionPane.showConfirmDialog(null, "增加成功，再增加一条？");
                if (result == JOptionPane.YES_OPTION) {
                    reset();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "增加失败，请重试！");
        }
    }

    private void reset() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_jtf.setText(sdf.format(new Date()));
        money_jtf.setText("");
    }

    public void setValue(Consumption consumption) {
        cid = consumption.getId();
        uid = consumption.getUid();
        money_jtf.setText(consumption.getMoney() + "");
        desc_jta.setText(consumption.getDesc());
        color.setBackground(new Color(consumption.getColor()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_jtf.setText(sdf.format(consumption.getCreateAt()));
    }

    private Color chooseColor(Component comp) {
        Color rsltColor = JColorChooser.showDialog(this,
                "颜色选择",
                comp.getBackground());
        return rsltColor;
    }
}
