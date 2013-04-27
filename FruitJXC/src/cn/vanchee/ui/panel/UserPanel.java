package cn.vanchee.ui.panel;

import cn.vanchee.model.Resource;
import cn.vanchee.model.User;
import cn.vanchee.service.ResourceService;
import cn.vanchee.ui.MainApp;
import cn.vanchee.ui.table.UserTableModel;
import cn.vanchee.util.Constants;
import cn.vanchee.util.MyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vanchee
 * @date 13-1-31
 * @package cn.vanchee.ui
 * @verson v1.0.0
 */
public class UserPanel extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(UserPanel.class);

    private MainApp mainApp;

    private int oid = -1;
    private JTextField username_jtf;
    private JTextField password_jtf;

    private List<User> result;

    private JPanel resourcePanel;
    private JScrollPane dataPanel;

    public UserPanel(final MainApp mainApp) {
        this.mainApp = mainApp;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Dimension inputDimension = Constants.getInputDimension();

        JPanel addPanel = new JPanel();
        addPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        addPanel.setBorder(new TitledBorder("增加用户"));

        JLabel price = new JLabel("用户名：");
        addPanel.add(price);
        username_jtf = new JTextField();
        username_jtf.setPreferredSize(inputDimension);
        addPanel.add(username_jtf);

        JLabel psw = new JLabel("密码：");
        addPanel.add(psw);
        password_jtf = new JTextField();
        password_jtf.setPreferredSize(inputDimension);
        addPanel.add(password_jtf);

        if (MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.USER_MNG)) {
            JButton save = new JButton("保存");
            save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    save();
                }
            });
            addPanel.add(save);
        }

        JButton back = new JButton("返回");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.back();
            }
        });
        addPanel.add(back);

        this.add(addPanel);
        this.addResourcePanel(null);
        addDataPanel();
    }

    private void addResourcePanel(final String username) {
        resourcePanel = new JPanel();
        resourcePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        resourcePanel.setBorder(new TitledBorder("对应权限"));
        resourcePanel.setPreferredSize(new Dimension(800, 150));

        ResourceService resourceService = MyFactory.getResourceService();
        Map<Integer, String> resourceMap = resourceService.getResourceMap();

        User user = username == null ? null : MyFactory.getUserService().queryUserByName(username);

        for (Map.Entry<Integer, String> m : resourceMap.entrySet()) {
            JCheckBox cb = new JCheckBox(m.getValue());
            if (user != null) {
                if (resourceService.hasRight(user, m.getKey())) {
                    cb.setSelected(true);
                }
            }
            resourcePanel.add(cb);
        }

        if (MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.USER_MNG)) {
            JButton save = new JButton("保存");
            save.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    saveResource(username);
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

            resourcePanel.add(save);
        }

        this.add(resourcePanel, 1);
    }

    private void addDataPanel() {
        result = MyFactory.getUserService().getUserList();
        String[] columnNames = new String[]{"用户名", "密码", "删除"};
        UserTableModel userTableModel = new UserTableModel(result, columnNames);
        JTable table = new JTable(userTableModel);

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = ((JTable) e.getSource()).getSelectedRow();
                int column = ((JTable) e.getSource()).getSelectedColumn();
                if (column == 2) {
                    delete(row);
                } else {
                    User selectedRow = result.get(row);
                    setResource(selectedRow.getName());
                }
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

        dataPanel = new JScrollPane(table);
        dataPanel.setBorder(new TitledBorder("用户列表"));
        dataPanel.setViewportView(table);
        this.add(dataPanel);
    }

    private void setResource(String username) {
        this.remove(resourcePanel);
        addResourcePanel(username);
        this.validate();
        this.repaint();
    }

    private void refresh() {
        this.remove(dataPanel);
        addDataPanel();
        this.validate();
        this.repaint();
    }

    private void save() {
        if (!MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.USER_MNG)) {
            return;
        }
        String username = username_jtf.getText();
        if (username == null || "".equals(username)) {
            JOptionPane.showMessageDialog(null, "用户名不能为空");
        }
        String password = password_jtf.getText();
        if (password == null || "".equals(password)) {
            JOptionPane.showMessageDialog(null, "密码不能为空");
        }

        boolean flag = MyFactory.getUserService().create(username, password);

        if (flag) {
            JOptionPane.showMessageDialog(null, "增加成功");
            refresh();
        } else {
            JOptionPane.showMessageDialog(null, "增加失败，请重试！");
        }
    }

    private void delete(int row) {
        if (!MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.USER_MNG)) {
            return;
        }
        User selectedRow = result == null ? null : result.get(row);
        if (selectedRow != null) {
            int result = JOptionPane.showConfirmDialog(null, "确定要删除" + selectedRow.getName() + "?");
            if (result == JOptionPane.YES_OPTION) {
                boolean flag = MyFactory.getUserService().delete(selectedRow.getName());
                if (flag) {
                    JOptionPane.showMessageDialog(null, "删除成功");
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(null, "删除失败，请重试");
                }
            }
        }
    }

    private void saveResource(String username) {
        if (!MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.USER_MNG)) {
            return;
        }
        User user = MyFactory.getUserService().queryUserByName(username);

        List<String> displayNames = new ArrayList<String>();
        int count = resourcePanel.getComponentCount();
        for (int i = 0; i < count; i++) {
            if (resourcePanel.getComponent(i) instanceof JCheckBox) {
                JCheckBox cb = (JCheckBox) resourcePanel.getComponent(i);
                if (cb.isSelected()) {
                    displayNames.add(cb.getText());
                }
            }
        }

        ResourceService resourceService = MyFactory.getResourceService();
        int[] resource = new int[displayNames.size()];
        int i = 0;
        for (String name : displayNames) {
            resource[i] = resourceService.queryResourceId(name);
            i++;
        }

        user.setResource(resource);

        if (MyFactory.getUserService().update(user)) {
            JOptionPane.showMessageDialog(null, "保存成功");
        } else {
            JOptionPane.showMessageDialog(null, "保存失败，请重试");
        }
    }
}
