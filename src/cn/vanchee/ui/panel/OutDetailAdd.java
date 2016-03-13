package cn.vanchee.ui.panel;

import cn.vanchee.model.InDetail;
import cn.vanchee.model.OutDetail;
import cn.vanchee.ui.MainApp;
import cn.vanchee.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author vanchee
 * @date 13-1-31
 * @package cn.vanchee.ui
 * @verson v1.0.0
 */
public class OutDetailAdd extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(OutDetailAdd.class);

    private MainApp mainApp;
    private JTextField owner_jtf;
    private JTextField consumer_jtf;
    private JTextField fruit_jtf;
    private JTextField date_jtf;
    private JTextField price_jtf;
    private JTextField num_jtf;
    private JButton save;
    private JTextField color;

    private int id = -1;
    private int iid = -1;
    private int remain = -1;

    public OutDetailAdd(final MainApp mainApp, boolean init) {
        this.mainApp = mainApp;

        Dimension inputDimension = Constants.getInputDimension();
        Dimension dateDimension = Constants.getDateDimension();

        this.setLayout(new FlowLayout());

        JPanel p1 = new JPanel(new FlowLayout());
        JLabel owner = new JLabel("货主：");
        p1.add(owner);
        owner_jtf = new JTextField();
        owner_jtf.setEnabled(false);
        owner_jtf.setPreferredSize(inputDimension);
        p1.add(owner_jtf);
        p1.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p1);

        JPanel p2 = new JPanel(new FlowLayout());
        JLabel consumer = new JLabel("买家：");
        p2.add(consumer);
        consumer_jtf = new JTextField();
        consumer_jtf.setPreferredSize(inputDimension);
        String[] consumers = MyFactory.getConsumerService().listNames();
        AutoCompleteExtender consumerAce = new AutoCompleteExtender(consumer_jtf, consumers, null);
        consumerAce.setMatchDataAsync(true);
        consumerAce.setSizeFitComponent();
        consumerAce.setMaxVisibleRows(10);
        consumerAce.setCommitListener(new AutoCompleteExtender.CommitListener() {
            public void commit(String value) {
                consumer_jtf.setText(value);
            }
        });
        p2.add(consumer_jtf);
        p2.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p2);

        JPanel p3 = new JPanel();
        JLabel fruit = new JLabel("货品：");
        p3.add(fruit);
        fruit_jtf = new JTextField();
        fruit_jtf.setEnabled(false);
        fruit_jtf.setPreferredSize(inputDimension);
        p3.add(fruit_jtf);
        p3.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p3);

        JPanel p4 = new JPanel();
        JLabel date = new JLabel("日期：");
        p4.add(date);
        DateChooser dateChooserFrom = DateChooser.getInstance("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_jtf = new JTextField(sdf.format(new Date()));
        date_jtf.setPreferredSize(dateDimension);
        dateChooserFrom.register(date_jtf);
        p4.add(date_jtf);
        p4.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p4);

        JPanel p5 = new JPanel();
        JLabel price = new JLabel("价格：");
        p5.add(price);
        price_jtf = new JTextField();
        price_jtf.setPreferredSize(inputDimension);
        price_jtf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!InputUtils.checkNum(e)) {
                    String value = price_jtf.getText();
                    price_jtf.setText(value.substring(0, value.length() - 1));
                }
            }
        });
        p5.add(price_jtf);
        p5.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p5);

        JPanel p6 = new JPanel();
        JLabel num = new JLabel("数量：");
        p6.add(num);
        num_jtf = new JTextField();
        num_jtf.setPreferredSize(inputDimension);
        num_jtf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!InputUtils.checkNum(e)) {
                    String value = num_jtf.getText();
                    num_jtf.setText(value.substring(0, value.length() - 1));
                }
            }
        });
        p6.add(num_jtf);
        p6.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p6);

        JPanel p7 = new JPanel();
        JLabel jbl_color = new JLabel("颜色：");
        p7.add(jbl_color);
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
        p7.add(color);
        p7.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p7);

        JPanel p8 = new JPanel();
        save = new JButton("保存");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        p8.add(save);
        JButton reset = new JButton("重置");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        p8.add(reset);
        JButton back = new JButton("返回");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.back();
            }
        });
        p8.add(back);
        p8.setPreferredSize(Constants.getAddPanelDimension());
        this.add(p8);
    }

    private void save() {
        String owner = owner_jtf.getText();
        if ("".equals(owner)) {
            JOptionPane.showMessageDialog(null, "货主必须填");
            return;
        }
        String consumer = consumer_jtf.getText();
        if ("".equals(consumer)) {
            JOptionPane.showMessageDialog(null, "买家必须填");
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

        int numInt = 0;
        try {
            numInt = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "数量必须是数字");
            return;
        }

        if (numInt > remain) {
            JOptionPane.showMessageDialog(null, "库存只有" + remain + ",而你现在填的销售数量是" + num);
            return;
        }

        OutDetail outDetail = new OutDetail();
        outDetail.setIid(iid);
        outDetail.setCid(MyFactory.getConsumerService().getIdByName4Add(consumer));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        outDetail.setNum(numInt);
        outDetail.setColor(color.getBackground().getRGB());
        try {
            outDetail.setDate(sdf.parse(date).getTime());
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        try {
            outDetail.setPrice(Double.parseDouble(price));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "价格必须是数字");
            return;
        }


        boolean flag = false;
        if (id == -1) {
            outDetail.setStatus(Constants.OUT_STATUS_ORIGINAL);
            flag = MyFactory.getOutDetailService().create(outDetail);
        } else {
            outDetail.setId(id);
            flag = MyFactory.getOutDetailService().update(outDetail);
        }

        if (flag) {
            if (id != -1) {
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
        consumer_jtf.setText("");
        fruit_jtf.setText("");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_jtf.setText(sdf.format(new Date()));
        price_jtf.setText("");
        num_jtf.setText("");
        color.setBackground(Color.WHITE);
    }

    public void setValue(OutDetail outDetail, boolean update) {
        iid = outDetail.getIid();
        owner_jtf.setText(outDetail.getOwnerName());
        fruit_jtf.setText(outDetail.getFruitName());
        color.setBackground(Color.WHITE);

        // need get the in detail remain
        InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
        if (inDetail != null) {
            remain = inDetail.getRemain() + (update ? outDetail.getNum() : 0);
        }

        if (update) {
            id = outDetail.getId();
            consumer_jtf.setText(outDetail.getConsumerName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date_jtf.setText(sdf.format(new Date(outDetail.getDate())));
            price_jtf.setText(outDetail.getPrice() + "");
            num_jtf.setText(outDetail.getNum() + "");
            color.setBackground(new Color(outDetail.getColor()));
            save.setText("修改");
        }
    }

    private Color chooseColor(Component comp) {
        Color rsltColor = JColorChooser.showDialog(this,
                "颜色选择",
                comp.getBackground());
        return rsltColor;
    }
}
