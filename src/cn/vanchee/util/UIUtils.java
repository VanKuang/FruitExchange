package cn.vanchee.util;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

/**
 * @author vanchee
 * @date 13-2-4
 * @package cn.vanchee.util
 * @verson v1.0.0
 */
public class UIUtils {

    private UIUtils() {
    }

    /**
     * ��������
     *
     * @param fontName
     * @param style
     * @param size
     */
    public static void initGlobalFontSetting(String fontName, int style, int size) {
        Font fnt = new Font(fontName, style, size);
        FontUIResource fontRes = new FontUIResource(fnt);
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

    public static void changeRightPanel(JPanel parentPanel, JPanel subPanel) {
        parentPanel.removeAll();
        parentPanel.add(subPanel);
        parentPanel.validate();
        parentPanel.repaint();
    }
}
