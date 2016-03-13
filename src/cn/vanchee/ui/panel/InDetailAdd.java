package cn.vanchee.ui.panel;

import cn.vanchee.model.InDetail;
import cn.vanchee.ui.MainApp;
import cn.vanchee.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author vanchee
 * @date 13-1-31
 * @package cn.vanchee.ui
 * @verson v1.0.0
 */
public class InDetailAdd extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(InDetailAdd.class);

    private MainApp mainApp;

    private JTextField owner_jtf;
    private JTextField fruit_jtf;
    private JTextField date_jtf;
    private DigitalTextField price_jtf;
    private DigitalTextField num_jtf;
    private JButton save;
    private JTextField color;

    private int iid = -1;
    private int uid = -1;
    private int sale = -1;

    public InDetailAdd(final MainApp mainApp) {
        this.mainApp = mainApp;

        Dimension inputDimension = Constants.getInputDimension();
        Dimension dateDimension = Constants.getDateDimension();

        this.setLayout(new FlowLayout());

        JPanel p1 = new JPanel(new FlowLayout());
        JLabel owner = new JLabel("货主：");
        p1.add(owner);
        owner_jtf = new JTextField();
        owner_jtf.setPreferredSize(inputDimension);
        String[] owners = MyFactory.getOwnerService().listNames();
        AutoCompleteExtender autoCompleteExtender = new AutoCompleteExtender(owner_jtf, owners, null);
        autoCompleteExtender.setMatchDataAsync(true);
        autoCompleteExtender.setSizeFitComponent();
        autoCompleteExtender.setMaxVisibleRows(10);
        autoCompleteExtender.setCommitListener(new AutoCompleteExtender.CommitListener() {
            public void commit(String value) {
                owner_jtf.setText(value);
            }
        });
        p1.add(owner_jtf);
        p1.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p1);

        JPanel p2 = new JPanel();
        JLabel fruit = new JLabel("货品：");
        p2.add(fruit);
        fruit_jtf = new JTextField();
        fruit_jtf.setPreferredSize(inputDimension);
        String[] fruits = MyFactory.getFruitService().listNames();
        AutoCompleteExtender fruitAce = new AutoCompleteExtender(fruit_jtf, fruits, null);
        fruitAce.setMatchDataAsync(true);
        fruitAce.setSizeFitComponent();
        fruitAce.setMaxVisibleRows(10);
        fruitAce.setCommitListener(new AutoCompleteExtender.CommitListener() {
            public void commit(String value) {
                fruit_jtf.setText(value);
            }
        });
        p2.add(fruit_jtf);
        p2.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p2);

        JPanel p3 = new JPanel();
        JLabel date = new JLabel("日期：");
        p3.add(date);
        DateChooser dateChooserFrom = DateChooser.getInstance("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_jtf = new JTextField(sdf.format(new Date()));
        date_jtf.setPreferredSize(dateDimension);
        dateChooserFrom.register(date_jtf);
        p3.add(date_jtf);
        p3.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p3);

        JPanel p4 = new JPanel();
        JLabel price = new JLabel("价格：");
        p4.add(price);
        price_jtf = new DigitalTextField();
        price_jtf.setPreferredSize(inputDimension);
        p4.add(price_jtf);
        p4.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p4);

        JPanel p5 = new JPanel();
        JLabel num = new JLabel("数量：");
        p5.add(num);
        num_jtf = new DigitalTextField();
        num_jtf.setPreferredSize(inputDimension);
        p5.add(num_jtf);
        p5.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p5);

        JPanel p6 = new JPanel();
        JLabel jbl_color = new JLabel("颜色：");
        p6.add(jbl_color);
        color = new JTextField();
        color.setEnabled(false);
        color.setBackground(Color.WHITE);
        color.setPreferredSize(inputDimension);
        color.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((JTextField) e.getSource()).setBackground(chooseColor((JTextField) e.getSource()));
            }
        });
        p6.add(color);
        p6.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p6);

        JPanel p7 = new JPanel();
        save = new JButton("保存");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        p7.add(save);
        JButton reset = new JButton("重置");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        p7.add(reset);
        JButton back = new JButton("返回");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.back();
            }
        });
        p7.add(back);
        p7.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p7);
    }

    private Color chooseColor(Component comp) {
        Color rsltColor = JColorChooser.showDialog(this,
                "颜色选择",
                comp.getBackground());
        return rsltColor;
    }

    private void save() {
        String owner = owner_jtf.getText();
        if ("".equals(owner)) {
            JOptionPane.showMessageDialog(null, "货主必须填");
            return;
        }
        String date = date_jtf.getText();
        if ("".equals(date)) {
            JOptionPane.showMessageDialog(null, "日期必须填");
            return;
        }
        String fruit = fruit_jtf.getText();
        if ("".equals(fruit)) {
            JOptionPane.showMessageDialog(null, "货品必须填");
            return;
        }
        String price = price_jtf.getText();
        if ("".equals(price)) {
            JOptionPane.showMessageDialog(null, "价格必须填");
            return;
        }
        String num = num_jtf.getText();
        if ("".equals(num)) {
            JOptionPane.showMessageDialog(null, "数量必须填");
            return;
        }
        InDetail inDetail = new InDetail();
        inDetail.setSale(0);
        inDetail.setOid(MyFactory.getOwnerService().getIdByName4Add(owner));
        inDetail.setFid(MyFactory.getFruitService().getIdByName4Add(fruit));
        inDetail.setColor(color.getBackground().getRGB());
        inDetail.setCreateAt(date);
        try {
            inDetail.setPrice(Double.parseDouble(price));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "价格必须是数字");
            return;
        }
        try {
            inDetail.setNum(Integer.parseInt(num));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "数量必须是数字");
            return;
        }

        boolean flag;
        if (iid == -1) {
            flag = MyFactory.getInDetailService().create(inDetail);
        } else {
            inDetail.setId(iid);
            inDetail.setUid(uid);
            inDetail.setSale(sale);
            flag = MyFactory.getInDetailService().update(inDetail);
        }

        if (flag) {
            if (iid != -1) {
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
        owner_jtf.setText("");
        fruit_jtf.setText("");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_jtf.setText(sdf.format(new Date()));
        price_jtf.setText("");
        num_jtf.setText("");
        color.setBackground(Color.WHITE);
        save.setText("修改");
    }

    public void setValue(InDetail inDetail) {
        iid = inDetail.getId();
        uid = inDetail.getUid();
        sale = inDetail.getSale();
        owner_jtf.setText(inDetail.getOwnerName());
        fruit_jtf.setText(inDetail.getFruitName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_jtf.setText(sdf.format(inDetail.getCreateAt()));
        price_jtf.setText(inDetail.getPrice() + "");
        num_jtf.setText(inDetail.getNum() + "");
        color.setBackground(new Color(inDetail.getColor()));
        save.setText("修改");
    }

}
