package cn.vanchee.test;

import javax.swing.*;
import java.awt.*;

/**
 * Java ��GridBagLayout���ֹ�������С����
 *
 * @author �嶷�� <��ת���뱣�����ߺͳ���>
 * @blog http://blog.csdn.net/mq612
 */

public class UiTest extends JFrame {

    private static final long serialVersionUID = -2397593626990759111L;

    private JPanel pane = null;

    private JButton b_1 = null, b_2 = null, b_3 = null, b_4 = null, b_5 = null;

    private int gridx, gridy, gridwidth, gridheight, anchor, fill, ipadx, ipady;

    private double weightx, weighty;

    private Insets insert = null;

    private GridBagLayout gbl = null;

    private GridBagConstraints gbc = null;

    public UiTest() {
        super("Test");

        // ���ð�ť����
        UIManager.put("Button.font", new Font("Dialog", Font.PLAIN, 12));

        pane = new JPanel();

        // �����ť����ť��ΪX��Y���꣬��������ռ��Ԫ����
        b_1 = new JButton("X0Y0W1H3");
        b_2 = new JButton("X1Y0W1H2");
        b_3 = new JButton("X2Y1W1H1");
        b_4 = new JButton("X3Y2W1H1");
        b_5 = new JButton("X0Y3W2H1");

        gbl = new GridBagLayout();
        pane.setLayout(gbl);

        gridx = 0; // X0
        gridy = 0; // Y0
        gridwidth = 1; // ��ռһ����Ԫ��
        gridheight = 3; // ��ռ������Ԫ��
        weightx = 1.0; // �����ڷŴ�ʱ��������֮�Ŵ�
        weighty = 1.0; // �����ڷŴ�ʱ���߶���֮�Ŵ�
        anchor = GridBagConstraints.NORTH; // �����û�пռ��ʱ��ʹ������ڱ���
        fill = GridBagConstraints.BOTH; // ����ʣ��ռ�ʱ�����ռ�
        insert = new Insets(0, 0, 0, 10); // ����˴˵ļ��
        ipadx = 0; // ����ڲ����ռ䣬�����������С�����Ӷ��Ŀռ�
        ipady = 0; // ����ڲ����ռ䣬�����������С�߶���Ӷ��Ŀռ�
        gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
        gbl.setConstraints(b_1, gbc);
        pane.add(b_1);

        /**
         * ����ÿ����Ԫ��������ж��кܶ�������ǰ���Ѿ���õģ�����Ҫ���¸�ֵ����ôд��Ϊ�˸����׵Ŀ���ÿ����Ԫ���е����á�
         */

        gridx = 1; // X1
        gridy = 0; // Y0
        gridwidth = 1; // ��ռһ����Ԫ��
        gridheight = 2; // ��ռ������Ԫ��
        weightx = 1.0; // �����ڷŴ�ʱ��������֮�Ŵ�
        weighty = 1.0; // �����ڷŴ�ʱ���߶���֮�Ŵ�
        anchor = GridBagConstraints.NORTH; // �����û�пռ��ʱ��ʹ������ڱ���
        fill = GridBagConstraints.BOTH; // ��������ʣ��ռ�ʱ�����ռ�
        insert = new Insets(0, 0, 0, 10); // ����˴˵ļ��
        ipadx = 0; // ����ڲ����ռ䣬�����������С�����Ӷ��Ŀռ�
        ipady = 0; // ����ڲ����ռ䣬�����������С�߶���Ӷ��Ŀռ�
        gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
        gbl.setConstraints(b_2, gbc);
        pane.add(b_2);

        // ������һ����ʱ�������壬��������п��ܻᱻ����
        gridx = 1; // X1
        gridy = 2; // Y2
        gridwidth = 1; // ��ռһ����Ԫ��
        gridheight = 1; // ��ռһ����Ԫ��
        weightx = 0.0; // �����ڷŴ�ʱ�����Ȳ���
        weighty = 0.0; // �����ڷŴ�ʱ���߶Ȳ���
        anchor = GridBagConstraints.NORTH; // �����û�пռ��ʱ��ʹ������ڱ���
        fill = GridBagConstraints.BOTH; // ��������ʣ��ռ�ʱ�����ռ�
        insert = new Insets(0, 0, 0, 0); // ����˴˵ļ��
        ipadx = 0; // ����ڲ����ռ䣬�����������С�����Ӷ��Ŀռ�
        ipady = 0; // ����ڲ����ռ䣬�����������С�߶���Ӷ��Ŀռ�
        gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
        JPanel tempPane = new JPanel();
        gbl.setConstraints(tempPane, gbc);
        pane.add(tempPane);

        // ����һ����ʱ��������
        gridx = 2; // X2
        gridy = 0; // Y0
        gridwidth = 1; // ��ռһ����Ԫ��
        gridheight = 1; // ��ռһ����Ԫ��
        weightx = 0.0; // �����ڷŴ�ʱ�����Ȳ���
        weighty = 0.0; // �����ڷŴ�ʱ���߶Ȳ���
        anchor = GridBagConstraints.NORTH; // �����û�пռ��ʱ��ʹ������ڱ���
        fill = GridBagConstraints.BOTH; // ��������ʣ��ռ�ʱ�����ռ�
        insert = new Insets(0, 0, 0, 0); // ����˴˵ļ��
        ipadx = 0; // ����ڲ����ռ䣬�����������С�����Ӷ��Ŀռ�
        ipady = 0; // ����ڲ����ռ䣬�����������С�߶���Ӷ��Ŀռ�
        gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
        tempPane = new JPanel();
        gbl.setConstraints(tempPane, gbc);
        pane.add(tempPane);

        gridx = 2; // X2
        gridy = 1; // Y1
        gridwidth = 1; // ����Ϊ1
        gridheight = 1; // �߶�Ϊ1
        weightx = 1.0; // �����ڷŴ�ʱ��������֮�Ŵ�
        weighty = 0.0; // �����ڷŴ�ʱ���߶Ȳ���
        anchor = GridBagConstraints.SOUTH; // �����û�пռ��ʱ��ʹ������ڵײ�
        fill = GridBagConstraints.HORIZONTAL; // ����ʣ��ռ�ʱ���������ռ�
        insert = new Insets(0, 0, 0, 0); // ����˴˵ļ��
        ipadx = 0; // ����ڲ����ռ䣬�����������С�����Ӷ��Ŀռ�
        ipady = 0; // ����ڲ����ռ䣬�����������С�߶���Ӷ��Ŀռ�
        gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
        gbl.setConstraints(b_3, gbc);
        pane.add(b_3);

        // ����һ����ʱ��������
        gridx = 3; // X3
        gridy = 0; // Y0
        gridwidth = 1; // ��ռһ����Ԫ��
        gridheight = 2; // ��ռ������Ԫ��
        weightx = 0.0; // �����ڷŴ�ʱ�����Ȳ���
        weighty = 0.0; // �����ڷŴ�ʱ���߶Ȳ���
        anchor = GridBagConstraints.NORTH; // �����û�пռ��ʱ��ʹ������ڱ���
        fill = GridBagConstraints.BOTH; // ��������ʣ��ռ�ʱ�����ռ�
        insert = new Insets(0, 0, 0, 0); // ����˴˵ļ��
        ipadx = 0; // ����ڲ����ռ䣬�����������С�����Ӷ��Ŀռ�
        ipady = 0; // ����ڲ����ռ䣬�����������С�߶���Ӷ��Ŀռ�
        gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
        tempPane = new JPanel();
        gbl.setConstraints(tempPane, gbc);
        pane.add(tempPane);

        gridx = 3; // X3
        gridy = 2; // Y2
        gridwidth = 1; // ����Ϊ1
        gridheight = 2; // �߶�Ϊ2
        weightx = 0.0; // �����ڷŴ�ʱ������û�б仯
        weighty = 1.0; // �����ڷŴ�ʱ���߶���֮�Ŵ�
        anchor = GridBagConstraints.NORTH; // �����û�пռ��ʱ��ʹ������ڶ���
        fill = GridBagConstraints.VERTICAL; // ����ʣ��ռ�ʱ���������ռ�
        insert = new Insets(0, 0, 0, 0); // ����˴˵ļ��
        ipadx = 0; // ����ڲ����ռ䣬�����������С�����Ӷ��Ŀռ�
        ipady = 0; // ����ڲ����ռ䣬�����������С�߶���Ӷ��Ŀռ�
        gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
        gbl.setConstraints(b_4, gbc);
        pane.add(b_4);

        gridx = 0; // X0
        gridy = 3; // Y3
        gridwidth = 2; // ����Ϊ2
        gridheight = 1; // �߶�Ϊ1
        weightx = 1.0; // �����ڷŴ�ʱ��������֮�Ŵ�
        weighty = 0.0; // �����ڷŴ�ʱ���߶�û�б仯
        anchor = GridBagConstraints.SOUTH; // �����û�пռ��ʱ��ʹ������ڵײ�
        fill = GridBagConstraints.HORIZONTAL; // ����ʣ��ռ�ʱ���������ռ�
        insert = new Insets(0, 0, 0, 0); // ����˴˵ļ��
        ipadx = 0; // ����ڲ����ռ䣬�����������С�����Ӷ��Ŀռ�
        ipady = 0; // ����ڲ����ռ䣬�����������С�߶���Ӷ��Ŀռ�
        gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insert, ipadx, ipady);
        gbl.setConstraints(b_5, gbc);
        pane.add(b_5);

        this.getContentPane().add(pane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 180);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String args[]) {
        new UiTest();
    }

}