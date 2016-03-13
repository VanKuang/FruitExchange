package cn.vanchee.ui;

import cn.vanchee.util.BackupUtils;
import cn.vanchee.util.MyFactory;
import cn.vanchee.util.UIUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author vanchee
 * @date 13-1-31
 * @package cn.vanchee.ui
 * @verson v1.0.0
 */
public class MainApp {

    private JFrame jFrame;
    private JPanel content;
    private JPanel rightPanel;
    private GridBagConstraints gbc;
    private GridBagLayout gbl;
    private int gridX, gridY, gridWidth, gridHeight, anchor, fill, iPadX, iPadY;
    private double weightX, weightY;
    private Insets insert = null;

    private JPanel tempPanel;

    public static boolean debug = false;

    public MainApp() {

    }

    private JFrame getJFrame() {
        if (jFrame == null) {
            UIUtils.initGlobalFontSetting("微软雅黑", Font.PLAIN, 13);

            jFrame = new JFrame();
            jFrame.setTitle("FruitJXC");
            if (!debug) {
                jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                jFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        if (JOptionPane.showConfirmDialog(null, "确定要退出？") == JOptionPane.OK_OPTION) {
                            System.exit(0);
                        }
                    }
                });
            } else {
                jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            }

            gbl = new GridBagLayout();
            gbc = new GridBagConstraints();

            if (debug) {
                initContent();
            } else {
                jFrame.setSize(300, 300);
                jFrame.setResizable(false);
                jFrame.add(getLoginPanel(), BorderLayout.CENTER);
            }


//        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
//        jFrame.setLocation((d.width - jFrame.getSize().width) / 2, (d.height - jFrame.getSize().height) / 2);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            jFrame.setLocation((d.width - jFrame.getSize().width) / 2, (d.height - jFrame.getSize().height) / 2);
        }
        return jFrame;
    }

    public void initContent() {
        jFrame.setTitle("FruitJXC,欢迎：" + MyFactory.getUserService().getCurrentUserName());
        jFrame.getContentPane().removeAll();

        jFrame.setSize(1000, 600);
        jFrame.setResizable(true);
        content = new JPanel();
        content.setLayout(gbl);
        addLeftPanel();
        addRightPanel(getRightPanel4Test());
        jFrame.add(content);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setLocation((d.width - jFrame.getSize().width) / 2, (d.height - jFrame.getSize().height) / 2);

        jFrame.getContentPane().validate();
        jFrame.getContentPane().repaint();

        BackupUtils.backupDaily();
    }

    public void changeRightPanel(JPanel panel) {
        content.remove(rightPanel);
        addRightPanel(panel);
        content.validate();
        content.repaint();
    }

    private void addLeftPanel() {
        JGroupPanel jGroupPanel = new JGroupPanel(this);
        jGroupPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        gridX = 0; // X0
        gridY = 0; // Y0
        gridWidth = 1; // 横占一个单元格
        gridHeight = 3; // 列占三个单元格
        weightX = 0.1; // 当窗口放大时，长度随之放大
        weightY = 1.0; // 当窗口放大时，高度随之放大
        anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
        fill = GridBagConstraints.BOTH; // 当有剩余空间时，填充空间
        insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
        iPadX = 75; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
        iPadY = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
        gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor,
                fill, insert, iPadX, iPadY);
        gbl.setConstraints(jGroupPanel, gbc);

        content.add(jGroupPanel);
    }

    public void back() {
        changeRightPanel(tempPanel);
    }

    private void addRightPanel(JPanel panel) {
        tempPanel = rightPanel;

        rightPanel = panel;
        gridX = 1; // X1
        gridY = 0; // Y0
        gridWidth = 2; // 横占一个单元格
        gridHeight = 3; // 列占两个单元格
        weightX = 1.0; // 当窗口放大时，长度随之放大
        weightY = 1.0; // 当窗口放大时，高度随之放大
        anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
        fill = GridBagConstraints.BOTH; // 当格子有剩余空间时，填充空间
        insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
        iPadX = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
        iPadY = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
        gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor,
                fill, insert, iPadX, iPadY);
        gbl.setConstraints(rightPanel, gbc);
        content.add(rightPanel);
    }

    public JPanel getRightPanel4Test() {
        JPanel jPanel = new JPanel();
        JLabel jLabel = new JLabel("欢迎使用");
        jPanel.add(jLabel);
        return jPanel;
    }

    private JPanel getLoginPanel() {
        LoginPanel loginPanel = new LoginPanel(this);
        return loginPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    String windowsClassName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
                    UIManager.setLookAndFeel((LookAndFeel) Class.forName(
                            windowsClassName).newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MainApp application = new MainApp();
                application.getJFrame().setVisible(true);
            }
        });
    }

}

