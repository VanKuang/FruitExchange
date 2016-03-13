package cn.vanchee.util;

/**
 * @author vanchee
 * @date 13-5-4
 * @package cn.vanchee.util
 * @verson v1.0.0
 */

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class DigitalTextField extends JTextField {
    private Toolkit toolkit;

    public DigitalTextField() {
        toolkit = Toolkit.getDefaultToolkit();
    }

    protected Document createDefaultModel() {
        return new DigitalDocument();
    }

    protected class DigitalDocument extends PlainDocument {
        public void insertString(int offs, String str, AttributeSet a)
                throws BadLocationException {

            char[] source = str.toCharArray();
            char[] result = new char[source.length];
            int j = 0;

            for (int i = 0; i < result.length; i++) {
                if (Character.isDigit(source[i]) || source[i] == '.') {
                    result[j++] = source[i];
                } else {
                    toolkit.beep();
                }
            }
            super.insertString(offs, new String(result, 0, j), a);
        }
    }

}