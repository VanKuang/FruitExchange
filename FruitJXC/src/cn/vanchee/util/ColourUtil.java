package cn.vanchee.util;

import java.awt.Color;
import jxl.format.Colour;

/**
 * @author vanchee
 * @date 13-4-21
 * @package cn.vanchee.util
 * @verson v1.0.0
 */
public class ColourUtil {

    public static Colour getNearestColour(int r, int g, int b) {
        Colour color = null;
        Colour[] colors = Colour.getAllColours();
        if ((colors != null) && (colors.length > 0)) {
            Colour crtColor = null;
            int[] rgb = null;
            int diff = 0;
            int minDiff = 999;
            for (int i = 0; i < colors.length; i++) {
                crtColor = colors[i];
                rgb = new int[3];
                rgb[0] = crtColor.getDefaultRGB().getRed();
                rgb[1] = crtColor.getDefaultRGB().getGreen();
                rgb[2] = crtColor.getDefaultRGB().getBlue();

                diff = Math.abs(rgb[0] - r)
                        + Math.abs(rgb[1] - g)
                        + Math.abs(rgb[2] - b);
                if (diff < minDiff) {
                    minDiff = diff;
                    color = crtColor;
                }
            }
        }
        if (color == null)
            color = Colour.BLACK;
        return color;
    }
}
