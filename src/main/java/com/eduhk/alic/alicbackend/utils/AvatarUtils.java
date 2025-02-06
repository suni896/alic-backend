package com.eduhk.alic.alicbackend.utils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author FuSu
 * @date 2025/2/5 21:17
 */
public class AvatarUtils {

    public static BufferedImage generateImg(String name) {
        int width = 100;
        int height = 100;
        int nameLen = name.length();
        String nameWritten;
        // 如果用户输入的姓名少于等于2个字符，不用截取
        if (nameLen <= 2) {
            nameWritten = name;
        } else {
            // 如果用户输入的姓名大于等于3个字符，截取后面两位
            String first = name.substring(0, 1);
            if (isChinese(first)) {
                // 截取倒数两位汉字
                nameWritten = name.substring(nameLen - 2);
            } else {
                // 截取前面的两个英文字母
                nameWritten = name.substring(0, 2).toUpperCase();
            }
        }

        //Font font = new Font("微软雅黑", Font.PLAIN, 30);

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setBackground(getRandomColor());

        g2.clearRect(0, 0, width, height);

        g2.setPaint(Color.WHITE);


        Font font = null;
        // 两个字及以上
        if (nameWritten.length() >= 2) {
            font = new Font("微软雅黑", Font.PLAIN, 30);
            g2.setFont(font);

            String firstWritten = nameWritten.substring(0, 1);
            String secondWritten = nameWritten.substring(1, 2);

            // 两个中文 如 张三
            if (isChinese(firstWritten) && isChinese(secondWritten)) {
                g2.drawString(nameWritten, 20, 60);
            }
            // 首中次英 如 张S
            else if (isChinese(firstWritten) && !isChinese(secondWritten)) {
                g2.drawString(nameWritten, 27, 60);

            }
            // 首英,如 ZS
            else {
                nameWritten = nameWritten.substring(0, 1);
            }

        }
        // 一个字
        if (nameWritten.length() == 1) {
            // 中文
            if (isChinese(nameWritten)) {
                font = new Font("微软雅黑", Font.PLAIN, 50);
                g2.setFont(font);
                g2.drawString(nameWritten, 25, 70);
            }
            // 英文
            else {
                font = new Font("微软雅黑", Font.PLAIN, 55);
                g2.setFont(font);
                g2.drawString(nameWritten.toUpperCase(), 33, 67);
            }

        }

        // 生成圆形头像
        BufferedImage rounded = makeCircular(bi);
        g2.dispose();
        return rounded;
    }


    /**
     * 判断字符串是否为中文
     *
     * @param str
     * @return
     */
    private static boolean isChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) return true;
        else return false;
    }

    /**
     * 获得随机颜色
     *
     * @return
     */
    private static Color getRandomColor() {
        String[] beautifulColors = new String[]{"2,168,250"};
        int len = beautifulColors.length;
        Random random = new Random();
        String[] color = beautifulColors[random.nextInt(len)].split(",");
        return new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
    }


    /**
     * 图片做圆角处理
     *
     * @param image
     * @param cornerRadius
     * @return
     */
    private static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return output;
    }

    private static BufferedImage makeCircular(BufferedImage image) {
        int diameter = Math.min(image.getWidth(), image.getHeight());
        BufferedImage output = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 创建一个圆形剪裁区域
        g2.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));

        // 在圆形区域内绘制原始图片
        g2.drawImage(image, 0, 0, diameter, diameter, null);

        g2.dispose();
        return output;
    }

}
