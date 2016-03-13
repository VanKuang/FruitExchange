package cn.vanchee.ui;

import cn.vanchee.service.UserService;
import cn.vanchee.util.MyFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author vanchee
 * @date 13-1-31
 * @package cn.vanchee.ui
 * @verson v1.0.0
 */
public class LoginPanel extends JPanel {

    private JLabel jlUsername = null;
    private JLabel jlPassword = null;
    private JTextField jtUsername = null;
    private JPasswordField jpPassword = null;
    private JButton btnLogin = null;

    private MainApp mainApp;

    public LoginPanel(MainApp mainApp) {
        this.mainApp = mainApp;

        jlUsername = new JLabel("用户名：");
        jlUsername.setBounds(new Rectangle(70, 69, 90, 30));
        jtUsername = new JTextField();
        jtUsername.setBounds(new Rectangle(120, 69, 100, 30));

        jlPassword = new JLabel("密   码：");
        jlPassword.setBounds(new Rectangle(70, 129, 90, 30));
        jpPassword = new JPasswordField();
        jpPassword.setBounds(new Rectangle(120, 129, 100, 30));

        btnLogin = new JButton("登陆");
        btnLogin.setBounds(new Rectangle(120, 170, 60, 30));

        jtUsername.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    login();
                }
            }
        });
        jpPassword.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    login();
                }
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        this.add(jlUsername);
        this.add(jlPassword);
        this.add(jtUsername);
        this.add(jpPassword);
        this.add(btnLogin);

        this.setLayout(null);
    }

    private void login() {
        // 收集参数
        String name = jtUsername.getText();
        String pwd = new String(jpPassword.getPassword());
        UserService userService = MyFactory.getUserService();
        boolean flag = userService.login(name, pwd);
        if (flag) {
            mainApp.initContent();
        } else {
            JOptionPane.showMessageDialog(null, "用户名或密码错误");
        }
    }
}
