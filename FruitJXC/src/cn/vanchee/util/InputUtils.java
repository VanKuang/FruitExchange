package cn.vanchee.util;

import java.awt.event.KeyEvent;

public class InputUtils {

    private InputUtils() {}

    public static boolean checkNum(KeyEvent e) {
        int code = e.getKeyChar();
        if (code == KeyEvent.VK_PERIOD ||
                code == KeyEvent.VK_ENTER ||
                code == KeyEvent.VK_BACK_SPACE ||
                code == KeyEvent.VK_TAB ||
                code == KeyEvent.VK_0 ||
                code == KeyEvent.VK_1 ||
                code == KeyEvent.VK_2 ||
                code == KeyEvent.VK_3 ||
                code == KeyEvent.VK_4 ||
                code == KeyEvent.VK_5 ||
                code == KeyEvent.VK_6 ||
                code == KeyEvent.VK_7 ||
                code == KeyEvent.VK_8 ||
                code == KeyEvent.VK_9) {
            return true;
        }
        return false;
    }
}
