package util;

import javax.swing.*;
import java.awt.*;
import java.io.File;


public class ImageUtil {

    public static ImageIcon loadFoodImage(String relativePath) {
        try {
            File imageFile = new File(relativePath);

            System.out.println("尝试加载图片：" + imageFile.getAbsolutePath());

            if (imageFile.exists() && imageFile.isFile()) {

                ImageIcon originIcon = new ImageIcon(imageFile.getAbsolutePath());

                Image scaledImage = originIcon.getImage().getScaledInstance(
                        200, 150, Image.SCALE_SMOOTH
                );
                return new ImageIcon(scaledImage);
            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "图片不存在：\n" + imageFile.getAbsolutePath(),
                        "加载失败",
                        JOptionPane.ERROR_MESSAGE
                );
                return new ImageIcon();
            }
        } catch (Exception e) {

            e.printStackTrace();
            return new ImageIcon();
        }
    }
}