package cn.vanchee.test;

/**
 * @author vanchee
 * @date 13-3-5
 * @package cn.vanchee.test
 * @verson v1.0.0
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//以下三个包是内嵌颜色选择器时要用到的
public class CorlorChooserTest extends JFrame
        implements ActionListener, ChangeListener {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new CorlorChooserTest();
    }

    public CorlorChooserTest() {
        biuldFrame();
    }

    private void biuldFrame() {
        JButton jbt_open = new JButton("背景颜色选择");

        //添加监听器
        jbt_open.addActionListener(this);

        //内容面板设定一个流布局管理器
        Container ctnr = this.getContentPane();
        ctnr.setLayout(new FlowLayout());

        ctnr.add(jbt_open);
        this.pack();

        //设定窗体出现在屏幕的正中
        //获取屏幕的大小先...
        Dimension scrnDim = Toolkit.getDefaultToolkit().getScreenSize();
        //计算本窗体的起始位置
        //注意不要使用this.width和this.height来获取本窗体的宽和高
        //不然计算位置不在屏幕中央，原因可能是因为pack方法的影响
        //getSize方法可能重新计算了pack后的窗体大小
        int x = (scrnDim.width - this.getSize().width) / 2;
        int y = (scrnDim.height - this.getSize().height) / 2;

        this.setLocation(x, y);
        this.setVisible(true);
        //以下设定窗体的关闭动作
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    private Color chooseColor(Component comp) {
        //颜色选择对话框的参数意义
        //参一表示其父控件，非顶级控件（窗体类）都有一个父控件
        //显示时会以父控件作为中心显示，参二是对话框的标题
        //参三是，取消选择时返回的默认颜色这里默认颜色取自
        //所传参数的背景色。
        Color rsltColor = JColorChooser.showDialog(this,
                "颜色选择",
                comp.getBackground());
        return rsltColor;
    }
//*********<<内嵌颜色选择器的示例*******************************************************

    private JColorChooser jcc = new JColorChooser();

    //对颜色选择器选择事件添加监听器
    private void anotherUsingDemo() {
        JDialog dlg = new javax.swing.JDialog();
        dlg.setTitle("内嵌颜色选择器");

        JLabel jlb_top = new JLabel("下面的颜色选择器是内嵌的");
        jlb_top.setForeground(Color.MAGENTA);
        //作为独立的颜色选择器，可以给其构造器一个初始颜色
        //这里没给。

        jcc.getSelectionModel().addChangeListener(this);

        Container c = dlg.getContentPane();
        c.add(jlb_top, BorderLayout.NORTH);
        c.add(jcc, BorderLayout.CENTER);

        dlg.pack();
        dlg.setVisible(true);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    @Override
    public void stateChanged(ChangeEvent arg0) {
        // TODO Auto-generated method stub
        //这个方法是处理颜色选择器选择事件的

        //选择的颜色
        Color selectedColor = jcc.getColor();
        //设定主窗体第三个按钮的背景色

        Component[] comps = this.getContentPane().getComponents();
        //这里使用了一个遍历所有子控件的方法
        for (int i = 0; i < comps.length; i++) {
            //判断某控件的类型是否为JButton
            //我们知道最后一个控件的索引位置
            //所以这里大胆使用，这就是没有
            //将那些控件弄为实例变量带来的麻烦！！
            if ((comps[i] instanceof JButton) &&
                    (i == comps.length - 1)) {
                comps[i].setBackground(selectedColor);
            }
        }
    }

    //****************内嵌颜色选择器的示例>>************************************************
    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        String evtCmd = arg0.getActionCommand();

        JButton jbt_bk = (JButton) arg0.getSource();
        jbt_bk.setBackground(this.chooseColor(this));
    }
}