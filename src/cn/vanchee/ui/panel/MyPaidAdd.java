package cn.vanchee.ui.panel;

import cn.vanchee.model.InDetail;
import cn.vanchee.model.PaidDetail;
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
public class MyPaidAdd extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(MyPaidAdd.class);

    private MainApp mainApp;

    private JTextField date_jtf;
    private DigitalTextField money_jtf;
    private JButton save;
    private JTextField color;

    private int pid = -1;
    private int iid = -1;
    private int uid = -1;
    private double oldMoney = 0;
    private boolean updated = false;

    public MyPaidAdd(final MainApp mainApp) {
        this.mainApp = mainApp;
        Dimension inputDimension = Constants.getInputDimension();

        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel price = new JLabel("还款：");
        this.add(price);
        money_jtf = new DigitalTextField();
        money_jtf.setPreferredSize(inputDimension);
        this.add(money_jtf);

        JLabel date = new JLabel("日期：");
        this.add(date);
        DateChooser dateChooserFrom = DateChooser.getInstance("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_jtf = new JTextField(sdf.format(new Date()));
        date_jtf.setPreferredSize(Constants.getDateDimension());
        dateChooserFrom.register(date_jtf);
        this.add(date_jtf);

        JLabel jbl_color = new JLabel("颜色：");
        this.add(jbl_color);
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
        this.add(color);

        save = new JButton("保存");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        this.add(save);
        JButton back = new JButton("返回");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.back();
            }
        });
        this.add(back);
    }

    private Color chooseColor(Component comp) {
        Color rsltColor = JColorChooser.showDialog(this,
                "颜色选择",
                comp.getBackground());
        return rsltColor;
    }

    private void save() {
        String money = money_jtf.getText();
        if ("".equals(money)) {
            JOptionPane.showMessageDialog(null, "还款必须填");
            return;
        }
        String date = date_jtf.getText();
        if ("".equals(date)) {
            JOptionPane.showMessageDialog(null, "日期必须填");
            return;
        }
        PaidDetail myPaid = new PaidDetail();
        myPaid.setOid(iid);
        myPaid.setColor(color.getBackground().getRGB());

        try {
            myPaid.setMoney(Double.parseDouble(money));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "还款必须是数字");
            return;
        }
        myPaid.setCreateAt(date);

        //检查是否已经超过了应还款
        InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
        if (inDetail.getMoney() - inDetail.getPaidMoney() < (myPaid.getMoney() - (updated ? oldMoney : 0))) {
            int result = JOptionPane.showConfirmDialog(null, "实际应还款："
                    + (inDetail.getMoney() - inDetail.getPaidMoney())
                    + "元，但是现在你现在增加的还款是："
                    + (myPaid.getMoney() - (updated ? oldMoney : 0))
                    + "元，确定要增加？");

            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }
        boolean flag = false;
        if (pid == -1) {
            flag = MyFactory.getPaidDetailService().create(myPaid);
        } else {
            myPaid.setId(pid);
            myPaid.setUid(uid);
            flag = MyFactory.getPaidDetailService().update(myPaid);
        }

        if (flag) {
            if (pid != -1) {
                JOptionPane.showMessageDialog(null, "修改成功");
            } else {
                int r = JOptionPane.showConfirmDialog(null, "增加成功，再增加一条？");
                if (r == JOptionPane.YES_OPTION) {
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
        color.setBackground(Color.WHITE);
    }

    public void create(int oid) {
        this.iid = oid;
    }

    public void update(PaidDetail myPaid) {
        this.iid = myPaid.getIid();
        pid = myPaid.getId();
        uid = myPaid.getUid();
        oldMoney = myPaid.getMoney();
        updated = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_jtf.setText(sdf.format(myPaid.getCreateAt()));
        money_jtf.setText(myPaid.getMoney() + "");
        color.setBackground(new Color(myPaid.getColor()));
        save.setText("修改");
    }
}
